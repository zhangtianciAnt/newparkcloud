package com.nt.controller.Controller.BASF.BASFLANController;

import com.nt.dao_BASF.DutySimt;
import com.nt.service_BASF.DutySimtServices;
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
 * @ClassName: DutySimtController
 * @Author: WXZ
 * @Description: DutySimtController
 * @Date: 2019/12/19 15:22
 * @Version: 1.0
 */
@RestController
@RequestMapping("/BASF10205")
public class BASF10205Controller {

    @Autowired
    private DutySimtServices dutySimtServices;

    @Autowired
    private TokenService tokenService;

    /**
     * @param request
     * @Method list
     * @Author Wxz
     * @Version 1.0
     * @Description 获取值班人列表
     * @Return com.nt.utils.ApiResult
     * @Date 2019/12/19 15:24
     */
    @RequestMapping(value = "/list",method = {RequestMethod.POST})
    public ApiResult list(HttpServletRequest request)throws Exception{
        return ApiResult.success(dutySimtServices.list());
    }

    /**
     * @param dutySimt
     * @param request
     * @Method update
     * @Author Wxz
     * @Version 1.0
     * @Description 更新值班人
     * @Return com.nt.utils.ApiResult
     * @Date 2019/12/19 15:30
     */
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody DutySimt dutySimt , HttpServletRequest request) throws Exception {
        if (dutySimt == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        dutySimtServices.update(dutySimt,tokenModel);
        return ApiResult.success();
    }
}
