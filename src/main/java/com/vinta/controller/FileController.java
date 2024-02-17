package com.vinta.controller;

import com.vinta.entity.dto.ResultDTO;
import com.vinta.enums.StatusCode;
import com.vinta.exception.BusinessException;
import com.vinta.mapper.MediaInfoMapper;
import com.vinta.service.MediaInfoService;
import com.vinta.utils.FileUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@Tag(name = "文件管理", description = "文件相关api")
@RequestMapping("/post/file")
public class FileController {

    @Resource
    private MediaInfoService mediaInfoService;

    @PostMapping("/upload")
    @Operation(summary = "文件上传",
            parameters = {
                    @Parameter(name = "file", description = "文件", required = true),
                    @Parameter(name = "fileMd5", description = "文件md5", required = true),
                    @Parameter(name = "userId", description = "用户id", required = true),
                    @Parameter(name = "postId", description = "笔记id", required = true)
            })
    public ResultDTO upload(String fileMd5, String userId, String postId, MultipartFile file) {
        try {
            String filename = FileUtil.upload(file);
            int i = mediaInfoService.insertOne(fileMd5, userId, postId, filename);
            return i == 1 ? ResultDTO.ok(filename) : ResultDTO.failed(StatusCode.UPLOAD_ERROR);
        } catch (Exception e) {
            log.error("文件上传失败", e);
            throw new BusinessException(StatusCode.UPLOAD_ERROR);
        }
    }
}
