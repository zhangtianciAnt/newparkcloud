package com.nt.service_pfans.PFANS6000;

import com.nt.dao_Pfans.PFANS6000.Coststatistics;
import com.nt.dao_Pfans.PFANS6000.CoststatisticsVo;
import com.nt.utils.LogicalException;
import com.nt.utils.dao.TokenModel;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface CoststatisticsService {
    List<Coststatistics> getCostList(Coststatistics coststatistics) throws Exception;

    Integer insertCoststatistics(Coststatistics coststatistics, TokenModel tokenModel) throws Exception;

    public XSSFWorkbook downloadExcel(CoststatisticsVo coststatisticsVo, HttpServletRequest request, HttpServletResponse resp) throws LogicalException;
}
