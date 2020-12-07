package com.nt.service_pfans.PFANS1000.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_Org.OrgTree;
import com.nt.dao_Pfans.PFANS1000.*;
//import com.nt.dao_Pfans.PFANS1000.Businessplandet;
import com.nt.dao_Pfans.PFANS1000.Vo.*;
import com.nt.service_Org.DictionaryService;
import com.nt.service_Org.OrgTreeService;
import com.nt.service_Org.mapper.DictionaryMapper;
import com.nt.service_pfans.PFANS1000.BusinessplanService;
import com.nt.service_pfans.PFANS1000.mapper.*;
//import com.nt.service_pfans.PFANS1000.mapper.BusinessplandetMapper;
import com.nt.utils.StringUtils;
import com.nt.utils.dao.TokenModel;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
@EnableScheduling
public class BusinessplanServiceImpl implements BusinessplanService {
    @Autowired
    private OrgTreeService orgTreeService;
    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private ThemePlanDetailMapper themePlanDetailMapper;
    @Autowired
    private BusinessplanMapper businessplanMapper;
    @Autowired
    private TotalplanMapper totalplanMapper;
    @Autowired
    private PieceworktotalMapper pieceworktotalMapper;
    @Autowired
    private PersonnelplanMapper personnelplanMapper;
    @Autowired
    private MongoTemplate mongoTemplate;

    DecimalFormat df = new DecimalFormat("#0.00");
    //@Autowired
    //private BusinessplandetMapper businessplandetMapper;

    @Override
    public List<Businessplan> get(Businessplan businessplan) throws Exception {
        return businessplanMapper.select(businessplan);
    }

    @Override
    public List<OrgTreeVo> getgroupcompanyen(String year) throws Exception {
        List<OrgTreeVo> OrgTreeVolist = new ArrayList<>();
        OrgTree orgs = orgTreeService.get(new OrgTree());
        for (OrgTree org : orgs.getOrgs()) {
            for (OrgTree org1 : org.getOrgs()) {
                OrgTreeVo orgtreevo = new OrgTreeVo();
                orgtreevo.set_id(org1.get_id());
                orgtreevo.setCompanyen(org1.getCompanyen());
                orgtreevo.setRedirict(org1.getRedirict());
                OrgTreeVolist.add(orgtreevo);
            }
        }
        Businessplan businessplan = new Businessplan();
        businessplan.setYear(year);
        businessplan.setStatus("4");
        businessplan.setGroup_id("91B253A1C605E9CA814462FB4C4D2605F43F");
        List<Businessplan> businessplanlist = businessplanMapper.select(businessplan);
        if (businessplanlist.size() > 0) {
            OrgTreeVolist.get(0).setType("1");
            OrgTreeVolist.get(0).setAssets_lodyear(businessplanlist.get(0).getAssets_lodyear());
            OrgTreeVolist.get(0).setEquipment_lodyear(businessplanlist.get(0).getEquipment_lodyear());
        } else {
            OrgTreeVolist.get(0).setType("0");

        }
        return OrgTreeVolist;
    }

    @Override
    public List<BusinessGroupA2Vo> getgroup(String year, String type) throws Exception {
        int scale = 2;//设置位数
        int roundingMode = 4;//表示四舍五入，可以选择其他舍值方式，例如去尾，等等.
        List<BusinessGroupA2Vo> VoList = new ArrayList<>();
        List<BusinessGroupA2Vo> getgroup = new ArrayList<>();
        if (type.equals("1")) {
            getgroup = businessplanMapper.getgroupA2(year);
        } else if (type.equals("2")) {
            getgroup = businessplanMapper.getgroupB1(year);
        } else if (type.equals("3")) {
            getgroup = businessplanMapper.getgroupB2(year);
        } else if (type.equals("4")) {
            getgroup = businessplanMapper.getgroupB3(year);
        }
        Map<String, String> map = new HashMap<>();
        OrgTree orgs = orgTreeService.get(new OrgTree());
        for (OrgTree org : orgs.getOrgs()) {
            for (OrgTree org1 : org.getOrgs()) {
                map.put(org1.get_id(), org1.getCompanyen());
            }
        }
        Set<String> conditionKeys = map.keySet();
        for (BusinessGroupA2Vo vo2 : getgroup) {
            conditionKeys = conditionKeys.parallelStream().filter(i -> !i.equals(vo2.getGroupname())).collect(Collectors.toSet());
        }

        for (BusinessGroupA2Vo vo2 : getgroup) {
            PersonnelPlan personnelplan = new PersonnelPlan();
            personnelplan.setGroupid(vo2.getGroupname());
            personnelplan.setYears(year);
            List<PersonnelPlan> personnelplanlist = personnelplanMapper.select(personnelplan);
            if (personnelplanlist.size() > 0) {
                vo2.setCommission(personnelplanlist.get(0).getMoneyavg());
            } else {
                vo2.setCommission("0");
            }
            BigDecimal money1 = new BigDecimal(vo2.getMoney1());
            BigDecimal money2 = new BigDecimal(vo2.getMoney2());
            BigDecimal money3 = new BigDecimal(vo2.getMoney3());
            BigDecimal money4 = new BigDecimal(vo2.getMoney4());
            BigDecimal money5 = new BigDecimal(vo2.getMoney5());
            BigDecimal money6 = new BigDecimal(vo2.getMoney6());
            BigDecimal money7 = new BigDecimal(vo2.getMoney7());
            BigDecimal money8 = new BigDecimal(vo2.getMoney8());
            BigDecimal money9 = new BigDecimal(vo2.getMoney9());
            BigDecimal money10 = new BigDecimal(vo2.getMoney10());
            BigDecimal money11 = new BigDecimal(vo2.getMoney11());
            BigDecimal money12 = new BigDecimal(vo2.getMoney12());
            BigDecimal moneyfirst = new BigDecimal(vo2.getMoneyfirst());
            BigDecimal moneysecond = new BigDecimal(vo2.getMoneysecond());
            BigDecimal moneytotal = new BigDecimal(vo2.getMoneytotal());
            BigDecimal moneys1 = money1.divide(new BigDecimal("1000"));
            BigDecimal moneys2 = money2.divide(new BigDecimal("1000"));
            BigDecimal moneys3 = money3.divide(new BigDecimal("1000"));
            BigDecimal moneys4 = money4.divide(new BigDecimal("1000"));
            BigDecimal moneys5 = money5.divide(new BigDecimal("1000"));
            BigDecimal moneys6 = money6.divide(new BigDecimal("1000"));
            BigDecimal moneys7 = money7.divide(new BigDecimal("1000"));
            BigDecimal moneys8 = money8.divide(new BigDecimal("1000"));
            BigDecimal moneys9 = money9.divide(new BigDecimal("1000"));
            BigDecimal moneys10 = money10.divide(new BigDecimal("1000"));
            BigDecimal moneys11 = money11.divide(new BigDecimal("1000"));
            BigDecimal moneys12 = money12.divide(new BigDecimal("1000"));
            BigDecimal moneyfirsts = moneyfirst.divide(new BigDecimal("1000"));
            BigDecimal moneyseconds = moneysecond.divide(new BigDecimal("1000"));
            BigDecimal moneytotals = moneytotal.divide(new BigDecimal("1000"));
            vo2.setMoney1(String.valueOf(moneys1));
            vo2.setMoney2(String.valueOf(moneys2));
            vo2.setMoney3(String.valueOf(moneys3));
            vo2.setMoney4(String.valueOf(moneys4));
            vo2.setMoney5(String.valueOf(moneys5));
            vo2.setMoney6(String.valueOf(moneys6));
            vo2.setMoney7(String.valueOf(moneys7));
            vo2.setMoney8(String.valueOf(moneys8));
            vo2.setMoney9(String.valueOf(moneys9));
            vo2.setMoney10(String.valueOf(moneys10));
            vo2.setMoney11(String.valueOf(moneys11));
            vo2.setMoney12(String.valueOf(moneys12));
            vo2.setMoneyfirst(String.valueOf(moneyfirsts));
            vo2.setMoneysecond(String.valueOf(moneyseconds));
            vo2.setMoneytotal(String.valueOf(moneytotals));
            VoList.add(vo2);
        }
        if (conditionKeys.size() > 0) {
            for (String str : conditionKeys) {
                PersonnelPlan personnelplan = new PersonnelPlan();
                BusinessGroupA2Vo businessgroupa2 = new BusinessGroupA2Vo();
                personnelplan.setGroupid(str);
                personnelplan.setYears(year);
                List<PersonnelPlan> personnelplanlist = personnelplanMapper.select(personnelplan);
                if (personnelplanlist.size() > 0) {
                    businessgroupa2.setCommission(personnelplanlist.get(0).getMoneyavg());
                } else {
                    businessgroupa2.setCommission("0");
                }
                businessgroupa2.setMoney1("0");
                businessgroupa2.setMoney2("0");
                businessgroupa2.setMoney3("0");
                businessgroupa2.setMoney4("0");
                businessgroupa2.setMoney5("0");
                businessgroupa2.setMoney6("0");
                businessgroupa2.setMoney7("0");
                businessgroupa2.setMoney8("0");
                businessgroupa2.setMoney9("0");
                businessgroupa2.setMoney10("0");
                businessgroupa2.setMoney11("0");
                businessgroupa2.setMoney12("0");
                businessgroupa2.setMoneyfirst("0");
                businessgroupa2.setMoneysecond("0");
                businessgroupa2.setMoneytotal("0");
                businessgroupa2.setNumber1("0");
                businessgroupa2.setNumber2("0");
                businessgroupa2.setNumber3("0");
                businessgroupa2.setNumber4("0");
                businessgroupa2.setNumber5("0");
                businessgroupa2.setNumber6("0");
                businessgroupa2.setNumber7("0");
                businessgroupa2.setNumber8("0");
                businessgroupa2.setNumber9("0");
                businessgroupa2.setNumber10("0");
                businessgroupa2.setNumber11("0");
                businessgroupa2.setNumber12("0");
                businessgroupa2.setNumberfirst("0");
                businessgroupa2.setNumbersecond("0");
                businessgroupa2.setNumbertotal("0");
                businessgroupa2.setGroupname(str);
                VoList.add(businessgroupa2);
            }
        }
        for (Map.Entry<String, String> str : map.entrySet())
            for (BusinessGroupA2Vo volist : VoList) {
                {
                    if (volist.getGroupname().equals(str.getKey())) {
                        volist.setGroupname(str.getValue());
                    }
                }
            }
        return VoList;
    }

    @Override
    public List<BusinessGroupA1Vo> getgroupA1(String year, String groupid) throws Exception {
        int scale = 2;//设置位数
        int roundingMode = 4;//表示四舍五入，可以选择其他舍值方式，例如去尾，等等.
        List<BusinessGroupA1Vo> VoList = new ArrayList<>();
        ThemePlanDetail themeplandetail = new ThemePlanDetail();
        themeplandetail.setYear(year);
        themeplandetail.setGroup_id(groupid);
        themeplandetail.setType("0");
        themeplandetail.setContracttype("PJ142001");
        List<ThemePlanDetail> themeplandetaillist = themePlanDetailMapper.select(themeplandetail);
        if (themeplandetaillist.size() > 0) {
            List<BusinessGroupA1Vo> getone = businessplanMapper.getoneGroupA1(groupid, year);
            if (getone.size() > 0) {
                BusinessGroupA1Vo businessgroupa1vo1 = new BusinessGroupA1Vo();
                String name1 = "PJ142";
                List<Dictionary> curListA = dictionaryService.getForSelect(name1);
                for (Dictionary iteA : curListA) {
                    if (iteA.getCode().equals("PJ142001")) {
                        businessgroupa1vo1.setName1(iteA.getValue1());
                        businessgroupa1vo1.setName2(iteA.getValue2());
                    }
                }
                BigDecimal money1 = new BigDecimal(getone.get(0).getMoney1());
                BigDecimal money2 = new BigDecimal(getone.get(0).getMoney2());
                BigDecimal money3 = new BigDecimal(getone.get(0).getMoney3());
                BigDecimal money4 = new BigDecimal(getone.get(0).getMoney4());
                BigDecimal money5 = new BigDecimal(getone.get(0).getMoney5());
                BigDecimal money6 = new BigDecimal(getone.get(0).getMoney6());
                BigDecimal money7 = new BigDecimal(getone.get(0).getMoney7());
                BigDecimal money8 = new BigDecimal(getone.get(0).getMoney8());
                BigDecimal money9 = new BigDecimal(getone.get(0).getMoney9());
                BigDecimal money10 = new BigDecimal(getone.get(0).getMoney10());
                BigDecimal money11 = new BigDecimal(getone.get(0).getMoney11());
                BigDecimal money12 = new BigDecimal(getone.get(0).getMoney12());
                BigDecimal moneyfirst = new BigDecimal(getone.get(0).getMoneyfirst());
                BigDecimal moneysecond = new BigDecimal(getone.get(0).getMoneysecond());
                BigDecimal moneytotal = new BigDecimal(getone.get(0).getMoneytotal());
                if (businessgroupa1vo1.getName2().equals("0")) {
                    BigDecimal moneys1 = money1.divide(new BigDecimal("1000"));
                    BigDecimal moneys2 = money2.divide(new BigDecimal("1000"));
                    BigDecimal moneys3 = money3.divide(new BigDecimal("1000"));
                    BigDecimal moneys4 = money4.divide(new BigDecimal("1000"));
                    BigDecimal moneys5 = money5.divide(new BigDecimal("1000"));
                    BigDecimal moneys6 = money6.divide(new BigDecimal("1000"));
                    BigDecimal moneys7 = money7.divide(new BigDecimal("1000"));
                    BigDecimal moneys8 = money8.divide(new BigDecimal("1000"));
                    BigDecimal moneys9 = money9.divide(new BigDecimal("1000"));
                    BigDecimal moneys10 = money10.divide(new BigDecimal("1000"));
                    BigDecimal moneys11 = money11.divide(new BigDecimal("1000"));
                    BigDecimal moneys12 = money12.divide(new BigDecimal("1000"));
                    BigDecimal moneyfirsts = moneyfirst.divide(new BigDecimal("1000"));
                    BigDecimal moneyseconds = moneysecond.divide(new BigDecimal("1000"));
                    BigDecimal moneytotals = moneytotal.divide(new BigDecimal("1000"));
                    businessgroupa1vo1.setMoney1(String.valueOf(moneys1));
                    businessgroupa1vo1.setMoney2(String.valueOf(moneys2));
                    businessgroupa1vo1.setMoney3(String.valueOf(moneys3));
                    businessgroupa1vo1.setMoney4(String.valueOf(moneys4));
                    businessgroupa1vo1.setMoney5(String.valueOf(moneys5));
                    businessgroupa1vo1.setMoney6(String.valueOf(moneys6));
                    businessgroupa1vo1.setMoney7(String.valueOf(moneys7));
                    businessgroupa1vo1.setMoney8(String.valueOf(moneys8));
                    businessgroupa1vo1.setMoney9(String.valueOf(moneys9));
                    businessgroupa1vo1.setMoney10(String.valueOf(moneys10));
                    businessgroupa1vo1.setMoney11(String.valueOf(moneys11));
                    businessgroupa1vo1.setMoney12(String.valueOf(moneys12));
                    businessgroupa1vo1.setMoneyfirst(String.valueOf(moneyfirsts));
                    businessgroupa1vo1.setMoneysecond(String.valueOf(moneyseconds));
                    businessgroupa1vo1.setMoneytotal(String.valueOf(moneytotals));
                } else {
                    BigDecimal bd = new BigDecimal(businessgroupa1vo1.getName2());
                    BigDecimal bd1 = new BigDecimal("1");
                    BigDecimal bd2 = bd1.add(bd);
                    BigDecimal moneys1 = money1.divide(bd2, scale, roundingMode).multiply(bd).divide(new BigDecimal("1000"));
                    BigDecimal moneys2 = money2.divide(bd2, scale, roundingMode).multiply(bd).divide(new BigDecimal("1000"));
                    BigDecimal moneys3 = money3.divide(bd2, scale, roundingMode).multiply(bd).divide(new BigDecimal("1000"));
                    BigDecimal moneys4 = money4.divide(bd2, scale, roundingMode).multiply(bd).divide(new BigDecimal("1000"));
                    BigDecimal moneys5 = money5.divide(bd2, scale, roundingMode).multiply(bd).divide(new BigDecimal("1000"));
                    BigDecimal moneys6 = money6.divide(bd2, scale, roundingMode).multiply(bd).divide(new BigDecimal("1000"));
                    BigDecimal moneys7 = money7.divide(bd2, scale, roundingMode).multiply(bd).divide(new BigDecimal("1000"));
                    BigDecimal moneys8 = money8.divide(bd2, scale, roundingMode).multiply(bd).divide(new BigDecimal("1000"));
                    BigDecimal moneys9 = money9.divide(bd2, scale, roundingMode).multiply(bd).divide(new BigDecimal("1000"));
                    BigDecimal moneys10 = money10.divide(bd2, scale, roundingMode).multiply(bd).divide(new BigDecimal("1000"));
                    BigDecimal moneys11 = money11.divide(bd2, scale, roundingMode).multiply(bd).divide(new BigDecimal("1000"));
                    BigDecimal moneys12 = money12.divide(bd2, scale, roundingMode).multiply(bd).divide(new BigDecimal("1000"));
                    BigDecimal moneyfirsts = moneyfirst.divide(bd2, scale, roundingMode).multiply(bd).divide(new BigDecimal("1000"));
                    BigDecimal moneyseconds = moneysecond.divide(bd2, scale, roundingMode).multiply(bd).divide(new BigDecimal("1000"));
                    BigDecimal moneytotals = moneytotal.divide(bd2, scale, roundingMode).multiply(bd).divide(new BigDecimal("1000"));
                    businessgroupa1vo1.setMoney1(String.valueOf(moneys1));
                    businessgroupa1vo1.setMoney2(String.valueOf(moneys2));
                    businessgroupa1vo1.setMoney3(String.valueOf(moneys3));
                    businessgroupa1vo1.setMoney4(String.valueOf(moneys4));
                    businessgroupa1vo1.setMoney5(String.valueOf(moneys5));
                    businessgroupa1vo1.setMoney6(String.valueOf(moneys6));
                    businessgroupa1vo1.setMoney7(String.valueOf(moneys7));
                    businessgroupa1vo1.setMoney8(String.valueOf(moneys8));
                    businessgroupa1vo1.setMoney9(String.valueOf(moneys9));
                    businessgroupa1vo1.setMoney10(String.valueOf(moneys10));
                    businessgroupa1vo1.setMoney11(String.valueOf(moneys11));
                    businessgroupa1vo1.setMoney12(String.valueOf(moneys12));
                    businessgroupa1vo1.setMoneyfirst(String.valueOf(moneyfirsts));
                    businessgroupa1vo1.setMoneysecond(String.valueOf(moneyseconds));
                    businessgroupa1vo1.setMoneytotal(String.valueOf(moneytotals));
                }
                VoList.add(businessgroupa1vo1);
            }
        } else {
            BusinessGroupA1Vo businessgroupa1vo1 = new BusinessGroupA1Vo();
            businessgroupa1vo1.setMoney1("0");
            businessgroupa1vo1.setMoney2("0");
            businessgroupa1vo1.setMoney3("0");
            businessgroupa1vo1.setMoney4("0");
            businessgroupa1vo1.setMoney5("0");
            businessgroupa1vo1.setMoney6("0");
            businessgroupa1vo1.setMoney7("0");
            businessgroupa1vo1.setMoney8("0");
            businessgroupa1vo1.setMoney9("0");
            businessgroupa1vo1.setMoney10("0");
            businessgroupa1vo1.setMoney11("0");
            businessgroupa1vo1.setMoney12("0");
            businessgroupa1vo1.setMoneyfirst("0");
            businessgroupa1vo1.setMoneysecond("0");
            businessgroupa1vo1.setMoneytotal("0");
            String name1 = "PJ142";
            List<Dictionary> curListA = dictionaryService.getForSelect(name1);
            for (Dictionary iteA : curListA) {
                if (iteA.getCode().equals("PJ142001")) {
                    businessgroupa1vo1.setName1(iteA.getValue1());
                    businessgroupa1vo1.setName2(iteA.getValue2());
                }
            }
            VoList.add(businessgroupa1vo1);
        }

        themeplandetail.setContracttype("PJ142002");
        List<ThemePlanDetail> themeplandetaillist2 = themePlanDetailMapper.select(themeplandetail);
        if (themeplandetaillist2.size() > 0) {
            List<BusinessGroupA1Vo> gettwo = businessplanMapper.gettwoGroupA1(groupid, year);
            if (gettwo.size() > 0) {
                BusinessGroupA1Vo businessgroupa1vo1 = new BusinessGroupA1Vo();
                String name1 = "PJ142";
                List<Dictionary> curListA = dictionaryService.getForSelect(name1);
                for (Dictionary iteA : curListA) {
                    if (iteA.getCode().equals("PJ142002")) {
                        businessgroupa1vo1.setName1(iteA.getValue1());
                        businessgroupa1vo1.setName2(iteA.getValue2());
                    }
                }
                BigDecimal money1 = new BigDecimal(gettwo.get(0).getMoney1());
                BigDecimal money2 = new BigDecimal(gettwo.get(0).getMoney2());
                BigDecimal money3 = new BigDecimal(gettwo.get(0).getMoney3());
                BigDecimal money4 = new BigDecimal(gettwo.get(0).getMoney4());
                BigDecimal money5 = new BigDecimal(gettwo.get(0).getMoney5());
                BigDecimal money6 = new BigDecimal(gettwo.get(0).getMoney6());
                BigDecimal money7 = new BigDecimal(gettwo.get(0).getMoney7());
                BigDecimal money8 = new BigDecimal(gettwo.get(0).getMoney8());
                BigDecimal money9 = new BigDecimal(gettwo.get(0).getMoney9());
                BigDecimal money10 = new BigDecimal(gettwo.get(0).getMoney10());
                BigDecimal money11 = new BigDecimal(gettwo.get(0).getMoney11());
                BigDecimal money12 = new BigDecimal(gettwo.get(0).getMoney12());
                BigDecimal moneyfirst = new BigDecimal(gettwo.get(0).getMoneyfirst());
                BigDecimal moneysecond = new BigDecimal(gettwo.get(0).getMoneysecond());
                BigDecimal moneytotal = new BigDecimal(gettwo.get(0).getMoneytotal());
                if (businessgroupa1vo1.getName2().equals("0")) {
                    BigDecimal moneys1 = money1.divide(new BigDecimal("1000"));
                    BigDecimal moneys2 = money2.divide(new BigDecimal("1000"));
                    BigDecimal moneys3 = money3.divide(new BigDecimal("1000"));
                    BigDecimal moneys4 = money4.divide(new BigDecimal("1000"));
                    BigDecimal moneys5 = money5.divide(new BigDecimal("1000"));
                    BigDecimal moneys6 = money6.divide(new BigDecimal("1000"));
                    BigDecimal moneys7 = money7.divide(new BigDecimal("1000"));
                    BigDecimal moneys8 = money8.divide(new BigDecimal("1000"));
                    BigDecimal moneys9 = money9.divide(new BigDecimal("1000"));
                    BigDecimal moneys10 = money10.divide(new BigDecimal("1000"));
                    BigDecimal moneys11 = money11.divide(new BigDecimal("1000"));
                    BigDecimal moneys12 = money12.divide(new BigDecimal("1000"));
                    BigDecimal moneyfirsts = moneyfirst.divide(new BigDecimal("1000"));
                    BigDecimal moneyseconds = moneysecond.divide(new BigDecimal("1000"));
                    BigDecimal moneytotals = moneytotal.divide(new BigDecimal("1000"));
                    businessgroupa1vo1.setMoney1(String.valueOf(moneys1));
                    businessgroupa1vo1.setMoney2(String.valueOf(moneys2));
                    businessgroupa1vo1.setMoney3(String.valueOf(moneys3));
                    businessgroupa1vo1.setMoney4(String.valueOf(moneys4));
                    businessgroupa1vo1.setMoney5(String.valueOf(moneys5));
                    businessgroupa1vo1.setMoney6(String.valueOf(moneys6));
                    businessgroupa1vo1.setMoney7(String.valueOf(moneys7));
                    businessgroupa1vo1.setMoney8(String.valueOf(moneys8));
                    businessgroupa1vo1.setMoney9(String.valueOf(moneys9));
                    businessgroupa1vo1.setMoney10(String.valueOf(moneys10));
                    businessgroupa1vo1.setMoney11(String.valueOf(moneys11));
                    businessgroupa1vo1.setMoney12(String.valueOf(moneys12));
                    businessgroupa1vo1.setMoneyfirst(String.valueOf(moneyfirsts));
                    businessgroupa1vo1.setMoneysecond(String.valueOf(moneyseconds));
                    businessgroupa1vo1.setMoneytotal(String.valueOf(moneytotals));
                } else {
                    BigDecimal bd = new BigDecimal(businessgroupa1vo1.getName2());
                    BigDecimal bd1 = new BigDecimal("1");
                    BigDecimal bd2 = bd1.add(bd);
                    BigDecimal moneys1 = money1.divide(bd2, scale, roundingMode).multiply(bd).divide(new BigDecimal("1000"));
                    BigDecimal moneys2 = money2.divide(bd2, scale, roundingMode).multiply(bd).divide(new BigDecimal("1000"));
                    BigDecimal moneys3 = money3.divide(bd2, scale, roundingMode).multiply(bd).divide(new BigDecimal("1000"));
                    BigDecimal moneys4 = money4.divide(bd2, scale, roundingMode).multiply(bd).divide(new BigDecimal("1000"));
                    BigDecimal moneys5 = money5.divide(bd2, scale, roundingMode).multiply(bd).divide(new BigDecimal("1000"));
                    BigDecimal moneys6 = money6.divide(bd2, scale, roundingMode).multiply(bd).divide(new BigDecimal("1000"));
                    BigDecimal moneys7 = money7.divide(bd2, scale, roundingMode).multiply(bd).divide(new BigDecimal("1000"));
                    BigDecimal moneys8 = money8.divide(bd2, scale, roundingMode).multiply(bd).divide(new BigDecimal("1000"));
                    BigDecimal moneys9 = money9.divide(bd2, scale, roundingMode).multiply(bd).divide(new BigDecimal("1000"));
                    BigDecimal moneys10 = money10.divide(bd2, scale, roundingMode).multiply(bd).divide(new BigDecimal("1000"));
                    BigDecimal moneys11 = money11.divide(bd2, scale, roundingMode).multiply(bd).divide(new BigDecimal("1000"));
                    BigDecimal moneys12 = money12.divide(bd2, scale, roundingMode).multiply(bd).divide(new BigDecimal("1000"));
                    BigDecimal moneyfirsts = moneyfirst.divide(bd2, scale, roundingMode).multiply(bd).divide(new BigDecimal("1000"));
                    BigDecimal moneyseconds = moneysecond.divide(bd2, scale, roundingMode).multiply(bd).divide(new BigDecimal("1000"));
                    BigDecimal moneytotals = moneytotal.divide(bd2, scale, roundingMode).multiply(bd).divide(new BigDecimal("1000"));
                    businessgroupa1vo1.setMoney1(String.valueOf(moneys1));
                    businessgroupa1vo1.setMoney2(String.valueOf(moneys2));
                    businessgroupa1vo1.setMoney3(String.valueOf(moneys3));
                    businessgroupa1vo1.setMoney4(String.valueOf(moneys4));
                    businessgroupa1vo1.setMoney5(String.valueOf(moneys5));
                    businessgroupa1vo1.setMoney6(String.valueOf(moneys6));
                    businessgroupa1vo1.setMoney7(String.valueOf(moneys7));
                    businessgroupa1vo1.setMoney8(String.valueOf(moneys8));
                    businessgroupa1vo1.setMoney9(String.valueOf(moneys9));
                    businessgroupa1vo1.setMoney10(String.valueOf(moneys10));
                    businessgroupa1vo1.setMoney11(String.valueOf(moneys11));
                    businessgroupa1vo1.setMoney12(String.valueOf(moneys12));
                    businessgroupa1vo1.setMoneyfirst(String.valueOf(moneyfirsts));
                    businessgroupa1vo1.setMoneysecond(String.valueOf(moneyseconds));
                    businessgroupa1vo1.setMoneytotal(String.valueOf(moneytotals));
                }
                VoList.add(businessgroupa1vo1);
            }
        } else {
            BusinessGroupA1Vo businessgroupa1vo1 = new BusinessGroupA1Vo();
            businessgroupa1vo1.setMoney1("0");
            businessgroupa1vo1.setMoney2("0");
            businessgroupa1vo1.setMoney3("0");
            businessgroupa1vo1.setMoney4("0");
            businessgroupa1vo1.setMoney5("0");
            businessgroupa1vo1.setMoney6("0");
            businessgroupa1vo1.setMoney7("0");
            businessgroupa1vo1.setMoney8("0");
            businessgroupa1vo1.setMoney9("0");
            businessgroupa1vo1.setMoney10("0");
            businessgroupa1vo1.setMoney11("0");
            businessgroupa1vo1.setMoney12("0");
            businessgroupa1vo1.setMoneyfirst("0");
            businessgroupa1vo1.setMoneysecond("0");
            businessgroupa1vo1.setMoneytotal("0");
            String name1 = "PJ142";
            List<Dictionary> curListA = dictionaryService.getForSelect(name1);
            for (Dictionary iteA : curListA) {
                if (iteA.getCode().equals("PJ142002")) {
                    businessgroupa1vo1.setName1(iteA.getValue1());
                    businessgroupa1vo1.setName2(iteA.getValue2());
                }
            }
            VoList.add(businessgroupa1vo1);
        }

        themeplandetail.setContracttype("PJ142003");
        List<ThemePlanDetail> themeplandetaillist3 = themePlanDetailMapper.select(themeplandetail);
        if (themeplandetaillist3.size() > 0) {
            List<BusinessGroupA1Vo> getthree = businessplanMapper.getthreeGroupA1(groupid, year);
            if (getthree.size() > 0) {
                BusinessGroupA1Vo businessgroupa1vo1 = new BusinessGroupA1Vo();
                String name1 = "PJ142";
                List<Dictionary> curListA = dictionaryService.getForSelect(name1);
                for (Dictionary iteA : curListA) {
                    if (iteA.getCode().equals("PJ142003")) {
                        businessgroupa1vo1.setName1(iteA.getValue1());
                        businessgroupa1vo1.setName2(iteA.getValue2());
                    }
                }
                BigDecimal money1 = new BigDecimal(getthree.get(0).getMoney1());
                BigDecimal money2 = new BigDecimal(getthree.get(0).getMoney2());
                BigDecimal money3 = new BigDecimal(getthree.get(0).getMoney3());
                BigDecimal money4 = new BigDecimal(getthree.get(0).getMoney4());
                BigDecimal money5 = new BigDecimal(getthree.get(0).getMoney5());
                BigDecimal money6 = new BigDecimal(getthree.get(0).getMoney6());
                BigDecimal money7 = new BigDecimal(getthree.get(0).getMoney7());
                BigDecimal money8 = new BigDecimal(getthree.get(0).getMoney8());
                BigDecimal money9 = new BigDecimal(getthree.get(0).getMoney9());
                BigDecimal money10 = new BigDecimal(getthree.get(0).getMoney10());
                BigDecimal money11 = new BigDecimal(getthree.get(0).getMoney11());
                BigDecimal money12 = new BigDecimal(getthree.get(0).getMoney12());
                BigDecimal moneyfirst = new BigDecimal(getthree.get(0).getMoneyfirst());
                BigDecimal moneysecond = new BigDecimal(getthree.get(0).getMoneysecond());
                BigDecimal moneytotal = new BigDecimal(getthree.get(0).getMoneytotal());
                if (businessgroupa1vo1.getName2().equals("0")) {
                    BigDecimal moneys1 = money1.divide(new BigDecimal("1000"));
                    BigDecimal moneys2 = money2.divide(new BigDecimal("1000"));
                    BigDecimal moneys3 = money3.divide(new BigDecimal("1000"));
                    BigDecimal moneys4 = money4.divide(new BigDecimal("1000"));
                    BigDecimal moneys5 = money5.divide(new BigDecimal("1000"));
                    BigDecimal moneys6 = money6.divide(new BigDecimal("1000"));
                    BigDecimal moneys7 = money7.divide(new BigDecimal("1000"));
                    BigDecimal moneys8 = money8.divide(new BigDecimal("1000"));
                    BigDecimal moneys9 = money9.divide(new BigDecimal("1000"));
                    BigDecimal moneys10 = money10.divide(new BigDecimal("1000"));
                    BigDecimal moneys11 = money11.divide(new BigDecimal("1000"));
                    BigDecimal moneys12 = money12.divide(new BigDecimal("1000"));
                    BigDecimal moneyfirsts = moneyfirst.divide(new BigDecimal("1000"));
                    BigDecimal moneyseconds = moneysecond.divide(new BigDecimal("1000"));
                    BigDecimal moneytotals = moneytotal.divide(new BigDecimal("1000"));
                    businessgroupa1vo1.setMoney1(String.valueOf(moneys1));
                    businessgroupa1vo1.setMoney2(String.valueOf(moneys2));
                    businessgroupa1vo1.setMoney3(String.valueOf(moneys3));
                    businessgroupa1vo1.setMoney4(String.valueOf(moneys4));
                    businessgroupa1vo1.setMoney5(String.valueOf(moneys5));
                    businessgroupa1vo1.setMoney6(String.valueOf(moneys6));
                    businessgroupa1vo1.setMoney7(String.valueOf(moneys7));
                    businessgroupa1vo1.setMoney8(String.valueOf(moneys8));
                    businessgroupa1vo1.setMoney9(String.valueOf(moneys9));
                    businessgroupa1vo1.setMoney10(String.valueOf(moneys10));
                    businessgroupa1vo1.setMoney11(String.valueOf(moneys11));
                    businessgroupa1vo1.setMoney12(String.valueOf(moneys12));
                    businessgroupa1vo1.setMoneyfirst(String.valueOf(moneyfirsts));
                    businessgroupa1vo1.setMoneysecond(String.valueOf(moneyseconds));
                    businessgroupa1vo1.setMoneytotal(String.valueOf(moneytotals));
                } else {
                    BigDecimal bd = new BigDecimal(businessgroupa1vo1.getName2());
                    BigDecimal bd1 = new BigDecimal("1");
                    BigDecimal bd2 = bd1.add(bd);
                    BigDecimal moneys1 = money1.divide(bd2, scale, roundingMode).multiply(bd).divide(new BigDecimal("1000"));
                    BigDecimal moneys2 = money2.divide(bd2, scale, roundingMode).multiply(bd).divide(new BigDecimal("1000"));
                    BigDecimal moneys3 = money3.divide(bd2, scale, roundingMode).multiply(bd).divide(new BigDecimal("1000"));
                    BigDecimal moneys4 = money4.divide(bd2, scale, roundingMode).multiply(bd).divide(new BigDecimal("1000"));
                    BigDecimal moneys5 = money5.divide(bd2, scale, roundingMode).multiply(bd).divide(new BigDecimal("1000"));
                    BigDecimal moneys6 = money6.divide(bd2, scale, roundingMode).multiply(bd).divide(new BigDecimal("1000"));
                    BigDecimal moneys7 = money7.divide(bd2, scale, roundingMode).multiply(bd).divide(new BigDecimal("1000"));
                    BigDecimal moneys8 = money8.divide(bd2, scale, roundingMode).multiply(bd).divide(new BigDecimal("1000"));
                    BigDecimal moneys9 = money9.divide(bd2, scale, roundingMode).multiply(bd).divide(new BigDecimal("1000"));
                    BigDecimal moneys10 = money10.divide(bd2, scale, roundingMode).multiply(bd).divide(new BigDecimal("1000"));
                    BigDecimal moneys11 = money11.divide(bd2, scale, roundingMode).multiply(bd).divide(new BigDecimal("1000"));
                    BigDecimal moneys12 = money12.divide(bd2, scale, roundingMode).multiply(bd).divide(new BigDecimal("1000"));
                    BigDecimal moneyfirsts = moneyfirst.divide(bd2, scale, roundingMode).multiply(bd).divide(new BigDecimal("1000"));
                    BigDecimal moneyseconds = moneysecond.divide(bd2, scale, roundingMode).multiply(bd).divide(new BigDecimal("1000"));
                    BigDecimal moneytotals = moneytotal.divide(bd2, scale, roundingMode).multiply(bd).divide(new BigDecimal("1000"));
                    businessgroupa1vo1.setMoney1(String.valueOf(moneys1));
                    businessgroupa1vo1.setMoney2(String.valueOf(moneys2));
                    businessgroupa1vo1.setMoney3(String.valueOf(moneys3));
                    businessgroupa1vo1.setMoney4(String.valueOf(moneys4));
                    businessgroupa1vo1.setMoney5(String.valueOf(moneys5));
                    businessgroupa1vo1.setMoney6(String.valueOf(moneys6));
                    businessgroupa1vo1.setMoney7(String.valueOf(moneys7));
                    businessgroupa1vo1.setMoney8(String.valueOf(moneys8));
                    businessgroupa1vo1.setMoney9(String.valueOf(moneys9));
                    businessgroupa1vo1.setMoney10(String.valueOf(moneys10));
                    businessgroupa1vo1.setMoney11(String.valueOf(moneys11));
                    businessgroupa1vo1.setMoney12(String.valueOf(moneys12));
                    businessgroupa1vo1.setMoneyfirst(String.valueOf(moneyfirsts));
                    businessgroupa1vo1.setMoneysecond(String.valueOf(moneyseconds));
                    businessgroupa1vo1.setMoneytotal(String.valueOf(moneytotals));
                }
                VoList.add(businessgroupa1vo1);
            }
        } else {
            BusinessGroupA1Vo businessgroupa1vo1 = new BusinessGroupA1Vo();
            businessgroupa1vo1.setMoney1("0");
            businessgroupa1vo1.setMoney2("0");
            businessgroupa1vo1.setMoney3("0");
            businessgroupa1vo1.setMoney4("0");
            businessgroupa1vo1.setMoney5("0");
            businessgroupa1vo1.setMoney6("0");
            businessgroupa1vo1.setMoney7("0");
            businessgroupa1vo1.setMoney8("0");
            businessgroupa1vo1.setMoney9("0");
            businessgroupa1vo1.setMoney10("0");
            businessgroupa1vo1.setMoney11("0");
            businessgroupa1vo1.setMoney12("0");
            businessgroupa1vo1.setMoneyfirst("0");
            businessgroupa1vo1.setMoneysecond("0");
            businessgroupa1vo1.setMoneytotal("0");
            String name1 = "PJ142";
            List<Dictionary> curListA = dictionaryService.getForSelect(name1);
            for (Dictionary iteA : curListA) {
                if (iteA.getCode().equals("PJ142003")) {
                    businessgroupa1vo1.setName1(iteA.getValue1());
                    businessgroupa1vo1.setName2(iteA.getValue2());
                }
            }
            VoList.add(businessgroupa1vo1);
        }
        return VoList;
    }

    @Override
    public Businessplan selectById(String businessplanid) throws Exception {
        Businessplan businessplan = businessplanMapper.selectByPrimaryKey(businessplanid);
        return businessplan;
    }

    @Override
    public void updateBusinessplanVo(Businessplan businessplan, TokenModel tokenModel) throws Exception {
        businessplan.preUpdate(tokenModel);
        businessplanMapper.updateByPrimaryKeySelective(businessplan);
    }

    @Override
    public String[] getPersonPlan(String year, String groupid) throws Exception {
        String[] personTable = new String[4];
        List<ActualPL> actualPl = businessplanMapper.getAcutal(groupid, year + "-04-01", (Integer.parseInt(year) + 1) + "-03-31");
        for (ActualPL pl : actualPl) {
            Convert(pl, "code");
        }
        List<PersonPlanTable> personPlanTables = businessplanMapper.selectPersonTable(groupid);
        PersonnelPlan personnelPlan = new PersonnelPlan();
        personnelPlan.setYears(year);
        personnelPlan.setGroupid(groupid);
        List<PersonnelPlan> personnelPlans = personnelplanMapper.select(personnelPlan);
        if (personnelPlans.size() > 0) {
            personnelPlan = personnelPlans.get(0);
            PersonPlanTable personPlan = new PersonPlanTable();
            Field[] fields = personPlan.getClass().getDeclaredFields();
            List<PersonPlanTable> nowPersonTable = getNowPersonTable(personnelPlan, personPlanTables);
            List<PersonPlanTable> nextPersonTable = getNextPersonTable(personnelPlan, personPlanTables);
            List<PersonPlanTable> allPersonTable = new ArrayList<>();
            allPersonTable.addAll(nowPersonTable);
            allPersonTable.addAll(nextPersonTable);
            for (PersonPlanTable allPT :
                    allPersonTable) {
                for (int i = 0; i < fields.length; i++) {
                    fields[i].setAccessible(true);
                    if (fields[i].getGenericType() == int.class) {
                        int value = (int) fields[i].get(allPT) + (int) fields[i].get(personPlan);
                        fields[i].set(personPlan, value);
                    } else if (fields[i].getGenericType() == BigDecimal.class) {
                        BigDecimal value1 = (BigDecimal) fields[i].get(allPT);
                        BigDecimal value2 = fields[i].get(personPlan) == null ? new BigDecimal(0) : (BigDecimal) fields[i].get(personPlan);
                        fields[i].set(personPlan, value1.add(value2));
                    }
                }
            }
            personTable[0] = JSON.toJSONString(nowPersonTable);
            personTable[1] = JSON.toJSONString(nextPersonTable);
            personTable[2] = JSON.toJSONString(personPlan);
            personTable[3] = JSON.toJSONString(actualPl);
        } else {
            personTable[0] = "";
            personTable[1] = "";
            personTable[2] = "";
            personTable[3] = JSON.toJSONString(actualPl);
        }
        return personTable;
    }

    @Override
    public void insertBusinessplan(Businessplan businessplan, TokenModel tokenModel) throws Exception {
        String businessplanid = UUID.randomUUID().toString();
        List<BussinessPlanPL> pl = JSON.parseArray(businessplan.getTableP(), BussinessPlanPL.class);
        businessplan.setBusinessplanid(businessplanid);
        businessplan.preInsert(tokenModel);
        businessplanMapper.insert(businessplan);

    }

    private List<PersonPlanTable> getNowPersonTable(PersonnelPlan personnelPlan, List<PersonPlanTable> personPlanTables) throws Exception {
        if (!personnelPlan.getEmployed().equals("[]")) {
            List<PersonPlanTable> _personPlanTables = deepCopy(personPlanTables);
            List<Employed> employedList = JSON.parseArray(personnelPlan.getEmployed(), Employed.class);
            for (PersonPlanTable pt :
                    _personPlanTables) {
                for (Employed employed :
                        employedList) {
                    if (pt.getCode().equals(employed.getNextyear())) {
                        for (int i = 1; i <= 12; i++) {
                            int count = (int) PropertyUtils.getSimpleProperty(pt, "amount" + i) + 1;
                            PropertyUtils.setProperty(pt, "amount" + i, count);
                        }
                    }
                }
                BigDecimal payHour = pt.getPayhour().equals("") ? new BigDecimal(0) : new BigDecimal(pt.getPayhour());
                BigDecimal overTimeHour = pt.getOvertimehour().equals("") ? new BigDecimal(0) : new BigDecimal(pt.getOvertimehour());
                BigDecimal giving46 = pt.getMoney46().equals("") ? new BigDecimal(0) : new BigDecimal(pt.getMoney46()); //给与4-6
                BigDecimal giving37 = pt.getMoney73().equals("") ? new BigDecimal(0) : new BigDecimal(pt.getMoney73()); //给与3-7
                for (int i = 1; i <= 12; i++) {
                    BigDecimal giving;
                    int count = (int) PropertyUtils.getProperty(pt, "amount" + i);
                    BigDecimal workingHour = new BigDecimal(count).multiply(payHour); //残業
                    BigDecimal pay = workingHour.multiply(overTimeHour); //残業费
                    PropertyUtils.setProperty(pt, "workinghour" + i, workingHour);
                    PropertyUtils.setProperty(pt, "pay" + i, pay);
                    if (i == 4 || i == 5 || i == 6) {
                        giving = new BigDecimal(count).multiply(giving46);
                    } else {
                        giving = new BigDecimal(count).multiply(giving37);
                    }
                    PropertyUtils.setProperty(pt, "giving" + i, giving);
                }
                pt.setAmountfirst(pt.getAmount4() * 6);
                pt.setAmountsecond(pt.getAmount4() * 6);
                pt.setAmounttotal(pt.getAmount4() * 12);

                pt.setWorkinghourfirst(pt.getWorkinghour4().multiply(new BigDecimal(6)));
                pt.setWorkinghoursecond(pt.getWorkinghour4().multiply(new BigDecimal(6)));
                pt.setWorkinghourtotal(pt.getWorkinghour4().multiply(new BigDecimal(12)));

                pt.setPayfirst(pt.getPay4().multiply(new BigDecimal(6)));
                pt.setPaysecond(pt.getPay4().multiply(new BigDecimal(6)));
                pt.setPaytotal(pt.getPay4().multiply(new BigDecimal(12)));

                pt.setGivingfirst(AllAdd(pt.getGiving4(), pt.getGiving5(), pt.getGiving6(), pt.getGiving7(), pt.getGiving8(), pt.getGiving9()));
                pt.setGivingsecond(AllAdd(pt.getGiving10(), pt.getGiving11(), pt.getGiving12(), pt.getGiving1(), pt.getGiving2(), pt.getGiving3()));
                pt.setGivingtotal(pt.getGivingfirst().add(pt.getGivingsecond()));
            }
            return _personPlanTables;
        }
        return new ArrayList<PersonPlanTable>();
    }

    private List<PersonPlanTable> getNextPersonTable(PersonnelPlan personnelPlan, List<PersonPlanTable> personPlanTables) throws Exception {
        if (!personnelPlan.getNewentry().equals("[{\"isoutside\":false,\"entermouth\":null}]")) {
            int[] arr = new int[]{4, 5, 6, 7, 8, 9, 10, 11, 12, 1, 2, 3};
            List<PersonPlanTable> _personPlanTables = deepCopy(personPlanTables);
            Calendar calendar = Calendar.getInstance();
            List<NewEmployed> employedList = JSON.parseArray(personnelPlan.getNewentry(), NewEmployed.class);
            for (PersonPlanTable pt :
                    _personPlanTables) {
                for (NewEmployed employed :
                        employedList) {
                    if (pt.getCode().equals(employed.getNextyear()) && !employed.isIsoutside() && StringUtils.isNotBlank(employed.getEntermouth())) {
                        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(employed.getEntermouth());
                        calendar.setTime(date);
                        calendar.add(Calendar.DATE, 1);
                        int mouth = getIndex(arr, calendar.get(Calendar.MONTH) + 1);
                        for (int i = mouth; i < arr.length; i++) {
                            int count = (int) PropertyUtils.getSimpleProperty(pt, "amount" + arr[i]) + 1;
                            PropertyUtils.setProperty(pt, "amount" + arr[i], count);
                        }
                    }
                }
                BigDecimal payHour = pt.getPayhour().equals("") ? new BigDecimal(0) : new BigDecimal(pt.getPayhour());
                BigDecimal overTimeHour = pt.getOvertimehour().equals("") ? new BigDecimal(0) : new BigDecimal(pt.getOvertimehour());
                BigDecimal giving46 = pt.getMoney46().equals("") ? new BigDecimal(0) : new BigDecimal(pt.getMoney46()); //给与4-6
                BigDecimal giving37 = pt.getMoney73().equals("") ? new BigDecimal(0) : new BigDecimal(pt.getMoney73()); //给与3-7
                for (int i = 1; i <= 12; i++) {
                    BigDecimal giving;
                    int count = (int) PropertyUtils.getProperty(pt, "amount" + i);
                    BigDecimal workingHour = new BigDecimal(count).multiply(payHour); //残業
                    BigDecimal pay = workingHour.multiply(overTimeHour); //残業费
                    PropertyUtils.setProperty(pt, "workinghour" + i, workingHour);
                    PropertyUtils.setProperty(pt, "pay" + i, pay);
                    if (i == 4 || i == 5 || i == 6) {
                        giving = new BigDecimal(count).multiply(giving46);
                    } else {
                        giving = new BigDecimal(count).multiply(giving37);
                    }
                    PropertyUtils.setProperty(pt, "giving" + i, giving);
                }
                pt.setAmountfirst(pt.getAmount4() + pt.getAmount5() + pt.getAmount6() + pt.getAmount7() + pt.getAmount8() + pt.getAmount9());
                pt.setAmountsecond(pt.getAmount10() + pt.getAmount11() + pt.getAmount12() + pt.getAmount1() + pt.getAmount2() + pt.getAmount3());
                pt.setAmounttotal(pt.getAmountfirst() + pt.getAmountsecond());

                pt.setWorkinghourfirst(AllAdd(pt.getWorkinghour4(), pt.getWorkinghour5(), pt.getWorkinghour6(), pt.getWorkinghour7(), pt.getWorkinghour8(), pt.getWorkinghour9()));
                pt.setWorkinghoursecond(AllAdd(pt.getWorkinghour10(), pt.getWorkinghour11(), pt.getWorkinghour12(), pt.getWorkinghour1(), pt.getWorkinghour2(), pt.getWorkinghour3()));
                pt.setWorkinghourtotal(pt.getWorkinghourfirst().add(pt.getWorkinghoursecond()));

                pt.setPayfirst(AllAdd(pt.getPay4(), pt.getPay5(), pt.getPay6(), pt.getPay7(), pt.getPay8(), pt.getPay9()));
                pt.setPaysecond(AllAdd(pt.getPay10(), pt.getPay11(), pt.getPay12(), pt.getPay1(), pt.getPay2(), pt.getPay3()));
                pt.setPaytotal(pt.getPayfirst().add(pt.getPaysecond()));

                pt.setGivingfirst(AllAdd(pt.getGiving4(), pt.getGiving5(), pt.getGiving6(), pt.getGiving7(), pt.getGiving8(), pt.getGiving9()));
                pt.setGivingsecond(AllAdd(pt.getGiving10(), pt.getGiving11(), pt.getGiving12(), pt.getGiving1(), pt.getGiving2(), pt.getGiving3()));
                pt.setGivingtotal(pt.getGivingfirst().add(pt.getGivingsecond()));
            }
            return _personPlanTables;
        }
        return new ArrayList<PersonPlanTable>();
    }

    private BigDecimal AllAdd(BigDecimal... number) {
        BigDecimal count = new BigDecimal(0);
        for (BigDecimal bd :
                number) {
            count = count.add(bd);
        }
        return count;
    }

    private static <T> List<T> deepCopy(List<T> src) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);
        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        @SuppressWarnings("unchecked")
        List<T> dest = (List<T>) in.readObject();
        out.close();
        in.close();
        byteOut.close();
        return dest;
    }

    private int getIndex(int[] arr, int value) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == value) {
                return i;
            }
        }
        return 0;
    }

    private <T> void Convert(T t, String val) throws Exception {
        Field[] fields = t.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            if (!fields[i].getName().equals(val)) {
                String value = df.format(new BigDecimal(fields[i].get(t).toString()));
                fields[i].set(t, value);
            }
        }
    }

    //获取第二层 type=2 groupOrg
    private List<OrgTree> getGroupTree(OrgTree orgTree) {
        List<OrgTree> orgTreeList = new ArrayList<>();
        List<OrgTree> orgTrees = orgTree.getOrgs();
        for (OrgTree org :
                orgTrees) {
            orgTreeList.addAll(org.getOrgs());
        }
        return orgTreeList;
    }
}
