package com.nt.service_pfans.PFANS2000.Impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Pfans.PFANS2000.*;
import com.nt.dao_Pfans.PFANS8000.WorkingDay;
import com.nt.service_pfans.PFANS2000.PunchcardRecordService;
import com.nt.service_pfans.PFANS2000.mapper.*;
import com.nt.service_pfans.PFANS8000.mapper.WorkingDayMapper;
import com.nt.utils.ApiResult;
import com.nt.utils.AuthConstants;
import com.nt.utils.LogicalException;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class PunchcardRecordServiceImpl implements PunchcardRecordService {
    @Autowired
    private PunchcardRecordMapper punchcardrecordMapper;

    @Autowired
    private PunchcardRecordDetailMapper punchcardrecorddetailmapper;
    @Autowired
    private OvertimeMapper overtimeMapper;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private IrregulartimingMapper irregulartimingMapper;
    @Autowired
    private AttendanceSettingMapper attendanceSettingMapper;
    @Autowired
    private FlexibleWorkMapper flexibleworkMapper;
    @Autowired
    private AttendanceMapper attendanceMapper;
    @Autowired
    private AbNormalMapper abNormalMapper;
    @Autowired
    private WorkingDayMapper workingDayMapper;
    @Autowired
    private ReplacerestMapper replacerestMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<PunchcardRecord> list(PunchcardRecord punchcardrecord, TokenModel tokenModel) throws Exception {
        return punchcardrecordMapper.select(punchcardrecord);
    }

    @Override
    public List<PunchcardRecord> getDataList(PunchcardRecord punchcardrecord, TokenModel tokenModel) throws Exception {
        return punchcardrecordMapper.select(punchcardrecord);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public List<String> importUser(HttpServletRequest request, TokenModel tokenModel) throws Exception {
        try {
            List<PunchcardRecordDetail> listVo = new ArrayList<PunchcardRecordDetail>();
            List<String> Result = new ArrayList<String>();
            MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
            File f = null;
            f = File.createTempFile("tmp", null);
            file.transferTo(f);
            ExcelReader reader = ExcelUtil.getReader(f);
            List<List<Object>> list = reader.read();
            List<Object> model = new ArrayList<Object>();
            model.add("工号");
//            model.add("姓名");
            model.add("打卡时间");
//            model.add("区域");
            List<Object> key = list.get(0);
            for (int i = 0; i < key.size(); i++) {
                if (!key.get(i).toString().trim().equals(model.get(i))) {
                    throw new LogicalException("第" + (i + 1) + "列标题错误，应为" + model.get(i).toString());
                }
            }
            int k = 1;
            int accesscount = 0;
            int error = 0;
            for (int i = 1; i < list.size(); i++) {
                PunchcardRecordDetail punchcardrecorddetail = new PunchcardRecordDetail();
                List<Object> value = list.get(k);
                k++;
                if (value != null && !value.isEmpty()) {
                    if (value.get(0).toString().equals("")) {
                        continue;
                    }
                    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    if (value.size() > 1) {
                        String date = value.get(1).toString();
                        String date1 = value.get(1).toString();
                        date = date.substring(5, 7);
                        date1 = date1.substring(8, 10);
                        if (Integer.parseInt(date1) > 31) {
                            error = error + 1;
                            Result.add("模板第" + (k - 1) + "行的日期格式错误，请输入正确的日子，导入失败");
                            continue;
                        }
                        if (Integer.parseInt(date) > 12) {
                            error = error + 1;
                            Result.add("模板第" + (k - 1) + "行的日期格式错误，请输入正确的月份，导入失败");
                            continue;
                        }
                    }
//                    if (value.size() > 3) {
//                        if (value.get(3).toString().length() > 2) {
//                            error = error + 1;
//                            Result.add("模板第" + (k - 1) + "行的区域字段过长，请输入正确的区域，导入失败");
//                            continue;
//                        }
//                    }
                    Query query = new Query();
                    String jobnumber = value.get(0).toString();
                    query.addCriteria(Criteria.where("userinfo.jobnumber").is(jobnumber));
                    CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                    if (customerInfo != null) {
                        punchcardrecorddetail.setUser_id(customerInfo.getUserid());
                        punchcardrecorddetail.setJobnumber(value.get(0).toString());
                    }
                    if (customerInfo == null) {
                        error = error + 1;
                        Result.add("模板第" + (k - 1) + "行的工号字段没有找到，请输入正确的工号，导入失败");
                        continue;
                    }
                    punchcardrecorddetail.setCenter_id(customerInfo.getUserinfo().getCentername());
                    punchcardrecorddetail.setGroup_id(customerInfo.getUserinfo().getGroupname());
                    punchcardrecorddetail.setTeam_id(customerInfo.getUserinfo().getTeamname());
                    String Punchcardrecord_date = value.get(1).toString();
                    punchcardrecorddetail.setPunchcardrecord_date(sf.parse(Punchcardrecord_date));
//                    punchcardrecorddetail.setRegion(value.get(3).toString());
                }
                punchcardrecorddetail.preInsert(tokenModel);
                punchcardrecorddetail.setPunchcardrecorddetail_id(UUID.randomUUID().toString());
                punchcardrecorddetailmapper.insert(punchcardrecorddetail);

                listVo.add(punchcardrecorddetail);
                accesscount = accesscount + 1;
            }

            Query query_userid = new Query();
            List<CustomerInfo> customerinfo_all = mongoTemplate.findAll(CustomerInfo.class);
            String books[] = new String[customerinfo_all.size()];
            int i = 0;

            PunchcardRecord punchcardrecord = new PunchcardRecord();
            Double Worktime = 0d;
            DecimalFormat df = new DecimalFormat(".00");
            List<PunchcardRecord> punchcardrecordlist = punchcardrecorddetailmapper.getPunchCardRecord();
            for (PunchcardRecord punchcard : punchcardrecordlist) {
                if (punchcard.getWorktime() != null) {
                    Worktime = Double.valueOf(punchcard.getWorktime());
                }
                punchcardrecord.setPunchcardrecord_date(punchcard.getPunchcardrecord_date());
                punchcardrecord.setTeam_id(punchcard.getTeam_id());
                punchcardrecord.setGroup_id(punchcard.getGroup_id());
                punchcardrecord.setCenter_id(punchcard.getCenter_id());
                punchcardrecord.setUser_id(punchcard.getUser_id());
                punchcardrecord.setJobnumber(punchcard.getJobnumber());
                punchcardrecord.setWorktime("0.08");
                punchcardrecord.setAbsenteeismam("0.08");
                punchcardrecord.setPunchcardrecord_id(UUID.randomUUID().toString());
                punchcardrecord.setTime_start(punchcard.getTime_start());
                punchcardrecord.setTime_end(punchcard.getTime_end());
                punchcardrecord.setOwner(punchcard.getUser_id());
                punchcardrecord.preInsert(tokenModel);
                punchcardrecord.setRegion(punchcard.getRegion());
                punchcardrecordMapper.insert(punchcardrecord);

                //创建考勤数据
                Attendance attendance = new Attendance();
                attendance.setUser_id(punchcard.getUser_id());
                attendance.setDates(punchcard.getPunchcardrecord_date());

                attendance.setNormal("8");
                // 设置统计外出的时间
                attendance.setAbsenteeism("0");
                attendance.setCenter_id(punchcard.getCenter_id());
                attendance.setGroup_id(punchcard.getGroup_id());
                attendance.setTeam_id(punchcard.getTeam_id());
                attendance.setYears(DateUtil.format(punchcard.getPunchcardrecord_date(), "YYYY").toString());
                attendance.setMonths(DateUtil.format(punchcard.getPunchcardrecord_date(), "MM").toString());
                attendance.setAttendanceid(UUID.randomUUID().toString());
                attendance.setRecognitionstate(AuthConstants.RECOGNITION_FLAG_NO);
                attendance.setOwner(attendance.getUser_id());
                attendance.preInsert(tokenModel);
                saveAttendance(attendance, "1", tokenModel);
                books[i] = punchcard.getUser_id();
            }
            query_userid.addCriteria(Criteria.where("userid").nin(books));
            List<CustomerInfo> customerInfoList = mongoTemplate.find(query_userid, CustomerInfo.class);
            for (CustomerInfo customerInfo : customerInfoList) {
                if (customerInfo.getUserid().equals("5e78b2424e3b194874180fff")) {
                    //插入没有打卡记录的员工的考勤
                    Attendance attendance = new Attendance();
                    attendance.setAbsenteeism("8");
                    attendance.setNormal("0");
                    attendance.setAttendanceid(UUID.randomUUID().toString());
                    attendance.setCenter_id(customerInfo.getUserinfo().getCentername());
                    attendance.setGroup_id(customerInfo.getUserinfo().getGroupname());
                    attendance.setTeam_id(customerInfo.getUserinfo().getTeamname());

                    attendance.setUser_id(customerInfo.getUserid());
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date());
                    calendar.add(Calendar.DAY_OF_YEAR, -1);
                    attendance.setDates(calendar.getTime());
                    attendance.setYears(DateUtil.format(attendance.getDates(), "YYYY").toString());
                    attendance.setMonths(DateUtil.format(attendance.getDates(), "MM").toString());
                    attendance.setRecognitionstate(AuthConstants.RECOGNITION_FLAG_NO);
                    attendance.setOwner(attendance.getUser_id());
                    attendance.preInsert(tokenModel);
                    saveAttendance(attendance, "1", tokenModel);
                }
            }

            //methodAttendance_b(-1);
            Result.add("失败数：" + error);
            Result.add("成功数：" + accesscount);
            return Result;
        } catch (Exception e) {
            throw new LogicalException(e.getMessage());
        }

    }

    @Override
    public void methodAttendance_b(Calendar cal) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
        SimpleDateFormat sf1ymd = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sfy = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat sdfxx = new SimpleDateFormat("EEE MMM dd 00:00:00 zzz yyyy", Locale.US);
        DecimalFormat df = new DecimalFormat("######0.00");
        //上班时间开始
        String workshift_start = null;
        //上班时间结束
        String workshift_end = null;
        //下班时间开始
        String closingtime_start = null;
        //下班时间结束
        String closingtime_end = null;
        //午休时间开始
        String lunchbreak_start = null;
        //午休时间结束
        String lunchbreak_end = null;

        //上班时间开始
        String reserveoverTime = null;
        //实际加班時間
        String actualoverTime = null;
        //平日晚加班时间扣除
        String weekdaysovertime = null;
        //迟到/早退基本计算单位
        String lateearlyleave = null;
        //事假基本计算单位
        String compassionateleave = null;

        //加班基本计算单位
        String strovertime = null;
        //工作时间
        String workinghours = null;
        //考勤设定
        AttendanceSetting attendancesetting = new AttendanceSetting();
        List<AttendanceSetting> attendancesettinglist = attendanceSettingMapper.select(attendancesetting);
        if (attendancesettinglist.size() > 0) {
            //------------------------------查询公司考勤设定数据start-------------
            //公司考勤设定上班开始时间
            workshift_start = attendancesettinglist.get(0).getWorkshift_start().replace(":", "");
            workshift_end = attendancesettinglist.get(0).getWorkshift_end().replace(":", "");
            //公司考勤设定下班开始时间
            closingtime_start = attendancesettinglist.get(0).getClosingtime_start().replace(":", "");
            closingtime_end = attendancesettinglist.get(0).getClosingtime_end().replace(":", "");
            lunchbreak_start = attendancesettinglist.get(0).getLunchbreak_start().replace(":", "");

            lunchbreak_end = attendancesettinglist.get(0).getLunchbreak_end().replace(":", "");


            //迟到早退基本计算单位
            lateearlyleave = attendancesettinglist.get(0).getLateearlyleave();
            BigDecimal lateearlyleavehour = new BigDecimal(lateearlyleave).multiply(new BigDecimal(60 * 60 * 1000));
            lateearlyleave = String.valueOf(lateearlyleavehour.intValue());

            //事假基本计算单位
            compassionateleave = attendancesettinglist.get(0).getCompassionateleave();
            //旷工基本计算单位
            lateearlyleave = attendancesettinglist.get(0).getAbsenteeism();
            //加班基本计算单位
            strovertime = attendancesettinglist.get(0).getOvertime();
            BigDecimal strovertimehour = new BigDecimal(strovertime).multiply(new BigDecimal(60 * 60 * 1000));
            strovertime = String.valueOf(strovertimehour.intValue());

            //工作时间
            workinghours = attendancesettinglist.get(0).getWorkinghours();
            //------------------------------查询公司考勤设定数据end-------------

            //所有人一天的考勤记录
            List<Attendance> attendancelist = new ArrayList<Attendance>();

            Query query_userid = new Query();
            List<CustomerInfo> customerInfoList = mongoTemplate.findAll(CustomerInfo.class);
            for (CustomerInfo customerInfo : customerInfoList) {
                try {
                    TokenModel tokenModel = new TokenModel();
//                    if(customerInfo.getUserid().equals("5eb8ee1d29b7371b2840ba58") || customerInfo.getUserid().equals("5eb5199b29b7371e7c8bffc7"))
//                    {
                    Calendar rightNow = Calendar.getInstance();
                    //上月1号
                    Calendar calStart = Calendar.getInstance();
                    calStart.setTime(new Date());
                    calStart.add(Calendar.MONTH, -1);
                    calStart.set(Calendar.DAY_OF_MONTH, 1);

                    //本月末日
                    Calendar calend = Calendar.getInstance();
                    calend.setTime(new Date());
                    int maxCurrentMonthDay = calend.getActualMaximum(Calendar.DAY_OF_MONTH);
                    calend.set(Calendar.DAY_OF_MONTH, maxCurrentMonthDay);

                    //入职日
                    String enterdate = customerInfo.getUserinfo().getEnterday().substring(0, 10);
                    if (customerInfo.getUserinfo().getEnterday().length() >= 24) {
                        rightNow.setTime(Convert.toDate(enterdate));
                        rightNow.add(Calendar.DAY_OF_YEAR, 1);
                        enterdate = sf1ymd.format(rightNow.getTime());
                    }

                    if((customerInfo.getUserinfo().getResignation_date() == null || customerInfo.getUserinfo().getResignation_date().isEmpty())
                            && (sf1ymd.parse(sf1ymd.format(calStart.getTime())).getTime() <= (sf1ymd.parse(Convert.toStr(sf1ymd.format(Convert.toDate(enterdate)))).getTime())
                                && sf1ymd.parse(sf1ymd.format(calend.getTime())).getTime() >= (sf1ymd.parse(Convert.toStr(sf1ymd.format(Convert.toDate(enterdate)))).getTime())))
                    {
                        //俩个月内入职的人员
                        //入职日期月份与计算考勤日期月份比较
                        if (sfy.parse(Convert.toStr(sfy.format(Convert.toDate(enterdate)))).getTime() <= sfy.parse(sfy.format(cal.getTime())).getTime())
                        {
                            //入职日和计算考勤当天比较
                            if(sf1ymd.parse(sf1ymd.format(cal.getTime())).getTime() < (sf1ymd.parse(Convert.toStr(sf1ymd.format(Convert.toDate(enterdate)))).getTime()))
                            {
                                Attendance attendance = new Attendance();
                                attendance.setUser_id(customerInfo.getUserid());
                                attendance.setDates(sf1ymd.parse(sf1ymd.format(cal.getTime())));
                                List<Attendance> attendanceList = attendanceMapper.select(attendance);
                                if(attendanceList.size()==0)
                                {
                                    attendance.setCenter_id(customerInfo.getUserinfo().getCentername());
                                    attendance.setGroup_id(customerInfo.getUserinfo().getGroupname());
                                    attendance.setTeam_id(customerInfo.getUserinfo().getTeamname());
                                    attendance.setAttendanceid(UUID.randomUUID().toString());
                                    attendance.setYears(DateUtil.format(attendance.getDates(), "YYYY").toString());
                                    attendance.setMonths(DateUtil.format(attendance.getDates(), "MM").toString());
                                    attendance.setRecognitionstate("");
                                    tokenModel.setUserId(attendance.getUser_id());
                                    tokenModel.setExpireDate(new Date());
                                    saveAttendance(attendance, "1", tokenModel);
                                }
                            }
                            else
                            {
                                //查询更新一天的考勤数据
                                Attendance attendance = new Attendance();
                                attendance.setUser_id(customerInfo.getUserid());
                                attendance.setDates(sf1ymd.parse(sf1ymd.format(cal.getTime())));
                                List<Attendance> attendanceList = attendanceMapper.select(attendance);
                                if (attendanceList.size() > 0) {
                                    if (attendanceList.get(0).getRecognitionstate().equals(AuthConstants.RECOGNITION_FLAG_NO))
                                    {
                                        attendanceList.get(0).setNormal("0");
                                        attendanceList.get(0).setActual(null);
                                        attendanceList.get(0).setOrdinaryindustry(null);
                                        attendanceList.get(0).setWeekendindustry(null);
                                        attendanceList.get(0).setStatutoryresidue(null);
                                        attendanceList.get(0).setAnnualrestday(null);
                                        attendanceList.get(0).setSpecialday(null);
                                        attendanceList.get(0).setYouthday(null);
                                        attendanceList.get(0).setWomensday(null);
                                        attendanceList.get(0).setShortsickleave(null);
                                        attendanceList.get(0).setLongsickleave(null);
                                        attendanceList.get(0).setCompassionateleave(null);
                                        attendanceList.get(0).setAnnualrest(null);
                                        attendanceList.get(0).setDaixiu(null);
                                        attendanceList.get(0).setNursingleave(null);
                                        attendanceList.get(0).setWelfare(null);
                                        attendanceList.get(0).setAbsenteeism(null);
                                        attendanceList.get(0).setTshortsickleave(null);
                                        attendanceList.get(0).setTlongsickleave(null);
                                        attendanceList.get(0).setTabsenteeism(null);
                                        tokenModel.setUserId(attendanceList.get(0).getUser_id());
                                        tokenModel.setExpireDate(new Date());
                                        saveAttendance(attendanceList.get(0), "0", tokenModel);
                                        attendancelist.add(attendanceList.get(0));
                                    }
                                }
                                else {
                                    attendance.setNormal("0");
                                    attendance.setAbsenteeism("0");
                                    attendance.setCenter_id(customerInfo.getUserinfo().getCentername());
                                    attendance.setGroup_id(customerInfo.getUserinfo().getGroupname());
                                    attendance.setTeam_id(customerInfo.getUserinfo().getTeamname());
                                    attendance.setAttendanceid(UUID.randomUUID().toString());
                                    attendance.setYears(DateUtil.format(attendance.getDates(), "YYYY").toString());
                                    attendance.setMonths(DateUtil.format(attendance.getDates(), "MM").toString());
                                    attendance.setRecognitionstate(AuthConstants.RECOGNITION_FLAG_NO);
                                    tokenModel.setUserId(attendance.getUser_id());
                                    tokenModel.setExpireDate(new Date());
                                    saveAttendance(attendance, "1", tokenModel);
                                    attendancelist.add(attendance);
                                }
                            }
                        }
                    }
                    else if (customerInfo.getUserinfo().getResignation_date() == null || customerInfo.getUserinfo().getResignation_date().isEmpty()) {
                        // 俩月内在职人员
                        //查询更新一天的考勤数据
                        Attendance attendance = new Attendance();
                        attendance.setUser_id(customerInfo.getUserid());
                        attendance.setDates(sf1ymd.parse(sf1ymd.format(cal.getTime())));
                        List<Attendance> attendanceList = attendanceMapper.select(attendance);
                        if (attendanceList.size() > 0) {
                            if (attendanceList.get(0).getRecognitionstate().equals(AuthConstants.RECOGNITION_FLAG_NO))
                            {
                                attendanceList.get(0).setNormal("0");
                                attendanceList.get(0).setActual(null);
                                attendanceList.get(0).setOrdinaryindustry(null);
                                attendanceList.get(0).setWeekendindustry(null);
                                attendanceList.get(0).setStatutoryresidue(null);
                                attendanceList.get(0).setAnnualrestday(null);
                                attendanceList.get(0).setSpecialday(null);
                                attendanceList.get(0).setYouthday(null);
                                attendanceList.get(0).setWomensday(null);
                                attendanceList.get(0).setShortsickleave(null);
                                attendanceList.get(0).setLongsickleave(null);
                                attendanceList.get(0).setCompassionateleave(null);
                                attendanceList.get(0).setAnnualrest(null);
                                attendanceList.get(0).setDaixiu(null);
                                attendanceList.get(0).setNursingleave(null);
                                attendanceList.get(0).setWelfare(null);
                                attendanceList.get(0).setAbsenteeism(null);
                                attendanceList.get(0).setTshortsickleave(null);
                                attendanceList.get(0).setTlongsickleave(null);
                                attendanceList.get(0).setTabsenteeism(null);
                                tokenModel.setUserId(attendanceList.get(0).getUser_id());
                                tokenModel.setExpireDate(new Date());
                                saveAttendance(attendanceList.get(0), "0", tokenModel);
                                attendancelist.add(attendanceList.get(0));
                            }
                        } else {
                            attendance.setNormal("0");
                            attendance.setAbsenteeism("0");
                            attendance.setCenter_id(customerInfo.getUserinfo().getCentername());
                            attendance.setGroup_id(customerInfo.getUserinfo().getGroupname());
                            attendance.setTeam_id(customerInfo.getUserinfo().getTeamname());
                            attendance.setAttendanceid(UUID.randomUUID().toString());
                            attendance.setYears(DateUtil.format(attendance.getDates(), "YYYY").toString());
                            attendance.setMonths(DateUtil.format(attendance.getDates(), "MM").toString());
                            attendance.setRecognitionstate(AuthConstants.RECOGNITION_FLAG_NO);
                            tokenModel.setUserId(attendance.getUser_id());
                            tokenModel.setExpireDate(new Date());
                            saveAttendance(attendance, "1", tokenModel);
                            attendancelist.add(attendance);

                        }
                    }
                    else {
                        //俩月内离职人员
                        //离职日
                        String resignationdate = customerInfo.getUserinfo().getResignation_date().substring(0, 10);

                        if (customerInfo.getUserinfo().getResignation_date().length() >= 24) {
                            rightNow.setTime(Convert.toDate(resignationdate));
                            rightNow.add(Calendar.DAY_OF_YEAR, 1);
                            resignationdate = sf1ymd.format(rightNow.getTime());
                        }
                        //离职日期与计算考勤日期比较
                        if (sf1ymd.parse(Convert.toStr(sf1ymd.format(Convert.toDate(resignationdate)))).getTime() >= sf1ymd.parse(sf1ymd.format(cal.getTime())).getTime()) {
                            //查询更新一天的考勤数据
                            Attendance attendance = new Attendance();
                            attendance.setUser_id(customerInfo.getUserid());
                            attendance.setDates(sf1ymd.parse(sf1ymd.format(cal.getTime())));
                            List<Attendance> attendanceList = attendanceMapper.select(attendance);
                            if (attendanceList.size() > 0) {
                                if (attendanceList.get(0).getRecognitionstate().equals(AuthConstants.RECOGNITION_FLAG_NO))
                                {
                                    attendanceList.get(0).setNormal("0");
                                    attendanceList.get(0).setActual(null);
                                    attendanceList.get(0).setOrdinaryindustry(null);
                                    attendanceList.get(0).setWeekendindustry(null);
                                    attendanceList.get(0).setStatutoryresidue(null);
                                    attendanceList.get(0).setAnnualrestday(null);
                                    attendanceList.get(0).setSpecialday(null);
                                    attendanceList.get(0).setYouthday(null);
                                    attendanceList.get(0).setWomensday(null);
                                    attendanceList.get(0).setShortsickleave(null);
                                    attendanceList.get(0).setLongsickleave(null);
                                    attendanceList.get(0).setCompassionateleave(null);
                                    attendanceList.get(0).setAnnualrest(null);
                                    attendanceList.get(0).setDaixiu(null);
                                    attendanceList.get(0).setNursingleave(null);
                                    attendanceList.get(0).setWelfare(null);
                                    attendanceList.get(0).setAbsenteeism(null);
                                    attendanceList.get(0).setTshortsickleave(null);
                                    attendanceList.get(0).setTlongsickleave(null);
                                    attendanceList.get(0).setTabsenteeism(null);
                                    tokenModel.setUserId(attendanceList.get(0).getUser_id());
                                    tokenModel.setExpireDate(new Date());
                                    saveAttendance(attendanceList.get(0), "0", tokenModel);
                                    attendancelist.add(attendanceList.get(0));
                                }
                            } else {
                                attendance.setNormal("0");
                                attendance.setAbsenteeism("0");
                                attendance.setCenter_id(customerInfo.getUserinfo().getCentername());
                                attendance.setGroup_id(customerInfo.getUserinfo().getGroupname());
                                attendance.setTeam_id(customerInfo.getUserinfo().getTeamname());
                                attendance.setAttendanceid(UUID.randomUUID().toString());
                                attendance.setYears(DateUtil.format(attendance.getDates(), "YYYY").toString());
                                attendance.setMonths(DateUtil.format(attendance.getDates(), "MM").toString());
                                attendance.setRecognitionstate(AuthConstants.RECOGNITION_FLAG_NO);
                                tokenModel.setUserId(attendance.getUser_id());
                                tokenModel.setExpireDate(new Date());
                                saveAttendance(attendance, "1", tokenModel);
                                attendancelist.add(attendance);
                            }
                        }
                        else if (sf1ymd.parse(sf1ymd.format(calStart.getTime())).getTime() <= (sf1ymd.parse(Convert.toStr(sf1ymd.format(Convert.toDate(resignationdate)))).getTime()) && sf1ymd.parse(sf1ymd.format(calend.getTime())).getTime() >= (sf1ymd.parse(Convert.toStr(sf1ymd.format(Convert.toDate(resignationdate)))).getTime())) {
                            if (sfy.parse(Convert.toStr(sfy.format(Convert.toDate(resignationdate)))).getTime() >= sfy.parse(sfy.format(cal.getTime())).getTime())
                            {
                                //查询更新一天的考勤数据
                                Attendance attendance = new Attendance();
                                attendance.setUser_id(customerInfo.getUserid());
                                attendance.setDates(sf1ymd.parse(sf1ymd.format(cal.getTime())));
                                List<Attendance> attendanceList = attendanceMapper.select(attendance);
                                if (attendanceList.size() > 0) {
                                    attendanceList.get(0).setNormal(null);
                                    attendanceList.get(0).setActual(null);
                                    attendanceList.get(0).setOrdinaryindustry(null);
                                    attendanceList.get(0).setWeekendindustry(null);
                                    attendanceList.get(0).setStatutoryresidue(null);
                                    attendanceList.get(0).setAnnualrestday(null);
                                    attendanceList.get(0).setSpecialday(null);
                                    attendanceList.get(0).setYouthday(null);
                                    attendanceList.get(0).setWomensday(null);
                                    attendanceList.get(0).setShortsickleave(null);
                                    attendanceList.get(0).setLongsickleave(null);
                                    attendanceList.get(0).setCompassionateleave(null);
                                    attendanceList.get(0).setAnnualrest(null);
                                    attendanceList.get(0).setDaixiu(null);
                                    attendanceList.get(0).setNursingleave(null);
                                    attendanceList.get(0).setWelfare(null);
                                    attendanceList.get(0).setAbsenteeism(null);
                                    attendanceList.get(0).setTshortsickleave(null);
                                    attendanceList.get(0).setTlongsickleave(null);
                                    attendanceList.get(0).setTabsenteeism(null);
                                    attendanceList.get(0).setRecognitionstate("");
                                    tokenModel.setUserId(attendanceList.get(0).getUser_id());
                                    tokenModel.setExpireDate(new Date());
                                    saveAttendance(attendanceList.get(0), "0", tokenModel);
                                }
                                else {
                                    attendance.setCenter_id(customerInfo.getUserinfo().getCentername());
                                    attendance.setGroup_id(customerInfo.getUserinfo().getGroupname());
                                    attendance.setTeam_id(customerInfo.getUserinfo().getTeamname());
                                    attendance.setAttendanceid(UUID.randomUUID().toString());
                                    attendance.setYears(DateUtil.format(attendance.getDates(), "YYYY").toString());
                                    attendance.setMonths(DateUtil.format(attendance.getDates(), "MM").toString());
                                    attendance.setRecognitionstate("");
                                    tokenModel.setUserId(attendance.getUser_id());
                                    tokenModel.setExpireDate(new Date());
                                    saveAttendance(attendance, "1", tokenModel);
                                }
                            }
                            else
                            {
                                Attendance attendance = new Attendance();
                                attendance.setDates(sf1ymd.parse(sf1ymd.format(cal.getTime())));
                                attendance.setUser_id(customerInfo.getUserid());
                                attendanceMapper.delete(attendance);
                            }
                        }
                    }
//                    }
                } catch (Exception e) {
                    System.out.println("考勤数据取得异常:" + customerInfo.getUserinfo().getCustomername());
                    continue;
                }
            }

            TokenModel token = new TokenModel();
            //所有人一天是考勤数据计算
            if (attendancelist.size() > 0) {
                for (Attendance ad : attendancelist) {
                    try {
//                        if(ad.getUser_id().equals("5eb5199b29b7371e7c8bffc7") || ad.getUser_id().equals("5eb8ee1d29b7371b2840ba58"))
//                        {
                        token.setUserId(ad.getUser_id());
                        token.setExpireDate(new Date());
                        WorkingDay workDay = new WorkingDay();
                        workDay.setYears(DateUtil.format(ad.getDates(), "YYYY").toString());
                        workDay.setWorkingdate(sf1ymd.parse(sf1ymd.format(ad.getDates())));
                        List<WorkingDay> workingDaysList = workingDayMapper.select(workDay);

                        //判断当天是否是休日，青年节，妇女节，周六周日
                        if (workingDaysList.size() > 0) {
                            //振替出勤日
                            if (workingDaysList.get(0).getType().equals("4")) {
                                workinghours = "8";
                            } else {
                                workinghours = "0";
                            }
                        } else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                            workinghours = "0";
                        } else if (sf1ymd.format(ad.getDates()).equals(DateUtil.format(ad.getDates(), "YYYY").toString() + "-03-08") || sf1ymd.format(ad.getDates()).equals(DateUtil.format(ad.getDates(), "YYYY").toString() + "-05-04")) {
                            workinghours = "4";
                        } else {
                            workinghours = "8";
                        }

                        CustomerInfo customer = new CustomerInfo();
                        Query query = new Query();
                        String userid = ad.getUser_id();
                        query.addCriteria(Criteria.where("userid").is(userid));
                        CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                        //---------不定时考勤人员(非不定时考勤人员才计算)start-------
                        Irregulartiming irregulartiming = new Irregulartiming();
                        irregulartiming.setUser_id(ad.getUser_id());
                        irregulartiming.setStatus(AuthConstants.APPROVED_FLAG_YES);
                        List<Irregulartiming> irregulartiminglist = irregulartimingMapper.select(irregulartiming);
                        if (irregulartiminglist.size() == 0) {

                            //---------查询昨天大打卡记录start-------
                            PunchcardRecord punchcardRecord = new PunchcardRecord();
                            punchcardRecord.setStatus("0");
                            punchcardRecord.setUser_id(ad.getUser_id());
                            punchcardRecord.setPunchcardrecord_date(ad.getDates());
                            List<PunchcardRecord> punchcardRecordlist = punchcardrecordMapper.select(punchcardRecord);
                            //病假的判断（当天之前一年的短病假之和）
                            Attendance attendSumSick = new Attendance();
                            attendSumSick.setDates(ad.getDates());
                            attendSumSick.setUser_id(ad.getUser_id());
                            Double sumSick = attendanceMapper.selectAttenSumSick(attendSumSick);
                            // if有打卡记录  else 没有打卡记录
                            if (punchcardRecordlist.size() > 0) {
                                for (PunchcardRecord PR : punchcardRecordlist) {
                                    String time_start = sdf.format(PR.getTime_start());
                                    String time_end = sdf.format(PR.getTime_end());
                                    int i = 0; // 代休-特殊
                                    int j = 0; // 年休
                                    String nomal = "0";//申请的外出时间
                                    //---------处理昨日审批通过的加班申请start-------
                                    Overtime overtime = new Overtime();
                                    overtime.setUserid(ad.getUser_id());
                                    overtime.setRecognitionstate(AuthConstants.RECOGNITION_FLAG_NO);
                                    overtime.setReserveovertimedate(ad.getDates());
                                    List<Overtime> overtimelist = overtimeMapper.select(overtime);
                                    List<Overtime> ovList = new ArrayList<Overtime>();
                                    //筛选审批通过的数据
                                    for (Overtime ov : overtimelist) {
                                        if (ov.getStatus().equals(AuthConstants.APPROVED_FLAG_YES) || ov.getStatus().equals("5") || ov.getStatus().equals("6") || ov.getStatus().equals("7")) {
                                            ovList.add(ov);
                                        }
                                    }
                                    if (ovList.size() > 0) {
                                        for (Overtime Ot : ovList) {
                                            //予定加班時間
                                            reserveoverTime = Ot.getReserveovertime();
                                            //实际加班時間
                                            actualoverTime = Ot.getActualovertime();
                                            String overtimeHours = "0";

                                            if (sdf.parse(time_end).getTime() <= sdf.parse(closingtime_end).getTime()) {
                                                //18点之前下班
                                                //实际加班时间
                                                overtimeHours = "0";
                                            } else if (!(sdf.parse(time_end).getTime() == sdf.parse(time_start).getTime())) {
                                                //18点之后下班
                                                long result1 = sdf.parse(time_end).getTime() - sdf.parse(closingtime_end).getTime();
                                                //实际加班时间
                                                overtimeHours = String.valueOf(Double.valueOf(String.valueOf(result1)) / 60 / 60 / 1000);
                                            }

                                            if (Ot.getStatus().equals("7")) {
                                                overtimeHours = String.valueOf(Double.valueOf(actualoverTime));
                                            } else {
                                                if (Double.valueOf(overtimeHours) > Double.valueOf(reserveoverTime)) {
                                                    overtimeHours = String.valueOf(Double.valueOf(reserveoverTime));
                                                } else {
                                                    overtimeHours = String.valueOf(Double.valueOf(overtimeHours));
                                                }
                                            }

                                            if (Ot.getOvertimetype().equals("PR001005")) {
                                                //会社特别休日
                                                overtimeHours = huisheshijiworkLength(time_start, time_end, lunchbreak_start, lunchbreak_end, PR);

                                                if (sdf.parse(time_start).getTime() == sdf.parse(time_end).getTime()) {
                                                    overtimeHours = "0";
                                                }
                                                if (Ot.getStatus().equals("7")) {
                                                    overtimeHours = actualoverTime;
                                                } else {
                                                    if (Double.valueOf(overtimeHours) > Double.valueOf(reserveoverTime)) {
                                                        overtimeHours = reserveoverTime;
                                                    }
                                                }
                                            } else if (Ot.getOvertimetype().equals("PR001002") || Ot.getOvertimetype().equals("PR001003") || Ot.getOvertimetype().equals("PR001004")) {
                                                //周末加班/法定日加班/一齐年休日加班
                                                overtimeHours = timeLength(time_start, time_end, lunchbreak_start, lunchbreak_end);
                                                overtimeHours = String.valueOf(Double.valueOf(overtimeHours) - Double.valueOf(PR.getWorktime()));
                                                if (sdf.parse(time_start).getTime() == sdf.parse(time_end).getTime()) {
                                                    overtimeHours = "0";
                                                }
                                                if (Ot.getStatus().equals("7")) {
                                                    overtimeHours = actualoverTime;
                                                } else {
                                                    if (Double.valueOf(overtimeHours) > Double.valueOf(reserveoverTime)) {
                                                        overtimeHours = reserveoverTime;
                                                    }
                                                }
                                            }

                                            if (Ot.getOvertimetype().equals("PR001001")) {//平日加班

                                                if (ad.getOrdinaryindustry() != null && !ad.getOrdinaryindustry().isEmpty()) {
                                                    overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours) + Double.valueOf(ad.getOrdinaryindustry())));
                                                } else {
                                                    overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours)));
                                                }
                                                //加班时间大于等于15分
                                                if (Double.valueOf(overtimeHours) % ((Double.valueOf(strovertime)) / 60 / 60 / 1000) == 0) {
                                                    ad.setOrdinaryindustry(Double.valueOf(overtimeHours) == 0 ? null : overtimeHours);
                                                } else {
                                                    ad.setOrdinaryindustry(df.format(Math.floor(Double.valueOf(overtimeHours) / ((Double.valueOf(strovertime)) / 60 / 60 / 1000)) * ((Double.valueOf(strovertime)) / 60 / 60 / 1000)));
                                                }
                                            } else if (Ot.getOvertimetype().equals("PR001002")) {//周末加班
                                                if (ad.getWeekendindustry() != null && !ad.getWeekendindustry().isEmpty()) {
                                                    overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours) + Double.valueOf(ad.getWeekendindustry())));
                                                } else {
                                                    overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours)));
                                                }
                                                //加班时间大于等于15分
                                                if (Double.valueOf(overtimeHours) % ((Double.valueOf(strovertime)) / 60 / 60 / 1000) == 0) {
                                                    ad.setWeekendindustry(Double.valueOf(overtimeHours) == 0 ? null : overtimeHours);
                                                } else {
                                                    ad.setWeekendindustry(df.format(Math.floor(Double.valueOf(overtimeHours) / ((Double.valueOf(strovertime)) / 60 / 60 / 1000)) * ((Double.valueOf(strovertime)) / 60 / 60 / 1000)));
                                                }
                                            } else if (Ot.getOvertimetype().equals("PR001003")) {//法定日加班
                                                if (ad.getStatutoryresidue() != null && !ad.getStatutoryresidue().isEmpty()) {
                                                    overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours) + Double.valueOf(ad.getStatutoryresidue())));
                                                } else {
                                                    overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours)));
                                                }
                                                //加班时间大于等于15分
                                                if (Double.valueOf(overtimeHours) % ((Double.valueOf(strovertime)) / 60 / 60 / 1000) == 0) {
                                                    ad.setStatutoryresidue(Double.valueOf(overtimeHours) == 0 ? null : overtimeHours);
                                                } else {
                                                    ad.setStatutoryresidue(df.format(Math.floor(Double.valueOf(overtimeHours) / ((Double.valueOf(strovertime)) / 60 / 60 / 1000)) * ((Double.valueOf(strovertime)) / 60 / 60 / 1000)));
                                                }
                                            } else if (Ot.getOvertimetype().equals("PR001004")) {//一齐年休日加班
                                                if (ad.getAnnualrestday() != null && !ad.getAnnualrestday().isEmpty()) {
                                                    overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours) + Double.valueOf(ad.getAnnualrestday())));
                                                } else {
                                                    overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours)));
                                                }

                                                //加班时间大于等于15分
                                                if (Double.valueOf(overtimeHours) % ((Double.valueOf(strovertime)) / 60 / 60 / 1000) == 0) {
                                                    ad.setAnnualrestday(Double.valueOf(overtimeHours) == 0 ? null : overtimeHours);
                                                } else {
                                                    ad.setAnnualrestday(df.format(Math.floor(Double.valueOf(overtimeHours) / ((Double.valueOf(strovertime)) / 60 / 60 / 1000)) * ((Double.valueOf(strovertime)) / 60 / 60 / 1000)));
                                                }

                                            } else if (Ot.getOvertimetype().equals("PR001005")) {//会社特别休日加班
                                                if (ad.getSpecialday() != null && !ad.getSpecialday().isEmpty()) {
                                                    overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours) + Double.valueOf(ad.getSpecialday())));
                                                } else {
                                                    overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours)));
                                                }
                                                //加班时间大于等于15分
                                                if (Double.valueOf(overtimeHours) % ((Double.valueOf(strovertime)) / 60 / 60 / 1000) == 0) {
                                                    ad.setSpecialday(Double.valueOf(overtimeHours) == 0 ? null : overtimeHours);
                                                } else {
                                                    ad.setSpecialday(df.format(Math.floor(Double.valueOf(overtimeHours) / ((Double.valueOf(strovertime)) / 60 / 60 / 1000)) * ((Double.valueOf(strovertime)) / 60 / 60 / 1000)));
                                                }
                                            } else if (Ot.getOvertimetype().equals("PR001007")) {//五四青年节
                                                if (ad.getYouthday() != null && !ad.getYouthday().isEmpty()) {
                                                    overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours) + Double.valueOf(ad.getYouthday())));
                                                } else {
                                                    overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours)));
                                                }
                                                //加班时间大于等于15分
                                                if (Double.valueOf(overtimeHours) % ((Double.valueOf(strovertime)) / 60 / 60 / 1000) == 0) {
                                                    ad.setYouthday(Double.valueOf(overtimeHours) == 0 ? null : overtimeHours);
                                                } else {
                                                    ad.setYouthday(df.format(Math.floor(Double.valueOf(overtimeHours) / ((Double.valueOf(strovertime)) / 60 / 60 / 1000)) * ((Double.valueOf(strovertime)) / 60 / 60 / 1000)));
                                                }
                                            } else if (Ot.getOvertimetype().equals("PR001008")) {//妇女节
                                                if (ad.getWomensday() != null && !ad.getWomensday().isEmpty()) {
                                                    overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours) + Double.valueOf(ad.getWomensday())));
                                                } else {
                                                    overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours)));
                                                }
                                                //加班时间大于等于15分
                                                if (Double.valueOf(overtimeHours) % ((Double.valueOf(strovertime)) / 60 / 60 / 1000) == 0) {
                                                    ad.setWomensday(Double.valueOf(overtimeHours) == 0 ? null : overtimeHours);
                                                } else {
                                                    ad.setWomensday(df.format(Math.floor(Double.valueOf(overtimeHours) / ((Double.valueOf(strovertime)) / 60 / 60 / 1000)) * ((Double.valueOf(strovertime)) / 60 / 60 / 1000)));
                                                }
                                            }
                                        }
                                    }
                                    //---------处理昨日审批通过的加班申请end-------
                                    //---------处理昨日审批通过的异常考勤申请start-------
                                    AbNormal abnormal = new AbNormal();
                                    abnormal.setUser_id(ad.getUser_id());
                                    abnormal.setRecognitionstate(AuthConstants.RECOGNITION_FLAG_NO);
                                    List<AbNormal> abNormal = abNormalMapper.select(abnormal);
                                    List<AbNormal> abNormallist = new ArrayList<AbNormal>();
                                    //筛选审批通过的数据
                                    for (AbNormal an : abNormal) {
                                        if (an.getStatus().equals(AuthConstants.APPROVED_FLAG_YES) || an.getStatus().equals("5") || an.getStatus().equals("6") || an.getStatus().equals("7")) {
                                            abNormallist.add(an);
                                        }
                                    }
                                    //一条打卡记录记录的路况
                                    if (sdf.parse(time_start).getTime() == sdf.parse(time_end).getTime()) {
                                        ad.setAbsenteeism(df.format(Double.valueOf(workinghours)));
                                        ad.setNormal(null);
                                    }
                                    if (abNormallist.size() > 0) {
                                        i = 0;
                                        j = 0;
                                        for (AbNormal ab : abNormallist) {
                                            String strlengthtime = null;
                                            if (ab.getStatus().equals("7")) {
                                                ab.setRelengthtime(ab.getRelengthtime() == null || ab.getRelengthtime() == "" ? "0" : ab.getRelengthtime());
                                                strlengthtime = ab.getRelengthtime();
                                                if (Double.valueOf(strlengthtime) <= 8 && !ab.getErrortype().equals("PR013022")) {
//                                                    if(ab.getReoccurrencedate() == null )
//                                                    {
//                                                        ab.setReoccurrencedate(ab.getOccurrencedate());
//                                                    }
                                                    ab.setRefinisheddate(ab.getReoccurrencedate());
                                                }
                                                //在申请的日期范围内
                                                if (ab.getReoccurrencedate().compareTo(sf1ymd.parse(sf1ymd.format(ad.getDates()))) <= 0 && ab.getRefinisheddate().compareTo(sf1ymd.parse(sf1ymd.format(ad.getDates()))) >= 0) {
                                                    if (!(ab.getReoccurrencedate().compareTo(sf1ymd.parse(sf1ymd.format(ad.getDates()))) == 0 && ab.getRefinisheddate().compareTo(sf1ymd.parse(sf1ymd.format(ad.getDates()))) == 0)) {
                                                        strlengthtime = workinghours;
                                                        if (ab.getErrortype().equals("PR013022"))//加餐哺乳假
                                                        {
                                                            strlengthtime = "1";
                                                        }
                                                    }
                                                } else {
                                                    strlengthtime = "0";
                                                }
                                            } else {
                                                ab.setLengthtime(ab.getLengthtime() == null || ab.getLengthtime() == "" ? "0" : ab.getLengthtime());
                                                strlengthtime = ab.getLengthtime();
                                                if (Double.valueOf(strlengthtime) <= 8 && !ab.getErrortype().equals("PR013022")) {
                                                    ab.setFinisheddate(ab.getOccurrencedate());
                                                }
                                                //在申请的日期范围内
                                                if (ab.getOccurrencedate().compareTo(sf1ymd.parse(sf1ymd.format(ad.getDates()))) <= 0 && ab.getFinisheddate().compareTo(sf1ymd.parse(sf1ymd.format(ad.getDates()))) >= 0) {
                                                    if (!(ab.getOccurrencedate().compareTo(sf1ymd.parse(sf1ymd.format(ad.getDates()))) == 0 && ab.getFinisheddate().compareTo(sf1ymd.parse(sf1ymd.format(ad.getDates()))) == 0)) {
                                                        strlengthtime = workinghours;
                                                        if (ab.getErrortype().equals("PR013022"))//加餐哺乳假
                                                        {
                                                            strlengthtime = "1";
                                                        }
                                                    }
                                                } else {
                                                    strlengthtime = "0";
                                                }
                                            }

                                            if (ab.getErrortype().equals("PR013001")) {//外出

                                                nomal = String.valueOf(Double.valueOf(nomal) + Double.valueOf(strlengthtime));
                                            } else if (ab.getErrortype().equals("PR013005")) {//年休
                                                if (ab.getStatus().equals("7")) {
                                                    if (ad.getAnnualrest() != null && !ad.getAnnualrest().isEmpty()) {
                                                        if (Double.valueOf(ad.getAnnualrest()) + Double.valueOf(strlengthtime) >= Double.valueOf(workinghours)) {
                                                            strlengthtime = df.format(Double.valueOf(workinghours));
                                                        } else {
                                                            strlengthtime = String.valueOf(df.format(Double.valueOf(strlengthtime) + Double.valueOf(ad.getAnnualrest())));
                                                        }
                                                    }
                                                    if (Double.valueOf(strlengthtime) > 0) {
                                                        j = j + 1;
                                                    }
                                                    ad.setAnnualrest(strlengthtime);
                                                }
                                            } else if (ab.getErrortype().equals("PR013006")) {//代休-周末
                                                if (ab.getStatus().equals("7")) {
                                                    if (ad.getDaixiu() != null && !ad.getDaixiu().isEmpty()) {
                                                        if (Double.valueOf(ad.getDaixiu()) + Double.valueOf(strlengthtime) >= Double.valueOf(workinghours)) {
                                                            strlengthtime = df.format(Double.valueOf(workinghours));
                                                        } else {
                                                            strlengthtime = String.valueOf(df.format(Double.valueOf(strlengthtime) + Double.valueOf(ad.getDaixiu())));
                                                        }
                                                    }
                                                    //代休-周末大于等于15分
                                                    if (Double.valueOf(strlengthtime) % (Double.valueOf(lateearlyleave)) == 0) {
                                                        ad.setDaixiu(strlengthtime);
                                                    } else {
                                                        ad.setDaixiu(df.format(Math.floor(Double.valueOf(strlengthtime) / Double.valueOf(lateearlyleave)) * Double.valueOf(lateearlyleave) + Double.valueOf(lateearlyleave)));
                                                    }
                                                    ad.setDaixiu(strlengthtime);
                                                }
                                            } else if (ab.getErrortype().equals("PR013007")) {//代休-特殊
                                                if (ab.getStatus().equals("7")) {
                                                    if (ad.getDaixiu() != null && !ad.getDaixiu().isEmpty()) {
                                                        if (Double.valueOf(ad.getDaixiu()) + Double.valueOf(strlengthtime) >= Double.valueOf(workinghours)) {
                                                            strlengthtime = df.format(Double.valueOf(workinghours));
                                                        } else {
                                                            strlengthtime = String.valueOf(df.format(Double.valueOf(strlengthtime) + Double.valueOf(ad.getDaixiu())));
                                                        }
                                                    }
                                                    if (Double.valueOf(strlengthtime) > 0) {
                                                        i = i + 1;
                                                    }
                                                    ad.setDaixiu(strlengthtime);
                                                }
                                            } else if (ab.getErrortype().equals("PR013008")) {//事休
                                                if (ad.getCompassionateleave() != null && !ad.getCompassionateleave().isEmpty()) {
                                                    if (Double.valueOf(ad.getCompassionateleave()) + Double.valueOf(strlengthtime) >= Double.valueOf(workinghours)) {
                                                        strlengthtime = df.format(Double.valueOf(workinghours));
                                                    } else {
                                                        strlengthtime = String.valueOf(df.format(Double.valueOf(strlengthtime) + Double.valueOf(ad.getCompassionateleave())));
                                                    }
                                                }
                                                //事休大于等于15分
                                                if (Double.valueOf(strlengthtime) % (Double.valueOf(compassionateleave)) == 0) {
                                                    ad.setCompassionateleave(strlengthtime);
                                                } else {
                                                    ad.setCompassionateleave(df.format(Math.floor(Double.valueOf(strlengthtime) / Double.valueOf(compassionateleave)) * Double.valueOf(compassionateleave) + Double.valueOf(compassionateleave)));
                                                }
                                            } else if (ab.getErrortype().equals("PR013009")) {//短期病休
                                                if (ab.getStatus().equals("7")) {
                                                    if (ad.getShortsickleave() != null && !ad.getShortsickleave().isEmpty()) {
                                                        //前一年病假满足30天
                                                        if (sumSick + Double.valueOf(ad.getShortsickleave()) + Double.valueOf(strlengthtime) > 240 && Double.valueOf(strlengthtime) + Double.valueOf(ad.getShortsickleave()) > 0) {
                                                            ad.setShortsickleave(df.format(Double.valueOf(ad.getShortsickleave()) + sumSick + Double.valueOf(strlengthtime) - 240));
                                                            if (ad.getLongsickleave() != null && !ad.getLongsickleave().isEmpty()) {
                                                                ad.setLongsickleave(df.format(Double.valueOf(ad.getLongsickleave()) + Double.valueOf(strlengthtime) - Double.valueOf(ad.getShortsickleave())));
                                                            } else {
                                                                ad.setLongsickleave(df.format(Double.valueOf(strlengthtime) - Double.valueOf(ad.getShortsickleave())));
                                                            }

                                                        } else {
                                                            ad.setShortsickleave(df.format(Double.valueOf(strlengthtime) + Double.valueOf(ad.getShortsickleave())));
                                                        }
                                                    } else {
                                                        //前一年病假满足30天
                                                        if (sumSick + Double.valueOf(strlengthtime) > 240 && Double.valueOf(strlengthtime) > 0) {
                                                            ad.setShortsickleave(df.format(sumSick + Double.valueOf(strlengthtime) - 240));
                                                            ad.setLongsickleave(df.format(Double.valueOf(strlengthtime) - Double.valueOf(ad.getShortsickleave())));
                                                        } else {
                                                            ad.setShortsickleave(df.format(Double.valueOf(strlengthtime)));
                                                        }
                                                    }
                                                }
                                            } else if (ab.getErrortype().equals("PR013012") || ab.getErrortype().equals("PR013013")) { //産休（女） 护理假（男）
                                                if (ab.getStatus().equals("7")) {
                                                    if (ad.getNursingleave() != null && !ad.getNursingleave().isEmpty()) {
                                                        if (Double.valueOf(ad.getNursingleave()) + Double.valueOf(strlengthtime) >= Double.valueOf(workinghours)) {
                                                            strlengthtime = df.format(Double.valueOf(workinghours));
                                                        } else {
                                                            strlengthtime = String.valueOf(df.format(Double.valueOf(strlengthtime) + Double.valueOf(ad.getNursingleave())));
                                                        }
                                                    }
                                                    ad.setNursingleave(strlengthtime);
                                                }
                                            } else if (ab.getErrortype().equals("PR013014") || ab.getErrortype().equals("PR013019")) {
                                                //家长会假//其他休暇
                                                if (ad.getWelfare() != null && !ad.getWelfare().isEmpty()) {
                                                    if (Double.valueOf(ad.getWelfare()) + Double.valueOf(strlengthtime) >= Double.valueOf(workinghours)) {
                                                        strlengthtime = df.format(Double.valueOf(workinghours));
                                                    } else {
                                                        strlengthtime = String.valueOf(df.format(Double.valueOf(strlengthtime) + Double.valueOf(ad.getWelfare())));
                                                    }
                                                }
                                                if (Double.valueOf(strlengthtime) % (Double.valueOf(lateearlyleave)) == 0) {
                                                    ad.setWelfare(strlengthtime);
                                                } else {
                                                    ad.setWelfare(df.format(Math.floor(Double.valueOf(strlengthtime) / Double.valueOf(lateearlyleave)) * Double.valueOf(lateearlyleave) + Double.valueOf(lateearlyleave)));
                                                }
                                            } else if (ab.getErrortype().equals("PR013011") || ab.getErrortype().equals("PR013015")
                                                    || ab.getErrortype().equals("PR013016") || ab.getErrortype().equals("PR013017")
                                                    || ab.getErrortype().equals("PR013021") || ab.getErrortype().equals("PR013018")
                                                    || ab.getErrortype().equals("PR013020")) {
                                                //婚假 //丧假 //妊娠檢查休暇 //流产假 //计划生育手术假// 労災休暇//工伤
                                                if (ab.getStatus().equals("7")) {
                                                    if (ad.getWelfare() != null && !ad.getWelfare().isEmpty()) {
                                                        if (Double.valueOf(ad.getWelfare()) + Double.valueOf(strlengthtime) >= Double.valueOf(workinghours)) {
                                                            strlengthtime = df.format(Double.valueOf(workinghours));
                                                        } else {
                                                            strlengthtime = String.valueOf(df.format(Double.valueOf(strlengthtime) + Double.valueOf(ad.getWelfare())));
                                                        }
                                                    }
                                                    if (Double.valueOf(strlengthtime) % (Double.valueOf(lateearlyleave)) == 0) {
                                                        ad.setWelfare(strlengthtime);
                                                    } else {
                                                        ad.setWelfare(df.format(Math.floor(Double.valueOf(strlengthtime) / Double.valueOf(lateearlyleave)) * Double.valueOf(lateearlyleave) + Double.valueOf(lateearlyleave)));
                                                    }
                                                }
                                            } else if (ab.getErrortype().equals("PR013022")) {//加餐，哺乳假
                                                if (ab.getStatus().equals("7")) {
                                                    if (ad.getWelfare() != null && !ad.getWelfare().isEmpty()) {
                                                        ad.setWelfare(df.format(Double.valueOf(strlengthtime) + Double.valueOf(ad.getWelfare())));
                                                    } else {
                                                        ad.setWelfare(df.format(Double.valueOf(strlengthtime)));
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        //有打卡记录 没有申请
                                        ad.setNormal(df.format(Double.valueOf(workinghours) - Double.valueOf(PR.getWorktime() == null ? "0" : PR.getWorktime())));
                                    }
                                    //---------处理昨日审批通过的异常考勤申请end-------

                                    ad.setNormal(ad.getNormal() == null ? "0" : ad.getNormal());
                                    ad.setAnnualrest(ad.getAnnualrest() == null ? "0" : ad.getAnnualrest());
                                    ad.setDaixiu(ad.getDaixiu() == null ? "0" : ad.getDaixiu());
                                    ad.setCompassionateleave(ad.getCompassionateleave() == null ? "0" : ad.getCompassionateleave());
                                    ad.setShortsickleave(ad.getShortsickleave() == null ? "0" : ad.getShortsickleave());
                                    ad.setLongsickleave(ad.getLongsickleave() == null ? "0" : ad.getLongsickleave());
                                    ad.setNursingleave(ad.getNursingleave() == null ? "0" : ad.getNursingleave());
                                    ad.setWelfare(ad.getWelfare() == null ? "0" : ad.getWelfare());
                                    ad.setAbsenteeism(ad.getAbsenteeism() == null ? "0" : ad.getAbsenteeism());

                                    //申请假期的时间
                                    String leavetime = df.format(Double.valueOf(ad.getShortsickleave())
                                            + Double.valueOf(ad.getLongsickleave()) + Double.valueOf(ad.getCompassionateleave()) + Double.valueOf(ad.getAnnualrest())
                                            + Double.valueOf(ad.getDaixiu()) + Double.valueOf(ad.getNursingleave()) + Double.valueOf(ad.getWelfare()));
                                    //青年节，妇女节换代休
                                    if (workinghours.equals("4")) {
                                        String time_end_temp = time_end;
                                        String time_start_temp = time_start;
                                        if (Double.valueOf(sdf.parse(time_end).getTime()) >= Double.valueOf(sdf.parse(closingtime_end).getTime())) {
                                            time_end_temp = closingtime_end;
                                        }
                                        if (Double.valueOf(sdf.parse(time_start).getTime()) <= Double.valueOf(sdf.parse(workshift_start).getTime())) {
                                            time_start_temp = workshift_start;
                                        }

                                        String shijiworkHoursAM = "0";//上午上班时间
                                        String shijiworkHoursPM = "0";//下午上班时间

                                        if (sdf.parse(time_end_temp).getTime() <= sdf.parse(lunchbreak_start).getTime()) {
                                            long result1 = sdf.parse(time_end_temp).getTime() - sdf.parse(time_start_temp).getTime();
                                            shijiworkHoursAM = String.valueOf((Double.valueOf(String.valueOf(result1)) / 60 / 60 / 1000) - Double.valueOf(PR.getAbsenteeismam()));
                                        } else if (sdf.parse(time_start_temp).getTime() >= sdf.parse(lunchbreak_end).getTime()) {
                                            long result1 = sdf.parse(time_end_temp).getTime() - sdf.parse(time_start_temp).getTime();
                                            shijiworkHoursPM = String.valueOf((Double.valueOf(String.valueOf(result1)) / 60 / 60 / 1000) - (Double.valueOf(PR.getWorktime()) - Double.valueOf(PR.getAbsenteeismam())));
                                        } else if (sdf.parse(time_start_temp).getTime() <= sdf.parse(lunchbreak_start).getTime() && sdf.parse(time_end_temp).getTime() < sdf.parse(lunchbreak_end).getTime()) {
                                            long result1 = sdf.parse(lunchbreak_start).getTime() - sdf.parse(time_start_temp).getTime();
                                            shijiworkHoursAM = String.valueOf((Double.valueOf(String.valueOf(result1)) / 60 / 60 / 1000) - Double.valueOf(PR.getAbsenteeismam()));
                                        } else if (sdf.parse(time_start_temp).getTime() > sdf.parse(lunchbreak_start).getTime() && sdf.parse(time_end_temp).getTime() >= sdf.parse(lunchbreak_end).getTime()) {
                                            long result1 = sdf.parse(time_end_temp).getTime() - sdf.parse(lunchbreak_end).getTime();
                                            shijiworkHoursPM = String.valueOf((Double.valueOf(String.valueOf(result1)) / 60 / 60 / 1000) - (Double.valueOf(PR.getWorktime()) - Double.valueOf(PR.getAbsenteeismam())));
                                        } else {
                                            //上午上班时间
                                            long result1 = sdf.parse(lunchbreak_start).getTime() - sdf.parse(time_start_temp).getTime();
                                            shijiworkHoursAM = String.valueOf(Double.valueOf(result1) / 60 / 60 / 1000 - Double.valueOf(PR.getAbsenteeismam()));
                                            //下午上班时间
                                            long result2 = sdf.parse(time_end_temp).getTime() - sdf.parse(lunchbreak_end).getTime();
                                            shijiworkHoursPM = String.valueOf(Double.valueOf(result2) / 60 / 60 / 1000 - (Double.valueOf(PR.getWorktime()) - Double.valueOf(PR.getAbsenteeismam())));
                                        }

                                        if (Double.valueOf(sdf.parse(time_start_temp).getTime()) >= Double.valueOf(sdf.parse(time_end_temp).getTime())) {
                                            shijiworkHoursAM = "0";
                                            shijiworkHoursPM = "0";
                                        }

                                        if (sdf.parse(time_end).getTime() == sdf.parse(time_start).getTime()) {
                                            shijiworkHoursAM = "0";
                                            shijiworkHoursPM = "0";
                                        }

                                        if (Double.valueOf(shijiworkHoursAM) >= 4 && Double.valueOf(shijiworkHoursPM) >= 4) {
                                            //上午>=4 && 下午>=4
                                            ad.setNormal(df.format(Double.valueOf(workinghours)));
                                            if (ad.getYouthday() != null && !ad.getYouthday().isEmpty()) {
                                                ad.setYouthday(df.format(Double.valueOf(workinghours)));
                                            }
                                            if (ad.getWomensday() != null && !ad.getWomensday().isEmpty()) {
                                                ad.setWomensday(df.format(Double.valueOf(workinghours)));
                                            }
                                        } else if (Double.valueOf(shijiworkHoursAM) >= 4 && Double.valueOf(shijiworkHoursPM) < 4) {
                                            //上午>=4 && 下午<4
                                            ad.setNormal(df.format(Double.valueOf(workinghours)));
                                            if (ad.getYouthday() != null && !ad.getYouthday().isEmpty()) {
                                                //下午上班时间 + 申请的假期 + 申请的因公外出 >= 4
                                                if (Double.valueOf(shijiworkHoursPM) + Double.valueOf(leavetime) + Double.valueOf(nomal) >= 4) {
                                                    ad.setYouthday(df.format(Double.valueOf(workinghours)));
                                                } else {
                                                    ad.setYouthday(null);
                                                }
                                            }
                                            if (ad.getWomensday() != null && !ad.getWomensday().isEmpty()) {
                                                //下午上班时间 + 申请的假期 + 申请的因公外出 >= 4
                                                if (Double.valueOf(shijiworkHoursPM) + Double.valueOf(leavetime) + Double.valueOf(nomal) >= 4) {
                                                    ad.setWomensday(df.format(Double.valueOf(workinghours)));
                                                } else {
                                                    ad.setWomensday(null);
                                                }
                                            }
                                        } else if (Double.valueOf(shijiworkHoursAM) < 4 && Double.valueOf(shijiworkHoursPM) >= 4) {
                                            //上午<4 && 下午>=4
                                            ad.setNormal(df.format(Double.valueOf(workinghours)));
                                            if (ad.getYouthday() != null && !ad.getYouthday().isEmpty()) {
                                                //上午上班时间 + 申请的假期 + 申请的因公外出 >= 4
                                                if (Double.valueOf(shijiworkHoursAM) + Double.valueOf(leavetime) + Double.valueOf(nomal) >= 4) {
                                                    ad.setYouthday(df.format(Double.valueOf(workinghours)));
                                                } else {
                                                    ad.setYouthday(null);
                                                }
                                            }
                                            if (ad.getWomensday() != null && !ad.getWomensday().isEmpty()) {
                                                //上午上班时间 + 申请的假期 + 申请的因公外出 >= 4
                                                if (Double.valueOf(shijiworkHoursAM) + Double.valueOf(leavetime) + Double.valueOf(nomal) >= 4) {
                                                    ad.setWomensday(df.format(Double.valueOf(workinghours)));
                                                } else {
                                                    ad.setWomensday(null);
                                                }
                                            }

                                        } else {
                                            //上午<4 && 下午<4
                                            if (Double.valueOf(shijiworkHoursAM) + Double.valueOf(shijiworkHoursPM) + Double.valueOf(leavetime) + Double.valueOf(nomal) >= 8) {
                                                ad.setNormal(df.format(Double.valueOf(workinghours)));
                                                if (ad.getYouthday() != null && !ad.getYouthday().isEmpty()) {
                                                    ad.setYouthday(df.format(Double.valueOf(workinghours)));
                                                }
                                                if (ad.getWomensday() != null && !ad.getWomensday().isEmpty()) {
                                                    ad.setWomensday(df.format(Double.valueOf(workinghours)));
                                                }
                                            } else {
                                                String shijiworkHoursMax = "0";
                                                shijiworkHoursMax = Double.valueOf(shijiworkHoursAM) > Double.valueOf(shijiworkHoursPM) ? shijiworkHoursAM : shijiworkHoursPM;
                                                //取上午下午最大上班时间 + 申请的假期 + 申请的因公外出 >= 4
                                                if (Double.valueOf(shijiworkHoursMax) + Double.valueOf(leavetime) + Double.valueOf(nomal) >= 4) {
                                                    ad.setNormal(df.format(Double.valueOf(workinghours)));
                                                    ad.setYouthday(null);
                                                    ad.setWomensday(null);
                                                } else {
                                                    ad.setNormal(df.format(Double.valueOf(shijiworkHoursMax) + Double.valueOf(nomal)));
                                                    ad.setNormal(df.format(Math.floor(Double.valueOf(ad.getNormal()) / Double.valueOf(lateearlyleave)) * Double.valueOf(lateearlyleave)));
                                                    ad.setAbsenteeism(df.format(Double.valueOf(workinghours) - Double.valueOf(leavetime) - Double.valueOf(ad.getNormal())));
                                                    ad.setYouthday(null);
                                                    ad.setWomensday(null);
                                                }
                                            }
                                        }
                                    } else if (workinghours.equals("8")) {
                                        //申请了年休，代休
                                        if (i > 0 || j > 0) {
                                            String time_end_temp = time_end;
                                            String time_start_temp = time_start;
                                            if (Double.valueOf(sdf.parse(time_end).getTime()) >= Double.valueOf(sdf.parse(closingtime_end).getTime())) {
                                                time_end_temp = closingtime_end;
                                            }
                                            if (Double.valueOf(sdf.parse(time_start).getTime()) <= Double.valueOf(sdf.parse(workshift_start).getTime())) {
                                                time_start_temp = workshift_start;
                                            }
                                            String shijiworkHours = shijiworkLength(time_start_temp, time_end_temp, lunchbreak_start, lunchbreak_end, PR);
                                            shijiworkHours = df.format(Math.floor(Double.valueOf(shijiworkHours) / ((Double.valueOf(strovertime)) / 60 / 60 / 1000)) * ((Double.valueOf(strovertime)) / 60 / 60 / 1000));

                                            if (Double.valueOf(sdf.parse(time_start_temp).getTime()) >= Double.valueOf(sdf.parse(time_end_temp).getTime())) {
                                                shijiworkHours = "0";
                                            }

                                            if (sdf.parse(time_end).getTime() == sdf.parse(time_start).getTime()) {
                                                shijiworkHours = "0";
                                            }
                                            //申请代休，没有申请年休
                                            if (i > 0 && j == 0) {
                                                leavetime = String.valueOf(Double.valueOf(ad.getShortsickleave()) + Double.valueOf(ad.getLongsickleave())
                                                        + Double.valueOf(ad.getCompassionateleave()) + Double.valueOf(ad.getDaixiu()) + Double.valueOf(ad.getNursingleave()) + Double.valueOf(ad.getWelfare()));
                                                if (Double.valueOf(leavetime) >= 8) {
                                                    ad.setNormal("0");
                                                    ad.setAbsenteeism("0");
                                                } else {
                                                    if (Double.valueOf(shijiworkHours) + Double.valueOf(nomal) >= 4) {
                                                        ad.setNormal(df.format(Double.valueOf(workinghours) - Double.valueOf(leavetime)));
                                                        ad.setAbsenteeism("0");
                                                    } else {
                                                        ad.setNormal(df.format(Double.valueOf(shijiworkHours) + Double.valueOf(nomal)));
                                                        ad.setNormal(df.format(Math.floor(Double.valueOf(ad.getNormal()) / Double.valueOf(lateearlyleave)) * Double.valueOf(lateearlyleave)));
                                                        ad.setAbsenteeism(df.format(Double.valueOf(workinghours) - Double.valueOf(leavetime) - Double.valueOf(ad.getNormal())));
                                                    }
                                                }

                                            }
                                            //没有申请代休周末，申请年休
                                            if (j > 0 && i == 0) {

                                                leavetime = String.valueOf(Double.valueOf(ad.getShortsickleave()) + Double.valueOf(ad.getLongsickleave())
                                                        + Double.valueOf(ad.getCompassionateleave()) + Double.valueOf(ad.getDaixiu()) + Double.valueOf(ad.getAnnualrest()) + Double.valueOf(ad.getNursingleave()) + Double.valueOf(ad.getWelfare()));
                                                if (Double.valueOf(leavetime) >= 8) {
                                                    ad.setNormal("0");
                                                    ad.setAbsenteeism("0");
                                                } else {
                                                    if (Double.valueOf(shijiworkHours) + Double.valueOf(nomal) >= 4) {
                                                        ad.setNormal(df.format(Double.valueOf(workinghours) - Double.valueOf(leavetime)));
                                                        ad.setAbsenteeism("0");
                                                    } else {
                                                        ad.setNormal(df.format(Double.valueOf(shijiworkHours) + Double.valueOf(nomal)));
                                                        ad.setNormal(df.format(Math.floor(Double.valueOf(ad.getNormal()) / Double.valueOf(lateearlyleave)) * Double.valueOf(lateearlyleave)));
                                                        ad.setAbsenteeism(df.format(Double.valueOf(workinghours) - Double.valueOf(leavetime) - Double.valueOf(ad.getNormal())));
                                                    }
                                                }
                                            }
                                            if (i > 0 && j > 0) {
                                                leavetime = workinghours;
                                                ad.setNormal("0");
                                                ad.setAbsenteeism("0");
                                            }
                                        }

                                        //没有申请年休和代休特殊
                                        if (i == 0 && j == 0) {
                                            //正常的打卡记录
                                            String time_end_temp = time_end;
                                            String time_start_temp = time_start;
                                            if (Double.valueOf(sdf.parse(time_end).getTime()) >= Double.valueOf(sdf.parse(closingtime_end).getTime())) {
                                                time_end_temp = closingtime_end;
                                            }
                                            if (Double.valueOf(sdf.parse(time_start).getTime()) <= Double.valueOf(sdf.parse(workshift_start).getTime())) {
                                                time_start_temp = workshift_start;
                                            }

                                            Double resultwork = Double.valueOf(timeLength(time_start_temp, time_end_temp, lunchbreak_start, lunchbreak_end));

                                            if (Double.valueOf(sdf.parse(time_start_temp).getTime()) >= Double.valueOf(sdf.parse(time_end_temp).getTime())) {
                                                resultwork = 0.0;
                                            }

                                            if (sdf.parse(time_end).getTime() == sdf.parse(time_start).getTime()) {
                                                resultwork = 0.0;
                                            }

                                            if (Double.valueOf(sdf.parse(time_start).getTime()) <= Double.valueOf(sdf.parse(workshift_end).getTime())
                                                    && Double.valueOf(sdf.parse(time_end).getTime()) >= Double.valueOf(sdf.parse(closingtime_start).getTime())
                                                    && resultwork + Double.valueOf(nomal) - Double.valueOf(PR.getWorktime() == null ? "0" : PR.getWorktime()) >= 8) {
                                                ad.setNormal(df.format(Double.valueOf(workinghours) - Double.valueOf(leavetime)));
                                                if (Double.valueOf(workinghours) == 8) {
                                                    ad.setAbsenteeism("0");
                                                }
                                            } else //异常打卡的情况
                                            {
                                                if (workinghours.equals("8")) {

                                                    //除去申请以外的出勤时间
                                                    String shuyutime = df.format(Double.valueOf(workinghours) - Double.valueOf(leavetime));

                                                    if (resultwork + Double.valueOf(nomal) - Double.valueOf(PR.getWorktime()) >= Double.valueOf(shuyutime)) {
                                                        ad.setNormal(df.format(Math.floor(Double.valueOf(shuyutime) / Double.valueOf(lateearlyleave)) * Double.valueOf(lateearlyleave)));
                                                        ad.setAbsenteeism(df.format(Double.valueOf(workinghours) - Double.valueOf(ad.getNormal()) - Double.valueOf(leavetime)));
                                                    } else {
                                                        if (resultwork + Double.valueOf(nomal) - Double.valueOf(PR.getWorktime()) > 0) {
                                                            //欠勤 = 8-（实际出勤(出勤 +申请因公外出异常 - 外出时间 + 申请的其他休假)）
                                                            ad.setAbsenteeism(df.format(Double.valueOf(workinghours) -
                                                                    (Double.valueOf(resultwork) + Double.valueOf(nomal) - Double.valueOf(PR.getWorktime()) + Double.valueOf(leavetime))));
                                                            if (!(Double.valueOf(ad.getAbsenteeism()) % (Double.valueOf(lateearlyleave)) == 0)) {
                                                                ad.setAbsenteeism(df.format(Math.floor(Double.valueOf(ad.getAbsenteeism()) / Double.valueOf(lateearlyleave)) * Double.valueOf(lateearlyleave) + Double.valueOf(lateearlyleave)));

                                                            }
                                                            if (Double.valueOf(ad.getAbsenteeism()) > 0) {
                                                                ad.setNormal(df.format(Double.valueOf(workinghours) - Double.valueOf(ad.getAbsenteeism()) - Double.valueOf(leavetime)));
                                                            } else {
                                                                ad.setNormal(df.format(Double.valueOf(workinghours) - Double.valueOf(leavetime)));
                                                            }
                                                        } else {
                                                            ad.setNormal(df.format(Double.valueOf(nomal)));
                                                            ad.setNormal(df.format(Math.floor(Double.valueOf(ad.getNormal()) / Double.valueOf(lateearlyleave)) * Double.valueOf(lateearlyleave)));
                                                            ad.setAbsenteeism(df.format(Double.valueOf(workinghours) - Double.valueOf(leavetime) - Double.valueOf(ad.getNormal())));
                                                        }
                                                    }

                                                }
                                            }
                                        }
                                    } else {
                                        if (ad.getWeekendindustry() != null && !ad.getWeekendindustry().isEmpty()) {
                                            if (Double.valueOf(ad.getWeekendindustry()) >= 8) {
                                                //换代休1天
                                                //String duration = "8";
                                                //操作代休表
                                                //insertReplace(ad,tokenModel,"1",duration);
                                                //周末加班除去换代休的时间
                                                ad.setWeekendindustry(df.format(Double.valueOf(ad.getWeekendindustry()) - 8));
                                            }
                                        } else if (ad.getSpecialday() != null && !ad.getSpecialday().isEmpty()) {
                                            String duration = null;
                                            if (Double.valueOf(ad.getSpecialday()) >= 8) {
                                                //特别休日
                                                //换代休1天
                                                ad.setSpecialday(df.format(8));

                                            } else if (Double.valueOf(ad.getSpecialday()) >= 4 && Double.valueOf(ad.getSpecialday()) < 8) {
                                                //特别休日
                                                //换代休0.5天
                                                ad.setSpecialday(df.format(4));
                                            } else {
                                                ad.setSpecialday(null);
                                            }
                                        }
                                    }

                                    ad.setNormal(ad.getNormal() == null ? null : (Double.valueOf(ad.getNormal()) <= 0 ? null : df.format(Double.valueOf(ad.getNormal()))));
                                    ad.setAnnualrest(Double.valueOf(ad.getAnnualrest()) <= 0 ? null : df.format(Double.valueOf(ad.getAnnualrest())));
                                    ad.setDaixiu(Double.valueOf(ad.getDaixiu()) <= 0 ? null : df.format(Double.valueOf(ad.getDaixiu())));
                                    ad.setCompassionateleave(Double.valueOf(ad.getCompassionateleave()) <= 0 ? null : df.format(Double.valueOf(ad.getCompassionateleave())));
                                    ad.setShortsickleave(Double.valueOf(ad.getShortsickleave()) <= 0 ? null : df.format(Double.valueOf(ad.getShortsickleave())));
                                    ad.setLongsickleave(Double.valueOf(ad.getLongsickleave()) <= 0 ? null : df.format(Double.valueOf(ad.getLongsickleave())));
                                    ad.setNursingleave(Double.valueOf(ad.getNursingleave()) <= 0 ? null : df.format(Double.valueOf(ad.getNursingleave())));
                                    ad.setWelfare(Double.valueOf(ad.getWelfare()) <= 0 ? null : df.format(Double.valueOf(ad.getWelfare())));
                                    ad.setAbsenteeism(Double.valueOf(ad.getAbsenteeism()) <= 0 ? null : df.format(Double.valueOf(ad.getAbsenteeism())));

                                    //更新考勤表
                                    if (customerInfo.getUserinfo().getEnddate() == null || customerInfo.getUserinfo().getEnddate().isEmpty()) {
                                        ad.setTshortsickleave(ad.getShortsickleave());
                                        ad.setTlongsickleave(ad.getLongsickleave());
                                        ad.setTabsenteeism(ad.getAbsenteeism());
                                        ad.setShortsickleave(null);
                                        ad.setLongsickleave(null);
                                        ad.setAbsenteeism(null);
                                    } else {
                                        String enddate = customerInfo.getUserinfo().getEnddate().substring(0, 10);
                                        if (sf1ymd.parse(Convert.toStr(sf1ymd.format(Convert.toDate(enddate)))).getTime() >= ad.getDates().getTime()) {
                                            ad.setTshortsickleave(ad.getShortsickleave());
                                            ad.setTlongsickleave(ad.getLongsickleave());
                                            ad.setTabsenteeism(ad.getAbsenteeism());
                                            ad.setShortsickleave(null);
                                            ad.setLongsickleave(null);
                                            ad.setAbsenteeism(null);
                                        }
                                    }

                                    if (workinghours.equals("0")) {
                                        ad.setNormal(null);
                                        ad.setAbsenteeism(null);
                                        ad.setShortsickleave(null);
                                        ad.setLongsickleave(null);
                                        ad.setCompassionateleave(null);
                                        ad.setAnnualrest(null);
                                        ad.setDaixiu(null);
                                        ad.setNursingleave(null);
                                        ad.setWelfare(null);
                                        ad.setTshortsickleave(null);
                                        ad.setTlongsickleave(null);
                                        ad.setTabsenteeism(null);
                                    }

                                    //员工填写日志对应的实际工作时间
                                    ad.setOutgoinghours(getOutgoinghours(lunchbreak_start, lunchbreak_end, PR, nomal));
                                    PR.setEffectiveduration(ad.getOutgoinghours());
                                    PR.setModifyby(ad.getUser_id());
                                    PR.setModifyon(new Date());
                                    punchcardrecordMapper.updateByPrimaryKey(PR);
                                    saveAttendance(ad, "0", token);
                                }
                            } else {
                                //---------处理昨日审批通过的加班申请start-------
                                Overtime overtime = new Overtime();
                                overtime.setUserid(ad.getUser_id());
                                overtime.setRecognitionstate(AuthConstants.RECOGNITION_FLAG_NO);
                                overtime.setReserveovertimedate(ad.getDates());
                                overtime.setStatus("7");
                                List<Overtime> ovList = overtimeMapper.select(overtime);

                                if (ovList.size() > 0) {
                                    for (Overtime Ot : ovList) {
                                        //申请的加班時間
                                        actualoverTime = Ot.getActualovertime();
                                        String overtimeHours = null;
                                        overtimeHours = String.valueOf(Double.valueOf(actualoverTime));

                                        if (Ot.getOvertimetype().equals("PR001001")) {//平日加班

                                            if (ad.getOrdinaryindustry() != null && !ad.getOrdinaryindustry().isEmpty()) {
                                                overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours) + Double.valueOf(ad.getOrdinaryindustry())));
                                            } else {
                                                overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours)));
                                            }
                                            //加班时间大于等于15分
                                            if (Double.valueOf(overtimeHours) % ((Double.valueOf(strovertime)) / 60 / 60 / 1000) == 0) {
                                                ad.setOrdinaryindustry(Double.valueOf(overtimeHours) == 0 ? null : overtimeHours);
                                            } else {
                                                ad.setOrdinaryindustry(df.format(Math.floor(Double.valueOf(overtimeHours) / ((Double.valueOf(strovertime)) / 60 / 60 / 1000)) * ((Double.valueOf(strovertime)) / 60 / 60 / 1000)));
                                            }
                                        } else if (Ot.getOvertimetype().equals("PR001002")) {//周末加班
                                            if (ad.getWeekendindustry() != null && !ad.getWeekendindustry().isEmpty()) {
                                                overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours) + Double.valueOf(ad.getWeekendindustry())));
                                            } else {
                                                overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours)));
                                            }
                                            //加班时间大于等于15分
                                            if (Double.valueOf(overtimeHours) % ((Double.valueOf(strovertime)) / 60 / 60 / 1000) == 0) {
                                                ad.setWeekendindustry(Double.valueOf(overtimeHours) == 0 ? null : overtimeHours);
                                            } else {
                                                ad.setWeekendindustry(df.format(Math.floor(Double.valueOf(overtimeHours) / ((Double.valueOf(strovertime)) / 60 / 60 / 1000)) * ((Double.valueOf(strovertime)) / 60 / 60 / 1000)));
                                            }
                                        } else if (Ot.getOvertimetype().equals("PR001003")) {//法定日加班
                                            if (ad.getStatutoryresidue() != null && !ad.getStatutoryresidue().isEmpty()) {
                                                overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours) + Double.valueOf(ad.getStatutoryresidue())));
                                            } else {
                                                overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours)));
                                            }
                                            //加班时间大于等于15分
                                            if (Double.valueOf(overtimeHours) % ((Double.valueOf(strovertime)) / 60 / 60 / 1000) == 0) {
                                                ad.setStatutoryresidue(Double.valueOf(overtimeHours) == 0 ? null : overtimeHours);
                                            } else {
                                                ad.setStatutoryresidue(df.format(Math.floor(Double.valueOf(overtimeHours) / ((Double.valueOf(strovertime)) / 60 / 60 / 1000)) * ((Double.valueOf(strovertime)) / 60 / 60 / 1000)));
                                            }
                                        } else if (Ot.getOvertimetype().equals("PR001004")) {//一齐年休日加班
                                            if (ad.getAnnualrestday() != null && !ad.getAnnualrestday().isEmpty()) {
                                                overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours) + Double.valueOf(ad.getAnnualrestday())));
                                            } else {
                                                overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours)));
                                            }

                                            //加班时间大于等于15分
                                            if (Double.valueOf(overtimeHours) % ((Double.valueOf(strovertime)) / 60 / 60 / 1000) == 0) {
                                                ad.setAnnualrestday(Double.valueOf(overtimeHours) == 0 ? null : overtimeHours);
                                            } else {
                                                ad.setAnnualrestday(df.format(Math.floor(Double.valueOf(overtimeHours) / ((Double.valueOf(strovertime)) / 60 / 60 / 1000)) * ((Double.valueOf(strovertime)) / 60 / 60 / 1000)));
                                            }

                                        } else if (Ot.getOvertimetype().equals("PR001005")) {//会社特别休日加班
                                            if (ad.getSpecialday() != null && !ad.getSpecialday().isEmpty()) {
                                                overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours) + Double.valueOf(ad.getSpecialday())));
                                            } else {
                                                overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours)));
                                            }
                                            //加班时间大于等于15分
                                            if (Double.valueOf(overtimeHours) % ((Double.valueOf(strovertime)) / 60 / 60 / 1000) == 0) {
                                                ad.setSpecialday(Double.valueOf(overtimeHours) == 0 ? null : overtimeHours);
                                            } else {
                                                ad.setSpecialday(df.format(Math.floor(Double.valueOf(overtimeHours) / ((Double.valueOf(strovertime)) / 60 / 60 / 1000)) * ((Double.valueOf(strovertime)) / 60 / 60 / 1000)));
                                            }
                                        } else if (Ot.getOvertimetype().equals("PR001007")) {//五四青年节
                                            if (ad.getYouthday() != null && !ad.getYouthday().isEmpty()) {
                                                overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours) + Double.valueOf(ad.getYouthday())));
                                            } else {
                                                overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours)));
                                            }
                                            //加班时间大于等于15分
                                            if (Double.valueOf(overtimeHours) % ((Double.valueOf(strovertime)) / 60 / 60 / 1000) == 0) {
                                                ad.setYouthday(Double.valueOf(overtimeHours) == 0 ? null : overtimeHours);
                                            } else {
                                                ad.setYouthday(df.format(Math.floor(Double.valueOf(overtimeHours) / ((Double.valueOf(strovertime)) / 60 / 60 / 1000)) * ((Double.valueOf(strovertime)) / 60 / 60 / 1000)));
                                            }
                                        } else if (Ot.getOvertimetype().equals("PR001008")) {//妇女节
                                            if (ad.getWomensday() != null && !ad.getWomensday().isEmpty()) {
                                                overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours) + Double.valueOf(ad.getWomensday())));
                                            } else {
                                                overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours)));
                                            }
                                            //加班时间大于等于15分
                                            if (Double.valueOf(overtimeHours) % ((Double.valueOf(strovertime)) / 60 / 60 / 1000) == 0) {
                                                ad.setWomensday(Double.valueOf(overtimeHours) == 0 ? null : overtimeHours);
                                            } else {
                                                ad.setWomensday(df.format(Math.floor(Double.valueOf(overtimeHours) / ((Double.valueOf(strovertime)) / 60 / 60 / 1000)) * ((Double.valueOf(strovertime)) / 60 / 60 / 1000)));
                                            }
                                        }
                                    }
                                    if (workinghours.equals("0")) {
                                        //周末加班
                                        if (ad.getWeekendindustry() != null && !ad.getWeekendindustry().isEmpty()) {
                                            if (Double.valueOf(ad.getWeekendindustry()) >= 8) {
                                                ad.setWeekendindustry(df.format(Double.valueOf(ad.getWeekendindustry()) - 8));
                                            }
                                        }
                                    }
                                }
                                //---------处理昨日审批通过的加班申请end-------
                                //---------处理昨日审批通过的异常考勤申请start-------
                                AbNormal abnormal = new AbNormal();
                                abnormal.setUser_id(ad.getUser_id());
                                abnormal.setRecognitionstate(AuthConstants.RECOGNITION_FLAG_NO);
                                List<AbNormal> abNormal = abNormalMapper.select(abnormal);
                                List<AbNormal> abNormallist = new ArrayList<AbNormal>();
                                //筛选审批通过的数据
                                for (AbNormal an : abNormal) {
                                    if (an.getStatus().equals(AuthConstants.APPROVED_FLAG_YES) || an.getStatus().equals("5") || an.getStatus().equals("6") || an.getStatus().equals("7")) {
                                        abNormallist.add(an);
                                    }
                                }
                                String nomal = "0";
                                //是否是工作日，青年节，妇女节，休日
                                ad.setAbsenteeism(workinghours);
                                if (abNormallist.size() > 0) {
                                    for (AbNormal ab : abNormallist) {
                                        //在申请的日期范围内
                                        String strlengthtime = null;
                                        if (ab.getStatus().equals("7")) {
                                            ab.setRelengthtime(ab.getRelengthtime() == null || ab.getRelengthtime() == "" ? "0" : ab.getRelengthtime());
                                            strlengthtime = ab.getRelengthtime();
                                            if (Double.valueOf(strlengthtime) <= 8 && !ab.getErrortype().equals("PR013022")) {
//                                                if(ab.getReoccurrencedate() == null )
//                                                {
//                                                    ab.setReoccurrencedate(ab.getOccurrencedate());
//                                                }
                                                ab.setRefinisheddate(ab.getReoccurrencedate());
                                            }
                                            if (ab.getReoccurrencedate().compareTo(sf1ymd.parse(sf1ymd.format(ad.getDates()))) <= 0 && ab.getRefinisheddate().compareTo(sf1ymd.parse(sf1ymd.format(ad.getDates()))) >= 0) {
                                                if (!(ab.getReoccurrencedate().compareTo(sf1ymd.parse(sf1ymd.format(ad.getDates()))) == 0 && ab.getRefinisheddate().compareTo(sf1ymd.parse(sf1ymd.format(ad.getDates()))) == 0)) {
                                                    strlengthtime = workinghours;
                                                    if (ab.getErrortype().equals("PR013022"))//加餐哺乳假
                                                    {
                                                        strlengthtime = "1";
                                                    }
                                                }
                                            } else {
                                                strlengthtime = "0";
                                            }
                                        } else {
                                            ab.setLengthtime(ab.getLengthtime() == null || ab.getLengthtime() == "" ? "0" : ab.getLengthtime());
                                            strlengthtime = ab.getLengthtime();
                                            if (Double.valueOf(strlengthtime) <= 8 && !ab.getErrortype().equals("PR013022")) {
                                                ab.setFinisheddate(ab.getOccurrencedate());
                                            }
                                            if (ab.getOccurrencedate().compareTo(sf1ymd.parse(sf1ymd.format(ad.getDates()))) <= 0 && ab.getFinisheddate().compareTo(sf1ymd.parse(sf1ymd.format(ad.getDates()))) >= 0) {
                                                if (!(ab.getOccurrencedate().compareTo(sf1ymd.parse(sf1ymd.format(ad.getDates()))) == 0 && ab.getFinisheddate().compareTo(sf1ymd.parse(sf1ymd.format(ad.getDates()))) == 0)) {
                                                    strlengthtime = workinghours;
                                                    if (ab.getErrortype().equals("PR013022"))//加餐哺乳假
                                                    {
                                                        strlengthtime = "1";
                                                    }
                                                }
                                            } else {
                                                strlengthtime = "0";
                                            }
                                        }

                                        if (ab.getErrortype().equals("PR013001")) {//外出
                                            nomal = String.valueOf(Double.valueOf(nomal) + Double.valueOf(strlengthtime));

                                        } else if (ab.getErrortype().equals("PR013005")) {//年休
                                            if (ab.getStatus().equals("7")) {
                                                if (ad.getAnnualrest() != null && !ad.getAnnualrest().isEmpty()) {
                                                    if (Double.valueOf(ad.getAnnualrest()) + Double.valueOf(strlengthtime) >= Double.valueOf(workinghours)) {
                                                        strlengthtime = df.format(Double.valueOf(workinghours));
                                                    } else {
                                                        strlengthtime = String.valueOf(df.format(Double.valueOf(strlengthtime) + Double.valueOf(ad.getAnnualrest())));
                                                    }
                                                }
                                                ad.setAnnualrest(strlengthtime);
                                            }
                                        } else if (ab.getErrortype().equals("PR013006")) {//代休-周末
                                            if (ab.getStatus().equals("7")) {
                                                if (ad.getDaixiu() != null && !ad.getDaixiu().isEmpty()) {
                                                    if (Double.valueOf(ad.getDaixiu()) + Double.valueOf(strlengthtime) >= Double.valueOf(workinghours)) {
                                                        strlengthtime = df.format(Double.valueOf(workinghours));
                                                    } else {
                                                        strlengthtime = String.valueOf(df.format(Double.valueOf(strlengthtime) + Double.valueOf(ad.getDaixiu())));
                                                    }
                                                }
                                                //代休-周末大于等于15分
                                                if (Double.valueOf(strlengthtime) % (Double.valueOf(lateearlyleave)) == 0) {
                                                    ad.setDaixiu(strlengthtime);
                                                } else {
                                                    ad.setDaixiu(df.format(Math.floor(Double.valueOf(strlengthtime) / Double.valueOf(lateearlyleave)) * Double.valueOf(lateearlyleave) + Double.valueOf(lateearlyleave)));
                                                }
                                                ad.setDaixiu(strlengthtime);
                                            }
                                        } else if (ab.getErrortype().equals("PR013007")) {//代休-特殊
                                            if (ab.getStatus().equals("7")) {
                                                if (ad.getDaixiu() != null && !ad.getDaixiu().isEmpty()) {
                                                    if (Double.valueOf(ad.getDaixiu()) + Double.valueOf(strlengthtime) >= Double.valueOf(workinghours)) {
                                                        strlengthtime = df.format(Double.valueOf(workinghours));
                                                    } else {
                                                        strlengthtime = String.valueOf(df.format(Double.valueOf(strlengthtime) + Double.valueOf(ad.getDaixiu())));
                                                    }
                                                }
                                                ad.setDaixiu(strlengthtime);
                                            }
                                        } else if (ab.getErrortype().equals("PR013008")) {//事休
                                            if (ad.getCompassionateleave() != null && !ad.getCompassionateleave().isEmpty()) {
                                                if (Double.valueOf(ad.getCompassionateleave()) + Double.valueOf(strlengthtime) >= Double.valueOf(workinghours)) {
                                                    strlengthtime = df.format(Double.valueOf(workinghours));
                                                } else {
                                                    strlengthtime = String.valueOf(df.format(Double.valueOf(strlengthtime) + Double.valueOf(ad.getCompassionateleave())));
                                                }
                                            }
                                            //事休大于等于15分
                                            if (Double.valueOf(strlengthtime) % (Double.valueOf(lateearlyleave)) == 0) {
                                                ad.setCompassionateleave(strlengthtime);
                                            } else {
                                                ad.setCompassionateleave(df.format(Math.floor(Double.valueOf(strlengthtime) / Double.valueOf(lateearlyleave)) * Double.valueOf(lateearlyleave) + Double.valueOf(lateearlyleave)));
                                            }
                                        } else if (ab.getErrortype().equals("PR013009")) {//短期病休
                                            if (ab.getStatus().equals("7")) {
                                                if (ad.getShortsickleave() != null && !ad.getShortsickleave().isEmpty()) {
                                                    //前一年病假满足30天
                                                    if (sumSick + Double.valueOf(ad.getShortsickleave()) + Double.valueOf(strlengthtime) > 240 && Double.valueOf(strlengthtime) + Double.valueOf(ad.getShortsickleave()) > 0) {
                                                        ad.setShortsickleave(df.format(Double.valueOf(ad.getShortsickleave()) + sumSick + Double.valueOf(strlengthtime) - 240));
                                                        if (ad.getLongsickleave() != null && !ad.getLongsickleave().isEmpty()) {
                                                            ad.setLongsickleave(df.format(Double.valueOf(ad.getLongsickleave()) + Double.valueOf(strlengthtime) - Double.valueOf(ad.getShortsickleave())));
                                                        } else {
                                                            ad.setLongsickleave(df.format(Double.valueOf(strlengthtime) - Double.valueOf(ad.getShortsickleave())));
                                                        }

                                                    } else {
                                                        ad.setShortsickleave(df.format(Double.valueOf(strlengthtime) + Double.valueOf(ad.getShortsickleave())));
                                                    }
                                                } else {
                                                    //前一年病假满足30天   240小时
                                                    if (sumSick + Double.valueOf(strlengthtime) > 240 && Double.valueOf(strlengthtime) > 0) {
                                                        ad.setShortsickleave(df.format(sumSick + Double.valueOf(strlengthtime) - 240));
                                                        ad.setLongsickleave(df.format(Double.valueOf(strlengthtime) - Double.valueOf(ad.getShortsickleave())));
                                                    } else {
                                                        ad.setShortsickleave(df.format(Double.valueOf(strlengthtime)));
                                                    }
                                                }
                                            }

                                        } else if (ab.getErrortype().equals("PR013012") || ab.getErrortype().equals("PR013013")) { //産休（女） 护理假（男）
                                            //産休（女）//産休看護休暇（男）
                                            if (ab.getStatus().equals("7")) {
                                                if (ad.getNursingleave() != null && !ad.getNursingleave().isEmpty()) {
                                                    if (Double.valueOf(ad.getNursingleave()) + Double.valueOf(strlengthtime) >= Double.valueOf(workinghours)) {
                                                        strlengthtime = df.format(Double.valueOf(workinghours));
                                                    } else {
                                                        strlengthtime = String.valueOf(df.format(Double.valueOf(strlengthtime) + Double.valueOf(ad.getNursingleave())));
                                                    }
                                                }
                                                ad.setNursingleave(strlengthtime);
                                            }
                                        } else if (ab.getErrortype().equals("PR013014") || ab.getErrortype().equals("PR013019")) {
                                            //家长会假//其他休暇

                                            if (ad.getWelfare() != null && !ad.getWelfare().isEmpty()) {
                                                if (Double.valueOf(ad.getWelfare()) + Double.valueOf(strlengthtime) >= Double.valueOf(workinghours)) {
                                                    strlengthtime = df.format(Double.valueOf(workinghours));
                                                } else {
                                                    strlengthtime = String.valueOf(df.format(Double.valueOf(strlengthtime) + Double.valueOf(ad.getWelfare())));
                                                }
                                            }
                                            if (Double.valueOf(strlengthtime) % (Double.valueOf(lateearlyleave)) == 0) {
                                                ad.setWelfare(strlengthtime);
                                            } else {
                                                ad.setWelfare(df.format(Math.floor(Double.valueOf(strlengthtime) / Double.valueOf(lateearlyleave)) * Double.valueOf(lateearlyleave) + Double.valueOf(lateearlyleave)));
                                            }
                                        } else if (ab.getErrortype().equals("PR013011") || ab.getErrortype().equals("PR013015")
                                                || ab.getErrortype().equals("PR013016") || ab.getErrortype().equals("PR013017")
                                                || ab.getErrortype().equals("PR013021") || ab.getErrortype().equals("PR013018")
                                                || ab.getErrortype().equals("PR013020")) {
                                            //婚假 //丧假 //妊娠檢查休暇 //流产假 //计划生育手术假// 労災休暇//工伤
                                            if (ab.getStatus().equals("7")) {
                                                if (ad.getWelfare() != null && !ad.getWelfare().isEmpty()) {
                                                    if (Double.valueOf(ad.getWelfare()) + Double.valueOf(strlengthtime) >= Double.valueOf(workinghours)) {
                                                        strlengthtime = df.format(Double.valueOf(workinghours));
                                                    } else {
                                                        strlengthtime = String.valueOf(df.format(Double.valueOf(strlengthtime) + Double.valueOf(ad.getWelfare())));
                                                    }
                                                }
                                                if (Double.valueOf(strlengthtime) % (Double.valueOf(lateearlyleave)) == 0) {
                                                    ad.setWelfare(strlengthtime);
                                                } else {
                                                    ad.setWelfare(df.format(Math.floor(Double.valueOf(strlengthtime) / Double.valueOf(lateearlyleave)) * Double.valueOf(lateearlyleave) + Double.valueOf(lateearlyleave)));
                                                }
                                            }
                                        } else if (ab.getErrortype().equals("PR013022")) {//加餐，哺乳假
                                            if (ab.getStatus().equals("7")) {
                                                if (ad.getWelfare() != null && !ad.getWelfare().isEmpty()) {
                                                    ad.setWelfare(df.format(Double.valueOf(strlengthtime) + Double.valueOf(ad.getWelfare())));
                                                } else {
                                                    ad.setWelfare(df.format(Double.valueOf(strlengthtime)));
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    //没有记录，没有申请
                                    ad.setAbsenteeism(df.format(Double.valueOf(workinghours)));
                                    ad.setNormal(null);
                                }
                                //---------处理昨日审批通过的异常考勤申请end-------
                                ad.setNormal(ad.getNormal() == null ? "0" : ad.getNormal());
                                ad.setAnnualrest(ad.getAnnualrest() == null ? "0" : ad.getAnnualrest());
                                ad.setDaixiu(ad.getDaixiu() == null ? "0" : ad.getDaixiu());
                                ad.setCompassionateleave(ad.getCompassionateleave() == null ? "0" : ad.getCompassionateleave());
                                ad.setShortsickleave(ad.getShortsickleave() == null ? "0" : ad.getShortsickleave());
                                ad.setLongsickleave(ad.getLongsickleave() == null ? "0" : ad.getLongsickleave());
                                ad.setNursingleave(ad.getNursingleave() == null ? "0" : ad.getNursingleave());
                                ad.setWelfare(ad.getWelfare() == null ? "0" : ad.getWelfare());
                                ad.setAbsenteeism(df.format(Double.valueOf(ad.getAbsenteeism()) - Double.valueOf(ad.getShortsickleave())
                                        - Double.valueOf(ad.getLongsickleave()) - Double.valueOf(ad.getCompassionateleave()) - Double.valueOf(ad.getAnnualrest())
                                        - Double.valueOf(ad.getDaixiu()) - Double.valueOf(ad.getNursingleave()) - Double.valueOf(ad.getWelfare()) - Double.valueOf(nomal)));

                                String leave = df.format(Double.valueOf(ad.getShortsickleave())
                                        + Double.valueOf(ad.getLongsickleave()) + Double.valueOf(ad.getCompassionateleave()) + Double.valueOf(ad.getAnnualrest())
                                        + Double.valueOf(ad.getDaixiu()) + Double.valueOf(ad.getNursingleave()) + Double.valueOf(ad.getWelfare()));

                                //当前日期
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(new Date());

                                if (workinghours.equals("4")) {
                                    if (sf1ymd.parse(sf1ymd.format(calendar.getTime())).compareTo(sf1ymd.parse(sf1ymd.format(ad.getDates()))) > 0) {
                                        if (Double.valueOf(leave) + Double.valueOf(nomal) >= 4) {
                                            ad.setNormal(df.format(Double.valueOf(workinghours) - Double.valueOf(leave)));
                                            ad.setAbsenteeism(null);
                                        } else {
                                            ad.setNormal(df.format(Double.valueOf(leave) + Double.valueOf(nomal)));
                                            ad.setNormal(df.format(Math.floor(Double.valueOf(ad.getNormal()) / Double.valueOf(lateearlyleave)) * Double.valueOf(lateearlyleave)));
                                            ad.setAbsenteeism(df.format(Double.valueOf(workinghours) - Double.valueOf(leave) - Double.valueOf(ad.getNormal())));
                                        }
                                    } else {
                                        if (Double.valueOf(leave) + Double.valueOf(nomal) >= 4) {
                                            ad.setNormal(df.format(Double.valueOf(workinghours) - Double.valueOf(leave)));
                                            ad.setAbsenteeism(null);
                                        } else {
                                            ad.setNormal(df.format(Double.valueOf(workinghours) - Double.valueOf(leave)));
                                            //ad.setNormal(df.format(Math.floor(Double.valueOf(ad.getNormal()) / Double.valueOf(lateearlyleave)) * Double.valueOf(lateearlyleave)));
                                            ad.setAbsenteeism(null);
                                        }
                                    }

                                } else if (workinghours.equals("8")) {
                                    if (sf1ymd.parse(sf1ymd.format(calendar.getTime())).compareTo(sf1ymd.parse(sf1ymd.format(ad.getDates()))) > 0) {
                                        if (Double.valueOf(ad.getAbsenteeism()) >= Double.valueOf(workinghours)) {
                                            ad.setNormal("0");
                                            ad.setAbsenteeism(workinghours);
                                        } else if (Double.valueOf(ad.getAbsenteeism()) <= 0) {
                                            if (Double.valueOf(leave) >= 8) {
                                                ad.setNormal("0");
                                            } else {
                                                ad.setNormal(df.format(Double.valueOf(workinghours) - Double.valueOf(leave)));
                                            }
                                        } else {
                                            if (!(Double.valueOf(ad.getAbsenteeism()) % (Double.valueOf(lateearlyleave)) == 0)) {
                                                ad.setAbsenteeism(df.format(Math.floor(Double.valueOf(ad.getAbsenteeism()) / Double.valueOf(lateearlyleave)) * Double.valueOf(lateearlyleave) + Double.valueOf(lateearlyleave)));
                                            }
                                            ad.setNormal(df.format(Double.valueOf(workinghours) - Double.valueOf(leave) - Double.valueOf(ad.getAbsenteeism())));
                                        }
                                    } else {
                                        if (Double.valueOf(ad.getAbsenteeism()) >= Double.valueOf(workinghours)) {
                                            ad.setNormal(workinghours);
                                            ad.setAbsenteeism("0");
                                        } else if (Double.valueOf(ad.getAbsenteeism()) <= 0) {
                                            if (Double.valueOf(leave) >= 8) {
                                                ad.setNormal("0");
                                            } else {
                                                ad.setNormal(df.format(Double.valueOf(workinghours) - Double.valueOf(leave)));
                                            }
                                        } else {
                                            ad.setAbsenteeism("0");
                                            ad.setNormal(df.format(Double.valueOf(workinghours) - Double.valueOf(leave)));
                                        }
                                    }

                                }
                                ad.setNormal(ad.getNormal() == null ? null : (Double.valueOf(ad.getNormal()) <= 0 ? null : df.format(Double.valueOf(ad.getNormal()))));
                                ad.setAnnualrest(Double.valueOf(ad.getAnnualrest()) <= 0 ? null : df.format(Double.valueOf(ad.getAnnualrest())));
                                ad.setDaixiu(Double.valueOf(ad.getDaixiu()) <= 0 ? null : df.format(Double.valueOf(ad.getDaixiu())));
                                ad.setCompassionateleave(Double.valueOf(ad.getCompassionateleave()) <= 0 ? null : df.format(Double.valueOf(ad.getCompassionateleave())));
                                ad.setShortsickleave(Double.valueOf(ad.getShortsickleave()) <= 0 ? null : df.format(Double.valueOf(ad.getShortsickleave())));
                                ad.setLongsickleave(Double.valueOf(ad.getLongsickleave()) <= 0 ? null : df.format(Double.valueOf(ad.getLongsickleave())));
                                ad.setNursingleave(Double.valueOf(ad.getNursingleave()) <= 0 ? null : df.format(Double.valueOf(ad.getNursingleave())));
                                ad.setWelfare(Double.valueOf(ad.getWelfare()) <= 0 ? null : df.format(Double.valueOf(ad.getWelfare())));
                                ad.setAbsenteeism(Double.valueOf(ad.getAbsenteeism()) <= 0 ? null : df.format(Double.valueOf(ad.getAbsenteeism())));
                                //更新考勤表
                                if (customerInfo.getUserinfo().getEnddate() == null || customerInfo.getUserinfo().getEnddate().isEmpty()) {
                                    ad.setTshortsickleave(ad.getShortsickleave());
                                    ad.setTlongsickleave(ad.getLongsickleave());
                                    ad.setTabsenteeism(ad.getAbsenteeism());
                                    ad.setShortsickleave(null);
                                    ad.setLongsickleave(null);
                                    ad.setAbsenteeism(null);
                                } else {
                                    String enddate = customerInfo.getUserinfo().getEnddate().substring(0, 10);
                                    if (sf1ymd.parse(Convert.toStr(sf1ymd.format(Convert.toDate(enddate)))).getTime() >= ad.getDates().getTime()) {
                                        ad.setTshortsickleave(ad.getShortsickleave());
                                        ad.setTlongsickleave(ad.getLongsickleave());
                                        ad.setTabsenteeism(ad.getAbsenteeism());
                                        ad.setShortsickleave(null);
                                        ad.setLongsickleave(null);
                                        ad.setAbsenteeism(null);
                                    }
                                }
                                if (workinghours.equals("0")) {
                                    ad.setNormal(null);
                                    ad.setAbsenteeism(null);
                                    ad.setShortsickleave(null);
                                    ad.setLongsickleave(null);
                                    ad.setCompassionateleave(null);
                                    ad.setAnnualrest(null);
                                    ad.setDaixiu(null);
                                    ad.setNursingleave(null);
                                    ad.setWelfare(null);
                                    ad.setTshortsickleave(null);
                                    ad.setTlongsickleave(null);
                                    ad.setTabsenteeism(null);
                                }
                                PunchcardRecord phr = new PunchcardRecord();
                                //员工填写日志对应的实际工作时间
                                ad.setOutgoinghours(getOutgoinghours(lunchbreak_start, lunchbreak_end, phr, nomal));
                                saveAttendance(ad, "0", token);
                            }
                            //---------查询昨天大打卡记录end-------
                        }
//                        }
                    } catch (Exception e) {
                        System.out.println("考勤数据生成异常:" + ad.getUser_id());
                        continue;
                    }
                    //---------不定时考勤人员(非不定时考勤人员才计算)end-------
//                }
                }
            }
        }
        //---------查询考勤表是否有数据end-------
    }

    //员工填写日志时的实际时长
    public String getOutgoinghours(String lunchbreak_start, String lunchbreak_end, PunchcardRecord PR, String nomal) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
        String outgoinghours = "0";
        nomal = nomal == null || nomal == "" ? "0" : nomal;
        if (PR.getUser_id() == null || PR.getUser_id() == "") {
            outgoinghours = nomal;
        } else {
            if (sdf.format(PR.getTime_start()).equals(sdf.format(PR.getTime_end()))) {
                outgoinghours = nomal;
            } else {
                outgoinghours = timeLength(sdf.format(PR.getTime_start()), sdf.format(PR.getTime_end()), lunchbreak_start, lunchbreak_end);
                outgoinghours = String.valueOf(Double.valueOf(outgoinghours) + Double.valueOf(nomal) - Double.valueOf(PR.getOutgoinghours() == null || PR.getOutgoinghours() == "" ? "0" : PR.getOutgoinghours()));
            }
        }
        DecimalFormat dlf = new DecimalFormat("#0.00");
        outgoinghours = String.valueOf(dlf.format(Double.valueOf(outgoinghours)));
        return outgoinghours;
    }

    //考勤管理
    public void saveAttendance(Attendance attendance, String Flg, TokenModel tokenModel) throws Exception {
        if (Flg.equals("0")) {//更新
            attendance.preUpdate(tokenModel);
            attendanceMapper.updateByPrimaryKey(attendance);
        } else {//新建
            attendance.preInsert(tokenModel);
            attendanceMapper.insert(attendance);
        }
    }

    //时间段判断
    public String timeLength(String time_start, String time_end, String lunchbreak_start, String lunchbreak_end) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
        String overtimeHours = null;
        if (sdf.parse(time_start).getTime() <= sdf.parse(lunchbreak_start).getTime() && sdf.parse(time_end).getTime() >= sdf.parse(lunchbreak_end).getTime()) {
            //午休都包括在内
            //打卡开始-结束时间
            long result1 = sdf.parse(time_end).getTime() - sdf.parse(time_start).getTime();
            //午休时间
            long result2 = sdf.parse(lunchbreak_end).getTime() - sdf.parse(lunchbreak_start).getTime();
            //打卡记录加班时间
            Double result3 = Double.valueOf(String.valueOf(result1 - result2)) / 60 / 60 / 1000;
            overtimeHours = String.valueOf(result3);
        } else if (sdf.parse(time_start).getTime() >= sdf.parse(lunchbreak_end).getTime() || sdf.parse(time_end).getTime() <= sdf.parse(lunchbreak_start).getTime()) {
            //午休不包括在内
            //打卡开始-结束时间
            long result1 = sdf.parse(time_end).getTime() - sdf.parse(time_start).getTime();
            //打卡记录加班时间
            Double result3 = Double.valueOf(String.valueOf(result1)) / 60 / 60 / 1000;
            overtimeHours = String.valueOf(result3);

        } else if (sdf.parse(time_start).getTime() < sdf.parse(lunchbreak_start).getTime() && (sdf.parse(lunchbreak_start).getTime() < sdf.parse(time_end).getTime() || sdf.parse(time_end).getTime() < sdf.parse(lunchbreak_end).getTime())) {
            //午休包括在内一部分（打卡结束时间在午休内）
            //打卡开始-午休开始时间
            long result1 = sdf.parse(lunchbreak_start).getTime() - sdf.parse(time_start).getTime();
            Double result3 = Double.valueOf(String.valueOf(result1)) / 60 / 60 / 1000;
            overtimeHours = String.valueOf(result3);
        } else {
            //午休包括在内一部分（打卡开始时间在午休内）
            //午休结束-打开结束时间
            long result1 = sdf.parse(time_end).getTime() - sdf.parse(lunchbreak_end).getTime();
            Double result3 = Double.valueOf(String.valueOf(result1)) / 60 / 60 / 1000;
            overtimeHours = String.valueOf(result3);
        }
        return overtimeHours;
    }

    //平日出勤
    public String shijiworkLength(String time_start, String time_end, String lunchbreak_start, String lunchbreak_end, PunchcardRecord PR) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
        String shijiworkHours = "0";
        if (sdf.parse(time_end).getTime() <= sdf.parse(lunchbreak_start).getTime()) {
            long result1 = sdf.parse(time_end).getTime() - sdf.parse(time_start).getTime();
            shijiworkHours = String.valueOf((Double.valueOf(String.valueOf(result1)) / 60 / 60 / 1000) - Double.valueOf(PR.getAbsenteeismam()));
        } else if (sdf.parse(time_start).getTime() >= sdf.parse(lunchbreak_end).getTime()) {
            long result1 = sdf.parse(time_end).getTime() - sdf.parse(time_start).getTime();
            shijiworkHours = String.valueOf((Double.valueOf(String.valueOf(result1)) / 60 / 60 / 1000) - (Double.valueOf(PR.getWorktime()) - Double.valueOf(PR.getAbsenteeismam())));
        } else if (sdf.parse(time_start).getTime() <= sdf.parse(lunchbreak_start).getTime() && sdf.parse(time_end).getTime() < sdf.parse(lunchbreak_end).getTime()) {
            long result1 = sdf.parse(lunchbreak_start).getTime() - sdf.parse(time_start).getTime();
            shijiworkHours = String.valueOf((Double.valueOf(String.valueOf(result1)) / 60 / 60 / 1000) - Double.valueOf(PR.getAbsenteeismam()));
        } else if (sdf.parse(time_start).getTime() > sdf.parse(lunchbreak_start).getTime() && sdf.parse(time_end).getTime() >= sdf.parse(lunchbreak_end).getTime()) {
            long result1 = sdf.parse(time_end).getTime() - sdf.parse(lunchbreak_end).getTime();
            shijiworkHours = String.valueOf((Double.valueOf(String.valueOf(result1)) / 60 / 60 / 1000) - (Double.valueOf(PR.getWorktime()) - Double.valueOf(PR.getAbsenteeismam())));
        } else {
            //下午上班时间
            long result1 = sdf.parse(time_end).getTime() - sdf.parse(lunchbreak_end).getTime();
            //上午上班时间
            long result2 = sdf.parse(lunchbreak_start).getTime() - sdf.parse(time_start).getTime();

            Double result3 = Double.valueOf(result2) / 60 / 60 / 1000 - Double.valueOf(PR.getAbsenteeismam());
            Double result4 = Double.valueOf(result1) / 60 / 60 / 1000 - (Double.valueOf(PR.getWorktime()) - Double.valueOf(PR.getAbsenteeismam()));
            shijiworkHours = String.valueOf(result3 > result4 ? result3 : result4);

        }
        return shijiworkHours;
    }

    //会社特别休日
    public String huisheshijiworkLength(String time_start, String time_end, String lunchbreak_start, String lunchbreak_end, PunchcardRecord PR) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
        String shijiworkHours = "0";
        if (sdf.parse(time_end).getTime() <= sdf.parse(lunchbreak_start).getTime()) {
            long result1 = sdf.parse(time_end).getTime() - sdf.parse(time_start).getTime();
            shijiworkHours = String.valueOf((Double.valueOf(String.valueOf(result1)) / 60 / 60 / 1000) - Double.valueOf(PR.getAbsenteeismam()));
        } else if (sdf.parse(time_start).getTime() >= sdf.parse(lunchbreak_end).getTime()) {
            long result1 = sdf.parse(time_end).getTime() - sdf.parse(time_start).getTime();
            shijiworkHours = String.valueOf((Double.valueOf(String.valueOf(result1)) / 60 / 60 / 1000) - (Double.valueOf(PR.getWorktime()) - Double.valueOf(PR.getAbsenteeismam())));
        } else if (sdf.parse(time_start).getTime() <= sdf.parse(lunchbreak_start).getTime() && sdf.parse(time_end).getTime() < sdf.parse(lunchbreak_end).getTime()) {
            long result1 = sdf.parse(lunchbreak_start).getTime() - sdf.parse(time_start).getTime();
            shijiworkHours = String.valueOf((Double.valueOf(String.valueOf(result1)) / 60 / 60 / 1000) - Double.valueOf(PR.getAbsenteeismam()));
        } else if (sdf.parse(time_start).getTime() > sdf.parse(lunchbreak_start).getTime() && sdf.parse(time_end).getTime() >= sdf.parse(lunchbreak_end).getTime()) {
            long result1 = sdf.parse(time_end).getTime() - sdf.parse(lunchbreak_end).getTime();
            shijiworkHours = String.valueOf((Double.valueOf(String.valueOf(result1)) / 60 / 60 / 1000) - (Double.valueOf(PR.getWorktime()) - Double.valueOf(PR.getAbsenteeismam())));
        } else {
            //下午上班时间
            long result1 = sdf.parse(time_end).getTime() - sdf.parse(lunchbreak_end).getTime();
            //上午上班时间
            long result2 = sdf.parse(lunchbreak_start).getTime() - sdf.parse(time_start).getTime();

            Double result3 = Double.valueOf(result2) / 60 / 60 / 1000 - Double.valueOf(PR.getAbsenteeismam());
            Double result4 = Double.valueOf(result1) / 60 / 60 / 1000 - (Double.valueOf(PR.getWorktime()) - Double.valueOf(PR.getAbsenteeismam()));
            //上午
            if (result3 >= 4) {
                shijiworkHours = String.valueOf(4);
            } else {
                shijiworkHours = String.valueOf(0);
            }

            //下午
            if (result4 >= 4) {
                shijiworkHours = String.valueOf(4 + Double.valueOf(shijiworkHours));
            } else {
                shijiworkHours = String.valueOf(0 + Double.valueOf(shijiworkHours));
            }
//
//
//            if(result3 + result4 >= 8)
//            {
//                shijiworkHours = String.valueOf(8);
//            }
//            else
//            {
//                shijiworkHours = String.valueOf(result3 > result4 ? result3 : result4);
//            }
        }
        return shijiworkHours;
    }

    //代休添加
    public void insertReplace(Attendance ad, TokenModel tokenModel, String type, String duration) throws Exception {
        Replacerest replacerest = new Replacerest();
        replacerest.setUser_id(ad.getUser_id());
        List<Replacerest> replacerestlist = replacerestMapper.select(replacerest);
        replacerest.setUser_id(ad.getUser_id());
        replacerest.setCenter_id(ad.getCenter_id());
        replacerest.setGroup_id(ad.getGroup_id());
        replacerest.setTeam_id(ad.getTeam_id());
        replacerest.setApplication_date(DateUtil.format(new Date(), "YYYY/MM/dd"));
        replacerest.setRecognitionstate("0");
        if (replacerestlist.size() > 0) {
            //更新代休
            replacerest.preUpdate(tokenModel);
            replacerest.setReplacerest_id(replacerestlist.get(0).getReplacerest_id());

            if (type.equals("1")) {
                //周末
                replacerest.setType(type);
                replacerest.setDuration(replacerestlist.get(0).getDuration() == null || replacerestlist.get(0).getDuration().isEmpty() ? duration : String.valueOf(Double.valueOf(replacerestlist.get(0).getDuration()) + Double.valueOf(duration)));
                replacerest.setType_teshu(replacerestlist.get(0).getType_teshu());
                replacerest.setDuration_teshu(replacerestlist.get(0).getDuration_teshu());
            } else {
                //特殊
                replacerest.setType_teshu(type);
                replacerest.setDuration_teshu(replacerestlist.get(0).getDuration_teshu() == null || replacerestlist.get(0).getDuration_teshu().isEmpty() ? duration : String.valueOf(Double.valueOf(replacerestlist.get(0).getDuration_teshu()) + Double.valueOf(duration)));
                replacerest.setType(replacerestlist.get(0).getType());
                replacerest.setDuration(replacerestlist.get(0).getDuration());
            }
            replacerestMapper.updateByPrimaryKey(replacerest);
        } else {
            //登录代休
            replacerest.preInsert(tokenModel);
            replacerest.setReplacerest_id(UUID.randomUUID().toString());

            if (type.equals("1")) {
                //周末
                replacerest.setType(type);
                replacerest.setDuration(duration);
                replacerest.setType_teshu("2");
                replacerest.setDuration_teshu("0");
            } else {
                //特殊
                replacerest.setType_teshu(type);
                replacerest.setDuration_teshu(duration);
                replacerest.setType("1");
                replacerest.setDuration("0");
            }
            replacerestMapper.insert(replacerest);
        }
    }

    //获取基本计算单位
    public Double getUnit(Double d1, Double d2) throws Exception {
        Double d3 = d1 / d2;
        return Double.valueOf((int) Math.ceil(d3)) * d2;
    }

    @Override
    public List<PunchcardRecordDetail> getPunDetail(PunchcardRecordDetail detail) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String Jobnumber = detail.getJobnumber();
        String user_id = detail.getUser_id();
        String Punchcardrecord_date = sdf.format(detail.getPunchcardrecord_date());
        List<PunchcardRecordDetail> detaillist = punchcardrecorddetailmapper.getPunDetail(Jobnumber, user_id, Punchcardrecord_date);
        return detaillist;
    }

    @Override
    public List<PunchcardRecord> getTodaylist(PunchcardRecord punchcardrecord, TokenModel tokenModel) throws Exception {

        List<PunchcardRecordDetail> punDetaillist = new ArrayList<PunchcardRecordDetail>();
        //测试接口 GBB add
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sfymd = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdhm = new SimpleDateFormat("HHmm");
        SimpleDateFormat sdhms = new SimpleDateFormat("HHmmss");
        DecimalFormat df = new DecimalFormat("######0.00");
        String thisDate = DateUtil.format(new Date(),"yyyy-MM-dd");
        //正式
        String doorIDList = "34,16,17,80,81,83,84";//34:自动门；16：1F子母门-左；17：1F子母门-右；80：B2南侧；81：B2北侧；83：B1北侧；84：B2南侧；
        String StaffNoList = "00010";//34:自动门；16：1F子母门-左；17：1F子母门-右；80：B2南侧；81：B2北侧；83：B1北侧；84：B2南侧；
        //Admin/QueryRecordByStaffNoList?userName={userName}&password={password}&pageIndex={pageIndex}&pageSize={pageSize}&startDate={startDate}&endDate={endDate}&doorIDList={doorIDList}&StaffNoList={staffNoList}&sortFieldID={sortFieldID}
        String url = "http://192.168.2.202:80/KernelService/Admin/QueryRecordByStaffNoList?userName=admin&password=admin&pageIndex=1&pageSize=999999&startDate=" + thisDate + "&endDate=" + thisDate + "&doorIDList=" + doorIDList + "&StaffNoList=" + StaffNoList;
        //請求接口
        ApiResult getresult = this.restTemplate.getForObject(url, ApiResult.class);
        Object obj = JSON.toJSON(getresult.getData());
        JSONArray jsonArray = JSONArray.parseArray(obj.toString());
        //打卡时间
        String recordTime = "";
        //员工编号
        String jobnumber = "";
        if(jsonArray.size() > 0){
            for(Object ob : jsonArray){
                //打卡时间
                recordTime = getProperty(ob, "recordTime");
                //员工编号
                jobnumber = getProperty(ob, "staffNo");
                //进出状态(1，正常进入；2，正常外出;30:无效-反潜回)
                String eventNo = getProperty(ob, "eventNo");
                //无效-反潜回
                if(eventNo.equals("30")){
                    continue;
                }
                //PSCDC(本社人员)
                String departmentName_P = getProperty(ob, "departmentName");
                if(!departmentName_P.equals("PSDCD")){
                    continue;
                }
                //员工姓名
                String staffName = getProperty(ob, "staffName");
                //员工部门
                String departmentName = getProperty(ob, "departmentName");
                //门号
                String doorID = getProperty(ob, "doorID");
                //添加打卡详细
                PunchcardRecordDetail punchcardrecorddetail = new PunchcardRecordDetail();
                //卡号
                punchcardrecorddetail.setJobnumber(jobnumber);
                punchcardrecorddetail.setDates(sfymd.parse(recordTime));
                //打卡时间
                punchcardrecorddetail.setPunchcardrecord_date(sf.parse(recordTime));
                //打卡时间
                punchcardrecorddetail.setUser_id(staffName);
                //进出状态
                punchcardrecorddetail.setEventno(eventNo);
                punchcardrecorddetail.preInsert(tokenModel);
                punchcardrecorddetail.setPunchcardrecorddetail_id(UUID.randomUUID().toString());
                //punchcardrecorddetailmapper.insert(punchcardrecorddetail);
                punDetaillist.add(punchcardrecorddetail);
            }
        }
        if(punDetaillist.size() > 0){
            //考勤设定
            AttendanceSetting attendancesetting = new AttendanceSetting();
            //上班开始时间
            String workshift_start = "";
            //午下班结束时间
            String closingtime_end = "";
            //午休时间开始
            String lunchbreak_start = "";
            //午休时间结束
            String lunchbreak_end = "";
            List<AttendanceSetting> attendancesettinglist = attendanceSettingMapper.select(attendancesetting);
            if(attendancesettinglist.size() > 0) {
                //上班开始时间
                workshift_start = attendancesettinglist.get(0).getWorkshift_start().replace(":", "");
                //下班结束时间
                closingtime_end = attendancesettinglist.get(0).getClosingtime_end().replace(":", "");;
                //午休时间开始
                lunchbreak_start = attendancesettinglist.get(0).getLunchbreak_start().replace(":", "");
                //午休时间结束
                lunchbreak_end = attendancesettinglist.get(0).getLunchbreak_end().replace(":", "");
            }

            //卡号去重得到打卡总人数
            List<PunchcardRecordDetail> punDetaillistCount = new ArrayList<PunchcardRecordDetail>();
            punDetaillistCount = punDetaillist.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() ->new TreeSet<>(Comparator.comparing(t -> t.getJobnumber()))),ArrayList::new));
            int x = 0;
            for(PunchcardRecordDetail count : punDetaillistCount){
                x = x + 1;
                //欠勤时间 8点到18点
                Double minute = 0D;
                //上午
                Double minuteam = 0D;
                List<PunchcardRecordDetail> punDetaillistx = punDetaillist.stream().filter(p->(count.getJobnumber().equalsIgnoreCase(p.getJobnumber()))).collect(Collectors.toList());

                //所有记录时间升序
                Collections.sort(punDetaillistx, new Comparator<PunchcardRecordDetail>() {
                    @Override
                    public int compare(PunchcardRecordDetail o1, PunchcardRecordDetail o2) {
                        Date dt1 = o1.getPunchcardrecord_date();
                        Date dt2 = o2.getPunchcardrecord_date();
                        if (dt1.getTime() > dt2.getTime()) {
                            return 1;
                        } else if (dt1.getTime() < dt2.getTime()) {
                            return -1;
                        } else {
                            return 0;
                        }
                    }
                });

                String Eventno = "";
                //去除重复
                for (int i = 0;i < punDetaillistx.size();i++){
                    if(i < punDetaillistx.size() ){
                        if(punDetaillistx.get(i).getEventno().equals(Eventno)){
                            if(punDetaillistx.get(i).getEventno().equals("1")){
                                //进进选后
                                punDetaillistx.remove(i - 1);
                            }
                            else{
                                //出出选前
                                punDetaillistx.remove(i);
                            }
                        }
                    }
                    if(i < punDetaillistx.size() ){
                        Eventno = punDetaillistx.get(i).getEventno();
                    }
                }
                //个人所有进门记录
                List<PunchcardRecordDetail> punDetaillistevent1 = punDetaillistx.stream().filter(p->(p.getEventno().equalsIgnoreCase("1"))).collect(Collectors.toList());
                //第一条进门记录
                Date Time_start = null;
                //第一条进门时间
                long startlfirst = 0L;
                //第一条进门时间
                long startlfirsts = 0L;
                if(punDetaillistevent1.size() > 0){
                    Time_start = punDetaillistevent1.get(0).getPunchcardrecord_date();
                    //第一条进门时间
                    startlfirst = sdhm.parse(sdhm.format(Time_start)).getTime();
                    startlfirsts = sdhms.parse(sdhms.format(Time_start)).getTime();
                }
                //个人所有出门记录
                List<PunchcardRecordDetail> punDetaillistevent2 = punDetaillistx.stream().filter(p->(p.getEventno().equalsIgnoreCase("2"))).collect(Collectors.toList());
                //最后一条出门记录
                Date Time_end = null;
                //第一条出门时间
                long endlfirst = 0L;
                if(punDetaillistevent2.size() > 0){
                    //最后一条出门记录
                    Time_end = punDetaillistevent2.get(punDetaillistevent2.size() - 1).getPunchcardrecord_date();
                    //第一条出门时间
                    endlfirst = sdhms.parse(sdhms.format(punDetaillistevent2.get(0).getPunchcardrecord_date())).getTime();
                    //个人第一条考勤时出门记录的情况
                    if(endlfirst < startlfirsts){
                        punDetaillistevent2.remove(0);
                    }
                }
                //从第一次出门开始计算
                for (int i = 0; i < punDetaillistevent2.size() - 1; i ++){
                    if(i < punDetaillistevent1.size() - 1){
                        Date DateStart = punDetaillistevent2.get(i).getPunchcardrecord_date();
                        Date DateEnd = punDetaillistevent1.get(i + 1).getPunchcardrecord_date();
                        //个人出门时间
                        long startl = sdhm.parse(sdhm.format(DateStart)).getTime();
                        //个人出门之后再次进门时间
                        long endl = sdhm.parse(sdhm.format(DateEnd)).getTime();

                        //region 考勤用外出时间合计
                        //个人出门时间小于8点
                        if(startl < sdhm.parse(workshift_start).getTime()){
                            //进门时间跨18点的情况
                            if(endl >= sdhm.parse(closingtime_end).getTime()){
                                //全天欠勤
                                minute = minute + 480D;
                            }
                            //进门时间跨13点的情况
                            else if(endl >= sdhm.parse(lunchbreak_end).getTime()){
                                //午餐结束时间
                                long fromlunchbreak_end = sdhm.parse(lunchbreak_end).getTime();
                                //时间出门到进门的相差分钟数
                                Double minutesi = Convert.toDouble((endl - fromlunchbreak_end)/(1000 * 60));
                                //累计欠勤时间(上午欠勤+午餐结束完归时间)
                                minute = minute + 240D + minutesi;
                            }
                            //进门时间跨12点的情况
                            else if(endl >= sdhm.parse(lunchbreak_start).getTime()){
                                //上午欠勤
                                minute = minute + 240D;
                            }
                            //进门时间跨8点的情况
                            else if(endl > sdhm.parse(workshift_start).getTime()){
                                //时间出门到进门的相差分钟数
                                Double minutes =Convert.toDouble((endl - sdhm.parse(workshift_start).getTime())/(1000 * 60));
                                minute = minute + minutes;
                                minuteam = minuteam + minutes;
                            }
                            continue;
                        }
                        //个人出门时间晚于18点的数据排除
                        if(startl >= sdhm.parse(closingtime_end).getTime()){
                            continue;
                        }

                        //进出时间都在8点到12点之间
                        if((startl <= sdhm.parse(lunchbreak_start).getTime() && endl <= sdhm.parse(lunchbreak_start).getTime()))
                        {
                            //个人出门时间
                            long from = sf.parse(sf.format(DateStart)).getTime();
                            //个人出门之后再次进门时间
                            long to = sf.parse(sf.format(DateEnd)).getTime();
                            //时间出门到进门的相差分钟数
                            Double minutes =Convert.toDouble((to - from)/(1000 * 60));
                            minute = minute + minutes;
                            minuteam = minuteam + minutes;
                        }
                        //进出时间都在13点之后
                        else if((startl >= sdhm.parse(lunchbreak_end).getTime() && endl >= sdhm.parse(lunchbreak_end).getTime()))
                        {
                            //个人出门时间
                            long from = sf.parse(sf.format(DateStart)).getTime();
                            //个人出门之后再次进门时间
                            long to = sf.parse(sf.format(DateEnd)).getTime();
                            //出门时间再18点之后
                            if(sdhm.parse(closingtime_end).getTime() <= endl){
                                //18点减18之前的最后一次出门时间
                                Double minutes = Convert.toDouble((sdhm.parse(closingtime_end).getTime() - startl)/(1000 * 60));
                                minute = minute + minutes;
                            }
                            else{
                                //时间出门到进门的相差分钟数
                                Double minutes = Convert.toDouble((to - from)/(1000 * 60));
                                minute = minute + minutes;
                            }
                        }
                        //进出门跨12点的情况
                        else if(startl <= sdhm.parse(lunchbreak_start).getTime() && endl >= sdhm.parse(lunchbreak_start).getTime()){
                            //午餐前出门时间
                            long from = startl;
                            //午餐开始时间
                            long to = sdhm.parse(lunchbreak_start).getTime();
                            //12点减12点之前最后一次出门时间
                            Double minutes = Convert.toDouble((to - from)/(1000 * 60));
                            //累计欠勤时间
                            minute = minute + minutes;
                            minuteam = minuteam + minutes;
                            if(sdhm.parse(closingtime_end).getTime() <= endl){
                                //18点减18之前的最后一次出门时间
                                Double minutesi = Convert.toDouble((sdhm.parse(closingtime_end).getTime() - sdhm.parse(lunchbreak_end).getTime())/(1000 * 60));
                                minute = minute + minutesi;
                            }
                            //午餐结束之后进门的情况3
                            else if(endl >= sdhm.parse(lunchbreak_end).getTime()){
                                //午餐结束时间
                                long fromlunchbreak_end = sdhm.parse(lunchbreak_end).getTime();
                                //午餐结束之后进门
                                long toendl = endl;
                                //时间出门到进门的相差分钟数
                                Double minutesi = Convert.toDouble((toendl - fromlunchbreak_end)/(1000 * 60));
                                //累计欠勤时间
                                minute = minute + minutesi;
                            }
                        }
                        //跨进门13点的情况
                        else if(startl >= sdhm.parse(lunchbreak_start).getTime() && startl <= sdhm.parse(lunchbreak_end).getTime() && endl >= sdhm.parse(lunchbreak_end).getTime()){
                            //午餐期间出门并且午餐结束之后进门4
                            //午餐结束时间
                            long from = sdhm.parse(lunchbreak_end).getTime();
                            //午餐结束之后进门时间
                            long to = endl;
                            if(sdhm.parse(closingtime_end).getTime() <= to){
                                //18点减18之前的最后一次出门时间
                                Double minutes = Convert.toDouble((sdhm.parse(closingtime_end).getTime() - from)/(1000 * 60));
                                minute = minute + minutes;
                            }
                            else{
                                //时间出门到进门的相差分钟数
                                Double minutes = Convert.toDouble((to - from)/(1000 * 60));
                                //累计欠勤时间
                                minute = minute + minutes;
                            }
                        }
                        //endregion
                    }
                }
                //添加打卡记录start
                double minutess= minute.doubleValue();
                minute = NumberUtil.round(minutess/60,2).doubleValue();
                double minutesss= minuteam.doubleValue();
                minuteam = NumberUtil.round(minutesss/60,2).doubleValue();
                //获取人员信息
                Query query = new Query();
                query.addCriteria(Criteria.where("userinfo.jobnumber").is(count.getJobnumber().trim()));
                CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
//                if (customerInfo != null) {
//                    //打卡记录
//                    PunchcardRecord punchcardrecord = new PunchcardRecord();
//                    tokenModel.setUserId(customerInfo.getUserid());
//                    tokenModel.setExpireDate(new Date());
//                    punchcardrecord.setPunchcardrecord_date(sfymd.parse(recordTime));
//                    punchcardrecord.setUser_id(customerInfo.getUserid());
//                    punchcardrecord.setJobnumber(count.getJobnumber());
//                    punchcardrecord.setCenter_id(customerInfo.getUserinfo().getCentername());
//                    punchcardrecord.setGroup_id(customerInfo.getUserinfo().getGroupname());
//                    punchcardrecord.setTeam_id(customerInfo.getUserinfo().getTeamname());
//                    //外出超过15分钟的欠勤时间
//                    punchcardrecord.setWorktime(minute.toString());
//                    punchcardrecord.setAbsenteeismam(minuteam.toString());
//                    // 日志用外出时长
//                    punchcardrecord.setOutgoinghours("0");
//                    punchcardrecord.setTime_start(Time_start);
//                    punchcardrecord.setTime_end(Time_end);
//                    punchcardrecord.setPunchcardrecord_id(UUID.randomUUID().toString());
//                    punchcardrecord.preInsert(tokenModel);
//                    punchcardrecordMapper.insert(punchcardrecord);
//                }
                //添加打卡记录end
            }
        }

        return punchcardrecordMapper.select(punchcardrecord);
    }

    //取object的值
    private String getProperty(Object o, String key) throws Exception{
        try {
            return org.apache.commons.beanutils.BeanUtils.getProperty(o, key);
        } catch (Exception e) {
            throw new LogicalException(e.getMessage());
        }
    }
}
