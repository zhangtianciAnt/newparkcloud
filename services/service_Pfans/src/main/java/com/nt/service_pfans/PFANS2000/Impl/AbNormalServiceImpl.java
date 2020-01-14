package com.nt.service_pfans.PFANS2000.Impl;

import com.nt.dao_Pfans.PFANS2000.AbNormal;
import com.nt.dao_Pfans.PFANS2000.Attendance;
import com.nt.service_pfans.PFANS2000.AbNormalService;
import com.nt.service_pfans.PFANS2000.mapper.AbNormalMapper;
import com.nt.service_pfans.PFANS2000.mapper.AttendanceMapper;
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
        if(abNormal.getStatus().equals(AuthConstants.APPROVED_FLAG_YES)){
            Attendance attendance = new Attendance();
            attendance.setUser_id(abNormal.getUser_id());
            attendance.setDates(abNormal.getOccurrencedate());
            List<Attendance> attendancelist = attendanceMapper.select(attendance);
            DecimalFormat df = new DecimalFormat("######0.00");
            if(attendancelist.size() > 0){
                for (Attendance attend : attendancelist) {
                    if(abNormal.getErrortype().equals("PR013005")){//年休
                        attend.setAnnualrest(abNormal.getLengthtime());
                    }
                    else if(abNormal.getErrortype().equals("PR013008")){//事休/事假
                        attend.setCompassionateleave(abNormal.getLengthtime());
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
