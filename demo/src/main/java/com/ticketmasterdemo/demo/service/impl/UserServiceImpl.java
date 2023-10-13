package com.ticketmasterdemo.demo.service.impl;

import java.security.DrbgParameters.Reseed;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.ticketmasterdemo.demo.common.exception.InvalidArgsException;
import com.ticketmasterdemo.demo.common.exception.UserException;
import com.ticketmasterdemo.demo.dto.User;
import com.ticketmasterdemo.demo.dto.VerificationEmail;
import com.ticketmasterdemo.demo.repository.UserRepository;
import com.ticketmasterdemo.demo.service.UserService;
import com.ticketmasterdemo.demo.util.Utility;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RabbitTemplate rabbitTemplate) {
        this.userRepository = userRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Bean
    public Queue queue() {
        return new Queue("emailQueue");
    }

    @Override
    public void sendVerificationTokenToEmailService(String email, String token) {
        String verificationUrl = "https://yourwebsite.com/verify?token=" + token;
        VerificationEmail verificationEmail = new VerificationEmail();
        verificationEmail.setEmail(email);
        verificationEmail.setVerificationUrl(verificationUrl);

        rabbitTemplate.convertAndSend("emailQueue", verificationEmail);
    }

    @Override
    public User getUser(String email, String mobile) {
        Utility utility = new Utility();
        if (!utility.emailWhitelist(email) || !utility.isInputSafe(mobile)) {
            throw new InvalidArgsException("Invalid email and/or mobile");
        }

        User user = userRepository.findUserByEmailAndMobile(email, mobile);
        if (user == null)
            throw new UserException("user does not exist");
        return user;
    }

    @Override
    public User getUser(String email) {
        Utility utility = new Utility();
        if (!utility.emailWhitelist(email)) {
            throw new InvalidArgsException("Invalid email");
        }
        User user = userRepository.findAnyUserByEmail(email);
        if (user == null)
            throw new UserException("User does not exist");
        return user;
    }

    @Override
    public User createUser(String email, String mobile, String password) {
        Utility utility = new Utility();
        if (!utility.emailWhitelist(email) || !utility.isInputSafe(mobile)) {
            throw new InvalidArgsException("Invalid email and/or mobile");
        }
        String userId = utility.generateUserId();
        String authenticatorId = utility.generateRandomUUID();
        userRepository.createUser(email, mobile, password, userId, authenticatorId, false);
        User user = getUser(email);
        // create verification token
        String token = utility.generateEmailVerificationToken();
        sendVerificationTokenToEmailService(email, token);
        return user;

    }

    @Override
    public boolean isUserVerified(String email, String mobile) {
        Utility utility = new Utility();
        if (!utility.emailWhitelist(email) || !utility.isInputSafe(mobile)) {
            throw new InvalidArgsException("Invalid email and/or mobile");
        }
        try {
            User user = getUser(email, mobile);
            return (user != null) && user.isVerified();
        } catch (UserException e) {
            return false;
        }
    }

    @Override
    public boolean isUserEmailVerified(String email) {
        Utility utility = new Utility();
        if (!utility.emailWhitelist(email)) {
            throw new InvalidArgsException("Invalid email");
        }
        User user = userRepository.findVerifiedUserByEmail(email);
        return (user != null) && user.isVerified();
    }

    @Override
    public boolean authenticateUser(String email, String mobile, String password) {
        Utility utility = new Utility();
        if (!utility.emailWhitelist(email) || !utility.isInputSafe(mobile) || password == null) {
            throw new InvalidArgsException("invalid login credentials - failed preliminary check");
        }

        String userPwd = userRepository.retrieveUserForAuth(email, mobile);
        return password.equals(userPwd);
    }

    @Override
    public boolean verifyEmailToken(String token) {
        LocalDateTime currDateTime = LocalDateTime.now();
        String userId = userRepository.findEmailVerificationToken(token, currDateTime);
        if (userId == null) {
            throw new UserException("Verification Token Invalid/Expired");
        }
        userRepository.updateEmailVerification(userId);
        return true;
    }
}
