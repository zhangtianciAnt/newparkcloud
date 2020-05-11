package com.nt.service_pfans.PFANS2000.Impl;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import com.nt.dao_Org.CustomerInfo;
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

@Service
@Transactional(rollbackFor=Exception.class)
public class TalentPlanServiceImpl implements TalentPlanService {

    @Autowired
    private TalentPlanMapper talentPlanMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<TalentPlan> list(TalentPlan talentPlan ) throws Exception {
        return talentPlanMapper.select(talentPlan);
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
