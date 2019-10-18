package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.Overtime;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface OvertimeService {
    //查看
    List<Overtime> getOvertime() throws Exception;
    Overtime getOvertimeOne(String overtime_id) throws  Exception;
    //增加
    void insertOvertime(Overtime overtime, TokenModel tokenModel)throws Exception;
    //修改
    void updateOvertime(Overtime overtime, TokenModel tokenModel)throws Exception;
}
