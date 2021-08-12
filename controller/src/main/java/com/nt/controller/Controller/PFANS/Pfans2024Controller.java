package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS2000.TalentPlan;
import com.nt.dao_Pfans.PFANS2000.Vo.TalentPlanVo;
import com.nt.service_pfans.PFANS2000.TalentPlanService;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/talentplan")
public class Pfans2024Controller {

    @Autowired
    private TalentPlanService talentplanService;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/one",method={RequestMethod.POST})
    public ApiResult one(@RequestBody TalentPlan talentPlan, HttpServletRequest request) throws Exception {
        if (talentPlan == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        TalentPlan log=talentplanService.One(talentPlan.getTalentplan_id());
        return ApiResult.success(log);
    }

    @RequestMapping(value="/list", method={RequestMethod.POST})
    public ApiResult List(@RequestParam String years, TalentPlan talentPlan, HttpServletRequest request) throws Exception {
        if (talentPlan == null||years == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        talentPlan.setOwners(tokenModel.getOwnerList());
        List<TalentPlan> talentPlanList = new ArrayList<TalentPlan>();
        talentPlanList = talentplanService.list(years,talentPlan,tokenModel);
        talentPlanList = talentPlanList.stream().filter(item -> (!item.getUser_id().equals(tokenModel.getUserId()))).collect(Collectors.toList());
        return ApiResult.success(talentPlanList);
    }
    //add-ws-6/4-禅道031-人才育成修改各人员查看数据范围修改
    @RequestMapping(value = "/getDataList", method = {RequestMethod.POST})
    public ApiResult getDataList(@RequestParam String years, HttpServletRequest request) throws Exception {
        if (years == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        TalentPlan talentPlan = new TalentPlan();
        talentPlan.setYear(years);
        talentPlan.setOwners(tokenModel.getOwnerList());
        List<TalentPlan> talentPlanList = talentplanService.getDataList(talentPlan);
        talentPlanList = talentPlanList.stream().filter(item -> (!item.getUser_id().equals(tokenModel.getUserId()))).collect(Collectors.toList());
        return ApiResult.success(talentPlanList);
    }
    //add-ws-6/4-禅道031-人才育成修改各人员查看数据范围修改
    @RequestMapping(value = "/download", method = {RequestMethod.GET})
    public void download(String type, HttpServletResponse response) throws Exception {
        Map<String, Object> data = new HashMap<>();
        String templateName = null;
        String fileName = null;
        templateName = "rankExplanation.xlsx";
        fileName = "Rank说明";

        if (templateName != null ) {
            ExcelOutPutUtil.OutPut(fileName,templateName,data,response);
        }
    }


    @RequestMapping(value="/updateInfo",method = {RequestMethod.POST})
    public ApiResult updateInformation(@RequestBody TalentPlan talentPlan, HttpServletRequest request) throws Exception{
        if (talentPlan == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        talentplanService.upd(talentPlan,tokenModel);
        return ApiResult.success();
    }


    @RequestMapping(value = "/createNewUser",method={RequestMethod.POST})
    public ApiResult create(@RequestBody TalentPlan talentPlan, HttpServletRequest request) throws Exception {
        if (talentPlan == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        talentplanService.insert(talentPlan,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/create",method={RequestMethod.POST})
    public ApiResult createByOrg(@RequestBody TalentPlanVo TalentPlanVo, HttpServletRequest request) throws Exception {
        if (TalentPlanVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        talentplanService.insertByOrg(TalentPlanVo,tokenModel);
        return ApiResult.success();
    }

    //人才育成页面数据结转
    @RequestMapping(value = "/dataCarryover",method={RequestMethod.POST})
    public ApiResult dataCarryover(@RequestBody TalentPlan talentPlan, HttpServletRequest request) throws Exception{
        if (talentPlan == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        talentplanService.dataCarryover(talentPlan,tokenModel);
        return ApiResult.success();
    }
}
