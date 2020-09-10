package com.nt.service_WorkFlow.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.mysql.jdbc.StringUtils;
import com.nt.dao_Org.OrgTree;
import com.nt.dao_Org.ToDoNotice;
import com.nt.dao_Org.Vo.UserVo;
import com.nt.dao_Workflow.Vo.*;
import com.nt.dao_Workflow.*;
import com.nt.service_Org.OrgTreeService;
import com.nt.service_Org.ToDoNoticeService;
import com.nt.service_Org.UserService;
import com.nt.service_WorkFlow.WorkflowServices;
import com.nt.service_WorkFlow.mapper.*;
import com.nt.utils.AuthConstants;
import com.nt.utils.LogicalException;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.dao.TokenModel;
import org.apache.commons.beanutils.ConvertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class WorkflowServicesImpl implements WorkflowServices {

    private static Logger log = LoggerFactory.getLogger(WorkflowServicesImpl.class);
    private String upFlg = "0";
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

    @Autowired
    private UserService userService;

    @Autowired
    private OrgTreeService orgTreeService;

    @Override
    public void insert(WorkflowVo workflowVo, TokenModel tokenModel) throws Exception {
        Workflow workflow = new Workflow();
        BeanUtil.copyProperties(workflowVo, workflow);
        workflow.preInsert(tokenModel);
        workflow.setWorkflowid(UUID.randomUUID().toString());
        workflowMapper.insert(workflow);

        for (Workflownode item : workflowVo.getNodeList()) {
            Workflownode workflownode = new Workflownode();
            BeanUtil.copyProperties(item, workflownode);
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
        BeanUtil.copyProperties(workflowVo, workflow);
        workflowMapper.updateByPrimaryKeySelective(workflow);
        Workflownode del = new Workflownode();
        del.setWorkflowid(workflow.getWorkflowid());
        workflownodeMapper.delete(del);

        for (Workflownode item : workflowVo.getNodeList()) {
            Workflownode workflownode = new Workflownode();
            BeanUtil.copyProperties(item, workflownode);
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
        BeanUtil.copyProperties(workflow, workflowVo);
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
    public Map<String, Object> isStartWorkflow(StartWorkflowVo startWorkflowVo, TokenModel tokenModel)
            throws Exception {
        Map<String, Object> rst = new HashMap<String, Object>();
        Workflow workflow = new Workflow();
        workflow.setFormid(startWorkflowVo.getMenuUrl());
//        workflow.setTenantid(startWorkflowVo.getTenantId());
        workflow.setStatus(AuthConstants.DEL_FLAG_NORMAL);

//        workflow = RequestUtils.CurrentPageOwnerList(tokenModel, workflow);

        List<Workflow> Workflowlist = workflowMapper.select(workflow);
        if (Workflowlist.size() > 0) {
            Workflowinstance workflowinstance = new Workflowinstance();
            workflowinstance.setDataid(startWorkflowVo.getDataId());
            workflowinstance.setFormid(startWorkflowVo.getMenuUrl());
            workflowinstance.setTenantid(startWorkflowVo.getTenantId());
            // workflowinstance.setStatus(AuthConstants.DEL_FLAG_NORMAL);
            List<Workflowinstance> list = workflowinstanceMapper.select(workflowinstance);
            if (list.size() == 0) {
                rst.put("list", Workflowlist);
                rst.put("can", "0");
                return rst;
            } else if (list.size() > 0 && !(list.stream().filter(item -> "0".equals(item.getStatus())).count() > 0
                    || list.stream().filter(item -> "4".equals(item.getStatus())).count() > 0)) {
                rst.put("list", Workflowlist);
                rst.put("can", "0");
                return rst;
            } else {
                if (list.size() > 0 && list.stream().filter(item -> "0".equals(item.getStatus())).count() > 0) {
                    rst.put("list", Workflowlist);
                    rst.put("can", "1");
                    return rst;
                } else {
                    rst.put("list", Workflowlist);
                    rst.put("can", "2");
                    return rst;
                }

            }
        }

        rst.put("list", Workflowlist);
        rst.put("can", "3");
        return rst;

    }

    @Override
    public Map<String, String> isOperationWorkflow(StartWorkflowVo startWorkflowVo) throws Exception {
        Workflowinstance workflowinstance = new Workflowinstance();
        workflowinstance.setDataid(startWorkflowVo.getDataId());
        workflowinstance.setFormid(startWorkflowVo.getMenuUrl());
//        workflowinstance.setTenantid(startWorkflowVo.getTenantId());
        workflowinstance.setStatus(AuthConstants.DEL_FLAG_NORMAL);
        List<Workflowinstance> Workflowinstancelist = workflowinstanceMapper.select(workflowinstance);
        if (Workflowinstancelist.size() > 0) {
            Workflownodeinstance workflownodeinstance = new Workflownodeinstance();
            workflownodeinstance.setWorkflowinstanceid(Workflowinstancelist.get(0).getWorkflowinstanceid());
            workflownodeinstance.setStatus(AuthConstants.DEL_FLAG_NORMAL);
//            workflownodeinstance.setTenantid(startWorkflowVo.getTenantId());
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
                    rst.put("nodeusertype", item.getNodeusertype());
                    rst.put("remarks", item.getRemarks());
                    rst.put("nodeord", item.getNodeord().toString());
                    rst.put("code", Workflowinstancelist.get(0).getCode());
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
//        workflow.setTenantid(startWorkflowVo.getTenantId());
        rst = workflowMapper.select(workflow).size() > 0;
        if (rst) {
            Workflowinstance workflowinstance = new Workflowinstance();
            workflowinstance.setDataid(startWorkflowVo.getDataId());
            workflowinstance.setFormid(startWorkflowVo.getMenuUrl());
            workflowinstance.setTenantid(startWorkflowVo.getTenantId());
            rst = workflowinstanceMapper.select(workflowinstance).size() > 0;
        }

        return rst;
    }

    @Override
    public List<WorkflowLogVo> ViewWorkflow(StartWorkflowVo startWorkflowVo, String locale) throws Exception {
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
                endnode.setStepStatus(MessageUtil.getMessage(MsgConstants.WORKFLOW_03, locale));
                endnode.setTitle(MessageUtil.getMessage(MsgConstants.WORKFLOW_01, locale));
                WorkflowLogDetailVo workflowLogDetailVo = new WorkflowLogDetailVo();
                workflowLogDetailVo.setResult(MessageUtil.getMessage(MsgConstants.WORKFLOW_01, locale));
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
                    workflowLogDetailVo.setResult(MessageUtil.getMessage(MsgConstants.WORKFLOW_04, locale));
                } else {
                    workflowLogDetailVo.setResult(stepResultConvert(it.getResult(), locale));
                }
                if ("3".equals(it.getResult())) {

                    workflowLogDetailVo.setUserId(it.getCreateby());
                } else {

                    workflowLogDetailVo.setUserId(it.getItemid());
                }
                workflowLogDetailVo.setRemark(it.getRemark());
                workflowLogDetailVo.setSdata(it.getCreateon());
                workflowLogDetailVo.setEdata(it.getModifyon());

                item.getDetail().add(workflowLogDetailVo);
                if (!StringUtils.isNullOrEmpty(it.getResult())
                        && (it.getResult().equals("0") || it.getResult().equals("11"))) {
                    approval++;
                } else if (!StringUtils.isNullOrEmpty(it.getResult()) && it.getResult().equals("1")) {
                    nodeStatus = MessageUtil.getMessage(MsgConstants.WORKFLOW_05, locale);
                } else if (!StringUtils.isNullOrEmpty(it.getResult()) && it.getResult().equals("3")) {
                    nodeStatus = MessageUtil.getMessage(MsgConstants.WORKFLOW_06, locale);
                } else {
                    nodeStatus = MessageUtil.getMessage(MsgConstants.WORKFLOW_04, locale);
                }
            }
            if (approval != 0 && approval == Workflowsteplist.size())
                nodeStatus = MessageUtil.getMessage(MsgConstants.WORKFLOW_03, locale);

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

                    endnode.setStepStatus(workResultConvert(workflowinstancelist.get(0).getStatus(), locale));
                    workflowLogDetailVo.setResult(workResultConvert(workflowinstancelist.get(0).getStatus(), locale));
                }
                endnode.setTitle(MessageUtil.getMessage(MsgConstants.WORKFLOW_02, locale));

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
    public List<WorkflowLogDetailVo> ViewWorkflow2(StartWorkflowVo startWorkflowVo, String locale) throws Exception {
        List<WorkflowLogDetailVo> rst = new ArrayList<WorkflowLogDetailVo>();

        Workflowinstance workflowinstance = new Workflowinstance();
        workflowinstance.setDataid(startWorkflowVo.getDataId());
        workflowinstance.setFormid(startWorkflowVo.getMenuUrl());
        workflowinstance.setTenantid(startWorkflowVo.getTenantId());
        List<Workflowinstance> Workflowinstancelist = workflowinstanceMapper.select(workflowinstance);
        Workflowinstancelist = Workflowinstancelist.stream()
                .sorted(Comparator.comparing(Workflowinstance::getCreateon).reversed()).collect(Collectors.toList());
        for (Workflowinstance item : Workflowinstancelist) {
            Workflownodeinstance workflownodeinstance = new Workflownodeinstance();
            workflownodeinstance.setWorkflowinstanceid(item.getWorkflowinstanceid());
            List<Workflownodeinstance> workflownodeinstancelist = workflownodeinstanceMapper.select(workflownodeinstance);
            workflownodeinstancelist = workflownodeinstancelist.stream()
                    .sorted(Comparator.comparing(Workflownodeinstance::getNodeord).reversed()).collect(Collectors.toList());
            for (Workflownodeinstance node : workflownodeinstancelist) {

                Workflowstep workflowstep = new Workflowstep();
                workflowstep.setWorkflownodeinstanceid(node.getWorkflownodeinstanceid());
                List<Workflowstep> Workflowsteplist = workflowstepMapper.select(workflowstep);

                for (Workflowstep it : Workflowsteplist) {
                    WorkflowLogDetailVo workflowLogDetailVo = new WorkflowLogDetailVo();
                    if (StringUtils.isNullOrEmpty(it.getResult())) {
                        workflowLogDetailVo.setResult(MessageUtil.getMessage(MsgConstants.WORKFLOW_04, locale));
                    } else {
                        workflowLogDetailVo.setResult(stepResultConvert(it.getResult(), locale));
                    }
                    if ("3".equals(it.getResult())) {

                        workflowLogDetailVo.setUserId(it.getCreateby());
                    } else {

                        workflowLogDetailVo.setUserId(it.getItemid());
                    }
                    workflowLogDetailVo.setRemark(it.getRemark());
                    workflowLogDetailVo.setSdata(it.getCreateon());
                    workflowLogDetailVo.setEdata(it.getModifyon());
                    workflowLogDetailVo.setIsvirtual("1");
                    rst.add(workflowLogDetailVo);
                }

                if (node == workflownodeinstancelist.get(workflownodeinstancelist.size() - 1)) {

                    WorkflowLogDetailVo workflowLogDetailVo = new WorkflowLogDetailVo();
                    workflowLogDetailVo.setResult(MessageUtil.getMessage(MsgConstants.WORKFLOW_01, locale));
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

    @Override
    public List<Workflowinstance> allWorkFlowIns(String menuUrl) throws Exception {
        Workflowinstance con = new Workflowinstance();
        if (StrUtil.isNotBlank(menuUrl)) {
            con.setFormid(menuUrl);
        }
        List<Workflowinstance> rst = workflowinstanceMapper.select(con);
        for (Workflowinstance item : rst) {
            item.setStatus(workResultConvert(item.getStatus(), ""));
        }

        return rst;
    }

    //ccm 20200713 离职 获取离职人员离职月的考勤审批情况
    @Override
    public List<Workflowinstance> oneWorkFlowIns(String menuUrl,String dataid) throws Exception {
        Workflowinstance con = new Workflowinstance();
        if (StrUtil.isNotBlank(menuUrl)) {
            con.setFormid(menuUrl);
        }
        if (StrUtil.isNotBlank(dataid)) {
            con.setDataid(dataid);
        }
        List<Workflowinstance> rst = workflowinstanceMapper.select(con);
        for (Workflowinstance item : rst) {
            item.setStatus(workResultConvert(item.getStatus(), ""));
        }

        return rst;
    }
    //ccm 20200713 离职 获取离职人员离职月的考勤审批情况


    private String stepResultConvert(String code, String locale) {
        switch (code) {
            case "1":
                return MessageUtil.getMessage(MsgConstants.WORKFLOW_05, locale);
            case "3":
                return MessageUtil.getMessage(MsgConstants.WORKFLOW_06, locale);
            case "11":
                return MessageUtil.getMessage(MsgConstants.WORKFLOW_07, locale);
            default:
                return MessageUtil.getMessage(MsgConstants.WORKFLOW_03, locale);
        }
    }

    private String workResultConvert(String code, String locale) {
        switch (code) {
            case "0":
                return MessageUtil.getMessage(MsgConstants.WORKFLOW_04, locale);
            case "4":
                return MessageUtil.getMessage(MsgConstants.WORKFLOW_03, locale);
            case "2":
                return MessageUtil.getMessage(MsgConstants.WORKFLOW_05, locale);
            case "3":
                return MessageUtil.getMessage(MsgConstants.WORKFLOW_06, locale);
            default:
                return "";
        }
    }

    @Override
    public OutOperationWorkflowVo OperationWorkflow(OperationWorkflowVo operationWorkflowVo, TokenModel tokenModel)
            throws Exception {
        OutOperationWorkflowVo outOperationWorkflowVo = new OutOperationWorkflowVo();
        // 更新当前节点
        Workflowstep workflowstep = workflowstepMapper.selectByPrimaryKey(operationWorkflowVo.getId());
        // 同意&拒绝
        if ("3".equals(operationWorkflowVo.getResult()) || "4".equals(operationWorkflowVo.getResult())) {
            operationWorkflowVo.setResult("0");
        }
        workflowstep.setResult(operationWorkflowVo.getResult());
        workflowstep.setRemark(operationWorkflowVo.getRemark());
        workflowstep.setModifyby(tokenModel.getUserId());
        workflowstep.setModifyon(new Date());
        workflowstep.setStatus(AuthConstants.APPROVED_FLAG_YES);
        workflowstepMapper.updateByPrimaryKeySelective(workflowstep);

        // 结束当前代办
        ToDoNotice toDoNotice1 = new ToDoNotice();
        toDoNotice1.setDataid(operationWorkflowVo.getDataId());
        toDoNotice1.setUrl(operationWorkflowVo.getDataUrl());
        toDoNotice1.setOwner(tokenModel.getUserId());
        toDoNotice1.setStatus(AuthConstants.DEL_FLAG_NORMAL);
        List<ToDoNotice> rst1 = toDoNoticeService.get(toDoNotice1);
        for (ToDoNotice item :
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
            for (ToDoNotice item :
                    rst) {
                item.setStatus(AuthConstants.TODO_STATUS_DELETE);
                toDoNoticeService.updateNoticesStatus(item);
            }

            //发起人创建代办
            // 创建代办
            toDoNotice = new ToDoNotice();
            List<String> params = new ArrayList<String>();
            toDoNotice.setTitle("您有一个审批被驳回！");
            toDoNotice.setInitiator(workflowinstance.getOwner());
            toDoNotice.setContent("您有一个审批被驳回！");
            toDoNotice.setDataid(operationWorkflowVo.getDataId());
            toDoNotice.setUrl(operationWorkflowVo.getDataUrl());
            toDoNotice.setWorkflowurl(operationWorkflowVo.getMenuUrl());
            toDoNotice.preInsert(tokenModel);
            toDoNotice.setOwner(nodeinstance.getCreateby());
            toDoNoticeService.save(toDoNotice);

            outOperationWorkflowVo.setState("1");
            outOperationWorkflowVo.setWorkflowCode(workflowinstance.getCode());
            return outOperationWorkflowVo;
        }

        // 转办
        else if ("11".equals(operationWorkflowVo.getResult())) {
            // 创建转办节点
            Workflowstep step = new Workflowstep();
            step.setWorkflowstepid(UUID.randomUUID().toString());
            step.setWorkflownodeinstanceid(workflowstep.getWorkflownodeinstanceid());
            if ("11".equals(operationWorkflowVo.getResult())) {

                step.setName(MessageUtil.getMessage(MsgConstants.WORKFLOW_07, tokenModel.getLocale()));
            }

            step.setCreateby(tokenModel.getUserId());
            step.setCreateon(new Date());
            step.setStatus(AuthConstants.DEL_FLAG_NORMAL);
            step.setTenantid(operationWorkflowVo.getTenantid());
            step.setItemid(operationWorkflowVo.getToAnotherUser());
            step.setOwner(operationWorkflowVo.getToAnotherUser());
            workflowstepMapper.insert(step);

        }
        // 生成下一节点信息

        return cresteStep(nodeinstance.getWorkflowinstanceid(), tokenModel, operationWorkflowVo.getDataId(),
                operationWorkflowVo.getDataUrl(), operationWorkflowVo.getMenuUrl(), workflowinstance.getWorkflowname(), operationWorkflowVo.getUserlist());

    }

    private String getUpUser(String curentUser, String nodeName) throws Exception {
        String userId = "";
        UserVo user = userService.getAccountCustomerById(curentUser);
        String orgId = "";
        if (nodeName.toUpperCase().contains("TL")) {
            orgId = user.getCustomerInfo().getUserinfo().getTeamid();
        } else if (nodeName.toUpperCase().contains("GM")) {
            orgId = user.getCustomerInfo().getUserinfo().getGroupid();
        } else if (nodeName.toUpperCase().contains("CENTER")) {
            orgId = user.getCustomerInfo().getUserinfo().getCenterid();
        } else if (nodeName.toUpperCase().contains("一次上司") || nodeName.toUpperCase().contains("二次上司")|| nodeName.toUpperCase().contains("三次上司")) {
            if (StrUtil.isNotBlank(user.getCustomerInfo().getUserinfo().getTeamid())) {
                orgId = user.getCustomerInfo().getUserinfo().getTeamid();
            } else if (StrUtil.isNotBlank(user.getCustomerInfo().getUserinfo().getGroupid())) {
                orgId = user.getCustomerInfo().getUserinfo().getGroupid();
            } else if (StrUtil.isNotBlank(user.getCustomerInfo().getUserinfo().getCenterid())) {
                orgId = user.getCustomerInfo().getUserinfo().getCenterid();
            }
        }

        OrgTree orgs = orgTreeService.get(new OrgTree());

        OrgTree currentOrg = getCurrentOrg(orgs, orgId);

        if (currentOrg.getUser() == null || StrUtil.isEmpty(currentOrg.getUser())) {
            throw new LogicalException("无上级人员信息！");
        }
        if (currentOrg.getUser().equals(curentUser)) {
            upFlg = "0";
            OrgTree upOrgs = upCurrentOrg(orgs, orgId);
            if (upOrgs != null) {
                userId = upOrgs.getUser();

                if (nodeName.toUpperCase().contains("二次上司") || nodeName.toUpperCase().contains("三次上司")){
                    upFlg = "0";
                    upOrgs = upCurrentOrg(orgs, upOrgs.get_id());
                    if (upOrgs != null) {
                        userId = upOrgs.getUser();
                    }else{
                        userId = "";
                    }

                    if(nodeName.toUpperCase().contains("三次上司")){
                        upFlg = "0";
                        upOrgs = upCurrentOrg(orgs, upOrgs.get_id());
                        if (upOrgs != null) {
                            userId = upOrgs.getUser();
                        }else{
                            userId = "";
                        }
                    }
                }
            }
        } else {
            userId = currentOrg.getUser();

            if (nodeName.toUpperCase().contains("二次上司")){
                upFlg = "0";
                OrgTree upOrgs = upCurrentOrg(orgs, orgId);
                if (upOrgs != null) {
                    userId = upOrgs.getUser();
                }else{
                    userId = "";
                }
            }
        }
//
//        if(userId == null || StrUtil.isEmpty(userId)){
//            throw new LogicalException("无上级人员信息！");
//        }

        return userId;
    }

    private OrgTree getCurrentOrg(OrgTree org, String orgId) throws Exception {
        if (org.get_id().equals(orgId)) {
            return org;
        } else {
            if (org.getOrgs() != null) {
                for (OrgTree item : org.getOrgs()) {
                    OrgTree or = getCurrentOrg(item, orgId);
                    if (or.get_id().equals(orgId)) {
                        return or;
                    }
                }
            }

        }
        return org;
    }

    private OrgTree upCurrentOrg(OrgTree org, String orgId) throws Exception {
        if (org.get_id().equals(orgId)) {
            return null;
        } else {
            if (org.getOrgs() != null) {
                for (OrgTree item : org.getOrgs()) {
                    OrgTree rst = upCurrentOrg(item, orgId);
                    if (rst == null) {
                        upFlg = "1";
                        return org;
                    }
                    if (upFlg == "1") {
                        return rst;
                    }
                }
            }
        }
        return org;
    }

    // 0:进行中
    // 1：拒绝
    // 2：通过
    private OutOperationWorkflowVo cresteStep(String instanceId, TokenModel tokenModel, String dataId, String url, String workFlowurl,
                                              String workflowname, List<String> userList) throws Exception {
        OutOperationWorkflowVo outOperationWorkflowVo = new OutOperationWorkflowVo();
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
                    //指定审批人
                    if ("1".equals(item.getNodeusertype())) {
                        for (String user : item.getItemid().split(",")) {


                            // 创建节点
                            Workflowstep workflowstep = new Workflowstep();
                            workflowstep.setWorkflowstepid(UUID.randomUUID().toString());
                            workflowstep.setWorkflownodeinstanceid(item.getWorkflownodeinstanceid());
                            workflowstep.setName(item.getNodename());
                            workflowstep.setItemid(user);
                            workflowstep.preInsert(tokenModel);
                            //通知节点
                            if ("3".equals(item.getNodetype())) {
                                workflowstep.setResult("0");
                                workflowstep.setRemark("通知节点");
                                workflowstep.setModifyby(tokenModel.getUserId());
                                workflowstep.setModifyon(new Date());
                                workflowstep.setStatus(AuthConstants.APPROVED_FLAG_YES);
                            }
                            workflowstepMapper.insert(workflowstep);


                            // 创建代办
                            ToDoNotice toDoNotice = new ToDoNotice();
                            List<String> params = new ArrayList<String>();
                            params.add(workflowname);
                            if (!"3".equals(item.getNodetype()) || ("3".equals(item.getNodetype()) && StrUtil.isEmpty(item.getRemarks()))) {
                                toDoNotice.setTitle(MessageUtil.getMessage(MsgConstants.WORKFLOW_10, params, tokenModel.getLocale()));
                            } else {
                                toDoNotice.setTitle(item.getRemarks());
                            }

                            toDoNotice.setInitiator(workflowinstance.getOwner());
                            toDoNotice.setContent(item.getNodename());
                            toDoNotice.setDataid(dataId);
                            toDoNotice.setUrl(url);
                            toDoNotice.setWorkflowurl(workFlowurl);
                            toDoNotice.preInsert(tokenModel);
                            toDoNotice.setOwner(user);
                            toDoNoticeService.save(toDoNotice);
                        }
                        if ("3".equals(item.getNodetype())) {

                            // 如果节点为最后一个节点时，结束流程
                            if (item == workflownodeinstancelist.get(workflownodeinstancelist.size() - 1)) {
                                workflowinstance.setModifyby(tokenModel.getUserId());
                                workflowinstance.setModifyon(new Date());
                                workflowinstance.setStatus(AuthConstants.APPROVED_FLAG_YES);
                                workflowinstanceMapper.updateByPrimaryKeySelective(workflowinstance);
                                outOperationWorkflowVo.setState("2");
                                outOperationWorkflowVo.setWorkflowCode(workflowinstance.getCode());

                                ToDoNotice toDoNotice = new ToDoNotice();
                                List<String> params = new ArrayList<String>();
                                params.add(workflowname);
                                toDoNotice.setTitle(MessageUtil.getMessage(MsgConstants.WORKFLOW_11, params, tokenModel.getLocale()));
                                toDoNotice.setInitiator(workflowinstance.getOwner());
                                toDoNotice.setContent(item.getNodename());
                                toDoNotice.setDataid(dataId);
                                toDoNotice.setUrl(url);
                                toDoNotice.setWorkflowurl(workFlowurl);
                                toDoNotice.preInsert(tokenModel);
                                toDoNotice.setOwner(workflowinstance.getOwner());
                                toDoNoticeService.save(toDoNotice);

                                return outOperationWorkflowVo;
                            }

                            continue;
                        }
                        //上级审批
                    } else if ("2".equals(item.getNodeusertype())) {
                        String user = "";


                        UserVo userInfo = userService.getAccountCustomerById(workflowinstance.getOwner());
                        //TL
                        if (item.getNodename().toUpperCase().contains("TL")) {
                            if (StrUtil.isNotBlank(userInfo.getCustomerInfo().getUserinfo().getTeamid())) {
                                OrgTree orgs = orgTreeService.get(new OrgTree());
                                OrgTree currentOrg = getCurrentOrg(orgs, userInfo.getCustomerInfo().getUserinfo().getTeamid());
                                if (currentOrg.getUser() == null || StrUtil.isEmpty(currentOrg.getUser())) {
                                    throw new LogicalException("无上级人员信息！");
                                }
                                if (currentOrg.getUser().equals(tokenModel.getUserId())) {
                                    Workflowstep workflowstep = new Workflowstep();
                                    workflowstep.setWorkflowstepid(UUID.randomUUID().toString());
                                    workflowstep.setWorkflownodeinstanceid(item.getWorkflownodeinstanceid());
                                    workflowstep.setName(item.getNodename());
                                    workflowstep.setItemid(tokenModel.getUserId());
                                    workflowstep.setResult("0");
                                    workflowstep.setRemark("系统自动跳过");
                                    workflowstep.setModifyby(tokenModel.getUserId());
                                    workflowstep.setModifyon(new Date());
                                    workflowstep.preInsert(tokenModel);
                                    workflowstep.setStatus(AuthConstants.APPROVED_FLAG_YES);
                                    workflowstepMapper.insert(workflowstep);

                                    // 如果节点为最后一个节点时，结束流程
                                    if (item == workflownodeinstancelist.get(workflownodeinstancelist.size() - 1)) {
                                        workflowinstance.setModifyby(tokenModel.getUserId());
                                        workflowinstance.setModifyon(new Date());
                                        workflowinstance.setStatus(AuthConstants.APPROVED_FLAG_YES);
                                        workflowinstanceMapper.updateByPrimaryKeySelective(workflowinstance);
                                        outOperationWorkflowVo.setState("2");
                                        outOperationWorkflowVo.setWorkflowCode(workflowinstance.getCode());

                                        ToDoNotice toDoNotice = new ToDoNotice();
                                        List<String> params = new ArrayList<String>();
                                        params.add(workflowname);
                                        toDoNotice.setTitle(MessageUtil.getMessage(MsgConstants.WORKFLOW_11, params, tokenModel.getLocale()));
                                        toDoNotice.setInitiator(workflowinstance.getOwner());
                                        toDoNotice.setContent(item.getNodename());
                                        toDoNotice.setDataid(dataId);
                                        toDoNotice.setUrl(url);
                                        toDoNotice.setWorkflowurl(workFlowurl);
                                        toDoNotice.preInsert(tokenModel);
                                        toDoNotice.setOwner(workflowinstance.getOwner());
                                        toDoNoticeService.save(toDoNotice);

                                        return outOperationWorkflowVo;
                                    }

                                    continue;
                                }
                            } else {
                                Workflowstep workflowstep = new Workflowstep();
                                workflowstep.setWorkflowstepid(UUID.randomUUID().toString());
                                workflowstep.setWorkflownodeinstanceid(item.getWorkflownodeinstanceid());
                                workflowstep.setName(item.getNodename());
                                workflowstep.setItemid(tokenModel.getUserId());
                                workflowstep.setResult("0");
                                workflowstep.setRemark("系统自动跳过");
                                workflowstep.setModifyby(tokenModel.getUserId());
                                workflowstep.setModifyon(new Date());

                                workflowstep.preInsert(tokenModel);
                                workflowstep.setStatus(AuthConstants.APPROVED_FLAG_YES);
                                workflowstepMapper.insert(workflowstep);

                                // 如果节点为最后一个节点时，结束流程
                                if (item == workflownodeinstancelist.get(workflownodeinstancelist.size() - 1)) {
                                    workflowinstance.setModifyby(tokenModel.getUserId());
                                    workflowinstance.setModifyon(new Date());
                                    workflowinstance.setStatus(AuthConstants.APPROVED_FLAG_YES);
                                    workflowinstanceMapper.updateByPrimaryKeySelective(workflowinstance);
                                    outOperationWorkflowVo.setState("2");
                                    outOperationWorkflowVo.setWorkflowCode(workflowinstance.getCode());

                                    ToDoNotice toDoNotice = new ToDoNotice();
                                    List<String> params = new ArrayList<String>();
                                    params.add(workflowname);
                                    toDoNotice.setTitle(MessageUtil.getMessage(MsgConstants.WORKFLOW_11, params, tokenModel.getLocale()));
                                    toDoNotice.setInitiator(workflowinstance.getOwner());
                                    toDoNotice.setContent(item.getNodename());
                                    toDoNotice.setDataid(dataId);
                                    toDoNotice.setUrl(url);
                                    toDoNotice.setWorkflowurl(workFlowurl);
                                    toDoNotice.preInsert(tokenModel);
                                    toDoNotice.setOwner(workflowinstance.getOwner());
                                    toDoNoticeService.save(toDoNotice);

                                    return outOperationWorkflowVo;
                                }

                                continue;
                            }
                        }

                        // GM
                        if (item.getNodename().toUpperCase().contains("GM")) {
                            if (StrUtil.isEmpty(userInfo.getCustomerInfo().getUserinfo().getTeamid())) {

                                if (StrUtil.isNotBlank(userInfo.getCustomerInfo().getUserinfo().getGroupid())) {
                                    OrgTree orgs = orgTreeService.get(new OrgTree());
                                    OrgTree currentOrg = getCurrentOrg(orgs, userInfo.getCustomerInfo().getUserinfo().getGroupid());
                                    if (currentOrg.getUser() == null || StrUtil.isEmpty(currentOrg.getUser())) {
                                        throw new LogicalException("无上级人员信息！");
                                    }
                                    if (currentOrg.getUser().equals(tokenModel.getUserId())) {
                                        Workflowstep workflowstep = new Workflowstep();
                                        workflowstep.setWorkflowstepid(UUID.randomUUID().toString());
                                        workflowstep.setWorkflownodeinstanceid(item.getWorkflownodeinstanceid());
                                        workflowstep.setName(item.getNodename());
                                        workflowstep.setItemid(tokenModel.getUserId());
                                        workflowstep.setResult("0");
                                        workflowstep.setRemark("系统自动跳过");
                                        workflowstep.setModifyby(tokenModel.getUserId());
                                        workflowstep.setModifyon(new Date());

                                        workflowstep.preInsert(tokenModel);
                                        workflowstep.setStatus(AuthConstants.APPROVED_FLAG_YES);
                                        workflowstepMapper.insert(workflowstep);

                                        // 如果节点为最后一个节点时，结束流程
                                        if (item == workflownodeinstancelist.get(workflownodeinstancelist.size() - 1)) {
                                            workflowinstance.setModifyby(tokenModel.getUserId());
                                            workflowinstance.setModifyon(new Date());
                                            workflowinstance.setStatus(AuthConstants.APPROVED_FLAG_YES);
                                            workflowinstanceMapper.updateByPrimaryKeySelective(workflowinstance);
                                            outOperationWorkflowVo.setState("2");
                                            outOperationWorkflowVo.setWorkflowCode(workflowinstance.getCode());

                                            ToDoNotice toDoNotice = new ToDoNotice();
                                            List<String> params = new ArrayList<String>();
                                            params.add(workflowname);
                                            toDoNotice.setTitle(MessageUtil.getMessage(MsgConstants.WORKFLOW_11, params, tokenModel.getLocale()));
                                            toDoNotice.setInitiator(workflowinstance.getOwner());
                                            toDoNotice.setContent(item.getNodename());
                                            toDoNotice.setDataid(dataId);
                                            toDoNotice.setUrl(url);
                                            toDoNotice.setWorkflowurl(workFlowurl);
                                            toDoNotice.preInsert(tokenModel);
                                            toDoNotice.setOwner(workflowinstance.getOwner());
                                            toDoNoticeService.save(toDoNotice);

                                            return outOperationWorkflowVo;
                                        }

                                        continue;
                                    }
                                } else {
                                    Workflowstep workflowstep = new Workflowstep();
                                    workflowstep.setWorkflowstepid(UUID.randomUUID().toString());
                                    workflowstep.setWorkflownodeinstanceid(item.getWorkflownodeinstanceid());
                                    workflowstep.setName(item.getNodename());
                                    workflowstep.setItemid(tokenModel.getUserId());
                                    workflowstep.setResult("0");
                                    workflowstep.setRemark("系统自动跳过");
                                    workflowstep.setModifyby(tokenModel.getUserId());
                                    workflowstep.setModifyon(new Date());

                                    workflowstep.preInsert(tokenModel);
                                    workflowstep.setStatus(AuthConstants.APPROVED_FLAG_YES);
                                    workflowstepMapper.insert(workflowstep);

                                    // 如果节点为最后一个节点时，结束流程
                                    if (item == workflownodeinstancelist.get(workflownodeinstancelist.size() - 1)) {
                                        workflowinstance.setModifyby(tokenModel.getUserId());
                                        workflowinstance.setModifyon(new Date());
                                        workflowinstance.setStatus(AuthConstants.APPROVED_FLAG_YES);
                                        workflowinstanceMapper.updateByPrimaryKeySelective(workflowinstance);
                                        outOperationWorkflowVo.setState("2");
                                        outOperationWorkflowVo.setWorkflowCode(workflowinstance.getCode());

                                        ToDoNotice toDoNotice = new ToDoNotice();
                                        List<String> params = new ArrayList<String>();
                                        params.add(workflowname);
                                        toDoNotice.setTitle(MessageUtil.getMessage(MsgConstants.WORKFLOW_11, params, tokenModel.getLocale()));
                                        toDoNotice.setInitiator(workflowinstance.getOwner());
                                        toDoNotice.setContent(item.getNodename());
                                        toDoNotice.setDataid(dataId);
                                        toDoNotice.setUrl(url);
                                        toDoNotice.setWorkflowurl(workFlowurl);
                                        toDoNotice.preInsert(tokenModel);
                                        toDoNotice.setOwner(workflowinstance.getOwner());
                                        toDoNoticeService.save(toDoNotice);

                                        return outOperationWorkflowVo;
                                    }

                                    continue;
                                }
                            } else {
                                if (StrUtil.isNotBlank(userInfo.getCustomerInfo().getUserinfo().getGroupid())) {
                                    OrgTree orgs = orgTreeService.get(new OrgTree());
                                    OrgTree currentOrg = getCurrentOrg(orgs, userInfo.getCustomerInfo().getUserinfo().getGroupid());
                                    if (currentOrg.getUser() == null || StrUtil.isEmpty(currentOrg.getUser())) {
                                        throw new LogicalException("无上级人员信息！");
                                    }

                                    Workflowstep workflowstep = new Workflowstep();
                                    workflowstep.setWorkflowstepid(UUID.randomUUID().toString());
                                    workflowstep.setWorkflownodeinstanceid(item.getWorkflownodeinstanceid());
                                    workflowstep.setName(item.getNodename());
                                    workflowstep.setItemid(currentOrg.getUser());
                                    workflowstep.preInsert(tokenModel);
                                    workflowstepMapper.insert(workflowstep);


                                    // 创建代办
                                    ToDoNotice toDoNotice = new ToDoNotice();
                                    List<String> params = new ArrayList<String>();
                                    params.add(workflowname);
                                    toDoNotice.setTitle(MessageUtil.getMessage(MsgConstants.WORKFLOW_10, params, tokenModel.getLocale()));
                                    toDoNotice.setInitiator(workflowinstance.getOwner());
                                    toDoNotice.setContent(item.getNodename());
                                    toDoNotice.setDataid(dataId);
                                    toDoNotice.setUrl(url);
                                    toDoNotice.setWorkflowurl(workFlowurl);
                                    toDoNotice.preInsert(tokenModel);
                                    toDoNotice.setOwner(currentOrg.getUser());
                                    toDoNoticeService.save(toDoNotice);

                                    outOperationWorkflowVo.setState("0");
                                    outOperationWorkflowVo.setWorkflowCode(workflowinstance.getCode());
                                    return outOperationWorkflowVo;
                                }
                            }
                        }

                        //CENTER
                        if (item.getNodename().toUpperCase().contains("CENTER")) {
                            if (StrUtil.isEmpty(userInfo.getCustomerInfo().getUserinfo().getTeamid()) && StrUtil.isEmpty(userInfo.getCustomerInfo().getUserinfo().getGroupid())) {

                                if (StrUtil.isNotBlank(userInfo.getCustomerInfo().getUserinfo().getCenterid())) {
                                    OrgTree orgs = orgTreeService.get(new OrgTree());
                                    OrgTree currentOrg = getCurrentOrg(orgs, userInfo.getCustomerInfo().getUserinfo().getCenterid());
                                    if (currentOrg.getUser() == null || StrUtil.isEmpty(currentOrg.getUser())) {
                                        throw new LogicalException("无上级人员信息！");
                                    }
                                    if (currentOrg.getUser().equals(tokenModel.getUserId())) {
                                        Workflowstep workflowstep = new Workflowstep();
                                        workflowstep.setWorkflowstepid(UUID.randomUUID().toString());
                                        workflowstep.setWorkflownodeinstanceid(item.getWorkflownodeinstanceid());
                                        workflowstep.setName(item.getNodename());
                                        workflowstep.setItemid(tokenModel.getUserId());
                                        workflowstep.setResult("0");
                                        workflowstep.setRemark("系统自动跳过");
                                        workflowstep.setModifyby(tokenModel.getUserId());
                                        workflowstep.setModifyon(new Date());
                                        workflowstep.preInsert(tokenModel);

                                        workflowstep.setStatus(AuthConstants.APPROVED_FLAG_YES);
                                        workflowstepMapper.insert(workflowstep);

                                        // 如果节点为最后一个节点时，结束流程
                                        if (item == workflownodeinstancelist.get(workflownodeinstancelist.size() - 1)) {
                                            workflowinstance.setModifyby(tokenModel.getUserId());
                                            workflowinstance.setModifyon(new Date());
                                            workflowinstance.setStatus(AuthConstants.APPROVED_FLAG_YES);
                                            workflowinstanceMapper.updateByPrimaryKeySelective(workflowinstance);
                                            outOperationWorkflowVo.setState("2");
                                            outOperationWorkflowVo.setWorkflowCode(workflowinstance.getCode());

                                            ToDoNotice toDoNotice = new ToDoNotice();
                                            List<String> params = new ArrayList<String>();
                                            params.add(workflowname);
                                            toDoNotice.setTitle(MessageUtil.getMessage(MsgConstants.WORKFLOW_11, params, tokenModel.getLocale()));
                                            toDoNotice.setInitiator(workflowinstance.getOwner());
                                            toDoNotice.setContent(item.getNodename());
                                            toDoNotice.setDataid(dataId);
                                            toDoNotice.setUrl(url);
                                            toDoNotice.setWorkflowurl(workFlowurl);
                                            toDoNotice.preInsert(tokenModel);
                                            toDoNotice.setOwner(workflowinstance.getOwner());
                                            toDoNoticeService.save(toDoNotice);

                                            return outOperationWorkflowVo;
                                        }

                                        continue;
                                    }
                                }
                            }
                        }

                        user = getUpUser(workflowinstance.getOwner(), item.getNodename().toUpperCase());

                        if (StrUtil.isEmpty(user)) {
                            continue;
                        }
                        // 创建节点
                        Workflowstep workflowstep = new Workflowstep();
                        workflowstep.setWorkflowstepid(UUID.randomUUID().toString());
                        workflowstep.setWorkflownodeinstanceid(item.getWorkflownodeinstanceid());
                        workflowstep.setName(item.getNodename());
                        workflowstep.setItemid(user);
                        workflowstep.preInsert(tokenModel);

                        //通知节点
                        if ("3".equals(item.getNodetype())) {
                            workflowstep.setResult("0");
                            workflowstep.setRemark("通知节点");
                            workflowstep.setModifyby(tokenModel.getUserId());
                            workflowstep.setModifyon(new Date());
                            workflowstep.setStatus(AuthConstants.APPROVED_FLAG_YES);
                        }

                        workflowstepMapper.insert(workflowstep);

                        // 创建代办
                        ToDoNotice toDoNotice = new ToDoNotice();
                        List<String> params = new ArrayList<String>();
                        params.add(workflowname);
                        if (!"3".equals(item.getNodetype()) || ("3".equals(item.getNodetype()) && StrUtil.isEmpty(item.getRemarks()))) {
                            toDoNotice.setTitle(MessageUtil.getMessage(MsgConstants.WORKFLOW_10, params, tokenModel.getLocale()));
                        } else {
                            toDoNotice.setTitle(item.getRemarks());
                        }
                        toDoNotice.setInitiator(workflowinstance.getOwner());
                        toDoNotice.setContent(item.getNodename());
                        toDoNotice.setDataid(dataId);
                        toDoNotice.setUrl(url);
                        toDoNotice.setWorkflowurl(workFlowurl);
                        toDoNotice.preInsert(tokenModel);
                        toDoNotice.setOwner(user);
                        toDoNoticeService.save(toDoNotice);

                        if ("3".equals(item.getNodetype())) {

                            // 如果节点为最后一个节点时，结束流程
                            if (item == workflownodeinstancelist.get(workflownodeinstancelist.size() - 1)) {
                                workflowinstance.setModifyby(tokenModel.getUserId());
                                workflowinstance.setModifyon(new Date());
                                workflowinstance.setStatus(AuthConstants.APPROVED_FLAG_YES);
                                workflowinstanceMapper.updateByPrimaryKeySelective(workflowinstance);
                                outOperationWorkflowVo.setState("2");
                                outOperationWorkflowVo.setWorkflowCode(workflowinstance.getCode());

                                toDoNotice = new ToDoNotice();
                                params = new ArrayList<String>();
                                params.add(workflowname);
                                toDoNotice.setTitle(MessageUtil.getMessage(MsgConstants.WORKFLOW_11, params, tokenModel.getLocale()));
                                toDoNotice.setInitiator(workflowinstance.getOwner());
                                toDoNotice.setContent(item.getNodename());
                                toDoNotice.setDataid(dataId);
                                toDoNotice.setUrl(url);
                                toDoNotice.setWorkflowurl(workFlowurl);
                                toDoNotice.preInsert(tokenModel);
                                toDoNotice.setOwner(workflowinstance.getOwner());
                                toDoNoticeService.save(toDoNotice);

                                return outOperationWorkflowVo;
                            }

                            continue;
                        }
                        //节点指定
                    } else if ("3".equals(item.getNodeusertype())) {

                        if (userList.size() == 0) {
                            throw new LogicalException("当前节点未指定审批人！");
                        }

                        for (String user : userList) {

                            // 创建节点
                            Workflowstep workflowstep = new Workflowstep();
                            workflowstep.setWorkflowstepid(UUID.randomUUID().toString());
                            workflowstep.setWorkflownodeinstanceid(item.getWorkflownodeinstanceid());
                            workflowstep.setName(item.getNodename());
                            workflowstep.setItemid(user);
                            workflowstep.preInsert(tokenModel);

                            //通知节点
                            if ("3".equals(item.getNodetype())) {
                                workflowstep.setResult("0");
                                workflowstep.setRemark("通知节点");
                                workflowstep.setModifyby(tokenModel.getUserId());
                                workflowstep.setModifyon(new Date());
                                workflowstep.setStatus(AuthConstants.APPROVED_FLAG_YES);
                            }
                            workflowstepMapper.insert(workflowstep);

                            // 创建代办
                            ToDoNotice toDoNotice = new ToDoNotice();
                            List<String> params = new ArrayList<String>();
                            params.add(workflowname);
                            if (!"3".equals(item.getNodetype()) || ("3".equals(item.getNodetype()) && StrUtil.isEmpty(item.getRemarks()))) {
                                toDoNotice.setTitle(MessageUtil.getMessage(MsgConstants.WORKFLOW_10, params, tokenModel.getLocale()));
                            } else {
                                toDoNotice.setTitle(item.getRemarks());
                            }
                            toDoNotice.setInitiator(workflowinstance.getOwner());
                            toDoNotice.setContent(item.getNodename());
                            toDoNotice.setDataid(dataId);
                            toDoNotice.setUrl(url);
                            toDoNotice.setWorkflowurl(workFlowurl);
                            toDoNotice.preInsert(tokenModel);
                            toDoNotice.setOwner(user);
                            toDoNoticeService.save(toDoNotice);
                        }

                        if ("3".equals(item.getNodetype())) {

                            // 如果节点为最后一个节点时，结束流程
                            if (item == workflownodeinstancelist.get(workflownodeinstancelist.size() - 1)) {
                                workflowinstance.setModifyby(tokenModel.getUserId());
                                workflowinstance.setModifyon(new Date());
                                workflowinstance.setStatus(AuthConstants.APPROVED_FLAG_YES);
                                workflowinstanceMapper.updateByPrimaryKeySelective(workflowinstance);
                                outOperationWorkflowVo.setState("2");
                                outOperationWorkflowVo.setWorkflowCode(workflowinstance.getCode());


                                //为发起人创建完成通知
                                ToDoNotice toDoNotice = new ToDoNotice();
                                List<String> params = new ArrayList<String>();
                                params.add(workflowname);
                                toDoNotice.setTitle(MessageUtil.getMessage(MsgConstants.WORKFLOW_11, params, tokenModel.getLocale()));
                                toDoNotice.setInitiator(workflowinstance.getOwner());
                                toDoNotice.setContent(item.getNodename());
                                toDoNotice.setDataid(dataId);
                                toDoNotice.setUrl(url);
                                toDoNotice.setWorkflowurl(workFlowurl);
                                toDoNotice.preInsert(tokenModel);
                                toDoNotice.setOwner(workflowinstance.getOwner());
                                toDoNoticeService.save(toDoNotice);

                                return outOperationWorkflowVo;
                            }

                            continue;
                        }
                    }

                    outOperationWorkflowVo.setState("0");
                    outOperationWorkflowVo.setWorkflowCode(workflowinstance.getCode());
                    return outOperationWorkflowVo;
                }
                // 有节点信息
                else {
                    // 当前所有审批节点
                    conditionWorkflowstep = new Workflowstep();
                    conditionWorkflowstep.setWorkflownodeinstanceid(item.getWorkflownodeinstanceid());
//                    conditionWorkflowstep.setStatus(AuthConstants.DEL_FLAG_NORMAL);
                    workflowsteplist = workflowstepMapper.select(conditionWorkflowstep);
                    if (item.getOutcondition() == null || item.getOutcondition() == "") {
                        item.setOutcondition("1");
                    }
                    if(0 != Convert.toInt(item.getOutcondition())){
                        if (workflowsteplist.size() > 0 && workflowsteplist.stream().filter(stepi -> (AuthConstants.APPROVED_FLAG_YES.equals(stepi.getStatus()))).count() < Convert.toInt(item.getOutcondition())) {
                            // 结束操作
                            outOperationWorkflowVo.setState("0");
                            outOperationWorkflowVo.setWorkflowCode(workflowinstance.getCode());
                            return outOperationWorkflowVo;
                        } else {
                            if (item.getItemid() != null) {
                                for (String user : item.getItemid().split(",")) {
                                    ToDoNotice toDoNotice1 = new ToDoNotice();
                                    toDoNotice1.setDataid(dataId);
                                    toDoNotice1.setUrl(url);
                                    toDoNotice1.setStatus(AuthConstants.DEL_FLAG_NORMAL);
                                    toDoNotice1.setOwner(user);
                                    List<ToDoNotice> rst1 = toDoNoticeService.get(toDoNotice1);
                                    for (ToDoNotice itemToDoNotice :
                                            rst1) {
                                        itemToDoNotice.setStatus(AuthConstants.TODO_STATUS_DONE);
                                        toDoNoticeService.updateNoticesStatus(itemToDoNotice);
                                    }
                                }
                            }


                        }
                    }else{
                        if (workflowsteplist.size() > 0 && workflowsteplist.stream().filter(stepi -> (AuthConstants.APPROVED_FLAG_NO.equals(stepi.getStatus()))).count() > 0) {
                            // 结束操作
                            outOperationWorkflowVo.setState("0");
                            outOperationWorkflowVo.setWorkflowCode(workflowinstance.getCode());
                            return outOperationWorkflowVo;
                        } else {
                            if (item.getItemid() != null) {
                                for (String user : item.getItemid().split(",")) {
                                    ToDoNotice toDoNotice1 = new ToDoNotice();
                                    toDoNotice1.setDataid(dataId);
                                    toDoNotice1.setUrl(url);
                                    toDoNotice1.setStatus(AuthConstants.DEL_FLAG_NORMAL);
                                    toDoNotice1.setOwner(user);
                                    List<ToDoNotice> rst1 = toDoNoticeService.get(toDoNotice1);
                                    for (ToDoNotice itemToDoNotice :
                                            rst1) {
                                        itemToDoNotice.setStatus(AuthConstants.TODO_STATUS_DONE);
                                        toDoNoticeService.updateNoticesStatus(itemToDoNotice);
                                    }
                                }
                            }


                        }
                    }


                    // 如果节点为最后一个节点时，结束流程
                    if (item == workflownodeinstancelist.get(workflownodeinstancelist.size() - 1)) {
                        workflowinstance.setModifyby(tokenModel.getUserId());
                        workflowinstance.setModifyon(new Date());
                        workflowinstance.setStatus(AuthConstants.APPROVED_FLAG_YES);
                        workflowinstanceMapper.updateByPrimaryKeySelective(workflowinstance);
                        outOperationWorkflowVo.setState("2");
                        outOperationWorkflowVo.setWorkflowCode(workflowinstance.getCode());

                        //为发起人创建完成通知
                        ToDoNotice toDoNotice = new ToDoNotice();
                        List<String> params = new ArrayList<String>();
                        params.add(workflowname);
                        toDoNotice.setTitle(MessageUtil.getMessage(MsgConstants.WORKFLOW_11, params, tokenModel.getLocale()));
                        toDoNotice.setInitiator(workflowinstance.getOwner());
                        toDoNotice.setContent(item.getNodename());
                        toDoNotice.setDataid(dataId);
                        toDoNotice.setUrl(url);
                        toDoNotice.setWorkflowurl(workFlowurl);
                        toDoNotice.preInsert(tokenModel);
                        toDoNotice.setOwner(workflowinstance.getOwner());
                        toDoNoticeService.save(toDoNotice);

                        return outOperationWorkflowVo;
                    }

                    // 其他_即当前节点均操作完成，则继续循环生成下一节点信息
                }
            }
        }

        outOperationWorkflowVo.setState("0");
        outOperationWorkflowVo.setWorkflowCode(workflowinstance.getCode());
        return outOperationWorkflowVo;
    }

    @Override
    public OutOperationWorkflowVo StartWorkflow(StartWorkflowVo startWorkflowVo, TokenModel tokenModel) throws Exception {

        // 创建流程实例
        Workflow workflow = workflowMapper.selectByPrimaryKey(startWorkflowVo.getWorkFlowId());
        Workflowinstance workflowinstance = new Workflowinstance();
        BeanUtils.copyProperties(workflow, workflowinstance);
        workflowinstance.setWorkflowinstanceid(UUID.randomUUID().toString());
        workflowinstance.setDataid(startWorkflowVo.getDataId());
        workflowinstance.setFormid(startWorkflowVo.getMenuUrl());
        workflowinstance.setUrl(startWorkflowVo.getDataUrl());
        workflowinstance.setOwner("");
        workflowinstance.preInsert(tokenModel);
        workflowinstanceMapper.insert(workflowinstance);

        // 创建节点实例
        Workflownode workflownode = new Workflownode();
        workflownode.setWorkflowid(workflow.getWorkflowid());
        workflownode.setStatus(AuthConstants.DEL_FLAG_NORMAL);
        List<Workflownode> workflownodelist = workflownodeMapper.select(workflownode);
        workflownodelist = workflownodelist.stream()
                .sorted(Comparator.comparing(Workflownode::getNodeord)).collect(Collectors.toList());


        for (int i = 0; i < workflownodelist.size(); i++) {
            Workflownodeinstance workflownodeinstance = new Workflownodeinstance();
            BeanUtils.copyProperties(workflownodelist.get(i), workflownodeinstance);
            workflownodeinstance.setWorkflowinstanceid(workflowinstance.getWorkflowinstanceid());
            workflownodeinstance.setWorkflownodeinstanceid(UUID.randomUUID().toString());
            workflownodeinstance.preInsert(tokenModel);
            workflownodeinstanceMapper.insert(workflownodeinstance);
        }

        ToDoNotice toDoNotice1 = new ToDoNotice();
        toDoNotice1.setDataid(startWorkflowVo.getDataId());
        toDoNotice1.setUrl(startWorkflowVo.getDataUrl());
        toDoNotice1.setStatus(AuthConstants.DEL_FLAG_NORMAL);
        List<ToDoNotice> rst1 = toDoNoticeService.get(toDoNotice1);
        for (ToDoNotice item :
                rst1) {
            item.setStatus(AuthConstants.TODO_STATUS_DONE);
            toDoNoticeService.updateNoticesStatus(item);
        }

        // 生成节点操作
        return cresteStep(workflowinstance.getWorkflowinstanceid(), tokenModel, startWorkflowVo.getDataId(),
                startWorkflowVo.getDataUrl(), startWorkflowVo.getMenuUrl(), workflow.getWorkflowname(), startWorkflowVo.getUserList());

    }

    @Override
    public String isDelWorkflow(StartWorkflowVo startWorkflowVo) throws Exception {

        Workflowinstance workflowinstance = new Workflowinstance();
        workflowinstance.setDataid(startWorkflowVo.getDataId());
        workflowinstance.setFormid(startWorkflowVo.getMenuUrl());
        workflowinstance.setOwner(startWorkflowVo.getUserId());
//        workflowinstance.setTenantid(startWorkflowVo.getTenantId());
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
    public void DelWorkflow(String instanceId, String locale) throws Exception {
        Workflowinstance workflowinstance = workflowinstanceMapper.selectByPrimaryKey(instanceId);

        // 结束当前流程 并更新状态为撤销 status 为3
        workflowinstance.setStatus("3");
        workflowinstance.setModifyby(workflowinstance.getCreateby());
        workflowinstance.setModifyon(new Date());
        workflowinstanceMapper.updateByPrimaryKeySelective(workflowinstance);

        Workflownodeinstance workflownodeinstance = new Workflownodeinstance();
        workflownodeinstance.setWorkflowinstanceid(instanceId);
        List<Workflownodeinstance> workflownodeinstancelist = workflownodeinstanceMapper.select(workflownodeinstance);

        if (workflownodeinstancelist.size() > 0) {
            workflownodeinstancelist = workflownodeinstancelist.stream()
                    .sorted(Comparator.comparing(Workflownodeinstance::getNodeord).reversed()).collect(Collectors.toList());

            Workflowstep workflowstep = new Workflowstep();
            workflowstep.setWorkflowstepid(UUID.randomUUID().toString());
            workflowstep.setWorkflownodeinstanceid(workflownodeinstancelist.get(0).getWorkflownodeinstanceid());
            workflowstep.setName(MessageUtil.getMessage(MsgConstants.WORKFLOW_09, locale));
            workflowstep.setItemid(workflowinstance.getOwner());
            workflowstep.setCreateby(workflowinstance.getCreateby());
            workflowstep.setCreateon(new Date());
            workflowstep.setResult("3");
            workflowstep.setRemark(MessageUtil.getMessage(MsgConstants.WORKFLOW_09, locale));
            workflowstep.setStatus(AuthConstants.APPROVED_FLAG_YES);

            workflowstepMapper.insert(workflowstep);

        }


//        for (Workflownodeinstance item : workflownodeinstancelist) {
//            Workflowstep workflowstep = new Workflowstep();
//            workflowstep.setWorkflownodeinstanceid(item.getWorkflownodeinstanceid());
//            List<Workflowstep> workflowsteplist = workflowstepMapper.select(workflowstep);
//            for (Workflowstep it : workflowsteplist) {
//                it.setResult("3");
//                it.setRemark(MessageUtil.getMessage(MsgConstants.WORKFLOW_09, locale));
//                it.setModifyby(workflowinstance.getCreateby());
//                it.setModifyon(new Date());
//                workflowstepMapper.updateByPrimaryKeySelective(it);
//            }
//        }

        // 结束所有代办

        ToDoNotice toDoNotice = new ToDoNotice();
        toDoNotice.setDataid(workflowinstance.getDataid());
        toDoNotice.setUrl(workflowinstance.getUrl());
        List<ToDoNotice> rst = toDoNoticeService.get(toDoNotice);
        for (ToDoNotice item :
                rst) {
            item.setStatus(AuthConstants.TODO_STATUS_DELETE);
            toDoNoticeService.updateNoticesStatus(item);
        }
    }

}
