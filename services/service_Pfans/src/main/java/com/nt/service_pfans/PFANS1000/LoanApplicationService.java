package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.LoanApplication;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface LoanApplicationService {

    List<LoanApplication> getLoanApplication(LoanApplication loanapplication)throws Exception;

    public LoanApplication One(String loanapplication_id)throws  Exception;

    public void insert(LoanApplication loanapplication, TokenModel tokenModel)throws  Exception;

    public void updateLoanApplication(LoanApplication loanapplication, TokenModel tokenModel)throws  Exception;


}
