package com.nt.service_AOCHUAN.AOCHUAN6000;


import com.nt.dao_AOCHUAN.AOCHUAN6000.Secrecy;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface SecrecyService {
    List<Secrecy> get(Secrecy secrecy) throws Exception;

    Secrecy One(String id) throws Exception;

    void update(Secrecy secrecy, TokenModel tokenModel) throws Exception;

    void insert(Secrecy secrecy, TokenModel tokenModel)throws Exception;

    void delete(String id) throws Exception;
}
