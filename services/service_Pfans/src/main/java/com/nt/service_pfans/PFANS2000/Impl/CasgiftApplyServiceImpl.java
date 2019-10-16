package com.nt.service_pfans.PFANS2000.Impl;

import com.nt.dao_Pfans.PFANS2000.Casgiftapply;
import com.nt.service_pfans.PFANS2000.CasgiftapplyService;
import com.nt.service_pfans.PFANS2000.mapper.CasgiftapplyMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor=Exception.class)
public class CasgiftapplyServiceImpl implements CasgiftapplyService {

    @Autowired
    private CasgiftapplyMapper casgiftapplyMapper;

    @Override
    public List<Casgiftapply> getCasgiftapply() {
         if(casgiftapplyMapper.getCasgiftapply().isEmpty()){
             return null;
         }
        return casgiftapplyMapper.getCasgiftapply();
    }

    @Override
    public void insertCasgiftapply(Casgiftapply casgiftapply, TokenModel tokenModel) throws Exception {
        if(!casgiftapply.equals(null)){
            casgiftapply.preInsert(tokenModel);
            casgiftapply.setCasgiftapply_id(UUID.randomUUID().toString());
            casgiftapplyMapper.insertSelective(casgiftapply);
        }
    }
}
