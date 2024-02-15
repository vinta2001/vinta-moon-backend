package com.vinta.enums;

import lombok.Getter;

@Getter
public enum StatusCode {

    OK(200, "success"),
    EMAIL_CODE_SEND_SUCCESS(200, "验证码发送成功"),
    CREATE_SUCCESS(200, "注册成功"),
    UPDATE_SUCCESS(200,"更新成功"),
    LOGOUT_SUCCESS(200, "退出成功"),
    UPLOAD_SUCCESS(200, "上传成功"),

    UPDATE_ERROR(201,"更新失败"),
    UPLOAD_ERROR(201,"上传失败"),

    USERNAME_NOT_EXISTS(202, "用户不存在"),
    USER_EXISTS(202, "用户已存在"),

    PASSWORD_ERROR(203, "密码错误"),
    VERIFY_CODE_ERROR(203, "验证码错误"),
    EMAIL_ERROR(203, "邮箱错误"),
    BAD_REQUEST(203, "请求错误"),

    CODE_EXPIRED(204, "验证码已过期"),
    TOKEN_ERROR(204,"用户token失效"),

    NOT_FOUND(404, "Not Found"),
    METHOD_NOT_ALLOWED(402,"Method Not Allowed"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error");
    private final int code;
    private final String message;

    StatusCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
