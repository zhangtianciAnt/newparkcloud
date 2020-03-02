package com.nt.service_BASF.Impl;

import com.nt.dao_BASF.Application;
import com.nt.dao_BASF.Deviceinformation;
import com.nt.service_BASF.DeviceInformationServices;
import com.nt.service_BASF.MapBox_MapLevelServices;
import com.nt.service_BASF.mapper.ApplicationMapper;
import com.nt.service_BASF.mapper.DeviceinformationMapper;
import com.nt.service_BASF.mapper.MapBox_MapLevelMapper;
import com.nt.utils.AuthConstants;
import com.nt.utils.dao.TokenModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.NotWritablePropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.util.*;

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
public class DeviceInformationServicesImpl implements DeviceInformationServices {

    private static Logger log = LoggerFactory.getLogger(DeviceInformationServicesImpl.class);

    @Autowired
    private DeviceinformationMapper deviceinformationMapper;

    @Autowired
    private MapBox_MapLevelServices mapBox_mapLevelServices;

    @Autowired
    private ApplicationMapper applicationMapper;

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
//        Deviceinformation deviceinformation = new Deviceinformation();
//        deviceinformation = new Deviceinformation();
        List<Deviceinformation> deviceinformationList = deviceinformationMapper.select(deviceinformation);
        //移除虚拟路桩信息
        for (Iterator<Deviceinformation> iterator = deviceinformationList.iterator(); iterator.hasNext(); ) {
            if (iterator.next().getDevicetype().equals("BC004006")) {
                iterator.remove();
            }
        }
        return deviceinformationList;
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
    public void insert(Deviceinformation deviceinformation, TokenModel tokenModel, String type) throws Exception {
        if (type.equals("Barricades")) {
            deviceinformation.preInsert(tokenModel);
            deviceinformation.setStatus("1");
            deviceinformationMapper.insert(deviceinformation);
        } else {
            deviceinformation.preInsert(tokenModel);
            deviceinformation.setDeviceinformationid(UUID.randomUUID().toString());
            deviceinformationMapper.insert(deviceinformation);
        }
    }

    /**
     * @param deviceinformation
     * @Method Delete
     * @Author SKAIXX
     * @Version 1.0
     * @Description 删除设备
     * @Return void
     * @Date 2019/11/5 15:31
     */
    @Override
    public void delete(Deviceinformation deviceinformation) throws Exception {
        //逻辑删除（status -> "1"）
        deviceinformationMapper.updateByPrimaryKeySelective(deviceinformation);
    }

    /**
     * @param deviceid
     * @Method one
     * @Author SKAIXX
     * @Version 1.0
     * @Description 获取设备详情
     * @Return com.nt.dao_BASF.Deviceinformation
     * @Date 2019/11/5 15:53
     */
    @Override
    public Deviceinformation one(String deviceid) throws Exception {
        return deviceinformationMapper.selectByPrimaryKey(deviceid);
    }

    /**
     * @param deviceinformation
     * @param tokenModel
     * @Method update
     * @Author SKAIXX
     * @Version 1.0
     * @Description 更新设备详情
     * @Return void
     * @Date 2019/11/5 16:07
     */
    @Override
    public void update(Deviceinformation deviceinformation, TokenModel tokenModel) throws Exception {
        deviceinformation.preUpdate(tokenModel);
        deviceinformationMapper.updateByPrimaryKeySelective(deviceinformation);
    }

    /**
     * @Method deviceList
     * @Author 王哲
     * @Version 1.0
     * @Description 查询设备列表（GIS专用）
     * @Return Deviceinformation
     * @Date 2019/12/12 14:36
     */
    @Override
    public List<Deviceinformation> deviceList(String mapid, String[] devicetype, String[] devicetypesmall, String devicename, Integer pageindex, Integer pagesize) throws Exception {
        if (Arrays.asList(devicetype).contains("BC004006")) {
            //查询更新路障信息
            Deviceinformation deviceinformation0 = new Deviceinformation();
            deviceinformation0.setDevicetype("BC004006");
            deviceinformation0.setStatus("0");
            for (Deviceinformation deviceinformation : deviceinformationMapper.select(deviceinformation0)) {
                deviceinformation.setStatus(AuthConstants.DEL_FLAG_DELETE);
                deviceinformationMapper.updateByPrimaryKeySelective(deviceinformation);
            }
            //获取符合要求的虚拟路障的设备信息表的id
            List<Application> applicationList = applicationMapper.selectBarricades();
            //更新路障信息
            for (Application application : applicationList) {
                Deviceinformation deviceinformation = new Deviceinformation();
                deviceinformation.setDeviceinformationid(application.getDeviceinformationid());
                deviceinformation.setStatus("0");
                deviceinformationMapper.updateByPrimaryKeySelective(deviceinformation);
            }
        }
        List<String> mapidList = mapBox_mapLevelServices.getChildrensStr(mapid);
        return deviceinformationMapper.selectDeviceList(mapidList, devicetype, devicetypesmall, devicename, pageindex, pagesize);

    }
}
