package com.ticketmasterdemo.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ticketmasterdemo.demo.common.exception.InvalidArgsException;
import com.ticketmasterdemo.demo.common.exception.UserException;
import com.ticketmasterdemo.demo.dto.User;
import com.ticketmasterdemo.demo.repository.UserRepository;
import com.ticketmasterdemo.demo.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUser(String email, String mobile) {
        if (email == null || mobile == null) {
            throw new InvalidArgsException("mobile and email is invalid");
        }
        User user = userRepository.findUserByEmailAndMobile(email, mobile);
        if (user == null)
            throw new UserException("user does not exist");
        return user;
    }

    @Override
    public boolean isUserVerified(String email, String mobile) {
        
        User user = getUser(email, mobile);
        return (user != null) && user.isVerified();
    }

    @Override
    public boolean isUserEmailVerified(String email) {
        if (email == null)
            throw new InvalidArgsException("email is invalid");
        User user = userRepository.findUserByEmail(email);
        return (user != null) && user.isVerified();
    }

}
