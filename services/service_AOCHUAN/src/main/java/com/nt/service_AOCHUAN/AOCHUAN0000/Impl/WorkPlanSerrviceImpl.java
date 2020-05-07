package com.nt.service_AOCHUAN.AOCHUAN0000.Impl;

import com.nt.dao_AOCHUAN.AOCHUAN0000.WorkPlan;
import com.nt.service_AOCHUAN.AOCHUAN0000.WorkPlanService;
import com.nt.service_AOCHUAN.AOCHUAN0000.mapper.WorkPlanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkPlanSerrviceImpl implements WorkPlanService {

    @Autowired
    private WorkPlanMapper workPlanMapper;

    /**
     *查询
     * @return
     * @throws Exception
     */
    @Override
    public List<WorkPlan> getWorkPlanList() throws Exception {
        return workPlanMapper.selectByMonth();
    }

    /**
     * 按当前日期查询
     * @return
     * @throws Exception
     */
    @Override
    public List<WorkPlan> selectByToday() throws Exception {
        return workPlanMapper.selectByToday();
    }

    /**
     * 新建
     * @param workPlan
     * @throws Exception
     */
    @Override
    public Boolean insert(WorkPlan workPlan) throws Exception {
        if(workPlanMapper.existCheck(workPlan.getWorkplan_id()) == 0){
            workPlanMapper.insert(workPlan);
        }else{
            return false;
        }
        return true;
    }

    /**
     * 更新
     * @param workPlan
     * @throws Exception
     */
    @Override
    public Boolean update(WorkPlan workPlan) throws Exception {
        if(workPlanMapper.existCheck(workPlan.getWorkplan_id())>0){
            workPlanMapper.updateByPrimaryKey(workPlan);
        }else{
            return false;
        }
        return true;
    }

    /**
     * 删除
     * @param workPlan
     * @throws Exception
     */
    @Override
    public Boolean del(WorkPlan workPlan) throws Exception {
        if(workPlanMapper.existCheck(workPlan.getWorkplan_id())>0){
            workPlanMapper.del(workPlan.getWorkplan_id(),workPlan.getModifyby());
        }else{
            return false;
        }
        return true;
    }

    /**
     * 存在Check
     * @param workPlan
     * @return
     * @throws Exception
     */
    @Override
    public Boolean existCheck(WorkPlan workPlan) throws Exception {

        if(workPlanMapper.existCheck(workPlan.getWorkplan_id()) == 0){
            return false;
        }
        return true;
    }
}
