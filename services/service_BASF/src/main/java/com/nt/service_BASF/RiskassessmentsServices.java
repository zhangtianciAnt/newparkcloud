package com.nt.service_BASF;

import com.nt.dao_BASF.Riskassessments;
import com.nt.utils.dao.TokenModel;

import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF
 * @ClassName: RiskassessmentsServices
 * @Author: 王哲
 * @Description: 风险研判（MySql表）Service
 * @Date: 2020/3/25 15:42
 * @Version: 1.0
 */
public interface RiskassessmentsServices {

    //获取风险研判数据
    List<Riskassessments> getAll(Riskassessments riskassessments) throws Exception;

    //更新风险研判数据
    void updataRiskassessments(Riskassessments riskassessments, TokenModel tokenModel) throws Exception;

    //增加风险判研数据
    void insertRiskassessments(Riskassessments riskassessments, TokenModel tokenModel) throws Exception;

    //根据id查找风险研判数据
    Riskassessments getDataById(String id) throws Exception;

    //根据装置code查找今日有无填写信息
    boolean checkExist(String devicecode) throws Exception;

    //查询装置今日已填写的风险研判信息
    List<Riskassessments> writeList() throws Exception;


}
