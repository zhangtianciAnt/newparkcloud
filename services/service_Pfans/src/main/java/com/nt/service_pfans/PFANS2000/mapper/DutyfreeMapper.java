package com.nt.service_pfans.PFANS2000.mapper;

import com.nt.dao_Pfans.PFANS2000.Dutyfree;
import com.nt.dao_Pfans.PFANS2000.Vo.DutyfreeVo;
import com.nt.utils.MyMapper;

import java.util.List;

public interface DutyfreeMapper  extends MyMapper<Dutyfree> {
    List<DutyfreeVo> getdutyfree();
}
