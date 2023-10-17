package com.ticketmasterdemo.demo.service;

import java.util.List;

import com.ticketmasterdemo.demo.dto.Queue;

public interface QueueRegisterService {
    boolean processQueueChoices(String groupId, String eventId, String userId, List<String> selectedQueueIds, List<String> selectedShowIds);

    List<Queue> getQueueInformation(String eventId);
}
