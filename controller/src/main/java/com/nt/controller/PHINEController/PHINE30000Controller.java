package com.nt.controller.PHINEController;

import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.Vo.UserVo;
import com.nt.dao_PHINE.Projectinfo;
import com.nt.dao_PHINE.Vo.UserAuthListVo;
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
    public ApiResult saveProjectInfo(@RequestBody Projectinfo projectinfo,HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        if(!projectinfoService.selectProjectIdExist(projectinfo.getProjectid())) {
            UserVo userVo = userService.getAccountCustomerById(tokenModel.getUserId());
            String companyId = userVo.getCustomerInfo().getUserinfo().getCompanyid();
            projectinfo.setCompanyid(companyId);
            projectinfo.setCreateby(tokenModel.getUserId());
            projectinfo.setTenantid(tokenModel.getTenantId());
            projectinfo.setCreateon(new java.sql.Date(System.currentTimeMillis()));
            projectinfoService.saveProjectInfo(projectinfo);
            return ApiResult.success();
        }else{
            return ApiResult.fail("项目ID已经存在，请重新输入新的项目ID。");
        }
    }

    /**
     * @方法名：getUserAuthList
     * @描述：获取用户权限及设备信息列表
     * @创建日期：2020/1/30
     * @作者：MYT
     * @参数：[request]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/getUserAuthList", method = {RequestMethod.GET})
    public ApiResult getUserAuthList(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        UserVo userVo = userService.getAccountCustomerById(tokenModel.getUserId());
        String companyId = userVo.getCustomerInfo().getUserinfo().getCompanyid();
        List<CustomerInfo> userInfoList = userService.getAccountCustomer(companyId,"");
        List< UserAuthListVo > userAuthInfoList = new ArrayList<UserAuthListVo>();
        for( CustomerInfo info :userInfoList){
            UserAuthListVo vo = new UserAuthListVo();
            vo.setUserid(info.get_id());
            vo.setInfoauth("1");
            vo.setFileauth("1");
            vo.setAuthmanage("1");
            vo.setMachineauth("1");
            userAuthInfoList.add(vo);
        }
        return ApiResult.success(userAuthInfoList);
    }

    /**
     * @方法名：addUserAuth
     * @描述：添加用户权限及设备信息
     * @创建日期：2020/1/30
     * @作者：MYT
     * @参数：[request]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/addUserAuth", method = {RequestMethod.POST})
    public ApiResult addUserAuth(String projectid,HttpServletRequest request) throws Exception {
        if(projectinfoService.selectProjectIdExist(projectid)){
            TokenModel tokenModel = tokenService.getToken(request);
            UserVo userVo = userService.getAccountCustomerById(tokenModel.getUserId());
            userVo.getCustomerInfo().getUserinfo().getCompanyid();
            return ApiResult.success();
        }else{
            return ApiResult.fail("项目ID不存在，请先创建项目后再添加用户权限信息。");
        }
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
