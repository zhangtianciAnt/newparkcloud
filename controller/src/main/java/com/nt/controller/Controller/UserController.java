package com.nt.controller.Controller;

import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.UserAccount;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
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

    @RequestMapping(value = "/register",method={RequestMethod.POST})
    public ApiResult addUser(@RequestBody UserAccount userAccount, @RequestBody CustomerInfo customerInfo, HttpServletRequest request) {
        if (userAccount == null || customerInfo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.PARAM_ERR_02));
        }
        // todo:调用注册实现类
        return ApiResult.success();
    }
}
