package com.vinta.entity.vo;


import com.vinta.entity.po.UserInfo;
import lombok.Data;

@Data
public class UserResultVO {
    private String userId;
    private String nickName;
    private String avatar;

    public UserResultVO(UserInfo userInfo) {
        this.userId = userInfo.getUserId();
        this.nickName = userInfo.getUserNickname();
        this.avatar = userInfo.getHead();
    }
}
