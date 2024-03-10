package com.vinta.exception;


import com.vinta.enums.StatusCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final Integer code;
    private final String message;
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BusinessException(StatusCode statusCode){
        super(statusCode.getMessage());
        this.code = statusCode.getCode();
        this.message = statusCode.getMessage();
    }

    public BusinessException(String message) {
        super(message);
        this.code = StatusCode.BAD_REQUEST.getCode();
        this.message = message;
    }
}
