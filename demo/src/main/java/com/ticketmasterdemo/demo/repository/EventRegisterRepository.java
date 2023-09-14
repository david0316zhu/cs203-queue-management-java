package com.ticketmasterdemo.demo.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ticketmasterdemo.demo.dto.Registration;
import com.ticketmasterdemo.demo.dto.User;

@Mapper
public interface EventRegisterRepository {
    int registerGroup(Registration form);
    int registerUser(@Param("user") User user, @Param("group_id") String groupId, @Param("event_id") String eventId);
    Boolean checkGroupStatus(@Param("group_id") String groupId, @Param("event_id") String eventId);
}
