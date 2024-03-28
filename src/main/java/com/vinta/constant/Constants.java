package com.vinta.constant;

public class Constants {
    public static final Integer USERNAME_LENGTH = 20;
    public static final Integer CODE_LENGTH = 5;
    public static final String EMAIL_CODE = "emailCode";
    public static final String USER_TOKEN_KEY = "userToken";


    // 系统文件路径配置

    public static final String ROOT_PATH = "D:/vintaMoon/file/";
    public static final String VIDEO_PATH = "video/";
    public static final String IMAGE_PATH = "image/";
    public static final String AUDIO_PATH = "audio/";
    public static final String POST_IMAGE_PATH = "post/";
    public static final String HEADER_IMAGE_PATH = "header/";
    public static final String TEMP_PATH = "temp/";

    // 完整文件
    public static final String FULL_IMAGE_PATH = ROOT_PATH + IMAGE_PATH;
    public static final String FULL_POST_IMAGE_PATH = FULL_IMAGE_PATH + POST_IMAGE_PATH;
    public static final String FULL_HEADER_IMAGE_PATH = FULL_IMAGE_PATH + HEADER_IMAGE_PATH;
    public static final String FULL_VIDEO_PATH = ROOT_PATH + VIDEO_PATH;
    public static final String FULL_AUDIO_PATH = ROOT_PATH + AUDIO_PATH;

    // 临时文件
    public static final String TEMP_IMAGE_PATH = FULL_IMAGE_PATH + TEMP_PATH;
    public static final String TEMP_POST_IMAGE_PATH = FULL_POST_IMAGE_PATH + TEMP_PATH;
    public static final String TEMP_HEADER_IMAGE_PATH = FULL_HEADER_IMAGE_PATH + TEMP_PATH;
    public static final String TEMP_VIDEO_PATH = FULL_VIDEO_PATH + TEMP_PATH;
    public static final String TEMP_AUDIO_PATH = FULL_AUDIO_PATH + TEMP_PATH;

    public static final String HOST = "http://localhost:9090/api";
    public static final String NOTE_PIC_URL = "/post/pic/download/";
    public static final String HEAD_PIC_URL = "/user/avatar/download/";
    public static final String DEFAULT_AVATAR = "default_avatar.jpg";

    public static final String FULL_NOTE_PIC_URL = HOST + NOTE_PIC_URL;
    public static final String FULL_HEAD_PIC_URL = HOST + HEAD_PIC_URL;

    public static final String THUMB_UP_SUCCESS = "点赞成功";
    public static final String THUMB_CANCEL_SUCCESS = "取消成功";



}
