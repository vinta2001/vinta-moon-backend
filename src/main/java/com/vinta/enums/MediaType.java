package com.vinta.enums;

import lombok.Getter;

@Getter
public enum MediaType {

    IMAGE(1, "image"),
    VIDEO(2, "video");

    private final Integer type;
    private final String name;

    MediaType(Integer type, String name){
        this.type = type;
        this.name = name;
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



}
