package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS2000.AnnualLeave;
import com.nt.service_pfans.PFANS2000.AnnualLeaveService;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @name 年度休假一览
 */
@RestController
@RequestMapping("/annualLeave")
public class Pfans2013Controller {

    @Autowired
    private AnnualLeaveService annualLeaveService;

    @Autowired
    private TokenService tokenService;

    /**
     * @方法名：getDataList
     * @参数：[request]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/getDataList", method={RequestMethod.GET})
    public ApiResult getDataList(HttpServletRequest request) throws Exception{
        AnnualLeave annualLeave = new AnnualLeave();
        TokenModel tokenModel = tokenService.getToken(request);
//        annualLeave.setOwner(tokenModel.getUserId());
        Calendar cal = Calendar.getInstance();
        String this_year = String.valueOf(cal.get(cal.YEAR));
        annualLeave.setYears(this_year);
        return ApiResult.success(annualLeaveService.getDataList(tokenModel));
    }

    //ccm 1019 计算一个月出勤多少小时
    @RequestMapping(value = "/getDataHours", method={RequestMethod.GET})
    public ApiResult getDataList(String year,String month, HttpServletRequest request) throws Exception{
        SimpleDateFormat sfymd = new SimpleDateFormat("yyyy-MM-dd");
        TokenModel tokenModel = tokenService.getToken(request);
        String years = year;
        if(Integer.valueOf(month) < 4)
        {
            years = String.valueOf(Integer.valueOf(year)-1);
        }
        String startDate = year + "-" + month + "-01";

        Calendar tempEnd = Calendar.getInstance();

        tempEnd.setTime(sfymd.parse(startDate));
        tempEnd.add(Calendar.MONTH, 1);
        tempEnd.add(Calendar.DAY_OF_YEAR,-1);
        String endDate = sfymd.format(tempEnd.getTime());
        Double hours = 0d;
        String Days = annualLeaveService.workDayBymonth(startDate,endDate,years);
        hours = Double.valueOf(Days)*8;
        return ApiResult.success(hours);
    }
    //ccm 1019 计算一个月出勤多少小时
}
