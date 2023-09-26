package com.ticketmasterdemo.demo.service;

import java.util.List;
import java.util.Map;

import com.ticketmasterdemo.demo.dto.AddMember;
import com.ticketmasterdemo.demo.dto.Registration;
import com.ticketmasterdemo.demo.dto.User;

public interface EventRegisterService {
    public Registration registerGroup(Registration form);
    public Boolean registerUsers(List<User> userList, String groupId, String eventId, String groupLeaderEmail);
    public Boolean checkGroupRegistrationStatus(String groupId, String eventId);
    public Boolean updateEventGroupUserConfirmation(String userId, String eventId, String groupId);
    public Boolean addUsersToGroup(AddMember form);
}
