package com.nt.service_pfans.PFANS1000.Impl;

import cn.hutool.core.util.StrUtil;
import com.nt.dao_Auth.Vo.MembersVo;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.OrgTree;
import com.nt.dao_Org.ToDoNotice;
import com.nt.dao_Org.UserAccount;
import com.nt.dao_Org.Vo.UserVo;
import com.nt.dao_Pfans.PFANS1000.*;
import com.nt.dao_Pfans.PFANS1000.Vo.BusinessVo;
import com.nt.dao_Pfans.PFANS3000.Tickets;
import com.nt.dao_Workflow.Vo.StartWorkflowVo;
import com.nt.dao_Workflow.Vo.WorkflowLogDetailVo;
import com.nt.dao_Workflow.Workflowinstance;
import com.nt.service_Auth.RoleService;
import com.nt.service_Org.OrgTreeService;
import com.nt.service_Org.ToDoNoticeService;
import com.nt.service_Org.UserService;
import com.nt.service_Org.mapper.TodoNoticeMapper;
import com.nt.service_WorkFlow.WorkflowServices;
import com.nt.service_WorkFlow.mapper.WorkflowinstanceMapper;
import com.nt.service_pfans.PFANS1000.BusinessService;
import com.nt.service_pfans.PFANS1000.mapper.*;
import com.nt.service_pfans.PFANS3000.mapper.TicketsMapper;
import com.nt.utils.ApiResult;
import com.nt.utils.AuthConstants;
import com.nt.utils.StringUtils;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class BusinessServiceImpl implements BusinessService {
    //add-ws-7/7-禅道247
    @Autowired
    private OrgTreeService orgTreeService;

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @Autowired
    private ToDoNoticeService toDoNoticeService;

    @Autowired
    private WorkflowinstanceMapper workflowinstanceMapper;

    @Autowired
    private TicketsMapper ticketsMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private TodoNoticeMapper todoNoticeMapper;

    @Autowired
    private WorkflowServices workflowServices;
    //add-ws-7/7-禅道247
    @Autowired
    private BusinessMapper businessMapper;
    @Autowired
    private TravelContentMapper travelcontentMapper;

    @Override
    public List<Business> get(Business business) throws Exception {
        return businessMapper.select(business);
    }

    @Override
    public List<Business> getBuse() throws Exception {
        return businessMapper.getBuse();
    }

    //add-ws-7/10-禅道247
    @Override
    public List<Business> list(BusinessVo businessVo) throws Exception {
        TokenModel tokenModel =new TokenModel();
        Business business = new Business();
        business.setBusiness_id(businessVo.getBusiness().getBusiness_id());
        Business Bus = businessMapper.selectByPrimaryKey(business);
        String busserid = Bus.getBusiness_number();
        UserVo userInfo = userService.getAccountCustomerById(Bus.getUser_id());
        OrgTree orgs = orgTreeService.get(new OrgTree());
        String flgid = "";
        String curuser = "";
        int flgroles = 0;//区分flg = 0 ？正式社员 ：（领导）
        UserAccount us = userInfo.getUserAccount();
        if (us != null) {
            String ro = "";
            for (int i = 0; i < us.getRoles().size(); i++) {
                ro += us.getRoles().get(i).getRolename() + ",";
            }
            if (ro.indexOf("总经理") != -1) {
                flgroles++;
            } else if (ro.toUpperCase().indexOf("CENTER") != -1) {
                flgroles++;
            } else if (ro.toUpperCase().indexOf("GM") != -1) {
                flgroles++;
            } else if (ro.toUpperCase().indexOf("TL") != -1) {
                flgroles++;
            }
        }
        CustomerInfo.UserInfo cus = userInfo.getCustomerInfo().getUserinfo();
        if (cus != null) {
            if (flgroles == 0) {
                if (cus.getTeamid() != null && StrUtil.isNotEmpty(cus.getTeamid())) {
                    flgid = cus.getTeamid();
                } else if (cus.getGroupid() != null && StrUtil.isNotEmpty(cus.getGroupid())) {
                    flgid = cus.getGroupid();
                } else if (cus.getCenterid() != null && StrUtil.isNotEmpty(cus.getCenterid())) {
                    flgid = cus.getCenterid();
                }
            } else {
                if (cus.getTeamid() != null && StrUtil.isNotEmpty(cus.getTeamid())) {
                    flgid = cus.getGroupid();
                } else if (cus.getGroupid() != null && StrUtil.isNotEmpty(cus.getGroupid())) {
                    flgid = cus.getCenterid();
                } else if (cus.getCenterid() != null && StrUtil.isNotEmpty(cus.getCenterid())) {
                    flgid = "";
                }
            }
        }
        OrgTree currentOrg = getCurrentOrg(orgs, flgid);
        if (currentOrg.getUser() != null && StrUtil.isNotEmpty(currentOrg.getUser())) {
            curuser = currentOrg.getUser();
        }
        String username = "";
        Query query3 = new Query();
        query3.addCriteria(Criteria.where("userid").is(Bus.getUser_id()));
        CustomerInfo customerInfo2 = mongoTemplate.findOne(query3, CustomerInfo.class);
        if (customerInfo2 != null) {
            username = customerInfo2.getUserinfo().getCustomername();
        }
        String url = "";
        String url1 = "";
        if (Bus.getBusinesstype().equals("1")) {
            url = "/PFANS1035FormView";
            url1 = "/PFANS1035View";
        } else {
            url = "/PFANS1002FormView";
            url1 = "/PFANS1002View";
        }
        ToDoNotice toDoNotice3 = new ToDoNotice();
        toDoNotice3.setTitle("【" + username + "得出差已取消，请及时处理】");
        toDoNotice3.setInitiator(Bus.getUser_id());
        toDoNotice3.setContent("请您处理以取消得出差");
        toDoNotice3.setDataid(Bus.getBusiness_id());
        toDoNotice3.setUrl(url);
        toDoNotice3.setWorkflowurl(url1);
        toDoNotice3.preInsert(tokenModel);
        toDoNotice3.setOwner(curuser);
        toDoNoticeService.save(toDoNotice3);
        Tickets tickets = new Tickets();
        tickets.setBusiness_id(Bus.getBusiness_id());
        List<Tickets> ticketlist = ticketsMapper.select(tickets);
        if (ticketlist.size() > 0) {
//            Workflowinstance workflowinstance = new Workflowinstance();
//            workflowinstance.setDataid(Bus.getBusiness_id());
//            if (Bus.getBusinesstype().equals("1")) {
//                workflowinstance.setFormid("/PFANS1035FormView");
//            } else {
//                workflowinstance.setFormid("/PFANS1002FormView");
//            }
//            List<Workflowinstance> workflowinstancelist = workflowinstanceMapper.select(workflowinstance);
//            if (workflowinstancelist.size() > 0) {
//                for (Workflowinstance workflow : workflowinstancelist) {
//                    workflow.setModifyby(tokenModel.getUserId());
//                    workflow.setModifyon(new Date());
//                    workflow.setStatus(AuthConstants.APPROVED_FLAG_YES);
//                    workflowinstanceMapper.updateByPrimaryKeySelective(workflow);
//                }
//                StartWorkflowVo startWorkflowVo = new StartWorkflowVo();
//                startWorkflowVo.setDataId(Bus.getBusiness_id());
//                List<WorkflowLogDetailVo> wfList = workflowServices.ViewWorkflow2(startWorkflowVo, tokenModel.getLocale());
//                if (wfList.size() > 0) {
//                    ToDoNotice condition = new ToDoNotice();
//                    condition.setOwner(wfList.get(0).getUserId());
//                    condition.setStatus(AuthConstants.TODO_STATUS_TODO);
//                    condition.setDataid(Bus.getBusiness_id());
//                    if (Bus.getBusinesstype().equals("1")) {
//                        condition.setUrl("/PFANS1035FormView");
//                    } else {
//                        condition.setUrl("/PFANS1002FormView");
//                    }
//                    List<ToDoNotice> list = todoNoticeMapper.select(condition);
//                    for (ToDoNotice todono : list) {
//                        todono.preUpdate(tokenModel);
//                        todono.setStatus(AuthConstants.TODO_STATUS_DONE);
//                        todoNoticeMapper.updateByPrimaryKeySelective(todono);
//                    }
//                }
//            }
            List<MembersVo> rolelist = roleService.getMembers("5e7863d88f4316308435113b");
            if (rolelist.size() > 0) {
                for(MembersVo role:rolelist){
                    ToDoNotice toDoNotice = new ToDoNotice();
                    toDoNotice.setTitle("【" + username + "得出差已取消，请及时处理机票】");
                    toDoNotice.setInitiator(Bus.getUser_id());
                    toDoNotice.setContent("请修改机票");
                    toDoNotice.setDataid(busserid);
                    toDoNotice.setUrl("/PFANS3001View");
                    toDoNotice.setWorkflowurl("/PFANS3001View");
                    toDoNotice.preInsert(tokenModel);
                    toDoNotice.setOwner(role.getUserid());
                    toDoNoticeService.save(toDoNotice);
                }
            }
        }
        Business business2 = new Business();
        BeanUtils.copyProperties(businessVo.getBusiness(), business2);
        business2.setCheckch("1");
        business2.preUpdate(tokenModel);
        businessMapper.updateByPrimaryKey(business2);
        String businessid = business2.getBusiness_id();
        TravelContent travel = new TravelContent();
        travel.setBusinessid(businessid);
        travelcontentMapper.delete(travel);
        List<TravelContent> travelcontentlist = businessVo.getTravelcontent();
        if (travelcontentlist != null) {
            int rowindex = 0;
            for (TravelContent travelcontent : travelcontentlist) {
                rowindex = rowindex + 1;
                travelcontent.preInsert(tokenModel);
                travelcontent.setTravelcontent_id(UUID.randomUUID().toString());
                travelcontent.setBusinessid(businessid);
                travelcontent.setRowindex(rowindex);
                travelcontentMapper.insertSelective(travelcontent);
            }
        }
        return businessMapper.select(business);
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

    //add-ws-7/10-禅道247
    //add-ws-7/7-禅道153
    @Override
    public List<Business> selectById3(String offshore_id) throws Exception {
        Business business = new Business();
        business.setOffshore_id(offshore_id);
        return businessMapper.select(business);
    }
    //add-ws-7/7-禅道153

    @Override
    public BusinessVo selectById(String businessid) throws Exception {
        BusinessVo busVo = new BusinessVo();
        TravelContent travelcontent = new TravelContent();
        travelcontent.setBusinessid(businessid);
        List<TravelContent> travelcontentlist = travelcontentMapper.select(travelcontent);
        travelcontentlist = travelcontentlist.stream().sorted(Comparator.comparing(TravelContent::getRowindex)).collect(Collectors.toList());
        Business Bus = businessMapper.selectByPrimaryKey(businessid);
        busVo.setBusiness(Bus);
        busVo.setTravelcontent(travelcontentlist);
        return busVo;
    }

    @Override
    public void updateBusinessVo(BusinessVo businessVo, TokenModel tokenModel) throws Exception {
        Business business = new Business();
        BeanUtils.copyProperties(businessVo.getBusiness(), business);
        business.preUpdate(tokenModel);
        businessMapper.updateByPrimaryKey(business);
        String businessid = business.getBusiness_id();
        TravelContent travel = new TravelContent();
        travel.setBusinessid(businessid);
        travelcontentMapper.delete(travel);
        List<TravelContent> travelcontentlist = businessVo.getTravelcontent();
        if (travelcontentlist != null) {
            int rowindex = 0;
            for (TravelContent travelcontent : travelcontentlist) {
                rowindex = rowindex + 1;
                travelcontent.preInsert(tokenModel);
                travelcontent.setTravelcontent_id(UUID.randomUUID().toString());
                travelcontent.setBusinessid(businessid);
                travelcontent.setRowindex(rowindex);
                travelcontentMapper.insertSelective(travelcontent);
            }
        }
    }

    @Override
    public void insertBusinessVo(BusinessVo businessVo, TokenModel tokenModel) throws Exception {
        String businessid = UUID.randomUUID().toString();
        Business business = new Business();
        List<Business> businessList = businessMapper.selectAll();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        String new_date = sdf1.format(date);
        int number = 0;
        String number_str = "";
        String no_str = "";
        if (businessList.size() > 0) {
            for (Business busin : businessList) {
                if (busin.getBusiness_number() != "" && busin.getBusiness_number() != null) {
                    String checkNumber = StringUtils.uncapitalize(StringUtils.substring(busin.getBusiness_number(), 1, 9));
                    if (new_date.equals(checkNumber)) {
                        number = number + 1;
                    }
                }
            }
            if (number <= 8) {
                no_str = "0" + (number + 1);
            } else {
                no_str = String.valueOf(number + 1);
            }
        } else {
            no_str = "01";
        }
        number_str = "C" + new_date + no_str;
        BeanUtils.copyProperties(businessVo.getBusiness(), business);
        business.preInsert(tokenModel);
        business.setBusiness_id(businessid);
        business.setBusiness_number(number_str);
        businessMapper.insertSelective(business);
        List<TravelContent> travelcontentlist = businessVo.getTravelcontent();
        if (travelcontentlist != null) {
            int rowindex = 0;
            for (TravelContent travelcontent : travelcontentlist) {
                rowindex = rowindex + 1;
                travelcontent.preInsert(tokenModel);
                travelcontent.setTravelcontent_id(UUID.randomUUID().toString());
                travelcontent.setBusinessid(businessid);
                travelcontent.setRowindex(rowindex);
                travelcontentMapper.insertSelective(travelcontent);
            }
        }
    }

}
