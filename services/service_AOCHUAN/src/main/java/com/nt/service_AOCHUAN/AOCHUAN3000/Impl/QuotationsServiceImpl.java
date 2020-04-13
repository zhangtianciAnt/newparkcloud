package com.nt.service_AOCHUAN.AOCHUAN3000.Impl;

import com.nt.dao_AOCHUAN.AOCHUAN3000.Quotations;
import com.nt.service_AOCHUAN.AOCHUAN3000.QuotationsService;
import com.nt.service_AOCHUAN.AOCHUAN3000.mapper.QuotationsMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class QuotationsServiceImpl implements QuotationsService {

    @Autowired
    private QuotationsMapper quotationsMapper;

    @Override
    public List<Quotations> get() throws Exception {
        return quotationsMapper.selectAll();
    }

    @Override
    public Quotations getOne(String id) throws Exception {
        return quotationsMapper.selectByPrimaryKey(id);
    }

    @Override
    public void update(Quotations quotations, TokenModel tokenModel) throws Exception {
              quotations.preUpdate(tokenModel);
              quotationsMapper.updateByPrimaryKeySelective(quotations);
    }

    @Override
    public void insert(Quotations quotations, TokenModel tokenModel) throws Exception {
        String id = UUID.randomUUID().toString();
        quotations.setQuotations_id(id);
        quotations.preInsert(tokenModel);
        quotationsMapper.insert(quotations);
    }

    @Override
    public void delete(String id) throws Exception {
        quotationsMapper.deleteByPrimaryKey(id);
    }
}
