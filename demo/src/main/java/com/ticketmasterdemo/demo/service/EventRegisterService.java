package com.ticketmasterdemo.demo.service;

import java.util.List;

import com.ticketmasterdemo.demo.model.Registration;
import com.ticketmasterdemo.demo.model.User;

public interface EventRegisterService {
    public Registration registerGroup(Registration form);
    public boolean registerUsers(List<User> userList, String groupId);
    
}
