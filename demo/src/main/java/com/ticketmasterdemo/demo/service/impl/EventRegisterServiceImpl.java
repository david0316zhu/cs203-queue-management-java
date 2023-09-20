package com.ticketmasterdemo.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        List<User> userList = form.getUserGroup();
        List<User> responseUserList = new ArrayList<>();

        for (User user : userList) {
            try {
                User verifiedUser = userRepository.findUserByEmailAndMobile(user.getEmail(), user.getMobile());
                System.out.println("Passes here");
                System.out.println(user.getEmail() + user.getMobile());
                System.out.println(verifiedUser);
                if (verifiedUser == null) {
                    throw new UserException("Email(s) in group are not registered as users");
                }
                
                
                if (verifiedUser != null && !verifiedUser.isVerified()) {
                    throw new UserException("User(s) in group are not verified"); // User is not authenticated
                }
                System.out.println(user.getEmail() + "and" + user.getMobile());
                responseUserList.add(verifiedUser);
            }
            catch (UserException e) {
                throw new UserException(e.getMessage());
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
        try {
            eventRegisterRepository.registerGroup(form);
        }
        catch (Exception e){
            throw new UserException("User Group registration error!");
        }
        log.info("Group Registration Success");

        try {
            if (!this.registerUsers(responseUserList, groupId, form.getEventId(), form.getGroupLeaderEmail())) {
                throw new UserException("User Registration Error!");
            }
            log.info("Group Users Registration Success");
        }
        catch (Exception e){
            throw new UserException("User Registration Error!");
        }
        return form; // Registration is successful
       
    }

    @Override
    public Boolean registerUsers(List<User> userList, String groupId, String eventId, String groupLeaderEmail) {
        try {
            for (User user : userList) {
                boolean isLeader = user.getEmail().equals(groupLeaderEmail);
                eventRegisterRepository.registerUser(user, groupId, eventId, isLeader);
            }
            return true; // User registration is successful
        } catch (Exception e) {
            // Handle any unexpected exceptions here
            e.printStackTrace(); // Log the exception for debugging
            return false;
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
