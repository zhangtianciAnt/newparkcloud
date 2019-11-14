package com.nt.service_BASF.Impl;

import com.nt.dao_BASF.Vehicletrajectory;
import com.nt.service_BASF.VehicletrajectoryServices;
import com.nt.service_BASF.mapper.VehicletrajectoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF.Impl
 * @ClassName: VehicletrajectoryServicesImpl
 * @Author: Wxz
 * @Description: VehicletrajectoryServicesImpl
 * @Date: 2019/11/14 14:26
 * @Version: 1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class VehicletrajectoryServicesImpl implements VehicletrajectoryServices {

    private static Logger log = LoggerFactory.getLogger(DeviceInformationServicesImpl.class);

    @Autowired
    private VehicletrajectoryMapper vehicletrajectoryMapper;

    /**
     * @param vehicleinformationid
     * @Method one
     * @Author Wxz
     * @Version 1.0
     * @Description 获取车辆轨迹信息详情
     * @Return com.nt.dao_BASF.vehicleinformationid
     * @Date 2019/11/14 16：16
     */
    @Override
    public Vehicletrajectory one(String vehicleinformationid) throws Exception {
        return vehicletrajectoryMapper.selectByPrimaryKey(vehicleinformationid);
    }
}
