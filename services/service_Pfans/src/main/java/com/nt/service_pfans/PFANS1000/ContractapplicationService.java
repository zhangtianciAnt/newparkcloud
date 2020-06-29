package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.Contractapplication;
import com.nt.dao_Pfans.PFANS1000.Contractnumbercount;
import com.nt.dao_Pfans.PFANS1000.Petition;
import com.nt.dao_Pfans.PFANS1000.Vo.ContractapplicationVo;
import com.nt.dao_Pfans.PFANS1000.Vo.ExistVo;
import com.nt.utils.dao.TokenModel;

import java.util.List;
import java.util.Map;

public interface ContractapplicationService {

    ContractapplicationVo get(Contractapplication contractapplication) throws Exception;

    List<ContractapplicationVo> getList(List<Contractapplication> contractapplicationlist) throws Exception;

    public void update(ContractapplicationVo contractapplication, TokenModel tokenModel) throws Exception;

    public Map<String, Object> insert(ContractapplicationVo contractapplication, TokenModel tokenModel)throws Exception;

    String insertBook(String contractnumber, String rowindex, String countNumber, TokenModel tokenModel) throws Exception;
    //存在check
    ExistVo existCheck(String contractNumber) throws Exception;

    List<Petition> getPe(String countNumber) throws Exception;
}
