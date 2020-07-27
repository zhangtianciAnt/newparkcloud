package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.Contractapplication;
import com.nt.dao_Pfans.PFANS1000.Contractnumbercount;
import com.nt.dao_Pfans.PFANS1000.Individual;
import com.nt.dao_Pfans.PFANS1000.Petition;
import com.nt.dao_Pfans.PFANS1000.Vo.ContractapplicationVo;
import com.nt.dao_Pfans.PFANS1000.Vo.ExistVo;
import com.nt.utils.dao.TokenModel;

import java.util.List;
import java.util.Map;

public interface ContractapplicationService {

    ContractapplicationVo get(Contractapplication contractapplication) throws Exception;
    //add-ws-7/22-禅道341任务
    List<Individual> getindividual(Individual individual ) throws Exception;
    //add-ws-7/22-禅道341任务
    List<ContractapplicationVo> getList(List<Contractapplication> contractapplicationlist) throws Exception;

    public void update(ContractapplicationVo contractapplication, TokenModel tokenModel) throws Exception;

    public Map<String, Object> insert(ContractapplicationVo contractapplication, TokenModel tokenModel)throws Exception;
    //upd-ws-7/1-禅道152任务
    Map<String, Object> insertBook(String contractnumber, String rowindex, String countNumber, TokenModel tokenModel) throws Exception;
    //upd-ws-7/1-禅道152任务
    //存在check
    ExistVo existCheck(String contractNumber) throws Exception;

    List<Petition> getPe(String countNumber) throws Exception;
    //add ccm 0725  采购合同chongfucheck
    List<String> purchaseExistCheck(String purnumbers) throws Exception;
    //add ccm 0725  采购合同chongfucheck
}
