package com.nt.service_pfans.PFANS6000.Impl;

import com.nt.dao_Pfans.PFANS6000.Variousfunds;
import com.nt.service_pfans.PFANS6000.VariousfundsService;
import com.nt.service_pfans.PFANS6000.mapper.VariousfundsMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class VariousfundsServiceImpl implements VariousfundsService {

    @Autowired
    private VariousfundsMapper variousfundsMapper;
    @Override
    public List<Variousfunds> getvariousfunds(Variousfunds variousfunds) throws Exception {
        return variousfundsMapper.select(variousfunds);
    }

    @Override
    public Variousfunds getvariousfundsApplyOne(String variousfunds_id) throws Exception {
        return variousfundsMapper.selectByPrimaryKey(variousfunds_id);
    }

    @Override
    public void updatevariousfundsApply(Variousfunds variousfunds, TokenModel tokenModel) throws Exception {
        variousfundsMapper.updateByPrimaryKeySelective(variousfunds);
    }

    @Override
    public void createvariousfundsApply(Variousfunds variousfunds, TokenModel tokenModel) throws Exception {
        variousfunds.preInsert(tokenModel);
        variousfunds.setVariousfunds_id(UUID.randomUUID().toString());
        variousfundsMapper.insert(variousfunds);
    }
}
