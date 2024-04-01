package com.vinta.mapper;

import com.vinta.entity.po.PostInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;

/**
* @author VINTA
* @description 针对表【post_info】的数据库操作Mapper
* @createDate 2024-02-17 16:15:05
* @Entity com.vinta.entity.po.PostInfo
*/
public interface PostInfoMapper extends BaseMapper<PostInfo> {

    @Update("update post_info set status = 1 where post_id = #{postId}")
    void updateStatusById(String postId);

    @Update("update post_info set comments_num = comments_num + 1 where post_id = #{postId}")
    void addComments(String postId);

    @Update("update post_info set comments_num = comments_num - 1 where post_id = #{postId}")
    void deleteComments(String postId);
}




