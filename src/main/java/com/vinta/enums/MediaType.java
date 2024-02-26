package com.vinta.enums;

import lombok.Getter;

import java.lang.reflect.Array;
import java.util.ArrayList;

@Getter
public enum MediaType {

    IMAGE(1, "image", new String[]{".png", ".jpg", ".jpeg", ".gif"}),
    VIDEO(2, "video", new String[]{".mp4"}),;

    private final Integer type;
    private final String name;
    private final String[] suffix;

    MediaType(Integer type, String name,String[] suffix){
        this.type = type;
        this.name = name;
        this.suffix = suffix;
    }

    public static MediaType getMediaTypeByType(Integer type){
        for(MediaType mediaType : MediaType.values()){
            if(mediaType.getType().equals(type)){
                return mediaType;
            }
        }
        return null;
    }
    public static MediaType getMediaTypeByName(String name){
        for(MediaType mediaType : MediaType.values()){
            if(mediaType.getName().equals(name)){
                return mediaType;
            }
        }
        return null;
    }
    public static MediaType getMediaTypeBySuffix(String name) {
        for (MediaType mediaType : MediaType.values()) {
            for (String suffix : mediaType.getSuffix()) {
                if (suffix.equals(name)) {
                    return mediaType;
                }
            }
        }
        return null;
    }
}
