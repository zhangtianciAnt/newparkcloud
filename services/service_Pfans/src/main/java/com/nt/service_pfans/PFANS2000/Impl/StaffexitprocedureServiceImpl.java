package com.nt.service_pfans.PFANS2000.Impl;

import com.mongodb.client.result.UpdateResult;
import com.nt.dao_Auth.Role;
import com.nt.dao_Auth.Vo.MembersVo;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.ToDoNotice;
import com.nt.dao_Pfans.PFANS2000.AnnualLeave;
import com.nt.dao_Pfans.PFANS2000.Citation;
import com.nt.dao_Pfans.PFANS2000.Staffexitprocedure;
import com.nt.dao_Pfans.PFANS2000.Staffexitproce;
import com.nt.dao_Pfans.PFANS2000.Vo.StaffexitprocedureVo;
import com.nt.dao_Pfans.PFANS5000.Projectsystem;
import com.nt.dao_Pfans.PFANS5000.Prosystem;
import com.nt.dao_Workflow.Vo.StartWorkflowVo;
import com.nt.dao_Workflow.Vo.WorkflowLogDetailVo;
import com.nt.service_WorkFlow.WorkflowServices;
import com.nt.service_pfans.PFANS2000.StaffexitprocedureService;
import com.nt.service_pfans.PFANS2000.mapper.AnnualLeaveMapper;
import com.nt.service_pfans.PFANS2000.mapper.CitationMapper;
import com.nt.service_pfans.PFANS2000.mapper.StaffexitprocedureMapper;
import com.nt.service_pfans.PFANS2000.mapper.StaffexitproceMapper;
import com.nt.service_pfans.PFANS5000.mapper.ProjectsystemMapper;
import com.nt.service_pfans.PFANS5000.mapper.ProsystemMapper;
import com.nt.utils.AuthConstants;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.dao.TokenModel;
import com.nt.service_Auth.RoleService;
import com.nt.service_Org.ToDoNoticeService;
import com.nt.utils.sign;
import lombok.Builder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class StaffexitprocedureServiceImpl implements StaffexitprocedureService {

    @Autowired
    private ProsystemMapper prosystemMapper;

    @Autowired
    private ProjectsystemMapper projectsystemMapper;

    @Autowired
    private StaffexitprocedureMapper staffexitprocedureMapper;

    @Autowired
    private StaffexitproceMapper staffexitproceMapper;

    @Autowired
    private WorkflowServices workflowServices;

    @Autowired
    private CitationMapper citationMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private AnnualLeaveMapper annualLeaveMapper;

    @Autowired
    private ToDoNoticeService toDoNoticeService;

    @Autowired
    private RoleService roleService;

    //列表查询
    @Override
    public List<Staffexitprocedure> get(Staffexitprocedure staffexitprocedure) throws Exception {
        return staffexitprocedureMapper.select(staffexitprocedure);
    }

    @Override
    public List<Staffexitproce> get2(Staffexitproce staffexit) throws Exception {
        return staffexitproceMapper.select(staffexit);
    }

    //add-ws-6/16-禅道106
    @Override
    public void delete(Staffexitprocedure staffexitprocedure) throws Exception {
        staffexitprocedure.setType("1");
        staffexitprocedureMapper.updateByPrimaryKeySelective(staffexitprocedure);
    }

    //add-ws-6/16-禅道106
    //按id查询
    @Override
    public StaffexitprocedureVo selectById(String staffexitprocedureid) throws Exception {
        StaffexitprocedureVo staffVo = new StaffexitprocedureVo();
        Citation citation = new Citation();
        citation.setStaffexitproce_id(staffexitprocedureid);
        List<Citation> citationlist = citationMapper.select(citation);
        citationlist = citationlist.stream().sorted(Comparator.comparing(Citation::getRowindex)).collect(Collectors.toList());
        Staffexitprocedure Staff = staffexitprocedureMapper.selectByPrimaryKey(staffexitprocedureid);
        staffVo.setStaffexitprocedure(Staff);
        staffVo.setCitation(citationlist);
        return staffVo;
    }


    @Override
    public StaffexitprocedureVo selectById2(String staffexitproceid) throws Exception {
        StaffexitprocedureVo staffVo = new StaffexitprocedureVo();
        Citation citation = new Citation();
        citation.setStaffexitproce_id(staffexitproceid);
        List<Citation> citationlist = citationMapper.select(citation);
        citationlist = citationlist.stream().sorted(Comparator.comparing(Citation::getRowindex)).collect(Collectors.toList());
        Staffexitproce Staff = staffexitproceMapper.selectByPrimaryKey(staffexitproceid);
        staffVo.setStaffexitproce(Staff);
        staffVo.setCitation(citationlist);
        return staffVo;
    }
    //更新
    @Override
    public void update(StaffexitprocedureVo staffexitprocedureVo, TokenModel tokenModel) throws Exception {
        Staffexitprocedure staffexitprocedure = new Staffexitprocedure();
        StartWorkflowVo startWorkflowVo = new StartWorkflowVo();
        startWorkflowVo.setDataId(staffexitprocedureVo.getStaffexitprocedure().getStaffexitprocedure_id());
        List<WorkflowLogDetailVo> wfList = workflowServices.ViewWorkflow2(startWorkflowVo, tokenModel.getLocale());
//        if (staffexitprocedureVo.getStaffexitprocedure().getStage().equals("1") && staffexitprocedureVo.getStaffexitprocedure().getStatus().equals("0")) {
//            if(staffexitprocedureVo.getStaffexitprocedure().getStage().equals("1")){
        //代办完成  Status:1
//                ToDoNotice toDoNotice1 = new ToDoNotice();
//                toDoNotice1.setDataid(staffexitprocedureVo.getStaffexitprocedure().getStaffexitprocedure_id());
//                toDoNotice1.setUrl("/PFANS2026FormView");
//                toDoNotice1.setOwner(staffexitprocedureVo.getStaffexitprocedure().getLoginper());
//                toDoNotice1.setStatus(AuthConstants.DEL_FLAG_NORMAL);
//                List<ToDoNotice> rst1 = toDoNoticeService.get(toDoNotice1);
//                if(rst1.size() > 0){
//                    for (ToDoNotice item : rst1) {
//                        item.setStatus(AuthConstants.TODO_STATUS_DONE);
//                        toDoNoticeService.updateNoticesStatus(item);
//                    }
//                } else {
//            //所属TL
//            Query query = new Query();
//            query.addCriteria(Criteria.where("userinfo.teamid").is(staffexitprocedureVo.getStaffexitprocedure().getTeam_id()).and("userinfo.post").is("PG021005"));
//            query.addCriteria(Criteria.where("status").is(AuthConstants.DEL_FLAG_NORMAL));
//            List<CustomerInfo> CustinfoList = mongoTemplate.find(query, CustomerInfo.class);
//            if (CustinfoList.size() > 0) {
//                for (CustomerInfo cs : CustinfoList) {
//                    cs.getUserid();
//                    cs.getUserinfo().getCustomername();
//                }
//            }
        //给离职审批人员发代办
        if (staffexitprocedureVo.getStaffexitprocedure().getStatus().equals("4")) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            updateRetireDate(staffexitprocedureVo);
            String Userid = staffexitprocedureVo.getStaffexitprocedure().getUser_id();
//            if (wfList.size() > 0) {
//                Userid = wfList.get(0).getUserId();
//            }
            String username = "";
            String center = "";
            String group = "";
            String team = "";
            String date = sdf.format(staffexitprocedureVo.getStaffexitprocedure().getHope_exit_date());
            Query query3 = new Query();
            query3.addCriteria(Criteria.where("userid").is(staffexitprocedureVo.getStaffexitprocedure().getUser_id()));
            CustomerInfo customerInfo2 = mongoTemplate.findOne(query3, CustomerInfo.class);
            if (customerInfo2 != null) {
                username = customerInfo2.getUserinfo().getCustomername();
            }
            //薪资担当代办
            ToDoNotice toDoNotice3 = new ToDoNotice();
            toDoNotice3.setTitle("【" + username + "将于" + date + "日离职，请悉知】");
            toDoNotice3.setInitiator(Userid);
            toDoNotice3.setContent("您的离职申请已审批通过！");
            toDoNotice3.setDataid(staffexitprocedureVo.getStaffexitprocedure().getStaffexitprocedure_id());
            toDoNotice3.setUrl("/PFANS2026FormView");
            toDoNotice3.setWorkflowurl("/PFANS2026View");
            toDoNotice3.preInsert(tokenModel);
            toDoNotice3.setOwner("5e78b2034e3b194874180e37");
            toDoNoticeService.save(toDoNotice3);

            //发起人创建代办
            ToDoNotice toDoNotice = new ToDoNotice();
            toDoNotice.setTitle("【您的离职申请已审批通过，系统中如有进行中的流程，请及时处理。您现可以提前进行离职日前的考勤承认】");
            toDoNotice.setInitiator(Userid);
            toDoNotice.setContent("您的离职申请已审批通过！");
            toDoNotice.setDataid(staffexitprocedureVo.getStaffexitprocedure().getStaffexitprocedure_id());
            toDoNotice.setUrl("/PFANS2026FormView");
            toDoNotice.setWorkflowurl("/PFANS2026View");
            toDoNotice.preInsert(tokenModel);
            toDoNotice.setOwner(staffexitprocedureVo.getStaffexitprocedure().getUser_id());
            toDoNoticeService.save(toDoNotice);

//            String user = "";
//            if (wfList.get(4).getRemark() != null) {
//                if (wfList.get(4).getRemark().equals("系统自动跳过")) {
//                    Query query2 = new Query();
//                    query2.addCriteria(Criteria.where("userid").is(wfList.get(3).getUserId()));
//                    CustomerInfo customerInfo = mongoTemplate.findOne(query2, CustomerInfo.class);
//                    if (customerInfo != null) {
//                        user = wfList.get(3).getUserId();
//                    }
//                } else if (wfList.get(5).getRemark().equals("系统自动跳过")) {
//                    Query query2 = new Query();
//                    query2.addCriteria(Criteria.where("userid").is(wfList.get(4).getUserId()));
//                    CustomerInfo customerInfo = mongoTemplate.findOne(query2, CustomerInfo.class);
//                    if (customerInfo != null) {
//                        user = wfList.get(4).getUserId();
//                    }
//                } else {
//                    Query query2 = new Query();
//                    query2.addCriteria(Criteria.where("userid").is(wfList.get(5).getUserId()));
//                    CustomerInfo customerInfo = mongoTemplate.findOne(query2, CustomerInfo.class);
//                    if (customerInfo != null) {
//                        user = wfList.get(5).getUserId();
//                    }
//                }
//            }
            //发起人创建代办
            ToDoNotice toDoNotice2 = new ToDoNotice();
            toDoNotice2.setTitle("【" + username + "离职审批已通过，请填写退职者调书】");
            toDoNotice2.setInitiator(Userid);
            toDoNotice2.setContent("请填写退职者调书！");
            toDoNotice2.setDataid(staffexitprocedureVo.getStaffexitprocedure().getStaffexitprocedure_id());
            toDoNotice2.setUrl("/PFANS2026FormView");
            toDoNotice2.setWorkflowurl("/PFANS2026View");
            toDoNotice2.preInsert(tokenModel);
            toDoNotice2.setOwner(wfList.get(2).getUserId());
            toDoNoticeService.save(toDoNotice2);
        }

//        }
//        if(staffexitprocedureVo.getStaffexitprocedure().getStage().equals("1") && staffexitprocedureVo.getStaffexitprocedure().getStatus().equals("4")) {
        BeanUtils.copyProperties(staffexitprocedureVo.getStaffexitprocedure(), staffexitprocedure);
        staffexitprocedure.preUpdate(tokenModel);
        staffexitprocedureMapper.updateByPrimaryKey(staffexitprocedure);
        String staffexitprocedureid = staffexitprocedure.getStaffexitprocedure_id();
        Citation cita = new Citation();
        cita.setStaffexitproce_id(staffexitprocedureid);
        citationMapper.delete(cita);
        List<Citation> citationlist = staffexitprocedureVo.getCitation();
        if (citationlist != null) {
            int rowundex = 0;
            for (Citation citation : citationlist) {
                rowundex = rowundex + 1;
                citation.preInsert(tokenModel);
                citation.setCitation_id(UUID.randomUUID().toString());
                citation.setStaffexitproce_id(staffexitprocedureid);
                citation.setRowindex(rowundex);
                citationMapper.insertSelective(citation);
            }
        }
    }


    //更新
    @Override
    public void update2(StaffexitprocedureVo staffexitprocedureVo, TokenModel tokenModel) throws Exception {
        Staffexitproce staffexitproce = new Staffexitproce();
        StartWorkflowVo startWorkflowVo = new StartWorkflowVo();
        startWorkflowVo.setDataId(staffexitprocedureVo.getStaffexitproce().getStaffexitproce_id());
        List<WorkflowLogDetailVo> wfList = workflowServices.ViewWorkflow2(startWorkflowVo, tokenModel.getLocale());
        if (staffexitprocedureVo.getStaffexitproce().getStatus().equals("4")) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String Userid = staffexitprocedureVo.getStaffexitproce().getUser_id();
//            if (wfList.size() > 0) {
//                Userid = wfList.get(0).getUserId();
//            }
            String username = "";
            String center = "";
            String group = "";
            String team = "";
            Query query3 = new Query();
            query3.addCriteria(Criteria.where("userid").is(staffexitprocedureVo.getStaffexitproce().getUser_id()));
            CustomerInfo customerInfo2 = mongoTemplate.findOne(query3, CustomerInfo.class);
            if (customerInfo2 != null) {
                username = customerInfo2.getUserinfo().getCustomername();
            }
            ToDoNotice toDoNotice3 = new ToDoNotice();
            toDoNotice3.setTitle("【"  + username + "离职确认流程结束】");
            toDoNotice3.setInitiator(Userid);
            toDoNotice3.setContent("您的离职申请已审批通过！");
            toDoNotice3.setDataid(staffexitprocedureVo.getStaffexitproce().getStaffexitproce_id());
            toDoNotice3.setUrl("/PFANS2026FormView");
            toDoNotice3.setWorkflowurl("/PFANS2026View");
            toDoNotice3.preInsert(tokenModel);
            toDoNotice3.setOwner(staffexitprocedureVo.getStaffexitproce().getReporter());
            toDoNoticeService.save(toDoNotice3);

            Projectsystem projectsystem = new Projectsystem();
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
            projectsystem.setName(staffexitprocedureVo.getStaffexitproce().getUser_id());
            List<Projectsystem> projectsystemList = projectsystemMapper.select(projectsystem);
            for (Projectsystem pro : projectsystemList) {
                String Date = sdf1.format(pro.getExittime());
                String Date1 = sdf1.format(new Date());
                if (Integer.valueOf(Date) >= Integer.valueOf(Date1)) {
                    pro.setExittime(new Date());
                    projectsystemMapper.updateByPrimaryKeySelective(pro);
                }
            }

            Prosystem prosystem = new Prosystem();
            prosystem.setName(staffexitprocedureVo.getStaffexitproce().getUser_id());
            List<Prosystem> prosystemList = prosystemMapper.select(prosystem);
            for (Prosystem prolist : prosystemList) {
                String Date = sdf1.format(prolist.getExittime());
                String Date1 = sdf1.format(new Date());
                if (Integer.valueOf(Date) >= Integer.valueOf(Date1)) {
                    prolist.setExittime(new Date());
                    prosystemMapper.updateByPrimaryKeySelective(prolist);
                }
            }
            Query query = new Query();
            query.addCriteria(Criteria.where("userid").is(staffexitprocedureVo.getStaffexitproce().getUser_id()));
            CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
            if (customerInfo != null) {
                customerInfo.getUserinfo().setJobnumber("00000");
            }
            mongoTemplate.save(customerInfo);
        }
//        }
//        if(staffexitprocedureVo.getStaffexitprocedure().getStage().equals("1") && staffexitprocedureVo.getStaffexitprocedure().getStatus().equals("4")) {
        BeanUtils.copyProperties(staffexitprocedureVo.getStaffexitproce(), staffexitproce);
        staffexitproce.preUpdate(tokenModel);
        staffexitproceMapper.updateByPrimaryKey(staffexitproce);
        String staffexitproceid = staffexitproce.getStaffexitproce_id();
        Citation cita = new Citation();
        cita.setStaffexitproce_id(staffexitproceid);
        citationMapper.delete(cita);
        List<Citation> citationlist = staffexitprocedureVo.getCitation();
        if (citationlist != null) {
            int rowundex = 0;
            for (Citation citation : citationlist) {
                rowundex = rowundex + 1;
                citation.preInsert(tokenModel);
                citation.setCitation_id(UUID.randomUUID().toString());
                citation.setStaffexitproce_id(staffexitproceid);
                citation.setRowindex(rowundex);
                citationMapper.insertSelective(citation);
            }
        }
    }

    @Override
    public List<Staffexitproce> getList(Staffexitproce staffexitproce, TokenModel tokenModel) throws Exception {
        return staffexitproceMapper.select(staffexitproce);
    }

    @Override
    public List<Staffexitprocedure> getList2(Staffexitprocedure staffexitprocedure, TokenModel tokenModel) throws Exception {
        return staffexitprocedureMapper.select(staffexitprocedure);
    }

    //新建
    @Override
    public void insert(StaffexitprocedureVo staffexitprocedureVo, TokenModel tokenModel) throws Exception {
        String staffexitprocedureid = UUID.randomUUID().toString();
        Staffexitprocedure staffexitprocedure = new Staffexitprocedure();
        BeanUtils.copyProperties(staffexitprocedureVo.getStaffexitprocedure(), staffexitprocedure);
        staffexitprocedure.preInsert(tokenModel);
        staffexitprocedure.setStaffexitprocedure_id(staffexitprocedureid);
        staffexitprocedureMapper.insertSelective(staffexitprocedure);
        List<Citation> citationlist = staffexitprocedureVo.getCitation();
        if (citationlist != null) {
            int rowundex = 0;
            for (Citation citation : citationlist) {
                rowundex = rowundex + 1;
                citation.preInsert(tokenModel);
                citation.setCitation_id(UUID.randomUUID().toString());
                citation.setStaffexitproce_id(staffexitprocedureid);
                citation.setRowindex(rowundex);
                citationMapper.insertSelective(citation);
            }
        }
    }

    //新建
    @Override
    public void insert2(StaffexitprocedureVo staffexitprocedureVo, TokenModel tokenModel) throws Exception {
        String staffexitproceid = UUID.randomUUID().toString();
        Staffexitproce staffexitproce = new Staffexitproce();
        BeanUtils.copyProperties(staffexitprocedureVo.getStaffexitproce(), staffexitproce);
        staffexitproce.preInsert(tokenModel);
        staffexitproce.setStaffexitproce_id(staffexitproceid);
        staffexitproce.setOwner(staffexitprocedureVo.getStaffexitproce().getReporter());
        staffexitproceMapper.insertSelective(staffexitproce);
        List<Citation> citationlist = staffexitprocedureVo.getCitation();
        if (citationlist != null) {
            int rowundex = 0;
            for (Citation citation : citationlist) {
                rowundex = rowundex + 1;
                citation.preInsert(tokenModel);
                citation.setCitation_id(UUID.randomUUID().toString());
                citation.setStaffexitproce_id(staffexitproceid);
                citation.setRowindex(rowundex);
                citationMapper.insertSelective(citation);
            }
        }
    }

    public void updateRetireDate(StaffexitprocedureVo staffexitprocedureVo) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Query query3 = new Query();
        query3.addCriteria(Criteria.where("userid").is(staffexitprocedureVo.getStaffexitprocedure().getUser_id()));
        CustomerInfo customerInfo2 = mongoTemplate.findOne(query3, CustomerInfo.class);
        if (customerInfo2 != null) {
            customerInfo2.getUserinfo().setResignation_date(sdf.format(staffexitprocedureVo.getStaffexitprocedure().getHope_exit_date()));
        }
        mongoTemplate.save(customerInfo2);
    }

    private void updateAnnualDays(StaffexitprocedureVo staffexitprocedureVo) throws Exception {
        int result = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        AnnualLeave annualLeave = new AnnualLeave();
        Calendar now = Calendar.getInstance();
        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();
        Date _now = sdf.parse(sdf.format(now.getTime()));
        now.setTime(_now);
        int year = now.get(Calendar.MONTH) < 3 ? now.get(Calendar.YEAR) - 1 : now.get(Calendar.YEAR);
        annualLeave.setYears(year + "");
        annualLeave.setUser_id(staffexitprocedureVo.getStaffexitprocedure().getUser_id());
        AnnualLeave _annualLeave = annualLeaveMapper.selectOne(annualLeave);
        if (_annualLeave != null) {
            min.setTime(staffexitprocedureVo.getStaffexitprocedure().getHope_exit_date());
            Date date = sdf.parse(year + "-" + "04" + "-" + "01");
            max.setTime(date);
            while (min.before(max)) {
                result++;
                min.add(Calendar.MONTH, 1);
            }
            Double annual_leave_thisyear = _annualLeave.getAnnual_leave_thisyear().doubleValue();
            BigDecimal annualRest = new BigDecimal((float) (12 - result) / 12 * annual_leave_thisyear);
            Double _annual_leave_thisyear = Math.floor(annualRest.doubleValue());
            _annualLeave.setAnnual_leave_thisyear(BigDecimal.valueOf(_annual_leave_thisyear));
            Double deduct_annual_leave_thisyear = _annualLeave.getDeduct_annual_leave_thisyear().doubleValue();
            _annualLeave.setRemaining_annual_leave_thisyear(BigDecimal.valueOf(_annual_leave_thisyear - deduct_annual_leave_thisyear));

//            Double paid_leave_thisyear = _annualLeave.getPaid_leave_thisyear().doubleValue();
//            BigDecimal _annualRest = new BigDecimal((float) (12 - result) / 12 * paid_leave_thisyear);
//            Double _paid_leave_thisyear = Math.floor(_annualRest.doubleValue());
//            _annualLeave.setPaid_leave_thisyear(BigDecimal.valueOf(_paid_leave_thisyear));
//            Double deduct_paid_leave_thisyear = _annualLeave.getDeduct_paid_leave_thisyear().doubleValue();
//            _annualLeave.setRemaining_annual_leave_thisyear(BigDecimal.valueOf(_paid_leave_thisyear - deduct_paid_leave_thisyear));

            annualLeaveMapper.updateByPrimaryKeySelective(_annualLeave);


        }
    }
}
