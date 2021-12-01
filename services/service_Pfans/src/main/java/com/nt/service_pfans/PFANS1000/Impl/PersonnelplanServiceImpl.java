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
import com.nt.dao_Pfans.PFANS1000.Vo.BusinessTableOBase;
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

    //view添加分页 ztc 1130 fr
    @Override
    public List<PersonnelPlan> getListforType(PersonnelPlan personnelplan) throws Exception {
        return personnelplanMapper.select(personnelplan);
    }
    //view添加分页 ztc 1130 to

    @Override
    public PersonnelPlan getOne(String id) {
        PersonnelPlan personnelPlan =  personnelplanMapper.selectByPrimaryKey(id);
        return personnelPlan;
    }

    public void updateBusinessInfo(List<Businessplan> busplanList,String[] resultPlan){
        List<JSONObject> resultTableP = new ArrayList<>();
        List<JSONObject> resultTableO = new ArrayList<>();
        int num4 = 0;
        int num5 = 0;
        int num6 = 0;
        int num7 = 0;
        int num8 = 0;
        int num9 = 0;
        int num10 = 0;
        int num11 = 0;
        int num12 = 0;
        int num1 = 0;
        int num2 = 0;
        int num3 = 0;
        int numRc4 = 0;
        int numRc5 = 0;
        int numRc6 = 0;
        int numRc7 = 0;
        int numRc8 = 0;
        int numRc9 = 0;
        int numRc10 = 0;
        int numRc11 = 0;
        int numRc12 = 0;
        int numRc1 = 0;
        int numRc2 = 0;
        int numRc3 = 0;
        busplanList.get(0).setTableC(resultPlan[0]);
        busplanList.get(0).setTableD(resultPlan[1]);
        busplanList.get(0).setTableA("[" + resultPlan[2] + "]");
        JSONArray tablePList = JSON.parseArray(busplanList.get(0).getTableP());
        List<BusinessInBase> businessTablePList = JSONArray.parseObject(tablePList.toJSONString(), new TypeReference<List<BusinessInBase>>() {});
        JSONObject jsonObjectTbP = JSON.parseObject(String.valueOf(businessTablePList.get(4)));
        JSONObject jsonObjectTbP9 = JSON.parseObject(String.valueOf(businessTablePList.get(9)));//构外
        JSONObject jsonObjectTbP10 = JSON.parseObject(String.valueOf(businessTablePList.get(10)));//构内
        if(resultPlan[2] != null){//社内
            JSONObject resultPlanJson = JSON.parseObject(resultPlan[2]);
            jsonObjectTbP.put("money4", Integer.valueOf(resultPlanJson.getString("amount4")));
            numRc4 = Integer.parseInt(resultPlanJson.getString("amount4"));
            jsonObjectTbP.put("money5", Integer.valueOf(resultPlanJson.getString("amount5")));
            numRc5 = Integer.parseInt(resultPlanJson.getString("amount5"));
            jsonObjectTbP.put("money6", Integer.valueOf(resultPlanJson.getString("amount6")));
            numRc6 = Integer.parseInt(resultPlanJson.getString("amount6"));
            jsonObjectTbP.put("money7", Integer.valueOf(resultPlanJson.getString("amount7")));
            numRc7 = Integer.parseInt(resultPlanJson.getString("amount7"));
            jsonObjectTbP.put("money8", Integer.valueOf(resultPlanJson.getString("amount8")));
            numRc8 = Integer.parseInt(resultPlanJson.getString("amount8"));
            jsonObjectTbP.put("money9", Integer.valueOf(resultPlanJson.getString("amount9")));
            numRc9 = Integer.parseInt(resultPlanJson.getString("amount9"));
            jsonObjectTbP.put("money10", Integer.valueOf(resultPlanJson.getString("amount10")));
            numRc10 = Integer.parseInt(resultPlanJson.getString("amount10"));
            jsonObjectTbP.put("money11", Integer.valueOf(resultPlanJson.getString("amount11")));
            numRc11 = Integer.parseInt(resultPlanJson.getString("amount11"));
            jsonObjectTbP.put("money12", Integer.valueOf(resultPlanJson.getString("amount12")));
            numRc12 = Integer.parseInt(resultPlanJson.getString("amount12"));
            jsonObjectTbP.put("money1", Integer.valueOf(resultPlanJson.getString("amount1")));
            numRc1 = Integer.parseInt(resultPlanJson.getString("amount1"));
            jsonObjectTbP.put("money2", Integer.valueOf(resultPlanJson.getString("amount2")));
            numRc2 = Integer.parseInt(resultPlanJson.getString("amount2"));
            jsonObjectTbP.put("money3", Integer.valueOf(resultPlanJson.getString("amount3")));
            numRc3 = Integer.parseInt(resultPlanJson.getString("amount3"));
        }
        //region
        //外驻新人
//            if (resultPlan[6] != null) {
//                int[] inside = new int[12]; //4~12~3月  构内
//                int[] outside = new int[12]; //4~12~3月 构外
//                JSONArray resultPlan6 = JSON.parseArray(resultPlan[6]);
//                List<BusinessOutBase> businessTableP6 = JSONArray.parseObject(resultPlan6.toJSONString(), new TypeReference<List<BusinessOutBase>>() {
//                });
//                for (int t = 0; t < businessTableP6.size(); t++) {
//                    int monthAnt = 0;
//                    if (businessTableP6.get(t).getString("isoutside").equals("false")) {//构内
//                        monthAnt = Integer.parseInt(businessTableP6.get(t).getString("entermouth").substring(5, 7));
//                        if (monthAnt >= 4) {
//                            inside[monthAnt - 4] = inside[monthAnt - 4] + 1;
//                        } else {
//                            inside[monthAnt + 8] = inside[monthAnt + 8] + 1;
//                        }
//                    } else {//构外
//                        monthAnt = Integer.parseInt(businessTableP6.get(t).getString("entermouth").substring(5, 7));
//                        if (monthAnt >= 4) {
//                            outside[monthAnt - 4] = inside[monthAnt - 4] + 1;
//                        } else {
//                            outside[monthAnt + 8] = inside[monthAnt + 8] + 1;
//                        }
//                    }
//                }
//                //                事业计划人件费单价 每个月份乘以人数 ztc fr
//                int inAnt = Integer.parseInt(resultPlan[4] != null ? resultPlan[4] : "0");
//                int[] insideResult = new int[12];
//                for (int ins = 0; ins < inside.length; ins++) {
//                    inAnt = inside[ins] + inAnt;
//                    insideResult[ins] = inAnt;
//                }
//                int outAnt = Integer.parseInt(resultPlan[5] != null ? resultPlan[5] : "0");
//                //                事业计划人件费单价 每个月份乘以人数 ztc to
//                int[] outsideResult = new int[12];
//                for (int outs = 0; outs < outside.length; outs++) {
//                    outAnt = outside[outs] + outAnt;
//                    outsideResult[outs] = outAnt;
//                }
//                jsonObjectTbP10.put("money4", insideResult[0]);
//                jsonObjectTbP10.put("money5", insideResult[1]);
//                jsonObjectTbP10.put("money6", insideResult[2]);
//                jsonObjectTbP10.put("money7", insideResult[3]);
//                jsonObjectTbP10.put("money8", insideResult[4]);
//                jsonObjectTbP10.put("money9", insideResult[5]);
//                jsonObjectTbP10.put("money10", insideResult[6]);
//                jsonObjectTbP10.put("money11", insideResult[7]);
//                jsonObjectTbP10.put("money12", insideResult[8]);
//                jsonObjectTbP10.put("money1", insideResult[9]);
//                jsonObjectTbP10.put("money2", insideResult[10]);
//                jsonObjectTbP10.put("money3", insideResult[11]);
//                jsonObjectTbP9.put("money4", outsideResult[0]);
//                jsonObjectTbP9.put("money5", outsideResult[1]);
//                jsonObjectTbP9.put("money6", outsideResult[2]);
//                jsonObjectTbP9.put("money7", outsideResult[3]);
//                jsonObjectTbP9.put("money8", outsideResult[4]);
//                jsonObjectTbP9.put("money9", outsideResult[5]);
//                jsonObjectTbP9.put("money10", outsideResult[6]);
//                jsonObjectTbP9.put("money11", outsideResult[7]);
//                jsonObjectTbP9.put("money12", outsideResult[8]);
//                jsonObjectTbP9.put("money1", outsideResult[9]);
//                jsonObjectTbP9.put("money2", outsideResult[10]);
//                jsonObjectTbP9.put("money3", outsideResult[11]);
//            }
        //endregion
        if(resultPlan[4] != null){//构内
            JSONObject insideResult = JSON.parseObject(resultPlan[4]);
            jsonObjectTbP10.put("money4", String.valueOf(insideResult.getString("april")));
            num4 += Integer.parseInt(insideResult.getString("april"));
            jsonObjectTbP10.put("money5", String.valueOf(insideResult.getString("may")));
            num5 += Integer.parseInt(insideResult.getString("may"));
            jsonObjectTbP10.put("money6", String.valueOf(insideResult.getString("june")));
            num6 += Integer.parseInt(insideResult.getString("june"));
            jsonObjectTbP10.put("money7", String.valueOf(insideResult.getString("july")));
            num7 += Integer.parseInt(insideResult.getString("july"));
            jsonObjectTbP10.put("money8", String.valueOf(insideResult.getString("august")));
            num8 += Integer.parseInt(insideResult.getString("august"));
            jsonObjectTbP10.put("money9", String.valueOf(insideResult.getString("september")));
            num9 += Integer.parseInt(insideResult.getString("september"));
            jsonObjectTbP10.put("money10", String.valueOf(insideResult.getString("october")));
            num10 += Integer.parseInt(insideResult.getString("october"));
            jsonObjectTbP10.put("money11", String.valueOf(insideResult.getString("november")));
            num11 += Integer.parseInt(insideResult.getString("november"));
            jsonObjectTbP10.put("money12", String.valueOf(insideResult.getString("december")));
            num12 += Integer.parseInt(insideResult.getString("december"));
            jsonObjectTbP10.put("money1", String.valueOf(insideResult.getString("january")));
            num1 += Integer.parseInt(insideResult.getString("january"));
            jsonObjectTbP10.put("money2", String.valueOf(insideResult.getString("february")));
            num2 += Integer.parseInt(insideResult.getString("february"));
            jsonObjectTbP10.put("money3", String.valueOf(insideResult.getString("march")));
            num3 += Integer.parseInt(insideResult.getString("march"));
            jsonObjectTbP10.put("moneytotal", String.valueOf(insideResult.getString("moneytotal")));
        }
        if(resultPlan[5] != null){//构外
            JSONObject outsideResult = JSON.parseObject(resultPlan[5]);
            jsonObjectTbP9.put("money4", String.valueOf(outsideResult.getString("april")));
            //num4 += Integer.parseInt(outsideResult.getString("april"));
            jsonObjectTbP9.put("money5", String.valueOf(outsideResult.getString("may")));
            //num5 += Integer.parseInt(outsideResult.getString("may"));
            jsonObjectTbP9.put("money6", String.valueOf(outsideResult.getString("june")));
            //num6 += Integer.parseInt(outsideResult.getString("june"));
            jsonObjectTbP9.put("money7", String.valueOf(outsideResult.getString("july")));
            //num7 += Integer.parseInt(outsideResult.getString("july"));
            jsonObjectTbP9.put("money8", String.valueOf(outsideResult.getString("august")));
            //num8 += Integer.parseInt(outsideResult.getString("august"));
            jsonObjectTbP9.put("money9", String.valueOf(outsideResult.getString("september")));
            //num9 += Integer.parseInt(outsideResult.getString("september"));
            jsonObjectTbP9.put("money10", String.valueOf(outsideResult.getString("october")));
            //num10 += Integer.parseInt(outsideResult.getString("october"));
            jsonObjectTbP9.put("money11", String.valueOf(outsideResult.getString("november")));
            //num11 += Integer.parseInt(outsideResult.getString("november"));
            jsonObjectTbP9.put("money12", String.valueOf(outsideResult.getString("december")));
            //num12 += Integer.parseInt(outsideResult.getString("december"));
            jsonObjectTbP9.put("money1", String.valueOf(outsideResult.getString("january")));
            //num1 += Integer.parseInt(outsideResult.getString("january"));
            jsonObjectTbP9.put("money2", String.valueOf(outsideResult.getString("february")));
            //num2 += Integer.parseInt(outsideResult.getString("february"));
            jsonObjectTbP9.put("money3", String.valueOf(outsideResult.getString("march")));
            //num3 += Integer.parseInt(outsideResult.getString("march"));
            jsonObjectTbP9.put("moneytotal", String.valueOf(outsideResult.getString("moneytotal")));
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
        //各种经费——各种经费 TABLEO
        JSONArray tableOList = JSON.parseArray(busplanList.get(0).getTableO());
        List<BusinessTableOBase> businessTableOList = JSONArray.parseObject(tableOList.toJSONString(), new TypeReference<List<BusinessTableOBase>>() {});
        JSONObject jsonObjectTbFZ = new JSONObject();
        JSONObject jsonObjectTbDQ = new JSONObject();
        JSONObject jsonObjectTbRC = new JSONObject();
        for(BusinessTableOBase bobse : businessTableOList) {
            if (bobse.get("sprogramme") != null && bobse.get("sprogramme").equals("PJ148001")) {
                jsonObjectTbFZ = JSON.parseObject(String.valueOf(bobse));
                jsonObjectTbFZ.put("number4", num4 + numRc4);
                jsonObjectTbFZ.put("number5", num5 + numRc5);
                jsonObjectTbFZ.put("number6", num6 + numRc6);
                jsonObjectTbFZ.put("number7", num7 + numRc7);
                jsonObjectTbFZ.put("number8", num8 + numRc8);
                jsonObjectTbFZ.put("number9", num9 + numRc9);
                jsonObjectTbFZ.put("numberfirst", new BigDecimal(jsonObjectTbFZ.get("number4").toString())
                        .add(new BigDecimal(jsonObjectTbFZ.get("number5").toString())
                                .add(new BigDecimal(jsonObjectTbFZ.get("number6").toString())
                                        .add(new BigDecimal(jsonObjectTbFZ.get("number7").toString())
                                                .add(new BigDecimal(jsonObjectTbFZ.get("number8").toString())
                                                        .add(new BigDecimal(jsonObjectTbFZ.get("number9").toString())))))).toString());
                jsonObjectTbFZ.put("number10", num10 + numRc10);
                jsonObjectTbFZ.put("number11", num11 + numRc11);
                jsonObjectTbFZ.put("number12", num12 + numRc12);
                jsonObjectTbFZ.put("number1", num1 + numRc1);
                jsonObjectTbFZ.put("number2", num2 + numRc2);
                jsonObjectTbFZ.put("number3", num3 + numRc3);
                jsonObjectTbFZ.put("numbersecond", new BigDecimal(jsonObjectTbFZ.get("number10").toString())
                        .add(new BigDecimal(jsonObjectTbFZ.get("number11").toString())
                                .add(new BigDecimal(jsonObjectTbFZ.get("number12").toString())
                                        .add(new BigDecimal(jsonObjectTbFZ.get("number1").toString())
                                                .add(new BigDecimal(jsonObjectTbFZ.get("number2").toString())
                                                        .add(new BigDecimal(jsonObjectTbFZ.get("number3").toString())))))).toString());
                jsonObjectTbFZ.put("numbertotal", new BigDecimal(jsonObjectTbFZ.get("numberfirst").toString())
                        .add(new BigDecimal(jsonObjectTbFZ.get("numbersecond").toString())).toString());

                jsonObjectTbFZ.put("money4", new BigDecimal(String.valueOf(num4 + numRc4)).multiply(new BigDecimal(String.valueOf(jsonObjectTbFZ.get("price")))).divide(new BigDecimal("1000")).setScale(3, BigDecimal.ROUND_HALF_UP).toString());
                jsonObjectTbFZ.put("money5", new BigDecimal(String.valueOf(num5 + numRc5)).multiply(new BigDecimal(String.valueOf(jsonObjectTbFZ.get("price")))).divide(new BigDecimal("1000")).setScale(3, BigDecimal.ROUND_HALF_UP).toString());
                jsonObjectTbFZ.put("money6", new BigDecimal(String.valueOf(num6 + numRc6)).multiply(new BigDecimal(String.valueOf(jsonObjectTbFZ.get("price")))).divide(new BigDecimal("1000")).setScale(3, BigDecimal.ROUND_HALF_UP).toString());
                jsonObjectTbFZ.put("money7", new BigDecimal(String.valueOf(num7 + numRc7)).multiply(new BigDecimal(String.valueOf(jsonObjectTbFZ.get("price")))).divide(new BigDecimal("1000")).setScale(3, BigDecimal.ROUND_HALF_UP).toString());
                jsonObjectTbFZ.put("money8", new BigDecimal(String.valueOf(num8 + numRc8)).multiply(new BigDecimal(String.valueOf(jsonObjectTbFZ.get("price")))).divide(new BigDecimal("1000")).setScale(3, BigDecimal.ROUND_HALF_UP).toString());
                jsonObjectTbFZ.put("money9", new BigDecimal(String.valueOf(num9 + numRc9)).multiply(new BigDecimal(String.valueOf(jsonObjectTbFZ.get("price")))).divide(new BigDecimal("1000")).setScale(3, BigDecimal.ROUND_HALF_UP).toString());
                jsonObjectTbFZ.put("moneyfirst", new BigDecimal(jsonObjectTbFZ.get("money4").toString())
                        .add(new BigDecimal(jsonObjectTbFZ.get("money5").toString())
                                .add(new BigDecimal(jsonObjectTbFZ.get("money6").toString())
                                        .add(new BigDecimal(jsonObjectTbFZ.get("money7").toString())
                                                .add(new BigDecimal(jsonObjectTbFZ.get("money8").toString())
                                                        .add(new BigDecimal(jsonObjectTbFZ.get("money9").toString())))))).toString());
                jsonObjectTbFZ.put("money10", new BigDecimal(String.valueOf(num10 + numRc10)).multiply(new BigDecimal(String.valueOf(jsonObjectTbFZ.get("price")))).divide(new BigDecimal("1000")).setScale(3, BigDecimal.ROUND_HALF_UP).toString());
                jsonObjectTbFZ.put("money11", new BigDecimal(String.valueOf(num11 + numRc11)).multiply(new BigDecimal(String.valueOf(jsonObjectTbFZ.get("price")))).divide(new BigDecimal("1000")).setScale(3, BigDecimal.ROUND_HALF_UP).toString());
                jsonObjectTbFZ.put("money12", new BigDecimal(String.valueOf(num12 + numRc12)).multiply(new BigDecimal(String.valueOf(jsonObjectTbFZ.get("price")))).divide(new BigDecimal("1000")).setScale(3, BigDecimal.ROUND_HALF_UP).toString());
                jsonObjectTbFZ.put("money1", new BigDecimal(String.valueOf(num1 + numRc1)).multiply(new BigDecimal(String.valueOf(jsonObjectTbFZ.get("price")))).divide(new BigDecimal("1000")).setScale(3, BigDecimal.ROUND_HALF_UP).toString());
                jsonObjectTbFZ.put("money2", new BigDecimal(String.valueOf(num2 + numRc2)).multiply(new BigDecimal(String.valueOf(jsonObjectTbFZ.get("price")))).divide(new BigDecimal("1000")).setScale(3, BigDecimal.ROUND_HALF_UP).toString());
                jsonObjectTbFZ.put("money3", new BigDecimal(String.valueOf(num3 + numRc3)).multiply(new BigDecimal(String.valueOf(jsonObjectTbFZ.get("price")))).divide(new BigDecimal("1000")).setScale(3, BigDecimal.ROUND_HALF_UP).toString());
                jsonObjectTbFZ.put("moneysecond", new BigDecimal(jsonObjectTbFZ.get("money10").toString())
                        .add(new BigDecimal(jsonObjectTbFZ.get("money11").toString())
                                .add(new BigDecimal(jsonObjectTbFZ.get("money12").toString())
                                        .add(new BigDecimal(jsonObjectTbFZ.get("money1").toString())
                                                .add(new BigDecimal(jsonObjectTbFZ.get("money2").toString())
                                                        .add(new BigDecimal(jsonObjectTbFZ.get("money3").toString())))))).toString());
                jsonObjectTbFZ.put("moneytotal", new BigDecimal(jsonObjectTbFZ.get("moneyfirst").toString())
                        .add(new BigDecimal(jsonObjectTbFZ.get("moneysecond").toString())).toString());
            }
            else if (bobse.get("sprogramme") != null && bobse.get("sprogramme").equals("PJ148002")) {
                jsonObjectTbDQ = JSON.parseObject(String.valueOf(bobse));
                jsonObjectTbDQ.put("number4", num4 + numRc4);
                jsonObjectTbDQ.put("number5", num5 + numRc5);
                jsonObjectTbDQ.put("number6", num6 + numRc6);
                jsonObjectTbDQ.put("number7", num7 + numRc7);
                jsonObjectTbDQ.put("number8", num8 + numRc8);
                jsonObjectTbDQ.put("number9", num9 + numRc9);
                jsonObjectTbDQ.put("numberfirst", new BigDecimal(jsonObjectTbDQ.get("number4").toString())
                        .add(new BigDecimal(jsonObjectTbDQ.get("number5").toString())
                                .add(new BigDecimal(jsonObjectTbDQ.get("number6").toString())
                                        .add(new BigDecimal(jsonObjectTbDQ.get("number7").toString())
                                                .add(new BigDecimal(jsonObjectTbDQ.get("number8").toString())
                                                        .add(new BigDecimal(jsonObjectTbDQ.get("number9").toString())))))).toString());
                jsonObjectTbDQ.put("number10", num10 + numRc10);
                jsonObjectTbDQ.put("number11", num11 + numRc11);
                jsonObjectTbDQ.put("number12", num12 + numRc12);
                jsonObjectTbDQ.put("number1", num1 + numRc1);
                jsonObjectTbDQ.put("number2", num2 + numRc2);
                jsonObjectTbDQ.put("number3", num3 + numRc3);
                jsonObjectTbDQ.put("numbersecond", new BigDecimal(jsonObjectTbDQ.get("number10").toString())
                        .add(new BigDecimal(jsonObjectTbDQ.get("number11").toString())
                                .add(new BigDecimal(jsonObjectTbDQ.get("number12").toString())
                                        .add(new BigDecimal(jsonObjectTbDQ.get("number1").toString())
                                                .add(new BigDecimal(jsonObjectTbDQ.get("number2").toString())
                                                        .add(new BigDecimal(jsonObjectTbDQ.get("number3").toString())))))).toString());
                jsonObjectTbDQ.put("numbertotal", new BigDecimal(jsonObjectTbDQ.get("moneyfirst").toString())
                        .add(new BigDecimal(jsonObjectTbDQ.get("moneysecond").toString())).toString());
                jsonObjectTbDQ.put("money4", new BigDecimal(String.valueOf(num4 + numRc4)).multiply(new BigDecimal(String.valueOf(jsonObjectTbDQ.get("price")))).divide(new BigDecimal("1000")).setScale(3, BigDecimal.ROUND_HALF_UP).toString());
                jsonObjectTbDQ.put("money5", new BigDecimal(String.valueOf(num5 + numRc5)).multiply(new BigDecimal(String.valueOf(jsonObjectTbDQ.get("price")))).divide(new BigDecimal("1000")).setScale(3, BigDecimal.ROUND_HALF_UP).toString());
                jsonObjectTbDQ.put("money6", new BigDecimal(String.valueOf(num6 + numRc6)).multiply(new BigDecimal(String.valueOf(jsonObjectTbDQ.get("price")))).divide(new BigDecimal("1000")).setScale(3, BigDecimal.ROUND_HALF_UP).toString());
                jsonObjectTbDQ.put("money7", new BigDecimal(String.valueOf(num7 + numRc7)).multiply(new BigDecimal(String.valueOf(jsonObjectTbDQ.get("price")))).divide(new BigDecimal("1000")).setScale(3, BigDecimal.ROUND_HALF_UP).toString());
                jsonObjectTbDQ.put("money8", new BigDecimal(String.valueOf(num8 + numRc8)).multiply(new BigDecimal(String.valueOf(jsonObjectTbDQ.get("price")))).divide(new BigDecimal("1000")).setScale(3, BigDecimal.ROUND_HALF_UP).toString());
                jsonObjectTbDQ.put("money9", new BigDecimal(String.valueOf(num9 + numRc9)).multiply(new BigDecimal(String.valueOf(jsonObjectTbDQ.get("price")))).divide(new BigDecimal("1000")).setScale(3, BigDecimal.ROUND_HALF_UP).toString());
                jsonObjectTbDQ.put("moneyfirst", new BigDecimal(jsonObjectTbDQ.get("money4").toString())
                        .add(new BigDecimal(jsonObjectTbDQ.get("money5").toString())
                                .add(new BigDecimal(jsonObjectTbDQ.get("money6").toString())
                                        .add(new BigDecimal(jsonObjectTbDQ.get("money7").toString())
                                                .add(new BigDecimal(jsonObjectTbDQ.get("money8").toString())
                                                        .add(new BigDecimal(jsonObjectTbDQ.get("money9").toString())))))).toString());
                jsonObjectTbDQ.put("money10", new BigDecimal(String.valueOf(num10 + numRc10)).multiply(new BigDecimal(String.valueOf(jsonObjectTbDQ.get("price")))).divide(new BigDecimal("1000")).setScale(3, BigDecimal.ROUND_HALF_UP).toString());
                jsonObjectTbDQ.put("money11", new BigDecimal(String.valueOf(num11 + numRc11)).multiply(new BigDecimal(String.valueOf(jsonObjectTbDQ.get("price")))).divide(new BigDecimal("1000")).setScale(3, BigDecimal.ROUND_HALF_UP).toString());
                jsonObjectTbDQ.put("money12", new BigDecimal(String.valueOf(num12 + numRc12)).multiply(new BigDecimal(String.valueOf(jsonObjectTbDQ.get("price")))).divide(new BigDecimal("1000")).setScale(3, BigDecimal.ROUND_HALF_UP).toString());
                jsonObjectTbDQ.put("money1", new BigDecimal(String.valueOf(num1 + numRc1)).multiply(new BigDecimal(String.valueOf(jsonObjectTbDQ.get("price")))).divide(new BigDecimal("1000")).setScale(3, BigDecimal.ROUND_HALF_UP).toString());
                jsonObjectTbDQ.put("money2", new BigDecimal(String.valueOf(num2 + numRc2)).multiply(new BigDecimal(String.valueOf(jsonObjectTbDQ.get("price")))).divide(new BigDecimal("1000")).setScale(3, BigDecimal.ROUND_HALF_UP).toString());
                jsonObjectTbDQ.put("money3", new BigDecimal(String.valueOf(num3 + numRc3)).multiply(new BigDecimal(String.valueOf(jsonObjectTbDQ.get("price")))).divide(new BigDecimal("1000")).setScale(3, BigDecimal.ROUND_HALF_UP).toString());
                jsonObjectTbDQ.put("moneysecond", new BigDecimal(jsonObjectTbDQ.get("money10").toString())
                        .add(new BigDecimal(jsonObjectTbDQ.get("money11").toString())
                                .add(new BigDecimal(jsonObjectTbDQ.get("money12").toString())
                                        .add(new BigDecimal(jsonObjectTbDQ.get("money1").toString())
                                                .add(new BigDecimal(jsonObjectTbDQ.get("money2").toString())
                                                        .add(new BigDecimal(jsonObjectTbDQ.get("money3").toString())))))).toString());
                jsonObjectTbDQ.put("moneytotal", new BigDecimal(jsonObjectTbDQ.get("moneyfirst").toString())
                        .add(new BigDecimal(jsonObjectTbDQ.get("moneysecond").toString())).toString());
            }
            else if (bobse.get("sprogramme") != null && bobse.get("sprogramme").equals("PJ148003")) {
                jsonObjectTbRC = JSON.parseObject(String.valueOf(bobse));//本社
                jsonObjectTbRC.put("number4", numRc4);
                jsonObjectTbRC.put("number5", numRc5);
                jsonObjectTbRC.put("number6", numRc6);
                jsonObjectTbRC.put("number7", numRc7);
                jsonObjectTbRC.put("number8", numRc8);
                jsonObjectTbRC.put("number9", numRc9);
                jsonObjectTbRC.put("numberfirst", new BigDecimal(jsonObjectTbRC.get("number4").toString())
                        .add(new BigDecimal(jsonObjectTbRC.get("number5").toString())
                                .add(new BigDecimal(jsonObjectTbRC.get("number6").toString())
                                        .add(new BigDecimal(jsonObjectTbRC.get("number7").toString())
                                                .add(new BigDecimal(jsonObjectTbRC.get("number8").toString())
                                                        .add(new BigDecimal(jsonObjectTbRC.get("number9").toString())))))).toString());
                jsonObjectTbRC.put("number10", numRc10);
                jsonObjectTbRC.put("number11", numRc11);
                jsonObjectTbRC.put("number12", numRc12);
                jsonObjectTbRC.put("number1", numRc1);
                jsonObjectTbRC.put("number2", numRc2);
                jsonObjectTbRC.put("number3", numRc3);
                jsonObjectTbRC.put("numbersecond", new BigDecimal(jsonObjectTbRC.get("number10").toString())
                        .add(new BigDecimal(jsonObjectTbRC.get("number11").toString())
                                .add(new BigDecimal(jsonObjectTbRC.get("number12").toString())
                                        .add(new BigDecimal(jsonObjectTbRC.get("number1").toString())
                                                .add(new BigDecimal(jsonObjectTbRC.get("number2").toString())
                                                        .add(new BigDecimal(jsonObjectTbRC.get("number3").toString())))))).toString());
                jsonObjectTbRC.put("numbertotal", new BigDecimal(jsonObjectTbRC.get("moneyfirst").toString())
                        .add(new BigDecimal(jsonObjectTbRC.get("moneysecond").toString())).toString());
                jsonObjectTbRC.put("money4", new BigDecimal(String.valueOf(num4 + numRc4)).multiply(new BigDecimal(String.valueOf(jsonObjectTbRC.get("price")))).divide(new BigDecimal("1000")).setScale(3, BigDecimal.ROUND_HALF_UP).toString());
                jsonObjectTbRC.put("money5", new BigDecimal(String.valueOf(num5 + numRc5)).multiply(new BigDecimal(String.valueOf(jsonObjectTbRC.get("price")))).divide(new BigDecimal("1000")).setScale(3, BigDecimal.ROUND_HALF_UP).toString());
                jsonObjectTbRC.put("money6", new BigDecimal(String.valueOf(num6 + numRc6)).multiply(new BigDecimal(String.valueOf(jsonObjectTbRC.get("price")))).divide(new BigDecimal("1000")).setScale(3, BigDecimal.ROUND_HALF_UP).toString());
                jsonObjectTbRC.put("money7", new BigDecimal(String.valueOf(num7 + numRc7)).multiply(new BigDecimal(String.valueOf(jsonObjectTbRC.get("price")))).divide(new BigDecimal("1000")).setScale(3, BigDecimal.ROUND_HALF_UP).toString());
                jsonObjectTbRC.put("money8", new BigDecimal(String.valueOf(num8 + numRc8)).multiply(new BigDecimal(String.valueOf(jsonObjectTbRC.get("price")))).divide(new BigDecimal("1000")).setScale(3, BigDecimal.ROUND_HALF_UP).toString());
                jsonObjectTbRC.put("money9", new BigDecimal(String.valueOf(num9 + numRc9)).multiply(new BigDecimal(String.valueOf(jsonObjectTbRC.get("price")))).divide(new BigDecimal("1000")).setScale(3, BigDecimal.ROUND_HALF_UP).toString());
                jsonObjectTbRC.put("moneyfirst", new BigDecimal(jsonObjectTbRC.get("money4").toString())
                        .add(new BigDecimal(jsonObjectTbRC.get("money5").toString())
                                .add(new BigDecimal(jsonObjectTbRC.get("money6").toString())
                                        .add(new BigDecimal(jsonObjectTbRC.get("money7").toString())
                                                .add(new BigDecimal(jsonObjectTbRC.get("money8").toString())
                                                        .add(new BigDecimal(jsonObjectTbRC.get("money9").toString())))))).toString());
                jsonObjectTbRC.put("money10", new BigDecimal(String.valueOf(num10 + numRc10)).multiply(new BigDecimal(String.valueOf(jsonObjectTbRC.get("price")))).divide(new BigDecimal("1000")).setScale(3, BigDecimal.ROUND_HALF_UP).toString());
                jsonObjectTbRC.put("money11", new BigDecimal(String.valueOf(num11 + numRc11)).multiply(new BigDecimal(String.valueOf(jsonObjectTbRC.get("price")))).divide(new BigDecimal("1000")).setScale(3, BigDecimal.ROUND_HALF_UP).toString());
                jsonObjectTbRC.put("money12", new BigDecimal(String.valueOf(num12 + numRc12)).multiply(new BigDecimal(String.valueOf(jsonObjectTbRC.get("price")))).divide(new BigDecimal("1000")).setScale(3, BigDecimal.ROUND_HALF_UP).toString());
                jsonObjectTbRC.put("money1", new BigDecimal(String.valueOf(num1 + numRc1)).multiply(new BigDecimal(String.valueOf(jsonObjectTbRC.get("price")))).divide(new BigDecimal("1000")).setScale(3, BigDecimal.ROUND_HALF_UP).toString());
                jsonObjectTbRC.put("money2", new BigDecimal(String.valueOf(num2 + numRc2)).multiply(new BigDecimal(String.valueOf(jsonObjectTbRC.get("price")))).divide(new BigDecimal("1000")).setScale(3, BigDecimal.ROUND_HALF_UP).toString());
                jsonObjectTbRC.put("money3", new BigDecimal(String.valueOf(num3 + numRc3)).multiply(new BigDecimal(String.valueOf(jsonObjectTbRC.get("price")))).divide(new BigDecimal("1000")).setScale(3, BigDecimal.ROUND_HALF_UP).toString());
                jsonObjectTbRC.put("moneysecond", new BigDecimal(jsonObjectTbRC.get("money10").toString())
                        .add(new BigDecimal(jsonObjectTbRC.get("money11").toString())
                                .add(new BigDecimal(jsonObjectTbRC.get("money12").toString())
                                        .add(new BigDecimal(jsonObjectTbRC.get("money1").toString())
                                                .add(new BigDecimal(jsonObjectTbRC.get("money2").toString())
                                                        .add(new BigDecimal(jsonObjectTbRC.get("money3").toString())))))).toString());
                jsonObjectTbRC.put("moneytotal", new BigDecimal(jsonObjectTbRC.get("moneyfirst").toString())
                        .add(new BigDecimal(jsonObjectTbRC.get("moneysecond").toString())).toString());
            }
        }


        for (int i = 0; i < businessTableOList.size(); i++) {
            if(businessTableOList.get(i).get("sprogramme") != null && businessTableOList.get(i).get("sprogramme").equals("PJ148001")) {
                resultTableO.add(jsonObjectTbFZ);
            } else if(businessTableOList.get(i).get("sprogramme") != null && businessTableOList.get(i).get("sprogramme").equals("PJ148002")) {
                resultTableO.add(jsonObjectTbDQ);
            } else if(businessTableOList.get(i).get("sprogramme") != null && businessTableOList.get(i).get("sprogramme").equals("PJ148003")) {
                resultTableO.add(jsonObjectTbRC);
            } else{
                resultTableO.add(JSONObject.parseObject(String.valueOf(businessTableOList.get(i))));
            }
        }
        busplanList.get(0).setTableP(String.valueOf(resultTableP));
        busplanList.get(0).setTableO(String.valueOf(resultTableO));
        businessplanMapper.updateByPrimaryKey(busplanList.get(0));
    }

    @Override
    public void update(PersonnelPlan personnelPlan, TokenModel tokenModel) {
        personnelPlan.preUpdate(tokenModel);
        personnelplanMapper.updateByPrimaryKeySelective(personnelPlan);
        //事业计划状态为0或3时修改人员计划，事业计划数据变更 ztc fr
        String[] resultPlan = {};
        Businessplan businessplan = new Businessplan();
        businessplan.setYear(personnelPlan.getYears());
        businessplan.setCenter_id(personnelPlan.getCenterid());
        List<Businessplan> busplanList = businessplanMapper.select(businessplan);
        if(busplanList.size() != 0 && (busplanList.get(0).getStatus().equals("0") || busplanList.get(0).getStatus().equals("3"))) {
            try {
                resultPlan = businessplanService.getPersonPlan(personnelPlan.getYears(),personnelPlan.getCenterid());
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.updateBusinessInfo(busplanList,resultPlan);
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
        //region scc 10/22 创建theme报异常,原因moneyavg为空，type为integer不能用eqaul from
        if(personnelPlan.getType() == 0){
            //endregion scc 10/22 创建theme报异常 to
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
        }else{
            personnelplanMapper.insert(personnelPlan);
        }
        String[] resultPlan = {};
        Businessplan businessplan = new Businessplan();
        businessplan.setYear(personnelPlan.getYears());
        businessplan.setCenter_id(personnelPlan.getCenterid());
        List<Businessplan> busplanList = businessplanMapper.select(businessplan);
        if(busplanList.size() != 0 && (busplanList.get(0).getStatus().equals("0") || busplanList.get(0).getStatus().equals("3"))) {
            try {
                resultPlan = businessplanService.getPersonPlan(personnelPlan.getYears(),personnelPlan.getCenterid());
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.updateBusinessInfo(busplanList,resultPlan);
        }
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
                List<Dictionary> dtemp = new ArrayList<>();
                dtemp = dictionaryRank.stream().filter(i->i.getCode().equals(customerInfo.getUserinfo().getRank())).collect(Collectors.toList());
                if(dtemp.size()>0)
                {
                    String dictionaryValue1 = dtemp.get(0).getValue1();
                    p.setAptoju(peoplewareFeeList.stream().filter(item->item.getRanks().equals(dictionaryValue1)).collect(Collectors.toList()).get(0).getAgeprice());
                }
                else
                {
                    String dictionarynull = "日本出向者";
                    p.setAptoju(peoplewareFeeList.stream().filter(item->item.getRanks().equals(dictionarynull)).collect(Collectors.toList()).get(0).getAgeprice());
                }
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
