package com.nt.service_pfans.PFANS6000;

import com.nt.dao_Pfans.PFANS6000.Coststatistics;
import com.nt.dao_Pfans.PFANS6000.CoststatisticsVo;
import com.nt.utils.LogicalException;
import com.nt.utils.dao.TokenModel;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface CoststatisticsService {
    List<Coststatistics> getCostList(Coststatistics coststatistics) throws Exception;
    List<Coststatistics> getCostListBygroupid(String groupid) throws Exception;

    Integer insertCoststatistics(String groupid,Coststatistics coststatistics, TokenModel tokenModel) throws Exception;

    Map<String, Double> getUserPriceMap() throws Exception;
    Map<String, Double> getUserPriceMapBygroupid(String groupid,String years) throws Exception;

    public XSSFWorkbook downloadExcel(CoststatisticsVo coststatisticsVo, HttpServletRequest request, HttpServletResponse resp) throws LogicalException;
}
