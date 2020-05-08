package com.nt.service_BASF;

import com.nt.dao_BASF.Deviceinformation;
import com.nt.dao_BASF.Switchnotifications;
import com.nt.utils.dao.TokenModel;

import java.util.List;


public interface SwitchnotificationsServices {

    //获取主备服务通知表
    List<Switchnotifications> list(Switchnotifications switchnotifications) throws Exception;

    //删除主备服务通知表
    void delete(Switchnotifications switchnotifications) throws Exception;


}
