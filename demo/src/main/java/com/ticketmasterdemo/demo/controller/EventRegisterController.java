package com.ticketmasterdemo.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ticketmasterdemo.demo.model.Registration;
import com.ticketmasterdemo.demo.service.EventRegisterService;

@RestController
public class EventRegisterController {
    private EventRegisterService eventRegisterService;

    public EventRegisterController(EventRegisterService eventRegisterService) {
        this.eventRegisterService = eventRegisterService;
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping("/events/register/group")
    public Registration registerGroup(@RequestBody Registration form) {
        return eventRegisterService.registerGroup(form);
    }
}
