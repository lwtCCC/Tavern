package com.lwt.tavern.entry.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginDTO {
    private String username;
    private String phone;
    private String password;
    private String code;
}
