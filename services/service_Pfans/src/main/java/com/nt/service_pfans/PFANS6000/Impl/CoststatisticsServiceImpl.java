package com.nt.service_pfans.PFANS6000.Impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_Pfans.PFANS6000.*;
import com.nt.service_Org.DictionaryService;
import com.nt.service_Org.mapper.DictionaryMapper;
import com.nt.service_pfans.PFANS6000.CoststatisticsService;
import com.nt.service_pfans.PFANS6000.mapper.*;
import com.nt.utils.ApiResult;
import com.nt.utils.LogicalException;
import com.nt.utils.StringUtils;
import com.nt.utils.dao.TokenModel;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bytedeco.javacpp.presets.opencv_core;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class CoststatisticsServiceImpl implements CoststatisticsService {

    @Autowired
    private CoststatisticsMapper coststatisticsMapper;

    @Autowired
    private VariousfundsMapper variousfundsMapper;

    @Autowired
    private PricesetMapper pricesetMapper;

    @Autowired
    private PricesetGroupMapper pricesetGroupMapper;

    @Autowired
    private ExpatriatesinforMapper expatriatesinforMapper;

    @Autowired
    private DictionaryMapper dictionaryMapper;

    @Override
    public List<Coststatistics> getCostList(Coststatistics coststatistics) throws Exception {
        return coststatisticsMapper.select(coststatistics);
    }

    @Override
    public List<Coststatistics> getCostListBygroupid(String groupid) throws Exception {

        return coststatisticsMapper.selectBygroupid(groupid);
    }

    @Override
    public Integer insertCoststatistics(String groupid,Coststatistics coststatistics, TokenModel tokenModel) throws Exception {
        Calendar calendar = Calendar.getInstance();
        int year = 0;
        int month = calendar.get(Calendar.MONTH);
        if(month >= 1 && month <= 3) {
            year = calendar.get(Calendar.YEAR) - 1;
        }else {
            year = calendar.get(Calendar.YEAR);
        }


        List<Coststatistics> allCostList = getCostList(groupid,coststatistics, tokenModel);

        for (Coststatistics c : allCostList) {

            coststatistics.setBpname(c.getBpname());
            coststatistics.setYears(String.valueOf(year).trim());
            coststatistics.setGroupid(groupid);
            coststatisticsMapper.delete(coststatistics);
        }

        int insertCount = 0;
        if ( allCostList.size() > 0 ) {
                insertCount = coststatisticsMapper.insertAll(allCostList);
            }
        return insertCount;
    }

    private List<Coststatistics> getCostList(String groupid,Coststatistics coststatistics, TokenModel tokenModel) throws Exception {
        //获取经费
        Variousfunds variousfunds = new Variousfunds();
//        variousfunds.setOwner(tokenModel.getUserId());
        Calendar calendar = Calendar.getInstance();
        int year = 0;
        int month = calendar.get(Calendar.MONTH);
        if(month >= 1 && month <= 3) {
            year = calendar.get(Calendar.YEAR) - 1;
        }else {
            year = calendar.get(Calendar.YEAR);
        }
        List<Variousfunds> allVariousfunds = variousfundsMapper.selectBygroupid(groupid, String.valueOf(year));
        Map<String, Double> variousfundsMap = new HashMap<String, Double>();
        for ( Variousfunds v : allVariousfunds ) {
            String plan = "";
            if(v.getPlmonthplan()!=null && !v.getPlmonthplan().isEmpty())
            {
                Dictionary dictionary =new Dictionary();
                dictionary = dictionaryMapper.selectByPrimaryKey(v.getPlmonthplan().trim());
                if(dictionary!=null)
                {
                    plan = dictionary.getValue1().trim().replace("月","");
                }
            }
            String key = v.getBpplayer() + plan;
            Double value = 0.0;
            try {
                value = Double.parseDouble(v.getPayment().trim());
            } catch (Exception e) {}
            if ( variousfundsMap.containsKey(key) ) {
                value = value + variousfundsMap.get(key);
            }
            variousfundsMap.put(key, value);
        }

        Map<String, Double> pricesetMap = getUserPriceMapBygroupid(groupid, String.valueOf(year));

        // 获取公司名称
        Expatriatesinfor expatriatesinfor = new Expatriatesinfor();
        //expatriatesinfor.setGroup_id(groupid);
        List<Expatriatesinfor> companyList = expatriatesinforMapper.select(expatriatesinfor);
        Map<String, String> companyMap = new HashMap<String, String>();
        for ( Expatriatesinfor ex : companyList) {
            String key = ex.getExpatriatesinfor_id();
            String value = ex.getSupplierinfor_id();
            companyMap.put(key, value);
        }
//        Calendar calendar = Calendar.getInstance();
//        //calendar.add(Calendar.MONTH, -3);
//        int year = 0;
//        int month = calendar.get(Calendar.MONTH);
//        if(month >= 1 && month <= 3) {
//            year = calendar.get(Calendar.YEAR) - 1;
//        }else {
//            year = calendar.get(Calendar.YEAR);
//        }
        // 获取活用情报信息
        List<Coststatistics> allCostList = coststatisticsMapper.getCoststatisticsBygroupid(year, groupid);
        for ( Coststatistics c : allCostList ) {
            // 合计费用
            double totalmanhours = 0;
            // 合计工数
            double totalcost = 0;

            for ( int i = 1 ; i <= 12; i++ ) {
                // 单价
                double price = 0;
                String priceKey = c.getBpname() + "price" + i;
                if ( pricesetMap.containsKey(priceKey) ) {
                    price = pricesetMap.get(priceKey);
                }
                BeanUtils.setProperty(c, "price" +i, price);
                double manhour = 0;
                String property = "manhour" + i;
                try {
//                    if(price !=0)
//                    {
                        manhour = Double.parseDouble(BeanUtils.getProperty(c, property));
//                    }
                } catch (Exception e) {}
                double cost = price * manhour;
                BeanUtils.setProperty(c, "cost" + i, String.format("%.2f", cost));
                totalmanhours += manhour;
                totalcost += cost;
                if ( i%3 ==0 ) {
                    // 经费处理
                    String variousKey = c.getBpname1() + i;
                    double various = 0;
                    if ( variousfundsMap.containsKey(variousKey) ) {
                        various = variousfundsMap.get(variousKey);
                    }
                    BeanUtils.setProperty(c, "expense" +i, String.format("%.2f", various));
                    BeanUtils.setProperty(c, "contract" +i, String.format("%.2f", various + totalcost));

                    BeanUtils.setProperty(c,"support" + i, i );
                    BeanUtils.setProperty(c, "totalcost" + i, String.format("%.2f", totalcost));
                    BeanUtils.setProperty(c, "totalmanhours" + i, String.format("%.2f", totalmanhours));
                    totalcost = 0;
                    totalmanhours = 0;
                }
            }
            //供应商名称
            String companyName = "";
            if ( companyMap.containsKey(c.getBpname()) ) {
                BeanUtils.setProperty(c, "bpcompany", companyMap.get(c.getBpname()));
            }
            c.setCoststatistics_id(UUID.randomUUID().toString());
            c.setSupport3("3");
            c.setSupport6("6");
            c.setSupport9("9");
            c.setSupport12("12");

            c.preInsert(tokenModel);
        }
        return allCostList;
    }


    @Override
    public Map<String, Double> getUserPriceMap() throws Exception {
        // 获取所有人的单价设定
        Calendar now = Calendar.getInstance();
        int year = 0;
        int month = now.get(Calendar.MONTH);
        if(month >= 1 && month <= 3) {
            year = now.get(Calendar.YEAR) - 1;
        }else {
            year = now.get(Calendar.YEAR);
        }

        List<Priceset> allPriceset = pricesetMapper.selectByYear(String.valueOf(year).trim());
        Map<String, Double> pricesetMap = new HashMap<String, Double>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //DateFormat df = DateFormat.getDateInstance();
        //Date startDate = sdf.parse(startTime);
        for ( Priceset priceset : allPriceset ) {
            //Date pointDate = sdf.parse(priceset.getAssesstime().substring(0, 10));

            //int startM = Integer.parseInt(priceset.getAssesstime().substring(5, 7));
            String totalUnit = "0";
            if(StringUtils.isNotBlank(priceset.getTotalunit())){
                totalUnit = priceset.getTotalunit().trim();
            }
//            String key = priceset.getUser_id() + "price" + startM;
//            Double value = 0.0;
//            value = Double.parseDouble(totalUnit);
//            pricesetMap.put(key, value);
            PricesetGroup pricesetGroup = new PricesetGroup();
            pricesetGroup.setPricesetgroup_id(priceset.getPricesetgroup_id());
            pricesetGroup = pricesetGroupMapper.selectOne(pricesetGroup);
            int i = 0;
            i = Integer.parseInt(pricesetGroup.getPd_date().substring(5, 7));
            String key = priceset.getUser_id() + "price" + i;
            Double value = 0.0;
            value = Double.parseDouble(totalUnit);
            pricesetMap.put(key, value);

//            for ( int i=1; i<=12; i++) {
//                    String key = priceset.getUser_id() + "price" + i;
//                    Double value = 0.0;
//                    value = Double.parseDouble(totalUnit);
//                    pricesetMap.put(key, value);
//            }
//            if ( pointDate.before(startDate) ) {
//
//            }else {
//                if(startM >= 1 && startM<=3){
//                    for (int k = startM; k<=3; k++) {
//                        pricesetMap.put(priceset.getUser_id() + "price" + k, Double.parseDouble(totalUnit));
//                    }
//                }else {
//                    for (int k = startM; k<=12; k++) {
//                        pricesetMap.put(priceset.getUser_id() + "price" + k, Double.parseDouble(totalUnit));
//                    }
//                    for (int k = 1; k<=3; k++) {
//                        pricesetMap.put(priceset.getUser_id() + "price" + k, Double.parseDouble(totalUnit));
//                    }
//                }
//            }

        }
        return pricesetMap;
    }

    @Override
    public Map<String, Double> getUserPriceMapBygroupid(String groupid,String years) throws Exception {
        // 获取所有人的单价设定
//        Calendar now = Calendar.getInstance();
//        int year = 0;
//        int month = now.get(Calendar.MONTH);
//        if(month >= 1 && month <= 3) {
//            year = now.get(Calendar.YEAR) - 1;
//        }else {
//            year = now.get(Calendar.YEAR);
//        }

        List<Priceset> allPriceset = pricesetMapper.selectBygroupid(Integer.valueOf(years),groupid);

        Map<String, Double> pricesetMap = new HashMap<String, Double>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for ( Priceset priceset : allPriceset ) {
            String totalUnit = "0";
            if(StringUtils.isNotBlank(priceset.getTotalunit())){
                totalUnit = priceset.getTotalunit().trim();
            }
            PricesetGroup pricesetGroup = new PricesetGroup();
            pricesetGroup.setPricesetgroup_id(priceset.getPricesetgroup_id());
            pricesetGroup = pricesetGroupMapper.selectOne(pricesetGroup);
            int i = 0;
            i = Integer.parseInt(pricesetGroup.getPd_date().substring(5, 7));
            String key = priceset.getUser_id() + "price" + i;
            Double value = 0.0;
            value = Double.parseDouble(totalUnit);
            pricesetMap.put(key, value);

        }
        return pricesetMap;
    }

    @Override
    public XSSFWorkbook downloadExcel(CoststatisticsVo coststatisticsVo, HttpServletRequest request, HttpServletResponse resp) throws LogicalException {
        try {
            // 导出文件
            List<Coststatistics> list = coststatisticsVo.getCoststatistics();
            InputStream in = null;
            FileOutputStream f = null;
            XSSFWorkbook work = null;
            //表格操作
            in = getClass().getClassLoader().getResourceAsStream("jxls_templates/feiyongtongji.xlsx");
            work = new XSSFWorkbook(in);
            XSSFSheet sheet1 = work.getSheetAt(0);
            //将数据放入Excel
            int i = 3;
            for (Coststatistics c : list) {
                //创建工作表的行
                XSSFRow row = sheet1.createRow(i);
                int j = 0;
                int r = 0;
                int t = 0;
                for (int k = 1; k <=12; k++) {
                    double manhour = 0;
                    double cost = 0;
                    double price = 0;
                    double totalmanhours = 0;
                    double totalcost = 0;
                    double expense = 0;
                    double contract = 0;
                    String property = "manhour" + k;
                    String propertyc = "cost" + k;
                    String propertyp = "price" + k;
                    String propertyM = "";
                    String propertyCo = "";
                    String propertyE = "";
                    String propertyCon = "";
                    if(k%3 == 0) {
                        propertyM = "totalmanhours" + k;
                        propertyCo = "totalcost" + k;
                        propertyE = "expense" + k;
                        propertyCon = "contract" + k;

                    }

                    try {
                        manhour = Double.parseDouble(BeanUtils.getProperty(c, property));
                        price = Double.parseDouble(BeanUtils.getProperty(c, propertyp));
                        cost = Double.parseDouble(BeanUtils.getProperty(c, propertyc));


                        if(k <= 3) {
                            row.createCell(51 + r).setCellValue(price);
                            row.createCell(52 + r).setCellValue(manhour);
                            row.createCell(53 + r).setCellValue(cost);
                            row.createCell(54 + r).setCellValue(c.getSupport3());
                            r = r + 4;
                        }

                        if(k >=4 && k<=6){
                            row.createCell(3 + j).setCellValue(price);
                            row.createCell(4 + j).setCellValue(manhour);
                            row.createCell(5 + j).setCellValue(cost);
                            row.createCell(6 + j).setCellValue(c.getSupport6());
                            j = j + 4;
                        }

                        if(k>=7 && k<=9) {
                            row.createCell(7 + j).setCellValue(price);
                            row.createCell(8 + j).setCellValue(manhour);
                            row.createCell(9 + j).setCellValue(cost);
                            row.createCell(10 + j).setCellValue(c.getSupport9());
                            j = j + 4;
                        }

                        if(k>=10 && k<=12) {
                            row.createCell(11 + j).setCellValue(price);
                            row.createCell(12 + j).setCellValue(manhour);
                            row.createCell(13 + j).setCellValue(cost);
                            row.createCell(14 + j).setCellValue(c.getSupport12());
                            j = j + 4;
                        }

                        if(k == 3) {
                            row.createCell(63).setCellValue(c.getTotalmanhours3());
                            row.createCell(64).setCellValue(c.getTotalcost3());
                            row.createCell(65).setCellValue(c.getExpense3());
                            row.createCell(66).setCellValue(c.getContract3());
                        }

                        totalmanhours = Double.parseDouble(BeanUtils.getProperty(c, propertyM));
                        totalcost = Double.parseDouble(BeanUtils.getProperty(c, propertyCo));
                        expense = Double.parseDouble(BeanUtils.getProperty(c, propertyE));
                        contract = Double.parseDouble(BeanUtils.getProperty(c, propertyCon));

                        if(k%3 == 0 && k>=6){
                            row.createCell(15 + t).setCellValue(totalmanhours);
                            row.createCell(16 + t).setCellValue(totalcost);
                            row.createCell(17 + t).setCellValue(expense);
                            row.createCell(18 + t).setCellValue(contract);
                            t = t + 16;
                        }

                    } catch (Exception e) {}

                }
                row.createCell(0).setCellValue(i - 2);
                row.createCell(1).setCellValue(c.getBpname());
                row.createCell(2).setCellValue(c.getBpcompany());
                i++;
            }

            // 写出到文件
            return work;
        } catch (Exception e) {
            throw new LogicalException(e.getMessage());
        }
    }
}
