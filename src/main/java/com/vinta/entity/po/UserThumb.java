package com.vinta.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

/**
 * 
 * @TableName user_thumb
 */
@TableName(value ="user_thumb")
@Data
@Builder
public class UserThumb implements Serializable {
    /**
     * 点赞id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 点赞用户id
     */
    private String userId;

    /**
     * 点赞的笔记id
     */
    private String postId;

    /**
     * 
     */
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}