package com.nt.service_AOCHUAN.AOCHUAN7000;

import com.nt.dao_AOCHUAN.AOCHUAN7000.Crerule;
import com.nt.dao_AOCHUAN.AOCHUAN7000.Docurule;
import com.nt.dao_AOCHUAN.AOCHUAN7000.Helprule;
import com.nt.dao_AOCHUAN.AOCHUAN7000.Vo.All;
import com.nt.dao_AOCHUAN.AOCHUAN7000.Vo.DocuruleVo;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface DocuruleService {
    List<Docurule> get(Docurule docurule) throws Exception;

    DocuruleVo One(String docurule_id) throws Exception;

//    List<Helprule> helpOne(String docurule_id) throws Exception;

/*编辑凭证*/
    void update(DocuruleVo docuruleVo, TokenModel tokenModel) throws Exception;

//    /*编辑分录*/
//    void updateCr(Crerule crerule, TokenModel tokenModel) throws Exception;
//
//    /*编辑辅助*/
//    void updateHe(Helprule helprule, TokenModel tokenModel) throws Exception;

    void insert(DocuruleVo docuruleVo, TokenModel tokenModel)throws Exception;

    void delCrerule(String helprule_id) throws Exception;

    Docurule selectByDocutype(String docutype);

    List<All> selectrule(String docurule_id) throws  Exception;

    //删除
    Boolean delete(DocuruleVo docuruleVo, TokenModel tokenModel) throws Exception;

}
