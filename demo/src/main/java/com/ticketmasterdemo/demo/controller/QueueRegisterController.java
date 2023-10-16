package com.ticketmasterdemo.demo.controller;

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

    public QueueRegisterController(QueueRegisterService queueRegisterService){
        this.queueRegisterService = queueRegisterService;
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> registerForQueues(@RequestBody QueueSelection regInfo){
        try {
            return ResponseEntity.ok().body(true);
        } catch (InvalidArgsException e){
            return ResponseEntity.unprocessableEntity().body("Invalid request " + e.getMessage());

        } catch (QueueRegisterException e){
            return ResponseEntity.status(406).body("Queue Register Error: " + e.getMessage());

        } catch (Exception e){
            return ResponseEntity.status(500).body("Server Error: " + e.getMessage());
        }

    }
    
}
