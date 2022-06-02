package com.tmp.send.email.microservice.services;

import com.tmp.send.email.microservice.configuration.EmailConfiguration;
import com.tmp.send.email.microservice.exceptions.SendEmailUnknownException;
import com.tmp.send.email.microservice.models.EmailAssignedCourseEmployeeDTO;
import com.tmp.send.email.microservice.models.EmailAssignedCourseManagerDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Log4j2
@Service
@RequiredArgsConstructor
public class EmailService {

    private final EmailConfiguration emailConfiguration;
    private final SpringTemplateEngine springTemplateEngine;

    /*
        EmailService methods
    */
    private Properties setMailProperties() {
        Properties properties = System.getProperties();

        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
        properties.put("mail.smtp.starttls.required", "true");

        return properties;
    }

    private void createNewEmailAndSendIt(Session session, String emailTo, String subject, String html) {
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailConfiguration.fromEmail));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));
            message.setSubject(subject);
            message.setContent(html, "text/html; charset=utf-8");

            Transport.send(message);
        } catch (MessagingException messagingException) {
            log.error("Problem trying to send email - {}", messagingException.getMessage());
            throw new SendEmailUnknownException();
        }
    }

    /*
        Methods from EmailController
    */
    public void sendEmailAssignedCourseManagerReq(EmailAssignedCourseManagerDTO emailAssignedCourseManagerDTO,
            String template) {
        // Set the mail properties
        Properties properties = setMailProperties();

        // Create a new SMTP session with previous properties and with
        // email and password authentication from Gmail account
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailConfiguration.fromEmail, emailConfiguration.fromPassword);
            }
        });

        session.setDebug(true);

        // Create a Map with all variables and their values from template .html file
        Map<String, Object> variables = new HashMap<>();
        variables.put("managerUsername", emailAssignedCourseManagerDTO.getManagerUsername());
        variables.put("employeeUsername", emailAssignedCourseManagerDTO.getEmployeeUsername());
        variables.put("employeeEmail", emailAssignedCourseManagerDTO.getEmployeeEmail());
        variables.put("courseName", emailAssignedCourseManagerDTO.getCourseName());
        variables.put("courseId", emailAssignedCourseManagerDTO.getCourseId());
        variables.put("courseCategory", emailAssignedCourseManagerDTO.getCourseCategory());
        variables.put("timeToMakeCourse", emailAssignedCourseManagerDTO.getTimeToMakeCourse());

        Context context = new Context();
        context.setVariables(variables);

        String html = springTemplateEngine.process(template, context);

        final String emailTo = emailAssignedCourseManagerDTO.getManagerEmail();
        createNewEmailAndSendIt(session, emailTo, emailConfiguration.subjectAssignedCourse, html);
    }

    public void sendEmailAssignedCourseEmployeeReq(EmailAssignedCourseEmployeeDTO emailAssignedCourseEmployeeDTO,
            String template) {
        // Set the mail properties
        Properties properties = setMailProperties();

        // Create a new SMTP session with previous properties and with
        // email and password authentication from Gmail account
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailConfiguration.fromEmail, emailConfiguration.fromPassword);
            }
        });

        session.setDebug(true);

        // Create a Map with all variables and their values from template .html file
        Map<String, Object> variables = new HashMap<>();
        variables.put("employeeEmail", emailAssignedCourseEmployeeDTO.getEmployeeEmail());
        variables.put("employeeUsername", emailAssignedCourseEmployeeDTO.getEmployeeUsername());
        variables.put("courseName", emailAssignedCourseEmployeeDTO.getCourseName());
        variables.put("courseId", emailAssignedCourseEmployeeDTO.getCourseId());
        variables.put("courseCategory", emailAssignedCourseEmployeeDTO.getCourseCategory());
        variables.put("timeToMakeCourse", emailAssignedCourseEmployeeDTO.getTimeToMakeCourse());

        Context context = new Context();
        context.setVariables(variables);

        String html = springTemplateEngine.process(template, context);

        final String emailTo = emailAssignedCourseEmployeeDTO.getEmployeeEmail();
        createNewEmailAndSendIt(session, emailTo, emailConfiguration.subjectAssignedCourse, html);
    }
}
