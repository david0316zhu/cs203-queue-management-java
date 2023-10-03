package com.ticketmasterdemo.demo.service.impl;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.ticketmasterdemo.demo.dto.VerificationEmail;
import com.ticketmasterdemo.demo.service.EmailService;

public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @RabbitListener(queues = "emailQueue")
    public void listenForVerificationUrls(VerificationEmail verificationEmail) {
        sendVerificationEmail(verificationEmail.getVerificationUrl(), verificationEmail.getVerificationUrl());
    }

    public void sendVerificationEmail(String email, String verificationUrl) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Verification of Email Address");
        message.setText("Thank you for registering! Please click the following link to verify your email address: "
                + verificationUrl);

    }
}
