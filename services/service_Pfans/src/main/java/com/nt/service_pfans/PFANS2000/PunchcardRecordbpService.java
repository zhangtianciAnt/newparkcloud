package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.PunchcardRecordbp;
import com.nt.dao_Pfans.PFANS2000.PunchcardRecordDetailbp;
import com.nt.utils.dao.TokenModel;
import java.util.List;


public interface PunchcardRecordbpService {

    List<PunchcardRecordbp> list(PunchcardRecordbp punchcardrecord, TokenModel tokenModel) throws Exception;

    List<PunchcardRecordDetailbp> getPunDetailbp(PunchcardRecordDetailbp detail)throws Exception;
}
