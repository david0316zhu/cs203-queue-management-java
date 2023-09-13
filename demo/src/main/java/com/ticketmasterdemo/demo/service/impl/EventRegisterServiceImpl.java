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
import com.ticketmasterdemo.demo.util.Utility;
import com.ticketmasterdemo.demo.common.exception.UserException;

import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EventRegisterServiceImpl implements EventRegisterService{
    
    private final EventRegisterRepository eventRegisterRepository;
    private final UserRepository userRepository;

    @Autowired
    public EventRegisterServiceImpl(UserRepository userRepository, EventRegisterRepository eventRegisterRepository) {
        this.userRepository = userRepository;
        this.eventRegisterRepository = eventRegisterRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Registration registerGroup(Registration form) throws UserException {

        List<User> userList = form.getUserGroup();
        List<User> responseUserList = new ArrayList<>();

        for (User user : userList) {
            User verifiedUser = userRepository.findUserByEmail(user.getEmail());
            if (verifiedUser == null) {
                throw new UserException("Email(s) in group are not registered as users");
            }
            
            if (verifiedUser != null && !verifiedUser.isVerified()) {
                throw new UserException("User(s) in group are not verified"); // User is not authenticated
            }
            responseUserList.add(verifiedUser);
        }
        form.setUserGroup(responseUserList);

        Utility util = new Utility();
        String groupId = util.generateRandomUUID();
        form.setId(groupId);
        eventRegisterRepository.registerGroup(form);
        
        return form; // Registration is successful
       
    }

    public boolean registerUsers(List<User> userList, String groupId) {
        try {
            for (User user : userList) {
                eventRegisterRepository.registerUser(user, groupId);
            }
            return true; // User registration is successful
        } catch (Exception e) {
            // Handle any unexpected exceptions here
            e.printStackTrace(); // Log the exception for debugging
            return false;
        }
    }
}
