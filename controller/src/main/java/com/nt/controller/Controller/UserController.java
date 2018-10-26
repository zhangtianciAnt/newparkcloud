package com.nt.controller.Controller;

import com.nt.dao_Org.UserAccount;
import com.nt.service_Org.UserService;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
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

    @RequestMapping(value = "/register",method={RequestMethod.POST})
    public ApiResult addUser(@RequestBody UserAccount userAccount) throws Exception {
        if (userAccount == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.PARAM_ERR_02));
        }
        userService.register(userAccount);
        return ApiResult.success();
    }

    @RequestMapping(value = "/login",method={RequestMethod.POST})
    public ApiResult login(@RequestBody UserAccount userAccount) throws Exception {
        if (userAccount == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.PARAM_ERR_02));
        }
        userService.register(userAccount);
        // todo:调用注册实现类
        return ApiResult.success();
    }
}
