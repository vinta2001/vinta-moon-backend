package com.vinta.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vinta.constant.Constants;
import com.vinta.entity.po.UserInfo;
import com.vinta.entity.vo.LoginBodyVO;
import com.vinta.entity.vo.RegisterBodyVO;
import com.vinta.entity.vo.ResetPwdBodyVO;
import com.vinta.entity.dto.LoginResultDTO;
import com.vinta.enums.StatusCode;
import com.vinta.exception.BusinessException;
import com.vinta.service.UserInfoService;
import com.vinta.mapper.UserInfoMapper;
import com.vinta.utils.FileComponent;
import com.vinta.utils.VerifyCodeUtil;
import com.vinta.utils.JWTUtil;
import com.vinta.utils.RandomUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author VINTA
 * @description 针对表【user_info】的数据库操作Service实现
 * @createDate 2024-02-06 14:23:00
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
        implements UserInfoService {

    @Resource
    private JWTUtil jwtUtil;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private FileComponent fileComponent;

    @Bean
    private QueryWrapper<UserInfo> queryWrapper() {
        return new QueryWrapper<>();
    }

    @Resource
    private VerifyCodeUtil verifyCodeUtil;

    @Override
    public int register(HttpSession session, RegisterBodyVO registerBodyVO) {

        String attribute = (String)session.getAttribute(Constants.EMAIL_CODE);
        if(attribute == null){
            throw new BusinessException(StatusCode.VERIFY_CODE_ERROR);
        }
        String emailCode = registerBodyVO.getEmailCode();
        String email = registerBodyVO.getEmail();
        UserInfo userInfo = userInfoMapper.selectOne(queryWrapper().eq("email", email));
        if (userInfo != null) {
            throw new BusinessException(StatusCode.USER_EXISTS);
        }
        boolean checkCode = verifyCodeUtil.checkCode(email, emailCode);
        if (!checkCode) {
            throw new BusinessException(StatusCode.CODE_EXPIRED);
        }
        String password = registerBodyVO.getPassword();
        userInfo = new UserInfo();
        String userId = RandomUtil.getUUID();
        String username = RandomUtil.getRandomUsername();
        userInfo.setUserId(userId);
        userInfo.setUserNickname(RandomUtil.getRandomNickName());
        userInfo.setUserName(username);
        userInfo.setEmail(email);
        userInfo.setPassword(password);
        return userInfoMapper.insert(userInfo);
    }

    @Override
    public LoginResultDTO login(LoginBodyVO loginBodyVO, HttpServletResponse response) {
        String email = loginBodyVO.getEmail();
        String password = loginBodyVO.getPassword();
        UserInfo userInfo = userInfoMapper.login(email);

        LoginResultDTO loginResultDTO = new LoginResultDTO();
        if (userInfo == null) {
            throw new BusinessException(StatusCode.USERNAME_NOT_EXISTS);
        }
        if (!password.equals(userInfo.getPassword())) {
            throw new BusinessException(StatusCode.PASSWORD_ERROR);
        }
        String token = jwtUtil.createToken(userInfo.getUserId(), null);
        response.setHeader("Authorization", token);
        loginResultDTO.setId(userInfo.getUserId());
        loginResultDTO.setEmail(userInfo.getEmail());
        loginResultDTO.setUsername(userInfo.getUserName());
        return loginResultDTO;
    }

    @Override
    public int updatePassword(ResetPwdBodyVO resetPwdBodyVO) {
        String email = resetPwdBodyVO.getEmail();
        String newPassword = resetPwdBodyVO.getNewPassword();
        String oldPassword = resetPwdBodyVO.getOldPassword();
        UserInfo userInfo = userInfoMapper.selectOne(queryWrapper().eq("email", email));
        if (userInfo == null) {
            throw new BusinessException(StatusCode.USERNAME_NOT_EXISTS);
        }
        if (!oldPassword.equals(userInfo.getPassword())) {
            throw new BusinessException(StatusCode.PASSWORD_ERROR);
        }
        userInfo.setPassword(newPassword);
        return userInfoMapper.updateById(userInfo);
    }


    @Override
    public UserInfo getUserByToken(String token) {
        String userId = jwtUtil.getUserId(token);
        return userInfoMapper.selectOne(queryWrapper().eq("user_id", userId));
    }

    @Override
    public int resetPassword(ResetPwdBodyVO resetPwdBodyVO) {
        String email = resetPwdBodyVO.getEmail();
        String newPassword = resetPwdBodyVO.getNewPassword();
        UserInfo userInfo = userInfoMapper.selectOne(queryWrapper().eq("email", email));
        if (userInfo == null) {
            throw new BusinessException(StatusCode.USERNAME_NOT_EXISTS);
        }
        userInfo.setPassword(newPassword);
        return userInfoMapper.updateById(userInfo);
    }

    @Override
    public int uploadProfile(String token, MultipartFile file) {
        if (token == null) {
            throw new BusinessException(StatusCode.TOKEN_ERROR);
        }
        try {
            UserInfo userInfo = getUserByToken(token);
            String userId = userInfo.getUserId();
            String upload = fileComponent.upload(userId, file);
            userInfo.setProfile(upload);
            return userInfoMapper.updateById(userInfo);
        } catch (Exception e) {
            throw new BusinessException(StatusCode.UPLOAD_ERROR);
        }
    }

    @Override
    public int updateUserInfo(UserInfo userInfo) {
        return userInfoMapper.updateById(userInfo);
    }

    @Override
    public void downloadProfile(String userId, HttpServletResponse response) {
        if (userId == null) {
            throw new BusinessException(StatusCode.BAD_REQUEST);
        }
        fileComponent.getProfile(response, userId);
    }

    @Override
    public UserInfo getUserByUserId(String id) {
        return userInfoMapper.selectByUserId(id);
    }
}






