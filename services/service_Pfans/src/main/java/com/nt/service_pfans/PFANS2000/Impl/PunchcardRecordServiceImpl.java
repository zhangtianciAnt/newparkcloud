package com.nt.service_pfans.PFANS2000.Impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Pfans.PFANS2000.*;
import com.nt.service_pfans.PFANS2000.PunchcardRecordService;
import com.nt.service_pfans.PFANS2000.mapper.*;
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
    private MongoTemplate mongoTemplate;

    @Override
    public List<PunchcardRecord> list(PunchcardRecord punchcardrecord,TokenModel tokenModel) throws Exception {
//        methodAttendance(tokenModel);
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

            PunchcardRecord punchcardrecord = new PunchcardRecord();
            Double Worktime =0d;
            DecimalFormat df = new DecimalFormat(".00");
            List<PunchcardRecord> punchcardrecordlist = punchcardrecorddetailmapper.getPunchCardRecord();
            for (PunchcardRecord punchcard : punchcardrecordlist) {
                if(punchcard.getWorktime() != null){
                    Worktime=Double.valueOf(punchcard.getWorktime());
                }
                PunchcardRecord del = new PunchcardRecord();
                del.setUser_id(punchcard.getUser_id());
                del.setPunchcardrecord_date(punchcard.getPunchcardrecord_date());
                punchcardrecordMapper.delete(del);

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

                //删除当天存在的考勤数据
                Attendance attendance = new Attendance();
                attendance.setUser_id(punchcard.getUser_id());
                attendance.setDates(punchcard.getPunchcardrecord_date());
                attendanceMapper.delete(attendance);

                //创建考勤数据
                if(Worktime >= 8){
                    attendance.setNormal("8");
                }
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
            }
            methodAttendance(tokenModel);
            Result.add("失败数：" + error);
            Result.add("成功数：" + accesscount);
            return Result;
        } catch (Exception e) {
            throw new LogicalException(e.getMessage());
        }
    }

    @Override
    public void methodAttendance(TokenModel tokenModel) throws Exception {

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
        //深夜加班开始
        String nightshift_start = null;
        //深夜加班结束
        String nightshift_end = null;
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
        //旷工基本计算单位
        String absenteeism = null;
        //加班基本计算单位
        String strovertime = null;
        //工作时间
        String workinghours = null;
        //考勤设定
        AttendanceSetting attendancesetting = new AttendanceSetting();
        List<AttendanceSetting> attendancesettinglist = attendanceSettingMapper.select(attendancesetting);
        if(attendancesettinglist.size() > 0){
            //------------------------------查询公司考勤设定数据start-------------
            //公司考勤设定上班开始时间
            workshift_start = attendancesettinglist.get(0).getWorkshift_start().replace(":","");
            workshift_end = attendancesettinglist.get(0).getWorkshift_end();
            //公司考勤设定下班开始时间
            closingtime_start = attendancesettinglist.get(0).getClosingtime_start().replace(":","");
            closingtime_end = attendancesettinglist.get(0).getClosingtime_end();
            lunchbreak_start = attendancesettinglist.get(0).getLunchbreak_start().replace(":","");;
            lunchbreak_end = attendancesettinglist.get(0).getLunchbreak_end().replace(":","");;
            nightshift_start = attendancesettinglist.get(0).getNightshift_start().replace(":","");
            nightshift_end = attendancesettinglist.get(0).getNightshift_end().replace(":","");
            //迟加班基本计算单位
            lateearlyleave = attendancesettinglist.get(0).getLateearlyleave();
            BigDecimal lateearlyleavehour = new BigDecimal(lateearlyleave).multiply(new BigDecimal(60 * 60 * 1000));
            lateearlyleave = String.valueOf(lateearlyleavehour.intValue());

            //事假基本计算单位
            compassionateleave = attendancesettinglist.get(0).getCompassionateleave();
            //旷工基本计算单位
            absenteeism = attendancesettinglist.get(0).getAbsenteeism();
            //加班基本计算单位
            strovertime = attendancesettinglist.get(0).getOvertime();
            BigDecimal strovertimehour = new BigDecimal(strovertime).multiply(new BigDecimal(60 * 60 * 1000));
            strovertime = String.valueOf(strovertimehour.intValue());
            //平时晚加班时间扣除
            weekdaysovertime = attendancesettinglist.get(0).getWeekdaysovertime();
            BigDecimal weekdaysovertimehour = new BigDecimal(weekdaysovertime).multiply(new BigDecimal(60 * 60 * 1000));
            weekdaysovertime = String.valueOf(weekdaysovertimehour.intValue());
            //工作时间
            workinghours = attendancesettinglist.get(0).getWorkinghours();
            //------------------------------查询公司考勤设定数据end-------------
            //------------------------------查询本次打卡记录导入的数据start-------------
            //昨天的打卡记录
            Date dateStart = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateStart);
            cal.add(Calendar.DAY_OF_MONTH, -1);
            dateStart =  sdfxx.parse(sdfxx.format(cal.getTime()));
            //打卡记录
            PunchcardRecord punchcardRecord = new PunchcardRecord();
            punchcardRecord.setStatus("0");
            punchcardRecord.setPunchcardrecord_date(dateStart);
            List<PunchcardRecord> punchcardRecordlist = punchcardrecordMapper.select(punchcardRecord);
            if(punchcardRecordlist.size() > 0){
                for (PunchcardRecord PR : punchcardRecordlist) {
                    String strUserid = PR.getUser_id();
                    String time_start = sdf.format(PR.getTime_start());
                    String time_end = sdf.format(PR.getTime_end());
                    String worktime = PR.getWorktime();
                    String strCenter_id = PR.getCenter_id();
                    String strGroup_id = PR.getGroup_id();
                    String strTeam_id = PR.getTeam_id();
                    //---------不定时考勤人员(非不定时考勤人员才计算)start-------
                    Irregulartiming irregulartiming = new Irregulartiming();
                    irregulartiming.setUser_id(strUserid);
                    irregulartiming.setStatus(AuthConstants.APPROVED_FLAG_YES);
                    List<Irregulartiming> irregulartiminglist = irregulartimingMapper.select(irregulartiming);
                    if(irregulartiminglist.size() == 0){
                        //---------弹性工作制start-------
                        String Flexibleworkshift_start = null;
                        FlexibleWork flexibleWork = new FlexibleWork();
                        flexibleWork.setUser_id(strUserid);
                        flexibleWork.setStatus(AuthConstants.APPROVED_FLAG_YES);
                        List<FlexibleWork> flexibleWorklist = flexibleworkMapper.select(flexibleWork);
                        if(flexibleWorklist.size() > 0){
                            String strImplement_date = flexibleWorklist.get(0).getImplement_date().substring(0,10);
                            String strReserveovertimedate = sf1ymd.format(dateStart);//**
                            int res=strReserveovertimedate.compareTo(strImplement_date);
                            if(res>0){
                                Flexibleworkshift_start = flexibleWorklist.get(0).getWorktime().replace(":","");
                                String result = null;
                                if(workshift_start != null){
                                    //公司考勤设定上班开始时间与该员工弹性工作上班的时间的差的分钟数
                                    result = String.valueOf((sdf.parse(Flexibleworkshift_start).getTime() - sdf.parse(workshift_start).getTime())/(1000*60));
                                    //弹性工作实际上班时间
                                    workshift_start = Flexibleworkshift_start;
                                }
                                if(closingtime_start != null && result != null){
                                    //根据公司考勤设定上班开始时间与该员工弹性工作上班的时间的差的分钟数计算该员工实际的下班时间
                                    long result1 = sdf.parse(closingtime_start).getTime() + (Long.parseLong(result) * 60000);
                                    Date d1 = new Date(result1);
                                    //弹性工作实际下班时间
                                    closingtime_start = sdf.format(d1).toString();
                                }
                            }
                        }
                        //---------查询考勤表是否有数据start-------
                        Attendance attendance = new Attendance();
                        attendance.setUser_id(strUserid);
                        attendance.setDates(dateStart);
                        List<Attendance> attendancelist = attendanceMapper.select(attendance);
                        if(attendancelist.size() > 0){
                            attendance = attendancelist.get(0);

                            //---------处理昨日审批通过的加班申请start-------
                            Overtime overtime = new Overtime();
                            overtime.setUserid(strUserid);
                            overtime.setRecognitionstate(AuthConstants.RECOGNITION_FLAG_NO);
                            overtime.setReserveovertimedate(dateStart);
                            List<Overtime> overtimelist = overtimeMapper.select(overtime);
                            List<Overtime> ovList = new ArrayList<Overtime>();
                            //筛选审批通过的数据
                            for (Overtime ov : overtimelist){
                                if(ov.getStatus().equals(AuthConstants.APPROVED_FLAG_YES) || ov.getStatus().equals("7")){
                                    ovList.add(ov);
                                }
                            }
                            if(ovList.size() > 0){
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
                                    if(!actualoverTime.equals("0")){
                                        //根据公司考勤设定上班开始时间与该员工弹性工作上班的时间的差的分钟数计算该员工实际的下班时间
                                        long result1 = sdf.parse(closingtime_start).getTime() +
                                                (Long.parseLong(weekdaysovertime)) + (Long.parseLong(actualoverTime));
                                        Date d1 = new Date(result1);
                                        overtime_end = sdf.format(d1).toString();
                                        //实际加班时间
                                        overtimeHours = String.valueOf(Double.valueOf(actualoverTime) / 60 / 60 / 1000);
                                    }
                                    else{
                                        //根据公司考勤设定上班开始时间与该员工弹性工作上班的时间的差的分钟数计算该员工实际的下班时间
                                        //1分钟=60*1000=60000毫秒
                                        long result1 = sdf.parse(closingtime_start).getTime() +
                                                (Long.parseLong(weekdaysovertime)) + (Long.parseLong(reserveoverTime));
                                        Date d1 = new Date(result1);
                                        overtime_end = sdf.format(d1).toString();
                                        //予定加班時間
                                        overtimeHours = String.valueOf(Double.valueOf(reserveoverTime) / 60 / 60 / 1000);
                                    }
                                    //深夜加班時間
                                    String overtimeHoursNight = null;
                                    //周末加班/法定日加班
                                    if(Ot.getOvertimetype().equals("PR001002") || Ot.getOvertimetype().equals("PR001003")){
                                        //打卡开始-结束时间
                                        long result1 = sdf.parse(time_end).getTime() - sdf.parse(time_start).getTime();
                                        //午休时间
                                        long result2 = sdf.parse(lunchbreak_end).getTime() - sdf.parse(lunchbreak_start).getTime();
                                        //打卡结束时间-公司考勤下班时间（判断周末晚餐时间）
                                        //long result3 = sdf.parse(time_end).getTime() - sdf.parse(closingtime_start).getTime();
                                        Double result3 = Double.valueOf(String.valueOf(result1 - result2)) / 60 / 60 / 1000;
                                        if(Double.valueOf(overtimeHours) < Double.valueOf(result3)){
                                            BigDecimal dovertimeHours = new BigDecimal(overtimeHours).multiply(new BigDecimal(60 * 60 * 1000));
                                            long result5 = sdf.parse(workshift_start).getTime() + result2 + (Long.parseLong(String.valueOf(dovertimeHours.intValue())));
                                            Date d1 = new Date(result1);
                                            //加班后应该下班的时间
                                            overtime_end = sdf.format(d1).toString();
                                            //判斷是否是深夜加班并計算深夜加班的時常
                                            if(Integer.valueOf(overtime_end) > Integer.valueOf(nightshift_start)){//考虑深夜*00
                                                long result4 = sdf.parse(overtime_end).getTime() - sdf.parse(nightshift_start).getTime();
                                                overtimeHoursNight = String.valueOf(Double.valueOf(String.valueOf(result4)) / 60 / 60 / 1000);
                                                overtimeHours = String.valueOf(Double.valueOf(overtimeHours) - Double.valueOf(overtimeHoursNight));
                                            }
                                        }
                                        else{
                                            //实际打卡记录的时间设定为加班时间。
                                            overtimeHours = String.valueOf(result3);
                                        }
                                    }
                                    else{
                                        //加班后应该下班的时间
                                        if(!overtime_end.equals("")){
                                            if(Integer.valueOf(overtime_end) <= Integer.valueOf(time_end)){
                                                //判斷是否是深夜加班并計算深夜加班的時常
                                                if(Integer.valueOf(overtime_end) > Integer.valueOf(nightshift_start)){//考虑深夜*00
                                                    long result1 = sdf.parse(overtime_end).getTime() - sdf.parse(nightshift_start).getTime();
                                                    overtimeHoursNight = String.valueOf(Double.valueOf(String.valueOf(result1)) / 60 / 60 / 1000);
                                                    overtimeHours = String.valueOf(Double.valueOf(overtimeHours) - Double.valueOf(overtimeHoursNight));
                                                }
                                            }
                                            else{
                                                //最后打卡记录的时间减去吃饭时间
                                                long result1 = sdf.parse(time_end).getTime() - (Long.parseLong(weekdaysovertime)) - sdf.parse(closingtime_start).getTime();
                                                overtimeHours = String.valueOf(Double.valueOf(String.valueOf(result1)) / 60 / 60 / 1000);
                                            }
                                        }
                                    }

                                    if(Ot.getOvertimetype().equals("PR001001")){//平日加班
                                        if(attendance.getOrdinaryindustry() != null && !attendance.getOrdinaryindustry().isEmpty()){
                                            overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours) + Double.valueOf(attendance.getOrdinaryindustry())));
                                        }
                                        else{
                                            overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours)));
                                        }
                                        attendance.setOrdinaryindustry(overtimeHours);
                                        if(overtimeHoursNight != null && !overtimeHoursNight.isEmpty()){
                                            if(attendance.getOrdinaryindustrynight() != null && !attendance.getOrdinaryindustrynight().isEmpty()){
                                                overtimeHoursNight = String.valueOf(df.format(Double.valueOf(overtimeHoursNight) + Double.valueOf(attendance.getOrdinaryindustrynight())));
                                            }
                                            attendance.setOrdinaryindustrynight(overtimeHoursNight);
                                        }
                                    }
                                    else if(Ot.getOvertimetype().equals("PR001002")){//周末加班
                                        if(attendance.getWeekendindustry() != null && !attendance.getWeekendindustry().isEmpty()){
                                            overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours) + Double.valueOf(attendance.getWeekendindustry())));
                                        }
                                        else{
                                            overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours)));
                                        }
                                        attendance.setWeekendindustry(overtimeHours);
                                        if(overtimeHoursNight != null && !overtimeHoursNight.isEmpty()){
                                            if(attendance.getWeekendindustrynight() != null && !attendance.getWeekendindustrynight().isEmpty()){
                                                overtimeHoursNight = String.valueOf(df.format(Double.valueOf(overtimeHoursNight) + Double.valueOf(attendance.getWeekendindustrynight())));
                                            }
                                            else{
                                                overtimeHoursNight = String.valueOf(df.format(Double.valueOf(overtimeHoursNight)));
                                            }
                                            attendance.setWeekendindustrynight(overtimeHoursNight);
                                        }
                                        //进代休表111
                                    }
                                    else if(Ot.getOvertimetype().equals("PR001003")){//法定日加班
                                        if(attendance.getStatutoryresidue() != null && !attendance.getStatutoryresidue().isEmpty()){
                                            overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours) + Double.valueOf(attendance.getStatutoryresidue())));
                                        }
                                        else{
                                            overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours)));
                                        }
                                        attendance.setStatutoryresidue(overtimeHours);
                                        if(overtimeHoursNight != null && !overtimeHoursNight.isEmpty()){
                                            if(attendance.getStatutoryresiduenight() != null && !attendance.getStatutoryresiduenight().isEmpty()){
                                                overtimeHoursNight = String.valueOf(df.format(Double.valueOf(overtimeHoursNight) + Double.valueOf(attendance.getStatutoryresiduenight())));
                                            }
                                            else{
                                                overtimeHoursNight = String.valueOf(df.format(Double.valueOf(overtimeHoursNight)));
                                            }
                                            attendance.setStatutoryresiduenight(overtimeHoursNight);
                                        }
                                    }
                                    else if(Ot.getOvertimetype().equals("PR001004")){//一齐年休日加班
                                        overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours)));
                                        attendance.setAnnualrestday(overtimeHours);

                                    }
                                    else if(Ot.getOvertimetype().equals("PR001005")){//会社特别休日加班
                                        overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours)));
                                        attendance.setSpecialday(overtimeHours);
                                    }
                                    else if(Ot.getOvertimetype().equals("PR001007")){//五四青年节
                                        overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours)));
                                        attendance.setYouthday(overtimeHours);
                                        //进代休表
                                    }
                                    else if(Ot.getOvertimetype().equals("PR001008")){//妇女节
                                        overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours)));
                                        attendance.setWomensday(overtimeHours);
                                        //进代休表
                                    }
                                    //承认
                                    Ot.setRecognitionstate(AuthConstants.RECOGNITION_FLAG_YES);
                                    Ot.preUpdate(tokenModel);
                                    overtimeMapper.updateByPrimaryKey(Ot);
                                }
                            }

                            //---------处理昨日审批通过的加班申请end-------

                            //正常的打卡记录
                            if(sdf.parse(time_start).getTime() <= sdf.parse(workshift_start).getTime()
                                && sdf.parse(closingtime_start).getTime() <= sdf.parse(time_end).getTime()){
                                attendance.setNormal(workinghours);
                            }
                            else //异常打卡的情况
                            {
                                attendance.setAbsenteeism("0");
                                //一条卡达记录记录的请路况
                                if(sdf.parse(time_start).getTime() == sdf.parse(time_end).getTime()){
                                    attendance.setAbsenteeism(String.valueOf(Double.valueOf(absenteeism) * 2));
                                }
                                //迟到
                                if(sdf.parse(time_start).getTime() != sdf.parse(time_end).getTime() &&
                                        sdf.parse(time_start).getTime() > sdf.parse(workshift_start).getTime()){
                                    //迟到的时间
                                    long result = sdf.parse(time_start).getTime() - sdf.parse(workshift_start).getTime();
                                    //应该补的时间
                                    Double strunit = getUnit(Double.valueOf(result),Double.valueOf(lateearlyleave));
                                    //迟到的小时
                                    Double Dhourresult = strunit / 60 / 60 / 1000;
                                    if(Integer.valueOf(lateearlyleave) > Integer.valueOf(String.valueOf(result))){
                                        //迟到小于15分钟的处理（算旷工半天）
                                        attendance.setAbsenteeism(absenteeism);
                                        attendance.setLatetime(String.valueOf(df.format(Dhourresult)));
                                    }
                                    else{//迟到大于15分钟算旷工
                                        if(Dhourresult <= Double.valueOf(absenteeism)){//迟到半天
                                            attendance.setAbsenteeism(absenteeism);
                                            attendance.setLatetime(String.valueOf(df.format(Dhourresult)));
                                        }
                                        else{//迟到大于半天
                                            attendance.setAbsenteeism(String.valueOf(Double.valueOf(absenteeism) * 2));
                                            attendance.setLatetime(String.valueOf(df.format(Dhourresult)));
                                        }
                                    }
                                }
                                //早退
                                if(sdf.parse(time_start).getTime() != sdf.parse(time_end).getTime() &&
                                        sdf.parse(closingtime_start).getTime() > sdf.parse(time_end).getTime()){
                                    long result = sdf.parse(closingtime_start).getTime() - sdf.parse(time_end).getTime();
                                    //应该补的时间
                                    Double strunit = getUnit(Double.valueOf(result),Double.valueOf(lateearlyleave));
                                    //早退的小时
                                    Double Dhourresult = strunit / 60 / 60 / 1000;
                                    if(Integer.valueOf(lateearlyleave) > Integer.valueOf(String.valueOf(result))){
                                        //早退小于15分钟的处理（算旷工半天）
                                        attendance.setAbsenteeism(String.valueOf(Double.valueOf(attendance.getAbsenteeism()) + Double.valueOf(absenteeism)));
                                        attendance.setLeaveearlytime(String.valueOf(df.format(Dhourresult)));
                                    }
                                    else{//早退大于15分钟算旷工
                                        if(Dhourresult <= Double.valueOf(absenteeism)){//早退半天
                                            attendance.setAbsenteeism(String.valueOf(Double.valueOf(attendance.getAbsenteeism()) + Double.valueOf(absenteeism)));
                                            attendance.setLeaveearlytime(String.valueOf(df.format(Dhourresult)));
                                        }
                                        else{//早退大于半天
                                            attendance.setAbsenteeism(String.valueOf(Double.valueOf(absenteeism) * 2));
                                            attendance.setLeaveearlytime(String.valueOf(df.format(Dhourresult)));
                                        }
                                    }
                                }
                            }

                            //---------处理昨日审批通过的异常考勤申请start-------
                            AbNormal abnormal = new AbNormal();
                            abnormal.setUser_id(strUserid);
                            abnormal.setOccurrencedate(dateStart);
                            abnormal.setStatus(AuthConstants.APPROVED_FLAG_YES);
                            List<AbNormal> abNormallist = abNormalMapper.select(abnormal);
                            if(abNormallist.size() > 0){
                                for(AbNormal ab : abNormallist){
                                    String strlengthtime = ab.getLengthtime();
                                    if(ab.getErrortype().equals("PR013002")){//迟到
                                        if(Double.valueOf(strlengthtime) >= Double.valueOf(attendance.getLatetime())){
                                            attendance.setLate(strlengthtime);
                                            if(Double.valueOf(attendance.getLatetime()) > Double.valueOf(absenteeism)){
                                                attendance.setAbsenteeism(absenteeism);
                                            }
                                            else{
                                                attendance.setAbsenteeism("");
                                            }
                                            attendance.setLatetime(null);
                                        }
                                    }
                                    else if(ab.getErrortype().equals("PR013003")){//早退
                                        if(Double.valueOf(strlengthtime) >= Double.valueOf(attendance.getLeaveearlytime())){
                                            attendance.setLeaveearly(strlengthtime);
                                            if(Double.valueOf(attendance.getLeaveearlytime()) > Double.valueOf(absenteeism)){
                                                attendance.setAbsenteeism(absenteeism);
                                            }
                                            else{
                                                attendance.setAbsenteeism("");
                                            }
                                            attendance.setLeaveearlytime(null);
                                        }
                                    }
                                    else if(ab.getErrortype().equals("PR013004")){//女性三期
                                    }
                                    else if(ab.getErrortype().equals("PR013005")){//年休
                                        if(attendance.getAnnualrest() != null && !attendance.getAnnualrest().isEmpty()){
                                            strlengthtime = String.valueOf(df.format(Double.valueOf(strlengthtime) + Double.valueOf(attendance.getAnnualrest())));
                                        }
                                        else{
                                            strlengthtime = String.valueOf(df.format(Double.valueOf(strlengthtime)));
                                        }
                                        attendance.setAnnualrest(strlengthtime);
                                    }
                                    else if(ab.getErrortype().equals("PR013006")){//代休-周末
                                        if(attendance.getDaixiu() != null && !attendance.getDaixiu().isEmpty()){
                                            strlengthtime = String.valueOf(df.format(Double.valueOf(strlengthtime) + Double.valueOf(attendance.getDaixiu())));
                                        }
                                        else{
                                            strlengthtime = String.valueOf(df.format(Double.valueOf(strlengthtime)));
                                        }
                                        attendance.setDaixiu(strlengthtime);
                                    }
                                    else if(ab.getErrortype().equals("PR013007")){//代休-特殊
                                        if(attendance.getDaixiu() != null && !attendance.getDaixiu().isEmpty()){
                                            strlengthtime = String.valueOf(df.format(Double.valueOf(strlengthtime) + Double.valueOf(attendance.getDaixiu())));
                                        }
                                        else{
                                            strlengthtime = String.valueOf(df.format(Double.valueOf(strlengthtime)));
                                        }
                                        attendance.setDaixiu(strlengthtime);
                                    }
                                    else if(ab.getErrortype().equals("PR013008")){//事休
                                        if(attendance.getCompassionateleave() != null && !attendance.getCompassionateleave().isEmpty()){
                                            strlengthtime = String.valueOf(df.format(Double.valueOf(strlengthtime) + Double.valueOf(attendance.getCompassionateleave())));
                                        }
                                        else{
                                            strlengthtime = String.valueOf(df.format(Double.valueOf(strlengthtime)));
                                        }
                                        attendance.setCompassionateleave(strlengthtime);
                                    }
                                    else if(ab.getErrortype().equals("PR013009")){//短期病休
                                        if(attendance.getShortsickleave() != null && !attendance.getShortsickleave().isEmpty()){
                                            strlengthtime = String.valueOf(df.format(Double.valueOf(strlengthtime) + Double.valueOf(attendance.getShortsickleave())));
                                        }
                                        else{
                                            strlengthtime = String.valueOf(df.format(Double.valueOf(strlengthtime)));
                                        }
                                        attendance.setShortsickleave(strlengthtime);
                                    }
                                    else if(ab.getErrortype().equals("PR013010")){//長期病休
                                        if(attendance.getLongsickleave() != null && !attendance.getLongsickleave().isEmpty()){
                                            strlengthtime = String.valueOf(df.format(Double.valueOf(strlengthtime) + Double.valueOf(attendance.getLongsickleave())));
                                        }
                                        else{
                                            strlengthtime = String.valueOf(df.format(Double.valueOf(strlengthtime)));
                                        }
                                        attendance.setLongsickleave(strlengthtime);
                                    }
                                    else if(ab.getErrortype().equals("PR013011")){//结婚休假
//                                        if(attendance.getLongsickleave() != null && !attendance.getLongsickleave().isEmpty()){
//                                            strlengthtime = String.valueOf(df.format(Double.valueOf(strlengthtime) + Double.valueOf(attendance.getLongsickleave())));
//                                        }
//                                        else{
//                                            strlengthtime = String.valueOf(df.format(Double.valueOf(strlengthtime)));
//                                        }
//                                        attendance.setLongsickleave(strlengthtime);
                                    }
                                    else if(ab.getErrortype().equals("PR013012")){//産休（女）
                                        if(attendance.getNursingleave() != null && !attendance.getNursingleave().isEmpty()){
                                            strlengthtime = String.valueOf(df.format(Double.valueOf(strlengthtime) + Double.valueOf(attendance.getNursingleave())));
                                        }
                                        else{
                                            strlengthtime = String.valueOf(df.format(Double.valueOf(strlengthtime)));
                                        }
                                        attendance.setNursingleave(strlengthtime);
                                    }
                                    else if(ab.getErrortype().equals("PR013012")){//産休看護休暇（男）
                                        if(attendance.getNursingleave() != null && !attendance.getNursingleave().isEmpty()){
                                            strlengthtime = String.valueOf(df.format(Double.valueOf(strlengthtime) + Double.valueOf(attendance.getNursingleave())));
                                        }
                                        else{
                                            strlengthtime = String.valueOf(df.format(Double.valueOf(strlengthtime)));
                                        }
                                        attendance.setNursingleave(strlengthtime);
                                    }
                                    else if(ab.getErrortype().equals("PR013014") || ab.getErrortype().equals("PR013015")
                                        || ab.getErrortype().equals("PR013016") || ab.getErrortype().equals("PR013017")
                                        || ab.getErrortype().equals("PR013018") || ab.getErrortype().equals("PR013019")){//福利假期
                                        //家长会假
                                        //葬儀休暇
                                        //妊娠檢查休暇
                                        //哺乳休暇
                                        //労災休暇
                                        //其他休暇
                                        if(attendance.getWelfare() != null && !attendance.getWelfare().isEmpty()){
                                            strlengthtime = String.valueOf(df.format(Double.valueOf(strlengthtime) + Double.valueOf(attendance.getWelfare())));
                                        }
                                        else{
                                            strlengthtime = String.valueOf(df.format(Double.valueOf(strlengthtime)));
                                        }
                                        attendance.setWelfare(strlengthtime);
                                    }

                                }
                            }
                            //---------处理昨日审批通过的异常考勤申请end-------

                            //更新考勤表
                            if(attendance.getAbsenteeism() != null){
                                if(attendance.getAbsenteeism().equals("")){
                                    attendance.setAbsenteeism(null);
                                    attendance.setNormal(workinghours);
                                }
                                else{
                                    if(Double.valueOf(attendance.getAbsenteeism()) > Double.valueOf(absenteeism) * 2){
                                        attendance.setAbsenteeism(String.valueOf(Double.valueOf(absenteeism) * 2));
                                    }
                                }
                            }
                            saveAttendance(attendance,"0",tokenModel);
                        }
                    }
                    //---------不定时考勤人员(非不定时考勤人员才计算)end-------
                }
            }
            //------------------------------查询本次打卡记录导入的数据end-------------
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
