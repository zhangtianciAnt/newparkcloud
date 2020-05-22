package com.nt.controller.Controller;

import cn.hutool.core.util.StrUtil;
import com.nt.dao_Auth.AppPermission;
import com.nt.dao_Auth.Role;
import com.nt.dao_Pfans.PFANS2000.Punchcard;
import com.nt.service_Auth.AuthService;
import com.nt.service_Auth.RoleService;
import com.nt.service_pfans.PFANS2000.AnnualLeaveService;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
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

    @Autowired
    private AnnualLeaveService annualLeaveService;
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
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
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

    @RequestMapping(value = "/getAttendance",method={RequestMethod.GET})
    public ApiResult getAttendance(Integer diffday,String staffId,String staffNo,HttpServletRequest request) throws Exception {
        annualLeaveService.insertattendance(diffday,staffId,staffNo);
        return ApiResult.success();
    }

    @RequestMapping(value = "/getAttendancebp",method={RequestMethod.GET})
    public ApiResult getAttendancebp(Integer diffday,String staffId,String staffNo,HttpServletRequest request) throws Exception {
        annualLeaveService.insertattendancebp(diffday,staffId,staffNo);
        return ApiResult.success();
    }

    @RequestMapping(value = "/insertpunchcard",method={RequestMethod.GET})
    public ApiResult insertpunchcard(Integer diffday,HttpServletRequest request) throws Exception {
        annualLeaveService.insertpunchcard(diffday);
        return ApiResult.success();
    }

    @RequestMapping(value = "/selectattendance",method={RequestMethod.GET})
    public ApiResult selectattendance(HttpServletRequest request) throws Exception {
        annualLeaveService.selectattendance();
        return ApiResult.success();
    }

    @RequestMapping(value = "/selectattendancebp",method={RequestMethod.GET})
    public ApiResult selectattendancebp(HttpServletRequest request) throws Exception {
        annualLeaveService.selectattendancebp();
        return ApiResult.success();
    }

    @RequestMapping(value = "/creatAnnualLeaveAn",method={RequestMethod.GET})
    public ApiResult creatAnnualLeaveAn(HttpServletRequest request) throws Exception {
        annualLeaveService.insert();
        return ApiResult.success();
    }

    //获取打卡记录（参数）
    @RequestMapping(value = "/getPunchcard",method={RequestMethod.POST})
    public ApiResult getPunchcard(@RequestBody List<Punchcard> Punchcard,HttpServletRequest request) throws Exception {
        annualLeaveService.getPunchcard(Punchcard);
        return ApiResult.success();
    }

    //获取打卡记录bp（参数）
    @RequestMapping(value = "/getPunchcardbp",method={RequestMethod.POST})
    public ApiResult getPunchcardbp(@RequestBody List<Punchcard> Punchcard,HttpServletRequest request) throws Exception {
        annualLeaveService.getPunchcardbp(Punchcard);
        return ApiResult.success();
    }
}
