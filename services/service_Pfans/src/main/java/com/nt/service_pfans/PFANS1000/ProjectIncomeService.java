package com.nt.service_pfans.PFANS1000;


import com.nt.dao_Pfans.PFANS1000.ProjectIncome;
import com.nt.dao_Pfans.PFANS1000.Vo.ProjectIncomeVo3;
import com.nt.dao_Pfans.PFANS6000.CoststatisticsVo;
import com.nt.utils.LogicalException;
import com.nt.utils.dao.TokenModel;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface ProjectIncomeService {
    public XSSFWorkbook downloadExcel(String projectincomeid, HttpServletRequest request, HttpServletResponse resp) throws LogicalException;

    List<String> importUser(HttpServletRequest request, TokenModel tokenModel) throws Exception ;

    ProjectIncome selectById(String projectincomeid) throws Exception;

    List<ProjectIncome> get(ProjectIncome projectincome) throws Exception;

    ProjectIncomeVo3 getprojects(String groupid, String userid, String year, String month) throws Exception;

    void insert(ProjectIncome projectincome, TokenModel tokenModel) throws Exception;
}
