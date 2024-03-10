package com.vinta.entity.vo;

import com.vinta.constant.Constants;
import com.vinta.entity.po.UserInfo;
import com.vinta.utils.StringUtil;
import lombok.Data;

@Data
public class UserResultVO {
    private String userId;
    private String nickName;
    private String avatar;

    public UserResultVO(UserInfo userInfo) {
        this.userId = userInfo.getUserId();
        this.nickName = userInfo.getUserNickname();
        this.avatar = StringUtil.hasContent(userInfo.getAvatar()) ? Constants.HOST+userInfo.getAvatar() : Constants.FULL_HEAD_PIC_URL + userInfo.getUserId() + ".jpg";
    }
}
