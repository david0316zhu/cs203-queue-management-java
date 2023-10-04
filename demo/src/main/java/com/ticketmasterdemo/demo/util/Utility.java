package com.ticketmasterdemo.demo.util;

import java.util.UUID;


public class Utility {
    public String generateRandomUUID() {
        UUID randomUUID = UUID.randomUUID();
        return randomUUID.toString();
    }

    public String generateUserId() {
        UUID randomUUID = UUID.randomUUID();
        return randomUUID.toString();
    }

    public String generateEmailVerificationToken() {
        UUID randomUUID = UUID.randomUUID();
        return randomUUID.toString();
    }

}
