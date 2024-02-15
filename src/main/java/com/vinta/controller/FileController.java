package com.vinta.controller;

import com.vinta.entity.vo.ResultVO;
import com.vinta.enums.StatusCode;
import com.vinta.exception.BusinessException;
import com.vinta.utils.FileUtil;
import io.swagger.v3.oas.annotations.Operation;
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

    @PostMapping("/upload")
    @Operation(summary = "文件上传")
    public ResultVO upload(MultipartFile file) {
        try{
            String upload = FileUtil.upload(file);
            return ResultVO.ok(upload);
        }catch (Exception e){
            log.error("文件上传失败",e);
            throw new BusinessException(StatusCode.UPLOAD_ERROR);
        }
    }
}
