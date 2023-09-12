package com.ticketmasterdemo.demo.repository;

import com.ticketmasterdemo.demo.model.Registration;
import com.ticketmasterdemo.demo.model.User;


public interface EventRegisterRepository {
    Registration addRegistration(Registration form);
    boolean registerUser(User user, String group_id);
}
