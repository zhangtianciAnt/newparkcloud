package com.nt.service_pfans.PFANS2000.Impl;

import com.nt.dao_Pfans.PFANS2000.AbNormal;
import com.nt.dao_Pfans.PFANS2000.Attendance;
import com.nt.dao_Pfans.PFANS2000.Overtime;
import com.nt.dao_Pfans.PFANS2000.Vo.AttendanceVo;
import com.nt.dao_Workflow.Workflowinstance;
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

    @Override
    public List<Attendance> getlist(Attendance attendance) throws Exception {
        return attendanceMapper.getAttendance(attendance);
    }

    //日志使用
    @Override
    public List<Attendance> getAttendancelist(Attendance attendance) throws Exception {
        //add-ws-5/6-根据当前月份和当前月的上个月获取数据
        List<Attendance> attendancelist = attendanceMapper.select(attendance);
        SimpleDateFormat sf1 = new SimpleDateFormat("MM");
        String strTemp = sf1.format(new Date());
        SimpleDateFormat format = new SimpleDateFormat("MM");
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date); // 设置为当前时间
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1); // 设置为上一个月
        date = calendar.getTime();
        String accDate = format.format(date);
        attendancelist = attendancelist.stream()
                .filter(item -> (item.getMonths().equals(accDate) || item.getMonths().equals(strTemp)))
                .collect(Collectors.toList());
        //add-ws-5/6-根据当前月份和当前月的上个月获取数据
        return attendancelist;
    }

    //考勤使用
    @Override
    public List<Attendance> getAttendancelist1(Attendance attendance) throws Exception {
        List<Attendance> attendancelist = attendanceMapper.select(attendance);
        return attendancelist;
    }

    //获取离职考勤对比
    @Override
    public List<Attendance> getAttendancelistCompared(Attendance attendance) throws Exception {

        List<Attendance> attendancelist = attendanceMapper.selectResignationAll(attendance.getUser_id(),attendance.getYears(),attendance.getMonths());
        return attendancelist;
    }
    @Override
    public void disclickUpdateStates(Attendance attendance, TokenModel tokenModel) throws Exception {
        Workflowinstance ws = new Workflowinstance();
        ws.setDataid(attendance.getUser_id()+","+attendance.getYears()+","+attendance.getMonths());
        ws.setStatus("4");
        List<Workflowinstance> wslist = workflowinstanceMapper.select(ws);
        if(wslist.size()>0)
        {
            wslist.get(0).setStatus("2");
            wslist.get(0).setModifyby(tokenModel.getUserId());
            wslist.get(0).setModifyon(new Date());
            workflowinstanceMapper.updateByPrimaryKey(wslist.get(0));
        }
        //变成未承认
        attendance.preUpdate(tokenModel);
        attendanceMapper.updStatus1(attendance.getUser_id(), attendance.getYears(), attendance.getMonths());
    }


    @Override
    public void update(AttendanceVo attendancevo, TokenModel tokenModel) throws Exception {
        List<Attendance> attendancelist = attendancevo.getAttendance();
        attendanceMapper.updStatusre(tokenModel.getUserId(),attendancelist);
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

        for(Attendance attendance :attendancelist)
        {
            //判断考勤未审批的数据
            for(AbNormal ab:abList)
            {
                if(!returnDateList.contains(attendance.getDates()))
                {
                    if(ab.getStatus().equals("2") && sfymd.parse(sfymd.format(attendance.getDates())).getTime() >= sfymd.parse(sfymd.format(ab.getOccurrencedate())).getTime() && sfymd.parse(sfymd.format(attendance.getDates())).getTime() <= sfymd.parse(sfymd.format(ab.getFinisheddate())).getTime())
                    {
                        returnDateList.add(attendance.getDates());
                    }
                    else if(ab.getStatus().equals("5") && sfymd.parse(sfymd.format(attendance.getDates())).getTime() >= sfymd.parse(sfymd.format(ab.getReoccurrencedate())).getTime() && sfymd.parse(sfymd.format(attendance.getDates())).getTime() <= sfymd.parse(sfymd.format(ab.getRefinisheddate())).getTime())
                    {
                        returnDateList.add(attendance.getDates());
                    }
                }
            }

            //判断加班未审批通过的数据
            List<Overtime> ov = oList.stream().filter(item -> sfymd.format(attendance.getDates()).equals(sfymd.format(item.getReserveovertimedate()))).collect(Collectors.toList());
            if(ov.size()>0 && !returnDateList.contains(attendance.getDates()))
            {
                returnDateList.add(attendance.getDates());
            }
        }
        return returnDateList;
    }
    //add ccm 2020729 考勤异常加班审批中的日期，考勤不允许承认
}
