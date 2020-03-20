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
                //attendance.setAbsenteeism();
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
                //if (customerInfo.getUserid().equals("5e0ee8a8c0911e1c24f1a57c")) {
                //插入没有打卡记录的员工的考勤
                Attendance attendance = new Attendance();
                attendance.setAbsenteeism("8");
                attendance.setNormal("0");
                attendance.setAttendanceid(UUID.randomUUID().toString());
                attendance.setCenter_id(customerInfo.getUserinfo().getCentername());
                attendance.setGroup_id(customerInfo.getUserinfo().getGroupname());
                attendance.setTeam_id(customerInfo.getUserinfo().getTeamname());
                attendance.setYears(DateUtil.format(new Date(), "YYYY").toString());
                attendance.setMonths(DateUtil.format(new Date(), "MM").toString());
                attendance.setUser_id(customerInfo.getUserid());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.add(Calendar.DAY_OF_YEAR, -1);
                attendance.setDates(calendar.getTime());
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
            workshift_end = attendancesettinglist.get(0).getWorkshift_end();
            //公司考勤设定下班开始时间
            closingtime_start = attendancesettinglist.get(0).getClosingtime_start().replace(":", "");
            closingtime_end = attendancesettinglist.get(0).getClosingtime_end();
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
                        //判断当前天是 青年节，妇女节 ,工作日表中的数据
                        if(sf1ymd.format(dateStart).equals(DateUtil.format(new Date(),"YYYY").toString() + "03-08") || sf1ymd.format(dateStart).equals(DateUtil.format(new Date(),"YYYY").toString() + "05-04"))
                        {
                            workinghours = "4";
                        }else
                        {
                            if(workingDaysList.size()>0)
                            {
                                workinghours = "0";
                            }
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
                            //---------弹性工作制start-------
                            String Flexibleworkshift_start = null;
                            FlexibleWork flexibleWork = new FlexibleWork();
                            flexibleWork.setUser_id(ad.getUser_id());
                            flexibleWork.setStatus(AuthConstants.APPROVED_FLAG_YES);
                            List<FlexibleWork> flexibleWorklist = flexibleworkMapper.select(flexibleWork);
                            if (flexibleWorklist.size() > 0) {
                                String strImplement_date = flexibleWorklist.get(0).getImplement_date().substring(0, 10);
                                String strReserveovertimedate = sf1ymd.format(dateStart);//**
                                int res = strReserveovertimedate.compareTo(strImplement_date);
                                if (res > 0) {
                                    Flexibleworkshift_start = flexibleWorklist.get(0).getWorktime().replace(":", "");
                                    String result = null;
                                    if (workshift_start != null) {
                                        //公司考勤设定上班开始时间与该员工弹性工作上班的时间的差的分钟数
                                        result = String.valueOf((sdf.parse(Flexibleworkshift_start).getTime() - sdf.parse(workshift_start).getTime()) / (1000 * 60));
                                        //弹性工作实际上班时间
                                        workshift_start = Flexibleworkshift_start;
                                    }
                                    if (closingtime_start != null && result != null) {
                                        //根据公司考勤设定上班开始时间与该员工弹性工作上班的时间的差的分钟数计算该员工实际的下班时间
                                        long result1 = sdf.parse(closingtime_start).getTime() + (Long.parseLong(result) * 60000);
                                        Date d1 = new Date(result1);
                                        //弹性工作实际下班时间
                                        closingtime_start = sdf.format(d1).toString();
                                    }
                                }
                            }
                            //---------弹性工作制end-------
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
                                    //---------处理昨日审批通过的加班申请start-------
                                    Overtime overtime = new Overtime();
                                    overtime.setUserid(ad.getUser_id());
                                    overtime.setRecognitionstate(AuthConstants.RECOGNITION_FLAG_NO);
                                    overtime.setReserveovertimedate(dateStart);
                                    List<Overtime> overtimelist = overtimeMapper.select(overtime);
                                    List<Overtime> ovList = new ArrayList<Overtime>();
                                    //筛选审批通过的数据
                                    for (Overtime ov : overtimelist) {
                                        if (ov.getStatus().equals(AuthConstants.APPROVED_FLAG_YES) || ov.getStatus().equals("7")) {
                                            ovList.add(ov);
                                        }
                                    }
                                    if (ovList.size() > 0) {
                                        for (Overtime Ot : ovList) {
                                            //予定加班時間
                                            reserveoverTime = Ot.getReserveovertime();
                                            BigDecimal reserveoverTimehour = new BigDecimal(reserveoverTime).multiply(new BigDecimal(60 * 60 * 1000));
                                            reserveoverTime = String.valueOf(reserveoverTimehour.intValue());
                                            //实际加班時間
                                            actualoverTime = Ot.getActualovertime();
                                            BigDecimal actualoverTimehour = new BigDecimal(actualoverTime).multiply(new BigDecimal(60 * 60 * 1000));
                                            actualoverTime = String.valueOf(actualoverTimehour.intValue());
                                            String overtimeHours = null;
                                            //加班后应该下班的时间
                                            String overtime_end = null;
                                            if (!actualoverTime.equals("0")) {
                                                //根据公司考勤设定上班开始时间与该员工弹性工作上班的时间的差的分钟数计算该员工实际的下班时间
                                                long result1 = sdf.parse(closingtime_start).getTime() + (Long.parseLong(actualoverTime));
                                                Date d1 = new Date(result1);
                                                overtime_end = sdf.format(d1).toString();
                                                //实际加班时间
                                                overtimeHours = String.valueOf(Double.valueOf(actualoverTime) / 60 / 60 / 1000);
                                            } else {
                                                //根据公司考勤设定上班开始时间与该员工弹性工作上班的时间的差的分钟数计算该员工实际的下班时间
                                                //1分钟=60*1000=60000毫秒
                                                long result1 = sdf.parse(closingtime_start).getTime() + (Long.parseLong(reserveoverTime));
                                                Date d1 = new Date(result1);
                                                overtime_end = sdf.format(d1).toString();
                                                //予定加班時間
                                                overtimeHours = String.valueOf(Double.valueOf(reserveoverTime) / 60 / 60 / 1000);
                                            }

                                            //周末加班/法定日加班
                                            if (Ot.getOvertimetype().equals("PR001002") || Ot.getOvertimetype().equals("PR001003")) {
                                                //打卡开始-结束时间
                                                long result1 = sdf.parse(time_end).getTime() - sdf.parse(time_start).getTime();
                                                //午休时间
                                                long result2 = sdf.parse(lunchbreak_end).getTime() - sdf.parse(lunchbreak_start).getTime();
                                                //打卡记录加班时间
                                                Double result3 = Double.valueOf(String.valueOf(result1 - result2)) / 60 / 60 / 1000;

                                                if (Double.valueOf(overtimeHours) >= Double.valueOf(result3)) {
                                                    //实际打卡记录的时间设定为加班时间。
                                                    overtimeHours = String.valueOf(result3);
                                                }
                                            } else {
                                                //加班后应该下班的时间
                                                if (!overtime_end.equals("")) {
                                                    if (Integer.valueOf(overtime_end) > Integer.valueOf(time_end)) {

                                                        //最后打卡记录的时间减去吃饭时间
                                                        long result1 = sdf.parse(time_end).getTime() - sdf.parse(closingtime_start).getTime();
                                                        overtimeHours = String.valueOf(Double.valueOf(String.valueOf(result1)) / 60 / 60 / 1000);
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
                                                    ad.setOrdinaryindustry(overtimeHours);
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
                                                    ad.setWeekendindustry(overtimeHours);
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
                                                    ad.setStatutoryresidue(overtimeHours);
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
                                                    ad.setAnnualrestday(overtimeHours);
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

                                                //加班时间大于等于15分
                                                if (Double.valueOf(overtimeHours) % ((Double.valueOf(strovertime))/60/60/1000)==0) {
                                                    ad.setSpecialday(overtimeHours);
                                                }
                                                else
                                                {
                                                    ad.setSpecialday(df.format(Math.floor(Double.valueOf(overtimeHours) / ((Double.valueOf(strovertime))/60/60/1000))*((Double.valueOf(strovertime))/60/60/1000)));
                                                }
                                            } else if (Ot.getOvertimetype().equals("PR001007")) {//五四青年节
                                                if (ad.getYouthday() != null && !ad.getYouthday().isEmpty()) {
                                                    overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours) + Double.valueOf(ad.getYouthday())));
                                                }else {
                                                    overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours)));
                                                }

                                                //加班时间大于等于15分
                                                if (Double.valueOf(overtimeHours) % ((Double.valueOf(strovertime))/60/60/1000)==0) {
                                                    ad.setYouthday(overtimeHours);
                                                }
                                                else
                                                {
                                                    ad.setYouthday(df.format(Math.floor(Double.valueOf(overtimeHours) / ((Double.valueOf(strovertime))/60/60/1000)) *((Double.valueOf(strovertime))/60/60/1000)));
                                                }
                                            } else if (Ot.getOvertimetype().equals("PR001008")) {//妇女节
                                                if (ad.getWomensday() != null && !ad.getWomensday().isEmpty()) {
                                                    overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours) + Double.valueOf(ad.getWomensday())));
                                                }else {
                                                    overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours)));
                                                }

                                                //加班时间大于等于15分
                                                if (Double.valueOf(overtimeHours) % ((Double.valueOf(strovertime))/60/60/1000)==0) {
                                                    ad.setWomensday(overtimeHours);
                                                }
                                                else
                                                {
                                                    ad.setWomensday(df.format(Math.floor(Double.valueOf(overtimeHours) / ((Double.valueOf(strovertime))/60/60/1000))*((Double.valueOf(strovertime))/60/60/1000)));
                                                }
                                            }
                                            //承认
                                            Ot.setRecognitionstate(AuthConstants.RECOGNITION_FLAG_YES);
                                            Ot.preUpdate(tokenModel);
                                            overtimeMapper.updateByPrimaryKey(Ot);
                                        }
                                    }
                                    //---------处理昨日审批通过的加班申请end-------
                                    //正常的打卡记录
                                    if (sdf.parse(time_start).getTime() <= sdf.parse(workshift_start).getTime()
                                            && sdf.parse(closingtime_start).getTime() <= sdf.parse(time_end).getTime()) {

                                            ad.setNormal(workinghours);

                                    } else //异常打卡的情况
                                    {

                                        ad.setAbsenteeism("0");
                                        String absenteeism = null;
                                        //一条卡达记录记录的请路况
                                        if (sdf.parse(time_start).getTime() == sdf.parse(time_end).getTime()) {
                                            ad.setAbsenteeism(String.valueOf(Double.valueOf(lateearlyleave) * 32));
                                        }
                                        //迟到
                                        if (sdf.parse(time_start).getTime() != sdf.parse(time_end).getTime() &&
                                                sdf.parse(time_start).getTime() > sdf.parse(workshift_start).getTime()) {
                                            //迟到的时间
                                            long result = sdf.parse(time_start).getTime() - sdf.parse(workshift_start).getTime();
                                            //应该补的时间
                                            Double strunit = getUnit(Double.valueOf(result), Double.valueOf(lateearlyleave));
                                            //迟到的小时
                                            Double Dhourresult = strunit / 60 / 60 / 1000;

                                            if(ad.getAbsenteeism() != null && !ad.getAbsenteeism().isEmpty()) {
                                                absenteeism = String.valueOf(df.format(Double.valueOf(Dhourresult) + Double.valueOf(ad.getAbsenteeism())));
                                            }
                                            else
                                            {
                                                absenteeism = String.valueOf(df.format(Double.valueOf(Dhourresult)));
                                            }
                                            //迟到大于等于15分
                                            if (Double.valueOf(absenteeism) % (Double.valueOf(lateearlyleave))==0) {
                                                ad.setAbsenteeism(absenteeism);
                                            }
                                            else
                                            {
                                                ad.setAbsenteeism(df.format(Math.floor(Double.valueOf(absenteeism) / Double.valueOf(lateearlyleave))*Double.valueOf(lateearlyleave) + Double.valueOf(lateearlyleave)));
                                            }
                                        }
                                        //早退
                                        if (sdf.parse(time_start).getTime() != sdf.parse(time_end).getTime() &&
                                                sdf.parse(closingtime_start).getTime() > sdf.parse(time_end).getTime()) {
                                            long result = sdf.parse(closingtime_start).getTime() - sdf.parse(time_end).getTime();
                                            //应该补的时间
                                            Double strunit = getUnit(Double.valueOf(result), Double.valueOf(lateearlyleave));
                                            //早退的小时
                                            Double Dhourresult = strunit / 60 / 60 / 1000;
                                            if(ad.getAbsenteeism() != null && !ad.getAbsenteeism().isEmpty()) {
                                                absenteeism = String.valueOf(df.format(Double.valueOf(Dhourresult) + Double.valueOf(ad.getAbsenteeism())));
                                            }
                                            else
                                            {
                                                absenteeism = String.valueOf(df.format(Double.valueOf(Dhourresult)));
                                            }
                                            //早退大于等于15分
                                            if (Double.valueOf(absenteeism) %  (Double.valueOf(lateearlyleave))==0) {
                                                ad.setAbsenteeism(absenteeism);
                                            }
                                            else
                                            {
                                                ad.setAbsenteeism(df.format(Math.floor(Double.valueOf(absenteeism) / Double.valueOf(lateearlyleave))*Double.valueOf(lateearlyleave) + Double.valueOf(lateearlyleave)));
                                            }
                                        }
                                        //---------处理昨日审批通过的异常考勤申请start-------
                                        AbNormal abnormal = new AbNormal();
                                        abnormal.setUser_id(ad.getUser_id());
                                        //abnormal.setOccurrencedate(dateStart);
                                        abnormal.setStatus(AuthConstants.APPROVED_FLAG_YES);
                                        List<AbNormal> abNormallist = abNormalMapper.select(abnormal);
                                        if (abNormallist.size() > 0) {
                                            for (AbNormal ab : abNormallist) {
                                                String strlengthtime = null;
                                                if(ab.getRelengthtime().equals("0"))
                                                {
                                                    strlengthtime = ab.getLengthtime();
                                                }else
                                                {
                                                    strlengthtime = ab.getRelengthtime();
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
                                                    ad.setAnnualrest(strlengthtime);
                                                } else if (ab.getErrortype().equals("PR013006")) {//代休-周末
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
                                                } else if (ab.getErrortype().equals("PR013004") || ab.getErrortype().equals("PR013011")
                                                        || ab.getErrortype().equals("PR013014") || ab.getErrortype().equals("PR013015")
                                                        || ab.getErrortype().equals("PR013016") || ab.getErrortype().equals("PR013017")
                                                        || ab.getErrortype().equals("PR013018") || ab.getErrortype().equals("PR013019")) {
                                                    //福利假期
                                                    //女性三期//结婚休假//家长会假//葬儀休暇//妊娠檢查休暇//哺乳休暇//労災休暇//其他休暇
                                                    if (ad.getWelfare() != null && !ad.getWelfare().isEmpty()) {
                                                        strlengthtime = String.valueOf(df.format(Double.valueOf(strlengthtime) + Double.valueOf(ad.getWelfare())));
                                                    }
                                                    ad.setWelfare(strlengthtime);
                                                }
                                            }
                                            if(Double.valueOf(ad.getNormal()) <= 0)
                                            {
                                                ad.setAbsenteeism(null);
                                            }
                                        } else {
                                            //有记录 没有申请
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
                                    ad.setAbsenteeism(df.format(Double.valueOf(ad.getAbsenteeism()) - Double.valueOf(ad.getShortsickleave())
                                            - Double.valueOf(ad.getLongsickleave()) - Double.valueOf(ad.getCompassionateleave()) - Double.valueOf(ad.getAnnualrest())
                                            - Double.valueOf(ad.getDaixiu()) - Double.valueOf(ad.getNursingleave()) - Double.valueOf(ad.getWelfare())));

                                    if(Double.valueOf(ad.getAbsenteeism())  >=  Double.valueOf(workinghours) )
                                    {
                                        ad.setNormal(null);
                                    }
                                    else
                                    {
                                        ad.setNormal(df.format(Double.valueOf(ad.getNormal()) - Double.valueOf(ad.getAbsenteeism()) -  Double.valueOf(ad.getShortsickleave())
                                                - Double.valueOf(ad.getLongsickleave()) - Double.valueOf(ad.getCompassionateleave()) - Double.valueOf(ad.getAnnualrest())
                                                - Double.valueOf(ad.getDaixiu()) - Double.valueOf(ad.getNursingleave()) - Double.valueOf(ad.getWelfare())));
                                    }
                                    ad.setNormal(Double.valueOf(ad.getNormal()) <= 0 ? null :ad.getNormal());
                                    ad.setAnnualrest(Double.valueOf(ad.getAnnualrest()) <= 0 ? null  :ad.getAnnualrest());
                                    ad.setDaixiu(Double.valueOf(ad.getDaixiu()) <= 0 ? null :ad.getDaixiu());
                                    ad.setCompassionateleave(Double.valueOf(ad.getCompassionateleave()) <= 0 ? null :ad.getCompassionateleave());
                                    ad.setShortsickleave(Double.valueOf(ad.getShortsickleave()) <= 0 ? null :ad.getShortsickleave());
                                    ad.setLongsickleave(Double.valueOf(ad.getLongsickleave()) <= 0 ? null :ad.getLongsickleave());
                                    ad.setNursingleave(Double.valueOf(ad.getNursingleave()) <= 0 ? null :ad.getNursingleave());
                                    ad.setWelfare(Double.valueOf(ad.getWelfare()) <= 0 ? null :ad.getWelfare());
                                    ad.setAbsenteeism(Double.valueOf(ad.getAbsenteeism()) <= 0 ? null :ad.getAbsenteeism());

                                    if(workingDaysList.size()>0 || !workinghours.equals("8"))
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
                                        if (sdf.parse(customerInfo.getUserinfo().getEnddate()).getTime() > dateStart.getTime())
                                        {
                                            ad.setTshortsickleave(ad.getShortsickleave());
                                            ad.setTlongsickleave(ad.getLongsickleave());
                                            ad.setTabsenteeism(ad.getAbsenteeism());
                                            ad.setShortsickleave(null);
                                            ad.setLongsickleave(null);
                                            ad.setAbsenteeism(null);
                                        }
                                    }
                                    saveAttendance(ad, "0", tokenModel);
                                }
                            }
                            else {
                                //---------处理昨日审批通过的异常考勤申请start-------
                                AbNormal abnormal = new AbNormal();
                                abnormal.setUser_id(ad.getUser_id());
                                //abnormal.setOccurrencedate(dateStart);
                                abnormal.setStatus(AuthConstants.APPROVED_FLAG_YES);
                                List<AbNormal> abNormallist = abNormalMapper.select(abnormal);
                                if (abNormallist.size() > 0) {
                                    for (AbNormal ab : abNormallist) {
                                        //在申请的日期范围内
                                        String strlengthtime = ab.getLengthtime();
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
                                        } else if (ab.getErrortype().equals("PR013004") || ab.getErrortype().equals("PR013011")
                                                || ab.getErrortype().equals("PR013014") || ab.getErrortype().equals("PR013015")
                                                || ab.getErrortype().equals("PR013016") || ab.getErrortype().equals("PR013017")
                                                || ab.getErrortype().equals("PR013018") || ab.getErrortype().equals("PR013019")) {
                                            //福利假期
                                            //女性三期//结婚休假//家长会假//葬儀休暇//妊娠檢查休暇//哺乳休暇//労災休暇//其他休暇
                                            if (ad.getWelfare() != null && !ad.getWelfare().isEmpty()) {
                                                strlengthtime = String.valueOf(df.format(Double.valueOf(strlengthtime) + Double.valueOf(ad.getWelfare())));
                                            }
                                            ad.setWelfare(strlengthtime);
                                        }
                                    }
                                    ad.setNormal(Double.valueOf(ad.getNormal()) <= 0 ? null :ad.getNormal());
                                    ad.setAbsenteeism(Double.valueOf(ad.getAbsenteeism()) <= 0 ? null :ad.getAbsenteeism());

                                } else {
                                    //没有记录，没有申请
                                    ad.setAbsenteeism(workinghours);
                                    ad.setNormal(Double.valueOf(ad.getNormal()) <= 0 ? null :ad.getNormal());
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

                                if(Double.valueOf(ad.getAbsenteeism())  >=  Double.valueOf(workinghours) )
                                {
                                    ad.setNormal(null);
                                }
                                else
                                {
                                    ad.setNormal(df.format(Double.valueOf(workinghours) - Double.valueOf(ad.getAbsenteeism()) -  Double.valueOf(ad.getShortsickleave())
                                            - Double.valueOf(ad.getLongsickleave()) - Double.valueOf(ad.getCompassionateleave()) - Double.valueOf(ad.getAnnualrest())
                                            - Double.valueOf(ad.getDaixiu()) - Double.valueOf(ad.getNursingleave()) - Double.valueOf(ad.getWelfare())));
                                }
                                ad.setNormal(Double.valueOf(ad.getNormal()) <= 0 ? null :ad.getNormal());
                                ad.setAnnualrest(Double.valueOf(ad.getAnnualrest()) <= 0 ? null  :ad.getAnnualrest());
                                ad.setDaixiu(Double.valueOf(ad.getDaixiu()) <= 0 ? null :ad.getDaixiu());
                                ad.setCompassionateleave(Double.valueOf(ad.getCompassionateleave()) <= 0 ? null :ad.getCompassionateleave());
                                ad.setShortsickleave(Double.valueOf(ad.getShortsickleave()) <= 0 ? null :ad.getShortsickleave());
                                ad.setLongsickleave(Double.valueOf(ad.getLongsickleave()) <= 0 ? null :ad.getLongsickleave());
                                ad.setNursingleave(Double.valueOf(ad.getNursingleave()) <= 0 ? null :ad.getNursingleave());
                                ad.setWelfare(Double.valueOf(ad.getWelfare()) <= 0 ? null :ad.getWelfare());
                                ad.setAbsenteeism(Double.valueOf(ad.getAbsenteeism()) <= 0 ? null :ad.getAbsenteeism());
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
                                else if (sdf.parse(customerInfo.getUserinfo().getEnddate()).getTime() > dateStart.getTime())
                                {
                                    ad.setTshortsickleave(ad.getShortsickleave());
                                    ad.setTlongsickleave(ad.getLongsickleave());
                                    ad.setTabsenteeism(ad.getAbsenteeism());
                                    ad.setShortsickleave(null);
                                    ad.setLongsickleave(null);
                                    ad.setAbsenteeism(null);
                                }
                                if(workingDaysList.size()>0 || !workinghours.equals("8"))
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

    //获取基本计算单位
    public Double getUnit(Double d1,Double d2) throws Exception {
        Double d3 = d1 / d2;
        return Double.valueOf((int) Math.ceil(d3)) * d2;
    }
}
