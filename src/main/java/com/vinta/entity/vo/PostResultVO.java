package com.vinta.entity.vo;

import lombok.Data;

@Data
public class PostResultVO {
    private String id;
    private UserResultVO user;
    private String desc;
    private String content;
    private Long comments;
    private String musicLink;
    private Long thumbs;
    private Long collects;
    private boolean like;
    private String cover;


    public PostResultVO() {
    }
}
