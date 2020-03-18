package com.nt.service_pfans.PFANS2000.Impl;

import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Pfans.PFANS2000.Lunarbonus;
import com.nt.dao_Pfans.PFANS2000.Lunardetail;
import com.nt.dao_Pfans.PFANS2000.Vo.LunardetailVo;
import com.nt.service_pfans.PFANS2000.LunarbonusService;
import com.nt.service_pfans.PFANS2000.mapper.ExaminationobjectMapper;
import com.nt.service_pfans.PFANS2000.mapper.LunarbonusMapper;
import com.nt.service_pfans.PFANS2000.mapper.LunardetailMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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

//        Query query = new Query();
//        List<CustomerInfo> CustomerInfoList = mongoTemplate.find(query, CustomerInfo.class);
//        for (CustomerInfo customerInfo : CustomerInfoList) {
//            Lunardetail lunardetail = new Lunardetail();
//            if (customerInfo != null) {
//                if("PJ103001".equals(lunardetailVo.getSubjectmon())){
//
//                }
//                SimpleDateFormat sf = new SimpleDateFormat("yyyy");
//                Date date = new Date();
//                String da = sf.format(date);
//
//                lunardetail.preInsert(tokenModel);
//                lunardetail.setLunardetail_id(UUID.randomUUID().toString());
//                lunardetail.setSubjectmon(lunarbonus.getSubjectmon());
//                lunardetail.setEvaluatenum(lunarbonus.getEvaluatenum());
//                lunardetail.setLunarbonus_id(lunarbonus.getLunarbonus_id());
//                lunardetail.setExaminationobject_id(lunardetailVo.getExaminationobject_id());
//                lunardetail.setEvaluationday(da);
//                lunardetail.setUser_id(customerInfo.getUserid());
//                lunardetail.setRn(customerInfo.getUserinfo().getRank());
//                lunardetail.setEnterday(customerInfo.getUserinfo().getEnterday());
//                lunardetail.setGroup_id(customerInfo.getUserinfo().getGroupid());
//                lunardetail.setSalary(customerInfo.getUserinfo().getSalary());
//                lunardetail.setTeam_id(customerInfo.getUserinfo().getTeamid());
//                lunardetail.setDifference(customerInfo.getUserinfo().getDifference());
//                lunardetailMapper.insert(lunardetail);
//            }
//        }
    }

    //获取详情列表初始数据
    @Override
    public Lunarbonus getOne(String id) throws Exception {
        return lunarbonusMapper.selectByPrimaryKey(id);
    }
}
