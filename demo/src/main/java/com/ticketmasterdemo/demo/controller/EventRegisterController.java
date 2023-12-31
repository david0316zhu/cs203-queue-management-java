package com.ticketmasterdemo.demo.controller;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.ticketmasterdemo.demo.dto.AddMember;
import com.ticketmasterdemo.demo.dto.Registration;
import com.ticketmasterdemo.demo.dto.RegistrationInfo;
import com.ticketmasterdemo.demo.dto.Status;

import com.ticketmasterdemo.demo.service.EventRegisterService;

@RestController
@Slf4j
@CrossOrigin(allowedHeaders = { "*" }, exposedHeaders = { "Access-Control-Allow-Origin",
        "Access-Control-Allow-Credentials" })
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
        } catch (InvalidArgsException e) {
            return ResponseEntity.status(422).body("Invalid Request Error: " + e.getMessage());
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
        } catch (InvalidArgsException e) {
            return ResponseEntity.status(422).body("Invalid Request Error: " + e.getMessage());
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
        } catch (InvalidArgsException e) {
            return ResponseEntity.status(422).body("Invalid Request Error: " + e.getMessage());
        } catch (Exception e) {
            log.error("Event Group User confirmation error: ", e);
            return ResponseEntity.status(500).body("Server Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/group/leave-group")
    public ResponseEntity<?> leaveGroup(@RequestParam String groupId, @RequestParam String eventId, @RequestParam String userId){
        try {
            boolean status = eventRegisterService.removeMemberFromGroup(groupId, userId, eventId);
            return ResponseEntity.ok().body(status);
        } catch (InvalidArgsException e){
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        } catch (EventRegisterException e){
            // An EventRegisterException is thrown in this case, because we are unable to remove a group leader from the group.
            if (e.getMessage().startsWith("Unable to remove")){
              return ResponseEntity.status(403).body(e.getMessage());  
            }
            return ResponseEntity.status(500).body("Event Register Error: "+ e.getMessage());
        } catch (Exception e){
            return ResponseEntity.internalServerError().body("Server Error: " +e.getMessage());
        }
    }

    @PostMapping("/group/event/add-member")
    public ResponseEntity<?> addUserGroupEvent(@RequestBody AddMember form) {
        try {
            Boolean addUsersStatus = eventRegisterService.addUsersToGroup(form);
            Status responseStatus = new Status();
            responseStatus.setStatus(addUsersStatus);
            return new ResponseEntity<>(responseStatus, HttpStatus.OK);
        } catch (EventRegisterException e) {
            log.error("Event Group User confirmation error: ", e);
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (InvalidArgsException e) {
            return ResponseEntity.status(422).body("Invalid Request Error: " + e.getMessage());
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
        } catch (UserException e) {
            log.error("User Registration Group Info error: ", e);
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (InvalidArgsException e) {
            return ResponseEntity.status(422).body("Invalid Request Error: " + e.getMessage());
        } catch (Exception e) {
            log.error("User Registration Group Info error: ", e);
            return ResponseEntity.status(500).body("Server Error: " + e.getMessage());
        }
    }

    @PostMapping("/modify-group")
    public ResponseEntity<?> modifyGroup(@RequestBody Registration form){
        try{
            boolean status = eventRegisterService.modifyGroup(form);
            // true on success
            return ResponseEntity.status(200).body(status);
            
        } catch (InvalidArgsException e){
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (UserException e){
            return ResponseEntity.status(422).body("Invalid Request Error: " + e.getMessage());
        } catch (Exception e){
            log.error("Group modification error", e);
            return ResponseEntity.status(500).body("Server Error: " + e.getMessage());
        }
    }
}
