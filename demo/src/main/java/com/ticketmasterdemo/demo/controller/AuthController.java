package com.ticketmasterdemo.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ticketmasterdemo.demo.common.exception.InvalidArgsException;
import com.ticketmasterdemo.demo.dto.User;
import com.ticketmasterdemo.demo.service.UserService;
import com.ticketmasterdemo.demo.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateAndLoginUser(@RequestBody User user) {
        try {
            if (userService.authenticateUser(user.getEmail(), user.getMobile(), user.getPassword())) {
                String jwt = JwtUtil.generateToken(user.getMobile()); // I'm lazy
                return ResponseEntity.ok().body(jwt);
            }
            return ResponseEntity.ok().body(false);
        } catch (InvalidArgsException e) {
            log.error("Verify multiple error: ", e);
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
            return ResponseEntity.internalServerError().body("Server Error: " + e.getMessage());
        }
    }
}
