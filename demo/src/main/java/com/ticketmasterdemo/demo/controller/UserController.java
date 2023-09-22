package com.ticketmasterdemo.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ticketmasterdemo.demo.common.exception.InvalidArgsException;
import com.ticketmasterdemo.demo.dto.User;
import com.ticketmasterdemo.demo.service.UserService;

import lombok.extern.slf4j.Slf4j;


@RestController
@Slf4j
@CrossOrigin
@RequestMapping("/users")
public class UserController {
    
    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("")
    public ResponseEntity<?> getUserByEmailAndMobile(@RequestParam String email, @RequestParam String mobile) {
        try{
            User user = userService.getUser(email, mobile);
            return ResponseEntity.ok().body(user);
        } catch (InvalidArgsException e){
            log.error("Get-user error: ", e);
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.internalServerError().body("Server Error: " + e.getMessage());
        }
    }

    @GetMapping("/is-verified")
    public ResponseEntity<?> isUserVerified(@RequestParam String email, @RequestParam String mobile) {
        try {
            boolean success = userService.isUserVerified(email, mobile);
            return ResponseEntity.ok().body(success);
        } catch (InvalidArgsException e){
            log.error("User verification error: " , e);
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.internalServerError().body("Server Error: " + e.getMessage());
        }
    }


}

