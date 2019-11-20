package com.nt.controller.Controller.BASF.BASFLANController;

import cn.hutool.core.util.StrUtil;
import com.nt.dao_BASF.Emergencyplan;
import com.nt.service_BASF.EmergencyplanServices;
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
 * @ClassName: BASF10802Controller
 * @Author: Y
 * @Description: BASF接警单管理模块Controller
 * @Date: 2019/11/18 18：03
 * @Version: 1.0
 */
@RestController
@RequestMapping("/BASF10802")
public class BASF10802Controller {
    @Autowired
    private EmergencyplanServices emergencyplanServices;
    @Autowired
    private TokenService tokenService;

    /**
     * @param request
     * @Method list
     * @Author Y
     * @Version 1.0
     * @Description 获取应急预案列表
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/18 18：03
     */
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public ApiResult list(HttpServletRequest request) throws Exception {
        return ApiResult.success(emergencyplanServices.list());
    }

    /**
     * @param emergencyplan
     * @param request
     * @Method create
     * @Author Y
     * @Version 1.0
     * @Description 创建应急预案
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/18 18:03
     */
    @RequestMapping(value = "/create", method = {RequestMethod.POST})
    public ApiResult create(@RequestBody Emergencyplan emergencyplan, HttpServletRequest request) throws Exception {
        if (emergencyplan == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        emergencyplanServices.insert(emergencyplan, tokenModel);
        return ApiResult.success();
    }

    /**
     * @param emergencyplan
     * @param request
     * @Method delete
     * @Author Y
     * @Version 1.0
     * @Description 删除应急预案
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/18 18：02
     */
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public ApiResult delete(@RequestBody Emergencyplan emergencyplan, HttpServletRequest request) throws Exception {
        if (emergencyplan == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        emergencyplan.setStatus(AuthConstants.DEL_FLAG_DELETE);
        emergencyplanServices.delete(emergencyplan);
        return ApiResult.success();
    }

    /**
     * @param emergencyplanid
     * @param request
     * @Method selectById
     * @Author Y
     * @Version 1.0
     * @Description 获取应急预案详情
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/18 18:01
     */
    @RequestMapping(value = "/selectById", method = {RequestMethod.GET})
    public ApiResult selectById(String emergencyplanid, HttpServletRequest request) throws Exception {
        if (StrUtil.isEmpty(emergencyplanid)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(emergencyplanServices.one(emergencyplanid));
    }

    /**
     * @param emergencyplan
     * @param request
     * @Method update
     * @Author Y
     * @Version 1.0
     * @Description 更新应急预案详情
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/18 18:01
     */
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody Emergencyplan emergencyplan, HttpServletRequest request) throws Exception {
        if (emergencyplan == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        emergencyplanServices.update(emergencyplan, tokenModel);
        return ApiResult.success();
    }
}
