package com.vinta.entity.vo;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetPwdBodyVO {
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
