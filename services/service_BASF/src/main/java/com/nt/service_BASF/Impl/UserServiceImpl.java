package com.nt.service_BASF.Impl;

import com.nt.dao_BASF.User;
import com.nt.service_BASF.UserService;
import com.nt.service_BASF.mapper.UserMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> get(User user) throws Exception {
        return UserMapper.select(user);
    }

    @Override
    public void insert(TokenModel tokenModel, User user) throws Exception {
        user.preInsert(tokenModel);
        user.userid(UUID.randomUUID().toString());
        UserMapper.insert(user);
    }

    @Override
    public void update(TokenModel tokenModel, User user) throws Exception {
        user.preUpdate(tokenModel);
        userMapper.updateByPrimaryKey(user);
    }

    @Override
    public void del(TokenModel tokenModel, User user) throws Exception {
        user.preUpdate(tokenModel);
        userMapper.updateByPrimaryKeySelective(user);
    }
}
