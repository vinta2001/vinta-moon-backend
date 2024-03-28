package com.vinta.mapper;

import com.vinta.entity.po.UserThumb;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
* @author VINTA
* @description 针对表【user_thumb】的数据库操作Mapper
* @createDate 2024-03-28 19:41:22
* @Entity com.vinta.entity.po.UserThumb
*/
public interface UserThumbMapper extends BaseMapper<UserThumb> {


    @Select("select * from user_thumb where user_id = #{userId} and post_id = #{postId}")
    UserThumb selectByUserIdAndPostId(@Param("userId")String userId, @Param("postId") String postId);

    @Delete("delete from user_thumb where user_id = #{userId} and post_id = #{postId}")
    void deleteByUserIdAndPostId(String userId, String postId);
}




