package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.Psdcd;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface PsdcdService {

    List<Psdcd> getPsdcd(Psdcd psdcd)throws Exception;

    public Psdcd One(String psdcd_id)throws  Exception;

    public void insert(Psdcd psdcd, TokenModel tokenModel)throws  Exception;

    public void updatePsdcd(Psdcd psdcd, TokenModel tokenModel)throws  Exception;
}
