package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_Pfans.PFANS1000.*;
import com.nt.dao_Pfans.PFANS3000.Purchase;
import com.nt.dao_Pfans.PFANS8000.MonthlyRate;
import com.nt.service_Org.DictionaryService;
import com.nt.service_pfans.PFANS1000.LoanApplicationService;
import com.nt.service_pfans.PFANS1000.mapper.*;
import com.nt.service_pfans.PFANS3000.PurchaseService;
import com.nt.service_pfans.PFANS3000.mapper.PurchaseMapper;
import com.nt.service_pfans.PFANS8000.mapper.MonthlyRateMapper;
import com.nt.utils.StringUtils;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class LoanApplicationServiceImpl implements LoanApplicationService {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private LoanApplicationMapper loanapplicationMapper;
    @Autowired
    private PublicExpenseMapper publicExpenseMapper;
    @Autowired
    private PurchaseApplyMapper purchaseapplyMapper;
    @Autowired
    private CommunicationMapper communicationMapper;
    @Autowired
    private JudgementMapper judgementMapper;

    @Autowired
    private PurchaseMapper purchaseMapper;
    @Autowired
    private AwardMapper awardMapper;
    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private BusinessMapper businessMapper;
    @Autowired
    private PurchaseService purchaseService;
    @Autowired
    private MonthlyRateMapper monthlyratemapper;


    @Override
    public List<LoanApplication> getLoanApplication(LoanApplication loanapplication) {
        return loanapplicationMapper.select(loanapplication);
    }

    @Override
    public List<LoanApplication> getLoanApplicationList(String loanappno, TokenModel tokenModel) {
        String[] loa = loanappno.split(",");
        List<LoanApplication> loaList = new ArrayList<LoanApplication>();
        LoanApplication lo = new LoanApplication();
        for (String l : loa) {
            lo = loanapplicationMapper.selectByPrimaryKey(l);
            if (lo != null && !loaList.contains(lo)) {
                if (lo.getStatus().equals("4")) {
                    loaList.add(lo);
                }
            }
        }
        //add-ws-8/24-禅道任务544
        Query query = new Query();
        query.addCriteria(Criteria.where("userid").is(tokenModel.getUserId()));
        CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
        if (customerInfo != null) {
            LoanApplication loanapplication = new LoanApplication();
            if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(customerInfo.getUserinfo().getGroupid())) {
                loanapplication.setGroup_id(customerInfo.getUserinfo().getGroupid());
            } else if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(customerInfo.getUserinfo().getCenterid())) {
                loanapplication.setCenter_id(customerInfo.getUserinfo().getCenterid());
            }
            loanapplication.setPublicradio("1");
            loanapplication.setStatus("4");
            List<LoanApplication> LoanApplicationlist = loanapplicationMapper.select(loanapplication);
            for (LoanApplication loan : LoanApplicationlist) {
                if(!loaList.contains(loan))
                {
                    loaList.add(loan);
                }
            }
        }
        //add-ws-8/24-禅道任务544
        return loaList;
    }

    @Override
    public List<LoanApplication> getLoanApplicationList2(String loanappno, TokenModel tokenModel) {
        String[] loa = loanappno.split(",");
        List<LoanApplication> loaList = new ArrayList<LoanApplication>();
        LoanApplication lo = new LoanApplication();
        for (String l : loa) {
            lo = loanapplicationMapper.selectByPrimaryKey(l);
            if (lo != null && !loaList.contains(lo)) {
                if (lo.getStatus().equals("4")) {
                    loaList.add(lo);
                }
            }
        }
        return loaList;
    }


    @Override
    public List<LoanApplication> getLoapp() throws Exception {
        return loanapplicationMapper.getLoapp();
    }

    @Override
    public LoanApplication One(String loanapplication_id) throws Exception {
        return loanapplicationMapper.selectByPrimaryKey(loanapplication_id);
    }

    @Override
    public List<PublicExpense> getpublice(String loanapplication_id) throws Exception {
        List<PublicExpense> pb = publicExpenseMapper.getLoan(loanapplication_id);
        return pb;
    }

    @Override
    public void updateLoanApplication(LoanApplication loanapplication, TokenModel tokenModel) throws Exception {
        //upd-8/20-ws-禅道468任务
        LoanApplication loanapp = new LoanApplication();
        Date modeon = loanapplication.getModifyon();
        String status = loanapplication.getStatus();
        String processingstatus = loanapplication.getProcessingstatus();
        BeanUtils.copyProperties(loanapplication, loanapp);
        loanapp.preUpdate(tokenModel);
        if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(loanapplication.getProcessingstatus())) {
            if (status.equals("4") && processingstatus.equals("1")) {
                loanapp.setModifyon(modeon);
            }
        }
        //add_fjl_0929  添加作废时决裁数据回滚  start
        if (StringUtils.isNotEmpty(loanapplication.getProcessingstatus()) && loanapplication.getProcessingstatus().equals("2")) {
            String dic = "";
            List<Dictionary> curList1 = dictionaryService.getForSelect("HT014");
            for (Dictionary item : curList1) {
                dic += item.getValue2() + ",";
            }
            if (StringUtils.isNotEmpty(loanapplication.getJudgements_name())) {
                if (dic != "") {
                    if (dic.contains(loanapplication.getJudgements_name().substring(0, 2))) //委托决裁
                    {
                        String[] pur = loanapplication.getJudgements_name().split(",");
                        for (String p : pur) {
                            Award award = new Award();
                            award.setContractnumber(p);
                            List<Award> awardList = awardMapper.select(award);
                            if (awardList.size() > 0) {
                                if (StringUtils.isNotEmpty(awardList.get(0).getLoanapno()) && awardList.get(0).getLoanapno().contains(loanapplication.getLoanapno())) {
                                    awardList.get(0).setLoanapno(awardList.get(0).getLoanapno().replace(loanapplication.getLoanapno(), ""));
                                    awardList.get(0).setLoanapplication_id(awardList.get(0).getLoanapplication_id().replace(loanapplication.getLoanapplication_id(), ""));
                                }
                                awardList.get(0).preUpdate(tokenModel);
                                awardMapper.updateByPrimaryKey(awardList.get(0));
                            }
                        }
                    }
                }
                if (loanapplication.getJudgements_name().substring(0, 2).equals("CG")) //采购
                {
                    String[] pur = loanapplication.getJudgements_name().split(",");
                    for (String p : pur) {
                        Purchase purchase = new Purchase();
                        purchase.setPurnumbers(p);
                        List<Purchase> purchaseList = purchaseMapper.select(purchase);
                        if (purchaseList.size() > 0) {
                            if (StringUtils.isNotEmpty(purchaseList.get(0).getLoanapno()) && purchaseList.get(0).getLoanapno().contains(loanapplication.getLoanapno())) {
                                purchaseList.get(0).setLoanapno(purchaseList.get(0).getLoanapno().replace(loanapplication.getLoanapno(), ""));
                                purchaseList.get(0).setLoanapplication_id(purchaseList.get(0).getLoanapplication_id().replace(loanapplication.getLoanapplication_id(), ""));
                            }

                            purchaseList.get(0).preUpdate(tokenModel);
                            purchaseMapper.updateByPrimaryKey(purchaseList.get(0));
                        }
                    }
                } else if (loanapplication.getJudgements_name().substring(0, 3).equals("JJF"))//交际费
                {
                    String[] pur = loanapplication.getJudgements_name().split(",");
                    for (String p : pur) {
                        Communication communication = new Communication();
                        communication.setNumbercation(p);
                        List<Communication> communicationList = communicationMapper.select(communication);
                        if (communicationList.size() > 0) {
                            if (StringUtils.isNotEmpty(communicationList.get(0).getLoanapno()) && communicationList.get(0).getLoanapno().contains(loanapplication.getLoanapno())) {
                                communicationList.get(0).setLoanapno(communicationList.get(0).getLoanapno().replace(loanapplication.getLoanapno(), ""));
                                communicationList.get(0).setLoanapplication_id(communicationList.get(0).getLoanapplication_id().replace(loanapplication.getLoanapplication_id(), ""));
                            }

                            communicationList.get(0).preUpdate(tokenModel);
                            communicationMapper.updateByPrimaryKey(communicationList.get(0));
                        }
                    }
                } else if (loanapplication.getJudgements_name().substring(0, 2).equals("JC") || loanapplication.getJudgements_name().substring(0, 2).equals("WC"))//其他业务//无偿设备
                {
                    String[] pur = loanapplication.getJudgements_name().split(",");
                    for (String p : pur) {
                        Judgement judgement = new Judgement();
                        judgement.setJudgnumbers(p);
                        List<Judgement> judgementList = judgementMapper.select(judgement);
                        if (judgementList.size() > 0) {
                            if (StringUtils.isNotEmpty(judgementList.get(0).getLoanapno()) && judgementList.get(0).getLoanapno().contains(loanapplication.getLoanapno())) {
                                judgementList.get(0).setLoanapno(judgementList.get(0).getLoanapno().replace(loanapplication.getLoanapno(), ""));
                                judgementList.get(0).setLoanapplication_id(judgementList.get(0).getLoanapplication_id().replace(loanapplication.getLoanapplication_id(), ""));
                            }
                            judgementList.get(0).preUpdate(tokenModel);
                            judgementMapper.updateByPrimaryKey(judgementList.get(0));
                        }
                    }
                } else if (loanapplication.getJudgements_name().substring(0, 2).equals("QY"))//千元费用
                {
                    String[] pur = loanapplication.getJudgements_name().split(",");
                    for (String p : pur) {
                        PurchaseApply purchaseapply = new PurchaseApply();
                        purchaseapply.setPurchasenumbers(p);
                        List<PurchaseApply> purchaseapplyList = purchaseapplyMapper.select(purchaseapply);
                        if (purchaseapplyList.size() > 0) {
                            if (StringUtils.isNotEmpty(purchaseapplyList.get(0).getLoanapno()) && purchaseapplyList.get(0).getLoanapno().contains(loanapplication.getLoanapno())) {
                                purchaseapplyList.get(0).setLoanapno(purchaseapplyList.get(0).getLoanapno().replace(loanapplication.getLoanapno(), ""));
                                purchaseapplyList.get(0).setLoanapplication_id(purchaseapplyList.get(0).getLoanapplication_id().replace(loanapplication.getLoanapplication_id(), ""));
                            }

                            purchaseapplyList.get(0).preUpdate(tokenModel);
                            purchaseapplyMapper.updateByPrimaryKey(purchaseapplyList.get(0));
                        }
                    }
                } else if (loanapplication.getJudgements_name().substring(0, 1).equals("C"))//境内外出差
                {
                    String[] pur = loanapplication.getJudgements_name().split(",");
                    for (String p : pur) {
                        Business business = new Business();
                        business.setBusiness_number(p);
                        List<Business> businessList = businessMapper.select(business);
                        if (businessList.size() > 0) {
                            if (StringUtils.isNotEmpty(businessList.get(0).getLoanapno()) && businessList.get(0).getLoanapno().contains(loanapplication.getLoanapno())) {
                                businessList.get(0).setLoanapno(businessList.get(0).getLoanapno().replace(loanapplication.getLoanapno(), ""));
                                businessList.get(0).setLoanapplication_id(businessList.get(0).getLoanapplication_id().replace(loanapplication.getLoanapplication_id(), ""));
                            }
                            businessList.get(0).preUpdate(tokenModel);
                            businessMapper.updateByPrimaryKey(businessList.get(0));
                        }
                    }
                }
            }
            loanapp.setJudgements("");
            loanapp.setJudgements_name("");
            loanapp.setJudgements_moneys("");
            loanapp.setJudgements_type("");
        }
        //add_fjl_0929  添加作废时决裁数据回滚  end
        loanapplicationMapper.updateByPrimaryKey(loanapp);
        //upd-8/20-ws-禅道468任务
    }

    public Float aFloat(Float a) {
        return a > 0 ? a : -a;
    }

    @Override
    public void insert(LoanApplication loanapplication, TokenModel tokenModel) throws Exception {
//        add_fjl_05/27  --添加申请编号
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String year = sdf.format(new Date());
        String no = "";
        if (loanapplicationMapper.getLoappCount(year) != null) {
            int count = loanapplicationMapper.getLoappCount(year);
            no = String.format("%2d", count + 1).replace(" ", "0");
        } else {
            no = "01";
        }

        String apdate = year.replace("-", "");
        String loanapNo = "Z" + apdate + no;
        loanapplication.setLoanapno(loanapNo);
//        add_fjl_05/27  --添加申请编号
        loanapplication.preInsert(tokenModel);
        loanapplication.setLoanapplication_id(UUID.randomUUID().toString());
        loanapplicationMapper.insert(loanapplication);
        //add_fjl_0807  获取委托合同番号的命名规则
        String dic = "";
        List<Dictionary> curList1 = dictionaryService.getForSelect("HT014");
        for (Dictionary item : curList1) {
            dic += item.getValue2() + ",";
        }
//        float money = Float.parseFloat(loanapplication.getMoneys());
        //add_fjl_0807  获取委托合同番号的命名规则
        //CCM ADD 0726
        if (loanapplication.getJudgements_name() != null && !loanapplication.getJudgements_name().equals("")) {
            //add_fjl_0807
            if (dic != "") {
                if (dic.contains(loanapplication.getJudgements_name().substring(0, 2))) //委托决裁
                {
                    String[] pur = loanapplication.getJudgements_name().split(",");
                    for (String p : pur) {
                        Award award = new Award();
                        award.setContractnumber(p);
                        List<Award> awardList = awardMapper.select(award);
                        if (awardList.size() > 0) {
//                            float diff = money - Float.parseFloat(awardList.get(0).getBalancejude());
//                            if (diff >= 0) {
//                                diff = 0;
//                                money = diff;
//                            } else {
//                                aFloat(diff);
//                            }
//                            awardList.get(0).setBalancejude(String.valueOf(diff));
                            //add ccm 0813 决裁到暂借款，精算  check去掉  决裁中的暂借款和精算存在多条的可能
                            if (StringUtils.isNotBlank(awardList.get(0).getLoanapplication_id())) {
                                awardList.get(0).setLoanapplication_id(awardList.get(0).getLoanapplication_id() + "," + loanapplication.getLoanapplication_id());
                                awardList.get(0).setLoanapno(awardList.get(0).getLoanapno() + "," + loanapplication.getLoanapno());
                            } else {
                                awardList.get(0).setLoanapplication_id(loanapplication.getLoanapplication_id());
                                awardList.get(0).setLoanapno(loanapplication.getLoanapno());
                            }
                            //add ccm 0813 决裁到暂借款，精算  check去掉  决裁中的暂借款和精算存在多条的可能
                            awardList.get(0).preUpdate(tokenModel);
                            awardMapper.updateByPrimaryKey(awardList.get(0));
                        }
                    }
                }
            }
            //add_fjl_0807
            if (loanapplication.getJudgements_name().substring(0, 2).equals("CG")) //采购
            {
                String[] pur = loanapplication.getJudgements_name().split(",");
                for (String p : pur) {
                    Purchase purchase = new Purchase();
                    purchase.setPurnumbers(p);
                    List<Purchase> purchaseList = purchaseMapper.select(purchase);
                    if (purchaseList.size() > 0) {
//                        float diff = money - Float.parseFloat(purchaseList.get(0).getBalancejude());
//                        if (diff >= 0) {
//                            diff = 0;
//                            money = diff;
//                        } else {
//                            aFloat(diff);
//                        }
//                        purchaseList.get(0).setBalancejude(String.valueOf(diff));
                        //add ccm 0813 决裁到暂借款，精算  check去掉  决裁中的暂借款和精算存在多条的可能
                        if (StringUtils.isNotBlank(purchaseList.get(0).getLoanapplication_id())) {
                            purchaseList.get(0).setLoanapplication_id(purchaseList.get(0).getLoanapplication_id() + "," + loanapplication.getLoanapplication_id());
                            purchaseList.get(0).setLoanapno(purchaseList.get(0).getLoanapno() + "," + loanapplication.getLoanapno());
                        } else {
                            purchaseList.get(0).setLoanapplication_id(loanapplication.getLoanapplication_id());
                            purchaseList.get(0).setLoanapno(loanapplication.getLoanapno());
                        }
                        //add ccm 0813 决裁到暂借款，精算  check去掉  决裁中的暂借款和精算存在多条的可能

                        purchaseList.get(0).preUpdate(tokenModel);
                        //add ccm 0813 决裁到暂借款，精算  check去掉  决裁中的暂借款和精算存在多条的可能
                        purchaseMapper.updateByPrimaryKey(purchaseList.get(0));
                    }
                }
//                    if (loanapplication.getPublicradio() != "1") {
//                        //禅道447 处理可用暂借款金额 ztc
//                        Map<String, Double> moneyMap = new HashMap<String, Double>();
//                        List<Purchase> surLoappmoneyList = new ArrayList<>();
//                        for (String l : pur) {
//                            Purchase purchase = new Purchase();
//                            purchase.setPurnumbers(l);
//                            surLoappmoneyList = purchaseMapper.select(purchase);
//                            moneyMap.put(surLoappmoneyList.get(0).getPurchase_id(), Double.valueOf(surLoappmoneyList.get(0).getSurloappmoney()));
//                        }
//                        if (moneyMap.size() > 1) {
//                            //暂借款申请金额
//                            Double applyMoney = Double.valueOf(loanapplication.getMoneys());
//                            List<Map.Entry<String, Double>> halePurListMap = sortMapAnt(moneyMap);
//                            for (Map.Entry<String, Double> lm : halePurListMap) {
//                                if (applyMoney > 0) {
//                                    applyMoney = subAnt(applyMoney, lm.getValue());
//                                    //被减后的申请金额如果小于等于0，说明已经完成抵消
//                                    if (applyMoney <= 0) {
//                                        Purchase purchase = purchaseMapper.selectByPrimaryKey(lm.getKey());
//                                        Double abapplyMoney = applyMoney * -1;
//                                        purchase.setSurloappmoney(abapplyMoney.toString());
//                                        purchaseMapper.updateByPrimaryKey(purchase);
//                                    } else {
//                                        Purchase purchase = purchaseMapper.selectByPrimaryKey(lm.getKey());
//                                        purchase.setSurloappmoney("0");
//                                        purchaseMapper.updateByPrimaryKey(purchase);
//                                    }
//                                }
//                            }
//                        } else {
//                            Double surplusM = Double.valueOf(surLoappmoneyList.get(0).getSurloappmoney());
//                            Double useM = Double.valueOf(loanapplication.getMoneys());
//                            //期望暂借款金额 传入
//                            Double surLoappmLast = subAnt(surplusM, useM);
//                            surLoappmoneyList.get(0).setSurloappmoney(surLoappmLast.toString());
//                            purchaseMapper.updateByPrimaryKey(surLoappmoneyList.get(0));
//                        }
//                    }
            }
            //ADD_FJL_0730  start
            else if (loanapplication.getJudgements_name().substring(0, 3).equals("JJF"))//交际费
            {
                String[] pur = loanapplication.getJudgements_name().split(",");
                for (String p : pur) {
                    Communication communication = new Communication();
                    communication.setNumbercation(p);
                    List<Communication> communicationList = communicationMapper.select(communication);
                    if (communicationList.size() > 0) {
//                        float diff = money - Float.parseFloat(communicationList.get(0).getBalancejude());
//                        if (diff >= 0) {
//                            diff = 0;
//                            money = diff;
//                        } else {
//                            aFloat(diff);
//                        }
//                        communicationList.get(0).setBalancejude(String.valueOf(diff));
                        //add ccm 0813 决裁到暂借款，精算  check去掉  决裁中的暂借款和精算存在多条的可能
                        if (StringUtils.isNotBlank(communicationList.get(0).getLoanapplication_id())) {
                            communicationList.get(0).setLoanapplication_id(communicationList.get(0).getLoanapplication_id() + "," + loanapplication.getLoanapplication_id());
                            communicationList.get(0).setLoanapno(communicationList.get(0).getLoanapno() + "," + loanapplication.getLoanapno());
                        } else {
                            communicationList.get(0).setLoanapplication_id(loanapplication.getLoanapplication_id());
                            communicationList.get(0).setLoanapno(loanapplication.getLoanapno());
                        }
                        //add ccm 0813 决裁到暂借款，精算  check去掉  决裁中的暂借款和精算存在多条的可能

                        communicationList.get(0).preUpdate(tokenModel);
                        communicationMapper.updateByPrimaryKey(communicationList.get(0));
                    }
                }
            }
            else if (loanapplication.getJudgements_name().substring(0, 2).equals("JC"))//其他业务
            {
                String[] pur = loanapplication.getJudgements_name().split(",");
                for (String p : pur) {
                    Judgement judgement = new Judgement();
                    judgement.setJudgnumbers(p);
                    List<Judgement> judgementList = judgementMapper.select(judgement);
                    if (judgementList.size() > 0) {
//                        float diff = money - Float.parseFloat(judgementList.get(0).getBalancejude());
//                        if (diff >= 0) {
//                            diff = 0;
//                            money = diff;
//                        } else {
//                            aFloat(diff);
//                        }
//                        judgementList.get(0).setBalancejude(String.valueOf(diff));
                        //add ccm 0813 决裁到暂借款，精算  check去掉  决裁中的暂借款和精算存在多条的可能
                        if (StringUtils.isNotBlank(judgementList.get(0).getLoanapplication_id())) {
                            judgementList.get(0).setLoanapplication_id(judgementList.get(0).getLoanapplication_id() + "," + loanapplication.getLoanapplication_id());
                            judgementList.get(0).setLoanapno(judgementList.get(0).getLoanapno() + "," + loanapplication.getLoanapno());
                        } else {
                            judgementList.get(0).setLoanapplication_id(loanapplication.getLoanapplication_id());
                            judgementList.get(0).setLoanapno(loanapplication.getLoanapno());
                        }
                        //add ccm 0813 决裁到暂借款，精算  check去掉  决裁中的暂借款和精算存在多条的可能

                        judgementList.get(0).preUpdate(tokenModel);
                        judgementMapper.updateByPrimaryKey(judgementList.get(0));
                    }
                }
            }
            else if (loanapplication.getJudgements_name().substring(0, 2).equals("WC"))//无偿设备
            {
                String[] pur = loanapplication.getJudgements_name().split(",");
                for (String p : pur) {
                    Judgement judgement = new Judgement();
                    judgement.setJudgnumbers(p);
                    List<Judgement> judgementList = judgementMapper.select(judgement);
                    if (judgementList.size() > 0) {
//                        float diff = money - Float.parseFloat(judgementList.get(0).getBalancejude());
//                        if (diff >= 0) {
//                            diff = 0;
//                            money = diff;
//                        } else {
//                            aFloat(diff);
//                        }
//                        judgementList.get(0).setBalancejude(String.valueOf(diff));
                        //add ccm 0813 决裁到暂借款，精算  check去掉  决裁中的暂借款和精算存在多条的可能
                        if (StringUtils.isNotBlank(judgementList.get(0).getLoanapplication_id())) {
                            judgementList.get(0).setLoanapplication_id(judgementList.get(0).getLoanapplication_id() + "," + loanapplication.getLoanapplication_id());
                            judgementList.get(0).setLoanapno(judgementList.get(0).getLoanapno() + "," + loanapplication.getLoanapno());
                        } else {
                            judgementList.get(0).setLoanapplication_id(loanapplication.getLoanapplication_id());
                            judgementList.get(0).setLoanapno(loanapplication.getLoanapno());
                        }
                        //add ccm 0813 决裁到暂借款，精算  check去掉  决裁中的暂借款和精算存在多条的可能

                        judgementList.get(0).preUpdate(tokenModel);
                        judgementMapper.updateByPrimaryKey(judgementList.get(0));
                    }
                }
            }
            else if (loanapplication.getJudgements_name().substring(0, 2).equals("QY"))//千元费用
            {
                String[] pur = loanapplication.getJudgements_name().split(",");
                for (String p : pur) {
                    PurchaseApply purchaseapply = new PurchaseApply();
                    purchaseapply.setPurchasenumbers(p);
                    List<PurchaseApply> purchaseapplyList = purchaseapplyMapper.select(purchaseapply);
                    if (purchaseapplyList.size() > 0) {
//                        float diff = money - Float.parseFloat(purchaseapplyList.get(0).getBalancejude());
//                        if (diff >= 0) {
//                            diff = 0;
//                            money = diff;
//                        } else {
//                            aFloat(diff);
//                        }
//                        purchaseapplyList.get(0).setBalancejude(String.valueOf(diff));
                        //add ccm 0813 决裁到暂借款，精算  check去掉  决裁中的暂借款和精算存在多条的可能
                        if (StringUtils.isNotBlank(purchaseapplyList.get(0).getLoanapplication_id())) {
                            purchaseapplyList.get(0).setLoanapplication_id(purchaseapplyList.get(0).getLoanapplication_id() + "," + loanapplication.getLoanapplication_id());
                            purchaseapplyList.get(0).setLoanapno(purchaseapplyList.get(0).getLoanapno() + "," + loanapplication.getLoanapno());
                        } else {
                            purchaseapplyList.get(0).setLoanapplication_id(loanapplication.getLoanapplication_id());
                            purchaseapplyList.get(0).setLoanapno(loanapplication.getLoanapno());
                        }
                        //add ccm 0813 决裁到暂借款，精算  check去掉  决裁中的暂借款和精算存在多条的可能

                        purchaseapplyList.get(0).preUpdate(tokenModel);
                        purchaseapplyMapper.updateByPrimaryKey(purchaseapplyList.get(0));
                    }
                }
                //ADD_FJL_0730  end
            }
            else if (loanapplication.getJudgements_name().substring(0, 1).equals("C"))//境内外出差
            {
                String[] pur = loanapplication.getJudgements_name().split(",");
                for (String p : pur) {
                    Business business = new Business();
                    business.setBusiness_number(p);
                    List<Business> businessList = businessMapper.select(business);
                    if (businessList.size() > 0) {
//                        float diff = money - Float.parseFloat(businessList.get(0).getBalancejude());
//                        if (diff >= 0) {
//                            diff = 0;
//                            money = diff;
//                        } else {
//                            aFloat(diff);
//                        }
//                        businessList.get(0).setBalancejude(String.valueOf(diff));
                        //add ccm 0813 决裁到暂借款，精算  check去掉  决裁中的暂借款和精算存在多条的可能
                        //add ccm 0813 决裁到暂借款，精算  check去掉  决裁中的暂借款和精算存在多条的可能
                        businessList.get(0).setLoanapplication_id(loanapplication.getLoanapplication_id());
                        businessList.get(0).setLoanapno(loanapplication.getLoanapno());
                        businessList.get(0).setLoanday(new Date());
                        //add  借款金额（元）  from
                        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM");
                        MonthlyRate month = new MonthlyRate();
                        month.setMonth(sf.format(businessList.get(0).getLoanday()));
                        SimpleDateFormat sf1 = new SimpleDateFormat("yyyy");
                        month.setYear(sf1.format(businessList.get(0).getLoanday()));
                        month.setCurrency(loanapplication.getCurrencychoice());
                        List<MonthlyRate> monthList = monthlyratemapper.select(month);
                        if(monthList.size() > 0){
                            if(monthList.size() > 0){
                                businessList.get(0).setLoanmoney(
                                        String.valueOf(com.mysql.jdbc.StringUtils.isNullOrEmpty(monthList.get(0).getExchangerate())
                                                ? BigDecimal.ZERO
                                                : new BigDecimal(monthList.get(0).getExchangerate())
                                                .multiply(com.mysql.jdbc.StringUtils.isNullOrEmpty(loanapplication.getMoneys())
                                                        ? BigDecimal.ZERO
                                                        : new BigDecimal(loanapplication.getMoneys()))
                                                .setScale(2,BigDecimal.ROUND_HALF_UP)));
                            }
                        }

                        //add  借款金额（元）  to
                        businessList.get(0).preUpdate(tokenModel);
                        businessMapper.updateByPrimaryKey(businessList.get(0));
                    }
                }
            }
        }
    }

    /**
     * 描述：将决裁剩余可用金额（暂借款，公共费用）升序排列
     * 并根据使用金额消耗选择单的可用金额
     * <p>
     * ztc
     */
    public static List<Map.Entry<String, Double>> sortMapAnt(Map<String, Double> waitListMap) {
        //升序排列
        List<Map.Entry<String, Double>> paresList = new ArrayList<Map.Entry<String, Double>>(waitListMap.entrySet());
        Comparator<Map.Entry<String,Double>>comparator = new Comparator<Map.Entry<String,Double>>() {
            @Override
            public int compare(Map.Entry<String,Double> o1, Map.Entry<String,Double> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        };
        Collections.sort(paresList,comparator);
        return paresList;

    }

    /**
     * 精确减法 ztc
     */
    public static double subAnt(double val1, double val2) {
        BigDecimal surMouney1 = BigDecimal.valueOf(val1);
        BigDecimal surMouney2 = BigDecimal.valueOf(val2);
        return surMouney1.subtract(surMouney2).doubleValue();
    }

    public Map<String, String> getworkfolwPurchaseData(LoanApplication loanapplication) throws Exception {
        Map<String, String> getpurchaseMap = new HashMap<String, String>();
        Map<String, String> getpurchaseMapsub = new HashMap<String, String>();
        loanapplication = loanapplicationMapper.selectByPrimaryKey(loanapplication.getLoanapplication_id());
        if (loanapplication.getJudgements_name() != null && !loanapplication.getJudgements_name().equals("")) {
            String[] pusname = loanapplication.getJudgements_name().split(",");
            String[] pusid = loanapplication.getJudgements().split(",");
            if (pusname.length > 0) {
                for (int i = 0; i < pusname.length; i++) {
                    if (pusname[i].substring(0, 2).equals("CG")) {
                        Purchase purchase = new Purchase();
                        purchase.setPurnumbers(pusname[i]);
                        List<Purchase> purchaseList = new ArrayList<Purchase>();
                        purchaseList = purchaseMapper.select(purchase);
                        if (purchaseList.size() > 0) {
                            //采购决裁
                            if (getpurchaseMap.containsKey("purchase")) {
                                String val = getpurchaseMap.get("purchase") + ";" + purchaseList.get(0).getPurchase_id() + "," + purchaseList.get(0).getStatus();
                                getpurchaseMap.put("purchase", val);
                            } else {
                                getpurchaseMap.put("purchase", purchaseList.get(0).getPurchase_id() + "," + purchaseList.get(0).getStatus());
                            }
                            //暂借款
                            if (getpurchaseMap.containsKey("loanApplication")) {
                                String val = getpurchaseMap.get("loanApplication") + ";" + loanapplication.getLoanapplication_id() + "," + loanapplication.getStatus();
                                getpurchaseMap.put("loanApplication", val);
                            } else {
                                getpurchaseMap.put("loanApplication", loanapplication.getLoanapplication_id() + "," + loanapplication.getStatus());
                            }
                        }
                    }
                }
            }
        }
        return getpurchaseMap;
    }

    //region   add  ml  220112  检索  from
    @Override
    public List<LoanApplication> getLoanapplicationSearch(LoanApplication loanapplication) throws Exception {
        return loanapplicationMapper.getLoanapplicationSearch(loanapplication);
    }
    //endregion   add  ml  220112  检索  to

}
