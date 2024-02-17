package com.vinta.controller;


import com.vinta.constant.Constants;
import com.vinta.entity.dto.ResultDTO;
import com.vinta.enums.CodeEnum;
import com.vinta.enums.StatusCode;
import com.vinta.utils.VerifyCodeUtil;
import com.vinta.utils.RandomUtil;
import com.vinta.utils.RedisUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
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
    private RedisUtil redisUtil;

    /**
     * @param type:1表示邮箱验证码 0表示图片验证码
     * @return
     */
    @GetMapping("email-code")
    @Operation(summary = "发送邮箱验证码")
    public ResultDTO sendEmailCode(HttpSession session,
                                   @RequestParam(value = "type", required = true) Integer type,
                                   @RequestParam(value = "email", required = true) String email) {
        CodeEnum codeEnum = CodeEnum.getCodeEnumByType(type);
        assert codeEnum != null;
        if (Objects.equals(type, codeEnum.getType())) {
            sendEmailCode(session, email);
        }
        return ResultDTO.success(StatusCode.EMAIL_CODE_SEND_SUCCESS);
    }

    @GetMapping("picture-code")
    @Operation(summary = "发送图片验证码")
    public ResultDTO sendPictureCode(HttpSession session,
                                     @RequestParam(value = "type", required = true) Integer type,
                                     @RequestParam(value = "email", required = true) String email) {
        System.out.println(email);
        if (Objects.equals(type, CodeEnum.PICTURE_CODE.getType())) {
            sendPictureCode(session, email);
        }
        return ResultDTO.failed(StatusCode.BAD_REQUEST);
    }

    public void sendEmailCode(HttpSession session, String email) {
        String emailCode = RandomUtil.getRandomEmailCode();
        session.setAttribute(Constants.EMAIL_CODE, emailCode);
        System.out.println(session.getAttribute(Constants.EMAIL_CODE));
        verifyCodeUtil.sendEmailCode(email, emailCode);
    }

    public void sendPictureCode(HttpSession session, String email) {
        String emailCode = RandomUtil.getRandomEmailCode();
        session.setAttribute("type", CodeEnum.PICTURE_CODE.getType());
        session.setAttribute(CodeEnum.PICTURE_CODE.getDesc(), emailCode);
        verifyCodeUtil.sendEmailCode(email, emailCode);
    }
}
