package com.nt.service_pfans.PFANS1000.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Pfans.PFANS1000.Employed;
import com.nt.dao_Pfans.PFANS1000.Moneyavg;
import com.nt.dao_Pfans.PFANS1000.PersonnelPlan;
import com.nt.dao_Pfans.PFANS1000.Vo.ExternalVo;
import com.nt.dao_Pfans.PFANS2000.PersonalCost;
import com.nt.dao_Pfans.PFANS6000.Supplierinfor;
import com.nt.service_pfans.PFANS1000.PersonnelplanService;
import com.nt.service_pfans.PFANS1000.mapper.PersonnelplanMapper;
import com.nt.service_pfans.PFANS2000.mapper.PersonalCostMapper;
import com.nt.utils.LogicalException;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PersonnelplanServiceImpl implements PersonnelplanService {

    @Autowired
    private PersonnelplanMapper personnelplanMapper;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private PersonalCostMapper personalcostmapper;
    @Override
    public List<CustomerInfo> SelectCustomer(String id) {
        Query query = new Query();
        Criteria criteria = Criteria.where("userinfo.groupid").is(id);
        query.addCriteria(criteria);
        List<CustomerInfo> customerInfos = mongoTemplate.find(query, CustomerInfo.class);
        return customerInfos;
    }

    @Override
    public List<Supplierinfor> getExternal() {
         List<Supplierinfor> supplierinfors = personnelplanMapper.getSupplierinfor();
        return supplierinfors;
    }

    @Override
    public List<ExternalVo> getExpatriatesinfor(String groupid) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
        Date PD_DATE = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(PD_DATE); // 设置为当前时间
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1); // 设置为上一个月
        PD_DATE = calendar.getTime();
        String pdStr = formatter.format(PD_DATE);
        List<ExternalVo> externalVos = personnelplanMapper.getExternal(groupid,pdStr);
        return externalVos;
    }

    @Override
    public List<PersonnelPlan> getAll(PersonnelPlan personnelplan) throws Exception {
        return personnelplanMapper.select(personnelplan);
    }

    @Override
    public PersonnelPlan getOne(String id) {
        PersonnelPlan personnelPlan =  personnelplanMapper.selectByPrimaryKey(id);
        return personnelPlan;
    }

    @Override
    public void update(PersonnelPlan personnelPlan, TokenModel tokenModel) {
        personnelPlan.preUpdate(tokenModel);
        personnelplanMapper.updateByPrimaryKeySelective(personnelPlan);
    }

    @Override
    public void insert(PersonnelPlan personnelPlan, TokenModel tokenModel) throws LogicalException{
        PersonnelPlan personnel = new PersonnelPlan();
        personnel.setYears(personnelPlan.getYears());
        personnel.setCenterid(personnelPlan.getGroupid());
        personnel.setType(personnelPlan.getType());
        List<PersonnelPlan>  personnelPlanList = personnelplanMapper.select(personnel);
        if(personnelPlanList.size()>0){
            throw new LogicalException("当前年份已填写人员计划");
        }
        personnelPlan.preInsert(tokenModel);
        personnelPlan.setPersonnelplanid(UUID.randomUUID().toString());
        List<Moneyavg> moneyavgList = JSON.parseArray(personnelPlan.getEmployed(), Moneyavg.class);
        BigDecimal moneyavgSum = new BigDecimal("0.0");
        int perNum = 0;
        for(Moneyavg mavg : moneyavgList){
            perNum ++;
            //update gbb 20210415 事业计划-外驻计划-新建时统计值用【Unitprice】 start
            if(mavg.getSummerplanpc() != null){
                BigDecimal summerMoney = new BigDecimal(mavg.getSummerplanpc());
                BigDecimal winterMoney = new BigDecimal(mavg.getWinterplanpc());
                moneyavgSum = moneyavgSum.add(summerMoney.add(winterMoney));
            }
            else{
                BigDecimal unitprice = new BigDecimal(mavg.getUnitprice());
                moneyavgSum = moneyavgSum.add(unitprice);
            }
            //update gbb 20210415 事业计划-外驻计划-新建时统计值用【Unitprice】 end
        }

        List<Moneyavg> newmoneyavgList = JSON.parseArray(personnelPlan.getNewentry(), Moneyavg.class);
        for(Moneyavg newmavg : newmoneyavgList){
            perNum ++;
            BigDecimal newsummerMoney = newmavg.getSummerplanpc() == null? new BigDecimal(0.0) : new BigDecimal(newmavg.getSummerplanpc());
            BigDecimal newwinterMoney = newmavg.getSummerplanpc() == null? new BigDecimal(0.0) : new BigDecimal(newmavg.getSummerplanpc());
            moneyavgSum = moneyavgSum.add(newsummerMoney.add(newwinterMoney));
        }
        BigDecimal perNumBig = new BigDecimal(String.valueOf(perNum));
        moneyavgSum = moneyavgSum.divide(perNumBig,2, BigDecimal.ROUND_HALF_UP);
        personnelPlan.setMoneyavg(moneyavgSum.toString());
        personnelplanMapper.insert(personnelPlan);
    }

    @Override
    public List<PersonnelPlan> get(PersonnelPlan personnelPlan) {
      personnelPlan.setStatus("0");
      List<PersonnelPlan> personnelPlan1 =  personnelplanMapper.select(personnelPlan);

      return  personnelPlan1;
    }
    // add-lyt-21/1/29-禅道任务648-start
    @Override
    public List<PersonalCost> getPersonalCost(String groupid, String years) throws Exception {
        List<PersonalCost> personalcost =  personalcostmapper.getPersonalCost(groupid,years);
        return personalcost;
    }
   // add-lyt-21/1/29-禅道任务648-end

}
