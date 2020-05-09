package com.nt.service_pfans.PFANS6000.Impl;

import com.nt.dao_Org.Dictionary;
import com.nt.dao_Pfans.PFANS6000.*;
import com.nt.dao_Pfans.PFANS6000.Vo.bpSum2Vo;
import com.nt.dao_Pfans.PFANS6000.Vo.bpSum3Vo;
import com.nt.service_Org.DictionaryService;
import com.nt.service_pfans.PFANS6000.CompanyStatisticsService;
import com.nt.service_pfans.PFANS6000.CoststatisticsService;
import com.nt.service_pfans.PFANS6000.mapper.*;
import com.nt.utils.LogicalException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional(rollbackFor = Exception.class)
public class CompanyStatisticsServiceImpl implements CompanyStatisticsService {

    @Autowired
    private CoststatisticsMapper coststatisticsMapper;

    @Autowired
    private VariousfundsMapper variousfundsMapper;

    @Autowired
    private CompanyStatisticsMapper companyStatisticsMapper;

    @Autowired
    private CoststatisticsService coststatisticsService;

    @Autowired
    private ExpatriatesinforMapper expatriatesinforMapper;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private SupplierinforMapper supplierinforMapper;


    @Override
    public Map<String, Object> getCosts(String groupid, String years) throws Exception {
        Map<String, Object> result = new HashMap<>();
        //Variousfunds variousfunds = new Variousfunds();
        //variousfunds.setOwner(tokenModel.getUserId());
        List<Variousfunds> allVariousfunds = variousfundsMapper.selectBygroupid(groupid, years);
        List<Dictionary> dictionaryList = dictionaryService.getForSelect("BP013");
        Map<String, String> plmonthPlanMap = new HashMap<>();
        Pattern pattern = Pattern.compile("(\\d+)");
        for (Dictionary d : dictionaryList) {
            String value = d.getValue1();
            Matcher matcher = pattern.matcher(value);
            if (matcher.find()) {
                value = matcher.group(1);
            }
            plmonthPlanMap.put(d.getCode(), value);
        }

        Map<String, Double> tripMap = new HashMap<>();
        Map<String, Double> assetsMap = new HashMap<>();
        for (Variousfunds v : allVariousfunds) {
            String month = plmonthPlanMap.getOrDefault(v.getPlmonthplan(), "");
            Map<String, Double> targetMap = null;
            if ("BP014001".equals(v.getTypeoffees())) {
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
            } catch (Exception e) {
            }
            targetMap.put(month, value + addValue);
        }

        Map<String, Double> finalTripMap = new HashMap<>();
        Map<String, Double> finalAssetsMap = new HashMap<>();
        double totalTrip = 0;
        double totalAssets = 0;
        for (int i = 1; i <= 12; i++) {
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

        //获取该group下外驻人员的单价
        Map<String, Double> userPriceMap = coststatisticsService.getUserPriceMapBygroupid(groupid, years);
        // 获取公司名称
        Expatriatesinfor expatriatesinfor = new Expatriatesinfor();
        expatriatesinfor.setGroup_id(groupid);
        List<Expatriatesinfor> companyList = expatriatesinforMapper.select(expatriatesinfor);
        Map<String, String> user2CompanyMap = new HashMap<String, String>();
        for (Expatriatesinfor ex : companyList) {
            String key = ex.getExpatriatesinfor_id();
            String value = ex.getSupplierinfor_id();
            user2CompanyMap.put(key, value);
        }
//        Calendar calendar = Calendar.getInstance();
//        int year = 0;
//        int month = calendar.get(Calendar.MONTH);
//        if(month >= 1 && month <= 3) {
//            year = calendar.get(Calendar.YEAR) - 1;
//        }else {
//            year = calendar.get(Calendar.YEAR);
//        }
        List<Coststatistics> allCostList = coststatisticsMapper.getCoststatisticsBygroupid(Integer.valueOf(years), groupid);
        Map<String, CompanyStatistics> companyMap = new HashMap<>();
        DecimalFormat df = new DecimalFormat("######0.00");
        for (Coststatistics c : allCostList) {
            String bpcompany = user2CompanyMap.getOrDefault(c.getBpname(), "");
            CompanyStatistics company = companyMap.getOrDefault(bpcompany, new CompanyStatistics());
            company.setBpcompany(bpcompany);

            String userPriceKey = c.getBpname() + "price";
            // 个人单位的合计费用(行合计)
            double totalmanhours = 0;
            // 个人单位的合计工数(行合计)
            double totalcost = 0;

            // 分别计算12个月的累计值
            for (int i = 1; i <= 12; i++) {
                String p_manhour = "manhour" + i;
                String p_cost = "cost" + i;

                // 上一次合计结果
                double oldManhour = 0;
                double oldCost = 0;
                try {
                    oldManhour = Double.parseDouble(BeanUtils.getProperty(company, p_manhour));
                } catch (Exception e) {
                }
                try {
                    oldCost = Double.parseDouble(BeanUtils.getProperty(company, p_cost));
                } catch (Exception e) {
                }

                // 当前结果
                double manhour = 0;
                try {
                    manhour = Double.parseDouble(BeanUtils.getProperty(c, p_manhour));
                } catch (Exception e) {
                }
                double price = userPriceMap.getOrDefault(userPriceKey + i, 0d);
                double cost = price * manhour;

                double newCost = cost + oldCost;
                double newManhour = manhour + oldManhour;

//                company.setCost1("");
//                company.setManhour1("");
                BeanUtils.setProperty(company, p_cost, df.format(newCost));
                BeanUtils.setProperty(company, p_manhour, newManhour);
                totalcost += cost;
                totalmanhours += manhour;
            }

            // 操作行合计值
            double oldTotalmanhours = 0;
            double oldTotalcost = 0;
            try {
                oldTotalcost = Double.parseDouble(company.getTotalcost());
            } catch (Exception e) {
            }
            try {
                oldTotalmanhours = Double.parseDouble(company.getTotalmanhours());
            } catch (Exception e) {
            }

            company.setTotalcost(df.format(oldTotalcost + totalcost));
            company.setTotalmanhours(df.format(oldTotalmanhours + totalmanhours));
            companyMap.put(bpcompany, company);
        }
        result.put("company", new ArrayList<>(companyMap.values()));

        // year
        result.put("year", getBusinessYear());

        return result;
    }

//    @Override
//    public Map<String, Object> getWorkTimes(Coststatistics coststatistics,String groupid,String years) {
//        Map<String, Object> result = new HashMap<>();
//
//        Map<String, Object> sqlParams = new HashMap<>();
//        sqlParams.putAll(BeanMap.create(coststatistics));
//        sqlParams.put("yesValue", "是");
//        sqlParams.put("noValue", "否");
//        List<CompanyStatistics> list = companyStatisticsMapper.getWorkTimes(sqlParams);
//        result.put("worktimes", list);
//        // year
//        result.put("year", getBusinessYear());
//        return result;
//    }
//
//    @Override
//    public Map<String, Object> getWorkerCounts(Coststatistics coststatistics,String groupid,String years) {
//        Map<String, Object> result = new HashMap<>();
//        Map<String, Object> sqlParams = new HashMap<>();
//        sqlParams.putAll(BeanMap.create(coststatistics));
//        sqlParams.put("yesValue", "是");
//        List<CompanyStatistics> list = companyStatisticsMapper.getWorkers(sqlParams);
//        result.put("workers", list);
//        // year
//        result.put("year", getBusinessYear());
//        return result;
//    }


    @Override
    public List<bpSum2Vo> getWorkTimes(String groupid, String years) throws LogicalException {
        List<bpSum2Vo> list2 = companyStatisticsMapper.getbpsum2(groupid, years);
        return list2;
    }


    @Override
    public List<bpSum3Vo> getWorkerCounts(String groupid, String years) throws LogicalException {
        List<bpSum3Vo> list = companyStatisticsMapper.getbpsum(groupid, years);
        return list;
    }


    private String[] getBusinessYear() {
        String[] arr = {"", ""};
        Calendar now = Calendar.getInstance();
        int month = now.get(Calendar.MONTH) + 1;

        if (month >= 1 && month <= 4) {
            String yearL = String.valueOf(now.get(Calendar.YEAR) - 1);
            String year = String.valueOf(now.get(Calendar.YEAR));
            arr[0] = yearL;
            arr[1] = year;
            return arr;
        } else {
            String yearL = String.valueOf(now.get(Calendar.YEAR));
            String year = String.valueOf(now.get(Calendar.YEAR) + 1);
            arr[0] = yearL;
            arr[1] = year;
            return arr;
        }
    }

    @Override
    public XSSFWorkbook downloadExcel(String groupid, String years, HttpServletRequest request, HttpServletResponse resp) throws LogicalException {
        InputStream in = null;
        try {
            //表格操作
            in = getClass().getClassLoader().getResourceAsStream("jxls_templates/BPshetongji.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(in);
            this.getReportWork1(workbook.getSheetAt(0), groupid, years);
            this.getReportWork2(workbook.getSheetAt(1), groupid, years);
            this.getReportWork3(workbook.getSheetAt(2), groupid, years);

            return workbook;
        } catch (Exception e) {
            throw new LogicalException(e.getMessage());
        } finally {
            IOUtils.closeQuietly(in);
        }
    }


    private void getReportWork1(XSSFSheet sheet1, String groupid, String years) throws LogicalException {
        try {
            Coststatistics coststatistics = new Coststatistics();

            Map<String, Object> result = getCosts(groupid, years);
            List<CompanyStatistics> companyStatisticsList = (List<CompanyStatistics>) result.get("company");
            Map<String, Double> trip = (Map<String, Double>) result.get("trip");
            Map<String, Double> asset = (Map<String, Double>) result.get("asset");

            Calendar now = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
            now.setTime(sdf.parse(getBusinessYear()[0] + "年04月"));
            //日期赋值
            for (int j = 1; j <= 12; j++) {
                sheet1.getRow(1).getCell(2 * j + 1).setCellValue(sdf.format(now.getTime()));
                now.set(Calendar.MONTH, now.get(Calendar.MONTH) + 1);
            }
            //
            Map<String, Double> totalCostMap = new HashMap<>();
            //将数据放入Excel
            int i = 3;
            for (CompanyStatistics c : companyStatisticsList) {
                //创建工作表的行
                XSSFRow row = sheet1.createRow(i);

                for (int k = 1; k <= 13; k++) {
                    double manhour = 0;
                    double cost = 0;
                    String property = "manhour" + k;
                    String propertyC = "cost" + k;
                    if (k > 12) {
                        property = "totalmanhours";
                        propertyC = "totalcost";
                    }
                    try {
                        manhour = Double.parseDouble(BeanUtils.getProperty(c, property));
                        cost = Double.parseDouble(BeanUtils.getProperty(c, propertyC));

                        int colIndex = getColIndex4Month(k);
                        row.createCell(colIndex).setCellValue(manhour);
                        row.createCell(colIndex + 1).setCellValue(cost);

                        totalCostMap.put(property, totalCostMap.getOrDefault(property, 0.0) + manhour);
                        totalCostMap.put(propertyC, totalCostMap.getOrDefault(propertyC, 0.0) + cost);
                    } catch (Exception e) {
                    }

                }

                row.createCell(1).setCellValue(i - 2);
                Supplierinfor ls = supplierinforMapper.selectByPrimaryKey(c.getBpcompany());
                if (ls != null) {
                    row.createCell(2).setCellValue(ls.getSupchinese());
                }
//                row.createCell(27).setCellValue(c.getTotalmanhours());
//                row.createCell(28).setCellValue(c.getTotalcost());

                totalCostMap.put("totalmanhours", totalCostMap.getOrDefault("totalmanhours", 0.0) + getDoubleValue(c, "totalmanhours"));
                totalCostMap.put("totalcost", totalCostMap.getOrDefault("totalcost", 0.0) + getDoubleValue(c, "totalcost"));
                i++;
            }
            int rowIndex = companyStatisticsList.size() + 2;
            //合计行
            XSSFRow rowT = sheet1.createRow(1 + rowIndex);
            rowT.createCell(1).setCellValue("合计");
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

            CellRangeAddress region = new CellRangeAddress(1 + rowIndex, 1 + rowIndex, 1, 2);
            sheet1.addMergedRegion(region);
            // 合计28列，后数4行，两两合并
            for (int r = rowIndex + 2; r < rowIndex + 6; r++) {
                for (i = 1; i <= 28; i = i + 2) {
                    CellRangeAddress region1 = new CellRangeAddress(r, r, i, i + 1);
                    sheet1.addMergedRegion(region1);
                }
            }
            // 设置值
            for (int k = 1; k <= 13; k++) {
                String property = "manhour" + k;
                String propertyC = "cost" + k;
                if (k > 12) {
                    property = "totalmanhours";
                    propertyC = "totalcost";
                }
                int colIndex = getColIndex4Month(k);
                double tripflg = 0.0;
                double assetflg = 0.0;
                double totalCostMapflg = 0.0;
                if (trip.size() > 0) {
                    //出張経費(元)
                    rowT2.createCell(colIndex).setCellValue(trip.get(propertyC));
                    tripflg = trip.get(propertyC);
                } else {
                    rowT2.createCell(colIndex).setCellValue(0.0);
                }
                if (asset.size() > 0) {
                    //設備経費(元)
                    rowT3.createCell(colIndex).setCellValue(asset.get(propertyC));
                    assetflg = asset.get(propertyC);
                } else {
                    rowT3.createCell(colIndex).setCellValue(0.0);
                }
                if (totalCostMap.size() > 0) {
                    //合计行
                    rowT.createCell(colIndex).setCellValue(totalCostMap.get(property));
                    rowT.createCell(colIndex + 1).setCellValue(totalCostMap.get(propertyC));
                    //経費除きの平均単価(人月)
                    if (totalCostMap.get(property) == 0) {
                        Double avg = 0.00;
                        rowT1.createCell(colIndex).setCellValue(avg);
                        totalCostMapflg = totalCostMap.get(propertyC);
                    } else {
                        Double avg = totalCostMap.get(propertyC) / totalCostMap.get(property);
                        rowT1.createCell(colIndex).setCellValue(avg);
                        totalCostMapflg = totalCostMap.get(propertyC);
                    }

                } else {
                    rowT.createCell(colIndex).setCellValue(0.0);
                    rowT.createCell(colIndex + 1).setCellValue(0.0);
                    rowT1.createCell(colIndex).setCellValue(0.0);
                }
                //外注総合費用合計(元)
                Double totalAll = totalCostMapflg + tripflg + assetflg;
                rowT4.createCell(colIndex).setCellValue(totalAll);
            }

        } catch (Exception e) {
            throw new LogicalException(e.getMessage());
        }
    }


    private void getReportWork2(XSSFSheet sheet1, String groupid, String years) throws LogicalException {
        try {
            Calendar now = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
            now.setTime(sdf.parse(getBusinessYear()[0] + "年04月"));
            //日期赋值
            for (int j = 1; j <= 12; j++) {
                sheet1.getRow(1).getCell(2 * j + 1).setCellValue(sdf.format(now.getTime()));
                now.set(Calendar.MONTH, now.get(Calendar.MONTH) + 1);
            }
            List<bpSum2Vo> list = this.getWorkTimes(groupid, years);
            int rowIndex = list.size() + 2;
            //合计行
            XSSFRow rowT = sheet1.createRow(1 + rowIndex);
            rowT.createCell(1).setCellValue("合计");
            CellRangeAddress region = new CellRangeAddress(1 + rowIndex, 1 + rowIndex, 1, 2);
            sheet1.addMergedRegion(region);

            //最后大合计
            Double TotalN = 0.0, TotalW = 0.0;
            //将数据放入Excel
            int i = 3;
            Map<String, Double> totalCostMap = new HashMap<>();
            SimpleDateFormat sf1ym = new SimpleDateFormat("yyyy-MM");
            for (bpSum2Vo c : list) {
                //创建工作表的行
                XSSFRow row = sheet1.createRow(i);

                //行合计
                Double lineTotalN = 0.0, lineTotalW = 0.0;

                int month = Integer.valueOf(sf1ym.format(sf1ym.parse(c.getDate())).substring(5, 7));
                for (int k = 1; k <= 12; k++) {
                    int colIndex = getColIndex4Month(k);
                    double manhourN = 0;
                    double manhourW = 0;
                    String propertyN = "manhourN" + k;
                    String propertyW = "manhourM" + k;
                    //如果是本月
                    if (month == k) {
                        if (c.getOPERATIONFORM().equals("BP024001")) {
                            manhourN = Double.valueOf(c.getWorktime());
                            manhourW = 0;
                        } else {
                            manhourN = 0;
                            manhourW = Double.valueOf(c.getWorktime());
                        }
                    }

                    lineTotalN += manhourN;
                    lineTotalW += manhourW;
                    TotalN += manhourN;
                    TotalW += manhourW;
                    row.createCell(colIndex).setCellValue(manhourN);
                    row.createCell(colIndex + 1).setCellValue(manhourW);
                    totalCostMap.put(propertyN, totalCostMap.getOrDefault(propertyN, 0.0) + manhourN);
                    totalCostMap.put(propertyW, totalCostMap.getOrDefault(propertyW, 0.0) + manhourW);

                    rowT.createCell(colIndex).setCellValue(totalCostMap.get(propertyN));
                    rowT.createCell(colIndex + 1).setCellValue(totalCostMap.get(propertyW));
                }
                row.createCell(1).setCellValue(i - 2);
                row.createCell(2).setCellValue(c.getSUPPLIERNAME());
                row.createCell(27).setCellValue(lineTotalN);
                row.createCell(28).setCellValue(lineTotalW);
                i++;
            }
            int colIndexTol = getColIndex4Month(13);
            rowT.createCell(colIndexTol).setCellValue(TotalN);
            rowT.createCell(colIndexTol + 1).setCellValue(TotalW);
        } catch (Exception e) {
            throw new LogicalException(e.getMessage());
        }
    }

    private void getReportWork3(XSSFSheet sheet, String groupid, String years) throws LogicalException {
        try {
            Calendar now = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
            now.setTime(sdf.parse(getBusinessYear()[0] + "年04月"));
            //日期赋值
            for (int j = 1; j <= 12; j++) {
                sheet.getRow(2).getCell(j + 2).setCellValue(sdf.format(now.getTime()));
                now.set(Calendar.MONTH, now.get(Calendar.MONTH) + 1);
            }
            List<bpSum3Vo> list = this.getWorkerCounts(groupid, years);
            Double lineTotal = 0.0;
            //将数据放入Excel
            int i = 4;
            Map<String, Double> totalCostMap = new HashMap<>();
            SimpleDateFormat sf1ym = new SimpleDateFormat("yyyy-MM");
            for (bpSum3Vo c : list) {
                //创建工作表的行
                XSSFRow row = sheet.createRow(i);
                Double lineTotalManhour = 0.0;
                int month = Integer.valueOf(sf1ym.format(sf1ym.parse(c.getDate())).substring(5, 7));
                for (int k = 1; k <= 12; k++) {
                    double manhour = 0;
                    String property = "manhour" + k;
                    int colIndex = k > 12 ? k + 2 : (getColIndex4Month(k) - 3) / 2 + 3;
                    if (month == k) {
                        manhour = Double.valueOf(c.getCounts());
                    }
                    lineTotalManhour += manhour;
                    lineTotal += manhour;
                    row.createCell(colIndex).setCellValue(manhour);
                    totalCostMap.put(property, totalCostMap.getOrDefault(property, 0.0) + manhour);
                }
                row.createCell(1).setCellValue(i - 3);
                row.createCell(2).setCellValue(c.getSUPPLIERNAME());
                row.createCell(15).setCellValue(lineTotalManhour);
                totalCostMap.put("totalmanhours", totalCostMap.getOrDefault("totalmanhours", 0.0) + getDoubleValue(c, "totalmanhours"));
                i++;
            }

            int rowIndex = list.size() + 3;
            //合计行
            XSSFRow rowT = sheet.createRow(1 + rowIndex);
            rowT.createCell(1).setCellValue("合计");
            CellRangeAddress region = new CellRangeAddress(1 + rowIndex, 1 + rowIndex, 1, 2);
            sheet.addMergedRegion(region);
//            // 设置值
            for (int k = 1; k <= 13; k++) {
                String property = "manhour" + k;
                if (k > 12) {
                    property = "totalmanhours";
                }
                int colIndex = k > 12 ? k + 2 : (getColIndex4Month(k) - 3) / 2 + 3;
                //合计行
                if (totalCostMap.size() > 0) {
                    rowT.createCell(colIndex).setCellValue(totalCostMap.get(property));
                } else {
                    rowT.createCell(colIndex).setCellValue(0.0);
                }
            }
            rowT.createCell(15).setCellValue(lineTotal);
        } catch (Exception e) {
            throw new LogicalException(e.getMessage());
        }
    }

    private Double getDoubleValue(Object o, String property) {
        try {
            return Double.parseDouble(BeanUtils.getProperty(o, property));
        } catch (Exception e) {
            return 0.0;
        }
    }

    private int getColIndex4Month(int month) {
        if (month <= 3) {
            return 2 * (month - 4 + 12) + 3;
        } else if (month > 12) {
            return 2 * month + 1;
        } else {
            return 2 * (month - 4) + 3;
        }
    }
}
