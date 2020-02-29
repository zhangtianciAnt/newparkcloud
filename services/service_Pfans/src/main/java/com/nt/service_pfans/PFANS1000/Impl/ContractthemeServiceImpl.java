package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Pfans.PFANS1000.Contracttheme;
import com.nt.service_pfans.PFANS1000.ContractthemeService;
import com.nt.service_pfans.PFANS1000.mapper.ContractthemeMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class ContractthemeServiceImpl implements ContractthemeService {

    @Autowired
    private ContractthemeMapper contractthemeMapper;

    @Override
    public List<Contracttheme> get(Contracttheme contracttheme) {
        return contractthemeMapper.select(contracttheme);
    }

    @Override
    public Contracttheme One(String contracttheme_id) throws Exception {
        return contractthemeMapper.selectByPrimaryKey(contracttheme_id);
    }

    @Override
    public void update(List<Contracttheme> contracttheme, TokenModel tokenModel) throws Exception {
        int rowindex = 0;
        for(int i = 0; i < contracttheme.size(); i ++){
            rowindex = rowindex + 1;
            Contracttheme co = contracttheme.get(i);
            co.preUpdate(tokenModel);
            co.setRowindex(rowindex);
            contractthemeMapper.updateByPrimaryKey(co);
        }
    }

    @Override
    public void insert(List<Contracttheme> contracttheme, TokenModel tokenModel) throws Exception {
        int rowindex = 0;
        for(int i = 0; i < contracttheme.size(); i ++){
            rowindex = rowindex + 1;
            Contracttheme co = contracttheme.get(i);
            co.preInsert(tokenModel);
            co.setContractthemeid(UUID.randomUUID().toString());
            co.setRowindex(rowindex);
            contractthemeMapper.insert(co);
        }
    }
}
