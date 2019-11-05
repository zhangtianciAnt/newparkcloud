package com.nt.service_BASF.Impl;

import com.nt.dao_BASF.Usergroup;
import com.nt.service_BASF.BASF10103Services;
import com.nt.service_BASF.mapper.UsergroupMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF.Impl
 * @ClassName: BASF10103ServicesImpl
 * @Author: SUN
 * @Description: BASF用户组模块实现类
 * @Date: 2019/11/4 16:30
 * @Version: 1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BASF10103ServicesImpl implements BASF10103Services {

    private static Logger log = LoggerFactory.getLogger(BASF10103ServicesImpl.class);

    @Autowired
    private UsergroupMapper usergroupMapper;

    /**
     * @param usergroup
     * @Method list
     * @Author SUN
     * @Version 1.0
     * @Description
     * @Return java.util.List<Usergroup>
     * @Date 2019/11/4 16:35
     */
    @Override
    public List<Usergroup> list(Usergroup usergroup) throws Exception {
        return usergroupMapper.select(usergroup);
    }
}
