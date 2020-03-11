package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Pfans.PFANS1000.LoanApplication;
import com.nt.service_pfans.PFANS1000.LoanApplicationService;
import com.nt.service_pfans.PFANS1000.mapper.LoanApplicationMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class LoanApplicationServiceImpl implements LoanApplicationService {

    @Autowired
    private LoanApplicationMapper loanapplicationMapper;

    @Override
    public List<LoanApplication> getLoanApplication(LoanApplication loanapplication) {
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
        loanapplication.preInsert(tokenModel);
        loanapplication.setLoanapplication_id(UUID.randomUUID().toString());
        loanapplicationMapper.insert(loanapplication);
    }

}
