package com.ticketmasterdemo.demo.repository;

import org.apache.ibatis.annotations.Mapper;

import com.ticketmasterdemo.demo.dto.Registration;
import com.ticketmasterdemo.demo.dto.User;

@Mapper
public interface EventRegisterRepository {
    int registerGroup(Registration form);
    int registerUser(User user, String group_id);
}
