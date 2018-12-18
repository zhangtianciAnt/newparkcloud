package com.nt.controller.Controller;




import com.nt.dao_Org.Information;
import com.nt.service_Org.InformationService;
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
import java.util.Date;

@RestController
@RequestMapping("/information")
public class InformationController {

    @Autowired
    private InformationService informationService;

    @Autowired
    private TokenService tokenService;


    /**
     * @方法名：saveinformation
     * @描述：信息发布保存
     * @创建日期：2018/12/13
     * @作者：SUNXU
     * @参数：[information, request]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/saveinformation", method = {RequestMethod.POST})
    public ApiResult save(@RequestBody Information information, HttpServletRequest request) throws Exception {
        if (information == null || StringUtils.isEmpty(information)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.PARAM_ERR_02));
        }
        if(information.getStatus()==null)
        {
            TokenModel tokenModel = tokenService.getToken(request);
            information.preInsert(tokenModel);
            information.setReleaseperson(tokenModel.getUserId());
            information.setReleasetime(new Date());
            informationService.save(information,tokenModel);
        }
        else
        {
            if(information.getStatus().equals("1"))
            {
                TokenModel tokenModel = tokenService.getToken(request);
                information.preUpdate(tokenModel);
                information.setReleaseperson(tokenModel.getUserId());
                information.setReleasetime(new Date());
                informationService.save(information,tokenModel);
            }
            if(information.getStatus().equals("0"))
            {
                TokenModel tokenModel = tokenService.getToken(request);
                information.preInsert(tokenModel);
                information.setReleaseperson(tokenModel.getUserId());
                information.setReleasetime(new Date());
                informationService.save(information,tokenModel);
            }
        }
        return ApiResult.success();
    }

    /**
     * @方法名：getinformation
     * @描述：获取信息发布列表
     * @创建日期：2018/12/13
     * @作者：SUNXU
     * @参数：[request]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/getinformation", method = {RequestMethod.GET})
    public ApiResult get(HttpServletRequest request) throws Exception {
        Information information = new Information();
        TokenModel tokenModel = tokenService.getToken(request);
        information.setTenantid(tokenModel.getTenantId());
        information.setOwners(tokenModel.getOwnerList());
        information.setIds(tokenModel.getIdList());
        return ApiResult.success(informationService.get(information));
    }

    /**
     * @方法名：importexcel
     * @描述：导出excel到本地固定位置
     * @创建日期：2018/12/13
     * @作者：SUNXU
     * @参数：[id,request]
     * @返回值：
     */
    @RequestMapping(value = "/importexcel", method = {RequestMethod.GET})
    public ApiResult importexcel(String id,HttpServletRequest request) throws Exception {
        Information information = new Information();
        TokenModel tokenModel = tokenService.getToken(request);
        information.setTenantid(tokenModel.getTenantId());
        information.setOwners(tokenModel.getOwnerList());
        information.setIds(tokenModel.getIdList());
        informationService.importexcel(id,request);
        return ApiResult.success();
    }
    /**
     * @方法名：getcustomerinfo
     * @描述：查询customerinfo
     * @创建日期：2018/12/13
     * @作者：SUNXU
     * @参数：[request]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/getcustomerinfo", method = {RequestMethod.GET})
    public ApiResult getcustomerinfo(HttpServletRequest request) throws Exception {
        return ApiResult.success(informationService.getcustomerinfo());
    }

    /**
     * @方法名：getInfoByType
     * @描述：根据type获取发布信息
     * @创建日期：2018/12/11
     * @作者：ZHANGYING
     * @参数：[type, request]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/getInfoByType", method = {RequestMethod.POST})
    public ApiResult getInfoByType(@RequestBody Information information, HttpServletRequest request) throws Exception {
        if(StringUtils.isEmpty(information.getType())){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.PARAM_ERR_02));
        }
        return ApiResult.success(informationService.getInfoByType(information));
    }

    /**
     * @方法名：getInfoById
     * @描述：根据id获取发布信息
     * @创建日期：2018/12/11
     * @作者：ZHANGYING
     * @参数：[id, request]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/getInfoById", method = {RequestMethod.GET})
    public ApiResult getInfoById(String id, HttpServletRequest request) throws Exception {
        Information information = new Information();
        return ApiResult.success(informationService.getInfoById(id));
    }

    /**
     * @方法名：addActivity
     * @描述：报名成功添加用户信息
     * @创建日期：2018/12/11
     * @作者：ZHANGYING
     * @参数：[information, request]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/addActivity", method = {RequestMethod.POST})
    public ApiResult addActivity(@RequestBody Information information,String id, HttpServletRequest request) throws Exception {
        informationService.addActivity(information, id);
        return ApiResult.success();
    }
}
