package com.vinta.entity.dto;


import lombok.Data;

@Data
public class LoginResultDTO {
    private String id;
    private String username;
    private String email;
    private String token;
}
