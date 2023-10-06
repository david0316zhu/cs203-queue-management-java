package com.ticketmasterdemo.demo.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ticketmasterdemo.demo.dto.User;
import com.ticketmasterdemo.demo.dto.UserInfo;

@Mapper
public interface UserRepository {

    User findUserByEmailAndMobile(@Param("email") String email, @Param("mobile") String mobile);

    User findUserByEmail(@Param("email") String email);

    String retrieveUserForAuth(@Param("email") String email, @Param("mobile") String mobile);

    List<UserInfo> getAllUserInfo(@Param("group_id") String groupId);

    String findEmailVerificationToken(@Param("token") String token, @Param("date_time") LocalDateTime dateTime);

    int updateEmailVerification(@Param("user_id") String userId);
}
