package com.ticketmasterdemo.demo.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ticketmasterdemo.demo.common.exception.EventException;
import com.ticketmasterdemo.demo.dto.Event;
import com.ticketmasterdemo.demo.service.EventService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@CrossOrigin
@RequestMapping("/events")
public class EventController {
    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("")
    public ResponseEntity<?> getAllEvents() {
        try {
            List<Event> allEvents = eventService.getAllEvents();
            return new ResponseEntity<>(allEvents, HttpStatus.OK);
        }
        catch (Exception e) {
            log.error("Events Retrieval Error: ", e);
            return ResponseEntity.status(500).body("Server Error: " + e.getMessage());
        }
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<?> getSpecificEvent(@PathVariable String eventId) {
        try {
            Event event = eventService.getEvent(eventId);
            return new ResponseEntity<>(event, HttpStatus.OK);
        }
        catch (EventException e) {
            return ResponseEntity.status(404).body("Event Error: " + e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body("Server Error: " + e.getMessage());
        } 
    }
    
}




