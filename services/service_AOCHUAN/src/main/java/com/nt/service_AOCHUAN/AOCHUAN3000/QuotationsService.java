package com.nt.service_AOCHUAN.AOCHUAN3000;

import com.nt.dao_AOCHUAN.AOCHUAN3000.Quotations;
import com.nt.utils.dao.TokenModel;
import org.springframework.stereotype.Service;

import java.util.List;

public interface QuotationsService {
    List<Quotations> get() throws Exception;

    Quotations getOne(String id) throws Exception;

    void update(Quotations quotations, TokenModel tokenModel) throws Exception;

    void insert(Quotations quotations, TokenModel tokenModel)throws Exception;

    void delete(String id) throws Exception;
}
