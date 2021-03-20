package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS2000.CasgiftApply;
import com.nt.dao_Pfans.PFANS2000.PersonalCost;
import com.nt.dao_Pfans.PFANS2000.PersonalCostYears;
import com.nt.service_pfans.PFANS2000.PersonalCostService;
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

@RestController
@RequestMapping("/personalcost")
public class Pfans2036Controller {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PersonalCostService personalCostService;

    @RequestMapping(value = "/getPersonalCost", method = {RequestMethod.GET})
    public ApiResult getPersonalCost(String groupid,String yearsantid, HttpServletRequest request) throws Exception {
        if (groupid == null || yearsantid == null ) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(personalCostService.getPersonalCost(groupid,yearsantid));
    }

    @RequestMapping(value = "/getGroupId", method = {RequestMethod.GET})
    public ApiResult getGroupId(String yearsantid, HttpServletRequest request) throws Exception {
        if (yearsantid == null ) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(personalCostService.getGroupId(yearsantid));
    }

    @RequestMapping(value = "/getChangeRanks", method = {RequestMethod.GET})
    public ApiResult getChangeRanks() throws Exception {
        return ApiResult.success(personalCostService.getChangeRanks());
    }


    @RequestMapping(value = "/gettableBm", method = {RequestMethod.GET})
    public ApiResult gettableBm(String yearsantid, HttpServletRequest request) throws Exception {
        if (yearsantid == null ) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(personalCostService.gettableBm(yearsantid));
    }

    @RequestMapping(value = "/gettableGs", method = {RequestMethod.GET})
    public ApiResult gettableGs(String yearsantid, HttpServletRequest request) throws Exception {
        if (yearsantid == null ) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(personalCostService.gettableGs(yearsantid));
    }

    @RequestMapping(value = "/gettableRb", method = {RequestMethod.GET})
    public ApiResult gettableRb(String yearsantid, HttpServletRequest request) throws Exception {
        if (yearsantid == null ) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(personalCostService.gettableRb(yearsantid));
    }








    @RequestMapping(value = "/getYears", method = {RequestMethod.GET})
    public ApiResult getYears(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        PersonalCostYears personalCostYears = new PersonalCostYears();
        personalCostYears.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(personalCostService.getPerCostYarList(personalCostYears));
    }

    @RequestMapping(value = "/insertPenalcost", method = {RequestMethod.GET})
    public ApiResult insertPenalcost(String year , HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        PersonalCost personalCost = new PersonalCost();
        return ApiResult.success(personalCostService.insertPenalcost(year,tokenModel));
    }



    @RequestMapping(value = "/upPersonalCost", method = {RequestMethod.POST})
    public ApiResult upPersonalCost(@RequestBody List<PersonalCost> personalCostList, HttpServletRequest request) throws Exception {
        if (personalCostList == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        personalCostService.upPersonalCost(personalCostList, tokenModel);
        return ApiResult.success();
    }
    //add-lyt-21/2/19-PSDCD_PFANS_20201123_XQ_017-start
    @RequestMapping(value = "/getFuzzyQuery", method = {RequestMethod.GET})
    public ApiResult getFuzzyQuery(String yearsantid,String username,String allotmentAnt,String group_id,String rnAnt) throws Exception {
        PersonalCost personalcost = new PersonalCost();
        return ApiResult.success(personalCostService.getFuzzyQuery(yearsantid,username,allotmentAnt,group_id,rnAnt));
    }

}
