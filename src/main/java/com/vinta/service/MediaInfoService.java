package com.vinta.service;

import com.vinta.entity.po.MediaInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vinta.entity.vo.MediaBodyVO;
import com.vinta.entity.vo.PostBodyVO;

import java.util.List;

/**
* @author VINTA
* @description 针对表【media_info】的数据库操作Service
* @createDate 2024-02-17 16:14:58
*/
public interface MediaInfoService extends IService<MediaInfo> {

    List<String> getMediaByPostId(String postId);


    MediaInfo selectByMd5(String md5);

    int insertAll(PostBodyVO postBodyVO);

    String getMediaMd5ByPhotoId(String filename);
}
