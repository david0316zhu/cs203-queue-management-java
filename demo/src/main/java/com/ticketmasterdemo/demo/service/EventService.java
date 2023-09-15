package com.ticketmasterdemo.demo.service;

import java.util.List;

import com.ticketmasterdemo.demo.dto.Event;

public interface EventService {
    public List<Event> getAllEvents();
    public Event getEvent(String eventId);
}
