package com.nt.service_Org;

import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.UserAccount;
import com.nt.dao_Org.Vo.UserVo;
import com.nt.utils.ApiResult;
import com.nt.utils.LogicalException;
import com.nt.utils.dao.TokenModel;

import java.util.List;
import java.util.Map;

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
    TokenModel login(UserAccount userAccount,String locale) throws Exception;

    //获取客户信息
    List<CustomerInfo> getCustomerInfo(CustomerInfo customerInfo) throws Exception;

    // 添加客户信息（公司/个人（个人用户包括注册用户及个人客户））
    void inCustomerInfo(CustomerInfo customerInfo) throws Exception;

    // 更新客户信息（公司/个人（个人用户包括注册用户及个人客户））
    void upCustomerInfo(CustomerInfo customerInfo) throws Exception;

    // 添加用户及用户信息
    String addAccountCustomer(UserVo userVo) throws Exception;

    // 根据orgid获取用户及用户信息列表
    List<CustomerInfo> getAccountCustomer(String orgid, String orgtype) throws Exception;

    // 根据用户id获取用户信息
    UserVo getAccountCustomerById(String userid) throws Exception;

    // 验证手机号是否重复
    void mobileCheck(String id, String mobilenumber) throws Exception;

    // 更新用户状态
    void updUserStatus(String userid, String status) throws Exception;

    // 给用户赋角色
    void setRoleToUser(UserAccount userAccount) throws Exception;

    // 微信端用获取用户信息
    UserVo updUserInfo(CustomerInfo customerInfo) throws Exception;

    /**
     * 微信userId登录
     *
     * @param weChatUserId 微信userId
     * @return
     * @throws Exception
     */
    TokenModel wxLogin(String weChatUserId) throws Exception;

    // 微信端根据用户id获取信息
    Map<String, Object> getWxById(String userid) throws Exception;

    List<CustomerInfo> getAllCustomerInfo();

}
