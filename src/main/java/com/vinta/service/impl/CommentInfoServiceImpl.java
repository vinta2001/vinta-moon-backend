package com.vinta.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vinta.constant.Constants;
import com.vinta.entity.dto.CommentDTO;
import com.vinta.entity.dto.CommentPageDTO;
import com.vinta.entity.po.CommentInfo;
import com.vinta.entity.po.UserInfo;
import com.vinta.entity.vo.CommentBodyVO;
import com.vinta.entity.vo.CommentPageVO;
import com.vinta.mapper.PostInfoMapper;
import com.vinta.mapper.UserInfoMapper;
import com.vinta.service.CommentInfoService;
import com.vinta.mapper.CommentInfoMapper;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author VINTA
 * @description 针对表【comment_info】的数据库操作Service实现
 * @createDate 2024-03-30 23:26:40
 */
@Service
public class CommentInfoServiceImpl extends ServiceImpl<CommentInfoMapper, CommentInfo>
        implements CommentInfoService{
    @Resource
    private CommentInfoMapper commentInfoMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private PostInfoMapper postInfoMapper;

    @Override
    @Transactional
    public void addComment(CommentBodyVO commentBody) {
        CommentInfo commentInfo = new CommentInfo();
        BeanUtils.copyProperties(commentBody, commentInfo);
        postInfoMapper.addComments(commentBody.getPostId());
        commentInfoMapper.insert(commentInfo);
    }

    @Override
    @Transactional
    public CommentPageDTO getComments(CommentPageVO commentPageVO) {
        IPage<CommentInfo> commentInfoIPage = new Page<>(commentPageVO.getPageNum(), commentPageVO.getPageSize());
        commentInfoMapper.selectPage(commentInfoIPage, new LambdaQueryWrapper<CommentInfo>()
                .eq(CommentInfo::getStatus, Constants.STATUS_AVAILABLE)
                .eq(CommentInfo::getPostId, commentPageVO.getPostId()));
        List<CommentDTO> comments = getCommentDTOS(commentInfoIPage);

        return CommentPageDTO.builder()
                .pageNum(commentInfoIPage.getCurrent())
                .total(commentInfoIPage.getTotal())
                .pageSize(commentInfoIPage.getSize())
                .comments(comments)
                .postId(commentPageVO.getPostId())
                .totalPage(commentInfoIPage.getPages())
                .build();
    }

    private List<CommentDTO> getCommentDTOS(IPage<CommentInfo> commentInfoIPage) {
        List<CommentInfo> records = commentInfoIPage.getRecords();
        List<CommentDTO> comments = new ArrayList<>();
        records.forEach(commentInfo -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(commentInfo, commentDTO);
            String commenterId = commentInfo.getCommenterId();
            UserInfo userInfo = userInfoMapper.selectById(commenterId);
            commentDTO.setCommenterNickname(userInfo.getUserNickname());
            commentDTO.setCommenterAvatar(Constants.HOST + userInfo.getAvatar());
            comments.add(commentDTO);
        });
        return comments;
    }

    @Override
    @Transactional
    public void deleteComment(Integer commentId) {
        CommentInfo commentInfo = commentInfoMapper.selectById(commentId);
        postInfoMapper.deleteComments(commentInfo.getPostId());
        commentInfoMapper.updateStatusToDisableById(commentId);
    }
}




