package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.Contractapplication;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface ContractapplicationService {

    List<Contractapplication> get(Contractapplication contractapplication) throws Exception;

    public void update(List<Contractapplication> contractapplication, TokenModel tokenModel) throws Exception;

    public void insert(List<Contractapplication> contractapplication, TokenModel tokenModel)throws Exception;

    public void insertBook(Contractapplication contractapplication, TokenModel tokenModel)throws Exception;

    List<?> getContractList(String contractId, TokenModel tokenModel) throws Exception;
}
