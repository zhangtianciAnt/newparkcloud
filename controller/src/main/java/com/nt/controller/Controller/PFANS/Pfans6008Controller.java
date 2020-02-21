package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS6000.Coststatistics;
import com.nt.dao_Pfans.PFANS6000.CoststatisticsVo;
import com.nt.service_pfans.PFANS6000.CoststatisticsService;
import com.nt.utils.*;
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
import java.util.List;

@RestController
@RequestMapping("/coststatistics")
public class Pfans6008Controller {

    @Autowired
    private CoststatisticsService coststatisticsService;


    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/getCostList", method = {RequestMethod.GET})
    public ApiResult getCostList(HttpServletRequest request) throws Exception {
        Coststatistics coststatistics = new Coststatistics();
        TokenModel tokenModel = tokenService.getToken(request);
//        coststatistics.setOwner(tokenModel.getUserId());
        return ApiResult.success(coststatisticsService.getCostList(coststatistics));
    }

    @RequestMapping(value = "/insertCoststatistics", method = {RequestMethod.POST})
    public ApiResult insertCoststatistics(HttpServletRequest request) throws Exception {
        Coststatistics coststatistics = new Coststatistics();
        TokenModel tokenModel = tokenService.getToken(request);
//        coststatistics.setOwner(tokenModel.getUserId());
        return ApiResult.success(coststatisticsService.insertCoststatistics(coststatistics, tokenModel));
    }

    /**
     * 导出Excel
     *
     */
    @RequestMapping(value = "/downloadExcel", method = { RequestMethod.POST })
    public ApiResult downloadExcel(@RequestBody CoststatisticsVo coststatisticsVo, HttpServletRequest request, HttpServletResponse resp) {
        try {
            XSSFWorkbook work =  coststatisticsService.downloadExcel(coststatisticsVo,request,resp);
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
