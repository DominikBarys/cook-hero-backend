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
public class FileServiceEndpoints {
    private final RouteValidator routeValidator;


    private final List<Endpoint> endpointList = new ArrayList<>(List.of(
            new Endpoint("/api/v1/image", HttpMethod.GET, Role.GUEST),
            new Endpoint("/api/v1/image", HttpMethod.POST, Role.GUEST),
            new Endpoint("/api/v1/image", HttpMethod.PATCH, Role.GUEST),
            new Endpoint("/api/v1/image", HttpMethod.DELETE, Role.ADMIN)
    ));


    @PostConstruct
    public void registerEndpoints(){
        routeValidator.addEndpoints(endpointList);
    }


}
