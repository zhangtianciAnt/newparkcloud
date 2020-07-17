package com.nt.controller.Controller.PFANS;

import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_Pfans.PFANS1000.LoanApplication;
import com.nt.dao_Pfans.PFANS1000.PurchaseApply;
import com.nt.dao_Workflow.Vo.StartWorkflowVo;
import com.nt.dao_Workflow.Vo.WorkflowLogDetailVo;
import com.nt.service_Org.DictionaryService;
import com.nt.service_WorkFlow.WorkflowServices;
import com.nt.service_pfans.PFANS1000.LoanApplicationService;
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
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/loanapplication")
public class Pfans1006Controller {

    @Autowired
    private LoanApplicationService loanapplicationService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private WorkflowServices workflowServices;

    @Autowired
    private DictionaryService dictionaryService;


    @RequestMapping(value="/get",method = {RequestMethod.GET})
    public ApiResult getLoanapplication(HttpServletRequest request)throws  Exception{
        TokenModel tokenModel = tokenService.getToken(request);
        LoanApplication loanapplication = new LoanApplication();
        loanapplication.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(loanapplicationService.getLoanApplication(loanapplication));

    }

    @RequestMapping(value = "/one",method={RequestMethod.POST})
    public ApiResult one(@RequestBody LoanApplication loanapplication, HttpServletRequest request) throws Exception {
        if (loanapplication == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(loanapplicationService.One(loanapplication.getLoanapplication_id()));
    }

    @RequestMapping(value="/update",method = {RequestMethod.POST})
    public ApiResult updateFlexibleWork(@RequestBody LoanApplication loanapplication, HttpServletRequest request) throws Exception{
        if (loanapplication == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        loanapplicationService.updateLoanApplication(loanapplication,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/create",method={RequestMethod.POST})
    public ApiResult create(@RequestBody LoanApplication loanapplication, HttpServletRequest request) throws Exception {
        if (loanapplication == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        loanapplicationService.insert(loanapplication,tokenModel);
        return ApiResult.success();
    }

    //add_fjl_0725  添加暂借款打印功能  start
    @RequestMapping(value = "/exportjs", method = {RequestMethod.GET})
    public void exportjs(String loanapplicationid, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        LoanApplication loanApplication = loanapplicationService.One(loanapplicationid);
        Map<String, Object> data = new HashMap<>();
        if (loanApplication != null) {
            String userim = "";
            String taa = "";
            String tan = "";
            Query query = new Query();
            query.addCriteria(Criteria.where("userid").is(loanApplication.getUser_id()));
            CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
            if (customerInfo != null) {
                taa = customerInfo.getUserinfo().getCaiwupersonalcode();
                //申请人
                loanApplication.setUser_id(customerInfo.getUserinfo().getCustomername());
                userim = customerInfo.getUserinfo().getCustomername();
                userim = sign.startGraphics2D(userim);
                //部门
                loanApplication.setGroup_id(customerInfo.getUserinfo().getGroupname());
            }
            query = new Query();
            query.addCriteria(Criteria.where("userid").is(loanApplication.getUser_name()));
            customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
            if (customerInfo != null) {
                tan = customerInfo.getUserinfo().getCaiwupersonalcode();
                //收款人
                loanApplication.setUser_name(customerInfo.getUserinfo().getCustomername());
            }
            //货币种类
            List<Dictionary> curList1 = dictionaryService.getForSelect("PG019");
            for (Dictionary item : curList1) {
                if (item.getCode().equals(loanApplication.getCurrencychoice())) {
                    loanApplication.setCurrencychoice(item.getValue3());
                }
            }
            //预算单位
            List<Dictionary> curList2 = dictionaryService.getForSelect("JY002");
            for (Dictionary item : curList2) {
                if (item.getCode().equals(loanApplication.getBudgetunit())) {
                    loanApplication.setBudgetunit(item.getValue2() + "_" + item.getValue3());
                }
            }
            //获取审批节点的负责人
            String wfList1 = "";
            String wfList2 = "";
            String wfList3 = "";
            String wfList4 = "";
            String wfList7 = "";
            StartWorkflowVo startWorkflowVo = new StartWorkflowVo();
            startWorkflowVo.setDataId(loanApplication.getLoanapplication_id());
            List<WorkflowLogDetailVo> wfList = workflowServices.ViewWorkflow2(startWorkflowVo, tokenModel.getLocale());
            if (wfList.size() > 0) {
                query = new Query();
                query.addCriteria(Criteria.where("userid").is(wfList.get(0).getUserId()));
                customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                if (customerInfo != null) {
                    wfList7 = customerInfo.getUserinfo().getCustomername();
                    wfList7 = sign.startGraphics2D(wfList7);
                }
                query = new Query();
                query.addCriteria(Criteria.where("userid").is(wfList.get(1).getUserId()));
                customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                if (customerInfo != null) {
                    wfList1 = customerInfo.getUserinfo().getCustomername();
                    wfList1 = sign.startGraphics2D(wfList1);
                }
                query = new Query();
                query.addCriteria(Criteria.where("userid").is(wfList.get(2).getUserId()));
                customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                if (customerInfo != null) {
                    wfList2 = customerInfo.getUserinfo().getCustomername();
                    wfList2 = sign.startGraphics2D(wfList2);
                }
                if (wfList.get(3).getRemark() != null) {
                    if (wfList.get(3).getRemark().equals("系统自动跳过")) {
                        wfList3 = "";
                    } else {
                        query = new Query();
                        query.addCriteria(Criteria.where("userid").is(wfList.get(3).getUserId()));
                        customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                        if (customerInfo != null) {
                            wfList3 = customerInfo.getUserinfo().getCustomername();
                            wfList3 = sign.startGraphics2D(wfList3);
                        }
                    }
                } else {
                    query = new Query();
                    query.addCriteria(Criteria.where("userid").is(wfList.get(3).getUserId()));
                    customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                    if (customerInfo != null) {
                        wfList3 = customerInfo.getUserinfo().getCustomername();
                        wfList3 = sign.startGraphics2D(wfList3);
                    }
                }
                if (wfList.get(4).getRemark() != null) {
                    if (wfList.get(4).getRemark().equals("系统自动跳过")) {
                        wfList4 = "";
                    } else {
                        query = new Query();
                        query.addCriteria(Criteria.where("userid").is(wfList.get(4).getUserId()));
                        customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                        if (customerInfo != null) {
                            wfList4 = customerInfo.getUserinfo().getCustomername();
                            wfList4 = sign.startGraphics2D(wfList4);
                        }
                    }
                } else {
                    query = new Query();
                    query.addCriteria(Criteria.where("userid").is(wfList.get(4).getUserId()));
                    customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                    if (customerInfo != null) {
                        wfList4 = customerInfo.getUserinfo().getCustomername();
                        wfList4 = sign.startGraphics2D(wfList4);
                    }
                }
            }
            String str_format = "";
            DecimalFormat df = new DecimalFormat("###,###.00");
            if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(loanApplication.getMoneys())) {
                BigDecimal bd = new BigDecimal(loanApplication.getMoneys());
                str_format = df.format(bd);
                if (str_format.equals(".00")) {
                    str_format = "0.00";
                }
                loanApplication.setMoneys(str_format);
            } else {
                loanApplication.setMoneys("0.00");
            }
            data.put("wfList1", wfList1);
            data.put("wfList2", wfList2);
            data.put("wfList3", wfList3);
            data.put("wfList4", wfList4);
            data.put("wfList7", wfList7);
            data.put("taa", taa);
            data.put("tan", tan);
            data.put("userim", userim);
            data.put("loan", loanApplication);

            ExcelOutPutUtil.OutPutPdf("暂借款申请单", "zanjiekuan.xls", data, response);

            ExcelOutPutUtil.deleteDir("E:\\PFANS\\image");
        }
    }
    //add_fjl_0725  添加暂借款打印功能  end
}
