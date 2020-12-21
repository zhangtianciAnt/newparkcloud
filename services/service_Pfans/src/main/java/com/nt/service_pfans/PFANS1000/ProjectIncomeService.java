package com.nt.service_pfans.PFANS1000;


import com.nt.dao_Pfans.PFANS1000.ProjectIncome;
import com.nt.dao_Pfans.PFANS1000.Vo.ProjectIncomeVo3;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ProjectIncomeService {

    List<String> importUser(HttpServletRequest request, TokenModel tokenModel) throws Exception ;

    ProjectIncome selectById(String projectincomeid) throws Exception;

    List<ProjectIncome> get(ProjectIncome projectincome) throws Exception;

    ProjectIncomeVo3 getprojects(String groupid, String userid, String year, String month) throws Exception;

    void insert(ProjectIncome projectincome, TokenModel tokenModel) throws Exception;
}
