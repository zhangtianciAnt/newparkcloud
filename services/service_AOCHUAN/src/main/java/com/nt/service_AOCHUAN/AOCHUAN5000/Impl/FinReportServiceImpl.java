package com.nt.service_AOCHUAN.AOCHUAN5000.Impl;

import com.nt.dao_AOCHUAN.AOCHUAN3000.Vo.FinReport;
import com.nt.service_AOCHUAN.AOCHUAN5000.FinReportService;
import com.nt.service_AOCHUAN.AOCHUAN5000.mapper.FinReportMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinReportServiceImpl implements FinReportService {

    @Autowired
    private FinReportMapper finReportMapper;

    @Override
    public List<FinReport> getAll() throws Exception {
        return finReportMapper.getAll();
    }
}
