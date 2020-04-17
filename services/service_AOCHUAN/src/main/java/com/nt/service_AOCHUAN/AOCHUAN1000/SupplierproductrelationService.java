package com.nt.service_AOCHUAN.AOCHUAN1000;

import com.nt.dao_AOCHUAN.AOCHUAN1000.Supplierproductrelation;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface SupplierproductrelationService {

    List<Supplierproductrelation> get() throws Exception;

    List<Supplierproductrelation> getBySupplierbaseinforId(String supplierbaseinforId) throws Exception;

    Supplierproductrelation getOne(String id) throws Exception;

    void update(Supplierproductrelation supplierproductrelation, TokenModel tokenModel) throws Exception;

    void insert(Supplierproductrelation supplierproductrelation, TokenModel tokenModel)throws Exception;

    void delete(String id) throws Exception;
}
