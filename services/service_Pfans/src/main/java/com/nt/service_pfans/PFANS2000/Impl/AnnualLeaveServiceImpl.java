package com.nt.service_pfans.PFANS2000.Impl;

import com.nt.dao_Pfans.PFANS2000.AnnualLeave;
import com.nt.service_pfans.PFANS2000.AnnualLeaveService;
import com.nt.service_pfans.PFANS2000.mapper.AnnualLeaveMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor=Exception.class)
public class AnnualLeaveServiceImpl implements AnnualLeaveService {

    @Autowired
    private AnnualLeaveMapper annualLeaveMapper;

    @Override
    public List<AnnualLeave> getDataList(AnnualLeave annualLeave) {
        return annualLeaveMapper.select(annualLeave);
    }
}
