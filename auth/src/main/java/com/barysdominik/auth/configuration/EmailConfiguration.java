package com.barysdominik.auth.configuration;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class EmailConfiguration {

    private final String email;
    private final String password;
    private final String smtpHost;
    private final int smtpPort;
    private Authenticator authenticator;
    private Session session;
    private Properties properties;

    public EmailConfiguration(
            @Value("${smtp.mail-address}") String email,
            @Value("${smtp.password}") String password,
            @Value("${smtp.host}") String smtpHost,
            @Value("${smtp.port}") int smtpPort
    ) {
        this.email = email;
        this.password = password;
        this.smtpHost = smtpHost;
        this.smtpPort = smtpPort;
        initConfiguration(smtpHost, smtpPort);
    }

    private void initConfiguration(String host, int port) {
        properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.port", smtpPort);

        this.authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
            }
        };
    }

    private void refreshSession() {
        session = Session.getInstance(properties, authenticator);
    }

    public void sendMail(String receiverMail, String content, String subject, boolean firstAttempt) {
        if(session == null) {
            refreshSession();
        }
        try{
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiverMail));
            message.setSubject(subject);
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(content, "text/html; charset=utf-8");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);
            message.setContent(multipart);
            Transport.send(message);
        } catch (MessagingException e) {
            if(firstAttempt) {
                refreshSession();
                sendMail(receiverMail, content, subject, false);
            }
        }
    }
}
