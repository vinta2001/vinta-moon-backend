package com.vinta.exception;


import com.vinta.entity.dto.ResultDTO;
import com.vinta.enums.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(Exception.class)
    public ResultDTO handler(Exception e) { // ResultVO<T>
        log.error(e.getMessage(), e);
        return ResultDTO.failed(StatusCode.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpMessageConversionException.class)
    public ResultDTO handler(HttpMessageConversionException e) {
        log.error(e.getMessage(), e);
        return ResultDTO.failed(StatusCode.BAD_REQUEST);
    }

    @ExceptionHandler(BusinessException.class)
    public ResultDTO handler(BusinessException e) {
        log.error(e.getMessage(), e);
        return ResultDTO.failed(e.getCode(),e.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResultDTO handler(HttpRequestMethodNotSupportedException e) {
        log.error(e.getMessage(), e);
        return ResultDTO.failed(StatusCode.METHOD_NOT_ALLOWED);
    }
}
