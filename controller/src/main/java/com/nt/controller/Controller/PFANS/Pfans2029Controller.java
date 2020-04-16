package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS2000.PunchcardRecord;
import com.nt.dao_Pfans.PFANS2000.Attendancebp;
import com.nt.dao_Pfans.PFANS2000.PunchcardRecordDetail;
import com.nt.dao_Pfans.PFANS2000.PunchcardRecordDetailbp;
import com.nt.dao_Pfans.PFANS2000.PunchcardRecordbp;
import com.nt.service_pfans.PFANS2000.PunchcardRecordService;
import com.nt.service_pfans.PFANS2000.PunchcardRecordbpService;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/punchcardrecordbp")
public class Pfans2029Controller {

    @Autowired
    private PunchcardRecordbpService punchcardrecordService;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public ApiResult list(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        PunchcardRecordbp punchcardrecord = new PunchcardRecordbp();
        punchcardrecord.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(punchcardrecordService.list(punchcardrecord,tokenModel));
    }
    //add-ws-外协人员表查询
    @RequestMapping(value = "/getAttendancelist", method = {RequestMethod.POST})
    public ApiResult getAttendancelist(@RequestBody Attendancebp attendancebp, HttpServletRequest request) throws Exception {
        if (attendancebp == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        attendancebp.setStatus(AuthConstants.DEL_FLAG_NORMAL);
        return ApiResult.success(punchcardrecordService.getAttendancebplist(attendancebp));
    }
    //add-ws-外协人员表查询
    @RequestMapping(value = "/list1", method = {RequestMethod.POST})
    public ApiResult list1(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        PunchcardRecordbp punchcardrecord = new PunchcardRecordbp();
        punchcardrecord.setOwner(tokenModel.getUserId());
        return ApiResult.success(punchcardrecordService.list(punchcardrecord,tokenModel));
    }

    @RequestMapping(value = "/getPunDetailbp", method = {RequestMethod.POST})
    public ApiResult getPunDetailbp(@RequestBody PunchcardRecordDetailbp detail, HttpServletRequest request) throws Exception {
        return ApiResult.success(punchcardrecordService.getPunDetailbp(detail));
    }
}
