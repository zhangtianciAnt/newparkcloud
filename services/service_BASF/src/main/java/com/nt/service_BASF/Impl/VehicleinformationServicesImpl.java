package com.nt.service_BASF.Impl;

import com.nt.dao_BASF.VO.InsideVehicleTypeVo;
import com.nt.dao_BASF.VO.InsideVehicleinformationVo;
import com.nt.dao_BASF.VO.VehicleAccessStatisticsVo;
import com.nt.dao_BASF.Vehicleinformation;
import com.nt.service_BASF.VehicleinformationServices;
import com.nt.service_BASF.mapper.VehicleinformationMapper;
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
 * @ClassName: VehicleinformationServicesImpl
 * @Author: Wxz
 * @Description: VehicleinformationServicesImpl
 * @Date: 2019/11/14 13:23
 * @Version: 1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class VehicleinformationServicesImpl implements VehicleinformationServices {

    private static Logger log = LoggerFactory.getLogger(DeviceInformationServicesImpl.class);

    @Autowired
    private VehicleinformationMapper vehicleinformationMapper;

    /**
     * @param Vehicleinformation
     * @Method list
     * @Author Wxz
     * @Version 1.0
     * @Description 获取车辆信息列表
     * @Return java.util.List<Vehicleinformation>
     * @Date 2019/11/14 13：27
     */
    @Override
    public List<Vehicleinformation> list() throws Exception {
        Vehicleinformation vehicleinformation = new Vehicleinformation();
        return vehicleinformationMapper.select(vehicleinformation);
    }

    /**
     * @param vehicleinformation
     * @param tokenModel
     * @Method update
     * @Author Wxz
     * @Version 1.0
     * @Description 更新车辆信息详情
     * @Return void
     * @Date 2019/11/14 13：39
     */
    @Override
    public void update(Vehicleinformation vehicleinformation, TokenModel tokenModel) throws Exception {
        vehicleinformation.preUpdate(tokenModel);
        vehicleinformationMapper.updateByPrimaryKeySelective(vehicleinformation);
    }

    /**
     * @Method getInsideList
     * @Author SKAIXX
     * @Version  1.0
     * @Description 获取在场车辆信息一览
     * @param
     * @Return java.util.List<com.nt.dao_BASF.Vehicleinformation>
     * @Date 2019/12/17 11:35
     */
    @Override
    public List<InsideVehicleinformationVo> getInsideList() throws Exception {
        return vehicleinformationMapper.getInsideList();
    }

    /**
     * @Method getAccessStatistics
     * @Author SKAIXX
     * @Version  1.0
     * @Description 获取车辆出入统计
     * @param
     * @Return java.util.List<com.nt.dao_BASF.VO.VehicleAccessStatisticsVo>
     * @Date 2019/12/17 11:35
     */
    @Override
    public List<VehicleAccessStatisticsVo> getAccessStatistics() throws Exception {
        return vehicleinformationMapper.getAccessStatistics();
    }

    /**
     * @Method getDailyVehicleInfo
     * @Author SKAIXX
     * @Version  1.0
     * @Description 获取当日入场车辆信息
     * @param
     * @Return java.util.List<com.nt.dao_BASF.Vehicleinformation>
     * @Date 2019/12/17 11:35
     */
    @Override
    public List<Vehicleinformation> getDailyVehicleInfo() throws Exception {
        return vehicleinformationMapper.getDailyVehicleInfo();
    }

    /**
     * @Method getInsideVehicleType
     * @Author SKAIXX
     * @Version  1.0
     * @Description 获取在场车辆类别统计
     * @param
     * @Return java.util.List<com.nt.dao_BASF.VO.InsideVehicleTypeVo>
     * @Date 2019/12/17 11:35
     */
    @Override
    public List<InsideVehicleTypeVo> getInsideVehicleType() throws Exception {
        return vehicleinformationMapper.getInsideVehicleType();
    }

    /**
     * @param vehicleinformation
     * @param
     * @Method insert
     * @Author Sun
     * @Version 1.0
     * @Description 创建车辆进出厂信息
     * @Return void
     * @Date 2019/11/4 18:48
     */
    @Override
    public void insert(Vehicleinformation vehicleinformation) throws Exception {
        vehicleinformation.preInsert();
        vehicleinformation.setVehicleinformationid(UUID.randomUUID().toString());
        vehicleinformationMapper.insert(vehicleinformation);
    }

    /**
     * @Method getQueryVehiclesRegularlyInfo
     * @Author SKAIXX
     * @Version  1.0
     * @Description 定时查询车辆信息表（出场时间为空的数据）
     * @param
     * @Return java.util.List<com.nt.dao_BASF.Vehicleinformation>
     * @Date 2020/04/13 14:49
     */
    @Override
    public List<Vehicleinformation> getQueryVehiclesRegularlyInfo() throws Exception {
        return vehicleinformationMapper.getQueryVehiclesRegularlyInfo();
    }
}
