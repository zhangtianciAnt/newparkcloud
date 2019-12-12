package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.Holiday;
import com.nt.dao_Pfans.PFANS1000.Vo.HolidayVo;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface HolidayService {

    List<Holiday> getHoliday(Holiday holiday) throws Exception;

    public HolidayVo selectById(String holidayid) throws Exception;

    void update(HolidayVo holidayVo, TokenModel tokenModel) throws Exception;

    public void insert(HolidayVo holidayVo, TokenModel tokenModel)throws Exception;

}
