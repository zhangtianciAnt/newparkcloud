package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS1000.PolicyContract;
import com.nt.dao_Pfans.PFANS1000.ProjectIncome;
import com.nt.service_pfans.PFANS1000.ProjectIncomeService;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

@RestController
@RequestMapping("/projectincome")
public class Pfans1048Controller {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private ProjectIncomeService projectincomeservice;

    @GetMapping("/downloadExcel")
    public ApiResult downloadExcel(String projectincomeid, HttpServletRequest request, HttpServletResponse resp) {
        try {
            XSSFWorkbook work = projectincomeservice.downloadExcel(projectincomeid, request, resp);
            OutputStream os = resp.getOutputStream();// 取得输出流
            resp.setContentType("application/vnd.ms-excel;charset=utf-8");
            String fileName = "项目结转表";
            resp.setContentType("application/vnd.ms-excel;charset=utf-8");
            resp.setHeader("Content-Disposition", "attachment;filename="
                    + new String((fileName + ".xls").getBytes(), "iso-8859-1"));
            work.write(os);
            os.close();
            return ApiResult.success();
        } catch (LogicalException e) {
            return ApiResult.fail(e.getMessage());
        } catch (Exception e) {
            return ApiResult.fail("操作失败！");
        }
    }

    @PostMapping("/importUser")
    public ApiResult importUser(HttpServletRequest request, String flag) {
        try {
            TokenModel tokenModel = tokenService.getToken(request);
            return ApiResult.success(projectincomeservice.importUser(request, tokenModel));
        } catch (LogicalException e) {
            return ApiResult.fail(e.getMessage());
        } catch (Exception e) {
            return ApiResult.fail("操作失败！");
        }
    }

    @GetMapping("/selectById")
    public ApiResult selectById(String projectincomeid, HttpServletRequest request) throws Exception {
        if (projectincomeid == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(projectincomeservice.selectById(projectincomeid));
    }

    @GetMapping("/get")
    public ApiResult get(HttpServletRequest request) throws Exception {
        ProjectIncome projectincome = new ProjectIncome();
        return ApiResult.success(projectincomeservice.get(projectincome));
    }

    @GetMapping("/getprojects")
    public ApiResult getprojects(String groupid, String userid, String year, String month, HttpServletRequest request) throws Exception {
        return ApiResult.success(projectincomeservice.getprojects(groupid, userid, year, month));
    }

    @PostMapping("/insert")
    public ApiResult insert(@RequestBody ProjectIncome projectincome, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        projectincomeservice.insert(projectincome, tokenModel);
        return ApiResult.success();
    }
}
