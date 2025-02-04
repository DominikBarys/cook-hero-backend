package com.barysdominik.gateway.service;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CarouselService {
    private final EurekaClient eurekaClient;
    List<InstanceInfo> instances = new ArrayList<>();
    int index = 0;

    public CarouselService(EurekaClient eurekaClient) {
        this.eurekaClient = eurekaClient;
        try {
            initAuthCarousel();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        manageEvents();
    }

    public String getUriAuth() {
        StringBuilder stringBuilder = new StringBuilder();
        InstanceInfo instanceInfo = instances.get(index);
        stringBuilder.append(instanceInfo.getIPAddr()).append(":").append(instanceInfo.getPort());
        if(instances.size() -1 == index) {
            index = 0;
        } else {
            index++;
        }
        return stringBuilder.toString();
    }

    private void manageEvents() {
        eurekaClient.registerEventListener(event -> {
            initAuthCarousel();
            log.info("Registered new services successfully");
        });
        eurekaClient.unregisterEventListener(event -> {
            try {
                initAuthCarousel();
                log.info("Unregistered services successfully");
            } catch (NullPointerException e) {
                log.error("Error while unregistering services");
            }
        });
    }

    private void initAuthCarousel() throws NullPointerException {
        instances = eurekaClient.getApplication("AUTH-SERVICE").getInstances();
    }

}
