package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.Napalm;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface NapalmService {

    List<Napalm> get(Napalm napalm)throws Exception;

    //  add  ml  211130  分页  from
    List<Napalm> getPage(Napalm napalm)throws Exception;
    //  add  ml  211130  分页  to

    public Napalm One(String napalm_id)throws  Exception;

    public void update(Napalm napalm, TokenModel tokenModel)throws  Exception;
}
