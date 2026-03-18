package com.ecommerce.sb_ecommerce.security.Response;

import lombok.Data;

import java.util.List;

@Data
public class UserInfoResponse {
    private Long userId;
    private String jwtToken;
    private String username;
    private List<String> roles;

    public UserInfoResponse(Long userId, String username, List<String> roles, String jwtToken) {
        this.userId = userId;
        this.username = username;
        this.roles = roles;
        this.jwtToken = jwtToken;
    }


}


