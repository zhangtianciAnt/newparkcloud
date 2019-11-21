package com.nt.service_BASF.Impl;

import com.nt.dao_BASF.Emergencyplan;
import com.nt.service_BASF.EmergencyplanServices;
import com.nt.service_BASF.mapper.EmergencyplanMapper;
import com.nt.utils.dao.TokenModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF.Impl
 * @ClassName:BASF10802ServicesImpl
 * @Author: Y
 * @Description: BASF数据监控模块实现类
 * @Date: 2019/11/18 17:15
 * @Version: 1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class EmergencyplanServicesImpl implements EmergencyplanServices {

    private static Logger log = LoggerFactory.getLogger(EmergencyplanServicesImpl.class);

    @Autowired
    private EmergencyplanMapper emergencyplanMapper;

    /**
     * @param Emergencyplan
     * @Method list
     * @Author Y
     * @Version 1.0
     * @Description 获取模板数据列表
     * @Return java.util.List<Environment>
     * @Date 2019/11/18 17:17
     */

    @Override
    public List<Emergencyplan> list() throws Exception {
        Emergencyplan emergencyplan = new Emergencyplan();
        return emergencyplanMapper.select(emergencyplan);
    }

    /**
     * @param Emergencyplan
     * @param tokenModel
     * @Method insert
     * @Author Y
     * @Version 1.0
     * @Description 创建应急预案
     * @Return void
     * @Date 2019/11/18 17:18
     */

    @Override
    public void insert(Emergencyplan emergencyplan, TokenModel tokenModel) throws Exception {
        emergencyplan.preInsert(tokenModel);
        emergencyplan.setEmergencyplanid(UUID.randomUUID().toString());
        emergencyplan.setCreationtime(new Date());
        emergencyplanMapper.insert(emergencyplan);
    }

    /**
     * @param emergencyplan
     * @Method Delete
     * @Author Y
     * @Version 1.0
     * @Description 删除应急预案
     * @Return void
     * @Date 2019/11/18 17：22
     */

    @Override
    public void delete(Emergencyplan emergencyplan) throws Exception {
        //逻辑删除（status -> "1"）
        emergencyplanMapper.updateByPrimaryKeySelective(emergencyplan);

    }

    /**
     * @param emergencyplanid
     * @Method one
     * @Author Y
     * @Version 1.0
     * @Description 获取应急预案详情
     * @Return com.nt.dao_BASF.Emergencyplan
     * @Date 2019/11/18 17：24
     */

    @Override
    public Emergencyplan one(String emergencyplanid) throws Exception {
        return emergencyplanMapper.selectByPrimaryKey(emergencyplanid);
    }


    /**
     * @param emergencyplan
     * @param tokenModel
     * @Method update
     * @Author Y
     * @Version 1.0
     * @Description 更新模板详情
     * @Return void
     * @Date 2019/11/18 17：24
     */

    @Override
    public void update(Emergencyplan emergencyplan, TokenModel tokenModel) throws Exception {
        emergencyplan.preUpdate(tokenModel);
        emergencyplanMapper.updateByPrimaryKey(emergencyplan);
    }
}

