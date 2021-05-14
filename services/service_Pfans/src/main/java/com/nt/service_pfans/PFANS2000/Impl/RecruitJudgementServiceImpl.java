package com.nt.service_pfans.PFANS2000.Impl;

import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.mysql.jdbc.StringUtils;
import com.nt.dao_Auth.Role;
import com.nt.dao_Auth.Vo.MembersVo;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_Org.UserAccount;
import com.nt.dao_Org.ToDoNotice;
import com.nt.dao_Pfans.PFANS2000.InterviewRecord;
import com.nt.dao_Pfans.PFANS2000.RecruitJudgement;
import com.nt.service_Auth.RoleService;
import com.nt.service_Org.DictionaryService;
import com.nt.service_Org.ToDoNoticeService;
import com.nt.service_pfans.PFANS2000.RecruitJudgementService;
import com.nt.service_pfans.PFANS2000.mapper.InterviewRecordMapper;
import com.nt.service_pfans.PFANS2000.mapper.RecruitJudgementMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class RecruitJudgementServiceImpl implements RecruitJudgementService {

    @Autowired
    private RecruitJudgementMapper recruitJudgementMapper;
    @Autowired
    private InterviewRecordMapper interviewRecordMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ToDoNoticeService toDoNoticeService;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private RoleService roleService;

    @Override
    public List<RecruitJudgement> get(TokenModel tokenModel) throws Exception {
        List<String> owners = tokenModel.getOwnerList();
        List<RecruitJudgement> recruitJudgement = recruitJudgementMapper.getRecruitJudgement(owners);
        return recruitJudgement;
    }

    @Override
    public List<RecruitJudgement> getOne(String id, TokenModel tokenModel) throws Exception {
        RecruitJudgement recruitJudgement = recruitJudgementMapper.selectByPrimaryKey(id);
        List<RecruitJudgement> recruitJudgements = new ArrayList<RecruitJudgement>();
        recruitJudgements.add(recruitJudgement);
        return recruitJudgements;
    }

    @Override
    public void insert(RecruitJudgement recruitJudgement, TokenModel tokenModel) throws Exception {
        recruitJudgement.preInsert(tokenModel);
        recruitJudgement.setRecruitjudgement_id(UUID.randomUUID().toString());
        recruitJudgementMapper.insert(recruitJudgement);

    }

    @Override
    public void update(RecruitJudgement recruitJudgement, TokenModel tokenModel) throws Exception {
        recruitJudgement.preUpdate(tokenModel);
        //审批通过，入职区分默认待入职。add ccm 1113
        if(recruitJudgement.getStatus().equals("4") && StringUtils.isNullOrEmpty(recruitJudgement.getEntrydivision()))
        {
            List<Dictionary> dicList = dictionaryService.getForSelect("PR065");
            dicList = dicList.stream().filter(item -> (item.getValue1().equals("待入职"))).collect(Collectors.toList());
            if(dicList.size()>0)
            {
                recruitJudgement.setEntrydivision(dicList.get(0).getCode());
            }
        }
        //审批通过，入职区分默认待入职。add ccm 1113

        recruitJudgementMapper.updateByPrimaryKeySelective(recruitJudgement);
//        add_fjl_06/02  --填写入职日并且审批通过的数据插入到人员信息，并给薪资担当发代办
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String ent = "";
        if (recruitJudgement.getEntrytime() != null) {
            ent = formatter.format(recruitJudgement.getEntrytime());
        }
        if (recruitJudgement.getStatus().equals("4") && !StringUtils.isNullOrEmpty(ent)) {
            Query query0 = new Query();
            UserAccount userAccount = new UserAccount();
            userAccount.setAccount(PinyinHelper.convertToPinyinString(recruitJudgement.getName(), "", PinyinFormat.WITHOUT_TONE));
            //UPD CCM 20210311 PSDCD_PFANS_20210309_BUG_029 FR
//            userAccount.setPassword(PinyinHelper.convertToPinyinString(recruitJudgement.getName(), "", PinyinFormat.WITHOUT_TONE));
//            userAccount.setUsertype("0");
//            query0.addCriteria(Criteria.where("account").regex(userAccount.getAccount()));
////            query0.addCriteria(Criteria.where("password").is(userAccount.getPassword()));
//            query0.addCriteria(Criteria.where("usertype").is(userAccount.getUsertype()));
//            List<UserAccount> list = mongoTemplate.find(query0, UserAccount.class);
//            int uf = 0;
//            String us = "";
//            if (list.size() > 0) {
//                for (UserAccount ua : list) {
//                    uf++;
//                }
//                if (uf <= 9) {
//                    //UPD CCM 20210311 PSDCD_PFANS_20210309_BUG_029 FR
////                    us = "00" + (uf + 1);
//                    us = "00" + (uf);
//                    //UPD CCM 20210311 PSDCD_PFANS_20210309_BUG_029 TO
//                } else {
//                    //UPD CCM 20210311 PSDCD_PFANS_20210309_BUG_029 FR
////                    us = "0" + (uf + 1);
//                    us = "0" + (uf);
//                    //UPD CCM 20210311 PSDCD_PFANS_20210309_BUG_029 TO
//                }
//            }
            String account = userAccount.getAccount();
            account  = selectByAccount(account,"0",0);
            //UPD CCM 20210311 PSDCD_PFANS_20210309_BUG_029 TO
            List<Role> rl = new ArrayList<>();
            Role role = new Role();
            role.set_id("5e7860c68f43163084351131");
            role.setRolename("正式社员");
            role.setDescription("正式社员");
            role.setDefaultrole("true");
            rl.add(role);
            userAccount.setRoles(rl);
            //UPD CCM 20210311 PSDCD_PFANS_20210309_BUG_029 FR
//            userAccount.setAccount(userAccount.getAccount() + us);
            userAccount.setAccount(account);
            userAccount.setPassword(account);
            userAccount.setUsertype("0");
            //UPD CCM 20210311 PSDCD_PFANS_20210309_BUG_029 TO
            userAccount.preInsert(tokenModel);
            mongoTemplate.save(userAccount);
            query0 = new Query();
            query0.addCriteria(Criteria.where("account").is(userAccount.getAccount()));
            query0.addCriteria(Criteria.where("password").is(userAccount.getPassword()));
            List<UserAccount> userAccountlist = mongoTemplate.find(query0, UserAccount.class);
            if (userAccountlist.size() > 0) {
                String _id = userAccountlist.get(0).get_id();
                //add_fjl_0731  审批通过之后把useri往招聘信息管理中插入userid
                if (!StringUtils.isNullOrEmpty(recruitJudgement.getInterviewrecord_id())) {
                    String intcordid = recruitJudgement.getInterviewrecord_id();
                    InterviewRecord interviewRecord = interviewRecordMapper.selectByPrimaryKey(intcordid);
                    if (interviewRecord != null) {
                        interviewRecord.setUserid(_id);
                        interviewRecordMapper.updateByPrimaryKey(interviewRecord);
                    }
                }
                //add_fjl_0731  审批通过之后把useri往招聘信息管理中插入userid

                CustomerInfo customerInfo = new CustomerInfo();
                CustomerInfo.UserInfo info = new CustomerInfo.UserInfo();
                customerInfo.setUserid(_id);
                Query query = new Query();
                //UPD CCM 20210311 PSDCD_PFANS_20210309_BUG_029 FR
//                query.addCriteria(Criteria.where("userinfo.customername").regex(recruitJudgement.getName()));
//                List<CustomerInfo> cnameL = mongoTemplate.find(query, CustomerInfo.class);
//                int cn = 0;
//                String cs = "";
//                if (cnameL != null && cnameL.size() > 0) {
//                    for (CustomerInfo n : cnameL) {
//                        cn++;
//                    }
//                    if (cn <= 9) {
//                        cs = "00" + (cn + 1);
//                    } else {
//                        cs = "0" + (cn + 1);
//                    }
//                }
//                info.setCustomername(recruitJudgement.getName() + cs);
                String name = recruitJudgement.getName();
                name = selectByName(name,0);
                info.setCustomername(name);
                //UPD CCM 20210311 PSDCD_PFANS_20210309_BUG_029 TO

                info.setSex(recruitJudgement.getSex());
                info.setBirthday(formatter.format(recruitJudgement.getBirthday()));
                info.setEnterday(formatter.format(recruitJudgement.getEntrytime()));
                info.setRank(recruitJudgement.getLevel());

                // region add gbb 0724 职级联动职责工资
                Dictionary dictionary = new Dictionary();
                dictionary.setCode(recruitJudgement.getLevel());
                List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                if (dictionaryList.size() > 0) {
                    dictionary = new Dictionary();
                    dictionary.setCode(dictionaryList.get(0).getCode());
                    List<Dictionary> diclist = dictionaryService.getDictionaryList(dictionary);
                    if (diclist.size() > 0) {
                        info.setDuty(diclist.get(0).getValue3());
                    }
                }
                // endregion add gbb 0724 职级联动职责工资

                info.setBasic(String.valueOf(recruitJudgement.getGiving()));
                info.setAdfield(userAccount.getAccount());
                query = new Query();
                query.addCriteria(Criteria.where("userinfo.centerid").is(recruitJudgement.getCenter_id()));
                CustomerInfo cslist = mongoTemplate.findOne(query, CustomerInfo.class);
                if (cslist != null) {
                    info.setCenterid(recruitJudgement.getCenter_id());
                    info.setCentername(cslist.getUserinfo().getCentername());
                }
                Query query1 = new Query();
                query1.addCriteria(Criteria.where("userinfo.groupid").is(recruitJudgement.getGroup_id()));
                CustomerInfo cslist1 = mongoTemplate.findOne(query1, CustomerInfo.class);
                if (cslist1 != null) {
                    info.setGroupid(recruitJudgement.getGroup_id());
                    info.setGroupname(cslist1.getUserinfo().getGroupname());
                }
                Query query2 = new Query();
                query2.addCriteria(Criteria.where("userinfo.teamid").is(recruitJudgement.getTeam_id()));
                CustomerInfo cslist2 = mongoTemplate.findOne(query2, CustomerInfo.class);
                if (cslist2 != null) {
                    info.setTeamid(recruitJudgement.getTeam_id());
                    info.setTeamname(cslist2.getUserinfo().getTeamname());
                }
                info.setType("0");
                customerInfo.setType("1");
                customerInfo.setStatus("0");
                customerInfo.setUserinfo(info);
                customerInfo.preInsert(tokenModel);
                mongoTemplate.save(customerInfo);
                List<MembersVo> rolelist = roleService.getMembers("5e7863668f43163084351139");
                if (rolelist.size() > 0) {
                    for (MembersVo rs : rolelist) {
                        ToDoNotice toDoNotice = new ToDoNotice();
                        List<String> params = new ArrayList<String>();
                        toDoNotice.setTitle("您有一个新入职员工的信息待完善！");
                        toDoNotice.setInitiator(customerInfo.getUserid());
                        toDoNotice.setContent("您有一个人员信息申请！");
//                        toDoNotice.setDataid(recruitJudgement.getRecruitjudgement_id());
                        toDoNotice.setDataid(customerInfo.getUserid());
                        toDoNotice.setUrl("/usersFormView");
                        toDoNotice.setWorkflowurl("/usersView");
                        toDoNotice.preInsert(tokenModel);
                        toDoNotice.setOwner(rs.getUserid());
                        toDoNoticeService.save(toDoNotice);
                    }
                }
            }
        }
//        add_fjl_06/02  --填写入职日并且审批通过的数据插入到人员信息，并给薪资担当发代办
    }


    //ADD CCM 20210311 PSDCD_PFANS_20210309_BUG_029 FR
    public String selectByAccount(String account,String usertype,int count)throws Exception
    {
        String countString = "";
        Query query0 = new Query();
        query0.addCriteria(Criteria.where("account").is(account));
//        query0.addCriteria(Criteria.where("usertype").is(usertype));
        List<UserAccount> list = mongoTemplate.find(query0, UserAccount.class);
        if(list.size()>0)
        {
            countString = "00"+(count+list.size());
            if(list.size()+count>9)
            {
                countString = "0"+(list.size()+count);
            }
            return selectByAccount(account + countString,"0",list.size()+count);
        }
        return account;
    }

    public String selectByName(String name,int count)throws Exception
    {
        String countString = "";
        Query query = new Query();
        query.addCriteria(Criteria.where("userinfo.customername").is(name));
        List<CustomerInfo> cnameL = mongoTemplate.find(query, CustomerInfo.class);
        if(cnameL.size()>0)
        {
            countString = "00"+(count+cnameL.size());
            if(cnameL.size()+count>9)
            {
                countString = "0"+(cnameL.size()+count);
            }
            return selectByName(name + countString,cnameL.size()+count);
        }
        return name;
    }
    //ADD CCM 20210311 PSDCD_PFANS_20210309_BUG_029 TO

     public void userinsert(RecruitJudgement recruitJudgement){
        if(recruitJudgement.getStatus().equals("4")){

        }
     }
}
