package com.vinta.entity.vo;

import lombok.Data;

import java.util.Date;

@Data
public class CommentBodyVO {
    private Integer replyId;
    private String content;
    private String commenterId;
    private String postId;
    private Date createTime;
    private String location;
}
