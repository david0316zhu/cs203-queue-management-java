package com.ticketmasterdemo.demo.service;

import java.util.List;

import com.ticketmasterdemo.demo.dto.Registration;
import com.ticketmasterdemo.demo.dto.User;

public interface EventRegisterService {
    public Registration registerGroup(Registration form);
    public boolean registerUsers(List<User> userList, String groupId, String eventId);
    
}
