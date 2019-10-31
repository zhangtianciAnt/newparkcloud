package com.nt.service_pfans.PFANS2000.mapper;

import com.nt.dao_Pfans.PFANS2000.RecruitJudgement;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RecruitJudgementMapper extends MyMapper<RecruitJudgement> {
    List<RecruitJudgement> getRecruitJudgement(@Param("owners") List<String> owners);
}
