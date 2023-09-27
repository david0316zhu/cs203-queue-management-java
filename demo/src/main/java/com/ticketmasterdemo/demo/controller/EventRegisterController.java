package com.ticketmasterdemo.demo.controller;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ticketmasterdemo.demo.common.exception.EventRegisterException;
import com.ticketmasterdemo.demo.common.exception.InvalidArgsException;
import com.ticketmasterdemo.demo.common.exception.UserException;
import com.ticketmasterdemo.demo.dto.Registration;
import com.ticketmasterdemo.demo.dto.RegistrationInfo;
import com.ticketmasterdemo.demo.dto.Status;
import com.ticketmasterdemo.demo.service.EventRegisterService;
import com.ticketmasterdemo.demo.service.enums.ValStatus;

@RestController
@Slf4j
@CrossOrigin
// @CrossOrigin(allowedHeaders = {"*"}, exposedHeaders = {"Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"})
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
            form.setGroupSize(form.getUserGroup().size());
            Registration responseForm = eventRegisterService.registerGroup(form);
            return new ResponseEntity<>(responseForm, HttpStatus.OK);
        } catch (UserException e) {
            log.error("User registration error: ", e);
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            log.error("Insert registration error: ", e);
            return ResponseEntity.status(500).body("Server Error: " + e.getMessage());
        }
    }

    @GetMapping("/{eventId}/reg-statuses/{groupId}")
    public ResponseEntity<?> getGroupEventRegistrationStatus(@PathVariable String eventId,
            @PathVariable String groupId) {
        try {
            Boolean groupEventRegistrationStatus = eventRegisterService.checkGroupRegistrationStatus(groupId, eventId);
            Status responseStatus = new Status();
            responseStatus.setStatus(groupEventRegistrationStatus);
            return new ResponseEntity<>(responseStatus, HttpStatus.OK);
        } catch (EventRegisterException e) {
            log.error("Event registration error: ", e);
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            log.error("Event registration status error: ", e);
            return ResponseEntity.status(500).body("Server Error: " + e.getMessage());
        }
    }

    @PutMapping("/group/event/user/confirm")
    public ResponseEntity<?> updateGroupEventUserConfirmation(@RequestBody Map<String, String> requestData) {
        try {
            Boolean groupEventUserStatus = eventRegisterService.updateEventGroupUserConfirmation(
                    requestData.get("userId"), requestData.get("eventId"), requestData.get("groupId"));
            Status responseStatus = new Status();
            responseStatus.setStatus(groupEventUserStatus);
            return new ResponseEntity<>(responseStatus, HttpStatus.OK);
        } catch (EventRegisterException e) {
            log.error("Event Group User confirmation error: ", e);
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            log.error("Event Group User confirmation error: ", e);
            return ResponseEntity.status(500).body("Server Error: " + e.getMessage());
        }
    }

    @GetMapping("/{eventId}/user/{userId}/group-info")
    public ResponseEntity<?> getRegistrationGroupInfo(@PathVariable String eventId, @PathVariable String userId) {
        try {
            RegistrationInfo registrationGroupInfo = eventRegisterService.getRegistrationGroupInfo(userId, eventId);
            return new ResponseEntity<>(registrationGroupInfo, HttpStatus.OK);
        } catch (EventRegisterException e) {
            log.error("User Registration Group Info error: ", e);
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            log.error("User Registration Group Info error: ", e);
            return ResponseEntity.status(500).body("Server Error: " + e.getMessage());
        }
    }
}
