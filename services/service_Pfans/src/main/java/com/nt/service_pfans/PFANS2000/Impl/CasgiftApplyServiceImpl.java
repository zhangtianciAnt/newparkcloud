package com.nt.service_pfans.PFANS2000.Impl;

import com.nt.dao_Pfans.PFANS2000.CasgiftApply;
import com.nt.service_pfans.PFANS2000.CasgiftApplyService;
import com.nt.service_pfans.PFANS2000.mapper.CasgiftApplyMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor=Exception.class)
public class CasgiftApplyServiceImpl implements CasgiftApplyService {

    @Autowired
    private CasgiftApplyMapper casgiftapplyMapper;

    @Override
    public List<CasgiftApply> getCasgiftapply(CasgiftApply Casgiftapply ) {
        return casgiftapplyMapper.select(Casgiftapply);
    }

    @Override
    public void insertCasgiftapply(CasgiftApply casgiftapply, TokenModel tokenModel) throws Exception {
        if(!casgiftapply.equals(null)){
            casgiftapply.preInsert(tokenModel);
            //casgiftapply.setCasgiftapply_id(UUID.randomUUID().toString());
            casgiftapplyMapper.insertSelective(casgiftapply);
        }
    }
}
