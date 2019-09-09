package com.nt.service_WorkFlow.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import cn.hutool.core.bean.BeanUtil;
import com.mysql.jdbc.StringUtils;
import com.nt.dao_Workflow.*;
import com.nt.dao_Workflow.Vo.*;
import com.nt.service_WorkFlow.WorkflowServices;
import com.nt.service_WorkFlow.mapper.*;
import com.nt.utils.AuthConstants;
import com.nt.utils.LogicalException;
import com.nt.utils.RequestUtils;
import com.nt.utils.dao.TokenModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

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

    @Override
    public void insert(WorkflowVo workflowVo, TokenModel tokenModel) throws LogicalException {
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
	public void update(Workflow workflow) throws LogicalException {
		workflowMapper.updateByPrimaryKeySelective(workflow);
	}

	@Override
	public void upd(WorkflowVo workflowVo, TokenModel tokenModel) throws LogicalException {
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
	public WorkflowVo get(String workflowid) throws LogicalException {
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
	public Workflow One(String workflowid) throws LogicalException {
		return workflowMapper.selectByPrimaryKey(workflowid);
	}

	@Override
	public List<Workflow> list(Workflow workflow) throws LogicalException {
		return workflowMapper.select(workflow);
	}

	@Override
	public List<Workflow> isStartWorkflow(StartWorkflowVo startWorkflowVo, TokenModel tokenModel)
			throws LogicalException {
		try {
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
		} catch (Exception e) {
			throw new LogicalException("操作失败!");
		}
	}

	@Override
	public Map<String, String> isOperationWorkflow(StartWorkflowVo startWorkflowVo) throws LogicalException {
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
	public Boolean isViewWorkflow(StartWorkflowVo startWorkflowVo) throws LogicalException {
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
	public List<WorkflowLogVo> ViewWorkflow(StartWorkflowVo startWorkflowVo) throws LogicalException {
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
				endnode.setStepStatus("通过");
				endnode.setTitle("流程开始");
				WorkflowLogDetailVo workflowLogDetailVo = new WorkflowLogDetailVo();
				workflowLogDetailVo.setResult("流程开始");
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
					workflowLogDetailVo.setResult("进行中");
				} else {
					workflowLogDetailVo.setResult(stepResultConvert(it.getResult()));
				}

				workflowLogDetailVo.setUserId(it.getOwner());
				workflowLogDetailVo.setRemark(it.getRemark());
				workflowLogDetailVo.setSdata(it.getCreateon());
				workflowLogDetailVo.setEdata(it.getModifyon());

				item.getDetail().add(workflowLogDetailVo);
				if (!StringUtils.isNullOrEmpty(it.getResult())
						&& (it.getResult().equals("0") || it.getResult().equals("11") || it.getResult().equals("5"))) {
					approval++;
				} else if (!StringUtils.isNullOrEmpty(it.getResult()) && it.getResult().equals("1")) {
					nodeStatus = "驳回";
				} else if (!StringUtils.isNullOrEmpty(it.getResult()) && it.getResult().equals("3")) {
					nodeStatus = "撤销";
				} else {
					nodeStatus = "进行中";
				}
			}
			if (approval != 0 && approval == Workflowsteplist.size())
				nodeStatus = "通过";

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

					endnode.setStepStatus(workResultConvert(workflowinstancelist.get(0).getStatus()));
					workflowLogDetailVo.setResult(workResultConvert(workflowinstancelist.get(0).getStatus()));
				}
				endnode.setTitle("流程结束");

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

	private String stepResultConvert(String code) {
		switch (code) {
		case "1":
			return "驳回";
		case "3":
			return "撤销";
		case "11":
			return "转办";
		default:
			return "通过";
		}
	}

	private String workResultConvert(String code) {
		switch (code) {
		case "0":
			return "进行中";
		case "1":
			return "通过";
		case "2":
			return "驳回";
		case "3":
			return "撤销";
		default:
			return "";
		}
	}

	@Override
	public void OperationWorkflow(OperationWorkflowVo operationWorkflowVo, TokenModel tokenModel)
			throws LogicalException {
		try {
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
//			this.restTemplate.getForObject("http://" + RequestUtil.MESSAGE + "/tenantTask/revokeTask?dataid="
//					+ operationWorkflowVo.getDataId() + "&userid=" + operationWorkflowVo.getUserid() + "&tenantid="
//					+ operationWorkflowVo.getTenantid(), JSONObject.class);
			Workflownodeinstance nodeinstance = workflownodeinstanceMapper
					.selectByPrimaryKey(workflowstep.getWorkflownodeinstanceid());
			Workflowinstance workflowinstance = workflowinstanceMapper
					.selectByPrimaryKey(nodeinstance.getWorkflowinstanceid());
			// 当操作为驳回时
			if ("1".equals(operationWorkflowVo.getResult())) {
				// 结束当前流程 并更新状态为驳回 status 为2
				workflowinstanceMapper.stopInstanceByReject(workflowstep.getWorkflownodeinstanceid(), "2");
				// 结束所有代办
//				this.restTemplate.getForObject(
//						"http://" + RequestUtil.MESSAGE + "/tenantTask/revokeTask?dataid="
//								+ operationWorkflowVo.getDataId() + "&tenantid=" + operationWorkflowVo.getTenantid(),
//						JSONObject.class);
			}

			// 转办&指定审批人
			else if ("11".equals(operationWorkflowVo.getResult()) || "5".equals(operationWorkflowVo.getResult())) {
				// 创建转办节点
				Workflowstep step = new Workflowstep();
				step.setWorkflowstepid(UUID.randomUUID().toString());
				step.setWorkflownodeinstanceid(workflowstep.getWorkflownodeinstanceid());
				if ("11".equals(operationWorkflowVo.getResult())) {

					step.setName("转办");
				} else {

					step.setName("指定");
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
//
//					HttpHeaders headers = new HttpHeaders();
//					Enumeration<String> headerNames = request.getHeaderNames();
//					while (headerNames.hasMoreElements()) {
//						String key = (String) headerNames.nextElement();
//						String value = request.getHeader(key);
//						headers.add(key, value);
//					}
//					HttpEntity<TenanttaskVo> requestEntity = new HttpEntity<TenanttaskVo>(tenanttaskVo, headers);
//					restTemplate.postForObject("http://" + RequestUtil.MESSAGE + "/tenantTask/insertTask",
//							requestEntity, ApiResult.class);
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
					operationWorkflowVo.getMenuUrl(), workflowinstance.getWorkflowname());
		} catch (Exception e) {
			log.warn(e.getMessage());
			throw new LogicalException("操作失败！");
		}
	}

	private void cresteStep(String instanceId, TokenModel tokenModel, String dataId, String url,
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
//						TenanttaskVo tenanttaskVo = new TenanttaskVo();
//						tenanttaskVo.setDataurl(url + "?wfid=" + dataId);
//						tenanttaskVo.setType("1");
//						tenanttaskVo.setTitle("您有一个【" + workflowname + "】审批待处理！");
//						tenanttaskVo.setFromuser(user);
//						tenanttaskVo.setContent(dataId);
//						tenanttaskVo.setCreateby(user);
//						tenanttaskVo.setOwner(user);
//						tenanttaskVo.setTenantid(workflowinstance.getTenantid());
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
//					for (String user : item.getCc().split(",")) {
//						TenanttaskVo tenanttaskVo = new TenanttaskVo();
//						tenanttaskVo.setDataurl(url + "?wfid=" + dataId);
//						tenanttaskVo.setType("1");
//						tenanttaskVo.setTitle("有一个【" + workflowname + "】审批已处理！");
//						tenanttaskVo.setFromuser(user);
//						tenanttaskVo.setContent(dataId);
//						tenanttaskVo.setCreateby(user);
//						tenanttaskVo.setOwner(user);
//						tenanttaskVo.setTenantid(workflowinstance.getTenantid());
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
//					}
					// 创建指定人员代办

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

//					// 循环节点_未回到循环开始节点
//					if (!StringUtils.isNullOrEmpty(item.getBackitemid())
//							&& !item.getBackitemid().equals(tokenModel.getUserId())) {
//						// 循环开始人创建代办
//						// 创建节点
//						Workflowstep workflowstep = new Workflowstep();
//						workflowstep.setWorkflowstepid(UUID.randomUUID().toString());
//						workflowstep.setWorkflownodeinstanceid(item.getWorkflownodeinstanceid());
//						workflowstep.setName(item.getNodename());
//						workflowstep.setItemid(item.getBackitemid());
//						workflowstep.setCreateby(item.getBackitemid());
//						workflowstep.setCreateon(new Date());
//						workflowstep.setOwner(item.getBackitemid());
//						workflowstep.setStatus(AuthConstants.DEL_FLAG_NORMAL);
//						workflowstep.setTenantid(workflowinstance.getTenantid());
//						workflowstepMapper.insert(workflowstep);
//
//						// 创建代办
////						TenanttaskVo tenanttaskVo = new TenanttaskVo();
////						tenanttaskVo.setDataurl(url + "?wfid=" + dataId);
////						tenanttaskVo.setType("1");
////						tenanttaskVo.setTitle("您有一个【" + workflowname + "】审批待处理！");
////						tenanttaskVo.setFromuser(item.getBackitemid());
////						tenanttaskVo.setContent(dataId);
////						tenanttaskVo.setCreateby(item.getBackitemid());
////						tenanttaskVo.setOwner(item.getBackitemid());
////						tenanttaskVo.setTenantid(workflowinstance.getTenantid());
////
////						HttpHeaders headers = new HttpHeaders();
////						Enumeration<String> headerNames = request.getHeaderNames();
////						while (headerNames.hasMoreElements()) {
////							String key = (String) headerNames.nextElement();
////							String value = request.getHeader(key);
////							headers.add(key, value);
////						}
////						HttpEntity<TenanttaskVo> requestEntity = new HttpEntity<TenanttaskVo>(tenanttaskVo, headers);
////						restTemplate.postForObject("http://" + RequestUtil.MESSAGE + "/tenantTask/insertTask",
////								requestEntity, ApiResult.class);
//						return;
//					}

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
	public void StartWorkflow(StartWorkflowVo startWorkflowVo, TokenModel tokenModel) throws LogicalException {
		try {
			// 创建流程实例
			Workflow workflow = workflowMapper.selectByPrimaryKey(startWorkflowVo.getWorkFlowId());
			Workflowinstance workflowinstance = new Workflowinstance();
			BeanUtils.copyProperties(workflow, workflowinstance);
			workflowinstance.setWorkflowinstanceid(UUID.randomUUID().toString());
			workflowinstance.setDataid(startWorkflowVo.getDataId());
			workflowinstance.setFormid(startWorkflowVo.getMenuUrl());
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
					startWorkflowVo.getMenuUrl(), workflow.getWorkflowname());
		} catch (Exception e) {
			log.warn(e.getMessage());
			throw new LogicalException("操作失败！");
		}
	}

	@Override
	public String isDelWorkflow(StartWorkflowVo startWorkflowVo) throws LogicalException {

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
	public void DelWorkflow(String instanceId) throws LogicalException {
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
				it.setRemark("流程备发起人撤销！");
				it.setModifyby(workflowinstance.getCreateby());
				it.setModifyon(new Date());
				workflowstepMapper.updateByPrimaryKeySelective(it);
			}
		}
		// 结束所有代办
//		this.restTemplate.getForObject("http://" + RequestUtil.MESSAGE + "/tenantTask/revokeTask?dataid="
//				+ workflowinstance.getDataid() + "&tenantid=" + workflowinstance.getTenantid(), JSONObject.class);
	}

}
