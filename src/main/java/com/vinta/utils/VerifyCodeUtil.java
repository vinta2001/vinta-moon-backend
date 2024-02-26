package com.vinta.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.vinta.component.RedisComponent;
import com.vinta.entity.po.UserInfo;
import com.vinta.enums.StatusCode;
import com.vinta.exception.BusinessException;
import com.vinta.mapper.UserInfoMapper;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Objects;


@Component
public class VerifyCodeUtil {

    @Resource
    private JavaMailSender javaMailSender;

    @Resource
    private RedisComponent redisComponent;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Value("${spring.mail.username}")
    private String sendFrom;

    private final String title = "验证码";
    private final String content = "您的验证码是:%s, 请勿泄露给其他人。请在三分钟内完成验证。";

    public void sendEmailCode(String email, String emailCode) {

        UserInfo userInfo = userInfoMapper.selectOne(new QueryWrapper<UserInfo>().eq("email", email));
        if (userInfo != null) {
            throw new BusinessException(StatusCode.USER_EXISTS);
        }
//        将验证码发送到邮箱
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(sendFrom);
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject(title);
        simpleMailMessage.setText(String.format(content, emailCode));
        try {
            javaMailSender.send(simpleMailMessage);
        } catch (Exception e) {
            throw new BusinessException(StatusCode.EMAIL_ERROR);
        }
//        将验证码保存到redis并设置过期时间
        redisComponent.setVerifyCode(email, emailCode);
    }

    public boolean checkCode(String email, String emailCode) {
//        System.out.println(email+"checkCode(String email, String emailCode)");
        String code = (String) redisComponent.getVerifyCode(email);
        if (code == null) {
            throw new BusinessException(StatusCode.CODE_EXPIRED);
        }
        if (Objects.equals(emailCode, code)) {
            String delete = (String) redisComponent.deleteVerifyCode(email);
            return delete != null;
        }
        return false;
    }
}
