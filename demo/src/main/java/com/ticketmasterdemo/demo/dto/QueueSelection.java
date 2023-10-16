package com.ticketmasterdemo.demo.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QueueSelection {
    private String groupId;
    private String userId;
    private String eventId;
    private List<String> queueIdList;
    
}
