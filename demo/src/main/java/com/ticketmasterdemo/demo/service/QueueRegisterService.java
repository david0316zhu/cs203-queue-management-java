package com.ticketmasterdemo.demo.service;

import java.util.List;

public interface QueueRegisterService {
    boolean processQueueChoices(String groupId, String eventId, String userId, List<String> selectedQueueIds, List<String> selectedShowIds);
}
