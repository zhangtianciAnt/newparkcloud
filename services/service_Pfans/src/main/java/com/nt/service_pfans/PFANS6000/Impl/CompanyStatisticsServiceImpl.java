package com.nt.service_pfans.PFANS6000.Impl;

import com.alibaba.fastjson.JSONObject;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_Org.Vo.DepartmentVo;
import com.nt.dao_Pfans.PFANS1000.PublicExpense;
import com.nt.dao_Pfans.PFANS6000.*;
import com.nt.dao_Pfans.PFANS6000.Vo.bpSum2Vo;
import com.nt.dao_Pfans.PFANS6000.Vo.bpSum3Vo;
import com.nt.service_Org.DictionaryService;
import com.nt.service_Org.OrgTreeService;
import com.nt.service_pfans.PFANS1000.mapper.PublicExpenseMapper;
import com.nt.service_pfans.PFANS6000.CompanyStatisticsService;
import com.nt.service_pfans.PFANS6000.CoststatisticsService;
import com.nt.service_pfans.PFANS6000.mapper.*;
import com.nt.utils.LogicalException;
import com.nt.utils.StringUtils;
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
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

    @Autowired
    private BpCompanyCostMapper bpCompanyCostMapper;

    @Autowired
    private PublicExpenseMapper publicExpenseMapper;
    @Autowired
    private OrgTreeService orgTreeService;

    @Override
    public Map<String, Object> getCosts(String groupid, String years) throws Exception {
        Map<String, Object> result = new HashMap<>();

        // reigon BP社统计页面【出差经费(元)】和【设备经费(元)】合并为经费字段放置【预提】-【外注费用】列
//        List<Variousfunds> allVariousfunds = variousfundsMapper.selectBygroupid(groupid, years);
//        List<Dictionary> dictionaryList = dictionaryService.getForSelect("BP013");
//        Map<String, String> plmonthPlanMap = new HashMap<>();
//        Pattern pattern = Pattern.compile("(\\d+)");
//        for (Dictionary d : dictionaryList) {
//            String value = d.getValue1();
//            Matcher matcher = pattern.matcher(value);
//            if (matcher.find()) {
//                value = matcher.group(1);
//            }
//            plmonthPlanMap.put(d.getCode(), value);
//        }
//
//        Map<String, Double> tripMap = new HashMap<>();
//        Map<String, Double> assetsMap = new HashMap<>();
//        for (Variousfunds v : allVariousfunds) {
//            String month = plmonthPlanMap.getOrDefault(v.getPlmonthplan(), "");
//            Map<String, Double> targetMap = null;
//            if ("BP014001".equals(v.getTypeoffees())) {
//                //出张经费
//                targetMap = tripMap;
//            } else if ("BP014002".equals(v.getTypeoffees())) {
//                //设备经费
//                targetMap = assetsMap;
//            } else {
//                continue;
//            }
//            double value = targetMap.getOrDefault(month, 0.0);
//            double addValue = 0;
//            try {
//                addValue = Double.parseDouble(v.getPayment());
//            } catch (Exception e) {
//            }
//            targetMap.put(month, value + addValue);
//        }
//
//        Map<String, Double> finalTripMap = new HashMap<>();
//        Map<String, Double> finalAssetsMap = new HashMap<>();
//        double totalTrip = 0;
//        double totalAssets = 0;
//        for (int i = 1; i <= 12; i++) {
//            String key = i + "";
//            double trip = tripMap.getOrDefault(key, 0.0);
//            double asset = assetsMap.getOrDefault(key, 0.0);
//            finalTripMap.put("cost".concat(key), trip);
//            finalAssetsMap.put("cost".concat(key), asset);
//            totalAssets += asset;
//            totalTrip += trip;
//        }
//        finalTripMap.put("totalcost", totalTrip);
//        finalAssetsMap.put("totalcost", totalAssets);
//
//        // add to result
//        result.put("trip", finalTripMap);
//        result.put("asset", finalAssetsMap);
        // endreigon BP社统计页面【出差经费(元)】和【设备经费(元)】合并为经费字段放置【预提】-【外注费用】列

        //获取该group下外驻人员的单价
        Map<String, Double> userPriceMap = coststatisticsService.getUserPriceMapBygroupid(groupid, years);
        // 获取公司名称
        Expatriatesinfor expatriatesinfor = new Expatriatesinfor();
//        if(groupid !=null && !groupid.isEmpty())
//        {
//            expatriatesinfor.setGroup_id(groupid);
//        }
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
        // add gbb 210914 BP社统计添加添加费用列 start
        // 查询费用的作业工数和外注费用
        List<PublicExpense> publicExpenseList = publicExpenseMapper.getPublicexpenseRmb(years,groupid);
        // 查询经费
        List<Coststatistics> cosExpenseList = coststatisticsMapper.getCoststatisticsExpense(years,groupid);
        // add gbb 210914 BP社统计添加添加费用列 end
        Map<String, CompanyStatistics> companyMap = new HashMap<>();
        DecimalFormat df = new DecimalFormat("######0.00");
        DecimalFormat dlf = new DecimalFormat("#0.00");
        for (Coststatistics c : allCostList) {

            String bpcompany = user2CompanyMap.getOrDefault(c.getBpname(), "");
            CompanyStatistics company = companyMap.getOrDefault(bpcompany, new CompanyStatistics());
//            if(!StringUtils.isNullOrEmpty(company.getManhour5())){
//                company.setManhour5(dlf.format(Double.valueOf(company.getManhour5())));
//            }
            company.setBpcompany(bpcompany);

            String userPriceKey = c.getBpname() + c.getGroupid() + "price";
            // 个人单位的合计费用(行合计)
            double totalmanhours = 0;
            // 个人单位的合计工数(行合计)
            double totalcost = 0;

            // 分别计算12个月的累计值
            for (int i = 1; i <= 12; i++) {
                String p_manhour = "manhour" + i;
                String p_cost = "cost" + i;
                String p_manhourf = "manhour" + i + "f";
                String p_costf = "cost" + i + "f";

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

                BigDecimal cost1 = new BigDecimal(Double.toString(cost));
                BigDecimal cost2 = new BigDecimal(Double.toString(oldCost));
                BigDecimal cost3 = new BigDecimal(Double.toString(manhour));
                BigDecimal cost4 = new BigDecimal(Double.toString(oldManhour));
                double newCost = cost1.add(cost2).doubleValue();
                double newManhour =cost3.add(cost4).doubleValue();

//                company.setCost1("");
//                company.setManhour1("");
                BeanUtils.setProperty(company, p_cost, df.format(newCost));
                BeanUtils.setProperty(company, p_manhour, newManhour);
                BeanUtils.setProperty(company, p_manhourf, 0.0);
                BeanUtils.setProperty(company, p_costf, 0.00);
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
        // add gbb 210914 BP社统计添加添加费用列 start
        for (Map.Entry<String, CompanyStatistics> map : companyMap.entrySet()){
            List<PublicExpense> newPList = publicExpenseList.stream().filter(str ->(str.getPayeename().contains(map.getKey()))).collect(Collectors.toList());
            List<Coststatistics> newCoststatisticsList = cosExpenseList.stream().filter(str ->(str.getBpcompany().contains(map.getKey()))).collect(Collectors.toList());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formatterM = new SimpleDateFormat("M");
            // 单价
            BigDecimal unitPrice = new BigDecimal(Double.toString(18500));
            // 费用-作业工数
            double bigTormbCount = 0;
            // 费用-外注费用
            double bigManhourCount = 0;
            //费用计算
            for(PublicExpense newPub : newPList){
                if (newPub.getJzmonth() != null && StringUtils.isNotEmpty(newPub.getTormb())) {
                    String strJzmonth = formatterM.format(newPub.getJzmonth());
                    // 外注费用
                    BigDecimal bigTormb = new BigDecimal(newPub.getTormb());
                    // 费用-总外注费用
                    bigManhourCount += bigTormb.doubleValue();

                    // 作业工数 = 外注费用 / 18500
                    BigDecimal bigManhour = BigDecimal.valueOf(bigTormb.doubleValue() / 18500).setScale(2, RoundingMode.HALF_UP);

                    // 费用-总作业工数
                    bigTormbCount += bigTormb.doubleValue() / 18500;

                    if(strJzmonth.equals("1")){
                        map.getValue().setCost1f(bigTormb.toString());
                        map.getValue().setManhour1f(bigManhour.toString());
                    }
                    else if(strJzmonth.equals("2")){
                        map.getValue().setCost2(bigTormb.toString());
                        map.getValue().setManhour2f(bigManhour.toString());
                    }
                    if(strJzmonth.equals("3")){
                        map.getValue().setCost3f(bigTormb.toString());
                        map.getValue().setManhour3f(bigManhour.toString());
                    }
                    if(strJzmonth.equals("4")){
                        map.getValue().setCost4f(bigTormb.toString());
                        map.getValue().setManhour4f(bigManhour.toString());
                    }
                    if(strJzmonth.equals("5")){
                        map.getValue().setCost5f(bigTormb.toString());
                        map.getValue().setManhour5f(bigManhour.toString());
                    }
                    if(strJzmonth.equals("6")){
                        map.getValue().setCost6f(bigTormb.toString());
                        map.getValue().setManhour6f(bigManhour.toString());
                    }
                    if(strJzmonth.equals("7")){
                        map.getValue().setCost7f(bigTormb.toString());
                        map.getValue().setManhour7f(bigManhour.toString());
                    }
                    if(strJzmonth.equals("8")){
                        map.getValue().setCost8f(bigTormb.toString());
                        map.getValue().setManhour8f(bigManhour.toString());
                    }
                    if(strJzmonth.equals("9")){
                        map.getValue().setCost9f(bigTormb.toString());
                        map.getValue().setManhour9f(bigManhour.toString());
                    }
                    if(strJzmonth.equals("10")){
                        map.getValue().setCost10f(bigTormb.toString());
                        map.getValue().setManhour10f(bigManhour.toString());
                    }
                    if(strJzmonth.equals("11")){
                        map.getValue().setCost11f(bigTormb.toString());
                        map.getValue().setManhour11f(bigManhour.toString());
                    }
                    if(strJzmonth.equals("12")){
                        map.getValue().setCost12f(bigTormb.toString());
                        map.getValue().setManhour12f(bigManhour.toString());
                    }
                    companyMap.put(map.getKey(),map.getValue());
                }
            }
            // 预提费用计算
            for(Coststatistics newCoststatistics : newCoststatisticsList){
                // 外注费用1月
                BigDecimal bigExpense1 = new BigDecimal(newCoststatistics.getExpense1()).add(new BigDecimal(map.getValue().getCost1()));
                map.getValue().setCost1(bigExpense1.setScale(2, RoundingMode.HALF_UP).toString());
                // 外注费用2月
                BigDecimal bigExpense2 = new BigDecimal(newCoststatistics.getExpense2()).add(new BigDecimal(map.getValue().getCost2()));
                map.getValue().setCost2(bigExpense2.setScale(2, RoundingMode.HALF_UP).toString());
                // 外注费用3月
                BigDecimal bigExpense3 = new BigDecimal(newCoststatistics.getExpense3()).add(new BigDecimal(map.getValue().getCost3()));
                map.getValue().setCost3(bigExpense3.setScale(2, RoundingMode.HALF_UP).toString());
                // 外注费用4月
                BigDecimal bigExpense4 = new BigDecimal(newCoststatistics.getExpense4()).add(new BigDecimal(map.getValue().getCost4()));
                map.getValue().setCost4(bigExpense4.setScale(2, RoundingMode.HALF_UP).toString());
                // 外注费用5月
                BigDecimal bigExpense5 = new BigDecimal(newCoststatistics.getExpense5()).add(new BigDecimal(map.getValue().getCost5()));
                map.getValue().setCost5(bigExpense5.setScale(2, RoundingMode.HALF_UP).toString());
                // 外注费用6月
                BigDecimal bigExpense6 = new BigDecimal(newCoststatistics.getExpense6()).add(new BigDecimal(map.getValue().getCost6()));
                map.getValue().setCost6(bigExpense6.setScale(2, RoundingMode.HALF_UP).toString());
                // 外注费用7月
                BigDecimal bigExpense7 = new BigDecimal(newCoststatistics.getExpense7()).add(new BigDecimal(map.getValue().getCost7()));
                map.getValue().setCost7(bigExpense7.setScale(2, RoundingMode.HALF_UP).toString());
                // 外注费用8月
                BigDecimal bigExpense8 = new BigDecimal(newCoststatistics.getExpense8()).add(new BigDecimal(map.getValue().getCost8()));
                map.getValue().setCost8(bigExpense8.setScale(2, RoundingMode.HALF_UP).toString());
                // 外注费用9月
                BigDecimal bigExpense9 = new BigDecimal(newCoststatistics.getExpense9()).add(new BigDecimal(map.getValue().getCost9()));
                map.getValue().setCost9(bigExpense9.setScale(2, RoundingMode.HALF_UP).toString());
                // 外注费用10月
                BigDecimal bigExpense10 = new BigDecimal(newCoststatistics.getExpense10()).add(new BigDecimal(map.getValue().getCost10()));
                map.getValue().setCost10(bigExpense10.setScale(2, RoundingMode.HALF_UP).toString());
                // 外注费用11月
                BigDecimal bigExpense11 = new BigDecimal(newCoststatistics.getExpense11()).add(new BigDecimal(map.getValue().getCost11()));
                map.getValue().setCost11(bigExpense11.setScale(2, RoundingMode.HALF_UP).toString());
                // 外注费用12月
                BigDecimal bigExpense12 = new BigDecimal(newCoststatistics.getExpense12()).add(new BigDecimal(map.getValue().getCost12()));
                map.getValue().setCost12(bigExpense12.setScale(2, RoundingMode.HALF_UP).toString());
            }
            // 费用合计费用
            map.getValue().setTotalcostf(new BigDecimal(bigManhourCount).setScale(2, RoundingMode.HALF_UP).toString());
            // 费用合计工数
            map.getValue().setTotalmanhourf(new BigDecimal(bigTormbCount).setScale(2, RoundingMode.HALF_UP).toString());
        }
        // add gbb 210914 BP社统计添加添加费用列 end
        result.put("company", new ArrayList<>(companyMap.values()));

        //region  add_qhr_20210901 添加bp社统计费用数据
        BpCompanyCost bpCompanyCost = new BpCompanyCost();
        bpCompanyCost.setGroup_id(groupid);
        bpCompanyCost.setYear(years);
        List<BpCompanyCost> bpCompanyCostList = bpCompanyCostMapper.select(bpCompanyCost);
        HashMap<String, BpCompanyCost> bpmap = new HashMap<>();
        for (BpCompanyCost companyCost : bpCompanyCostList) {
            bpmap.put(companyCost.getBpcompany(), companyCost);
        }
        for (CompanyStatistics value : companyMap.values()) {
            String BpCompany = value.getBpcompany();
            BpCompanyCost bpCompanyCost1 = bpmap.get(BpCompany);
            if (bpCompanyCost1 == null) {
                continue;
            }
//            if (StringUtils.isNullOrEmpty(value.getManhour1f())) {
//                value.setBpcompany(bpCompanyCost1.getBpcompany());
//            }
//            if (StringUtils.isNullOrEmpty(sameOr.getSex())) {
//                sameOr.setSex(sameOr1.getSex());
//            }
        }
        //endregion  add_qhr_20210901 添加bp社统计费用数据

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

        if (month >= 1 && month <= 3) {
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
        if(groupid.equals("all"))
        {
            groupid = "";
        }
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
//            Map<String, Double> trip = (Map<String, Double>) result.get("trip");
//            Map<String, Double> asset = (Map<String, Double>) result.get("asset");

            Calendar now = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("MM月");
            now.setTime(sdf.parse("04月"));
            //日期赋值
            for (int j = 1; j <= 12; j++) {
                // sheet1.getRow(1).getCell(2 * j + 1).setCellValue(sdf.format(now.getTime()))
                sheet1.getRow(1).getCell(3 +  4 * (j - 1)).setCellValue(sdf.format(now.getTime()));
                now.set(Calendar.MONTH, now.get(Calendar.MONTH) + 1);
            }
            //
            Map<String, Double> totalCostMap = new HashMap<>();
            //Map<String, Double> totalCostMap1 = new HashMap<>();
            //将数据放入Excel
            int i = 4;
            for (CompanyStatistics c : companyStatisticsList) {
                //创建工作表的行
                XSSFRow row = sheet1.createRow(i);

                for (int k = 1; k <= 13; k++) {
                    double manhour = 0;
                    double cost = 0;
                    double manhourf = 0;
                    double costf = 0;
                    String property = "manhour" + k;
                    String propertyC = "cost" + k;
                    String propertyf = "manhour" + k + "f";
                    String propertyCf = "cost" + k + "f";
                    if (k > 12) {
                        property = "totalmanhours";
                        propertyC = "totalcost";
                        propertyf = "totalmanhourf";
                        propertyCf = "totalcostf";
                    }
                    try {

                        manhour = Double.parseDouble(BeanUtils.getProperty(c, property));
                        cost = Double.parseDouble(BeanUtils.getProperty(c, propertyC));
                        manhourf = Double.parseDouble(BeanUtils.getProperty(c, propertyf));
                        costf = Double.parseDouble(BeanUtils.getProperty(c, propertyCf));

                        int colIndex = getColIndex41Month(k);
                        row.createCell(colIndex).setCellValue(manhour);
                        row.createCell(colIndex + 1).setCellValue(cost);
                        row.createCell(colIndex + 2).setCellValue(manhourf);
                        row.createCell(colIndex + 3).setCellValue(costf);

                        totalCostMap.put(property, totalCostMap.getOrDefault(property, 0.0) + manhour);
                        totalCostMap.put(propertyC, totalCostMap.getOrDefault(propertyC, 0.0) + cost);
                        totalCostMap.put(propertyf, totalCostMap.getOrDefault(propertyf, 0.0) + manhourf);
                        totalCostMap.put(propertyCf, totalCostMap.getOrDefault(propertyCf, 0.0) + costf);
                    } catch (Exception e) {
                    }

                }

                row.createCell(1).setCellValue(i - 3);
                Supplierinfor ls = supplierinforMapper.selectByPrimaryKey(c.getBpcompany());
                if (ls != null) {
                    row.createCell(2).setCellValue(ls.getSupchinese());
                }
//                row.createCell(27).setCellValue(c.getTotalmanhours());
//                row.createCell(28).setCellValue(c.getTotalcost());

                //totalCostMap.put("totalmanhours", totalCostMap.getOrDefault("totalmanhours", 0.0) + getDoubleValue(c, "totalmanhours"));
                //totalCostMap.put("totalcost", totalCostMap.getOrDefault("totalcost", 0.0) + getDoubleValue(c, "totalcost"));
                i++;
            }
            int rowIndex = companyStatisticsList.size() + 3;
            //合计行
            XSSFRow rowT = sheet1.createRow(1 + rowIndex);
            rowT.createCell(1).setCellValue("合计");
//            //経費除きの平均単価(人月)
//            XSSFRow rowT1 = sheet1.createRow(2 + rowIndex);
//            rowT1.createCell(1).setCellValue("経費除きの平均単価(人月)");
//            //出張経費(元)
//            XSSFRow rowT2 = sheet1.createRow(3 + rowIndex);
//            rowT2.createCell(1).setCellValue("出張経費(元)");
//            //設備経費(元)
//            XSSFRow rowT3 = sheet1.createRow(4 + rowIndex);
//            rowT3.createCell(1).setCellValue("設備経費(元)");
//            //外注総合費用合計(元)
//            XSSFRow rowT4 = sheet1.createRow(5 + rowIndex);
//            rowT4.createCell(1).setCellValue("外注総合費用合計(元)");

            CellRangeAddress region = new CellRangeAddress(1 + rowIndex, 1 + rowIndex, 1, 2);
            sheet1.addMergedRegion(region);
            // 合计28列，后数4行，两两合并
//            for (int r = rowIndex + 2; r < rowIndex + 6; r++) {
//                for (i = 1; i <= 28; i = i + 2) {
//                    CellRangeAddress region1 = new CellRangeAddress(r, r, i, i + 1);
//                    sheet1.addMergedRegion(region1);
//                }
//            }
            // 设置值
            for (int k = 1; k <= 13; k++) {
                String property = "manhour" + k;
                String propertyC = "cost" + k;
                String propertyf = "manhour" + k + "f";
                String propertyCf = "cost" + k + "f";
                if (k > 12) {
                    property = "totalmanhours";
                    propertyC = "totalcost";
                    propertyf = "totalmanhourf";
                    propertyCf = "totalcostf";
                }
                int colIndex = getColIndex41Month(k);
//                double tripflg = 0.0;
//                double assetflg = 0.0;
//                double totalCostMapflg = 0.0;
//                if (trip.size() > 0) {
//                    //出張経費(元)
//                    rowT2.createCell(colIndex).setCellValue(trip.get(propertyC));
//                    tripflg = trip.get(propertyC);
//                } else {
//                    rowT2.createCell(colIndex).setCellValue(0.0);
//                }
//                if (asset.size() > 0) {
//                    //設備経費(元)
//                    rowT3.createCell(colIndex).setCellValue(asset.get(propertyC));
//                    assetflg = asset.get(propertyC);
//                } else {
//                    rowT3.createCell(colIndex).setCellValue(0.0);
//                }
                if (totalCostMap.size() > 0) {
                    //合计行
                    rowT.createCell(colIndex).setCellValue(totalCostMap.get(property));
                    rowT.createCell(colIndex + 1).setCellValue(totalCostMap.get(propertyC));
                    rowT.createCell(colIndex + 2).setCellValue(totalCostMap.get(propertyf));
                    rowT.createCell(colIndex + 3).setCellValue(totalCostMap.get(propertyCf));
                    //経費除きの平均単価(人月)
//                    if (totalCostMap.get(property) == 0) {
//                        Double avg = 0.00;
//                        rowT1.createCell(colIndex).setCellValue(avg);
//                        totalCostMapflg = totalCostMap.get(propertyC);
//                    } else {
//                        Double avg = totalCostMap.get(propertyC) / totalCostMap.get(property);
//                        DecimalFormat dlf = new DecimalFormat("#0.00");
//                         avg = Double.valueOf(dlf.format(avg));
//                         rowT1.createCell(colIndex).setCellValue(avg);
//                        totalCostMapflg = totalCostMap.get(propertyC);
//                    }

                } else {
                    rowT.createCell(colIndex).setCellValue(0.0);
                    rowT.createCell(colIndex + 1).setCellValue(0.0);
                    // rowT1.createCell(colIndex).setCellValue(0.0);
                }
                //外注総合費用合計(元)
                // Double totalAll = totalCostMapflg + tripflg + assetflg;
                // rowT4.createCell(colIndex).setCellValue(totalAll);
            }

        } catch (Exception e) {
            throw new LogicalException(e.getMessage());
        }
    }


    private void getReportWork2(XSSFSheet sheet1, String groupid, String years) throws LogicalException {
        try {
            Calendar now = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("MM月");
            now.setTime(sdf.parse("04月"));
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

                //4月
                int colIndex1 = getColIndex4Month(4);
                row.createCell(colIndex1).setCellValue(Double.valueOf(c.getAprilgn()));
                row.createCell(colIndex1 + 1).setCellValue(Double.valueOf(c.getAprilgw()));
                lineTotalN += Double.valueOf(c.getAprilgn());
                lineTotalW += Double.valueOf(c.getAprilgw());
                totalCostMap.put("N4", totalCostMap.getOrDefault("N4", 0.0) + Double.valueOf(c.getAprilgn()));
                totalCostMap.put("W4", totalCostMap.getOrDefault("W4", 0.0) + Double.valueOf(c.getAprilgw()));
                //5月
                int colIndex2 = getColIndex4Month(5);
                row.createCell(colIndex2).setCellValue(Double.valueOf(c.getMaygn()));
                row.createCell(colIndex2 + 1).setCellValue(Double.valueOf(c.getMaygw()));
                lineTotalN += Double.valueOf(c.getMaygn());
                lineTotalW += Double.valueOf(c.getMaygw());
                totalCostMap.put("N5", totalCostMap.getOrDefault("N5", 0.0) + Double.valueOf(c.getMaygn()));
                totalCostMap.put("W5", totalCostMap.getOrDefault("W5", 0.0) + Double.valueOf(c.getMaygw()));
                //6月
                int colIndex3 = getColIndex4Month(6);
                row.createCell(colIndex3).setCellValue(Double.valueOf(c.getJunegn()));
                row.createCell(colIndex3 + 1).setCellValue(Double.valueOf(c.getJunegw()));
                lineTotalN += Double.valueOf(c.getJunegn());
                lineTotalW += Double.valueOf(c.getJunegw());
                totalCostMap.put("N6", totalCostMap.getOrDefault("N6", 0.0) + Double.valueOf(c.getJunegn()));
                totalCostMap.put("W6", totalCostMap.getOrDefault("W6", 0.0) + Double.valueOf(c.getJunegw()));
                //7月
                int colIndex4 = getColIndex4Month(7);
                row.createCell(colIndex4).setCellValue(Double.valueOf(c.getJulygn()));
                row.createCell(colIndex4 + 1).setCellValue(Double.valueOf(c.getJulygw()));
                lineTotalN += Double.valueOf(c.getJulygn());
                lineTotalW += Double.valueOf(c.getJulygw());
                totalCostMap.put("N7", totalCostMap.getOrDefault("N7", 0.0) + Double.valueOf(c.getJulygn()));
                totalCostMap.put("W7", totalCostMap.getOrDefault("W7", 0.0) + Double.valueOf(c.getJulygw()));
                //8月
                int colIndex5 = getColIndex4Month(8);
                row.createCell(colIndex5).setCellValue(Double.valueOf(c.getAugustgn()));
                row.createCell(colIndex5 + 1).setCellValue(Double.valueOf(c.getAugustgw()));
                lineTotalN += Double.valueOf(c.getAugustgn());
                lineTotalW += Double.valueOf(c.getAugustgw());
                totalCostMap.put("N8", totalCostMap.getOrDefault("N8", 0.0) + Double.valueOf(c.getAugustgn()));
                totalCostMap.put("W8", totalCostMap.getOrDefault("W8", 0.0) + Double.valueOf(c.getAugustgw()));
                //9月
                int colIndex6 = getColIndex4Month(9);
                row.createCell(colIndex6).setCellValue(Double.valueOf(c.getSeptembergn()));
                row.createCell(colIndex6 + 1).setCellValue(Double.valueOf(c.getSeptembergw()));
                lineTotalN += Double.valueOf(c.getSeptembergn());
                lineTotalW += Double.valueOf(c.getSeptembergw());
                totalCostMap.put("N9", totalCostMap.getOrDefault("N9", 0.0) + Double.valueOf(c.getSeptembergn()));
                totalCostMap.put("W9", totalCostMap.getOrDefault("W9", 0.0) + Double.valueOf(c.getSeptembergw()));
                //10月
                int colIndex7 = getColIndex4Month(10);
                row.createCell(colIndex7).setCellValue(Double.valueOf(c.getOctobergn()));
                row.createCell(colIndex7 + 1).setCellValue(Double.valueOf(c.getOctobergw()));
                lineTotalN += Double.valueOf(c.getOctobergn());
                lineTotalW += Double.valueOf(c.getOctobergw());
                totalCostMap.put("N10", totalCostMap.getOrDefault("N10", 0.0) + Double.valueOf(c.getOctobergn()));
                totalCostMap.put("W10", totalCostMap.getOrDefault("W10", 0.0) + Double.valueOf(c.getOctobergw()));
                //11月
                int colIndex8 = getColIndex4Month(11);
                row.createCell(colIndex8).setCellValue(Double.valueOf(c.getNovembergn()));
                row.createCell(colIndex8 + 1).setCellValue(Double.valueOf(c.getNovembergw()));
                lineTotalN += Double.valueOf(c.getNovembergn());
                lineTotalW += Double.valueOf(c.getNovembergw());
                totalCostMap.put("N11", totalCostMap.getOrDefault("N11", 0.0) + Double.valueOf(c.getNovembergn()));
                totalCostMap.put("W11", totalCostMap.getOrDefault("W11", 0.0) + Double.valueOf(c.getNovembergw()));
                //12月
                int colIndex9 = getColIndex4Month(12);
                row.createCell(colIndex9).setCellValue(Double.valueOf(c.getDecembergn()));
                row.createCell(colIndex9 + 1).setCellValue(Double.valueOf(c.getDecembergw()));
                lineTotalN += Double.valueOf(c.getDecembergn());
                lineTotalW += Double.valueOf(c.getDecembergw());
                totalCostMap.put("N12", totalCostMap.getOrDefault("N12", 0.0) + Double.valueOf(c.getDecembergn()));
                totalCostMap.put("W12", totalCostMap.getOrDefault("W12", 0.0) + Double.valueOf(c.getDecembergw()));
                //1月
                int colIndex10 = getColIndex4Month(1);
                row.createCell(colIndex10).setCellValue(Double.valueOf(c.getJanuarygn()));
                row.createCell(colIndex10 + 1).setCellValue(Double.valueOf(c.getJanuarygw()));
                lineTotalN += Double.valueOf(c.getJanuarygn());
                lineTotalW += Double.valueOf(c.getJanuarygw());
                totalCostMap.put("N1", totalCostMap.getOrDefault("N1", 0.0) + Double.valueOf(c.getJanuarygn()));
                totalCostMap.put("W1", totalCostMap.getOrDefault("W1", 0.0) + Double.valueOf(c.getJanuarygw()));
                //2月
                int colIndex11 = getColIndex4Month(2);
                row.createCell(colIndex11).setCellValue(Double.valueOf(c.getFebruarygn()));
                row.createCell(colIndex11 + 1).setCellValue(Double.valueOf(c.getFebruarygw()));
                lineTotalN += Double.valueOf(c.getFebruarygn());
                lineTotalW += Double.valueOf(c.getFebruarygw());
                totalCostMap.put("N2", totalCostMap.getOrDefault("N2", 0.0) + Double.valueOf(c.getFebruarygn()));
                totalCostMap.put("W2", totalCostMap.getOrDefault("W2", 0.0) + Double.valueOf(c.getFebruarygw()));
                //3月
                int colIndex12 = getColIndex4Month(3);
                row.createCell(colIndex12).setCellValue(Double.valueOf(c.getMarchgn()));
                row.createCell(colIndex12 + 1).setCellValue(Double.valueOf(c.getMarchgw()));
                lineTotalN += Double.valueOf(c.getMarchgn());
                lineTotalW += Double.valueOf(c.getMarchgw());
                totalCostMap.put("N3", totalCostMap.getOrDefault("N3", 0.0) + Double.valueOf(c.getMarchgn()));
                totalCostMap.put("W3", totalCostMap.getOrDefault("W3", 0.0) + Double.valueOf(c.getMarchgw()));

                TotalN +=lineTotalN;
                TotalW +=lineTotalW;

                row.createCell(1).setCellValue(i - 2);
                row.createCell(2).setCellValue(c.getSuppliername());
                row.createCell(27).setCellValue(lineTotalN);
                row.createCell(28).setCellValue(lineTotalW);
                i++;
            }
            for (int k = 1; k <= 12; k++) {
                int colIndex = getColIndex4Month(k);
                String propertyN = "N" + k;
                String propertyW = "W" + k;
                rowT.createCell(colIndex).setCellValue(totalCostMap.get(propertyN));
                rowT.createCell(colIndex + 1).setCellValue(totalCostMap.get(propertyW));
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
            SimpleDateFormat sdf = new SimpleDateFormat("MM月");
            now.setTime(sdf.parse("04月"));
            //日期赋值
            for (int j = 1; j <= 12; j++) {
                sheet.getRow(2).getCell(j + 2).setCellValue(sdf.format(now.getTime()));
                now.set(Calendar.MONTH, now.get(Calendar.MONTH) + 1);
            }
            List<bpSum3Vo> list = this.getWorkerCounts(groupid, years);
            int Total = 0;
            //将数据放入Excel
            int i = 4;
            Map<String, Double> totalCostMap = new HashMap<>();
            SimpleDateFormat sf1ym = new SimpleDateFormat("yyyy-MM");
//            for (bpSum3Vo c : list) {
//                //创建工作表的行
//                XSSFRow row = sheet.createRow(i);
//                Double lineTotalManhour = 0.0;
//                int month = Integer.valueOf(sf1ym.format(sf1ym.parse(c.getDate())).substring(5, 7));
//                for (int k = 1; k <= 12; k++) {
//                    double manhour = 0;
//                    String property = "manhour" + k;
//                    int colIndex = k > 12 ? k + 2 : (getColIndex4Month(k) - 3) / 2 + 3;
//                    if (month == k) {
//                        manhour = Double.valueOf(c.getCounts());
//                    }
//                    lineTotalManhour += manhour;
//                    lineTotal += manhour;
//                    row.createCell(colIndex).setCellValue(manhour);
//                    totalCostMap.put(property, totalCostMap.getOrDefault(property, 0.0) + manhour);
//                }
//                row.createCell(1).setCellValue(i - 3);
//                row.createCell(2).setCellValue(c.getSUPPLIERNAME());
//                row.createCell(15).setCellValue(lineTotalManhour);
//                totalCostMap.put("totalmanhours", totalCostMap.getOrDefault("totalmanhours", 0.0) + getDoubleValue(c, "totalmanhours"));
//                i++;
//            }

            for (bpSum3Vo c : list) {
                //创建工作表的行
                XSSFRow row = sheet.createRow(i);

                //行合计
                int lineTotal = 0;

                //4月
                int colIndex1 = (getColIndex4Month(4) - 3) / 2 + 3;
                row.createCell(colIndex1).setCellValue(Integer.valueOf(c.getApril()));
                lineTotal += Integer.valueOf(c.getApril());
                totalCostMap.put("N4", totalCostMap.getOrDefault("N4", 0.0) + Integer.valueOf(c.getApril()));
                //5月
                int colIndex2 = (getColIndex4Month(5) - 3) / 2 + 3;
                row.createCell(colIndex2).setCellValue(Integer.valueOf(c.getMay()));
                lineTotal += Integer.valueOf(c.getMay());
                totalCostMap.put("N5", totalCostMap.getOrDefault("N5", 0.0) + Integer.valueOf(c.getMay()));
                //6月
                int colIndex3 = (getColIndex4Month(6) - 3) / 2 + 3;
                row.createCell(colIndex3).setCellValue(Integer.valueOf(c.getJune()));
                lineTotal += Integer.valueOf(c.getJune());
                totalCostMap.put("N6", totalCostMap.getOrDefault("N6", 0.0) + Integer.valueOf(c.getJune()));
                //7月
                int colIndex4 = (getColIndex4Month(7) - 3) / 2 + 3;
                row.createCell(colIndex4).setCellValue(Integer.valueOf(c.getJuly()));
                lineTotal += Integer.valueOf(c.getJuly());
                totalCostMap.put("N7", totalCostMap.getOrDefault("N7", 0.0) + Integer.valueOf(c.getJuly()));
                //8月
                int colIndex5 = (getColIndex4Month(8) - 3) / 2 + 3;
                row.createCell(colIndex5).setCellValue(Integer.valueOf(c.getAugust()));
                lineTotal += Integer.valueOf(c.getAugust());
                totalCostMap.put("N8", totalCostMap.getOrDefault("N8", 0.0) + Integer.valueOf(c.getAugust()));
                //9月
                int colIndex6 = (getColIndex4Month(9) - 3) / 2 + 3;
                row.createCell(colIndex6).setCellValue(Integer.valueOf(c.getSeptember()));
                lineTotal += Integer.valueOf(c.getSeptember());
                totalCostMap.put("N9", totalCostMap.getOrDefault("N9", 0.0) + Integer.valueOf(c.getSeptember()));
                //10月
                int colIndex7 = (getColIndex4Month(10) - 3) / 2 + 3;
                row.createCell(colIndex7).setCellValue(Integer.valueOf(c.getOctober()));
                lineTotal += Integer.valueOf(c.getOctober());
                totalCostMap.put("N10", totalCostMap.getOrDefault("N10", 0.0) + Integer.valueOf(c.getOctober()));
                //11月
                int colIndex8 = (getColIndex4Month(11) - 3) / 2 + 3;
                row.createCell(colIndex8).setCellValue(Integer.valueOf(c.getNovember()));
                lineTotal += Integer.valueOf(c.getNovember());
                totalCostMap.put("N11", totalCostMap.getOrDefault("N11", 0.0) + Integer.valueOf(c.getNovember()));
                //12月
                int colIndex9 = (getColIndex4Month(12) - 3) / 2 + 3;
                row.createCell(colIndex9).setCellValue(Integer.valueOf(c.getDecember()));
                lineTotal += Integer.valueOf(c.getDecember());
                totalCostMap.put("N12", totalCostMap.getOrDefault("N12", 0.0) + Integer.valueOf(c.getDecember()));
                //1月
                int colIndex10 = (getColIndex4Month(1) - 3) / 2 + 3;
                row.createCell(colIndex10).setCellValue(Integer.valueOf(c.getJanuary()));
                lineTotal += Integer.valueOf(c.getJanuary());
                totalCostMap.put("N1", totalCostMap.getOrDefault("N1", 0.0) + Integer.valueOf(c.getJanuary()));
                //2月
                int colIndex11 = (getColIndex4Month(2) - 3) / 2 + 3;
                row.createCell(colIndex11).setCellValue(Integer.valueOf(c.getFebruary()));
                lineTotal += Integer.valueOf(c.getFebruary());
                totalCostMap.put("N2", totalCostMap.getOrDefault("N2", 0.0) + Integer.valueOf(c.getFebruary()));
                //3月
                int colIndex12 = (getColIndex4Month(3) - 3) / 2 + 3;
                row.createCell(colIndex12).setCellValue(Integer.valueOf(c.getMarch()));
                lineTotal += Integer.valueOf(c.getMarch());
                totalCostMap.put("N3", totalCostMap.getOrDefault("N3", 0.0) + Integer.valueOf(c.getMarch()));

                Total += lineTotal;

                row.createCell(1).setCellValue(i - 3);
                row.createCell(2).setCellValue(c.getSuppliername());
                row.createCell(15).setCellValue(lineTotal);
                i++;
            }

            int rowIndex = list.size() + 3;
            //合计行
            XSSFRow rowT = sheet.createRow(1 + rowIndex);
            rowT.createCell(1).setCellValue("合计");
            CellRangeAddress region = new CellRangeAddress(1 + rowIndex, 1 + rowIndex, 1, 2);
            sheet.addMergedRegion(region);
//            // 设置值
            for (int k = 1; k <= 12; k++) {
                int colIndex = (getColIndex4Month(k) - 3) / 2 + 3;
                String propertyN = "N" + k;
                String propertyW = "W" + k;
                rowT.createCell(colIndex).setCellValue(totalCostMap.get(propertyN));
            }
            int colIndexTol = getColIndex4Month(10);
            rowT.createCell(colIndexTol).setCellValue(Total);
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

    private int getColIndex41Month(int month) {
        if (month <= 3) {
            return 4 * (month - 1) + 39;
        } else if (month > 12) {
            return 51;
        } else {
            return 4 * (month - 4) + 3;
        }
    }

    @Override
    public Map<String, Object> downloadPdf(String dates) throws Exception {
        Map<String, Object> resultRet = new HashMap<>();

        String strYears = dates.substring(0,4);
        int intMonths = Integer.valueOf(dates.substring(5,7));
        if(intMonths == 1 || intMonths == 2 || intMonths == 3){
            strYears = String.valueOf(Integer.valueOf(strYears) - 1);
        }
        List<DepartmentVo> allDepartment = orgTreeService.getAllDepartment();
        JSONObject mapJb = new JSONObject();
        List<String> supchinese = new ArrayList<>();

        Map<String, Double> totalCostMapx = new HashMap<>();

        if(allDepartment.size() > 0){
            // 预提-工数
            String property = "manhour" + intMonths;
            // 预提-费用
            String propertyC = "cost" + intMonths;
            // 费用-工数
            String propertyf = "manhour" + intMonths + "f";
            // 费用-费用
            String propertyCf = "cost" + intMonths + "f";
            // 预提-工数总额-部门
            String propertyCountD = "totalmanhoursD";
            // 预提-工数总额-公司
            String propertyCountP = "totalmanhoursP";
            // 预提-费用总额-部门
            String propertyCCountD = "totalcostD";
            // 预提-费用总额-公司
            String propertyCCountC = "totalcostC";
            // 费用-工数总额-部门
            String propertyfCountD = "totalmanhourf";
            // 费用-工数总额-公司
            String propertyfCountC = "totalmanhourfC";
            // 费用-费用总额-部门
            String propertyCfCountD = "totalcostf";
            // 费用-费用总额-公司
            String propertyCfCountC = "totalcostfC";

            // 循环部门
            for(DepartmentVo depVo : allDepartment){
                totalCostMapx.put("manhour" + depVo.getDepartmentEn(), 0.0);
                totalCostMapx.put("cost" + depVo.getDepartmentEn(), 0.0);
                totalCostMapx.put("manhourf" + depVo.getDepartmentEn(), 0.0);
                totalCostMapx.put("costf" + depVo.getDepartmentEn(), 0.0);
                // 单个部门数据
                Map<String, Object> resultRetAll = new HashMap<>();
                double manhourCount = 0;
                double costCount = 0;
                double manhourfCount = 0;
                double  costfCount = 0;
                //获取数据
                Map<String, Object> result = getCosts(depVo.getDepartmentId(), strYears);

                List<CompanyStatistics> companyStatisticsList = (List<CompanyStatistics>) result.get("company");

                for (CompanyStatistics c : companyStatisticsList) {
                    Map<String, Double> totalCostMap = new HashMap<>();

                    //预提/费用计算
                    double manhour = Double.parseDouble(BeanUtils.getProperty(c, property));
                    double cost = Double.parseDouble(BeanUtils.getProperty(c, propertyC));
                    double manhourf = Double.parseDouble(BeanUtils.getProperty(c, propertyf));
                    double  costf = Double.parseDouble(BeanUtils.getProperty(c, propertyCf));
                    // 合计值
                    manhourCount = manhourCount + manhour;
                    costCount = costCount + cost;
                    manhourfCount = manhourfCount + manhourf;
                    costfCount = costfCount + costf;

                    totalCostMap.put("manhour", totalCostMap.getOrDefault(property, 0.0) + manhour);
                    totalCostMap.put("cost", totalCostMap.getOrDefault(propertyC, 0.0) + cost);
                    totalCostMap.put("manhourf", totalCostMap.getOrDefault(propertyf, 0.0) + manhourf);
                    totalCostMap.put("costf", totalCostMap.getOrDefault(propertyCf, 0.0) + costf);


                    Supplierinfor ls = supplierinforMapper.selectByPrimaryKey(c.getBpcompany());
                    if (ls != null) {
                        resultRetAll.put(ls.getSupchinese(),totalCostMap);
                        if(supchinese.indexOf(ls.getSupchinese()) < 0){
                            supchinese.add(ls.getSupchinese());
                            resultRet.put(ls.getSupchinese(),new ArrayList<>());
                        }
                    }
                }
                resultRetAll.put("manhourCount", manhourCount);
                resultRetAll.put("costCount", costCount);
                resultRetAll.put("manhourfCount", manhourfCount);
                resultRetAll.put("costfCount", costfCount);
                mapJb.put(depVo.getDepartmentEn(), resultRetAll);
            }
        }
        for (Map.Entry<String, Object> map : mapJb.entrySet()){
            String mapkry = map.getKey();
            Object mapkryValue = map.getValue();
//            for (Object resultRetmap : mapkryValue){
//                resultRetmap.setValue(totalCostMapx);
//                String b = "1";
//            }
            for (Map.Entry<String, Object> resultRetmap : resultRet.entrySet()){
                resultRetmap.setValue(totalCostMapx);
            }
        }
//        Iterator iter = mapJb.entrySet().iterator();
//        while (iter.hasNext()) {
//            Map.Entry entry = (Map.Entry) iter.next();
//            System.out.println(entry.getKey().toString());
//            System.out.println(entry.getValue().toString());
//        }
        return resultRet;
    }
}
