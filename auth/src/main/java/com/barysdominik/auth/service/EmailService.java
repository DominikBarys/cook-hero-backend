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
    private Resource mailContent;

    public void sendAccountActivationMail(User user) {
        try {
            String htmlMailContent = Files.toString(mailContent.getFile(), Charsets.UTF_8);
        } catch (IOException e) {
            //make unique exception for this
            throw new RuntimeException(e);
        }
    }

}
