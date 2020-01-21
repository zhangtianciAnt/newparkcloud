package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.Contractapplication;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface ContractapplicationService {

    List<Contractapplication> get(Contractapplication contractapplication) throws Exception;

    public void update(Contractapplication contractapplication, TokenModel tokenModel) throws Exception;

    public void insert(Contractapplication contractapplication, TokenModel tokenModel)throws Exception;

}
