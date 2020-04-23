package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.Overtime;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface OvertimeService {
    List<Overtime> getOvertime(Overtime overtime) throws Exception;

    List<Overtime> getOvertimeDay(Overtime overtime) throws Exception;

    List<Overtime> getOvertimeOne(Overtime overtime) throws Exception;

    List<Overtime> getOvertimelist(Overtime overtime) throws Exception;

    public Overtime One(String overtimeid) throws Exception;

    public void insertOvertime(Overtime overtime, TokenModel tokenModel)throws Exception;

    public void updateOvertime(Overtime overtime, TokenModel tokenModel)throws Exception;
}
