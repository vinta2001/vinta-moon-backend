package com.vinta.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vinta.entity.po.MediaInfo;
import com.vinta.entity.vo.PostBodyVO;
import com.vinta.enums.MediaType;
import com.vinta.service.MediaInfoService;
import com.vinta.mapper.MediaInfoMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author VINTA
 * @description 针对表【media_info】的数据库操作Service实现
 * @createDate 2024-02-17 16:14:58
 */
@Service
public class MediaInfoServiceImpl extends ServiceImpl<MediaInfoMapper, MediaInfo>
        implements MediaInfoService {

    @Resource
    private MediaInfoMapper mediaInfoMapper;

    @Override
    public List<String> getMediaByPostId(String postId) {
        return mediaInfoMapper.selectMediaByPostId(postId);
    }


    @Override
    public int insertOne(String fileMd5,String userId,String postId, String filename) {
        String fileSuffix = filename.split("\\.")[1];
        MediaType mediaType = MediaType.getMediaTypeByName(fileSuffix);
        MediaInfo mediaInfo = new MediaInfo();
        mediaInfo.setMediaMd5(fileMd5);
        mediaInfo.setPhotoUrl(filename);
        mediaInfo.setUserId(userId);
        mediaInfo.setPostId(postId);
        mediaInfo.setPostTime(new Date());
        assert mediaType != null;
        mediaInfo.setMediaType(mediaType.getType());
        return mediaInfoMapper.insert(mediaInfo);
    }
}




