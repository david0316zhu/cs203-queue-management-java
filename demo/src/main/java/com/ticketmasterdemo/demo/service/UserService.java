package com.ticketmasterdemo.demo.service;

import com.ticketmasterdemo.demo.dto.User;

public interface UserService {
    public boolean isUserEmailVerified(String email);

    public boolean isUserVerified(String email, String mobile);
    
    public User getUser(String email, String mobile);
    
}
