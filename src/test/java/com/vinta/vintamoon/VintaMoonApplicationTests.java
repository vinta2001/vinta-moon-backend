package com.vinta.vintamoon;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.vinta.entity.po.UserInfo;
import com.vinta.mapper.UserInfoMapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest

class VintaMoonApplicationTests {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Test
    void contextLoads() {
        System.out.println(userInfoMapper.selectOne((new QueryWrapper<UserInfo>()).eq("id", 1)));
    }

}
