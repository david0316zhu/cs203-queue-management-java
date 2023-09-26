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
public class RegistrationInfo {
    String groupId;
    List<UserInfo> userInfoList;
    Boolean hasAllUsersConfirmed;
}
