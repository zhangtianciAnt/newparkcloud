package com.nt.service_pfans.PFANS2000.Impl;

import com.mongodb.client.result.UpdateResult;
import com.nt.dao_Auth.Role;
import com.nt.dao_Auth.Vo.MembersVo;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.ToDoNotice;
import com.nt.dao_Pfans.PFANS2000.AnnualLeave;
import com.nt.dao_Pfans.PFANS2000.Citation;
import com.nt.dao_Pfans.PFANS2000.Staffexitprocedure;
import com.nt.dao_Pfans.PFANS2000.Vo.StaffexitprocedureVo;
import com.nt.service_pfans.PFANS2000.StaffexitprocedureService;
import com.nt.service_pfans.PFANS2000.mapper.AnnualLeaveMapper;
import com.nt.service_pfans.PFANS2000.mapper.CitationMapper;
import com.nt.service_pfans.PFANS2000.mapper.StaffexitprocedureMapper;
import com.nt.utils.AuthConstants;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.dao.TokenModel;
import com.nt.service_Auth.RoleService;
import com.nt.service_Org.ToDoNoticeService;
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
    private StaffexitprocedureMapper staffexitprocedureMapper;

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

    //按id查询
    @Override
    public StaffexitprocedureVo selectById(String staffexitprocedureid) throws Exception {
        StaffexitprocedureVo staffVo = new StaffexitprocedureVo();
        Citation citation = new Citation();
        citation.setStaffexitprocedure_id(staffexitprocedureid);
        List<Citation> citationlist = citationMapper.select(citation);
        citationlist = citationlist.stream().sorted(Comparator.comparing(Citation::getRowindex)).collect(Collectors.toList());
        Staffexitprocedure Staff = staffexitprocedureMapper.selectByPrimaryKey(staffexitprocedureid);
        staffVo.setStaffexitprocedure(Staff);
        staffVo.setCitation(citationlist);
        return staffVo;
    }

    //更新
    @Override
    public void update(StaffexitprocedureVo staffexitprocedureVo, TokenModel tokenModel) throws Exception {
        Staffexitprocedure staffexitprocedure = new Staffexitprocedure();
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
//            //给离职审批人员发代办
//            List<MembersVo> rolelist = roleService.getMembers("5ebcdb0d8e3cff0440ab706a");
//            if (rolelist.size() > 0) {
//                for (MembersVo rt : rolelist) {
//                    //发起人创建代办
//                    ToDoNotice toDoNotice = new ToDoNotice();
//                    List<String> params = new ArrayList<String>();
//                    toDoNotice.setTitle("您有一个【离职相关】审批待处理！");
//                    toDoNotice.setInitiator(staffexitprocedureVo.getStaffexitprocedure().getUser_id());
//                    toDoNotice.setContent("您有一个离职相关申请！");
//                    toDoNotice.setDataid(staffexitprocedureVo.getStaffexitprocedure().getStaffexitprocedure_id());
//                    toDoNotice.setUrl("/PFANS2026FormView");
//                    toDoNotice.setWorkflowurl("/PFANS2026View");
//                    toDoNotice.preInsert(tokenModel);
//                    toDoNotice.setOwner(rt.getUserid());
//                    toDoNoticeService.save(toDoNotice);
//                }
//            }
//                }

//        }
//        if(staffexitprocedureVo.getStaffexitprocedure().getStage().equals("1") && staffexitprocedureVo.getStaffexitprocedure().getStatus().equals("4")) {
        if (staffexitprocedureVo.getStaffexitprocedure().getStatus().equals("4")) {
            updateRetireDate(staffexitprocedureVo);
            updateAnnualDays(staffexitprocedureVo);
        }
        BeanUtils.copyProperties(staffexitprocedureVo.getStaffexitprocedure(), staffexitprocedure);
        staffexitprocedure.preUpdate(tokenModel);
        staffexitprocedureMapper.updateByPrimaryKey(staffexitprocedure);
        String staffexitprocedureid = staffexitprocedure.getStaffexitprocedure_id();
        Citation cita = new Citation();
        cita.setStaffexitprocedure_id(staffexitprocedureid);
        citationMapper.delete(cita);
        List<Citation> citationlist = staffexitprocedureVo.getCitation();
        if (citationlist != null) {
            int rowundex = 0;
            for (Citation citation : citationlist) {
                rowundex = rowundex + 1;
                citation.preInsert(tokenModel);
                citation.setCitation_id(UUID.randomUUID().toString());
                citation.setStaffexitprocedure_id(staffexitprocedureid);
                citation.setRowindex(rowundex);
                citationMapper.insertSelective(citation);
            }
        }
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
                citation.setStaffexitprocedure_id(staffexitprocedureid);
                citation.setRowindex(rowundex);
                citationMapper.insertSelective(citation);
            }
        }
    }

    public void updateRetireDate(StaffexitprocedureVo staffexitprocedureVo) {
        Query query = new Query(Criteria.where("userid").is(staffexitprocedureVo.getStaffexitprocedure().getUser_id()));
        Update update = new Update();
        if (staffexitprocedureVo.getStaffexitprocedure().getResignation_date() != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            update.set("userinfo.resignation_date", formatter.format(staffexitprocedureVo.getStaffexitprocedure().getResignation_date()));
        }
        if (staffexitprocedureVo.getStaffexitprocedure().getReason2() != null) {
            update.set("userinfo.reason2", staffexitprocedureVo.getStaffexitprocedure().getReason2());
        }
        update.set("userinfo.staffexitprocedure", staffexitprocedureVo.getStaffexitprocedure().getStaffexitprocedure_id());
        UpdateResult user = mongoTemplate.updateFirst(query, update, CustomerInfo.class);
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
            min.setTime(staffexitprocedureVo.getStaffexitprocedure().getResignation_date());
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
