package com.nt.service_pfans.PFANS2000.Impl;

import com.mysql.jdbc.StringUtils;
import com.nt.dao_Pfans.PFANS2000.*;
import com.nt.service_pfans.PFANS2000.PunchcardRecordbpService;
import com.nt.service_pfans.PFANS2000.mapper.*;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class PunchcardRecordbpServiceImpl implements PunchcardRecordbpService {
    @Autowired
    private PunchcardRecordbpMapper punchcardrecordMapper;

    @Autowired
    private PunchcardRecordDetailbpMapper punchcardrecorddetailmapper;

    @Autowired
    private AttendancebpMapper attendancebpmapper;

    @Override
    public List<PunchcardRecordbp> list(String dates,PunchcardRecordbp punchcardrecord,TokenModel tokenModel) throws Exception {
        SimpleDateFormat sformat = new SimpleDateFormat("yyyy-MM");//日期格式
        List<PunchcardRecordbp> punlist = punchcardrecordMapper.select(punchcardrecord);
        if(!dates.equals("")){
            punlist = punlist.stream().filter(item -> (sformat.format(item.getPunchcardrecord_date()).equals(dates))).collect(Collectors.toList());
        }
        return punlist;
    }

    //add-ws-外协人员表查询
    @Override
    public List<Attendancebp> getAttendancebplist(Attendancebp attendancebp) throws Exception {
        return attendancebpmapper.select(attendancebp);
    }
    //add-ws-外协人员表查询

    @Override
    public List<PunchcardRecordDetailbp> getPunDetailbp(PunchcardRecordDetailbp detail) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String Jobnumber = detail.getJobnumber();
        String user_id = detail.getUser_id();
        String Punchcardrecord_date = sdf.format(detail.getPunchcardrecord_date());
        List<PunchcardRecordDetailbp> detaillist = punchcardrecorddetailmapper.getPunDetailbp(Jobnumber,user_id,Punchcardrecord_date);
        return detaillist;
    }
}
