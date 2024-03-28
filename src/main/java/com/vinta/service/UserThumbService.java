package com.vinta.service;

import com.vinta.entity.po.UserThumb;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author VINTA
* @description 针对表【user_thumb】的数据库操作Service
* @createDate 2024-03-28 19:41:22
*/
public interface UserThumbService extends IService<UserThumb> {

    String changeStatus(String userId, String postId);
}
