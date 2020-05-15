package com.nt.controller.Controller.AOCHUAN;

import com.nt.dao_AOCHUAN.AOCHUAN3000.Sample;
import com.nt.service_AOCHUAN.AOCHUAN3000.SampleService;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/sample")
public class AOCHUAN3007Controller {
    @Autowired
    private SampleService sampleService;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/get",method={RequestMethod.GET})
    public ApiResult get(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        Sample sample  = new Sample();
        sample.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(sampleService.get(sample));
    }

    @RequestMapping(value = "/getone",method={RequestMethod.GET})
    public ApiResult getOne(@RequestParam String id, HttpServletRequest request) throws Exception {
        if(!StringUtils.isNotBlank(id)){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(sampleService.getOne(id));
    }

    @RequestMapping(value = "/getForSupplier",method={RequestMethod.GET})
    public ApiResult getForSupplier(@RequestParam String id, HttpServletRequest request) throws Exception {
        if(!StringUtils.isNotBlank(id)){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(sampleService.getForSupplier(id));
    }

    @RequestMapping(value = "/getForCustomer",method={RequestMethod.GET})
    public ApiResult getForCustomer(@RequestParam String id, HttpServletRequest request) throws Exception {
        if(!StringUtils.isNotBlank(id)){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(sampleService.getForCustomer(id));
    }

    @RequestMapping(value = "/update",method={RequestMethod.POST})
    public ApiResult update(@RequestBody Sample sample, HttpServletRequest request) throws Exception {
        if(sample == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        sampleService.update(sample,tokenService.getToken(request));
        return ApiResult.success();
    }

    @RequestMapping(value = "/insert",method={RequestMethod.POST})
    public ApiResult insert(@RequestBody Sample sample, HttpServletRequest request) throws Exception {
        if(sample == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        sampleService.insert(sample,tokenService.getToken(request));
        return ApiResult.success();
    }

    @RequestMapping(value = "/delete",method={RequestMethod.GET})
    public ApiResult delete(@RequestParam String id, HttpServletRequest request) throws Exception {
        if(!StringUtils.isNotBlank(id)){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        sampleService.delete(id);
        return ApiResult.success();
    }

}
