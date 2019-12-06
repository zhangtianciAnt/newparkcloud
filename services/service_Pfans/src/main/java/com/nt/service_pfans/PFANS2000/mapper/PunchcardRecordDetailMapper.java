package com.nt.service_pfans.PFANS2000.mapper;

import com.nt.dao_Pfans.PFANS2000.PunchcardRecord;
import com.nt.dao_Pfans.PFANS2000.PunchcardRecordDetail;
import com.nt.utils.MyMapper;

import java.util.List;


public interface PunchcardRecordDetailMapper extends MyMapper<PunchcardRecordDetail> {
    List<PunchcardRecord> getPunchCardRecord();

}
