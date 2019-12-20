package com.nt.controller.Controller.BASF.BASFLANController;

import cn.hutool.core.util.StrUtil;
import com.nt.dao_BASF.Responseinformation;
import com.nt.service_BASF.ResponseinformationServices;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.RequestUtils;
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
 * @ProjectName: BASF应急平台
 * @Package: com.nt.controller.Controller.BASF.BASFLANController
 * @ClassName: BASF10803Controller
 * @Author: 王哲
 * @Description: 应急预案响应信息
 * @Date: 2019/12/19 16:16
 * @Version: 1.0
 */
@RestController
@RequestMapping("/BASF10803")
public class BASF10803Controller {
    @Autowired
    private ResponseinformationServices responseinformationServices;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public ApiResult list(HttpServletRequest request) throws Exception {
        return ApiResult.success(responseinformationServices.list());
    }

    @RequestMapping(value = "/getone", method = {RequestMethod.GET})
    public ApiResult selectById(String responseinformationid, HttpServletRequest request) throws Exception {
        if (StrUtil.isEmpty(responseinformationid)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(responseinformationServices.getone(responseinformationid));
    }

    @RequestMapping(value = "/insert", method = {RequestMethod.POST})
    public ApiResult insert(@RequestBody Responseinformation responseinformation, HttpServletRequest request) throws Exception {
        if (responseinformation == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        responseinformationServices.insert(tokenModel, responseinformation);
        return ApiResult.success();
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody Responseinformation responseinformation, HttpServletRequest request) throws Exception {
        if (responseinformation == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        responseinformationServices.update(tokenModel, responseinformation);
        return ApiResult.success();
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public ApiResult delete(@RequestBody Responseinformation responseinformation, HttpServletRequest request) throws Exception {
        if (responseinformation == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        responseinformationServices.delete(tokenModel, responseinformation);
        return ApiResult.success();
    }

}


