package com.nt.service_AOCHUAN.AOCHUAN1000.Impl;

import com.nt.dao_AOCHUAN.AOCHUAN1000.Supplierbaseinfor;
import com.nt.service_AOCHUAN.AOCHUAN1000.SupplierbaseinforService;
import com.nt.service_AOCHUAN.AOCHUAN1000.mapper.SupplierbaseinforMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SupplierbaseinforServiceImpl implements SupplierbaseinforService {
    @Autowired
    private SupplierbaseinforMapper supplierbaseinforMapper;


    @Override
    public List<Supplierbaseinfor> get() throws Exception {
        return supplierbaseinforMapper.selectAll();
    }

    @Override
    public Supplierbaseinfor getOne(String id) throws Exception {
        return supplierbaseinforMapper.selectByPrimaryKey(id);
    }

    @Override
    public void update(Supplierbaseinfor supplierbaseinfor, TokenModel tokenModel) throws Exception {
        supplierbaseinfor.preUpdate(tokenModel);
        supplierbaseinforMapper.updateByPrimaryKeySelective(supplierbaseinfor);
    }

    @Override
    public String insert(Supplierbaseinfor supplierbaseinfor, TokenModel tokenModel) throws Exception {
        String id = UUID.randomUUID().toString();
        supplierbaseinfor.setSupplierbaseinfor_id(id);
        supplierbaseinfor.preInsert(tokenModel);
        supplierbaseinforMapper.insert(supplierbaseinfor);
        return id;
    }

    @Override
    public void delete(String id) throws Exception {
        supplierbaseinforMapper.deleteByPrimaryKey(id);
    }
}
