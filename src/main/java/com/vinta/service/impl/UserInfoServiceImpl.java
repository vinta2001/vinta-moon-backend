package com.vinta.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vinta.constant.Constants;
import com.vinta.entity.po.UserInfo;
import com.vinta.entity.vo.ResultVO;
import com.vinta.entity.vo.request.LoginRequest;
import com.vinta.entity.vo.request.RegisterRequest;
import com.vinta.entity.vo.request.ResetPwdRequest;
import com.vinta.entity.vo.response.LoginResponse;
import com.vinta.enums.CodeEnum;
import com.vinta.enums.StatusCode;
import com.vinta.exception.BusinessException;
import com.vinta.service.UserInfoService;
import com.vinta.mapper.UserInfoMapper;
import com.vinta.utils.FileUtil;
import com.vinta.utils.VerifyCodeUtil;
import com.vinta.utils.JWTUtil;
import com.vinta.utils.RandomUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

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

    @Bean
    private QueryWrapper<UserInfo> queryWrapper() {
        return new QueryWrapper<>();
    }

    @Resource
    private VerifyCodeUtil verifyCodeUtil;

    @Override
    public int register(HttpSession session, RegisterRequest registerRequest) {

        String attribute = (String)session.getAttribute(Constants.EMAIL_CODE);
        if(attribute == null){
            throw new BusinessException(StatusCode.VERIFY_CODE_ERROR);
        }
        if (userInfoMapper.selectOne(queryWrapper().eq("email", registerRequest.getEmail())) != null) {
            return 0;
        }
        String emailCode = registerRequest.getEmailCode();
        String email = registerRequest.getEmail();
        UserInfo userInfo = userInfoMapper.selectOne(queryWrapper().eq("email", email));
        if (userInfo != null) {
            throw new BusinessException(StatusCode.USER_EXISTS);
        }
        boolean checkCode = verifyCodeUtil.checkCode(email, emailCode);
        if (!checkCode) {
            throw new BusinessException(StatusCode.CODE_EXPIRED);
        }
        String password = registerRequest.getPassword();
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
    public LoginResponse login(LoginRequest loginRequest, HttpServletResponse response) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        UserInfo userInfo = userInfoMapper.login(email);

        LoginResponse loginResponse = new LoginResponse();
        if (userInfo == null) {
            return null;
        }
        if (!password.equals(userInfo.getPassword())) {
            loginResponse.setId(userInfo.getUserId());
            return loginResponse;
        }
        String token = jwtUtil.createToken(userInfo.getUserId(), null);
        response.setHeader("Authorization", token);
        loginResponse.setId(userInfo.getUserId());
        loginResponse.setToken(token);
        loginResponse.setEmail(userInfo.getEmail());
        loginResponse.setUsername(userInfo.getUserName());
        return loginResponse;
    }

    @Override
    public int updatePassword(ResetPwdRequest resetPwdRequest) {
        String email = resetPwdRequest.getEmail();
        String newPassword = resetPwdRequest.getNewPassword();
        String oldPassword = resetPwdRequest.getOldPassword();
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
    public int resetPassword(ResetPwdRequest resetPwdRequest) {
        String email = resetPwdRequest.getEmail();
        String newPassword = resetPwdRequest.getNewPassword();
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
            String upload = FileUtil.upload(userId, file);
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
        File file = new File(userId);
        if (!file.exists()) {
            FileUtil.getDefaultProfile(response);
        }
        FileUtil.download(response, userId);
    }
}






