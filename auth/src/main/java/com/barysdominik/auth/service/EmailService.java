package com.barysdominik.auth.service;

import com.barysdominik.auth.configuration.EmailConfiguration;
import com.barysdominik.auth.entity.user.User;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.core.io.Resource;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final EmailConfiguration emailConfiguration;
    @Value("${front-end.url}")
    private String frontendUrl;
    //make custom mail message
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
        } catch (IOException e) {
            //TODO make unique exception for this
            throw new RuntimeException(e);
        }
    }

    public void sendPasswordRecoveryMail(User user) {
        try{
            String htmlMailContent = Files.toString(resetPasswordMailContent.getFile(), Charsets.UTF_8);
            htmlMailContent = htmlMailContent.replace(
                    "FRONTEND_URL",
                    frontendUrl + "/odzyskaj-haslo/" + user.getUuid()
            );
            emailConfiguration.sendMail(user.getEmail(), htmlMailContent,"Odzyskaj hasło",true);
        }catch (IOException e){
            //TODO make unique exception for this
            throw new RuntimeException(e);
        }

    }

}
