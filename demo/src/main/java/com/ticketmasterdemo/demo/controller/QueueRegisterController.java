package com.ticketmasterdemo.demo.controller;

import java.sql.SQLIntegrityConstraintViolationException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ticketmasterdemo.demo.common.exception.InvalidArgsException;
import com.ticketmasterdemo.demo.common.exception.QueueRegisterException;
import com.ticketmasterdemo.demo.dto.QueueSelection;
import com.ticketmasterdemo.demo.service.QueueRegisterService;

@RestController
@CrossOrigin
@RequestMapping("queues")
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
}
