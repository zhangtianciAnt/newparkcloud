package com.nt.controller.Controller.PFANS;

import cn.hutool.core.io.FileUtil;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_Pfans.PFANS1000.*;
import com.nt.dao_Pfans.PFANS1000.Vo.PublicExpenseVo;
import com.nt.dao_Pfans.PFANS1000.Vo.TotalCostVo;
import com.nt.service_Org.DictionaryService;
import com.nt.dao_Workflow.Vo.StartWorkflowVo;
import com.nt.dao_Workflow.Vo.WorkflowLogDetailVo;
import com.nt.service_WorkFlow.WorkflowServices;
import com.nt.service_pfans.PFANS1000.JudgementService;

import com.nt.service_pfans.PFANS1000.LoanApplicationService;
import com.nt.service_pfans.PFANS1000.PublicExpenseService;
import com.nt.service_pfans.PFANS1000.mapper.LoanApplicationMapper;
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
import com.nt.service_Org.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/publicexpense")
public class Pfans1012Controller {

    @Autowired
    private PublicExpenseService publicExpenseService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private WorkflowServices workflowServices;

    @Autowired
    private JudgementService judgementService;

    @Autowired
    private LoanApplicationMapper loanapplicationMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private UserService userService;

    @Autowired
    private LoanApplicationService loanapplicationService;

    @RequestMapping(value = "/exportjs", method = {RequestMethod.GET})
    public void exportjs(String publicexpenseid, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        PublicExpenseVo pubvo = publicExpenseService.selectById(publicexpenseid);
        String trr = "";
        //交通费的预算编码
        List<TrafficDetails> tralist = pubvo.getTrafficdetails();
        if (tralist.size() > 0) {
            trr = "交通费";
            for (TrafficDetails tl : tralist) {
                List<Dictionary> curListT = dictionaryService.getForSelect("JY002");
                for (Dictionary ite : curListT) {
                    if (ite.getCode().equals(tl.getBudgetcoding())) {
                        tl.setBudgetcoding(ite.getValue2() + "_" + ite.getValue3());
                    }
                }
                if (tl.getAccountcode().length() > 5) {
                    String traAccountcode = tl.getAccountcode().substring(0, 5);
                    List<Dictionary> curListA = dictionaryService.getForSelect(traAccountcode);
                    for (Dictionary iteA : curListA) {
                        if (iteA.getCode().equals(tl.getAccountcode())) {
                            //科目名
                            tl.setAccountcode(iteA.getValue1());
                            //科目代码
//                            tl.setSubjectnumber(iteA.getValue2());
                        }
                    }
                }
            }
        }
        //采购明细
        List<PurchaseDetails> purlist = pubvo.getPurchasedetails();
        if (purlist.size() > 0) {
            trr = "采购费";
            for (PurchaseDetails pl : purlist) {
                List<Dictionary> curListT = dictionaryService.getForSelect("JY002");
                for (Dictionary ite : curListT) {
                    if (ite.getCode().equals(pl.getBudgetcoding())) {
                        pl.setBudgetcoding(ite.getValue2() + "_" + ite.getValue3());
                    }
                }
                if (pl.getAccountcode().length() > 5) {
                    String purAccountcode = pl.getAccountcode().substring(0, 5);
                    List<Dictionary> curListA = dictionaryService.getForSelect(purAccountcode);
                    for (Dictionary iteA : curListA) {
                        if (iteA.getCode().equals(pl.getAccountcode())) {
                            //科目名
                            pl.setAccountcode(iteA.getValue1());
                            //科目代码
//                            tl.setSubjectnumber(iteA.getValue2());
                        }
                    }
                }
            }
        }
        //其他费用明细
        List<OtherDetails> othlist = pubvo.getOtherdetails();
        String tro = "";
        if (othlist.size() > 0) {
            tro = "其他费用";
            for (OtherDetails ol : othlist) {
                List<Dictionary> curListT = dictionaryService.getForSelect("JY002");
                for (Dictionary ite : curListT) {
                    if (ite.getCode().equals(ol.getBudgetcoding())) {
                        ol.setBudgetcoding(ite.getValue2() + "_" + ite.getValue3());
                    }
                }
                if (ol.getAccountcode().length() > 5) {
                    String othAccountcode = ol.getAccountcode().substring(0, 5);
                    List<Dictionary> curListA = dictionaryService.getForSelect(othAccountcode);
                    for (Dictionary iteA : curListA) {
                        if (iteA.getCode().equals(ol.getAccountcode())) {
                            //科目名
                            ol.setAccountcode(iteA.getValue1());
                            //科目代码
//                            tl.setSubjectnumber(iteA.getValue2());
                        }
                    }
                }
            }
        }

//            String str = "";
//            if(pubvo.getTrafficdetails().size() > 0){
//                str = "交通费";
//            }
//            if(pubvo.getPurchasedetails().size() > 0){
//                if(str == ""){
//                    str = "采购费";
//                } else {
//                    str += ","+"采购费";
//                }
//            }
//            if(pubvo.getOtherdetails().size() > 0){
//                if(str == ""){
//                    str = "其他费用";
//                } else {
//                    str += ","+"其他费用";
//                }
//            }
        //模块
        List<Dictionary> curList1 = dictionaryService.getForSelect("PJ002");
        for (Dictionary item : curList1) {
            if (item.getCode().equals(pubvo.getPublicexpense().getModuleid())) {
                pubvo.getPublicexpense().setModuleid(item.getValue1());
            }
        }
        //申请人图片


        //add-ws-5/25-No.196
        String user = pubvo.getPublicexpense().getUser_name();
        Query query3 = new Query();
        query3.addCriteria(Criteria.where("userid").is(user));
        CustomerInfo customerInfolist = mongoTemplate.findOne(query3, CustomerInfo.class);
        if (customerInfolist != null) {
            pubvo.getPublicexpense().setUser_name(customerInfolist.getUserinfo().getCustomername());
        }
        //add-ws-5/25-No.196
        String taa = "";
        String userim = "";
        Query query = new Query();
        query.addCriteria(Criteria.where("userid").is(pubvo.getPublicexpense().getUser_id()));
        CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
        if (customerInfo != null) {
            taa = customerInfo.getUserinfo().getCaiwupersonalcode();
            //申请人
            pubvo.getPublicexpense().setUser_id(customerInfo.getUserinfo().getCustomername());
            userim = customerInfo.getUserinfo().getCustomername();
            userim = sign.startGraphics2D(userim);
            //部门
            pubvo.getPublicexpense().setGroupid(customerInfo.getUserinfo().getGroupname());
        }
        //获取审批节点的负责人
        String wfList1 = "";
        String wfList2 = "";
        String wfList3 = "";
        String wfList4 = "";
        String wfList7 = "";
        StartWorkflowVo startWorkflowVo = new StartWorkflowVo();
        startWorkflowVo.setDataId(pubvo.getPublicexpense().getPublicexpenseid());
        List<WorkflowLogDetailVo> wfList = workflowServices.ViewWorkflow2(startWorkflowVo, tokenModel.getLocale());
        if (wfList.size() > 0) {
          //UPD-WS-6/12-禅道105
            query = new Query();
            if (wfList.size()>5){
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
                if(wfList.get(3).getRemark().equals("系统自动跳过")){
                    wfList3 = "";
                }else {
                    query = new Query();
                    query.addCriteria(Criteria.where("userid").is(wfList.get(2).getUserId()));
                    customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                    if (customerInfo != null) {
                        wfList3 = customerInfo.getUserinfo().getCustomername();
                        wfList3 = sign.startGraphics2D(wfList3);
                    }
                }
                if(wfList.get(4).getRemark().equals("系统自动跳过")){
                    wfList4 = "";
                }else{
                    query = new Query();
                    query.addCriteria(Criteria.where("userid").is(wfList.get(3).getUserId()));
                    customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                    if (customerInfo != null) {
                        wfList4 = customerInfo.getUserinfo().getCustomername();
                        wfList4 = sign.startGraphics2D(wfList4);
                    }
                }

            }else{
                query = new Query();
                query.addCriteria(Criteria.where("userid").is(wfList.get(0).getUserId()));
                customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                if (customerInfo != null) {
                    wfList1 = customerInfo.getUserinfo().getCustomername();
                    wfList1 = sign.startGraphics2D(wfList1);
                }
                query = new Query();
                query.addCriteria(Criteria.where("userid").is(wfList.get(1).getUserId()));
                customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                if (customerInfo != null) {
                    wfList2 = customerInfo.getUserinfo().getCustomername();
                    wfList2 = sign.startGraphics2D(wfList2);
                }
                if(wfList.get(2).getRemark().equals("系统自动跳过")){
                    wfList3 = "";
                }else {
                    query = new Query();
                    query.addCriteria(Criteria.where("userid").is(wfList.get(2).getUserId()));
                    customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                    if (customerInfo != null) {
                        wfList3 = customerInfo.getUserinfo().getCustomername();
                        wfList3 = sign.startGraphics2D(wfList3);
                    }
                }
                if(wfList.get(3).getRemark().equals("系统自动跳过")){
                    wfList4 = "";
                }else{
                    query = new Query();
                    query.addCriteria(Criteria.where("userid").is(wfList.get(3).getUserId()));
                    customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                    if (customerInfo != null) {
                        wfList4 = customerInfo.getUserinfo().getCustomername();
                        wfList4 = sign.startGraphics2D(wfList4);
                    }
                }
            }
            //UPD-WS-6/12-禅道105
        }
        Map<String, Object> data = new HashMap<>();
        if("PJ004004".equals(pubvo.getPublicexpense().getPaymentmethod())){
            String payeeName = "";
            LoanApplication loanApplication = new LoanApplication();
            loanApplication = loanapplicationMapper.selectByPrimaryKey(pubvo.getPublicexpense().getLoan());
            payeeName = loanApplication.getPayeename();
            data.put("payeeName", payeeName);
        }
        String str_format = "";
        DecimalFormat df = new DecimalFormat("###,###.00");
        if (pubvo.getPublicexpense().getRmbexpenditure() != null) {
            BigDecimal bd = new BigDecimal(pubvo.getPublicexpense().getRmbexpenditure());
            str_format = df.format(bd);
            pubvo.getPublicexpense().setRmbexpenditure(str_format);
        }
        for (int k = 0; k < pubvo.getTrafficdetails().size(); k++) {
            if (pubvo.getTrafficdetails().get(k).getRmb() != null) {
                BigDecimal bd = new BigDecimal(pubvo.getTrafficdetails().get(k).getRmb());
                str_format = df.format(bd);
                pubvo.getTrafficdetails().get(k).setRmb(str_format);
            }
        }
        data.put("wfList1", wfList1);
        data.put("wfList2", wfList2);
        data.put("wfList3", wfList3);
        data.put("wfList4", wfList4);
        data.put("wfList7", wfList7);
        data.put("userim", userim);
        data.put("taa", taa);
        data.put("trr", trr);
        data.put("tro", tro);
        data.put("pub", pubvo.getPublicexpense());
        data.put("tra", pubvo.getTrafficdetails());
        data.put("pur", pubvo.getPurchasedetails());
        data.put("otd", pubvo.getOtherdetails());
        BigDecimal bd = new BigDecimal(pubvo.getPublicexpense().getMoneys());
        if(bd.intValue()>=20000){
            if (pubvo.getTrafficdetails().size() > 0) {
                ExcelOutPutUtil.OutPutPdf("公共費用精算書", "newgonggongfeiyongjingsuanshu.xls", data, response);
            } else {
                ExcelOutPutUtil.OutPutPdf("公共費用精算書", "newgonggongfeiyongjingsuanshu_other.xls", data, response);
            }
        }else{
            if (pubvo.getTrafficdetails().size() > 0) {
                ExcelOutPutUtil.OutPutPdf("公共費用精算書", "gonggongfeiyongjingsuanshu.xls", data, response);
            } else {
                ExcelOutPutUtil.OutPutPdf("公共費用精算書", "gonggongfeiyongjingsuanshu_other.xls", data, response);
            }
        }
        FileUtil.del("E:\\PFANS\\image" + "/" + wfList1);
        FileUtil.del("E:\\PFANS\\image" + "/" + wfList2);
        FileUtil.del("E:\\PFANS\\image" + "/" + wfList3);
        FileUtil.del("E:\\PFANS\\image" + "/" + wfList4);
        FileUtil.del("E:\\PFANS\\image" + "/" + wfList7);
        FileUtil.del("E:\\PFANS\\image" + "/" + userim);
    }

    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    public ApiResult get(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        PublicExpense publicExpense = new PublicExpense();
        publicExpense.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(publicExpenseService.get(publicExpense));
    }

    @RequestMapping(value = "/getpublicelist", method = {RequestMethod.GET})
    public ApiResult getpublicelist(String publicexpenseid, HttpServletRequest request) throws Exception {
        if (publicexpenseid == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenMode1 = tokenService.getToken(request);
        return ApiResult.success(publicExpenseService.getpublicelist(publicexpenseid));
    }

    @RequestMapping(value = "/selectById", method = {RequestMethod.GET})
    public ApiResult selectById(String publicexpenseid, HttpServletRequest request) throws Exception {
        if (publicexpenseid == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenMode1 = tokenService.getToken(request);
        return ApiResult.success(publicExpenseService.selectById(publicexpenseid));
    }

    @RequestMapping(value = "/insert", method = {RequestMethod.POST})
    public ApiResult insert(@RequestBody PublicExpenseVo publicExpenseVo, HttpServletRequest request) throws Exception {
        if (publicExpenseVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        publicExpenseService.insert(publicExpenseVo, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody PublicExpenseVo publicExpenseVo, HttpServletRequest request) throws Exception {
        if (publicExpenseVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        publicExpenseService.update(publicExpenseVo, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/gettotalcost", method = {RequestMethod.POST})
    public ApiResult gettotalcost(@RequestBody TotalCostVo totalcostvo, HttpServletRequest request) throws Exception {
        if (totalcostvo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(publicExpenseService.gettotalcost(totalcostvo));
    }


    @RequestMapping(value = "/getJudgement", method = {RequestMethod.POST})
    public ApiResult getJudgement(@RequestBody Judgement judgement, HttpServletRequest request) throws Exception {
        if (judgement == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(judgementService.getJudgement(judgement));
    }


    @RequestMapping(value = "/getLoanApplication", method = {RequestMethod.POST})
    public ApiResult getLoanApplication(@RequestBody LoanApplication loanapplication, HttpServletRequest request) throws Exception {
        if (loanapplication == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
//        TokenModel tokenModel=tokenService.getToken(request);
        return ApiResult.success(loanapplicationService.getLoanApplication(loanapplication));
    }


}
