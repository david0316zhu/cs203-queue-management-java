package com.ticketmasterdemo.demo.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface QueueRegisterRepository {
    void saveQueueSelection(@Param("group_id") String groupId, @Param("queue_id") String queueId, @Param("rand_no") String randomNum, @Param("show_id") String showId, @Param("event_id") String eventID);
}
