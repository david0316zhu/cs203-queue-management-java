package com.ticketmasterdemo.demo.model;
import java.util.List;

public class Registration {
    private String id;
    private User user;
    private int groupSize;
    private String queueId;
    private List<User> userGroup;

    public Registration(String queueId, List<User> userGroup, User user) {
        this.queueId = queueId;
        this.user = user;
        this.userGroup = userGroup;
        this.groupSize = userGroup.size();
    }

    public void setUserGroup(List<User> userGroup) {
        this.userGroup = userGroup;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<User> getUserGroup() {
        return userGroup;
    }

    public String getId() {
        return id;
    }

    public int getGroupSize() {
        return groupSize;
    }

    public String getQueueId() {
        return queueId;
    }

    public User getUser() {
        return user;
    }

}
