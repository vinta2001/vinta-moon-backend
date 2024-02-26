package com.vinta.component;


import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class RedisComponent {
    private static final String codeTable = "user:code:";

    private static final String fileTempTable = "user:file:temp:";

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

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
        key=codeTable+key;
        Object result = redisTemplate.opsForValue().get(key);
        redisTemplate.delete(key);
        return (T) result;
    }

    public <T> T getVerifyCode(String key) {
        key = codeTable + key;
        System.out.println(key+"getVerifyCode(String key)");
        return (T) redisTemplate.opsForValue().get(key);
    }

    public <T> void setVerifyCode(String key, T value) {
        key = codeTable + key;
        set(key, value, 3, TimeUnit.MINUTES);
    }

    // 向redis中存放map
    public void setFileInfo2Temp(String key, Map<String, Object> value) {
        redisTemplate.opsForHash().put(fileTempTable, key, value);
    }

    public Map<String, Object> getFileInfoFromTemp(String key) {
        return (Map<String, Object>) redisTemplate.opsForHash().get(fileTempTable, key);
    }
}
