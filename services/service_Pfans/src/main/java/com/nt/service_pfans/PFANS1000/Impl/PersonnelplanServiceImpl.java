package com.nt.service_pfans.PFANS1000.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.mysql.jdbc.StringUtils;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_Pfans.PFANS1000.*;
import com.nt.dao_Pfans.PFANS1000.Vo.BusinessInBase;
import com.nt.dao_Pfans.PFANS1000.Vo.BusinessOutBase;
import com.nt.dao_Pfans.PFANS1000.Vo.ExternalVo;
import com.nt.dao_Pfans.PFANS2000.PersonalCost;
import com.nt.dao_Pfans.PFANS4000.PeoplewareFee;
import com.nt.dao_Pfans.PFANS6000.Supplierinfor;
import com.nt.service_Org.DictionaryService;
import com.nt.service_pfans.PFANS1000.BusinessplanService;
import com.nt.service_pfans.PFANS1000.PersonnelplanService;
import com.nt.service_pfans.PFANS1000.mapper.BusinessplanMapper;
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

    @Autowired
    private BusinessplanMapper businessplanMapper;

    @Autowired
    private BusinessplanService businessplanService;

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
        //事业计划状态为0或3时修改人员计划，事业计划数据变更 ztc fr
        String[] resultPlan = {};
        List<JSONObject> resultTableP = new ArrayList<>();
        Businessplan businessplan = new Businessplan();
        businessplan.setYear(personnelPlan.getYears());
        businessplan.setCenter_id(personnelPlan.getCenterid());
        List<Businessplan> busplanList = businessplanMapper.select(businessplan);
        try {
            resultPlan = businessplanService.getPersonPlan(personnelPlan.getYears(),personnelPlan.getCenterid());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(busplanList.size() != 0 && (busplanList.get(0).getStatus().equals("0") || busplanList.get(0).getStatus().equals("3"))) {
            busplanList.get(0).setTableC(resultPlan[0]);
            busplanList.get(0).setTableD(resultPlan[1]);
            busplanList.get(0).setTableA("[" + resultPlan[2] + "]");
            JSONArray tablePList = JSON.parseArray(busplanList.get(0).getTableP());
            List<BusinessInBase> businessTablePList = JSONArray.parseObject(tablePList.toJSONString(), new TypeReference<List<BusinessInBase>>() {});
            JSONObject jsonObjectTbP = JSON.parseObject(String.valueOf(businessTablePList.get(4)));
            JSONObject jsonObjectTbP9 = JSON.parseObject(String.valueOf(businessTablePList.get(9)));//构外
            JSONObject jsonObjectTbP10 = JSON.parseObject(String.valueOf(businessTablePList.get(10)));//构内
            if(resultPlan[2] != null){
                JSONObject resultPlanJson = JSON.parseObject(resultPlan[2]);
                jsonObjectTbP.put("money4", Integer.valueOf(resultPlanJson.getString("amount4")));
                jsonObjectTbP.put("money5", Integer.valueOf(resultPlanJson.getString("amount5")));
                jsonObjectTbP.put("money6", Integer.valueOf(resultPlanJson.getString("amount6")));
                jsonObjectTbP.put("money7", Integer.valueOf(resultPlanJson.getString("amount7")));
                jsonObjectTbP.put("money8", Integer.valueOf(resultPlanJson.getString("amount8")));
                jsonObjectTbP.put("money9", Integer.valueOf(resultPlanJson.getString("amount9")));
                jsonObjectTbP.put("money10", Integer.valueOf(resultPlanJson.getString("amount10")));
                jsonObjectTbP.put("money11", Integer.valueOf(resultPlanJson.getString("amount11")));
                jsonObjectTbP.put("money12", Integer.valueOf(resultPlanJson.getString("amount12")));
                jsonObjectTbP.put("money1", Integer.valueOf(resultPlanJson.getString("amount1")));
                jsonObjectTbP.put("money2", Integer.valueOf(resultPlanJson.getString("amount2")));
                jsonObjectTbP.put("money3", Integer.valueOf(resultPlanJson.getString("amount3")));
            }
            //外驻新人
            if (resultPlan[6] != null) {
                int[] inside = new int[12]; //4~12~3月  构内
                int[] outside = new int[12]; //4~12~3月 构外
                JSONArray resultPlan6 = JSON.parseArray(resultPlan[6]);
                List<BusinessOutBase> businessTableP6 = JSONArray.parseObject(resultPlan6.toJSONString(), new TypeReference<List<BusinessOutBase>>() {
                });
                for (int t = 0; t < businessTableP6.size(); t++) {
                    int monthAnt = 0;
                    if (businessTableP6.get(t).getString("isoutside").equals("false")) {//构内
                        monthAnt = Integer.parseInt(businessTableP6.get(t).getString("entermouth").substring(5, 7));
                        if (monthAnt >= 4) {
                            inside[monthAnt - 4] = inside[monthAnt - 4] + 1;
                        } else {
                            inside[monthAnt + 8] = inside[monthAnt + 8] + 1;
                        }
                    } else {//构外
                        monthAnt = Integer.parseInt(businessTableP6.get(t).getString("entermouth").substring(5, 7));
                        if (monthAnt >= 4) {
                            outside[monthAnt - 4] = inside[monthAnt - 4] + 1;
                        } else {
                            outside[monthAnt + 8] = inside[monthAnt + 8] + 1;
                        }
                    }
                }
                //                事业计划人件费单价 每个月份乘以人数 ztc fr
                int inAnt = Integer.parseInt(resultPlan[4] != null ? resultPlan[4] : "0");
                int[] insideResult = new int[12];
                for (int ins = 0; ins < inside.length; ins++) {
                    inAnt = inside[ins] + inAnt;
                    insideResult[ins] = inAnt;
                }
                int outAnt = Integer.parseInt(resultPlan[5] != null ? resultPlan[5] : "0");
                //                事业计划人件费单价 每个月份乘以人数 ztc to
                int[] outsideResult = new int[12];
                for (int outs = 0; outs < outside.length; outs++) {
                    outAnt = outside[outs] + outAnt;
                    outsideResult[outs] = outAnt;
                }
                jsonObjectTbP10.put("money4", insideResult[0]);
                jsonObjectTbP10.put("money5", insideResult[1]);
                jsonObjectTbP10.put("money6", insideResult[2]);
                jsonObjectTbP10.put("money7", insideResult[3]);
                jsonObjectTbP10.put("money8", insideResult[4]);
                jsonObjectTbP10.put("money9", insideResult[5]);
                jsonObjectTbP10.put("money10", insideResult[6]);
                jsonObjectTbP10.put("money11", insideResult[7]);
                jsonObjectTbP10.put("money12", insideResult[8]);
                jsonObjectTbP10.put("money1", insideResult[9]);
                jsonObjectTbP10.put("money2", insideResult[10]);
                jsonObjectTbP10.put("money3", insideResult[11]);
                jsonObjectTbP9.put("money4", outsideResult[0]);
                jsonObjectTbP9.put("money5", outsideResult[1]);
                jsonObjectTbP9.put("money6", outsideResult[2]);
                jsonObjectTbP9.put("money7", outsideResult[3]);
                jsonObjectTbP9.put("money8", outsideResult[4]);
                jsonObjectTbP9.put("money9", outsideResult[5]);
                jsonObjectTbP9.put("money10", outsideResult[6]);
                jsonObjectTbP9.put("money11", outsideResult[7]);
                jsonObjectTbP9.put("money12", outsideResult[8]);
                jsonObjectTbP9.put("money1", outsideResult[9]);
                jsonObjectTbP9.put("money2", outsideResult[10]);
                jsonObjectTbP9.put("money3", outsideResult[11]);
            }

            for (int i = 0; i < businessTablePList.size(); i++) {
                if (i == 4) {
                    resultTableP.add(jsonObjectTbP);
                } else if (i == 9) {
                    resultTableP.add(jsonObjectTbP9);
                } else if (i == 10) {
                    resultTableP.add(jsonObjectTbP10);
                } else {
                    resultTableP.add(JSONObject.parseObject(String.valueOf(businessTablePList.get(i))));
                }
            }
            busplanList.get(0).setTableP(String.valueOf(resultTableP));
            businessplanMapper.updateByPrimaryKey(busplanList.get(0));
        }
//        事业计划状态为0或3时修改人员计划，事业计划数据变更 ztc to
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
