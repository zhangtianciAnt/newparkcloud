package com.nt.service_pfans.PFANS1000;


import com.nt.dao_Pfans.PFANS1000.IncomeExpenditure;
import com.nt.dao_Pfans.PFANS1000.ThemePlan;
import com.nt.dao_Pfans.PFANS1000.Vo.IncomeExpenditureVo;
import com.nt.utils.dao.TokenModel;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface IncomeExpenditureService {

    List<IncomeExpenditure> getdatalist() throws Exception;

    List<IncomeExpenditure> selectlist(String year, String group_id) throws Exception;

    XSSFWorkbook getradio(String radiox, String radioy, HttpServletRequest request, HttpServletResponse resp) throws Exception;

    void insert(List<IncomeExpenditure> incomeexpenditure, TokenModel tokenModel) throws Exception;
}
