package com.nt.service_pfans.PFANS8000.Impl;

import com.nt.dao_Pfans.PFANS8000.WorkingDay;
import com.nt.service_pfans.PFANS8000.WorkingDayService;
import com.nt.service_pfans.PFANS8000.mapper.WorkingDayMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class WorkingDayServiceImpl implements WorkingDayService {

    @Autowired
    private WorkingDayMapper workingdayMapper;

    @Override
    public void insert(WorkingDay workingday, TokenModel tokenModel) throws Exception {
        workingday.preInsert(tokenModel);
        workingday.setWorkingday_id(UUID.randomUUID().toString());
        workingdayMapper.insert(workingday);
    }
    @Override
    public List<WorkingDay> getList(WorkingDay workingday) throws Exception {

        return workingdayMapper.select(workingday) ;
    }
}
