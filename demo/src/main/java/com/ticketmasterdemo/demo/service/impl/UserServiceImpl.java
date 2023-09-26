package com.ticketmasterdemo.demo.service.impl;

import java.security.DrbgParameters.Reseed;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
        try {
           User user = getUser(email, mobile);
           return (user != null) && user.isVerified(); 
        } catch(UserException e){
            return false;
        }
    }

    @Override
    public boolean isUserEmailVerified(String email) {
        if (email == null)
            throw new InvalidArgsException("email is invalid");
        User user = userRepository.findUserByEmail(email);
        return (user != null) && user.isVerified();
    }

    @Override
    public List<Boolean> verifyMultiple(List<String> emailList, List<String> mobileList) {
        if (emailList == null || mobileList == null || emailList.size() != mobileList.size()) {
            throw new InvalidArgsException("email list or mobile list provided is invalid.");
        }
        List<Boolean> output = new ArrayList<>();
        Iterator<String> emailIter = emailList.iterator(), mobileIter = mobileList.iterator();
        while (emailIter.hasNext() && mobileIter.hasNext()) {
            output.add(isUserVerified(emailIter.next(), mobileIter.next()));
        }
        return output;
    }

    @Override
    public boolean authenticateUser(String email, String mobile, String password){
        if (email == null || mobile == null || password == null) {
            throw new InvalidArgsException("invalid login credentials - failed preliminary check");
        } 
        String userPwd = userRepository.retrieveUserForAuth(email, mobile);
        return password.equals(userPwd);
    }
}
