package com.vinta.entity.vo;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class MediaBodyVO {
    /**
     * 该请求体应该包含
     * 1.文件 file
     * 2. md5值 md5；
     * 3。当前片号 currentChunk
     * 4。分片总数 totalChunks
     * 5.分片大小 chunkSize
     * 6.文件总大小 totalSize
     * 7.文件名直接采用前端传入的md5值
     * 8. 已经上传的分片数
     * 9. 总分片数
     */
    private MultipartFile file;
    private String md5;
    private Integer currentChunk;
    private Integer totalChunks;
    private Long currentChunkSize;
    private Long totalSize;
    private String type;
    private String name;
}
