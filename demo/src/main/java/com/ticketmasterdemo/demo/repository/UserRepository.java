package com.ticketmasterdemo.demo.repository;


import org.apache.ibatis.annotations.Mapper;
import com.ticketmasterdemo.demo.dto.User;

@Mapper
public interface UserRepository {
    
    User findUserByEmailAndMobile(String email, String mobile);
    User findUserByEmail(String email);
}
