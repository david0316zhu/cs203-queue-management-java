package com.ticketmasterdemo.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ticketmasterdemo.demo.repository.QueueRegisterRepository;
import com.ticketmasterdemo.demo.service.QueueRegisterService;

@Service
public class QueueRegisterServiceImpl implements QueueRegisterService {
    private final QueueRegisterRepository queueRegisterRepository;

    public QueueRegisterServiceImpl(QueueRegisterRepository queueRegisterRepository){
        this.queueRegisterRepository = queueRegisterRepository;
    }

    @Override
    public boolean processQueueChoices(String groupId, String eventId, List<String> selectedQueueIds) {
        // TODO Auto-generated method stub
        return false;
    }
    
}
