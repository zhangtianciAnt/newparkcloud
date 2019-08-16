package com.nt.service_WorkFlow;

import com.nt.dao_Workflow.Vo.OperationWorkflowVo;
import com.nt.dao_Workflow.Vo.StartWorkflowVo;
import com.nt.dao_Workflow.Vo.WorkflowLogVo;
import com.nt.dao_Workflow.Workflow;
import com.nt.utils.LogicalException;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


public interface WorkflowServices {

	// 是否可以撤销审批
	public String isDelWorkflow(StartWorkflowVo startWorkflowVo) throws LogicalException;

	// 撤销审批
	public void DelWorkflow(String instanceId) throws LogicalException;

	// 是否可以发起审批
	public List<Workflow> isStartWorkflow(StartWorkflowVo startWorkflowVo, HttpServletRequest request) throws LogicalException;

	// 发起审批
	public void StartWorkflow(StartWorkflowVo startWorkflowVo, HttpServletRequest request) throws LogicalException;

	// 是否可以进行审批
	public Map<String, String> isOperationWorkflow(StartWorkflowVo startWorkflowVo) throws LogicalException;

	// 进行审批
	public void OperationWorkflow(OperationWorkflowVo operationWorkflowVo, HttpServletRequest request) throws LogicalException;

	// 是否可以查看流程
	public Boolean isViewWorkflow(StartWorkflowVo startWorkflowVo) throws LogicalException;

	// 查看流程日志
	public List<WorkflowLogVo> ViewWorkflow(StartWorkflowVo startWorkflowVo) throws LogicalException;
}
