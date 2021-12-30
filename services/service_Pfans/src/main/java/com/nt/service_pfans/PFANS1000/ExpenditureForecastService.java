package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.ExpenditureForecast;
import com.nt.dao_Pfans.PFANS1000.RevenueForecast;
import com.nt.dao_Pfans.PFANS1000.Vo.ExpenditureForecastVo;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface ExpenditureForecastService {

    List<ExpenditureForecast> getInfo(ExpenditureForecast forecast) throws Exception;

    void saveInfo(ExpenditureForecastVo expenditureVo, TokenModel tokenModel) throws Exception;

    //获取未关联theme
    List<ExpenditureForecast> getThemeOutDepth(ExpenditureForecast forecast) throws Exception;

    //获取部门人员与计划差
    List<ExpenditureForecast> getPoortDepth(ExpenditureForecast forecast) throws Exception;

    //定时自动保存
    void saveAuto() throws Exception;

    //页面初始发布时用于初始化数据，只在发布时执行一次
    void temporaryAccess() throws Exception;
}
