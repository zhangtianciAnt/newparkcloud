package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.ThemePlan;
import com.nt.dao_Pfans.PFANS1000.ThemePlanDetail;
import com.nt.dao_Pfans.PFANS1000.Vo.ThemePlanVo;
import com.nt.utils.LogicalException;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface ThemePlanService {

    //列表
    List<ThemePlan> getList(ThemePlan themePlan) throws Exception;

    //详细
    List<ThemePlanDetail> get(ThemePlan themePlan) throws Exception;

    //创建
    public void insert(ThemePlanVo themePlan, TokenModel tokenModel) throws LogicalException;

    //更新
    public void update(ThemePlanVo themePlan, TokenModel tokenModel) throws LogicalException;

}
