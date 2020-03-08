package com.nt.service_pfans.PFANS1000.mapper;

import com.nt.dao_Pfans.PFANS1000.Contractapplication;
import com.nt.dao_Pfans.PFANS1000.Vo.ExistVo;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface ContractapplicationMapper extends MyMapper<Contractapplication>{
    //存在check
    public ExistVo existCheck(@Param("contractNumber") String contractNumber);
}
