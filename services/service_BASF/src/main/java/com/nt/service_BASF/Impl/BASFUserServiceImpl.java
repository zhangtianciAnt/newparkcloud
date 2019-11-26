package com.nt.service_BASF.Impl;
/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF.Impl
 * @ClassName: BASFUserServiceImpl
 * @Author: LXY
 * @Description: 人员通讯录接口实现类
 * @Date: 2019/11/06
 * @Version: 1.0
 */
import com.nt.dao_BASF.BASFUser;
import com.nt.service_BASF.BASFUserService;
import com.nt.service_BASF.mapper.BASFUserMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BASFUserServiceImpl implements BASFUserService {

    @Autowired
    private BASFUserMapper userMapper;

    @Override
    public List<BASFUser> get(BASFUser user) throws Exception {
        return userMapper.select(user);
    }

    @Override
    public void insert(TokenModel tokenModel, BASFUser user) throws Exception {
        user.preInsert(tokenModel);
        user.setUserid(UUID.randomUUID().toString());
        userMapper.insert(user);
    }

    @Override
    public void update(TokenModel tokenModel, BASFUser user) throws Exception {
        user.preUpdate(tokenModel);
        userMapper.updateByPrimaryKey(user);
    }

    @Override
    public void del(TokenModel tokenModel, BASFUser user) throws Exception {
        user.preUpdate(tokenModel);
        userMapper.updateByPrimaryKeySelective(user);
    }
}
