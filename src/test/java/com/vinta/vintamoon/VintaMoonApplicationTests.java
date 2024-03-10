package com.vinta.vintamoon;

import com.vinta.entity.po.MediaInfo;
import com.vinta.mapper.MediaInfoMapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest
class VintaMoonApplicationTests {

    @Resource
    private MediaInfoMapper mediaInfoMapper;

    @Test
    void contextLoads() {
        List<MediaInfo> mediaInfo = mediaInfoMapper.selectAll();
        for (MediaInfo media : mediaInfo) {
            String photoUrl = media.getPhotoUrl();
            media.setPhotoUrl("/"+photoUrl);
            mediaInfoMapper.updateById(media);
        }
    }

    @Test
    void test4regex(){
        String url ="post/pic/download/5adda88f7577812e857b7e42f08d9b8c.jpg";
        System.out.println(url.replaceAll("post/pic/download/(\\w+)\\.jpg", ""));
    }
    @Test
    void test1(){
        String input = "post/pic/download/5adda88f7577812e857b7e42f08d9b8c.jpg";
        String pattern = "post/pic/download/(\\w+)\\.jpg";  // 匹配数字、字母或下划线的模式
        String replacement = "replacementString";  // 替换成你想要的字符串

        String result = input.replaceAll(pattern, replacement);
        System.out.println(result);

    }

}
