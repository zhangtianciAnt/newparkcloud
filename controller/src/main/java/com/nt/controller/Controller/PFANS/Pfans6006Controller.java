package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS2000.Bonussend;
import com.nt.dao_Pfans.PFANS6000.Delegainformation;
import com.nt.service_pfans.PFANS6000.DeleginformationService;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.RequestUtils;
import com.nt.utils.services.TokenService;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/delegainformation")
public class Pfans6006Controller {

    @Autowired
    private DeleginformationService deleginformationService;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult updateDeleginformation(@RequestBody List<Delegainformation> delegainformationList, HttpServletRequest request) throws Exception {
        if (delegainformationList == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        deleginformationService.updateDeleginformation(delegainformationList, tokenModel);
        return ApiResult.success();
    }

//    @RequestMapping(value = "/get", method = {RequestMethod.GET})
//    public ApiResult getDelegainformation(HttpServletRequest request) throws Exception {
//        TokenModel tokenModel = tokenService.getToken(request);
//        return ApiResult.success(deleginformationService.getDelegainformation());
//    }

    //
//    @RequestMapping(value = "/List", method = {RequestMethod.GET})
//    public ApiResult List(HttpServletRequest request) throws Exception {
//        TokenModel tokenModel = tokenService.getToken(request);
//        return ApiResult.success(deleginformationService.getDelegainformation());
//    }
    @RequestMapping(value = "/getYears", method = {RequestMethod.GET})
    public ApiResult getYears(String year, HttpServletRequest request) throws Exception {
        return ApiResult.success(deleginformationService.getYears(year));
    }
}
