package com.nt.controller.Controller;

import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.UserAccount;
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
import java.util.List;

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

    //注册
    @RequestMapping(value = "/register",method={RequestMethod.POST})
    public ApiResult addUser(@RequestBody UserAccount userAccount) throws Exception {
        if (userAccount == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.PARAM_ERR_02));
        }
        userAccount.preInsert();
        userService.inUserAccount(userAccount);
        return ApiResult.success();
    }

    //登陆
    @RequestMapping(value = "/login",method={RequestMethod.POST})
    public ApiResult login(@RequestBody UserAccount userAccount) throws Exception {
        if (userAccount == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.PARAM_ERR_02));
        }
        userService.login(userAccount);
        return ApiResult.success();
    }

    //获取当前用户信息
    @RequestMapping(value = "/getCurrentUserInfo",method={RequestMethod.POST})
    public ApiResult getCurrentUserInfo(HttpServletRequest request) throws Exception {
        String userId = RequestUtils.CurrentUserId(request);
        val customerInfo = new CustomerInfo();
        customerInfo.setUserid(userId);
        return ApiResult.success(userService.getCustomerInfo(customerInfo));
    }

    //创建当前用户用户信息
    @RequestMapping(value = "/inCurrentUserInfo",method={RequestMethod.POST})
    public ApiResult inCurrentUserInfo(@RequestBody CustomerInfo customerInfo,HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        customerInfo.setUserid(tokenModel.getUserId());
        customerInfo.preInsert(tokenModel);
        userService.inCustomerInfo(customerInfo);
        return ApiResult.success();
    }

    //更新当前用户用户信息
    @RequestMapping(value = "/upCurrentUserInfo",method={RequestMethod.POST})
    public ApiResult upCurrentUserInfo(@RequestBody CustomerInfo customerInfo,HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        customerInfo.setUserid(tokenModel.getUserId());
        customerInfo.preUpdate(tokenModel);
        userService.upCustomerInfo(customerInfo);
        return ApiResult.success();
    }

    //当前用户申请成为正式租户
    @RequestMapping(value = "/applyCurrentUserTenantId",method={RequestMethod.POST})
    public ApiResult applyTenantId(HttpServletRequest request) throws Exception {
        val userAccount = new UserAccount();
        userAccount.setUserid(RequestUtils.CurrentUserId(request));
        val userAccountlist = userService.getUserAccount(userAccount);
        if(userAccountlist.size() <= 0){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.USER_ERR_01));
        }else if(userAccountlist.get(0).getStatus().equals(AuthConstants.APPLYTENANTID)){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.USER_ERR_02));
        }else{
            TokenModel tokenModel = tokenService.getToken(request);
            userAccountlist.get(0).setStatus(AuthConstants.APPLYTENANTID);
            userAccountlist.get(0).preUpdate(tokenModel);
            userService.upUserAccount(userAccountlist.get(0));
        }
        return ApiResult.success();
    }
}
