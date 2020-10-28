package com.nt.controller.Controller.BASF.BASFLANController;

import com.nt.dao_BASF.Existtrainarchives;
import com.nt.service_BASF.ExisttrainarchivesServices;
import com.nt.utils.*;
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
 * @ClassName: BASF21213Controller
 * @Author: mayuntao
 * @Description: 既有培训档案导入Controller
 * @Date: 2020/10/22 17:41
 * @Version: 1.0
 */
@RestController
@RequestMapping("/BASF21213")
public class BASF21213Controller {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ExisttrainarchivesServices existtrainarchivesServices;

    // 获取全部既有的培训档案列表
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public ApiResult list(HttpServletRequest request) throws Exception {
        return ApiResult.success(existtrainarchivesServices.getAllList());
    }

    /**
     * @param existtrainarchives
     * @param request
     * @Method update
     * @Author
     * @Version 1.0
     * @Description 更新培训清单
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/25 14：20
     */
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody Existtrainarchives existtrainarchives, HttpServletRequest request) throws Exception {
        if (existtrainarchives == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        existtrainarchivesServices.update(existtrainarchives, tokenModel);
        return ApiResult.success();
    }

    /**
     * @param id
     * @param request
     * @Method delete
     * @Author
     * @Version 1.0
     * @Description 删除培训清单
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/25 14：20
     */
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public ApiResult delete(String id, HttpServletRequest request) throws Exception {
        if (StringUtils.isEmpty(id)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        existtrainarchivesServices.delete(id, tokenModel);
        return ApiResult.success();
    }

    /**
     * @param id
     * @param request
     * @Method selectById
     * @Author
     * @Version 1.0
     * @Description 删除培训清单
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/25 14：20
     */
    @RequestMapping(value = "/selectById", method = {RequestMethod.GET})
    public ApiResult selectById(String id, HttpServletRequest request) throws Exception {
        if (StringUtils.isEmpty(id)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(existtrainarchivesServices.getExisttrainarchivesInfoById(id));
    }

    /**
     * @ProjectName: BASF应急平台
     * @Package: com.nt.controller.Controller.BASF.BASFLANController
     * @ClassName: BASF21207
     * @Author:
     * @Description: EXECL导入
     * @Date: 2019/12/15
     * @Version: 1.0
     */
    @RequestMapping(value = "/importExisttrainarchives", method = {RequestMethod.POST})
    public ApiResult importExisttrainarchives(HttpServletRequest request) {
        try {
            TokenModel tokenModel = tokenService.getToken(request);
            return ApiResult.success(existtrainarchivesServices.insert(request, tokenModel));
        } catch (LogicalException e) {
            return ApiResult.fail(e.getMessage());
        } catch (Exception e) {
            return ApiResult.fail("操作失败！");
        }
    }
}
