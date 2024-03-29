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

    @Value("${subject.create.course}")
    public String subjectCreateCourse;

    @Value("${mail.smtp.port}")
    public Integer mailSmtpPort;

    @Value("${mail.smtp.host}")
    public String mailSmtpHost;

    @Value("${mail.smtp.debug}")
    public String mailSmtpDebug;

    @Value("${mail.smtp.auth}")
    public String mailSmtpAuth;

    @Value("${mail.smtp.transport.protocol}")
    public String mailSmtpTransportProtocol;

    @Value("${mail.smtp.starttls.enable}")
    public String mailSmtpStarttlsEnable;

    @Value("${mail.smtp.ssl.protocols}")
    public String mailSmtpSslProtocols;

    @Value("${mail.smtp.ssl.trust}")
    public String mailSmtpSslTrust;
}
