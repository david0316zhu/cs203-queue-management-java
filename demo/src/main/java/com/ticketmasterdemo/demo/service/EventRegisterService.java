package com.ticketmasterdemo.demo.service;

import java.util.List;

import com.ticketmasterdemo.demo.dto.Registration;
import com.ticketmasterdemo.demo.dto.RegistrationInfo;
import com.ticketmasterdemo.demo.dto.User;
import com.ticketmasterdemo.demo.dto.UserInfo;

public interface EventRegisterService {
    public Registration registerGroup(Registration form);
    public Boolean registerUsers(List<User> userList, String groupId, String eventId, String groupLeaderEmail);
    public Boolean checkGroupRegistrationStatus(String groupId, String eventId);
    public Boolean updateEventGroupUserConfirmation(String userId, String eventId, String groupId);
    public RegistrationInfo getRegistrationGroupInfo(String userId, String eventId);
}
