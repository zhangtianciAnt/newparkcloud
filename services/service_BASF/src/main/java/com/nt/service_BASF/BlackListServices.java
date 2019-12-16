package com.nt.service_BASF;

import com.nt.dao_BASF.BlackList;
import com.nt.utils.dao.TokenModel;

import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF
 * @ClassName: DriverInformationServices
 * @Author: Wxz
 * @Description: DriverInformationServices
 * @Date: 2019/11/22 15:00
 * @Version: 1.0
 */
public interface BlackListServices {

    //获取黑名单列表
    List<BlackList> list(BlackList blackList) throws Exception;

    //查询是否为黑名单
    boolean checkblack(String driveridnum) throws Exception;

    //删除驾驶员黑名单信息
    void delete(BlackList blackList) throws Exception;
}
