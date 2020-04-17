package com.nt.service_AOCHUAN.AOCHUAN2000.Impl;

import com.nt.dao_AOCHUAN.AOCHUAN2000.Customerbaseinfor;
import com.nt.service_AOCHUAN.AOCHUAN2000.CustomerbaseinforService;
import com.nt.service_AOCHUAN.AOCHUAN2000.mapper.CustomerbaseinforMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CustomerbaseinforServiceImpl implements CustomerbaseinforService {

    @Autowired
    private CustomerbaseinforMapper customerbaseinforMapper;

    @Override
    public List<Customerbaseinfor> get() throws Exception {
        return customerbaseinforMapper.selectAll();
    }

    @Override
    public Customerbaseinfor getOne(String id) throws Exception {
        return customerbaseinforMapper.selectByPrimaryKey(id);
    }

    @Override
    public void update(Customerbaseinfor customerbaseinfor, TokenModel tokenModel) throws Exception {
        customerbaseinfor.preUpdate(tokenModel);
        customerbaseinforMapper.updateByPrimaryKeySelective(customerbaseinfor);
    }

    @Override
    public String insert(Customerbaseinfor customerbaseinfor, TokenModel tokenModel) throws Exception {
        String id = UUID.randomUUID().toString();
        customerbaseinfor.setCustomerbaseinfor_id(id);
        customerbaseinfor.preInsert(tokenModel);
        customerbaseinforMapper.insert(customerbaseinfor);
        return id;
    }

    @Override
    public void delete(String id) throws Exception {
        customerbaseinforMapper.deleteByPrimaryKey(id);
    }
}
