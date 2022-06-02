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
}
