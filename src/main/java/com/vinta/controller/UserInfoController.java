package com.vinta.controller;

import com.vinta.annotation.UserLoginRequired;
import com.vinta.constant.Constants;
import com.vinta.entity.vo.LoginBodyVO;
import com.vinta.entity.vo.RegisterBodyVO;
import com.vinta.entity.dto.ResultDTO;
import com.vinta.entity.vo.ResetPwdBodyVO;
import com.vinta.entity.dto.LoginResultDTO;
import com.vinta.enums.StatusCode;
import com.vinta.service.UserInfoService;
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
    public ResultDTO login(HttpSession session,
                           @RequestBody LoginBodyVO loginBodyVO,
                           HttpServletResponse response) {
        LoginResultDTO loginResultDTO = userInfoService.login(loginBodyVO,response);
        session.setAttribute(Constants.USER_TOKEN_KEY,response.getHeader("Authorization"));
        return ResultDTO.ok(loginResultDTO);
    }

    @PostMapping("/register")
    @Operation(summary = "用户注册")
    @Parameter(name = "registerRequest", description = "注册信息", required = true)
    public ResultDTO register(HttpSession session, @RequestBody RegisterBodyVO registerBodyVO) {
        int register = userInfoService.register(session, registerBodyVO);
        return register == 1 ? ResultDTO.success(StatusCode.CREATE_SUCCESS) : ResultDTO.failed(StatusCode.USER_EXISTS);
    }

    @GetMapping("/logout")
    @Operation(summary = "用户退出")
    @UserLoginRequired
    public ResultDTO logout(HttpSession session, HttpServletResponse response) {
        response.reset();
        session.removeAttribute(Constants.USER_TOKEN_KEY);
        return ResultDTO.success(StatusCode.LOGOUT_SUCCESS);
    }

    @PostMapping("/update")
    @Operation(summary = "更换密码")
    @Parameter(name = "resetPwdRequest", description = "请求体", required = true)
    @UserLoginRequired
    public ResultDTO updatePassword(@RequestBody ResetPwdBodyVO resetPwdBodyVO) {
        int updatePassword = userInfoService.updatePassword(resetPwdBodyVO);
        return updatePassword == 1 ? ResultDTO.success(StatusCode.UPDATE_SUCCESS) : ResultDTO.failed(StatusCode.UPDATE_ERROR);
    }

    @PostMapping("/reset")
    @Operation(summary = "找回密码")
    @UserLoginRequired
    @Parameter(name = "resetPwdRequest", description = "resetPwdRequest", required = true)
    public ResultDTO restPassword(@RequestBody ResetPwdBodyVO resetPwdBodyVO) {
        int reset = userInfoService.resetPassword(resetPwdBodyVO);
        return reset == 1 ? ResultDTO.success(StatusCode.OK) : ResultDTO.failed(StatusCode.UPDATE_ERROR);
    }

    @PostMapping("/avatar/upload")
    @UserLoginRequired
    @Operation(summary = "上传头像")
    @Parameter(name = "file", description = "文件", required = true)
    public ResultDTO uploadAvatar(@NotBlank @RequestHeader("Authorization") String Authorization,
                                   @NotBlank MultipartFile file) {
        int i = userInfoService.uploadAvatar(Authorization, file);
        return i == 1 ? ResultDTO.success(StatusCode.UPLOAD_SUCCESS) : ResultDTO.failed(StatusCode.UPLOAD_ERROR);
    }

    @GetMapping("/avatar/download/{userId}")
    @Operation(summary = "下载头像")
    public void downloadAvatar(@NotBlank @PathVariable String userId,
                                HttpServletResponse response) {
        userInfoService.downloadAvatar(userId, response);
    }
}

