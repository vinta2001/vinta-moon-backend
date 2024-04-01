package com.vinta.entity.vo;

import lombok.Data;

@Data
public class CommentPageVO {
    private String postId;
    private Integer pageNum;
    private Integer pageSize;
}
