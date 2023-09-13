package com.ticketmasterdemo.demo.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    private String id;
    private String mobile;
    private String email;
    private String password;
    private String authenticatorId;
    private boolean verified;

    public User(String email) {
        this.email = email;
    }

    public User(String id, String email, boolean verified) {
        this.id = id;
        this.email = email;
        this.verified = verified;

    }

}
