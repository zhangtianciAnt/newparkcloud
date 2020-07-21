package com.nt.service_AOCHUAN.AOCHUAN6000.Impl;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.nt.dao_AOCHUAN.AOCHUAN6000.Attendance;
import com.nt.dao_AOCHUAN.AOCHUAN6000.Vacation;
import com.nt.dao_Org.CustomerInfo;
import com.nt.service_AOCHUAN.AOCHUAN6000.AttendancesService;
import com.nt.service_AOCHUAN.AOCHUAN6000.mapper.AttendancesMapper;
import com.nt.service_AOCHUAN.AOCHUAN6000.mapper.VacationMapper;
import com.nt.service_Auth.RoleService;
import com.nt.service_Org.EWechatService;
import com.nt.service_Org.ToDoNoticeService;
import com.nt.utils.ApiResult;
import com.nt.utils.EWxUserApi;
import com.nt.utils.LogicalException;
import com.nt.utils.dao.EWeixinOauth2Token;
import com.nt.utils.dao.EWxBaseResponse;
import com.nt.utils.dao.EWxCheckData;
import com.nt.utils.dao.TokenModel;
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
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@EnableScheduling
@Transactional(rollbackFor = Exception.class)
public class AttendancesServiceImpl implements AttendancesService {

    @Autowired
    private AttendancesMapper attendanceMapper;

    @Autowired
    private VacationMapper vacationMapper;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ToDoNoticeService toDoNoticeService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private EWechatService ewechatService;

    @Autowired
    private AttendancesService attendanceService;

    //考勤一览初期值
    @Override
    public List<Attendance> get(Attendance attendance) throws Exception {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<Attendance> attendanceList = attendanceMapper.select(attendance);
        for (Attendance att : attendanceList) {
            Vacation vacation = new Vacation();
            vacation.setApplicanterid(att.getNames());
            vacation.setStatus("4");

            List<Vacation> vacationList = vacationMapper.select(vacation);//休假
            for (Vacation vac : vacationList) {
                String vacstart = simpleDateFormat.format(vac.getStartdate());
                Date datestart = simpleDateFormat.parse(vacstart); //请假开始时间

                String vacend = simpleDateFormat.format(vac.getEnddate());
                Date datestaend = simpleDateFormat.parse(vacend); //请假结束时间


                String attendancetim = "20" + att.getAttendancetim();
                Date attdate = simpleDateFormat.parse(attendancetim);//考勤时间

                int compareToKS = attdate.compareTo(datestart);
                int compareToJS = attdate.compareTo(datestaend);

                if (compareToKS == 1 && compareToJS == -1) {
                    att.setLeaves("1");
                    break;
                }
                if (compareToKS == 0 && compareToJS == -1 && (vac.getStarttim().equals("1") || vac.getStarttim().equals("2"))) {
                    att.setLeaves("1");
                    break;
                }
                if (compareToKS == 0 && compareToJS == -1 && (vac.getStarttim().equals("2"))) {
                    att.setLeaves("0.5");
                    att.setTimtype("2");
                    break;
                }
                if (compareToKS == 1 && compareToJS == 0 && (vac.getEndtim().equals("1") || vac.getStarttim().equals("3"))) {
                    att.setLeaves("1");
                    break;
                }
                if (compareToKS == 1 && compareToJS == 0 && (vac.getEndtim().equals("2"))) {
                    att.setLeaves("0.5");
                    att.setTimtype("2");
                    break;
                }
                if (compareToKS == 1 && compareToJS == 1) {
                    att.setLeaves("0");

                }
                if (compareToKS == -1 && compareToJS == -1) {
                    att.setLeaves("0");

                }
                if (compareToKS == 0 && compareToJS == 0 && vac.getDatetype().equals("1")) {
                    att.setLeaves("1");
                    break;
                }
                if (compareToKS == 0 && compareToJS == 0 && vac.getDatetype().equals("2")) {
                    att.setLeaves("0.5");
//                    att.setTimtype("2");
                    break;
                }
                if (compareToKS == 0 && compareToJS == 0 && vac.getDatetype().equals("3")) {
                    att.setLeaves("0.5");
//                    att.setTimtype("3");
                    break;
                }


            }

        }
        return attendanceList;
    }

    //    考勤异常数据
    @Override
    public List<Attendance> getYICHANG(Attendance attendance) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<Attendance> attendanceList = attendanceMapper.getYICHANG("星期一", "星期二", "星期三", "星期四", "星期五");
        for (Attendance att : attendanceList) {
            Vacation vacation = new Vacation();
            vacation.setApplicanterid(att.getNames());
            vacation.setStatus("4");

            List<Vacation> vacationList = vacationMapper.select(vacation);//休假
            for (Vacation vac : vacationList) {
                String vacstart = simpleDateFormat.format(vac.getStartdate());
                Date datestart = simpleDateFormat.parse(vacstart); //请假开始时间

                String vacend = simpleDateFormat.format(vac.getEnddate());
                Date datestaend = simpleDateFormat.parse(vacend); //请假结束时间


                String attendancetim = "20" + att.getAttendancetim();
                Date attdate = simpleDateFormat.parse(attendancetim);//考勤时间

                int compareToKS = attdate.compareTo(datestart);
                int compareToJS = attdate.compareTo(datestaend);

                if (compareToKS == 1 && compareToJS == -1) {
                    att.setLeaves("1");
                    break;
                }
                if (compareToKS == 0 && compareToJS == -1 && (vac.getStarttim().equals("1") || vac.getStarttim().equals("2"))) {
                    att.setLeaves("1");
                    break;
                }
                if (compareToKS == 0 && compareToJS == -1 && (vac.getStarttim().equals("2"))) {
                    att.setLeaves("0.5");
                    att.setTimtype("2");
                    break;
                }
                if (compareToKS == 1 && compareToJS == 0 && (vac.getEndtim().equals("1") || vac.getStarttim().equals("3"))) {
                    att.setLeaves("1");
                    break;
                }
                if (compareToKS == 1 && compareToJS == 0 && (vac.getEndtim().equals("2"))) {
                    att.setLeaves("0.5");
                    att.setTimtype("2");
                    break;
                }
                if (compareToKS == 1 && compareToJS == 1) {
                    att.setLeaves("0");

                }
                if (compareToKS == -1 && compareToJS == -1) {
                    att.setLeaves("0");

                }
                if (compareToKS == 0 && compareToJS == 0 && vac.getDatetype().equals("1")) {
                    att.setLeaves("1");
                    break;
                }
                if (compareToKS == 0 && compareToJS == 0 && vac.getDatetype().equals("2")) {
                    att.setLeaves("0.5");
//                    att.setTimtype("2");
                    break;
                }
                if (compareToKS == 0 && compareToJS == 0 && vac.getDatetype().equals("3")) {
                    att.setLeaves("0.5");
//                    att.setTimtype("3");
                    break;
                }


            }

        }

        return attendanceList;
    }

    //考勤一览根据天去查询(废弃)
    @Override
    public List<Attendance> getNow(Attendance attendance) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<Attendance> attendanceList = attendanceMapper.select(attendance);
        for (Attendance att : attendanceList) {
            Vacation vacation = new Vacation();//异常申请表
            vacation.setApplicanterid(att.getNames());
            vacation.setStatus("4");

            List<Vacation> vacationList = vacationMapper.select(vacation);
            for (Vacation vac : vacationList) {
                String vacstart = simpleDateFormat.format(vac.getStartdate());
                Date datestart = simpleDateFormat.parse(vacstart); //请假开始时间

                String vacend = simpleDateFormat.format(vac.getEnddate());
                Date datestaend = simpleDateFormat.parse(vacend); //请假结束时间


                String attendancetim = "20" + att.getAttendancetim();
                Date attdate = simpleDateFormat.parse(attendancetim);//考勤时间

                int compareToJS = attdate.compareTo(datestart);
                int compareToKS = attdate.compareTo(datestaend);

                if (compareToJS == 1 && compareToKS == -1) {
                    att.setLeaves("1");
                    break;
                }
                if ((compareToJS == 0 && compareToKS == -1) || (compareToJS == 1 && compareToKS == 0)) {
                    att.setLeaves("1");
                    break;
                }
                if (compareToJS == 1 && compareToKS == 1) {
                    att.setLeaves("0");
                    break;
                }
                if (compareToJS == 0 && compareToKS == 0 && vac.getDatetype().equals("1")) {
                    att.setLeaves("1");
                    break;
                }
                if (compareToJS == 0 && compareToKS == 0 && vac.getDatetype().equals("2")) {
                    att.setLeaves("0.5");
                    break;
                }
                if (compareToJS == 0 && compareToKS == 0 && vac.getDatetype().equals("3")) {
                    att.setLeaves("0.5");
                    break;
                }


            }

        }

        return attendanceList;
    }

    //考勤一览根据月，年去查询
    @Override
    public List<Attendance> getNowMons(String attendancetim, List<String> ownerList) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        List<Attendance> attendanceList = attendanceMapper.getNowMon(attendancetim, ownerList);
        for (Attendance att : attendanceList) {
            Vacation vacation = new Vacation();//异常申请表
            vacation.setApplicanterid(att.getNames());
            vacation.setStatus("4");

            List<Vacation> vacationList = vacationMapper.select(vacation);
            for (Vacation vac : vacationList) {
                String vacstart = simpleDateFormat.format(vac.getStartdate());
                Date datestart = simpleDateFormat.parse(vacstart); //请假开始时间

                String vacend = simpleDateFormat.format(vac.getEnddate());
                Date datestaend = simpleDateFormat.parse(vacend); //请假结束时间


                String attendancetims = "20" + att.getAttendancetim();
                Date attdate = simpleDateFormat.parse(attendancetims);//考勤时间

                int compareToKS = attdate.compareTo(datestart);
                int compareToJS = attdate.compareTo(datestaend);

                if (compareToKS == 1 && compareToJS == -1) {
                    att.setLeaves("1");
                    break;
                }
                if (compareToKS == 0 && compareToJS == -1 && (vac.getStarttim().equals("1") || vac.getStarttim().equals("2"))) {
                    att.setLeaves("1");
                    break;
                }
                if (compareToKS == 0 && compareToJS == -1 && (vac.getStarttim().equals("2"))) {
                    att.setLeaves("0.5");
                    att.setTimtype("2");
                    break;
                }
                if (compareToKS == 1 && compareToJS == 0 && (vac.getEndtim().equals("1") || vac.getStarttim().equals("3"))) {
                    att.setLeaves("1");
                    break;
                }
                if (compareToKS == 1 && compareToJS == 0 && (vac.getEndtim().equals("2"))) {
                    att.setLeaves("0.5");
                    att.setTimtype("2");
                    break;
                }
                if (compareToKS == 1 && compareToJS == 1) {
                    att.setLeaves("0");

                }
                if (compareToKS == -1 && compareToJS == -1) {
                    att.setLeaves("0");

                }
                if (compareToKS == 0 && compareToJS == 0 && vac.getDatetype().equals("1")) {
                    att.setLeaves("1");
                    break;
                }
                if (compareToKS == 0 && compareToJS == 0 && vac.getDatetype().equals("2")) {
                    att.setLeaves("0.5");
//                    att.setTimtype("2");
                    break;
                }
                if (compareToKS == 0 && compareToJS == 0 && vac.getDatetype().equals("3")) {
                    att.setLeaves("0.5");
//                    att.setTimtype("3");
                    break;
                }
            }
        }
        return attendanceList;
    }


    //考勤异常根据天，月，年去查询
    @Override
    public List<Attendance> getNowMonYC(String attendancetim, List<String> ownerList) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<Attendance> attendanceList = attendanceMapper.getNowMonYC("星期一", "星期二", "星期三", "星期四", "星期五", attendancetim,ownerList);
        for (Attendance att : attendanceList) {
            Vacation vacation = new Vacation();//异常申请表
            vacation.setApplicanterid(att.getNames());
            vacation.setStatus("4");

            List<Vacation> vacationList = vacationMapper.select(vacation);
            for (Vacation vac : vacationList) {
                String vacstart = simpleDateFormat.format(vac.getStartdate());
                Date datestart = simpleDateFormat.parse(vacstart); //请假开始时间

                String vacend = simpleDateFormat.format(vac.getEnddate());
                Date datestaend = simpleDateFormat.parse(vacend); //请假结束时间


                String attendancetims = "20" + att.getAttendancetim();
                Date attdate = simpleDateFormat.parse(attendancetims);//考勤时间

                int compareToKS = attdate.compareTo(datestart);
                int compareToJS = attdate.compareTo(datestaend);

                if (compareToKS == 1 && compareToJS == -1) {
                    att.setLeaves("1");
                    break;
                }
                if (compareToKS == 0 && compareToJS == -1 && (vac.getStarttim().equals("1") || vac.getStarttim().equals("2"))) {
                    att.setLeaves("1");
                    break;
                }
                if (compareToKS == 0 && compareToJS == -1 && (vac.getStarttim().equals("2"))) {
                    att.setLeaves("0.5");
                    att.setTimtype("2");
                    break;
                }
                if (compareToKS == 1 && compareToJS == 0 && (vac.getEndtim().equals("1") || vac.getStarttim().equals("3"))) {
                    att.setLeaves("1");
                    break;
                }
                if (compareToKS == 1 && compareToJS == 0 && (vac.getEndtim().equals("2"))) {
                    att.setLeaves("0.5");
                    att.setTimtype("2");
                    break;
                }
                if (compareToKS == 1 && compareToJS == 1) {
                    att.setLeaves("0");

                }
                if (compareToKS == -1 && compareToJS == -1) {
                    att.setLeaves("0");

                }
                if (compareToKS == 0 && compareToJS == 0 && vac.getDatetype().equals("1")) {
                    att.setLeaves("1");
                    break;
                }
                if (compareToKS == 0 && compareToJS == 0 && vac.getDatetype().equals("2")) {
                    att.setLeaves("0.5");
//                    att.setTimtype("2");
                    break;
                }
                if (compareToKS == 0 && compareToJS == 0 && vac.getDatetype().equals("3")) {
                    att.setLeaves("0.5");
//                    att.setTimtype("3");
                    break;
                }


            }

        }
        return attendanceList;
    }


    //  打卡记录导入 钉钉EXCEL
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public List<String> importUser(HttpServletRequest request, TokenModel tokenModel) throws Exception {
        try {
            //画面导入按钮
            List<String> Result = new ArrayList<String>();
//            MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
//            File f = null;
//            f = File.createTempFile("tmp", null);
//            file.transferTo(f);
//            //指定sheet页面读取
//            ExcelReader reader = ExcelUtil.getReader(f,1);

            //直接读取路径导入
            ExcelReader reader = ExcelUtil.getReader("E:\\Excel\\奥川生物666_考勤报表_20200401-20200430.xlsx", 1);

            List<List<Object>> list = reader.read();
            List<Object> model = new ArrayList<Object>();
            model.add("日期");
            model.add("姓名");
            model.add("部门");
            model.add("上班时间");
            model.add("下班时间");

            for (int j = 4; j < list.size(); j++) {

                if (!"总计".equals(list.get(j).get(6).toString())) {
                    Attendance attendance = new Attendance();
                    attendance.preInsert(tokenModel);
                    //工号
                    attendance.setJobnum(list.get(j).get(3).toString());
                    //mongodb
                    Query query = new Query();
                    query.addCriteria(Criteria.where("userinfo.jobnumber").is(list.get(j).get(3).toString()));
                    CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                    //姓名
                    if (customerInfo == null) {
                        //attendance.setNames(customerInfo.getUserinfo().getCustomername());
                    } else {
                        attendance.setNames(customerInfo.getUserid());
                    }
                    //上班1班次打卡时间
                    String working = list.get(j).get(8).toString();
                    attendance.setWorkinghours(working);
                    //下班1班次打卡时间
                    String off = list.get(j).get(10).toString();
                    attendance.setOffhours(off);
                    //日期
                    attendance.setAttendancetim(list.get(j).get(6).toString());
                    attendance.setAttendance_id(UUID.randomUUID().toString());
                    attendanceMapper.insert(attendance);
                }
            }
            return Result;
        } catch (Exception e) {
            throw new LogicalException(e.getMessage());
        }
    }

    //打卡记录导入企业微信
    @Override
    public List<Attendance> getAutoCheckInData(EWxBaseResponse data) throws Exception {

        try {

            //删除之前同步的信息
            Attendance attendancedel = new Attendance();
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            String strDateFormat = dateFormat.format(calendar.getTime());
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            String strDateFormat = dateFormat.format(date);
            attendanceMapper.getStatus(strDateFormat);
            attendanceMapper.getdel("1");

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            HashSet<String> set = new HashSet<>();
            for (EWxCheckData eWxCheckData :
                    data.getCheckindata()) {
                set.add(eWxCheckData.getUserid());
            }
            for (String val : set) {
                Attendance attendance = new Attendance();
                for (int k = 0; k < data.getCheckindata().size(); k++) {
                    if (val.equals(data.getCheckindata().get(k).getUserid())) {
                        if (!"上班打卡".equals(data.getCheckindata().get(k).getCheckin_type())) {
                            if ("未打卡".equals(data.getCheckindata().get(k).getException_type())) {
                                long endepoch = data.getCheckindata().get(k).getCheckin_time();
                                Date dateendepoch = new Date(endepoch * 1000);
                                String stroff = df.format(dateendepoch);
                                //下班打卡时间
                                String off = "";
                                String wo = stroff.substring(0, 11);
                                String[] weeks = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(dateendepoch);
                                int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
                                if (week_index < 0) {
                                    week_index = 0;
                                }
                                String xingqi = weeks[week_index];
                                wo = wo + "" + xingqi;
                                attendance.setAttendancetim(wo);
                                attendance.setOffhours(off);
                            }else {
                                long endepoch = data.getCheckindata().get(k).getCheckin_time();
                                Date dateendepoch = new Date(endepoch * 1000);
                                String stroff = df.format(dateendepoch);
                                //下班打卡时间
                                String off = stroff.substring(11);
                                String wo = stroff.substring(0, 11);
                                String[] weeks = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(dateendepoch);
                                int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
                                if (week_index < 0) {
                                    week_index = 0;
                                }
                                String xingqi = weeks[week_index];
                                wo = wo + "" + xingqi;
                                attendance.setAttendancetim(wo);
                                attendance.setOffhours(off);
                            }

                        }
                        if (!"下班打卡".equals(data.getCheckindata().get(k).getCheckin_type())) {
                            if ("未打卡".equals(data.getCheckindata().get(k).getException_type())) {
                                long strepoch = data.getCheckindata().get(k).getCheckin_time();
                                Date datestrepoch = new Date(strepoch * 1000);
                                String strworking = df.format(datestrepoch);
                                String[] weeks = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(datestrepoch);
                                int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
                                if (week_index < 0) {
                                    week_index = 0;
                                }
                                String xingqi = weeks[week_index];
                                //上班打卡时间
                                String working = "";
                                String wo = strworking.substring(0, 11);
                                wo = wo + "" + xingqi;
                                attendance.setAttendancetim(wo);
                                attendance.setWorkinghours(working);
                            }else{
                                long strepoch = data.getCheckindata().get(k).getCheckin_time();
                                Date datestrepoch = new Date(strepoch * 1000);
                                String strworking = df.format(datestrepoch);
                                String[] weeks = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(datestrepoch);
                                int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
                                if (week_index < 0) {
                                    week_index = 0;
                                }
                                String xingqi = weeks[week_index];
                                //上班打卡时间
                                String working = strworking.substring(11);
                                String wo = strworking.substring(0, 11);
                                wo = wo + "" + xingqi;
                                attendance.setAttendancetim(wo);
                                attendance.setWorkinghours(working);
                            }


                        }

                        //mongodb
                        Query query = new Query();
                        query.addCriteria(Criteria.where("userinfo.ewechatid").is(data.getCheckindata().get(k).getUserid()));
                        CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                        //姓名
                        if (customerInfo == null) {
                        } else {
                            attendance.setNames(customerInfo.getUserid());
                            attendance.setJobnum(customerInfo.getUserinfo().getJobnumber());
                            attendance.setStatus("0");
                        }
                    }
                }
                //打卡日期
                attendance.setAttendance_id(UUID.randomUUID().toString());
                attendanceMapper.insert(attendance);
            }

            return null;
        } catch (Exception e) {
            throw new LogicalException(e.getMessage());
        }
    }

    //打卡记录导入企业微信
    @Override
    public List<Attendance> getCheckInData(EWxBaseResponse data) throws Exception {

        try {

            //删除之前同步的信息
            Attendance attendancedel = new Attendance();
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String strDateFormat = dateFormat.format(date);
            attendanceMapper.getStatus(strDateFormat);
            attendanceMapper.getdel("1");

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            HashSet<String> set = new HashSet<>();
            for (EWxCheckData eWxCheckData :
                    data.getCheckindata()) {
                set.add(eWxCheckData.getUserid());
            }
            for (String val : set) {
                Attendance attendance = new Attendance();
                for (int k = 0; k < data.getCheckindata().size(); k++) {
                    if (val.equals(data.getCheckindata().get(k).getUserid())) {
                        if (!"上班打卡".equals(data.getCheckindata().get(k).getCheckin_type())) {
                            long endepoch = data.getCheckindata().get(k).getCheckin_time();
                            Date dateendepoch = new Date(endepoch * 1000);
                            String stroff = df.format(dateendepoch);
                            //下班打卡时间
                            String off = stroff.substring(11);
                            String wo = stroff.substring(0, 11);
                            String[] weeks = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(dateendepoch);
                            int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
                            if (week_index < 0) {
                                week_index = 0;
                            }
                            String xingqi = weeks[week_index];
                            wo = wo + "" + xingqi;
                            attendance.setAttendancetim(wo);
                            attendance.setOffhours(off);
                        }
                        if (!"下班打卡".equals(data.getCheckindata().get(k).getCheckin_type())) {
                            long strepoch = data.getCheckindata().get(k).getCheckin_time();
                            Date datestrepoch = new Date(strepoch * 1000);
                            String strworking = df.format(datestrepoch);
                            String[] weeks = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(datestrepoch);
                            int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
                            if (week_index < 0) {
                                week_index = 0;
                            }
                            String xingqi = weeks[week_index];
                            //上班打卡时间
                            String working = strworking.substring(11);
                            String wo = strworking.substring(0, 11);
                            wo = wo + "" + xingqi;
                            attendance.setAttendancetim(wo);
                            attendance.setWorkinghours(working);
                        }
                        //mongodb
                        Query query = new Query();
                        query.addCriteria(Criteria.where("userinfo.ewechatid").is(data.getCheckindata().get(k).getUserid()));
                        CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                        //姓名
                        if (customerInfo == null) {
                        } else {
                            attendance.setNames(customerInfo.getUserid());
                            attendance.setJobnum(customerInfo.getUserinfo().getJobnumber());
                            attendance.setStatus("0");
                        }
                    }
                }
                //打卡日期
                attendance.setAttendance_id(UUID.randomUUID().toString());
                attendanceMapper.insert(attendance);
            }

            return null;
        } catch (Exception e) {
            throw new LogicalException(e.getMessage());
        }
    }

    @Override
    public List<Attendance> getByUserId(String userId) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM");
        Date now = new Date();
        String nowMons = sdf.format(now);
        Calendar date = Calendar.getInstance();
        date.setTime(now);
        date.set(Calendar.MONTH, date.get(Calendar.MONTH) - 1);
        String lastMons = sdf.format(date.getTime());
        return attendanceMapper.getByUserId(userId, nowMons, lastMons);
    }


    //自动获取企业微信打卡记录
    @Scheduled(cron = "0 0 1 * * ?")
    public ApiResult getautocheckindata() throws Exception {

        try {
            String corpid = "ww52676ba41e44fb89";
            String corpSecret = "CP6colO-9OR5HRGZt7IAEzN5xYQH09H7yiuNvMextNE";
            EWeixinOauth2Token eweixinOauth = EWxUserApi.getWeChatOauth2Token(corpid, corpSecret);
            if (eweixinOauth != null && eweixinOauth.getErrcode() == 0) {
                String token = eweixinOauth.getAccess_token();
                String access_token = ewechatService.ewxLogin(token);
                List<CustomerInfo> userIdList = ewechatService.useridList();
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, -1);
                String strDateFormat = dateFormat.format(calendar.getTime());
                String startStrDateFormat = strDateFormat + " " + "0:10:00";
                String endStrDateFormat = strDateFormat + " " + "23:59:59";
                Date startDatDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startStrDateFormat);
                Date endDatDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endStrDateFormat);
                long startTime = startDatDateFormat.getTime() / 1000;
                long endTime = endDatDateFormat.getTime() / 1000;
                String[] strList = new String[userIdList.size()];
                for (int i = 0; i < userIdList.size(); i++) {
                    strList[i] = userIdList.get(i).getUserinfo().getEwechatid();
                }
                EWxBaseResponse jsob = new EWxBaseResponse();
                jsob = EWxUserApi.inData(access_token, 1, startTime, endTime, strList);
                attendanceService.getAutoCheckInData(jsob);
                return ApiResult.success("成功");
            } else {
                return ApiResult.fail("获取企业微信access_token失败");
            }
        } catch (LogicalException e) {
            return ApiResult.fail(e.getMessage());
        }
    }
}
