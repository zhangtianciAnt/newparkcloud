package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.PersonnelPlan;
import com.nt.dao_Pfans.PFANS1000.ThemePlan;
import com.nt.dao_Pfans.PFANS1000.ThemePlanDetail;
import com.nt.dao_Pfans.PFANS1000.Vo.ThemePlanVo;
import com.nt.dao_Pfans.PFANS1000.Vo.ThemePlanDetailVo;
import com.nt.utils.LogicalException;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface ThemePlanService {

    List<PersonnelPlan> getAll(String groupid, String year) throws Exception;

    //列表
    List<ThemePlan> getList(ThemePlan themePlan) throws Exception;


    List<ThemePlanDetailVo> detilList(ThemePlanDetail themePlanDetail) throws Exception;

    List<ThemePlanDetail> getthemename(String themename) throws Exception;
    //add-ws-01/06-禅道任务710
    List<ThemePlanDetail> themenametype(String type) throws Exception;
    //add-ws-01/06-禅道任务710
    //更新
    public void update(ThemePlanVo themePlan, TokenModel tokenModel) throws LogicalException;

    //新建
    public void inserttheme(List<ThemePlanDetailVo> themePlanDetailVo, TokenModel tokenModel) throws LogicalException;

}
