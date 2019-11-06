package com.nt.service_pfans.PFANS3000;

import com.nt.dao_Pfans.PFANS3000.JapanCondominium;
import com.nt.dao_Pfans.PFANS3000.Vo.JapanCondominiumVo;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface JapanCondominiumService {
    //查看
    List<JapanCondominium> getJapanCondominium(JapanCondominium japancondominium) throws Exception;

    List<JapanCondominium> getJapanCondominiumlist(JapanCondominium japancondominium) throws Exception;

    public JapanCondominium One(String japancondominiumid) throws Exception;
    public JapanCondominiumVo selectById(String japancondominiumid) throws Exception;
    //增加
    public void insertJapanCondominium(JapanCondominium japancondominium, TokenModel tokenModel)throws Exception;
    public void insertJapanCondominiumVo(JapanCondominiumVo japanCondominiumVo, TokenModel tokenModel)throws Exception;
    //修改
    public void updateJapanCondominium(JapanCondominium japancondominium, TokenModel tokenModel)throws Exception;
    public void updateJapanCondominiumVo(JapanCondominiumVo japancondominiumVo, TokenModel tokenModel)throws Exception;

}
