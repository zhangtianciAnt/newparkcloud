package com.nt.controller.Controller.PFANS;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.nt.dao_Auth.Vo.SignInVo;
import com.nt.dao_Auth.model.SignInInformation;
import com.nt.dao_Pfans.PFANS2000.CasgiftApply;
import com.nt.dao_Pfans.PFANS2000.PersonalCost;
import com.nt.dao_Pfans.PFANS2000.PersonalCostYears;
import com.nt.dao_Pfans.PFANS2000.Vo.PersonalCostExpVo;
import com.nt.service_pfans.PFANS2000.PersonalCostService;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    @RequestMapping(value = "/exportinfo", method = {RequestMethod.GET})
    public ApiResult exportinfo(String yearsantid, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<PersonalCostExpVo> personalCostExpVoList = personalCostService.exportinfo(yearsantid);
        List<PersonalCostExpVo> rows = new ArrayList<PersonalCostExpVo>();
        ArrayList<Map<String, Object>> rowLists = CollUtil.newArrayList();
        for (PersonalCostExpVo item :personalCostExpVoList
        ) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("姓名",item.getUsername());
            row.put("kahao",item.getJobnumber());
            row.put("部门简称",item.getDepartshort());
            row.put("配付与否",item.getAllotment());
            row.put("新人入社预定月",item.getNewpersonaldate());
            row.put("升格前Rn",item.getExrank());
            row.put("是否升格升号",item.getChangerank());
            row.put("升格后Rn",item.getLtrank());
            row.put("基本给",item.getBasicallyant());
            row.put("职责给",item.getResponsibilityant());
            row.put("月工资",item.getMonthlysalary());
            row.put("一括补贴",item.getAllowanceant());
            row.put("拓展项补贴1",item.getOtherantone());
            row.put("拓展项补贴2",item.getOtheranttwo());
            row.put("独生子女费",item.getOnlychild());
            row.put("取暖补贴",item.getQnbt());
            row.put("补贴总计",item.getTotalsubsidies());
            row.put("月度奖金月数",item.getMonthlybonusmonths());
            row.put("月度奖金",item.getMonthlybonus());
            row.put("年度奖金月数",item.getAnnualbonusmonths());
            row.put("年度奖金",item.getAnnualbonus());
            row.put("工资总额",item.getTotalwages());
            row.put("工会经费",item.getTradeunionfunds());
            row.put("加班费时给",item.getOvertimepay());
            row.put("是否大连户籍",item.getIndalian());
            row.put("养老保险基(4-6)",item.getOldylbxjaj());
            row.put("失业保险基(4-6)",item.getLossybxjaj());
            row.put("工伤保险基(4-6)",item.getGsbxjaj());
            row.put("医疗保险基(4-6)",item.getYlbxjaj());
            row.put("生育保险基(4-6)",item.getSybxjaj());
            row.put("公积金基数(4-6)",item.getGjjjsaj());
            row.put("社保企业(4-6)",item.getSbqyaj());
            row.put("大病险(4-6)",item.getDbxaj());
            row.put("社保公司(4-6)",item.getSbgsaj());
            row.put("公积金公司负担(4-6)",item.getGjjgsfdaj());
            row.put("4月-6月",item.getAptoju());
            row.put("养老保险基(7-3)",item.getOldylbxjjm());
            row.put("失业保险基(7-3)",item.getLossybxjjm());
            row.put("工伤保险基(7-3)",item.getGsbxjjm());
            row.put("医疗保险基(7-3)",item.getYlbxjjm());
            row.put("生育保险基(7-3)",item.getSybxjjm());
            row.put("公积金基数(7-3)",item.getGjjjsjm());
            row.put("社保企业(7-3)",item.getSbqyjm());
            row.put("大病险(7-3)",item.getDbxjm());
            row.put("社保公司(7-3)",item.getSbgsjm());
            row.put("公积金公司负担(7-3)",item.getGjjgsfdjm());
            row.put("7月-3月",item.getJutoma());
            rowLists.add(row);
        }
        String destFilePath = "D:/" + "人件费.xlsx";
        ExcelWriter writer = ExcelUtil.getWriter(destFilePath, "人件费");
        writer.write(rowLists);
        writer.close();
        return ApiResult.success();
    }

    @RequestMapping(value = "/importPersInfo", method = {RequestMethod.POST})
    public ApiResult importPersInfo( HttpServletRequest request) throws Exception {
        try {
            TokenModel tokenModel = tokenService.getToken(request);
            List<String> stringList =  personalCostService.importPersInfo(request, tokenModel);
            return ApiResult.success(stringList);
        }catch(LogicalException e){
            return ApiResult.fail(e.getMessage());
        }catch (Exception e) {
            return ApiResult.fail("操作失败！");
        }
    }
}
