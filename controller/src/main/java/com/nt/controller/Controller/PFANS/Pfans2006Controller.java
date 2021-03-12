package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS2000.Bonussend;
import com.nt.dao_Pfans.PFANS2000.Vo.BaseVo;
import com.nt.dao_Pfans.PFANS2000.Wages;
import com.nt.service_pfans.PFANS2000.WagesService;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/Getwages")
public class Pfans2006Controller {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private WagesService wagesService;

    @RequestMapping(value = "/getTaxestotalList", method = {RequestMethod.GET})
    // update gbb 20210312 NT_PFANS_20210308_BUG_168 工资详细（个人）,根据日期组件筛选 start
    //public ApiResult getList(String userid,HttpServletRequest request) throws Exception {
    public ApiResult getList(String dates,String userid,HttpServletRequest request) throws Exception {
//        TokenModel tokenModel = tokenService.getToken(request);
//        wagesService.select(tokenModel);
        Wages wages=new Wages();
        if(StringUtils.isNotEmpty(userid)){
            wages.setUser_id(userid);
        }
        if(StringUtils.isNotEmpty(dates)){
            wages.setCreateonym(dates);
        }
        // update gbb 20210312 NT_PFANS_20210308_BUG_168 工资详细（个人）,根据日期组件筛选 end
        return ApiResult.success(wagesService.wagesList(wages));
    }

    @RequestMapping(value = "/getwagesList", method = {RequestMethod.GET})
    public ApiResult getwagesList(@RequestParam String dates, HttpServletRequest request) throws Exception {
        if (dates == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(wagesService.selectBase(dates));
    }

    @RequestMapping(value = "/getBonusList", method = {RequestMethod.GET})
    // update gbb 20210312 NT_PFANS_20210308_BUG_166 工资详细（全社）,奖金详细数据根据日期组件筛选 start
    //public ApiResult BonusList(String userid,HttpServletRequest request) throws Exception {
    public ApiResult BonusList(String dates,String userid,HttpServletRequest request) throws Exception {
        Bonussend bonussend =new Bonussend();
        //bonussend.setUser_id(userid);
        if(StringUtils.isNotEmpty(userid)){
            bonussend.setUser_id(userid);
        }
        if(StringUtils.isNotEmpty(dates)){
            bonussend.setYears(dates.substring(0,4));
        }
        // update gbb 20210312 NT_PFANS_20210308_BUG_166 工资详细（全社）,奖金详细数据根据日期组件筛选 end
        return ApiResult.success(wagesService.bonusList(bonussend));
    }


}
