package com.nt.service_pfans.PFANS1000.Impl;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
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
import com.nt.utils.LogicalException;
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
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
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

    private String getProperty(Object o, String key) throws Exception {
        try {
            return org.apache.commons.beanutils.BeanUtils.getProperty(o, key);
        } catch (Exception e) {
            throw new LogicalException(e.getMessage());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public List<String> importUser(HttpServletRequest request, TokenModel tokenModel) throws Exception {
        try {
            List<String> Result = new ArrayList<String>();
            MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
            File f = null;
            f = File.createTempFile("tmp", null);
            file.transferTo(f);
            ExcelReader reader = ExcelUtil.getReader(f);
            List<List<Object>> list = reader.read();
            List<Object> model = new ArrayList<Object>();
            model.add("4月");
            model.add("5月");
            model.add("6月");
            model.add("7月");
            model.add("8月");
            model.add("9月");
            model.add("10月");
            model.add("11月");
            model.add("12月");
            model.add("1月");
            model.add("2月");
            model.add("3月");
            List<Object> key = list.get(0);
            for (int i = 1; i < key.size(); i++) {
                if (!key.get(i).toString().trim().equals(model.get(i - 1))) {
                    throw new LogicalException("第" + (i) + "列标题错误，应为" + model.get(i - 1).toString());
                }
            }
            int n = 1;
            int accesscount = 0;
            int error = 0;
            List<Map<String, String>> listmap = new ArrayList<>();
            for (int i = 1; i < list.size(); i++) {
                Map<String, String> map = new HashMap<>();
                List<Object> value = list.get(n);
                n++;
                if (value != null && !value.isEmpty()) {
                    if (value.get(0).toString().equals("")) {
                        continue;
                    }
                }
                if (value.size() > 1) {
                    Businessplan businessplan = new Businessplan();
                    businessplan.setEncoding(value.get(0).toString().substring(0, 2));
                    List<Businessplan> businessplanlist = businessplanMapper.select(businessplan);
                    if (businessplanlist.size() == 0) {
                        error = error + 1;
                        Result.add("模板第" + (n - 1) + "行的部门未创建事业计划，请创建数据，导入失败");
                        map.put("delete", value.get(0).toString());
                        listmap.add(map);
                        continue;
                    }
                }
            }
            if (listmap.size() > 0) {
                for (int k = 0; k < listmap.size(); k++) {
                    for (int m = 1; m < list.size(); m++) {
                        List<Object> value = list.get(m);
                        for (Map<String, String> map : listmap) {
                            for (String s : map.values()) {
                                if (value.get(0).toString().equals(s)) {
                                    list.remove(m);
                                }
                            }
                        }
                    }
                }
            }
            int m = 1;
            for (int i = 1; i < list.size(); i++) {
                List<Map<String, String>> lists = new ArrayList<>();
                List<Map<String, String>> lists1 = new ArrayList<>();
                List<Object> value = list.get(m);
                m++;
                if (value.size() > 1) {
                    Businessplan businessplan = new Businessplan();
                    businessplan.setEncoding(value.get(0).toString().substring(0, 2));
                    List<Businessplan> businessplanlist = businessplanMapper.select(businessplan);
                    if (businessplanlist.size() > 0) {
                        String money4 = "";
                        String money5 = "";
                        String money6 = "";
                        String money7 = "";
                        String money8 = "";
                        String money9 = "";
                        String money10 = "";
                        String money11 = "";
                        String money12 = "";
                        String money1 = "";
                        String money2 = "";
                        String money3 = "";
                        String name = "";
                        String companyen = "";
                        int scale = 2;//设置位数
                        int roundingMode = 4;//表示四舍五入，可以选择其他舍值方式，例如去尾，等等.
                        JSONArray jsonArray = JSONArray.parseArray(businessplanlist.get(0).getAssets_lodyear());
                        JSONArray jsonArray1 = JSONArray.parseArray(businessplanlist.get(0).getEquipment_lodyear());
                        for (Object ob : jsonArray) {
                            Map<String, String> map = new HashMap<>();
                            money4 = getProperty(ob, "money4");
                            money5 = getProperty(ob, "money5");
                            money6 = getProperty(ob, "money6");
                            money7 = getProperty(ob, "money7");
                            money8 = getProperty(ob, "money8");
                            money9 = getProperty(ob, "money9");
                            money10 = getProperty(ob, "money10");
                            money11 = getProperty(ob, "money11");
                            money12 = getProperty(ob, "money12");
                            money1 = getProperty(ob, "money1");
                            money2 = getProperty(ob, "money2");
                            money3 = getProperty(ob, "money3");
                            name = getProperty(ob, "encoding");
                            companyen = getProperty(ob, "companyen");
                            BigDecimal money1s = new BigDecimal("0");
                            BigDecimal money2s = new BigDecimal("0");
                            BigDecimal money3s = new BigDecimal("0");
                            BigDecimal money4s = new BigDecimal("0");
                            BigDecimal money5s = new BigDecimal("0");
                            BigDecimal money6s = new BigDecimal("0");
                            BigDecimal money7s = new BigDecimal("0");
                            BigDecimal money8s = new BigDecimal("0");
                            BigDecimal money9s = new BigDecimal("0");
                            BigDecimal money10s = new BigDecimal("0");
                            BigDecimal money11s = new BigDecimal("0");
                            BigDecimal money12s = new BigDecimal("0");
                            if (name != null) {
                                if (name.equals(value.get(0).toString().substring(0, 2))) {
                                    BigDecimal money41 = new BigDecimal(money4);
                                    BigDecimal money411 = new BigDecimal(value.get(1).toString());
                                    money4s = money41.add(money411).setScale(scale, roundingMode);
                                    BigDecimal money51 = new BigDecimal(money5);
                                    BigDecimal money511 = new BigDecimal(value.get(2).toString());
                                    money5s = money51.add(money511).setScale(scale, roundingMode);
                                    BigDecimal money61 = new BigDecimal(money6);
                                    BigDecimal money611 = new BigDecimal(value.get(3).toString());
                                    money6s = money61.add(money611).setScale(scale, roundingMode);
                                    BigDecimal money71 = new BigDecimal(money7);
                                    BigDecimal money711 = new BigDecimal(value.get(4).toString());
                                    money7s = money71.add(money711).setScale(scale, roundingMode);
                                    BigDecimal money81 = new BigDecimal(money8);
                                    BigDecimal money811 = new BigDecimal(value.get(5).toString());
                                    money8s = money81.add(money811).setScale(scale, roundingMode);
                                    BigDecimal money91 = new BigDecimal(money9);
                                    BigDecimal money911 = new BigDecimal(value.get(6).toString());
                                    money9s = money91.add(money911).setScale(scale, roundingMode);
                                    BigDecimal money101 = new BigDecimal(money10);
                                    BigDecimal money1011 = new BigDecimal(value.get(7).toString());
                                    money10s = money101.add(money1011).setScale(scale, roundingMode);
                                    BigDecimal money111 = new BigDecimal(money11);
                                    BigDecimal money1111 = new BigDecimal(value.get(8).toString());
                                    money11s = money111.add(money1111).setScale(scale, roundingMode);
                                    BigDecimal money121 = new BigDecimal(money12);
                                    BigDecimal money1211 = new BigDecimal(value.get(9).toString());
                                    money12s = money121.add(money1211).setScale(scale, roundingMode);
                                    BigDecimal money110 = new BigDecimal(money1);
                                    BigDecimal money1110 = new BigDecimal(value.get(10).toString());
                                    money1s = money110.add(money1110).setScale(scale, roundingMode);
                                    BigDecimal money21 = new BigDecimal(money2);
                                    BigDecimal money211 = new BigDecimal(value.get(11).toString());
                                    money2s = money21.add(money211).setScale(scale, roundingMode);
                                    BigDecimal money31 = new BigDecimal(money3);
                                    BigDecimal money311 = new BigDecimal(value.get(12).toString());
                                    money3s = money31.add(money311).setScale(scale, roundingMode);
                                }
                            }
                            map.put("money4", money4s.toString());
                            map.put("money5", money5s.toString());
                            map.put("money6", money6s.toString());
                            map.put("money7", money7s.toString());
                            map.put("money8", money8s.toString());
                            map.put("money9", money9s.toString());
                            map.put("money10", money10s.toString());
                            map.put("money11", money11s.toString());
                            map.put("money12", money12s.toString());
                            map.put("money1", money1s.toString());
                            map.put("money2", money2s.toString());
                            map.put("money3", money3s.toString());
                            map.put("encoding", name);
                            map.put("companyen", companyen);
                            lists.add(map);
                        }

                        for (Object ob : jsonArray1) {
                            Map<String, String> map = new HashMap<>();
                            money4 = getProperty(ob, "money4");
                            money5 = getProperty(ob, "money5");
                            money6 = getProperty(ob, "money6");
                            money7 = getProperty(ob, "money7");
                            money8 = getProperty(ob, "money8");
                            money9 = getProperty(ob, "money9");
                            money10 = getProperty(ob, "money10");
                            money11 = getProperty(ob, "money11");
                            money12 = getProperty(ob, "money12");
                            money1 = getProperty(ob, "money1");
                            money2 = getProperty(ob, "money2");
                            money3 = getProperty(ob, "money3");
                            name = getProperty(ob, "encoding");
                            companyen = getProperty(ob, "companyen");
                            BigDecimal money1s = new BigDecimal("0");
                            BigDecimal money2s = new BigDecimal("0");
                            BigDecimal money3s = new BigDecimal("0");
                            BigDecimal money4s = new BigDecimal("0");
                            BigDecimal money5s = new BigDecimal("0");
                            BigDecimal money6s = new BigDecimal("0");
                            BigDecimal money7s = new BigDecimal("0");
                            BigDecimal money8s = new BigDecimal("0");
                            BigDecimal money9s = new BigDecimal("0");
                            BigDecimal money10s = new BigDecimal("0");
                            BigDecimal money11s = new BigDecimal("0");
                            BigDecimal money12s = new BigDecimal("0");
                            if (name != null) {
                                if (name.equals(value.get(0).toString().substring(0, 2))) {
                                    BigDecimal money41 = new BigDecimal(money4);
                                    BigDecimal money411 = new BigDecimal(value.get(1).toString());
                                    money4s = money41.add(money411).setScale(scale, roundingMode);
                                    BigDecimal money51 = new BigDecimal(money5);
                                    BigDecimal money511 = new BigDecimal(value.get(2).toString());
                                    money5s = money51.add(money511).setScale(scale, roundingMode);
                                    BigDecimal money61 = new BigDecimal(money6);
                                    BigDecimal money611 = new BigDecimal(value.get(3).toString());
                                    money6s = money61.add(money611).setScale(scale, roundingMode);
                                    BigDecimal money71 = new BigDecimal(money7);
                                    BigDecimal money711 = new BigDecimal(value.get(4).toString());
                                    money7s = money71.add(money711).setScale(scale, roundingMode);
                                    BigDecimal money81 = new BigDecimal(money8);
                                    BigDecimal money811 = new BigDecimal(value.get(5).toString());
                                    money8s = money81.add(money811).setScale(scale, roundingMode);
                                    BigDecimal money91 = new BigDecimal(money9);
                                    BigDecimal money911 = new BigDecimal(value.get(6).toString());
                                    money9s = money91.add(money911).setScale(scale, roundingMode);
                                    BigDecimal money101 = new BigDecimal(money10);
                                    BigDecimal money1011 = new BigDecimal(value.get(7).toString());
                                    money10s = money101.add(money1011).setScale(scale, roundingMode);
                                    BigDecimal money111 = new BigDecimal(money11);
                                    BigDecimal money1111 = new BigDecimal(value.get(8).toString());
                                    money11s = money111.add(money1111).setScale(scale, roundingMode);
                                    BigDecimal money121 = new BigDecimal(money12);
                                    BigDecimal money1211 = new BigDecimal(value.get(9).toString());
                                    money12s = money121.add(money1211).setScale(scale, roundingMode);
                                    BigDecimal money110 = new BigDecimal(money1);
                                    BigDecimal money1110 = new BigDecimal(value.get(10).toString());
                                    money1s = money110.add(money1110).setScale(scale, roundingMode);
                                    BigDecimal money21 = new BigDecimal(money2);
                                    BigDecimal money211 = new BigDecimal(value.get(11).toString());
                                    money2s = money21.add(money211).setScale(scale, roundingMode);
                                    BigDecimal money31 = new BigDecimal(money3);
                                    BigDecimal money311 = new BigDecimal(value.get(12).toString());
                                    money3s = money31.add(money311).setScale(scale, roundingMode);
                                }
                            }
                            map.put("money4", money4s.toString());
                            map.put("money5", money5s.toString());
                            map.put("money6", money6s.toString());
                            map.put("money7", money7s.toString());
                            map.put("money8", money8s.toString());
                            map.put("money9", money9s.toString());
                            map.put("money10", money10s.toString());
                            map.put("money11", money11s.toString());
                            map.put("money12", money12s.toString());
                            map.put("money1", money1s.toString());
                            map.put("money2", money2s.toString());
                            map.put("money3", money3s.toString());
                            map.put("encoding", name);
                            map.put("companyen", companyen);
                            lists1.add(map);
                        }
                        Businessplan business = new Businessplan();
                        BeanUtils.copyProperties(businessplanlist.get(0), business);
                        businessplanMapper.delete(business);
                        Businessplan business1 = new Businessplan();
                        BeanUtils.copyProperties(businessplanlist.get(0), business1);
                        business1.preInsert(tokenModel);
                        business1.setAssets_lodyear(JSONArray.toJSONString(lists));
                        business1.setEquipment_lodyear(JSONArray.toJSONString(lists1));
                        businessplanMapper.insert(business1);
                        accesscount = accesscount + 1;
                    }
                }
            }
            Result.add("失败数：" + error);
            Result.add("成功数：" + accesscount);
            return Result;
        } catch (Exception e) {
            throw new LogicalException("导入失败");
        }
    }


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
                orgtreevo.setEncoding(org1.getEncoding().substring(0, 2));
                orgtreevo.setMoney4("0");
                orgtreevo.setMoney5("0");
                orgtreevo.setMoney6("0");
                orgtreevo.setMoney7("0");
                orgtreevo.setMoney8("0");
                orgtreevo.setMoney9("0");
                orgtreevo.setMoney10("0");
                orgtreevo.setMoney11("0");
                orgtreevo.setMoney12("0");
                orgtreevo.setMoney1("0");
                orgtreevo.setMoney2("0");
                orgtreevo.setMoney3("0");
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
        String businessplanid = businessplan.getBusinessplanid();
        Businessplan business = new Businessplan();
        business.setYear(businessplan.getYear());
        business.setGroup_id(businessplan.getGroup_id());
        List<Businessplan> businessplanlist = businessplanMapper.select(business);
        businessplanlist = businessplanlist.stream().filter(item -> (!item.getBusinessplanid().equals(businessplanid))).collect(Collectors.toList());
        if (businessplanlist.size() > 0) {
            throw new LogicalException("本部门该年度事业计划已经创建，请到列表页中查找编辑。");
        }
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
        Businessplan business = new Businessplan();
        business.setYear(businessplan.getYear());
        business.setGroup_id(businessplan.getGroup_id());
        List<Businessplan> businessplanlist = businessplanMapper.select(business);
        if (businessplanlist.size() > 0) {
            throw new LogicalException("本部门该年度事业计划已经创建，请到列表页中查找编辑。");
        }
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
