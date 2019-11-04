package com.nt.controller.Controller.BASF.BASFLANController;

import com.nt.dao_BASF.Deviceinformation;
import com.nt.service_BASF.BASF10105Services;
import com.nt.utils.ApiResult;
import com.nt.utils.AuthConstants;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.controller.Controller.BASF.BASFLANController
 * @ClassName: BASF10105Controller
 * @Author: SKAIXX
 * @Description: BASF设备管理模块Controller
 * @Date: 2019/11/4 16:09
 * @Version: 1.0
 */
@RestController
@RequestMapping("/BASF10105")
public class BASF10105Controller {

    @Autowired
    private BASF10105Services basf10105Services;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public ApiResult list(HttpServletRequest request) throws Exception {
        Deviceinformation deviceinformation = new Deviceinformation();
        deviceinformation.setStatus(AuthConstants.DEL_FLAG_NORMAL);
        return ApiResult.success(basf10105Services.list(deviceinformation));
    }
}
