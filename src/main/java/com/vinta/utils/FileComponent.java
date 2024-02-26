package com.vinta.utils;

import com.vinta.component.RedisComponent;
import com.vinta.entity.dto.MediaDTO;
import com.vinta.entity.po.MediaInfo;
import com.vinta.entity.vo.MediaBodyVO;
import com.vinta.enums.MediaType;
import com.vinta.exception.BusinessException;

import com.vinta.service.MediaInfoService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

@Getter
@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "project.file")
@Slf4j
public class FileComponent {
    private static final String ROOT_PATH = "D:/vintaMoon/file/";
    private static final String VIDEO_PATH = "video/";
    private static final String IMAGE_PATH = "image/";
    private static final String AUDIO_PATH = "audio/";
    private static final String POST_IMAGE_PATH = "post/";
    private static final String HEADER_IMAGE_PATH = "header/";
    private static final String TEMP_PATH = "temp/";

    private static final String FULL_IMAGE_PATH = ROOT_PATH + IMAGE_PATH;
    private static final String FULL_POST_IMAGE_PATH = FULL_IMAGE_PATH + POST_IMAGE_PATH;
    private static final String FULL_HEADER_IMAGE_PATH = FULL_IMAGE_PATH + HEADER_IMAGE_PATH;
    private static final String FULL_VIDEO_PATH = ROOT_PATH + VIDEO_PATH;
    private static final String FULL_AUDIO_PATH = ROOT_PATH + AUDIO_PATH;

    private static final String TEMP_IMAGE_PATH = FULL_IMAGE_PATH + TEMP_PATH;
    private static final String TEMP_POST_IMAGE_PATH = FULL_POST_IMAGE_PATH + TEMP_PATH;
    private static final String TEMP_HEADER_IMAGE_PATH = FULL_HEADER_IMAGE_PATH + TEMP_PATH;
    private static final String TEMP_VIDEO_PATH = FULL_VIDEO_PATH + TEMP_PATH;
    private static final String TEMP_AUDIO_PATH = FULL_AUDIO_PATH + TEMP_PATH;

    @Resource
    private MediaInfoService mediaInfoService;

    @Resource
    private RedisComponent redisComponent;

    @Resource
    @Lazy
    private FileComponent fileComponent;


    private static final String DEFAULT_AVATAR = "default_avatar.jpg";


    public void getProfile(HttpServletResponse response, String userId) {
        String filePath = FULL_HEADER_IMAGE_PATH + userId + ".jpg";
        File file = new File(filePath);
        if (file.exists()) {
            download(response, filePath);
        } else {
            getDefaultProfile(response);
        }
    }

    public void readFile(HttpServletResponse response, String filePath) {
        FileInputStream fileInputStream = null;
        OutputStream fileOutputStream = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                throw new BusinessException("文件不存在");
            }
            response.setContentType("image/*");
            fileInputStream = new FileInputStream(file);
            byte[] bytes = new byte[1024];
            fileOutputStream = response.getOutputStream();
            int len = 0;
            while ((len = fileInputStream.read(bytes)) != -1) {
                fileOutputStream.write(bytes, 0, len);
            }
            fileOutputStream.flush();
        } catch (Exception e) {
            log.error("文件读取异常", e);
            throw new BusinessException("文件不存在");
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    log.error("文件关闭异常", e);
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    log.error("文件关闭异常", e);
                }
            }
        }
    }

    public String upload(MultipartFile file) throws IOException {
        if (file == null) {
            throw new BusinessException("文件不存在");
        }
        String fileSuffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        String filename = RandomUtil.getRandomFileName() + "." + fileSuffix;
        File fileFolder = new File(FULL_IMAGE_PATH);
        if (!fileFolder.exists()) {
            fileFolder.mkdirs();
        }
        //文件写入
        File file2 = new File(FULL_IMAGE_PATH, filename);
        file.transferTo(file2);
        return filename;
    }


    @Transactional(rollbackFor = Exception.class)
    public MediaDTO upload2Temp(MediaBodyVO mediaBodyVO) {
        MediaDTO mediaDTO = new MediaDTO();
        String fileMD5 = mediaBodyVO.getMd5();
        String type = mediaBodyVO.getType();
        Integer currentChunk = mediaBodyVO.getCurrentChunk();
        //查询第一个分片的md5值是否在mysql数据库中
        //如果在，说明已经上传过，直接返回
        if (currentChunk == 0) {
            MediaInfo mediaInfo = mediaInfoService.selectByMd5(fileMD5);
            //已经上传过，直接返回文件地址
            //文件地址就是文件名称
            if (mediaInfo != null) {
                mediaDTO.setSkip(true);
                mediaDTO.setMediaUrl(mediaInfo.getPhotoUrl());
                mediaDTO.setUploadedChunks(null);
                mediaDTO.setUploadedChunkNum(mediaBodyVO.getTotalChunks());
                mediaDTO.setMediaMd5(mediaInfo.getMediaMd5());
                mediaDTO.setTotalChunks(mediaBodyVO.getTotalChunks());
                return mediaDTO;
            }
            //没有上传过，查询redis中是否存在
            Map<String, Object> map = redisComponent.getFileInfoFromTemp(fileMD5);
            if (map != null) {
                List<Integer> chunkList = (List<Integer>) map.get("uploadedChunks");
                mediaDTO.setUploadedChunks(chunkList);
                mediaDTO.setSkip(chunkList.size() == mediaBodyVO.getTotalChunks());
                mediaDTO.setMediaUrl((String) map.get("url"));
                mediaDTO.setMediaMd5((String) map.get("mediaMd5"));
                mediaDTO.setTotalChunks((Integer) map.get("totalChunks"));
                return mediaDTO;
            }
        }
        //都不存在时，分片上传
        //将文件存放在临时文件夹 file/media?/temp/md5中
        chunk2MediaTemp(mediaBodyVO);
        //redis 缓存信息
        chunk2RedisTemp(mediaBodyVO);
        List<Integer> list = (List<Integer>) redisComponent.getFileInfoFromTemp(fileMD5).get("uploadedChunks");
        System.out.println(list);
        String url = mediaBodyVO.getMd5() + "." + mediaBodyVO.getName().substring(mediaBodyVO.getName().lastIndexOf(".") + 1);
        mediaDTO.setSkip(list.size() == mediaBodyVO.getTotalChunks());
        mediaDTO.setMediaUrl(url);
        mediaDTO.setUploadedChunks(list);
        mediaDTO.setUploadedChunkNum(list.size());
        mediaDTO.setMediaMd5(mediaBodyVO.getMd5());
        mediaDTO.setTotalChunks(mediaBodyVO.getTotalChunks());
        Map<String, Object> map = redisComponent.getFileInfoFromTemp(fileMD5);
        if (map != null) {
            map.put("url", url);
        }
        redisComponent.setFileInfo2Temp(fileMD5, map);
        //异步合并
        if (currentChunk == (mediaBodyVO.getTotalChunks() - 1)) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    System.out.println("合并文件");
                    String filename = fileMD5 + "." + mediaBodyVO.getName().substring(mediaBodyVO.getName().lastIndexOf(".") + 1);
                    fileComponent.transferFile(fileMD5, filename, type, true);
                }
            });
        }
        return mediaDTO;
    }

    public void chunk2RedisTemp(MediaBodyVO mediaBodyVO) {
        MultipartFile file = mediaBodyVO.getFile();
        assert file.getOriginalFilename() != null;
        String suffix = mediaBodyVO.getName().substring(mediaBodyVO.getName().lastIndexOf(".") + 1);
        String fileMD5 = mediaBodyVO.getMd5();
        String filename = fileMD5 + "." + suffix;
        Integer currentChunk = mediaBodyVO.getCurrentChunk();
        List<Integer> list = null;
        Map<String, Object> fileInfoFromTemp = redisComponent.getFileInfoFromTemp(fileMD5);
        if (fileInfoFromTemp != null) {
            list = (List<Integer>) redisComponent.getFileInfoFromTemp(fileMD5).get("uploadedChunks");
        } else {
            list = new ArrayList<>();
        }
        list.add(currentChunk);
        Map<String, Object> map = new HashMap<>();
        map.put("mediaMd5", fileMD5);
        map.put("fileName", filename);
        map.put("uploadedChunks", list);
        map.put("totalChunks", mediaBodyVO.getTotalChunks());
        map.put("type", mediaBodyVO.getType());
        map.put("uploadedChunkNum", list.size());
        map.put("currentChunkSize", mediaBodyVO.getCurrentChunkSize());
        redisComponent.setFileInfo2Temp(fileMD5, map);
    }


    public void chunk2MediaTemp(MediaBodyVO mediaBodyVO) {
        String type = mediaBodyVO.getType();
        MultipartFile multipartFile = mediaBodyVO.getFile();
        File file = getFile(mediaBodyVO, multipartFile, type);
        if (!file.exists()) {
            file.mkdirs();
        }
        // TODO
        //  1.将文件存放在临时文件夹 file/media?/temp/md5中
        try {
            multipartFile.transferTo(file);
        } catch (Exception e) {
            log.error("文件保存失败", e);
            throw new BusinessException("文件保存失败");
        }
    }

    private File getFile(MediaBodyVO mediaBodyVO, MultipartFile multipartFile, String type) {
        assert multipartFile.getOriginalFilename() != null;
        String tempPath = type.equals(MediaType.VIDEO.getName()) ? TEMP_VIDEO_PATH : TEMP_IMAGE_PATH;
        File fileFolder = new File(tempPath);
        if (!fileFolder.exists()) {
            fileFolder.mkdirs();
        }
        String filename = tempPath + mediaBodyVO.getMd5() + "/" + mediaBodyVO.getCurrentChunk();
        return new File(filename);
    }

    public String upload(String userId, MultipartFile file) throws IOException {
        if (file == null) {
            throw new BusinessException("文件不存在");
        }
        String fileSuffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        String filename = userId + "." + fileSuffix;
        File fileFolder = new File(FULL_HEADER_IMAGE_PATH);
        if (!fileFolder.exists()) {
            fileFolder.mkdirs();
        }
        //文件写入
        File file2 = new File(FULL_HEADER_IMAGE_PATH, filename);
        file.transferTo(file2);
        return filename;
    }

    public void download(HttpServletResponse response, String filePath) {
        readFile(response, filePath);
    }

    public void getDefaultProfile(HttpServletResponse response) {
        File file = new File(FULL_HEADER_IMAGE_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
        readFile(response, FULL_HEADER_IMAGE_PATH + DEFAULT_AVATAR);
    }

    @Async
    public void transferFile(String fileMD5, String filename, String type, Boolean deleteSource) {
        String tempName = type.equals(MediaType.VIDEO.getName()) ? TEMP_VIDEO_PATH : TEMP_IMAGE_PATH + "/" + fileMD5;
        String targetName = type.equals(MediaType.VIDEO.getName()) ? FULL_VIDEO_PATH : FULL_POST_IMAGE_PATH + "/" + filename;
        RandomAccessFile writeFile = null;
        File file = new File(tempName);
        if (!file.exists()) {
            throw new BusinessException("文件不存在");
        }
        File[] files = file.listFiles();
        File targetFile = new File(targetName);
        try {
            writeFile = new RandomAccessFile(targetFile, "rw");
            byte[] bytes = new byte[1024 * 10];
            for (int i = 0; i < files.length; i++) {
                int len = -1;
                File chunkFile = files[i];
                RandomAccessFile readFile = null;
                try {
                    readFile = new RandomAccessFile(chunkFile, "r");
                    while ((len = readFile.read(bytes)) != -1) {
                        writeFile.write(bytes, 0, len);
                    }
                } catch (Exception e) {
                    log.error("文件读取异常", e);
                } finally {
                    if (readFile != null) {
                        readFile.close();
                    }
                }
            }
        } catch (Exception e) {
            log.error("文件写入异常", e);
        } finally {
            if (writeFile != null) {
                try {
                    writeFile.close();
                } catch (IOException e) {
                    log.error("文件关闭异常", e);
                }
            }
            try {
                if (deleteSource && file.exists()) {
                    FileUtils.delete(file);
                }
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        //删除redis中的数据
//        redisComponent.deleteFileInfoFromTemp(fileMD5);
    }
}
