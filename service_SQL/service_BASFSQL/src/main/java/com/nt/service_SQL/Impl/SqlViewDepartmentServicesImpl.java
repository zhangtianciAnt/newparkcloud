package com.nt.service_SQL.Impl;


import com.nt.dao_SQL.SqlAPBCardHolder;
import com.nt.dao_SQL.SqlUserInfo;
import com.nt.dao_SQL.SqlViewDepartment;
import com.nt.service_SQL.SqlUserInfoServices;
import com.nt.service_SQL.SqlViewDepartmentServices;
import com.nt.service_SQL.sqlMapper.SqlUserInfoMapper;
import com.nt.service_SQL.sqlMapper.SqlViewDepartmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@Transactional(rollbackFor = Exception.class)
public class SqlViewDepartmentServicesImpl implements SqlViewDepartmentServices {

    @Autowired
    private SqlViewDepartmentMapper sqlViewDepartmentMapper;


    @Override
    public List<SqlViewDepartment> sqllist() throws Exception {
        return sqlViewDepartmentMapper.list();
    }
}
