package com.vinta.mapper;

import com.vinta.entity.po.CommentInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;

/**
* @author VINTA
* @description 针对表【comment_info】的数据库操作Mapper
* @createDate 2024-03-31 00:00:59
* @Entity com.vinta.entity.po.CommentInfo
*/
public interface CommentInfoMapper extends BaseMapper<CommentInfo> {

    @Update("update comment_info set status = 0 where comment_id = #{commentId}")
    void updateStatusToDisableById(Integer commentId);
}




