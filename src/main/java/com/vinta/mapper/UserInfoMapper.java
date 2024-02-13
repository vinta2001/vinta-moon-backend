package com.vinta.mapper;

import com.vinta.entity.po.UserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author VINTA
* @description 针对表【user_info】的数据库操作Mapper
* @createDate 2024-02-06 14:23:00
* @Entity com.vinta.entity.po.UserInfo
*/
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    UserInfo login(String email);
}




