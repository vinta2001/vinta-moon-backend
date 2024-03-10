package com.vinta.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vinta.entity.po.PostInfo;
import com.vinta.entity.vo.PaginationBodyVO;
import com.vinta.entity.vo.PostBodyVO;
import com.vinta.enums.MediaAccess;
import com.vinta.enums.MediaStatus;
import com.vinta.service.PostInfoService;
import com.vinta.mapper.PostInfoMapper;
import com.vinta.utils.RandomUtil;
import com.vinta.utils.StringUtil;
import io.lettuce.core.ScriptOutputType;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author VINTA
 * @description 针对表【post_info】的数据库操作Service实现
 * @createDate 2024-02-17 16:15:05
 */
@Service
public class PostInfoServiceImpl extends ServiceImpl<PostInfoMapper, PostInfo>
        implements PostInfoService {

    @Resource
    private PostInfoMapper postInfoMapper;

    @Bean
    private QueryWrapper<PostInfo> postInfoQueryWrapper() {
        return new QueryWrapper<>();
    }

    @Override
    @Transactional
    public IPage<PostInfo> findPostListByQuery(PaginationBodyVO paginationBodyVO) {
        IPage<PostInfo> postInfoIPage = new Page<>(paginationBodyVO.getPageNum(), paginationBodyVO.getPageSize());
        QueryWrapper<PostInfo> wrapper = postInfoQueryWrapper()
                .eq( "status",MediaStatus.PUBLISHED.getStatus())
                .eq( "access", MediaAccess.PUBLIC.getAccess())
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
            //todo 定时发布
        }else if(postBodyVO.getStatus().equals(MediaStatus.PUBLISHED.getStatus())) {
            postInfo.setStatus(MediaStatus.PUBLISHED.getStatus());
        }else if(postBodyVO.getStatus().equals(MediaStatus.DRAFT.getStatus())) {
            postInfo.setStatus(MediaStatus.DRAFT.getStatus());
            //todo 做草稿缓存
        }
        postInfo.setAccess(MediaAccess.getAccessByDesc(postBodyVO.getAccess()));
        return postInfoMapper.insert(postInfo);
    }
}




