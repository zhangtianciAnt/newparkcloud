package com.nt.controller.Controller.BASF.BASFLANController;

import com.nt.dao_BASF.Deviceinformation;
import com.nt.service_BASF.DeviceinFormationServices;
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
    private DeviceinFormationServices deviceinFormationServices;

    @Autowired
    private TokenService tokenService;

    /**
     * @Method list
     * @Author SKAIXX
     * @Version  1.0
     * @Description 获取设备列表
     * @param request
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/4 19:38
     */
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public ApiResult list(HttpServletRequest request) throws Exception {
        Deviceinformation deviceinformation = new Deviceinformation();
<<<<<<< HEAD
        return ApiResult.success(basf10105Services.list(deviceinformation));
=======
        //deviceinformation.setStatus(AuthConstants.DEL_FLAG_NORMAL);
        return ApiResult.success(deviceinFormationServices.list(deviceinformation));
>>>>>>> d889055bfd83edaae6faeae35e0fcda5f12d7561
    }

    /**
     * @Method create
     * @Author SKAIXX
     * @Version  1.0
     * @Description 创建设备
     * @param deviceinformation
     * @param request
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/4 19:39
     */
    @RequestMapping(value = "/create", method = {RequestMethod.POST})
    public ApiResult create(@RequestBody Deviceinformation deviceinformation, HttpServletRequest request) throws Exception {
        if (deviceinformation == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        deviceinFormationServices.insert(deviceinformation, tokenModel);
        return ApiResult.success();
    }
}
