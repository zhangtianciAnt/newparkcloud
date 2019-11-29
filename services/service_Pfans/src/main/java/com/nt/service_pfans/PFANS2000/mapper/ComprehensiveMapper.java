package com.nt.service_pfans.PFANS2000.mapper;

import com.nt.dao_Pfans.PFANS2000.Base;
import com.nt.dao_Pfans.PFANS2000.Vo.ComprehensiveVo;
import com.nt.utils.MyMapper;

import java.util.List;

public interface ComprehensiveMapper extends MyMapper<Base> {
    List<ComprehensiveVo> getcomprehensive();
}
