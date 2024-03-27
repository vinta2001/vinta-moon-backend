package com.vinta.component;

import com.vinta.constant.Constants;
import com.vinta.entity.dto.MediaDTO;
import com.vinta.entity.vo.MediaBodyVO;
import com.vinta.enums.MediaType;
import com.vinta.enums.StatusCode;
import com.vinta.exception.BusinessException;

import com.vinta.service.MediaInfoService;
import com.vinta.utils.RandomUtil;
import com.vinta.utils.StringUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
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

    @Resource
    private MediaInfoService mediaInfoService;

    @Resource
    private RedisComponent redisComponent;

    @Resource
    @Lazy
    private FileComponent fileComponent;

    public void getAvatar(HttpServletResponse response, String filename) {
        if (StringUtil.hasContent(filename)) {
            filename = Constants.FULL_HEADER_IMAGE_PATH + filename;
            File file = new File(filename);
            if (file.exists()) {
                downloadAvatar(response, filename);
            } else {
                getDefaultAvatar(response);
            }
        } else {
            getDefaultAvatar(response);
        }
    }

    public String getAvatarUrl(String userId) {
        String filePath = Constants.FULL_HEADER_IMAGE_PATH + userId + ".jpg";
        File file = new File(filePath);
        if (file.exists()) {
            return Constants.FULL_HEAD_PIC_URL + userId + ".jpg";
        } else {
            return Constants.FULL_HEAD_PIC_URL + Constants.DEFAULT_AVATAR;
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
        File fileFolder = new File(Constants.FULL_IMAGE_PATH);
        if (!fileFolder.exists()) {
            fileFolder.mkdirs();
        }
        //文件写入
        File file2 = new File(Constants.FULL_IMAGE_PATH, filename);
        file.transferTo(file2);
        return filename;
    }


    @Transactional(rollbackFor = Exception.class)
    public MediaDTO upload2Temp(MediaBodyVO mediaBodyVO) {
        MediaDTO mediaDTO = new MediaDTO();
        String fileMD5 = mediaBodyVO.getMd5();
        String type = mediaBodyVO.getType();
        String suffix = mediaBodyVO.getName().substring(mediaBodyVO.getName().lastIndexOf("."));
        Integer currentChunk = mediaBodyVO.getCurrentChunk();
        //查询第一个分片的md5值是否在mysql数据库中
        //如果在，说明已经上传过，直接返回
        if (currentChunk == 0) {
            //去文件夹查询是否已经上传过
            String filename = (type.equals(MediaType.VIDEO.getName()) ? Constants.FULL_VIDEO_PATH : Constants.FULL_POST_IMAGE_PATH )+ mediaBodyVO.getMd5() + suffix;
            Boolean exist = isExist(filename);
            //已经上传过，直接返回文件地址
            //文件地址就是文件名称
            if (exist) {
                String photoId = RandomUtil.getRandomFileName() + suffix;
                mediaDTO.setSkip(true);
                mediaDTO.setMediaUrl(Constants.NOTE_PIC_URL + photoId);
                mediaDTO.setUploadedChunks(null);
                mediaDTO.setUploadedChunkNum(mediaBodyVO.getTotalChunks());
                mediaDTO.setMediaMd5(mediaBodyVO.getMd5());
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
                mediaDTO.setMediaMd5((String) map.get("filename"));
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
        mediaDTO.setSkip(list.size() == mediaBodyVO.getTotalChunks());
        mediaDTO.setUploadedChunks(list);
        mediaDTO.setUploadedChunkNum(list.size());
        mediaDTO.setMediaMd5(mediaBodyVO.getMd5());
        mediaDTO.setTotalChunks(mediaBodyVO.getTotalChunks());
        Map<String, Object> map = redisComponent.getFileInfoFromTemp(fileMD5);
        String photoId = (String) map.get("photoId");
        String url = photoId + suffix;
        url = Constants.NOTE_PIC_URL + url;
        map.putIfAbsent("url", url);
        mediaDTO.setMediaUrl(url);
        redisComponent.setFileInfo2Temp(fileMD5, map);
        //异步合并
        if (currentChunk == (mediaBodyVO.getTotalChunks() - 1)) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    String filename = fileMD5 + suffix;
                    fileComponent.transferFile(fileMD5, filename, type, false);
                }
            });
        }
        return mediaDTO;
    }

    private Boolean isExist(String fileName) {
        File file = new File(fileName);
        return file.exists();
    }

    private Boolean deleteFolder(File file) {
        if (!file.exists()) {
            log.error(file.getName() + "不存在");
            return false;
        }
        try {
            File[] subFiles = file.listFiles();
            assert subFiles != null;
            for (File f : subFiles) {
                if (f.isDirectory()) {
                    deleteFolder(f);
                }
                boolean res = f.delete();
                System.out.println(f.getName() + "删除" + (res ? "成功" : "失败"));
            }
            return file.delete();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void chunk2RedisTemp(MediaBodyVO mediaBodyVO) {
        MultipartFile file = mediaBodyVO.getFile();
        assert file.getOriginalFilename() != null;
        String suffix = mediaBodyVO.getName().substring(mediaBodyVO.getName().lastIndexOf(".") + 1);
        String fileMD5 = mediaBodyVO.getMd5();
        String photoId = RandomUtil.getRandomFileName();
        String filename = fileMD5 + suffix;
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
        map.put("photoId", photoId);
        redisComponent.setFileInfo2Temp(fileMD5, map);
    }

    public void chunk2MediaTemp(MediaBodyVO mediaBodyVO) {
        String type = mediaBodyVO.getType();
        MultipartFile multipartFile = mediaBodyVO.getFile();
        File file = getFile(mediaBodyVO, multipartFile, type);
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            multipartFile.transferTo(file);
        } catch (Exception e) {
            log.error("文件保存失败", e);
            throw new BusinessException("文件保存失败");
        }
    }

    private File getFile(MediaBodyVO mediaBodyVO, MultipartFile multipartFile, String type) {
        assert multipartFile.getOriginalFilename() != null;
        String tempPath = type.equals(MediaType.VIDEO.getName()) ? Constants.TEMP_VIDEO_PATH : Constants.TEMP_IMAGE_PATH;
        File fileFolder = new File(tempPath);
        if (!fileFolder.exists()) {
            fileFolder.mkdirs();
        }
        String filename = tempPath + mediaBodyVO.getMd5() + "/" + mediaBodyVO.getCurrentChunk();
        return new File(filename);
    }

    public String uploadAvatar(String userId, MultipartFile file) throws IOException {
        if (file == null) {
            throw new BusinessException("文件不存在");
        }
        String fileSuffix = "jpg";
        String filename = userId + "." + fileSuffix;
        File fileFolder = new File(Constants.FULL_HEADER_IMAGE_PATH);
        if (!fileFolder.exists()) {
            fileFolder.mkdirs();
        }
        //文件写入
        File file2 = new File(Constants.FULL_HEADER_IMAGE_PATH, filename);
        file.transferTo(file2);
        return Constants.HEAD_PIC_URL + filename;
    }

    public void download(HttpServletResponse response, String filename) {
        readFile(response, filename);
    }
    public void downloadAvatar(HttpServletResponse response, String filename) {
        readFile(response, filename);
    }

    public void getDefaultAvatar(HttpServletResponse response) {
        File file = new File(Constants.FULL_HEADER_IMAGE_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
        readFile(response, Constants.FULL_HEADER_IMAGE_PATH + Constants.DEFAULT_AVATAR);
    }

    @Async
    public void transferFile(String fileMD5, String filename, String type, Boolean deleteSource) {
        String tempName = type.equals(MediaType.VIDEO.getName()) ? Constants.TEMP_VIDEO_PATH : Constants.TEMP_IMAGE_PATH + "/" + fileMD5;
        String targetName = type.equals(MediaType.VIDEO.getName()) ? Constants.FULL_VIDEO_PATH : Constants.FULL_POST_IMAGE_PATH + "/" + filename;
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
                int len;
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
            if (deleteSource && file.exists()) {
                deleteFolder(file);
            }
        }
    }

    public void getPostPicture(String filename, HttpServletResponse response) {
        if (StringUtil.isEmpty(filename)) {
            throw new BusinessException(StatusCode.BAD_REQUEST);
        }
        String filenameWithoutSuffix = filename.split("\\.")[0];
        String suffix = filename.substring(filename.lastIndexOf("."));
        String mediaMd5 = mediaInfoService.getMediaMd5ByPhotoId(filenameWithoutSuffix);
        filename = Constants.FULL_POST_IMAGE_PATH + mediaMd5+suffix;
        download(response, filename);
    }
}