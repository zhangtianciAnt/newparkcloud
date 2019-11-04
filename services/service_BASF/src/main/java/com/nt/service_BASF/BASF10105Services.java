package com.nt.service_BASF;

import com.nt.dao_BASF.Deviceinformation;

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
public interface BASF10105Services {

    //获取设备列表
    public List<Deviceinformation> list(Deviceinformation deviceinformation) throws Exception;
}
