package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS2000.PunchcardRecord;
import com.nt.service_pfans.PFANS2000.PunchcardRecordService;
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

    @Autowired
    private PunchcardRecordService punchcardrecordService;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public ApiResult list(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        PunchcardRecord punchcardrecord = new PunchcardRecord();
        punchcardrecord.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(punchcardrecordService.list(punchcardrecord));
    }

}
