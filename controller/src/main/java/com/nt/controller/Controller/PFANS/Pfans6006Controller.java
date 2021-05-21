package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS2000.Bonussend;
import com.nt.dao_Pfans.PFANS6000.Delegainformation;
import com.nt.dao_Pfans.PFANS6000.Vo.DelegainformationtaxVo;
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

    //insert gbb 20210223 PSDCD_PFANS_20201117_XQ_011 外协委托信息添加【总额税金】和【税率】 start
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    //public ApiResult updateDeleginformation(@RequestBody List<Delegainformation> delegainformationList, HttpServletRequest request) throws Exception {
    public ApiResult updateDeleginformation(@RequestBody DelegainformationtaxVo taxVo, HttpServletRequest request) throws Exception {
        //if (delegainformationList == null) {
        if (taxVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        deleginformationService.updateDeleginformation(taxVo, tokenModel);
        //insert gbb 20210223 PSDCD_PFANS_20201117_XQ_011 外协委托信息添加【总额税金】和【税率】 end
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
    public ApiResult getYears(String year, String group_id,HttpServletRequest request) throws Exception {
        if(group_id.equals(""))
        {
            group_id = "1";
        }
        TokenModel tokenModel = tokenService.getToken(request);
        List<String> owners = tokenModel.getOwnerList();
        return ApiResult.success(deleginformationService.getYears(year,group_id,owners));
    }
}
