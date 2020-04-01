package com.nt.service_pfans.PFANS2000.Impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Pfans.PFANS2000.*;
import com.nt.dao_Pfans.PFANS8000.WorkingDay;
import com.nt.service_pfans.PFANS2000.PunchcardRecordService;
import com.nt.service_pfans.PFANS2000.mapper.*;
import com.nt.service_pfans.PFANS8000.mapper.WorkingDayMapper;
import com.nt.service_pfans.PFANS6000.Impl.DelegainformationServiceImpl;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;


import javax.servlet.http.HttpServletRequest;
import java.util.Date;
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
    public List<PunchcardRecord> list(PunchcardRecord punchcardrecord,TokenModel tokenModel) throws Exception {
        return punchcardrecordMapper.select(punchcardrecord);
    }

    @Override
    public List<PunchcardRecord> getDataList(PunchcardRecord punchcardrecord,TokenModel tokenModel) throws Exception{
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
            Double Worktime =0d;
            DecimalFormat df = new DecimalFormat(".00");
            List<PunchcardRecord> punchcardrecordlist = punchcardrecorddetailmapper.getPunchCardRecord();
            for (PunchcardRecord punchcard : punchcardrecordlist) {
                if(punchcard.getWorktime() != null){
                    Worktime=Double.valueOf(punchcard.getWorktime());
                }
                punchcardrecord.setPunchcardrecord_date(punchcard.getPunchcardrecord_date());
                punchcardrecord.setTeam_id(punchcard.getTeam_id());
                punchcardrecord.setGroup_id(punchcard.getGroup_id());
                punchcardrecord.setCenter_id(punchcard.getCenter_id());
                punchcardrecord.setUser_id(punchcard.getUser_id());
                punchcardrecord.setJobnumber(punchcard.getJobnumber());
                punchcardrecord.setWorktime(df.format(Worktime));
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
                attendance.setAbsenteeism("0.25");
                attendance.setCenter_id(punchcard.getCenter_id());
                attendance.setGroup_id(punchcard.getGroup_id());
                attendance.setTeam_id(punchcard.getTeam_id());
                attendance.setYears(DateUtil.format(punchcard.getPunchcardrecord_date(),"YYYY").toString());
                attendance.setMonths(DateUtil.format(punchcard.getPunchcardrecord_date(),"MM").toString());
                attendance.setAttendanceid(UUID.randomUUID().toString());
                attendance.setRecognitionstate(AuthConstants.RECOGNITION_FLAG_NO);
                attendance.setOwner(attendance.getUser_id());
                attendance.preInsert(tokenModel);
                saveAttendance(attendance,"1",tokenModel);
                books[i] = punchcard.getUser_id();
            }
            query_userid.addCriteria(Criteria.where("userid").nin(books));
            List<CustomerInfo> customerInfoList = mongoTemplate.find(query_userid, CustomerInfo.class);
            for (CustomerInfo customerInfo : customerInfoList)
            {
                //if (customerInfo.getUserid().equals("5e02c92ac5e5250ef8cba3b2")) {
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
            //}
            }

            methodAttendance_b(tokenModel,customerInfoList);
            Result.add("失败数：" + error);
            Result.add("成功数：" + accesscount);
            return Result;
        } catch (Exception e) {
            throw new LogicalException(e.getMessage());
        }
    }

    public void methodAttendance_b(TokenModel tokenModel,List<CustomerInfo> customerInfoList) throws Exception
    {
        SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
        SimpleDateFormat sf1ymd = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfxx = new SimpleDateFormat("EEE MMM dd 00:00:00 zzz yyyy", Locale.US);
        DecimalFormat  df = new DecimalFormat("######0.00");
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
        if(attendancesettinglist.size() > 0) {
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

            //昨天的考勤记录
            Date dateStart = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateStart);
            cal.add(Calendar.DAY_OF_MONTH, -1);
            dateStart =  sdfxx.parse(sdfxx.format(cal.getTime()));
            //---------查询考勤表是否有数据start-------
            Attendance attendance = new Attendance();
            attendance.setDates(dateStart);
            List<Attendance> attendancelist = attendanceMapper.select(attendance);
            if(attendancelist.size() > 0){
                for (Attendance ad : attendancelist) {
                    //if (ad.getUser_id().equals("5e0ee8a8c0911e1c24f1a57c")) {
                        WorkingDay workDay = new WorkingDay();
                        workDay.setYears(DateUtil.format(new Date(),"YYYY").toString());
                        workDay.setWorkingdate(sf1ymd.parse(sf1ymd.format(dateStart)));
                        List<WorkingDay> workingDaysList = workingDayMapper.select(workDay);

                        //判断当天是否是休日，青年节，妇女节，周六周日
                        if(workingDaysList.size()>0)
                        {
                            workinghours = "0";
                        }
                        else if(sf1ymd.format(dateStart).equals(DateUtil.format(new Date(),"YYYY").toString() + "-03-08") || sf1ymd.format(dateStart).equals(DateUtil.format(new Date(),"YYYY").toString() + "-05-04"))
                        {
                            workinghours = "4";
                        }
                        else if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
                        {
                            workinghours = "0";
                        }
                        else
                        {
                            workinghours = "8";
                        }

                        CustomerInfo customer =new CustomerInfo();
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
                            punchcardRecord.setPunchcardrecord_date(dateStart);
                            List<PunchcardRecord> punchcardRecordlist = punchcardrecordMapper.select(punchcardRecord);
                            // if有打卡记录  else 没有打卡记录
                            if (punchcardRecordlist.size() > 0) {
                                for (PunchcardRecord PR : punchcardRecordlist) {
                                    String time_start = sdf.format(PR.getTime_start());
                                    String time_end = sdf.format(PR.getTime_end());
                                    int i = 0; // 代休-特殊
                                    int j = 0; // 年休
                                    //---------处理昨日审批通过的加班申请start-------
                                    Overtime overtime = new Overtime();
                                    overtime.setUserid(ad.getUser_id());
                                    overtime.setRecognitionstate(AuthConstants.RECOGNITION_FLAG_NO);
                                    overtime.setReserveovertimedate(dateStart);
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
                                            String overtimeHours = null;
                                            //加班后应该下班的时间
                                            String overtime_end = null;
                                            if(sdf.parse(time_end).getTime() < sdf.parse(closingtime_end).getTime())
                                            {
                                                //18点之前下班
                                                //实际加班时间
                                                overtimeHours = "0";
                                            }
                                            else
                                            {
                                                //18点之后下班
                                                long result1 = sdf.parse(time_end).getTime() - sdf.parse(closingtime_end).getTime();
                                                //实际加班时间
                                                overtimeHours = String.valueOf(Double.valueOf(String.valueOf(result1))/ 60 / 60 / 1000);
                                            }

                                            if(Ot.getStatus().equals("7"))
                                            {
                                                if(Double.valueOf(overtimeHours) > Double.valueOf(actualoverTime))
                                                {
                                                    overtimeHours = String.valueOf(Double.valueOf(actualoverTime));
                                                }
                                                else
                                                {
                                                    overtimeHours = String.valueOf(Double.valueOf(overtimeHours));
                                                }
                                            }
                                            else
                                            {
                                                if(Double.valueOf(overtimeHours) > Double.valueOf(reserveoverTime))
                                                {
                                                    overtimeHours = String.valueOf(Double.valueOf(reserveoverTime));
                                                }
                                                else
                                                {
                                                    overtimeHours = String.valueOf(Double.valueOf(overtimeHours));
                                                }
                                            }

                                            if(Ot.getOvertimetype().equals("PR001007") || Ot.getOvertimetype().equals("PR001008") || Ot.getOvertimetype().equals("PR001005"))
                                            {
                                                //五四青年节  妇女节   会社特别休日加班
                                                if(!actualoverTime.equals("0"))
                                                {
                                                    overtimeHours = actualoverTime;
                                                }
                                                else
                                                {
                                                    overtimeHours = reserveoverTime;
                                                }
                                            }
                                            else if(Ot.getOvertimetype().equals("PR001002") || Ot.getOvertimetype().equals("PR001003") || Ot.getOvertimetype().equals("PR001004"))
                                            {
                                                //周末加班/法定日加班/一齐年休日加班
                                                overtimeHours = timeLength(time_start,time_end,lunchbreak_start,lunchbreak_end);
                                                if(!actualoverTime.equals("0"))
                                                {
                                                    if(Double.valueOf(overtimeHours) > Double.valueOf(actualoverTime))
                                                    {
                                                        overtimeHours = actualoverTime;
                                                    }
                                                }
                                                else
                                                {
                                                    if(Double.valueOf(overtimeHours) > Double.valueOf(reserveoverTime))
                                                    {
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
                                                if (Double.valueOf(overtimeHours) % ((Double.valueOf(strovertime))/60/60/1000)==0) {
                                                    ad.setOrdinaryindustry(Double.valueOf(overtimeHours) ==0?null:overtimeHours);
                                                }
                                                else
                                                {
                                                    ad.setOrdinaryindustry(df.format(Math.floor(Double.valueOf(overtimeHours) / ((Double.valueOf(strovertime))/60/60/1000))*((Double.valueOf(strovertime))/60/60/1000)));
                                                }
                                            } else if (Ot.getOvertimetype().equals("PR001002")) {//周末加班
                                                if (ad.getWeekendindustry() != null && !ad.getWeekendindustry().isEmpty()) {
                                                    overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours) + Double.valueOf(ad.getWeekendindustry())));
                                                } else {
                                                    overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours)));
                                                }
                                                //加班时间大于等于15分
                                                if (Double.valueOf(overtimeHours) % ((Double.valueOf(strovertime))/60/60/1000)==0) {
                                                    ad.setWeekendindustry(Double.valueOf(overtimeHours) ==0?null:overtimeHours);
                                                }
                                                else
                                                {
                                                    ad.setWeekendindustry(df.format(Math.floor(Double.valueOf(overtimeHours) / ((Double.valueOf(strovertime))/60/60/1000))*((Double.valueOf(strovertime))/60/60/1000)));
                                                }
                                            } else if (Ot.getOvertimetype().equals("PR001003")) {//法定日加班
                                                if (ad.getStatutoryresidue() != null && !ad.getStatutoryresidue().isEmpty()) {
                                                    overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours) + Double.valueOf(ad.getStatutoryresidue())));
                                                } else {
                                                    overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours)));
                                                }
                                                //加班时间大于等于15分
                                                if (Double.valueOf(overtimeHours) % ((Double.valueOf(strovertime))/60/60/1000)==0) {
                                                    ad.setStatutoryresidue(Double.valueOf(overtimeHours) ==0?null:overtimeHours);
                                                }
                                                else
                                                {
                                                    ad.setStatutoryresidue(df.format(Math.floor(Double.valueOf(overtimeHours) / ((Double.valueOf(strovertime))/60/60/1000))*((Double.valueOf(strovertime))/60/60/1000)));
                                                }
                                            } else if (Ot.getOvertimetype().equals("PR001004")) {//一齐年休日加班
                                                if (ad.getAnnualrestday() != null && !ad.getAnnualrestday().isEmpty()) {
                                                    overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours) + Double.valueOf(ad.getAnnualrestday())));
                                                }else {
                                                    overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours)));
                                                }

                                                //加班时间大于等于15分
                                                if (Double.valueOf(overtimeHours) % ((Double.valueOf(strovertime))/60/60/1000)==0) {
                                                    ad.setAnnualrestday(Double.valueOf(overtimeHours) ==0?null:overtimeHours);
                                                }
                                                else
                                                {
                                                    ad.setAnnualrestday(df.format(Math.floor(Double.valueOf(overtimeHours) / ((Double.valueOf(strovertime))/60/60/1000))*((Double.valueOf(strovertime))/60/60/1000)));
                                                }

                                            } else if (Ot.getOvertimetype().equals("PR001005")) {//会社特别休日加班
                                                if (ad.getSpecialday() != null && !ad.getSpecialday().isEmpty()) {
                                                    overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours) + Double.valueOf(ad.getSpecialday())));
                                                }else {
                                                    overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours)));
                                                }
                                                ad.setSpecialday(Double.valueOf(overtimeHours) ==0?null:overtimeHours);
                                            } else if (Ot.getOvertimetype().equals("PR001007")) {//五四青年节
                                                if (ad.getYouthday() != null && !ad.getYouthday().isEmpty()) {
                                                    overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours) + Double.valueOf(ad.getYouthday())));
                                                }else {
                                                    overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours)));
                                                }
                                                ad.setYouthday(Double.valueOf(overtimeHours) ==0?null:overtimeHours);
                                            } else if (Ot.getOvertimetype().equals("PR001008")) {//妇女节
                                                if (ad.getWomensday() != null && !ad.getWomensday().isEmpty()) {
                                                    overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours) + Double.valueOf(ad.getWomensday())));
                                                }else {
                                                    overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours)));
                                                }
                                                ad.setWomensday(Double.valueOf(overtimeHours) ==0?null:overtimeHours);
                                            }
                                        }
                                    }
                                    //---------处理昨日审批通过的加班申请end-------
                                    //正常的打卡记录
                                    Double resultwork = Double.valueOf(sdf.parse(time_end).getTime() - sdf.parse(time_start).getTime())/60/60/1000;
                                    if ( Double.valueOf(sdf.parse(time_start).getTime()) <= Double.valueOf(sdf.parse(workshift_end).getTime())
                                            && Double.valueOf(sdf.parse(time_end).getTime()) >= Double.valueOf(sdf.parse(closingtime_start).getTime())
                                            && resultwork - Double.valueOf(ad.getAbsenteeism() == null ? "0":ad.getAbsenteeism())>=9) {
                                        ad.setNormal(df.format(Double.valueOf(workinghours)));
                                    }
                                    else //异常打卡的情况
                                    {
                                        String absenteeism = null;
                                        //五四青年节，妇女节
                                        if(workinghours.equals("4"))
                                        {
                                            ad.setNormal(workinghours);
                                            //一条卡达记录记录的请路况
                                            if (sdf.parse(time_start).getTime() == sdf.parse(time_end).getTime()) {
                                                ad.setAbsenteeism(df.format(Double.valueOf(lateearlyleave) * 16));
                                            }
                                        }
                                        else if(workinghours.equals("8"))
                                        {
                                            //一条卡达记录记录的请路况
                                            if (sdf.parse(time_start).getTime() == sdf.parse(time_end).getTime()) {
                                                ad.setAbsenteeism(df.format(Double.valueOf(lateearlyleave) * 32));
                                            }
                                            else if(sdf.parse(time_start).getTime() > sdf.parse(workshift_end).getTime())
                                            {
                                                Double result1 = Double.valueOf(sdf.parse(time_start).getTime() - sdf.parse(workshift_end).getTime())/60/60/1000;
                                                if(sdf.parse(time_end).getTime() < sdf.parse(closingtime_end).getTime())
                                                {
                                                    result1 = result1 + Double.valueOf(sdf.parse(closingtime_end).getTime() - sdf.parse(time_end).getTime())/60/60/1000;
                                                }
                                                ad.setAbsenteeism(df.format(Double.valueOf(ad.getAbsenteeism() == null ? "0":ad.getAbsenteeism()) + Double.valueOf(result1)));
                                            }
                                            else
                                            {
                                                if(sdf.parse(time_start).getTime() > sdf.parse(workshift_start).getTime())
                                                {
                                                    Double result1 = Double.valueOf(sdf.parse(time_start).getTime())/60/60/1000 + 9;
                                                    if(result1 > sdf.parse(time_end).getTime()/60/60/1000)
                                                    {
                                                        ad.setAbsenteeism(df.format(Double.valueOf(ad.getAbsenteeism() == null ? "0":ad.getAbsenteeism()) + Double.valueOf(result1 - (sdf.parse(time_end).getTime()/60/60/1000)) - 1 ));
                                                    }
                                                }
                                                else
                                                {
                                                    if(sdf.parse(closingtime_start).getTime() > sdf.parse(time_end).getTime())
                                                    {
                                                        ad.setAbsenteeism(df.format(Double.valueOf(ad.getAbsenteeism() == null ? "0":ad.getAbsenteeism()) + Double.valueOf(sdf.parse(closingtime_start).getTime() - sdf.parse(time_end).getTime())/60/60/1000));
                                                    }
                                                }
                                            }
                                            //迟到
//                                            if (sdf.parse(time_start).getTime() != sdf.parse(time_end).getTime() &&
//                                                    sdf.parse(time_start).getTime() > sdf.parse(workshift_end).getTime()) {
//                                                //迟到的时间
//                                                long result = sdf.parse(time_start).getTime() - sdf.parse(workshift_start).getTime();
//                                                //应该补的时间
//                                                Double strunit = getUnit(Double.valueOf(result), Double.valueOf(lateearlyleave));
//                                                //迟到的小时
//                                                Double Dhourresult = strunit / 60 / 60 / 1000;
//
//                                                if(ad.getAbsenteeism() != null && !ad.getAbsenteeism().isEmpty()) {
//                                                    absenteeism = String.valueOf(df.format(Double.valueOf(Dhourresult) + Double.valueOf(ad.getAbsenteeism())));
//                                                }
//                                                else
//                                                {
//                                                    absenteeism = String.valueOf(df.format(Double.valueOf(Dhourresult)));
//                                                }
//                                                //迟到大于等于15分
//                                                if (Double.valueOf(absenteeism) % (Double.valueOf(lateearlyleave))==0) {
//                                                    ad.setAbsenteeism(absenteeism);
//                                                }
//                                                else
//                                                {
//                                                    ad.setAbsenteeism(df.format(Math.floor(Double.valueOf(absenteeism) / Double.valueOf(lateearlyleave))*Double.valueOf(lateearlyleave) + Double.valueOf(lateearlyleave)));
//                                                }
//                                            }
//                                            //早退
//                                            if (sdf.parse(time_start).getTime() != sdf.parse(time_end).getTime() &&
//                                                    sdf.parse(closingtime_start).getTime() > sdf.parse(time_end).getTime()) {
//                                                long result = sdf.parse(closingtime_start).getTime() - sdf.parse(time_end).getTime();
//                                                //应该补的时间
//                                                Double strunit = getUnit(Double.valueOf(result), Double.valueOf(lateearlyleave));
//                                                //早退的小时
//                                                Double Dhourresult = strunit / 60 / 60 / 1000;
//                                                if(ad.getAbsenteeism() != null && !ad.getAbsenteeism().isEmpty()) {
//                                                    absenteeism = String.valueOf(df.format(Double.valueOf(Dhourresult) + Double.valueOf(ad.getAbsenteeism())));
//                                                }
//                                                else
//                                                {
//                                                    absenteeism = String.valueOf(df.format(Double.valueOf(Dhourresult)));
//                                                }
//                                                //早退大于等于15分
//                                                if (Double.valueOf(absenteeism) %  (Double.valueOf(lateearlyleave))==0) {
//                                                    ad.setAbsenteeism(absenteeism);
//                                                }
//                                                else
//                                                {
//                                                    ad.setAbsenteeism(df.format(Math.floor(Double.valueOf(absenteeism) / Double.valueOf(lateearlyleave))*Double.valueOf(lateearlyleave) + Double.valueOf(lateearlyleave)));
//                                                }
//                                            }
                                            ad.setAbsenteeism(df.format(Math.floor(Double.valueOf(ad.getAbsenteeism())/ Double.valueOf(lateearlyleave))*Double.valueOf(lateearlyleave) + Double.valueOf(lateearlyleave)));

                                        }
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
                                        if (abNormallist.size() > 0) {
                                            i = 0;
                                            j = 0;
                                            for (AbNormal ab : abNormallist) {
                                                String strlengthtime = null;
                                                if (ab.getStatus().equals("7"))
                                                {
                                                    strlengthtime = ab.getRelengthtime();
                                                }
                                                else
                                                {
                                                    strlengthtime = ab.getLengthtime();
                                                }

                                                //在申请的日期范围内
                                                if (ab.getOccurrencedate().compareTo(sdfxx.parse(dateStart.toString())) <= 0 && ab.getFinisheddate().compareTo(sdfxx.parse(dateStart.toString())) >= 0) {
                                                    if(!(ab.getOccurrencedate().compareTo(sdfxx.parse(dateStart.toString())) == 0 && ab.getFinisheddate().compareTo(sdfxx.parse(dateStart.toString())) == 0))
                                                    {
                                                        strlengthtime = workinghours;
                                                    }
                                                }
                                                else
                                                {
                                                    strlengthtime = "0";
                                                }
                                                if (ab.getErrortype().equals("PR013001")) {//外出
                                                    //ad.setNormal(strlengthtime);
                                                    //外出大于等于15分
                                                    if (Double.valueOf(strlengthtime) %  (Double.valueOf(lateearlyleave))==0) {
                                                        //欠勤里扣除外出时间
                                                        ad.setAbsenteeism(df.format(Double.valueOf(ad.getAbsenteeism())-Double.valueOf(strlengthtime)));
                                                    }
                                                    else
                                                    {
                                                        ad.setAbsenteeism(df.format(Double.valueOf(ad.getAbsenteeism())-Math.floor(Double.valueOf(strlengthtime) / Double.valueOf(lateearlyleave))*Double.valueOf(lateearlyleave)));
                                                    }
                                                } else if (ab.getErrortype().equals("PR013005")) {//年休
                                                    if (ad.getAnnualrest() != null && !ad.getAnnualrest().isEmpty()) {
                                                        strlengthtime = String.valueOf(df.format(Double.valueOf(strlengthtime) + Double.valueOf(ad.getAnnualrest())));
                                                    }
                                                    j=j+1;
                                                    ad.setAnnualrest(strlengthtime);
                                                } else if (ab.getErrortype().equals("PR013006")) {//代休-周末
                                                    if (ad.getDaixiu() != null && !ad.getDaixiu().isEmpty()) {
                                                        strlengthtime = String.valueOf(df.format(Double.valueOf(strlengthtime) + Double.valueOf(ad.getDaixiu())));
                                                    }
                                                    //代休-周末大于等于15分
                                                    if (Double.valueOf(strlengthtime) %  (Double.valueOf(lateearlyleave))==0) {
                                                        ad.setDaixiu(strlengthtime);
                                                    }
                                                    else
                                                    {
                                                        ad.setDaixiu(df.format(Math.floor(Double.valueOf(strlengthtime) / Double.valueOf(lateearlyleave))*Double.valueOf(lateearlyleave) + Double.valueOf(lateearlyleave)));
                                                    }

                                                    ad.setDaixiu(strlengthtime);
                                                }else if (ab.getErrortype().equals("PR013007")) {//代休-特殊
                                                    if (ad.getDaixiu() != null && !ad.getDaixiu().isEmpty()) {
                                                        strlengthtime = String.valueOf(df.format(Double.valueOf(strlengthtime) + Double.valueOf(ad.getDaixiu())));
                                                    }
                                                    i=i+1;
                                                    ad.setDaixiu(strlengthtime);
                                                }else if (ab.getErrortype().equals("PR013008")) {//事休
                                                    if (ad.getCompassionateleave() != null && !ad.getCompassionateleave().isEmpty()) {
                                                        strlengthtime = String.valueOf(df.format(Double.valueOf(strlengthtime) + Double.valueOf(ad.getCompassionateleave())));
                                                    }
                                                    //事休大于等于15分
                                                    if (Double.valueOf(strlengthtime) %  (Double.valueOf(compassionateleave))==0) {
                                                        ad.setCompassionateleave(strlengthtime);
                                                    }
                                                    else
                                                    {
                                                        ad.setCompassionateleave(df.format(Math.floor(Double.valueOf(strlengthtime) / Double.valueOf(compassionateleave))*Double.valueOf(compassionateleave) + Double.valueOf(compassionateleave)));
                                                    }
                                                } else if (ab.getErrortype().equals("PR013009")) {//短期病休
                                                    if (ad.getShortsickleave() != null && !ad.getShortsickleave().isEmpty()) {
                                                        strlengthtime = String.valueOf(df.format(Double.valueOf(strlengthtime) + Double.valueOf(ad.getShortsickleave())));
                                                    }
                                                    //短期病休大于等于15分
                                                    if (Double.valueOf(strlengthtime) %  (Double.valueOf(lateearlyleave))==0) {
                                                        ad.setShortsickleave(strlengthtime);
                                                    }
                                                    else
                                                    {
                                                        ad.setShortsickleave(df.format(Math.floor(Double.valueOf(strlengthtime) / Double.valueOf(lateearlyleave))*Double.valueOf(lateearlyleave) + Double.valueOf(lateearlyleave)));
                                                    }
                                                } else if (ab.getErrortype().equals("PR013010")) {//長期病休
                                                    if (ad.getLongsickleave() != null && !ad.getLongsickleave().isEmpty()) {
                                                        strlengthtime = String.valueOf(df.format(Double.valueOf(strlengthtime) + Double.valueOf(ad.getLongsickleave())));
                                                    }
                                                    //長期病休大于等于15分
                                                    if (Double.valueOf(strlengthtime) %  (Double.valueOf(lateearlyleave))==0) {
                                                        ad.setLongsickleave(strlengthtime);
                                                    }
                                                    else
                                                    {
                                                        ad.setLongsickleave(df.format(Math.floor(Double.valueOf(strlengthtime) / Double.valueOf(lateearlyleave))*Double.valueOf(lateearlyleave) + Double.valueOf(lateearlyleave)));
                                                    }
                                                } else if (ab.getErrortype().equals("PR013012") || ab.getErrortype().equals("PR013013")) { //産休（女） 护理假（男）

                                                    if (ad.getNursingleave() != null && !ad.getNursingleave().isEmpty()) {
                                                        strlengthtime = String.valueOf(df.format(Double.valueOf(strlengthtime) + Double.valueOf(ad.getNursingleave())));
                                                    }
                                                    ad.setNursingleave(strlengthtime);
                                                } else if (ab.getErrortype().equals("PR013014") || ab.getErrortype().equals("PR013016")
                                                        || ab.getErrortype().equals("PR013018") || ab.getErrortype().equals("PR013019")
                                                        || ab.getErrortype().equals("PR013011") || ab.getErrortype().equals("PR013015")
                                                        || ab.getErrortype().equals("PR013004") || ab.getErrortype().equals("PR013017")
                                                        || ab.getErrortype().equals("PR013020")) {
                                                    //福利假期
                                                    //家长会假//妊娠檢查休暇
                                                    // 労災休暇//其他休暇
                                                    //婚假 //丧假
                                                    //流产假 //计划生育手术假//工伤
                                                    if (ad.getWelfare() != null && !ad.getWelfare().isEmpty()) {
                                                        strlengthtime = String.valueOf(df.format(Double.valueOf(strlengthtime) + Double.valueOf(ad.getWelfare())));
                                                    }
                                                    if (Double.valueOf(strlengthtime) %  (Double.valueOf(lateearlyleave))==0) {
                                                        ad.setWelfare(strlengthtime);
                                                    }
                                                    else
                                                    {
                                                        ad.setWelfare(df.format(Math.floor(Double.valueOf(strlengthtime) / Double.valueOf(lateearlyleave))*Double.valueOf(lateearlyleave) + Double.valueOf(lateearlyleave)));
                                                    }
                                                }
                                            }

                                        } else {
                                            //有打卡记录 没有申请
                                            ad.setNormal(df.format(Double.valueOf(workinghours) - Double.valueOf(ad.getAbsenteeism())));
                                        }

                                        //---------处理昨日审批通过的异常考勤申请end-------
                                    }
                                    ad.setNormal(ad.getNormal() == null ? "0" :ad.getNormal());
                                    ad.setAnnualrest(ad.getAnnualrest()==null ? "0"  :ad.getAnnualrest());
                                    ad.setDaixiu(ad.getDaixiu()==null ? "0" :ad.getDaixiu());
                                    ad.setCompassionateleave(ad.getCompassionateleave()==null? "0" :ad.getCompassionateleave());
                                    ad.setShortsickleave(ad.getShortsickleave()==null ? "0" :ad.getShortsickleave());
                                    ad.setLongsickleave(ad.getLongsickleave()==null ? "0" :ad.getLongsickleave());
                                    ad.setNursingleave(ad.getNursingleave()==null ? "0" :ad.getNursingleave());
                                    ad.setWelfare(ad.getWelfare()==null ? "0" :ad.getWelfare());

                                    //青年节，妇女节换代休
                                    if(workinghours.equals("4"))
                                    {
                                        String leavetime = String.valueOf(Double.valueOf(ad.getShortsickleave()) + Double.valueOf(ad.getLongsickleave())
                                                + Double.valueOf(ad.getCompassionateleave()) + Double.valueOf(ad.getAnnualrest())
                                                + Double.valueOf(ad.getDaixiu()) + Double.valueOf(ad.getNursingleave()) + Double.valueOf(ad.getWelfare()));
                                        String shijiworkHours ="0";
                                        if(sdf.parse(time_end).getTime() <= sdf.parse(lunchbreak_start).getTime() && sdf.parse(time_start).getTime() >= sdf.parse(lunchbreak_end).getTime())
                                        {
                                           long result1 = sdf.parse(time_end).getTime() - sdf.parse(time_start).getTime();
                                            shijiworkHours = String.valueOf((Double.valueOf(String.valueOf(result1)) / 60 / 60 / 1000)-Double.valueOf(ad.getAbsenteeism()));
                                        }
                                        else if(sdf.parse(time_start).getTime() <= sdf.parse(lunchbreak_start).getTime() && sdf.parse(time_end).getTime() < sdf.parse(lunchbreak_end).getTime())
                                        {
                                            long result1 = sdf.parse(lunchbreak_start).getTime() - sdf.parse(time_start).getTime();
                                            shijiworkHours = String.valueOf((Double.valueOf(String.valueOf(result1)) / 60 / 60 / 1000)-Double.valueOf(ad.getAbsenteeism()));
                                        }
                                        else if(sdf.parse(time_start).getTime() > sdf.parse(lunchbreak_start).getTime() && sdf.parse(time_end).getTime() >= sdf.parse(lunchbreak_end).getTime())
                                        {
                                            long result1 = sdf.parse(time_end).getTime() - sdf.parse(lunchbreak_end).getTime();
                                            shijiworkHours = String.valueOf((Double.valueOf(String.valueOf(result1)) / 60 / 60 / 1000)-Double.valueOf(ad.getAbsenteeism()));
                                        }
                                        else
                                        {
                                            //打卡开始-结束时间
                                            long result1 = sdf.parse(time_end).getTime() - sdf.parse(time_start).getTime();
                                            //午休时间
                                            long result2 = sdf.parse(lunchbreak_end).getTime() - sdf.parse(lunchbreak_start).getTime();
                                            //打卡记录加班时间
                                            Double result3 = Double.valueOf(String.valueOf(result1 - result2)) / 60 / 60 / 1000;
                                            shijiworkHours = String.valueOf(result3 - Double.valueOf(ad.getAbsenteeism()));
                                            if(Double.valueOf(shijiworkHours) >= 8)
                                            {
                                                result1 = sdf.parse(lunchbreak_start).getTime() - sdf.parse(time_start).getTime();
                                                result2 = sdf.parse(time_end).getTime() - sdf.parse(lunchbreak_end).getTime();
                                                if(result1 >=4 && result2>=4)
                                                {
                                                    shijiworkHours = shijiworkHours;
                                                }
                                                else
                                                {
                                                    result3 = Double.valueOf(String.valueOf(result1 > result2 ? result1 : result2)) / 60 / 60 / 1000;
                                                    shijiworkHours = String.valueOf(result3);
                                                }
                                            }
                                            else
                                            {
                                                //上午上班时间
                                                result1 = sdf.parse(lunchbreak_start).getTime() - sdf.parse(time_start).getTime();
                                                //下午上班时间
                                                result2 = sdf.parse(time_end).getTime() - sdf.parse(lunchbreak_end).getTime();
                                                result3 = Double.valueOf(String.valueOf(result1 > result2 ? result1 : result2)) / 60 / 60 / 1000;
                                                shijiworkHours = String.valueOf(result3);
                                            }
                                        }
                                        shijiworkHours = df.format(Math.floor(Double.valueOf(shijiworkHours) / ((Double.valueOf(strovertime))/60/60/1000))*((Double.valueOf(strovertime))/60/60/1000));
                                        //青年节，妇女节换代休
                                        if(Double.valueOf(shijiworkHours)>= Double.valueOf(workinghours) *2)
                                        {
                                            ad.setNormal(df.format(Double.valueOf(workinghours)-Double.valueOf(ad.getAbsenteeism())));

                                            if(!(ad.getWomensday() == null || ad.getWomensday().isEmpty()))
                                            {
                                                ad.setWomensday(df.format(Double.valueOf(workinghours)));
                                                //换代休0.5天
                                                String duration = "4";
                                                //操作代休表
                                                insertReplace(ad,tokenModel,"2",duration);
                                                if(!(ad.getOrdinaryindustry() == null || ad.getOrdinaryindustry().isEmpty()))
                                                {
                                                    if( Double.valueOf(shijiworkHours) > 8)
                                                    {
                                                        if(Double.valueOf(ad.getOrdinaryindustry()) > Double.valueOf(shijiworkHours) - 8)
                                                        {
                                                            ad.setOrdinaryindustry(df.format(Double.valueOf(shijiworkHours) - 8));
                                                        }
                                                    }
                                                    else
                                                    {
                                                        ad.setOrdinaryindustry(null);
                                                    }
                                                }
                                            }
                                            else if(!(ad.getYouthday() == null || ad.getYouthday().isEmpty()))
                                            {
                                                ad.setYouthday(df.format(Double.valueOf(workinghours)));
                                                //换代休0.5天
                                                String duration = "4";
                                                //操作代休表
                                                insertReplace(ad,tokenModel,"2",duration);
                                                if(!(ad.getOrdinaryindustry() == null || ad.getOrdinaryindustry().isEmpty()))
                                                {
                                                    if( Double.valueOf(shijiworkHours) > 8)
                                                    {
                                                        if(Double.valueOf(ad.getOrdinaryindustry()) > Double.valueOf(shijiworkHours) - 8)
                                                        {
                                                            ad.setOrdinaryindustry(df.format(Double.valueOf(shijiworkHours) - 8));
                                                        }
                                                    }
                                                    else
                                                    {
                                                        ad.setOrdinaryindustry(null);
                                                    }
                                                }
                                            }
                                            else if(!(ad.getOrdinaryindustry() == null || ad.getOrdinaryindustry().isEmpty()))
                                            {
                                                if(Double.valueOf(ad.getOrdinaryindustry()) > 4 && Double.valueOf(shijiworkHours) > 8)
                                                {
                                                    ad.setOrdinaryindustry(df.format(Double.valueOf(shijiworkHours) - 8));
                                                    //换代休0.5天
                                                    String duration = "4";
                                                    //操作代休表
                                                    insertReplace(ad,tokenModel,"2",duration);
                                                }
                                                else
                                                {
                                                    ad.setOrdinaryindustry(null);
                                                }
                                            }
                                        }
                                        else if(Double.valueOf(shijiworkHours)>= Double.valueOf(workinghours) && Double.valueOf(shijiworkHours) <=Double.valueOf(workinghours) *2)
                                        {
                                            ad.setNormal(df.format(Double.valueOf(workinghours)-Double.valueOf(ad.getAbsenteeism())));
                                            if(Double.valueOf(leavetime) + Double.valueOf(shijiworkHours) >= Double.valueOf(workinghours) *2)
                                            {
                                                if(!(ad.getWomensday() == null || ad.getWomensday().isEmpty()))
                                                {
                                                    ad.setWomensday(df.format(Double.valueOf(workinghours)));
                                                    //换代休0.5天
                                                    String duration = "4";
                                                    //操作代休表
                                                    insertReplace(ad,tokenModel,"2",duration);
                                                    if(!(ad.getOrdinaryindustry() == null || ad.getOrdinaryindustry().isEmpty()))
                                                    {
                                                        if( Double.valueOf(shijiworkHours) > 8)
                                                        {
                                                            if(Double.valueOf(ad.getOrdinaryindustry()) > Double.valueOf(leavetime) + Double.valueOf(shijiworkHours) - 8)
                                                            {
                                                                ad.setOrdinaryindustry(df.format(Double.valueOf(leavetime) + Double.valueOf(shijiworkHours) - 8));
                                                            }
                                                        }
                                                        else
                                                        {
                                                            ad.setOrdinaryindustry(null);
                                                        }
                                                    }
                                                }
                                                else if(!(ad.getYouthday() == null || ad.getYouthday().isEmpty()))
                                                {
                                                    ad.setYouthday(df.format(Double.valueOf(workinghours)));
                                                    //换代休0.5天
                                                    String duration = "4";
                                                    //操作代休表
                                                    insertReplace(ad,tokenModel,"2",duration);
                                                    if(!(ad.getOrdinaryindustry() == null || ad.getOrdinaryindustry().isEmpty()))
                                                    {
                                                        if( Double.valueOf(leavetime) + Double.valueOf(shijiworkHours) > 8)
                                                        {
                                                            if(Double.valueOf(ad.getOrdinaryindustry()) > Double.valueOf(leavetime) + Double.valueOf(shijiworkHours) - 8)
                                                            {
                                                                ad.setOrdinaryindustry(df.format(Double.valueOf(leavetime) + Double.valueOf(shijiworkHours) - 8));
                                                            }
                                                        }
                                                        else
                                                        {
                                                            ad.setOrdinaryindustry(null);
                                                        }
                                                    }
                                                }
                                                else if(!(ad.getOrdinaryindustry() == null || ad.getOrdinaryindustry().isEmpty()))
                                                {
                                                    if(Double.valueOf(ad.getOrdinaryindustry()) > 4 && Double.valueOf(leavetime) + Double.valueOf(shijiworkHours) > 8)
                                                    {
                                                        ad.setOrdinaryindustry(df.format(Double.valueOf(leavetime) + Double.valueOf(shijiworkHours) - 8));
                                                        //换代休0.5天
                                                        String duration = "4";
                                                        //操作代休表
                                                        insertReplace(ad,tokenModel,"2",duration);
                                                    }
                                                    else
                                                    {
                                                        ad.setOrdinaryindustry(null);
                                                    }
                                                }
                                            }
                                            else
                                            {
                                                ad.setWomensday(null);
                                                ad.setYouthday(null);
                                            }
                                        }
                                        else
                                        {
                                            ad.setNormal(df.format(Math.floor(Double.valueOf(shijiworkHours) / ((Double.valueOf(strovertime))/60/60/1000))*((Double.valueOf(strovertime))/60/60/1000)));
                                            ad.setAbsenteeism(df.format(Double.valueOf(workinghours) - Double.valueOf(ad.getNormal()) -  Double.valueOf(leavetime)));
                                            ad.setWomensday(null);
                                            ad.setYouthday(null);
                                        }
                                    }
                                    else if(workinghours.equals("8"))
                                    {
                                        //申请了年休，代休
                                        if( (Double.valueOf(ad.getAnnualrest()) > 0 || Double.valueOf(ad.getDaixiu())>0) && (sdf.parse(time_start).getTime() <= sdf.parse(lunchbreak_start).getTime() && sdf.parse(time_end).getTime() >= sdf.parse(lunchbreak_end).getTime()
                                                && !(Double.valueOf(sdf.parse(time_start).getTime()) <= Double.valueOf(sdf.parse(workshift_end).getTime())
                                                    && Double.valueOf(sdf.parse(time_end).getTime()) >= Double.valueOf(sdf.parse(closingtime_start).getTime())
                                                    && resultwork - Double.valueOf(ad.getAbsenteeism() == null ? "0":ad.getAbsenteeism())>=9)))
                                        {
                                            String leavetime = "0";
                                            //申请代休，没有申请年休
                                            if(i>0 && j==0)
                                            {
                                                leavetime = String.valueOf(Double.valueOf(ad.getShortsickleave()) + Double.valueOf(ad.getLongsickleave())
                                                        + Double.valueOf(ad.getCompassionateleave()) + Double.valueOf(ad.getDaixiu()) + Double.valueOf(ad.getNursingleave()) + Double.valueOf(ad.getWelfare()));
                                            }
                                            //申请代休周末，申请年休
                                            if(j>0 && i == 0 )
                                            {
                                                leavetime = String.valueOf(Double.valueOf(ad.getShortsickleave()) + Double.valueOf(ad.getLongsickleave())
                                                        + Double.valueOf(ad.getCompassionateleave()) + Double.valueOf(ad.getDaixiu()) + Double.valueOf(ad.getAnnualrest()) + Double.valueOf(ad.getNursingleave()) + Double.valueOf(ad.getWelfare()));
                                            }
                                            if(i>0 && j>0)
                                            {
                                                leavetime = workinghours;
                                            }

                                            //上午上班时间
                                            long result1 = sdf.parse(lunchbreak_start).getTime() - sdf.parse(time_start).getTime();
                                            //下午上班时间
                                            long result2 = sdf.parse(time_end).getTime() - sdf.parse(lunchbreak_end).getTime();
                                            Double result3 = Double.valueOf(String.valueOf(result1 > result2 ? result1 : result2)) / 60 / 60 / 1000;

                                            if(Double.valueOf(leavetime)<8)
                                            {

                                                ad.setNormal(df.format(Math.floor(Double.valueOf(result3) / Double.valueOf(lateearlyleave))*Double.valueOf(lateearlyleave)));
                                                ad.setAbsenteeism(df.format(Double.valueOf(workinghours) - Double.valueOf(leavetime) -Double.valueOf(ad.getNormal()) ));
                                            }
                                            else
                                            {
                                                ad.setNormal("0");
                                                ad.setAbsenteeism("0");
                                            }
                                        }
                                        else
                                        {
                                            ad.setAbsenteeism(df.format(Double.valueOf(ad.getAbsenteeism()) - Double.valueOf(ad.getShortsickleave())
                                                    - Double.valueOf(ad.getLongsickleave()) - Double.valueOf(ad.getCompassionateleave()) - Double.valueOf(ad.getAnnualrest())
                                                    - Double.valueOf(ad.getDaixiu()) - Double.valueOf(ad.getNursingleave()) - Double.valueOf(ad.getWelfare())));
                                            if(Double.valueOf(ad.getAbsenteeism())  >=  Double.valueOf(workinghours) )
                                            {
                                                ad.setNormal("0");
                                            }
                                            else if(Double.valueOf(ad.getAbsenteeism())<0)
                                            {
                                                ad.setNormal("0");
                                            }
                                            else
                                            {
                                                ad.setNormal(df.format(Double.valueOf(workinghours) - Double.valueOf(ad.getAbsenteeism()) -  Double.valueOf(ad.getShortsickleave())
                                                        - Double.valueOf(ad.getLongsickleave()) - Double.valueOf(ad.getCompassionateleave()) - Double.valueOf(ad.getAnnualrest())
                                                        - Double.valueOf(ad.getDaixiu()) - Double.valueOf(ad.getNursingleave()) - Double.valueOf(ad.getWelfare())));
                                            }
                                        }
                                    }
                                    else
                                    {
                                        if (ad.getWeekendindustry() != null && !ad.getWeekendindustry().isEmpty())
                                        {
                                            if(Double.valueOf(ad.getWeekendindustry())>=8)
                                            {
                                                //换代休1天
                                                String duration = "8";
                                                //操作代休表
                                                insertReplace(ad,tokenModel,"1",duration);
                                                //周末加班除去换代休的时间
                                                ad.setWeekendindustry(df.format(Double.valueOf(ad.getWeekendindustry()) - 8));
                                            }
                                        }
                                        else if (ad.getSpecialday() != null && !ad.getSpecialday().isEmpty())
                                        {
                                            String shijiworkHours ="0";
                                            if(sdf.parse(time_end).getTime() <= sdf.parse(lunchbreak_start).getTime() && sdf.parse(time_start).getTime() >= sdf.parse(lunchbreak_end).getTime())
                                            {
                                                long result1 = sdf.parse(time_end).getTime() - sdf.parse(time_start).getTime();
                                                shijiworkHours = String.valueOf((Double.valueOf(String.valueOf(result1)) / 60 / 60 / 1000)-Double.valueOf(ad.getAbsenteeism()));
                                            }
                                            else if(sdf.parse(time_start).getTime() <= sdf.parse(lunchbreak_start).getTime() && sdf.parse(time_end).getTime() < sdf.parse(lunchbreak_end).getTime())
                                            {
                                                long result1 = sdf.parse(lunchbreak_start).getTime() - sdf.parse(time_start).getTime();
                                                shijiworkHours = String.valueOf((Double.valueOf(String.valueOf(result1)) / 60 / 60 / 1000)-Double.valueOf(ad.getAbsenteeism()));
                                            }
                                            else if(sdf.parse(time_start).getTime() > sdf.parse(lunchbreak_start).getTime() && sdf.parse(time_end).getTime() >= sdf.parse(lunchbreak_end).getTime())
                                            {
                                                long result1 = sdf.parse(time_end).getTime() - sdf.parse(lunchbreak_end).getTime();
                                                shijiworkHours = String.valueOf((Double.valueOf(String.valueOf(result1)) / 60 / 60 / 1000)-Double.valueOf(ad.getAbsenteeism()));
                                            }
                                            else
                                            {
                                                //打卡开始-结束时间
                                                long result1 = sdf.parse(time_end).getTime() - sdf.parse(time_start).getTime();
                                                //午休时间
                                                long result2 = sdf.parse(lunchbreak_end).getTime() - sdf.parse(lunchbreak_start).getTime();
                                                //打卡记录加班时间
                                                Double result3 = Double.valueOf(String.valueOf(result1 - result2)) / 60 / 60 / 1000;
                                                shijiworkHours = String.valueOf(result3 - Double.valueOf(ad.getAbsenteeism()));
                                                if(Double.valueOf(shijiworkHours) >= 8)
                                                {
                                                    result1 = sdf.parse(lunchbreak_start).getTime() - sdf.parse(time_start).getTime();
                                                    result2 = sdf.parse(time_end).getTime() - sdf.parse(lunchbreak_end).getTime();
                                                    if(result1 >=4 && result2>=4)
                                                    {
                                                        shijiworkHours = shijiworkHours;
                                                    }
                                                    else
                                                    {
                                                        //上午上班时间
                                                        result1 = sdf.parse(lunchbreak_start).getTime() - sdf.parse(time_start).getTime();
                                                        //下午上班时间
                                                        result2 = sdf.parse(time_end).getTime() - sdf.parse(lunchbreak_end).getTime();
                                                        result3 = Double.valueOf(String.valueOf(result1 > result2 ? result1 : result2)) / 60 / 60 / 1000;
                                                        shijiworkHours = String.valueOf(result3);
                                                    }
                                                }
                                                else
                                                {
                                                    //上午上班时间
                                                    result1 = sdf.parse(lunchbreak_start).getTime() - sdf.parse(time_start).getTime();
                                                    //下午上班时间
                                                    result2 = sdf.parse(time_end).getTime() - sdf.parse(lunchbreak_end).getTime();
                                                    result3 = Double.valueOf(String.valueOf(result1 > result2 ? result1 : result2)) / 60 / 60 / 1000;
                                                    shijiworkHours = String.valueOf(result3);
                                                }
                                            }
                                            String duration = null;
                                            if(Double.valueOf(shijiworkHours)>=8)
                                            {
                                                //特别休日
                                                //换代休1天
                                                duration = "8";
                                                //操作代休表
                                                insertReplace(ad,tokenModel,"2",duration);

                                            }
                                            else if(Double.valueOf(shijiworkHours)>=4 && Double.valueOf(shijiworkHours)<8)
                                            {
                                                //特别休日
                                                //换代休0.5天
                                                duration = "4";
                                                //操作代休表
                                                insertReplace(ad,tokenModel,"2",duration);
                                            }
                                            ad.setSpecialday(duration == null ? null:df.format(Double.valueOf(duration)));
                                        }
                                    }
                                    ad.setNormal(ad.getNormal() == null || ad.getNormal() =="0" ? null : df.format(Double.valueOf(ad.getNormal())));
                                    ad.setAnnualrest(Double.valueOf(ad.getAnnualrest()) <= 0 ? null  :df.format(Double.valueOf(ad.getAnnualrest())));
                                    ad.setDaixiu(Double.valueOf(ad.getDaixiu()) <= 0 ? null :df.format(Double.valueOf(ad.getDaixiu())));
                                    ad.setCompassionateleave(Double.valueOf(ad.getCompassionateleave()) <= 0 ? null :df.format(Double.valueOf(ad.getCompassionateleave())));
                                    ad.setShortsickleave(Double.valueOf(ad.getShortsickleave()) <= 0 ? null :df.format(Double.valueOf(ad.getShortsickleave())));
                                    ad.setLongsickleave(Double.valueOf(ad.getLongsickleave()) <= 0 ? null :df.format(Double.valueOf(ad.getLongsickleave())));
                                    ad.setNursingleave(Double.valueOf(ad.getNursingleave()) <= 0 ? null :df.format(Double.valueOf(ad.getNursingleave())));
                                    ad.setWelfare(Double.valueOf(ad.getWelfare()) <= 0 ? null :df.format(Double.valueOf(ad.getWelfare())));
                                    ad.setAbsenteeism(Double.valueOf(ad.getAbsenteeism()) <= 0 ? null :df.format(Double.valueOf(ad.getAbsenteeism())));

                                    //更新考勤表
                                    if(customerInfo.getUserinfo().getEnddate() == null || customerInfo.getUserinfo().getEnddate().isEmpty())
                                    {
                                        ad.setTshortsickleave(ad.getShortsickleave());
                                        ad.setTlongsickleave(ad.getLongsickleave());
                                        ad.setTabsenteeism(ad.getAbsenteeism());
                                        ad.setShortsickleave(null);
                                        ad.setLongsickleave(null);
                                        ad.setAbsenteeism(null);
                                    }
                                    else
                                    {
                                        if (sf1ymd.parse(customerInfo.getUserinfo().getEnddate()).getTime() > dateStart.getTime())
                                        {
                                            ad.setTshortsickleave(ad.getShortsickleave());
                                            ad.setTlongsickleave(ad.getLongsickleave());
                                            ad.setTabsenteeism(ad.getAbsenteeism());
                                            ad.setShortsickleave(null);
                                            ad.setLongsickleave(null);
                                            ad.setAbsenteeism(null);
                                        }
                                    }

                                    if(workinghours.equals("0"))
                                    {
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
                                    saveAttendance(ad, "0", tokenModel);
                                }
                            }
                            else {
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
                                if (abNormallist.size() > 0) {
                                    for (AbNormal ab : abNormallist) {
                                        //在申请的日期范围内
                                        String strlengthtime = null;
                                        if (ab.getStatus().equals("7"))
                                        {
                                            strlengthtime = ab.getRelengthtime();
                                        }
                                        else
                                        {
                                            strlengthtime = ab.getLengthtime();
                                        }
                                        if (ab.getOccurrencedate().compareTo(sdfxx.parse(dateStart.toString())) <= 0 && ab.getFinisheddate().compareTo(sdfxx.parse(dateStart.toString())) >= 0) {
                                            if(!(ab.getOccurrencedate().compareTo(sdfxx.parse(dateStart.toString())) == 0 && ab.getFinisheddate().compareTo(sdfxx.parse(dateStart.toString())) == 0))
                                            {
                                                strlengthtime = workinghours;
                                            }
                                        }
                                        else
                                        {
                                            strlengthtime = "0";
                                        }
                                        //是否是工作日，青年节，妇女节，休日
                                        ad.setAbsenteeism(workinghours);

                                        if (ab.getErrortype().equals("PR013001")) {//外出
                                            if (ad.getNormal() != null && !ad.getNormal().isEmpty()) {
                                                strlengthtime = String.valueOf(df.format(Double.valueOf(strlengthtime) + Double.valueOf(ad.getNormal())));
                                            }
                                            //外出大于等于15分
                                            if (Double.valueOf(strlengthtime) %  (Double.valueOf(lateearlyleave))==0) {
                                                //欠勤里扣除外出时间
                                                strlengthtime=df.format(Double.valueOf(ad.getAbsenteeism())-Double.valueOf(strlengthtime));
                                            }
                                            else
                                            {
                                                strlengthtime=df.format(Double.valueOf(ad.getAbsenteeism())-Math.floor(Double.valueOf(strlengthtime) / Double.valueOf(lateearlyleave))*Double.valueOf(lateearlyleave));
                                            }
                                            ad.setAbsenteeism(strlengthtime);
                                        } else if (ab.getErrortype().equals("PR013005")) {//年休
                                            if (ad.getAnnualrest() != null && !ad.getAnnualrest().isEmpty()) {
                                                strlengthtime = String.valueOf(df.format(Double.valueOf(strlengthtime) + Double.valueOf(ad.getAnnualrest())));
                                            }
                                            ad.setAnnualrest(strlengthtime);
                                        } else if (ab.getErrortype().equals("PR013006")) {//代休-周末
                                            if (ad.getDaixiu() != null && !ad.getDaixiu().isEmpty()) {
                                                strlengthtime = String.valueOf(df.format(Double.valueOf(strlengthtime) + Double.valueOf(ad.getDaixiu())));
                                            }
                                            //代休-周末大于等于15分
                                            if (Double.valueOf(strlengthtime) %  (Double.valueOf(lateearlyleave))==0) {
                                                ad.setDaixiu(strlengthtime);
                                            }
                                            else
                                            {
                                                ad.setDaixiu(df.format(Math.floor(Double.valueOf(strlengthtime) / Double.valueOf(lateearlyleave))*Double.valueOf(lateearlyleave) + Double.valueOf(lateearlyleave)));
                                            }
                                            ad.setDaixiu(strlengthtime);
                                        }else if (ab.getErrortype().equals("PR013007")) {//代休-特殊
                                            if (ad.getDaixiu() != null && !ad.getDaixiu().isEmpty()) {
                                                strlengthtime = String.valueOf(df.format(Double.valueOf(strlengthtime) + Double.valueOf(ad.getDaixiu())));
                                            }
                                            ad.setDaixiu(strlengthtime);
                                        } else if (ab.getErrortype().equals("PR013008")) {//事休
                                            if (ad.getCompassionateleave() != null && !ad.getCompassionateleave().isEmpty()) {
                                                strlengthtime = String.valueOf(df.format(Double.valueOf(strlengthtime) + Double.valueOf(ad.getCompassionateleave())));
                                            }
                                            //事休大于等于15分
                                            if (Double.valueOf(strlengthtime) %  (Double.valueOf(lateearlyleave))==0) {
                                                ad.setCompassionateleave(strlengthtime);
                                            }
                                            else
                                            {
                                                ad.setCompassionateleave(df.format(Math.floor(Double.valueOf(strlengthtime) / Double.valueOf(lateearlyleave))*Double.valueOf(lateearlyleave) + Double.valueOf(lateearlyleave)));
                                            }
                                        } else if (ab.getErrortype().equals("PR013009")) {//短期病休
                                            if (ad.getShortsickleave() != null && !ad.getShortsickleave().isEmpty()) {
                                                strlengthtime = String.valueOf(df.format(Double.valueOf(strlengthtime) + Double.valueOf(ad.getShortsickleave())));
                                            }
                                            //短期病休大于等于15分
                                            if (Double.valueOf(strlengthtime) %  (Double.valueOf(lateearlyleave))==0) {
                                                ad.setShortsickleave(strlengthtime);
                                            }
                                            else
                                            {
                                                ad.setShortsickleave(df.format(Math.floor(Double.valueOf(strlengthtime) / Double.valueOf(lateearlyleave))*Double.valueOf(lateearlyleave) + Double.valueOf(lateearlyleave)));
                                            }
                                        } else if (ab.getErrortype().equals("PR013010")) {//長期病休
                                            if (ad.getLongsickleave() != null && !ad.getLongsickleave().isEmpty()) {
                                                strlengthtime = String.valueOf(df.format(Double.valueOf(strlengthtime) + Double.valueOf(ad.getLongsickleave())));
                                            }
                                            //短期病休大于等于15分
                                            if (Double.valueOf(strlengthtime) % (Double.valueOf(lateearlyleave))==0) {
                                                ad.setLongsickleave(strlengthtime);
                                            }
                                            else
                                            {
                                                ad.setLongsickleave(df.format(Math.floor(Double.valueOf(strlengthtime) / Double.valueOf(lateearlyleave))*Double.valueOf(lateearlyleave) + Double.valueOf(lateearlyleave)));
                                            }
                                        } else if (ab.getErrortype().equals("PR013012") || ab.getErrortype().equals("PR013013")) { //産休（女） 护理假（男）
                                            //産休（女）//産休看護休暇（男）
                                            if (ad.getNursingleave() != null && !ad.getNursingleave().isEmpty()) {
                                                strlengthtime = String.valueOf(df.format(Double.valueOf(strlengthtime) + Double.valueOf(ad.getNursingleave())));
                                            }
                                            ad.setNursingleave(strlengthtime);
                                        } else if (ab.getErrortype().equals("PR013014") || ab.getErrortype().equals("PR013016")
                                                || ab.getErrortype().equals("PR013018") || ab.getErrortype().equals("PR013019")
                                                || ab.getErrortype().equals("PR013011") || ab.getErrortype().equals("PR013015")
                                                || ab.getErrortype().equals("PR013004") || ab.getErrortype().equals("PR013017")
                                                || ab.getErrortype().equals("PR013020")) {
                                            //福利假期
                                            //家长会假//妊娠檢查休暇
                                            //労災休暇//其他休暇
                                            //婚假 //丧假
                                            //流产假 //计划生育手术假//工伤
                                            if (ad.getWelfare() != null && !ad.getWelfare().isEmpty()) {
                                                strlengthtime = String.valueOf(df.format(Double.valueOf(strlengthtime) + Double.valueOf(ad.getWelfare())));
                                            }
                                            if (Double.valueOf(strlengthtime) %  (Double.valueOf(lateearlyleave))==0) {
                                                ad.setWelfare(strlengthtime);
                                            }
                                            else
                                            {
                                                ad.setWelfare(df.format(Math.floor(Double.valueOf(strlengthtime) / Double.valueOf(lateearlyleave))*Double.valueOf(lateearlyleave) + Double.valueOf(lateearlyleave)));
                                            }
                                        }
                                    }
                                } else {
                                    //没有记录，没有申请
                                    ad.setAbsenteeism(workinghours);
                                    ad.setNormal(Double.valueOf(ad.getNormal()) == 0 ? null :ad.getNormal());
                                }
                                //---------处理昨日审批通过的异常考勤申请end-------
                                ad.setNormal(ad.getNormal() == null ? "0" :ad.getNormal());
                                ad.setAnnualrest(ad.getAnnualrest()==null ? "0"  :ad.getAnnualrest());
                                ad.setDaixiu(ad.getDaixiu()==null ? "0" :ad.getDaixiu());
                                ad.setCompassionateleave(ad.getCompassionateleave()==null? "0" :ad.getCompassionateleave());
                                ad.setShortsickleave(ad.getShortsickleave()==null ? "0" :ad.getShortsickleave());
                                ad.setLongsickleave(ad.getLongsickleave()==null ? "0" :ad.getLongsickleave());
                                ad.setNursingleave(ad.getNursingleave()==null ? "0" :ad.getNursingleave());
                                ad.setWelfare(ad.getWelfare()==null ? "0" :ad.getWelfare());
                                ad.setAbsenteeism(df.format(Double.valueOf(ad.getAbsenteeism()) - Double.valueOf(ad.getShortsickleave())
                                        - Double.valueOf(ad.getLongsickleave()) - Double.valueOf(ad.getCompassionateleave()) - Double.valueOf(ad.getAnnualrest())
                                        - Double.valueOf(ad.getDaixiu()) - Double.valueOf(ad.getNursingleave()) - Double.valueOf(ad.getWelfare())));

                                ad.setAbsenteeism(df.format(Double.valueOf(ad.getAbsenteeism()) - Double.valueOf(ad.getShortsickleave())
                                        - Double.valueOf(ad.getLongsickleave()) - Double.valueOf(ad.getCompassionateleave()) - Double.valueOf(ad.getAnnualrest())
                                        - Double.valueOf(ad.getDaixiu()) - Double.valueOf(ad.getNursingleave()) - Double.valueOf(ad.getWelfare())));
                                if(Double.valueOf(ad.getAbsenteeism())  >=  Double.valueOf(workinghours) )
                                {
                                    ad.setNormal("0");
                                }
                                else if(Double.valueOf(ad.getAbsenteeism())<0)
                                {
                                    ad.setNormal("0");
                                }
                                else
                                {
                                    ad.setNormal(df.format(Double.valueOf(ad.getNormal()) - Double.valueOf(ad.getAbsenteeism()) -  Double.valueOf(ad.getShortsickleave())
                                            - Double.valueOf(ad.getLongsickleave()) - Double.valueOf(ad.getCompassionateleave()) - Double.valueOf(ad.getAnnualrest())
                                            - Double.valueOf(ad.getDaixiu()) - Double.valueOf(ad.getNursingleave()) - Double.valueOf(ad.getWelfare())));
                                }
                                ad.setNormal(ad.getNormal() == null ? null :(ad.getNormal() =="0" ? null :df.format(Double.valueOf(ad.getNormal()))));
                                ad.setAnnualrest(Double.valueOf(ad.getAnnualrest()) <= 0 ? null  :df.format(Double.valueOf(ad.getAnnualrest())));
                                ad.setDaixiu(Double.valueOf(ad.getDaixiu()) <= 0 ? null :df.format(Double.valueOf(ad.getDaixiu())));
                                ad.setCompassionateleave(Double.valueOf(ad.getCompassionateleave()) <= 0 ? null :df.format(Double.valueOf(ad.getCompassionateleave())));
                                ad.setShortsickleave(Double.valueOf(ad.getShortsickleave()) <= 0 ? null :df.format(Double.valueOf(ad.getShortsickleave())));
                                ad.setLongsickleave(Double.valueOf(ad.getLongsickleave()) <= 0 ? null :df.format(Double.valueOf(ad.getLongsickleave())));
                                ad.setNursingleave(Double.valueOf(ad.getNursingleave()) <= 0 ? null :df.format(Double.valueOf(ad.getNursingleave())));
                                ad.setWelfare(Double.valueOf(ad.getWelfare()) <= 0 ? null :df.format(Double.valueOf(ad.getWelfare())));
                                ad.setAbsenteeism(Double.valueOf(ad.getAbsenteeism()) <= 0 ? null :df.format(Double.valueOf(ad.getAbsenteeism())));
                                //更新考勤表
                                if(customerInfo.getUserinfo().getEnddate() == null || customerInfo.getUserinfo().getEnddate().isEmpty())
                                {
                                    ad.setTshortsickleave(ad.getShortsickleave());
                                    ad.setTlongsickleave(ad.getLongsickleave());
                                    ad.setTabsenteeism(ad.getAbsenteeism());
                                    ad.setShortsickleave(null);
                                    ad.setLongsickleave(null);
                                    ad.setAbsenteeism(null);
                                }
                                else if (sf1ymd.parse(customerInfo.getUserinfo().getEnddate()).getTime() > dateStart.getTime())
                                {
                                    ad.setTshortsickleave(ad.getShortsickleave());
                                    ad.setTlongsickleave(ad.getLongsickleave());
                                    ad.setTabsenteeism(ad.getAbsenteeism());
                                    ad.setShortsickleave(null);
                                    ad.setLongsickleave(null);
                                    ad.setAbsenteeism(null);
                                }
                                if(workinghours.equals("0"))
                                {
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
                                saveAttendance(ad, "0", tokenModel);
                            }
                            //---------查询昨天大打卡记录end-------
                        }
                        //---------不定时考勤人员(非不定时考勤人员才计算)end-------
                    //}
                }
            }
            //---------查询考勤表是否有数据end-------
        }
    }

    //考勤管理
    public void saveAttendance(Attendance attendance,String Flg,TokenModel tokenModel) throws Exception {
        if(Flg.equals("0")){//更新
            attendance.preUpdate(tokenModel);
            attendanceMapper.updateByPrimaryKey(attendance);
        }
        else{//新建
            attendance.preInsert(tokenModel);
            attendanceMapper.insert(attendance);
        }
    }

    //时间段判断
    public String timeLength(String time_start,String time_end,String lunchbreak_start, String lunchbreak_end) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
        String overtimeHours =null;
        if(sdf.parse(time_start).getTime() <= sdf.parse(lunchbreak_start).getTime() && sdf.parse(time_end).getTime() >= sdf.parse(lunchbreak_end).getTime())
        {
            //午休都包括在内
            //打卡开始-结束时间
            long result1 = sdf.parse(time_end).getTime() - sdf.parse(time_start).getTime();
            //午休时间
            long result2 = sdf.parse(lunchbreak_end).getTime() - sdf.parse(lunchbreak_start).getTime();
            //打卡记录加班时间
            Double result3 = Double.valueOf(String.valueOf(result1 - result2)) / 60 / 60 / 1000;
            overtimeHours = String.valueOf(result3);
        }
        else if(sdf.parse(time_start).getTime() >= sdf.parse(lunchbreak_end).getTime() || sdf.parse(time_end).getTime() <= sdf.parse(lunchbreak_start).getTime())
        {
            //午休不包括在内
            //打卡开始-结束时间
            long result1 = sdf.parse(time_end).getTime() - sdf.parse(time_start).getTime();
            //打卡记录加班时间
            Double result3 = Double.valueOf(String.valueOf(result1)) / 60 / 60 / 1000;
            overtimeHours = String.valueOf(result3);

        }
        else if(sdf.parse(time_start).getTime() < sdf.parse(lunchbreak_start).getTime() && (sdf.parse(lunchbreak_start).getTime() < sdf.parse(time_end).getTime() ||sdf.parse(time_end).getTime() < sdf.parse(lunchbreak_end).getTime()))
        {
            //午休包括在内一部分（打卡结束时间在午休内）
            //打卡开始-午休开始时间
            long result1 = sdf.parse(lunchbreak_start).getTime() - sdf.parse(time_start).getTime();
            Double result3 = Double.valueOf(String.valueOf(result1)) / 60 / 60 / 1000;
            overtimeHours = String.valueOf(result3);
        }
        else
        {
            //午休包括在内一部分（打卡开始时间在午休内）
            //午休结束-打开结束时间
            long result1 = sdf.parse(time_end).getTime() - sdf.parse(lunchbreak_end).getTime();
            Double result3 = Double.valueOf(String.valueOf(result1)) / 60 / 60 / 1000;
            overtimeHours = String.valueOf(result3);
        }
        return overtimeHours;
    }

    //代休添加
    public void insertReplace(Attendance ad, TokenModel tokenModel,String type,String duration) throws Exception {
        Replacerest replacerest = new Replacerest();
        replacerest.setUser_id(ad.getUser_id());
        List<Replacerest> replacerestlist = replacerestMapper.select(replacerest);
        replacerest.setUser_id(ad.getUser_id());
        replacerest.setCenter_id(ad.getCenter_id());
        replacerest.setGroup_id(ad.getGroup_id());
        replacerest.setTeam_id(ad.getTeam_id());
        replacerest.setApplication_date(DateUtil.format(new Date(),"YYYY/MM/dd"));
        replacerest.setRecognitionstate("0");
        if (replacerestlist.size() > 0)
        {
            //更新代休
            replacerest.preUpdate(tokenModel);
            replacerest.setReplacerest_id(replacerestlist.get(0).getReplacerest_id());

            if(type.equals("1"))
            {
                //周末
                replacerest.setType(type);
                replacerest.setDuration(replacerestlist.get(0).getDuration() ==null || replacerestlist.get(0).getDuration().isEmpty() ? duration : String.valueOf(Double.valueOf(replacerestlist.get(0).getDuration())+Double.valueOf(duration)));
                replacerest.setType_teshu(replacerestlist.get(0).getType_teshu());
                replacerest.setDuration_teshu(replacerestlist.get(0).getDuration_teshu());
            }
            else
            {
                //特殊
                replacerest.setType_teshu(type);
                replacerest.setDuration_teshu(replacerestlist.get(0).getDuration_teshu() ==null || replacerestlist.get(0).getDuration_teshu().isEmpty() ? duration : String.valueOf(Double.valueOf(replacerestlist.get(0).getDuration_teshu())+Double.valueOf(duration)));
                replacerest.setType(replacerestlist.get(0).getType());
                replacerest.setDuration(replacerestlist.get(0).getDuration());
            }
            replacerestMapper.updateByPrimaryKey(replacerest);
        }
        else
        {
            //登录代休
            replacerest.preInsert(tokenModel);
            replacerest.setReplacerest_id(UUID.randomUUID().toString());

            if(type.equals("1"))
            {
                //周末
                replacerest.setType(type);
                replacerest.setDuration(duration);
                replacerest.setType_teshu("2");
                replacerest.setDuration_teshu("0");
            }
            else
            {
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
    public Double getUnit(Double d1,Double d2) throws Exception {
        Double d3 = d1 / d2;
        return Double.valueOf((int) Math.ceil(d3)) * d2;
    }
}
