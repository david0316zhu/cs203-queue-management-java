package com.ticketmasterdemo.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
            try {
                User verifiedUser = userRepository.findUserByEmailAndMobile(user.getEmail(), user.getMobile());
                if (verifiedUser == null) {
                    throw new UserException("Email(s) in group are not registered as users");
                }
                
                if (verifiedUser != null && !verifiedUser.isVerified()) {
                    throw new UserException("User(s) in group are not verified"); // User is not authenticated
                }
                System.out.println(user.getEmail() + "and" + user.getMobile());
                responseUserList.add(verifiedUser);
            }
            catch (Exception e){
                throw new UserException("User Error!");
            }
        }
        form.setUserGroup(responseUserList);

        Utility util = new Utility();
        String groupId = util.generateRandomUUID();
        form.setId(groupId);
        System.out.println(form.getGroupLeaderEmail());
        User groupLeader = userRepository.findUserByEmail(form.getGroupLeaderEmail());
        form.setGroupLeaderId(groupLeader.getId());
        eventRegisterRepository.registerGroup(form);
        log.info("Group Registration Success");

        if (!this.registerUsers(responseUserList, groupId, form.getEventId())) {
            throw new UserException("User Registration Error!");
        }
        log.info("Group Users Registration Success");
        return form; // Registration is successful
       
    }

    public boolean registerUsers(List<User> userList, String groupId, String eventId) {
        try {
            for (User user : userList) {
                eventRegisterRepository.registerUser(user, groupId, eventId);
            }
            return true; // User registration is successful
        } catch (Exception e) {
            // Handle any unexpected exceptions here
            e.printStackTrace(); // Log the exception for debugging
            return false;
        }
    }
}