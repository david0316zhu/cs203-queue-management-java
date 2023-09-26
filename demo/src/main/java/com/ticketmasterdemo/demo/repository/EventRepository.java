package com.ticketmasterdemo.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ticketmasterdemo.demo.dto.Event;
import com.ticketmasterdemo.demo.dto.Queue;
import com.ticketmasterdemo.demo.dto.Show;

@Mapper
public interface EventRepository {
    List<Event> retrieveAllEvents();
    Event retrieveEvent(@Param("event_id") String eventId);
    List<Show> retrieveAllShowsForSpecificEvent(@Param("event_id") String eventId);
    List<Queue> retrieveAllQueuesForSpecificEvent(@Param("event_id") String eventId);
}
