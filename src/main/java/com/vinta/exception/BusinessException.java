package com.vinta.exception;


import com.vinta.enums.StatusCode;
import lombok.Data;

@Data
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
}
