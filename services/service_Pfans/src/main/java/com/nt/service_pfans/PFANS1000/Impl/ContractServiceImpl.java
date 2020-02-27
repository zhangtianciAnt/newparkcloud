package com.nt.service_pfans.PFANS1000.Impl;
import com.nt.dao_Pfans.PFANS1000.Contract;
import com.nt.service_pfans.PFANS1000.ContractService;
import com.nt.service_pfans.PFANS1000.mapper.ContractMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional(rollbackFor = Exception.class)
public class ContractServiceImpl implements ContractService {

    @Autowired
    private ContractMapper contractMapper;
    @Override
    public List<Contract> get(Contract contract) throws Exception {
        return contractMapper.select(contract);
    }

    @Override
    public Contract One(String contract_id) throws Exception {
        return contractMapper.selectByPrimaryKey(contract_id);
    }

    @Override
    public void update(Contract contract, TokenModel tokenModel) throws Exception {
        contract.preUpdate(tokenModel);
        contractMapper.updateByPrimaryKey(contract);
    }
}
