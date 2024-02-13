package com.vinta.utils;

import com.vinta.constant.Constants;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.UUID;

public class RandomUtil {
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String getRandomUsername(Integer length) {
        return RandomStringUtils.random(length == null ? 0 : length, false, true);
    }

    public static String getRandomUsername() {
        return getRandomUsername(Constants.USERNAME_LENGTH);
    }

    public static String getRandomCode(Integer length) {
        return RandomStringUtils.random(length == null ? 0 : length, true, true);
    }

    public static String getRandomEmailCode() {
        return getRandomCode(Constants.CODE_LENGTH);
    }

    public static String getRandomNickName(){
        return "momo";
    }
}
