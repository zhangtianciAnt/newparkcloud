package com.nt.service_pfans.PFANS2000.mapper;

import com.nt.dao_Pfans.PFANS2000.PunchcardRecordDetailbp;
import com.nt.dao_Pfans.PFANS6000.Expatriatesinfor;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface PunchcardRecordDetailbpMapper extends MyMapper<PunchcardRecordDetailbp> {

    List<Expatriatesinfor> getexpatriatesinforbp(@Param("ids") List<String> ids);

    List<PunchcardRecordDetailbp> getPunDetailbp(@Param("jobnumber") String jobnumber, @Param("punchcardrecord_date") String punchcardrecord_date);

    void deletetepunbp(@Param("punchcardrecord_date") String punchcardrecord_date);

    void deletetepundetbp(@Param("punchcardrecord_date") String punchcardrecord_date);
}
