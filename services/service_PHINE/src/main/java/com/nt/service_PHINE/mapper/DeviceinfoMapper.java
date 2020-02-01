package com.nt.service_PHINE.mapper;

import com.nt.dao_PHINE.Deviceinfo;
import com.nt.dao_PHINE.Vo.DeviceListVo;
import com.nt.utils.MyMapper;

import java.util.List;

public interface DeviceinfoMapper extends MyMapper<Deviceinfo> {

    // 设备一览画面获取设备列表
    List<DeviceListVo> getDeviceInfoList();
}
