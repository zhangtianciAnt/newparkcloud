package com.nt.controller.Controller.BASF.BASFLANController;

import com.nt.dao_BASF.Startprogram;
import com.nt.service_BASF.StartprogramServices;
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
 * @ClassName: BASF21209Controller
 * @Author: 王哲
 * @Description: 申请考核
 * @Date: 2020/1/7 14:04
 * @Version: 1.0
 */
@RestController
@RequestMapping("/BASF21209")
public class BASF21209Controller {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private StartprogramServices startprogramServices;

    //创建培训列表
    @RequestMapping(value = "/create", method = {RequestMethod.POST})
    public ApiResult create(@RequestBody Startprogram startprogram, HttpServletRequest request) throws Exception {
        if (startprogram == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        startprogramServices.insert(startprogram, tokenModel);
        return ApiResult.success();
    }

    //更新培训列表
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody Startprogram startprogram, HttpServletRequest request) throws Exception {
        if (startprogram == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        startprogramServices.update(startprogram, tokenModel);
        return ApiResult.success();
    }

    //查询培训
    @RequestMapping(value = "/select", method = {RequestMethod.POST})
    public ApiResult select(@RequestBody Startprogram startprogram, HttpServletRequest request) throws Exception {
        if (startprogram == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(startprogramServices.select(startprogram));
    }

    //查询培训增强
    @RequestMapping(value = "/selectEnhance", method = {RequestMethod.POST})
    public ApiResult selectEnhance(@RequestBody Startprogram startprogram, HttpServletRequest request) throws Exception {
        if (startprogram == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(startprogramServices.selectEnhance(startprogram));
    }

    //删除培训
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public ApiResult delete(@RequestBody Startprogram startprogram, HttpServletRequest request) throws Exception {
        if (startprogram == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        startprogram.setStatus(AuthConstants.DEL_FLAG_DELETE);
        startprogramServices.delete(startprogram, tokenModel);
        return ApiResult.success();
    }

}
