package com.nt.service_AOCHUAN.AOCHUAN6000.Impl;

import com.nt.dao_AOCHUAN.AOCHUAN6000.Secrecy;
import com.nt.service_AOCHUAN.AOCHUAN6000.SecrecyService;
import com.nt.service_AOCHUAN.AOCHUAN6000.mapper.SecrecyMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor=Exception.class)
public class SecrecyServiceImpl implements SecrecyService {

    @Autowired
    private SecrecyMapper secrecyMapper;




    @Override
    public List<Secrecy> get(Secrecy secrecy) throws Exception {
        return secrecyMapper.select(secrecy);
    }

    @Override
    public Secrecy One(String id) throws Exception {
        return secrecyMapper.selectByPrimaryKey(id);
    }

    @Override
    public void update(Secrecy secrecy, TokenModel tokenModel) throws Exception {
        secrecy.preUpdate(tokenModel);
        secrecyMapper.updateByPrimaryKey(secrecy);

    }

    @Override
    public void insert(Secrecy secrecy, TokenModel tokenModel) throws Exception {
        secrecy.preInsert(tokenModel);
        secrecy.setSecrecy_id(UUID.randomUUID().toString());
        secrecyMapper.insert(secrecy);
    }

    @Override
    public void delete(String id) throws Exception {
        Secrecy secrecy=new Secrecy();
        secrecy.setSecrecy_id(id);
        secrecy.setStatus("1");
        secrecyMapper.updateByPrimaryKey(secrecy);
    }
}
