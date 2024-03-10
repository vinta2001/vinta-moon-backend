package com.vinta.entity.po;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 
 * @TableName post_info
 */
@Data
public class PostInfo implements Serializable {

    @TableId
    private String postId;
    private String userId;
    private String description;
    private String textContent;
    private Date postTime;
    private Long commentsNum;
    private Long thumbNum;
    private Long collectNum;
    private String musicLink;
    private Integer category;
    private String tags;
    private String location;
    private Integer status;
    private Date createTime;
    private Integer access;

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
        PostInfo other = (PostInfo) that;
        return (this.getPostId() == null ? other.getPostId() == null : this.getPostId().equals(other.getPostId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
            && (this.getTextContent() == null ? other.getTextContent() == null : this.getTextContent().equals(other.getTextContent()))
            && (this.getPostTime() == null ? other.getPostTime() == null : this.getPostTime().equals(other.getPostTime()))
            && (this.getCommentsNum() == null ? other.getCommentsNum() == null : this.getCommentsNum().equals(other.getCommentsNum()))
            && (this.getThumbNum() == null ? other.getThumbNum() == null : this.getThumbNum().equals(other.getThumbNum()))
            && (this.getCollectNum() == null ? other.getCollectNum() == null : this.getCollectNum().equals(other.getCollectNum()))
            && (this.getMusicLink() == null ? other.getMusicLink() == null : this.getMusicLink().equals(other.getMusicLink()))
            && (this.getCategory() == null ? other.getCategory() == null : this.getCategory().equals(other.getCategory()))
            && (this.getTags() == null ? other.getTags() == null : this.getTags().equals(other.getTags()))
            && (this.getLocation() == null ? other.getLocation() == null : this.getLocation().equals(other.getLocation()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getAccess() == null ? other.getAccess() == null : this.getAccess().equals(other.getAccess()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getPostId() == null) ? 0 : getPostId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getTextContent() == null) ? 0 : getTextContent().hashCode());
        result = prime * result + ((getPostTime() == null) ? 0 : getPostTime().hashCode());
        result = prime * result + ((getCommentsNum() == null) ? 0 : getCommentsNum().hashCode());
        result = prime * result + ((getThumbNum() == null) ? 0 : getThumbNum().hashCode());
        result = prime * result + ((getCollectNum() == null) ? 0 : getCollectNum().hashCode());
        result = prime * result + ((getMusicLink() == null) ? 0 : getMusicLink().hashCode());
        result = prime * result + ((getCategory() == null) ? 0 : getCategory().hashCode());
        result = prime * result + ((getTags() == null) ? 0 : getTags().hashCode());
        result = prime * result + ((getLocation() == null) ? 0 : getLocation().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getAccess() == null) ? 0 : getAccess().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", postId=").append(postId);
        sb.append(", userId=").append(userId);
        sb.append(", desc=").append(description);
        sb.append(", textContent=").append(textContent);
        sb.append(", postTime=").append(postTime);
        sb.append(", commentsNum=").append(commentsNum);
        sb.append(", thumbNum=").append(thumbNum);
        sb.append(", collectNum=").append(collectNum);
        sb.append(", musicLink=").append(musicLink);
        sb.append(", category=").append(category);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append(", tags=").append(tags);
        sb.append(", location=").append(location);
        sb.append(", status=").append(status);
        sb.append(", createTime=").append(createTime);
        sb.append(", access=").append(access);
        sb.append("]");
        return sb.toString();
    }
}