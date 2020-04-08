package com.nt.service_pfans.PFANS2000.mapper;

import com.nt.dao_Pfans.PFANS2000.PunchcardRecord;
import com.nt.dao_Pfans.PFANS2000.PunchcardRecordDetailbp;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface PunchcardRecordDetailbpMapper extends MyMapper<PunchcardRecordDetailbp> {
    List<PunchcardRecord> getPunchCardRecord();

    void deletetepun(@Param("punchcardrecord_date") String punchcardrecord_date);

    void deletetepundet(@Param("punchcardrecord_date") String punchcardrecord_date);
}
