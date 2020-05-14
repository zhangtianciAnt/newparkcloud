package com.nt.controller.Controller;
import com.nt.dao_Org.CustomerInfo;
import com.nt.service_Org.PersonalCenterVoService;
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

import javax.servlet.http.HttpServletRequest;
@RestController
@RequestMapping("/PersonalCenter")
public class PersonalCenterVoController {
    @Autowired
    private PersonalCenterVoService personalCenterService;
    @Autowired
    private TokenService tokenService;
    /**
     * @方法名：get
     * @描述：获取基本信息
     * @创建日期：2018/11/05
     * @作者：FEIJIALIANG
     * @参数：[request]
     * @返回值：ApiResult.success
     */
    //获取基本信息
    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    public ApiResult get(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(personalCenterService.get(tokenModel.getUserId()));
    }

    /**
     * @方法名：get
     * @描述：根据id获取基本信息
     * @创建日期：2018/11/05
     * @作者：FEIJIALIANG
     * @参数：[request]
     * @返回值：ApiResult.success
     */
    //获取基本信息
    @RequestMapping(value = "/getuserinfo", method = {RequestMethod.GET})
    public ApiResult getuserinfo(String userid) throws Exception {
        return ApiResult.success(personalCenterService.get(userid));
    }
    /**
     * @方法名：save
     * @描述：更新基本信息
     * @创建日期：2018/11/05
     * @作者：FEIJIALIANG
     * @参数：[customerInfo]
     * @返回值： ApiResult.success()
     */
    //更新基本信息
    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    public ApiResult save(@RequestBody CustomerInfo customerInfo, HttpServletRequest request) throws Exception {
        if (customerInfo == null || StringUtils.isEmpty(customerInfo)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        if(customerInfo.getCreateby() == null || customerInfo.getCreateon() == null){
            customerInfo.preInsert(tokenModel);
        }
        customerInfo.preUpdate(tokenModel);
        customerInfo.setUserid(tokenModel.getUserId());
        customerInfo.setType("1");
        personalCenterService.save(customerInfo);
        return ApiResult.success();
    }
}
