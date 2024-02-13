package com.vinta.entity.po;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName user_info
 */
@Data
public class UserInfo implements Serializable {
    /**
     * 用户唯一id
     */
    private String userId;

    /**
     * 用户的平台账户
     */
    private String userName;

    /**
     * 用户登录账号
     */
    private String email;

    /**
     * 用户的昵称
     */
    private String userNickname;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 用户个性签名与自我介绍
     */
    private String description;

    /**
     * 0：未知  1：男  2：女
     */
    private Integer gender;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 地址
     */
    private String region;

    /**
     * 职业
     */
    private String career;

    /**
     * 学校
     */
    private String school;

    /**
     * 背景图链接
     */
    private String profile;

    /**
     * 用户等级
     */
    private Integer level;

    /**
     * 头像链接
     */
    private String head;

    /**
     * 用户的粉丝
     */
    private Long follower;

    /**
     * 用户的关注
     */
    private Long follow;

    /**
     * 用户获得的点赞数
     */
    private Long great;

    /**
     * 上次登录时间
     */
    private Date lastLoginTime;

    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        UserInfo other = (UserInfo) that;
        return (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getUserName() == null ? other.getUserName() == null : this.getUserName().equals(other.getUserName()))
            && (this.getEmail() == null ? other.getEmail() == null : this.getEmail().equals(other.getEmail()))
            && (this.getUserNickname() == null ? other.getUserNickname() == null : this.getUserNickname().equals(other.getUserNickname()))
            && (this.getPassword() == null ? other.getPassword() == null : this.getPassword().equals(other.getPassword()))
            && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
            && (this.getGender() == null ? other.getGender() == null : this.getGender().equals(other.getGender()))
            && (this.getBirthday() == null ? other.getBirthday() == null : this.getBirthday().equals(other.getBirthday()))
            && (this.getRegion() == null ? other.getRegion() == null : this.getRegion().equals(other.getRegion()))
            && (this.getCareer() == null ? other.getCareer() == null : this.getCareer().equals(other.getCareer()))
            && (this.getSchool() == null ? other.getSchool() == null : this.getSchool().equals(other.getSchool()))
            && (this.getProfile() == null ? other.getProfile() == null : this.getProfile().equals(other.getProfile()))
            && (this.getLevel() == null ? other.getLevel() == null : this.getLevel().equals(other.getLevel()))
            && (this.getHead() == null ? other.getHead() == null : this.getHead().equals(other.getHead()))
            && (this.getFollower() == null ? other.getFollower() == null : this.getFollower().equals(other.getFollower()))
            && (this.getFollow() == null ? other.getFollow() == null : this.getFollow().equals(other.getFollow()))
            && (this.getGreat() == null ? other.getGreat() == null : this.getGreat().equals(other.getGreat()))
            && (this.getLastLoginTime() == null ? other.getLastLoginTime() == null : this.getLastLoginTime().equals(other.getLastLoginTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getUserName() == null) ? 0 : getUserName().hashCode());
        result = prime * result + ((getEmail() == null) ? 0 : getEmail().hashCode());
        result = prime * result + ((getUserNickname() == null) ? 0 : getUserNickname().hashCode());
        result = prime * result + ((getPassword() == null) ? 0 : getPassword().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getGender() == null) ? 0 : getGender().hashCode());
        result = prime * result + ((getBirthday() == null) ? 0 : getBirthday().hashCode());
        result = prime * result + ((getRegion() == null) ? 0 : getRegion().hashCode());
        result = prime * result + ((getCareer() == null) ? 0 : getCareer().hashCode());
        result = prime * result + ((getSchool() == null) ? 0 : getSchool().hashCode());
        result = prime * result + ((getProfile() == null) ? 0 : getProfile().hashCode());
        result = prime * result + ((getLevel() == null) ? 0 : getLevel().hashCode());
        result = prime * result + ((getHead() == null) ? 0 : getHead().hashCode());
        result = prime * result + ((getFollower() == null) ? 0 : getFollower().hashCode());
        result = prime * result + ((getFollow() == null) ? 0 : getFollow().hashCode());
        result = prime * result + ((getGreat() == null) ? 0 : getGreat().hashCode());
        result = prime * result + ((getLastLoginTime() == null) ? 0 : getLastLoginTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", userId=").append(userId);
        sb.append(", userName=").append(userName);
        sb.append(", email=").append(email);
        sb.append(", userNickname=").append(userNickname);
        sb.append(", password=").append(password);
        sb.append(", description=").append(description);
        sb.append(", gender=").append(gender);
        sb.append(", birthday=").append(birthday);
        sb.append(", region=").append(region);
        sb.append(", career=").append(career);
        sb.append(", school=").append(school);
        sb.append(", profile=").append(profile);
        sb.append(", level=").append(level);
        sb.append(", head=").append(head);
        sb.append(", follower=").append(follower);
        sb.append(", follow=").append(follow);
        sb.append(", great=").append(great);
        sb.append(", lastLoginTime=").append(lastLoginTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}