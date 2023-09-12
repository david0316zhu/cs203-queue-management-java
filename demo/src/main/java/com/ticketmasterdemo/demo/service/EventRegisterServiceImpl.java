package com.ticketmasterdemo.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.ticketmasterdemo.demo.model.Registration;
import com.ticketmasterdemo.demo.model.User;
import com.ticketmasterdemo.demo.repository.EventRegisterRepository;
import com.ticketmasterdemo.demo.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class EventRegisterServiceImpl implements EventRegisterService{
    
    private EventRegisterRepository eventRegisterRepository;
    private UserRepository userRepository;

    public EventRegisterServiceImpl(UserRepository userRepository, EventRegisterRepository eventRegisterRepository) {
        this.userRepository = userRepository;
        this.eventRegisterRepository = eventRegisterRepository;
    }


    public Registration registerGroup(Registration form) {
        List<User> userList = form.getUserGroup();
        List<User> responseUserList = new ArrayList<>();
        try {
            for (User user : userList) {
                System.out.println(user.getEmail());
                Optional<User> checkedUserOptional = userRepository.findUser(user.getEmail());
                User checkedUser = checkedUserOptional.orElse(null);
                
                if (checkedUser != null && !checkedUser.getAuthenticated()) {
                    return null; // User is not authenticated
                }
                responseUserList.add(checkedUser);
            }
            form.setUserGroup(responseUserList);
            Registration responseForm = eventRegisterRepository.addRegistration(form);
            
            if (responseForm == null) {
                // Handle the case where registration fails, e.g., database error
                return null;
            }
            
            if (!registerUsers(responseUserList, responseForm.getId())) {
                // Handle the case where user registration fails
                return null;
            }
            
            return responseForm; // Registration is successful
        } catch (Exception e) {
            // Handle any unexpected exceptions here
            e.printStackTrace(); // Log the exception for debugging
            return null;
        }
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
