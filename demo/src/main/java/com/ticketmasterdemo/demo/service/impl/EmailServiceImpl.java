package com.ticketmasterdemo.demo.service.impl;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.ticketmasterdemo.demo.configure.RabbitMqConfigure;
import com.ticketmasterdemo.demo.dto.VerificationEmail;
import com.ticketmasterdemo.demo.service.EmailService;

@Component
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @RabbitListener(queues = RabbitMqConfigure.QUEUE_NAME)
    public void listenForVerificationUrls(VerificationEmail verificationEmail) {
        System.out.println("I HAVE RECEIVED IT");
        sendVerificationEmail(verificationEmail.getEmail(), verificationEmail.getVerificationUrl());
    }

    public void sendVerificationEmail(String email, String verificationUrl) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Verification of Email Address");
        message.setText("Thank you for registering! Please click the following link to verify your email address: "
                + verificationUrl);
        System.out.println(message.getText());
        emailSender.send(message);
    }
}
