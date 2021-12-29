package com.nt.service_pfans.PFANS1000.Impl;

import com.mysql.jdbc.StringUtils;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_Org.Vo.DepartmentVo;
import com.nt.dao_Pfans.PFANS1000.ExpenditureForecast;
import com.nt.dao_Pfans.PFANS1000.PersonnelPlan;
import com.nt.dao_Pfans.PFANS1000.Vo.ExpenditureForecastVo;
import com.nt.dao_Pfans.PFANS6000.Vo.bpSum3Vo;
import com.nt.service_Org.DictionaryService;
import com.nt.service_Org.OrgTreeService;
import com.nt.service_pfans.PFANS1000.ExpenditureForecastService;
import com.nt.service_pfans.PFANS1000.mapper.DepartmentAccountMapper;
import com.nt.service_pfans.PFANS1000.mapper.DepartmentalInsideMapper;
import com.nt.service_pfans.PFANS1000.mapper.ExpenditureForecastMapper;
import com.nt.service_pfans.PFANS1000.mapper.PersonnelplanMapper;
import com.nt.service_pfans.PFANS4000.mapper.PeoplewareFeeMapper;
import com.nt.service_pfans.PFANS5000.mapper.LogPersonStatisticsMapper;
import com.nt.service_pfans.PFANS6000.mapper.CompanyStatisticsMapper;
import com.nt.utils.dao.TokenModel;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional(rollbackFor = Exception.class)
public class ExpenditureForecastServiceImpl implements ExpenditureForecastService {

    @Autowired
    private DepartmentAccountMapper departmentAccountMapper;

    @Autowired
    private ExpenditureForecastMapper expenditureForecastMapper;

    @Autowired
    private OrgTreeService orgTreeService;

    @Autowired
    private PersonnelplanMapper personnelplanMapper;

    @Autowired
    private PeoplewareFeeMapper personFeeMapper;

    @Autowired
    private LogPersonStatisticsMapper logPersonStatisticsMapper;

    @Autowired
    private DepartmentalInsideMapper departmentalInsideMapper;

    @Autowired
    private CompanyStatisticsMapper companyStatisticsMapper;

    @Autowired
    private DictionaryService dictionaryService;


    @Override
    public List<ExpenditureForecast> getInfo(ExpenditureForecast forecast) throws Exception {
        ArrayList<ExpenditureForecast> res = new ArrayList<>();
        //获取参数
        Date saveDate = forecast.getSaveDate();
        //部门ID
        String deptId = forecast.getDeptId();
        LocalDate localDate = saveDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        //根据月份判断获取年份
        int month = localDate.getMonthValue();
        int year = localDate.getYear();
        int year1 = year;//用于与当前系统时间比较
        if (month < 4) {
            year = year - 1;
        }
        //当前系统时间及月份
        Date now = new Date();
        LocalDate nowDate = now.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int nowMonth = nowDate.getMonthValue();
        int nowYear = nowDate.getYear();
        String property_hourActual = "hoursActual" + month;//实际工数
        String property_costsActual = "moneyActual" + month;//实际金额
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        forecast.setSaveDate(sdf.parse(sdf.format(forecast.getSaveDate())));
        List<ExpenditureForecast> record = expenditureForecastMapper.selectOldRevenueForecastList(deptId, String.valueOf(year), saveDate, forecast.getThemeName());
        if (record.size() > 0 && record != null) {//选择月份有数据，显示履历，显示见通
            for (ExpenditureForecast item : record) {
                BeanUtils.setProperty(item, property_hourActual, null);
                BeanUtils.setProperty(item, property_costsActual, null);
            }
        }
//        else {//选择月份无数据
//            if (month == nowMonth && year1 == nowYear) {//是否为当前月操作，如果不当前月操作，无数据就无数据
//                if (month == 4) {
//                    long startTime = System.currentTimeMillis();
//                    record = this.Initialize(deptId, year);
//                    long endTime = System.currentTimeMillis();
//                    System.out.println("当前程序耗时：" + (endTime - startTime) + "ms");
//                }
//                else {
//                    Calendar calendar = Calendar.getInstance();
//                    calendar.setTime(saveDate);
//                    calendar.add(Calendar.MONTH, -1);
////                int month1 = calendar.get(java.util.Calendar.MONTH) + 1;
//                    int month1 = calendar.get(Calendar.MONTH) + 1;
//                    int years = calendar.get(Calendar.YEAR);
//                    if (month1 < 4) {
//                        years = years - 1;
//                    }
//                    Date lastSaveDate = calendar.getTime();//上个月时间
//                    List<ExpenditureForecast> record1 = expenditureForecastMapper.selectOldRevenueForecastList(deptId, String.valueOf(years), lastSaveDate, null);
//                    if (record1.size() > 0 && record1 != null) {//上个月有数据
//                        if (BeanUtils.getProperty(record1.get(0), ("hoursActual" + month1)) == null) {//上月实际为空
//                            //执行上个月实际数据获取接口
//                            this.actualUpdate();
//                            record1 = expenditureForecastMapper.selectOldRevenueForecastList(deptId, String.valueOf(year), lastSaveDate, null);
//                            record1.forEach(item -> {
//                                item.setId(null);
//                            });
//                            record = record1;
//                        }
//                        record = record1;
//                    }
//                }
//            }
//        }
        return record;
    }

    @Override
    public void saveInfo(ExpenditureForecastVo expenditureVo, TokenModel tokenModel) throws Exception{
        ExpenditureForecast expenditureForecast = expenditureVo.getExpenditureForecast();
        //获取参数
        Date saveDate = expenditureForecast.getSaveDate();
        LocalDate localDate = saveDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        //年份
        int year = localDate.getYear();
        //月份
        int month = localDate.getMonthValue();
        if(month < 4){
            year = year - 1;
        }
        //部门ID
        String deptId = expenditureForecast.getDeptId();

        List<ExpenditureForecast> expenditureForecastList = expenditureVo.getExpenditureForecastList();
        if(expenditureForecastList.size() > 0){
            for (ExpenditureForecast ex : expenditureForecastList){
                if(StringUtils.isNullOrEmpty(ex.getId())){
                    ex.preInsert(tokenModel);
                    ex.setId(UUID.randomUUID().toString());
                    ex.setSaveDate(saveDate);
                    ex.setDeptId(deptId);
                    ex.setAnnual(String.valueOf(year));
                }else{
                    ex.setAnnual(String.valueOf(year));
                    ex.preUpdate(tokenModel);
                }
            }

            expenditureForecastMapper.insertOrUpdateBatch(expenditureForecastList);
        }
    }

    @Override
    public List<ExpenditureForecast> getThemeOutDepth(ExpenditureForecast forecast) throws Exception{
        List<ExpenditureForecast> listForReturn = new ArrayList<ExpenditureForecast>();
        //获取参数
        Date saveDate = forecast.getSaveDate();
        //部门ID
        String deptId = forecast.getDeptId();
        LocalDate localDate = saveDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        //根据月份判断获取年份
        int month = localDate.getMonthValue();
        int year = localDate.getYear();
        if(month < 4){
            year = year - 1;
        }

        //从theme表里获取
        listForReturn = expenditureForecastMapper.getThemeOutDepth(deptId,year,saveDate);
        return listForReturn;
    }

    @Override
    public List<ExpenditureForecast> getPoortDepth(ExpenditureForecast forecast) throws Exception {
        List<ExpenditureForecast> res = new ArrayList<>();
        //部门id--部门名称键值对
        List<DepartmentVo> allDepartment = orgTreeService.getAllDepartment();
        HashMap<String,String> companyid = new HashMap<>();
        for(DepartmentVo vo : allDepartment){
            companyid.put(vo.getDepartmentId(),vo.getDepartmentname());
        }
        //数字-月份键值对
        Map<Integer,String> inMapOfMonth = new HashMap<>();//月份映射值，方便使用
        String[] monthList = {"january","february","march","april","may","june","july","august","september","october","november","december"};
        for(int i = 0; i < monthList.length; i++){
            inMapOfMonth.put(i + 1,monthList[i]);
        }
        Date saveDate = forecast.getSaveDate();
        String deptId = forecast.getDeptId();
        LocalDate localDate = saveDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        //年份
        int year = localDate.getYear();//传过来年月
        int year1 = 0;//2021-3月份，对应年度为2020，实际全年度打卡为2020-4~2021-3
        int year2 = 0;//2021-4月份，对应年度为2021，实际全年度打卡为2021-4~2022-3
        //月份
        int month = localDate.getMonthValue();
        if(month < 4){
            year = year - 1;//当前年度
        }
        //实体属性：hoursPlan存放theme计划工数，hoursActual存放人员或构内人数
        ExpenditureForecast employee = expenditureForecastMapper.employeeWork(deptId, String.valueOf(year), "员工");//全年度员工对应theme工数
        if(employee == null){
            employee = new ExpenditureForecast();
        }
        ExpenditureForecast expenditureForecast = expenditureForecastMapper.employeesToClockIn(companyid.get(deptId),String.valueOf(year));//全年度对应社员人数
        String property_a = "hoursActual";
        for(int i = 1; i <= 12; i++){
            BeanUtils.setProperty(employee,property_a + i,new BigDecimal(BeanUtils.getProperty(expenditureForecast,property_a + i)).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
        }
        res.add(employee);
        ExpenditureForecast withinStructure = expenditureForecastMapper.employeeWork(deptId, String.valueOf(year), "构内外注");//全年度构内对应theme工数
        if(withinStructure == null){
            withinStructure = new ExpenditureForecast();
        }
        List<bpSum3Vo> list = companyStatisticsMapper.getbpsum(deptId, String.valueOf(year));//構内駐在人数状況：来自BP社统计
        for(int i = 1; i <= 12; i++){
            for (bpSum3Vo item : list) {
                BigDecimal prop = new BigDecimal(StringUtils.isNullOrEmpty(BeanUtils.getProperty(withinStructure, property_a + i)) ? "0" : BeanUtils.getProperty(withinStructure, property_a + i));//当前对象的每月值
                BigDecimal every = new BigDecimal(StringUtils.isNullOrEmpty(BeanUtils.getProperty(item, inMapOfMonth.get(i))) ? "0" : BeanUtils.getProperty(item, inMapOfMonth.get(i)));//list对象值
                BeanUtils.setProperty(withinStructure,property_a + i, prop.add(every).setScale(2,BigDecimal.ROUND_HALF_UP));
            }
        }
        res.add(withinStructure);
        return res;
    }

    private List<ExpenditureForecast> Initialize(String deptId, int year) throws Exception {
        TokenModel tokenModel = new TokenModel();
        PersonnelPlan personnelPlan = new PersonnelPlan();//人员计划
        personnelPlan.setType(0);
        personnelPlan.setCenterid(deptId);
        personnelPlan.setYears(String.valueOf(year));
        PersonnelPlan unitPricePerCapita = personnelplanMapper.selectOne(personnelPlan);//对应部门，年度，员工的人员计划
        BigDecimal PricePer = new BigDecimal(BigInteger.ZERO);//人均单价
        if(unitPricePerCapita != null && !StringUtils.isNullOrEmpty(unitPricePerCapita.getMoneyavg())){//部门人员计划为空，或没有人均单价
            PricePer = new BigDecimal(unitPricePerCapita.getMoneyavg());
        }
        Date saveDate = new Date();
        //id--部门键值对
        List<DepartmentVo> allDepartment = orgTreeService.getAllDepartment();
        HashMap<String,String> companyid = new HashMap<>();
        for(DepartmentVo vo : allDepartment){
            companyid.put(vo.getDepartmentId(),vo.getDepartmentname());
        }
        String[] classifications = {"构内外注", "构外外注","员工"};
        String propHourPlan = "hoursPlan";
        String propMoneyPlan = "moneyPlan";
        List<ExpenditureForecast> result = new ArrayList<>();
        List<ExpenditureForecast> theme = expenditureForecastMapper.getTheme(deptId, year);
        Map<String, List<ExpenditureForecast>> temp = theme.stream().collect(Collectors.groupingBy(ExpenditureForecast::getThemeName));
        Iterator<Map.Entry<String, List<ExpenditureForecast>>> iterator = temp.entrySet().iterator();
        while (iterator.hasNext()) {//关联theme遍历
            Map.Entry<String, List<ExpenditureForecast>> next = iterator.next();
            ExpenditureForecast reductionOf = new ExpenditureForecast();//存放构内外注和构外外注的工数每月之和，用员工每月工数减去
            for (String s : classifications) {//分类遍历
                if("构内外注".equals(s)){//构内外注：只能取构内委托的委托theme，值对应相应月数
                    ExpenditureForecast exp = new ExpenditureForecast();//当次对象：构内外注
                    for (ExpenditureForecast expenditureForecast : next.getValue()) {
                        for(int i = 1; i <= 12; i++){
                            if("0".equals(expenditureForecast.getType())){//受托theme
                                BigDecimal orghour = new BigDecimal((BeanUtils.getProperty(exp, propHourPlan + i) == null ? "0" : BeanUtils.getProperty(exp, propHourPlan + i)));//当次对象每月计划工数
                                BigDecimal orgmoney = new BigDecimal((BeanUtils.getProperty(exp, propMoneyPlan + i) == null ? "0" : BeanUtils.getProperty(exp, propMoneyPlan + i)));//当次对象每月计划金额
                                BeanUtils.setProperty(exp,propHourPlan + i, orghour.add(BigDecimal.ZERO).setScale(2, BigDecimal.ROUND_HALF_UP).toString());//0
                                BeanUtils.setProperty(exp,propMoneyPlan + i, orgmoney.add(BigDecimal.ZERO).setScale(2, BigDecimal.ROUND_HALF_UP).toString());//0
                            }
                            if("1".equals(expenditureForecast.getType())){//委托theme,只能构内委托
                                BigDecimal orghour = new BigDecimal((BeanUtils.getProperty(exp, propHourPlan + i) == null ? "0" : BeanUtils.getProperty(exp, propHourPlan + i)));//当次对象每月计划工数
                                BigDecimal orgmoney = new BigDecimal((BeanUtils.getProperty(exp, propMoneyPlan + i) == null ? "0" : BeanUtils.getProperty(exp, propMoneyPlan + i)));//当次对象每月计划金额
//                                BigDecimal numberOfReduce = new BigDecimal((BeanUtils.getProperty(reductionOf, propHourPlan + i) == null ? "0" : BeanUtils.getProperty(reductionOf, propHourPlan + i)));//开始计算员工要减去的工数
                                if("PJ142006".equals(expenditureForecast.getContracttype())){//委托theme，构内委托
                                    BeanUtils.setProperty(exp,propHourPlan + i, orghour.add(new BigDecimal(BeanUtils.getProperty(expenditureForecast,propHourPlan + i))).setScale(2, BigDecimal.ROUND_HALF_UP).toString());//计划工数：当次对象每月计划工数加上theme的每月工数
                                    BeanUtils.setProperty(exp,propMoneyPlan + i, orgmoney.add(new BigDecimal(BeanUtils.getProperty(expenditureForecast,propMoneyPlan + i))).setScale(2, BigDecimal.ROUND_HALF_UP).toString());//计划金额：当次对象每月计划金额加上theme的每月金额
//                                    BeanUtils.setProperty(reductionOf,propHourPlan + i,numberOfReduce.add(new BigDecimal(BeanUtils.getProperty(expenditureForecast,propHourPlan + i))).setScale(2, BigDecimal.ROUND_HALF_UP).toString());//开始计算员工要减去的工数
                                }else{
                                    BeanUtils.setProperty(exp,propHourPlan + i, orghour.add(BigDecimal.ZERO).setScale(2, BigDecimal.ROUND_HALF_UP).toString());//0
                                    BeanUtils.setProperty(exp,propMoneyPlan + i, orgmoney.add(BigDecimal.ZERO).setScale(2, BigDecimal.ROUND_HALF_UP).toString());//0
                                }
                            }
                        }
                    }
                    //社内为部门ID，转theme名称为部门
                    if(companyid.get(next.getKey()) != null){
                        exp.setThemeName(companyid.get(next.getKey()));//themename
                    }else{
                        exp.setThemeName(next.getKey());//themename
                    }
                    exp.setClassIfication(s);//分类
                    exp.setThemeinforId(next.getValue().get(0).getThemeinforId());
                    exp.setAnnual(String.valueOf(year));
                    exp.setId(UUID.randomUUID().toString());
                    exp.setDeptId(deptId);
                    exp.setSaveDate(saveDate);
                    exp.preInsert(tokenModel);
                    //获取当条theme对应构内工数
                    for(int i = 1; i <= 12; i++){
                        BigDecimal org = new BigDecimal(StringUtils.isNullOrEmpty(BeanUtils.getProperty(reductionOf, propHourPlan + i)) ? "0" :BeanUtils.getProperty(reductionOf, propHourPlan + i));
                        BeanUtils.setProperty(reductionOf,propHourPlan + i,org.add(new BigDecimal(BeanUtils.getProperty(exp,propHourPlan + i))).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
                    }
                    result.add(exp);
                }
                if("构外外注".equals(s)){//构外外注：只能取构外委托的委托theme，值对应相应月数
                    ExpenditureForecast exp = new ExpenditureForecast();//当次对象：构外外注
                    for (ExpenditureForecast expenditureForecast : next.getValue()) {
                        for(int i = 1; i <= 12; i++){
                            if("0".equals(expenditureForecast.getType())){//受托theme
                                BigDecimal orghour = new BigDecimal((BeanUtils.getProperty(exp, propHourPlan + i) == null ? "0" : BeanUtils.getProperty(exp, propHourPlan + i)));//当次对象每月计划工数
                                BigDecimal orgmoney = new BigDecimal((BeanUtils.getProperty(exp, propMoneyPlan + i) == null ? "0" : BeanUtils.getProperty(exp, propMoneyPlan + i)));//当次对象每月计划金额
                                BeanUtils.setProperty(exp,propHourPlan + i, orghour.add(BigDecimal.ZERO).setScale(2, BigDecimal.ROUND_HALF_UP).toString());//0
                                BeanUtils.setProperty(exp,propMoneyPlan + i, orgmoney.add(BigDecimal.ZERO).setScale(2, BigDecimal.ROUND_HALF_UP).toString());//0
                            }
                            if("1".equals(expenditureForecast.getType())){//委托theme,只能构内委托
                                BigDecimal orghour = new BigDecimal((BeanUtils.getProperty(exp, propHourPlan + i) == null ? "0" : BeanUtils.getProperty(exp, propHourPlan + i)));//当次对象每月计划工数
                                BigDecimal orgmoney = new BigDecimal((BeanUtils.getProperty(exp, propMoneyPlan + i) == null ? "0" : BeanUtils.getProperty(exp, propMoneyPlan + i)));//当次对象每月计划金额
//                                BigDecimal numberOfReduce = new BigDecimal((BeanUtils.getProperty(reductionOf, propHourPlan + i) == null ? "0" : BeanUtils.getProperty(reductionOf, propHourPlan + i)));//开始计算员工要减去的工数
                                if("PJ142007".equals(expenditureForecast.getContracttype())){
                                    BeanUtils.setProperty(exp,propHourPlan + i, orghour.add(new BigDecimal(BeanUtils.getProperty(expenditureForecast,propHourPlan + i))).setScale(2, BigDecimal.ROUND_HALF_UP).toString());//计划工数：当次对象每月计划工数加上theme的每月工数
                                    BeanUtils.setProperty(exp,propMoneyPlan + i, orgmoney.add(new BigDecimal(BeanUtils.getProperty(expenditureForecast,propMoneyPlan + i))).setScale(2, BigDecimal.ROUND_HALF_UP).toString());//计划金额：当次对象每月计划金额加上theme的每月金额
//                                    BeanUtils.setProperty(reductionOf,propHourPlan + i,numberOfReduce.add(new BigDecimal(BeanUtils.getProperty(expenditureForecast,propHourPlan + i))).setScale(2, BigDecimal.ROUND_HALF_UP).toString());//开始计算员工要减去的工数
                                }else{
                                    BeanUtils.setProperty(exp,propHourPlan + i, orghour.add(BigDecimal.ZERO).setScale(2, BigDecimal.ROUND_HALF_UP).toString());//0
                                    BeanUtils.setProperty(exp,propMoneyPlan + i, orgmoney.add(BigDecimal.ZERO).setScale(2, BigDecimal.ROUND_HALF_UP).toString());//0
                                }
                            }
                        }
                    }
                    //社内为部门ID，转theme名称为部门
                    if(companyid.get(next.getKey()) != null){
                        exp.setThemeName(companyid.get(next.getKey()));//themename
                    }else{
                        exp.setThemeName(next.getKey());//themename
                    }
                    exp.setClassIfication(s);//分类
                    exp.setThemeinforId(next.getValue().get(0).getThemeinforId());
                    exp.setAnnual(String.valueOf(year));
                    exp.setId(UUID.randomUUID().toString());
                    exp.setDeptId(deptId);
                    exp.setSaveDate(saveDate);
                    exp.preInsert(tokenModel);
                    //获取当条theme对应构外工数
                    for(int i = 1; i <= 12; i++){
                        BigDecimal org = new BigDecimal(StringUtils.isNullOrEmpty(BeanUtils.getProperty(reductionOf, propHourPlan + i)) ? "0" :BeanUtils.getProperty(reductionOf, propHourPlan + i));
                        BeanUtils.setProperty(reductionOf,propHourPlan + i,org.add(new BigDecimal(BeanUtils.getProperty(exp,propHourPlan + i))).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
                    }
                    result.add(exp);
                }
                if("员工".equals(s)){//员工：关联为受托theme时，每月计划工数取关联theme每月计划工数，每月计划金额为工数 * 部门人均单价，当关联为委托theme时（只能社内委托或center委托），取关联theme的每月对应值
                    ExpenditureForecast exp = new ExpenditureForecast();//当次对象：员工
                    for (ExpenditureForecast expenditureForecast : next.getValue()) {//themename对应多条themedetail
                        for(int i = 1; i <= 12; i++){
                            if("0".equals(expenditureForecast.getType())){//受托theme
                                BigDecimal orghour = new BigDecimal((BeanUtils.getProperty(exp, propHourPlan + i) == null ? "0" : BeanUtils.getProperty(exp, propHourPlan + i)));//当次对象每月计划工数
                                BigDecimal orgmoney = new BigDecimal((BeanUtils.getProperty(exp, propMoneyPlan + i) == null ? "0" : BeanUtils.getProperty(exp, propMoneyPlan + i)));//当次对象每月计划金额
                                BeanUtils.setProperty(exp,propHourPlan + i, orghour.add(new BigDecimal(BeanUtils.getProperty(expenditureForecast,propHourPlan + i))).setScale(2, BigDecimal.ROUND_HALF_UP).toString());//计划工数：当次对象每月计划工数加上theme的每月工数
                                BigDecimal price = new BigDecimal(BeanUtils.getProperty(expenditureForecast, propHourPlan + i)).multiply(PricePer).setScale(2, BigDecimal.ROUND_HALF_UP);
                                BeanUtils.setProperty(exp,propMoneyPlan + i, orgmoney.add(price).setScale(2, BigDecimal.ROUND_HALF_UP).toString());//计划金额：当次对象每月计划金额加上当月计划工数 * 部门人均单价
                            }
                            if("1".equals(expenditureForecast.getType())){//委托theme,只能社内委托或center委托
                                BigDecimal orghour = new BigDecimal((BeanUtils.getProperty(exp, propHourPlan + i) == null ? "0" : BeanUtils.getProperty(exp, propHourPlan + i)));//当次对象每月计划工数
                                BigDecimal orgmoney = new BigDecimal((BeanUtils.getProperty(exp, propMoneyPlan + i) == null ? "0" : BeanUtils.getProperty(exp, propMoneyPlan + i)));//当次对象每月计划金额
                                if("PJ142008".equals(expenditureForecast.getContracttype()) || "PJ142009".equals(expenditureForecast.getContracttype())){
                                    BeanUtils.setProperty(exp,propHourPlan + i, orghour.add(new BigDecimal(BeanUtils.getProperty(expenditureForecast,propHourPlan + i))).setScale(2, BigDecimal.ROUND_HALF_UP).toString());//计划工数：当次对象每月计划工数加上theme的每月工数
                                    BeanUtils.setProperty(exp,propMoneyPlan + i, orgmoney.add(new BigDecimal(BeanUtils.getProperty(expenditureForecast,propMoneyPlan + i))).setScale(2, BigDecimal.ROUND_HALF_UP).toString());//计划金额：当次对象每月计划金额加上theme的每月金额
                                }else{
                                    BeanUtils.setProperty(exp,propHourPlan + i, orghour.add(BigDecimal.ZERO).setScale(2, BigDecimal.ROUND_HALF_UP).toString());//0
                                    BeanUtils.setProperty(exp,propMoneyPlan + i, orgmoney.add(BigDecimal.ZERO).setScale(2, BigDecimal.ROUND_HALF_UP).toString());//0
                                }
                            }
                            //员工计划工数减构内，构外工数
//                            BigDecimal orghour = new BigDecimal((BeanUtils.getProperty(exp, propHourPlan + i) == null ? "0" : BeanUtils.getProperty(exp, propHourPlan + i)));//当次对象每月计划工数
//                            BigDecimal numberOfReduce = new BigDecimal((BeanUtils.getProperty(reductionOf, propHourPlan + i) == null ? "0" : BeanUtils.getProperty(reductionOf, propHourPlan + i)));
//                            BeanUtils.setProperty(exp,propHourPlan + i, orghour.subtract(numberOfReduce).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                        }
                    }
                    //社内为部门ID，转theme名称为部门
                    if(companyid.get(next.getKey()) != null){
                        exp.setThemeName(companyid.get(next.getKey()));//themename
                    }else{
                        exp.setThemeName(next.getKey());//themename
                    }
                    exp.setClassIfication(s);//分类
                    exp.setThemeinforId(next.getValue().get(0).getThemeinforId());//themeinforid
                    exp.setAnnual(String.valueOf(year));
                    exp.setId(UUID.randomUUID().toString());
                    exp.setDeptId(deptId);
                    exp.setSaveDate(saveDate);
                    exp.preInsert(tokenModel);
                    //当条theme员工工数减对应构外构内工数之和
                    for(int i = 1; i <= 12; i++){
                        BigDecimal org = new BigDecimal(StringUtils.isNullOrEmpty(BeanUtils.getProperty(reductionOf, propHourPlan + i)) ? "0" :BeanUtils.getProperty(reductionOf, propHourPlan + i));
                        BigDecimal now = new BigDecimal(BeanUtils.getProperty(exp, propHourPlan + i));
                        BeanUtils.setProperty(exp,propHourPlan + i,now.subtract(org).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
                    }
                    result.add(exp);
                }
            }
        }
        return result;
    }

    /**
     *   ※※※※ 每月实际只能由定时完成，不要手动赋值，影响页面显示状态 ※※※※※
     * */
    @Scheduled(cron="0 0 2 * * ?")//每日凌晨2点
    public void actualUpdate() throws Exception {
        List<ExpenditureForecast> updAct = new ArrayList<>();//要更新实际的所有记录
        //判断是否超过见通截至日
        List<Dictionary> pj160 = dictionaryService.getForSelect("PJ160");//见通截止日
        List<Dictionary> bP027 = dictionaryService.getForSelect("BP027");//日志填写截止日
        String endDate = pj160.get(0).getValue1();
        String logDate = bP027.get(0).getValue1();
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        int nowDate = cal.get(Calendar.DATE);
        if(nowDate > Integer.parseInt(endDate) || nowDate < Integer.parseInt(logDate)){
            return;
        }
        //获取上个月
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -1);
        int month1 = calendar.get(Calendar.MONTH) + 1;//上个月
        int year = calendar.get(Calendar.YEAR);//上个月份对应年
        String log_date = year + (month1 < 10 ? "-0" + month1 : "-" + month1);//上个月对应实际年月字符串
        if (month1 < 4) {
            year = year - 1;//年度
        }
        Date lastSaveDate = calendar.getTime();//上个月时间
        List<DepartmentVo> allDepartment = orgTreeService.getAllDepartment();
        List<String> companyid = new ArrayList<>();//所有有效部门
        for (DepartmentVo vo : allDepartment) {
            companyid.add(vo.getDepartmentId());
        }
        Map<String,String> inMapOfMonth = new HashMap<>();//pj别外注费用统计月份映射值，方便使用
        String[] monthList = {"january","february","march","april","may","june","july","august","september","october","november","december"};
        for(int i = 0; i < monthList.length; i++){
            inMapOfMonth.put(String.valueOf(i + 1),monthList[i]);
        }
        List<String> doNotConformTo = new ArrayList<>();//记录跑过实际的部门
        for (String item : companyid) {//循环部门,寻找做过实际的部门
            List<ExpenditureForecast> record = expenditureForecastMapper.selectOldRevenueForecastList(item, String.valueOf(year), lastSaveDate, null);
            if(record == null || record.size() == 0){
                break;
            }
            String property_h = "hoursActual";
            if(!StringUtils.isNullOrEmpty(BeanUtils.getProperty(record.get(0), property_h + String.valueOf(month1)))){
                doNotConformTo.add(item);
                break;
            }
        }
        //从所有部门中筛选出未跑实际的部门
        companyid = companyid.stream().filter(e -> !doNotConformTo.contains(e)).collect(Collectors.toList());//如果为空或长度0代表所有部门跑完实际
        if (companyid == null || companyid.size() == 0) {
            return;
        } else {
            String property_h = "hoursActual";
            String property_m = "moneyActual";
            for (String deptId : companyid) {//跑实际
                //员工
                List<ExpenditureForecast> lastMonth = expenditureForecastMapper.selectOldRevenueForecastList(deptId, String.valueOf(year), lastSaveDate, null);
                if (lastMonth.size() > 0) {
                    for (ExpenditureForecast forecast : lastMonth) {
                        ExpenditureForecast upd = new ExpenditureForecast();
                        BeanUtils.copyProperties(upd, forecast);
                        if ("员工".equals(forecast.getClassIfication())) {
                            //返回theme，对应项目对应人的对应月份的总工数和金额之和
                            List<Map<String, String>> hourAndMonth = expenditureForecastMapper.getProjectsystem(deptId, forecast.getThemeinforId(), log_date, String.valueOf(year), "0");
                            if (hourAndMonth != null && hourAndMonth.size() == 1) {//正常只会返回一条list
                                Iterator<Map.Entry<String, String>> iterator = hourAndMonth.get(0).entrySet().iterator();
                                while (iterator.hasNext()) {
                                    Map.Entry<String, String> res = iterator.next();
                                    if ("MONEY".equals(res.getKey())) {
                                        BeanUtils.setProperty(upd, property_h + month1, res.getValue());//工数
                                    } else if ("HOURS".equals(res.getKey())) {
                                        BeanUtils.setProperty(upd, property_m + month1, res.getValue());//金额
                                    }
                                }
                            }
                        }
                        if ("构内外注".equals(forecast.getClassIfication())) {
                            //返回theme，对应项目对应人的对应月份的总工数和金额之和
                            List<Map<String, String>> hourAndMonth = expenditureForecastMapper.getProjectsystem(deptId, forecast.getThemeinforId(), log_date, String.valueOf(year), "1");
                            if (hourAndMonth != null && hourAndMonth.size() == 1) {//正常只会返回一条list
                                Iterator<Map.Entry<String, String>> iterator = hourAndMonth.get(0).entrySet().iterator();
                                while (iterator.hasNext()) {
                                    Map.Entry<String, String> res = iterator.next();
                                    if ("HOURS".equals(res.getKey())) {
                                        BeanUtils.setProperty(upd, property_h + month1, res.getValue());
                                        String pjExterMoney = expenditureForecastMapper.getPjExter(deptId, String.valueOf(year), forecast.getThemeinforId(), log_date);
                                        pjExterMoney = new BigDecimal(String.valueOf(res.getValue())).multiply(new BigDecimal(StringUtils.isNullOrEmpty(pjExterMoney) ? "0" : pjExterMoney)).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                                        BeanUtils.setProperty(upd, property_m + month1, pjExterMoney);
                                    }
                                }
                            }
                        }
                        if ("构外外注".equals(forecast.getClassIfication())) {
                            List<Map<String, String>> hourAndMonth = expenditureForecastMapper.outsideStructure(deptId, forecast.getThemeinforId(), log_date);
                            if (hourAndMonth != null && hourAndMonth.size() == 1) {//正常只会返回一条list
                                Iterator<Map.Entry<String, String>> iterator = hourAndMonth.get(0).entrySet().iterator();
                                while (iterator.hasNext()) {
                                    Map.Entry<String, String> res = iterator.next();
                                    if ("HOURS".equals(res.getKey())) {
                                        BeanUtils.setProperty(upd, property_h + month1, res.getValue());
                                    }
                                    if ("MONEY".equals(res.getKey())) {
                                        BeanUtils.setProperty(upd, property_m + month1, res.getValue());
                                    }
                                }
                            }
                        }
//                    upd.preUpdate();//定时任务无法获取token
                        updAct.add(upd);
                    }
                }
            }
            if(updAct.size() > 0){
                expenditureForecastMapper.updateList(updAct);
            }
        }
    }

    @Scheduled(cron="0 0 3 * * ?")
    @Override
    public void saveAuto() throws Exception {
        List<DepartmentVo> allDepartment = orgTreeService.getAllDepartment();
        List<String> companyid = new ArrayList<>();//所有有效部门
        for (DepartmentVo vo : allDepartment) {
            companyid.add(vo.getDepartmentId());
        }
        //判断是否超过见通截至日
        List<Dictionary> pj160 = dictionaryService.getForSelect("PJ160");//见通截止日
        List<Dictionary> bP027 = dictionaryService.getForSelect("BP027");//日志填写截止日
        String endDate = pj160.get(0).getValue1();
        String logDate = bP027.get(0).getValue1();
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        int day = cal.get(Calendar.DATE);//当前天数
        int month = cal.get(Calendar.MONTH) + 1;//当前月
        int year = cal.get(Calendar.YEAR);//当前年
        if (month < 4) {
            year = year - 1;//当前年度
        }
        Date nowDate = cal.getTime();
        if(day > Integer.parseInt(endDate) || day < Integer.parseInt(logDate)){
            return;
        }
        //上个月
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -1);
        int month1 = calendar.get(Calendar.MONTH) + 1;//上个月
        int year1 = calendar.get(Calendar.YEAR);//上个月份对应年
        if (month1 < 4) {
            year1 = year - 1;//上月对应年度
        }
        Date lastSaveDate = calendar.getTime();//上个月时间
        for (String item : companyid) {//循环部门
            //寻找当前月份是否存在记录
            List<ExpenditureForecast> find = expenditureForecastMapper.selectOldRevenueForecastList(item, String.valueOf(year), nowDate, null);
            if (find.size() > 0) {
                continue;
            }
            List<ExpenditureForecast> record = new ArrayList<>();
            if (month == 4) {
                long startTime = System.currentTimeMillis();
                record = this.Initialize(item, year);//初始化数据
                long endTime = System.currentTimeMillis();
                System.out.println("当前程序耗时：" + (endTime - startTime) + "ms");
                if(record != null && record.size() != 0){
                    expenditureForecastMapper.insertOrUpdateBatch(record);
                }
            }
            else {
                List<ExpenditureForecast> record1 = expenditureForecastMapper.selectOldRevenueForecastList(item, String.valueOf(year1), lastSaveDate, null);
                if (record1.size() > 0 && record1 != null) {//上个月有数据
                    if (BeanUtils.getProperty(record1.get(0), ("hoursActual" + month1)) == null) {//上月实际为空
                        //执行上个月实际数据获取接口
                        this.actualUpdate();
                        record1 = expenditureForecastMapper.selectOldRevenueForecastList(item, String.valueOf(year1), lastSaveDate, null);
                        for (ExpenditureForecast items : record1) {
                            items.setId(UUID.randomUUID().toString());//新月份数据，保证无主键
                            items.setAnnual(String.valueOf(year));
                            items.setSaveDate(nowDate);
                        }
                        if (record1.size() > 0) {
                            expenditureForecastMapper.insertOrUpdateBatch(record1);
                        }
                    }else{//上个月实际不为空
                        for (ExpenditureForecast items : record1) {
                            items.setId(UUID.randomUUID().toString());//新月份数据，保证无主键
                            items.setAnnual(String.valueOf(year));
                            items.setSaveDate(nowDate);
                        }
                        if (record1.size() > 0) {
                            expenditureForecastMapper.insertOrUpdateBatch(record1);
                        }
                    }
                }
            }

        }
    }
}
