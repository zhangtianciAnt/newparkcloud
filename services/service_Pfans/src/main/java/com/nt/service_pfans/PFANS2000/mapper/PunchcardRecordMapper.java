package com.nt.service_pfans.PFANS2000.mapper;

import com.nt.dao_Pfans.PFANS2000.PunchcardRecord;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.Date;
import java.util.List;


public interface PunchcardRecordMapper extends MyMapper<PunchcardRecord> {

    List<PunchcardRecord> getDataList(@Param("user_id") String USER_ID);

   @Select("select time_end from PunchcardRecord where  DATE_FORMAT(punchcardrecord_date, '%Y-%m-%d') = #{punchcardrecord_date} and user_id=#{user_id}")
    List<PunchcardRecord> selectworktime(@Param("user_id") String user_id,@Param("punchcardrecord_date") String punchcardrecord_date);
}
