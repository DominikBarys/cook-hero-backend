package com.barysdominik.gateway.filter;

import com.barysdominik.gateway.configuration.RouteValidator;
import com.barysdominik.gateway.service.CarouselService;
import com.barysdominik.gateway.service.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.http.*;

import java.sql.Timestamp;
import java.util.List;

public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final RouteValidator routeValidator;
    private final RestTemplate restTemplate;
    private final JwtService jwtService;
    private CarouselService carouselService;
    @Value("${spring.profiles.active}")
    private String profile;

    public AuthenticationFilter(
            JwtService jwtService,
            RestTemplate restTemplate,
            RouteValidator routeValidator,
            CarouselService carouselService
    ) {
        super(Config.class);
        this.carouselService = carouselService;
        this.jwtService = jwtService;
        this.restTemplate = restTemplate;
        this.routeValidator = routeValidator;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (routeValidator.isSecure.test(exchange.getRequest())) {
                if (!exchange.getRequest().getCookies().containsKey("token") &&
                        !exchange.getRequest().getCookies().containsKey("refresh")) {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

                    StringBuilder stringBuilder = new StringBuilder("{\n")
                            .append("\"timestamp\": \"")
                            .append(new Timestamp(System.currentTimeMillis()))
                            .append("\",\n")
                            .append("\"message\": \"Wskazany token jest pusty lub nie ważny\",\n")
                            .append("\"code\": \"INVALID_TOKEN\"\n")
                            .append("}");

                    return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                            .bufferFactory()
                            .wrap((stringBuilder.toString()).getBytes())));
                }

                HttpCookie tokenCookie = exchange.getRequest().getCookies().get("token").get(0);
                HttpCookie refreshCookie = exchange.getRequest().getCookies().get("refresh").get(0);

                try {
                    if (profile.equals("test")){
                        //for testing purposes
                        jwtService.validateToken(tokenCookie.getValue());
                    }else {
                        String cookies = new StringBuilder()
                                .append(tokenCookie.getName())
                                .append("=")
                                .append(tokenCookie.getValue())
                                .append(";")
                                .append(refreshCookie.getName())
                                .append("=")
                                .append(refreshCookie.getValue()).toString();

                        HttpHeaders httpHeaders = new HttpHeaders();
                        httpHeaders.add("Cookie",cookies);
                        HttpEntity<Object> entity = new HttpEntity<>(httpHeaders);
                        ResponseEntity<String> response = restTemplate.exchange(
                                "http://"+carouselService.getUriAuth()+"/api/v1/auth/validate",
                                HttpMethod.GET,entity,
                                String.class
                        );

                        //przepisywanie cookiesa
                        if(response.getStatusCode() == HttpStatus.OK) {
                            List<String> cookiesList = response.getHeaders().get(HttpHeaders.SET_COOKIE);
                            if(cookiesList != null) {
                                List<java.net.HttpCookie> httpCookie = java.net.HttpCookie.parse(cookiesList.get(0));
                                for (java.net.HttpCookie cookie: httpCookie){
                                    exchange.getResponse().getCookies().add(cookie.getName(),
                                            //tworzenie response cookiesa z jakiegos juz istniejacego
                                            ResponseCookie.from(cookie.getName(),cookie.getValue())
                                                    .domain(cookie.getDomain())
                                                    .path(cookie.getPath())
                                                    .maxAge(cookie.getMaxAge())
                                                    .secure(cookie.getSecure())
                                                    .httpOnly(cookie.isHttpOnly())
                                                    .build());
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    exchange.getResponse().writeWith(
                            Flux.just(new DefaultDataBufferFactory().wrap(e.getMessage().getBytes()))
                    );
                }
            }
            //tutaj trafiamy od razu jezeli endpoint nie znajduje sie na liscie wymagajacej autoryzacji
            return chain.filter(exchange);
        });
    }

    public static class Config {
    }
}
