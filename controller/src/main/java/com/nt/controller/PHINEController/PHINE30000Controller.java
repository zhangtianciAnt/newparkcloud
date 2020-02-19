package com.nt.controller.PHINEController;

import cn.hutool.core.util.StrUtil;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.Vo.UserVo;
import com.nt.dao_PHINE.Project2device;
import com.nt.dao_PHINE.Project2deviceExtend;
import com.nt.dao_PHINE.Project2userExtend;
import com.nt.dao_PHINE.Projectinfo;
import com.nt.dao_PHINE.Vo.UserAuthListVo;
import com.nt.service_Org.UserService;
import com.nt.service_PHINE.ChipinfoService;
import com.nt.service_PHINE.DeviceinfoService;
import com.nt.service_PHINE.ProjectinfoService;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.RequestUtils;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.util.StringUtil;

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
    private ProjectinfoService projectinfoService;

    @Autowired
    private DeviceinfoService deviceinfoService;

    @Autowired
    private ChipinfoService chipinfoService;

    /**
     * @方法名：getChipTypeList
     * @描述：获取芯片信息列表
     * @创建日期：2020/2/4
     * @作者：MYT
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/getChipTypeList", method = {RequestMethod.GET})
    public ApiResult getChipTypeList() throws Exception {
        return ApiResult.success(chipinfoService.getChipTypeList());
    }

    /**
     * @方法名：saveProjectInfo
     * @描述：创建项目
     * @创建日期：2020/1/30
     * @作者：MYT
     * @参数：[projectinfo]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/saveProjectInfo", method = {RequestMethod.POST})
    public ApiResult saveProjectInfo(@RequestBody Projectinfo projectinfo, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        return projectinfoService.saveProjectInfo(tokenModel, projectinfo);
    }

    /**
     * @方法名：updateProjectInfo
     * @描述：更新项目信息
     * @创建日期：2020/1/30
     * @作者：MYT
     * @参数：[projectinfo]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/updateProjectInfo", method = {RequestMethod.PUT})
    public ApiResult updateProjectInfo(@RequestBody Projectinfo projectinfo, HttpServletRequest request) throws Exception {
        if (StrUtil.isEmpty(projectinfo.getId())) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return projectinfoService.updateProjectInfo(tokenModel, projectinfo);
    }

    /**
     * @方法名：saveUserAuthInfo
     * @描述：添加用户权限及设备信息
     * @创建日期：2020/1/30
     * @作者：MYT
     * @参数：[request]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/saveUserAuthInfo", method = {RequestMethod.POST})
    public ApiResult saveUserAuthInfo(@RequestBody Project2userExtend project2userExtend, HttpServletRequest request) throws Exception {
        if (StrUtil.isEmpty(project2userExtend.getProjectid())) {
            return ApiResult.fail("请先创建项目信息后，再添加用户权限！");
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return projectinfoService.saveUserAuthInfo(tokenModel, project2userExtend.getProjectid(), project2userExtend.getUseridList());
    }

    /**
     * @方法名：saveResourcesInfo
     * @描述：保存分配给项目的设备资源
     * @创建日期：2020/2/4
     * @作者：MYT
     * @参数：[projectinfo]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/saveResourcesInfo", method = {RequestMethod.POST})
    public ApiResult saveResourcesInfo(@RequestBody Project2deviceExtend project2deviceExtend, HttpServletRequest request) throws Exception {
        if (StrUtil.isEmpty(project2deviceExtend.getProjectid())) {
            return ApiResult.fail("请先创建项目信息后，再分配设备资源！");
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return projectinfoService.saveResourcesInfo(tokenModel, project2deviceExtend.getProjectid(), project2deviceExtend.getDeviceidList());
    }

    /**
     * @方法名：getProjectInfo
     * @描述：获取项目信息
     * @创建日期：2020/2/6
     * @作者：MYT
     * @参数：[request]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/getProjectInfo", method = {RequestMethod.GET})
    public ApiResult getProjectInfo(String projectid, HttpServletRequest request) throws Exception {
        if (StrUtil.isEmpty(projectid)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(projectinfoService.getProjectInfo("", projectid));
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
    public ApiResult getUserAuthList(String projectid, HttpServletRequest request) throws Exception {
        if (StrUtil.isEmpty(projectid)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(projectinfoService.getUserAuthList(projectid));
    }

    /**
     * @方法名：getDeviceListByCompanyId
     * @描述：根据企业ID获取设备列表信息
     * @创建日期：2020/1/31
     * @作者：MYT
     * @参数：[项目ID]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/getDeviceListByCompanyId", method = {RequestMethod.GET})
    public ApiResult getDeviceListByCompanyId(String companyid, HttpServletRequest request) throws Exception {
        if (StrUtil.isEmpty(companyid)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(deviceinfoService.getDeviceListByCompanyId(companyid));
    }

    /**
     * @方法名：getDeviceListByProjectId
     * @描述：根据项目ID获取设备列表信息
     * @创建日期：2020/1/31
     * @作者：MYT
     * @参数：[项目ID]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/getDeviceListByProjectId", method = {RequestMethod.GET})
    public ApiResult getDeviceListByProjectId(String projectid, HttpServletRequest request) throws Exception {
        if (StrUtil.isEmpty(projectid)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(deviceinfoService.getDeviceListByProjectId(projectid));
    }

    /**
     * @方法名：delUserAuth
     * @描述：删除用户权限及设备信息
     * @创建日期：2020/1/31
     * @作者：MYT
     * @参数：[登录用户的企业ID]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/delUserAuth", method = {RequestMethod.DELETE})
    public ApiResult delUserAuth(String id, HttpServletRequest request) throws Exception {
        if (StrUtil.isEmpty(id)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        projectinfoService.delUserAuth(id);
        return ApiResult.success();
    }
}
