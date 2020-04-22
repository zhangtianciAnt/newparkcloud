package com.nt.service_AOCHUAN.AOCHUAN8000;

import com.nt.dao_AOCHUAN.AOCHUAN8000.WorkingDay;
import com.nt.utils.dao.TokenModel;
import java.util.List;


public interface WorkingsDayService {


    void insert(WorkingDay workingday, TokenModel tokenModel)throws Exception;

    List<WorkingDay> getDataList()throws Exception;
    void deletete(WorkingDay workingday, TokenModel tokenModel)throws Exception;
}
