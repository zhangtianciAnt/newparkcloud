package com.nt.service_AOCHUAN.AOCHUAN5000;

import com.nt.dao_AOCHUAN.AOCHUAN5000.CredentialInformation;
import com.nt.dao_AOCHUAN.AOCHUAN5000.Vo.FinReport;

import java.util.List;

public interface FinReportService {

    //获取走货数据
    List<FinReport> getAll() throws  Exception;
}
