package com.barysdominik.gateway.entity.endpoint;

import com.barysdominik.gateway.configuration.RouteValidator;
import com.barysdominik.gateway.entity.Endpoint;
import com.barysdominik.gateway.entity.HttpMethod;
import com.barysdominik.gateway.entity.Role;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthServiceEndpoints {
    private final RouteValidator routeValidator;

    private List<Endpoint> endpointList = new ArrayList<>(List.of(
            new Endpoint("/auth/get-user", HttpMethod.GET, Role.GUEST),//
            new Endpoint("/api/v1/auth/reset-password-no-email", HttpMethod.GET, Role.GUEST),//
            new Endpoint("/auth/logout", HttpMethod.GET, Role.GUEST),//
            new Endpoint("/auth/register", HttpMethod.POST, Role.GUEST),//
            new Endpoint("/auth/login", HttpMethod.POST, Role.GUEST),//
            new Endpoint("/auth/validate", HttpMethod.POST, Role.GUEST),
            new Endpoint("/auth/activate", HttpMethod.GET, Role.GUEST),
            new Endpoint("/auth/authorize", HttpMethod.GET, Role.GUEST),
            new Endpoint("/auth/reset-password", HttpMethod.PATCH, Role.GUEST),
            new Endpoint("/auth/reset-password", HttpMethod.POST, Role.GUEST),
            new Endpoint("/api/v1/auth/auto-login", HttpMethod.GET, Role.GUEST),
            new Endpoint("/api/v1/auth/change-username", HttpMethod.PATCH, Role.GUEST),
            new Endpoint("/api/v1/auth/change-role", HttpMethod.PATCH, Role.ADMIN),
            new Endpoint("/api/v1/auth/logged-in", HttpMethod.GET, Role.GUEST),
            new Endpoint("/auth/delete", HttpMethod.DELETE, Role.ADMIN)
    ));

    @PostConstruct
    public void registerEndpoints(){
        routeValidator.addEndpoints(endpointList);
    }

}
