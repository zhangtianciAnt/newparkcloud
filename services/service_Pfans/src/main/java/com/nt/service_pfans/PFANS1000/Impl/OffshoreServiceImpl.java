package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Pfans.PFANS1000.Offshore;
import com.nt.service_pfans.PFANS1000.OffshoreService;
import com.nt.service_pfans.PFANS1000.mapper.OffshoreMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class OffshoreServiceImpl implements OffshoreService {

    @Autowired
    private OffshoreMapper offshoreMapper;

    @Override
    public List<Offshore> getOffshore(Offshore offshore) {
        return offshoreMapper.select(offshore);
    }

    @Override
    public Offshore One(String offshore_id) throws Exception {
        return offshoreMapper.selectByPrimaryKey(offshore_id);
    }

    @Override
    public void updateOffshore(Offshore offshore, TokenModel tokenModel) throws Exception {
        offshore.preUpdate(tokenModel);
        offshoreMapper.updateByPrimaryKey(offshore);
    }

    @Override
    public void insert(Offshore offshore, TokenModel tokenModel) throws Exception {
        offshore.preInsert(tokenModel);
        offshore.setOffshore_id(UUID.randomUUID().toString());
        offshoreMapper.insert(offshore);
    }

    //add-ws-7/7-禅道153
    @Override
    public List<Offshore>  selectById3(String business_id) throws Exception {
        Offshore offshore = new Offshore();
        offshore.setBusiness_id(business_id);
        return offshoreMapper.select(offshore);
    }
    //add-ws-7/7-禅道153
}
