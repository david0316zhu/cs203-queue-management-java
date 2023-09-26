package com.ticketmasterdemo.demo.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ticketmasterdemo.demo.common.exception.EventException;
import com.ticketmasterdemo.demo.dto.Event;
import com.ticketmasterdemo.demo.dto.Queue;
import com.ticketmasterdemo.demo.dto.Show;
import com.ticketmasterdemo.demo.repository.EventRepository;
import com.ticketmasterdemo.demo.service.EventService;


@Service
public class EventServiceImpl implements EventService{
    private final EventRepository eventRepository;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }
    
    @Override
    public List<Event> getAllEvents(boolean onlyHighlighted) {
        List<Event> eventsList = null;
        if(onlyHighlighted){

            eventsList = eventRepository.retrieveHighlightedEventInfo();

            // Update the eventsList with countries.
            for (Event event: eventsList){
                List<String> countries = eventRepository.retrieveAllCountriesForSpecificEvent(event.getId());
                event.setCountries(countries);
            }
        } else {
            eventsList = eventRepository.retrieveAllEvents();
        }
        return eventsList;
    }

    @Override
    public Event getEvent(String eventId){
        Event event = eventRepository.retrieveEvent(eventId);
        if (event == null) {
            throw new EventException("Event does not exist");
        }
        return event;
    }

    @Override
    public List<Show> getAllShowsForSpecificEvent(String eventId) {
        List<Show> allShowsForEvent = eventRepository.retrieveAllShowsForSpecificEvent(eventId);
        List<Queue> allQueuesForEvent = eventRepository.retrieveAllQueuesForSpecificEvent(eventId);
        
        Map<String, List<Queue>> groupedByShowId = allQueuesForEvent.stream()
            .collect(Collectors.groupingBy(Queue::getShowId));
        
        for (Show show : allShowsForEvent) {
            show.setQueues(groupedByShowId.get(show.getId()));
        }

        return allShowsForEvent;
    }

    
}
