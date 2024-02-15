package com.vinta.entity.vo.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetPwdRequest {
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String newPassword;
    @NotBlank
    private String oldPassword;
    @NotBlank
    private String emailCode;
}
