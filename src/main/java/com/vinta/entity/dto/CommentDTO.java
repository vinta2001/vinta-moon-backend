package com.vinta.entity.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CommentDTO {
    private Integer commentId;

    private String postId;

    private String commenterId;

    private String commenterNickname;

    private String commenterAvatar;

    private Integer replyId;


    private String content;


    private Date createTime;

    private String location;
}
