package com.vinta.enums;

import lombok.Getter;

@Getter
public enum CodeEnum {
    EMAIL_CODE(1,"email-code"),
    PICTURE_CODE(0,"picture-code");
    private final Integer type;
    private final String desc;
    CodeEnum(Integer type,String desc){
        this.desc=desc;
        this.type=type;
    }
}
