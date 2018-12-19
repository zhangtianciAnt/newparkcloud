package com.nt.controller.Controller;


import com.nt.dao_Org.ServiceCategory;
import com.nt.service_Org.ServiceCategoryService;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
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
@RequestMapping("/ServiceCategory")
public class ServiceCategoryController {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private ServiceCategoryService servicecategoryService;


    /**
     * @方法名：saveservicecategory
     * @描述：服务类目保存
     * @创建日期：2018/12/15
     * @作者：SUNXU
     * @参数：[servicecategory, request]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/saveservicecategory", method = {RequestMethod.POST})
    public ApiResult saveservicecategory(@RequestBody ServiceCategory servicecategory, HttpServletRequest request) throws Exception {
        if (servicecategory == null || StringUtils.isEmpty(servicecategory)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.PARAM_ERR_02));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        if(servicecategory.getStatus()==null)
        {

            servicecategory.preInsert(tokenModel);
            servicecategoryService.save(servicecategory);
        }
        else
        {
            servicecategory.preUpdate(tokenModel);
            servicecategoryService.save(servicecategory);
        }
        return ApiResult.success();
    }

    /**
     * @方法名：getservicecategory
     * @描述：获取服务类目
     * @创建日期：2018/12/15
     * @作者：SUNXU
     * @参数：[request]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/getservicecategory", method = {RequestMethod.GET})
    public ApiResult getservicecategory(HttpServletRequest request) throws Exception {
        ServiceCategory servicecategory = new ServiceCategory();
        TokenModel tokenModel = tokenService.getToken(request);
        servicecategory.setTenantid(tokenModel.getTenantId());
        servicecategory.setOwners(tokenModel.getOwnerList());
        servicecategory.setIds(tokenModel.getIdList());
        return ApiResult.success(servicecategoryService.get(servicecategory));
    }

    /**
     * @方法名：getwxservicecategory
     * @描述：获取服务类目
     * @创建日期：2018/12/19
     * @作者：SUNXU
     * @参数：[request]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/getwxservicecategory", method = {RequestMethod.GET})
    public ApiResult getwxservicecategory(HttpServletRequest request) throws Exception {
        ServiceCategory servicecategory = new ServiceCategory();
        return ApiResult.success(servicecategoryService.get(servicecategory));
    }
}
