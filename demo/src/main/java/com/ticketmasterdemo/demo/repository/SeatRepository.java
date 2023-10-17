package com.ticketmasterdemo.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ticketmasterdemo.demo.dto.Event;
import com.ticketmasterdemo.demo.dto.Queue;
import com.ticketmasterdemo.demo.dto.Seat;
import com.ticketmasterdemo.demo.dto.SeatCategoryInfo;
import com.ticketmasterdemo.demo.dto.Show;

@Mapper
public interface SeatRepository {
    List<SeatCategoryInfo> getSeatCategoryInfos(@Param("event_id") String eventId, @Param("show_id") String show_id);
    List<Seat> getSeatsInCategoryForSpecificShow(@Param("event_id") String eventId, @Param("show_id") String showId, @Param("category_id") int categoryId);
    int saveSeatCategorySelection(@Param("event_id") String eventId, @Param("show_id") String showId, @Param("category_id") int categoryId, @Param("seat_id") int seatId, @Param("user_id") String userId);
    boolean userHasSeatForSpecificShow(@Param("event_id") String eventId, @Param("show_id") String showId, @Param("user_id") String userId);
}
