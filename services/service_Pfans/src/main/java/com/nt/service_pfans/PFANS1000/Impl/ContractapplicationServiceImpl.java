package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Pfans.PFANS1000.Contractapplication;
import com.nt.service_pfans.PFANS1000.ContractapplicationService;
import com.nt.service_pfans.PFANS1000.mapper.ContractapplicationMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor=Exception.class)
public class ContractapplicationServiceImpl implements ContractapplicationService {

    @Autowired
    private ContractapplicationMapper contractapplicationMapper;

    @Override
    public List<Contractapplication> get(Contractapplication contractapplication ) {
        return contractapplicationMapper.select(contractapplication);
    }

    @Override
    public void update(Contractapplication contractapplication, TokenModel tokenModel) throws Exception {
        contractapplicationMapper.updateByPrimaryKeySelective(contractapplication);
    }

    @Override
    public void insert(Contractapplication contractapplication, TokenModel tokenModel) throws Exception {

        contractapplication.preInsert(tokenModel);
        contractapplication.setContractapplication_id(UUID.randomUUID().toString());
        contractapplicationMapper.insert(contractapplication);
    }
}
