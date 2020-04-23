package com.nt.controller.Controller.PFANS;

import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_Pfans.PFANS1000.Salesdetails;
import com.nt.dao_Pfans.PFANS1000.Scrapdetails;
import com.nt.dao_Pfans.PFANS1000.Vo.AssetinformationVo;
import com.nt.dao_Pfans.PFANS1000.Assetinformation;
import com.nt.dao_Workflow.Vo.StartWorkflowVo;
import com.nt.dao_Workflow.Vo.WorkflowLogDetailVo;
import com.nt.service_Org.DictionaryService;
import com.nt.service_WorkFlow.WorkflowServices;
import com.nt.service_pfans.PFANS1000.AssetinformationService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/assetinformation")
public class Pfans1007Controller {

    @Autowired
    private AssetinformationService assetinformationService;
    @Autowired
    private WorkflowServices workflowServices;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private TokenService tokenService;

    @RequestMapping(value="/get",method = {RequestMethod.GET})
    public ApiResult get(HttpServletRequest request) throws Exception{
            TokenModel tokenModel = tokenService.getToken(request);
            Assetinformation assetinformation = new Assetinformation();
            assetinformation.setOwners(tokenModel.getOwnerList());
            return ApiResult.success(assetinformationService.getAssetinformation(assetinformation));
    }

    @RequestMapping(value = "/selectById", method = {RequestMethod.GET})
    public ApiResult selectById(String assetinformationid, HttpServletRequest request) throws Exception {
        if (assetinformationid == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(assetinformationService.selectById(assetinformationid));
    }

    @RequestMapping(value="/update",method = {RequestMethod.POST})
    public ApiResult updateAssetinformation(@RequestBody AssetinformationVo assetinformationVo, HttpServletRequest request) throws Exception{
        if (assetinformationVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        assetinformationService.updateAssetinformation(assetinformationVo,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "insert", method = {RequestMethod.POST})
    public ApiResult insert(@RequestBody AssetinformationVo assetinformationVo, HttpServletRequest request) throws Exception {
        if (assetinformationVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        assetinformationService.insert(assetinformationVo, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/downLoad1", method = {RequestMethod.POST})
    public void downLoad1(@RequestBody List<String> assetinformationList, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        for (int i = 0; i < assetinformationList.size(); i++) {
            String wfList1 = "";
            String wfList2 = "";
            String wfList3 = "";
            String wfList4 = "";
            String wfList5 = "";
            String wfList6 = "";
            AssetinformationVo asvo = assetinformationService.selectById(assetinformationList.get(i));
            StartWorkflowVo startWorkflowVo = new StartWorkflowVo();
            startWorkflowVo.setDataId(asvo.getAssetinformation().getAssetinformationid());
            List<WorkflowLogDetailVo> wfList = workflowServices.ViewWorkflow2(startWorkflowVo, tokenModel.getLocale());
            List<Salesdetails> saList = asvo.getSalesdetails();
            List<Scrapdetails> scList = asvo.getScrapdetails();
            Query query = new Query();
            CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
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
                query = new Query();
                query.addCriteria(Criteria.where("userid").is(wfList.get(5).getUserId()));
                customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                if (customerInfo != null) {
                    wfList6 = customerInfo.getUserinfo().getCustomername();
                }
            }
            List<Dictionary> curList = dictionaryService.getForSelect("PJ012");
            for (Dictionary item : curList) {
                if (item.getCode().equals(asvo.getAssetinformation().getProcessingmethod())) {
                    asvo.getAssetinformation().setProcessingmethod(item.getValue1());
                }
            }
            List<Dictionary> curList1 = dictionaryService.getForSelect("PJ013");
            for (Dictionary item : curList1) {
                if (item.getCode().equals(asvo.getAssetinformation().getSalequotation())) {
                    asvo.getAssetinformation().setSalequotation(item.getValue1());
                }
            }
            Map<String, Object> data = new HashMap<>();
            data.put("wfList1", wfList1);
            data.put("wfList2", wfList2);
            data.put("wfList3", wfList3);
            data.put("wfList4", wfList4);
            data.put("wfList5", wfList5);
            data.put("wfList", wfList);
            data.put("asvo", asvo);
            data.put("saList", asvo.getSalesdetails());
            data.put("scList", asvo.getScrapdetails());
            ExcelOutPutUtil.OutPutPdf("固定资产·软件处理决裁", "gudingzichanzy.xls", data, response);
        }
    }
}
