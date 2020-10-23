package com.nt.service_AOCHUAN.AOCHUAN1000;

import com.nt.dao_AOCHUAN.AOCHUAN1000.Supplierbaseinfor;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface SupplierbaseinforService {
    List<Supplierbaseinfor> get() throws Exception;

    Supplierbaseinfor getOne(String id) throws Exception;

    void update(Supplierbaseinfor supplierbaseinfor, TokenModel tokenModel) throws Exception;

    String insert(Supplierbaseinfor supplierbaseinfor, TokenModel tokenModel)throws Exception;

    void delete(String id) throws Exception;

    List<Supplierbaseinfor> getSuppliersExceptUnique() throws Exception;

    List<String> importInfo() throws Exception;

//    ResultVo login(String url, String content) throws Exception;

    void login1(List<Supplierbaseinfor> supplierbaseinforList,TokenModel tokenModel,Boolean flg) throws Exception;

//    ResultVo batchSave(String url, String cookie, String content) throws Exception;
}
