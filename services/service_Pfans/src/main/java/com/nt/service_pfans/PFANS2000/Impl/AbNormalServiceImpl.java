package com.nt.service_pfans.PFANS2000.Impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Pfans.PFANS2000.*;
import com.nt.dao_Pfans.PFANS2000.Vo.restViewVo;
import com.nt.dao_Pfans.PFANS8000.WorkingDay;
import com.nt.service_pfans.PFANS2000.AbNormalService;
import com.nt.service_pfans.PFANS2000.mapper.*;
import com.nt.service_pfans.PFANS8000.mapper.WorkingDayMapper;
import com.nt.utils.AuthConstants;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class AbNormalServiceImpl implements AbNormalService {

    @Autowired
    private AbNormalMapper abNormalMapper;
    @Autowired
    private AttendanceMapper attendanceMapper;
    @Autowired
    private AttendanceSettingMapper attendanceSettingMapper;
    @Autowired
    private ReplacerestMapper replacerestMapper;
    @Autowired
    private WorkingDayMapper workingDayMapper;
    @Autowired
    private AnnualLeaveMapper annualLeaveMapper;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<AbNormal> list(AbNormal abNormal) throws Exception {
        return abNormalMapper.select(abNormal);
    }

    @Override
    public List<AbNormal> selectAbNormalParent(String userid) throws Exception {
        return abNormalMapper.selectAbNormalParent(userid);
    }

    @Override
    public Double getSickleave(String userid) throws Exception {
        Double a =abNormalMapper.selectAbNormalDate(userid);
        if(a == null){
            a = 0.0;
        }
        return a;
    }

    @Override
    public void insert(AbNormal abNormal, TokenModel tokenModel) throws Exception {
        abNormal.preInsert(tokenModel);
        abNormal.setAbnormalid(UUID.randomUUID().toString());
        abNormalMapper.insert(abNormal);
    }

    @Override
    public void upd(AbNormal abNormal, TokenModel tokenModel) throws Exception {
        abNormal.preUpdate(tokenModel);
        if(abNormal.getStatus().equals(AuthConstants.APPROVED_FLAG_YES) || abNormal.getStatus().equals("7")){
            AbNormal ab = new AbNormal();
            ab.setAbnormalid(abNormal.getAbnormalid());
            List<AbNormal> abNormallist = abNormalMapper.select(ab);
            if(!(abNormallist.get(0).getStatus().equals(abNormal.getStatus()))) {
                if (abNormal.getErrortype().equals("PR013006") || abNormal.getErrortype().equals("PR013007")) {//代休周末
                    //修改代休记录
                    updateReplacerest(abNormal, tokenModel);
                }
                if (abNormal.getErrortype().equals("PR013005")) //年休
                {
                    //修改年休
                    //updateAnnualleave(abNormal,tokenModel);
                }
                //旷工基本计算单位
                String absenteeism = null;
                //工作时间
                String workinghours = null;
                //事假单位
                String compassionateleave = null;
                AttendanceSetting attendancesetting = new AttendanceSetting();
                List<AttendanceSetting> attendancesettinglist = attendanceSettingMapper.select(attendancesetting);
                if (attendancesettinglist.size() > 0) {

                    workinghours = attendancesettinglist.get(0).getWorkinghours();
                    absenteeism = attendancesettinglist.get(0).getLateearlyleave();
                    //事假基本计算单位
                    compassionateleave = attendancesettinglist.get(0).getCompassionateleave();
                }
                String dateOccurrence = null;
                SimpleDateFormat sf1ymd = new SimpleDateFormat("yyyy-MM-dd");
                Attendance attendance = new Attendance();
                attendance.setUser_id(abNormal.getUser_id());
                attendance.setRecognitionstate(AuthConstants.RECOGNITION_FLAG_NO);

                CustomerInfo customer =new CustomerInfo();
                Query query = new Query();
                String userid = abNormal.getUser_id();
                query.addCriteria(Criteria.where("userid").is(userid));
                CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                if(abNormal.getStatus().equals("7"))
                {
                    if(!abNormal.getReoccurrencedate().equals(abNormal.getRefinisheddate()))
                    {
                        List<Attendance> attendancelist = attendanceMapper.selectAttendance(abNormal);
                        DecimalFormat df = new DecimalFormat("######0.00");
                        if(attendancelist.size() > 0)
                        {
                            for (Attendance attend : attendancelist)
                            {
                                WorkingDay workDay = new WorkingDay();
                                workDay.setWorkingdate(attend.getDates());
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(attend.getDates());
                                List<WorkingDay> workingDaysList = workingDayMapper.select(workDay);

                                //判断当天是否是休日，青年节，妇女节，周六周日
                                if(workingDaysList.size()>0)
                                {
                                    //振替出勤日
                                    if(workingDaysList.get(0).getType().equals("4"))
                                    {
                                        workinghours = "8";
                                    }
                                    else
                                    {
                                        workinghours = "0";
                                    }
                                }
                                else if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
                                {
                                    workinghours = "0";
                                }
                                else if(sf1ymd.format(abNormal.getOccurrencedate()).equals(DateUtil.format(new Date(),"YYYY").toString() + "-03-08") || sf1ymd.format(abNormal.getOccurrencedate()).equals(DateUtil.format(new Date(),"YYYY").toString() + "-05-04"))
                                {
                                    workinghours = "4";
                                }
                                else
                                {
                                    workinghours = "8";
                                }

                                if (abNormal.getErrortype().equals("PR013001")) {//外出
                                    attend.setNormal(df.format(Double.valueOf(workinghours)));
                                    attend.setAbsenteeism(null);

                                } else if (abNormal.getErrortype().equals("PR013005")) {//年休
                                    attend.setAnnualrest(df.format(Double.valueOf(workinghours)));
                                    attend.setNormal(null);
                                    attend.setAbsenteeism(null);
                                } else if (abNormal.getErrortype().equals("PR013006")) {//代休周末
                                    attend.setDaixiu(df.format(Double.valueOf(workinghours)));
                                    attend.setNormal(null);
                                    attend.setAbsenteeism(null);
                                } else if (abNormal.getErrortype().equals("PR013007")) {//代休特殊
                                    attend.setDaixiu(df.format(Double.valueOf(workinghours)));
                                    attend.setNormal(null);
                                    attend.setAbsenteeism(null);
                                } else if (abNormal.getErrortype().equals("PR013008")) {//事休
                                    attend.setCompassionateleave(df.format(Double.valueOf(workinghours)));
                                    attend.setNormal(null);
                                    attend.setAbsenteeism(null);
                                } else if (abNormal.getErrortype().equals("PR013009")) {//短期病休
                                    attend.setShortsickleave(df.format(Double.valueOf(workinghours)));
                                    attend.setNormal(null);
                                    attend.setAbsenteeism(null);
                                } else if (abNormal.getErrortype().equals("PR013010")) {//長期病休
                                    attend.setLongsickleave(df.format(Double.valueOf(workinghours)));
                                    attend.setNormal(null);
                                    attend.setAbsenteeism(null);
                                } else if (abNormal.getErrortype().equals("PR013012") || abNormal.getErrortype().equals("PR013013")) { //産休（女） 护理假（男）
                                    attend.setNursingleave(df.format(Double.valueOf(workinghours)));
                                    attend.setNormal(null);
                                    attend.setAbsenteeism(null);
                                } else if (abNormal.getErrortype().equals("PR013014") || abNormal.getErrortype().equals("PR013016")
                                        || abNormal.getErrortype().equals("PR013018") || abNormal.getErrortype().equals("PR013019")
                                        || abNormal.getErrortype().equals("PR013011") || abNormal.getErrortype().equals("PR013015")
                                        || abNormal.getErrortype().equals("PR013004") || abNormal.getErrortype().equals("PR013017")
                                        || abNormal.getErrortype().equals("PR013020")) {
                                    attend.setWelfare(df.format(Double.valueOf(workinghours)));
                                    attend.setNormal(null);
                                    attend.setAbsenteeism(null);
                                    //福利假期
                                    //家长会假//妊娠檢查休暇
                                    // 労災休暇//其他休暇
                                    //婚假 //丧假
                                    //流产假 //计划生育手术假//工伤
                                }
                                //更新考勤表
                                if(customerInfo.getUserinfo().getEnddate() == null || customerInfo.getUserinfo().getEnddate().isEmpty())
                                {
                                    attend.setTshortsickleave(attend.getShortsickleave());
                                    attend.setTlongsickleave(attend.getLongsickleave());
                                    attend.setTabsenteeism(attend.getAbsenteeism());
                                    attend.setShortsickleave(null);
                                    attend.setLongsickleave(null);
                                    attend.setAbsenteeism(null);
                                }
                                else
                                {
                                    String enddate = customerInfo.getUserinfo().getEnddate().substring(0,10);
                                    if (sf1ymd.parse(Convert.toStr(sf1ymd.format(Convert.toDate(enddate)))).getTime() > attend.getDates().getTime())
                                    {
                                        attend.setTshortsickleave(attend.getShortsickleave());
                                        attend.setTlongsickleave(attend.getLongsickleave());
                                        attend.setTabsenteeism(attend.getAbsenteeism());
                                        attend.setShortsickleave(null);
                                        attend.setLongsickleave(null);
                                        attend.setAbsenteeism(null);
                                    }
                                }
                                attend.preUpdate(tokenModel);
                                attendanceMapper.updateByPrimaryKey(attend);
                            }
                        }
                    }
                    else
                    {
                        attendance.setDates(abNormal.getOccurrencedate());
                        List<Attendance> attendancelist = attendanceMapper.select(attendance);
                        DecimalFormat df = new DecimalFormat("######0.00");

                        //检索打卡记录明细表（用用户id，）

                        if (attendancelist.size() > 0) {
                            for (Attendance attend : attendancelist) {
                                WorkingDay workDay = new WorkingDay();
                                workDay.setWorkingdate(abNormal.getOccurrencedate());
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(abNormal.getOccurrencedate());
                                List<WorkingDay> workingDaysList = workingDayMapper.select(workDay);

                                if(attend.getTabsenteeism() != null && !attend.getTabsenteeism().isEmpty())
                                {
                                    attend.setAbsenteeism(attend.getTabsenteeism());
                                }
                                if(attend.getTshortsickleave() != null && !attend.getTshortsickleave().isEmpty())
                                {
                                    attend.setShortsickleave(attend.getTshortsickleave());
                                }
                                if(attend.getTlongsickleave() != null && !attend.getTlongsickleave().isEmpty())
                                {
                                    attend.setLongsickleave(attend.getTlongsickleave());
                                }
                                //判断当天是否是休日，青年节，妇女节，周六周日
                                if(workingDaysList.size()>0)
                                {
                                    //振替出勤日
                                    if(workingDaysList.get(0).getType().equals("4"))
                                    {
                                        workinghours = "8";
                                    }
                                    else
                                    {
                                        workinghours = "0";
                                    }
                                }
                                else if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
                                {
                                    workinghours = "0";
                                }
                                else if(sf1ymd.format(abNormal.getOccurrencedate()).equals(DateUtil.format(new Date(),"YYYY").toString() + "-03-08") || sf1ymd.format(abNormal.getOccurrencedate()).equals(DateUtil.format(new Date(),"YYYY").toString() + "-05-04"))
                                {
                                    workinghours = "4";
                                }
                                else
                                {
                                    workinghours = "8";
                                }
                                String timeLength=null;
                                timeLength = df.format(Double.valueOf(abNormal.getRelengthtime()));
                                if (!(Double.valueOf(timeLength) % (Double.valueOf(absenteeism))==0))
                                {
                                    if (!abNormal.getErrortype().equals("PR013001"))
                                    {
                                        timeLength = df.format(Math.floor(Double.valueOf(abNormal.getLengthtime()) / Double.valueOf(absenteeism))*Double.valueOf(absenteeism) + Double.valueOf(absenteeism) );
                                    }
                                }
                                if (abNormal.getErrortype().equals("PR013001")) {//外出

                                    if (Double.valueOf(timeLength) >= Double.valueOf(attend.getAbsenteeism())) {
                                        if (Double.valueOf(timeLength) >= Double.valueOf(workinghours)) {
                                            attend.setNormal(workinghours);
                                            attend.setAbsenteeism(null);
                                        } else {

                                            attend.setNormal(df.format(Double.valueOf(attend.getNormal()) + Double.valueOf(attend.getAbsenteeism())));
                                            attend.setAbsenteeism(null);
                                        }
                                    }
                                    else
                                    {
                                        attend.setNormal(df.format(Double.valueOf(attend.getNormal()) + Double.valueOf(timeLength)));
                                        attend.setAbsenteeism(df.format(Double.valueOf(attend.getAbsenteeism()) - Double.valueOf(timeLength)));
                                        attend.setNormal(df.format(Math.floor(Double.valueOf(attend.getNormal()) / Double.valueOf(absenteeism))*Double.valueOf(absenteeism)));
                                        if (!(Double.valueOf(attend.getAbsenteeism()) % (Double.valueOf(absenteeism))==0))
                                        {
                                            attend.setAbsenteeism(df.format(Math.floor(Double.valueOf(attend.getAbsenteeism()) / Double.valueOf(absenteeism))*Double.valueOf(absenteeism) + Double.valueOf(absenteeism) ));
                                        }
                                    }

                                } else if (abNormal.getErrortype().equals("PR013005")) {//年休
                                    attend.setAnnualrest(df.format( Double.valueOf(timeLength)));
                                } else if (abNormal.getErrortype().equals("PR013006")) {//代休周末
                                    attend.setDaixiu(df.format( Double.valueOf(timeLength)));
                                } else if (abNormal.getErrortype().equals("PR013007")) {//代休特殊
                                    attend.setDaixiu(df.format( Double.valueOf(timeLength)));
                                } else if (abNormal.getErrortype().equals("PR013008")) {//事休
                                    attend.setCompassionateleave(df.format( Double.valueOf(timeLength)));
                                } else if (abNormal.getErrortype().equals("PR013009")) {//短期病休

                                    attend.setShortsickleave(df.format( Double.valueOf(timeLength)));

                                } else if (abNormal.getErrortype().equals("PR013010")) {//長期病休

                                    attend.setLongsickleave(df.format( Double.valueOf(timeLength)));
                                } else if (abNormal.getErrortype().equals("PR013012") || abNormal.getErrortype().equals("PR013013")) { //産休（女） 护理假（男）
                                    attend.setNursingleave(df.format( Double.valueOf(timeLength)));
                                } else if (abNormal.getErrortype().equals("PR013014") || abNormal.getErrortype().equals("PR013016")
                                        || abNormal.getErrortype().equals("PR013018") || abNormal.getErrortype().equals("PR013019")
                                        || abNormal.getErrortype().equals("PR013011") || abNormal.getErrortype().equals("PR013015")
                                        || abNormal.getErrortype().equals("PR013004") || abNormal.getErrortype().equals("PR013017")
                                        || abNormal.getErrortype().equals("PR013020")) {
                                    //福利假期
                                    //家长会假//妊娠檢查休暇
                                    // 労災休暇//其他休暇
                                    //婚假 //丧假
                                    //流产假 //计划生育手术假//工伤
                                    attend.setWelfare(df.format( Double.valueOf(timeLength)));
                                }

                                if(!abNormal.getErrortype().equals("PR013001"))
                                {
                                    attend.setAbsenteeism(attend.getAbsenteeism() == null ? "0" : attend.getAbsenteeism());
                                    if (Double.valueOf(attend.getAbsenteeism()) > Double.valueOf(timeLength) )
                                    {
                                        attend.setAbsenteeism(df.format(Double.valueOf(attend.getAbsenteeism()) - Double.valueOf(timeLength)));
                                    }
                                    else
                                    {
                                        attend.setAbsenteeism(null);
                                    }
                                }
                                attend.setNormal(attend.getNormal() == null ? "0" :attend.getNormal());
                                attend.setAnnualrest(attend.getAnnualrest()==null ? "0"  :attend.getAnnualrest());
                                attend.setDaixiu(attend.getDaixiu()==null ? "0" :attend.getDaixiu());
                                attend.setCompassionateleave(attend.getCompassionateleave()==null? "0" :attend.getCompassionateleave());
                                attend.setShortsickleave(attend.getShortsickleave()==null ? "0" :attend.getShortsickleave());
                                attend.setLongsickleave(attend.getLongsickleave()==null ? "0" :attend.getLongsickleave());
                                attend.setNursingleave(attend.getNursingleave()==null ? "0" :attend.getNursingleave());
                                attend.setWelfare(attend.getWelfare()==null ? "0" :attend.getWelfare());
                                attend.setAbsenteeism(attend.getAbsenteeism() ==null?"0":attend.getAbsenteeism());

                                String setNormal = null;

                                setNormal = df.format(Double.valueOf(attend.getAbsenteeism()) +  Double.valueOf(attend.getShortsickleave())
                                        + Double.valueOf(attend.getLongsickleave()) + Double.valueOf(attend.getCompassionateleave()) + Double.valueOf(attend.getAnnualrest())
                                        + Double.valueOf(attend.getDaixiu()) + Double.valueOf(attend.getNursingleave()) + Double.valueOf(attend.getWelfare()));

                                attend.setNormal(df.format(Double.valueOf(workinghours) - Double.valueOf(setNormal) <=0?null:Double.valueOf(workinghours) - Double.valueOf(setNormal)));

                                attend.setNormal(Double.valueOf(attend.getNormal()) <= 0 ? null :attend.getNormal());
                                attend.setAnnualrest(Double.valueOf(attend.getAnnualrest()) <= 0 ? null  :attend.getAnnualrest());
                                attend.setDaixiu(Double.valueOf(attend.getDaixiu()) <= 0 ? null :attend.getDaixiu());
                                attend.setCompassionateleave(Double.valueOf(attend.getCompassionateleave()) <= 0 ? null :attend.getCompassionateleave());
                                attend.setShortsickleave(Double.valueOf(attend.getShortsickleave()) <= 0 ? null :attend.getShortsickleave());
                                attend.setLongsickleave(Double.valueOf(attend.getLongsickleave()) <= 0 ? null :attend.getLongsickleave());
                                attend.setNursingleave(Double.valueOf(attend.getNursingleave()) <= 0 ? null :attend.getNursingleave());
                                attend.setWelfare(Double.valueOf(attend.getWelfare()) <= 0 ? null :attend.getWelfare());
                                attend.setAbsenteeism(Double.valueOf(attend.getAbsenteeism() ) <= 0 ? null:attend.getAbsenteeism());

                                //更新考勤表
                                if(customerInfo.getUserinfo().getEnddate() == null || customerInfo.getUserinfo().getEnddate().isEmpty())
                                {
                                    attend.setTshortsickleave(attend.getShortsickleave());
                                    attend.setTlongsickleave(attend.getLongsickleave());
                                    attend.setTabsenteeism(attend.getAbsenteeism());
                                    attend.setShortsickleave(null);
                                    attend.setLongsickleave(null);
                                    attend.setAbsenteeism(null);
                                }
                                else
                                {
                                    String enddate = customerInfo.getUserinfo().getEnddate().substring(0,10);
                                    if (sf1ymd.parse(Convert.toStr(sf1ymd.format(Convert.toDate(enddate)))).getTime() > attend.getDates().getTime())
                                    {
                                        attend.setTshortsickleave(attend.getShortsickleave());
                                        attend.setTlongsickleave(attend.getLongsickleave());
                                        attend.setTabsenteeism(attend.getAbsenteeism());
                                        attend.setShortsickleave(null);
                                        attend.setLongsickleave(null);
                                        attend.setAbsenteeism(null);
                                    }
                                }
                                attend.preUpdate(tokenModel);
                                attendanceMapper.updateByPrimaryKey(attend);
                            }
                        }
                    }
                }
                else
                {
                    if(!abNormal.getOccurrencedate().equals(abNormal.getFinisheddate()))
                    {
                        List<Attendance> attendancelist = attendanceMapper.selectAttendance(abNormal);
                        DecimalFormat df = new DecimalFormat("######0.00");
                        if(attendancelist.size() > 0)
                        {
                            for (Attendance attend : attendancelist)
                            {

                                WorkingDay workDay = new WorkingDay();
                                workDay.setWorkingdate(attend.getDates());
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(attend.getDates());
                                List<WorkingDay> workingDaysList = workingDayMapper.select(workDay);

                                //判断当天是否是休日，青年节，妇女节，周六周日
                                if(workingDaysList.size()>0)
                                {
                                    //振替出勤日
                                    if(workingDaysList.get(0).getType().equals("4"))
                                    {
                                        workinghours = "8";
                                    }
                                    else
                                    {
                                        workinghours = "0";
                                    }
                                }
                                else if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
                                {
                                    workinghours = "0";
                                }
                                else if(sf1ymd.format(abNormal.getOccurrencedate()).equals(DateUtil.format(new Date(),"YYYY").toString() + "-03-08") || sf1ymd.format(abNormal.getOccurrencedate()).equals(DateUtil.format(new Date(),"YYYY").toString() + "-05-04"))
                                {
                                    workinghours = "4";
                                }
                                else
                                {
                                    workinghours = "8";
                                }

                                if (abNormal.getErrortype().equals("PR013001")) {//外出
                                    attend.setNormal(df.format(Double.valueOf(workinghours)));
                                    attend.setAbsenteeism(null);

                                } else if (abNormal.getErrortype().equals("PR013005")) {//年休
                                    attend.setAnnualrest(df.format(Double.valueOf(workinghours)));
                                    attend.setNormal(null);
                                    attend.setAbsenteeism(null);
                                } else if (abNormal.getErrortype().equals("PR013006")) {//代休周末
                                    attend.setDaixiu(df.format(Double.valueOf(workinghours)));
                                    attend.setNormal(null);
                                    attend.setAbsenteeism(null);
                                } else if (abNormal.getErrortype().equals("PR013007")) {//代休特殊
                                    attend.setDaixiu(df.format(Double.valueOf(workinghours)));
                                    attend.setNormal(null);
                                    attend.setAbsenteeism(null);
                                } else if (abNormal.getErrortype().equals("PR013008")) {//事休
                                    attend.setCompassionateleave(df.format(Double.valueOf(workinghours)));
                                    attend.setNormal(null);
                                    attend.setAbsenteeism(null);
                                } else if (abNormal.getErrortype().equals("PR013009")) {//短期病休
                                    attend.setShortsickleave(df.format(Double.valueOf(workinghours)));
                                    attend.setNormal(null);
                                    attend.setAbsenteeism(null);
                                } else if (abNormal.getErrortype().equals("PR013010")) {//長期病休
                                    attend.setLongsickleave(df.format(Double.valueOf(workinghours)));
                                    attend.setNormal(null);
                                    attend.setAbsenteeism(null);
                                } else if (abNormal.getErrortype().equals("PR013012") || abNormal.getErrortype().equals("PR013013")) { //産休（女） 护理假（男）
                                    attend.setNursingleave(df.format(Double.valueOf(workinghours)));
                                    attend.setNormal(null);
                                    attend.setAbsenteeism(null);
                                } else if (abNormal.getErrortype().equals("PR013014") || abNormal.getErrortype().equals("PR013016")
                                        || abNormal.getErrortype().equals("PR013018") || abNormal.getErrortype().equals("PR013019")
                                        || abNormal.getErrortype().equals("PR013011") || abNormal.getErrortype().equals("PR013015")
                                        || abNormal.getErrortype().equals("PR013004") || abNormal.getErrortype().equals("PR013017")
                                        || abNormal.getErrortype().equals("PR013020")) {
                                    attend.setWelfare(df.format(Double.valueOf(workinghours)));
                                    attend.setNormal(null);
                                    attend.setAbsenteeism(null);
                                    //福利假期
                                    //家长会假//妊娠檢查休暇
                                    // 労災休暇//其他休暇
                                    //婚假 //丧假
                                    //流产假 //计划生育手术假//工伤
                                }
                                //更新考勤表
                                if(customerInfo.getUserinfo().getEnddate() == null || customerInfo.getUserinfo().getEnddate().isEmpty())
                                {
                                    attend.setTshortsickleave(attend.getShortsickleave());
                                    attend.setTlongsickleave(attend.getLongsickleave());
                                    attend.setTabsenteeism(attend.getAbsenteeism());
                                    attend.setShortsickleave(null);
                                    attend.setLongsickleave(null);
                                    attend.setAbsenteeism(null);
                                }
                                else
                                {
                                    String enddate = customerInfo.getUserinfo().getEnddate().substring(0,10);
                                    if (sf1ymd.parse(Convert.toStr(sf1ymd.format(Convert.toDate(enddate)))).getTime() > attend.getDates().getTime())
                                    {
                                        attend.setTshortsickleave(attend.getShortsickleave());
                                        attend.setTlongsickleave(attend.getLongsickleave());
                                        attend.setTabsenteeism(attend.getAbsenteeism());
                                        attend.setShortsickleave(null);
                                        attend.setLongsickleave(null);
                                        attend.setAbsenteeism(null);
                                    }
                                }
                                attend.preUpdate(tokenModel);
                                attendanceMapper.updateByPrimaryKey(attend);
                            }
                        }
                    }
                    else
                    {
                        attendance.setDates(abNormal.getOccurrencedate());
                        List<Attendance> attendancelist = attendanceMapper.select(attendance);
                        DecimalFormat df = new DecimalFormat("######0.00");

                        if (attendancelist.size() > 0) {
                            for (Attendance attend : attendancelist) {
                                WorkingDay workDay = new WorkingDay();
                                workDay.setWorkingdate(abNormal.getOccurrencedate());
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(abNormal.getOccurrencedate());
                                List<WorkingDay> workingDaysList = workingDayMapper.select(workDay);

                                if(attend.getTabsenteeism() != null && !attend.getTabsenteeism().isEmpty())
                                {
                                    attend.setAbsenteeism(attend.getTabsenteeism());
                                }
                                if(attend.getTshortsickleave() != null && !attend.getTshortsickleave().isEmpty())
                                {
                                    attend.setShortsickleave(attend.getTshortsickleave());
                                }
                                if(attend.getTlongsickleave() != null && !attend.getTlongsickleave().isEmpty())
                                {
                                    attend.setLongsickleave(attend.getTlongsickleave());
                                }
                                //判断当天是否是休日，青年节，妇女节，周六周日
                                if(workingDaysList.size()>0)
                                {
                                    //振替出勤日
                                    if(workingDaysList.get(0).getType().equals("4"))
                                    {
                                        workinghours = "8";
                                    }
                                    else
                                    {
                                        workinghours = "0";
                                    }
                                }
                                else if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
                                {
                                    workinghours = "0";
                                }
                                else if(sf1ymd.format(abNormal.getOccurrencedate()).equals(DateUtil.format(new Date(),"YYYY").toString() + "-03-08") || sf1ymd.format(abNormal.getOccurrencedate()).equals(DateUtil.format(new Date(),"YYYY").toString() + "-05-04"))
                                {
                                    workinghours = "4";
                                }
                                else
                                {
                                    workinghours = "8";
                                }
                                String timeLength=null;
                                timeLength = df.format(Double.valueOf(abNormal.getLengthtime()));
                                if (!(Double.valueOf(timeLength) % (Double.valueOf(absenteeism))==0))
                                {
                                    if(!abNormal.getErrortype().equals("PR013001")) {
                                        timeLength = df.format(Math.floor(Double.valueOf(abNormal.getLengthtime()) / Double.valueOf(absenteeism)) * Double.valueOf(absenteeism) + Double.valueOf(absenteeism));
                                    }
                                }
                                if (abNormal.getErrortype().equals("PR013001")) {//外出

                                    if (Double.valueOf(timeLength) >= Double.valueOf(attend.getAbsenteeism())) {
                                        if (Double.valueOf(timeLength) >= Double.valueOf(workinghours)) {
                                            attend.setNormal(workinghours);
                                            attend.setAbsenteeism(null);
                                        } else {
                                            attend.setNormal(df.format(Double.valueOf(attend.getNormal()) + Double.valueOf(attend.getAbsenteeism())));
                                            attend.setAbsenteeism(null);
                                        }
                                    }
                                    else
                                    {
                                        attend.setNormal(df.format(Double.valueOf(attend.getNormal()) + Double.valueOf(timeLength)));
                                        attend.setAbsenteeism(df.format(Double.valueOf(attend.getAbsenteeism()) - Double.valueOf(timeLength)));
                                        attend.setNormal(df.format(Math.floor(Double.valueOf(attend.getNormal()) / Double.valueOf(absenteeism))*Double.valueOf(absenteeism)));
                                        if (!(Double.valueOf(attend.getAbsenteeism()) % (Double.valueOf(absenteeism))==0))
                                        {
                                            attend.setAbsenteeism(df.format(Math.floor(Double.valueOf(attend.getAbsenteeism()) / Double.valueOf(absenteeism))*Double.valueOf(absenteeism) + Double.valueOf(absenteeism) ));
                                        }

                                    }

                                } else if (abNormal.getErrortype().equals("PR013005")) {//年休
                                    if(attend.getAnnualrest() != null && !attend.getAnnualrest().isEmpty()){
                                        attend.setAnnualrest(df.format(Double.valueOf(timeLength) + Double.valueOf(attend.getAnnualrest())));
                                    }
                                    else{
                                        attend.setAnnualrest(df.format(Double.valueOf(timeLength)));
                                    }
                                } else if (abNormal.getErrortype().equals("PR013006")) {//代休周末
                                    if(attend.getDaixiu() != null && !attend.getDaixiu().isEmpty()){
                                        attend.setDaixiu(df.format(Double.valueOf(timeLength) + Double.valueOf(attend.getDaixiu())));
                                    }
                                    else{
                                        attend.setDaixiu(df.format(Double.valueOf(timeLength)));
                                    }
                                } else if (abNormal.getErrortype().equals("PR013007")) {//代休特殊
                                    if(attend.getDaixiu() != null && !attend.getDaixiu().isEmpty()){
                                        attend.setDaixiu(df.format(Double.valueOf(timeLength) + Double.valueOf(attend.getDaixiu())));
                                    }
                                    else{
                                        attend.setDaixiu(df.format(Double.valueOf(timeLength)));
                                    }
                                } else if (abNormal.getErrortype().equals("PR013008")) {//事休
                                    if(attend.getCompassionateleave() != null && !attend.getCompassionateleave().isEmpty()){
                                        attend.setCompassionateleave(df.format(Double.valueOf(timeLength) + Double.valueOf(attend.getCompassionateleave())));
                                    }
                                    else{
                                        attend.setCompassionateleave(df.format(Double.valueOf(timeLength)));
                                    }
                                } else if (abNormal.getErrortype().equals("PR013009")) {//短期病休
                                    if(attend.getShortsickleave() != null && !attend.getShortsickleave().isEmpty()){
                                        attend.setShortsickleave(df.format(Double.valueOf(timeLength) + Double.valueOf(attend.getShortsickleave())));
                                    }
                                    else{
                                        attend.setShortsickleave(df.format(Double.valueOf(timeLength)));
                                    }

                                } else if (abNormal.getErrortype().equals("PR013010")) {//長期病休

                                    if(attend.getLongsickleave() != null && !attend.getLongsickleave().isEmpty()){
                                        attend.setLongsickleave(df.format(Double.valueOf(timeLength) + Double.valueOf(attend.getLongsickleave())));
                                    }
                                    else{
                                        attend.setLongsickleave(df.format(Double.valueOf(timeLength)));
                                    }
                                } else if (abNormal.getErrortype().equals("PR013012") || abNormal.getErrortype().equals("PR013013")) { //産休（女） 护理假（男）
                                    if(attend.getNursingleave() != null && !attend.getNursingleave().isEmpty()){
                                        attend.setNursingleave(df.format(Double.valueOf(timeLength) + Double.valueOf(attend.getNursingleave())));
                                    }
                                    else{
                                        attend.setNursingleave(df.format(Double.valueOf(timeLength)));
                                    }
                                } else if (abNormal.getErrortype().equals("PR013014") || abNormal.getErrortype().equals("PR013016")
                                        || abNormal.getErrortype().equals("PR013018") || abNormal.getErrortype().equals("PR013019")
                                        || abNormal.getErrortype().equals("PR013011") || abNormal.getErrortype().equals("PR013015")
                                        || abNormal.getErrortype().equals("PR013004") || abNormal.getErrortype().equals("PR013017")
                                        || abNormal.getErrortype().equals("PR013020")) {
                                    //福利假期
                                    //家长会假//妊娠檢查休暇
                                    // 労災休暇//其他休暇
                                    //婚假 //丧假
                                    //流产假 //计划生育手术假//工伤
                                    if(attend.getWelfare() != null && !attend.getWelfare().isEmpty()){
                                        attend.setWelfare(df.format(Double.valueOf(timeLength) + Double.valueOf(attend.getWelfare())));
                                    }
                                    else{
                                        attend.setWelfare(df.format(Double.valueOf(timeLength)));
                                    }
                                }

                                if(!abNormal.getErrortype().equals("PR013001"))
                                {
                                    attend.setAbsenteeism(attend.getAbsenteeism() == null ? "0" : attend.getAbsenteeism());
                                    if (Double.valueOf(attend.getAbsenteeism()) > Double.valueOf(timeLength) )
                                    {
                                        attend.setAbsenteeism(df.format(Double.valueOf(attend.getAbsenteeism()) - Double.valueOf(timeLength)));
                                    }
                                    else
                                    {
                                        attend.setAbsenteeism(null);
                                    }
                                }
                                attend.setNormal(attend.getNormal() == null ? "0" :attend.getNormal());
                                attend.setAnnualrest(attend.getAnnualrest()==null ? "0"  :attend.getAnnualrest());
                                attend.setDaixiu(attend.getDaixiu()==null ? "0" :attend.getDaixiu());
                                attend.setCompassionateleave(attend.getCompassionateleave()==null? "0" :attend.getCompassionateleave());
                                attend.setShortsickleave(attend.getShortsickleave()==null ? "0" :attend.getShortsickleave());
                                attend.setLongsickleave(attend.getLongsickleave()==null ? "0" :attend.getLongsickleave());
                                attend.setNursingleave(attend.getNursingleave()==null ? "0" :attend.getNursingleave());
                                attend.setWelfare(attend.getWelfare()==null ? "0" :attend.getWelfare());
                                attend.setAbsenteeism(attend.getAbsenteeism() ==null?"0":attend.getAbsenteeism());

                                String setNormal = null;

                                setNormal = df.format(Double.valueOf(attend.getAbsenteeism()) +  Double.valueOf(attend.getShortsickleave())
                                        + Double.valueOf(attend.getLongsickleave()) + Double.valueOf(attend.getCompassionateleave()) + Double.valueOf(attend.getAnnualrest())
                                        + Double.valueOf(attend.getDaixiu()) + Double.valueOf(attend.getNursingleave()) + Double.valueOf(attend.getWelfare()));

                                attend.setNormal(Double.valueOf(workinghours) - Double.valueOf(setNormal) <=0 ? null: df.format((Double.valueOf(workinghours) - Double.valueOf(setNormal))));

                                attend.setNormal(Double.valueOf(attend.getNormal()) <= 0 ? null :attend.getNormal());
                                attend.setAnnualrest(Double.valueOf(attend.getAnnualrest()) <= 0 ? null  :attend.getAnnualrest());
                                attend.setDaixiu(Double.valueOf(attend.getDaixiu()) <= 0 ? null :attend.getDaixiu());
                                attend.setCompassionateleave(Double.valueOf(attend.getCompassionateleave()) <= 0 ? null :attend.getCompassionateleave());
                                attend.setShortsickleave(Double.valueOf(attend.getShortsickleave()) <= 0 ? null :attend.getShortsickleave());
                                attend.setLongsickleave(Double.valueOf(attend.getLongsickleave()) <= 0 ? null :attend.getLongsickleave());
                                attend.setNursingleave(Double.valueOf(attend.getNursingleave()) <= 0 ? null :attend.getNursingleave());
                                attend.setWelfare(Double.valueOf(attend.getWelfare()) <= 0 ? null :attend.getWelfare());
                                attend.setAbsenteeism(Double.valueOf(attend.getAbsenteeism() ) <= 0 ? null:attend.getAbsenteeism());

                                //更新考勤表
                                if(customerInfo.getUserinfo().getEnddate() == null || customerInfo.getUserinfo().getEnddate().isEmpty())
                                {
                                    attend.setTshortsickleave(attend.getShortsickleave());
                                    attend.setTlongsickleave(attend.getLongsickleave());
                                    attend.setTabsenteeism(attend.getAbsenteeism());
                                    attend.setShortsickleave(null);
                                    attend.setLongsickleave(null);
                                    attend.setAbsenteeism(null);
                                }
                                else
                                {
                                    String enddate = customerInfo.getUserinfo().getEnddate().substring(0,10);
                                    if (sf1ymd.parse(Convert.toStr(sf1ymd.format(Convert.toDate(enddate)))).getTime() > attend.getDates().getTime())
                                    {
                                        attend.setTshortsickleave(attend.getShortsickleave());
                                        attend.setTlongsickleave(attend.getLongsickleave());
                                        attend.setTabsenteeism(attend.getAbsenteeism());
                                        attend.setShortsickleave(null);
                                        attend.setLongsickleave(null);
                                        attend.setAbsenteeism(null);
                                    }
                                }
                                attend.preUpdate(tokenModel);
                                attendanceMapper.updateByPrimaryKey(attend);
                            }
                        }
                    }
                }
            }
        }

        abNormalMapper.updateByPrimaryKey(abNormal);
    }

    @Override
    public AbNormal One(String abnormalid) throws Exception {
        return abNormalMapper.selectByPrimaryKey(abnormalid);
    }

    @Override
    public Map<String, String> cklength(AbNormal abNormal) throws Exception {
        Map<String, String> rst = new HashMap<String, String>();
        double lengths = 1;
        double relengths = 1;

        if("4".equals(abNormal.getLengthtime())){
            lengths = 0.5;
        }
        if(StrUtil.isNotBlank(abNormal.getRelengthtime())) {
            if ("4".equals(abNormal.getRelengthtime())) {
                relengths = 0.5;
            }
            lengths = relengths - lengths;
        }

        if("PR013005".equals(abNormal.getErrortype())){
            List<String> ls = new ArrayList<String>();
            ls.add(abNormal.getUser_id());
            List<AnnualLeave> list = annualLeaveMapper.getDataList(ls);
            if(list.size() > 0 ){
                if(list.get(0).getRemaining_annual_leave_thisyear().doubleValue() >= lengths){
                    String Timecheck = String.valueOf(list.get(0).getRemaining_annual_leave_thisyear().doubleValue());
                    rst.put("dat",Timecheck);
                    rst.put("error",abNormal.getErrortype());
                    rst.put("can","yes");
                }else{
                    rst.put("dat","");
                    rst.put("can","no");
                }
            }else{
                rst.put("dat","");
                rst.put("can","no");
            }
        }else if("PR013006".equals(abNormal.getErrortype())){

                List<restViewVo> list = annualLeaveMapper.getrest(abNormal.getUser_id());
                String dat="";
                for(restViewVo item:list){
                    if(Double.parseDouble(item.getRestdays()) != 0){
                        if(lengths >= 0){
                            lengths = lengths - Double.parseDouble(item.getRestdays());
                            dat += dat + "," + item.getApplicationdate();
                            if(lengths <= 0){
                                String check = String.valueOf(list.get(0).getRestdays());
                                dat = dat.substring(1,dat.length());
                                rst.put("checkdat",check);
                                rst.put("error",abNormal.getErrortype());
                                rst.put("dat",dat);
                                rst.put("can","yes");
                                return rst;
                            }
                        }else{
                            String check = String.valueOf(list.get(0).getRestdays());
                            rst.put("checkdat",check);
                            rst.put("error",abNormal.getErrortype());
                            rst.put("dat","");
                            rst.put("can","yes");
                        }
                    }
                }
            rst.put("dat","");
            rst.put("can","no");
        } else if("PR013007".equals(abNormal.getErrortype())){
            List<restViewVo> list = annualLeaveMapper.getrest(abNormal.getUser_id());
            String dat="";
            for(restViewVo item:list){
                if(Double.parseDouble(item.getRestdays()) != 0){
                    if(lengths >= 0){
                        lengths = lengths - Double.parseDouble(item.getRestdays());
                        dat += dat + "," + item.getApplicationdate();
                        if(lengths <= 0){
                            String check = String.valueOf(list.get(0).getRestdays());
                            dat = dat.substring(1,dat.length());
                            rst.put("checkdat",check);
                            rst.put("error",abNormal.getErrortype());
                            rst.put("dat",dat);
                            rst.put("can","yes");
                            return rst;
                        }
                    }else{
                        String check = String.valueOf(list.get(0).getRestdays());
                        rst.put("checkdat",check);
                        rst.put("error",abNormal.getErrortype());
                        rst.put("dat","");
                        rst.put("can","yes");
                    }
                }
            }


            rst.put("dat","");
            rst.put("can","no");
        }

        return rst;
    }

    //代休
    public void updateReplacerest(AbNormal abNormal, TokenModel tokenModel) throws Exception {
        Replacerest replacerest = new Replacerest();
        replacerest.setUser_id(abNormal.getUser_id());
        replacerest.setRecognitionstate("0");
        List<Replacerest> recruitlist = replacerestMapper.select(replacerest);
        for(Replacerest re : recruitlist){
            if(abNormal.getErrortype().equals("PR013007")) //代休特殊
            {
                String strDuration = String.valueOf(Double.valueOf(re.getDuration_teshu()) - Double.valueOf(abNormal.getLengthtime()));
                replacerest.setDuration_teshu(strDuration);
                if(strDuration.equals("0")){
                    replacerest.setDuration_teshu(null);
                    replacerest.setRecognitionstate("1");
                }
            }
            if(abNormal.getErrortype().equals("PR013006")) //代休周末
            {
                String strDuration = String.valueOf(Double.valueOf(re.getDuration()) - Double.valueOf(abNormal.getLengthtime()));
                replacerest.setDuration(strDuration);
                if(strDuration.equals("0")){
                    replacerest.setDuration(null);
                    replacerest.setRecognitionstate("1");
                }
            }
            replacerest.preUpdate(tokenModel);
            replacerestMapper.updateByPrimaryKey(replacerest);
        }

    }

}
