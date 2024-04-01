package com.vinta.service;

import com.vinta.entity.dto.CommentPageDTO;
import com.vinta.entity.po.CommentInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vinta.entity.vo.CommentBodyVO;
import com.vinta.entity.vo.CommentPageVO;

/**
* @author VINTA
* @description 针对表【comment_info】的数据库操作Service
* @createDate 2024-03-31 00:00:59
*/
public interface CommentInfoService extends IService<CommentInfo> {

    void deleteComment(Integer commentId);

    CommentPageDTO getComments(CommentPageVO commentPageVO);

    void addComment(CommentBodyVO commentBody);
}
