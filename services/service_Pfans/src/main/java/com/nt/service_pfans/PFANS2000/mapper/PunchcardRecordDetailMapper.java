package com.nt.service_pfans.PFANS2000.mapper;

import com.nt.dao_Pfans.PFANS2000.PunchcardRecord;
import com.nt.dao_Pfans.PFANS2000.PunchcardRecordDetail;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface PunchcardRecordDetailMapper extends MyMapper<PunchcardRecordDetail> {
    List<PunchcardRecord> getPunchCardRecord();

    List<PunchcardRecordDetail> getPunDetail(@Param("jobnumber") String jobnumber, @Param("punchcardrecord_date") String punchcardrecord_date);

    void deletetepun(@Param("punchcardrecord_date") String punchcardrecord_date);

    void deletetepundet(@Param("punchcardrecord_date") String punchcardrecord_date);
}
