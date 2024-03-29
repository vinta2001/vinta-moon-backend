package com.vinta.interceptor;

import com.vinta.annotation.UserLoginRequired;
import com.vinta.component.ThreadLocalComponent;
import com.vinta.enums.StatusCode;
import com.vinta.exception.BusinessException;
import com.vinta.utils.JWTUtil;
import com.vinta.utils.StringUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;

public class AuthenticationInterceptor implements HandlerInterceptor {
    @Resource
    private JWTUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("Authorization");
        if(token != null){
            ThreadLocalComponent.set(jwtUtil.getUserId(token));
        }
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        if(method.isAnnotationPresent(UserLoginRequired.class)){
            UserLoginRequired annotation = method.getAnnotation(UserLoginRequired.class);
            if(annotation.required()){
                if(StringUtil.isEmpty(token)){
                 throw new BusinessException(StatusCode.TOKEN_ERROR);
                }
                String userId = jwtUtil.getUserId(token);
                if(StringUtil.isEmpty(userId)){
                    throw new BusinessException(StatusCode.TOKEN_ERROR);
                }
                boolean validateToken = jwtUtil.validateToken(token);
                if(!validateToken){
                    throw new BusinessException(StatusCode.TOKEN_ERROR);
                }
                return true;
            }
        }
        return true;
    }
}
