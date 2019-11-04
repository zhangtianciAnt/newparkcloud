package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.PunchcardRecord;
import java.util.List;

public interface PunchcardRecordService {


    //获取异常申请列表信息
    List<PunchcardRecord> list(PunchcardRecord punchcardrecord) throws Exception;


}
