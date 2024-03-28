package com.vinta.mapper;

import com.vinta.entity.po.MediaInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
* @author VINTA
* @description 针对表【media_info】的数据库操作Mapper
* @createDate 2024-02-17 16:14:58
* @Entity com.vinta.entity.po.MediaInfo
*/


public interface MediaInfoMapper extends BaseMapper<MediaInfo> {

    List<String> selectMediaByPostId(@Param("postId") String postId);

    @Select("select * from media_info")
    @ResultType(MediaInfo.class)
    List<MediaInfo> selectAll();


    @Update("update media_info set status = 1 where post_id = #{postId}")
    void updateStatusByPostId(String postId);
}




