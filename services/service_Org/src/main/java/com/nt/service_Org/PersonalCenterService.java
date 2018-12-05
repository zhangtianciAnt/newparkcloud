package com.nt.service_Org;

import Vo.PersonalCenter;
import com.nt.dao_Org.CustomerInfo;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.service_Org
 * @ClassName: 基本信息的相关Services
 * @Description: java类作用描述
 * @Author: FEIJIALIANG
 * @CreateDate: 2018/12/05
 * @Version: 1.0
 */
public interface PersonalCenterService {

    // 获取基本信息
    CustomerInfo get(CustomerInfo customerInfo) throws Exception;

    // 更新或保存基本信息
    void save(PersonalCenter personalCenter) throws Exception;

}
