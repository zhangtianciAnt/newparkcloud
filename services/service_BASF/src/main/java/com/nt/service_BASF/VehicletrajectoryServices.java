package com.nt.service_BASF;

import com.nt.dao_BASF.Vehicletrajectory;

import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF
 * @ClassName: VehicletrajectoryServices
 * @Author: Wxz
 * @Description: VehicletrajectoryServices
 * @Date: 2019/11/14 14:14
 * @Version: 1.0
 */
public interface VehicletrajectoryServices {


    //获取车辆信息详情
    Vehicletrajectory one(String vehicleinformationid) throws Exception;
}
