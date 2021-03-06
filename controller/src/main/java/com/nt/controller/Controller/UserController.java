package com.nt.controller.Controller;

import cn.hutool.http.HttpUtil;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.Vo.UserVo;
import com.nt.dao_Org.Log;
import com.nt.dao_Org.UserAccount;
import com.nt.service_Org.LogService;
import com.nt.service_Org.UserService;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.controller.Controller
 * @ClassName: UserController
 * @Description: 用户相关操作Controller
 * @Author: SKAIXX
 * @CreateDate: 2018/10/25
 * @UpdateUser: SKAIXX
 * @UpdateDate: 2018/10/25
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private LogService logService;

    //注册
    @RequestMapping(value = "/register", method = {RequestMethod.POST})
    public ApiResult addUser(@RequestBody UserAccount userAccount) throws Exception {
        if (userAccount == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.PARAM_ERR_02));
        }
        userAccount.preInsert();
        userService.inUserAccount(userAccount);
        return ApiResult.success();
    }

    //获取用户账户
    @RequestMapping(value = "/getCurrentUserAccount", method = {RequestMethod.POST})
    public ApiResult getCurrentUserAccount(@RequestBody UserAccount userAccount) throws Exception {
        if (userAccount == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.PARAM_ERR_02));
        }

        var list = userService.getUserAccount(userAccount);
        return ApiResult.success(list.size());
    }

    //登陆
    @RequestMapping(value = "/login", method = {RequestMethod.POST})
    public ApiResult login(@RequestBody UserAccount userAccount, HttpServletRequest request) throws Exception {
        try {
            if (userAccount == null) {
                return ApiResult.fail(MessageUtil.getMessage(MsgConstants.PARAM_ERR_02));
            }
            TokenModel tokenModel = userService.login(userAccount);

            var log = new Log();
            log.setType(AuthConstants.LOG_TYPE_LOGIN);
            var logs = new Log.Logs();
            //logs.setIp(HttpUtil.getClientIP(request));
            logs.setEquipment(AuthConstants.LOG_EQUIPMENT_PC);
            log.setLogs(new ArrayList<Log.Logs>());
            log.getLogs().add(logs);
            log.preInsert(tokenModel);
            logService.save(log);

            return ApiResult.success(tokenModel);
        } catch (LogicalException ex) {
            return ApiResult.fail(ex.getMessage());
        }

    }

    //获取当前用户信息
    @RequestMapping(value = "/getCurrentUserInfo", method = {RequestMethod.POST})
    public ApiResult getCurrentUserInfo(HttpServletRequest request) throws Exception {
        String userId = RequestUtils.CurrentUserId(request);
        var customerInfo = new CustomerInfo();
        customerInfo.setUserid(userId);
        return ApiResult.success(userService.getCustomerInfo(customerInfo));
    }

    //创建当前用户用户信息
    @RequestMapping(value = "/inCurrentUserInfo", method = {RequestMethod.POST})
    public ApiResult inCurrentUserInfo(@RequestBody CustomerInfo customerInfo, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        customerInfo.setUserid(tokenModel.getUserId());
        customerInfo.preInsert(tokenModel);
        userService.inCustomerInfo(customerInfo);
        return ApiResult.success();
    }

    //更新当前用户用户信息
    @RequestMapping(value = "/upCurrentUserInfo", method = {RequestMethod.POST})
    public ApiResult upCurrentUserInfo(@RequestBody CustomerInfo customerInfo, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        customerInfo.setUserid(tokenModel.getUserId());
        customerInfo.preUpdate(tokenModel);
        userService.upCustomerInfo(customerInfo);
        return ApiResult.success();
    }

    //当前用户申请成为正式租户
    @RequestMapping(value = "/applyCurrentUserTenantId", method = {RequestMethod.POST})
    public ApiResult applyTenantId(HttpServletRequest request) throws Exception {
        var userAccount = new UserAccount();
        userAccount.setUserid(RequestUtils.CurrentUserId(request));
        val userAccountlist = userService.getUserAccount(userAccount);
        if (userAccountlist.size() <= 0) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.USER_ERR_01));
        } else if (userAccountlist.get(0).getStatus().equals(AuthConstants.APPLYTENANTID)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.USER_ERR_02));
        } else {
            TokenModel tokenModel = tokenService.getToken(request);
            userAccountlist.get(0).setStatus(AuthConstants.APPLYTENANTID);
            userAccountlist.get(0).preUpdate(tokenModel);
            userService.upUserAccount(userAccountlist.get(0));
        }
        return ApiResult.success();
    }

    /**
     * @方法名：addAccountCustomer
     * @描述：添加用户账号及用户信息
     * @创建日期：2018/12/06
     * @作者：ZHANGYING
     * @参数：[userVo, request]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/addAccountCustomer", method = {RequestMethod.POST})
    public ApiResult addAccountCustomer(@RequestBody UserVo userVo, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        if (userVo.getUserAccount().getCreateon() != null && userVo.getUserAccount().getCreateby() != null) {
            userVo.getUserAccount().preUpdate(tokenModel);
        } else {
            userVo.getUserAccount().preInsert(tokenModel);
        }

        if (userVo.getCustomerInfo().getCreateon() != null && userVo.getCustomerInfo().getCreateby() != null) {
            userVo.getCustomerInfo().preUpdate(tokenModel);
        } else {
            userVo.getCustomerInfo().preInsert(tokenModel);
        }
        return ApiResult.success(userService.addAccountCustomer(userVo));
    }

    /**
     * @方法名：getAccountCustomer
     * @描述：根据orgid获取用户账号及用户信息
     * @创建日期：2018/12/06
     * @作者：ZHANGYING
     * @参数：[orgid, orgtype, request]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/getAccountCustomer", method = {RequestMethod.GET})
    public ApiResult getAccountCustomer(String orgid, String orgtype, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(userService.getAccountCustomer(orgid, orgtype));
    }

    /**
     * @方法名：getAccountCustomerById
     * @描述：根据用户id获取用户账号及用户信息
     * @创建日期：2018/12/06
     * @作者：ZHANGYING
     * @参数：[userid, request]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/getAccountCustomerById", method = {RequestMethod.GET})
    public ApiResult getAccountCustomerById(String userid, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(userService.getAccountCustomerById(userid));
    }

    /**
     * @方法名：mobileCheck
     * @描述：验证手机号是否重复
     * @创建日期：2018/12/06
     * @作者：ZHANGYING
     * @参数：[id, mobileCheck]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/mobileCheck", method = {RequestMethod.GET})
    public ApiResult mobileCheck(String id, String mobilenumber, HttpServletRequest request) throws Exception {
        try {
            TokenModel tokenModel = tokenService.getToken(request);
            userService.mobileCheck(id, mobilenumber);
            return ApiResult.success();
        } catch (LogicalException e) {
            return ApiResult.fail(e.getMessage());
        }
    }

    /**
     * @方法名：updUserStatus
     * @描述：更新用户状态
     * @创建日期：2018/12/06
     * @作者：ZHANGYING
     * @参数：[userid, status, request]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/updUserStatus", method = {RequestMethod.GET})
    public ApiResult updUserStatus(String userid, String status, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        userService.updUserStatus(userid, status);
        return ApiResult.success();
    }

    /**
     * @方法名：setRoleToUser
     * @描述：给用户赋角色
     * @创建日期：2018/12/07
     * @作者：ZHANGYING
     * @参数：[userAccount, request]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/setRoleToUser", method = {RequestMethod.POST})
    public ApiResult setRoleToUser(@RequestBody UserAccount userAccount, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        userService.setRoleToUser(userAccount);
        return ApiResult.success();
    }

    /**
     * @方法名：updUserInfo
     * @描述：更新微信端用户信息
     * @创建日期：2018/12/14
     * @作者：ZHANGYING
     * @参数：[customerInfo, request]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/updUserInfo", method = {RequestMethod.POST})
    public ApiResult updUserInfo(@RequestBody CustomerInfo customerInfo) throws Exception {
        if (customerInfo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.PARAM_ERR_02));
        }

        return ApiResult.success(userService.updUserInfo(customerInfo));
    }

    /**
     * @方法名：getWxById0
     * @描述：微信端根据用户id获取用户账号及用户信息
     * @创建日期：2018/12/06
     * @作者：ZHANGYING
     * @参数：[userid, request]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/getWxById", method = {RequestMethod.GET})
    public ApiResult getWxById(String userid, HttpServletRequest request) throws Exception {
        // zqu start
//        return ApiResult.success(userService.getWxById(userid));
        return ApiResult.success();
        // zqu end
    }

    /**
     * 获取外部用户数据
     */
    @RequestMapping(value = "/getBasicUserInfo", method = {RequestMethod.POST})
    public ApiResult getBasicUserInfo(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(userService.getBasicUserInfo(tokenModel.getTenantId()));
    }
}
