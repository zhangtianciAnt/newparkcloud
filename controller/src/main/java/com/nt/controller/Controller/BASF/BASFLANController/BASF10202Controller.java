package com.nt.controller.Controller.BASF.BASFLANController;

import cn.hutool.core.util.StrUtil;
import com.nt.dao_BASF.Application;
import com.nt.dao_BASF.VO.ApplicationVo;
import com.nt.dao_Workflow.Vo.StartWorkflowVo;
import com.nt.dao_Workflow.Workflow;
import com.nt.dao_Workflow.Workflownode;
import com.nt.service_BASF.ApplicationServices;
import com.nt.service_WorkFlow.WorkflowServices;
import com.nt.service_WorkFlow.mapper.WorkflowMapper;
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
 * @ClassName: BASF10202Controller
 * @Author: LXY
 * @Description: 消防设备申请审核Controller
 * @Date: 2019/11/12 11.10
 * @Version: 1.0
 */
@RestController
@RequestMapping("/BASF10202")
public class BASF10202Controller {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ApplicationServices applicationServices;

    @Autowired
    private WorkflowServices workflowServices;

    @RequestMapping(value = "/get", method = {RequestMethod.POST})
    public ApiResult get(@RequestBody Application application) throws Exception {
        return ApiResult.success(applicationServices.get(application));
    }

//    @RequestMapping(value = "/getApplicationList", method = {RequestMethod.POST})
//    public ApiResult getApplicationList(HttpServletRequest request) throws Exception {
//        TokenModel tokenModel = tokenService.getToken(request);
//        return ApiResult.success(applicationServices.getList());
//    }

    @RequestMapping(value = "/insert", method = {RequestMethod.POST})
    public ApiResult insert(@RequestBody ApplicationVo applicationVo, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        applicationServices.insert(tokenModel, applicationVo.getApplication());
        String applicationid = applicationVo.getApplication().getApplicationid();

        StartWorkflowVo startWorkflowVo = new StartWorkflowVo();
        startWorkflowVo.setDataId(applicationid);
        startWorkflowVo.setMenuUrl("/BASF10202View");
        startWorkflowVo.setDataUrl("/BASF10202FormView");
        startWorkflowVo.setWorkFlowId(applicationVo.getWorkflowid());

        workflowServices.StartWorkflow(startWorkflowVo,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody Application application, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        applicationServices.update(tokenModel, application);
        return ApiResult.success();
    }

    @RequestMapping(value = "/del", method = {RequestMethod.POST})
    public ApiResult del(@RequestBody Application application, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        application.setStatus(AuthConstants.DEL_FLAG_DELETE);
        applicationServices.del(tokenModel, application);
        return ApiResult.success();
    }

    @RequestMapping(value = "/selectById", method = {RequestMethod.GET})
    public ApiResult selectById(String applicationid, HttpServletRequest request) throws Exception {
        if (StrUtil.isEmpty(applicationid)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(applicationServices.one(applicationid));
    }
}
