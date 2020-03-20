package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.Giving;
import com.nt.dao_Pfans.PFANS2000.Lackattendance;
import com.nt.dao_Pfans.PFANS2000.Residual;
import com.nt.dao_Pfans.PFANS2000.Vo.GivingVo;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface GivingService {

    GivingVo List(String giving_id) throws Exception;

    void insert(String generation, TokenModel tokenModel)throws Exception;

    void insertBase(String  givingid,TokenModel tokenModel)throws Exception;

    void insertOtherOne(String  givingid,TokenModel tokenModel)throws Exception;

    void insertOtherTwo(String  givingid,TokenModel tokenModel)throws Exception;

    void insertLackattendance(String  givingid,TokenModel tokenModel)throws Exception;

    void insertResidual(String  givingid,TokenModel tokenModel)throws Exception;

    void insertContrast(String  givingid,TokenModel tokenModel)throws Exception;

    List<Giving> getDataList(Giving giving) throws Exception;

    void save(GivingVo givingvo, TokenModel tokenModel)throws Exception;

    // 本月加班数据变更时，重新计算加班费合计
    Residual thisMonthOvertimeChange(GivingVo givingVo) throws Exception;

    // 本月欠勤数据变更时，重新计算欠勤费合计
    Lackattendance thisMonthLacktimeChange(GivingVo givingVo) throws Exception;
}


