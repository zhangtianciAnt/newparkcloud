package com.nt.service_pfans.PFANS1000.mapper;

import com.nt.dao_Pfans.PFANS1000.Petition;
import com.nt.dao_Pfans.PFANS1000.Vo.PetitionVo;
import com.nt.utils.MyMapper;

import java.util.List;

public interface PetitionMapper extends MyMapper<Petition> {
//    添加筛选条件 ztc fr
    List<Petition> selectList(PetitionVo petitionVo);
//    添加筛选条件 ztc to
}
