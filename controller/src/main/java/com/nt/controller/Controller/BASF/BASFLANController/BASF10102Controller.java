package com.nt.controller.Controller.BASF.BASFLANController;


import com.nt.dao_BASF.Usergroup;
import com.nt.dao_BASF.Usergroupdetailed;
import com.nt.dao_BASF.VO.UsergroupVo;
import com.nt.service_BASF.UsergroupServices;
import com.nt.utils.ApiResult;
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
 * @Description: BASF用户组Controller
 * @Date: 2019/11/6 22.31
 * @Version: 1.0
 */
@RestController
@RequestMapping("/BASF10103")
public class BASF10102Controller {

    @Autowired
    private UsergroupServices usergroupServices;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public ApiResult list(@RequestBody Usergroup usergroup ,HttpServletRequest request) throws Exception {
        return ApiResult.success(usergroupServices.list(usergroup));
    }

    @RequestMapping(value = "/getOneUserGroupDetailed", method = {RequestMethod.POST})
    public ApiResult getOneUserGroupDetailed(@RequestBody Usergroupdetailed usergroupdetailed, HttpServletRequest request) throws Exception {

        return ApiResult.success(usergroupServices.getDetailedList(usergroupdetailed));
    }

    @RequestMapping(value = "/insert", method = {RequestMethod.POST})
    public ApiResult insert(@RequestBody UsergroupVo usergroupVo, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        usergroupServices.insert(tokenModel,usergroupVo);
        return ApiResult.success();
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody UsergroupVo usergroupVo, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        usergroupServices.update(tokenModel,usergroupVo);
        return ApiResult.success();
    }
}
