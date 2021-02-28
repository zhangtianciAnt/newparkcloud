package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS1000.Business;
import com.nt.dao_Pfans.PFANS1000.IncomeExpenditure;
import com.nt.dao_Pfans.PFANS1000.Vo.BusinessVo;
import com.nt.service_pfans.PFANS1000.BusinessService;
import com.nt.service_pfans.PFANS1000.IncomeExpenditureService;
import com.nt.service_pfans.PFANS1000.OffshoreService;
import com.nt.service_pfans.PFANS3000.JapanCondominiumService;
import com.nt.service_pfans.PFANS3000.TicketsService;
import com.nt.utils.*;
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
@RequestMapping("/incomeexpenditure")
public class Pfans1049Controller {


    @Autowired
    private IncomeExpenditureService incomeexpenditureservice;
    @Autowired
    private TokenService tokenService;

    @GetMapping("list")
    public ApiResult list(HttpServletRequest request) throws Exception {
        return ApiResult.success(incomeexpenditureservice.getdatalist());
    }

    @GetMapping("/selectlist")
    public ApiResult selectlist(String year, String group_id, HttpServletRequest request) throws Exception {
        return ApiResult.success(incomeexpenditureservice.selectlist(year, group_id));
    }

    @GetMapping("/getradio")
    public ApiResult getradio(String radiox, String radioy, HttpServletRequest request, HttpServletResponse resp) throws Exception {
        try {
            XSSFWorkbook work = incomeexpenditureservice.getradio(radiox, radioy, request, resp);
            OutputStream os = resp.getOutputStream();// 取得输出流
            resp.setContentType("application/vnd.ms-excel;charset=utf-8");
            String fileName = "收支データ";
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

    @PostMapping("/insert")
    public ApiResult insert(@RequestBody List<IncomeExpenditure> incomeexpenditure, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        incomeexpenditureservice.insert(incomeexpenditure, tokenModel);
        return ApiResult.success();
    }
}
