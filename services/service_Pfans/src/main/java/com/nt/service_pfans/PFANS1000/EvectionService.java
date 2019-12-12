package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.Evection;
import com.nt.dao_Pfans.PFANS1000.Vo.EvectionVo;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface EvectionService {

    List<Evection> get(Evection evection) throws Exception;

     EvectionVo selectById(String evectionid) throws Exception;

    void insertEvectionVo(EvectionVo evectionvo, TokenModel tokenModel)throws Exception;

    void updateEvectionVo(EvectionVo evectionvo, TokenModel tokenModel)throws Exception;

}
