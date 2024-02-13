package com.vinta.entity.vo.response;


import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private String id;
    private String username;
    private String email;
}
