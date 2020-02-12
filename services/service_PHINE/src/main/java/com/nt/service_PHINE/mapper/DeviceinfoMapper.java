package com.nt.service_PHINE.mapper;

import com.nt.dao_PHINE.Deviceinfo;
import com.nt.dao_PHINE.Vo.DeviceListVo;
import com.nt.utils.MyMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DeviceinfoMapper extends MyMapper<Deviceinfo> {

    // 设备一览画面获取设备列表
    List<DeviceListVo> getDeviceInfoList();

    // 项目创建页面根据项目ID获取设备信息列表
    List<DeviceListVo> getDeviceListByProjectId(String projectid);

    // 项目创建页面根据企业ID获取设备信息列表
    List<DeviceListVo> getDeviceListByCompanyId(String companyid);

    // 获取全部设备状态信息
    List<DeviceListVo> getAllDeviceStatus();

    // 获取通信操作设备信息
    List<DeviceListVo> getCommunicationDeviceInfo(String projectid);

    void updateCommunicationDeviceInfo(Deviceinfo deviceinfo);
}
