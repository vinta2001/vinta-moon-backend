package com.vinta.entity.vo;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PostBodyVO {
    @NotEmpty
    private String userId;
    @NotEmpty
    private String postId;
    private String description;
    private String content;
    private List<MediaInNoteVO> mediaList;
    private List<String> tagList;
    private String musicLink;
    @NotNull
    private Long createTime;
    @NotNull
    private Long postTime;
    @NotEmpty
    private String type;
    @NotEmpty
    private String access;
    private String location;
    @NotNull
    private Integer status;
}
