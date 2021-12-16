package com.nt.controller.Controller.PFANS;

import com.mysql.jdbc.StringUtils;
import com.nt.dao_Pfans.PFANS6000.Coststatistics;
import com.nt.dao_Pfans.PFANS6000.EntrustSupport;
import com.nt.service_pfans.PFANS6000.EntrustSupportService;
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
import java.util.List;

@RestController
@RequestMapping("/ES")
public class Pfans6012Controller {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private EntrustSupportService entrustSupportService;

    @RequestMapping(value = "/getList", method = {RequestMethod.POST})
    public ApiResult getList(@RequestBody EntrustSupport entrustSupport, HttpServletRequest request) throws Exception {
        if (entrustSupport == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(entrustSupportService.getList(entrustSupport));
    }

    @RequestMapping(value = "/updList", method = {RequestMethod.POST})
    public ApiResult updList(@RequestBody List<EntrustSupport> entrustSupport, HttpServletRequest request) throws Exception {
        if (entrustSupport == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        entrustSupportService.updList(entrustSupport,tokenModel);
        return ApiResult.success();
    }

}
