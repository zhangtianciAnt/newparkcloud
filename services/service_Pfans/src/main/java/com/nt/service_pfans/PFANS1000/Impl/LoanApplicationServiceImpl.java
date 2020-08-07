package com.nt.service_pfans.PFANS1000.Impl;

import com.mysql.jdbc.StringUtils;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_Pfans.PFANS1000.*;
import com.nt.dao_Pfans.PFANS3000.Purchase;
import com.nt.service_Org.DictionaryService;
import com.nt.service_pfans.PFANS1000.LoanApplicationService;
import com.nt.service_pfans.PFANS1000.mapper.*;
import com.nt.service_pfans.PFANS3000.mapper.PurchaseMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class LoanApplicationServiceImpl implements LoanApplicationService {

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



    @Override
    public List<LoanApplication> getLoanApplication(LoanApplication loanapplication) {
        return loanapplicationMapper.select(loanapplication);
    }

    @Override
    public List<LoanApplication> getLoanApplicationList(String loanappno) {
        String[] loa = loanappno.split(",");
        List<LoanApplication> loaList =  new ArrayList<LoanApplication>();
        LoanApplication  lo = new LoanApplication();
        for(String l :loa)
        {
            lo = loanapplicationMapper.selectByPrimaryKey(l);
            if(lo!=null && !loaList.contains(lo))
            {
                loaList.add(lo);
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
    public void updateLoanApplication(LoanApplication loanapplication, TokenModel tokenModel) throws Exception {
        loanapplication.preUpdate(tokenModel);
        loanapplicationMapper.updateByPrimaryKey(loanapplication);
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
        //add_fjl_0807  获取委托合同番号的命名规则
        //CCM ADD 0726
        if(loanapplication.getJudgements_name()!=null && !loanapplication.getJudgements_name().equals(""))
        {
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
                            awardList.get(0).setLoanapplication_id(loanapplication.getLoanapplication_id());
                            awardList.get(0).setLoanapno(loanapplication.getLoanapno());
                            awardList.get(0).preUpdate(tokenModel);
                            awardMapper.updateByPrimaryKey(awardList.get(0));
                        }
                    }
                }
            }
            //add_fjl_0807
            if (loanapplication.getJudgements_name().substring(0, 2).equals("CG")) //采购
            {
                String []pur = loanapplication.getJudgements_name().split(",");
                for(String p:pur) {
                    Purchase purchase = new Purchase();
                    purchase.setPurnumbers(p);
                    List<Purchase> purchaseList = purchaseMapper.select(purchase);
                    if(purchaseList.size()>0) {
                        purchaseList.get(0).setLoanapplication_id(loanapplication.getLoanapplication_id());
                        purchaseList.get(0).setLoanapno(loanapplication.getLoanapno());
                        purchaseList.get(0).preUpdate(tokenModel);
                        purchaseMapper.updateByPrimaryKey(purchaseList.get(0));
                    }
                }
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
                        communicationList.get(0).setLoanapplication_id(loanapplication.getLoanapplication_id());
                        communicationList.get(0).setLoanapno(loanapplication.getLoanapno());
                        communicationList.get(0).preUpdate(tokenModel);
                        communicationMapper.updateByPrimaryKey(communicationList.get(0));
                    }
                }
            } else if (loanapplication.getJudgements_name().substring(0, 2).equals("JC"))//其他业务
            {
                String[] pur = loanapplication.getJudgements_name().split(",");
                for (String p : pur) {
                    Judgement judgement = new Judgement();
                    judgement.setJudgnumbers(p);
                    List<Judgement> judgementList = judgementMapper.select(judgement);
                    if (judgementList.size() > 0) {
                        judgementList.get(0).setLoanapplication_id(loanapplication.getLoanapplication_id());
                        judgementList.get(0).setLoanapno(loanapplication.getLoanapno());
                        judgementList.get(0).preUpdate(tokenModel);
                        judgementMapper.updateByPrimaryKey(judgementList.get(0));
                    }
                }
            } else if (loanapplication.getJudgements_name().substring(0, 2).equals("WC"))//无偿设备
            {
                String[] pur = loanapplication.getJudgements_name().split(",");
                for (String p : pur) {
                    Judgement judgement = new Judgement();
                    judgement.setJudgnumbers(p);
                    List<Judgement> judgementList = judgementMapper.select(judgement);
                    if (judgementList.size() > 0) {
                        judgementList.get(0).setLoanapplication_id(loanapplication.getLoanapplication_id());
                        judgementList.get(0).setLoanapno(loanapplication.getLoanapno());
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
                        purchaseapplyList.get(0).setLoanapplication_id(loanapplication.getLoanapplication_id());
                        purchaseapplyList.get(0).setLoanapno(loanapplication.getLoanapno());
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
                        businessList.get(0).setLoanapplication_id(loanapplication.getLoanapplication_id());
                        businessList.get(0).setLoanapno(loanapplication.getLoanapno());
                        businessList.get(0).setLoanday(new Date());
                        businessList.get(0).setLoanmoney(loanapplication.getMoneys());
                        businessList.get(0).preUpdate(tokenModel);
                        businessMapper.updateByPrimaryKey(businessList.get(0));
                    }
                }
            }
        }
    }

}
