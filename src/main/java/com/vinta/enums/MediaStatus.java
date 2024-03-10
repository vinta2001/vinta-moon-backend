package com.vinta.enums;

import com.vinta.exception.BusinessException;
import lombok.Getter;

@Getter
public enum MediaStatus {

    SCHEDULED(0, "已预约"),
    PUBLISHED(1, "已发布"),
    DRAFT(2, "草稿"),
    DELETED(3, "已删除");

    private final Integer status;
    private final String desc;

    MediaStatus(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static Integer getStatusByDesc(String status) {
        for (MediaStatus mediaStatus : MediaStatus.values()) {
            if (mediaStatus.getDesc().equals(status)) {
                return mediaStatus.getStatus();
            }
        }
        throw new BusinessException(StatusCode.BAD_REQUEST);
    }
}
