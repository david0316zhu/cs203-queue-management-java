package com.ticketmasterdemo.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import com.ticketmasterdemo.demo.common.exception.EventRegisterException;
import com.ticketmasterdemo.demo.common.exception.UserException;
import com.ticketmasterdemo.demo.dto.Registration;
import com.ticketmasterdemo.demo.dto.User;
import com.ticketmasterdemo.demo.repository.EventRegisterRepository;
import com.ticketmasterdemo.demo.repository.UserRepository;
import com.ticketmasterdemo.demo.service.EventRegisterService;
import com.ticketmasterdemo.demo.util.Utility;

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
        log.info("Begin registerGroup Service method ");
        List<User> userList = form.getUserGroup();
        List<User> responseUserList = new ArrayList<>();

        for (User user : userList) {
            try {
                log.info("Verify user: " + user.getEmail() + ", " + user.getMobile());
                User verifiedUser = userRepository.findUserByEmailAndMobile(user.getEmail(), user.getMobile());
                if (verifiedUser == null) {
                    throw new UserException("Email(s) in group are not registered as users");
                }
                
                
                if (verifiedUser != null && !verifiedUser.isVerified()) {
                    throw new UserException("User(s) in group are not verified"); // User is not authenticated
                }
                responseUserList.add(verifiedUser);
            }
            catch (UserException e) {
                log.info(e.getMessage());
                throw new UserException(e.getMessage());
            }

        }
        form.setUserGroup(responseUserList);

        Utility util = new Utility();
        String groupId = util.generateRandomUUID();
        log.info("Generate Group ID: " + groupId);
        form.setId(groupId);

        User groupLeader = userRepository.findUserByEmail(form.getGroupLeaderEmail());
        form.setGroupLeaderId(groupLeader.getId());
        try {
            log.info(form.getGroupLeaderId());
            eventRegisterRepository.registerGroup(form);
        }
        catch (DataIntegrityViolationException e) {
            if (e.getCause() instanceof SQLIntegrityConstraintViolationException){
                // Handle the specific duplicate key scenario
                throw new UserException("User is already a group leader of another Group of the same event!");
            }
            else {
                throw new UserException("Group Insertion Error");
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            throw new UserException("Group registration error!");
        }
        log.info("Group Registration Success");

        try {
            this.registerUsers(responseUserList, groupId, form.getEventId(), form.getGroupLeaderEmail());
            log.info("Group Users Registration Success");
        }
        catch (Exception e){
            throw new UserException(e.getMessage());
        }
        return form; // Registration is successful
       
    }

    @Override
    public Boolean registerUsers(List<User> userList, String groupId, String eventId, String groupLeaderEmail) {
        try {
            for (User user : userList) {
                boolean isLeaderBool = user.getEmail().equals(groupLeaderEmail);
                int isLeader = isLeaderBool ? 1 : 0;
                eventRegisterRepository.registerUser(user, groupId, eventId, isLeader);
            }
            return true; // User registration is successful
        }
        catch (DataIntegrityViolationException e) {
            if (e.getCause() instanceof SQLIntegrityConstraintViolationException){
                // Handle the specific duplicate key scenario
                throw new UserException("User is already in another Group of the same event!");
            }
            else {
                throw new UserException("Group Users Registration Insertion Error");
            }
        }
        catch (Exception e) {
            throw new UserException("Group Users Registration Error");
        }
    }
    
    @Override
    public Boolean checkGroupRegistrationStatus(String groupId, String eventId) {
        try {
            Boolean status = eventRegisterRepository.checkGroupStatus(groupId, eventId);
            
            if (status == null) {
                throw new EventRegisterException("Group ID/Event ID does not exist");
            }
            return status;
        }
        catch (Exception e) {
            // Handle any unexpected exceptions here
            e.printStackTrace(); // Log the exception for debugging
            throw new EventRegisterException("Unable to retrieve Group Data");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateEventGroupUserConfirmation(String userId, String eventId, String groupId) {
        try {
            eventRegisterRepository.updateUserStatus(groupId, eventId,userId);
            Boolean status = eventRegisterRepository.checkUserStatus(groupId, eventId, userId);
            if (status == null) {
                throw new EventRegisterException("User ID/Group ID/Event ID does not match");
            }
            return status;
        }
        catch (Exception e) {
            e.printStackTrace(); // Log the exception for debugging
            throw new EventRegisterException("Unable to update user confirmation Data");
        }
    }
}
