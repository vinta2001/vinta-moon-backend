package com.vinta.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vinta.component.ThreadLocalComponent;
import com.vinta.constant.Constants;
import com.vinta.entity.dto.PostDetail;
import com.vinta.entity.dto.UserDTO;
import com.vinta.entity.po.*;
import com.vinta.entity.vo.PaginationBodyVO;
import com.vinta.entity.vo.PostBodyVO;
import com.vinta.entity.vo.PostDTO;
import com.vinta.enums.MediaAccess;
import com.vinta.enums.MediaStatus;
import com.vinta.jobs.SimpleTask;
import com.vinta.mapper.MediaInfoMapper;
import com.vinta.mapper.UserThumbMapper;
import com.vinta.service.PostInfoService;
import com.vinta.mapper.PostInfoMapper;
import com.vinta.service.UserInfoService;
import com.vinta.utils.DateUtil;
import com.vinta.utils.QuartzUtils;
import com.vinta.utils.RandomUtil;
import com.vinta.utils.StringUtil;
import io.lettuce.core.ScriptOutputType;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author VINTA
 * @description 针对表【post_info】的数据库操作Service实现
 * @createDate 2024-02-17 16:15:05
 */
@Service
@Slf4j
public class PostInfoServiceImpl extends ServiceImpl<PostInfoMapper, PostInfo>
        implements PostInfoService {

    @Resource
    private PostInfoMapper postInfoMapper;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private UserThumbMapper userThumbMapper;

    @Resource
    private MediaInfoMapper mediaInfoMapper;

    @Resource
    private Scheduler scheduler;

    @Bean
    private QueryWrapper<PostInfo> postInfoQueryWrapper() {
        return new QueryWrapper<>();
    }

    @Override
    @Transactional
    public IPage<PostInfo> findPostListByQuery(PaginationBodyVO paginationBodyVO) {
        IPage<PostInfo> postInfoIPage = new Page<>(paginationBodyVO.getPageNum(), paginationBodyVO.getPageSize());
        QueryWrapper<PostInfo> wrapper = postInfoQueryWrapper()
                .eq("status", MediaStatus.PUBLISHED.getStatus())
                .eq("access", MediaAccess.PUBLIC.getAccess())
                .eq(StringUtil.hasContent(paginationBodyVO.getCategory()), "category", paginationBodyVO.getCategory())
                .eq(StringUtil.hasContent(paginationBodyVO.getUserId()), "user_id", paginationBodyVO.getUserId())
                .like(StringUtil.hasContent(paginationBodyVO.getSearchKey()), "description", paginationBodyVO.getSearchKey());
        return postInfoMapper.selectPage(postInfoIPage, wrapper);
    }

    @Override
    @Transactional
    public int insertOne(PostBodyVO postBodyVO) {
        PostInfo postInfo = new PostInfo();
        postInfo.setPostId(postBodyVO.getPostId());
        postInfo.setUserId(postBodyVO.getUserId());
        postInfo.setDescription(postBodyVO.getDescription());
        postInfo.setTextContent(postBodyVO.getContent());
        postInfo.setMusicLink(postBodyVO.getMusicLink());
        postInfo.setCommentsNum(0L);
        postInfo.setThumbNum(0L);
        postInfo.setCollectNum(0L);
        postInfo.setPostTime(new Date(postBodyVO.getPostTime()));
        postInfo.setTags(String.join(",", postBodyVO.getTagList()));
        postInfo.setCreateTime(new Date(postBodyVO.getCreateTime()));
        postInfo.setLocation(postBodyVO.getLocation());
        if (postBodyVO.getStatus().equals(MediaStatus.SCHEDULED.getStatus())) {
            postInfo.setStatus(MediaStatus.SCHEDULED.getStatus());
            String cron = DateUtil.getCron(postInfo.getPostTime());
            log.info("cron: {}", cron);
            QuartzInfo quartzInfo = QuartzInfo.builder()
                    .cron(cron)
                    .jobGroup("定时发布")
                    .jobId(postBodyVO.getPostId())
                    .jobClass(SimpleTask.class)
                    .build();
            QuartzUtils.createScheduledJob(scheduler, quartzInfo);
        } else if (postBodyVO.getStatus().equals(MediaStatus.PUBLISHED.getStatus())) {
            postInfo.setStatus(MediaStatus.PUBLISHED.getStatus());
        } else if (postBodyVO.getStatus().equals(MediaStatus.DRAFT.getStatus())) {
            postInfo.setStatus(MediaStatus.DRAFT.getStatus());
            //todo 做草稿缓存
        }
        postInfo.setAccess(MediaAccess.getAccessByDesc(postBodyVO.getAccess()));
        return postInfoMapper.insert(postInfo);
    }

    @Override
    public void updateStatusById(String postId) {
        postInfoMapper.updateStatusById(postId);
    }

    @Override
    public PostDetail getPostByPostId(String postId) {
        // 获取postInfo然后转PostDTO
        PostInfo postInfo = postInfoMapper.selectOne(new LambdaQueryWrapper<PostInfo>().eq(PostInfo::getPostId, postId));
        PostDTO postDTO = new PostDTO();
        //获取是否喜欢
        BeanUtils.copyProperties(postInfo, postDTO);
        String visitorId = ThreadLocalComponent.get();

        //获取照片url
        List<MediaInfo> medias = mediaInfoMapper.selectList(new LambdaQueryWrapper<MediaInfo>().eq(MediaInfo::getPostId, postId));
        ArrayList<String> urls = new ArrayList<>();
        medias.forEach(mediaInfo -> {
            urls.add(Constants.HOST+mediaInfo.getPhotoUrl());
        });
        postDTO.setMediaList(urls);

        UserThumb userThumb = userThumbMapper.selectByUserIdAndPostId(visitorId, postId);
        postDTO.setLike(userThumb != null);

        //获取用户信息
        String userId = postInfo.getUserId();
        UserInfo userInfo = userInfoService.getUserByUserId(userId);
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(userInfo, userDTO);
        userDTO.setAvatar(Constants.HOST + userDTO.getAvatar());
        return PostDetail.builder().post(postDTO).user(userDTO).build();
    }
}




