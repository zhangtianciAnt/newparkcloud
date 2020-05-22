package com.nt.service_pfans.PFANS6000;

import com.nt.dao_Pfans.PFANS6000.CompanyStatistics;
import com.nt.dao_Pfans.PFANS6000.Coststatistics;
import com.nt.dao_Pfans.PFANS6000.CoststatisticsVo;
import com.nt.dao_Pfans.PFANS6000.Vo.bpSum2Vo;
import com.nt.dao_Pfans.PFANS6000.Vo.bpSum3Vo;
import com.nt.utils.LogicalException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface CompanyStatisticsService {

    Map<String, Object> getCosts(String groupid,String years) throws Exception;


    List<bpSum2Vo> getWorkTimes(String groupid, String years) throws LogicalException;

    List<bpSum3Vo> getWorkerCounts(String groupid, String years) throws LogicalException;

    XSSFWorkbook downloadExcel(String groupid,String years,HttpServletRequest request, HttpServletResponse resp) throws LogicalException;
}
