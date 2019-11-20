package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.Giving;
import com.nt.dao_Pfans.PFANS2000.Vo.DutyfreeVo;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface DutyfreeService {

    public List<DutyfreeVo> getdutyfree(TokenModel tokenModel) throws Exception;

}
