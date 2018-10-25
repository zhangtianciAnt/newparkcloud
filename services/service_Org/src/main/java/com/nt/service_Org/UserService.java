package com.nt.service_Org;

import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.OrgTree;
import com.nt.dao_Org.UserAccount;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.service_Org
 * @ClassName: 用户的相关Services
 * @Description: java类作用描述
 * @Author: SKAIXX
 * @CreateDate: 2018/10/25
 * @UpdateUser: SKAIXX
 * @UpdateDate: 2018/10/25
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public interface UserService {

    // 用户注册
    void register(UserAccount userAccount, CustomerInfo customerInfo) throws Exception;

    // 添加客户信息（公司/个人（个人用户包括注册用户及个人客户））
    void saveCustomerInfo(CustomerInfo customerInfo) throws Exception;
}
