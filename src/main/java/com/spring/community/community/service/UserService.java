package com.spring.community.community.service;

import com.spring.community.community.mapper.UserMapper;
import com.spring.community.community.model.User;
import com.spring.community.community.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public void createOrUpdate(User user) {
        //整合了mybatis就要这么做，其实下面三行就是找出accountId符合要求的用户数据
        UserExample userExample = new UserExample();
        userExample.createCriteria().andAccountIdEqualTo(user.getAccountId());
        List<User> users = userMapper.selectByExample(userExample);
        if (users.size()==0){
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        }else{
            User dbUser = users.get(0);
            User updateUser = new User();
            updateUser.setGmtModified(System.currentTimeMillis());
            updateUser.setAvatarUrl(user.getAvatarUrl());
            updateUser.setName(user.getName());
            updateUser.setToken(user.getToken());
            UserExample example = new UserExample();
            example.createCriteria().andIdEqualTo(dbUser.getId());//额，其实我觉得可以直接用dbUser,不用新建一个updateUser啊
            userMapper.updateByExampleSelective(updateUser,example);

        }
    }
}
