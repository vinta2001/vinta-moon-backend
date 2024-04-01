package com.vinta.entity.dto;

import lombok.Data;

import java.util.Date;

@Data
public class UserDTO {
    private String userNickname;
    private String avatar;
    private Long follow;
    private Long follower;
    private Integer level;
    private String profile;
    private Long great;
    private Date birthday;
    private Integer gender;
    private String description;
    private String userName;
    private String userId;
}
