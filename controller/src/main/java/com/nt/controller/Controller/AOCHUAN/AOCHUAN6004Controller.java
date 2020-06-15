package com.nt.controller.Controller.AOCHUAN;

import com.alibaba.fastjson.JSONObject;
import com.mysql.fabric.xmlrpc.base.Data;
import com.nt.dao_AOCHUAN.AOCHUAN6000.Attendance;
import com.nt.dao_Org.CustomerInfo;
import com.nt.service_AOCHUAN.AOCHUAN6000.AttendancesService;
import com.nt.service_Org.EWechatService;
import com.nt.utils.*;
import com.nt.utils.dao.EWeixinOauth2Token;
import com.nt.utils.dao.EWxBaseResponse;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/attendance")
@EnableScheduling
public class AOCHUAN6004Controller {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AttendancesService attendanceService;

    @Autowired
    private EWechatService ewechatService;

    @RequestMapping(value = "/getList", method = {RequestMethod.GET})
    public ApiResult getList(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        Attendance attendance = new Attendance();
        attendance.setOwners(tokenModel.getOwnerList());
//        attendance.setNames(id);
//        fixedassets.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(attendanceService.get(attendance));

    }

    @RequestMapping(value = "/getYICHANG", method = {RequestMethod.GET})
    public ApiResult getYICHANG(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        Attendance attendance = new Attendance();
        attendance.setOwners(tokenModel.getOwnerList());
//        attendance.setNames(id);
//        fixedassets.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(attendanceService.getYICHANG(attendance));

    }

//    @RequestMapping(value = "/getNowday",method={RequestMethod.GET})
//    public ApiResult one(@RequestParam String attendancetim, HttpServletRequest request) throws Exception {
//        if (attendancetim == null) {
//            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
//        }
//        TokenModel tokenModel = tokenService.getToken(request);
//        Attendance attendance = new Attendance();
//        attendance.setAttendancetim(attendancetim);
//
//        return ApiResult.success(attendanceService.getNow(attendance));
//    }

    @RequestMapping(value = "/getNowMon", method = {RequestMethod.GET})
    public ApiResult getNowMon(@RequestParam String nowmons, @RequestParam String id, HttpServletRequest request) throws Exception {
        if (nowmons == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);

        return ApiResult.success(attendanceService.getNowMons(id, nowmons));
    }

    @RequestMapping(value = "/getNowMonYC", method = {RequestMethod.GET})
    public ApiResult getNowMonYC(@RequestParam String nowmons, @RequestParam String id, HttpServletRequest request) throws Exception {
        if (nowmons == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);

        return ApiResult.success(attendanceService.getNowMonYC(id, nowmons));
    }


    @RequestMapping(value = "/importUser", method = {RequestMethod.POST})
    public ApiResult importUser(HttpServletRequest request, String flag) {
        try {
            TokenModel tokenModel = tokenService.getToken(request);
            return ApiResult.success(attendanceService.importUser(request, tokenModel));
        } catch (LogicalException e) {
            return ApiResult.fail(e.getMessage());
        } catch (Exception e) {
            return ApiResult.fail("操作失败！");
        }
    }

    @RequestMapping(value = "/getByUserId", method = {RequestMethod.GET})
    public ApiResult getByUserId(@RequestParam String id, HttpServletRequest request) throws Exception {
        if (!StringUtils.isNotBlank(id)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(attendanceService.getByUserId(id));
    }

    //自动获取企业微信打卡记录
//    @Scheduled( cron="* * 21 * * ?")
    @RequestMapping(value = "/getautocheckindata", method = {RequestMethod.GET})
    public ApiResult getAutoCheckInData(String code) throws Exception {

        try {
            String corpid = "ww52676ba41e44fb89";
            String corpSecret = "CP6colO-9OR5HRGZt7IAEzN5xYQH09H7yiuNvMextNE";
            EWeixinOauth2Token eweixinOauth = EWxUserApi.getWeChatOauth2Token(corpid, corpSecret);
            if (eweixinOauth != null && eweixinOauth.getErrcode() == 0) {
                String token = eweixinOauth.getAccess_token();
                String access_token = ewechatService.ewxLogin(token);
                List<CustomerInfo> userIdList = ewechatService.useridList();
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String strDateFormat = dateFormat.format(date);
                String startStrDateFormat = strDateFormat + " " + "06:00:01";
                String endStrDateFormat = strDateFormat + " " + "23:59:59";
                Date startDatDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startStrDateFormat);
                Date endDatDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endStrDateFormat);
                long startTime = startDatDateFormat.getTime() / 1000;
                long endTime = endDatDateFormat.getTime() / 1000;
                String[] strList = new String[userIdList.size()];
                for (int i = 0; i < userIdList.size(); i++) {
                    strList[i] = userIdList.get(i).getUserinfo().getEwechatid();
                }
                EWxBaseResponse jsob = new EWxBaseResponse();
                jsob = EWxUserApi.inData(access_token, 1, startTime, endTime, strList);
                attendanceService.getCheckInData(jsob);
                return ApiResult.success("dasdasdsa");
//              return ApiResult.success(ewechatService.ewxLogin(access_token));
            } else {
                return ApiResult.fail("获取企业微信access_token失败");
            }
        } catch (LogicalException e) {
            return ApiResult.fail(e.getMessage());
        }
    }


}
