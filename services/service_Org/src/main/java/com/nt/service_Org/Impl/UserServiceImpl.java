package com.nt.service_Org.Impl;

import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.UserAccount;
import com.nt.service_Org.UserService;
import org.springframework.stereotype.Service;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.service_Org.Impl
 * @ClassName: UserServiceImpl
 * @Description: java类作用描述
 * @Author: SKAIXX
 * @CreateDate: 2018/10/25
 * @UpdateUser: SKAIXX
 * @UpdateDate: 2018/10/25
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Service
public class UserServiceImpl implements UserService {

    /**
     * @方法名：register
     * @描述：用户注册
     * @创建日期：2018/10/25
     * @作者：SKAIXX
     * @参数：[userAccount, customerInfo]
     * @返回值：void
     */
    @Override
    public void register(UserAccount userAccount, CustomerInfo customerInfo) throws Exception {
        // region 注册验证

        // endregion
    }

    /**
     * @方法名：saveCustomerInfo
     * @描述：添加客户信息（公司/个人（个人用户包括注册用户及个人客户））
     * @创建日期：2018/10/25
     * @作者：SKAIXX
     * @参数：[customerInfo]
     * @返回值：void
     */
    @Override
    public void saveCustomerInfo(CustomerInfo customerInfo) throws Exception {

    }
}
