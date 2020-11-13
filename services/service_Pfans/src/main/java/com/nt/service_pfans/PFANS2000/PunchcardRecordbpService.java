package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.Attendance;
import com.nt.dao_Pfans.PFANS2000.Attendancebp;
import com.nt.dao_Pfans.PFANS2000.PunchcardRecordbp;
import com.nt.dao_Pfans.PFANS2000.PunchcardRecordDetailbp;
import com.nt.utils.dao.TokenModel;
import java.util.List;


public interface PunchcardRecordbpService {
    //add-ws-外协人员表查询
    List<Attendancebp> getAttendancebplist(Attendancebp attendancebp) throws Exception;
    //add-ws-外协人员表查询
    List<PunchcardRecordbp> list(String dates,PunchcardRecordbp punchcardrecord, TokenModel tokenModel) throws Exception;

    List<PunchcardRecordDetailbp> getPunDetailbp(PunchcardRecordDetailbp detail)throws Exception;
}
