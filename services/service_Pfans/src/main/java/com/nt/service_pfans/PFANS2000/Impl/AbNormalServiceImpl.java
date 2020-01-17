package com.nt.service_pfans.PFANS2000.Impl;

import com.nt.dao_Pfans.PFANS2000.AbNormal;
import com.nt.dao_Pfans.PFANS2000.Attendance;
import com.nt.dao_Pfans.PFANS2000.AttendanceSetting;
import com.nt.service_pfans.PFANS2000.AbNormalService;
import com.nt.service_pfans.PFANS2000.mapper.AbNormalMapper;
import com.nt.service_pfans.PFANS2000.mapper.AttendanceMapper;
import com.nt.service_pfans.PFANS2000.mapper.AttendanceSettingMapper;
import com.nt.utils.AuthConstants;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class AbNormalServiceImpl implements AbNormalService {

    @Autowired
    private AbNormalMapper abNormalMapper;
    @Autowired
    private AttendanceMapper attendanceMapper;
    @Autowired
    private AttendanceSettingMapper attendanceSettingMapper;

    @Override
    public List<AbNormal> list(AbNormal abNormal) throws Exception {
        return abNormalMapper.select(abNormal);
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
        abNormalMapper.updateByPrimaryKey(abNormal);
        if(abNormal.getStatus().equals(AuthConstants.APPROVED_FLAG_NO)){
            //旷工基本计算单位
            String absenteeism = null;
            //工作时间
            String workinghours = null;
            AttendanceSetting attendancesetting = new AttendanceSetting();
            List<AttendanceSetting> attendancesettinglist = attendanceSettingMapper.select(attendancesetting);
            if(attendancesettinglist.size() > 0){
                //旷工基本计算单位
                absenteeism = attendancesettinglist.get(0).getAbsenteeism();
                //工作时间
                workinghours = attendancesettinglist.get(0).getWorkinghours();
            }
            //考勤管理
            Attendance attendance = new Attendance();
            attendance.setUser_id(abNormal.getUser_id());
            attendance.setDates(abNormal.getOccurrencedate());
            List<Attendance> attendancelist = attendanceMapper.select(attendance);
            DecimalFormat df = new DecimalFormat("######0.00");
            if(attendancelist.size() > 0){
                for (Attendance attend : attendancelist) {
                    if(abNormal.getErrortype().equals("PR013002")){//迟到
                        if(Double.valueOf(abNormal.getLengthtime()) >= Double.valueOf(attend.getLatetime())){
                            attend.setLate(abNormal.getLengthtime());
                            if(Double.valueOf(attend.getLatetime()) > Double.valueOf(absenteeism)){
                                attend.setAbsenteeism(absenteeism);
                            }
                            else{
                                attend.setAbsenteeism(null);
                                attend.setNormal(workinghours);
                            }
                            attend.setLatetime(null);
                        }
                    }
                    else if(abNormal.getErrortype().equals("PR013003")){//早退
                        if(Double.valueOf(abNormal.getLengthtime()) >= Double.valueOf(attend.getLeaveearlytime())){
                            attend.setLeaveearly(abNormal.getLengthtime());
                            if(Double.valueOf(attend.getLeaveearlytime()) > Double.valueOf(absenteeism)){
                                attend.setAbsenteeism(absenteeism);
                            }
                            else{
                                attend.setAbsenteeism(null);
                                attend.setNormal(workinghours);
                            }
                            attend.setLeaveearlytime(null);
                        }
                    }
                    else if(abNormal.getErrortype().equals("PR013005")){//年休
                        attend.setAnnualrest(abNormal.getLengthtime());
                    }
                    else if(abNormal.getErrortype().equals("PR013008")){//事休/事假
                        attend.setCompassionateleave(abNormal.getLengthtime());
                    }
                    else if(abNormal.getErrortype().equals("PR013009")){//短期病休
                        attend.setShortsickleave(abNormal.getLengthtime());
                    }
                    else if(abNormal.getErrortype().equals("PR013010")){//長期病休
                        attend.setLongsickleave(abNormal.getLengthtime());
                    }
                    else if(abNormal.getErrortype().equals("PR013012") || abNormal.getErrortype().equals("PR013013")){//産休（女）産休看護休暇（男）
                        attend.setNursingleave(abNormal.getLengthtime());
                    }
                    else if(abNormal.getErrortype().equals("PR013004")
                            || abNormal.getErrortype().equals("PR013011")
                            || abNormal.getErrortype().equals("PR013014")
                            || abNormal.getErrortype().equals("PR013015")
                            || abNormal.getErrortype().equals("PR013016")
                            || abNormal.getErrortype().equals("PR013017")
                            || abNormal.getErrortype().equals("PR013018")
                            || abNormal.getErrortype().equals("PR013019")
                            ){
                        //女性三期、结婚休假、家长会假、葬儀休暇、妊娠檢查休暇、哺乳休暇、労災休暇、其他休暇
                        attend.setWelfare(abNormal.getLengthtime());
                    }
                    attend.preUpdate(tokenModel);
                    attendanceMapper.updateByPrimaryKey(attend);
                }
            }
        }
    }

    @Override
    public AbNormal One(String abnormalid) throws Exception {
        return abNormalMapper.selectByPrimaryKey(abnormalid);
    }
}
