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
    /**
     * 笔记id
     */
    @TableId
    private String postId;

    /**
     * 作者id
     */
    private String userId;

    /**
     * 笔记描述/标题
     */
    private String description;

    /**
     * 笔记文字内容
     */
    private String textContent;

    /**
     * 发布时间
     */
    private Date postTime;

    /**
     * 作品评论数
     */
    private Long commentsNum;

    /**
     * 作品点赞数
     */
    private Long thumbNum;

    /**
     * 作品收藏数
     */
    private Long collectNum;

    /**
     * 作品包含的音乐信息
     */
    private String musicLink;

    /**
     * 作品的分类
     */
    private Integer category;
    /**
     * 作品的分类
     */
    private String tags;

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
            && (this.getTags() == null ? other.getTags() == null : this.getTags().equals(other.getTags()));
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
        sb.append("]");
        return sb.toString();
    }
}