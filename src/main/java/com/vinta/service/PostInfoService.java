package com.vinta.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.vinta.entity.po.PostInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vinta.entity.vo.PaginationBodyVO;
import com.vinta.entity.vo.PostBodyVO;

/**
* @author VINTA
* @description 针对表【post_info】的数据库操作Service
* @createDate 2024-02-17 16:15:05
*/
public interface PostInfoService extends IService<PostInfo> {

    IPage<PostInfo> findPostListByQuery(PaginationBodyVO paginationBodyVO);

    int insertOne(PostBodyVO postbodyvo);

    void updateStatusById(String postId);
}
