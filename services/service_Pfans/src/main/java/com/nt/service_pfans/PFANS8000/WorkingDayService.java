package com.nt.service_pfans.PFANS8000;

import com.nt.dao_Pfans.PFANS8000.WorkingDay;
import com.nt.utils.dao.TokenModel;
import java.util.List;


public interface WorkingDayService {


    void insert(WorkingDay workingday, TokenModel tokenModel)throws Exception;

    List<WorkingDay> getList(WorkingDay workingday) throws Exception;

    void delete(WorkingDay workingday, TokenModel tokenModel)throws Exception;
}
