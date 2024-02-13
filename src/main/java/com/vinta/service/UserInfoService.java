package com.vinta.service;

import com.vinta.entity.po.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vinta.entity.vo.request.LoginRequest;
import com.vinta.entity.vo.request.RegisterRequest;
import com.vinta.entity.vo.request.ResetPwdRequest;
import com.vinta.entity.vo.response.LoginResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * @author VINTA
 * @description 针对表【user_info】的数据库操作Service
 * @createDate 2024-02-06 14:23:00
 */
public interface UserInfoService extends IService<UserInfo> {

    int register(HttpSession session,RegisterRequest registerRequest);

    LoginResponse login(LoginRequest loginRequest, HttpServletResponse response);

    int updatePassword(ResetPwdRequest resetPwdRequest);

    UserInfo getUserByToken(String token);

    int resetPassword(ResetPwdRequest resetPwdRequest);
}
