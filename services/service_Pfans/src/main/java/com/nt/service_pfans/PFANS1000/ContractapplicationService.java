package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.*;
import com.nt.dao_Pfans.PFANS1000.Vo.ContractapplicationVo;
import com.nt.dao_Pfans.PFANS1000.Vo.ExistVo;
import com.nt.dao_Pfans.PFANS1000.Vo.ReportContractEnVo;
import com.nt.utils.dao.TableDataInfo;
import com.nt.utils.dao.TokenModel;

import java.util.List;
import java.util.Map;

public interface ContractapplicationService {

    ContractapplicationVo get(Contractapplication contractapplication) throws Exception;
    // add   ml   211126  分页   from
    List<Contractapplication> getPage(Contractapplication contractapplication) throws Exception;
    // add   ml   211126  分页   to
    // add   ml   211129  决裁书分页   from
    List<Award> getPages(Award ward) throws Exception;
    // add   ml   211129  决裁书分页   to
    //    dialog优化分页 ztc fr
    TableDataInfo getforContDiaLog(int currentPage, int pageSize) throws Exception;
    //    dialog优化分页 ztc to
    //add-ccm-0610-已经纳品的回数查询 str
    List<Contractnumbercount> getNaPpinAftercount(String contractnumber) throws Exception;
    //add-ccm-0610-已经纳品的回数查询 end

    //add  ml  20210706   契约番号废弃check   from
    boolean getProject(String contractnumber) throws Exception;
    //add  ml  20210706   契约番号废弃check   from

    //add-ws-7/22-禅道341任务
    List<Individual> getindividual(Individual individual ) throws Exception;
    //add-ws-7/22-禅道341任务

    //  add  ml  211130  个别合同分页  from
    List<Individual> getindividualPage(Individual individual ) throws Exception;
    //  add  ml  211130  个别合同分页  to
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

    //合同号申请页面中不同契约数据结转
    void dataCarryover(Contractapplication contractapplication,TokenModel tokenModel) throws Exception;

    //根据合同号查合同区间 scc
    List<String> getContranumber(String contra , TokenModel tokenModel) throws Exception;
}
