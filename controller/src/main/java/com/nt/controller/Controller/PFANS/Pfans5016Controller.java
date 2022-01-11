package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS5000.LogManagement;
import com.nt.dao_Pfans.PFANS5000.LogPersonStatistics;
import com.nt.dao_Pfans.PFANS5000.Vo.LogPersonReturnVo;
import com.nt.service_pfans.PFANS5000.Impl.LogPersonStatisticsServiceImpl;
import com.nt.service_pfans.PFANS5000.LogPersonStatisticsService;
import com.nt.service_pfans.PFANS5000.mapper.LogPersonStatisticsMapper;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/personlog")
public class Pfans5016Controller {

    @Autowired
    private LogPersonStatisticsService logpersonstatisticsservice;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private LogPersonStatisticsServiceImpl personStatisticsService;

    //region scc add 9/9 日志人别 from
    @RequestMapping(value = "/getLogPerson", method = {RequestMethod.GET})
    public ApiResult getLogPerson(String month, HttpServletRequest request) throws Exception {
        if (month == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        LogPersonStatistics owners = new LogPersonStatistics();
        owners.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(logpersonstatisticsservice.getLogPerson(owners,month,tokenModel));
    }
    //region scc add 9/9 日志人别 to

    //region scc add 9/13 根据id更新记录，参数vo from
    @RequestMapping(value = "/updateByVoId", method = {RequestMethod.POST})
    public ApiResult updateByVoId(@RequestBody List<LogPersonReturnVo> returnvo, HttpServletRequest request) throws Exception {
        if (returnvo.size() == 0) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        logpersonstatisticsservice.updateByVoId(returnvo);
        return ApiResult.success();
    }
    //endregion scc add 9/13 根据id更新记录，参数vo to


    //region scc add 9/24 定时任务获取日志人别数据 from
    @RequestMapping(value = "/saveLogPersonStatistics", method = {RequestMethod.GET})
    public ApiResult saveLogPersonStatistics(HttpServletRequest request) throws Exception {
        logpersonstatisticsservice.saveLogPersonStatistics();
        return ApiResult.success();
    }
    //region scc add 9/24 定时任务获取日志人别数据 to

    /**
     *  日志人别导出
     *  scc add 21/12/2
     */
    @RequestMapping(value = "/downloadExcel", method = { RequestMethod.GET},produces = "text/html;charset=UTF-8")
    public void downloadExcel(@RequestParam String month, HttpServletRequest request, HttpServletResponse resp) {
        try {
            logpersonstatisticsservice.downloadExcel(month,request,resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
