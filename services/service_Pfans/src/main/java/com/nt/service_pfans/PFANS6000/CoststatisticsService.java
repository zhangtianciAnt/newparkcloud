package com.nt.service_pfans.PFANS6000;

import com.nt.dao_Pfans.PFANS1000.Contractapplication;
import com.nt.dao_Pfans.PFANS6000.Coststatistics;
import com.nt.dao_Pfans.PFANS6000.CoststatisticsVo;
import com.nt.utils.LogicalException;
import com.nt.utils.dao.TokenModel;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface CoststatisticsService {
    List<Coststatistics> getCostList(Coststatistics coststatistics) throws Exception;
    List<Coststatistics> getCostListBygroupid(String groupid,String year) throws Exception;

    Integer insertCoststatistics(String groupid,String year,Coststatistics coststatistics, TokenModel tokenModel) throws Exception;

    Map<String, Double> getUserPriceMap() throws Exception;
    Map<String, Double> getUserPriceMapBygroupid(String groupid, String years) throws Exception;

    public XSSFWorkbook downloadExcel(CoststatisticsVo coststatisticsVo, HttpServletRequest request, HttpServletResponse resp) throws LogicalException;

    //gbb add 0804 月度赏与列表
    List<Coststatistics> getcostMonthList(String dates,String role,String groupid,TokenModel tokenModel) throws Exception;
    //gbb add 0804 月度赏与详情
    List<Map<String, String>> getcostMonth(String dates,String role,String groupid,TokenModel tokenModel) throws Exception;
    //gbb add 0805 添加費用統計
    void insertcoststatisticsdetail(List<ArrayList> strData, TokenModel tokenModel)throws  Exception;
    //0807 check是否已经生成个别合同
    int checkcontract(Contractapplication contract)throws  Exception;
}
