package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS1000.Psdcd;
import com.nt.dao_Pfans.PFANS1000.Vo.PsdcdVo;
import com.nt.service_pfans.PFANS1000.PsdcdService;
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

@RestController
@RequestMapping("/psdcd")
public class Pfans1017Controller {

    @Autowired
    private PsdcdService psdcdService;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value="/get",method = {RequestMethod.GET})
    public ApiResult getPsdcd(HttpServletRequest request)throws  Exception{
        TokenModel tokenModel = tokenService.getToken(request);
        Psdcd psdcd = new Psdcd();
        psdcd.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(psdcdService.getPsdcd(psdcd));

    }

    @RequestMapping(value = "/selectById", method = {RequestMethod.GET})
    public ApiResult selectById(String psdcd_id, HttpServletRequest request) throws Exception {
        if (psdcd_id == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(psdcdService.selectById(psdcd_id));
    }

    @RequestMapping(value="/update",method = {RequestMethod.POST})
    public ApiResult update(@RequestBody PsdcdVo psdcdVo, HttpServletRequest request) throws Exception{
        if (psdcdVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        psdcdService.update(psdcdVo,tokenModel);
        return ApiResult.success();
    }


    @RequestMapping(value = "/insert",method={RequestMethod.POST})
    public ApiResult insert(@RequestBody PsdcdVo psdcdVo, HttpServletRequest request) throws Exception {
        if (psdcdVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        psdcdService.insert(psdcdVo,tokenModel);
        return ApiResult.success();
    }
}
