package com.nt.service_pfans.PFANS2000.Impl;

import com.nt.dao_Pfans.PFANS2000.AbNormal;
import com.nt.dao_Pfans.PFANS2000.Attendance;
import com.nt.dao_Pfans.PFANS2000.Overtime;
import com.nt.dao_Pfans.PFANS2000.Vo.AttendanceVo;
import com.nt.dao_Workflow.Workflowinstance;
import com.nt.service_WorkFlow.WorkflowServices;
import com.nt.service_WorkFlow.mapper.WorkflowinstanceMapper;
import com.nt.service_WorkFlow.mapper.WorkflownodeinstanceMapper;
import com.nt.service_pfans.PFANS2000.AttendanceService;
import com.nt.service_pfans.PFANS2000.PunchcardRecordService;
import com.nt.service_pfans.PFANS2000.mapper.AbNormalMapper;
import com.nt.service_pfans.PFANS2000.mapper.AttendanceMapper;
import com.nt.service_pfans.PFANS2000.mapper.OvertimeMapper;
import com.nt.utils.StringUtils;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    private AttendanceMapper attendanceMapper;

    @Autowired
    private WorkflowinstanceMapper workflowinstanceMapper;

    @Autowired
    private WorkflownodeinstanceMapper workflownodeinstanceMapper;

    @Autowired
    private PunchcardRecordService punchcardRecordService;

    @Autowired
    private AbNormalMapper abNormalMapper;

    @Autowired
    private OvertimeMapper overtimeMapper;

    @Autowired
    private WorkflowServices workflowServices;

    @Override
    public List<Attendance> getlist(Attendance attendance) throws Exception {
        //分页功能 本功能移至后台 1125 ztc fr
        List<Workflowinstance> workList = workflowServices.allWorkFlowIns("/PFANS2010View");
        List<Attendance> attendances = attendanceMapper.getAttendance(attendance);
        attendances.forEach(att ->{
            String dataid = att.getUser_id() + "," + att.getYears() + "," + att.getMonths();
            List<Workflowinstance> wfList = workList.stream()
                    .filter(wf -> wf.getDataid().equals(dataid)).collect(Collectors.toList());
            if(wfList.size() > 0){
                att.setStatus("4");
            }else{
                att.setStatus("0");
            }
        });
        return attendances;
        //分页功能 本功能移至后台 1125 ztc to
    }

    //日志使用
    @Override
    public List<Attendance> getAttendancelist(Attendance attendance) throws Exception {
        //add-ws-5/6-根据当前月份和当前月的上个月获取数据
        String userid = attendance.getUser_id();
        String accYear = "";
        SimpleDateFormat format = new SimpleDateFormat("YYYY");
        SimpleDateFormat sf1 = new SimpleDateFormat("MM");
        String strTemp = sf1.format(new Date());     //当前月
        String strYear = format.format(new Date());     //当前年
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date); // 设置为当前时间
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1); // 设置为上一个月
        date = calendar.getTime();
        String accDate = sf1.format(date);          //上个月
        if (strTemp.equals("01")) {
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.YEAR, -1);
            Date y = c.getTime();
            accYear = format.format(y);              //上一年
        } else {
            accYear = format.format(new Date());     //上一年
        }
        List<Attendance> list = attendanceMapper.selectDataList(strTemp, strYear, accDate, accYear, userid);
        return list;
        //add-ws-5/6-根据当前月份和当前月的上个月获取数据
    }

    //考勤使用
    @Override
    public List<Attendance> getAttendancelist1(Attendance attendance) throws Exception {
        List<Attendance> attendancelist = attendanceMapper.select(attendance);
        for (Attendance atten : attendancelist) {
            if (atten.getAbsenteeism() != null && atten.getAbsenteeism() != "") {
                atten = selectAbnomaling(atten);
            } else if (atten.getTabsenteeism() != null && atten.getTabsenteeism() != "") {
                atten = selectAbnomaling(atten);
            }
            //判断前台查看按钮可用。临时存放到正式迟到字段
            List<AbNormal> abList = getabnormalByuseridandDate(atten);
            if (abList.size() > 0) {
                //判断当天所有申请的异常审批状态  暂存到早退字段中
                List<AbNormal> abList1 = new ArrayList<AbNormal>();
                abList1 = abList.stream().filter(item -> (item.getStatus().equals("2") || item.getStatus().equals("5"))).collect(Collectors.toList());
                if (abList1.size() > 0) {
                    //进行中
                    atten.setLeaveearly("0");
                } else {
                    abList1 = abList.stream().filter(item -> (item.getStatus().equals("0"))).collect(Collectors.toList());
                    if (abList1.size() > 0) {
                        //未发起
                        atten.setLeaveearly("1");
                    } else {
                        abList1 = abList.stream().filter(item -> (item.getStatus().equals("3") || item.getStatus().equals("6"))).collect(Collectors.toList());
                        if (abList1.size() > 0) {
                            //驳回
                            atten.setLeaveearly("2");
                        } else {
                            abList1 = abList.stream().filter(item -> (item.getStatus().equals("4") || item.getStatus().equals("7"))).collect(Collectors.toList());
                            if (abList1.size() > 0) {
                                //已完成
                                atten.setLeaveearly("3");
                            }
                        }
                    }
                }
                atten.setLate("can");
            } else {
                atten.setLate("nocan");
            }

            //判断当天所有的加班申请的审批状态，临时存放到
            Overtime o = new Overtime();
            o.setReserveovertimedate(atten.getDates());
            o.setUserid(atten.getUser_id());
            List<Overtime> ovList = overtimeMapper.select(o);
            ovList = ovList.stream().filter(item -> (!item.getStatus().equals("1"))).collect(Collectors.toList());
            if (ovList.size() > 0) {
                //判断当天所有申请的加班审批状态  暂存到试用早退字段中
                List<Overtime> ovList1 = new ArrayList<Overtime>();
                ovList1 = ovList.stream().filter(item -> (item.getStatus().equals("2") || item.getStatus().equals("5"))).collect(Collectors.toList());
                if (ovList1.size() > 0) {
                    //进行中
                    atten.setTleaveearly("0");
                } else {
                    ovList1 = ovList.stream().filter(item -> (item.getStatus().equals("0"))).collect(Collectors.toList());
                    if (ovList1.size() > 0) {
                        //未发起
                        atten.setTleaveearly("1");
                    } else {
                        ovList1 = ovList.stream().filter(item -> (item.getStatus().equals("3") || item.getStatus().equals("6"))).collect(Collectors.toList());
                        if (ovList1.size() > 0) {
                            //驳回
                            atten.setTleaveearly("2");
                        } else {
                            ovList1 = ovList.stream().filter(item -> (item.getStatus().equals("4") || item.getStatus().equals("7"))).collect(Collectors.toList());
                            if (ovList1.size() > 0) {
                                //已完成
                                atten.setTleaveearly("3");
                            }
                        }
                    }
                }
            }
        }
        return attendancelist;
    }

    //获取离职考勤对比
    @Override
    public List<Attendance> getAttendancelistCompared(Attendance attendance) throws Exception {

        List<Attendance> attendancelist = attendanceMapper.selectResignationAll(attendance.getUser_id(), attendance.getYears(), attendance.getMonths());
        return attendancelist;
    }

    @Override
    public void disclickUpdateStates(Attendance attendance, TokenModel tokenModel) throws Exception {
        Workflowinstance ws = new Workflowinstance();
        ws.setDataid(attendance.getUser_id() + "," + attendance.getYears() + "," + attendance.getMonths());
        ws.setStatus("4");
        List<Workflowinstance> wslist = workflowinstanceMapper.select(ws);
        if (wslist.size() > 0) {
            wslist.get(0).setStatus("2");
            wslist.get(0).setModifyby(tokenModel.getUserId());
            wslist.get(0).setModifyon(new Date());
            workflowinstanceMapper.updateByPrimaryKey(wslist.get(0));
        }
        //变成未承认
        attendance.preUpdate(tokenModel);
        attendanceMapper.updStatus1(attendance.getUser_id(), attendance.getYears(), attendance.getMonths());

        //add ccm 20210721 离职考勤一键驳回预计考勤按照实际打卡重新计算 fr
        //离职月初日
        SimpleDateFormat sfymd = new SimpleDateFormat("yyyy-MM-dd");
        String regdate = attendance.getYears()+"-"+attendance.getMonths()+"-01";
        Calendar calstart = Calendar.getInstance();
        calstart.setTime(sfymd.parse(regdate));
        //离职月末日
        Calendar calend = Calendar.getInstance();
        calend.clear();
        calend.setTime(sfymd.parse(regdate));
        int maxCurrentMonthDay = calend.getActualMaximum(Calendar.DAY_OF_MONTH);
        calend.set(Calendar.DAY_OF_MONTH, maxCurrentMonthDay);

        for(Calendar item = calstart;item.compareTo(calend) <= 0;item.add(Calendar.DAY_OF_MONTH,1))
        {
            punchcardRecordService.methodAttendance_b(item,attendance.getUser_id());
        }
        //add ccm 20210721 离职考勤一键驳回预计考勤按照实际打卡重新计算 to

    }


    @Override
    public void update(AttendanceVo attendancevo, TokenModel tokenModel) throws Exception {
        List<Attendance> attendancelist = attendancevo.getAttendance();
        attendanceMapper.updStatusre(tokenModel.getUserId(), attendancelist);
    }

    //add_fjl_05/13   --添加审批正常结束后，自动变成承认状态
    @Override
    public void updStatus(Attendance attendance, TokenModel tokenModel) throws Exception {
        attendance.preUpdate(tokenModel);
        attendanceMapper.updStatus(attendance.getUser_id(), attendance.getYears(), attendance.getMonths());
//        Attendance att = new Attendance();
//        if (attendance.getStatus().equals("4")) {
//            att.setUser_id(attendance.getUser_id());
//            att.setYears(attendance.getYears());
//            att.setMonths(attendance.getMonths());
//            List<Attendance> attList = attendanceMapper.select(att);
//            if (attList.size() > 0) {
//                for (Attendance at : attList) {
//                    at.setRecognitionstate("1");
//                    at.preUpdate(tokenModel);
//                    attendanceMapper.updateByPrimaryKeySelective(at);
//                }
//            }
//
//        }
    }
    //add_fjl_05/13   --添加审批正常结束后，自动变成承认状态

    @Override
    public void updStatus1(Attendance attendance, TokenModel tokenModel) throws Exception {
        attendance.preUpdate(tokenModel);
        attendanceMapper.updStatus1(attendance.getUser_id(), attendance.getYears(), attendance.getMonths());
    }

    //add ccm 2020729 考勤异常加班审批中的日期，考勤不允许承认
    @Override
    public List<Date> selectAbnomalandOvertime(AttendanceVo attendancevo) throws Exception {
        List<Date> returnDateList = new ArrayList<Date>();
        SimpleDateFormat sfymd = new SimpleDateFormat("yyyy-MM-dd");
        //本月选择承认的数据
        List<Attendance> attendancelist = attendancevo.getAttendance();

        //检索考勤未审批完成的数据
        List<AbNormal> abList = new ArrayList<AbNormal>();
        abList = abNormalMapper.selectAbnomalBystatusandUserid(attendancelist.get(0).getUser_id());
        //检索加班未完成的数据
        List<Overtime> oList = new ArrayList<Overtime>();
        oList = overtimeMapper.selectOvertimeBystatusandUserid(attendancelist.get(0).getUser_id());
        oList = oList.stream().filter(item -> (!item.getStatus().equals("1"))).collect(Collectors.toList());
        for (Attendance attendance : attendancelist) {
            //判断考勤未审批的数据
            for (AbNormal ab : abList) {
                if (!returnDateList.contains(attendance.getDates())) {
                    if (ab.getStatus().equals("2") && sfymd.parse(sfymd.format(attendance.getDates())).getTime() >= sfymd.parse(sfymd.format(ab.getOccurrencedate())).getTime() && sfymd.parse(sfymd.format(attendance.getDates())).getTime() <= sfymd.parse(sfymd.format(ab.getFinisheddate())).getTime()) {
                        returnDateList.add(attendance.getDates());
                    } else if (ab.getStatus().equals("5") && sfymd.parse(sfymd.format(attendance.getDates())).getTime() >= sfymd.parse(sfymd.format(ab.getReoccurrencedate())).getTime() && sfymd.parse(sfymd.format(attendance.getDates())).getTime() <= sfymd.parse(sfymd.format(ab.getRefinisheddate())).getTime()) {
                        returnDateList.add(attendance.getDates());
                    }
                }
            }

            //判断加班未审批通过的数据
            List<Overtime> ov = oList.stream().filter(item -> sfymd.format(attendance.getDates()).equals(sfymd.format(item.getReserveovertimedate()))).collect(Collectors.toList());
            if (ov.size() > 0 && !returnDateList.contains(attendance.getDates())) {
                returnDateList.add(attendance.getDates());
            }
        }
        return returnDateList;
    }
    //add ccm 2020729 考勤异常加班审批中的日期，考勤不允许承认

    //add ccm 0804 查询欠勤是否已经全部申请
    public Attendance selectAbnomaling(Attendance attendance) throws Exception {
        SimpleDateFormat sfymd = new SimpleDateFormat("yyyy-MM-dd");
        List<AbNormal> abList = new ArrayList<AbNormal>();
        abList = abNormalMapper.selectAbnomalBystatusandUserid(attendance.getUser_id());
        if (abList.size() > 0) {
            Double hours = 0D;
            for (AbNormal a : abList) {
                if (a.getStatus().equals("2")) {
                    if (sfymd.parse(sfymd.format(attendance.getDates())).getTime() >= sfymd.parse(sfymd.format(a.getOccurrencedate())).getTime() && sfymd.parse(sfymd.format(attendance.getDates())).getTime() <= sfymd.parse(sfymd.format(a.getFinisheddate())).getTime()) {
                        hours += Double.valueOf(a.getLengthtime());
                    } else {
                        hours += 0;
                    }
                } else {
                    if (sfymd.parse(sfymd.format(attendance.getDates())).getTime() >= sfymd.parse(sfymd.format(a.getReoccurrencedate())).getTime() && sfymd.parse(sfymd.format(attendance.getDates())).getTime() <= sfymd.parse(sfymd.format(a.getRefinisheddate())).getTime()) {
                        hours += Double.valueOf(a.getRelengthtime());
                    } else {
                        hours += 0;
                    }
                }
            }
            attendance.setTenantid(String.valueOf(hours));
        }
        return attendance;
    }
    //add ccm 0804 查询欠勤是否已经全部申请

    @Override
    //add ccm 0812 考情管理查看当天的异常申请数据
    public List<AbNormal> getabnormalByuseridandDate(Attendance attendance) throws Exception {
        SimpleDateFormat sfymd = new SimpleDateFormat("yyyy-MM-dd");
        List<AbNormal> abNormals = new ArrayList<AbNormal>();
        String dates = sfymd.format(attendance.getDates());
        abNormals = abNormalMapper.getabnormalByuseridandDate(attendance.getUser_id(), dates);
        return abNormals;
    }
    //add ccm 0812 考情管理查看当天的异常申请数据
}
