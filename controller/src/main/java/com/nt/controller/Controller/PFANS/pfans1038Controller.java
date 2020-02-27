package com.nt.controller.Controller.PFANS;


import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Pfans.PFANS1000.Businessplan;
import com.nt.dao_Pfans.PFANS1000.PersonnelPlan;
import com.nt.dao_Pfans.PFANS1000.Vo.ExternalVo;
import com.nt.dao_Pfans.PFANS6000.Expatriatesinfor;
import com.nt.dao_Pfans.PFANS6000.Supplierinfor;
import com.nt.service_pfans.PFANS1000.BusinessplanService;
import com.nt.service_pfans.PFANS1000.PersonnelplanService;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.RequestUtils;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/personnelplan")
public class pfans1038Controller {

    @Autowired
    private PersonnelplanService personnelplanService;
    @Autowired
    private TokenService tokenService;

    //获取部门下用户
    @RequestMapping(value = "/getcustomer", method = {RequestMethod.GET})
    public ApiResult get(HttpServletRequest request, @RequestParam String id) throws Exception {
       List<CustomerInfo> customerInfoList =  personnelplanService.SelectCustomer(id);
        return ApiResult.success(customerInfoList);
    }
    //获取所有供应商
    @RequestMapping(value = "/getsupplierinfor", method = {RequestMethod.GET})
    public ApiResult getExternal(HttpServletRequest request) throws Exception {
        List<Supplierinfor> SupplierinforList =  personnelplanService.getExternal();
        return ApiResult.success(SupplierinforList);
    }
   //获取所有外驻人员
   @RequestMapping(value = "/getexpatriatesinfor", method = {RequestMethod.GET})
   public ApiResult getExpatriatesinfor(HttpServletRequest request) throws Exception {
       List<ExternalVo> externalVos =  personnelplanService.getExpatriatesinfor();
       return ApiResult.success(externalVos);
   }
   //获取全部
    @RequestMapping(value = "/getall", method = {RequestMethod.GET})
    public ApiResult getAll(HttpServletRequest request) throws Exception {
        List<PersonnelPlan> personnelPlans = personnelplanService.getAll();
        return ApiResult.success(personnelPlans);
    }
    //获取选中
    @RequestMapping(value = "/getone", method = {RequestMethod.GET})
    public ApiResult getOne(HttpServletRequest request ,@RequestParam String id) throws Exception {
        PersonnelPlan personnelPlan = personnelplanService.getOne(id);
        return ApiResult.success(personnelPlan);
    }
    //更新
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(HttpServletRequest request,@RequestBody PersonnelPlan personnelPlan) throws Exception {
        if (StringUtils.isEmpty(personnelPlan)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        personnelplanService.update(personnelPlan, tokenModel);
        return ApiResult.success();
    }
    //创建
    @RequestMapping(value = "/insert", method = {RequestMethod.POST})
    public ApiResult insert(HttpServletRequest request, @RequestBody PersonnelPlan personnelPlan) throws Exception {
        if (StringUtils.isEmpty(personnelPlan)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        personnelplanService.insert(personnelPlan, tokenModel);
        return ApiResult.success();
    }

}
