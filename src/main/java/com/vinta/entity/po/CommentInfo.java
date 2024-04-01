package com.vinta.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName comment_info
 */
@TableName(value ="comment_info")
@Data
public class CommentInfo implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer commentId;

    /**
     * 笔记的id
     */
    private String postId;

    /**
     * 评论人的id
     */
    private String commenterId;

    /**
     * 回复的id
     */
    private Integer replyId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 1 可用 0不可用
     */
    private Integer status;

    /**
     * 评论时间
     */
    private Date createTime;

    /**
     * 
     */
    private String location;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}