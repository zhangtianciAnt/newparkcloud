package com.nt.service_pfans.PFANS2000.mapper;

import com.nt.dao_Pfans.PFANS2000.GoalManagement;
import com.nt.utils.MyMapper;

import java.util.List;

public interface GoalManagementMapper extends MyMapper<GoalManagement> {
    List<GoalManagement> yearsCheck(GoalManagement goalManagement);

}
