package com.vinta.entity.dto;


import com.vinta.enums.StatusCode;
import lombok.Data;

@Data
public class ResultDTO<T> {

    private Integer code;
    private String message;
    private T data;

    public ResultDTO() {
    }

    protected static <T> ResultDTO<T> build(T data) {
        ResultDTO<T> result = new ResultDTO<>();
        if (data != null) {
            result.setData(data);
        }
        return result;
    }

    public static <T> ResultDTO<T> build(T data, Integer code, String message) {
        ResultDTO<T> result = build(data);
        result.setData(data);
        result.setMessage(message);
        result.setCode(code);
        return result;
    }

    public static <T> ResultDTO<T> build(T data, StatusCode statusCode) {
        ResultDTO<T> result = build(data);
        result.setData(data);
        result.setMessage(statusCode.getMessage());
        result.setCode(statusCode.getCode());
        return result;
    }

    public static <T> ResultDTO<T> ok(T data) {
        return build(data, StatusCode.OK);
    }

    public static <T> ResultDTO<T> success(StatusCode statusCode) {
        return build(null, statusCode);
    }

    public static <T> ResultDTO<T> failed(StatusCode statusCode) {
        return build(null, statusCode);
    }

    public static <T> ResultDTO<T> failed(String message) {
        return failed(404, message);
    }

    public static <T> ResultDTO<T> failed(Integer code, String message) {
        return build(null, code, message);
    }
}
