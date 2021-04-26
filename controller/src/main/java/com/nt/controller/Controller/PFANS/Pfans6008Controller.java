package com.nt.controller.Controller.PFANS;

import com.mysql.jdbc.StringUtils;
import com.nt.dao_Pfans.PFANS1000.Contractapplication;
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
import java.util.ArrayList;
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

    @RequestMapping(value = "/getCostBygroupid", method = {RequestMethod.GET})
    public ApiResult getCostBygroupid(String groupid,String year,HttpServletRequest request) throws Exception {
        //Coststatistics coststatistics = new Coststatistics();
        return ApiResult.success(coststatisticsService.getCostListBygroupid(groupid,year));
    }

    @RequestMapping(value = "/insertCoststatistics", method = {RequestMethod.GET})
    public ApiResult insertCoststatistics(String groupid,String year,HttpServletRequest request) throws Exception {
        Coststatistics coststatistics = new Coststatistics();
        TokenModel tokenModel = tokenService.getToken(request);
//        coststatistics.setOwner(tokenModel.getUserId());
        return ApiResult.success(coststatisticsService.insertCoststatistics(groupid,year,coststatistics, tokenModel));
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

    //gbb add 0804 月度赏与列表
    @RequestMapping(value = "/getcostMonthList", method = {RequestMethod.GET})
    public ApiResult getcostMonthList(String dates,String role,String groupid,HttpServletRequest request) throws Exception {
        if(StringUtils.isNullOrEmpty(groupid))
        {
            groupid = "1";
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(coststatisticsService.getcostMonthList(dates,role,groupid,tokenModel));
    }

    //gbb add 0804 月度赏与详情
    @RequestMapping(value = "/getcostMonth", method = {RequestMethod.GET})
    public ApiResult getcostMonth(String dates,String role,String groupid,HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(coststatisticsService.getcostMonth(dates,role,groupid,tokenModel));
    }

    //gbb add 0805 添加費用統計
    @RequestMapping(value = "/insertcoststatisticsdetail", method = {RequestMethod.POST})
    public ApiResult insertcoststatisticsdetail(@RequestBody List<ArrayList> strData, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        coststatisticsService.insertcoststatisticsdetail(strData, tokenModel);
        return ApiResult.success();
    }

    //gbb add 0807 check是否已经生成个别合同
    @RequestMapping(value = "/checkcontract", method = {RequestMethod.POST})
    public ApiResult checkcontract(@RequestBody Contractapplication contract, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(coststatisticsService.checkcontract(contract));
    }

}
