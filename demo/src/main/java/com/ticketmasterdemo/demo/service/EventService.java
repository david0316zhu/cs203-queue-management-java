package com.ticketmasterdemo.demo.service;

import java.util.List;

import com.ticketmasterdemo.demo.dto.Event;
import com.ticketmasterdemo.demo.dto.SeatCategoryInfo;
import com.ticketmasterdemo.demo.dto.Show;

public interface EventService {
    public List<Event> getAllEvents(boolean onlyHighlighted);
    public Event getEvent(String eventId);
    public List<Show> getAllShowsForSpecificEvent(String eventId);
    public List<SeatCategoryInfo> getSeatCategoryInfos(String eventId, String showId);
}
