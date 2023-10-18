package com.ticketmasterdemo.demo.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VerificationEmail implements Serializable {
    private String email;
    private String verificationUrl;
}
