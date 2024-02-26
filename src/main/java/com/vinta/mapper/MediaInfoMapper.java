package com.vinta.mapper;

import com.vinta.entity.po.MediaInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author VINTA
* @description 针对表【media_info】的数据库操作Mapper
* @createDate 2024-02-17 16:14:58
* @Entity com.vinta.entity.po.MediaInfo
*/


public interface MediaInfoMapper extends BaseMapper<MediaInfo> {

    List<String> selectMediaByPostId(@Param("postId") String postId);
}




