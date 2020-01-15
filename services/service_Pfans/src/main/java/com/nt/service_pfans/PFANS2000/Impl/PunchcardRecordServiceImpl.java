package com.nt.service_pfans.PFANS2000.Impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Pfans.PFANS1000.Salesdetails;
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
                punchcardrecord.preInsert(tokenModel);
                punchcardrecord.setRegion(punchcard.getRegion());
                punchcardrecordMapper.insert(punchcardrecord);
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
            //平时晚加班时间扣除
            weekdaysovertime = attendancesettinglist.get(0).getWeekdaysovertime();
            BigDecimal weekdaysovertimehour = new BigDecimal(weekdaysovertime).multiply(new BigDecimal(60 * 60 * 1000));
            weekdaysovertime = String.valueOf(weekdaysovertimehour.intValue());
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
                        //正常的打卡记录
                        if(sdf.parse(time_start).getTime() <= sdf.parse(workshift_start).getTime()
                            && sdf.parse(closingtime_start).getTime() <= sdf.parse(time_end).getTime()){
                                String strNormal = "8";
                                //---------弹性工作制end-------
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
                                        if(Ot.getOvertimetype().equals("PR001002") || Ot.getOvertimetype().equals("PR001003")){//周末加班/法定日加班
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
                                        Attendance attendance = new Attendance();
                                        attendance.setUser_id(strUserid);
                                        attendance.setDates(dateStart);
                                        List<Attendance> attendancelist = attendanceMapper.select(attendance);
                                        DecimalFormat  df = new DecimalFormat("######0.00");
                                        if(attendancelist.size() > 0){
                                            for (Attendance attend : attendancelist) {
                                                if(Ot.getOvertimetype().equals("PR001001")){//平日加班
                                                    if(attend.getOrdinaryindustry() != null && !attend.getOrdinaryindustry().isEmpty()){
                                                        overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours) + Double.valueOf(attend.getOrdinaryindustry())));
                                                    }
                                                    else{
                                                        overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours)));
                                                    }
                                                    attend.setOrdinaryindustry(overtimeHours);
                                                    if(overtimeHoursNight != null && !overtimeHoursNight.isEmpty()){
                                                        if(attend.getOrdinaryindustrynight() != null && !attend.getOrdinaryindustrynight().isEmpty()){
                                                            overtimeHoursNight = String.valueOf(df.format(Double.valueOf(overtimeHoursNight) + Double.valueOf(attend.getOrdinaryindustrynight())));
                                                        }
                                                        attend.setOrdinaryindustrynight(overtimeHoursNight);
                                                    }
                                                }
                                                else if(Ot.getOvertimetype().equals("PR001002")){//周末加班
                                                    if(attend.getWeekendindustry() != null && !attend.getWeekendindustry().isEmpty()){
                                                        overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours) + Double.valueOf(attend.getWeekendindustry())));
                                                    }
                                                    else{
                                                        overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours)));
                                                    }
                                                    attend.setWeekendindustry(overtimeHours);
                                                    if(overtimeHoursNight != null && !overtimeHoursNight.isEmpty()){
                                                        if(attend.getWeekendindustrynight() != null && !attend.getWeekendindustrynight().isEmpty()){
                                                            overtimeHoursNight = String.valueOf(df.format(Double.valueOf(overtimeHoursNight) + Double.valueOf(attend.getWeekendindustrynight())));
                                                        }
                                                        else{
                                                            overtimeHoursNight = String.valueOf(df.format(Double.valueOf(overtimeHoursNight)));
                                                        }
                                                        attend.setWeekendindustrynight(overtimeHoursNight);
                                                    }
                                                    //进代休表
                                                }
                                                else if(Ot.getOvertimetype().equals("PR001003")){//法定日加班
                                                    if(attend.getStatutoryresidue() != null && !attend.getStatutoryresidue().isEmpty()){
                                                        overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours) + Double.valueOf(attend.getStatutoryresidue())));
                                                    }
                                                    else{
                                                        overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours)));
                                                    }
                                                    attend.setStatutoryresidue(overtimeHours);
                                                    if(overtimeHoursNight != null && !overtimeHoursNight.isEmpty()){
                                                        if(attend.getStatutoryresiduenight() != null && !attend.getStatutoryresiduenight().isEmpty()){
                                                            overtimeHoursNight = String.valueOf(df.format(Double.valueOf(overtimeHoursNight) + Double.valueOf(attend.getStatutoryresiduenight())));
                                                        }
                                                        else{
                                                            overtimeHoursNight = String.valueOf(df.format(Double.valueOf(overtimeHoursNight)));
                                                        }
                                                        attend.setStatutoryresiduenight(overtimeHoursNight);
                                                    }
                                                }
                                                else if(Ot.getOvertimetype().equals("PR001004")){//一齐年休日加班
                                                    overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours)));
                                                    attend.setAnnualrestday(overtimeHours);

                                                }
                                                else if(Ot.getOvertimetype().equals("PR001005")){//会社特别休日加班
                                                    overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours)));
                                                    attend.setSpecialday(overtimeHours);
                                                }
                                                else if(Ot.getOvertimetype().equals("PR001006")){//振替休日加班

                                                }
                                                else if(Ot.getOvertimetype().equals("PR001007")){//五四青年节
                                                    overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours)));
                                                    attend.setYouthday(overtimeHours);
                                                    //进代休表
                                                }
                                                else if(Ot.getOvertimetype().equals("PR001008")){//妇女节
                                                    overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours)));
                                                    attend.setWomensday(overtimeHours);
                                                    //进代休表
                                                }
                                                saveAttendance(attend,"0",tokenModel);
                                            }
                                        }
                                        else{
                                            attendance.setCenter_id(Ot.getCenterid());
                                            attendance.setGroup_id(Ot.getGroupid());
                                            attendance.setTeam_id(Ot.getTeamid());
                                            attendance.setYears(DateUtil.format(Ot.getReserveovertimedate(),"YYYY").toString());
                                            attendance.setMonths(DateUtil.format(Ot.getReserveovertimedate(),"MM").toString());
                                            attendance.setUser_id(Ot.getUserid());
                                            attendance.setDates(Ot.getReserveovertimedate());
                                            attendance.setNormal(strNormal);
                                            attendance.setActual(worktime);
                                            if(Ot.getOvertimetype().equals("PR001001")){//平日加班
                                                attendance.setOrdinaryindustry(overtimeHours);
                                                if(overtimeHoursNight != null && !overtimeHoursNight.isEmpty()){
                                                    attendance.setOrdinaryindustrynight(overtimeHoursNight);
                                                }
                                            }
                                            else if(Ot.getOvertimetype().equals("PR001002")){//周末加班
                                                attendance.setWeekendindustry(overtimeHours);
                                                if(overtimeHoursNight != null && !overtimeHoursNight.isEmpty()){
                                                    attendance.setWeekendindustrynight(overtimeHoursNight);
                                                }
                                                //进代休表
                                            }
                                            else if(Ot.getOvertimetype().equals("PR001003")){//法定日加班
                                                attendance.setStatutoryresidue(overtimeHours);
                                                if(overtimeHoursNight != null && !overtimeHoursNight.isEmpty()){
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
                                            else if(Ot.getOvertimetype().equals("PR001006")){//振替休日加班

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
                                            saveAttendance(attendance,"1",tokenModel);
                                        }

                                        //承认
                                        Ot.setRecognitionstate(AuthConstants.RECOGNITION_FLAG_YES);
                                        Ot.preUpdate(tokenModel);
                                        overtimeMapper.updateByPrimaryKey(Ot);
                                    }
                                }
                                //---------处理昨日审批通过的加班申请end-------
                                else{//没有加班的情况
                                    Attendance attendance = new Attendance();
                                    attendance.setUser_id(strUserid);
                                    attendance.setDates(dateStart);
                                    List<Attendance> attendancelist = attendanceMapper.select(attendance);
                                    if(attendancelist.size() == 0){
                                        attendance.setCenter_id(PR.getCenter_id());
                                        attendance.setGroup_id(PR.getGroup_id());
                                        attendance.setTeam_id(PR.getTeam_id());
                                        attendance.setYears(DateUtil.format(dateStart,"YYYY").toString());
                                        attendance.setMonths(DateUtil.format(dateStart,"MM").toString());
                                        attendance.setUser_id(strUserid);
                                        attendance.setDates(dateStart);
                                        attendance.setNormal(strNormal);
                                        attendance.setActual(worktime);
                                        saveAttendance(attendance,"1",tokenModel);
                                    }
                                }
                        }
                        else //异常打卡的情况
                        {
                            Attendance attendance = new Attendance();
                            attendance.setUser_id(strUserid);
                            attendance.setDates(dateStart);
                            List<Attendance> attendancelist = attendanceMapper.select(attendance);
                            //15分钟为基本计算单位
                            double unit = 15;
                            //迟到
                            if(sdf.parse(time_start).getTime() > sdf.parse(workshift_start).getTime()
                            && sdf.parse(closingtime_start).getTime() <= sdf.parse(time_end).getTime()){
                                long result = sdf.parse(time_start).getTime() - sdf.parse(workshift_start).getTime();
                                //迟到的时间
                                Double Dresult = Double.valueOf(String.valueOf(result)) / 60 / 1000;
                                //迟到的小时
                                Double Dhourresult = Double.valueOf(String.valueOf(result)) / 60 / 60 / 1000;
                                if(unit > Dresult){//迟到小于15分钟
                                    //迟到小于15分钟的处理
                                    AbNormal abnormal = new AbNormal();
                                    abnormal.setUser_id(strUserid);
                                    //迟到
                                    abnormal.setErrortype("PR013002");
                                    abnormal.setOccurrencedate(dateStart);
//                                    abnormal.setFinisheddate(dateStart);
                                    abnormal.setStatus("4");
                                    List<AbNormal> abNormallist = abNormalMapper.select(abnormal);
                                    if(abNormallist.size() > 0){
                                        String strPeriodstart = sdf.format(abNormallist.get(0).getPeriodstart());
                                        String strPeriodend = sdf.format(abNormallist.get(0).getPeriodend());
                                        long result1 = sdf.parse(strPeriodend).getTime() - sdf.parse(strPeriodstart).getTime();
                                        Double Dresult1 = Double.valueOf(String.valueOf(result1)) / 60 / 1000;
                                        //迟到申请时间大于等于15分钟（按迟到15分钟算）
                                        if(Dresult1 >= unit){
                                            attendance.setLate(String.valueOf(unit / 60));
                                        }
                                        else{
                                            //迟到时间小于15分钟（按没有申请处理，旷工半天）
                                            attendance.setAbsenteeism("4");
                                        }
                                    }
                                    else{
                                        //迟到小于15分钟没有申请的处理（算旷工半天）
                                        attendance.setAbsenteeism("4");
                                    }
                                }
                                else{//迟到大于15分钟算旷工
                                    if(Dhourresult <= 4){//迟到半天
                                        attendance.setAbsenteeism("4");
                                    }
                                    else{//迟到大于半天
                                        attendance.setAbsenteeism("8");
                                    }
                                }
                            }
                            //早退
                            else if(sdf.parse(time_start).getTime() <= sdf.parse(workshift_start).getTime()
                                    && sdf.parse(closingtime_start).getTime() > sdf.parse(time_end).getTime()){
                                long result = sdf.parse(closingtime_start).getTime() - sdf.parse(time_end).getTime();
                                //早退的时间
                                Double Dresult = Double.valueOf(String.valueOf(result)) / 60 / 1000;
                                //早退的小时
                                Double Dhourresult = Double.valueOf(String.valueOf(result)) / 60 / 60 / 1000;
                                if(unit > Dresult){//早退小于15分钟
                                    //早退小于15分钟的处理
                                    AbNormal abnormal = new AbNormal();
                                    abnormal.setUser_id(strUserid);
                                    //早退申请
                                    abnormal.setErrortype("PR013003");
                                    abnormal.setOccurrencedate(dateStart);
                                    abnormal.setStatus("4");
                                    List<AbNormal> abNormallist = abNormalMapper.select(abnormal);
                                    if(abNormallist.size() > 0){
                                        String strPeriodstart = sdf.format(abNormallist.get(0).getPeriodstart());
                                        String strPeriodend = sdf.format(abNormallist.get(0).getPeriodend());
                                        long result1 = sdf.parse(strPeriodend).getTime() - sdf.parse(strPeriodstart).getTime();
                                        Double Dresult1 = Double.valueOf(String.valueOf(result1)) / 60 / 1000;
                                        //早退申请时间大于等于15分钟（按早退15分钟算）
                                        if(Dresult1 >= unit){
                                            attendance.setLeaveearly(String.valueOf(unit / 60));
                                        }
                                        else{
                                            //早退申请时间小于15分钟（按没有申请处理，旷工半天）
                                            attendance.setAbsenteeism("4");
                                        }
                                    }
                                    else{
                                        //早退小于15分钟没有申请的处理（算旷工半天）
                                        attendance.setAbsenteeism("4");
                                    }
                                }
                                else{//早退大于15分钟算旷工
                                    if(Dhourresult <= 4){//早退半天
                                        attendance.setAbsenteeism("4");
                                    }
                                    else{//迟到大于半天
                                        attendance.setAbsenteeism("8");
                                    }
                                }
                            }
                            if(attendancelist.size() > 0){
                                saveAttendance(attendance,"0",tokenModel);
                            }
                            else{
                                attendance.setCenter_id(strCenter_id);
                                attendance.setGroup_id(strGroup_id);
                                attendance.setTeam_id(strTeam_id);
                                attendance.setYears(DateUtil.format(dateStart,"YYYY").toString());
                                attendance.setMonths(DateUtil.format(dateStart,"MM").toString());
//                                attendance.setNormal(strNormal);
                                attendance.setActual(worktime);
                                saveAttendance(attendance,"1",tokenModel);
                            }
                        }
                    }
                    //---------不定时考勤人员(非不定时考勤人员才计算)end-------
                }
            }
            //------------------------------查询本次打卡记录导入的数据end-------------
        }
    }

    public void saveAttendance(Attendance attendance,String Flg,TokenModel tokenModel) throws Exception {
        if(Flg.equals("0")){//更新
            attendance.preUpdate(tokenModel);
            attendanceMapper.updateByPrimaryKey(attendance);
        }
        else{//新建
            attendance.setAttendanceid(UUID.randomUUID().toString());
            attendance.setRecognitionstate(AuthConstants.RECOGNITION_FLAG_NO);
            attendance.setOwner(attendance.getUser_id());
            attendance.preInsert(tokenModel);
            attendanceMapper.insert(attendance);
        }
    }
}
