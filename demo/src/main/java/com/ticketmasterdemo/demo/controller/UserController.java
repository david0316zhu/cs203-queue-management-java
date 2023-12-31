package com.ticketmasterdemo.demo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ticketmasterdemo.demo.common.exception.InvalidArgsException;
import com.ticketmasterdemo.demo.common.exception.UserException;
import com.ticketmasterdemo.demo.dto.PaymentInfo;
import com.ticketmasterdemo.demo.dto.User;
import com.ticketmasterdemo.demo.service.UserService;
import com.ticketmasterdemo.demo.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@CrossOrigin(allowedHeaders = { "Authorization", "Content-Type" }, exposedHeaders = { "Access-Control-Allow-Origin",
        "Access-Control-Allow-Credentials", "Access-Control-Allow-Headers" })
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody Map<String, String> userData) {
        try {
            userService.createUser(userData.get("email"), userData.get("mobile"), userData.get("password"));
            User user = userService.getUser(userData.get("email"));
            return ResponseEntity.ok().body(user);
        } catch (InvalidArgsException e) {
            log.error("Create-user error: ", e);
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        } catch (UserException e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body("Server Error: " + e.getMessage());
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getUserByEmailAndMobile(@RequestParam String email, @RequestParam String mobile) {
        try {
            User user = userService.getUser(email, mobile);
            return ResponseEntity.ok().body(user);
        } catch (InvalidArgsException e) {
            log.error("Get-user error: ", e);
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Server Error: " + e.getMessage());
        }
    }

    @GetMapping("/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        try {
            User user = userService.getUser(email);
            return ResponseEntity.ok().body(user);
        } catch (InvalidArgsException e) {
            log.error("Get-user error: ", e);
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Server Error: " + e.getMessage());
        }
    }

    @GetMapping("/is-verified")
    public ResponseEntity<?> isUserVerified(@RequestParam String email, @RequestParam String mobile) {
        try {
            boolean success = userService.isUserVerified(email, mobile);
            return ResponseEntity.ok().body(success);
        } catch (InvalidArgsException e) {
            log.error("User verification error: ", e);
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Server Error: " + e.getMessage());
        }
    }

    @PostMapping("/auth/login")
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

    @PostMapping("/auth/verification/")
    public ResponseEntity<?> emailVerification(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        try {
            return ResponseEntity.ok().body(userService.verifyEmailToken(token));
        } catch (UserException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
            return ResponseEntity.internalServerError().body("Server Error: " + e.getMessage());
        }
    }

    @GetMapping("/mobile/{mobile}")
    public ResponseEntity<?> checkMobile(@PathVariable String mobile) {
        try {
            return ResponseEntity.ok().body(userService.hasMobileBeenUsed(mobile));
        } catch (InvalidArgsException e) {
            log.error("Verify multiple error: ", e);
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body("Server Error: " + e.getMessage());
        }
    }

    @GetMapping("/is-payment-verified/{email}/{mobile}")
    public ResponseEntity<?> userPaymentMethodVerification(@PathVariable String email, @PathVariable String mobile) {
        try {
            return ResponseEntity.ok().body(userService.verifyUserPaymentMethod(email, mobile));
        } catch (InvalidArgsException e) {
            log.error("Verify multiple error: ", e);
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body("Server Error: " + e.getMessage());
        }
    }


@PostMapping("/store-payment-info")
    public ResponseEntity<?> storePaymentInfo(@RequestBody PaymentInfo paymentInfo) {
        try {
            return ResponseEntity.ok().body(userService.saveUserPaymentMethod(paymentInfo));
        } catch (UserException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
            return ResponseEntity.internalServerError().body("Server Error: " + e.getMessage());
        }
    }
}
