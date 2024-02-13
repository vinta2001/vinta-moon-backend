package com.vinta.vintamoon;

import com.vinta.utils.RedisUtil;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class VintaMoonApplicationTests {

    @Resource
    private RedisUtil redisUtil;

    @Test
    void contextLoads() {
        redisUtil.set("name", "vinta");
        System.out.println(redisUtil.get("name").toString());
    }

}
