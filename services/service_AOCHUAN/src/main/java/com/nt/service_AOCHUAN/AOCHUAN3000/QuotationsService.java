package com.nt.service_AOCHUAN.AOCHUAN3000;

import com.nt.dao_AOCHUAN.AOCHUAN3000.Quotations;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Vo.QuoAndEnq;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface QuotationsService {
    List<Quotations> get() throws Exception;

    Quotations getOne(String id) throws Exception;

    List<QuoAndEnq> getForSupplier(String id) throws Exception;

    List<Quotations> getForCustomer(String id) throws Exception;

    void update(Quotations quotations, TokenModel tokenModel) throws Exception;

    void insert(Quotations quotations, TokenModel tokenModel)throws Exception;

    void delete(String id) throws Exception;
}
