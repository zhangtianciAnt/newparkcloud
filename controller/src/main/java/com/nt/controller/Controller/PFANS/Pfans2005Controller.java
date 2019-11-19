package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS2000.OtherTwo;
import com.nt.dao_Pfans.PFANS2000.Contrast;
import com.nt.service_pfans.PFANS2000.GivingService;
import com.nt.service_pfans.PFANS2000.ContrastService;
import com.nt.dao_Pfans.PFANS2000.Giving;
import com.nt.service_pfans.PFANS2000.OtherTwoService;
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
@RequestMapping("/giving")
public class Pfans2005Controller {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private GivingService givingService;

    @Autowired
    private OtherTwoService othertwoService;

    @Autowired
    private ContrastService contrastService;

    @RequestMapping(value = "/createNewUser", method = {RequestMethod.POST})
    public ApiResult create(@RequestBody Giving giving, HttpServletRequest request) throws Exception {
        if (giving == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        givingService.insert(giving, tokenModel);
        return ApiResult.success();
    }


    @RequestMapping(value = "/getDataList", method = {RequestMethod.GET})
    public ApiResult get(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        Giving giving=new Giving();
        giving.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(givingService.getDataList(giving));
    }

    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public ApiResult list(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        OtherTwo othertwo = new OtherTwo();
        othertwo.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(othertwoService.list(othertwo));
    }

    @RequestMapping(value = "/insertInfo", method = {RequestMethod.POST})
    public ApiResult create(@RequestBody  OtherTwo othertwo, HttpServletRequest request) throws Exception {
        if (othertwo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        othertwoService.insert(othertwo, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "insertContrast", method = {RequestMethod.GET})
    public ApiResult insert(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        contrastService.insert(tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/getListContrast", method = {RequestMethod.GET})
    public ApiResult getList(HttpServletRequest request) throws Exception {
//        TokenModel tokenModel = tokenService.getToken(request);
//        Contrast contrast =new Contrast();
//        contrast.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(contrastService.getList(null));
    }
}
