package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.Psdcd;
import com.nt.dao_Pfans.PFANS1000.Vo.PsdcdVo;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface PsdcdService {

    List<Psdcd> getPsdcd(Psdcd psdcd)throws Exception;

    public PsdcdVo selectById(String psdcd_id)throws  Exception;

    public void insert(PsdcdVo psdcdVo, TokenModel tokenModel)throws  Exception;

    public void update(PsdcdVo psdcdVo, TokenModel tokenModel)throws  Exception;
}
