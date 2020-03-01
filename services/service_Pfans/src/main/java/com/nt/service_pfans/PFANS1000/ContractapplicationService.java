package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.Contractapplication;
import com.nt.dao_Pfans.PFANS1000.Vo.ContractapplicationVo;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface ContractapplicationService {

    ContractapplicationVo get(Contractapplication contractapplication) throws Exception;

    public void update(ContractapplicationVo contractapplication, TokenModel tokenModel) throws Exception;

    public void insert(ContractapplicationVo contractapplication, TokenModel tokenModel)throws Exception;

    public void insertBook(Contractapplication contractapplication, TokenModel tokenModel)throws Exception;
}
