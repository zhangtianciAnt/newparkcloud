package com.nt.service_pfans.PFANS2000.Impl;

import com.nt.service_pfans.PFANS2000.GivingService;
import com.nt.service_pfans.PFANS2000.mapper.GivingMapper;
import com.nt.dao_Pfans.PFANS2000.Giving;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class GivingServiceImpl implements GivingService {

    @Autowired
    private GivingMapper givingMapper;

    @Override
    public void insert(Giving giving, TokenModel tokenModel) throws Exception {
        giving.preInsert(tokenModel);
        giving.setGiving_id(UUID.randomUUID().toString());
        givingMapper.insert(giving);
    }


    @Override
    public List<Giving> getDataList(Giving giving) throws Exception {

        return givingMapper.select(giving);
    }
}
