package com.chunlei.bili.auth.dto;

import lombok.Data;

@Data
public class LoginFormDTO {
    private String username;
    private String code;
    private String password;
}
