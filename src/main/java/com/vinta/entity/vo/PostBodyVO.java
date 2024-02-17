package com.vinta.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class PostBodyVO {
    private String userId;
    private String postId;
    private String description;
    private String content;
    private List<String> imageList;
    private List<String> tagList;
    private String musicLink;
}
