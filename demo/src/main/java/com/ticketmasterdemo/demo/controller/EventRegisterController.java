package com.ticketmasterdemo.demo.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ticketmasterdemo.demo.common.exception.UserException;
import com.ticketmasterdemo.demo.dto.Registration;
import com.ticketmasterdemo.demo.service.EventRegisterService;

@RestController
@Slf4j
@CrossOrigin
@RequestMapping("/events-register")
public class EventRegisterController {

    private final EventRegisterService eventRegisterService;

    @Autowired
    public EventRegisterController(EventRegisterService eventRegisterService) {
        this.eventRegisterService = eventRegisterService;
    }

    @PostMapping("/group")
    public ResponseEntity<?> registerGroup(@RequestBody Registration form) {
        try {
            Registration responseForm = eventRegisterService.registerGroup(form);
            return ResponseEntity.status(200).body(responseForm);
        }
        catch (UserException e){
            log.error("Insert registration error: ", e);
            return ResponseEntity.status(200).body(e.getMessage());
        }
        catch (Exception e){
            log.error("Insert registration error: ", e);
            return ResponseEntity.status(500).body("Server Error " + e.getMessage());
        }
        
    }
}
