package com.example.ubernet.service;

import com.example.ubernet.model.User;
import com.example.ubernet.utils.EmailContentUtils;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Objects;

@AllArgsConstructor
@Service
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final Environment env;

    private String siteURL = "http://localhost:4200";

    @Async
    public void sendRegistrationAsync(User user) throws MailException, MessagingException {
        System.out.println("Sending email...");
        MimeMessage message = javaMailSender.createMimeMessage();
        message.setSubject("Please verify your registration");
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo("ubernet-test@outlook.com");
        helper.setFrom(Objects.requireNonNull(env.getProperty("spring.mail.username")));
        String content = EmailContentUtils.getVerificationContent();
        String verifyURL = siteURL + "/verify/" + user.getUserAuth().getVerificationCode();
        content = content.replace("[[URL]]", verifyURL);
        helper.setText(content, true);
        javaMailSender.send(message);
        System.out.println("Email sent!");
    }

    @Async
    public void sendEmailResetAsync(User user) throws MailException, MessagingException {
        System.out.println("Sending email...");
        MimeMessage message = javaMailSender.createMimeMessage();
        message.setSubject("Forgot password");
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo("ubernet-test@outlook.com");
        helper.setFrom(Objects.requireNonNull(env.getProperty("spring.mail.username")));
        String content = EmailContentUtils.getVerificationContent();
        String verifyURL = siteURL + "/verify/" + user.getUserAuth().getVerificationCode();
        content = content.replace("[[URL]]", verifyURL);
        helper.setText(content, true);
        javaMailSender.send(message);
        System.out.println("Email sent!");
    }
}
