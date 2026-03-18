package com.ecommerce.sb_ecommerce.security.Request;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
