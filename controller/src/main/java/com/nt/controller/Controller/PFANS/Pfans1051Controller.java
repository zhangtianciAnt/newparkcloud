package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS1000.DepartmentAccount;
import com.nt.dao_Pfans.PFANS1000.IncomeExpenditure;
import com.nt.service_pfans.PFANS1000.DepartmentAccountService;
import com.nt.service_pfans.PFANS1000.IncomeExpenditureService;
import com.nt.utils.ApiResult;
import com.nt.utils.LogicalException;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.List;

@RestController
@RequestMapping("/departmentaccount")
public class Pfans1051Controller {


    @Autowired
    private DepartmentAccountService departmentAccountService;
    @Autowired
    private TokenService tokenService;

    @GetMapping("/selectBygroupid")
    public ApiResult selectBygroupid(String groupid,String year,HttpServletRequest request) throws Exception  {
        return ApiResult.success(departmentAccountService.selectBygroupid(year, groupid));
    }

    @PostMapping("/insert")
    public ApiResult insert(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        departmentAccountService.insert();
        return ApiResult.success();
    }

    //查看
    @RequestMapping(value = "/getTable1051infoReport", method = {RequestMethod.GET})
    public ApiResult getTable1051infoReport(String year, String group_id, HttpServletRequest request) throws Exception {
        return ApiResult.success("getTable1051infoReport",departmentAccountService.getTable1051infoReport(year, group_id));
    }
}
