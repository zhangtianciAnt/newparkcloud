package com.nt.service_BASF;

import com.nt.dao_BASF.Usergroup;

import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF
 * @ClassName: BASF10103Services
 * @Author: SUN
 * @Description: BASF用户组模块接口
 * @Date: 2019/11/4 16:09
 * @Version: 1.0
 */
public interface BASF10103Services {

    //获取设备列表
    public List<Usergroup> list(Usergroup usergroup) throws Exception;
}
