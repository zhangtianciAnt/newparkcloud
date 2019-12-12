package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Pfans.PFANS1000.Psdcd;
import com.nt.service_pfans.PFANS1000.PsdcdService;
import com.nt.service_pfans.PFANS1000.mapper.PsdcdMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class PsdcdServiceImpl  implements PsdcdService {

    @Autowired
    private PsdcdMapper psdcdMapper;

    @Override
    public List<Psdcd> getPsdcd(Psdcd psdcd) {
        return psdcdMapper.select(psdcd);
    }

    @Override
    public Psdcd One(String psdcd_id) throws Exception {
        return psdcdMapper.selectByPrimaryKey(psdcd_id);
    }

    @Override
    public void updatePsdcd(Psdcd psdcd, TokenModel tokenModel) throws Exception {
        psdcd.preUpdate(tokenModel);
        psdcdMapper.updateByPrimaryKey(psdcd);
    }

    @Override
    public void insert(Psdcd psdcd, TokenModel tokenModel) throws Exception {
        psdcd.preInsert(tokenModel);
        psdcd.setPsdcd_id(UUID.randomUUID().toString());
        psdcdMapper.insert(psdcd);
    }

}
