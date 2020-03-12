package com.nt.service_pfans.PFANS1000.mapper;

import com.nt.dao_Pfans.PFANS1000.Judgement;
import com.nt.utils.MyMapper;

import java.util.List;


public interface JudgementMapper extends MyMapper<Judgement>{
    List<Judgement> selectJudgement();
}
