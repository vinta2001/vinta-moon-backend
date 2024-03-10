package com.vinta.enums;

import com.vinta.exception.BusinessException;
import lombok.Getter;

@Getter
public enum MediaAccess {

    PUBLIC(1, "public"),
    PRIVATE(0, "private");

    private final Integer access;
    private final String desc;

    MediaAccess(Integer access, String desc) {
        this.access = access;
        this.desc = desc;
    }

    public static Integer getAccessByDesc(String desc) {
        for (MediaAccess mediaAccess : MediaAccess.values()) {
            if (mediaAccess.getDesc().equals(desc)) {
                return mediaAccess.getAccess();
            }
        }
        throw new BusinessException(StatusCode.BAD_REQUEST);
    }
}
