package com.nt.service_pfans.PFANS2000.mapper;


import com.nt.dao_Pfans.PFANS2000.PunchCardRecord;
import com.nt.utils.MyMapper;
import java.util.List;

public interface PunchCardRecordMapper extends MyMapper<PunchCardRecord> {
    List<PunchCardRecord> getPunchCardRecord();
}
