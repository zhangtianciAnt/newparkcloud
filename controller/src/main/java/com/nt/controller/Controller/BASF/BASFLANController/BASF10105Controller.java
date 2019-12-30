package com.nt.controller.Controller.BASF.BASFLANController;

import cn.hutool.core.util.StrUtil;
import com.nt.dao_BASF.Deviceinformation;
import com.nt.service_BASF.DeviceInformationServices;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.controller.Controller.BASF.BASFLANController
 * @ClassName: BASF10105Controller
 * @Author: SKAIXX
 * @Description: BASF设备管理模块Controller
 * @Date: 2019/11/4 16:09
 * @Version: 1.0
 */
@RestController
@RequestMapping("/BASF10105")
public class BASF10105Controller {

    @Autowired
    private DeviceInformationServices deviceinFormationServices;

    @Autowired
    private TokenService tokenService;

    /**
     * @param request
     * @Method list
     * @Author SKAIXX
     * @Version 1.0
     * @Description 获取设备列表
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/4 19:38
     */
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public ApiResult list(@RequestBody Deviceinformation deviceinformation, HttpServletRequest request) throws Exception {
        return ApiResult.success(deviceinFormationServices.list(deviceinformation));
    }

    /**
     * @param deviceinformation
     * @param request
     * @Method create
     * @Author SKAIXX
     * @Version 1.0
     * @Description 创建设备
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/4 19:39
     */
    @RequestMapping(value = "/create", method = {RequestMethod.POST})
    public ApiResult create(@RequestBody Deviceinformation deviceinformation, HttpServletRequest request) throws Exception {
        if (deviceinformation == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        deviceinFormationServices.insert(deviceinformation, tokenModel, "");
        return ApiResult.success();
    }

    /**
     * @param deviceinformation
     * @param request
     * @Method delete
     * @Author SKAIXX
     * @Version 1.0
     * @Description 删除设备
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/5 15:37
     */
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public ApiResult delete(@RequestBody Deviceinformation deviceinformation, HttpServletRequest request) throws Exception {
        if (deviceinformation == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        deviceinformation.setStatus(AuthConstants.DEL_FLAG_DELETE);
        deviceinFormationServices.delete(deviceinformation);
        return ApiResult.success();
    }

    /**
     * @param deviceid
     * @param request
     * @Method selectById
     * @Author SKAIXX
     * @Version 1.0
     * @Description 获取设备详情
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/5 15:58
     */
    @RequestMapping(value = "/selectById", method = {RequestMethod.GET})
    public ApiResult selectById(String deviceid, HttpServletRequest request) throws Exception {
        if (StrUtil.isEmpty(deviceid)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(deviceinFormationServices.one(deviceid));
    }

    /**
     * @param deviceinformation
     * @param request
     * @Method update
     * @Author SKAIXX
     * @Version 1.0
     * @Description 更新设备详情
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/5 16:08
     */
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody Deviceinformation deviceinformation, HttpServletRequest request) throws Exception {
        if (deviceinformation == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        deviceinFormationServices.update(deviceinformation, tokenModel);
        return ApiResult.success();
    }

    /**
     * @Method update
     * @Author 王哲
     * @Version 1.0
     * @Description 查询设备列表（GIS专用）
     * @Return com.nt.utils.ApiResult
     * @Date 2019/12/12 14:47
     */
    @RequestMapping(value = "/deviceList", method = {RequestMethod.GET})
    public ApiResult deviceList(String mapid, String devicetype, String devicename, Integer pageindex, Integer pagesize, HttpServletRequest request) throws Exception {
        if (StrUtil.isEmpty(mapid) || StrUtil.isEmpty(devicetype) || pageindex == null || pagesize == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        String[] devicetypeList = devicetype.split("-");
        if (!StrUtil.isEmpty(devicename)) {
            devicename = "%" + devicename + "%";
        }
        pageindex = pagesize * (pageindex - 1);
        return ApiResult.success(
                deviceinFormationServices.deviceList(mapid, devicetypeList, devicename, pageindex, pagesize)
        );
    }
}
