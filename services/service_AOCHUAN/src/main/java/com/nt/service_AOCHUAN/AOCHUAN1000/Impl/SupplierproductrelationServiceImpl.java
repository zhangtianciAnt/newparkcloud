package com.nt.service_AOCHUAN.AOCHUAN1000.Impl;

import com.nt.dao_AOCHUAN.AOCHUAN1000.Supplierproductrelation;
import com.nt.service_AOCHUAN.AOCHUAN1000.SupplierproductrelationService;
import com.nt.service_AOCHUAN.AOCHUAN1000.mapper.SupplierproductrelationMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@Service
public class SupplierproductrelationServiceImpl implements SupplierproductrelationService {
    @Autowired
    private SupplierproductrelationMapper supplierproductrelationMapper;

    @Override
    public List<Supplierproductrelation> get() throws Exception {
        return null;
    }

    @Override
    public List<Supplierproductrelation> getBySupplierbaseinforId(String supplierbaseinforId) throws Exception {
        return supplierproductrelationMapper.getBySupplierbaseinforId(supplierbaseinforId);
    }

    @Override
    public Supplierproductrelation getOne(String id) throws Exception {
        return null;
    }

    @Override
    public void update(Supplierproductrelation supplierproductrelation, TokenModel tokenModel) throws Exception {

    }

    @Override
    public void insert(Supplierproductrelation supplierproductrelation, TokenModel tokenModel) throws Exception {
        String id = UUID.randomUUID().toString();
        supplierproductrelation.setSupplierproductrelation_id(id);
        supplierproductrelation.preInsert(tokenModel);
        supplierproductrelationMapper.insert(supplierproductrelation);
    }

    @Override
    public void delete(String id) throws Exception {

    }

    @Override
    public void deleteByBaseinforId(String baseinforId) throws Exception {
        supplierproductrelationMapper.deleteByBaseinforId(baseinforId);
    }
}
