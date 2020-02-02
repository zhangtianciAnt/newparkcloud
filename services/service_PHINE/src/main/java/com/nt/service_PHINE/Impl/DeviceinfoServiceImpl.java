package com.nt.service_PHINE.Impl;

import com.nt.dao_PHINE.Vo.DeviceListVo;
import com.nt.service_PHINE.DeviceinfoService;
import com.nt.service_PHINE.mapper.DeviceinfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.service_PHINE.Impl
 * @ClassName: DeviceinfoServiceImpl
 * @Description: 设备信息实现类
 * @Author: SKAIXX
 * @CreateDate: 2020/1/30
 * @Version: 1.0
 */
@Service
public class DeviceinfoServiceImpl implements DeviceinfoService {

    @Autowired
    private DeviceinfoMapper deviceinfoMapper;

    /**
     * @return List<ProjectListVo>平台项目信息列表
     * @Method getProjectInfoList
     * @Author SKAIXX
     * @Description 平台项目画面获取项目列表
     * @Date 2020/1/30 15:27
     * @Param TODO:权限部分暂未实装
     **/
    @Override
    public List<DeviceListVo> getDeviceInfoList() {
        return deviceinfoMapper.getDeviceInfoList();
    }

    /**
     * @Method getDeviceList
     * @Author MYT
     * @Description 获取设备列表信息
     * @Date 2020/1/31 15:27
     * @Param TODO:未传递登录用户的企业ID参数
     **/
    @Override
    public List<DeviceListVo> getDeviceList() {
        return deviceinfoMapper.getDeviceList();
    }
}
