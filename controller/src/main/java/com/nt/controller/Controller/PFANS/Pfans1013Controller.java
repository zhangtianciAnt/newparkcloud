package com.nt.controller.Controller.PFANS;

import cn.hutool.core.io.FileUtil;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_Pfans.PFANS1000.*;
import com.nt.dao_Pfans.PFANS1000.Vo.EvectionVo;
import com.nt.dao_Pfans.PFANS1000.Vo.TravelCostVo;
import com.nt.dao_Workflow.Vo.StartWorkflowVo;
import com.nt.dao_Workflow.Vo.WorkflowLogDetailVo;
import com.nt.service_Org.DictionaryService;
import com.nt.service_WorkFlow.WorkflowServices;
import com.nt.service_pfans.PFANS1000.BusinessService;
import com.nt.service_pfans.PFANS1000.EvectionService;
import com.nt.service_pfans.PFANS1000.LoanApplicationService;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/evection")
public class Pfans1013Controller {

    @Autowired
    private EvectionService evectionService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private BusinessService businessService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private WorkflowServices workflowServices;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private LoanApplicationService loanapplicationService;

    @RequestMapping(value = "/exportjs", method = {RequestMethod.GET})
    public void exportjs(String evectionid , HttpServletRequest request, HttpServletResponse response) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        EvectionVo evevo = evectionService.selectById(evectionid);
        String trr = "";
        //外币兑换
        List<Currencyexchange> curlist = evevo.getCurrencyexchanges();
        if(curlist.size() > 0){
            for(Currencyexchange ac : curlist){
//                if(!ac.getCurrency().equals("")){
                    List<Dictionary> curListA = dictionaryService.getForSelect("PG019");
                    for (Dictionary iteA : curListA) {
                        if (iteA.getCode().equals(ac.getCurrency())) {
                            //币种
                            ac.setCurrency(iteA.getValue1());
                        }
                    }
//                }
            }
        }
        //交通费的预算编码
        double cflg = 0;
        double curflg = 0;
        double rmbtra = 0;
        double tratra = 0;
        List<TrafficDetails> tralist = evevo.getTrafficdetails();
        if(tralist.size() > 0){
            trr = "交通费";
            for(TrafficDetails tl : tralist){
                if(tl.getRmb() != null){
                    rmbtra += Double.valueOf(tl.getRmb());
                }
                if(tl.getForeigncurrency() != null){
                    tratra += Double.valueOf(tl.getForeigncurrency());
                }
                List<Dictionary> curListT = dictionaryService.getForSelect("JY002");
                for (Dictionary ite : curListT) {
                    if (ite.getCode().equals(tl.getBudgetcoding())) {
                        tl.setBudgetcoding(ite.getValue2() + "_"+ ite.getValue3());
                    }
                }
                if(tl.getAccountcode().length() > 5){
                    String traAccountcode = tl.getAccountcode().substring(0,5);
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
//                if(!tl.getCurrency().equals("")){
                    List<Dictionary> curListA = dictionaryService.getForSelect("PG019");
                    for (Dictionary iteA : curListA) {
                        if (iteA.getCode().equals(tl.getCurrency())) {
                            //币种
                            tl.setCurrency(iteA.getValue1());
                            if(iteA.getValue2() != null){
                                cflg = Double.valueOf(iteA.getValue2());
                                curflg += Double.valueOf(iteA.getValue2()) * tratra;
                            }
                        }
                    }
//                }
            }
        }
        //住宿费用明细
        List<AccommodationDetails> acclist = evevo.getAccommodationdetails();
        String tro = "";
        double aflg = 0;
        double accflg = 0;
        double rmbacc = 0;
        double traacc = 0;
        if(acclist.size() > 0){
            tro = "住宿费";
            for(AccommodationDetails ac : acclist){
                if(ac.getRmb() != null){
                    rmbacc += Double.valueOf(ac.getRmb());
                }
                if(ac.getTravel() != null){
                    traacc += Double.valueOf(ac.getTravel());
                }
                List<Dictionary> curListT = dictionaryService.getForSelect("JY002");
                for (Dictionary ite : curListT) {
                    if (ite.getCode().equals(ac.getBudgetcoding())) {
                        ac.setBudgetcoding(ite.getValue2() + "_"+ ite.getValue3());
                    }
                }
                if(ac.getAccountcode().length() > 5){
                    String accAccountcode = ac.getAccountcode().substring(0,5);
                    List<Dictionary> curListA = dictionaryService.getForSelect(accAccountcode);
                    for (Dictionary iteA : curListA) {
                        if (iteA.getCode().equals(ac.getAccountcode())) {
                            //科目名
                            ac.setAccountcode(iteA.getValue1());
                            //科目代码
//                            tl.setSubjectnumber(iteA.getValue2());
                        }
                    }
                }
//                if(!ac.getCurrency().equals("")){
                    List<Dictionary> curListAc = dictionaryService.getForSelect("PG019");
                    for (Dictionary iteA : curListAc) {
                        if (iteA.getCode().equals(ac.getCurrency())) {
                            //币种
                            ac.setCurrency(iteA.getValue1());
                            if(iteA.getValue2() != null){
                                aflg = Double.valueOf(iteA.getValue2());
                                accflg += Double.valueOf(iteA.getValue2()) * traacc;
                            }
                        }
                    }
//                }

            }
        }
        //出差地点
//        if(!evevo.getEvection().getPlace().equals("") && !evevo.getEvection().getPlace().equals(null)){
            List<Dictionary> curList1 = dictionaryService.getForSelect("PJ036");
            for (Dictionary item : curList1) {
                if (item.getCode().equals(evevo.getEvection().getPlace())) {
                    evevo.getEvection().setPlace(item.getValue1());
                }
            }
//        }
        Query query = new Query();
        String userim = "";
        query.addCriteria(Criteria.where("userid").is(evevo.getEvection().getUserid()));
        CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
        if (customerInfo != null) {
            //申请人
            evevo.getEvection().setUserid(customerInfo.getUserinfo().getCustomername());
            userim = customerInfo.getUserinfo().getCustomername();
            userim = sign.startGraphics2D(userim);
            //部门
            evevo.getEvection().setGroupid(customerInfo.getUserinfo().getGroupname());
        }
        //获取审批节点的负责人
        String wfList1 = "";
        String wfList2 = "";
        String wfList3 = "";
        String wfList4 = "";
        StartWorkflowVo startWorkflowVo = new StartWorkflowVo();
        startWorkflowVo.setDataId(evevo.getEvection().getEvectionid());
        List<WorkflowLogDetailVo> wfList = workflowServices.ViewWorkflow2(startWorkflowVo, tokenModel.getLocale());
        if (wfList.size() > 0) {
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
            query = new Query();
            query.addCriteria(Criteria.where("userid").is(wfList.get(2).getUserId()));
            customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
            if (customerInfo != null) {
                wfList3 = customerInfo.getUserinfo().getCustomername();
                wfList3 = sign.startGraphics2D(wfList3);
            }
            query = new Query();
            query.addCriteria(Criteria.where("userid").is(wfList.get(3).getUserId()));
            customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
            if (customerInfo != null) {
                wfList4 = customerInfo.getUserinfo().getCustomername();
                wfList4 = sign.startGraphics2D(wfList4);
            }
        }
        Map<String, Object> data = new HashMap<>();
        String str_format = "";
        DecimalFormat df = new DecimalFormat("###,###.00");
        if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(evevo.getEvection().getLoanamount())){
            BigDecimal bd = new BigDecimal(evevo.getEvection().getLoanamount());
            str_format = df.format(bd);
            if(str_format.equals(".00"))
            {
                str_format = "0.00";
            }
            evevo.getEvection().setLoanamount(str_format);
        }
        else{
            evevo.getEvection().setLoanamount("0.00");
        }
        if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(evevo.getEvection().getBalance())){
            BigDecimal bd = new BigDecimal(evevo.getEvection().getBalance());
            str_format = df.format(bd);
            if(str_format.equals(".00"))
            {
                str_format = "0.00";
            }
            evevo.getEvection().setBalance(str_format);
        }
        else{
            evevo.getEvection().setBalance("0.00");
        }

        for (int h = 0; h < evevo.getCurrencyexchanges().size(); h++) {
            if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(evevo.getCurrencyexchanges().get(h).getAmount())){
                BigDecimal bd = new BigDecimal(evevo.getCurrencyexchanges().get(h).getAmount());
                str_format = df.format(bd);
                if(str_format.equals(".00"))
                {
                    str_format = "0.00";
                }
                evevo.getCurrencyexchanges().get(h).setAmount(str_format);
            }
            else{
                evevo.getCurrencyexchanges().get(h).setAmount("0.00");
            }
        }

        for (int k = 0; k < evevo.getTrafficdetails().size(); k++) {
            if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(evevo.getTrafficdetails().get(k).getForeigncurrency())){
                BigDecimal bd = new BigDecimal(evevo.getTrafficdetails().get(k).getForeigncurrency());
                str_format = df.format(bd);
                if(str_format.equals(".00"))
                {
                    str_format = "0.00";
                }
                evevo.getTrafficdetails().get(k).setForeigncurrency(str_format);
            }
            if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(evevo.getTrafficdetails().get(k).getRmb())){
                BigDecimal bd = new BigDecimal(evevo.getTrafficdetails().get(k).getRmb());
                str_format = df.format(bd);
                if(str_format.equals(".00"))
                {
                    str_format = "0.00";
                }
                evevo.getTrafficdetails().get(k).setRmb(str_format);
            }
            else{
                evevo.getTrafficdetails().get(k).setRmb("0.00");
            }
        }

        for (int m = 0; m < evevo.getAccommodationdetails().size(); m++) {
            if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(evevo.getAccommodationdetails().get(m).getTravel())){
                BigDecimal bd = new BigDecimal(evevo.getAccommodationdetails().get(m).getTravel());
                str_format = df.format(bd);
                if(str_format.equals(".00"))
                {
                    str_format = "0.00";
                }
                evevo.getAccommodationdetails().get(m).setTravel(str_format);
            }
            else{
                evevo.getAccommodationdetails().get(m).setTravel("0.00");
            }
            if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(evevo.getAccommodationdetails().get(m).getRmb())){
                BigDecimal bd = new BigDecimal(evevo.getAccommodationdetails().get(m).getRmb());
                str_format = df.format(bd);
                if(str_format.equals(".00"))
                {
                    str_format = "0.00";
                }
                evevo.getAccommodationdetails().get(m).setRmb(str_format);
            }
            else{
                evevo.getAccommodationdetails().get(m).setRmb("0.00");
            }
        }

        String rmbflg = df.format(new BigDecimal(String.valueOf(rmbacc + rmbtra)));
        if(rmbflg.equals(".00")){
            rmbflg = "0.00";
        }
        String traflg = df.format(new BigDecimal(String.valueOf(tratra + traacc)));
        if(traflg.equals(".00")){
            traflg = "0.00";
        }
        String sumrmb = df.format(new BigDecimal(String.valueOf(accflg + curflg + rmbacc + rmbtra)));
        if(sumrmb.equals(".00")){
            sumrmb = "0.00";
        }
        data.put("wfList1", wfList1);
        data.put("wfList2", wfList2);
        data.put("wfList3", wfList3);
        data.put("wfList4", wfList4);
        data.put("trr", trr);
        data.put("tro", tro);
        data.put("cflg", cflg);
        data.put("aflg", aflg);
        data.put("userim", userim);
        data.put("rmbflg", rmbflg);
        data.put("traflg", traflg);
        data.put("sumrmb", sumrmb);
        data.put("eve", evevo.getEvection());
        data.put("cur", evevo.getCurrencyexchanges());
        data.put("tra", evevo.getTrafficdetails());
        data.put("acc", evevo.getAccommodationdetails());
        if(evevo.getEvection().getType().equals("0")){
            ExcelOutPutUtil.OutPutPdf("境内出差旅费精算书", "jingneijingsuanshu.xlsx", data, response);
        } else {
            ExcelOutPutUtil.OutPutPdf("境外出差旅费精算书", "jingwaijingsuanshu.xlsx", data, response);
        }
//        FileUtil.del("E:\\PFANS\\image" + "/" + wfList1);
//        FileUtil.del("E:\\PFANS\\image" + "/" + wfList2);
//        FileUtil.del("E:\\PFANS\\image" + "/" + wfList3);
//        FileUtil.del("E:\\PFANS\\image" + "/" + wfList4);
//        FileUtil.del("E:\\PFANS\\image" + "/" + userim);
    }


    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    public ApiResult get(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        Evection evection = new Evection();
        evection.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(evectionService.get(evection));
    }
    @RequestMapping(value = "/selectById", method = {RequestMethod.GET})
    public ApiResult selectById(String evectionid, HttpServletRequest request) throws Exception {
        if (evectionid == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(evectionService.selectById(evectionid));
    }
    @RequestMapping(value = "/create", method = {RequestMethod.POST})
    public ApiResult create(@RequestBody EvectionVo evectionVo, HttpServletRequest request) throws Exception {
        if (evectionVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        evectionService.insertEvectionVo(evectionVo, tokenModel);
        return ApiResult.success();
    }
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody EvectionVo evectionVo, HttpServletRequest request) throws Exception {
        if (evectionVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        evectionService.updateEvectionVo(evectionVo, tokenModel);
        return ApiResult.success();
    }
    @RequestMapping(value="/getBusiness" ,method = {RequestMethod.GET})
    public ApiResult getBusiness(HttpServletRequest request) throws Exception{
        return ApiResult.success(businessService.getBuse());
    }

    @RequestMapping(value="/gettravelcostvo" ,method = {RequestMethod.POST})
    public ApiResult gettravelcostvo(@RequestBody TravelCostVo travelcostvo, HttpServletRequest request) throws Exception{
        if(travelcostvo==null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(evectionService.gettravelcost(travelcostvo));
    }

    @RequestMapping(value="/getLoanApplication" ,method = {RequestMethod.GET})
    public ApiResult getLoanApplication(HttpServletRequest request) throws Exception {
        return ApiResult.success(loanapplicationService.getLoapp());
    }
}
