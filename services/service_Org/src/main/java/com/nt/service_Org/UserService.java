package com.nt.service_Org;

import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.OrgTree;
import com.nt.dao_Org.UserAccount;
import com.nt.utils.dao.TokenModel;

import java.util.List;

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

    //获取用户
    List<UserAccount> getUserAccount(UserAccount userAccount) throws Exception;

    // 增加用户
    void inUserAccount(UserAccount userAccount) throws Exception;

    // 更新用户
    void upUserAccount(UserAccount userAccount) throws Exception;

    // 用户登陆
    TokenModel login(UserAccount userAccount) throws Exception;

    //获取客户信息
    List<CustomerInfo> getCustomerInfo(CustomerInfo customerInfo)throws Exception;

    // 添加客户信息（公司/个人（个人用户包括注册用户及个人客户））
    void inCustomerInfo(CustomerInfo customerInfo) throws Exception;

    // 更新客户信息（公司/个人（个人用户包括注册用户及个人客户））
    void upCustomerInfo(CustomerInfo customerInfo) throws Exception;

}
