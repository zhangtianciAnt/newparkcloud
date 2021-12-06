package com.nt.service_pfans.PFANS5000.Impl;

import com.alibaba.fastjson.JSONObject;
import com.mysql.jdbc.StringUtils;
import com.nt.dao_Auth.Role;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_Org.UserAccount;
import com.nt.dao_Org.Vo.DepartmentVo;
import com.nt.dao_Pfans.PFANS5000.LogManagement;
import com.nt.dao_Pfans.PFANS5000.LogPersonStatistics;
import com.nt.dao_Pfans.PFANS5000.Vo.LogPersonReturnVo;
import com.nt.dao_Pfans.PFANS6000.Customerinfor;
import com.nt.dao_Pfans.PFANS6000.Expatriatesinfor;
import com.nt.dao_Pfans.PFANS8000.WorkingDay;
import com.nt.service_Org.DictionaryService;
import com.nt.service_Org.OrgTreeService;
import com.nt.service_pfans.PFANS5000.LogPersonStatisticsService;
import com.nt.service_pfans.PFANS5000.mapper.LogManagementMapper;
import com.nt.service_pfans.PFANS5000.mapper.LogPersonStatisticsMapper;
import com.nt.service_pfans.PFANS6000.mapper.ExpatriatesinforMapper;
import com.nt.service_pfans.PFANS8000.mapper.WorkingDayMapper;
import com.nt.utils.LogicalException;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
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

    @Autowired
    private ExpatriatesinforMapper expatriatesinforMapper;

    @Autowired
    private TokenService tokenService;

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
        //region 外注等其他人 from
        List<LogPersonStatistics> bpList = new ArrayList<>();
        Query query = new Query();
        query.addCriteria(Criteria.where("userid").is(tokenModel.getUserId()));
        List<CustomerInfo> customerInfolist = mongoTemplate.find(query,CustomerInfo.class);
        if(customerInfolist.size()>0)
        {
            List<String> accounts = new ArrayList<>();
            List<Expatriatesinfor> expatriatesinforList = new ArrayList<>();
            expatriatesinforList = expatriatesinforMapper.selectAll();
            expatriatesinforList = expatriatesinforList.stream().filter(item->(!StringUtils.isNullOrEmpty(item.getOrgInformationcenterid()) && !StringUtils.isNullOrEmpty(item.getOrgInformationgroupid()))).collect(Collectors.toList());
            query = new Query();
            query.addCriteria(Criteria.where("_id").is(tokenModel.getUserId()));
            List<UserAccount> userAccountlist = mongoTemplate.find(query,UserAccount.class);
            String roles = "";
            for(Role role : userAccountlist.get(0).getRoles()){
                roles = roles + role.getDescription() +";";
            }
            if(roles.contains("副总经理"))
            {
                if(!StringUtils.isNullOrEmpty(customerInfolist.get(0).getUserinfo().getCenterid()))
                {
                    List<Expatriatesinfor> e1List1 = expatriatesinforList.stream().filter(item -> (item.getOrgInformationcenterid().equals(customerInfolist.get(0).getUserinfo().getCenterid()))).collect(Collectors.toList());
                    for(Expatriatesinfor e1: e1List1)
                    {
                        accounts.add(e1.getAccount());
                    }
                }
                if(customerInfolist.get(0).getUserinfo().getOtherorgs() != null && customerInfolist.get(0).getUserinfo().getOtherorgs().size()>0)
                {
                    for(CustomerInfo.OtherOrgs otherorg : customerInfolist.get(0).getUserinfo().getOtherorgs())
                    {
                        List<Expatriatesinfor> e1List1 = expatriatesinforList.stream().filter(item -> (item.getOrgInformationcenterid().equals(otherorg.getCenterid()))).collect(Collectors.toList());
                        for(Expatriatesinfor e1: e1List1)
                        {
                            accounts.add(e1.getAccount());
                        }
                    }
                }
            }
            else if(roles.contains("总经理"))
            {
                for(Expatriatesinfor e1: expatriatesinforList)
                {
                    accounts.add(e1.getAccount());
                }
            }
            else if(roles.contains("center长"))
            {
                if(!StringUtils.isNullOrEmpty(customerInfolist.get(0).getUserinfo().getCenterid()))
                {
                    List<Expatriatesinfor> e1List1 = expatriatesinforList.stream().filter(item -> (item.getOrgInformationcenterid().equals(customerInfolist.get(0).getUserinfo().getCenterid()))).collect(Collectors.toList());
                    for(Expatriatesinfor e1: e1List1)
                    {
                        accounts.add(e1.getAccount());
                    }
                }
                if(customerInfolist.get(0).getUserinfo().getOtherorgs() != null && customerInfolist.get(0).getUserinfo().getOtherorgs().size()>0)
                {
                    for(CustomerInfo.OtherOrgs otherorg : customerInfolist.get(0).getUserinfo().getOtherorgs())
                    {
                        List<Expatriatesinfor> e1List1 = expatriatesinforList.stream().filter(item -> (item.getOrgInformationcenterid().equals(otherorg.getCenterid()))).collect(Collectors.toList());
                        for(Expatriatesinfor e1: e1List1)
                        {
                            accounts.add(e1.getAccount());
                        }
                    }
                }
            }
            else if(roles.contains("GM"))
            {
                if(!StringUtils.isNullOrEmpty(customerInfolist.get(0).getUserinfo().getGroupid()))
                {
                    List<Expatriatesinfor> e1List1 = expatriatesinforList.stream().filter(item -> (item.getOrgInformationgroupid().equals(customerInfolist.get(0).getUserinfo().getGroupid()))).collect(Collectors.toList());
                    for(Expatriatesinfor e1: e1List1)
                    {
                        accounts.add(e1.getAccount());
                    }
                }
                if(customerInfolist.get(0).getUserinfo().getOtherorgs() !=null && customerInfolist.get(0).getUserinfo().getOtherorgs().size()>0)
                {
                    for(CustomerInfo.OtherOrgs otherorg : customerInfolist.get(0).getUserinfo().getOtherorgs())
                    {
                        List<Expatriatesinfor> e1List1 = expatriatesinforList.stream().filter(item -> (item.getOrgInformationgroupid().equals(otherorg.getGroupid()))).collect(Collectors.toList());
                        for(Expatriatesinfor e1: e1List1)
                        {
                            accounts.add(e1.getAccount());
                        }
                    }
                }
            }
            else
            {
                accounts.add("0");
            }
            bpList = logPersonStatisticsMapper.selectByDate(accounts,lastDayOfThisMonth.trim().substring(0,lastDayOfThisMonth.length()-3));
        }
        //endregion 外注等其他人 to
        int workDaysExceptWeekend = this.getWorkDaysExceptWeekend(sdf.parse(firstDayOfThisMonth), sdf.parse(lastDayOfThisMonth));
        List<LogPersonReturnVo> logPersonReturnVos = new ArrayList<>();
//        logPersonList = logPersonList.stream().filter(item -> (item.getDepartment().equals(groupid))).collect(Collectors.toList());
        //region scc add 添加能看到的外注人员 from
        logPersonList.addAll(bpList);
        logPersonList = logPersonList.stream().distinct().collect(Collectors.toList());
        //endregion scc add 添加能看到的外注人员 to
        if(logPersonList.size() > 0) {
        Map<String,List<LogPersonStatistics>> lptList =  logPersonList.stream().collect(Collectors.groupingBy(LogPersonStatistics :: getUser_id, HashMap::new,Collectors.toList()));
            logPersonReturnVos = new ArrayList<>();
            for (Map.Entry<String, List<LogPersonStatistics>> m : lptList.entrySet()) {
                LogPersonReturnVo personReturnVo = new LogPersonReturnVo();
                personReturnVo.setGroupname(m.getValue().get(0).getDepartment());
                personReturnVo.setCompany(m.getValue().get(0).getCompany());
                personReturnVo.setProject("—");
//                personReturnVo.setAdjust(null);//scc del
                BigDecimal total = BigDecimal.ZERO;
                BigDecimal adtotal = BigDecimal.ZERO;
                for (LogPersonStatistics logs : m.getValue()) {
                    total = total.add(StringUtils.isNullOrEmpty(logs.getDuration()) ? BigDecimal.ZERO : new BigDecimal(logs.getDuration()));//总计
                    adtotal = adtotal.add(StringUtils.isNullOrEmpty(logs.getAdjust()) ? BigDecimal.ZERO : new BigDecimal(logs.getAdjust()));//工时调整总计
                    //region scc add 获取名字 from
                    Query name = new Query();
                    name.addCriteria(Criteria.where("userid").is(logs.getUser_id()));
                    CustomerInfo customerInfo = mongoTemplate.findOne(name, CustomerInfo.class);
                    if(customerInfo != null){
                        personReturnVo.setUsername(customerInfo.getUserinfo().getCustomername());
                    }else{
                        Expatriatesinfor noteNameOutside = new Expatriatesinfor();
                        noteNameOutside.setAccount(logs.getUser_id());
                        Expatriatesinfor noteName = expatriatesinforMapper.selectOne(noteNameOutside);
                        if(noteName != null){
                            personReturnVo.setUsername(noteName.getExpname());
                        }
                    }
                    //endregion scc add 获取名字 to
                }
                personReturnVo.setGeneral(total.toString());
                BigDecimal ratio = adtotal.divide(new BigDecimal(workDaysExceptWeekend * 8),4,BigDecimal.ROUND_HALF_UP);
                BigDecimal percentageOf = ratio.multiply(new BigDecimal("100")).setScale(2,BigDecimal.ROUND_HALF_UP);
                personReturnVo.setRatio(percentageOf.toString() + "%");
                personReturnVo.setLogpersonstatistics(m.getValue());
                personReturnVo.setAdjust(adtotal.toString());//scc add
                logPersonReturnVos.add(personReturnVo);
            }
            //region scc add 按照公司排序 from
            logPersonReturnVos = logPersonReturnVos.stream().sorted(Comparator.comparing(LogPersonReturnVo::getCompany)).collect(Collectors.toList());
            //endregion scc add 按照公司排序 to
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
                    Expatriatesinfor _id = new Expatriatesinfor();
                    _id.setAccount(item.getUser_id());
                    Expatriatesinfor expatriatesinfor = expatriatesinforMapper.selectOne(_id);
                    if(expatriatesinfor != null){
                        item.setCompany(expatriatesinfor.getSuppliername());
                    }else{
                        item.setCompany("PSDCD");
                    }
                    //region scc upd 21/12/6 变更获取项目名 from
                    if(item.getProject_name().contains("_")){
                        item.setProject_name(item.getProject_name().substring(item.getProject_name().indexOf("_") + 1));
                    }else{
                        item.setProject_name(item.getProject_name());
                    }
//                    item.setProject_name(item.getProject_name().trim().split("_").length == 2 ? item.getProject_name().trim().split("_")[1] : item.getProject_name().trim().split("_")[0]);
                    //endregion scc upd 21/12/6 变更获取项目名 to
                    item.setAdjust(item.getDuration());
                    logPersonStatisticsMapper.insert(item);
                });
            }
        }else{
            return;
        }

    }
    //endregion scc add 9/14 定时任务，根据日志更新数据 to

    // region scc add 日志人别导出 from
    @Override
    public void downloadExcel(String month,HttpServletRequest request, HttpServletResponse resp) throws Exception {
        InputStream in = null;
        List<LogPersonStatistics> logPersonList = new ArrayList<>();
        TokenModel tokenModel = tokenService.getToken(request);
        LogPersonStatistics owners = new LogPersonStatistics();
        owners.setOwners(tokenModel.getOwnerList());
        List<LogPersonReturnVo> logPerson = this.getLogPerson(owners, month, tokenModel);//获取导出信息，数据同画面初始请求
        //集合判空
        try {
            //表格操作
            in = getClass().getClassLoader().getResourceAsStream("jxls_templates/rizhirenbietongji.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(in);
            this.getReport(workbook.getSheetAt(0), logPerson,workbook);//数据写入excel
            OutputStream os = resp.getOutputStream();// 取得输出流
            String fileName = "日志人别统计";
            resp.setContentType("application/vnd.ms-excel;charset=utf-8");
            resp.setHeader("Content-Disposition", "attachment;filename="
                    + new String((fileName + ".xlsx").getBytes(), "iso-8859-1"));
            workbook.write(os);
            workbook.close();
        } catch (Exception e) {
            throw new LogicalException(e.getMessage());
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    private void getReport(XSSFSheet sheet1, List<LogPersonReturnVo> logPerson,XSSFWorkbook workbook) throws LogicalException {
        XSSFCellStyle style = workbook.createCellStyle();//创建单元格样式
        style.setAlignment(HorizontalAlignment.CENTER);//水平居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        //部门键值对
        List<DepartmentVo> allDepartment = null;
        try {
            allDepartment = orgTreeService.getAllDepartment();
        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap<String, String> companyid = new HashMap<>();
        for (DepartmentVo vo : allDepartment) {
            companyid.put(vo.getDepartmentId(), vo.getDepartmentname());
        }
        //部门键值对
        if (logPerson != null && logPerson.size() > 0) {
            int i = 1;
            int rowfirst = 0;//输出每人信息开始行
            int rowlast = 0;//输出每人信息结束行
            for (LogPersonReturnVo item : logPerson) {
                XSSFRow row = sheet1.createRow(i);
                rowfirst = i;
                for (int j = 0; j < 7; j++) {
                    XSSFCell cell = row.createCell(j);
                    switch (j) {
                        case 0:cell.setCellValue(item.getUsername());cell.setCellStyle(style); break;
                        case 1:cell.setCellValue(companyid.get(item.getGroupname()));cell.setCellStyle(style); break;
                        case 2:cell.setCellValue(item.getCompany());cell.setCellStyle(style);break;
                        case 3:cell.setCellValue(item.getRatio());cell.setCellStyle(style); break;
                        case 4:cell.setCellValue(item.getProject());cell.setCellStyle(style); break;
                        case 5:cell.setCellValue(item.getGeneral());cell.setCellStyle(style); break;
                        case 6:cell.setCellValue(item.getAdjust());cell.setCellStyle(style);break;
                    }
                }
                i++;
                for (LogPersonStatistics items : item.getLogpersonstatistics()) {
                    XSSFRow row1 = sheet1.createRow(i);
                    for (int j = 0; j < 7; j++) {
                        XSSFCell cell = row1.createCell(j);
                        switch (j) {
                            case 0:cell.setCellValue("");cell.setCellStyle(style);break;
                            case 1:cell.setCellValue("");cell.setCellStyle(style);break;
                            case 2:cell.setCellValue("");cell.setCellStyle(style);break;
                            case 3:cell.setCellValue("");cell.setCellStyle(style);break;
                            case 4:cell.setCellValue(items.getProject_name());cell.setCellStyle(style);break;
                            case 5:cell.setCellValue(items.getDuration());cell.setCellStyle(style);break;
                            case 6:cell.setCellValue(items.getAdjust());cell.setCellStyle(style);break;
                        }
                    }
                    rowlast = i;
                    i++;
                }
                sheet1.addMergedRegion(new CellRangeAddress(rowfirst,rowlast,0,0));//姓名合并
                sheet1.addMergedRegion(new CellRangeAddress(rowfirst,rowlast,1,1));//部门合并
                sheet1.addMergedRegion(new CellRangeAddress(rowfirst,rowlast,2,2));//公司合并
                sheet1.addMergedRegion(new CellRangeAddress(rowfirst,rowlast,3,3));//比率合并
            }
        }
    }
    // endregion scc add 日志人别导出 to


}
