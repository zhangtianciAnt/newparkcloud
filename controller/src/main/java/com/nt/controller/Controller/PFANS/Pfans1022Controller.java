package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS1000.Holiday;
import com.nt.dao_Pfans.PFANS1000.Vo.HolidayVo;
import com.nt.service_pfans.PFANS1000.HolidayService;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.RequestUtils;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/holiday")
public class Pfans1022Controller {

    @Autowired
    private HolidayService holidayService;
    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    public ApiResult get(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        Holiday holiday =new Holiday();
        holiday.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(holidayService.getHoliday(holiday));
    }

    @RequestMapping(value = "insert", method = {RequestMethod.POST})
    public ApiResult insert(@RequestBody HolidayVo holidayVo, HttpServletRequest request) throws Exception {
        if (holidayVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        holidayService.insert(holidayVo, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/selectById", method = {RequestMethod.GET})
    public ApiResult selectById(String holidayid, HttpServletRequest request) throws Exception {
        if (holidayid == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(holidayService.selectById(holidayid));
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody HolidayVo holidayVo, HttpServletRequest request) throws Exception {
        if (holidayVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        holidayService.updateHoliday(holidayVo, tokenModel);
        return ApiResult.success();

    }
}
