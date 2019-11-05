package com.nt.controller.Controller.BASF.BASFLANController; 

import com.nt.dao_BASF.Usergroup;
import com.nt.service_BASF.BASF10103Services;
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
 * @ClassName: BASF10103Controller
 * @Author: SUN
 * @Description: BASF用户组Controller
 * @Date: 2019/11/4 17:22
 * @Version: 1.0
 */
@RestController
@RequestMapping("/BASF10103")
public class BASF10103Controller {

    @Autowired
    private BASF10103Services basf10103Services;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public ApiResult list(HttpServletRequest request) throws Exception {
        Usergroup usergroup = new Usergroup();
        usergroup.setStatus(AuthConstants.DEL_FLAG_NORMAL);
        return ApiResult.success(basf10103Services.list(usergroup));
    }
}
