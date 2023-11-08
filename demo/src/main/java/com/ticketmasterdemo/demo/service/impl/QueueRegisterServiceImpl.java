package com.ticketmasterdemo.demo.service.impl;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ticketmasterdemo.demo.common.exception.InvalidArgsException;
import com.ticketmasterdemo.demo.common.exception.QueueRegisterException;
import com.ticketmasterdemo.demo.dto.Queue;
import com.ticketmasterdemo.demo.dto.User;
import com.ticketmasterdemo.demo.repository.EventRegisterRepository;
import com.ticketmasterdemo.demo.repository.QueueRegisterRepository;
import com.ticketmasterdemo.demo.repository.UserRepository;
import com.ticketmasterdemo.demo.service.QueueRegisterService;
import com.ticketmasterdemo.demo.util.Utility;

@Service
public class QueueRegisterServiceImpl implements QueueRegisterService {
    private final QueueRegisterRepository queueRegisterRepository;
    private final UserRepository userRepository;
    private final EventRegisterRepository eventRegisterRepository;

    public QueueRegisterServiceImpl(QueueRegisterRepository queueRegisterRepository, UserRepository userRepository, EventRegisterRepository eventRegisterRepository){
        this.queueRegisterRepository = queueRegisterRepository;
        this.userRepository = userRepository;
        this.eventRegisterRepository = eventRegisterRepository;
    }

    @Override
    public boolean processQueueChoices(String groupId, String eventId, String userId, List<String> selectedQueueIds, List<String> selectedShowIds) {
        System.out.println("groupId =" + groupId + ", eventId =" + eventId + ", queueIdList=" + selectedQueueIds  +  ", selectedShowIds =" + selectedShowIds + ", userId = " + userId);

        if (groupId == null || eventId == null || userId == null || selectedQueueIds == null || selectedQueueIds.isEmpty()){
            throw new InvalidArgsException("Unable to process queues as one or more methods are invalid");
        }

        // Check if user is valid and inside the group that they are running for
        boolean validUserInGroup = queueRegisterRepository.userInRegGroup(groupId, userId);
        if (!validUserInGroup) {
            throw new QueueRegisterException("User does not belong to the group that he/she is registering for.");
        }

        // Check if the group is able to queue, for instance if not all users has confirmed, we should throw an error message.
        boolean canGroupSelectQueues = queueRegisterRepository.groupCanSelectQueues(groupId, eventId);
        if (!canGroupSelectQueues) {
            throw new QueueRegisterException("RegGroup is unable to select queues");
        }

        // Check if queueID showID pairs are valid for this event.
        boolean areQueuesValid = this.validateQueueIds(selectedQueueIds, selectedShowIds, eventId);
        if (!areQueuesValid) {
            throw new QueueRegisterException("One or more queueIds are invalid");
        }
        
        // Retrieve maxQueueable for this event
        int maxQueueable = queueRegisterRepository.getMaxQueuableValue(eventId);
        
        int groupSize = queueRegisterRepository.getGroupSize(groupId);
        
        // Process only up to the point of maxqueueable OR the number of items inside the selectedQueueIds list
        for (int i = 0; i < Math.min(maxQueueable, selectedQueueIds.size()); ++i){
            int randomNumber = generateRandomNumberForGroup(groupSize);
            queueRegisterRepository.saveQueueSelection(groupId, selectedQueueIds.get(i), randomNumber, selectedShowIds.get(i), eventId);
        }
        
        

        return true;
    }

    @Override
    public List<Queue> getQueueInformation(String eventId){
        if (eventId == null){
            throw new InvalidArgsException("Queue Retrieval Error - eventId is null");
        }
        return queueRegisterRepository.retrieveAllQueueTimesAndShowTimes(eventId);
    }

    private boolean validateQueueIds(List<String> queueIdList, List<String> showIdList, String eventId){
        if (queueIdList.size() != showIdList.size()) return false; 

        Iterator<String> queueIter = queueIdList.iterator();
        Iterator<String> showIter = showIdList.iterator();
        while (queueIter.hasNext()){
            String queueId = queueIter.next();
            String showId = showIter.next();
            if ( queueRegisterRepository.validateQueueID(queueId, showId, eventId) ) continue; 

            return false; // invalid queueId, showId, eventId pair.
        }
        
        return true;
    }

    @Override
    public Integer getQueueNumber(String email, String eventId, String queueId) {
        Utility utility = new Utility();
        if (!utility.emailWhitelist(email) || !utility.isInputSafe(eventId) || !utility.isInputSafe(queueId) ) {
            throw new InvalidArgsException("invalid login credentials - failed preliminary check");
        }
        User user = userRepository.findVerifiedUserByEmail(email);
        String groupId = eventRegisterRepository.getRegistrationGroupId(user.getId(), eventId);
        String groupLeaderEmail = eventRegisterRepository.getRegistrationGroupLeader(groupId);
        if (!user.getId().equals(groupLeaderEmail)) {
            throw new RuntimeException("User not group leader");
        }
        

        Integer sQueueNumber = queueRegisterRepository.getQueueNumber(queueId, groupId);
        
        Integer queueFactor = queueRegisterRepository.getQueueFactor(queueId);
        
        Integer queueNumber = sQueueNumber - queueFactor;
        
        return queueNumber;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateQueueFactor(String queueId) {
        Utility utility = new Utility();
        if (!utility.isInputSafe(queueId) ) {
            throw new InvalidArgsException("invalid login credentials - failed preliminary check");
        }
        queueRegisterRepository.updateQueueFactor(queueId);
        return true;
    }
    
    /**
     * For a groupsize of k, we simulate fairness of multiple people queueing 
     * together by running the number generator k times and picking the smallest
     * number generated.
     * @param groupSize
     * @return
     */
    private int generateRandomNumberForGroup(int groupSize){
        Random randomNumGen = new Random();
        int randomNo = Integer.MAX_VALUE; 
        for (int i = 0; i < groupSize; ++i){
            randomNo = Math.min(randomNo, randomNumGen.nextInt());
        }
        return randomNo;
    }
}
