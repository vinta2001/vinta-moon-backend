package com.vinta.controller;

import com.vinta.entity.dto.MediaDTO;
import com.vinta.entity.dto.ResultDTO;
import com.vinta.entity.vo.MediaBodyVO;
import com.vinta.enums.StatusCode;
import com.vinta.exception.BusinessException;
import com.vinta.utils.FileComponent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@Tag(name = "文件管理", description = "文件相关api")
@RequestMapping("/post")
public class FileController {

    @Resource
    private FileComponent fileComponent;

    @PostMapping("/media/upload")
    @Operation(summary = "上传视频")
    public ResultDTO upload(@RequestParam("file") MultipartFile file,
                            @RequestParam("md5") String md5,
                            @RequestParam("currentChunk") Integer currentChunk,
                            @RequestParam("totalChunks") Integer totalChunks,
                            @RequestParam("currentChunkSize") Long currentChunkSize,
                            @RequestParam("totalSize") Long totalSize,
                            @RequestParam("name") String name,
                            @RequestParam("type") String type) {
        MediaBodyVO mediaBodyVO = new MediaBodyVO();
        mediaBodyVO.setFile(file);
        mediaBodyVO.setMd5(md5);
        mediaBodyVO.setCurrentChunk(currentChunk);
        mediaBodyVO.setTotalChunks(totalChunks);
        mediaBodyVO.setCurrentChunkSize(currentChunkSize);
        mediaBodyVO.setTotalSize(totalSize);
        mediaBodyVO.setType(type);
        mediaBodyVO.setName(name);
        try {
            MediaDTO mediaDTO = fileComponent.upload2Temp(mediaBodyVO);
            if(mediaDTO == null){
                return ResultDTO.success(StatusCode.UPLOAD_SUCCESS);
            }
            return ResultDTO.ok(mediaDTO);
        } catch (Exception e) {
            log.error("文件上传失败", e);
            throw new BusinessException(StatusCode.UPLOAD_ERROR);
        }
    }

    @PostMapping("/pic/upload")
    @Operation(summary = "图片上传",
            parameters = {
                    @Parameter(name = "file", description = "文件", required = true)
            })
    public ResultDTO uploadPic(@RequestParam("file") MultipartFile file) {

        try {
            String filename = fileComponent.upload(file);
            return ResultDTO.ok(filename);
        } catch (Exception e) {
            log.error("文件上传失败", e);
            throw new BusinessException(StatusCode.UPLOAD_ERROR);
        }
    }
}
