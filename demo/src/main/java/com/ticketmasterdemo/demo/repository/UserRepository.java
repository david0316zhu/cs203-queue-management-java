package com.ticketmasterdemo.demo.repository;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ticketmasterdemo.demo.dto.User;

@Mapper
public interface UserRepository {
    
    User findUserByEmailAndMobile(@Param("email") String email, @Param("mobile") String mobile);
    User findUserByEmail(@Param("email") String email);

    String retrieveUserForAuth(@Param("email") String email, @Param("mobile") String mobile);
}
