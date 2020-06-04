package com.nt.service_BASF;

import com.nt.dao_BASF.Deviceinformation;
import com.nt.dao_BASF.VO.DeviceinformationListVo;
import com.nt.utils.dao.TokenModel;

import java.lang.reflect.Array;
import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF
 * @ClassName: BASF10105Services
 * @Author: SKAIXX
 * @Description: BASF设备管理模块接口
 * @Date: 2019/11/4 16:09
 * @Version: 1.0
 */
public interface DeviceInformationServices {

    //获取设备列表
    List<Deviceinformation> list(Deviceinformation deviceinformation) throws Exception;

    //创建设备(type用于区分虚拟设备路障)
    void insert(Deviceinformation deviceinformation, TokenModel tokenModel, String type) throws Exception;

    //删除设备
    void delete(Deviceinformation deviceinformation) throws Exception;

    //获取设备详情
    Deviceinformation one(String deviceid) throws Exception;

    //更新设备详情
    void update(Deviceinformation deviceinformation, TokenModel tokenModel) throws Exception;

    //查询设备列表（GIS专用）
    DeviceinformationListVo deviceList(String mapid, String[] devicetype, String[] devicetypesmall, String devicename, Integer pageindex, Integer pagesize) throws Exception;
}
