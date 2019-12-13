package com.nt.service_SQL.impl;


import com.nt.dao_SQL.AccessLevel;
import com.nt.service_SQL.AccessLevelServices;
import com.nt.service_SQL.sqlMapper.AccessLevelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



@Service
@Transactional(rollbackFor = Exception.class)
public class AccessLevelServicesImpl implements AccessLevelServices {

    @Autowired
    private AccessLevelMapper accessLevelMapper;

    @Override
    public List<AccessLevel> list( ) throws Exception {
        return accessLevelMapper.selectList();
    }
}
