package com.nt.service_BASF.Impl;

import com.nt.dao_BASF.Environment;
import com.nt.service_BASF.EnvironmentServices;
import com.nt.service_BASF.mapper.EnvironmentMapper;
import com.nt.utils.dao.TokenModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF.Impl
 * @ClassName:BASF10301ServicesImpl
 * @Author: WXL
 * @Description: BASF数据监控模块实现类
 * @Date: 2019/11/14 15:50
 * @Version: 1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class EnvironmentServicesImpl implements EnvironmentServices {

    private static Logger log = LoggerFactory.getLogger(DeviceInformationServicesImpl.class);

    @Autowired
    private EnvironmentMapper environmentMapper;

    /**
     * @param Environment
     * @Method list
     * @Author WXL
     * @Version 1.0
     * @Description 获取监控数据列表
     * @Return java.util.List<Environment>
     * @Date 2019/11/14 15:54
     */
    @Override
    public List<Environment> list() throws Exception {
        Environment environment = new Environment();
        return environmentMapper.select(environment);
    }


    /**
     * @param environment
     * @param tokenModel
     * @Method insert
     * @Author Wxz
     * @Version 1.0
     * @Description 创建监控数据
     * @Return void
     * @Date 2019/11/14 15:55
     */
    @Override
    public void insert(Environment environment, TokenModel tokenModel) throws Exception {
        environment.preInsert(tokenModel);
        environment.setEnvironmentid(UUID.randomUUID().toString());
        environmentMapper.insert(environment);
    }


    /**
     * @param environment
     * @Method Delete
     * @Author WXL
     * @Version 1.0
     * @Description 删除监控数据
     * @Return void
     * @Date 2019/11/14 16：06
     */
    @Override
    public void delete(Environment environment) throws Exception {
        //逻辑删除（status -> "1"）
        environmentMapper.updateByPrimaryKeySelective(environment);
    }


    /**
     * @param environmentid
     * @Method one
     * @Author Wxz
     * @Version 1.0
     * @Description 获取数据监控详情
     * @Return com.nt.dao_BASF.Environment
     * @Date 2019/11/12 11：07
     */
    @Override
    public Environment one(String environmentid) throws Exception {
        return environmentMapper.selectByPrimaryKey(environmentid);
    }


    /**
     * @param environment
     * @param tokenModel
     * @Method update
     * @Author Wxz
     * @Version 1.0
     * @Description 更新接警单详情
     * @Return void
     * @Date 2019/11/12 11：07
     */
    @Override
    public void update(Environment environment, TokenModel tokenModel) throws Exception {
        environment.preUpdate(tokenModel);
        environmentMapper.updateByPrimaryKey(environment);

    }

}
