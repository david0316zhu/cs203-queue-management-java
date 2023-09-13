package com.ticketmasterdemo.demo.service;

import com.ticketmasterdemo.demo.dto.User;

public interface UserService {
    public boolean isUserEmailVerified(String email);
}
