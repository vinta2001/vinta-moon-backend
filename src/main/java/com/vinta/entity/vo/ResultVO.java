package com.vinta.entity.vo;


import com.vinta.enums.StatusCode;
import lombok.Data;

@Data
public class ResultVO<T> {

    private Integer code;
    private String message;
    private T data;

    public ResultVO() {
    }

    protected static <T> ResultVO<T> build(T data) {
        ResultVO<T> result = new ResultVO<>();
        if (data != null) {
            result.setData(data);
        }
        return result;
    }

    public static <T> ResultVO<T> build(T data, Integer code, String message) {
        ResultVO<T> result = build(data);
        result.setData(data);
        result.setMessage(message);
        result.setCode(code);
        return result;
    }

    public static <T> ResultVO<T> build(T data, StatusCode statusCode) {
        ResultVO<T> result = build(data);
        result.setData(data);
        result.setMessage(statusCode.getMessage());
        result.setCode(statusCode.getCode());
        return result;
    }

    public static <T> ResultVO<T> ok(T data) {
        return build(data, StatusCode.OK);
    }

    public static <T> ResultVO<T> success(StatusCode statusCode) {
        return build(null, statusCode);
    }

    public static <T> ResultVO<T> failed(StatusCode statusCode) {
        return build(null, statusCode);
    }

    public static <T> ResultVO<T> failed(Integer code, String message) {
        return build(null, code, message);
    }
}
