package com.nt.service_pfans.PFANS2000.Impl;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import com.nt.dao_Auth.Role;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.UserAccount;
import com.nt.dao_Pfans.PFANS2000.TalentPlan;
import com.nt.dao_Pfans.PFANS2000.Vo.TalentPlanVo;
import com.nt.service_pfans.PFANS2000.TalentPlanService;
import com.nt.service_pfans.PFANS2000.mapper.TalentPlanMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor=Exception.class)
public class TalentPlanServiceImpl implements TalentPlanService {

    @Autowired
    private TalentPlanMapper talentPlanMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<TalentPlan> list(TalentPlan talentPlan,TokenModel tokenModel ) throws Exception {
        List<CustomerInfo> customerInfoList = new ArrayList<CustomerInfo>();
        List<CustomerInfo> customerInfoListCENTER = new ArrayList<CustomerInfo>();
        List<CustomerInfo> customerInfoListGM = new ArrayList<CustomerInfo>();
        List<CustomerInfo> customerInfoListTL = new ArrayList<CustomerInfo>();
        List<String> userIdList = new ArrayList<String>();
        List<TalentPlan> talentPlanListALL = new ArrayList<TalentPlan>();
        Query query = new Query();
        //根据登陆用户id查看人员信息
        query.addCriteria(Criteria.where("_id").is(tokenModel.getUserId()));
        List<UserAccount> userAccountlist = mongoTemplate.find(query,UserAccount.class);
        String roles = "";
        for(Role role : userAccountlist.get(0).getRoles()){
            roles = roles + role.getDescription();
        }
        customerInfoList = mongoTemplate.findAll(CustomerInfo.class);

        for(CustomerInfo ci : customerInfoList)
        {
            if(ci.getUserinfo().getCenterid() == null)
            {
                ci.getUserinfo().setCenterid("");
            }
            if(ci.getUserinfo().getGroupid() == null)
            {
                ci.getUserinfo().setGroupid("");
            }
            if(ci.getUserinfo().getTeamid() == null)
            {
                ci.getUserinfo().setTeamid("");
            }
            if(ci.getUserinfo().getPost() == null)
            {
                ci.getUserinfo().setPost("");
            }
        }

        if(roles.contains("总经理"))
        {
            Query query1 = new Query();
            //查询当前登陆人的信息
            query1.addCriteria(Criteria.where("userid").is(userAccountlist.get(0).get_id()));
            List<CustomerInfo> customerInfop = mongoTemplate.find(query1,CustomerInfo.class);
            if(customerInfop.get(0).getUserinfo().getCenterid() == null)
            {
                customerInfop.get(0).getUserinfo().setCenterid("");
            }
            if(customerInfop.get(0).getUserinfo().getGroupid() == null)
            {
                customerInfop.get(0).getUserinfo().setGroupid("");
            }
            if(customerInfop.get(0).getUserinfo().getTeamid() == null)
            {
                customerInfop.get(0).getUserinfo().setTeamid("");
            }
            //查询当前登录人伞下所有的人
            //customerInfoList = mongoTemplate.findAll(CustomerInfo.class);
            //伞下所有人中检索职位为center长的人
            List<CustomerInfo> customerInfoListZONGP = new ArrayList<CustomerInfo>();
            customerInfoListZONGP = customerInfoList.stream().filter(customerInfo -> customerInfo.getUserinfo().getPost().trim().equals("PG021001")).collect(Collectors.toList());

            //追加到最后的人员list中
            for(CustomerInfo c : customerInfoListZONGP){
                userIdList.add(c.getUserid());
            }

            //存在兼职
            if(customerInfop.get(0).getUserinfo().getOtherorgs() != null && customerInfop.get(0).getUserinfo().getOtherorgs().size()>0)
            {
                //兼职
                for(CustomerInfo.OtherOrgs otherOrgs : customerInfop.get(0).getUserinfo().getOtherorgs())
                {
                    //兼center  查GM
                    if(otherOrgs.getCenterid() !=null && !otherOrgs.getCenterid().equals("")
                            && !otherOrgs.getCenterid().equals(customerInfop.get(0).getUserinfo().getCenterid().trim()))
                    {
                        customerInfoListZONGP = customerInfoList.stream().filter(customerInfo -> (customerInfo.getUserinfo().getPost().trim().equals("PG021002") || customerInfo.getUserinfo().getPost().trim().equals("PG021003"))
                                && customerInfo.getUserinfo().getCenterid().trim().equals(otherOrgs.getCenterid())).collect(Collectors.toList());
                        for(CustomerInfo c : customerInfoListZONGP){
                            userIdList.add(c.getUserid());
                        }
                    }
                    //兼GM   查TL
                    if(otherOrgs.getGroupid() !=null && !otherOrgs.getGroupid().equals("")
                            && !otherOrgs.getGroupid().equals(customerInfop.get(0).getUserinfo().getGroupid().trim()))
                    {
                        customerInfoListZONGP = customerInfoList.stream().filter(customerInfo -> customerInfo.getUserinfo().getPost().trim().equals("PG021005")
                                && customerInfo.getUserinfo().getGroupid().trim().equals(otherOrgs.getGroupid())).collect(Collectors.toList());
                        for(CustomerInfo c : customerInfoListZONGP){
                            userIdList.add(c.getUserid());
                        }
                    }
                    //兼TL
                    if(otherOrgs.getTeamid() !=null && !otherOrgs.getTeamid().equals("")
                            && !otherOrgs.getTeamid().equals(customerInfop.get(0).getUserinfo().getTeamid().trim()))
                    {
                        customerInfoListZONGP = customerInfoList.stream().filter(customerInfo -> customerInfo.getUserinfo().getTeamid().trim().equals(otherOrgs.getTeamid())).collect(Collectors.toList());
                        for(CustomerInfo c : customerInfoListZONGP){
                            userIdList.add(c.getUserid());
                        }
                    }
                }
            }
        }
        if(roles.contains("center长"))
        {
            Query query1 = new Query();
            //查询当前登陆人的信息
            query1.addCriteria(Criteria.where("userid").is(userAccountlist.get(0).get_id()));
            List<CustomerInfo> customerInfop = mongoTemplate.find(query1,CustomerInfo.class);
            if(customerInfop.get(0).getUserinfo().getCenterid() == null)
            {
                customerInfop.get(0).getUserinfo().setCenterid("");
            }
            if(customerInfop.get(0).getUserinfo().getGroupid() == null)
            {
                customerInfop.get(0).getUserinfo().setGroupid("");
            }
            if(customerInfop.get(0).getUserinfo().getTeamid() == null)
            {
                customerInfop.get(0).getUserinfo().setTeamid("");
            }
            //查询当前登录人伞下所有的人
            query1 = new Query();
            query1.addCriteria(Criteria.where("userinfo.centerid").is(customerInfop.get(0).getUserinfo().getCenterid().trim()));
            customerInfoListCENTER = mongoTemplate.find(query1,CustomerInfo.class);
            //伞下所有人中检索职位为GM的人
            List<CustomerInfo> customerInfoListCENTERP = new ArrayList<CustomerInfo>();
            customerInfoListCENTERP = customerInfoListCENTER.stream().filter(customerInfo -> customerInfo.getUserinfo().getPost().trim().equals("PG021002") || customerInfo.getUserinfo().getPost().trim().equals("PG021003")).collect(Collectors.toList());
            //追加到最后的人员list中
            for(CustomerInfo c : customerInfoListCENTERP){
                if(!userIdList.contains(c.getUserid()))
                {
                    userIdList.add(c.getUserid());
                }
            }
            //存在兼职
            if(customerInfop.get(0).getUserinfo().getOtherorgs() != null && customerInfop.get(0).getUserinfo().getOtherorgs().size()>0)
            {
                //兼职
                for(CustomerInfo.OtherOrgs otherOrgs : customerInfop.get(0).getUserinfo().getOtherorgs())
                {
                    //兼center  查GM
                    if(otherOrgs.getCenterid() !=null && !otherOrgs.getCenterid().equals("")
                            && !otherOrgs.getCenterid().equals(customerInfop.get(0).getUserinfo().getCenterid().trim()))
                    {
                        customerInfoListCENTERP = customerInfoList.stream().filter(customerInfo -> (customerInfo.getUserinfo().getPost().trim().equals("PG021002") || customerInfo.getUserinfo().getPost().trim().equals("PG021003"))
                                && customerInfo.getUserinfo().getCenterid().trim().equals(otherOrgs.getCenterid())).collect(Collectors.toList());
                        for(CustomerInfo c : customerInfoListCENTERP){
                            if(!userIdList.contains(c.getUserid()))
                            {
                                userIdList.add(c.getUserid());
                            }
                        }
                    }
                    //兼GM   查TL
                    if(otherOrgs.getGroupid() !=null && !otherOrgs.getGroupid().equals("")
                            && !otherOrgs.getGroupid().equals(customerInfop.get(0).getUserinfo().getGroupid().trim()))
                    {
                        customerInfoListCENTERP = customerInfoList.stream().filter(customerInfo -> customerInfo.getUserinfo().getPost().trim().equals("PG021005")
                                && customerInfo.getUserinfo().getGroupid().trim().equals(otherOrgs.getGroupid())).collect(Collectors.toList());
                        for(CustomerInfo c : customerInfoListCENTERP){
                            if(!userIdList.contains(c.getUserid()))
                            {
                                userIdList.add(c.getUserid());
                            }
                        }
                    }
                    //兼TL
                    if(otherOrgs.getTeamid() !=null && !otherOrgs.getTeamid().equals("")
                            && !otherOrgs.getTeamid().equals(customerInfop.get(0).getUserinfo().getTeamid().trim()))
                    {
                        customerInfoListCENTERP = customerInfoList.stream().filter(customerInfo -> customerInfo.getUserinfo().getTeamid().trim().equals(otherOrgs.getTeamid())).collect(Collectors.toList());
                        for(CustomerInfo c : customerInfoListCENTERP){
                            if(!userIdList.contains(c.getUserid()))
                            {
                                userIdList.add(c.getUserid());
                            }
                        }
                    }
                }
            }
        }
        if(roles.contains("GM"))
        {
            Query query1 = new Query();
            //查询当前登陆人的信息
            query1.addCriteria(Criteria.where("userid").is(userAccountlist.get(0).get_id()));
            List<CustomerInfo> customerInfop = mongoTemplate.find(query1,CustomerInfo.class);
            if(customerInfop.get(0).getUserinfo().getCenterid() == null)
            {
                customerInfop.get(0).getUserinfo().setCenterid("");
            }
            if(customerInfop.get(0).getUserinfo().getGroupid() == null)
            {
                customerInfop.get(0).getUserinfo().setGroupid("");
            }
            if(customerInfop.get(0).getUserinfo().getTeamid() == null)
            {
                customerInfop.get(0).getUserinfo().setTeamid("");
            }
            //查询当前登录人伞下所有的人
            query1 = new Query();
            query1.addCriteria(Criteria.where("userinfo.groupid").is(customerInfop.get(0).getUserinfo().getGroupid().trim()));
            customerInfoListGM = mongoTemplate.find(query1,CustomerInfo.class);
            //伞下所有人中检索职位为TL的人
            List<CustomerInfo> customerInfoListGMP = new ArrayList<CustomerInfo>();
            customerInfoListGMP = customerInfoListGM.stream().filter(customerInfo -> customerInfo.getUserinfo().getPost().trim().equals("PG021005")).collect(Collectors.toList());
            if(customerInfoListGMP.size() == 0)
            {
                customerInfoListGMP = customerInfoListGM;
            }
            //追加到最后的人员list中
            for(CustomerInfo c : customerInfoListGMP){
                if(!userIdList.contains(c.getUserid()))
                {
                    userIdList.add(c.getUserid());
                }
            }
            //存在兼职
            if(customerInfop.get(0).getUserinfo().getOtherorgs() != null && customerInfop.get(0).getUserinfo().getOtherorgs().size()>0)
            {
                //兼职
                for(CustomerInfo.OtherOrgs otherOrgs : customerInfop.get(0).getUserinfo().getOtherorgs())
                {
                    //兼center  查GM
                    if(otherOrgs.getCenterid() !=null && !otherOrgs.getCenterid().equals("")
                            && !otherOrgs.getCenterid().equals(customerInfop.get(0).getUserinfo().getCenterid().trim()))
                    {
                        customerInfoListGMP = customerInfoList.stream().filter(customerInfo -> (customerInfo.getUserinfo().getPost().trim().equals("PG021002") || customerInfo.getUserinfo().getPost().trim().equals("PG021003"))
                                && customerInfo.getUserinfo().getCenterid().trim().equals(otherOrgs.getCenterid())).collect(Collectors.toList());
                        for(CustomerInfo c : customerInfoListGMP){
                            if(!userIdList.contains(c.getUserid()))
                            {
                                userIdList.add(c.getUserid());
                            }
                        }
                    }
                    //兼GM   查TL
                    if(otherOrgs.getGroupid() !=null && !otherOrgs.getGroupid().equals("")
                            && !otherOrgs.getGroupid().equals(customerInfop.get(0).getUserinfo().getGroupid().trim()))
                    {
                        customerInfoListGMP = customerInfoList.stream().filter(customerInfo -> customerInfo.getUserinfo().getPost().trim().equals("PG021005")
                                && customerInfo.getUserinfo().getGroupid().trim().equals(otherOrgs.getGroupid())).collect(Collectors.toList());
                        for(CustomerInfo c : customerInfoListGMP){
                            if(!userIdList.contains(c.getUserid()))
                            {
                                userIdList.add(c.getUserid());
                            }
                        }
                    }
                    //兼TL
                    if(otherOrgs.getTeamid() !=null && !otherOrgs.getTeamid().equals("")
                            && !otherOrgs.getTeamid().equals(customerInfop.get(0).getUserinfo().getTeamid().trim()))
                    {
                        customerInfoListGMP = customerInfoList.stream().filter(customerInfo -> customerInfo.getUserinfo().getTeamid().trim().equals(otherOrgs.getTeamid())).collect(Collectors.toList());
                        for(CustomerInfo c : customerInfoListGMP){
                            if(!userIdList.contains(c.getUserid()))
                            {
                                userIdList.add(c.getUserid());
                            }
                        }
                    }
                }
            }
        }
        if(roles.contains("TL"))
        {
            Query query1 = new Query();
            //查询当前登陆人的信息
            query1.addCriteria(Criteria.where("userid").is(userAccountlist.get(0).get_id()));
            List<CustomerInfo> customerInfop = mongoTemplate.find(query1,CustomerInfo.class);
            if(customerInfop.get(0).getUserinfo().getCenterid() == null)
            {
                customerInfop.get(0).getUserinfo().setCenterid("");
            }
            if(customerInfop.get(0).getUserinfo().getGroupid() == null)
            {
                customerInfop.get(0).getUserinfo().setGroupid("");
            }
            if(customerInfop.get(0).getUserinfo().getTeamid() == null)
            {
                customerInfop.get(0).getUserinfo().setTeamid("");
            }
            //查询当前登录人伞下所有的人
            query1 = new Query();
            query1.addCriteria(Criteria.where("userinfo.teamid").is(customerInfop.get(0).getUserinfo().getTeamid().trim()));
            customerInfoListTL = mongoTemplate.find(query1,CustomerInfo.class);
            //追加到最后的人员list中
            for(CustomerInfo c : customerInfoListTL){
                if(!userIdList.contains(c.getUserid()))
                {
                    userIdList.add(c.getUserid());
                }
            }
            //存在兼职
            if(customerInfop.get(0).getUserinfo().getOtherorgs() != null && customerInfop.get(0).getUserinfo().getOtherorgs().size()>0)
            {
                //兼职
                for(CustomerInfo.OtherOrgs otherOrgs : customerInfop.get(0).getUserinfo().getOtherorgs())
                {
                    //兼center  查GM
                    if(otherOrgs.getCenterid() !=null && !otherOrgs.getCenterid().equals("")
                            && !otherOrgs.getCenterid().equals(customerInfop.get(0).getUserinfo().getCenterid().trim()))
                    {
                        customerInfoListTL = customerInfoList.stream().filter(customerInfo -> (customerInfo.getUserinfo().getPost().trim().equals("PG021002") || customerInfo.getUserinfo().getPost().trim().equals("PG021003"))
                                && customerInfo.getUserinfo().getCenterid().trim().equals(otherOrgs.getCenterid())).collect(Collectors.toList());
                        for(CustomerInfo c : customerInfoListTL){
                            if(!userIdList.contains(c.getUserid()))
                            {
                                userIdList.add(c.getUserid());
                            }
                        }
                    }
                    //兼GM   查TL
                    if(otherOrgs.getGroupid() !=null && !otherOrgs.getGroupid().equals("")
                            && !otherOrgs.getGroupid().equals(customerInfop.get(0).getUserinfo().getGroupid().trim()))
                    {
                        customerInfoListTL = customerInfoList.stream().filter(customerInfo -> customerInfo.getUserinfo().getPost().trim().equals("PG021005")
                                && customerInfo.getUserinfo().getGroupid().trim().equals(otherOrgs.getGroupid())).collect(Collectors.toList());
                        for(CustomerInfo c : customerInfoListTL){
                            if(!userIdList.contains(c.getUserid()))
                            {
                                userIdList.add(c.getUserid());
                            }
                        }
                    }
                    //兼TL
                    if(otherOrgs.getTeamid() !=null && !otherOrgs.getTeamid().equals("")
                            && !otherOrgs.getTeamid().equals(customerInfop.get(0).getUserinfo().getTeamid().trim()))
                    {
                        customerInfoListTL = customerInfoList.stream().filter(customerInfo -> customerInfo.getUserinfo().getTeamid().trim().equals(otherOrgs.getTeamid())).collect(Collectors.toList());
                        for(CustomerInfo c : customerInfoListTL){
                            if(!userIdList.contains(c.getUserid()))
                            {
                                userIdList.add(c.getUserid());
                            }
                        }
                    }
                }
            }
        }
        talentPlanListALL = talentPlanMapper.selectByuserId(userIdList);
        //冷美琴 康奕凝
        if(roles.contains("工资计算担当") || roles.contains("人事总务部长"))
        {
            userIdList = null;
            talentPlanListALL = talentPlanMapper.selectByuserId(userIdList);
        }

        return talentPlanListALL;
    }
    @Override
    public TalentPlan One(String talentplan_id) throws Exception {

        TalentPlan log   =talentPlanMapper.selectByPrimaryKey(talentplan_id);
        return log;
    }

    @Override
    public void insertByOrg(TalentPlanVo talentPlanVo, TokenModel tokenModel) throws Exception {

        List<TalentPlan> adds = new ArrayList<TalentPlan>();
        Query query = new Query();
        //根据登陆用户id查看人员信息
        List<CustomerInfo> customerInfos = new ArrayList<CustomerInfo>();
        query.addCriteria(new Criteria().orOperator(Criteria.where("userinfo.centerid").is(talentPlanVo.getOrg()),
                Criteria.where("userinfo.groupid").is(talentPlanVo.getOrg()), Criteria.where("userinfo.teamid").is(talentPlanVo.getOrg())));
        List<CustomerInfo> CustomerInfolist = mongoTemplate.find(query, CustomerInfo.class);

        for(CustomerInfo user : CustomerInfolist){
            if(user.getUserid().equals(tokenModel.getUserId()))
            {
                continue;
            }
            TalentPlan con = new TalentPlan();
            con.setUser_id(user.getUserid());
            con.setYear(talentPlanVo.getYear());
            List<TalentPlan> rst = talentPlanMapper.select(con);
            if(rst.size() == 0){
                TalentPlan add = new TalentPlan();
                add = new TalentPlan();
                add.setUser_id(user.getUserid());
                add.setYear(talentPlanVo.getYear());
                add.setCenter_id(user.getUserinfo().getCenterid());
                add.setGroup_id(user.getUserinfo().getGroupid());
                add.setTeam_id(user.getUserinfo().getTeamid());
                add.setSkilllevel(user.getUserinfo().getRank());
                add.setSkilllevelafter(user.getUserinfo().getRank());
                add.setSchoolspecies(user.getUserinfo().getEducational());
                add.setEntryyear(Convert.toDate(user.getUserinfo().getEnterday()));
                add.setGraduationyear(Convert.toDate(user.getUserinfo().getGraduationday()));
                add.setTalentplan_id(UUID.randomUUID().toString());
                add.preInsert(tokenModel);
                adds.add(add);
            }
        }

        if(adds.size() > 0){
            talentPlanMapper.insertListAllCols(adds);
        }
    }

    @Override
    public void upd (TalentPlan talentPlan, TokenModel tokenModel) throws Exception {
        talentPlan.preUpdate(tokenModel);
        talentPlanMapper.updateByPrimaryKeySelective(talentPlan);
    }
    @Override
    public void insert(TalentPlan talentPlan, TokenModel tokenModel) throws Exception {
        talentPlan.preInsert(tokenModel);
        talentPlan.setTalentplan_id(UUID.randomUUID().toString());
        talentPlanMapper.insert(talentPlan);
    }
}
