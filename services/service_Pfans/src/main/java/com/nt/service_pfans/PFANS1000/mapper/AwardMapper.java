package com.nt.service_pfans.PFANS1000.mapper;

import com.nt.dao_Pfans.PFANS1000.Award;
import com.nt.utils.MyMapper;

import java.util.List;

public interface AwardMapper extends MyMapper<Award> {

    List<Award> selectList(Award award);
}
