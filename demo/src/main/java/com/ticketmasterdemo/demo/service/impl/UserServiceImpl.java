package com.ticketmasterdemo.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.ticketmasterdemo.demo.common.exception.UserException;
import com.ticketmasterdemo.demo.dto.Registration;
import com.ticketmasterdemo.demo.dto.User;
import com.ticketmasterdemo.demo.repository.EventRegisterRepository;
import com.ticketmasterdemo.demo.repository.UserRepository;
import com.ticketmasterdemo.demo.service.EventRegisterService;
import com.ticketmasterdemo.demo.service.UserService;
import com.ticketmasterdemo.demo.util.Utility;
import com.ticketmasterdemo.demo.common.exception.UserException;

import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final EventRegisterRepository eventRegisterRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, EventRegisterRepository eventRegisterRepository) {
        this.userRepository = userRepository;
        this.eventRegisterRepository = eventRegisterRepository;
    }

    public boolean isUserEmailVerified(String email) {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            return false;
        }
        return user.isVerified();
    }
    public boolean isUserMobileVerified(String mobile) {
        User user = userRepository.findUserByMobile(mobile);
        if (user == null) {
            return false;
        }
        return user.isVerified();
    }

    public boolean isUserIdVerified(String id) {
        User user = userRepository.findUserByMobile(id);
        if (user == null) {
            return false;
        }
        return user.isVerified();
    }

}
