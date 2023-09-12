package com.ticketmasterdemo.demo.repository;

import java.util.Optional;

import com.ticketmasterdemo.demo.model.User;

public interface UserRepository {
    Optional<User> findUser(String email);
}
