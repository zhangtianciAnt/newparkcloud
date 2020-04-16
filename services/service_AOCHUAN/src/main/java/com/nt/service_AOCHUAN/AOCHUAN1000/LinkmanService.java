package com.nt.service_AOCHUAN.AOCHUAN1000;

import com.nt.dao_AOCHUAN.AOCHUAN1000.Linkman;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface LinkmanService {
    List<Linkman> get() throws Exception;

    List<Linkman> getByBaseinforId(String baseinfor_id) throws Exception;

    Linkman getOne(String id) throws Exception;

    void update(Linkman linkman, TokenModel tokenModel) throws Exception;

    void insert(Linkman linkman, TokenModel tokenModel)throws Exception;

    void delete(String id) throws Exception;

    void deleteByByBaseinforId(String baseinfor_id) throws Exception;
}
