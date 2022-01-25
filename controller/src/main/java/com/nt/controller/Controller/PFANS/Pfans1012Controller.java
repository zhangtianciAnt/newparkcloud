package com.nt.controller.Controller.PFANS;

import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_Org.UserAccount;
import com.nt.dao_Pfans.PFANS1000.*;
import com.nt.dao_Pfans.PFANS1000.Vo.PublicExpenseVo;
import com.nt.dao_Pfans.PFANS1000.Vo.TotalCostVo;
import com.nt.dao_Workflow.Vo.StartWorkflowVo;
import com.nt.dao_Workflow.Vo.WorkflowLogDetailVo;
import com.nt.service_Org.DictionaryService;
import com.nt.service_Org.UserService;
import com.nt.service_WorkFlow.WorkflowServices;
import com.nt.service_WorkFlow.mapper.WorkflowinstanceMapper;
import com.nt.service_pfans.PFANS1000.JudgementService;
import com.nt.service_pfans.PFANS1000.LoanApplicationService;
import com.nt.service_pfans.PFANS1000.PublicExpenseService;
import com.nt.service_pfans.PFANS1000.mapper.LoanApplicationMapper;
import com.nt.service_pfans.PFANS1000.mapper.PublicExpenseMapper;
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
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/publicexpense")
public class Pfans1012Controller {
    @Autowired
    private PublicExpenseMapper publicExpenseMapper;
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

    @Autowired
    private WorkflowinstanceMapper workflowinstanceMapper;


    @RequestMapping(value = "/exportjs", method = {RequestMethod.GET})
    public void exportjs(String publicexpenseid, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        //add-ws-7/9-禅道任务248
        PublicExpenseVo pubvo = publicExpenseService.selectById(publicexpenseid);
        Map<String, Object> listsum = publicExpenseService.exportjs(publicexpenseid, request);
        List<TrafficDetails> trafficlist = (List<TrafficDetails>) listsum.getOrDefault("交通费", new ArrayList<>());
        List<PurchaseDetails> purchasedetailslist = (List<PurchaseDetails>) listsum.getOrDefault("采购费", new ArrayList<>());
        List<OtherDetails> otherDetailslist = (List<OtherDetails>) listsum.getOrDefault("其他费用", new ArrayList<>());
        //add-ws-7/9-禅道任务248
        String trr = "";
        //add_fjl_0907  BigDecimal类型进行计算
//        int sum1 = 0;
        BigDecimal sum1 = new BigDecimal("0");
        //add_fjl_0907  BigDecimal类型进行计算
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
                //add_fjl_0907  BigDecimal类型进行计算
                //add-ws-6/29-禅道171问题修正
                sum1 = sum1.add(new BigDecimal(pl.getForeigncurrency()));
                //add-ws-6/29-禅道171问题修正
                //add_fjl_0907  BigDecimal类型进行计算
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
        //add_fjl_0907  BigDecimal类型进行计算
//        int sum = 0;
        BigDecimal sum = new BigDecimal("0");
        //add_fjl_0907  BigDecimal类型进行计算
        if (othlist.size() > 0) {
            tro = "其他费用";
            for (OtherDetails ol : othlist) {
                //add_fjl_0907  BigDecimal类型进行计算
                //add-ws-6/29-禅道171问题修正
                sum = sum.add(new BigDecimal(ol.getForeigncurrency()));
                //add-ws-6/29-禅道171问题修正
                //add_fjl_0907  BigDecimal类型进行计算
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
        //add-ws-禅道103任务2
        String username = "";
        BigDecimal bd7 = new BigDecimal(pubvo.getPublicexpense().getMoneys());
        int scale = 2;//设置位数
        int roundingMode = 4;//表示四舍五入，可以选择其他舍值方式，例如去尾，等等.
        bd7 = bd7.setScale(scale, roundingMode);
        //upd-ws-6/30-禅道169修正
        if (!pubvo.getPublicexpense().getLoan().equals("")) {
            query = new Query();
            LoanApplication loanapplication = new LoanApplication();
            loanapplication.setLoanapplication_id(pubvo.getPublicexpense().getLoan());
            List<LoanApplication> list = loanapplicationMapper.select(loanapplication);
            if (list.size() > 0) {
                pubvo.getPublicexpense().setJudgement_name(list.get(0).getLoanapno());
                if (list.get(0).getPaymentmethod().equals("PJ015002")) {
                    if (list.get(0).getUser_name() != null) {
                        query.addCriteria(Criteria.where("userid").is(list.get(0).getUser_name()));
                        customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                        if (customerInfo != null) {
                            username = customerInfo.getUserinfo().getCustomername();
                        }
                    }
                } else {
                    username = list.get(0).getAccountpayeename();
                }
            }
        }
        //upd-ws-6/30-禅道169修正
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        String checkdata = sf.format(pubvo.getPublicexpense().getCreateon());
        //add-ws-禅道103任务2
        int rol = 0;
//        String rolcreatuser = "";
        StartWorkflowVo startWorkflowVo = new StartWorkflowVo();
        startWorkflowVo.setDataId(pubvo.getPublicexpense().getPublicexpenseid());
        List<WorkflowLogDetailVo> wfList = workflowServices.ViewWorkflow2(startWorkflowVo, tokenModel.getLocale());

        if (wfList.size() > 0) {
            //UPD_FJL_0908 修改打印印章显示与流程不符 start
            //UPD-WS-6/12-禅道105
//            if (Integer.valueOf(checkdata) >= 20200624) {
            query = new Query();
            query.addCriteria(Criteria.where("_id").is(wfList.get(0).getUserId()));
            UserAccount account = mongoTemplate.findOne(query, UserAccount.class);
            if (account != null) {
                if (account.getRoles().size() > 0) {
                    for (int a = 0; a < account.getRoles().size(); a++) {
                        if (account.getRoles().get(a).getRolename().equals("总经理")) {
                            rol++;
                            break;
                        }
                    }
                }
            }
            //add ccm 1126
//            query = new Query();
//            query.addCriteria(Criteria.where("_id").is(wfList.get(wfList.size()-1).getUserId()));
//            UserAccount ac = mongoTemplate.findOne(query, UserAccount.class);
//            if (ac != null) {
//                if (ac.getRoles().size() > 0) {
//                    for (int a = 0; a < ac.getRoles().size(); a++) {
//                        if(ac.getRoles().get(a).getRolename().equals("GM")) {
//                            rolcreatuser = rolcreatuser+"1";
//                        }
//                        if(ac.getRoles().get(a).getRolename().equals("TL")) {
//                            rolcreatuser = rolcreatuser+"2";
//                        }
//                    }
//                }
//            }
            //add ccm 1126
            if (bd7.intValue() >= 20000.00 ) {
                if(rol==0)
                {
                    wfList7 = userim;
                    wfList7 = sign.startGraphics2D(wfList7);
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
                }
                else
                {
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
                }


                    List<WorkflowLogDetailVo> erci = wfList.stream().filter(item -> (item.getNodename().equals("二次上司"))).collect(Collectors.toList());
                    if(erci.size()>0)
                    {
                        query = new Query();
                        query.addCriteria(Criteria.where("userid").is(erci.get(0).getUserId()));
                        customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                        if (customerInfo != null) {
                            wfList3 = customerInfo.getUserinfo().getCustomername();
                            wfList3 = sign.startGraphics2D(wfList3);
                        }
                    }

                    List<WorkflowLogDetailVo> yici = wfList.stream().filter(item -> (item.getNodename().equals("一次上司"))).collect(Collectors.toList());
                    List<WorkflowLogDetailVo> renshibuzhang = wfList.stream().filter(item -> (item.getNodename().equals("人事部长"))).collect(Collectors.toList());

                    if(yici.size()>0)
                    {
                        query = new Query();
                        query.addCriteria(Criteria.where("userid").is(yici.get(0).getUserId()));
                        customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                        if (customerInfo != null) {
                            wfList4 = customerInfo.getUserinfo().getCustomername();
                            wfList4 = sign.startGraphics2D(wfList4);
                        }
                    } else if(renshibuzhang.size()>0)
                    {
                        query = new Query();
                        query.addCriteria(Criteria.where("userid").is(renshibuzhang.get(0).getUserId()));
                        customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                        if (customerInfo != null) {
                            wfList4 = customerInfo.getUserinfo().getCustomername();
                            wfList4 = sign.startGraphics2D(wfList4);
                        }
                    }
            }
            else {
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
                List<WorkflowLogDetailVo> erci = wfList.stream().filter(item -> (item.getNodename().equals("二次上司"))).collect(Collectors.toList());
                if(erci.size()>0)
                {
                    query = new Query();
                    query.addCriteria(Criteria.where("userid").is(erci.get(0).getUserId()));
                    customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                    if (customerInfo != null) {
                        wfList3 = customerInfo.getUserinfo().getCustomername();
                        wfList3 = sign.startGraphics2D(wfList3);
                    }
                }

                List<WorkflowLogDetailVo> yici = wfList.stream().filter(item -> (item.getNodename().equals("一次上司"))).collect(Collectors.toList());
                List<WorkflowLogDetailVo> renshibuzhang = wfList.stream().filter(item -> (item.getNodename().equals("人事部长"))).collect(Collectors.toList());

                if(yici.size()>0)
                {
                    query = new Query();
                    query.addCriteria(Criteria.where("userid").is(yici.get(0).getUserId()));
                    customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                    if (customerInfo != null) {
                        wfList4 = customerInfo.getUserinfo().getCustomername();
                        wfList4 = sign.startGraphics2D(wfList4);
                    }
                } else if(renshibuzhang.size()>0)
                {
                    query = new Query();
                    query.addCriteria(Criteria.where("userid").is(renshibuzhang.get(0).getUserId()));
                    customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                    if (customerInfo != null) {
                        wfList4 = customerInfo.getUserinfo().getCustomername();
                        wfList4 = sign.startGraphics2D(wfList4);
                    }
                }
            }
//            } else {
//                query = new Query();
//
//                query = new Query();
//                query.addCriteria(Criteria.where("userid").is(wfList.get(0).getUserId()));
//                customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
//                if (customerInfo != null) {
//                    wfList1 = customerInfo.getUserinfo().getCustomername();
//                    wfList1 = sign.startGraphics2D(wfList1);
//                }
//                query = new Query();
//                query.addCriteria(Criteria.where("userid").is(wfList.get(1).getUserId()));
//                customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
//                if (customerInfo != null) {
//                    wfList2 = customerInfo.getUserinfo().getCustomername();
//                    wfList2 = sign.startGraphics2D(wfList2);
//                }
//                if (wfList.get(2).getRemark() != null) {
//                    if (wfList.get(2).getRemark().equals("系统自动跳过")) {
//                        wfList3 = "";
//                    } else {
//                        query = new Query();
//                        query.addCriteria(Criteria.where("userid").is(wfList.get(2).getUserId()));
//                        customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
//                        if (customerInfo != null) {
//                            wfList3 = customerInfo.getUserinfo().getCustomername();
//                            wfList3 = sign.startGraphics2D(wfList3);
//                        }
//                    }
//                } else {
//                    query = new Query();
//                    query.addCriteria(Criteria.where("userid").is(wfList.get(2).getUserId()));
//                    customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
//                    if (customerInfo != null) {
//                        wfList3 = customerInfo.getUserinfo().getCustomername();
//                        wfList3 = sign.startGraphics2D(wfList3);
//                    }
//                }
//                if (wfList.get(3).getRemark() != null) {
//                    if (wfList.get(3).getRemark().equals("系统自动跳过")) {
//                        wfList4 = "";
//                    } else {
//                        query = new Query();
//                        query.addCriteria(Criteria.where("userid").is(wfList.get(3).getUserId()));
//                        customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
//                        if (customerInfo != null) {
//                            wfList4 = customerInfo.getUserinfo().getCustomername();
//                            wfList4 = sign.startGraphics2D(wfList4);
//                        }
//                    }
//                } else {
//                    query = new Query();
//                    query.addCriteria(Criteria.where("userid").is(wfList.get(3).getUserId()));
//                    customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
//                    if (customerInfo != null) {
//                        wfList4 = customerInfo.getUserinfo().getCustomername();
//                        wfList4 = sign.startGraphics2D(wfList4);
//                    }
//                }
//            }
            //UPD_FJL_0908 修改打印印章显示与流程不符 end
        }
        //UPD-WS-6/12-禅道105
        Map<String, Object> data = new HashMap<>();
        if ("PJ004004".equals(pubvo.getPublicexpense().getPaymentmethod())) {
            String payeeName = "";
            String loanNo = "";
//            LoanApplication loanApplication = new LoanApplication();
//            loanApplication = loanapplicationMapper.selectByPrimaryKey(pubvo.getPublicexpense().getLoan());
//            payeeName = loanApplication.getPayeename();
//            data.put("payeeName", payeeName);
            String loanid[] = pubvo.getPublicexpense().getLoan().split(",");
            for(String id:loanid)
            {
                LoanApplication loanApplication = new LoanApplication();
                loanApplication = loanapplicationMapper.selectByPrimaryKey(id);
                if(loanApplication!=null)
                {
                    payeeName = loanApplication.getPayeename();
                    loanNo = loanApplication.getLoanapno();
                }
                if(payeeName==null || payeeName .equals(""))
                {
                    query = new Query();
                    query.addCriteria(Criteria.where("userid").is(loanApplication.getUser_name()));
                    customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                    if(customerInfo!=null)
                    {
                        payeeName = customerInfo.getUserinfo().getCustomername();
                    }
                }
                if(!data.containsKey("payeeName"))
                {
                    data.put("payeeName", payeeName);
                }
                else
                {
                    if(!data.containsValue(payeeName))
                    {
                        data.put("payeeName", data.get("payeeName") + ","+payeeName);
                    }
                }

                if(!data.containsKey("loanNo"))
                {
                    data.put("loanNo", loanNo);
                }
                else
                {
                    if(!data.containsValue(loanNo))
                    {
                        data.put("loanNo", data.get("loanNo") + ","+loanNo);
                    }
                }
            }
        }
        String str_format = "";
        //PSDCD_PFANS_20210521_BUG_009 解决0.12bug scc fr
//        DecimalFormat df = new DecimalFormat("###,###.00");
        DecimalFormat df = new DecimalFormat("###,##0.00");
        //PSDCD_PFANS_20210521_BUG_009 解决0.12bug scc to

//add-ws-6/29-禅道171问题修正
        String currenct = "";
        String currenctsum = "";
        //add-ws-9/27-禅道任务556
        int checkcurrenct = 0;
        //add-ws-9/27-禅道任务556
        List<Dictionary> curListsum = dictionaryService.getForSelect("PG019");
        for (Dictionary ite : curListsum) {
            if (pubvo.getOtherdetails().size() > 0) {
                if (!pubvo.getOtherdetails().get(0).getForeigncurrency().equals("0")) {
                    if (ite.getCode().equals(pubvo.getOtherdetails().get(0).getCurrency())) {
                        checkcurrenct = checkcurrenct + 1;
                        currenct = ite.getValue3();
                        //add_fjl_0907  BigDecimal类型进行计算
                        BigDecimal mountsum = sum.add(sum1);
                        //add_fjl_0907  BigDecimal类型进行计算
                        currenctsum = String.valueOf(mountsum);
                    }
                } else if (!pubvo.getPurchasedetails().get(0).getForeigncurrency().equals("0")) {
                    if (ite.getCode().equals(pubvo.getPurchasedetails().get(0).getCurrency())) {
                        checkcurrenct = checkcurrenct + 1;
                        currenct = ite.getValue3();
                        //add_fjl_0907  BigDecimal类型进行计算
                        BigDecimal mountsum = sum.add(sum1);
                        //add_fjl_0907  BigDecimal类型进行计算
                        currenctsum = String.valueOf(mountsum);
                    }
                }
            }
        }
        //upd-ws-9/27-禅道任务556
        if (checkcurrenct != 0) {
            pubvo.getPublicexpense().setMoneys("");
        } else {
            if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(pubvo.getPublicexpense().getMoneys())) {
                BigDecimal bd = new BigDecimal(pubvo.getPublicexpense().getMoneys());
                str_format = df.format(bd);
                if (str_format.equals(".00")) {
                    str_format = "0.00";
                }
                pubvo.getPublicexpense().setMoneys(str_format);
            } else {
                pubvo.getPublicexpense().setMoneys("0.00");
            }
        }
        //upd-ws-9/27-禅道任务556
//add-ws-6/29-禅道171问题修正
        for (int k = 0; k < pubvo.getTrafficdetails().size(); k++) {
            if (pubvo.getTrafficdetails().get(k).getRmb() != null) {
                BigDecimal bd = new BigDecimal(pubvo.getTrafficdetails().get(k).getRmb());
                str_format = df.format(bd);
                pubvo.getTrafficdetails().get(k).setRmb(str_format);
            }
        }

        //add-ws-6/29-禅道任务173
//        List<PurchaseDetails> PurchasedetailsList = pubvo.getPurchasedetails();

        trafficlist = trafficlist.stream().filter(item -> (!item.getRmb().equals("0.00"))).collect(Collectors.toList());
        purchasedetailslist = purchasedetailslist.stream().filter(item -> (!item.getRmb().equals("0.00")) || (!item.getForeigncurrency().equals("0.00"))).collect(Collectors.toList());
        otherDetailslist = otherDetailslist.stream().filter(item -> (!item.getRmb().equals("0.00")) || (!item.getForeigncurrency().equals("0.00"))).collect(Collectors.toList());


//        PurchasedetailsList = PurchasedetailsList.stream().filter(item -> (!item.getRmb().equals("0")) || (!item.getForeigncurrency().equals("0"))).collect(Collectors.toList());
//        List<OtherDetails> OtherDetailsList = pubvo.getOtherdetails();
//        OtherDetailsList = OtherDetailsList.stream().filter(item -> (!item.getRmb().equals("0")) || (!item.getForeigncurrency().equals("0"))).collect(Collectors.toList());
        //add-ws-6/29-禅道任务173
        data.put("username", username);
        data.put("currenct", currenct);
        data.put("currenctsum", currenctsum);
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
        data.put("tra", trafficlist);
        data.put("pur", purchasedetailslist);
        data.put("otd", otherDetailslist);

        if (bd7.intValue() >= 20000 && rol > 0) {
            if (pubvo.getTrafficdetails().size() > 0) {
                //add_fjl_0826  添加如果是多行明细的时候，明细放到第二页  start
                if (trafficlist.size() >= 6) {
                    ExcelOutPutUtil.OutPutPdf("公共費用精算書", "newgonggongfeiyongjingsuanshu11.xls", data, response);
                } else {
                    ExcelOutPutUtil.OutPutPdf("公共費用精算書", "newgonggongfeiyongjingsuanshu.xls", data, response);
                }
            } else {
                if ((purchasedetailslist.size() + otherDetailslist.size()) >= 5) {
                    ExcelOutPutUtil.OutPutPdf("公共費用精算書", "newgonggongfeiyongjingsuanshu_other11.xls", data, response);
                } else {
                    ExcelOutPutUtil.OutPutPdf("公共費用精算書", "newgonggongfeiyongjingsuanshu_other.xls", data, response);
                }
            }
        } else {
            if (pubvo.getTrafficdetails().size() > 0) {
                if (trafficlist.size() >= 6) {
                    ExcelOutPutUtil.OutPutPdf("公共費用精算書", "gonggongfeiyongjingsuanshu11.xls", data, response);
                } else {
                    ExcelOutPutUtil.OutPutPdf("公共費用精算書", "gonggongfeiyongjingsuanshu.xls", data, response);
                }
            } else {
                if ((purchasedetailslist.size() + otherDetailslist.size()) >= 5) {
                    ExcelOutPutUtil.OutPutPdf("公共費用精算書", "gonggongfeiyongjingsuanshu_other11.xls", data, response);
                } else {
                    ExcelOutPutUtil.OutPutPdf("公共費用精算書", "gonggongfeiyongjingsuanshu_other.xls", data, response);
                }
            }
            //add_fjl_0826  添加如果是多行明细的时候，明细放到第二页  end
        }
        ExcelOutPutUtil.deleteDir("E:\\PFANS\\image");
//        FileUtil.del("E:\\PFANS\\image" + "/" + wfList1);
//        FileUtil.del("E:\\PFANS\\image" + "/" + wfList2);
        //add-ws-6/16-禅道101
//        if (wfList3 != "") {
//            FileUtil.del("E:\\PFANS\\image" + "/" + wfList3);
//        }
//        if (wfList4 != "") {
//            FileUtil.del("E:\\PFANS\\image" + "/" + wfList4);
//        }
//        //add-ws-6/16-禅道101
//        FileUtil.del("E:\\PFANS\\image" + "/" + wfList7);
//        FileUtil.del("E:\\PFANS\\image" + "/" + userim);
    }


//    PSDCD_PFANS_20210519_BUG_006 临时接口
    @RequestMapping(value = "/puloanInfo", method = {RequestMethod.GET})
    public void puloanInfo(HttpServletRequest request) throws Exception {
        List<PublicExpense> peList = publicExpenseMapper.getEmpty();
        for(PublicExpense pl : peList){
            PublicExpense expense = new PublicExpense();
            expense.setPublicexpenseid(pl.getPublicexpenseid());
            PublicExpense expensesList = publicExpenseMapper.selectByPrimaryKey(expense);
//            for(int i = 0; i < expensesList.size(); i++){
                if(!expensesList.getLoan().equals("")){
                    String[] loanID = expensesList.getLoan().split(",");
                    LoanApplication loanApplication = new LoanApplication();
//                    loanApplication.setLoanapplication_id(expensesList.get(i).getLoan());
                    loanApplication.setLoanapplication_id(loanID[0]);
                    loanApplication = loanapplicationMapper.selectByPrimaryKey(loanApplication);
                    String codeOrName = !com.mysql.jdbc.StringUtils.isNullOrEmpty(loanApplication.getPayeecode()) ? loanApplication.getPayeecode() : loanApplication.getName();
                    if("PJ015001".equals(loanApplication.getPaymentmethod()) || "PJ015003".equals(loanApplication.getPaymentmethod())){
                        expensesList.setLoantype("1"+ "," + codeOrName);
                    }else if("PJ015002".equals(loanApplication.getPaymentmethod())){
                        expensesList.setLoantype("0"+ "," + codeOrName);
                    }else{
                        expensesList.setLoantype("");
                    }
                    publicExpenseMapper.updateByPrimaryKey(expensesList);
                }
            }
        }
//      PSDCD_PFANS_20210519_BUG_006 临时接口


    //ws-8/14-决裁子任务
    @RequestMapping(value = "/one2", method = {RequestMethod.POST})
    public ApiResult one2(@RequestBody PublicExpense publicExpense, HttpServletRequest request) throws Exception {
        if (publicExpense == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        List<PublicExpense> publicexpenselist = new ArrayList<>();
        String aa[] = publicExpense.getPublicexpenseid().split(",");
        if (aa.length > 0) {
            for (int i = 0; i < aa.length; i++) {
                PublicExpense publicexpense = new PublicExpense();
                publicexpense.setPublicexpenseid(aa[i]);
                List<PublicExpense> publics = publicExpenseMapper.select(publicexpense);
                publicexpenselist.addAll(0, publics);
            }
        }
        return ApiResult.success(publicexpenselist);
    }
    //ws-8/14-决裁子任务

    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    public ApiResult get(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        PublicExpense publicExpense = new PublicExpense();
        publicExpense.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(publicExpenseService.get(publicExpense));
    }

    // add-ws-8/20-禅道469
    @RequestMapping(value = "/loanapplication", method = {RequestMethod.GET})
    public ApiResult loanapplication(HttpServletRequest request) throws Exception {
        List<LoanApplication> loanapplicationlist = new ArrayList<>();
        LoanApplication loanapplication = new LoanApplication();
        loanapplication.setPublicradio("1");
        List<LoanApplication> LoanApplicationlist = loanapplicationMapper.select(loanapplication);
//        for(LoanApplication loan : LoanApplicationlist){
//            int scale = 2;//设置位数
//            int roundingMode = 4;//表示四舍五入，可以选择其他舍值方式，例如去尾，等等.
//            BigDecimal bd = new BigDecimal(loan.getBalance());
//            bd = bd.setScale(scale, roundingMode);
//            BigDecimal bd1 = new BigDecimal(loan.getMoneys());
//            bd1 = bd1.setScale(scale, roundingMode);
//            if(bd.compareTo(bd1) == -1){
//                loanapplicationlist.add(loan);
//            }
//        }

        return ApiResult.success(LoanApplicationlist);
    }

    // add-ws-8/20-禅道469
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
    public ApiResult insert(@RequestBody PublicExpenseVo publicExpenseVo, HttpServletRequest request) throws
            Exception {
        if (publicExpenseVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        publicExpenseService.insert(publicExpenseVo, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody PublicExpenseVo publicExpenseVo, HttpServletRequest request) throws
            Exception {
        if (publicExpenseVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        publicExpenseService.update(publicExpenseVo, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/gettotalcost", method = {RequestMethod.POST})
    public ApiResult gettotalcost(@RequestBody TotalCostVo totalcostvo, HttpServletRequest request) throws
            Exception {
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
    public ApiResult getLoanApplication(@RequestBody LoanApplication loanapplication, HttpServletRequest request) throws
            Exception {
        if (loanapplication == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
//        TokenModel tokenModel=tokenService.getToken(request);
        return ApiResult.success(loanapplicationService.getLoanApplication(loanapplication));
    }

    //add ccm 0728  精算时关联多个暂借款
    @RequestMapping(value = "/getLoanApplicationList", method = {RequestMethod.GET})
    public ApiResult getLoanApplicationList(String loanapno, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(loanapplicationService.getLoanApplicationList(loanapno,tokenModel));
    }
    //add ccm 0728  精算时关联多个暂借款

    @RequestMapping(value = "/getLoanApplicationList2", method = {RequestMethod.GET})
    public ApiResult getLoanApplicationList2(String loanapno, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(loanapplicationService.getLoanApplicationList2(loanapno,tokenModel));
    }

    //采购业务数据流程查看详情
    @RequestMapping(value = "/getworkfolwPurchaseData", method = {RequestMethod.POST})
    public ApiResult getworkfolwPurchaseData(@RequestBody PublicExpense publicExpense, HttpServletRequest request) throws Exception {
        if (publicExpense == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(publicExpenseService.getworkfolwPurchaseData(publicExpense));
    }
    //采购业务数据流程查看详情

    //region   add  ml  220112  检索  from
    @RequestMapping(value = "/getSearch", method = {RequestMethod.POST})
    public ApiResult getSearch(HttpServletRequest request,@RequestBody PublicExpense publicExpense) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        publicExpense.setOwners(tokenModel.getOwnerList());
        List<PublicExpense> resultList = publicExpenseService.getSearch(publicExpense)
                .stream().sorted(Comparator.comparing(PublicExpense::getCreateon).reversed()).collect(Collectors.toList());
        return ApiResult.success(resultList);
    }
    //endregion   add  ml  220112  检索  to

}
