package com.coggiri.main.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {
    @Value("${mail.password}")
    private String password;

    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Bean
    public JavaMailSender javaMailSender(){
        log.info("===========================================");
        log.info("mail Configuration is initialized");
        log.info("Jasypt Password: {}", password);
        log.info("===========================================");
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("woodevelop3@gmail.com");
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol","smtp");
        props.put("mail.smtp.auth",true);
        props.put("mail.smtp.starttls.enable","true");
        props.put("mail.debug","true");

        return mailSender;
    }
}