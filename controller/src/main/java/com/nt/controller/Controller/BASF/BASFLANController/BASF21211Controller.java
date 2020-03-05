package com.nt.controller.Controller.BASF.BASFLANController;

import com.nt.dao_BASF.Trainjoinlist;
import com.nt.service_BASF.TrainjoinlistServices;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.RequestUtils;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.controller.Controller.BASF.BASFLANController
 * @ClassName: BASF21211Controller
 * @Author: 王哲
 * @Description: 在线培训
 * @Date: 2020/2/17 20:15
 * @Version: 1.0
 */
@RestController
@RequestMapping("/BASF21211")
public class BASF21211Controller {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private TrainjoinlistServices trainjoinlistServices;

    @PostMapping("/upTrain")
    public ApiResult upTrain(@RequestBody Trainjoinlist trainjoinlist, HttpServletRequest request) throws Exception {
        if (trainjoinlist == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        trainjoinlistServices.updata(trainjoinlist, tokenModel);
        return ApiResult.success();
    }
}
