package com.vinta.controller;


import com.vinta.entity.vo.ResultVO;
import com.vinta.enums.CodeEnum;
import com.vinta.enums.StatusCode;
import com.vinta.exception.BusinessException;
import com.vinta.utils.VerifyCodeUtil;
import com.vinta.utils.RandomUtil;
import com.vinta.utils.RedisUtil;
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
    public ResultVO sendEmailCode(HttpSession session,
                                          @RequestParam(value = "type", required = true) Integer type,
                                          @RequestParam(value = "email", required = true) String email) {
        if (Objects.equals(type, CodeEnum.EMAIL_CODE.getType())) {
            sendEmailCode(session, email);
        }
        return ResultVO.success(StatusCode.EMAIL_CODE_SEND_SUCCESS);
    }

    @GetMapping("picture-code")
    public ResultVO sendPictureCode(HttpSession session,
                                    @RequestParam(value = "type", required = true) Integer type,
                                    @RequestParam(value = "email", required = true) String email) {
        System.out.println(email);
        if (Objects.equals(type, CodeEnum.PICTURE_CODE.getType())) {
            sendPictureCode(session, email);
        }
        return ResultVO.failed(StatusCode.BAD_REQUEST);
    }

    public void sendEmailCode(HttpSession session, String email) {
        String emailCode = RandomUtil.getRandomEmailCode();
        verifyCodeUtil.sendEmailCode(email, emailCode);
    }

    public void sendPictureCode(HttpSession session, String email) {
        String emailCode = RandomUtil.getRandomEmailCode();
        session.setAttribute("type", CodeEnum.PICTURE_CODE.getType());
        session.setAttribute(CodeEnum.PICTURE_CODE.getDesc(), emailCode);
        verifyCodeUtil.sendEmailCode(email, emailCode);
    }



}
