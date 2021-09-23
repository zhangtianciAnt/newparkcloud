package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS5000.LogManagement;
import com.nt.dao_Pfans.PFANS5000.LogPersonStatistics;
import com.nt.dao_Pfans.PFANS5000.Vo.LogPersonReturnVo;
import com.nt.service_pfans.PFANS5000.Impl.LogPersonStatisticsServiceImpl;
import com.nt.service_pfans.PFANS5000.LogPersonStatisticsService;
import com.nt.service_pfans.PFANS5000.mapper.LogPersonStatisticsMapper;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.RequestUtils;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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



//    /**
//     * 积木报表
//     * 部门项目别年度统计
//     * 21/9/14 scc
//     * */
//    @GetMapping("/getTableinfoReport")
//    public ApiResult getTableinfoReport(@RequestParam String month, HttpServletRequest request) throws Exception {
//        return ApiResult.success("getTableinfoReport",logpersonstatisticsservice.getTableinfoReport(month));
//    }
}
