package com.tmp.send.email.microservice.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class JavaMailConfiguration {

    private final EmailConfiguration emailConfiguration;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setPort(emailConfiguration.mailSmtpPort);
        mailSender.setHost(emailConfiguration.mailSmtpHost);
        mailSender.setUsername(emailConfiguration.fromEmail);
        mailSender.setPassword(emailConfiguration.fromPassword);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.debug", emailConfiguration.mailSmtpDebug);

        props.put("mail.smtp.auth", emailConfiguration.mailSmtpAuth);
        props.put("mail.transport.protocol", emailConfiguration.mailSmtpTransportProtocol);
        props.put("mail.smtp.starttls.enable", emailConfiguration.mailSmtpStarttlsEnable);
        props.put("mail.smtp.ssl.protocols", emailConfiguration.mailSmtpSslProtocols);
        props.put("mail.smtp.ssl.trust", emailConfiguration.mailSmtpSslTrust);

        mailSender.setJavaMailProperties(props);

        return mailSender;
    }
}
