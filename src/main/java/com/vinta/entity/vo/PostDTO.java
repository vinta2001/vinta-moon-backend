package com.vinta.entity.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PostDTO {
    private String postId;
    private String userId;
    private String description;
    private String textContent;
    private Long commentsNum;
    private Long thumbNum;
    private Long collectNum;
    private String musicLink;
    private Integer category;
    private String tags;
    private String location;
    private Date postTime;
    private List<String> mediaList;
    private Boolean like;

}
