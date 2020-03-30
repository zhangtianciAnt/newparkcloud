package com.nt.service_pfans.PFANS2000.Impl;

import com.mysql.jdbc.StringUtils;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Pfans.PFANS2000.Lunarbasic;
import com.nt.dao_Pfans.PFANS2000.Lunarbonus;
import com.nt.dao_Pfans.PFANS2000.Lunardetail;
import com.nt.dao_Pfans.PFANS2000.Vo.LunarAllVo;
import com.nt.dao_Pfans.PFANS2000.Vo.LunardetailVo;
import com.nt.service_pfans.PFANS2000.LunarbonusService;
import com.nt.service_pfans.PFANS2000.mapper.ExaminationobjectMapper;
import com.nt.service_pfans.PFANS2000.mapper.LunarbasicMapper;
import com.nt.service_pfans.PFANS2000.mapper.LunarbonusMapper;
import com.nt.service_pfans.PFANS2000.mapper.LunardetailMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class LunarbonusServiceImpl implements LunarbonusService {


    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private LunarbonusMapper lunarbonusMapper;

    @Autowired
    private LunardetailMapper lunardetailMapper;

    @Autowired
    private ExaminationobjectMapper examinationobjectMapper;

    @Autowired
    private LunarbasicMapper lunarbasicMapper;

    //获取一览
    @Override
    public List<Lunarbonus> getList() throws Exception {
        Lunarbonus lunarbonus = new Lunarbonus();
        return lunarbonusMapper.select(lunarbonus);
    }

    //新建一览和详情
    @Override
    public void insert(LunardetailVo lunardetailVo, TokenModel tokenModel) throws Exception {
        Lunarbonus lunarbonus = new Lunarbonus();
        lunarbonus.preInsert(tokenModel);
        lunarbonus.setLunarbonus_id(UUID.randomUUID().toString());
        lunarbonus.setEvaluationday(new Date());
        lunarbonus.setSubjectmon(lunardetailVo.getSubjectmon());
        lunarbonus.setEvaluatenum(lunardetailVo.getEvaluatenum());
        lunarbonus.setSubject(lunardetailVo.getSubjectmon());
        lunarbonus.setUser_id(lunardetailVo.getUser_id());
        lunarbonusMapper.insert(lunarbonus);

        Query query = new Query();
        List<CustomerInfo> CustomerInfoList = mongoTemplate.find(query, CustomerInfo.class);
        for (CustomerInfo customerInfo : CustomerInfoList) {
            Lunardetail lunardetail = new Lunardetail();
            if (customerInfo != null) {
                SimpleDateFormat sf = new SimpleDateFormat("yyyy");
                Date date = new Date();
                String da = sf.format(date);

                lunardetail.preInsert(tokenModel);
                lunardetail.setLunardetail_id(UUID.randomUUID().toString());
                lunardetail.setSubjectmon(lunarbonus.getSubjectmon());
                lunardetail.setEvaluatenum(lunarbonus.getEvaluatenum());
                lunardetail.setLunarbonus_id(lunarbonus.getLunarbonus_id());
                lunardetail.setExaminationobject_id(lunardetailVo.getExaminationobject_id());
                lunardetail.setEvaluationday(da);
                lunardetail.setUser_id(customerInfo.getUserid());
                lunardetail.setRn(customerInfo.getUserinfo().getRank());
                lunardetail.setEnterday(customerInfo.getUserinfo().getEnterday());
                lunardetail.setGroup_id(customerInfo.getUserinfo().getGroupid());
                lunardetail.setSalary(customerInfo.getUserinfo().getSalary());
                lunardetail.setTeam_id(customerInfo.getUserinfo().getTeamid());
                lunardetail.setCenter_id(customerInfo.getUserinfo().getCenterid());
                lunardetail.setDifference(customerInfo.getUserinfo().getDifference());
                lunardetail.setOccupationtype(customerInfo.getUserinfo().getOccupationtype());
                lunardetailMapper.insert(lunardetail);
            }
        }
    }

    //获取详情列表初始数据
    @Override
    public LunarAllVo getOne(String id,TokenModel tokenModel) throws Exception {
        LunarAllVo LunarAllVo = new LunarAllVo();
        LunarAllVo.setLunarbonus(lunarbonusMapper.selectByPrimaryKey(id));
        Lunardetail lunardetailCondition = new Lunardetail();
        lunardetailCondition.setLunarbonus_id(id);
        if(!"5e78fefff1560b363cdd6db7".equals(tokenModel.getUserId())){
            List<String> users = new ArrayList<String>();
            Query cusquery = new Query();
            cusquery.addCriteria(Criteria.where("userid").is(tokenModel.getUserId()));
            CustomerInfo cus = mongoTemplate.findOne(cusquery, CustomerInfo.class);

            List<CustomerInfo> cuslist = new ArrayList<CustomerInfo>();
            String teamid = cus.getUserinfo().getTeamid();
            String groupid = cus.getUserinfo().getGroupid();
            String centerid = cus.getUserinfo().getCenterid();
            if(!StringUtils.isNullOrEmpty(teamid)){
                lunardetailCondition.setTeam_id(teamid);
            }
            else if(!StringUtils.isNullOrEmpty(groupid)){
                lunardetailCondition.setGroup_id(groupid);
            }
            else if(!StringUtils.isNullOrEmpty(centerid)){
                lunardetailCondition.setCenter_id(centerid);
            }
        }

        LunarAllVo.setLunardetail(lunardetailMapper.select(lunardetailCondition));
        Lunarbasic lunarbasicConditon = new Lunarbasic();
        lunarbasicConditon.setLunarbonus_id(id);
        LunarAllVo.setLunarbasic(lunarbasicMapper.select(lunarbasicConditon).stream().sorted(Comparator.comparing(Lunarbasic::getIndex)).collect(Collectors.toList()));
        return LunarAllVo ;
    }
}
