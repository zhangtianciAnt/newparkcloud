package com.nt.controller.Controller.PFANS;

import com.nt.dao_Assets.Assets;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Pfans.PFANS1000.Fixedassets;
import com.nt.dao_Workflow.Vo.StartWorkflowVo;
import com.nt.dao_Workflow.Vo.WorkflowLogDetailVo;
import com.nt.service_Assets.AssetsService;
import com.nt.service_WorkFlow.WorkflowServices;
import com.nt.service_pfans.PFANS1000.FixedassetsService;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/fixedassets")
public class Pfans1009Controller {

    @Autowired
    private FixedassetsService fixedassetsService;

    @Autowired
    private WorkflowServices workflowServices;

    @Autowired
    private TokenService tokenService;


    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private AssetsService assetsService;

    @RequestMapping(value="/get",method = {RequestMethod.GET})
    public ApiResult getFixedassets(HttpServletRequest request)throws  Exception{
        TokenModel tokenModel = tokenService.getToken(request);
        Fixedassets fixedassets = new Fixedassets();
        fixedassets.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(fixedassetsService.getFixedassets(fixedassets));
    }

    @RequestMapping(value = "/one",method={RequestMethod.POST})
    public ApiResult one(@RequestBody Fixedassets fixedassets, HttpServletRequest request) throws Exception {
        if (fixedassets == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(fixedassetsService.One(fixedassets.getFixedassets_id()));
    }

    @RequestMapping(value="/update",method = {RequestMethod.POST})
    public ApiResult updateFixedassets(@RequestBody Fixedassets fixedassets, HttpServletRequest request) throws Exception{
        if (fixedassets == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        fixedassetsService.updateFixedassets(fixedassets,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/create",method={RequestMethod.POST})
    public ApiResult create(@RequestBody Fixedassets fixedassets, HttpServletRequest request) throws Exception {
        if (fixedassets == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        fixedassetsService.insert(fixedassets,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/getAssetsnameList", method = {RequestMethod.POST})
    public ApiResult getAssetsnameList(@RequestBody Assets assets, HttpServletRequest request) throws Exception {
        if (assets == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        assets.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(assetsService.getAssetsnameList(assets, request));
    }

    @RequestMapping(value = "/downLoad1", method = {RequestMethod.GET})
    public void downLoad1(String fixedassetsId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
//        for (int i = 0; i < fixedassetsList.size(); i++) {
            Fixedassets fxs = fixedassetsService.One(fixedassetsId);
            String pp[] = fxs.getRepair().split(" ~ ");
            if (fxs.getRepairkits().equals("PJ010001")) {
                fxs.setRepairkits("有");
            } else {
                fxs.setRepairkits("无");
            }
            Query query = new Query();
            query.addCriteria(Criteria.where("userid").is(fxs.getUser_id()));
            CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
            if (customerInfo != null) {
                fxs.setUser_id(customerInfo.getUserinfo().getCustomername());
            }
            String wfList1 = "";
            String wfList2 = "";
            String wfList3 = "";
            String wfList4 = "";
            String wfList5 = "";
            StartWorkflowVo startWorkflowVo = new StartWorkflowVo();
            startWorkflowVo.setDataId(fxs.getFixedassets_id());
            List<WorkflowLogDetailVo> wfList = workflowServices.ViewWorkflow2(startWorkflowVo, tokenModel.getLocale());
            if (wfList.size() > 0) {
                query = new Query();
                query.addCriteria(Criteria.where("userid").is(wfList.get(0).getUserId()));
                customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                if (customerInfo != null) {
                    wfList1 = customerInfo.getUserinfo().getCustomername();
                }
                query = new Query();
                query.addCriteria(Criteria.where("userid").is(wfList.get(1).getUserId()));
                customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                if (customerInfo != null) {
                    wfList2 = customerInfo.getUserinfo().getCustomername();
                }
                query = new Query();
                query.addCriteria(Criteria.where("userid").is(wfList.get(2).getUserId()));
                customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                if (customerInfo != null) {
                    wfList3 = customerInfo.getUserinfo().getCustomername();
                }
                query = new Query();
                query.addCriteria(Criteria.where("userid").is(wfList.get(3).getUserId()));
                customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                if (customerInfo != null) {
                    wfList4 = customerInfo.getUserinfo().getCustomername();
                }
                query = new Query();
                query.addCriteria(Criteria.where("userid").is(wfList.get(4).getUserId()));
                customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                if (customerInfo != null) {
                    wfList5 = customerInfo.getUserinfo().getCustomername();
                }
            }
            Map<String, Object> data = new HashMap<>();
            data.put("fxs", fxs);
            data.put("wfList1", wfList1);
            data.put("wfList2", wfList2);
            data.put("wfList3", wfList3);
            data.put("wfList4", wfList4);
            data.put("wfList5", wfList5);
            data.put("wfList", wfList);
            if (pp.length > 0) {
                data.put("statime", pp);
            } else {
                data.put("statime", "");
            }
            ExcelOutPutUtil.OutPutPdf("固定資産貸出修理持出決裁願", "gudingzichan_jiechuxiulichichu.xls", data, response);
//        }
    }
}

