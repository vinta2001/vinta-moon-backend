package com.vinta.entity.dto;


import lombok.Data;

import java.util.List;

@Data
public class MediaDTO {
    //跳过
    private Boolean skip;
    // 已经传过的chunk
    private List<Integer> uploadedChunks;
    private Integer uploadedChunkNum;
    private String mediaUrl;
    private String mediaMd5;
    private Integer totalChunks;
    private boolean isUploadFinish;
}
