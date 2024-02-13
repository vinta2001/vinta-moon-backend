package com.vinta.entity.vo.request;


import lombok.Data;

@Data
public class ResetPwdRequest {
    private String email;
    private String newPassword;
    private String oldPassword;
    private String emailCode;
}
