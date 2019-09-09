package com.nt.controller.Controller;

import com.nt.dao_Auth.Role;
import com.nt.dao_Org.ToDoNotice;
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
            workflow.setOwners(tokenModel.getOwnerList());
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
        List<Workflow>  workflowlist= workflowServices.isStartWorkflow(startWorkflowVo,tokenModel);
        return ApiResult.success(workflowlist);
    }

}
