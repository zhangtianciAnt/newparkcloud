package com.nt.controller.PHINEController;

import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.Vo.UserVo;
import com.nt.dao_PHINE.Projectinfo;
import com.nt.service_Org.UserService;
import com.nt.service_PHINE.DeviceinfoService;
import com.nt.service_PHINE.ProjectinfoService;
import com.nt.utils.ApiResult;
import com.nt.utils.RequestUtils;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: PHINE管理平台
 * @Package: com.nt.controller.PHINEController.PHINE30000Controller
 * @ClassName: PHINE30000Controller
 * @Author: MYT
 * @Description: PHINE创建项目Controller
 * @Date: 2020/1/30 14.01
 * @Version: 1.0
 */
@RestController
@RequestMapping("/PHINE30000")
public class PHINE30000Controller {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectinfoService projectinfoService;

    @Autowired
    private DeviceinfoService deviceinfoService;


    /**
     * @方法名：saveProjectInfo
     * @描述：创建项目
     * @创建日期：2020/1/30
     * @作者：MYT
     * @参数：[projectinfo]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/saveProjectInfo", method = {RequestMethod.POST})
    public ApiResult saveProjectInfo(@RequestBody Projectinfo projectinfo) throws Exception {
        projectinfoService.saveProjectInfo(projectinfo);
        return ApiResult.success();
    }

    /**
     * @方法名：getUserAuth
     * @描述：根据用户id获取用户权限及设备信息
     * @创建日期：2020/1/30
     * @作者：MYT
     * @参数：[request]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/getUserAuthList", method = {RequestMethod.GET})
    public ApiResult getUserAuth(String companyid, HttpServletRequest request) throws Exception {
        System.out.println(companyid);
        TokenModel tokenModel = tokenService.getToken(request);
        UserVo userVo = userService.getAccountCustomerById(tokenModel.getUserId());
        return ApiResult.success();
    }

    /**
     * @方法名：getDeviceList
     * @描述：获取设备列表信息
     * @创建日期：2020/1/31
     * @作者：MYT
     * @参数：[登录用户的企业ID]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/getDeviceList", method = {RequestMethod.GET})
    public ApiResult getDeviceList(String companyid) throws Exception {
        return ApiResult.success(deviceinfoService.getDeviceList());
    }

    /**
     * @方法名：delUserAuth
     * @描述：删除用户权限及设备信息
     * @创建日期：2020/1/31
     * @作者：MYT
     * @参数：[登录用户的企业ID]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/delUserAuth", method = {RequestMethod.POST})
    public ApiResult delUserAuth(String userid) throws Exception {
        projectinfoService.delUserAuth(userid);
        return ApiResult.success();
    }
}
