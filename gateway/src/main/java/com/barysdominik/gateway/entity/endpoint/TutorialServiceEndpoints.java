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
            new Endpoint("/api/v1/tutorial", HttpMethod.GET, Role.GUEST),
            new Endpoint("/api/v1/tutorial", HttpMethod.POST, Role.GUEST),
            new Endpoint("/api/v1/tutorial", HttpMethod.DELETE, Role.GUEST),
            new Endpoint("/api/v1/tutorial/change-thumbnail", HttpMethod.PATCH, Role.GUEST),
            new Endpoint("/api/v1/tutorial/change-images", HttpMethod.PATCH, Role.GUEST),
            new Endpoint("/api/v1/tutorial/change-name", HttpMethod.PATCH, Role.GUEST),
            new Endpoint("/api/v1/tutorial/change-category", HttpMethod.PATCH, Role.GUEST),
            new Endpoint("/api/v1/tutorial/change-dish", HttpMethod.PATCH, Role.GUEST),
            new Endpoint("/api/v1/tutorial/add-ingredients", HttpMethod.PATCH, Role.GUEST),
            new Endpoint("/api/v1/tutorial/change-parameters", HttpMethod.PATCH, Role.GUEST),
            new Endpoint("/api/v1/tutorial/change-time-to-prepare", HttpMethod.PATCH, Role.GUEST),
            new Endpoint("/api/v1/tutorial/change-difficulty", HttpMethod.PATCH, Role.GUEST),
            new Endpoint("/api/v1/tutorial/change-short-description", HttpMethod.PATCH, Role.GUEST),
            new Endpoint("/api/v1/tutorial/change-special-parameters", HttpMethod.PATCH, Role.GUEST),
            new Endpoint("/api/v1/category", HttpMethod.GET, Role.GUEST),
            new Endpoint("/api/v1/category/all", HttpMethod.GET, Role.GUEST),
            new Endpoint("/api/v1/category", HttpMethod.POST, Role.ADMIN),
            new Endpoint("/api/v1/category", HttpMethod.DELETE, Role.ADMIN),
            new Endpoint("/api/v1/dish/all", HttpMethod.GET, Role.GUEST),
            new Endpoint("/api/v1/dish", HttpMethod.GET, Role.GUEST),
            new Endpoint("/api/v1/dish", HttpMethod.POST, Role.ADMIN),
            new Endpoint("/api/v1/dish", HttpMethod.DELETE, Role.ADMIN),
            new Endpoint("/api/v1/ingredient/all", HttpMethod.GET, Role.GUEST),
            new Endpoint("/api/v1/ingredient", HttpMethod.GET, Role.GUEST),
            new Endpoint("/api/v1/ingredient", HttpMethod.POST, Role.ADMIN),
            new Endpoint("/api/v1/ingredient", HttpMethod.DELETE, Role.ADMIN),
            new Endpoint("/api/v1/page", HttpMethod.GET, Role.GUEST),
            new Endpoint("/api/v1/page/all", HttpMethod.GET, Role.GUEST),
            new Endpoint("/api/v1/page", HttpMethod.POST, Role.GUEST),
            new Endpoint("/api/v1/page", HttpMethod.PATCH, Role.GUEST),
            new Endpoint("/api/v1/page", HttpMethod.DELETE, Role.GUEST),
            new Endpoint("/api/v1/user-ingredient", HttpMethod.GET, Role.GUEST),
            new Endpoint("/api/v1/user-ingredient", HttpMethod.POST, Role.GUEST),
            new Endpoint("/api/v1/user-ingredient", HttpMethod.PATCH, Role.GUEST),
            new Endpoint("/api/v1/user-ingredient", HttpMethod.DELETE, Role.GUEST),
            new Endpoint("/api/v1/assistant", HttpMethod.GET, Role.GUEST)
    ));

    @PostConstruct
    public void registerEndpoints(){
        routeValidator.addEndpoints(endpointList);
    }


}
