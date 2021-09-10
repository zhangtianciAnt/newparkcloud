package com.nt.service_pfans.PFANS1000.mapper;

import com.nt.dao_Pfans.PFANS1000.Contractapplication;
import com.nt.dao_Pfans.PFANS1000.Contractnumbercount;
import com.nt.dao_Pfans.PFANS1000.Vo.ExistVo;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface ContractapplicationMapper extends MyMapper<Contractapplication>{
    //存在check
    public ExistVo existCheck(@Param("contractNumber") String contractNumber);

    //存在check
    public ExistVo existN(@Param("NapinList") List<String> NapinList);

    //存在check
    public ExistVo existQ(@Param("QingqiuList") List<String> QingqiuList);


    //add ccm 0725  采购合同chongfucheck
    public List<Contractnumbercount> purchaseExistCheck(@Param("purnumbers") String purnumbers);
    //add ccm 0725  采购合同chongfucheck

    public List<String> selectPJ(@Param("contractnumber") String contractnumber);

    //做觉书时，不满足条件的回数设置status = 1，Projectcontract表
    public void updateProjectcontractById(@Param("PtList") List<String> PtList);
    //获取所有合同关联的项目编号
    public List<Contractapplication> getPjCode();
}
