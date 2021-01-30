package com.nt.service_pfans.PFANS2000.mapper;

import com.nt.dao_Pfans.PFANS2000.PunchcardRecord;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


public interface PunchcardRecordMapper extends MyMapper<PunchcardRecord> {

    List<PunchcardRecord> getDataList(@Param("user_id") String USER_ID);

}
