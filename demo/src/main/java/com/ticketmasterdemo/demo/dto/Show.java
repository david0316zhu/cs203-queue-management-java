package com.ticketmasterdemo.demo.dto;

import java.security.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Show {
    private String id;
    private Event event;
    private Timestamp dateTime;
    private String locationId;
    private String venueId;

}
