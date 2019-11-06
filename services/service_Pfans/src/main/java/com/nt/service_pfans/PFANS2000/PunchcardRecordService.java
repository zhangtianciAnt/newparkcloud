package com.nt.service_pfans.PFANS2000;



import com.nt.dao_Pfans.PFANS2000.PunchcardRecord;
import com.nt.utils.LogicalException;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


public interface PunchcardRecordService {

    //获取异常申请列表信息
    List<PunchcardRecord> list(PunchcardRecord punchcardrecord) throws Exception;


    List<PunchcardRecord> importUser(HttpServletRequest request, TokenModel tokenModel,String flag) throws Exception ;
    String downloadUserModel() throws LogicalException;

}
