package com.nt.service_BASF.mapper;

import com.nt.dao_BASF.VO.InsideVehicleTypeVo;
import com.nt.dao_BASF.VO.InsideVehicleinformationVo;
import com.nt.dao_BASF.VO.VehicleAccessStatisticsVo;
import com.nt.dao_BASF.Vehicleinformation;
import com.nt.utils.MyMapper;

import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF.mapper
 * @ClassName: VehicleinformationMapper
 * @Author: Wxz
 * @Description: VehicleinformationMapper
 * @Date: 2019/11/14 13:25
 * @Version: 1.0
 */
public interface VehicleinformationMapper extends MyMapper<Vehicleinformation> {

    //获取在场车辆信息一览
    List<InsideVehicleinformationVo> getInsideList() throws Exception;

    //获取本月车辆出入统计
    List<VehicleAccessStatisticsVo> getAccessStatistics() throws Exception;

    //获取本周车辆出入统计
    List<VehicleAccessStatisticsVo> getWeekAccessStatistics() throws Exception;

    //获取当日入场车辆信息
    List<Vehicleinformation> getDailyVehicleInfo() throws Exception;

    //获取在场车辆类别统计
    List<InsideVehicleTypeVo> getInsideVehicleType() throws Exception;
}
