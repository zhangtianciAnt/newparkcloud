package com.nt.service_pfans.PFANS1000;


import com.nt.dao_Pfans.PFANS1000.IncomeExpenditure;
import com.nt.dao_Pfans.PFANS1000.ThemePlan;
import com.nt.dao_Pfans.PFANS1000.Vo.IncomeExpenditureVo;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface IncomeExpenditureService {

    List<IncomeExpenditure> getdatalist() throws Exception;

    List<IncomeExpenditure> selectlist(String year, String group_id) throws Exception;

    List<IncomeExpenditureVo> getradio(String radiox, String radioy) throws Exception;

    void insert(List<IncomeExpenditure> incomeexpenditure, TokenModel tokenModel) throws Exception;
}
