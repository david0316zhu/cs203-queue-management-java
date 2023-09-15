package com.ticketmasterdemo.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.ticketmasterdemo.demo.common.exception.EventException;
import com.ticketmasterdemo.demo.dto.Event;
import com.ticketmasterdemo.demo.repository.EventRepository;
import com.ticketmasterdemo.demo.service.EventService;

public class EventServiceImpl implements EventService{
    private final EventRepository eventRepository;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }
    
    @Override
    public List<Event> getAllEvents() {
        List<Event> allEvents = eventRepository.retrieveAllEvents();
        return allEvents;
    }

    @Override
    public Event getEvent(String eventId){
        Event event = eventRepository.retrieveEvent(eventId);
        if (event == null) {
            throw new EventException("Event does not exist");
        }
        return event;
    }

    
}
