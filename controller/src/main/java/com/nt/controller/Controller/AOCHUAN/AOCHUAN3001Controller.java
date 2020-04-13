package com.nt.controller.Controller.AOCHUAN;

import cn.hutool.core.util.StrUtil;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Quotations;
import com.nt.service_AOCHUAN.AOCHUAN3000.QuotationsService;
import com.nt.utils.*;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/quotations")
public class AOCHUAN3001Controller {

    @Autowired
    private QuotationsService quotationsService;
    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/get",method={RequestMethod.GET})
    public ApiResult get(HttpServletRequest request) throws Exception {
        return ApiResult.success(quotationsService.get());
    }

    @RequestMapping(value = "/getone",method={RequestMethod.GET})
    public ApiResult getOne(@RequestParam String id, HttpServletRequest request) throws Exception {
        if(!StringUtils.isNotBlank(id)){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(quotationsService.getOne(id));
    }

    @RequestMapping(value = "/update",method={RequestMethod.GET})
    public ApiResult update(@RequestBody Quotations quotations, HttpServletRequest request) throws Exception {
        if(quotations == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        quotationsService.insert(quotations,tokenService.getToken(request));
        return ApiResult.success();
    }

    @RequestMapping(value = "/insert",method={RequestMethod.GET})
    public ApiResult insert(@RequestBody Quotations quotations, HttpServletRequest request) throws Exception {
        if(quotations == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        quotationsService.insert(quotations,tokenService.getToken(request));
        return ApiResult.success();
    }

    @RequestMapping(value = "/delete",method={RequestMethod.GET})
    public ApiResult delete(@RequestParam String id, HttpServletRequest request) throws Exception {
        if(!StringUtils.isNotBlank(id)){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        quotationsService.delete(id);
        return ApiResult.success();
    }
}
