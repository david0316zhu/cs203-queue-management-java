package com.ticketmasterdemo.demo.dto;

import java.util.List;

import lombok.AllArgsConstructor;
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
    private List<String> showIdList; // showIdList[0] corresponds to queueIdList[0], showIdlist[1] corresponds to queueIdList[1] and so on.
}
