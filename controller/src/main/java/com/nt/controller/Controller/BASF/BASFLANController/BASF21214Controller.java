package com.nt.controller.Controller.BASF.BASFLANController;

import com.nt.dao_BASF.Trainmail;
import com.nt.dao_BASF.VO.TrainmailVo;
import com.nt.service_BASF.TrainmailServices;
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
 * @ClassName: BASF21214Controller
 * @Author: myt
 * @Description: 培训邮件设置Controller
 * @Date: 2020/12/01 17:41
 * @Version: 1.0
 */
@RestController
@RequestMapping("/BASF21214")
public class BASF21214Controller {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private TrainmailServices trainmailServices;

    /**
     * @Method list
     * @Author myt
     * @Version 1.0
     * @Description 获取培训邮件设置内容
     * @Return com.nt.utils.ApiResult
     * @Date 2020/12/01 17:41
     */
    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    public ApiResult list() throws Exception {
        return ApiResult.success(trainmailServices.getAllList());
    }

    /**
     * @Method getone
     * @Author myt
     * @Version 1.0
     * @Description 获取培训邮件设置内容
     * @Return com.nt.utils.ApiResult
     * @Date 2020/12/01 17:41
     */
    @RequestMapping(value = "/getone", method = {RequestMethod.GET})
    public ApiResult getone(String trainmailid) throws Exception {
        TrainmailVo TrainmailVo = trainmailServices.getone(trainmailid);
        return ApiResult.success(trainmailServices.getone(trainmailid));
    }

    /**
     * @param trainmail
     * @param request
     * @Method update
     * @Author myt
     * @Version 1.0
     * @Description 更新培训邮件设置内容
     * @Return com.nt.utils.ApiResult
     * @Date 2020/12/01 17:41
     */
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody Trainmail trainmail, HttpServletRequest request) throws Exception {
        if (trainmail == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        trainmailServices.update(trainmail, tokenModel);
        return ApiResult.success();
    }

    /**
     * @param trainmail
     * @param request
     * @Method insert
     * @Author myt
     * @Version 1.0
     * @Description 新建培训邮件设置内容
     * @Return com.nt.utils.ApiResult
     * @Date 2020/12/01 17:41
     */
    @RequestMapping(value = "/insert", method = {RequestMethod.POST})
    public ApiResult insert(@RequestBody Trainmail trainmail, HttpServletRequest request) throws Exception {
        if (trainmail == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        trainmailServices.insert(trainmail, tokenModel);
        return ApiResult.success();
    }
}
