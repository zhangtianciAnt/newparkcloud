package com.nt.controller.Controller.BASF.BASFLANController;

import cn.hutool.core.util.StrUtil;
import com.nt.dao_BASF.Program;
import com.nt.service_BASF.ProgramServices;
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
 * @ClassName: BASF21203Controller
 * @Author: WXL
 * @Description: BASF培训项目奖管理模块Controller
 * @Date: 2019/11/21 13：56
 * @Version: 1.0
 */
@RestController
@RequestMapping("/BASF21203")
public class BASF21203Controller {

    @Autowired
    private ProgramServices programServices;
    @Autowired
    private TokenService tokenService;

    /**
     * @param request
     * @Method list
     * @Author WXL
     * @Version 1.0
     * @Description 获取培训项目列表
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/21 13：56
     */
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public ApiResult list(HttpServletRequest request) throws Exception {
        return ApiResult.success(programServices.list());
    }

    /**
     * @param program
     * @param request
     * @Method create
     * @Author Wxz
     * @Version 1.0
     * @Description 创建培训项目
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/20 16:24
     */
    @RequestMapping(value = "/create", method = {RequestMethod.POST})
    public ApiResult create(@RequestBody Program program, HttpServletRequest request) throws Exception {
        if (program == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        programServices.insert(program, tokenModel);
        return ApiResult.success();
    }

    /**
     * @param program
     * @param request
     * @Method delete
     * @Author WXL
     * @Version 1.0
     * @Description 删除项目
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/21 13：58
     */
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public ApiResult delete(@RequestBody Program program, HttpServletRequest request) throws Exception {
        if (program == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        program.setStatus(AuthConstants.DEL_FLAG_DELETE);
        programServices.delete(program);
        return ApiResult.success();
    }

    /**
     * @param programid
     * @param request
     * @Method selectById
     * @Author WXL
     * @Version 1.0
     * @Description 获取项目详情
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/21 13:58
     */
    @RequestMapping(value = "/selectById", method = {RequestMethod.GET})
    public ApiResult selectById(String programid, HttpServletRequest request) throws Exception {
        if (StrUtil.isEmpty(programid)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(programServices.one(programid));
    }

    /**
     * @param program
     * @param request
     * @Method update
     * @Author WXL
     * @Version 1.0
     * @Description 更新项目详情
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/21 16:00
     */
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody Program program, HttpServletRequest request) throws Exception {
        if (program == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        programServices.update(program, tokenModel);
        return ApiResult.success();
    }
}
