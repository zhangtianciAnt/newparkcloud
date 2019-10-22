package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS2000.PunchCardRecord;
import com.nt.service_pfans.PFANS2000.PunchCardRecordService;
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
@RequestMapping("/punchcardrecord")
public class Pfans2017Controller {
    //查找
    @Autowired
    private PunchCardRecordService punchcardrecordService;
    @Autowired
    private TokenService tokenService;

    @RequestMapping(value="/get",method = {RequestMethod.GET})
    public ApiResult getOvertime(HttpServletRequest request) throws Exception{
        TokenModel tokenModel = tokenService.getToken(request);
        PunchCardRecord punchcardrecord = new PunchCardRecord();
        punchcardrecord.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(punchcardrecordService.getPunchCardRecord(punchcardrecord));
    }



    @RequestMapping(value ="/one",method = { RequestMethod.POST} )
    public ApiResult one(@RequestBody PunchCardRecord punchcardrecord, HttpServletRequest request) throws Exception{
        if (punchcardrecord == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
//        return ApiResult.success(punchcardrecordService.One(punchcardrecord.getPunchCardRecordid()));
        return ApiResult.success();
    }

    @RequestMapping(value="/create",method = {RequestMethod.POST})
    public ApiResult create(@RequestBody PunchCardRecord punchcardrecord, HttpServletRequest request) throws Exception{
        if (punchcardrecord == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        punchcardrecordService.insertPunchCardRecord(punchcardrecord,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value="/update",method = {RequestMethod.POST})
    public ApiResult updatePunchCardRecord(@RequestBody PunchCardRecord punchcardrecord, HttpServletRequest request) throws Exception{
        if (punchcardrecord == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        punchcardrecordService.updatePunchCardRecord(punchcardrecord,tokenModel);
        return ApiResult.success();
    }

}
