package com.nt.service_pfans.PFANS8000;

import com.nt.dao_Pfans.PFANS1000.Vo.JudgementVo;
import com.nt.dao_Pfans.PFANS8000.MonthlyRate;
import com.nt.dao_Pfans.PFANS8000.WorkingDay;
import com.nt.utils.dao.TokenModel;

import java.util.List;


public interface MonthlyRateService {

    List<MonthlyRate> getdatalist() throws Exception;

    void CreateData(List<MonthlyRate> monthlyrate,TokenModel tokenModel) throws Exception;

    void UpdateData(List<MonthlyRate> monthlyrate, TokenModel tokenModel) throws Exception;

    List<MonthlyRate> slectlist(MonthlyRate monthlyrate) throws Exception;
}
