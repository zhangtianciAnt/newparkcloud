package com.nt.controller.Controller.BASF.BASFLANController;

import com.nt.dao_BASF.Visitorstraining;
import com.nt.service_BASF.VisitorstrainingServices;
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

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.controller.Controller.BASF.BASFLANController
 * @ClassName: BASF21205Controller
 * @Author: 王哲
 * @Description: 访客培训记录
 * @Date: 2019/11/25 10:39
 * @Version: 1.0
 */
@RestController
@RequestMapping("/BASF21205")
public class BASF21205Controller {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private VisitorstrainingServices visitorstrainingServices;

    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public ApiResult get(HttpServletRequest request) throws Exception {
        return ApiResult.success(visitorstrainingServices.list());
    }

    @RequestMapping(value = "/insert", method = {RequestMethod.POST})
    public ApiResult create(@RequestBody Visitorstraining visitorstraining, HttpServletRequest request) throws Exception {
        if (visitorstraining == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        visitorstrainingServices.insert(visitorstraining, tokenModel);
        return ApiResult.success();
    }


}
