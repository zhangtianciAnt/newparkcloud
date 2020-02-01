package com.nt.service_PHINE.mapper;

import com.nt.dao_PHINE.Deviceinfo;
import com.nt.dao_PHINE.Vo.DeviceListVo;
import com.nt.utils.MyMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DeviceinfoMapper extends MyMapper<Deviceinfo> {

    // 项目创建画面获取设备列表
    List<DeviceListVo> getDeviceList();
}
