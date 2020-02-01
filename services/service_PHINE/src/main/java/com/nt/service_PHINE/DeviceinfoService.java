package com.nt.service_PHINE;

import com.nt.dao_PHINE.Vo.DeviceListVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.service_PHINE
 * @ClassName: DeviceinfoService
 * @Description: 设备信息Service接口
 * @Author: SKAIXX
 * @CreateDate: 2020/1/30
 * @Version: 1.0
 */
@Service
public interface DeviceinfoService {

    // 设备一览画面获取设备列表
    List<DeviceListVo> getDeviceInfoList();
}
