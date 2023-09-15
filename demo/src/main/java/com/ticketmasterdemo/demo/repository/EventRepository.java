package com.ticketmasterdemo.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ticketmasterdemo.demo.dto.Event;

@Mapper
public interface EventRepository {
    List<Event> retrieveAllEvents();
    Event retrieveEvent(@Param("event_id") String eventId);
}
