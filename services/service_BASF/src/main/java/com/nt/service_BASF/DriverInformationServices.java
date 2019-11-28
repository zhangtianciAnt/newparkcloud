package com.nt.service_BASF;

import com.nt.dao_BASF.DriverInformation;
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
public interface DriverInformationServices {

    //获取驾驶员列表
    List<DriverInformation> list(DriverInformation driverInformation) throws Exception;

    //查询是否为黑名单
    boolean checkblack(DriverInformation driverInformation) throws Exception;

    //更新驾驶员信息
    void update(DriverInformation driverInformation, TokenModel tokenModel) throws Exception;
}
