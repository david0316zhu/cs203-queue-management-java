package com.ticketmasterdemo.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestHeader;

import com.ticketmasterdemo.demo.common.exception.InvalidArgsException;
import com.ticketmasterdemo.demo.common.exception.UserException;
import com.ticketmasterdemo.demo.service.UserService;
import com.ticketmasterdemo.demo.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@CrossOrigin(allowedHeaders = { "Authorization", "Content-Type", "email", "mobile", "password" }, 
             exposedHeaders = { "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials", "Access-Control-Allow-Headers" })
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateAndLoginUser(@RequestHeader("email") String email, @RequestHeader("mobile") String mobile, @RequestHeader("password") String password) {
        try {
            if (userService.authenticateUser(email, mobile , password)) {
                String jwt = JwtUtil.generateToken(mobile);
                return ResponseEntity.ok().body(jwt);
            }
            return ResponseEntity.ok().body(false);
        } catch (InvalidArgsException e) {
            log.error("Verify multiple error: ", e);
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        } catch (UserException e){
            log.error(e.getMessage());
            return ResponseEntity.status(401).body(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
            return ResponseEntity.internalServerError().body("Server Error: " + e.getMessage());
        }
    }
}
