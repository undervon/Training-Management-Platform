package com.tmp.send.email.microservice.services;

import com.tmp.send.email.microservice.configuration.EmailConfiguration;
import com.tmp.send.email.microservice.exceptions.SendEmailUnknownException;
import com.tmp.send.email.microservice.models.EmailAssignedCourseEmployeeDTO;
import com.tmp.send.email.microservice.models.EmailAssignedCourseManagerDTO;
import com.tmp.send.email.microservice.models.EmailCourseCompletedEmployeeDTO;
import com.tmp.send.email.microservice.models.EmailCourseCompletedManagerDTO;
import com.tmp.send.email.microservice.models.EmailCreateCourseManagerDTO;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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

        properties.put("mail.smtp.port", emailConfiguration.mailSmtpPort);
        properties.put("mail.smtp.host", emailConfiguration.mailSmtpHost);
        properties.put("mail.smtp.auth", emailConfiguration.mailSmtpAuth);
        properties.put("mail.smtp.ssl.enable", emailConfiguration.mailSmtpSslEnable);
        properties.put("mail.smtp.ssl.protocols", emailConfiguration.mailSmtpSslProtocols);
        properties.put("mail.smtp.starttls.enable", emailConfiguration.mailSmtpStarttlsEnable);
        properties.put("mail.smtp.starttls.required", emailConfiguration.mailSmtpStarttlsRequired);

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

    private String generatingDifferenceBetweenTwoDate(LocalDateTime startDate, LocalDateTime endDate) {
        LocalDateTime newStartDate = startDate;

        long years = newStartDate.until(endDate, ChronoUnit.YEARS);
        newStartDate = newStartDate.plusYears(years);

        long months = newStartDate.until(endDate, ChronoUnit.MONTHS);
        newStartDate = newStartDate.plusMonths(months);

        long days = newStartDate.until(endDate, ChronoUnit.DAYS);
        newStartDate = newStartDate.plusDays(days);

        long hours = newStartDate.until(endDate, ChronoUnit.HOURS);
        newStartDate = newStartDate.plusHours(hours);

        long minutes = newStartDate.until(endDate, ChronoUnit.MINUTES);
        newStartDate = newStartDate.plusMinutes(minutes);

        long seconds = newStartDate.until(endDate, ChronoUnit.SECONDS);

        String dateIntervalInitial = "";

        if (years != 0) {
            if (years == 1) {
                dateIntervalInitial = dateIntervalInitial + years + " an ";
            } else {
                dateIntervalInitial = dateIntervalInitial + years + " ani ";
            }
        }

        if (months != 0) {
            if (months == 1) {
                dateIntervalInitial = dateIntervalInitial + months + " luna ";
            } else {
                dateIntervalInitial = dateIntervalInitial + months + " luni ";
            }
        }

        if (days != 0) {
            if (days == 1) {
                dateIntervalInitial = dateIntervalInitial + days + " zi ";
            } else {
                dateIntervalInitial = dateIntervalInitial + days + " zile ";
            }
        }

        if (hours != 0) {
            dateIntervalInitial = dateIntervalInitial + hours + "h ";
        }

        if (minutes != 0) {
            dateIntervalInitial = dateIntervalInitial + minutes + "m ";
        }

        if (seconds != 0) {
            dateIntervalInitial = dateIntervalInitial + seconds + "s ";
        }

        StringBuilder dateIntervalBuilder = new StringBuilder(dateIntervalInitial);
        String dateIntervalFinal = "";

        if (dateIntervalInitial.endsWith(" ")) {
            dateIntervalFinal = dateIntervalBuilder.deleteCharAt(dateIntervalInitial.length() - 1).toString();
        }

        return dateIntervalFinal;
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

    public void sendEmailCourseCompletedManagerReq(EmailCourseCompletedManagerDTO emailCourseCompletedManagerDTO,
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

        LocalDateTime startDate = emailCourseCompletedManagerDTO.getCourseStartDate();
        LocalDateTime endDate = emailCourseCompletedManagerDTO.getCourseCompletionDate();

        String courseCompletionInterval = generatingDifferenceBetweenTwoDate(startDate, endDate);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy - HH:mm:ss");
        String formattedStartDate = formatter.format(startDate);
        String formattedEndDate = formatter.format(endDate);

        // Create a Map with all variables and their values from template .html file
        Map<String, Object> variables = new HashMap<>();
        variables.put("managerUsername", emailCourseCompletedManagerDTO.getManagerUsername());
        variables.put("employeeUsername", emailCourseCompletedManagerDTO.getEmployeeUsername());
        variables.put("employeeEmail", emailCourseCompletedManagerDTO.getEmployeeEmail());
        variables.put("courseName", emailCourseCompletedManagerDTO.getCourseName());
        variables.put("courseId", emailCourseCompletedManagerDTO.getCourseId());
        variables.put("courseCategory", emailCourseCompletedManagerDTO.getCourseCategory());
        variables.put("courseStartDate", formattedStartDate);
        variables.put("courseCompletionDate", formattedEndDate);
        variables.put("courseCompletionInterval", courseCompletionInterval);

        Context context = new Context();
        context.setVariables(variables);

        String html = springTemplateEngine.process(template, context);

        final String emailTo = emailCourseCompletedManagerDTO.getManagerEmail();
        createNewEmailAndSendIt(session, emailTo, emailConfiguration.subjectCourseCompleted, html);
    }

    public void sendEmailCourseCompletedEmployeeReq(EmailCourseCompletedEmployeeDTO emailCourseCompletedEmployeeDTO,
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

        LocalDateTime startDate = emailCourseCompletedEmployeeDTO.getCourseStartDate();
        LocalDateTime endDate = emailCourseCompletedEmployeeDTO.getCourseCompletionDate();

        String courseCompletionInterval = generatingDifferenceBetweenTwoDate(startDate, endDate);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy - HH:mm:ss");
        String formattedStartDate = formatter.format(startDate);
        String formattedEndDate = formatter.format(endDate);

        // Create a Map with all variables and their values from template .html file
        Map<String, Object> variables = new HashMap<>();
        variables.put("employeeEmail", emailCourseCompletedEmployeeDTO.getEmployeeEmail());
        variables.put("employeeUsername", emailCourseCompletedEmployeeDTO.getEmployeeUsername());
        variables.put("courseName", emailCourseCompletedEmployeeDTO.getCourseName());
        variables.put("courseId", emailCourseCompletedEmployeeDTO.getCourseId());
        variables.put("courseCategory", emailCourseCompletedEmployeeDTO.getCourseCategory());
        variables.put("courseStartDate", formattedStartDate);
        variables.put("courseCompletionDate", formattedEndDate);
        variables.put("courseCompletionInterval", courseCompletionInterval);

        Context context = new Context();
        context.setVariables(variables);

        String html = springTemplateEngine.process(template, context);

        final String emailTo = emailCourseCompletedEmployeeDTO.getEmployeeEmail();
        createNewEmailAndSendIt(session, emailTo, emailConfiguration.subjectCourseCompleted, html);
    }

    public void sendEmailCreateCourseManagerReq(EmailCreateCourseManagerDTO emailCreateCourseManagerDTO,
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
        variables.put("managerUsername", emailCreateCourseManagerDTO.getManagerUsername());
        variables.put("employeeUsername", emailCreateCourseManagerDTO.getEmployeeUsername());
        variables.put("employeeEmail", emailCreateCourseManagerDTO.getEmployeeEmail());
        variables.put("courseName", emailCreateCourseManagerDTO.getCourseName());
        variables.put("courseId", emailCreateCourseManagerDTO.getCourseId());
        variables.put("courseCategory", emailCreateCourseManagerDTO.getCourseCategory());

        Context context = new Context();
        context.setVariables(variables);

        String html = springTemplateEngine.process(template, context);

        final String emailTo = emailCreateCourseManagerDTO.getManagerEmail();
        createNewEmailAndSendIt(session, emailTo, emailConfiguration.subjectCreateCourse, html);
    }
}
