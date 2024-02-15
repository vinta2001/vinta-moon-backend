package com.vinta.utils;

import com.vinta.exception.BusinessException;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Getter
@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "project.file")
@Slf4j
public class FileUtil {

    private static String IMAGE_PATH;
    private static String VIDEO_PATH;
    private static String AUDIO_PATH;
    private static String ROOT_PATH;
    private static String TEMP_PATH;

    private static final String DEFAULT_AVATAR = "default_avatar.jpg";


    @Value("${project.file.tempPath}")
    public void setTempPath(String tempPath) {
        TEMP_PATH = ROOT_PATH + tempPath;
    }

    @Value("${project.file.imagePath}")
    public void setImagePath(String imagePath) {
        IMAGE_PATH = ROOT_PATH + imagePath;
    }

    @Value("${project.file.videoPath}")
    public void setVideoPath(String videoPath) {
        VIDEO_PATH = ROOT_PATH + videoPath;
    }

    @Value("${project.file.audioPath}")
    public void setAudioPath(String audioPath) {
        AUDIO_PATH = ROOT_PATH + audioPath;
    }

    @Value("${project.file.rootPath}")
    public void setRootPath(String rootPath) {
        ROOT_PATH = rootPath;
    }

    public static void readFile(HttpServletResponse response, String filePath) {
        response.setContentType("image/*");
        FileInputStream fileInputStream = null;
        OutputStream fileOutputStream = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                throw new BusinessException("文件不存在");
            }
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

    public static String upload(MultipartFile file) throws IOException {
        if (file == null) {
            throw new BusinessException("文件不存在");
        }
        String fileSuffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        String filename = RandomUtil.getRandomFileName() + "." + fileSuffix;
        File fileFolder = new File(IMAGE_PATH);
        if (!fileFolder.exists()) {
            fileFolder.mkdirs();
        }
        //文件写入
        File file2 = new File(IMAGE_PATH, filename);
        file.transferTo(file2);
        return filename;
    }

    public static String upload(String userId, MultipartFile file) throws IOException {
        if (file == null) {
            throw new BusinessException("文件不存在");
        }
        String fileSuffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        String filename = userId + "." + fileSuffix;
        File fileFolder = new File(IMAGE_PATH);
        if (!fileFolder.exists()) {
            fileFolder.mkdirs();
        }
        //文件写入
        File file2 = new File(IMAGE_PATH, filename);
        file.transferTo(file2);
        return filename;
    }

    public static void download(HttpServletResponse response,String filePath) {
        readFile(response, filePath);
    }
    public static void getDefaultProfile(HttpServletResponse response) {
        File file = new File(IMAGE_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
        readFile(response, IMAGE_PATH + DEFAULT_AVATAR);
    }
}
