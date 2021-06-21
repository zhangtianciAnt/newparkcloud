package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.*;
import com.nt.dao_Pfans.PFANS1000.Vo.ContractapplicationVo;
import com.nt.dao_Pfans.PFANS1000.Vo.ExistVo;
import com.nt.dao_Pfans.PFANS1000.Vo.ReportContractEnVo;
import com.nt.utils.dao.TokenModel;

import java.util.List;
import java.util.Map;

public interface ContractapplicationService {

    ContractapplicationVo get(Contractapplication contractapplication) throws Exception;

    //add-ccm-0610-已经纳品的回数查询 str
    List<Contractnumbercount> getNaPpinAftercount(String contractnumber) throws Exception;
    //add-ccm-0610-已经纳品的回数查询 end

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
    ExistVo existCheck(String qinqNapinList) throws Exception;

    ExistVo existN(List<String> NapinList) throws Exception;

    ExistVo existQ(List<String> QingqiuList) throws Exception;

    List<Petition> getPe(String countNumber) throws Exception;
    //add ccm 0725  采购合同chongfucheck
    List<Contractnumbercount> purchaseExistCheck(String purnumbers) throws Exception;
    //add ccm 0725  采购合同chongfucheck

    Map<String, String> getworkfolwPurchaseData(Award award) throws Exception;

    boolean getNapinQinqiu(Contractnumbercount contractnumbercount) throws Exception;

    List<ReportContractEnVo> reportContractEn(String conType) throws Exception;

}
