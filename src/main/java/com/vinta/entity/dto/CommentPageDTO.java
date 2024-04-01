package com.vinta.entity.dto;

import com.vinta.entity.po.CommentInfo;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CommentPageDTO {
    private Long total;
    private String postId;
    private String workerId;
    private Long pageNum;
    private Long pageSize;
    private Long totalPage;
    private List<CommentDTO> comments;
}
