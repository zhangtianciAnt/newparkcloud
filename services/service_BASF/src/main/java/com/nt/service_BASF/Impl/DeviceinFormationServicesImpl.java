package com.nt.service_BASF.Impl;

import com.nt.dao_BASF.Deviceinformation;
import com.nt.service_BASF.DeviceinFormationServices;
import com.nt.service_BASF.mapper.DeviceinformationMapper;
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
 * @ClassName: BASF10105ServicesImpl
 * @Author: SKAIXX
 * @Description: BASF设备管理模块实现类
 * @Date: 2019/11/4 16:30
 * @Version: 1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DeviceinFormationServicesImpl implements DeviceinFormationServices {

    private static Logger log = LoggerFactory.getLogger(DeviceinFormationServicesImpl.class);

    @Autowired
    private DeviceinformationMapper deviceinformationMapper;

    /**
     * @param deviceinformation
     * @Method list
     * @Author SKAIXX
     * @Version 1.0
     * @Description 获取设备列表
     * @Return java.util.List<Deviceinformation>
     * @Date 2019/11/4 16:35
     */
    @Override
    public List<Deviceinformation> list(Deviceinformation deviceinformation) throws Exception {
        return deviceinformationMapper.select(deviceinformation);
    }

    /**
     * @param deviceinformation
     * @param tokenModel
     * @Method insert
     * @Author SKAIXX
     * @Version 1.0
     * @Description 创建设备
     * @Return void
     * @Date 2019/11/4 18:48
     */
    @Override
    public void insert(Deviceinformation deviceinformation, TokenModel tokenModel) throws Exception {
        deviceinformation.preInsert(tokenModel);
        deviceinformation.setDeviceinformationid(UUID.randomUUID().toString());
        deviceinformationMapper.insert(deviceinformation);
    }
}
