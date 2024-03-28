package com.vinta.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vinta.constant.Constants;
import com.vinta.entity.po.PostInfo;
import com.vinta.entity.po.UserInfo;
import com.vinta.entity.po.UserThumb;
import com.vinta.mapper.PostInfoMapper;
import com.vinta.mapper.UserInfoMapper;
import com.vinta.service.UserThumbService;
import com.vinta.mapper.UserThumbMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
* @author VINTA
* @description 针对表【user_thumb】的数据库操作Service实现
* @createDate 2024-03-28 19:41:22
*/
@Service
public class UserThumbServiceImpl extends ServiceImpl<UserThumbMapper, UserThumb>
    implements UserThumbService{

    @Resource
    private UserThumbMapper userThumbMapper;

    @Resource
    private PostInfoMapper postInfoMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    @Transactional
    public String changeStatus(String userId, String postId) {

        UserThumb userThumb = userThumbMapper.selectByUserIdAndPostId(userId, postId);
        if (userThumb == null) {
            addThumb(userId, postId);
            return Constants.THUMB_UP_SUCCESS;
        }
        else {
            removeThumb(userId, postId);
            return Constants.THUMB_CANCEL_SUCCESS;
        }
    }

    public void addThumb(String userId, String postId) {
        UserThumb thumb = UserThumb.builder().userId(userId).postId(postId).createTime(new Date()).build();
        userThumbMapper.insert(thumb);

        PostInfo postInfo = postInfoMapper.selectById(postId);
        Long thumbNum = postInfo.getThumbNum();
        thumbNum += 1;
        postInfo.setThumbNum(thumbNum);
        postInfoMapper.updateById(postInfo);

        UserInfo userInfo = userInfoMapper.selectById(userId);
        thumbNum = userInfo.getGreat();
        thumbNum += 1;
        userInfo.setGreat(thumbNum);
        userInfoMapper.updateById(userInfo);
    }

    public void removeThumb(String userId, String postId) {
        userThumbMapper.deleteByUserIdAndPostId(userId, postId);

        PostInfo postInfo = postInfoMapper.selectById(postId);
        Long thumbNum = postInfo.getThumbNum();
        thumbNum -= 1;
        postInfo.setThumbNum(thumbNum);
        postInfoMapper.updateById(postInfo);

        UserInfo userInfo = userInfoMapper.selectById(userId);
        thumbNum = userInfo.getGreat();
        thumbNum -= 1;
        userInfo.setGreat(thumbNum);
        userInfoMapper.updateById(userInfo);
    }

}




