package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Auth.Vo.MembersVo;
import com.nt.dao_Org.ToDoNotice;
import com.nt.dao_Pfans.PFANS1000.*;
import com.nt.dao_Pfans.PFANS1000.Vo.AwardVo;
import com.nt.dao_Pfans.PFANS5000.CompanyProjects;
import com.nt.service_Auth.RoleService;
import com.nt.service_Org.ToDoNoticeService;
import com.nt.service_pfans.PFANS1000.AwardService;
import com.nt.service_pfans.PFANS1000.mapper.*;
import com.nt.service_pfans.PFANS5000.mapper.CompanyProjectsMapper;
import com.nt.utils.ExcelOutPutUtil;
import com.nt.utils.dao.TokenModel;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class AwardServiceImpl implements AwardService {

    @Autowired
    private PolicyContractMapper policycontractmapper;
    @Autowired
    private RoleService roleService;
    @Autowired
    private ToDoNoticeService toDoNoticeService;
    @Autowired
    private AwardMapper awardMapper;

    @Autowired
    private AwardDetailMapper awardDetailMapper;

    @Autowired
    private StaffDetailMapper staffDetailMapper;

    @Autowired
    private ContractnumbercountMapper contractnumbercountMapper;

    @Autowired
    CompanyProjectsMapper companyProjectsMapper;

    @Override
    public List<Award> get(Award award) throws Exception {
        List<Award> awardlist = awardMapper.select(award);
        if (awardlist.size() > 0) {
            awardlist = awardlist.stream().sorted(Comparator.comparing(Award::getCreateon).reversed()).collect(Collectors.toList());
        }
        return awardlist;
    }

    // 禅道任务152
    @Override
    public List<Award> One(Award award) throws Exception {
        return awardMapper.select(award);
    }

    // 禅道任务152
    @Override
    public AwardVo selectById(String award_id) throws Exception {
        AwardVo awavo = new AwardVo();
        AwardDetail awadetail = new AwardDetail();
        awadetail.setAward_id(award_id);
        StaffDetail staffdetail = new StaffDetail();
        staffdetail.setAward_id(award_id);
        List<AwardDetail> awalist = awardDetailMapper.select(awadetail);
        List<StaffDetail> stafflist = staffDetailMapper.select(staffdetail);
        awalist = awalist.stream().sorted(Comparator.comparing(AwardDetail::getRowindex)).collect(Collectors.toList());
        stafflist = stafflist.stream().sorted(Comparator.comparing(StaffDetail::getRowindex)).collect(Collectors.toList());
        Award awa = awardMapper.selectByPrimaryKey(award_id);
        Award award = awardMapper.selectByPrimaryKey(award_id);
//        String name = "";
//        String [] companyProjectsid = award.getPjnamechinese().split(",");
//        if(companyProjectsid.length > 0){
//            for (int i = 0;i < companyProjectsid.length;i++){
//                CompanyProjects companyProjects = new CompanyProjects();
//                companyProjects.setCompanyprojects_id(companyProjectsid[i]);
//                List<CompanyProjects> comList = companyProjectsMapper.select(companyProjects);
//                if(comList.size() > 0){
//                    name = name + comList.get(0).getProject_name() + ",";
//                }
//            }
//            if(!name.equals("")){
//                name = name.substring(0,name.length()-1);
//            }
//        }
//        award.setPjnamechinese(name);
        awavo.setAward(awa);
        awavo.setAwardDetail(awalist);
        awavo.setStaffDetail(stafflist);

        if (awa != null) {
            Contractnumbercount contractnumbercount = new Contractnumbercount();
            contractnumbercount.setContractnumber(awa.getContractnumber());
            List<Contractnumbercount> contractList = contractnumbercountMapper.select(contractnumbercount);
            if (contractList != null && contractList.size() > 1) {
                contractList = contractList.stream().sorted(Comparator.comparing(Contractnumbercount::getRowindex)).collect(Collectors.toList());
            }
            awavo.setNumbercounts(contractList);
        }
        return awavo;
    }

    @Override
    public void updateAwardVo(AwardVo awardVo, TokenModel tokenModel) throws Exception {
        Award award = new Award();
        BeanUtils.copyProperties(awardVo.getAward(), award);
        award.preUpdate(tokenModel);
        awardMapper.updateByPrimaryKey(award);
        String awardid = award.getAward_id();
        //add-ws-7/21-禅道任务341
        String contractnumber = award.getContractnumber();
        String status = award.getStatus();
        if (status.equals("4")) {
            //upd-ws-9/17-禅道任务530提交
            if (award.getPolicycontract_id() != "" && award.getPolicycontract_id() != null) {
                int scale = 2;//设置位数
                int roundingMode = 4;//表示四舍五入，可以选择其他舍值方式，例如去尾，等等.
                PolicyContract po = new PolicyContract();
                String policycontract_id = award.getPolicycontract_id();
                po.setPolicycontract_id(policycontract_id);
                PolicyContract policycontractlist = policycontractmapper.selectOne(po);
                PolicyContract policy = new PolicyContract();
                PolicyContract policy2 = new PolicyContract();
                BeanUtils.copyProperties(policycontractlist, policy);
                policy2.setPolicycontract_id(policy.getPolicycontract_id());
                policycontractmapper.delete(policy2);
                //region add_qhr_20210724 加入非空判断
                BigDecimal bd = new BigDecimal(com.mysql.jdbc.StringUtils.isNullOrEmpty(policy.getModifiedamount())? "0" : policy.getModifiedamount());
                bd = bd.setScale(scale, roundingMode);
                BigDecimal bd1 = new BigDecimal(com.mysql.jdbc.StringUtils.isNullOrEmpty(policy.getNewamountcase())? "0" : policy.getNewamountcase());
                bd1 = bd1.setScale(scale, roundingMode);
                BigDecimal bd2 = new BigDecimal(com.mysql.jdbc.StringUtils.isNullOrEmpty(award.getClaimamount())? "0" : award.getClaimamount());
                bd2 = bd2.setScale(scale, roundingMode);
                BigDecimal bd3 = new BigDecimal(com.mysql.jdbc.StringUtils.isNullOrEmpty(policy.getAvbleamount())? "0" : policy.getAvbleamount());
                //endregion add_qhr_20210724 加入非空判断
                bd3 = bd3.setScale(scale, roundingMode);
                policy.preUpdate(tokenModel);
                policy.setModifiedamount(String.valueOf(bd.subtract(bd2)));
                policy.setAvbleamount(String.valueOf(bd3.subtract(bd2)));
                policy.setNewamountcase(String.valueOf(bd1.add(bd2)));
                policycontractmapper.insertSelective(policy);
            }
            //upd-ws-9/17-禅道任务530提交
            if (award.getMaketype().equals("9")) {
                //合同担当
                List<MembersVo> rolelist = roleService.getMembers("5e7862618f43163084351135");
                if (rolelist.size() > 0) {
                    for (MembersVo rt : rolelist) {
                        ToDoNotice toDoNotice3 = new ToDoNotice();
                        toDoNotice3.setTitle("【" + contractnumber + "】决裁流程结束，请申请印章");
                        toDoNotice3.setInitiator(award.getUser_id());
                        toDoNotice3.setContent("流程结束，请申请印章");
                        toDoNotice3.setDataid(contractnumber);
                        toDoNotice3.setUrl("/PFANS1047View");
                        toDoNotice3.setWorkflowurl("/PFANS1047View");
                        toDoNotice3.preInsert(tokenModel);
                        toDoNotice3.setOwner(rt.getUserid());
                        toDoNoticeService.save(toDoNotice3);
                    }
                }
            }
//            else
//            {
//                //合同担当
//                List<MembersVo> rolelist = roleService.getMembers("5e7862618f43163084351135");
//                if (rolelist.size() > 0) {
//                    for (MembersVo rt : rolelist) {
//                        ToDoNotice toDoNotice3 = new ToDoNotice();
//                        toDoNotice3.setTitle("【" + contractnumber + "】决裁流程结束，请申请印章");
//                        toDoNotice3.setInitiator(award.getUser_id());
//                        toDoNotice3.setContent("流程结束，请申请印章");
//                        toDoNotice3.setDataid(contractnumber);
//                        toDoNotice3.setUrl("/PFANS1025View");
//                        toDoNotice3.setWorkflowurl("/PFANS1025View");
//                        toDoNotice3.preInsert(tokenModel);
//                        toDoNotice3.setOwner(rt.getUserid());
//                        toDoNoticeService.save(toDoNotice3);
//                    }
//                }
//            }
        }
        //add-ws-7/21-禅道任务341
        AwardDetail award2 = new AwardDetail();
        award2.setAward_id(awardid);
        awardDetailMapper.delete(award2);
        List<AwardDetail> awardDetails = awardVo.getAwardDetail();


        StaffDetail sta = new StaffDetail();
        sta.setAward_id(awardid);
        staffDetailMapper.delete(sta);
        List<StaffDetail> stalist = awardVo.getStaffDetail();


        if (awardDetails != null) {
            int rowindex = 0;
            for (AwardDetail awarddetail : awardDetails) {
                rowindex = rowindex + 1;
                awarddetail.preInsert(tokenModel);
                awarddetail.setAwarddetail_id(UUID.randomUUID().toString());
                awarddetail.setAward_id(awardid);
                awarddetail.setRowindex(rowindex);
                awardDetailMapper.insertSelective(awarddetail);
            }
        }

        if (stalist != null) {
            int rowindex = 0;
            for (StaffDetail staffDetail : stalist) {
                rowindex = rowindex + 1;
                staffDetail.preInsert(tokenModel);
                staffDetail.setStaffdetail_id(UUID.randomUUID().toString());
                staffDetail.setAward_id(awardid);
                staffDetail.setRowindex(rowindex);
                staffDetailMapper.insertSelective(staffDetail);
            }
        }

    }

    @Override
    public void dataCarryover(Award award,TokenModel tokenModel) throws Exception {
        award.preUpdate(tokenModel);
        awardMapper.updateByPrimaryKeySelective(award);
    }

}
