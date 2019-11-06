package com.nt.service_pfans.PFANS3000.Impl;

import com.nt.dao_Pfans.PFANS3000.JapanCondominium;
import com.nt.service_pfans.PFANS3000.JapanCondominiumService;
import com.nt.service_pfans.PFANS3000.mapper.JapanCondominiumMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;


@Service
@Transactional(rollbackFor=Exception.class)
public class JapanCondominiumServiceImpl implements JapanCondominiumService {

    @Autowired
    private JapanCondominiumMapper japancondominiumMapper;

    @Override
    public List<JapanCondominium> getJapanCondominium(JapanCondominium japancondominium) throws Exception {
        return japancondominiumMapper.select(japancondominium);
    }

    @Override
    public List<JapanCondominium> getJapanCondominiumlist(JapanCondominium japancondominium) throws Exception {
        return japancondominiumMapper.select(japancondominium);
    }

    @Override
    public JapanCondominium One(String japancondominiumid) throws Exception {

        return japancondominiumMapper.selectByPrimaryKey(japancondominiumid);
    }

    @Override
    public void insertJapanCondominium(JapanCondominium japancondominium, TokenModel tokenModel) throws Exception {
        if(!StringUtils.isEmpty(japancondominium)){
            japancondominium.preInsert(tokenModel);
            japancondominium.setJapancondominiumid(UUID.randomUUID().toString());
            japancondominiumMapper.insert(japancondominium);
        }
    }


    @Override
    public void updateJapanCondominium(JapanCondominium japancondominium, TokenModel tokenModel) throws Exception {
        if(!StringUtils.isEmpty(japancondominium)){
            japancondominium.preUpdate(tokenModel);
            japancondominiumMapper.updateByPrimaryKey(japancondominium);
        }
    }
}
