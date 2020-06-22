package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Pfans.PFANS1000.LoanApplication;
import com.nt.dao_Pfans.PFANS1000.PublicExpense;
import com.nt.service_pfans.PFANS1000.LoanApplicationService;
import com.nt.service_pfans.PFANS1000.mapper.LoanApplicationMapper;
import com.nt.service_pfans.PFANS1000.mapper.PublicExpenseMapper;
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

    @Override
    public List<LoanApplication> getLoanApplication(LoanApplication loanapplication) {
        List<LoanApplication> loanApplicationList = loanapplicationMapper.select(loanapplication);
        for(LoanApplication loanApplication : loanApplicationList){
            PublicExpense publicExpense = new PublicExpense();
            publicExpense.setJudgement(loanApplication.getJudgements());
            List<PublicExpense> publicExpenseList = publicExpenseMapper.select(publicExpense);
//            if(loanApplication.getCanafver() != null && loanApplication.getCanafver() != ""){
            if(publicExpenseList.size() != 0){
                if(publicExpenseList.get(0).getStatus().equals("4")){
                    loanApplication.setCanafver("1");
                }else{
                    loanApplication.setCanafver("0");
                }
                loanapplicationMapper.updateByPrimaryKeySelective(loanApplication);
            }
//            }
        }
        return loanapplicationMapper.select(loanapplication);
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
    }

}
