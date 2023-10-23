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
public class TutorialServiceEndpoints {

    private final RouteValidator routeValidator;

    private final List<Endpoint> endpointList = new ArrayList<>(List.of(
            new Endpoint("/api/v1/product", HttpMethod.GET, Role.GUEST),
            new Endpoint("/api/v1/product", HttpMethod.POST, Role.ADMIN),
            new Endpoint("/api/v1/product", HttpMethod.DELETE, Role.ADMIN),
            new Endpoint("/api/v1/category", HttpMethod.GET, Role.GUEST),
            new Endpoint("/api/v1/category", HttpMethod.POST, Role.ADMIN)
    ));


    @PostConstruct
    public void registerEndpoints(){
        routeValidator.addEndpoints(endpointList);
    }


}
