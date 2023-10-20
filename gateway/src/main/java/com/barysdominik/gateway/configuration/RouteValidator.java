package com.barysdominik.gateway.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.List;
import java.util.function.Predicate;

@Configuration
public class RouteValidator {

    public static final List<String> openApiEndpoints = List.of(
            "/auth/activate",
            "/auth/logout",
            "/auth/register",
            "/auth/login",
            "/auth/validate",
            "/auth/reset-password"
    );

    public Predicate<ServerHttpRequest> isSecure =
            serverHttpRequest -> openApiEndpoints.stream()
                    .noneMatch(uri -> serverHttpRequest.getURI()
                            .getPath()
                            .contains(uri));
}
