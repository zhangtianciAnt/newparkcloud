package com.nt.service_pfans.PFANS2000.Impl;

import com.nt.dao_Pfans.PFANS2000.*;
import com.nt.service_pfans.PFANS2000.PunchcardRecordbpService;
import com.nt.service_pfans.PFANS2000.mapper.*;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;

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
    public List<PunchcardRecordbp> list(PunchcardRecordbp punchcardrecord,TokenModel tokenModel) throws Exception {
        return punchcardrecordMapper.select(punchcardrecord);
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
        String Punchcardrecord_date = sdf.format(detail.getPunchcardrecord_date());
        List<PunchcardRecordDetailbp> detaillist = punchcardrecorddetailmapper.getPunDetailbp(Jobnumber,Punchcardrecord_date);
        return detaillist;
    }
}
