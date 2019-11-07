package com.nt.service_pfans.PFANS3000;

import com.nt.dao_Pfans.PFANS3000.JapanCondominium;
import com.nt.dao_Pfans.PFANS3000.Vo.JapanCondominiumVo;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface JapanCondominiumService {
    //列表查询
    List<JapanCondominium> getJapanCondominium(JapanCondominium japancondominium) throws Exception;

    // 按id查询
    public JapanCondominiumVo selectById(String japancondominiumid) throws Exception;

    //增加
    public void insertJapanCondominiumVo(JapanCondominiumVo japanCondominiumVo, TokenModel tokenModel)throws Exception;

    //修改
    public void updateJapanCondominiumVo(JapanCondominiumVo japancondominiumVo, TokenModel tokenModel)throws Exception;

}
