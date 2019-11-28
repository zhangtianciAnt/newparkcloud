package com.nt.controller.Controller.BASF.BASFLANController;

import com.nt.dao_BASF.DriverInformation;
import com.nt.service_BASF.DriverInformationServices;
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
 * @ClassName: BASF10702Controller
 * @Author: Wxz
 * @Description: BASF10702Controller
 * @Date: 2019/11/22 15:16
 * @Version: 1.0
 */
@RestController
@RequestMapping("/BASF10702")
public class BASF10702Controller {

    @Autowired
    private DriverInformationServices driverInformationServices;

    @Autowired
    private TokenService tokenService;

    /**
     * @param driverInformation
     * @Method list
     * @Author Wxz
     * @Version 1.0
     * @Description 获取驾驶员列表
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/22 15：22
     */
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public ApiResult list(@RequestBody(required = false) DriverInformation driverInformation) throws Exception {
        return ApiResult.success(driverInformationServices.list(driverInformation));
    }

    /**
     * @param driverInformation
     * @Method list
     * @Author Wxz
     * @Version 1.0
     * @Description 查询是否为黑名单
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/28 9:37
     */
    @RequestMapping(value = "/checkblack", method = {RequestMethod.POST})
    public ApiResult checkblack(@RequestBody DriverInformation driverInformation) throws Exception {
        return ApiResult.success(driverInformationServices.checkblack(driverInformation));
    }

    /**
     * @param driverInformation
     * @param request
     * @Method update
     * @Author Wxz
     * @Version 1.0
     * @Description 更新驾驶员信息
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/22 15：25
     */
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody DriverInformation driverInformation, HttpServletRequest request) throws Exception {
        if (driverInformation == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        driverInformationServices.update(driverInformation, tokenModel);
        return ApiResult.success();
    }
}
