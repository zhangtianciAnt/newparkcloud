package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Pfans.PFANS1000.Napalm;
import com.nt.service_pfans.PFANS1000.NapalmService;
import com.nt.service_pfans.PFANS1000.mapper.NapalmMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class NapalmServiceImpl implements NapalmService {

    @Autowired
    private NapalmMapper napalmMapper;

    @Override
    public List<Napalm> get(Napalm napalm) throws Exception{
        return napalmMapper.select(napalm);
    }

    @Override
    public Napalm One(String napalm_id) throws Exception {
        return napalmMapper.selectByPrimaryKey(napalm_id);
    }

    @Override
    public void update(Napalm napalm, TokenModel tokenModel) throws Exception {
        napalm.preUpdate(tokenModel);
        napalmMapper.updateByPrimaryKey(napalm);
    }


}
