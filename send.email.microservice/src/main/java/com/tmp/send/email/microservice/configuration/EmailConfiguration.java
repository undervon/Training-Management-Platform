package com.tmp.send.email.microservice.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:email-configuration.properties")
public class EmailConfiguration {

    @Value("${from.email}")
    public String fromEmail;

    @Value("${from.password}")
    public String fromPassword;

    @Value("${subject.assigned.course}")
    public String subjectAssignedCourse;

    @Value("${subject.course.completed}")
    public String subjectCourseCompleted;

    @Value("${mail.smtp.port}")
    public String mailSmtpPort;

    @Value("${mail.smtp.auth}")
    public String mailSmtpAuth;

    @Value("${mail.smtp.ssl.enable}")
    public String mailSmtpSslEnable;

    @Value("${mail.smtp.host}")
    public String mailSmtpHost;

    @Value("${mail.smtp.starttls.enable}")
    public String mailSmtpStarttlsEnable;

    @Value("${mail.smtp.ssl.protocols}")
    public String mailSmtpSslProtocols;

    @Value("${mail.smtp.starttls.required}")
    public String mailSmtpStarttlsRequired;
}
