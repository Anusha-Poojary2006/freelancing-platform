package com.freelancing.freelancing_platform.dto;

import com.freelancing.freelancing_platform.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {

    private String name;
    private String email;
    private String password;
    private User.Role role;
}