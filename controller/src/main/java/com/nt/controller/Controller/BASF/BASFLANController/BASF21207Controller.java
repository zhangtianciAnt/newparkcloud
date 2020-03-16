package com.nt.controller.Controller.BASF.BASFLANController;

import cn.hutool.core.util.StrUtil;
import com.nt.dao_BASF.Programlist;
import com.nt.service_BASF.DevicetrainerServices;
import com.nt.service_BASF.ProgramlistServices;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.controller.Controller.BASF.BASFLANController
 * @ClassName: BASF21207Controller
 * @Author:
 * @Description: BASF培训清单模块Controller
 * @Date: 2019/11/25 14：20
 * @Version: 1.0
 */
@RestController
@RequestMapping("/BASF21207")
public class BASF21207Controller {

    @Autowired
    private ProgramlistServices programlistroServices;
    @Autowired
    private DevicetrainerServices devicetrainerServices;
    @Autowired
    private TokenService tokenService;

    /**
     * @param programlist
     * @Method list
     * @Author
     * @Version 1.0
     * @Description 获取培训清单
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/25 14：20
     */
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public ApiResult list(@RequestBody Programlist programlist, HttpServletRequest request) throws Exception {
        return ApiResult.success(programlistroServices.list(programlist));
    }

    //获取培训计划清单增强
    @PostMapping("/listEnhance")
    public ApiResult listEnhance(HttpServletRequest request) throws Exception {
        return ApiResult.success(programlistroServices.listEnhance());
    }

    /**
     * @param programlistid
     * @param request
     * @Method selectById
     * @Author WXL
     * @Version 1.0
     * @Description 获取培训计划清单详情
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/25 14：20
     */
    @RequestMapping(value = "/selectById", method = {RequestMethod.GET})
    public ApiResult selectById(String programlistid, HttpServletRequest request) throws Exception {
        if (StrUtil.isEmpty(programlistid)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(programlistroServices.one(programlistid));
    }

    /**
     * @param programlist
     * @Method create
     * @Author
     * @Version 1.0
     * @Description 创建培训清单
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/20 16:24
     */
    @RequestMapping(value = "/create", method = {RequestMethod.POST})
    public ApiResult create(@RequestBody Programlist programlist, HttpServletRequest request) throws Exception {
        if (programlist == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(programlistroServices.insert(programlist, tokenModel));
    }

    /**
     * @param programlist
     * @param request
     * @Method update
     * @Author
     * @Version 1.0
     * @Description 更新培训清单
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/25 14：20
     */
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody Programlist programlist, HttpServletRequest request) throws Exception {
        if (programlist == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        programlistroServices.update(programlist, tokenModel);
        return ApiResult.success();
    }

    /**
     * @param programlist
     * @param request
     * @Method delete
     * @Author
     * @Version 1.0
     * @Description 删除培训清单
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/25 14：20
     */
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public ApiResult delete(@RequestBody Programlist programlist, HttpServletRequest request) throws Exception {
        if (programlist == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        programlist.setStatus(AuthConstants.DEL_FLAG_DELETE);
        programlistroServices.delete(programlist);
        return ApiResult.success();
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
    @RequestMapping(value = "/importProgramList", method = {RequestMethod.POST})
    public ApiResult importProgramList(HttpServletRequest request) {
        try {
            TokenModel tokenModel = tokenService.getToken(request);
            return ApiResult.success(programlistroServices.insert(request, tokenModel));
        } catch (LogicalException e) {
            return ApiResult.fail(e.getMessage());
        } catch (Exception e) {
            return ApiResult.fail("操作失败！");
        }
    }
}
