package com.nt.service_BASF.Impl;

import com.nt.dao_BASF.Highriskarea;
import com.nt.service_BASF.HighriskareaServices;
import com.nt.service_BASF.mapper.HighriskareaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF.Impl
 * @ClassName:BASF90921ServicesImpl
 * @Author: GJ
 * @Description: BASF高风险作业类
 * @Date: 2020/03/27 10:58
 * @Version: 1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class HighriskareaServicesImpl implements HighriskareaServices {

    private static Logger log = LoggerFactory.getLogger(DeviceInformationServicesImpl.class);

    @Autowired
    private HighriskareaMapper highriskareaMapper;
    /**
     * @Method list
     * @Author Wxz
     * @Version 1.0
     * @Description 获取高风险作业列表
     * @Return java.util.List<Firealarm>
     * @Date 2019/11/12 10:54
     */
    @Override
    public List<Highriskarea> list() throws Exception {
        Highriskarea highriskarea = new Highriskarea();
        return highriskareaMapper.select(highriskarea);
    }
}
