package com.vinta.exception;


import com.vinta.entity.vo.ResultVO;
import com.vinta.enums.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(Exception.class)
    public ResultVO handler(Exception e) { // ResultVO<T>
        log.error(e.getMessage(), e);
        return ResultVO.failed(StatusCode.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpMessageConversionException.class)
    public ResultVO handler(HttpMessageConversionException e) {
        log.error(e.getMessage(), e);
        return ResultVO.failed(StatusCode.BAD_REQUEST);
    }

    @ExceptionHandler(BusinessException.class)
    public ResultVO handler(BusinessException e) {
        log.error(e.getMessage(), e);
        return ResultVO.failed(e.getCode(),e.getMessage());
    }
}
