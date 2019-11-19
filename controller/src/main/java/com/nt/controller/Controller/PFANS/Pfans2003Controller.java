package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS2000.InterviewRecord;
import com.nt.service_pfans.PFANS2000.InterviewRecordService;
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
@RequestMapping("/interviewrecord")
public class Pfans2003Controller {

    @Autowired
    private InterviewRecordService interviewrecordService;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    public ApiResult getInterviewRecord(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        InterviewRecord interviewrecord = new InterviewRecord();
        interviewrecord.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(interviewrecordService.getInterviewRecord(interviewrecord));
    }

    @RequestMapping(value="/one",method={RequestMethod.POST})
    public ApiResult  one(@RequestBody InterviewRecord interviewrecord,HttpServletRequest request)throws Exception{
        if(interviewrecord == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03,RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(interviewrecordService.One(interviewrecord.getInterviewrecord_id()));
    }

    @RequestMapping(value = "/update",method = {RequestMethod.POST})
    public ApiResult updateInterviewRecord(@RequestBody InterviewRecord interviewrecord, HttpServletRequest request)throws Exception{
        if (interviewrecord == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        interviewrecordService.updateInterviewRecord(interviewrecord,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/create",method={RequestMethod.POST})
    public ApiResult create(@RequestBody InterviewRecord interviewrecord, HttpServletRequest request) throws Exception {
        if (interviewrecord == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        interviewrecordService.insert(interviewrecord,tokenModel);
        return ApiResult.success();
    }
}