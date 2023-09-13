package com.ticketmasterdemo.demo.repository;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import com.ticketmasterdemo.demo.dto.User;

@Mapper
public interface UserRepository {
    
    User findUserByEmail(String email);
    User findUserById(String userId);
}
