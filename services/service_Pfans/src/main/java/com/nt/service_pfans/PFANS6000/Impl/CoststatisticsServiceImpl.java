package com.nt.service_pfans.PFANS6000.Impl;

import com.nt.dao_Pfans.PFANS6000.*;
import com.nt.service_pfans.PFANS6000.CoststatisticsService;
import com.nt.service_pfans.PFANS6000.mapper.CoststatisticsMapper;
import com.nt.service_pfans.PFANS6000.mapper.ExpatriatesinforMapper;
import com.nt.service_pfans.PFANS6000.mapper.PricesetMapper;
import com.nt.service_pfans.PFANS6000.mapper.VariousfundsMapper;
import com.nt.utils.ApiResult;
import com.nt.utils.LogicalException;
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
import java.net.URLEncoder;
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
    private ExpatriatesinforMapper expatriatesinforMapper;

    @Override
    public List<Coststatistics> getCostList(Coststatistics coststatistics) throws Exception {
        return coststatisticsMapper.select(coststatistics);
    }

    @Override
    public Integer insertCoststatistics(Coststatistics coststatistics, TokenModel tokenModel) throws Exception {
        coststatisticsMapper.delete(coststatistics);
        //获取经费
        Variousfunds variousfunds = new Variousfunds();
//        variousfunds.setOwner(tokenModel.getUserId());
        List<Variousfunds> allVariousfunds = variousfundsMapper.select(variousfunds);
        Map<String, Double> variousfundsMap = new HashMap<String, Double>();
        for ( Variousfunds v : allVariousfunds ) {
            String key = v.getBpplayer() + v.getPlmonthplan();
            Double value = 0.0;
            try {
                value = Double.parseDouble(v.getPayment().trim());
            } catch (Exception e) {}
            if ( variousfundsMap.containsKey(key) ) {
                value = value + variousfundsMap.get(key);
            }
            variousfundsMap.put(key, value);
        }
        // 获取所有人的单价设定
        Calendar now = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat();
        int month = now.get(Calendar.MONTH);  //todo 
        String startTime = "";
        String endTime = "";
        if(month >= 1 && month <= 4) {
            startTime = String.valueOf(now.get(Calendar.YEAR) - 1) + "-" + "04" + "-" + "01";
            endTime = String.valueOf(now.get(Calendar.YEAR)) + "-" + "03" + "-" + "31";
        }else {
            startTime = String.valueOf(now.get(Calendar.YEAR)) + "-" + "04" + "-" + "01";
            endTime = String.valueOf(now.get(Calendar.YEAR) + 1) + "-" + "03" + "-" + "31";
        }

        List<Priceset> allPriceset = pricesetMapper.selectByYear(startTime, endTime);
        Map<String, Double> pricesetMap = new HashMap<String, Double>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = sdf.parse(startTime);
        for ( Priceset priceset : allPriceset ) {
            Date pointDate = sdf.parse(priceset.getAssesstime().substring(0, 10));
            int startM = Integer.parseInt(priceset.getAssesstime().substring(5, 7));
            if ( pointDate.before(startDate) ) {
                for ( int i=1; i<=12; i++) {
                    String key = priceset.getUser_id() + "price" + i;
                    Double value = 0.0;
                    value = Double.parseDouble(priceset.getTotalunit().trim());
                    pricesetMap.put(key, value);
                }
            }else {
                if(startM >= 1 && startM<=3){
                    for (int k = startM; k<=3; k++) {
                        pricesetMap.put(priceset.getUser_id() + "price" + k, Double.parseDouble(priceset.getTotalunit().trim()));
                    }
                }else {
                    for (int k = startM; k<=12; k++) {
                        pricesetMap.put(priceset.getUser_id() + "price" + k, Double.parseDouble(priceset.getTotalunit().trim()));
                    }
                    for (int k = 1; k<=3; k++) {
                        pricesetMap.put(priceset.getUser_id() + "price" + k, Double.parseDouble(priceset.getTotalunit().trim()));
                    }
                }
            }
        }

        // 获取公司名称
        Expatriatesinfor expatriatesinfor = new Expatriatesinfor();
        List<Expatriatesinfor> companyList = expatriatesinforMapper.select(expatriatesinfor);
        Map<String, String> companyMap = new HashMap<String, String>();
        for ( Expatriatesinfor ex : companyList) {
            String key = ex.getExpname();
            String value = ex.getSuppliername();
            companyMap.put(key, value);
        }
        // 获取活用情报信息
        List<Coststatistics> allCostList = coststatisticsMapper.getExpatriatesinfor(coststatistics);
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
                    manhour = Double.parseDouble(BeanUtils.getProperty(c, property));
                } catch (Exception e) {}
                double cost = price * manhour;
                BeanUtils.setProperty(c, "cost" + i, cost);
                totalmanhours += manhour;
                totalcost += cost;
                if ( i%3 ==0 ) {
                    // 经费处理
                    String variousKey = c.getBpname() + i;
                    double various = 0;
                    if ( variousfundsMap.containsKey(variousKey) ) {
                        various = variousfundsMap.get(variousKey);
                    }
                    BeanUtils.setProperty(c, "expense" +i, various);
                    BeanUtils.setProperty(c, "contract" +i, various + totalcost);

                    BeanUtils.setProperty(c,"support" + i, i );
                    BeanUtils.setProperty(c, "totalcost" + i, totalcost);
                    BeanUtils.setProperty(c, "totalmanhours" + i, totalmanhours);
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

        int insertCount = 0;
        if ( allCostList.size() > 0 ) {
            insertCount = coststatisticsMapper.insertAll(allCostList);
        }

        return insertCount;
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
                for (int k = 0; k <=12; k++) {
                    double manhour = 0;
                    String property = "manhour" + k;
                    try {
                        manhour = Double.parseDouble(BeanUtils.getProperty(c, property));
                        switch(k){
                            case 4:
                                row.createCell(4).setCellValue(manhour);
                            case 5:
                                row.createCell(8).setCellValue(manhour);
                            case 6:
                                row.createCell(12).setCellValue(manhour);
                            case 7:
                                row.createCell(20).setCellValue(manhour);
                            case 8:
                                row.createCell(24).setCellValue(manhour);
                            case 9:
                                row.createCell(28).setCellValue(manhour);
                            case 10:
                                row.createCell(36).setCellValue(manhour);
                            case 11:
                                row.createCell(40).setCellValue(manhour);
                            case 12:
                                row.createCell(44).setCellValue(manhour);
                            case 1:
                                row.createCell(52).setCellValue(manhour);
                            case 2:
                                row.createCell(56).setCellValue(manhour);
                            case 3:
                                row.createCell(60).setCellValue(manhour);
                        }
                    } catch (Exception e) {}

                }
                row.createCell(0).setCellValue(i - 2);
                row.createCell(1).setCellValue(c.getBpname());
                row.createCell(2).setCellValue(c.getBpcompany());
                row.createCell(3).setCellValue(c.getUnitprice());//todo danjia
                row.createCell(5).setCellValue(c.getCost4());
                row.createCell(6).setCellValue(c.getSupport6());
                row.createCell(7).setCellValue(c.getUnitprice());//todo danjia
                row.createCell(9).setCellValue(c.getCost5());
                row.createCell(10).setCellValue(c.getSupport6());
                row.createCell(11).setCellValue(c.getUnitprice());//todo danjia
                row.createCell(13).setCellValue(c.getCost6());
                row.createCell(14).setCellValue(c.getSupport6());
                row.createCell(15).setCellValue(c.getTotalmanhours6());
                row.createCell(16).setCellValue(c.getTotalcost6());
                row.createCell(17).setCellValue(c.getExpense6());
                row.createCell(18).setCellValue(c.getContract6());
                row.createCell(19).setCellValue(c.getUnitprice());//todo danjia
                row.createCell(21).setCellValue(c.getCost7());
                row.createCell(22).setCellValue(c.getSupport9());
                row.createCell(23).setCellValue(c.getUnitprice());//todo danjia
                row.createCell(25).setCellValue(c.getCost8());
                row.createCell(26).setCellValue(c.getSupport9());
                row.createCell(27).setCellValue(c.getUnitprice());//todo danjia
                row.createCell(29).setCellValue(c.getCost9());
                row.createCell(30).setCellValue(c.getSupport9());
                row.createCell(31).setCellValue(c.getTotalmanhours9());
                row.createCell(32).setCellValue(c.getTotalcost9());
                row.createCell(33).setCellValue(c.getExpense9());
                row.createCell(34).setCellValue(c.getContract9());
                row.createCell(35).setCellValue(c.getUnitprice());//todo danjia
                row.createCell(37).setCellValue(c.getCost10());
                row.createCell(38).setCellValue(c.getSupport12());
                row.createCell(39).setCellValue(c.getUnitprice());//todo danjia
                row.createCell(41).setCellValue(c.getCost11());
                row.createCell(42).setCellValue(c.getSupport12());
                row.createCell(43).setCellValue(c.getUnitprice());//todo danjia
                row.createCell(45).setCellValue(c.getCost11());
                row.createCell(46).setCellValue(c.getSupport12());
                row.createCell(47).setCellValue(c.getTotalmanhours12());
                row.createCell(48).setCellValue(c.getTotalcost12());
                row.createCell(49).setCellValue(c.getExpense12());
                row.createCell(50).setCellValue(c.getContract12());
                row.createCell(51).setCellValue(c.getUnitprice());//todo danjia
                row.createCell(53).setCellValue(c.getCost1());
                row.createCell(54).setCellValue(c.getSupport3());
                row.createCell(55).setCellValue(c.getUnitprice());//todo danjia
                row.createCell(57).setCellValue(c.getCost2());
                row.createCell(58).setCellValue(c.getSupport3());
                row.createCell(59).setCellValue(c.getUnitprice());//todo danjia
                row.createCell(61).setCellValue(c.getCost3());
                row.createCell(62).setCellValue(c.getSupport3());
                row.createCell(63).setCellValue(c.getTotalmanhours3());
                row.createCell(64).setCellValue(c.getTotalcost3());
                row.createCell(65).setCellValue(c.getExpense3());
                row.createCell(66).setCellValue(c.getContract3());
                i++;
            }

            // 写出到文件
            return work;
        } catch (Exception e) {
            throw new LogicalException(e.getMessage());
        }
    }
}
