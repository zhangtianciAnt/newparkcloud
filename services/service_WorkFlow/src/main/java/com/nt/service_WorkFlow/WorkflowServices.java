package com.nt.service_WorkFlow;

import com.nt.dao_Workflow.Vo.*;
import com.nt.dao_Workflow.Workflow;
import com.nt.utils.LogicalException;
import com.nt.utils.dao.TokenModel;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


public interface WorkflowServices {

	public void insert(WorkflowVo workflowVo, TokenModel tokenModel)throws Exception;

	public void upd(WorkflowVo workflowVo, TokenModel tokenModel)throws Exception;

	//获取所有审批流程
	public WorkflowVo get(String workflowid) throws Exception;

	//获取所有审批流程
	public List<Workflow> list(Workflow workflow) throws Exception;

	//根据id获取数据
	public Workflow One(String workflowid) throws Exception;

	//更新数据
	public void update(Workflow workflow) throws Exception;

	// 是否可以撤销审批
	public String isDelWorkflow(StartWorkflowVo startWorkflowVo) throws Exception;

	// 撤销审批
	public void DelWorkflow(String instanceId,String locale) throws Exception;

	// 是否可以发起审批
	public List<Workflow> isStartWorkflow(StartWorkflowVo startWorkflowVo, TokenModel tokenModel) throws Exception;

	// 发起审批
	public void StartWorkflow(StartWorkflowVo startWorkflowVo, TokenModel tokenModel) throws Exception;

	// 是否可以进行审批
	public Map<String, String> isOperationWorkflow(StartWorkflowVo startWorkflowVo) throws Exception;

	// 进行审批
	public void OperationWorkflow(OperationWorkflowVo operationWorkflowVo, TokenModel tokenModel) throws Exception;

	// 是否可以查看流程
	public Boolean isViewWorkflow(StartWorkflowVo startWorkflowVo) throws Exception;

	// 查看流程日志
	public List<WorkflowLogVo> ViewWorkflow(StartWorkflowVo startWorkflowVo,String locale) throws Exception;

	// 查看流程日志2
	public List<WorkflowLogDetailVo> ViewWorkflow2(StartWorkflowVo startWorkflowVo,String locale) throws Exception;
}
