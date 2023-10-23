package com.barysdominik.auth.service;

import com.barysdominik.auth.configuration.EmailConfiguration;
import com.barysdominik.auth.entity.user.User;
import com.barysdominik.auth.exception.SendingMailException;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final EmailConfiguration emailConfiguration;
    @Value("${front-end.url}")
    private String frontendUrl;
    @Value("classpath:static/activate-account-mail.html")
    private Resource activateAccountMailContent;
    @Value("classpath:static/reset-password-mail.html")
    private Resource resetPasswordMailContent;

    public void sendAccountActivationMail(User user) {
        try {
            String htmlMailContent = Files.toString(activateAccountMailContent.getFile(), Charsets.UTF_8);
            htmlMailContent = htmlMailContent.replace(
                    "FRONTEND_URL",
                    frontendUrl + "/aktywuj/" + user.getUuid()
            );
            emailConfiguration.sendMail(user.getEmail(), htmlMailContent, "Aktywuj konto", true);
            log.info("Account activation mail was successfully sent to email address: '" + user.getEmail() + "'");
        } catch (IOException e) {
            log.error("Error while sending account activation mail to email address: '" + user.getEmail() + "'");
            throw new SendingMailException("Error while sending account activation mail");
        }
    }

    public void sendPasswordRecoveryMail(User user, String uuid) {
        try{
            String htmlMailContent = Files.toString(resetPasswordMailContent.getFile(), Charsets.UTF_8);
            htmlMailContent = htmlMailContent.replace(
                    "FRONTEND_URL",
                    frontendUrl + "/odzyskaj-haslo/" + uuid
            );
            emailConfiguration.sendMail(user.getEmail(), htmlMailContent,"Odzyskaj hasło",true);
            log.info("Reset password mail was successfully sent to email address: '" + user.getEmail() + "'");
        }catch (IOException e){
            log.error("Error while sending reset password mail to email address: '" + user.getEmail() + "'");
            throw new SendingMailException("Error while sending reset password mail");
        }
    }
}
