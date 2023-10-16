package com.ticketmasterdemo.demo.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ticketmasterdemo.demo.common.exception.EventException;
import com.ticketmasterdemo.demo.common.exception.InvalidArgsException;
import com.ticketmasterdemo.demo.dto.Event;
import com.ticketmasterdemo.demo.dto.Queue;
import com.ticketmasterdemo.demo.dto.Show;
import com.ticketmasterdemo.demo.dto.SeatCategoryInfo;
import com.ticketmasterdemo.demo.repository.EventRepository;
import com.ticketmasterdemo.demo.repository.SeatRepository;
import com.ticketmasterdemo.demo.service.EventService;
import com.ticketmasterdemo.demo.util.Utility;

@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final SeatRepository seatRepository;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository, SeatRepository seatRepository) {
        this.eventRepository = eventRepository;
        this.seatRepository = seatRepository;
    }

    @Override
    public List<Event> getAllEvents(boolean onlyHighlighted) {
        List<Event> eventsList = null;
        if (onlyHighlighted) {

            eventsList = eventRepository.retrieveHighlightedEventInfo();

            // Update the eventsList with countries.
            for (Event event : eventsList) {
                List<String> countries = eventRepository.retrieveAllCountriesForSpecificEvent(event.getId());
                event.setCountries(countries);
            }
        } else {
            eventsList = eventRepository.retrieveAllEvents();
        }
        return eventsList;
    }

    @Override
    public Event getEvent(String eventId) {
        Utility utility = new Utility();
        if (!utility.isInputSafe(eventId)) {
            throw new InvalidArgsException("Invalid Request");
        }
        Event event = eventRepository.retrieveEvent(eventId);
        if (event == null) {
            throw new EventException("Event does not exist");
        }
        return event;
    }

    @Override
    public List<Show> getAllShowsForSpecificEvent(String eventId) {
        Utility utility = new Utility();
        if (!utility.isInputSafe(eventId)) {
            throw new InvalidArgsException("Invalid Request");
        }
        List<Show> allShowsForEvent = eventRepository.retrieveAllShowsForSpecificEvent(eventId);
        List<Queue> allQueuesForEvent = eventRepository.retrieveAllQueuesForSpecificEvent(eventId);

        Map<String, List<Queue>> groupedByShowId = allQueuesForEvent.stream()
                .collect(Collectors.groupingBy(Queue::getShowId));

        for (Show show : allShowsForEvent) {
            show.setQueues(groupedByShowId.get(show.getId()));
        }

        return allShowsForEvent;
    }

    @Override
    public List<SeatCategoryInfo> getSeatCategoryInfos(String eventId, String showId) {
        Utility utility = new Utility();
        if (!utility.isInputSafe(eventId) || !utility.isInputSafe(showId)) {
            throw new InvalidArgsException("Invalid Request");
        }
        List<SeatCategoryInfo> seatCategoryInfos = seatRepository.getSeatCategoryInfos(eventId, showId);
        if (seatCategoryInfos == null || seatCategoryInfos.size() < 1) {
            throw new EventException("Invalid Event ID / Show ID");
        }
        return seatCategoryInfos;
    }
}
