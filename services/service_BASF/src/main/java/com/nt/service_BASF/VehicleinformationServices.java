package com.nt.service_BASF;

import com.nt.dao_BASF.VO.*;
import com.nt.dao_BASF.Vehicleinformation;
import com.nt.utils.dao.TokenModel;

import java.util.List;
import java.util.Map;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF
 * @ClassName: VehicleinformationServices
 * @Author: Wxz
 * @Description: VehicleInformationServices
 * @Date: 2019/11/14 13:11
 * @Version: 1.0
 */
public interface VehicleinformationServices {

    //获取车辆列表
    List<Vehicleinformation> list()throws Exception;

    //获取车辆列表(危化品车辆数用)
    //Map<Integer,String> getlistinformation()throws Exception;
    List<VehicleinformationVo> getlistinformation()throws Exception;

    //更新车辆信息
    void update(Vehicleinformation vehicleinformation, TokenModel tokenModel)throws Exception;

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

    //创建车辆进出厂信息
    String insert(Vehicleinformation vehicleinformation) throws Exception;

    //定时查询车辆信息表（出场时间为空的数据）
    List<VehicleinformationGpsArrVo> getQueryVehiclesRegularlyInfo() throws Exception;
	
	
    //更新车辆gps信息
    void updategps(String vehicleinformationid,String gps,String speed)throws Exception;

    //更新车辆出场时间
    void updateouttime(Vehicleinformation vehicleinformation)throws Exception;
}
