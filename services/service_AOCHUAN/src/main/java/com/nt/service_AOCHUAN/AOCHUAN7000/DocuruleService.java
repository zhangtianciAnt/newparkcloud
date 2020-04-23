package com.nt.service_AOCHUAN.AOCHUAN7000;

import com.nt.dao_AOCHUAN.AOCHUAN6000.Secrecy;
import com.nt.dao_AOCHUAN.AOCHUAN7000.Docurule;
import com.nt.dao_AOCHUAN.AOCHUAN7000.Vo.DocuruleVo;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface DocuruleService {
    List<Docurule> get(Docurule docurule) throws Exception;

    List<Docurule> One(String docurule_id) throws Exception;

    void update(DocuruleVo docuruleVo, TokenModel tokenModel) throws Exception;

    void insert(DocuruleVo docuruleVo, TokenModel tokenModel)throws Exception;

    void delete(String id) throws Exception;


}
