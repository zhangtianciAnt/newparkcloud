package com.nt.service_pfans.PFANS6000.Impl;

import com.nt.dao_Pfans.PFANS6000.Supplierinfor;
import com.nt.service_pfans.PFANS6000.SupplierinforService;
import com.nt.service_pfans.PFANS6000.mapper.SupplierinforMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class SupplierinforServiceImpl implements SupplierinforService {

    @Autowired
    private SupplierinforMapper supplierinforMapper;

    @Override
    public List<Supplierinfor> getSupplierNameList(Supplierinfor supplierinfor, HttpServletRequest request) throws Exception {
        return null;
    }

    @Override
    public List<Supplierinfor> getsupplierinfor(Supplierinfor supplierinfor) throws Exception {
        return supplierinforMapper.select(supplierinfor);
    }

    @Override
    public Supplierinfor getsupplierinforApplyOne(String supplierinfor_id) throws Exception {
        return supplierinforMapper.selectByPrimaryKey(supplierinfor_id);
    }

    @Override
    public void updatesupplierinforApply(Supplierinfor supplierinfor, TokenModel tokenModel) throws Exception {
        supplierinforMapper.updateByPrimaryKeySelective(supplierinfor);
    }

    @Override
    public void createsupplierinforApply(Supplierinfor supplierinfor, TokenModel tokenModel) throws Exception {
        supplierinfor.preInsert(tokenModel);
        supplierinfor.setSupplierinfor_id(UUID.randomUUID().toString());
        supplierinforMapper.insert(supplierinfor);
    }

}
