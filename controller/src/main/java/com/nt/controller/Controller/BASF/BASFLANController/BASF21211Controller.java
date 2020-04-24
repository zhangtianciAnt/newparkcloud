package com.nt.controller.Controller.BASF.BASFLANController;

import com.nt.dao_BASF.Trainjoinlist;
import com.nt.service_Auth.RoleService;
import com.nt.service_BASF.ProgramlistServices;
import com.nt.service_BASF.StartprogramServices;
import com.nt.service_BASF.StartprogramTrainServices;
import com.nt.service_BASF.TrainjoinlistServices;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.controller.Controller.BASF.BASFLANController
 * @ClassName: BASF21211Controller
 * @Author: 王哲
 * @Description: 在线培训
 * @Date: 2020/2/17 20:15
 * @Version: 1.0
 */
@RestController
@RequestMapping("/BASF21211")
public class BASF21211Controller {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private TrainjoinlistServices trainjoinlistServices;

    @Autowired
    private ProgramlistServices programlistServices;

    @Autowired
    private RoleService roleService;

    @Autowired
    private StartprogramServices startprogramServices;

    @Autowired
    private StartprogramTrainServices startprogramTrainServices;

    //更新培训参加人员
    @PostMapping("/upTrain")
    public ApiResult upTrain(@RequestBody Trainjoinlist trainjoinlist, HttpServletRequest request) throws Exception {
        if (trainjoinlist == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        trainjoinlistServices.updataOnline(trainjoinlist, tokenModel);
        return ApiResult.success();
    }

    //根据培训清单模板id获取培训资料
    @GetMapping("/videoOrPdfFile")
    public ApiResult videoOrPdfFile(String programlistid, HttpServletRequest request) throws Exception {
        if (StringUtils.isEmpty(programlistid)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(programlistServices.videoOrPdfFile(programlistid));
    }

    //根据姓名（或员工号、卡号）和年份查询某人员培训信息（培训教育大屏用）
    @GetMapping("/getTrainEducationPerInfo")
    public ApiResult getTrainEducationPerInfo(String year, HttpServletRequest request) throws Exception {
        if (StringUtils.isEmpty(year)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(startprogramServices.getTrainEducationPerInfo(year));
    }

    //获取当前登录角色下的非系统登录用户
    @GetMapping("/getThisRoleUsers")
    public ApiResult getThisRoleUsers(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        //useraccount表的_id。customerinfo表的_userid
        String userid = tokenModel.getUserId();
        return ApiResult.success(roleService.getThisRoleUsers(userid));
    }

    //获取培训通过率（强制）
    @GetMapping("/getDeptThrough")
    public ApiResult getDeptThrough(String year, HttpServletRequest request) throws Exception {
        if (StringUtils.isEmpty(year)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(startprogramTrainServices.getDeptThrough(year));
    }

    //获取培训通过率（非强制）
    @GetMapping("/getUnDeptThrough")
    public ApiResult getUnDeptThrough(String year, HttpServletRequest request) throws Exception {
        if (StringUtils.isEmpty(year)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(startprogramTrainServices.getUnDeptThrough(year));
    }
}
