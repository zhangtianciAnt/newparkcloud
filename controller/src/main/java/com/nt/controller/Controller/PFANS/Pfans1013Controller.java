package com.nt.controller.Controller.PFANS;

import cn.hutool.core.io.FileUtil;
import com.mysql.jdbc.StringUtils;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_Org.UserAccount;
import com.nt.dao_Pfans.PFANS1000.*;
import com.nt.dao_Pfans.PFANS1000.Vo.BusinessVo;
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
import java.util.*;
import java.util.stream.Collectors;

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

    private static final String TAX_KEY = "__TAX_KEY__";

    @RequestMapping(value = "/exportjs", method = {RequestMethod.GET})
    public void exportjs(String evectionid, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        EvectionVo evevo = evectionService.selectById(evectionid);
        //add_fjl_0717 添加出差申请编号  start
        BusinessVo business = businessService.selectById(evevo.getEvection().getBusiness_id());
        //add_fjl_0717 添加出差申请编号  end
        Map<String, Object> list = evectionService.exportjs(evectionid, request);
        List<TrafficDetails> trafficlist = (List<TrafficDetails>) list.getOrDefault("交通费", new ArrayList<>());
        List<AccommodationDetails> accommodationlist = (List<AccommodationDetails>) list.getOrDefault("住宿费", new ArrayList<>());
//        List<AccommodationDetails> AccommodationList = (List<AccommodationDetails>) list.getOrDefault("补贴费用", new ArrayList<>());
        List<OtherDetails> otherDetailslist = (List<OtherDetails>) list.getOrDefault("其他费用", new ArrayList<>());
        String trr = "";
        //外币兑换
        List<Currencyexchange> curlist = evevo.getCurrencyexchanges();
        if (curlist.size() > 0) {
            for (Currencyexchange ac : curlist) {
                List<Dictionary> curListA = dictionaryService.getForSelect("PG019");
                for (Dictionary iteA : curListA) {
                    if (iteA.getCode().equals(ac.getCurrency())) {
                        ac.setCurrency(iteA.getValue1());
                    }
                }
            }
        }
        //交通费的预算编码
        double cflg = 0;
        double curflg = 0;
        double rmbtra = 0;
        double tratra = 0;
        List<TrafficDetails> tralist = evevo.getTrafficdetails();
        trr = "交通费";
        for (TrafficDetails tl : tralist) {
            if (tl.getRmb() != null) {
                rmbtra += Double.valueOf(tl.getRmb());
            }
            if (tl.getForeigncurrency() != null) {
                tratra += Double.valueOf(tl.getForeigncurrency());
            }
            List<Dictionary> curListA = dictionaryService.getForSelect("PG019");
            for (Dictionary iteA : curListA) {
                if (iteA.getCode().equals(tl.getCurrency())) {
                    if (iteA.getValue2() != null) {
                        cflg = Double.valueOf(iteA.getValue2());
                        curflg += Double.valueOf(iteA.getValue2()) * tratra;
                    }
                }
            }
        }
        //住宿费用明细
        List<AccommodationDetails> acclist = evevo.getAccommodationdetails();
        String tro = "";
        double rmbadd = 0;
        double aflg = 0;
        double accflg = 0;
        double rmbacc = 0;
        double traacc = 0;
        if (acclist.size() > 0) {
            tro = "住宿津贴";
            for (AccommodationDetails ac : acclist) {
                if (ac.getSubsidies() != null) {
                    rmbadd += Double.valueOf(ac.getSubsidies());
                }
                if (ac.getRmb() != null) {
                    rmbacc += Double.valueOf(ac.getRmb());
                }
                if (ac.getTravel() != null) {
                    traacc += Double.valueOf(ac.getTravel());
                }
                List<Dictionary> curListAc = dictionaryService.getForSelect("PG019");
                for (Dictionary iteA : curListAc) {
                    if (iteA.getCode().equals(ac.getCurrency())) {
                        if (iteA.getValue2() != null) {
                            aflg = Double.valueOf(iteA.getValue2());
                            accflg += Double.valueOf(iteA.getValue2()) * traacc;
                        }
                    }
                }

            }
        }

        String tdd = "出差津贴";
        //add-ws-5/14-其他费用明细
        List<OtherDetails> otherlist = evevo.getOtherdetails();
        String trd = "";
        double oflg = 0;
        double occflg = 0;
        double ombacc = 0;
        double oraacc = 0;
        if (otherlist.size() > 0) {
            trd = "其他费";
            for (OtherDetails other : otherlist) {
                if (other.getRmb() != null) {
                    ombacc += Double.valueOf(other.getRmb());
                }
                if (other.getForeigncurrency() != null) {
                    oraacc += Double.valueOf(other.getForeigncurrency());
                }
                List<Dictionary> curListAc = dictionaryService.getForSelect("PG019");
                for (Dictionary iteA : curListAc) {
                    if (iteA.getCode().equals(other.getCurrency())) {
                        if (iteA.getValue2() != null) {
                            oflg = Double.valueOf(iteA.getValue2());
                            occflg += Double.valueOf(iteA.getValue2()) * oraacc;
                        }
                    }
                }
            }
        }
        DecimalFormat df = new DecimalFormat("###,###.00");
        //add-ws-5/14-其他费用明细
//        String sum1 = "";
//        String sum2 = "";
//        String sum3 = "";
//        if(curflg!=0.0){
//            sum1 = df.format(new BigDecimal(String.valueOf(curflg+rmbtra)));
//            if (sum1.equals(".00")) {
//                sum1 = "0.00";
//            }
//        }else{
//            sum1 = df.format(new BigDecimal(String.valueOf(rmbtra)));
//            if (sum1.equals(".00")) {
//                sum1 = "0.00";
//            }
//        }
//        if(accflg!=0.0){
//            sum2 = df.format(new BigDecimal(String.valueOf(accflg+rmbacc)));
//            if (sum2.equals(".00")) {
//                sum2 = "0.00";
//            }
//        }else{
//            sum2 = df.format(new BigDecimal(String.valueOf(rmbacc)));
//            if (sum2.equals(".00")) {
//                sum2 = "0.00";
//            }
//        }
//        if(occflg!=0.0){
//            sum3 = df.format(new BigDecimal(String.valueOf(occflg+ombacc)));
//            if (sum3.equals(".00")) {
//                sum3 = "0.00";
//            }
//        }else{
//            sum3 = df.format(new BigDecimal(String.valueOf(ombacc)));
//            if (sum3.equals(".00")) {
//                sum3 = "0.00";
//            }
//        }
        //出差地点
//        if(!evevo.getEvection().getPlace().equals("") && !evevo.getEvection().getPlace().equals(null)){
        List<Dictionary> curList1 = dictionaryService.getForSelect("PJ036");
        for (Dictionary item : curList1) {
            if (item.getCode().equals(evevo.getEvection().getPlace())) {
                evevo.getEvection().setPlace(item.getValue1());
            }
        }
        //如果出差地域是‘其他’，赋地域名称的值 add_fjl
        if (!StringUtils.isNullOrEmpty(evevo.getEvection().getRegionname())) {
            evevo.getEvection().setPlace(evevo.getEvection().getRegionname());
        }
//        }
        Query query = new Query();
        String userim = "";
        //add-ws-6/29-禅道175问提修改
        String useridcheck = evevo.getEvection().getUserid();
        //add-ws-6/29-禅道175问提修改
        query.addCriteria(Criteria.where("userid").is(evevo.getEvection().getUserid()));
        CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
        if (customerInfo != null) {
            //add-ws-6/29-禅道175问提修改
            evevo.getEvection().setUserid(customerInfo.getUserinfo().getCustomername());
            //add-ws-6/29-禅道175问提修改
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
        // add-ws-7/10-禅道249
        Query query7 = new Query();
        String  roles = "";
        String check =  "";
        query7.addCriteria(Criteria.where("_id").is(useridcheck));
        UserAccount userAccount = mongoTemplate.findOne(query7, UserAccount.class);
        if (userAccount != null) {
            if(userAccount.getRoles().size() > 0){
                for(int i = 0;i<userAccount.getRoles().size();i++){
                    roles = roles +  userAccount.getRoles().get(i).getDescription();
                }
                if(roles.indexOf("总经理")!= -1) {
                    check  = "总经理";
                }
            }
        }
        // add-ws-7/10-禅道249
        //upd-ws-6/17-禅道101
        //upd-ws-6/29-禅道175问提修改
        if(check.equals("总经理")){
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

            }
            //upd-ws-6/29-禅道175问提修改
        } else if (useridcheck.equals("5e78b2264e3b194874180f35") || useridcheck.equals("5e78b2574e3b194874181099")) {
            //upd-ws-6/29-禅道175问提修改
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

            }
        } else {
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
                //upd-ws-禅道102
                if (wfList.get(2).getRemark() != null) {
                    if (wfList.get(2).getRemark().equals("系统自动跳过")) {
                        wfList3 = "";
                    } else {
                        query = new Query();
                        query.addCriteria(Criteria.where("userid").is(wfList.get(2).getUserId()));
                        customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                        if (customerInfo != null) {
                            wfList3 = customerInfo.getUserinfo().getCustomername();
                            wfList3 = sign.startGraphics2D(wfList3);
                        }
                    }
                }else{
                    query = new Query();
                    query.addCriteria(Criteria.where("userid").is(wfList.get(2).getUserId()));
                    customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                    if (customerInfo != null) {
                        wfList3 = customerInfo.getUserinfo().getCustomername();
                        wfList3 = sign.startGraphics2D(wfList3);
                    }
                }
                if (wfList.get(3).getRemark() != null) {
                    if (wfList.get(3).getRemark().equals("系统自动跳过")) {
                        wfList4 = "";
                    } else {
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
                    query.addCriteria(Criteria.where("userid").is(wfList.get(3).getUserId()));
                    customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                    if (customerInfo != null) {
                        wfList4 = customerInfo.getUserinfo().getCustomername();
                        wfList4 = sign.startGraphics2D(wfList4);
                    }
                }
                //upd-ws-禅道102
            }
        }
        //upd-ws-6/17-禅道101
        Map<String, Object> data = new HashMap<>();
        String str_format = "";
        if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(evevo.getEvection().getLoanamount())) {
            BigDecimal bd = new BigDecimal(evevo.getEvection().getLoanamount());
            str_format = df.format(bd);
            if (str_format.equals(".00")) {
                str_format = "0.00";
            }
            evevo.getEvection().setLoanamount(str_format);
        } else {
            evevo.getEvection().setLoanamount("0.00");
        }
        if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(evevo.getEvection().getTotalpay())) {
            BigDecimal bd = new BigDecimal(evevo.getEvection().getTotalpay());
            str_format = df.format(bd);
            if (str_format.equals(".00")) {
                str_format = "0.00";
            }
            evevo.getEvection().setTotalpay(str_format);
        } else {
            evevo.getEvection().setTotalpay("0.00");
        }
        if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(evevo.getEvection().getBalance())) {
            BigDecimal bd = new BigDecimal(evevo.getEvection().getBalance());
            str_format = df.format(bd);
            if (str_format.equals(".00")) {
                str_format = "0.00";
            }
            evevo.getEvection().setBalance(str_format);
        } else {
            evevo.getEvection().setBalance("0.00");
        }

        for (int h = 0; h < evevo.getCurrencyexchanges().size(); h++) {
            if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(evevo.getCurrencyexchanges().get(h).getAmount())) {
                BigDecimal bd = new BigDecimal(evevo.getCurrencyexchanges().get(h).getAmount());
                str_format = df.format(bd);
                if (str_format.equals(".00")) {
                    str_format = "0.00";
                }
                evevo.getCurrencyexchanges().get(h).setAmount(str_format);
            } else {
                evevo.getCurrencyexchanges().get(h).setAmount("0.00");
            }
        }

        for (int k = 0; k < evevo.getTrafficdetails().size(); k++) {
            if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(evevo.getTrafficdetails().get(k).getForeigncurrency())) {
                BigDecimal bd = new BigDecimal(evevo.getTrafficdetails().get(k).getForeigncurrency());
                str_format = df.format(bd);
                if (str_format.equals(".00")) {
                    str_format = "0.00";
                }
                evevo.getTrafficdetails().get(k).setForeigncurrency(str_format);
            }
            if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(evevo.getTrafficdetails().get(k).getRmb())) {
                BigDecimal bd = new BigDecimal(evevo.getTrafficdetails().get(k).getRmb());
                str_format = df.format(bd);
                if (str_format.equals(".00")) {
                    str_format = "0.00";
                }
                evevo.getTrafficdetails().get(k).setRmb(str_format);
            } else {
                evevo.getTrafficdetails().get(k).setRmb("0.00");
            }
        }

        for (int m = 0; m < evevo.getAccommodationdetails().size(); m++) {
            if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(evevo.getAccommodationdetails().get(m).getTravel())) {
                BigDecimal bd = new BigDecimal(evevo.getAccommodationdetails().get(m).getTravel());
                str_format = df.format(bd);
                if (str_format.equals(".00")) {
                    str_format = "0.00";
                }
                evevo.getAccommodationdetails().get(m).setTravel(str_format);
            } else {
                evevo.getAccommodationdetails().get(m).setTravel("0.00");
            }
            if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(evevo.getAccommodationdetails().get(m).getRmb())) {
                BigDecimal bd = new BigDecimal(evevo.getAccommodationdetails().get(m).getRmb());
                str_format = df.format(bd);
                if (str_format.equals(".00")) {
                    str_format = "0.00";
                }
                evevo.getAccommodationdetails().get(m).setRmb(str_format);
            } else {
                evevo.getAccommodationdetails().get(m).setRmb("0.00");
            }
            if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(evevo.getAccommodationdetails().get(m).getSubsidies())) {
                BigDecimal bd = new BigDecimal(evevo.getAccommodationdetails().get(m).getSubsidies());
                str_format = df.format(bd);
                if (str_format.equals(".00")) {
                    str_format = "0.00";
                }
                evevo.getAccommodationdetails().get(m).setSubsidies(str_format);
            } else {
                evevo.getAccommodationdetails().get(m).setSubsidies("0.00");
            }
        }

        for (int n = 0; n < evevo.getOtherdetails().size(); n++) {
            if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(evevo.getOtherdetails().get(n).getForeigncurrency())) {
                BigDecimal bd = new BigDecimal(evevo.getOtherdetails().get(n).getForeigncurrency());
                str_format = df.format(bd);
                if (str_format.equals(".00")) {
                    str_format = "0.00";
                }
                evevo.getOtherdetails().get(n).setForeigncurrency(str_format);
            } else {
                evevo.getOtherdetails().get(n).setForeigncurrency("0.00");
            }
            if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(evevo.getOtherdetails().get(n).getRmb())) {
                BigDecimal bd = new BigDecimal(evevo.getOtherdetails().get(n).getRmb());
                str_format = df.format(bd);
                if (str_format.equals(".00")) {
                    str_format = "0.00";
                }
                evevo.getOtherdetails().get(n).setRmb(str_format);
            } else {
                evevo.getOtherdetails().get(n).setRmb("0.00");
            }
        }

        String rmbflg = df.format(new BigDecimal(String.valueOf(rmbacc + rmbtra + ombacc + rmbadd)));
        if (rmbflg.equals(".00")) {
            rmbflg = "0.00";
        }
        String traflg = df.format(new BigDecimal(String.valueOf(tratra + traacc + oraacc)));
        if (traflg.equals(".00")) {
            traflg = "0.00";
        }
        String sumrmb = df.format(new BigDecimal(String.valueOf(occflg + accflg + curflg + rmbacc + rmbtra + ombacc + rmbadd)));
        if (sumrmb.equals(".00")) {
            sumrmb = "0.00";
        }

        trafficlist = trafficlist.stream().filter(item -> (!item.getRmb().equals("0.00"))).collect(Collectors.toList());
        List<AccommodationDetails> accommlist = accommodationlist.stream().filter(item -> (!item.getRmb().equals("0.00"))).collect(Collectors.toList());
        otherDetailslist = otherDetailslist.stream().filter(item -> (!item.getRmb().equals("0.00"))).collect(Collectors.toList());
        String ccc = "外币兑换";
        List<Dictionary> dictionaryL = dictionaryService.getForSelect("PJ119");
        String checkvalue = dictionaryL.get(4).getValue1();
        data.put("wfList1", wfList1);
        data.put("wfList2", wfList2);
        data.put("wfList3", wfList3);
        data.put("wfList4", wfList4);
        data.put("checkvalue", checkvalue);
        data.put("rmbadd", rmbadd);
        data.put("rmbadd", rmbadd);
        data.put("trd", trd);
//        data.put("sum1", sum1);
//        data.put("sum2", sum2);
//        data.put("sum3", sum3);
        data.put("trr", trr);
        data.put("tro", tro);
        data.put("tdd", tdd);
        data.put("oflg", oflg);
        data.put("cflg", cflg);
        data.put("aflg", aflg);
        data.put("userim", userim);
        data.put("rmbflg", rmbflg);
        data.put("traflg", traflg);
        data.put("sumrmb", sumrmb);
        data.put("eve", evevo.getEvection());
        data.put("busn", business.getBusiness().getBusiness_number());//出差申请编号
        data.put("cur", evevo.getCurrencyexchanges());
        data.put("ccc", ccc);
        data.put("tra", trafficlist);
        data.put("adl", accommodationlist);
        data.put("acc", accommlist);
        data.put("other", otherDetailslist);

        //upd-ws-6/17-禅道101
        //upd-ws-6/29-禅道175问提修改
        if (useridcheck.equals("5e78b2264e3b194874180f35") || useridcheck.equals("5e78b2574e3b194874181099")|| (check.equals("总经理"))) {
            //upd-ws-6/29-禅道175问提修改
            if (evevo.getEvection().getType().equals("0")) {
                ExcelOutPutUtil.OutPutPdf("境内出差旅费精算书", "newjingneijingsuanshu.xls", data, response);
            } else {
                ExcelOutPutUtil.OutPutPdf("境外出差旅费精算书", "newjingwaijingsuanshu.xls", data, response);
            }
        }else{

            if (evevo.getEvection().getType().equals("0")) {
                ExcelOutPutUtil.OutPutPdf("境内出差旅费精算书", "jingneijingsuanshu.xls", data, response);
            } else {
                ExcelOutPutUtil.OutPutPdf("境外出差旅费精算书", "jingwaijingsuanshu.xls", data, response);
            }
        }
        //upd-ws-6/17-禅道101
        //add-ws-6/16-禅道101
        ExcelOutPutUtil.deleteDir("E:\\PFANS\\image");
//        if (wfList3 != "") {
//            FileUtil.del("E:\\PFANS\\image" + "/" + wfList3);
//        }
//        if (wfList4 != "") {
//            FileUtil.del("E:\\PFANS\\image" + "/" + wfList4);
//        }
//        //add-ws-6/16-禅道101
//        FileUtil.del("E:\\PFANS\\image" + "/" + wfList1);
//        FileUtil.del("E:\\PFANS\\image" + "/" + wfList2);
//
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

    @RequestMapping(value = "/getBusiness", method = {RequestMethod.GET})
    public ApiResult getBusiness(HttpServletRequest request) throws Exception {
        return ApiResult.success(businessService.getBuse());
    }

    @RequestMapping(value = "/gettravelcostvo", method = {RequestMethod.POST})
    public ApiResult gettravelcostvo(@RequestBody TravelCostVo travelcostvo, HttpServletRequest request) throws Exception {
        if (travelcostvo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(evectionService.gettravelcost(travelcostvo));
    }

    @RequestMapping(value = "/getLoanApplication", method = {RequestMethod.GET})
    public ApiResult getLoanApplication(HttpServletRequest request) throws Exception {
        return ApiResult.success(loanapplicationService.getLoapp());
    }
}
