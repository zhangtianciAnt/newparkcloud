package com.nt.service_pfans.PFANS2000.Impl;

import com.mysql.jdbc.StringUtils;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Pfans.PFANS2000.AbNormal;
import com.nt.dao_Pfans.PFANS2000.Attendance;
import com.nt.dao_Pfans.PFANS2000.Overtime;
import com.nt.dao_Pfans.PFANS2000.Vo.AttendanceReport;
import com.nt.dao_Pfans.PFANS2000.Vo.AttendanceVo;
import com.nt.dao_Workflow.Workflowinstance;
import com.nt.service_WorkFlow.mapper.WorkflowinstanceMapper;
import com.nt.service_WorkFlow.mapper.WorkflownodeinstanceMapper;
import com.nt.service_pfans.PFANS2000.AttendanceService;
import com.nt.service_pfans.PFANS2000.PunchcardRecordService;
import com.nt.service_pfans.PFANS2000.mapper.AbNormalMapper;
import com.nt.service_pfans.PFANS2000.mapper.AttendanceMapper;
import com.nt.service_pfans.PFANS2000.mapper.OvertimeMapper;
import com.nt.service_pfans.PFANS2000.mapper.PunchcardRecordMapper;
import com.nt.utils.dao.TokenModel;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
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
    private MongoTemplate mongoTemplate;

    @Autowired
    private PunchcardRecordMapper punchcardRecordMapper;

    @Override
    public List<Attendance> getlist(Attendance attendance) throws Exception {
        return attendanceMapper.getAttendance(attendance);
    }

    //日志使用
    @Override
    public List<Attendance> getAttendancelist(Attendance attendance) throws Exception {
        //add-ws-5/6-根据当前月份和当前月的上个月获取数据
        String userid = attendance.getUser_id();
        String accYear = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
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
        String regdate = attendance.getYears() + "-" + attendance.getMonths() + "-01";
        Calendar calstart = Calendar.getInstance();
        calstart.setTime(sfymd.parse(regdate));
        //离职月末日
        Calendar calend = Calendar.getInstance();
        calend.clear();
        calend.setTime(sfymd.parse(regdate));
        int maxCurrentMonthDay = calend.getActualMaximum(Calendar.DAY_OF_MONTH);
        calend.set(Calendar.DAY_OF_MONTH, maxCurrentMonthDay);

        for (Calendar item = calstart; item.compareTo(calend) <= 0; item.add(Calendar.DAY_OF_MONTH, 1)) {
            punchcardRecordService.methodAttendance_b(item, attendance.getUser_id());
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

    //    //考勤导出 1125 ztc fr
    public void exportReported(String status, String year, String month, HttpServletRequest request, HttpServletResponse resp) throws Exception {
        List<AttendanceReport> attendanceReports = attendanceMapper.getAttInfo(year, month);
        XSSFWorkbook attract = new XSSFWorkbook();//内存中创建Excle
        XSSFCellStyle cellStyleAll = attract.createCellStyle();
        cellStyleAll.setTopBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        cellStyleAll.setBottomBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        cellStyleAll.setLeftBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        cellStyleAll.setRightBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        XSSFCellStyle cellStyleFir = attract.createCellStyle();
        cellStyleFir.setTopBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        cellStyleFir.setBottomBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        cellStyleFir.setLeftBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        cellStyleFir.setRightBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        cellStyleFir.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREY_25_PERCENT.getIndex());

        //  region  update  ml  220105  考勤导出   from
        XSSFSheet sheet = attract.createSheet(year + "-" + month + "月考勤");
        sheet.createFreezePane(0, 1, 0, 1);//冻结首行
        XSSFRow row = sheet.createRow(0);//创建行
        for (int k = 0; k < 19; k++) {
            XSSFCell cell = row.createCell(k);
            switch (k) {
                case 0: cell.setCellValue("人名");cell.setCellStyle(cellStyleFir);break;
                case 1: cell.setCellValue("センター");cell.setCellStyle(cellStyleFir);break;
                case 2: cell.setCellValue("グループ");cell.setCellStyle(cellStyleFir);break;
                case 3: cell.setCellValue("日期");cell.setCellStyle(cellStyleFir);break;
                case 4: cell.setCellValue("正常");cell.setCellStyle(cellStyleFir);break;
                case 5: cell.setCellValue("平日");cell.setCellStyle(cellStyleFir);break;
                case 6: cell.setCellValue("休日");cell.setCellStyle(cellStyleFir);break;
                case 7: cell.setCellValue("祝日");cell.setCellStyle(cellStyleFir);break;
                case 8: cell.setCellValue("特别休日");cell.setCellStyle(cellStyleFir);break;
                case 9: cell.setCellValue("青年节");cell.setCellStyle(cellStyleFir);break;
                case 10: cell.setCellValue("妇女节");cell.setCellStyle(cellStyleFir);break;
                case 11: cell.setCellValue("年休");cell.setCellStyle(cellStyleFir);break;
                case 12: cell.setCellValue("代休");cell.setCellStyle(cellStyleFir);break;
                case 13: cell.setCellValue("其他福利休假");cell.setCellStyle(cellStyleFir);break;
                case 14: cell.setCellValue("短病假");cell.setCellStyle(cellStyleFir);break;
                case 15: cell.setCellValue("长病假");cell.setCellStyle(cellStyleFir);break;
                case 16: cell.setCellValue("事假");cell.setCellStyle(cellStyleFir);break;
                case 17: cell.setCellValue("产休/护理假");cell.setCellStyle(cellStyleFir);break;
                case 18: cell.setCellValue("欠勤");cell.setCellStyle(cellStyleFir);break;
            }
        }

        List<AttendanceReport> onattReports = new ArrayList<>();
        List<AttendanceReport> outattReports = new ArrayList<>();
        if (attendanceReports != null && attendanceReports.size() > 0) {
            for (int i = 0; i < attendanceReports.size(); i++) {
                Query queryInfo = new Query();
                queryInfo.addCriteria(Criteria.where("userid").is(attendanceReports.get(i).getUser_id()));
                CustomerInfo customerInfo = mongoTemplate.findOne(queryInfo, CustomerInfo.class);
                if (customerInfo != null) {
                    if (!StringUtils.isNullOrEmpty(customerInfo.getUserinfo().getResignation_date())) {
                        String months = customerInfo.getUserinfo().getResignation_date().substring(5, 7);
                        if (months.equals(month)) {
                            //离职
                            outattReports.add(attendanceReports.get(i));
                        } else {
                            //在职
                            onattReports.add(attendanceReports.get(i));
                        }
                    } else {
                        //在职
                        onattReports.add(attendanceReports.get(i));
                    }
                }
            }
        }
        //在职
        if ("1".equals(status)) {
            if (onattReports != null && onattReports.size() > 0) {
                for (int i = 0; i < onattReports.size(); i++) {
                    XSSFRow rowDetail = sheet.createRow(i + 1);
                    AttendanceReport att = onattReports.get(i);
                    for (int j = 0; j < 19; j++) {
                        XSSFCell cellDetail = rowDetail.createCell(j);
                        switch (j) {
                            case 0: cellDetail.setCellValue(att.getUser_name());cellDetail.setCellStyle(cellStyleAll);break; // 人名
                            case 1: cellDetail.setCellValue(att.getCenter_id());cellDetail.setCellStyle(cellStyleAll);break; // Center
                            case 2: cellDetail.setCellValue(att.getGroup_id());cellDetail.setCellStyle(cellStyleAll);break; // Group
                            case 3: cellDetail.setCellValue(att.getDates());cellDetail.setCellStyle(cellStyleAll);break; // 日期
                            case 4: cellDetail.setCellValue("0".equals(att.getNormal()) ? null : att.getNormal());cellDetail.setCellStyle(cellStyleAll);break; // 正常
                            case 5: cellDetail.setCellValue("0".equals(att.getOrdinaryindustry()) ? null : att.getOrdinaryindustry());cellDetail.setCellStyle(cellStyleAll);break; // 平日
                            case 6: cellDetail.setCellValue("0".equals(att.getWeekendindustry()) ? null : att.getWeekendindustry());cellDetail.setCellStyle(cellStyleAll);break; // 休日
                            case 7: cellDetail.setCellValue("0".equals(att.getStatutoryresidue()) ? null : att.getShortsickleave());cellDetail.setCellStyle(cellStyleAll);break; // 祝日
                            case 8: cellDetail.setCellValue("0".equals(att.getSpecialday()) ? null : att.getSpecialday());cellDetail.setCellStyle(cellStyleAll);break; // 特别休日
                            case 9: cellDetail.setCellValue("0".equals(att.getYouthday()) ? null : att.getYouthday());cellDetail.setCellStyle(cellStyleAll);break; // 青年节
                            case 10: cellDetail.setCellValue("0".equals(att.getWomensday()) ? null : att.getWomensday());cellDetail.setCellStyle(cellStyleAll);break; // 妇女节
                            case 11: cellDetail.setCellValue("0".equals(att.getAnnualrest()) ? null : att.getAnnualrest());cellDetail.setCellStyle(cellStyleAll);break; // 年休
                            case 12: cellDetail.setCellValue("0".equals(att.getDaixiu()) ? null : att.getDaixiu());cellDetail.setCellStyle(cellStyleAll);break; // 代休
                            case 13: cellDetail.setCellValue("0".equals(att.getWelfare()) ? null : att.getWelfare());cellDetail.setCellStyle(cellStyleAll);break; // 其他福利休假
                            case 14: cellDetail.setCellValue("0".equals(att.getShortsickleave()) ? "0".equals(att.getTshortsickleave()) ? null : att.getTshortsickleave() : att.getShortsickleave());cellDetail.setCellStyle(cellStyleAll);break; // 短病假
                            case 15: cellDetail.setCellValue("0".equals(att.getLongsickleave()) ? "0".equals(att.getTlongsickleave()) ? null : att.getTlongsickleave() : att.getLongsickleave());cellDetail.setCellStyle(cellStyleAll);break; // 长病假
                            case 16: cellDetail.setCellValue("0".equals(att.getCompassionateleave()) ? "0".equals(att.getTcompassionateleave()) ? null : att.getTcompassionateleave() : att.getCompassionateleave());cellDetail.setCellStyle(cellStyleAll);break; // 事假
                            case 17: cellDetail.setCellValue("0".equals(att.getNursingleave()) ? null : att.getNursingleave());cellDetail.setCellStyle(cellStyleAll);break; // 产休/护理假
                            case 18: cellDetail.setCellValue("0".equals(att.getAbsenteeism()) ? "0".equals(att.getTabsenteeism()) ? null : att.getTabsenteeism() : att.getAbsenteeism());cellDetail.setCellStyle(cellStyleAll);break; // 欠勤
                        }
                    }
                }
            }
        }
        //离职
        if ("2".equals(status)) {
            if (outattReports != null && outattReports.size() > 0) {
                for (int i = 0; i < outattReports.size(); i++) {
                    XSSFRow rowDetail = sheet.createRow(i + 1);
                    AttendanceReport att = outattReports.get(i);
                    for (int j = 0; j < 19; j++) {
                        XSSFCell cellDetail = rowDetail.createCell(j);
                        switch (j) {
                            case 0: cellDetail.setCellValue(att.getUser_name());cellDetail.setCellStyle(cellStyleAll);break; // 人名
                            case 1: cellDetail.setCellValue(att.getCenter_id());cellDetail.setCellStyle(cellStyleAll);break; // Center
                            case 2: cellDetail.setCellValue(att.getGroup_id());cellDetail.setCellStyle(cellStyleAll);break; // Group
                            case 3: cellDetail.setCellValue(att.getDates());cellDetail.setCellStyle(cellStyleAll);break; // 日期
                            case 4: cellDetail.setCellValue("0".equals(att.getNormal()) ? null : att.getNormal());cellDetail.setCellStyle(cellStyleAll);break; // 正常
                            case 5: cellDetail.setCellValue("0".equals(att.getOrdinaryindustry()) ? null : att.getOrdinaryindustry());cellDetail.setCellStyle(cellStyleAll);break; // 平日
                            case 6: cellDetail.setCellValue("0".equals(att.getWeekendindustry()) ? null : att.getWeekendindustry());cellDetail.setCellStyle(cellStyleAll);break; // 休日
                            case 7: cellDetail.setCellValue("0".equals(att.getStatutoryresidue()) ? null : att.getShortsickleave());cellDetail.setCellStyle(cellStyleAll);break; // 祝日
                            case 8: cellDetail.setCellValue("0".equals(att.getSpecialday()) ? null : att.getSpecialday());cellDetail.setCellStyle(cellStyleAll);break; // 特别休日
                            case 9: cellDetail.setCellValue("0".equals(att.getYouthday()) ? null : att.getYouthday());cellDetail.setCellStyle(cellStyleAll);break; // 青年节
                            case 10: cellDetail.setCellValue("0".equals(att.getWomensday()) ? null : att.getWomensday());cellDetail.setCellStyle(cellStyleAll);break; // 妇女节
                            case 11: cellDetail.setCellValue("0".equals(att.getAnnualrest()) ? null : att.getAnnualrest());cellDetail.setCellStyle(cellStyleAll);break; // 年休
                            case 12: cellDetail.setCellValue("0".equals(att.getDaixiu()) ? null : att.getDaixiu());cellDetail.setCellStyle(cellStyleAll);break; // 代休
                            case 13: cellDetail.setCellValue("0".equals(att.getWelfare()) ? null : att.getWelfare());cellDetail.setCellStyle(cellStyleAll);break; // 其他福利休假
                            case 14: cellDetail.setCellValue("0".equals(att.getShortsickleave()) ? "0".equals(att.getTshortsickleave()) ? null : att.getTshortsickleave() : att.getShortsickleave());cellDetail.setCellStyle(cellStyleAll);break; // 短病假
                            case 15: cellDetail.setCellValue("0".equals(att.getLongsickleave()) ? "0".equals(att.getTlongsickleave()) ? null : att.getTlongsickleave() : att.getLongsickleave());cellDetail.setCellStyle(cellStyleAll);break; // 长病假
                            case 16: cellDetail.setCellValue("0".equals(att.getCompassionateleave()) ? "0".equals(att.getTcompassionateleave()) ? null : att.getTcompassionateleave() : att.getCompassionateleave());cellDetail.setCellStyle(cellStyleAll);break; // 事假
                            case 17: cellDetail.setCellValue("0".equals(att.getNursingleave()) ? null : att.getNursingleave());cellDetail.setCellStyle(cellStyleAll);break; // 产休/护理假
                            case 18: cellDetail.setCellValue("0".equals(att.getAbsenteeism()) ? "0".equals(att.getTabsenteeism()) ? null : att.getTabsenteeism() : att.getAbsenteeism());cellDetail.setCellStyle(cellStyleAll);break; // 欠勤
                        }
                    }
                }
            }
        }

        //  endregion  update  ml  220105  考勤导出   to
        OutputStream os = resp.getOutputStream();// 取得输出流
        String fileName = year + month + "月考勤信息";
        resp.setContentType("application/x-download");//下面三行是关键代码，处理乱码问题
        resp.setCharacterEncoding("utf-8");
        resp.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("gbk"), "iso8859-1") + ".xls");
        attract.write(os);
        attract.close();
    }
    //考勤导出 1125 ztc to
}
