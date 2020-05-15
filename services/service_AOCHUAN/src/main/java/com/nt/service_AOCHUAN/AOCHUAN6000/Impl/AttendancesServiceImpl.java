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
import com.nt.service_Org.ToDoNoticeService;
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
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional(rollbackFor=Exception.class)
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

    //考勤一览初期值
    @Override
    public List<Attendance> get(Attendance attendance) throws Exception {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<Attendance> attendanceList = attendanceMapper.select(attendance);
        for(Attendance att : attendanceList){
            Vacation vacation = new Vacation();
            vacation.setApplicanterid(att.getNames());
            vacation.setStatus("4");

            List<Vacation> vacationList = vacationMapper.select(vacation);//休假
            for(Vacation vac : vacationList){
                String vacstart = simpleDateFormat.format(vac.getStartdate());
                Date datestart = simpleDateFormat.parse(vacstart); //请假开始时间

                String vacend = simpleDateFormat.format(vac.getEnddate());
                Date datestaend = simpleDateFormat.parse(vacend); //请假结束时间


                String attendancetim = "20" + att.getAttendancetim();
                Date attdate = simpleDateFormat.parse(attendancetim);//考勤时间

                int compareToKS = attdate.compareTo(datestart);
                int compareToJS = attdate.compareTo(datestaend);

                if(compareToKS == 1 && compareToJS == -1){
                    att.setLeaves("1");
                    break;
                }
                if(compareToKS == 0 && compareToJS == -1 && (vac.getStarttim().equals("1") || vac.getStarttim().equals("2") )){
                    att.setLeaves("1");
                    break;
                }
                if(compareToKS == 0 && compareToJS == -1 && (vac.getStarttim().equals("2"))){
                    att.setLeaves("0.5");
                    att.setTimtype("2");
                    break;
                }
                if(compareToKS == 1 && compareToJS == 0 && (vac.getEndtim().equals("1") || vac.getStarttim().equals("3"))){
                    att.setLeaves("1");
                    break;
                }
                if(compareToKS == 1 && compareToJS == 0 && (vac.getEndtim().equals("2"))){
                    att.setLeaves("0.5");
                    att.setTimtype("2");
                    break;
                }
                if(compareToKS == 1 && compareToJS == 1){
                    att.setLeaves("0");

                }
                if(compareToKS == -1 && compareToJS == -1){
                    att.setLeaves("0");

                }
                if(compareToKS == 0 && compareToJS == 0 && vac.getDatetype().equals("1")){
                    att.setLeaves("1");
                    break;
                }
                if(compareToKS == 0 && compareToJS == 0 && vac.getDatetype().equals("2")){
                    att.setLeaves("0.5");
//                    att.setTimtype("2");
                    break;
                }
                if(compareToKS == 0 && compareToJS == 0 && vac.getDatetype().equals("3")){
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
        List<Attendance> attendanceList = attendanceMapper.getYICHANG("星期一","星期二","星期三","星期四","星期五");
        for(Attendance att : attendanceList){
            Vacation vacation = new Vacation();
            vacation.setApplicanterid(att.getNames());
            vacation.setStatus("4");

            List<Vacation> vacationList = vacationMapper.select(vacation);//休假
            for(Vacation vac : vacationList){
                String vacstart = simpleDateFormat.format(vac.getStartdate());
                Date datestart = simpleDateFormat.parse(vacstart); //请假开始时间

                String vacend = simpleDateFormat.format(vac.getEnddate());
                Date datestaend = simpleDateFormat.parse(vacend); //请假结束时间


                String attendancetim = "20" + att.getAttendancetim();
                Date attdate = simpleDateFormat.parse(attendancetim);//考勤时间

                int compareToKS = attdate.compareTo(datestart);
                int compareToJS = attdate.compareTo(datestaend);

                if(compareToKS == 1 && compareToJS == -1){
                    att.setLeaves("1");
                    break;
                }
                if(compareToKS == 0 && compareToJS == -1 && (vac.getStarttim().equals("1") || vac.getStarttim().equals("2") )){
                    att.setLeaves("1");
                    break;
                }
                if(compareToKS == 0 && compareToJS == -1 && (vac.getStarttim().equals("2"))){
                    att.setLeaves("0.5");
                    att.setTimtype("2");
                    break;
                }
                if(compareToKS == 1 && compareToJS == 0 && (vac.getEndtim().equals("1") || vac.getStarttim().equals("3"))){
                    att.setLeaves("1");
                    break;
                }
                if(compareToKS == 1 && compareToJS == 0 && (vac.getEndtim().equals("2"))){
                    att.setLeaves("0.5");
                    att.setTimtype("2");
                    break;
                }
                if(compareToKS == 1 && compareToJS == 1){
                    att.setLeaves("0");

                }
                if(compareToKS == -1 && compareToJS == -1){
                    att.setLeaves("0");

                }
                if(compareToKS == 0 && compareToJS == 0 && vac.getDatetype().equals("1")){
                    att.setLeaves("1");
                    break;
                }
                if(compareToKS == 0 && compareToJS == 0 && vac.getDatetype().equals("2")){
                    att.setLeaves("0.5");
//                    att.setTimtype("2");
                    break;
                }
                if(compareToKS == 0 && compareToJS == 0 && vac.getDatetype().equals("3")){
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
        for(Attendance att : attendanceList){
            Vacation vacation = new Vacation();//异常申请表
            vacation.setApplicanterid(att.getNames());
            vacation.setStatus("4");

            List<Vacation> vacationList = vacationMapper.select(vacation);
            for(Vacation vac : vacationList){
                String vacstart = simpleDateFormat.format(vac.getStartdate());
                Date datestart = simpleDateFormat.parse(vacstart); //请假开始时间

                String vacend = simpleDateFormat.format(vac.getEnddate());
                Date datestaend = simpleDateFormat.parse(vacend); //请假结束时间


                String attendancetim = "20" + att.getAttendancetim();
                Date attdate = simpleDateFormat.parse(attendancetim);//考勤时间

                int compareToJS = attdate.compareTo(datestart);
                int compareToKS = attdate.compareTo(datestaend);

                if(compareToJS == 1 && compareToKS == -1){
                    att.setLeaves("1");
                    break;
                }
                if((compareToJS == 0 && compareToKS == -1) || (compareToJS == 1 && compareToKS == 0)){
                    att.setLeaves("1");
                    break;
                }
                if(compareToJS == 1 && compareToKS == 1){
                    att.setLeaves("0");
                    break;
                }
                if(compareToJS == 0 && compareToKS == 0 && vac.getDatetype().equals("1")){
                    att.setLeaves("1");
                    break;
                }
                if(compareToJS == 0 && compareToKS == 0 && vac.getDatetype().equals("2")){
                    att.setLeaves("0.5");
                    break;
                }
                if(compareToJS == 0 && compareToKS == 0 && vac.getDatetype().equals("3")){
                    att.setLeaves("0.5");
                    break;
                }


            }

        }

        return attendanceList;
    }
    //考勤一览根据月，年去查询
    @Override
    public List<Attendance> getNowMons(String attendancetim) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<Attendance> attendanceList = attendanceMapper.getNowMon(attendancetim);
        for(Attendance att : attendanceList){
            Vacation vacation = new Vacation();//异常申请表
            vacation.setApplicanterid(att.getNames());
            vacation.setStatus("4");

            List<Vacation> vacationList = vacationMapper.select(vacation);
            for(Vacation vac : vacationList){
                String vacstart = simpleDateFormat.format(vac.getStartdate());
                Date datestart = simpleDateFormat.parse(vacstart); //请假开始时间

                String vacend = simpleDateFormat.format(vac.getEnddate());
                Date datestaend = simpleDateFormat.parse(vacend); //请假结束时间


                String attendancetims = "20" + att.getAttendancetim();
                Date attdate = simpleDateFormat.parse(attendancetims);//考勤时间

                int compareToKS = attdate.compareTo(datestart);
                int compareToJS = attdate.compareTo(datestaend);

                if(compareToKS == 1 && compareToJS == -1){
                    att.setLeaves("1");
                    break;
                }
                if(compareToKS == 0 && compareToJS == -1 && (vac.getStarttim().equals("1") || vac.getStarttim().equals("2") )){
                    att.setLeaves("1");
                    break;
                }
                if(compareToKS == 0 && compareToJS == -1 && (vac.getStarttim().equals("2"))){
                    att.setLeaves("0.5");
                    att.setTimtype("2");
                    break;
                }
                if(compareToKS == 1 && compareToJS == 0 && (vac.getEndtim().equals("1") || vac.getStarttim().equals("3"))){
                    att.setLeaves("1");
                    break;
                }
                if(compareToKS == 1 && compareToJS == 0 && (vac.getEndtim().equals("2"))){
                    att.setLeaves("0.5");
                    att.setTimtype("2");
                    break;
                }
                if(compareToKS == 1 && compareToJS == 1){
                    att.setLeaves("0");

                }
                if(compareToKS == -1 && compareToJS == -1){
                    att.setLeaves("0");

                }
                if(compareToKS == 0 && compareToJS == 0 && vac.getDatetype().equals("1")){
                    att.setLeaves("1");
                    break;
                }
                if(compareToKS == 0 && compareToJS == 0 && vac.getDatetype().equals("2")){
                    att.setLeaves("0.5");
//                    att.setTimtype("2");
                    break;
                }
                if(compareToKS == 0 && compareToJS == 0 && vac.getDatetype().equals("3")){
                    att.setLeaves("0.5");
//                    att.setTimtype("3");
                    break;
                }


            }

        }
        return attendanceList;
    }
    //打卡记录导入
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
            ExcelReader reader = ExcelUtil.getReader("E:\\Excel\\奥川生物666_考勤报表_20200401-20200430.xlsx" , 1);

            List<List<Object>> list = reader.read();
            List<Object> model = new ArrayList<Object>();
            model.add("日期");
            model.add("姓名");
            model.add("部门");
            model.add("上班时间");
            model.add("下班时间");

            for(int j = 4 ; j <list.size() ; j ++){

                if (!"总计".equals(list.get(j).get(6).toString())){
                    Attendance attendance = new Attendance();
                    attendance.preInsert(tokenModel);
                    //工号
                    attendance.setJobnum(list.get(j).get(3).toString());
                    //mongodb
                    Query query = new Query();
                    query.addCriteria(Criteria.where("userinfo.jobnumber").is(list.get(j).get(3).toString()));
                    CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                    //姓名
                    if(customerInfo == null){
                        //attendance.setNames(customerInfo.getUserinfo().getCustomername());
                    }else{
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

    @Override
    public List<Attendance> getByUserId(String userId) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM");
        Date now = new Date();
        String nowMons = sdf.format(now);
        Calendar date=Calendar.getInstance();
        date.setTime(now);
        date.set(Calendar.MONTH, date.get(Calendar.MONTH)-1);
        String lastMons = sdf.format(date.getTime());
        return attendanceMapper.getByUserId(userId,nowMons,lastMons);
    }
}
