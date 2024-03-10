package com.vinta.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vinta.component.RedisComponent;
import com.vinta.entity.po.MediaInfo;
import com.vinta.entity.vo.MediaInNoteVO;
import com.vinta.entity.vo.PostBodyVO;
import com.vinta.enums.MediaAccess;
import com.vinta.enums.MediaStatus;
import com.vinta.enums.MediaType;
import com.vinta.service.MediaInfoService;
import com.vinta.mapper.MediaInfoMapper;
import com.vinta.utils.RandomUtil;
import com.vinta.utils.StringUtil;
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

    @Resource
    private RedisComponent redisComponent;

    @Override
    public List<String> getMediaByPostId(String postId) {
        return mediaInfoMapper.selectMediaByPostId(postId);
    }


    @Override
    @Transactional
    public int insertAll(PostBodyVO postBodyVO) {
        List<MediaInNoteVO> mediaList = postBodyVO.getMediaList();
        String type = postBodyVO.getType();
        MediaInfo mediaInfo = new MediaInfo();
        int result = 0;
        for (MediaInNoteVO media : mediaList) {
            String fileMd5 = media.getMediaMd5();
            String fileId = media.getMediaUrl().split("\\.")[0];
            fileId = fileId.split("\\/")[fileId.split("\\/").length - 1];
            mediaInfo.setPostId(postBodyVO.getPostId());
            mediaInfo.setMediaType(Objects.requireNonNull(MediaType.getMediaTypeByName(type)).getType());
            mediaInfo.setUserId(postBodyVO.getUserId());
            mediaInfo.setPhotoId(fileId);
            mediaInfo.setPhotoUrl(media.getMediaUrl());
            mediaInfo.setPostTime(new Date(postBodyVO.getCreateTime()));
            mediaInfo.setMediaMd5(fileMd5);
            if (postBodyVO.getStatus().equals(MediaStatus.SCHEDULED.getStatus())) {
                mediaInfo.setStatus(MediaStatus.SCHEDULED.getStatus());
            }
            if (postBodyVO.getStatus().equals(MediaStatus.PUBLISHED.getStatus())) {
                mediaInfo.setStatus(MediaStatus.PUBLISHED.getStatus());
            }
            mediaInfo.setAccess(MediaAccess.getAccessByDesc(postBodyVO.getAccess()));
            // todo 删除redis
//            redisComponent.
            int res = mediaInfoMapper.insert(mediaInfo);
            if (res == 1) {
                System.out.println(fileId);
                redisComponent.deleteFileInfoFromTemp(fileId);
            }
            result += res;
        }
        return result;
    }

    @Override
    public String getMediaMd5ByPhotoId(String filename) {
        MediaInfo mediaInfo = mediaInfoMapper.selectOne(new QueryWrapper<MediaInfo>().eq("photo_id", filename));
        return mediaInfo.getMediaMd5();
    }
}




