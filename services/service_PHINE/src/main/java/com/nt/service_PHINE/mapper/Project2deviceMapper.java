package com.nt.service_PHINE.mapper;

import com.nt.dao_PHINE.Project2device;
import com.nt.dao_PHINE.Vo.DeviceListVo;
import com.nt.utils.MyMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface Project2deviceMapper extends MyMapper<Project2device> {

    // 平台项目管理画面根据项目ID获取设备列表
    List<DeviceListVo> getDeviceIdByProjectId(String projectid);
}
