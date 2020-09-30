package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.LoanApplication;
import com.nt.dao_Pfans.PFANS1000.PublicExpense;
import com.nt.utils.dao.TokenModel;

import java.util.List;
import java.util.Map;

public interface LoanApplicationService {

    List<LoanApplication> getLoanApplication(LoanApplication loanapplication)throws Exception;

    List<LoanApplication> getLoapp()throws Exception;

    public LoanApplication One(String loanapplication_id)throws  Exception;

    public List<PublicExpense> getpublice(String loanapplication_id) throws Exception;

    public void insert(LoanApplication loanapplication, TokenModel tokenModel)throws  Exception;

    public void updateLoanApplication(LoanApplication loanapplication, TokenModel tokenModel)throws  Exception;

    //add ccm 0728  精算时关联多个暂借款
    List<LoanApplication> getLoanApplicationList(String loanappno)throws Exception;
    //add ccm 0728  精算时关联多个暂借款

    Map<String, String> getworkfolwPurchaseData(LoanApplication loanapplication) throws Exception;
}
