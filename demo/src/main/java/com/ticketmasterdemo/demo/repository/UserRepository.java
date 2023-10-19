package com.ticketmasterdemo.demo.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ticketmasterdemo.demo.dto.User;
import com.ticketmasterdemo.demo.dto.UserAuth;
import com.ticketmasterdemo.demo.dto.UserInfo;

@Mapper
public interface UserRepository {

    User findUserByEmailAndMobile(@Param("email") String email, @Param("mobile") String mobile);

    User findVerifiedUserByEmail(@Param("email") String email);

    User findAnyUserByEmail(@Param("email") String email);

    void createUser(@Param("email") String email, @Param("mobile") String mobile, @Param("password") String password,
            @Param("user_id") String userId, @Param("authenticator_id") String authenticatorId,
            @Param("is_verified") boolean isVerified);

    UserAuth retrieveUserForAuth(@Param("email") String email, @Param("mobile") String mobile);

    List<UserInfo> getAllUserInfo(@Param("group_id") String groupId);

    String findEmailVerificationToken(@Param("token") String token, @Param("date_time") LocalDateTime dateTime);

    int updateEmailVerification(@Param("user_id") String userId);

    int saveEmailToken(@Param("user_id") String userId, @Param("token") String token, @Param("expiry_date") LocalDateTime dateTime);

    int insertUserVerifier(@Param("user_id") String userId, @Param("phone_verified") Boolean phoneVerified);

    String findMobile(@Param("mobile") String mobile);
}
