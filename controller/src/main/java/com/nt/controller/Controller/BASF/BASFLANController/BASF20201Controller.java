package com.nt.controller.Controller.BASF.BASFLANController;


import cn.hutool.core.util.StrUtil;
import com.nt.dao_BASF.Fireaccidentrecord;
import com.nt.dao_BASF.Commandrecord;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.Information;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.RequestUtils;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.nt.service_BASF.CommandrecordServices;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.controller.Controller.BASF.BASFLANController
 * @ClassName: BASF20201Controller
 * @Author: SUN
 * @Description: BASFERC消防接警页面Controller
 * @Date: 2019/11/11 17:22
 * @Version: 1.0
 */
@RestController
@RequestMapping("/BASF20201")
public class BASF20201Controller {

    @Autowired
    private CommandrecordServices commandrecordService;

    @Autowired
    private TokenService tokenService;


    @RequestMapping(value = "/getcommandrecord", method = {RequestMethod.GET})
    public ApiResult get(String cid, HttpServletRequest request) throws Exception {
        if (StrUtil.isEmpty(cid)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(commandrecordService.get(cid));
    }

    /**
     * @方法名：saveinformation
     * @描述：信息发布保存
     * @创建日期：2018/12/13
     * @作者：SUNXU
     * @参数：[information, request]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/savecommandrecord", method = {RequestMethod.POST})
    public ApiResult save(@RequestBody Commandrecord commandrecord, HttpServletRequest request) throws Exception {
        if (commandrecord == null || StringUtils.isEmpty(commandrecord)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03,RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);

        if (commandrecord.getStatus() == null) {
            commandrecord.preInsert(tokenModel);
        } else {
            if (commandrecord.getStatus().equals("1")) {
                commandrecord.preUpdate(tokenModel);
            }
            if (commandrecord.getStatus().equals("0")) {
                commandrecord.preInsert(tokenModel);
            }
        }
        return ApiResult.success(commandrecordService.save(commandrecord, tokenModel));
    }
}
