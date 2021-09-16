package com.nt.service_pfans.PFANS5000.Impl;

import com.alibaba.fastjson.JSONObject;
import com.mysql.jdbc.StringUtils;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_Org.Vo.DepartmentVo;
import com.nt.dao_Pfans.PFANS5000.LogManagement;
import com.nt.dao_Pfans.PFANS5000.LogPersonStatistics;
import com.nt.dao_Pfans.PFANS5000.Vo.LogPersonReturnVo;
import com.nt.dao_Pfans.PFANS8000.WorkingDay;
import com.nt.service_Org.DictionaryService;
import com.nt.service_Org.OrgTreeService;
import com.nt.service_pfans.PFANS2000.Impl.GivingServiceImpl;
import com.nt.service_pfans.PFANS5000.LogPersonStatisticsService;
import com.nt.service_pfans.PFANS5000.mapper.LogManagementMapper;
import com.nt.service_pfans.PFANS5000.mapper.LogPersonStatisticsMapper;
import com.nt.service_pfans.PFANS8000.mapper.WorkingDayMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public  class LogPersonStatisticsServiceImpl implements LogPersonStatisticsService {

    @Autowired
    private LogPersonStatisticsMapper logPersonStatisticsMapper;

    @Autowired
    private WorkingDayMapper workingDayMapper;

    @Autowired
    private LogManagementMapper logManagementMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private OrgTreeService orgTreeService;

    @Autowired
    private DictionaryService dictionaryService;

    @Override
    public List<LogPersonStatistics> getLogDataList(LogPersonStatistics logPersonStatistics,String dateOf) throws Exception {
        return logPersonStatisticsMapper.selectByDate(logPersonStatistics.getOwners(),dateOf);
    }


    //region scc add 日志人别统计 from
    @Override
    public List<LogPersonReturnVo> getLogPerson(LogPersonStatistics logPersonStatistics,String month, TokenModel tokenModel) throws Exception {
//        ower.setOwners(tokenModel.getOwnerList());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date revelation = sdf.parse(month);
        Calendar cal = Calendar.getInstance();
        cal.setTime(revelation);
        //当前月第一天
        cal.add(Calendar.MONTH,0);
        cal.set(Calendar.DAY_OF_MONTH,1);
        String firstDayOfThisMonth = sdf.format(cal.getTime());
        //当前月最后一天
        cal.setTime(revelation);
        cal.add(Calendar.MONTH,1);
        cal.set(Calendar.DAY_OF_MONTH,0);
        String lastDayOfThisMonth = sdf.format(cal.getTime());
        List<LogPersonStatistics> logPersonList = this.getLogDataList(logPersonStatistics,lastDayOfThisMonth.trim().substring(0,lastDayOfThisMonth.length()-3));
        int workDaysExceptWeekend = this.getWorkDaysExceptWeekend(sdf.parse(firstDayOfThisMonth), sdf.parse(lastDayOfThisMonth));
        List<LogPersonReturnVo> logPersonReturnVos = new ArrayList<>();
//        logPersonList = logPersonList.stream().filter(item -> (item.getDepartment().equals(groupid))).collect(Collectors.toList());
        if(logPersonList.size() > 0) {
        Map<String,List<LogPersonStatistics>> lptList =  logPersonList.stream().collect(Collectors.groupingBy(LogPersonStatistics :: getUser_id, HashMap::new,Collectors.toList()));
            logPersonReturnVos = new ArrayList<>();
            for (Map.Entry<String, List<LogPersonStatistics>> m : lptList.entrySet()) {
                LogPersonReturnVo personReturnVo = new LogPersonReturnVo();
                personReturnVo.setUsername(m.getKey());
                personReturnVo.setGroupname(m.getValue().get(0).getDepartment());
                personReturnVo.setCompany(m.getValue().get(0).getCompany());
                personReturnVo.setProject("—");
                personReturnVo.setAdjust(null);
                BigDecimal total = BigDecimal.ZERO;
                for (LogPersonStatistics logs : m.getValue()) {
                    total = total.add(StringUtils.isNullOrEmpty(logs.getDuration()) ? BigDecimal.ZERO : new BigDecimal(logs.getDuration()));
//                    total = total.add(StringUtils.isNullOrEmpty(logs.getDuration()) ? BigDecimal.ZERO : new BigDecimal(logs.getAdjust()));
                }
                personReturnVo.setGeneral(total.toString());
                BigDecimal ratio = total.divide(new BigDecimal(workDaysExceptWeekend * 8),4,BigDecimal.ROUND_HALF_UP);
                BigDecimal percentageOf = ratio.multiply(new BigDecimal("100")).setScale(2,BigDecimal.ROUND_HALF_UP);
                personReturnVo.setRatio(percentageOf.toString() + "%");
                personReturnVo.setLogpersonstatistics(m.getValue());
                logPersonReturnVos.add(personReturnVo);
            }
        }
        return logPersonReturnVos;
    }

    @Override
    public void updateByVoId(List<LogPersonReturnVo> logPersonReturnVoList) throws Exception {
        logPersonReturnVoList.forEach(item ->{
            item.getLogpersonstatistics().forEach(ids ->{
                LogPersonStatistics accordingToUpdate = new LogPersonStatistics();
                accordingToUpdate.setLogperson_id(ids.getLogperson_id());
                accordingToUpdate.setAdjust(ids.getAdjust());
                logPersonStatisticsMapper.updateByPrimaryKeySelective(accordingToUpdate);
            });
        });
    }
    //endregion scc add 日志人别统计 to

    //region 工作日 scc 9/13
    private int getWorkDaysExceptWeekend(Date start, Date end) {
        int workDays = 0;
        if (end.getTime() > start.getTime()) {
            Calendar calStar = Calendar.getInstance();
            calStar.setTime(start);
            Calendar calEnd = Calendar.getInstance();
            calEnd.setTime(end);
            while (calStar.compareTo(calEnd) <= 0) {
                if (calStar.get(Calendar.DAY_OF_WEEK) != 7 && calStar.get(Calendar.DAY_OF_WEEK) != 1) {
                    workDays++;
                }
                calStar.add(Calendar.DAY_OF_MONTH, 1);
            }
        }
        SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
        //工作日表
        List<WorkingDay> workingDaysList = workingDayMapper.getWorkingday(ymd.format(start),ymd.format(end));
        int workingDayscount = workingDaysList.size();
        for (int i = 0; i < workingDaysList.size(); i++) {
            Date bdate = workingDaysList.get(i).getWorkingdate();
            Calendar cal = Calendar.getInstance();
            cal.setTime(bdate);
            if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
                workingDayscount = workingDayscount - 1;
            }
        }
        //振替出勤日
        List<WorkingDay> workingDaysList1 = workingDaysList.stream().filter(p->(p.getType().equals("4"))).collect(Collectors.toList());

        return workDays - workingDayscount + workingDaysList1.size();
    }
    //endregion 工作日 scc 9/13


    //region scc add 9/14 定时任务，根据日志更新数据 from
    @Scheduled(cron="0 0 1 * * ?")//每日凌晨1点
    public void saveLogPersonStatistics() throws Exception{
        List<Dictionary> diclist = dictionaryService.getForSelect("BP027");
        List<Dictionary> log = diclist.stream().filter(item -> (item.getCode().equals("BP027001"))).collect(Collectors.toList());
        int deadline = Integer.valueOf(log.get(0).getValue1());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar currentDate = Calendar.getInstance();
        //当前天数
        int currentDays = currentDate.get(Calendar.DAY_OF_MONTH);
        //上个月第一天
        currentDate.add(Calendar.MONTH,-1);
        currentDate.set(Calendar.DAY_OF_MONTH,1);
        String firstDayOfLastMonth = sdf.format(currentDate.getTime());
        //上个月最后一天
        Calendar currentDate1 = Calendar.getInstance();
        currentDate1.add(Calendar.MONTH,0);
        currentDate1.set(Calendar.DAY_OF_MONTH,0);
        String lastDayOfLastMonth = sdf.format(currentDate1.getTime());
        LogPersonStatistics owens = new LogPersonStatistics();
        owens.setOwners(null);
        if(currentDays > deadline){
            List<LogPersonStatistics> logPersonList = this.getLogDataList(owens,lastDayOfLastMonth.trim().substring(0,lastDayOfLastMonth.length()-3));
            if(logPersonList.size() > 0){
                return;
            }else{
                LogManagement findlog = new LogManagement();
                findlog.setOwners(null);
                List<LogPersonStatistics> logs = logPersonStatisticsMapper.selectByDateLogment(lastDayOfLastMonth.trim().substring(0,lastDayOfLastMonth.length()-3));
                logs.forEach(item -> {
                    item.setLogperson_id(UUID.randomUUID().toString());
                    item.preInsert();
                    item.setOwner(item.getUser_id());
                    item.setCompany("PSDCD");
                    item.setAdjust(item.getDuration());
                    logPersonStatisticsMapper.insert(item);
                });
            }
        }else{
            return;
        }

    }
    //endregion scc add 9/14 定时任务，根据日志更新数据 to

    // region 积木
//    @Override
//    public Object getTableinfoReport(String month) throws Exception {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        Date revelation = sdf.parse(month);
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(revelation);
//        //当前月第一天
//        cal.add(Calendar.MONTH,0);
//        cal.set(Calendar.DAY_OF_MONTH,1);
//        String firstDayOfThisMonth = sdf.format(cal.getTime());
//        //当前月最后一天
//        cal.setTime(revelation);
//        cal.add(Calendar.MONTH,1);
//        cal.set(Calendar.DAY_OF_MONTH,0);
//        String lastDayOfThisMonth = sdf.format(cal.getTime());
//        List<LogPersonStatistics> logPersonList = this.getLogDataList(firstDayOfThisMonth, lastDayOfThisMonth);
//        int workDaysExceptWeekend = this.getWorkDaysExceptWeekend(sdf.parse(firstDayOfThisMonth), sdf.parse(lastDayOfThisMonth));
//        ArrayList<LogPersonStatistics> legoSet = new ArrayList<>();
//        //部门键值对
//        List<DepartmentVo> allDepartment = orgTreeService.getAllDepartment();
//        HashMap<String, String> companyid = new HashMap<>();
//        for (DepartmentVo vo : allDepartment) {
//            companyid.put(vo.getDepartmentId(),vo.getDepartmentname());
//        }
//        //部门键值对
//        if(logPersonList.size() > 0) {
//            Map<String,List<LogPersonStatistics>> lptList =  logPersonList.stream().collect(Collectors.groupingBy(LogPersonStatistics :: getUser_id, HashMap::new,Collectors.toList()));
//            for (Map.Entry<String, List<LogPersonStatistics>> m : lptList.entrySet()) {
//                LogPersonReturnVo personReturnVo = new LogPersonReturnVo();
//                LogPersonStatistics legoSetenty = new LogPersonStatistics();
//                Query query = new Query();
//                query.addCriteria(Criteria.where("userid").is(m.getKey()));
//                CustomerInfo managers = mongoTemplate.findOne(query, CustomerInfo.class);
//                legoSetenty.setUser_id(managers.getUserinfo().getCustomername());//名字
//                legoSetenty.setDepartment(companyid.get(m.getValue().get(0).getDepartment()));//部门
//                legoSetenty.setCompany(m.getValue().get(0).getCompany());//公司
//                BigDecimal total = BigDecimal.ZERO;
//                for (LogPersonStatistics logs : m.getValue()) {
//                    total = total.add(StringUtils.isNullOrEmpty(logs.getDuration()) ? BigDecimal.ZERO : new BigDecimal(logs.getAdjust()));
//                }
//                legoSetenty.setDuration(total.toString());
//                BigDecimal ratio = total.divide(new BigDecimal(workDaysExceptWeekend * 8),4,BigDecimal.ROUND_HALF_UP);
//                BigDecimal percentageOf = ratio.multiply(new BigDecimal("100")).setScale(2,BigDecimal.ROUND_HALF_UP);
//                legoSetenty.setRatios(percentageOf.toString());
//                legoSetenty.setProject_name("—");
//                personReturnVo.setAdjust("—");
//                legoSet.add(legoSetenty);
//                m.getValue().forEach(item ->{
//                    item.setUser_id(null);
//                    item.setDepartment(null);
//                    item.setCompany(null);
//                    item.setRatios(null);
//                    legoSet.add(item);
//                });
//            }
//        }
//        return JSONObject.toJSON(legoSet);
//    }
    //endregion


}
