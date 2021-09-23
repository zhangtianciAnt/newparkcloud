package com.nt.service_pfans.PFANS1000.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mysql.jdbc.StringUtils;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_Pfans.PFANS1000.Employed;
import com.nt.dao_Pfans.PFANS1000.Moneyavg;
import com.nt.dao_Pfans.PFANS1000.PersonnelPlan;
import com.nt.dao_Pfans.PFANS1000.Vo.ExternalVo;
import com.nt.dao_Pfans.PFANS2000.PersonalCost;
import com.nt.dao_Pfans.PFANS4000.PeoplewareFee;
import com.nt.dao_Pfans.PFANS6000.Supplierinfor;
import com.nt.service_Org.DictionaryService;
import com.nt.service_pfans.PFANS1000.PersonnelplanService;
import com.nt.service_pfans.PFANS1000.mapper.PersonnelplanMapper;
import com.nt.service_pfans.PFANS2000.mapper.PersonalCostMapper;
import com.nt.service_pfans.PFANS4000.mapper.PeoplewareFeeMapper;
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
import java.util.stream.Collectors;

@Service
public class PersonnelplanServiceImpl implements PersonnelplanService {

    @Autowired
    private PersonnelplanMapper personnelplanMapper;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private PersonalCostMapper personalcostmapper;

    @Autowired
    private PeoplewareFeeMapper peoplewarefeeMapper;

    @Autowired
    private DictionaryService dictionaryService;
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
        personnel.setCenterid(personnelPlan.getCenterid());
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
            if(!StringUtils.isNullOrEmpty(mavg.getSummerplanpc())){
                BigDecimal summerMoney = new BigDecimal(mavg.getSummerplanpc());
                moneyavgSum = moneyavgSum.add(summerMoney);
            }
            else{
                BigDecimal unitprice = new BigDecimal(mavg.getUnitprice() == null ? "0":mavg.getUnitprice());
                moneyavgSum = moneyavgSum.add(unitprice);
            }
            //update gbb 20210415 事业计划-外驻计划-新建时统计值用【Unitprice】 end
        }

        List<Moneyavg> newmoneyavgList = JSON.parseArray(personnelPlan.getNewentry(), Moneyavg.class);
        if(newmoneyavgList!=null)
        {
            for(Moneyavg newmavg : newmoneyavgList){
                perNum ++;
                if(!StringUtils.isNullOrEmpty(newmavg.getSummerplanpc())){
                    BigDecimal newsummerMoney = new BigDecimal(newmavg.getSummerplanpc());
                    moneyavgSum = moneyavgSum.add(newsummerMoney);
                }
                else
                {
                    BigDecimal unitprice = new BigDecimal(newmavg.getUnitprice() == null ? "0":newmavg.getUnitprice());
                    moneyavgSum = moneyavgSum.add(unitprice);
                }
            }
        }

        BigDecimal perNumBig = new BigDecimal(String.valueOf(perNum));
        if(personnelPlan.getType()==0)
        {
            moneyavgSum = moneyavgSum.divide(perNumBig.multiply(new BigDecimal(2)),2, BigDecimal.ROUND_HALF_UP);
        }
        else if(personnelPlan.getType()==1)
        {
            moneyavgSum = moneyavgSum.divide((perNumBig),2, BigDecimal.ROUND_HALF_UP);
        }

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
        List<PersonalCost> personalcost =  new ArrayList<>();

        //查询rank字典
        List<Dictionary> dictionaryRank = dictionaryService.getForSelect("PR021");
        //查询当前部门，当前年度的rank人件费
        PeoplewareFee pw = new PeoplewareFee();
        pw.setGroupid(groupid);
        pw.setYear(years);
        List<PeoplewareFee> peoplewareFeeList = peoplewarefeeMapper.select(pw);
        if(peoplewareFeeList.size()>0)
        {
            //查询当前年度当前部门所有人的信息
            Query query = new Query();
            query.addCriteria(new Criteria().orOperator(Criteria.where("userinfo.centerid").is(groupid),Criteria.where("userinfo.groupid").is(groupid)));
            //todo 筛选条件需要修改
//            query.addCriteria(new Criteria().orOperator(Criteria.where("userinfo.resignation_date").exists(false),new Criteria().andOperator(Criteria.where("userinfo.resignation_date").exists(true),Criteria.where("userinfo.resignation_date").ne(null).ne(""))));
            List<CustomerInfo> customerInfoList = mongoTemplate.find(query,CustomerInfo.class);
            for(CustomerInfo customerInfo : customerInfoList)
            {
                PersonalCost p = new PersonalCost();
                p.setUserid(customerInfo.getUserid());
                p.setUsername(customerInfo.getUserinfo().getCustomername());
                p.setExrank(customerInfo.getUserinfo().getRank());
                p.setLtrank(customerInfo.getUserinfo().getRank());
                //暂存年度平均值
                p.setAptoju(peoplewareFeeList.stream().filter(item->item.getRanks().equals(
                        dictionaryRank.stream().filter(i->i.getCode().equals(customerInfo.getUserinfo().getRank())).collect(Collectors.toList())
                                .get(0).getValue1())).collect(Collectors.toList()).get(0).getAgeprice());
                personalcost.add(p);
            }
        }
        else
        {
            throw new LogicalException("当前年度的人件费还未导入，请确认！");
        }
        return personalcost;
    }
   // add-lyt-21/1/29-禅道任务648-end

}
