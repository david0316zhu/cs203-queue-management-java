package com.ticketmasterdemo.demo.service;

import java.util.List;

import org.springframework.amqp.core.Queue;

import com.ticketmasterdemo.demo.dto.User;

public interface UserService {
    public boolean isUserEmailVerified(String email);

    public boolean isUserVerified(String email, String mobile);
    
    public User getUser(String email, String mobile);

    public User getUser(String email);

    public User createUser(String email, String mobile, String password);
    
    public boolean authenticateUser(String email, String mobile, String password);

    public Queue queue();

    public void sendVerificationTokenToEmailService(String email, String token);

    public boolean verifyEmailToken(String token);
}
