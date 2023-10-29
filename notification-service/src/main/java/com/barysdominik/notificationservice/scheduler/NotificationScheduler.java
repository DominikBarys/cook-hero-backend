package com.barysdominik.notificationservice.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class NotificationScheduler {


    @Scheduled(cron = "0 * * * * ?")
    public void minuteTask() {
        System.out.println("Co pełną minutę");
    }



}
