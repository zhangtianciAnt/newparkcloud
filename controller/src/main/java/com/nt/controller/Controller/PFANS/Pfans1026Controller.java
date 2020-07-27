package com.nt.controller.Controller.PFANS;

import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.UserAccount;
import com.nt.dao_Pfans.PFANS1000.*;
import com.nt.dao_Pfans.PFANS1000.Contractnumbercount;
import com.nt.dao_Pfans.PFANS1000.Vo.ContractapplicationVo;
import com.nt.dao_Pfans.PFANS2000.Vo.StaffexitprocedureVo;
import com.nt.dao_Workflow.Vo.StartWorkflowVo;
import com.nt.dao_Workflow.Vo.WorkflowLogDetailVo;
import com.nt.service_pfans.PFANS1000.ContractapplicationService;
import com.nt.service_pfans.PFANS1000.mapper.IndividualMapper;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/contractapplication")
public class Pfans1026Controller {
    @Autowired
    private IndividualMapper individualmapper;
    @Autowired
    private ContractapplicationService contractapplicationService;
    @Autowired
    private TokenService tokenService;

    //add-ws-7/22-禅道341任务
    @RequestMapping(value = "/getindividual", method = {RequestMethod.POST})
    public ApiResult getindividual(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        Individual individual = new Individual();
        individual.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(contractapplicationService.getindividual(individual));
    }

    @RequestMapping(value = "/generatesta", method = {RequestMethod.GET})
    public void generateJxls(String individual_id, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> data = new HashMap<>();
        Individual individual = individualmapper.selectByPrimaryKey(individual_id);
        data.put("ind", individual);
        ExcelOutPutUtil.OutPutPdf("个别合同书", "gebiehetong.xls", data, response);
    }

    //add-ws-7/22-禅道341任务
    @RequestMapping(value = "/get", method = {RequestMethod.POST})
    public ApiResult selectById(@RequestBody Contractapplication contractapplication, HttpServletRequest request) throws Exception {
        if (contractapplication == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        contractapplication.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(contractapplicationService.get(contractapplication));
    }

    @RequestMapping(value = "/get2", method = {RequestMethod.POST})
    public ApiResult selectById2(@RequestBody Contractapplication contractapplication, HttpServletRequest request) throws Exception {
        if (contractapplication == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
//        TokenModel tokenModel = tokenService.getToken(request);
//        contractapplication.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(contractapplicationService.get(contractapplication));
    }

    @RequestMapping(value = "/getList", method = {RequestMethod.POST})
    public ApiResult getList(@RequestBody List<Contractapplication> contractapplicationlist, HttpServletRequest request) throws Exception {
        if (contractapplicationlist == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(contractapplicationService.getList(contractapplicationlist));
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody ContractapplicationVo contractapplication, HttpServletRequest request) throws Exception {
        if (contractapplication == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        contractapplicationService.update(contractapplication, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/insert", method = {RequestMethod.POST})
    public ApiResult insert(@RequestBody ContractapplicationVo contractapplication, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        contractapplicationService.insert(contractapplication, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/insertBook", method = {RequestMethod.GET})
    public ApiResult insertBook(String contractnumber, String rowindex, String countNumber, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        //upd-ws-7/1-禅道152任务
        Map<String, Object> list = contractapplicationService.insertBook(contractnumber, rowindex, countNumber, tokenModel);
        return ApiResult.success(list);
        //upd-ws-7/1-禅道152任务
    }

    @RequestMapping(value = "/existCheck", method = {RequestMethod.GET})
    public ApiResult existCheck(String contractNumber, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(contractapplicationService.existCheck(contractNumber));
    }

    @RequestMapping(value = "/getPe", method = {RequestMethod.GET})
    public ApiResult getPe(String claimnumber, HttpServletRequest request) throws Exception {
        if (claimnumber == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(contractapplicationService.getPe(claimnumber));
    }

    //add ccm 0725  采购合同chongfucheck
    @RequestMapping(value = "/purchaseExistCheck",method={RequestMethod.GET})
    public ApiResult purchaseExistCheck(String purnumbers, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(contractapplicationService.purchaseExistCheck(purnumbers));
    }
    //add ccm 0725  采购合同chongfucheck

}
