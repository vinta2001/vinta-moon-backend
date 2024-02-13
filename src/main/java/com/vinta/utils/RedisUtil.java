package com.vinta.utils;


import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {
    private String tableName = "user:code:";


    @Resource
    private RedisTemplate redisTemplate;

    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public boolean deleteKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    public boolean expire(String key, int seconds, TimeUnit timeUnit) {
        return Boolean.TRUE.equals(redisTemplate.expire(key, seconds, timeUnit));
    }

    public <T> void set(String key, T value) {
        set(key, value, 3, TimeUnit.MINUTES);
    }


    public <T> void set(String key, T value, int expires, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, expires, timeUnit);
    }

    public <T> T delete(String key) {
        Object result = redisTemplate.opsForValue().get(key);
        redisTemplate.delete(key);
        return (T) result;
    }

    public <T> T get(String key) {
        return (T) redisTemplate.opsForValue().get(key);
    }

    public <T> T deleteVerifyCode(String key) {
        key=tableName+key;
        Object result = redisTemplate.opsForValue().get(key);
        redisTemplate.delete(key);
        return (T) result;
    }

    public <T> T getVerifyCode(String key) {
        key = tableName + key;
        System.out.println(key+"getVerifyCode(String key)");
        return (T) redisTemplate.opsForValue().get(key);
    }

    public <T> void setVerifyCode(String key, T value) {
        key = tableName + key;
        set(key, value, 3, TimeUnit.MINUTES);
    }
}
