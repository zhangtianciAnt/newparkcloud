package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS6000.Coststatistics;
import com.nt.dao_Pfans.PFANS6000.CoststatisticsVo;
import com.nt.service_pfans.PFANS6000.CompanyStatisticsService;
import com.nt.service_pfans.PFANS6000.CoststatisticsService;
import com.nt.utils.ApiResult;
import com.nt.utils.LogicalException;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.Map;

@RestController
@RequestMapping("/companystatistics")
public class Pfans6009Controller {

    @Autowired
    private CompanyStatisticsService companyStatisticsService;


    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/getCompanyReport1", method = {RequestMethod.GET})
    public ApiResult getCompanyReport1(String groupid,String years, HttpServletRequest request) throws Exception {
        Coststatistics coststatistics = new Coststatistics();
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(companyStatisticsService.getCosts(groupid,years));
    }


    @RequestMapping(value = "/getCompanyReport2", method = {RequestMethod.GET})
    public ApiResult getCompanyReport2(String groupid,String years,HttpServletRequest request) throws Exception {
        Coststatistics coststatistics = new Coststatistics();
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(companyStatisticsService.getWorkTimes(groupid,years));
    }

    @RequestMapping(value = "/getCompanyReport3", method = {RequestMethod.GET})
    public ApiResult getCompanyReport3(String groupid,String years,HttpServletRequest request) throws Exception {
        Coststatistics coststatistics = new Coststatistics();
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(companyStatisticsService.getWorkerCounts(groupid,years));
    }

    /**
     * 导出Excel
     *
     */
    @RequestMapping(value = "/downloadExcel", method = { RequestMethod.GET })
    public ApiResult downloadExcel(String groupid,String years,HttpServletRequest request, HttpServletResponse resp) {
        try {
            XSSFWorkbook work =  companyStatisticsService.downloadExcel(groupid,years,request,resp);
            OutputStream os = resp.getOutputStream();// 取得输出流
            String fileName = "任务清单";
            resp.setContentType("application/vnd.ms-excel;charset=utf-8");
            resp.setHeader("Content-Disposition", "attachment;filename="
                    + new String((fileName + ".xls").getBytes(), "iso-8859-1"));
            work.write(os);
            os.close();
            return ApiResult.success();
        }catch (LogicalException e) {
            return ApiResult.fail(e.getMessage());
        } catch (Exception e) {
            return ApiResult.fail("操作失败！");
        }
    }
}
