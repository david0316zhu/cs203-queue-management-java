package com.ticketmasterdemo.demo.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ticketmasterdemo.demo.dto.Registration;
import com.ticketmasterdemo.demo.dto.User;
import com.ticketmasterdemo.demo.dto.UserInfo;

@Mapper
public interface EventRegisterRepository {
    int registerGroup(Registration form);
    int registerUser(@Param("user") User user, @Param("group_id") String groupId, @Param("event_id") String eventId, @Param("confirmation") int confirmation);
    Boolean checkGroupStatus(@Param("group_id") String groupId, @Param("event_id") String eventId);
    int updateUserStatus(@Param("group_id") String groupId, @Param("event_id") String eventId, @Param("user_id") String userId);
    Boolean checkUserStatus(@Param("group_id") String groupId, @Param("event_id") String eventId, @Param("user_id") String userId);
    String getRegistrationGroupId(@Param("user_id") String userId, @Param("event_id") String eventId);
    String getRegistrationGroupLeader(@Param("group_id") String groupId);
    List<UserInfo> getAllUserInfo(@Param("group_id") String groupId);
}
