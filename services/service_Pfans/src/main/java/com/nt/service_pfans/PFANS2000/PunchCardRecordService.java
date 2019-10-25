package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.PunchCardRecord;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface PunchCardRecordService {
    //查看
    public List<PunchCardRecord> getPunchCardRecord(PunchCardRecord punchcardrecord) throws Exception;
    public PunchCardRecord One(String punchcardrecordid) throws Exception;

}
