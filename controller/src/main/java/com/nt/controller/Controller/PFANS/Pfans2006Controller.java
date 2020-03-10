package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS2000.Bonus;
import com.nt.dao_Pfans.PFANS2000.InterviewRecord;
import com.nt.dao_Pfans.PFANS2000.Wages;
import com.nt.service_pfans.PFANS2000.InterviewRecordService;
import com.nt.service_pfans.PFANS2000.WagesService;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.RequestUtils;
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
    public ApiResult getList(String userid,HttpServletRequest request) throws Exception {
//        TokenModel tokenModel = tokenService.getToken(request);
//        wagesService.select(tokenModel);
        Wages wages=new Wages();
        wages.setUser_id(userid);
        return ApiResult.success(wagesService.wagesList(wages));
    }

    @RequestMapping(value = "/getBonusList", method = {RequestMethod.GET})
    public ApiResult BonusList(String userid,HttpServletRequest request) throws Exception {
        Bonus bonus =new Bonus();
        bonus.setBename(userid);
        return ApiResult.success(wagesService.bonusList(bonus));
    }


}