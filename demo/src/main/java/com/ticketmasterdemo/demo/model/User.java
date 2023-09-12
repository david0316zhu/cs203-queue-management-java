package com.ticketmasterdemo.demo.model;

public class User {
    private String id;
    private String mobile;
    private String email;
    private String password;
    private boolean authenticated;

    public User(){}

    public User(String email, String mobile, String password) {
        this.mobile = mobile;
        this.email = email;
        this.password = password;
    }

    public User(String email) {
        this.email = email;
    }

    public User(String id, String email) {
        this.id = id;
        this.email = email;
    }

    public User(String id, String email, boolean authenticated) {
        this.id = id;
        this.email = email;
        this.authenticated = authenticated;

    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getAuthenticated() {
        return authenticated;
    }
    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    
    

    
    
}
