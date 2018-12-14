package com.nt.controller.Controller;

import cn.hutool.core.util.StrUtil;
import com.nt.dao_Auth.AppPermission;
import com.nt.dao_Auth.Role;
import com.nt.service_Auth.AuthService;
import com.nt.service_Auth.RoleService;
import com.nt.utils.ApiResult;
import com.nt.utils.AuthConstants;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.controller.Controller
 * @ClassName: AuthController
 * @Description: 权限相关Controller
 * @Author: WenChao
 * @CreateDate: 2018/12/14
 * @UpdateUser: WenChao
 * @UpdateDate: 2018/12/14
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private TokenService tokenService;

    /**
     * @方法名：getActionsAuth
     * @描述：获取按钮权限（新建，编辑，删除）
     * @创建日期：2018/12/14
     * @作者：WENCHAO
     * @参数：[role, request]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/getActionsAuth",method={RequestMethod.GET})
    public ApiResult getActionsAuth(String ownerid, HttpServletRequest request) throws Exception {
        if (StrUtil.isEmpty(ownerid)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.PARAM_ERR_02));
        }
        String url = request.getHeader(AuthConstants.CURRENTURL);
        TokenModel tokenModel = tokenService.getToken(request);
        String userid = tokenModel.getUserId();
        return ApiResult.success(authService.getActionsAuth(url,userid,ownerid));
    }

    /**
     * @方法名：getActionsAuth
     * @描述：获取按钮权限（新建）
     * @创建日期：2018/12/14
     * @作者：WENCHAO
     * @参数：[role, request]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/getNewActionAuth",method={RequestMethod.GET})
    public ApiResult getNewActionAuth(HttpServletRequest request) throws Exception {
        String url = request.getHeader(AuthConstants.CURRENTURL);
        TokenModel tokenModel = tokenService.getToken(request);
        String userid = tokenModel.getUserId();
        return ApiResult.success(authService.getNewActionAuth(url,userid));
    }
}
