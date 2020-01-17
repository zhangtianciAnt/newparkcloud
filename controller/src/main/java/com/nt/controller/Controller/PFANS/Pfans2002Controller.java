package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS2000.InterviewRecord;
import com.nt.dao_Pfans.PFANS2000.RecruitJudgement;
import com.nt.service_pfans.PFANS2000.InterviewRecordService;
import com.nt.service_pfans.PFANS2000.RecruitJudgementService;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.RequestUtils;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/recruitjudgement")
public class Pfans2002Controller {
    @Autowired
    private RecruitJudgementService recruitJudgementService;

    @Autowired
    private InterviewRecordService interviewrecordService;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    public ApiResult get(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(recruitJudgementService.get(tokenModel));
    }

    @RequestMapping(value = "/getone", method = {RequestMethod.GET})
    public ApiResult getOne(@RequestParam String id, HttpServletRequest request) throws Exception {
        if (id.isEmpty()) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(recruitJudgementService.getOne(id, tokenModel));
    }


    @RequestMapping(value = "/insert", method = {RequestMethod.POST})
    public ApiResult insert(@RequestBody RecruitJudgement recruitJudgement, HttpServletRequest request) throws Exception {
        if (StringUtils.isEmpty(recruitJudgement)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        recruitJudgementService.insert(recruitJudgement, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody RecruitJudgement recruitJudgement, HttpServletRequest request) throws Exception {
        if (StringUtils.isEmpty(recruitJudgement)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        recruitJudgementService.update(recruitJudgement, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/getNameList", method = {RequestMethod.POST})
    public ApiResult getNameList(@RequestBody InterviewRecord interviewRecord, HttpServletRequest request) throws Exception {
        if (interviewRecord == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(interviewrecordService.getInterviewRecord(interviewRecord));
    }


}
