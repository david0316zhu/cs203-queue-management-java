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
import com.ticketmasterdemo.demo.configure.RabbitMqConfigure;
import com.ticketmasterdemo.demo.dto.User;
import com.ticketmasterdemo.demo.dto.UserAuth;
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


    @Override
    public void sendVerificationTokenToEmailService(String email, String token) {
        String verificationUrl = "http://localhost:4200/user/verify/token/" + token;
        VerificationEmail verificationEmail = new VerificationEmail();
        verificationEmail.setEmail(email);
        verificationEmail.setVerificationUrl(verificationUrl);

        rabbitTemplate.convertAndSend(RabbitMqConfigure.QUEUE_NAME, verificationEmail);
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
        String hashedPassword = utility.hashPassword(password);
        userRepository.createUser(email, mobile, hashedPassword, userId, authenticatorId, false);
        userRepository.insertUserVerifier(userId, true);
        User user = getUser(email);
        // create verification token
        String token = utility.generateEmailVerificationToken();
        userRepository.saveEmailToken(userId, token, LocalDateTime.now().plusMinutes(15));
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

        UserAuth userAuth = userRepository.retrieveUserForAuth(email, mobile);
        if (userAuth.isAllowLogin()) {
            return utility.checkPassword(password, userAuth.getPassword());
        }
        throw new UserException("Email is not verified");
        
    }

    @Override
    public boolean verifyEmailToken(String token) {
        LocalDateTime currDateTime = LocalDateTime.now();
        String userId = userRepository.findEmailVerificationToken(token, currDateTime);
        System.out.println(userId);
        if (userId == null) {
            throw new UserException("Verification Token Invalid/Expired");
        }
        userRepository.updateEmailVerification(userId);
        return true;
    }

    @Override
    public boolean hasMobileBeenUsed(String mobile) {
        Utility utility = new Utility();
        if (!utility.isInputSafe(mobile)) {
            throw new InvalidArgsException("invalid mobile number");
        }
        String checkedMobile = userRepository.findMobile(mobile);
        if (checkedMobile != null) {
            return true;
        }
        return false;
    }
}
