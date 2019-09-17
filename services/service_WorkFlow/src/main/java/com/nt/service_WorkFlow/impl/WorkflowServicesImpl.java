package com.nt.service_WorkFlow.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import com.mysql.jdbc.StringUtils;
import com.nt.dao_Org.ToDoNotice;
import com.nt.dao_Workflow.*;
import com.nt.dao_Workflow.Vo.*;
import com.nt.service_Org.ToDoNoticeService;
import com.nt.service_WorkFlow.WorkflowServices;
import com.nt.service_WorkFlow.WorkflownodeinstanceServices;
import com.nt.service_WorkFlow.mapper.*;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class WorkflowServicesImpl implements WorkflowServices {

    private static Logger log = LoggerFactory.getLogger(WorkflowServicesImpl.class);

	@Autowired
	private WorkflowMapper workflowMapper;

	@Autowired
	private WorkflownodeMapper workflownodeMapper;

	@Autowired
	private WorkflowinstanceMapper workflowinstanceMapper;

	@Autowired
	private WorkflownodeinstanceMapper workflownodeinstanceMapper;

	@Autowired
	private WorkflowstepMapper workflowstepMapper;

	@Autowired
	private ToDoNoticeService toDoNoticeService;


	@Override
    public void insert(WorkflowVo workflowVo, TokenModel tokenModel) throws Exception {
        Workflow workflow = new Workflow();
        BeanUtil.copyProperties(workflowVo,workflow);
        workflow.preInsert(tokenModel);
        workflow.setWorkflowid(UUID.randomUUID().toString());
        workflowMapper.insert(workflow);

        for(Workflownode item:workflowVo.getNodeList()){
            Workflownode workflownode = new Workflownode();
            BeanUtil.copyProperties(item,workflownode);
            workflownode.setWorkflowid(workflow.getWorkflowid());
            workflownode.preInsert(tokenModel);
            workflownode.setWorkflownodeid(UUID.randomUUID().toString());
            workflownodeMapper.insert(workflownode);
        }
    }

	@Override
	public void update(Workflow workflow) throws Exception {
		workflowMapper.updateByPrimaryKeySelective(workflow);
	}

	@Override
	public void upd(WorkflowVo workflowVo, TokenModel tokenModel) throws Exception {
		Workflow workflow = new Workflow();
		BeanUtil.copyProperties(workflowVo,workflow);
		workflowMapper.updateByPrimaryKeySelective(workflow);
		Workflownode del = new Workflownode();
		del.setWorkflowid(workflow.getWorkflowid());
		workflownodeMapper.delete(del);

		for(Workflownode item:workflowVo.getNodeList()){
			Workflownode workflownode = new Workflownode();
			BeanUtil.copyProperties(item,workflownode);
			workflownode.setWorkflowid(workflow.getWorkflowid());
			workflownode.preInsert(tokenModel);
			workflownode.setWorkflownodeid(UUID.randomUUID().toString());
			workflownodeMapper.insert(workflownode);
		}
	}

	@Override
	public WorkflowVo get(String workflowid) throws Exception {
		WorkflowVo workflowVo = new WorkflowVo();
		Workflow workflow = workflowMapper.selectByPrimaryKey(workflowid);
		BeanUtil.copyProperties(workflow,workflowVo);
		Workflownode workflownode = new Workflownode();
		workflownode.setWorkflowid(workflow.getWorkflowid());
		List<Workflownode> list = workflownodeMapper.select(workflownode);
		list = list.stream().sorted(Comparator.comparing(Workflownode::getNodeord)).collect(Collectors.toList());
		workflowVo.setNodeList(list);
		return workflowVo;
	}

	@Override
	public Workflow One(String workflowid) throws Exception {
		return workflowMapper.selectByPrimaryKey(workflowid);
	}

	@Override
	public List<Workflow> list(Workflow workflow) throws Exception {
		return workflowMapper.select(workflow);
	}

	@Override
	public List<Workflow> isStartWorkflow(StartWorkflowVo startWorkflowVo, TokenModel tokenModel)
			throws Exception {

			Workflow workflow = new Workflow();
			workflow.setFormid(startWorkflowVo.getMenuUrl());
			workflow.setTenantid(startWorkflowVo.getTenantId());
			workflow.setStatus(AuthConstants.DEL_FLAG_NORMAL);

			workflow = RequestUtils.CurrentPageOwnerList(tokenModel, workflow);

			List<Workflow> Workflowlist = workflowMapper.select(workflow);
			if (Workflowlist.size() > 0) {
				Workflowinstance workflowinstance = new Workflowinstance();
				workflowinstance.setDataid(startWorkflowVo.getDataId());
				workflowinstance.setFormid(startWorkflowVo.getMenuUrl());
				workflowinstance.setTenantid(startWorkflowVo.getTenantId());
				// workflowinstance.setStatus(AuthConstants.DEL_FLAG_NORMAL);
				List<Workflowinstance> list = workflowinstanceMapper.select(workflowinstance);
				if (list.size() == 0) {
					return Workflowlist;
				} else if (list.size() > 0 && !(list.stream().filter(item -> "0".equals(item.getStatus())).count() > 0
						|| list.stream().filter(item -> "1".equals(item.getStatus())).count() > 0)) {
					return Workflowlist;
				}
			}

			return new ArrayList<Workflow>();

	}

	@Override
	public Map<String, String> isOperationWorkflow(StartWorkflowVo startWorkflowVo) throws Exception {
		Workflowinstance workflowinstance = new Workflowinstance();
		workflowinstance.setDataid(startWorkflowVo.getDataId());
		workflowinstance.setFormid(startWorkflowVo.getMenuUrl());
		workflowinstance.setTenantid(startWorkflowVo.getTenantId());
		workflowinstance.setStatus(AuthConstants.DEL_FLAG_NORMAL);
		List<Workflowinstance> Workflowinstancelist = workflowinstanceMapper.select(workflowinstance);
		if (Workflowinstancelist.size() > 0) {
			Workflownodeinstance workflownodeinstance = new Workflownodeinstance();
			workflownodeinstance.setWorkflowinstanceid(Workflowinstancelist.get(0).getWorkflowinstanceid());
			workflownodeinstance.setStatus(AuthConstants.DEL_FLAG_NORMAL);
			workflownodeinstance.setTenantid(startWorkflowVo.getTenantId());
			List<Workflownodeinstance> Workflownodeinstancelist = workflownodeinstanceMapper
					.select(workflownodeinstance);
			for (Workflownodeinstance item : Workflownodeinstancelist) {
				Workflowstep workflowstep = new Workflowstep();
				workflowstep.setWorkflownodeinstanceid(item.getWorkflownodeinstanceid());
				workflowstep.setStatus(AuthConstants.DEL_FLAG_NORMAL);
				workflowstep.setItemid(startWorkflowVo.getUserId());
				List<Workflowstep> Workflowsteplist = workflowstepMapper.select(workflowstep);

				if (Workflowsteplist.size() > 0) {
					Map<String, String> rst = new HashMap<String, String>();
					rst.put("id", Workflowsteplist.get(0).getWorkflowstepid());
					rst.put("type", item.getNodetype());
					return rst;
				}
			}
		}
		return new HashMap<String, String>();
	}

	@Override
	public Boolean isViewWorkflow(StartWorkflowVo startWorkflowVo) throws Exception {
		Boolean rst = false;
		Workflow workflow = new Workflow();
		workflow.setFormid(startWorkflowVo.getMenuUrl());
		workflow.setTenantid(startWorkflowVo.getTenantId());
		rst = workflowMapper.select(workflow).size() > 0 ? true : false;
		if (rst) {
			Workflowinstance workflowinstance = new Workflowinstance();
			workflowinstance.setDataid(startWorkflowVo.getDataId());
			workflowinstance.setFormid(startWorkflowVo.getMenuUrl());
			workflowinstance.setTenantid(startWorkflowVo.getTenantId());
			rst = workflowinstanceMapper.select(workflowinstance).size() > 0 ? true : false;
		}

		return rst;
	}

	@Override
	public List<WorkflowLogVo> ViewWorkflow(StartWorkflowVo startWorkflowVo,String locale) throws Exception {
		List<WorkflowLogVo> rst = new ArrayList<WorkflowLogVo>();
		Workflowinstance workflowinstance = new Workflowinstance();
		workflowinstance.setDataid(startWorkflowVo.getDataId());
		workflowinstance.setFormid(startWorkflowVo.getMenuUrl());
		workflowinstance.setTenantid(startWorkflowVo.getTenantId());
		List<WorkflowLogVo> WorkflowLogVolist = workflowinstanceMapper.ViewWorkflow(workflowinstance);

		List<Workflowinstance> workflowinstancelist = workflowinstanceMapper.select(workflowinstance);
		workflowinstancelist = workflowinstancelist.stream()
				.sorted(Comparator.comparing(Workflowinstance::getCreateon).reversed()).collect(Collectors.toList());

		for (WorkflowLogVo item : WorkflowLogVolist) {

			if (item == WorkflowLogVolist.get(0)) {
				WorkflowLogVo endnode = new WorkflowLogVo();
				endnode.setId("00");
				endnode.setStepStatus(MessageUtil.getMessage(MsgConstants.WORKFLOW_03,locale));
				endnode.setTitle(MessageUtil.getMessage(MsgConstants.WORKFLOW_01,locale));
				WorkflowLogDetailVo workflowLogDetailVo = new WorkflowLogDetailVo();
				workflowLogDetailVo.setResult(MessageUtil.getMessage(MsgConstants.WORKFLOW_01,locale));
				workflowLogDetailVo.setUserId(workflowinstancelist.get(0).getOwner());
				workflowLogDetailVo.setSdata(workflowinstancelist.get(0).getCreateon());
				workflowLogDetailVo.setEdata(workflowinstancelist.get(0).getCreateon());
				endnode.setDetail(new ArrayList<WorkflowLogDetailVo>());
				endnode.getDetail().add(workflowLogDetailVo);
				rst.add(endnode);
			}

			Workflowstep workflowstep = new Workflowstep();
			workflowstep.setWorkflownodeinstanceid(item.getId());
			List<Workflowstep> Workflowsteplist = workflowstepMapper.select(workflowstep);

			String nodeStatus = "";
			int approval = 0;
			item.setDetail(new ArrayList<WorkflowLogDetailVo>());
			for (Workflowstep it : Workflowsteplist) {
				WorkflowLogDetailVo workflowLogDetailVo = new WorkflowLogDetailVo();
				if (StringUtils.isNullOrEmpty(it.getResult())) {
					workflowLogDetailVo.setResult(MessageUtil.getMessage(MsgConstants.WORKFLOW_04,locale));
				} else {
					workflowLogDetailVo.setResult(stepResultConvert(it.getResult(),locale));
				}

				workflowLogDetailVo.setUserId(it.getOwner());
				workflowLogDetailVo.setRemark(it.getRemark());
				workflowLogDetailVo.setSdata(it.getCreateon());
				workflowLogDetailVo.setEdata(it.getModifyon());

				item.getDetail().add(workflowLogDetailVo);
				if (!StringUtils.isNullOrEmpty(it.getResult())
						&& (it.getResult().equals("0") || it.getResult().equals("11"))) {
					approval++;
				} else if (!StringUtils.isNullOrEmpty(it.getResult()) && it.getResult().equals("1")) {
					nodeStatus = MessageUtil.getMessage(MsgConstants.WORKFLOW_05,locale);
				} else if (!StringUtils.isNullOrEmpty(it.getResult()) && it.getResult().equals("3")) {
					nodeStatus = MessageUtil.getMessage(MsgConstants.WORKFLOW_06,locale);
				} else {
					nodeStatus = MessageUtil.getMessage(MsgConstants.WORKFLOW_04,locale);
				}
			}
			if (approval != 0 && approval == Workflowsteplist.size())
				nodeStatus = MessageUtil.getMessage(MsgConstants.WORKFLOW_03,locale);

			item.setStepStatus(nodeStatus);
			rst.add(item);
			if (item == WorkflowLogVolist.get(WorkflowLogVolist.size() - 1)) {
				WorkflowLogVo endnode = new WorkflowLogVo();
				endnode.setId("99");
				WorkflowLogDetailVo workflowLogDetailVo = new WorkflowLogDetailVo();

				if (StringUtils.isNullOrEmpty(workflowinstancelist.get(0).getStatus())) {

					endnode.setStepStatus("");
					workflowLogDetailVo.setResult("");
				} else {

					endnode.setStepStatus(workResultConvert(workflowinstancelist.get(0).getStatus(),locale));
					workflowLogDetailVo.setResult(workResultConvert(workflowinstancelist.get(0).getStatus(),locale));
				}
				endnode.setTitle(MessageUtil.getMessage(MsgConstants.WORKFLOW_02,locale));

				workflowLogDetailVo.setUserId(workflowinstancelist.get(0).getOwner());
				workflowLogDetailVo.setSdata(workflowinstancelist.get(0).getModifyon());
				workflowLogDetailVo.setEdata(workflowinstancelist.get(0).getModifyon());
				endnode.setDetail(new ArrayList<WorkflowLogDetailVo>());
				endnode.getDetail().add(workflowLogDetailVo);
				rst.add(endnode);
			}
		}

		return rst;
	}

	@Override
	public List<WorkflowLogDetailVo> ViewWorkflow2(StartWorkflowVo startWorkflowVo,String locale) throws Exception {
		List<WorkflowLogDetailVo> rst = new ArrayList<WorkflowLogDetailVo>();

		Workflowinstance workflowinstance = new Workflowinstance();
		workflowinstance.setDataid(startWorkflowVo.getDataId());
		workflowinstance.setFormid(startWorkflowVo.getMenuUrl());
		workflowinstance.setTenantid(startWorkflowVo.getTenantId());
		List<Workflowinstance> Workflowinstancelist = workflowinstanceMapper.select(workflowinstance);
		Workflowinstancelist = Workflowinstancelist.stream()
				.sorted(Comparator.comparing(Workflowinstance::getCreateon).reversed()).collect(Collectors.toList());
		for(Workflowinstance item:Workflowinstancelist){
			Workflownodeinstance workflownodeinstance = new Workflownodeinstance();
			workflownodeinstance.setWorkflowinstanceid(item.getWorkflowinstanceid());
			List<Workflownodeinstance> workflownodeinstancelist = workflownodeinstanceMapper.select(workflownodeinstance);
			workflownodeinstancelist = workflownodeinstancelist.stream()
					.sorted(Comparator.comparing(Workflownodeinstance::getCreateon).reversed()).collect(Collectors.toList());
			for(Workflownodeinstance node:workflownodeinstancelist){

				Workflowstep workflowstep = new Workflowstep();
				workflowstep.setWorkflownodeinstanceid(node.getWorkflownodeinstanceid());
				List<Workflowstep> Workflowsteplist = workflowstepMapper.select(workflowstep);

				for (Workflowstep it : Workflowsteplist) {
					WorkflowLogDetailVo workflowLogDetailVo = new WorkflowLogDetailVo();
					if (StringUtils.isNullOrEmpty(it.getResult())) {
						workflowLogDetailVo.setResult(MessageUtil.getMessage(MsgConstants.WORKFLOW_04,locale));
					} else {
						workflowLogDetailVo.setResult(stepResultConvert(it.getResult(),locale));
					}

					workflowLogDetailVo.setUserId(it.getOwner());
					workflowLogDetailVo.setRemark(it.getRemark());
					workflowLogDetailVo.setSdata(it.getCreateon());
					workflowLogDetailVo.setEdata(it.getModifyon());
					workflowLogDetailVo.setIsvirtual("1");
					rst.add(workflowLogDetailVo);
				}

				if (node == workflownodeinstancelist.get(workflownodeinstancelist.size() - 1)) {

					WorkflowLogDetailVo workflowLogDetailVo = new WorkflowLogDetailVo();
					workflowLogDetailVo.setResult(MessageUtil.getMessage(MsgConstants.WORKFLOW_01,locale));
					workflowLogDetailVo.setUserId(item.getOwner());
					workflowLogDetailVo.setSdata(item.getCreateon());
					workflowLogDetailVo.setEdata(item.getCreateon());
					workflowLogDetailVo.setIsvirtual("0");
					rst.add(workflowLogDetailVo);
				}
			}
		}
		return rst;
	}

	private String stepResultConvert(String code,String locale) {
		switch (code) {
		case "1":
			return MessageUtil.getMessage(MsgConstants.WORKFLOW_05,locale);
		case "3":
			return MessageUtil.getMessage(MsgConstants.WORKFLOW_06,locale);
		case "11":
			return MessageUtil.getMessage(MsgConstants.WORKFLOW_07,locale);
		default:
			return MessageUtil.getMessage(MsgConstants.WORKFLOW_03,locale);
		}
	}

	private String workResultConvert(String code,String locale) {
		switch (code) {
		case "0":
			return MessageUtil.getMessage(MsgConstants.WORKFLOW_04,locale);
		case "1":
			return MessageUtil.getMessage(MsgConstants.WORKFLOW_03,locale);
		case "2":
			return MessageUtil.getMessage(MsgConstants.WORKFLOW_05,locale);
		case "3":
			return MessageUtil.getMessage(MsgConstants.WORKFLOW_06,locale);
		default:
			return "";
		}
	}

	@Override
	public void OperationWorkflow(OperationWorkflowVo operationWorkflowVo, TokenModel tokenModel)
			throws Exception {

			// 更新当前节点
			Workflowstep workflowstep = workflowstepMapper.selectByPrimaryKey(operationWorkflowVo.getId());
			// 同意&拒绝
			if ("3".equals(operationWorkflowVo.getResult()) || "4".equals(operationWorkflowVo.getResult())) {
				operationWorkflowVo.setResult("0");
			}
			workflowstep.setResult(operationWorkflowVo.getResult());
			workflowstep.setRemark(operationWorkflowVo.getRemark());
			workflowstep.setModifyby(operationWorkflowVo.getUserid());
			workflowstep.setModifyon(new Date());
			workflowstep.setStatus(AuthConstants.DEL_FLAG_DELETE);
			workflowstepMapper.updateByPrimaryKeySelective(workflowstep);

			// 结束当前代办
			ToDoNotice toDoNotice1 = new ToDoNotice();
			toDoNotice1.setDataid(operationWorkflowVo.getDataId());
			toDoNotice1.setUrl(operationWorkflowVo.getDataUrl());
			toDoNotice1.setOwner(tokenModel.getUserId());
			List<ToDoNotice> rst1 = toDoNoticeService.get(toDoNotice1);
			for (ToDoNotice item:
					rst1) {
				item.setStatus(AuthConstants.TODO_STATUS_DONE);
				toDoNoticeService.updateNoticesStatus(item);
			}
			Workflownodeinstance nodeinstance = workflownodeinstanceMapper
					.selectByPrimaryKey(workflowstep.getWorkflownodeinstanceid());
			Workflowinstance workflowinstance = workflowinstanceMapper
					.selectByPrimaryKey(nodeinstance.getWorkflowinstanceid());
			// 当操作为驳回时
			if ("1".equals(operationWorkflowVo.getResult())) {
				// 结束当前流程 并更新状态为驳回 status 为2
				workflowinstanceMapper.stopInstanceByReject(workflowstep.getWorkflownodeinstanceid(), "2");
				// 结束所有代办

				ToDoNotice toDoNotice = new ToDoNotice();
				toDoNotice.setDataid(workflowinstance.getDataid());
				toDoNotice.setUrl(workflowinstance.getUrl());
				List<ToDoNotice> rst = toDoNoticeService.get(toDoNotice);
				for (ToDoNotice item:
						rst) {
					item.setStatus(AuthConstants.TODO_STATUS_DELETE);
					toDoNoticeService.updateNoticesStatus(item);
				}
			}

			// 转办&指定审批人
			else if ("11".equals(operationWorkflowVo.getResult()) || "5".equals(operationWorkflowVo.getResult())) {
				// 创建转办节点
				Workflowstep step = new Workflowstep();
				step.setWorkflowstepid(UUID.randomUUID().toString());
				step.setWorkflownodeinstanceid(workflowstep.getWorkflownodeinstanceid());
				if ("11".equals(operationWorkflowVo.getResult())) {

					step.setName(MessageUtil.getMessage(MsgConstants.WORKFLOW_07,tokenModel.getLocale()));
				} else {

					step.setName(MessageUtil.getMessage(MsgConstants.WORKFLOW_08,tokenModel.getLocale()));
				}

				step.setCreateby(operationWorkflowVo.getUserid());
				step.setCreateon(new Date());
				step.setStatus(AuthConstants.DEL_FLAG_NORMAL);
				step.setTenantid(operationWorkflowVo.getTenantid());
				if ("11".equals(operationWorkflowVo.getResult())) {
					step.setItemid(operationWorkflowVo.getToAnotherUser());
					step.setOwner(operationWorkflowVo.getToAnotherUser());
					workflowstepMapper.insert(step);

					// 创建代办
//					TenanttaskVo tenanttaskVo = new TenanttaskVo();
//					tenanttaskVo
//							.setDataurl(operationWorkflowVo.getMenuUrl() + "?wfid=" + operationWorkflowVo.getDataId());
//					tenanttaskVo.setType("1");
//					tenanttaskVo.setFromuser(operationWorkflowVo.getUserid());
//					tenanttaskVo.setTitle("您有一个【" + workflowinstance.getWorkflowname() + "】审批待处理！");
//					tenanttaskVo.setContent(operationWorkflowVo.getDataId());
//					tenanttaskVo.setCreateby(operationWorkflowVo.getToAnotherUser());
//					tenanttaskVo.setOwner(operationWorkflowVo.getToAnotherUser());
//					tenanttaskVo.setTenantid(operationWorkflowVo.getTenantid());

				} else {
					for (int i = 0; i < operationWorkflowVo.getUsers().split(",").length; i++) {
						step.setItemid(operationWorkflowVo.getUsers().split(",")[i]);
						step.setOwner(operationWorkflowVo.getUsers().split(",")[i]);
						workflowstepMapper.insert(step);

						// 创建代办
//						TenanttaskVo tenanttaskVo = new TenanttaskVo();
//						tenanttaskVo.setDataurl(
//								operationWorkflowVo.getMenuUrl() + "?wfid=" + operationWorkflowVo.getDataId());
//						tenanttaskVo.setType("1");
//						tenanttaskVo.setTitle("您有一个【" + workflowinstance.getWorkflowname() + "】审批待处理！");
//						tenanttaskVo.setFromuser(operationWorkflowVo.getUsers().split(",")[i]);
//						tenanttaskVo.setContent(operationWorkflowVo.getDataId());
//						tenanttaskVo.setCreateby(operationWorkflowVo.getUsers().split(",")[i]);
//						tenanttaskVo.setOwner(operationWorkflowVo.getUsers().split(",")[i]);
//						tenanttaskVo.setTenantid(operationWorkflowVo.getTenantid());
//
//						HttpHeaders headers = new HttpHeaders();
//						Enumeration<String> headerNames = request.getHeaderNames();
//						while (headerNames.hasMoreElements()) {
//							String key = (String) headerNames.nextElement();
//							String value = request.getHeader(key);
//							headers.add(key, value);
//						}
//						HttpEntity<TenanttaskVo> requestEntity = new HttpEntity<TenanttaskVo>(tenanttaskVo, headers);
//						restTemplate.postForObject("http://" + RequestUtil.MESSAGE + "/tenantTask/insertTask",
//								requestEntity, ApiResult.class);
					}
				}

			}
			// 生成下一节点信息

			cresteStep(nodeinstance.getWorkflowinstanceid(), tokenModel, operationWorkflowVo.getDataId(),
					operationWorkflowVo.getDataUrl(),operationWorkflowVo.getMenuUrl(), workflowinstance.getWorkflowname());

	}

	private void cresteStep(String instanceId, TokenModel tokenModel, String dataId, String url,String workFlowurl,
			String workflowname) throws Exception {

		Workflowinstance workflowinstance = workflowinstanceMapper.selectByPrimaryKey(instanceId);
		// 流程进行中
		if ("0".equals(workflowinstance.getStatus())) {
			// 查找流程节点实例
			Workflownodeinstance condition = new Workflownodeinstance();
			condition.setWorkflowinstanceid(instanceId);
			condition.setStatus(AuthConstants.DEL_FLAG_NORMAL);
			List<Workflownodeinstance> workflownodeinstancelist = workflownodeinstanceMapper.select(condition);
			workflownodeinstancelist = workflownodeinstancelist.stream()
					.sorted(Comparator.comparing(Workflownodeinstance::getNodeord)).collect(Collectors.toList());
			for (Workflownodeinstance item : workflownodeinstancelist) {
				// 查找节点操作
				Workflowstep conditionWorkflowstep = new Workflowstep();
				conditionWorkflowstep.setWorkflownodeinstanceid(item.getWorkflownodeinstanceid());
				List<Workflowstep> workflowsteplist = workflowstepMapper.select(conditionWorkflowstep);
				// 当前节点无任何信息_根据节点实例创建节点操作信息
				if (workflowsteplist.size() == 0) {
					for (String user : item.getItemid().split(",")) {
						// 创建节点
						Workflowstep workflowstep = new Workflowstep();
						workflowstep.setWorkflowstepid(UUID.randomUUID().toString());
						workflowstep.setWorkflownodeinstanceid(item.getWorkflownodeinstanceid());
						workflowstep.setName(item.getNodename());
						workflowstep.setItemid(user);
						workflowstep.preInsert(tokenModel);
						workflowstepMapper.insert(workflowstep);

						// 创建代办
						ToDoNotice toDoNotice = new ToDoNotice();
						List<String> params = new ArrayList<String>();
						params.add(workflowname);
						toDoNotice.setTitle(MessageUtil.getMessage(MsgConstants.WORKFLOW_10,params,tokenModel.getLocale()));
						toDoNotice.setInitiator(tokenModel.getUserId());
						toDoNotice.setContent(item.getNodename());
						toDoNotice.setDataid(dataId);
						toDoNotice.setUrl(url);
						toDoNotice.setWorkflowurl(workFlowurl);
						toDoNotice.preInsert(tokenModel);
						toDoNoticeService.save(toDoNotice);
					}

					return;
				}
				// 有节点信息
				else {
					// 当前有未审批节点
					conditionWorkflowstep = new Workflowstep();
					conditionWorkflowstep.setWorkflownodeinstanceid(item.getWorkflownodeinstanceid());
					conditionWorkflowstep.setStatus(AuthConstants.DEL_FLAG_NORMAL);
					workflowsteplist = workflowstepMapper.select(conditionWorkflowstep);
					if (workflowsteplist.size() > 0) {
						// 结束操作
						return;
					}

					// 如果节点为最后一个节点时，结束流程
					if (item == workflownodeinstancelist.get(workflownodeinstancelist.size() - 1)) {
						workflowinstance.setModifyby(tokenModel.getUserId());
						workflowinstance.setModifyon(new Date());
						workflowinstance.setStatus(AuthConstants.DEL_FLAG_DELETE);
						workflowinstanceMapper.updateByPrimaryKeySelective(workflowinstance);
					}

					// 其他_即当前节点均操作完成，则继续循环生成下一节点信息
				}
			}
		}

	}

	@Override
	public void StartWorkflow(StartWorkflowVo startWorkflowVo, TokenModel tokenModel) throws Exception {

			// 创建流程实例
			Workflow workflow = workflowMapper.selectByPrimaryKey(startWorkflowVo.getWorkFlowId());
			Workflowinstance workflowinstance = new Workflowinstance();
			BeanUtils.copyProperties(workflow, workflowinstance);
			workflowinstance.setWorkflowinstanceid(UUID.randomUUID().toString());
			workflowinstance.setDataid(startWorkflowVo.getDataId());
			workflowinstance.setFormid(startWorkflowVo.getMenuUrl());
			workflowinstance.setUrl(startWorkflowVo.getDataUrl());
			workflowinstance.preInsert(tokenModel);
			workflowinstanceMapper.insert(workflowinstance);

			// 创建节点实例
			Workflownode workflownode = new Workflownode();
			workflownode.setWorkflowid(workflow.getWorkflowid());
			workflownode.setStatus(AuthConstants.DEL_FLAG_NORMAL);
			List<Workflownode> workflownodelist = workflownodeMapper.select(workflownode);
			for (int i = 0; i < workflownodelist.size(); i++) {
				Workflownodeinstance workflownodeinstance = new Workflownodeinstance();
				BeanUtils.copyProperties(workflownodelist.get(i), workflownodeinstance);
				workflownodeinstance.setWorkflowinstanceid(workflowinstance.getWorkflowinstanceid());
				workflownodeinstance.setWorkflownodeinstanceid(UUID.randomUUID().toString());
				workflownodeinstance.preInsert(tokenModel);
				workflownodeinstanceMapper.insert(workflownodeinstance);
			}

			// 生成节点操作
			cresteStep(workflowinstance.getWorkflowinstanceid(), tokenModel, startWorkflowVo.getDataId(),
					startWorkflowVo.getDataUrl(),startWorkflowVo.getMenuUrl(), workflow.getWorkflowname());

	}

	@Override
	public String isDelWorkflow(StartWorkflowVo startWorkflowVo) throws Exception {

		Workflowinstance workflowinstance = new Workflowinstance();
		workflowinstance.setDataid(startWorkflowVo.getDataId());
		workflowinstance.setFormid(startWorkflowVo.getMenuUrl());
		workflowinstance.setOwner(startWorkflowVo.getUserId());
		workflowinstance.setTenantid(startWorkflowVo.getTenantId());
		workflowinstance.setStatus(AuthConstants.DEL_FLAG_NORMAL);
		List<Workflowinstance> Workflowinstancelist = workflowinstanceMapper.select(workflowinstance);
		if (Workflowinstancelist.size() > 0) {
			Workflownodeinstance workflownodeinstance = new Workflownodeinstance();
			workflownodeinstance.setWorkflowinstanceid(Workflowinstancelist.get(0).getWorkflowinstanceid());
			workflownodeinstance.setStatus(AuthConstants.DEL_FLAG_NORMAL);
			workflownodeinstance.setTenantid(startWorkflowVo.getTenantId());
			List<Workflownodeinstance> Workflownodeinstancelist = workflownodeinstanceMapper
					.select(workflownodeinstance);
			for (Workflownodeinstance item : Workflownodeinstancelist) {
				Workflowstep workflowstep = new Workflowstep();
				workflowstep.setWorkflownodeinstanceid(item.getWorkflownodeinstanceid());
				List<Workflowstep> Workflowsteplist = workflowstepMapper.select(workflowstep);

				for (Workflowstep it : Workflowsteplist) {
					if (!StringUtils.isNullOrEmpty(it.getResult())) {
						return "";

					}
				}
			}

			return Workflowinstancelist.get(0).getWorkflowinstanceid();
		}
		return "";
	}

	@Override
	public void DelWorkflow(String instanceId,String locale) throws Exception {
		Workflowinstance workflowinstance = workflowinstanceMapper.selectByPrimaryKey(instanceId);

		// 结束当前流程 并更新状态为撤销 status 为3
		workflowinstance.setStatus("3");
		workflowinstance.setModifyby(workflowinstance.getCreateby());
		workflowinstance.setModifyon(new Date());
		workflowinstanceMapper.updateByPrimaryKeySelective(workflowinstance);

		Workflownodeinstance workflownodeinstance = new Workflownodeinstance();
		workflownodeinstance.setWorkflowinstanceid(instanceId);
		List<Workflownodeinstance> workflownodeinstancelist = workflownodeinstanceMapper.select(workflownodeinstance);
		for (Workflownodeinstance item : workflownodeinstancelist) {
			Workflowstep workflowstep = new Workflowstep();
			workflowstep.setWorkflownodeinstanceid(item.getWorkflownodeinstanceid());
			List<Workflowstep> workflowsteplist = workflowstepMapper.select(workflowstep);
			for (Workflowstep it : workflowsteplist) {
				it.setResult("3");
				it.setRemark(MessageUtil.getMessage(MsgConstants.WORKFLOW_09,locale));
				it.setModifyby(workflowinstance.getCreateby());
				it.setModifyon(new Date());
				workflowstepMapper.updateByPrimaryKeySelective(it);
			}
		}
		// 结束所有代办

		ToDoNotice toDoNotice = new ToDoNotice();
		toDoNotice.setDataid(workflowinstance.getDataid());
		toDoNotice.setUrl(workflowinstance.getUrl());
		List<ToDoNotice> rst = toDoNoticeService.get(toDoNotice);
		for (ToDoNotice item:
				rst) {
			item.setStatus(AuthConstants.TODO_STATUS_DELETE);
			toDoNoticeService.updateNoticesStatus(item);
		}
	}

}
