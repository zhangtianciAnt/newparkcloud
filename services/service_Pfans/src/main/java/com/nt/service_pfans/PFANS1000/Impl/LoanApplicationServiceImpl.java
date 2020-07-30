package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Pfans.PFANS1000.LoanApplication;
import com.nt.dao_Pfans.PFANS1000.PublicExpense;
import com.nt.dao_Pfans.PFANS3000.Purchase;
import com.nt.service_pfans.PFANS1000.LoanApplicationService;
import com.nt.service_pfans.PFANS1000.mapper.LoanApplicationMapper;
import com.nt.service_pfans.PFANS1000.mapper.PublicExpenseMapper;
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
    private PurchaseMapper purchaseMapper;

    @Override
    public List<LoanApplication> getLoanApplication(LoanApplication loanapplication) {
        int canafvermoney = 0;
        List<LoanApplication> loanApplicationList = loanapplicationMapper.select(loanapplication);
        for (LoanApplication loanApplication : loanApplicationList) {
            PublicExpense publicExpense = new PublicExpense();
            publicExpense.setJudgement(loanApplication.getJudgements());
            List<PublicExpense> publicExpenseList = publicExpenseMapper.select(publicExpense);
            if (loanApplication.getCanafver() != null && loanApplication.getCanafver() != "") {
                if (publicExpenseList.size() != 0) {
                    if (publicExpenseList.get(0).getStatus().equals("4")) {
                        loanApplication.setCanafver("1");
                    } else {
                        loanApplication.setCanafver("0");
                    }
                    for(int i = 0; i < publicExpenseList.size(); i ++){
                        canafvermoney += Integer.parseInt(publicExpenseList.get(i).getMoneys());
                    }
                    loanApplication.setCanafvermoney(canafvermoney);
                    loanapplicationMapper.updateByPrimaryKeySelective(loanApplication);
                }
            }
        }
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

        //CCM ADD 0726
        if(loanapplication.getJudgements_name()!=null && !loanapplication.getJudgements_name().equals(""))
        {
            if(loanapplication.getJudgements_name().substring(0,2).equals("CG"))
            {
                String []pur = loanapplication.getJudgements_name().split(",");
                for(String p:pur)
                {
                    Purchase purchase = new Purchase();
                    purchase.setPurnumbers(p);
                    List<Purchase> purchaseList = purchaseMapper.select(purchase);
                    if(purchaseList.size()>0)
                    {
                        purchaseList.get(0).setLoanapplication_id(loanapplication.getLoanapplication_id());
                        purchaseList.get(0).setLoanapno(loanapplication.getLoanapno());
                        purchaseList.get(0).preUpdate(tokenModel);
                        purchaseMapper.updateByPrimaryKey(purchaseList.get(0));
                    }
                }
            }
        }
    }

}
