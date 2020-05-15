package com.nt.controller.Controller;

import cn.hutool.core.util.StrUtil;
import com.nt.dao_Auth.Role;
import com.nt.dao_Org.ToDoNotice;
import com.nt.dao_Workflow.Vo.OperationWorkflowVo;
import com.nt.dao_Workflow.Vo.StartWorkflowVo;
import com.nt.dao_Workflow.Vo.WorkflowVo;
import com.nt.dao_Workflow.Workflow;
import com.nt.service_Org.ToDoNoticeService;
import com.nt.service_WorkFlow.WorkflowServices;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.dao.WeixinOauth2Token;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/workflow")
public class WorkflowController {

    @Autowired
    private WorkflowServices workflowServices;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value="/list", method={RequestMethod.POST})
    public ApiResult list(HttpServletRequest request) throws Exception{

        try {

            TokenModel tokenModel = tokenService.getToken(request);
            Workflow workflow = new Workflow();
            workflow.setStatus(AuthConstants.DEL_FLAG_NORMAL);
            workflow.setTenantid(tokenModel.getTenantId());
//            workflow.setOwners(tokenModel.getOwnerList());
            workflow.setIds(tokenModel.getIdList());
            return ApiResult.success(workflowServices.list(workflow));

        } catch(LogicalException e){
            return ApiResult.fail(e.getMessage());
        }
    }

    @RequestMapping(value = "/del",method={RequestMethod.POST})
    public ApiResult del(@RequestBody Workflow workflow, HttpServletRequest request) throws Exception {
        if (workflow == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);

        Workflow rst = workflowServices.One(workflow.getWorkflowid());
        if (rst != null) {
            //更新人和更新时间
            rst.preUpdate(tokenModel);
            //逻辑删除
            rst.setStatus(AuthConstants.DEL_FLAG_DELETE);
            //保存
            workflowServices.update(rst);
        }
        return ApiResult.success();
    }

    @RequestMapping(value = "/create",method={RequestMethod.POST})
    public ApiResult create(@RequestBody WorkflowVo workflowVo, HttpServletRequest request) throws Exception {
        if (workflowVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        workflowServices.insert(workflowVo,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/upd",method={RequestMethod.POST})
    public ApiResult upd(@RequestBody WorkflowVo workflowVo, HttpServletRequest request) throws Exception {
        if (workflowVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        workflowServices.upd(workflowVo,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/one",method={RequestMethod.POST})
    public ApiResult one(@RequestBody Workflow workflow, HttpServletRequest request) throws Exception {
        if (workflow == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        WorkflowVo workflowVo = workflowServices.get(workflow.getWorkflowid());
        return ApiResult.success(workflowVo);
    }

    @RequestMapping(value = "/isStartWorkflow",method={RequestMethod.POST})
    public ApiResult isStartWorkflow(@RequestBody StartWorkflowVo startWorkflowVo, HttpServletRequest request) throws Exception {
        if (startWorkflowVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(workflowServices.isStartWorkflow(startWorkflowVo,tokenModel));
    }

    @RequestMapping(value = "/StartWorkflow",method={RequestMethod.POST})
    public ApiResult StartWorkflow(@RequestBody StartWorkflowVo startWorkflowVo, HttpServletRequest request) throws Exception {
        if (startWorkflowVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);

        return ApiResult.success(workflowServices.StartWorkflow(startWorkflowVo,tokenModel));
    }

    @RequestMapping(value = "/isViewWorkflow",method={RequestMethod.POST})
    public ApiResult isViewWorkflow(@RequestBody StartWorkflowVo startWorkflowVo, HttpServletRequest request) throws Exception {
        if (startWorkflowVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(workflowServices.isViewWorkflow(startWorkflowVo));
    }

    @RequestMapping(value = "/ViewWorkflow",method={RequestMethod.POST})
    public ApiResult ViewWorkflow(@RequestBody StartWorkflowVo startWorkflowVo, HttpServletRequest request) throws Exception {
        if (startWorkflowVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(workflowServices.ViewWorkflow(startWorkflowVo,tokenModel.getLocale()));
    }

    @RequestMapping(value = "/ViewWorkflow2",method={RequestMethod.POST})
    public ApiResult ViewWorkflow2(@RequestBody StartWorkflowVo startWorkflowVo, HttpServletRequest request) throws Exception {
        if (startWorkflowVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(workflowServices.ViewWorkflow2(startWorkflowVo,tokenModel.getLocale()));
    }

    @RequestMapping(value = "/isDelWorkflow",method={RequestMethod.POST})
    public ApiResult isDelWorkflow(@RequestBody StartWorkflowVo startWorkflowVo, HttpServletRequest request) throws Exception {
        if (startWorkflowVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        startWorkflowVo.setUserId(tokenModel.getUserId());
        return ApiResult.success(workflowServices.isDelWorkflow(startWorkflowVo));
    }

    @RequestMapping(value = "/DelWorkflow",method={RequestMethod.POST})
    public ApiResult DelWorkflow(String instanceId, HttpServletRequest request) throws Exception {
        if (StrUtil.isEmpty(instanceId)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        workflowServices.DelWorkflow(instanceId,tokenModel.getLocale());
        return ApiResult.success();
    }

    @RequestMapping(value = "/isOperationWorkflow",method={RequestMethod.POST})
    public ApiResult isOperationWorkflow(@RequestBody StartWorkflowVo startWorkflowVo, HttpServletRequest request) throws Exception {
        if (startWorkflowVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        startWorkflowVo.setUserId(tokenModel.getUserId());
        return ApiResult.success(workflowServices.isOperationWorkflow(startWorkflowVo));
    }

    @RequestMapping(value = "/OperationWorkflow",method={RequestMethod.POST})
    public ApiResult OperationWorkflow(@RequestBody OperationWorkflowVo operationWorkflowVo, HttpServletRequest request) throws Exception {
        if (operationWorkflowVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(workflowServices.OperationWorkflow(operationWorkflowVo,tokenModel));
    }

    @RequestMapping(value = "/allWorkFlowIns",method={RequestMethod.GET})
    public ApiResult allWorkFlowIns(String menuUrl,HttpServletRequest request) throws Exception {

        return ApiResult.success(workflowServices.allWorkFlowIns(menuUrl));
    }
}
