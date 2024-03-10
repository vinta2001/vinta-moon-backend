package com.vinta.service;

import com.vinta.entity.po.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vinta.entity.vo.LoginBodyVO;
import com.vinta.entity.vo.RegisterBodyVO;
import com.vinta.entity.vo.ResetPwdBodyVO;
import com.vinta.entity.dto.LoginResultDTO;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author VINTA
 * @description 针对表【user_info】的数据库操作Service
 * @createDate 2024-02-06 14:23:00
 */
public interface UserInfoService extends IService<UserInfo> {

    int register(HttpSession session, RegisterBodyVO registerBodyVO);

    LoginResultDTO login(LoginBodyVO loginBodyVO, HttpServletResponse response);

    int updatePassword(ResetPwdBodyVO resetPwdBodyVO);

    UserInfo getUserByToken(String token);

    int resetPassword(ResetPwdBodyVO resetPwdBodyVO);

    int uploadAvatar(String token, MultipartFile file);


    void downloadAvatar(String userId, HttpServletResponse response);

    UserInfo getUserByUserId(String id);
}
