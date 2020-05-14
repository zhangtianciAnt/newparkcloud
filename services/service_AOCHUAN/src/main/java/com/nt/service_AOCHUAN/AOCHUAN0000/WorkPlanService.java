package com.nt.service_AOCHUAN.AOCHUAN0000;

import com.nt.dao_AOCHUAN.AOCHUAN0000.WorkPlan;

import java.util.List;

public interface WorkPlanService {

    List<WorkPlan> getWorkPlanList(String id) throws Exception;

    List<WorkPlan> selectByToday() throws  Exception;

    Boolean insert(WorkPlan workPlan) throws  Exception;

    Boolean update(WorkPlan workPlan) throws  Exception;

    Boolean del(WorkPlan workPlan) throws  Exception;

    Boolean existCheck(WorkPlan workPlan) throws  Exception;
}
