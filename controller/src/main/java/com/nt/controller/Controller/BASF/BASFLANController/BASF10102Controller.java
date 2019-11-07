package com.nt.controller.Controller.BASF.BASFLANController;

import com.nt.dao_BASF.BASFUser;
import com.nt.service_BASF.BASFUserService;
import com.nt.utils.ApiResult;
import com.nt.utils.AuthConstants;
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
 * @ClassName: BASF10102Controller
 * @Author: LXY
 * @Description: BASF用户通讯录Controller
 * @Date: 2019/11/6 22.40
 * @Version: 1.0
 */
@RestController
@RequestMapping("/BASF10102")
public class BASF10102Controller {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private BASFUserService userService;

    @RequestMapping(value = "/get", method = {RequestMethod.POST})
    public ApiResult get(@RequestBody BASFUser user) throws Exception {
        return ApiResult.success(userService.get(user));
    }

    @RequestMapping(value = "/insert", method = {RequestMethod.POST})
    public ApiResult insert(@RequestBody BASFUser user, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        userService.insert(tokenModel, user);
        return ApiResult.success();
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody BASFUser user, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        userService.update(tokenModel, user);
        return ApiResult.success();
    }

    @RequestMapping(value = "/del", method = {RequestMethod.POST})
    public ApiResult del(@RequestBody BASFUser user, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        user.setStatus(AuthConstants.DEL_FLAG_DELETE);
        userService.del(tokenModel, user);
        return ApiResult.success();
    }
}
