package com.barysdominik.auth.service;

import com.barysdominik.auth.entity.user.User;
import com.barysdominik.auth.entity.user.UserResetPassword;
import com.barysdominik.auth.repository.UserResetPasswordRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@EnableScheduling
@RequiredArgsConstructor
public class UserResetPasswordService {

    private final UserResetPasswordRepository userResetPasswordRepository;

    @Transactional
    public UserResetPassword startResettingPassword(User user) {
        UserResetPassword userResetPassword = new UserResetPassword();

        userResetPassword.setUuid(UUID.randomUUID().toString());
        userResetPassword.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        userResetPassword.setUser(user);

        userResetPasswordRepository.deleteAllByUser(user);
        return userResetPasswordRepository.saveAndFlush(userResetPassword);
    }

    public void endResettingPassword(String uuid) {
        userResetPasswordRepository.findByUuid(uuid).ifPresent(userResetPasswordRepository::delete);
    }

   // @Scheduled(cron = "0 0/40 * * * *")
    protected void removeExpiredUserResetsOfPassword() {
        log.info("Removing expired user reset password data from database");
        List<UserResetPassword> userResetPasswords = userResetPasswordRepository.findExpiredOperations();
        if(userResetPasswords != null && !userResetPasswords.isEmpty()) {
            userResetPasswordRepository.deleteAll(userResetPasswords);
        }
    }

}
