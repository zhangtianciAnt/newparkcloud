package com.nt.service_pfans.PFANS6000.Impl;

import com.nt.dao_Pfans.PFANS6000.Customerinfor;
import com.nt.service_pfans.PFANS6000.CustomerinforService;
import com.nt.service_pfans.PFANS6000.mapper.CustomerinforMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class CustomerinforServiceImpl implements CustomerinforService {

    @Autowired
    private CustomerinforMapper customerinforMapper;


    @Override
    public List<Customerinfor> getcustomerinfor(Customerinfor customerinfor) throws Exception {
        return customerinforMapper.select(customerinfor);
    }

    @Override
    public Customerinfor getcustomerinforApplyOne(String customerinfor_id) throws Exception {
        return customerinforMapper.selectByPrimaryKey(customerinfor_id);
    }

    @Override
    public void updatecustomerinforApply(Customerinfor customerinfor, TokenModel tokenModel) throws Exception {
        customerinforMapper.updateByPrimaryKeySelective(customerinfor);
    }

    @Override
    public void createcustomerinforApply(Customerinfor customerinfor, TokenModel tokenModel) throws Exception {
        customerinfor.preInsert(tokenModel);
        customerinfor.setCustomerinfor_id(UUID.randomUUID().toString());
        customerinforMapper.insert(customerinfor);
    }

}
