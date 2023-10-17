package com.ticketmasterdemo.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ticketmasterdemo.demo.dto.Queue;

@Mapper
public interface QueueRegisterRepository {
    void saveQueueSelection(@Param("group_id") String groupId, @Param("queue_id") String queueId,
            @Param("rand_no") int randomNum, @Param("show_id") String showId, @Param("event_id") String eventID);

    boolean groupCanSelectQueues(@Param("group_id") String groupId, @Param("event_id") String eventId);

    boolean userInRegGroup(@Param("group_id") String groupId, @Param("user_id") String userId);

    boolean validateQueueID(@Param("queue_id") String queueId, @Param("show_id") String showId,
            @Param("event_id") String eventId);

    int getMaxQueuableValue(@Param("event_id") String eventId);

    int getGroupSize(@Param("group_id") String groupId);
    
    List<Queue> retrieveAllQueueTimesAndShowTimes(@Param("event_id") String eventId);

}
