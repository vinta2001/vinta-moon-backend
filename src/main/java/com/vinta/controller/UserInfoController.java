package com.vinta.controller;

import com.vinta.constant.Constants;
import com.vinta.entity.po.UserInfo;
import com.vinta.entity.vo.request.LoginRequest;
import com.vinta.entity.vo.request.RegisterRequest;
import com.vinta.entity.vo.ResultVO;
import com.vinta.entity.vo.request.ResetPwdRequest;
import com.vinta.entity.vo.response.LoginResponse;
import com.vinta.enums.StatusCode;
import com.vinta.exception.BusinessException;
import com.vinta.service.UserInfoService;
import com.vinta.utils.FileUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("user")
@Tag(name = "用户信息")
@Slf4j
public class UserInfoController {
    @Resource
    private UserInfoService userInfoService;

    @Operation(summary = "用户登录")
    @Parameter(name = "loginRequest", description = "登录信息", required = true)
    @PostMapping("/login")
    public ResultVO login(HttpSession session,
                          @RequestBody LoginRequest loginRequest,
                          HttpServletResponse response) {
        LoginResponse userInfo = userInfoService.login(loginRequest,response);

        if (userInfo == null) {
            return ResultVO.failed(StatusCode.USERNAME_NOT_EXISTS);
        }
        if (userInfo.getToken() == null) {
            return ResultVO.failed(StatusCode.PASSWORD_ERROR);
        }
        session.setAttribute(Constants.USER_TOKEN_KEY,userInfo.getToken());
        return ResultVO.ok(userInfo);
    }

    @PostMapping("/register")
    @Operation(summary = "用户注册")
    @Parameter(name = "registerRequest", description = "注册信息", required = true)
    public ResultVO register(HttpSession session, @RequestBody RegisterRequest registerRequest) {
        int register = userInfoService.register(session, registerRequest);
        return register == 1 ? ResultVO.success(StatusCode.CREATE_SUCCESS) : ResultVO.failed(StatusCode.USER_EXISTS);
    }

    @GetMapping("/logout")
    @Operation(summary = "用户退出")
    public ResultVO logout(HttpSession session) {
        session.removeAttribute(Constants.USER_TOKEN_KEY);
        return ResultVO.success(StatusCode.LOGOUT_SUCCESS);
    }

    @PostMapping("/update")
    @Operation(summary = "更换密码")
    @Parameter(name = "resetPwdRequest", description = "请求体", required = true)
    public ResultVO updatePassword(@RequestBody ResetPwdRequest resetPwdRequest) {
        int updatePassword = userInfoService.updatePassword(resetPwdRequest);
        return updatePassword == 1 ? ResultVO.success(StatusCode.UPDATE_SUCCESS) : ResultVO.failed(StatusCode.UPDATE_ERROR);
    }

    @PostMapping("/reset")
    @Operation(summary = "找回密码")
    @Parameter(name = "resetPwdRequest", description = "resetPwdRequest", required = true)
    public ResultVO restPassword(@RequestBody ResetPwdRequest resetPwdRequest) {
        int reset = userInfoService.resetPassword(resetPwdRequest);
        return reset == 1 ? ResultVO.success(StatusCode.OK) : ResultVO.failed(StatusCode.UPDATE_ERROR);
    }

    @PostMapping("/avatar/upload")
    @Operation(summary = "上传头像")
    @Parameter(name = "file", description = "文件", required = true)
    public ResultVO uploadProfile(@NotBlank @RequestHeader String token,
                                  @NotBlank MultipartFile file) {
        int i = userInfoService.uploadProfile(token, file);
        return i == 1 ? ResultVO.success(StatusCode.UPLOAD_SUCCESS) : ResultVO.failed(StatusCode.UPLOAD_ERROR);
    }

    @GetMapping("/avatar/download/{userId}")
    @Operation(summary = "下载头像")
    public void downloadProfile(@NotBlank @PathVariable String userId,
                                HttpServletResponse response) {
        userInfoService.downloadProfile(userId, response);
    }
}

