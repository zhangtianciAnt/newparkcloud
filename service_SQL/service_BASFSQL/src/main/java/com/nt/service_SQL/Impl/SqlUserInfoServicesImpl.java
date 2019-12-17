package com.nt.service_SQL.Impl;


import com.nt.dao_SQL.SqlUserInfo;
import com.nt.service_SQL.SqlUserInfoServices;
import com.nt.service_SQL.sqlMapper.SqlUserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(rollbackFor = Exception.class)
public class SqlUserInfoServicesImpl implements SqlUserInfoServices {

    @Autowired
    private SqlUserInfoMapper sqlUserInfoMapper;

    @Override
    public List<SqlUserInfo> list( ) throws Exception {
        return sqlUserInfoMapper.userinfolist();
    }
}
