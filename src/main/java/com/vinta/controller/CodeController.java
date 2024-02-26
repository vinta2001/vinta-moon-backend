package com.vinta.controller;


import com.vinta.constant.Constants;
import com.vinta.entity.dto.ResultDTO;
import com.vinta.enums.CodeEnum;
import com.vinta.enums.StatusCode;
import com.vinta.utils.VerifyCodeUtil;
import com.vinta.utils.RandomUtil;
import com.vinta.component.RedisComponent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;


@RestController
@RequestMapping("/code")
@Slf4j
@Tag(name = "验证码")
public class CodeController {

    @Resource
    private VerifyCodeUtil verifyCodeUtil;

    @Resource
    private RedisComponent redisComponent;

    /**
     * @param type:1表示邮箱验证码 0表示图片验证码
     * @return
     */
    @GetMapping("verifyCode")
    @Operation(summary = "发送邮箱验证码",description = "type:1表示邮箱验证码 0表示图片验证码")
    public ResultDTO sendEmailCode(HttpSession session,
                                   HttpServletResponse response,
                                   @RequestParam(value = "type") Integer type,
                                   @RequestParam(value = "email") String email) {
        CodeEnum codeEnum = CodeEnum.getCodeEnumByType(type);
        assert codeEnum != null;
        if (Objects.equals(codeEnum.getType(), CodeEnum.EMAIL_CODE.getType())) {
            sendEmailCode(session, email);
        } else if (Objects.equals(codeEnum.getType(), CodeEnum.PICTURE_CODE.getType())){
            sendPictureCode(session,response);
        }
        return ResultDTO.success(StatusCode.EMAIL_CODE_SEND_SUCCESS);
    }

    public void sendEmailCode(HttpSession session, String email) {
        String emailCode = RandomUtil.getRandomEmailCode();
        session.setAttribute(Constants.EMAIL_CODE, emailCode);
        System.out.println(session.getAttribute(Constants.EMAIL_CODE));
        verifyCodeUtil.sendEmailCode(email, emailCode);
    }

    public void sendPictureCode(HttpSession session,HttpServletResponse response) {
        //todo 发送图片验证码
    }
}
