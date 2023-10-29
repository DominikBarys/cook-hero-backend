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
public class NotificationServiceEndpoints {

    private final RouteValidator routeValidator;

    private List<Endpoint> endpointList = new ArrayList<>(List.of(
            new Endpoint("/api/v1/notification/all", HttpMethod.GET, Role.GUEST),
            new Endpoint("/api/v1/notification", HttpMethod.GET, Role.GUEST),
            new Endpoint("/api/v1/notification/all", HttpMethod.DELETE, Role.GUEST),
            new Endpoint("/api/v1/notification", HttpMethod.DELETE, Role.GUEST),
            new Endpoint("/api/v1/notification", HttpMethod.PATCH, Role.GUEST)
    ));

    @PostConstruct
    public void registerEndpoints(){
        routeValidator.addEndpoints(endpointList);
    }

}
