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
import com.nt.dao_Pfans.PFANS2000.PunchcardRecord;
import com.nt.dao_Pfans.PFANS2000.PunchcardRecordDetail;
import com.nt.dao_Pfans.PFANS3000.Tickets;
import com.nt.dao_Pfans.PFANS8000.WorkingDay;
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
import com.nt.service_pfans.PFANS2000.mapper.AbNormalMapper;
import com.nt.service_pfans.PFANS2000.mapper.PunchcardRecordDetailMapper;
import com.nt.service_pfans.PFANS2000.mapper.PunchcardRecordMapper;
import com.nt.service_pfans.PFANS3000.mapper.TicketsMapper;
import com.nt.service_pfans.PFANS8000.mapper.WorkingDayMapper;
import com.nt.utils.ApiResult;
import com.nt.utils.AuthConstants;
import com.nt.utils.StringUtils;
import com.nt.utils.dao.TokenModel;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
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
    @Autowired
    private WorkingDayMapper workingDayMapper;
    @Autowired
    private PunchcardRecordDetailMapper punmapper;
    @Autowired
    private PunchcardRecordMapper punchcardrecordMapper;
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

        //region gbb 20201029 禅道601 取消出差时删除打卡记录 start
        Query query = new Query();
        query.addCriteria(Criteria.where("userid").is(Bus.getUser_id()));
        CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
        Date start = Bus.getStartdate();
        Date end = Bus.getEnddate();
        Calendar calStar = Calendar.getInstance();
        calStar.setTime(start);
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(end);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        while (calStar.compareTo(calEnd) <= 0) {
            //刪除考勤列表記錄
            PunchcardRecord punchcardrecord = new PunchcardRecord();
            punchcardrecord.setPunchcardrecord_date(calStar.getTime());
            punchcardrecord.setTenantid("1");
            punchcardrecord.setJobnumber(customerInfo.getUserinfo().getJobnumber());
            punchcardrecordMapper.delete(punchcardrecord);

            //刪除考勤詳細記錄
            PunchcardRecordDetail punchcardrecorddetail = new PunchcardRecordDetail();
            punchcardrecorddetail.setDates(calStar.getTime());
            punchcardrecorddetail.setTenantid("1");
            punchcardrecorddetail.setJobnumber(customerInfo.getUserinfo().getJobnumber());
            punmapper.delete(punchcardrecorddetail);

            calStar.add(Calendar.DAY_OF_MONTH, 1);
        }
        //endregion gbb 20201029 禅道601 取消出差时删除打卡记录 end

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
        //endregion gbb 20201029 禅道601 审批通过时将出差期间工作日的考勤设为因公外出 end
    }

    //@scheduled(cron="0 1 0 * * ?")
    public void saveDaka()throws Exception {
        TokenModel tokenModel = new TokenModel();
        List<Business> businessList = businessMapper.getBusList();
        for(Business business : businessList){
            //region gbb 20201029 禅道601 审批通过时将出差期间工作日的考勤设为因公外出 start
            Query query = new Query();
            query.addCriteria(Criteria.where("userid").is(business.getUser_id()));
            CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
            //特殊的工作日(星期六、日工作)
            List<String> SPECIAL_WORK_DAYS = new ArrayList<>();
            //特殊的休息日(星期一到五休息)
            List<String> SPECIAL_REST_DAYS = new ArrayList<>();
            SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
            Date start = business.getStartdate();
            Date end = business.getEnddate();
            //工作日表
            List<WorkingDay> workingDaysList = workingDayMapper.getWorkingday(ymd.format(start),ymd.format(end));
            int workingDayscount = workingDaysList.size();
            for (int i = 0; i < workingDaysList.size(); i++) {
                Date workingdate = workingDaysList.get(i).getWorkingdate();
                String type = workingDaysList.get(i).getType();
                if(type.equals("4")){
                    //振替出勤日
                    SPECIAL_WORK_DAYS.add(ymd.format(workingdate));
                }
                else{
                    //其他节假日
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(workingdate);
                    if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
                        continue;
                    }
                    SPECIAL_REST_DAYS.add(ymd.format(workingdate));
                }
            }

            Calendar calStar = Calendar.getInstance();
            calStar.setTime(start);
            Calendar calEnd = Calendar.getInstance();
            calEnd.setTime(end);
            while (calStar.compareTo(calEnd) <= 0) {
                //如果不是周六或者周日则工作日
                if (calStar.get(Calendar.DAY_OF_WEEK) != 7 && calStar.get(Calendar.DAY_OF_WEEK) != 1) {
                    //如果不是周六或者周日，但是该日属于国家法定节假日或者特殊放假日则-1
                    if (SPECIAL_REST_DAYS.contains(DateFormatUtils.format(calStar.getTime(), "yyyy-MM-dd"))) {
                        calStar.add(Calendar.DAY_OF_MONTH, 1);
                        continue;
                    }
                    //添加打卡记录
                    insertpun(calStar,customerInfo,tokenModel);
                }
                //如果是周六或者周日，但是该日属于需要工作的日子则 +1
                if (SPECIAL_WORK_DAYS.contains(DateFormatUtils.format(calStar.getTime(), "yyyy-MM-dd"))) {
                    //如果不是周六或者周日，但是该日属于国家法定节假日或者特殊放假日则-1
                    if (SPECIAL_REST_DAYS.contains(DateFormatUtils.format(calStar.getTime(), "yyyy-MM-dd"))) {
                        calStar.add(Calendar.DAY_OF_MONTH, 1);
                        continue;
                    }
                    //添加打卡记录
                    insertpun(calStar,customerInfo,tokenModel);
                }
                calStar.add(Calendar.DAY_OF_MONTH, 1);
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

    //添加打卡记录
    public void insertpun(Calendar calStar,CustomerInfo customerInfo,TokenModel tokenModel) throws Exception {

        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String userid = customerInfo.getUserid();
        String jobnumber = customerInfo.getUserinfo().getJobnumber();
        String username = customerInfo.getUserinfo().getCustomername();
        Calendar calendcreate = Calendar.getInstance();
        calendcreate.setTime(calStar.getTime());
        calendcreate.add(Calendar.DATE,1);
        Date createdate = sf.parse(DateFormatUtils.format(calendcreate.getTime(), "yyyy-MM-dd") + " 00:30:00");

        //刪除考勤列表記錄
        PunchcardRecord pun = new PunchcardRecord();
        pun.setPunchcardrecord_date(calStar.getTime());
        pun.setTenantid("1");
        pun.setJobnumber(jobnumber);
        punchcardrecordMapper.delete(pun);

        //刪除考勤詳細記錄
        PunchcardRecordDetail pundetail = new PunchcardRecordDetail();
        pundetail.setDates(calStar.getTime());
        pundetail.setTenantid("1");
        pundetail.setJobnumber(jobnumber);
        punmapper.delete(pundetail);

        //region 添加打卡详细
        PunchcardRecordDetail punchcardrecorddetail = new PunchcardRecordDetail();
        //卡号
        punchcardrecorddetail.setJobnumber(jobnumber);
        punchcardrecorddetail.setDates(calStar.getTime());
        punchcardrecorddetail.setUser_id(username);
        punchcardrecorddetail.preInsert(tokenModel);
        punchcardrecorddetail.setCreateon(createdate);
        punchcardrecorddetail.setCreateby(userid);
        punchcardrecorddetail.setTenantid("1");//判断打卡记录数据来源

        //进门
        punchcardrecorddetail.setEventno("1");
        //打卡时间
        Date date1 = sf.parse(DateFormatUtils.format(calStar.getTime(), "yyyy-MM-dd") + " 09:00:00");
        punchcardrecorddetail.setPunchcardrecord_date(date1);
        punchcardrecorddetail.setPunchcardrecorddetail_id(UUID.randomUUID().toString());
        punmapper.insert(punchcardrecorddetail);
        //出门
        punchcardrecorddetail.setEventno("2");
        //打卡时间
        Date date2 = sf.parse(DateFormatUtils.format(calStar.getTime(), "yyyy-MM-dd") + " 18:00:00");
        punchcardrecorddetail.setPunchcardrecord_date(date2);
        punchcardrecorddetail.setPunchcardrecorddetail_id(UUID.randomUUID().toString());
        punmapper.insert(punchcardrecorddetail);
        //endregion

        //region 添加打卡
        PunchcardRecord punchcardrecord = new PunchcardRecord();
        punchcardrecord.setPunchcardrecord_date(calStar.getTime());
        punchcardrecord.setTeam_id(customerInfo.getUserinfo().getTeamname());
        punchcardrecord.setGroup_id(customerInfo.getUserinfo().getGroupname());
        punchcardrecord.setCenter_id(customerInfo.getUserinfo().getCentername());
        punchcardrecord.setUser_id(userid);
        punchcardrecord.setJobnumber(jobnumber);
        punchcardrecord.setWorktime("0.0");
        punchcardrecord.setAbsenteeismam("0.0");
        punchcardrecord.setOutgoinghours("0.0");
        punchcardrecord.setEffectiveduration("8.0");
        punchcardrecord.setPunchcardrecord_id(UUID.randomUUID().toString());
        punchcardrecord.setTime_start(date1);
        punchcardrecord.setTime_end(date2);
        punchcardrecord.preInsert(tokenModel);
        punchcardrecord.setCreateon(createdate);
        punchcardrecord.setOwner(userid);
        punchcardrecord.setTenantid("1");//判断打卡记录数据来源
        punchcardrecordMapper.insert(punchcardrecord);
        //endregion
    }

}
