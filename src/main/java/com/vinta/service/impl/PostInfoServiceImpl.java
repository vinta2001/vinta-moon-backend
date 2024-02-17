package com.vinta.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vinta.entity.po.PostInfo;
import com.vinta.entity.vo.PaginationBodyVO;
import com.vinta.entity.vo.PostBodyVO;
import com.vinta.service.PostInfoService;
import com.vinta.mapper.PostInfoMapper;
import com.vinta.utils.RandomUtil;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
    public IPage<PostInfo> findPostListByQuery(PaginationBodyVO paginationBodyVO) {
        IPage<PostInfo> postInfoIPage = new Page<>(paginationBodyVO.getPageNum(), paginationBodyVO.getPageSize());
        QueryWrapper<PostInfo> wrapper = postInfoQueryWrapper()
                .eq(paginationBodyVO.getCategory() != null, "category", paginationBodyVO.getCategory())
                .eq(paginationBodyVO.getUserId() != null, "user_id", paginationBodyVO.getUserId())
                .like(paginationBodyVO.getSearchKey() != null, "description", paginationBodyVO.getSearchKey());
        postInfoMapper.selectPage(postInfoIPage, wrapper);
        return postInfoIPage;
    }

    @Override
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
        postInfo.setPostTime(new Date());
        postInfo.setTags(String.join(",", postBodyVO.getTagList()));
        //todo 添加分类
        return postInfoMapper.insert(postInfo);
    }
}




