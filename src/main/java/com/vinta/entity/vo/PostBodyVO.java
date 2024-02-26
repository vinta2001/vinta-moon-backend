package com.vinta.entity.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PostBodyVO {
    private String userId;
    private String postId;
    private String description;
    private String content;
    private List<String> mediaList;
    private List<String> tagList;
    private String musicLink;
    private Date createTime;
    private String type;
}
