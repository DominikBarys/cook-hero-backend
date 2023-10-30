package com.barysdominik.notificationservice.scheduler;

import com.barysdominik.notificationservice.repository.dao.UserIngredientDao;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class NotificationScheduler {

    private final UserIngredientDao userIngredientDao;

    @Scheduled(cron = "0 0 6 * * ?")
    public void monitorUsersIngredients() {
        userIngredientDao.checkUsersIngredientsExpirationDates();
    }
}
