package com.nt.controller.Controller.BASF.BASFLANController;

import com.nt.dao_BASF.MenuShow;
import com.nt.service_BASF.MenuShowServices;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.controller.Controller.BASF.BASFLANController
 * @ClassName: BASF10208Controller
 * @Author: Wxz
 * @Description: BASF大屏显示状态管理模块Controller
 * @Date: 2020/04/09 16：43
 * @Version: 1.0
 */
@RestController
@RequestMapping("/BASF10208")
public class BASF10208Controller {
    @Autowired
    private MenuShowServices menuShowServices;
    @Autowired
    private TokenService tokenService;

    /**
     * @param request
     * @Method list
     * @Author Wxz
     * @Version 1.0
     * @Description 获取大屏分类状态列表
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/13 16：33
     */
    @RequestMapping(value = "/list",method = {RequestMethod.POST})
    public ApiResult list(HttpServletRequest request)throws Exception{
        return ApiResult.success(menuShowServices.list());
    }

    /**
     * @param menuShow
     * @param request
     * @Method update
     * @Author Wxz
     * @Version 1.0
     * @Description 更新大屏分类状态详情
     * @Return com.nt.utils.ApiResult
     * @Date 2020/04/10 12:17
     */
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody MenuShow menuShow , HttpServletRequest request) throws Exception {
        if (menuShow == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        menuShowServices.update(menuShow,tokenModel);
        return ApiResult.success();
    }

    /**
     * @param menuShow
     * @param request
     * @Method del
     * @Author Wxz
     * @Version 1.0
     * @Description 删除大屏分类状态详情
     * @Return com.nt.utils.ApiResult
     * @Date 2020/04/10 12:17
     */
    @RequestMapping(value = "/del", method = {RequestMethod.POST})
    public ApiResult del(@RequestBody MenuShow menuShow, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        menuShow.setStatus(AuthConstants.DEL_FLAG_DELETE);
        menuShowServices.del(menuShow,tokenModel);
        return ApiResult.success();
    }

    /**
     * @param menuShowList
     * @param request
     * @Method insert
     * @Author Wxz
     * @Version 1.0
     * @Description 插入大屏分类状态详情
     * @Return com.nt.utils.ApiResult
     * @Date 2020/04/10 12:25
     */
    @RequestMapping(value = "/insert", method = {RequestMethod.POST})
    public ApiResult insert(@RequestBody ArrayList<MenuShow> menuShowList, HttpServletRequest request) throws Exception {

        TokenModel tokenModel = tokenService.getToken(request);
        menuShowServices.insert(menuShowList, tokenModel);
        return ApiResult.success();
    }

}
