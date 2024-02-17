package com.vinta.entity.po;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 
 * @TableName media_info
 */
@Data
public class MediaInfo implements Serializable {
    /**
     * 图片的id
     */
    @TableId
    private String photoId;

    /**
     * 作品的id
     */

    private String postId;

    /**
     * 发布用户的id
     */
    private String userId;

    /**
     * 图片保存地址
     */
    private String photoUrl;

    /**
     * 创建时间
     */
    private Date postTime;

    /**
     * 媒体类型：0 图片  1 视频
     */
    private Integer mediaType;

    /**
     * 文件的md5值
     */
    private String mediaMd5;

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
        MediaInfo other = (MediaInfo) that;
        return (this.getPhotoId() == null ? other.getPhotoId() == null : this.getPhotoId().equals(other.getPhotoId()))
            && (this.getPostId() == null ? other.getPostId() == null : this.getPostId().equals(other.getPostId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getPhotoUrl() == null ? other.getPhotoUrl() == null : this.getPhotoUrl().equals(other.getPhotoUrl()))
            && (this.getPostTime() == null ? other.getPostTime() == null : this.getPostTime().equals(other.getPostTime()))
            && (this.getMediaType() == null ? other.getMediaType() == null : this.getMediaType().equals(other.getMediaType()))
            && (this.getMediaMd5() == null ? other.getMediaMd5() == null : this.getMediaMd5().equals(other.getMediaMd5()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getPhotoId() == null) ? 0 : getPhotoId().hashCode());
        result = prime * result + ((getPostId() == null) ? 0 : getPostId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getPhotoUrl() == null) ? 0 : getPhotoUrl().hashCode());
        result = prime * result + ((getPostTime() == null) ? 0 : getPostTime().hashCode());
        result = prime * result + ((getMediaType() == null) ? 0 : getMediaType().hashCode());
        result = prime * result + ((getMediaMd5() == null) ? 0 : getMediaMd5().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", photoId=").append(photoId);
        sb.append(", postId=").append(postId);
        sb.append(", userId=").append(userId);
        sb.append(", photoUrl=").append(photoUrl);
        sb.append(", postTime=").append(postTime);
        sb.append(", mediaType=").append(mediaType);
        sb.append(", mediaMd5=").append(mediaMd5);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}