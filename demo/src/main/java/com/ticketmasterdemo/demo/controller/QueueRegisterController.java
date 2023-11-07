package com.ticketmasterdemo.demo.controller;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ticketmasterdemo.demo.common.exception.InvalidArgsException;
import com.ticketmasterdemo.demo.common.exception.QueueRegisterException;
import com.ticketmasterdemo.demo.dto.Queue;
import com.ticketmasterdemo.demo.dto.QueueSelection;
import com.ticketmasterdemo.demo.service.QueueRegisterService;

@RestController
@CrossOrigin
@RequestMapping("/queues")
public class QueueRegisterController {
    private final QueueRegisterService queueRegisterService;

    public QueueRegisterController(QueueRegisterService queueRegisterService) {
        this.queueRegisterService = queueRegisterService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerForQueues(@RequestBody QueueSelection queueSelection) {
        try {
            boolean status = queueRegisterService.processQueueChoices(queueSelection.getGroupId(),
                    queueSelection.getEventId(), queueSelection.getUserId(), queueSelection.getQueueIdList(),
                    queueSelection.getShowIdList());

            return ResponseEntity.ok().body(status);
        } catch (InvalidArgsException e) {
            return ResponseEntity.unprocessableEntity().body("Invalid request: " + e.getMessage());

        } catch (QueueRegisterException e) {
            return ResponseEntity.status(406).body("Queue Register Error: " + e.getMessage());
        } catch (Exception e) {
            if (e.getMessage().contains("Duplicate entry")) {
                // Inefficient: but when i try to catch the
                // java.sqL.SQLIntegrityViolationException it doesn't work.
                return ResponseEntity.status(409).body(
                        "Queue Register Error: This group might have already registered queues --> " + e.getMessage());
            }
            return ResponseEntity.status(500).body("Server Error: " + e.getMessage());
        }
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<?> getQueuesForEvent(@PathVariable String eventId){
        try {
            List<Queue> queueInfo = queueRegisterService.getQueueInformation(eventId);
            return ResponseEntity.ok().body(queueInfo);
        } catch (InvalidArgsException e){
            return ResponseEntity.unprocessableEntity().body("Invalid Request: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Server Error: " + e.getMessage());
        }
    }

    

    @PostMapping("/{email}/{eventId}/{queueId}")
    public ResponseEntity<?> getQueueNumber(@PathVariable String email, @PathVariable String eventId, @PathVariable String queueId){
        try {
            Map<String, Integer> queueNumberMap = new HashMap<>();
            Integer queueNumber = queueRegisterService.getQueueNumber(email, eventId, queueId);
            queueNumberMap.put("queueNumber", queueNumber);
            return ResponseEntity.ok().body(queueNumberMap);
        } catch (InvalidArgsException e){
            return ResponseEntity.unprocessableEntity().body("Invalid Request: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Server Error: " + e.getMessage());
        }
    }
}
    

