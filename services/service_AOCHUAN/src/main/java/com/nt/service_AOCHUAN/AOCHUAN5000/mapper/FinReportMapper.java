package com.nt.service_AOCHUAN.AOCHUAN5000.mapper;

import com.nt.dao_AOCHUAN.AOCHUAN5000.Vo.FinReport;
import com.nt.utils.MyMapper;

import java.util.List;

public interface FinReportMapper extends MyMapper<FinReport> {

    List<FinReport> getAll();
}
