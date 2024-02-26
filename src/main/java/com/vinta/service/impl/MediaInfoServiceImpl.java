package com.vinta.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vinta.entity.po.MediaInfo;
import com.vinta.entity.vo.MediaBodyVO;
import com.vinta.entity.vo.PostBodyVO;
import com.vinta.enums.MediaType;
import com.vinta.service.MediaInfoService;
import com.vinta.mapper.MediaInfoMapper;
import com.vinta.utils.RandomUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Objects;

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
    public MediaInfo selectByMd5(String md5) {
        return mediaInfoMapper.selectOne(new QueryWrapper<MediaInfo>().eq("media_md5", md5));
    }

    @Override
    @Transactional
    public int insertAll(PostBodyVO postBodyVO) {
        List<String> mediaList = postBodyVO.getMediaList();
        String type = postBodyVO.getType();
        MediaInfo mediaInfo = new MediaInfo();
        int result = 0;
        for (String media : mediaList) {
            mediaInfo.setPostId(postBodyVO.getPostId());
            mediaInfo.setMediaType(Objects.requireNonNull(MediaType.getMediaTypeByName(type)).getType());
            mediaInfo.setUserId(postBodyVO.getUserId());
            mediaInfo.setPhotoId(media.split("\\.")[0]);
            mediaInfo.setPhotoUrl(media);
            mediaInfo.setPostTime(postBodyVO.getCreateTime());
            mediaInfo.setMediaMd5(media.split("\\.")[0]);
            result += mediaInfoMapper.insert(mediaInfo);
        }
        return result;
    }


}




