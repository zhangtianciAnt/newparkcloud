package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.Napalm;
import com.nt.dao_Pfans.PFANS1000.Vo.NapalmVo;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface NapalmService {

    List<Napalm> get(Napalm napalm)throws Exception;

    public Napalm One(String napalm_id)throws  Exception;

    public void update(Napalm napalm, TokenModel tokenModel)throws  Exception;

    List<Napalm> getNapSearch(NapalmVo napalmVo) throws Exception;
}
