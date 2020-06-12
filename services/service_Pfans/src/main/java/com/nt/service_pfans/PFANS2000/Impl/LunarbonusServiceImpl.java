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
import com.nt.utils.LogicalException;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
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
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
//        upd_fjl_06/10 start --添加先申请一次评价的check
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        String evaluationday = simpleDateFormat.format(lunardetailVo.getEvaluationday());
        Lunarbonus lu = new Lunarbonus();
        lu.setEvaluationday(evaluationday);
        lu.setSubjectmon(lunardetailVo.getSubjectmon());
        List<Lunarbonus> Li = lunarbonusMapper.select(lu);
        if (Li.size() > 0) {
            for (Lunarbonus ll : Li) {
                if (!lunardetailVo.getEvaluatenum().equals("PJ104001")) {
                    if (!StringUtils.isNullOrEmpty(ll.getEvaluatenum()) && !ll.getEvaluatenum().equals("PJ104001")) {
                        throw new LogicalException("请先申请【一次評価】");
                    }
                }
            }
        }
//        upd_fjl_06/10 start --添加先申请一次评价的check
        lunarbonus.setEvaluationday(evaluationday);
        lunarbonus.setSubjectmon(lunardetailVo.getSubjectmon());
        lunarbonus.setEvaluatenum(lunardetailVo.getEvaluatenum());

        List<Lunarbonus> List = lunarbonusMapper.select(lunarbonus);
        if (List.size() == 0) {
            lunarbonus.setSubject(lunardetailVo.getSubjectmon());
            lunarbonus.setUser_id(lunardetailVo.getUser_id());
            lunarbonus.preInsert(tokenModel);
            lunarbonus.preUpdate(tokenModel);
            lunarbonus.setLunarbonus_id(UUID.randomUUID().toString());
            lunarbonusMapper.insert(lunarbonus);
        } else {
            throw new LogicalException("不能重复评价");
        }

        if (lunardetailVo.getEvaluatenum().equals("PJ104001")) {
            Query query = new Query();
            SimpleDateFormat sff = new SimpleDateFormat("yyyy-MM-dd");
            List<CustomerInfo> CustomerInfoList = mongoTemplate.find(query, CustomerInfo.class);
//            add_fjl_0608 --填在在职人员筛选
            List<CustomerInfo> cust = new ArrayList<>();
            if (CustomerInfoList.size() > 0) {
                for (int i = 0; i < CustomerInfoList.size(); i++) {
                    if (StringUtils.isNullOrEmpty(CustomerInfoList.get(i).getUserinfo().getResignation_date())) {
                        cust.add(CustomerInfoList.get(i));
                    } else if (!StringUtils.isNullOrEmpty(CustomerInfoList.get(i).getUserinfo().getResignation_date()) && sff.parse(CustomerInfoList.get(i).getUserinfo().getResignation_date()).compareTo(new Date()) > 0) {
                        cust.add(CustomerInfoList.get(i));
                    }
                }
            }
//            add_fjl_0608 --填在在职人员筛选
            for (CustomerInfo customerInfo : cust) {
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

    }

    //获取详情列表初始数据
    @Override
    public LunarAllVo getOne(String id, TokenModel tokenModel) throws Exception {
        LunarAllVo LunarAllVo = new LunarAllVo();
        Lunarbonus lunarbonus = lunarbonusMapper.selectByPrimaryKey(id);
        LunarAllVo.setLunarbonus(lunarbonus);
        Lunardetail lunardetailCondition = new Lunardetail();
        lunardetailCondition.setLunarbonus_id(id);
        if (!"5e78fefff1560b363cdd6db7".equals(tokenModel.getUserId()) && !"5e78b22c4e3b194874180f5f".equals(tokenModel.getUserId()) && !"5e78b2034e3b194874180e37".equals(tokenModel.getUserId())) {
            List<String> users = new ArrayList<String>();
            Query cusquery = new Query();
            cusquery.addCriteria(Criteria.where("userid").is(tokenModel.getUserId()));
            CustomerInfo cus = mongoTemplate.findOne(cusquery, CustomerInfo.class);

            if (cus != null && cus.getUserinfo() != null) {
                List<CustomerInfo> cuslist = new ArrayList<CustomerInfo>();
                String teamid = cus.getUserinfo().getTeamid();
                String groupid = cus.getUserinfo().getGroupid();
                String centerid = cus.getUserinfo().getCenterid();
                if (!StringUtils.isNullOrEmpty(teamid)) {
                    lunardetailCondition.setTeam_id(teamid);
                } else if (!StringUtils.isNullOrEmpty(groupid)) {
                    lunardetailCondition.setGroup_id(groupid);
                } else if (!StringUtils.isNullOrEmpty(centerid)) {
                    lunardetailCondition.setCenter_id(centerid);
                }
            }

        }

        List<Lunardetail> detal = lunardetailMapper.select(lunardetailCondition);
        if (detal.size() > 0) {

            LunarAllVo.setLunardetail(detal);
        } else {
            Lunarbonus con = new Lunarbonus();
            if (lunarbonus.getEvaluatenum().equals("PJ104003")) {
                con.setEvaluatenum("PJ104002");
            } else if (lunarbonus.getEvaluatenum().equals("PJ104002")) {
                con.setEvaluatenum("PJ104001");
            }
            con.setEvaluationday(lunarbonus.getEvaluationday());
            con.setSubjectmon(lunarbonus.getSubjectmon());
            List<Lunarbonus> lunars = lunarbonusMapper.select(con);
            if (lunars.size() > 0) {
                lunardetailCondition.setLunarbonus_id(lunars.get(0).getLunarbonus_id());
                detal = lunardetailMapper.select(lunardetailCondition);
                for(Lunardetail item:detal){
                    item.setLunarbonus_id("");
                    item.setLunardetail_id("");
                }
                LunarAllVo.setLunardetail(detal);
            }else{
                if(lunarbonus.getEvaluatenum().equals("PJ104003")){
                    con.setEvaluatenum("PJ104001");

                    lunars = lunarbonusMapper.select(con);
                    if (lunars.size() > 0) {
                        lunardetailCondition.setLunarbonus_id(lunars.get(0).getLunarbonus_id());
                        detal = lunardetailMapper.select(lunardetailCondition);
                        for(Lunardetail item:detal){
                            item.setLunarbonus_id("");
                            item.setLunardetail_id("");
                        }
                        LunarAllVo.setLunardetail(detal);
                    }
                }
            }
//            LunarAllVo.setLunardetail();
        }
        Lunarbasic lunarbasicConditon = new Lunarbasic();
        lunarbasicConditon.setLunarbonus_id(id);
        LunarAllVo.setLunarbasic(lunarbasicMapper.select(lunarbasicConditon).stream().sorted(Comparator.comparing(Lunarbasic::getIndex)).collect(Collectors.toList()));
        return LunarAllVo;
    }
}
