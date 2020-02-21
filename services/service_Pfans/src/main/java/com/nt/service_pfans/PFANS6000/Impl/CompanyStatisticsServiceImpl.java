package com.nt.service_pfans.PFANS6000.Impl;

import com.nt.dao_Pfans.PFANS6000.CompanyStatistics;
import com.nt.dao_Pfans.PFANS6000.Coststatistics;
import com.nt.dao_Pfans.PFANS6000.CoststatisticsVo;
import com.nt.dao_Pfans.PFANS6000.Variousfunds;
import com.nt.service_pfans.PFANS6000.CompanyStatisticsService;
import com.nt.service_pfans.PFANS6000.mapper.CompanyStatisticsMapper;
import com.nt.service_pfans.PFANS6000.mapper.CoststatisticsMapper;
import com.nt.service_pfans.PFANS6000.mapper.VariousfundsMapper;
import com.nt.utils.LogicalException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class CompanyStatisticsServiceImpl implements CompanyStatisticsService {

    @Autowired
    private CoststatisticsMapper coststatisticsMapper;

    @Autowired
    private VariousfundsMapper variousfundsMapper;

    @Autowired
    private CompanyStatisticsMapper companyStatisticsMapper;


    @Override
    public Map<String, Object> getCosts(Coststatistics coststatistics) throws Exception{
        Map<String, Object> result = new HashMap<>();

        Variousfunds variousfunds = new Variousfunds();
//        variousfunds.setOwner(tokenModel.getUserId());
        List<Variousfunds> allVariousfunds = variousfundsMapper.select(variousfunds);
        Map<String, Double> tripMap = new HashMap<>();
        Map<String, Double> assetsMap = new HashMap<>();
        for ( Variousfunds v : allVariousfunds ) {
            String month = v.getPlmonthplan();
            Map<String, Double> targetMap = null;
            if ( "BP014001".equals(v.getTypeoffees())) {
                //出张经费
                targetMap = tripMap;
            } else if ("BP014002".equals(v.getTypeoffees())) {
                //设备经费
                targetMap = assetsMap;
            } else {
                continue;
            }
            double value = targetMap.getOrDefault(month, 0.0);
            double addValue = 0;
            try {
                addValue = Double.parseDouble(v.getPayment());
            } catch (Exception e) {}
            targetMap.put(month, value + addValue);
        }
        // rebuild Map
        // format : {cost1..12, totalcost}
        Map<String, Double> finalTripMap = new HashMap<>();
        Map<String, Double> finalAssetsMap = new HashMap<>();
        double totalTrip = 0;
        double totalAssets = 0;
        for (int i=1; i<=12; i++) {
            String key = i + "";
            double trip = tripMap.getOrDefault(key, 0.0);
            double asset = assetsMap.getOrDefault(key, 0.0);
            finalTripMap.put("cost".concat(key), trip);
            finalAssetsMap.put("cost".concat(key), asset);
            totalAssets += asset;
            totalTrip += trip;
        }
        finalTripMap.put("totalcost", totalTrip);
        finalAssetsMap.put("totalcost", totalAssets);

        // add to result
        result.put("trip", finalTripMap);
        result.put("asset", finalAssetsMap);



        List<Coststatistics> allCostList = coststatisticsMapper.getExpatriatesinfor(coststatistics);
        Map<String, CompanyStatistics> companyMap = new HashMap<>();
        for ( Coststatistics c : allCostList ) {
            String bpcompany = c.getBpcompany();
            CompanyStatistics company = companyMap.get(bpcompany);
            if ( company == null ) {
                company = new CompanyStatistics();
                company.setBpcompany(bpcompany);
            }
//            double price = c.getUnitprice();
            double price = 100;//todo 假数据
            // 个人单位的合计费用
            double totalmanhours = 0;
            // 个人单位的合计工数
            double totalcost = 0;
            for ( int i=1; i<=12; i++ ) {
                String p_manhour = "manhour" + i;
                String p_cost = "cost" + i;

                double oldManhour = 0;
                double oldCost = 0;
                try {
                    oldManhour = Double.parseDouble(BeanUtils.getProperty(company, p_manhour));
                } catch (Exception e) {}
                try {
                    oldCost = Double.parseDouble(BeanUtils.getProperty(company, p_cost));
                } catch (Exception e) {}

                double manhour = 0;
                try {
                    manhour = Double.parseDouble(BeanUtils.getProperty(c, p_manhour));
                } catch (Exception e) {}
                double cost = price * manhour;

                double newCost = cost + oldCost;
                double newManhour = manhour + oldManhour;

//                company.setCost1("");
//                company.setManhour1("");
                BeanUtils.setProperty(company, p_cost, newCost);
                BeanUtils.setProperty(company, p_manhour, newManhour);
                totalcost += cost;
                totalmanhours += manhour;
            }

            double oldTotalmanhours = 0;
            double oldTotalcost = 0;
            try {
                oldTotalcost = Double.parseDouble(company.getTotalcost());
            } catch (Exception e) {}
            try {
                oldTotalmanhours = Double.parseDouble(company.getTotalmanhours());
            } catch (Exception e) {}

            company.setTotalcost(oldTotalcost + totalcost + "");
            company.setTotalmanhours(oldTotalmanhours + totalmanhours + "");
            companyMap.put(bpcompany, company);
        }
        result.put("company", new ArrayList<>(companyMap.values()));

        // year todo 画面赋值年有问题
        result.put("year", getBusinessYear());

        return result;
    }

    @Override
    public Map<String, Object> getWorkTimes(Coststatistics coststatistics) {
        Map<String, Object> result = new HashMap<>();

        Map<String, Object> sqlParams = new HashMap<>();
        sqlParams.putAll(BeanMap.create(coststatistics));
        sqlParams.put("yesValue", "是");
        sqlParams.put("noValue", "否");
        List<CompanyStatistics> list = companyStatisticsMapper.getWorkTimes(sqlParams);
        result.put("worktimes", list);
        // year
        result.put("year", getBusinessYear());
        return result;
    }

    @Override
    public Map<String, Object> getWorkerCounts(Coststatistics coststatistics) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> sqlParams = new HashMap<>();
        sqlParams.putAll(BeanMap.create(coststatistics));
        sqlParams.put("yesValue", "是");
        List<CompanyStatistics> list = companyStatisticsMapper.getWorkers(sqlParams);
        result.put("workers", list);
        // year
        result.put("year", getBusinessYear());
        return result;
    }

    private int getBusinessYear() {
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        if ( now.get(Calendar.MONTH) <= 2 ) {
            year--;
        }
        return year;
    }

    @Override
    public XSSFWorkbook downloadExcel(HttpServletRequest request, HttpServletResponse resp) throws LogicalException {
        try {
            Coststatistics coststatistics = new Coststatistics();
            Map<String, Object> result = getCosts(coststatistics);
            List<CompanyStatistics> companyStatisticsList = (List<CompanyStatistics>)result.get("company");
            Map<String, Double> trip = (Map<String, Double>) result.get("trip");
            Map<String, Double> asset = (Map<String, Double>) result.get("asset");
            InputStream in = null;
            FileOutputStream f = null;
            XSSFWorkbook work = null;
            //表格操作
            in = getClass().getClassLoader().getResourceAsStream("jxls_templates/BPshetongji.xlsx");
            work = new XSSFWorkbook(in);
            XSSFSheet sheet1 = work.getSheetAt(0);
            Calendar now = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年mm月");
            int year = now.get(Calendar.YEAR);
            int year1 = year + 1;
            //日期赋值
            for(int c = 3; c< 25; c++) {
                if(c <= 19) {
                    int m = 4;
                    sheet1.getRow(1).getCell(c).setCellValue(year + "年" + m + "月");
                    m++;
                }else {
                    int n = 1;
                    sheet1.getRow(1).getCell(c).setCellValue(year1 + "年" + n + "月");
                    n++;
                }
                c = c+2;
            }
            //将数据放入Excel
            int i = 3;
            for (CompanyStatistics c : companyStatisticsList) {
                //创建工作表的行
                XSSFRow row = sheet1.createRow(i);

                for (int k = 0; k <=12; k++) {
                    double manhour = 0;
                    double cost = 0;
                    String property = "manhour" + k;
                    String propertyC = "cost" + k;
                    try {
                        manhour = Double.parseDouble(BeanUtils.getProperty(c, property));
                        cost = Double.parseDouble(BeanUtils.getProperty(c, propertyC));
                        switch(k){
                            case 4:
                                row.createCell(3).setCellValue(manhour);
                                row.createCell(4).setCellValue(cost);
                            case 5:
                                row.createCell(5).setCellValue(manhour);
                                row.createCell(6).setCellValue(cost);
                            case 6:
                                row.createCell(7).setCellValue(manhour);
                                row.createCell(8).setCellValue(cost);
                            case 7:
                                row.createCell(9).setCellValue(manhour);
                                row.createCell(10).setCellValue(cost);
                            case 8:
                                row.createCell(11).setCellValue(manhour);
                                row.createCell(12).setCellValue(cost);
                            case 9:
                                row.createCell(13).setCellValue(manhour);
                                row.createCell(14).setCellValue(cost);
                            case 10:
                                row.createCell(15).setCellValue(manhour);
                                row.createCell(16).setCellValue(cost);
                            case 11:
                                row.createCell(17).setCellValue(manhour);
                                row.createCell(18).setCellValue(cost);
                            case 12:
                                row.createCell(19).setCellValue(manhour);
                                row.createCell(20).setCellValue(cost);
                            case 1:
                                row.createCell(21).setCellValue(manhour);
                                row.createCell(22).setCellValue(cost);
                            case 2:
                                row.createCell(23).setCellValue(manhour);
                                row.createCell(24).setCellValue(cost);
                            case 3:
                                row.createCell(25).setCellValue(manhour);
                                row.createCell(26).setCellValue(cost);
                        }
                    } catch (Exception e) {}

                }

                row.createCell(1).setCellValue(i - 2);
                row.createCell(2).setCellValue(c.getBpcompany());
                row.createCell(27).setCellValue(c.getTotalmanhours());
                row.createCell(28).setCellValue(c.getTotalcost());
                i++;
            }

            Map<String, Double> addLine1 = new HashMap<String, Double>();
            Map<String, Double> addLine2 = new HashMap<String, Double>();
            Map<String, Double> addLine5 = new HashMap<String, Double>();
            String lineHour1 = "";
            String lineCost1 = "";
            String lineKey2 = "";
            String tripKey = "";
            String assetKey = "";
            String lineKey5 = "";
            for (int t =1; t<=13; t++) {
                double total_manhour = 0;
                double total_cost = 0;
                String key_hour = "manhour" + t;
                String key_cost  = "cost" + t;
                if ( t > 12 ) {
                    key_cost  = "totalcost";
                    key_hour  = "totalmanhours";
                }
                for (CompanyStatistics cList : companyStatisticsList) {

                    total_manhour += Double.parseDouble(BeanUtils.getProperty(cList, key_hour));
                    total_cost += Double.parseDouble(BeanUtils.getProperty(cList, key_cost));
                }
                lineHour1 = key_hour + t;
                lineCost1 = key_cost + t;
                addLine1.put(lineHour1, total_manhour);
                addLine1.put(lineCost1, total_cost);
                lineKey2 = key_cost + t;
                if ( total_manhour == 0 ) {
                    addLine2.put(lineKey2, 0.0);
                } else {
                    addLine2.put(lineKey2, total_cost/total_manhour);
                }
                tripKey = "cost" + t;
                assetKey = "cost" + t;
                lineKey5 = "cost" + t;
                addLine5.put(lineKey5, total_cost + trip.get("tripKey") + asset.get("assetKey"));
                addLine5.put("lineKeyTotal", total_cost + trip.get("totalcost") + asset.get("totalcost"));
            }

            int rowIndex = companyStatisticsList.size();
            //合计行
            XSSFRow rowT = sheet1.createRow(1 + rowIndex);
            rowT.createCell(1).setCellValue("合计");
            CellRangeAddress region = new CellRangeAddress(1 + rowIndex, 1 + rowIndex, 1, 2);
            sheet1.addMergedRegion(region);
            //経費除きの平均単価(人月)
            XSSFRow rowT1 = sheet1.createRow(2 + rowIndex);
            rowT1.createCell(1).setCellValue("経費除きの平均単価(人月)");
            //出張経費(元)
            XSSFRow rowT2 = sheet1.createRow(3 + rowIndex);
            rowT2.createCell(1).setCellValue("出張経費(元)");
            //設備経費(元)
            XSSFRow rowT3 = sheet1.createRow(4 + rowIndex);
            rowT3.createCell(1).setCellValue("設備経費(元)");
            //外注総合費用合計(元)
            XSSFRow rowT4 = sheet1.createRow(5 + rowIndex);
            rowT4.createCell(1).setCellValue("外注総合費用合計(元)");
            for(int r = 1; r<=13; r++) {
                CellRangeAddress region1 = new CellRangeAddress(rowIndex + 2, rowIndex + 2, r, r + 1);
                sheet1.addMergedRegion(region1);
                CellRangeAddress region2 = new CellRangeAddress(rowIndex + 3, rowIndex + 3, r, r + 1);
                sheet1.addMergedRegion(region2);
                CellRangeAddress region3 = new CellRangeAddress(rowIndex + 4, rowIndex + 4, r, r + 1);
                sheet1.addMergedRegion(region3);
                CellRangeAddress region4 = new CellRangeAddress(rowIndex + 5, rowIndex + 5, r, r + 1);
                sheet1.addMergedRegion(region4);
            }
            //合计五行赋值
            for(int p = 1; p <= 12; p++) {
                String hourKeys = "manhour" + p;
                String costKeys = "cost" + p;
                int j = 3;
                //合计行
                rowT.createCell(j).setCellValue(addLine1.get(hourKeys));
                rowT.createCell(j + 1).setCellValue(addLine1.get(costKeys));
                //
                rowT1.createCell(j).setCellValue(addLine2.get(costKeys));
                //
                rowT2.createCell(j).setCellValue(trip.get(costKeys));
                //
                rowT3.createCell(j).setCellValue(asset.get(costKeys));
                //
                rowT3.createCell(j).setCellValue(addLine5.get(costKeys));
                // todo 最后一列合计没做
                j = j + 2;
            }

            //写出到文件
            return work;
        } catch (Exception e) {
            throw new LogicalException(e.getMessage());
        }
    }
}
