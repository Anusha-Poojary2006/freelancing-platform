package com.freelancing.freelancing_platform.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    private String email;
    private String password;
}