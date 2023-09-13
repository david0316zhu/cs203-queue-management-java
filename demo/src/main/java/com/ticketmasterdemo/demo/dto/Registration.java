package com.ticketmasterdemo.demo.dto;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Registration {
    private String id;
    private String eventId;
    private User groupLeader;
    private int groupSize;
    private List<User> userGroup;

    public Registration(List<User> userGroup, User groupLeader, String eventId) {
        this.groupLeader = groupLeader;
        this.userGroup = userGroup;
        this.eventId = eventId;
        this.groupSize = userGroup.size();
    }

}
