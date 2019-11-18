package com.nt.service_pfans.PFANS3000;

import com.nt.dao_Pfans.PFANS3000.JapanCondominium;
import com.nt.dao_Pfans.PFANS3000.Vo.JapanCondominiumVo;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface JapanCondominiumService {

    List<JapanCondominium> getJapanCondominium(JapanCondominium japancondominium) throws Exception;

    public JapanCondominiumVo selectById(String japancondominiumid) throws Exception;

    public void insertJapanCondominiumVo(JapanCondominiumVo japanCondominiumVo, TokenModel tokenModel)throws Exception;

    public void updateJapanCondominiumVo(JapanCondominiumVo japancondominiumVo, TokenModel tokenModel)throws Exception;

}
