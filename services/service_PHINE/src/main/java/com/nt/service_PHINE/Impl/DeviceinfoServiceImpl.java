package com.nt.service_PHINE.Impl;

import com.nt.dao_PHINE.Vo.DeviceListVo;
import com.nt.service_PHINE.DeviceinfoService;
import com.nt.service_PHINE.mapper.DeviceinfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
     * @Method getDeviceListByProjectId
     * @Author MYT
     * @Description 根据项目ID获取设备列表信息
     * @Date 2020/1/31 15:27
     * @Param projectid 项目ID
     **/
    @Override
    public List<DeviceListVo> getDeviceListByProjectId(String projectid) {
        // 执行数据查询
        List<DeviceListVo> deviceList = deviceinfoMapper.getDeviceListByProjectId(projectid);
        // 按照设备ID和设备地址分组，重构数据库数据
        Map<String, List<String>> tmpMap = new HashMap<String, List<String>>();
        for(DeviceListVo vo : deviceList){
            if(tmpMap.containsKey(vo.getDeviceid().concat(",").concat(vo.getMachineroomaddress()))){
                tmpMap.get(vo.getDeviceid().concat(",").concat(vo.getMachineroomaddress())).add(vo.getBoardid());
            }else{//map中不存在，新建key，用来存放数据
                List<String> tmpList = new ArrayList<String>();
                tmpList.add(vo.getBoardid());
                tmpMap.put(vo.getDeviceid().concat(",").concat(vo.getMachineroomaddress()), tmpList);
            }
        }
        // 重新构建返回值
        List<DeviceListVo> rtnDeviceList = new ArrayList<DeviceListVo>();
        Set<Map.Entry<String,List<String>>> set = tmpMap.entrySet();
        for(Map.Entry<String,List<String>> entry:set){
            DeviceListVo deviceListVo = new DeviceListVo();
            String key[] =entry.getKey().split(",");
            deviceListVo.setDeviceid(key[0]);
            deviceListVo.setMachineroomaddress(key[1]);
            deviceListVo.setBoardList(entry.getValue());
            rtnDeviceList.add(deviceListVo);
        }
        // 结果集按照设备ID排序
        Collections.sort(rtnDeviceList);

        return rtnDeviceList;
    }

    /**
     * @Method getDeviceListByCompanyId
     * @Author MYT
     * @Description 根据企业ID获取设备列表信息
     * @Date 2020/1/31 15:27
     * @Param companyid 企业ID
     **/
    @Override
    public List<DeviceListVo> getDeviceListByCompanyId(String companyid) {
        return deviceinfoMapper.getDeviceListByCompanyId(companyid);
    }
}
