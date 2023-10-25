package com.ticketmasterdemo.demo.dto;

import java.text.SimpleDateFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentInfo {
    private String userId;
    private String cardNumber;
    private String expDate;
    private String name;
    private String street;
    private String city;
    private String state;
    private String zip;
    private String email;
    private String mobile;
}
