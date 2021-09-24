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
import com.nt.utils.dao.TokenModel;
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
    private PublicExpenseMapper publicExpenseMapper;
    @Autowired
    private OrgTreeService orgTreeService;

    public List<CompanyStatistics> getCostsByGrpAndY(String groupid,String years) throws Exception{
        List<CompanyStatistics> companyStatisticsList = new ArrayList<>();
        CompanyStatistics companyStatistics = new CompanyStatistics();
        companyStatistics.setGroup_id(groupid);
        companyStatistics.setYear(years);
        if(groupid.equals(""))
        {
            companyStatisticsList = companyStatisticsMapper.selectAllcompany(years);
        }
        else
        {
            companyStatisticsList = companyStatisticsMapper.select(companyStatistics);
        }
        return companyStatisticsList;
    }

    @Override
    public Integer insertCosts(String groupid, String years) throws Exception {
        Map<String, Object> result = new HashMap<>();

        Map<String, Double> userPriceMap = coststatisticsService.getUserPriceMapBygroupid(groupid, years);
        // 获取公司名称
        Expatriatesinfor expatriatesinfor = new Expatriatesinfor();
        List<Expatriatesinfor> companyList = expatriatesinforMapper.select(expatriatesinfor);
        Map<String, String> user2CompanyMap = new HashMap<String, String>();
        for (Expatriatesinfor ex : companyList) {
            String key = ex.getExpatriatesinfor_id();
            String value = ex.getSupplierinfor_id();
            user2CompanyMap.put(key, value);
        }
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
            company.setBpcompany(bpcompany);

            String userPriceKey = c.getBpname() + c.getGroupid() + "price";
            // 个人单位的合计费用(行合计)
            BigDecimal totalmanhours = BigDecimal.ZERO;
            // 个人单位的合计工数(行合计)
            BigDecimal totalcost = BigDecimal.ZERO;

            // 分别计算12个月的累计值
            for (int i = 1; i <= 12; i++) {
                String p_manhour = "manhour" + i;
                String p_cost = "cost" + i;
                String p_manhourf = "manhour" + i + "f";
                String p_costf = "cost" + i + "f";

                // 上一次合计结果
                BigDecimal oldManhour = BigDecimal.ZERO;
                BigDecimal oldCost = BigDecimal.ZERO;
                try {
                    oldManhour = new BigDecimal(BeanUtils.getProperty(company, p_manhour));
                } catch (Exception e) {
                }
                try {
                    oldCost = new BigDecimal(BeanUtils.getProperty(company, p_cost));
                } catch (Exception e) {
                }

                // 当前结果
                BigDecimal manhour = BigDecimal.ZERO;
                try {
                    manhour = new BigDecimal(BeanUtils.getProperty(c, p_manhour));
                } catch (Exception e) {
                }
                BigDecimal price = new BigDecimal(userPriceMap.getOrDefault(userPriceKey + i, 0d));
                BigDecimal cost = price.multiply(manhour);

                BigDecimal newCost = cost.add(oldCost);
                BigDecimal newManhour =manhour.add(oldManhour);

                BeanUtils.setProperty(company, p_cost, df.format(newCost));
                BeanUtils.setProperty(company, p_manhour, newManhour);
                BeanUtils.setProperty(company, p_manhourf, 0.0);
                BeanUtils.setProperty(company, p_costf, 0.00);
                totalcost = totalcost.add(cost);
                totalmanhours = totalmanhours.add(manhour);
            }

            // 操作行合计值
            BigDecimal oldTotalmanhours = BigDecimal.ZERO;
            BigDecimal oldTotalcost = BigDecimal.ZERO;
            try {
                oldTotalcost = company.getTotalcost() ==null  ?  BigDecimal.ZERO :company.getTotalcost();
            } catch (Exception e) {
            }
            try {
                oldTotalmanhours = company.getTotalmanhour()==null  ?  BigDecimal.ZERO :company.getTotalmanhour();
            } catch (Exception e) {
            }

            company.setTotalcost(oldTotalcost.add(totalcost));
            company.setTotalmanhour(oldTotalmanhours.add(totalmanhours));
            companyMap.put(bpcompany, company);

        }
        // add gbb 210914 BP社统计添加添加费用列 start
        for (Map.Entry<String, CompanyStatistics> map : companyMap.entrySet()){
            List<PublicExpense> newPList = publicExpenseList.stream().filter(str ->(str.getPayeename().contains(map.getKey()))).collect(Collectors.toList());
            List<Coststatistics> newCoststatisticsList = cosExpenseList.stream().filter(str ->(str.getBpcompany().contains(map.getKey()))).collect(Collectors.toList());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formatterM = new SimpleDateFormat("M");
            // 单价
            BigDecimal unitPrice = new BigDecimal(18500);
            // 费用-作业工数
            BigDecimal bigTormbCount = BigDecimal.ZERO;
            // 费用-外注费用
            BigDecimal bigManhourCount = BigDecimal.ZERO;
            //费用计算
            for(PublicExpense newPub : newPList){
                if (newPub.getJzmonth() != null && StringUtils.isNotEmpty(newPub.getTormb())) {
                    String strJzmonth = formatterM.format(newPub.getJzmonth());
                    // 外注费用
                    BigDecimal bigTormb = new BigDecimal(newPub.getTormb());
                    // 费用-总外注费用
                    bigManhourCount = bigManhourCount.add(bigTormb);

                    // 作业工数 = 外注费用 / 18500
                    BigDecimal bigManhour = bigTormb.divide(new BigDecimal(18500),2,4).setScale(2, RoundingMode.HALF_UP);

                    // 费用-总作业工数
                    bigTormbCount = bigTormbCount.add(bigManhour);

                    if(strJzmonth.equals("1")){
                        map.getValue().setCost1f(bigTormb);
                        map.getValue().setManhour1f(bigManhour);
                    }
                    else if(strJzmonth.equals("2")){
                        map.getValue().setCost2(bigTormb);
                        map.getValue().setManhour2f(bigManhour);
                    }
                    if(strJzmonth.equals("3")){
                        map.getValue().setCost3f(bigTormb);
                        map.getValue().setManhour3f(bigManhour);
                    }
                    if(strJzmonth.equals("4")){
                        map.getValue().setCost4f(bigTormb);
                        map.getValue().setManhour4f(bigManhour);
                    }
                    if(strJzmonth.equals("5")){
                        map.getValue().setCost5f(bigTormb);
                        map.getValue().setManhour5f(bigManhour);
                    }
                    if(strJzmonth.equals("6")){
                        map.getValue().setCost6f(bigTormb);
                        map.getValue().setManhour6f(bigManhour);
                    }
                    if(strJzmonth.equals("7")){
                        map.getValue().setCost7f(bigTormb);
                        map.getValue().setManhour7f(bigManhour);
                    }
                    if(strJzmonth.equals("8")){
                        map.getValue().setCost8f(bigTormb);
                        map.getValue().setManhour8f(bigManhour);
                    }
                    if(strJzmonth.equals("9")){
                        map.getValue().setCost9f(bigTormb);
                        map.getValue().setManhour9f(bigManhour);
                    }
                    if(strJzmonth.equals("10")){
                        map.getValue().setCost10f(bigTormb);
                        map.getValue().setManhour10f(bigManhour);
                    }
                    if(strJzmonth.equals("11")){
                        map.getValue().setCost11f(bigTormb);
                        map.getValue().setManhour11f(bigManhour);
                    }
                    if(strJzmonth.equals("12")){
                        map.getValue().setCost12f(bigTormb);
                        map.getValue().setManhour12f(bigManhour);
                    }
                    companyMap.put(map.getKey(),map.getValue());
                }
            }
            // 预提费用计算
            for(Coststatistics newCoststatistics : newCoststatisticsList){
                // 外注费用1月
                BigDecimal bigExpense1 = new BigDecimal(newCoststatistics.getExpensesolo1()).add(map.getValue().getCost1());
                map.getValue().setCost1(bigExpense1.setScale(2, RoundingMode.HALF_UP));
                // 外注费用2月
                BigDecimal bigExpense2 = new BigDecimal(newCoststatistics.getExpensesolo2()).add(map.getValue().getCost2());
                map.getValue().setCost2(bigExpense2.setScale(2, RoundingMode.HALF_UP));
                // 外注费用3月
                BigDecimal bigExpense3 = new BigDecimal(newCoststatistics.getExpensesolo3()).add(map.getValue().getCost3());
                map.getValue().setCost3(bigExpense3.setScale(2, RoundingMode.HALF_UP));
                // 外注费用4月
                BigDecimal bigExpense4 = new BigDecimal(newCoststatistics.getExpensesolo4()).add(map.getValue().getCost4());
                map.getValue().setCost4(bigExpense4.setScale(2, RoundingMode.HALF_UP));
                // 外注费用5月
                BigDecimal bigExpense5 = new BigDecimal(newCoststatistics.getExpensesolo5()).add(map.getValue().getCost5());
                map.getValue().setCost5(bigExpense5.setScale(2, RoundingMode.HALF_UP));
                // 外注费用6月
                BigDecimal bigExpense6 = new BigDecimal(newCoststatistics.getExpensesolo6()).add(map.getValue().getCost6());
                map.getValue().setCost6(bigExpense6.setScale(2, RoundingMode.HALF_UP));
                // 外注费用7月
                BigDecimal bigExpense7 = new BigDecimal(newCoststatistics.getExpensesolo7()).add(map.getValue().getCost7());
                map.getValue().setCost7(bigExpense7.setScale(2, RoundingMode.HALF_UP));
                // 外注费用8月
                BigDecimal bigExpense8 = new BigDecimal(newCoststatistics.getExpensesolo8()).add(map.getValue().getCost8());
                map.getValue().setCost8(bigExpense8.setScale(2, RoundingMode.HALF_UP));
                // 外注费用9月
                BigDecimal bigExpense9 = new BigDecimal(newCoststatistics.getExpensesolo9()).add(map.getValue().getCost9());
                map.getValue().setCost9(bigExpense9.setScale(2, RoundingMode.HALF_UP));
                // 外注费用10月
                BigDecimal bigExpense10 = new BigDecimal(newCoststatistics.getExpensesolo10()).add(map.getValue().getCost10());
                map.getValue().setCost10(bigExpense10.setScale(2, RoundingMode.HALF_UP));
                // 外注费用11月
                BigDecimal bigExpense11 = new BigDecimal(newCoststatistics.getExpensesolo11()).add(map.getValue().getCost11());
                map.getValue().setCost11(bigExpense11.setScale(2, RoundingMode.HALF_UP));
                // 外注费用12月
                BigDecimal bigExpense12 = new BigDecimal(newCoststatistics.getExpensesolo12()).add(map.getValue().getCost12());
                map.getValue().setCost12(bigExpense12.setScale(2, RoundingMode.HALF_UP));
            }
            // 费用合计费用
            map.getValue().setTotalcostf(bigManhourCount.setScale(2, RoundingMode.HALF_UP));
            // 费用合计工数
            map.getValue().setTotalmanhourf(bigTormbCount.setScale(2, RoundingMode.HALF_UP));
        }
        // add gbb 210914 BP社统计添加添加费用列 end
        result.put("company", new ArrayList<>(companyMap.values()));

        // year
        result.put("year", years);
        result.put("group", groupid);

        Integer months = 4;
        if(years.length()  == 7)
        {
            String [] ym = years.split("-");
            years = ym[0];
            months = Integer.parseInt(ym[1]);
        }
        else
        {
            Calendar calendar = Calendar.getInstance();
            months = calendar.get(Calendar.MONTH);
            if(months == 0)
            {
                months = 12;
            }
        }
        List<CompanyStatistics> cmyListinsertall = new ArrayList<>();
        List<CompanyStatistics> cmyListupdateall = new ArrayList<>();
        TokenModel tokenModel = new TokenModel();
        for (Map.Entry<String, Object> entry : result.entrySet())
        {
            CompanyStatistics company = new CompanyStatistics();
            company.setYear(years);
            company.setGroup_id(groupid);
            if(entry.getKey().equals("company"))
            {
                for(CompanyStatistics cpany : (List<CompanyStatistics>) result.get("company"))
                {
                    company.setBpcompanyid(cpany.getBpcompany());
                    Supplierinfor supplierinfors = supplierinforMapper.selectByPrimaryKey(company.getBpcompanyid());
                    if(supplierinfors!=null)
                    {
                        company.setBpcompany(supplierinfors.getSupchinese());
                        cpany.setYear(years);
                        cpany.setGroup_id(groupid);
                        cpany.setBpcompanyid(company.getBpcompanyid());
                        cpany.setBpcompany(company.getBpcompany());
                    }
                    List<CompanyStatistics> clist = null;
                    switch (months)
                    {
                        case 4:
                            clist = companyStatisticsMapper.select(company);
                            if(clist.size()>0)
                            {
                                clist.get(0).setManhour4(cpany.getManhour4());
                                clist.get(0).setCost4(cpany.getCost4());
                                clist.get(0).setManhour4f(cpany.getManhour4f());
                                clist.get(0).setCost4f(cpany.getCost4f());
                                clist.get(0).setTotalmanhour(clist.get(0).getManhour4().add(clist.get(0).getManhour5().add(clist.get(0).getManhour6().add(clist.get(0).getManhour7()
                                        .add(clist.get(0).getManhour8().add(clist.get(0).getManhour9().add(clist.get(0).getManhour10().add(clist.get(0).getManhour11()
                                        .add(clist.get(0).getManhour12().add(clist.get(0).getManhour1().add(clist.get(0).getManhour2().add(clist.get(0).getManhour3()))))))))))));

                                clist.get(0).setTotalcost(clist.get(0).getCost4().add(clist.get(0).getCost5().add(clist.get(0).getCost6().add(clist.get(0).getCost7()
                                        .add(clist.get(0).getCost8().add(clist.get(0).getCost9().add(clist.get(0).getCost10().add(clist.get(0).getCost11()
                                                .add(clist.get(0).getCost12().add(clist.get(0).getCost1().add(clist.get(0).getCost2().add(clist.get(0).getCost3()))))))))))));

                                clist.get(0).setTotalmanhourf(clist.get(0).getManhour4f().add(clist.get(0).getManhour5f().add(clist.get(0).getManhour6f().add(clist.get(0).getManhour7f()
                                        .add(clist.get(0).getManhour8f().add(clist.get(0).getManhour9f().add(clist.get(0).getManhour10f().add(clist.get(0).getManhour11f()
                                                .add(clist.get(0).getManhour12f().add(clist.get(0).getManhour1f().add(clist.get(0).getManhour2f().add(clist.get(0).getManhour3f()))))))))))));

                                clist.get(0).setTotalcostf(clist.get(0).getCost4f().add(clist.get(0).getCost5f().add(clist.get(0).getCost6f().add(clist.get(0).getCost7f()
                                        .add(clist.get(0).getCost8f().add(clist.get(0).getCost9f().add(clist.get(0).getCost10f().add(clist.get(0).getCost11f()
                                                .add(clist.get(0).getCost12f().add(clist.get(0).getCost1f().add(clist.get(0).getCost2f().add(clist.get(0).getCost3f()))))))))))));
                                clist.get(0).preUpdate(tokenModel);
                                cmyListupdateall.add(clist.get(0));
                            }
                            else
                            {
                                cpany.setCompanystatistics_id(UUID.randomUUID().toString());
                                cpany.preInsert();
                                cmyListinsertall.add(cpany);
                            }
                            break;
                        case 5:
                            clist = companyStatisticsMapper.select(company);
                            if(clist.size()>0)
                            {
                                clist.get(0).setManhour5(cpany.getManhour5());
                                clist.get(0).setCost5(cpany.getCost5());
                                clist.get(0).setManhour5f(cpany.getManhour5f());
                                clist.get(0).setCost5f(cpany.getCost5f());
                                clist.get(0).setTotalmanhour(clist.get(0).getManhour4().add(clist.get(0).getManhour5().add(clist.get(0).getManhour6().add(clist.get(0).getManhour7()
                                        .add(clist.get(0).getManhour8().add(clist.get(0).getManhour9().add(clist.get(0).getManhour10().add(clist.get(0).getManhour11()
                                                .add(clist.get(0).getManhour12().add(clist.get(0).getManhour1().add(clist.get(0).getManhour2().add(clist.get(0).getManhour3()))))))))))));

                                clist.get(0).setTotalcost(clist.get(0).getCost4().add(clist.get(0).getCost5().add(clist.get(0).getCost6().add(clist.get(0).getCost7()
                                        .add(clist.get(0).getCost8().add(clist.get(0).getCost9().add(clist.get(0).getCost10().add(clist.get(0).getCost11()
                                                .add(clist.get(0).getCost12().add(clist.get(0).getCost1().add(clist.get(0).getCost2().add(clist.get(0).getCost3()))))))))))));

                                clist.get(0).setTotalmanhourf(clist.get(0).getManhour4f().add(clist.get(0).getManhour5f().add(clist.get(0).getManhour6f().add(clist.get(0).getManhour7f()
                                        .add(clist.get(0).getManhour8f().add(clist.get(0).getManhour9f().add(clist.get(0).getManhour10f().add(clist.get(0).getManhour11f()
                                                .add(clist.get(0).getManhour12f().add(clist.get(0).getManhour1f().add(clist.get(0).getManhour2f().add(clist.get(0).getManhour3f()))))))))))));

                                clist.get(0).setTotalcostf(clist.get(0).getCost4f().add(clist.get(0).getCost5f().add(clist.get(0).getCost6f().add(clist.get(0).getCost7f()
                                        .add(clist.get(0).getCost8f().add(clist.get(0).getCost9f().add(clist.get(0).getCost10f().add(clist.get(0).getCost11f()
                                                .add(clist.get(0).getCost12f().add(clist.get(0).getCost1f().add(clist.get(0).getCost2f().add(clist.get(0).getCost3f()))))))))))));
                                clist.get(0).preUpdate(tokenModel);
                                cmyListupdateall.add(clist.get(0));
                            }
                            else
                            {
                                cpany.setCompanystatistics_id(UUID.randomUUID().toString());
                                cpany.preInsert();
                                cmyListinsertall.add(cpany);
                            }
                            break;
                        case 6:
                            clist = companyStatisticsMapper.select(company);
                            if(clist.size()>0)
                            {
                                clist.get(0).setManhour6(cpany.getManhour6());
                                clist.get(0).setCost6(cpany.getCost6());
                                clist.get(0).setManhour6f(cpany.getManhour6f());
                                clist.get(0).setCost6f(cpany.getCost6f());
                                clist.get(0).setTotalmanhour(clist.get(0).getManhour4().add(clist.get(0).getManhour5().add(clist.get(0).getManhour6().add(clist.get(0).getManhour7()
                                        .add(clist.get(0).getManhour8().add(clist.get(0).getManhour9().add(clist.get(0).getManhour10().add(clist.get(0).getManhour11()
                                                .add(clist.get(0).getManhour12().add(clist.get(0).getManhour1().add(clist.get(0).getManhour2().add(clist.get(0).getManhour3()))))))))))));

                                clist.get(0).setTotalcost(clist.get(0).getCost4().add(clist.get(0).getCost5().add(clist.get(0).getCost6().add(clist.get(0).getCost7()
                                        .add(clist.get(0).getCost8().add(clist.get(0).getCost9().add(clist.get(0).getCost10().add(clist.get(0).getCost11()
                                                .add(clist.get(0).getCost12().add(clist.get(0).getCost1().add(clist.get(0).getCost2().add(clist.get(0).getCost3()))))))))))));

                                clist.get(0).setTotalmanhourf(clist.get(0).getManhour4f().add(clist.get(0).getManhour5f().add(clist.get(0).getManhour6f().add(clist.get(0).getManhour7f()
                                        .add(clist.get(0).getManhour8f().add(clist.get(0).getManhour9f().add(clist.get(0).getManhour10f().add(clist.get(0).getManhour11f()
                                                .add(clist.get(0).getManhour12f().add(clist.get(0).getManhour1f().add(clist.get(0).getManhour2f().add(clist.get(0).getManhour3f()))))))))))));

                                clist.get(0).setTotalcostf(clist.get(0).getCost4f().add(clist.get(0).getCost5f().add(clist.get(0).getCost6f().add(clist.get(0).getCost7f()
                                        .add(clist.get(0).getCost8f().add(clist.get(0).getCost9f().add(clist.get(0).getCost10f().add(clist.get(0).getCost11f()
                                                .add(clist.get(0).getCost12f().add(clist.get(0).getCost1f().add(clist.get(0).getCost2f().add(clist.get(0).getCost3f()))))))))))));
                                clist.get(0).preUpdate(tokenModel);
                                cmyListupdateall.add(clist.get(0));
                            }
                            else
                            {
                                cpany.setCompanystatistics_id(UUID.randomUUID().toString());
                                cpany.preInsert();
                                cmyListinsertall.add(cpany);
                            }
                            break;
                        case 7:
                            clist = companyStatisticsMapper.select(company);
                            if(clist.size()>0)
                            {
                                clist.get(0).setManhour7(cpany.getManhour7());
                                clist.get(0).setCost7(cpany.getCost7());
                                clist.get(0).setManhour7f(cpany.getManhour7f());
                                clist.get(0).setCost7f(cpany.getCost7f());
                                clist.get(0).setTotalmanhour(clist.get(0).getManhour4().add(clist.get(0).getManhour5().add(clist.get(0).getManhour6().add(clist.get(0).getManhour7()
                                        .add(clist.get(0).getManhour8().add(clist.get(0).getManhour9().add(clist.get(0).getManhour10().add(clist.get(0).getManhour11()
                                                .add(clist.get(0).getManhour12().add(clist.get(0).getManhour1().add(clist.get(0).getManhour2().add(clist.get(0).getManhour3()))))))))))));

                                clist.get(0).setTotalcost(clist.get(0).getCost4().add(clist.get(0).getCost5().add(clist.get(0).getCost6().add(clist.get(0).getCost7()
                                        .add(clist.get(0).getCost8().add(clist.get(0).getCost9().add(clist.get(0).getCost10().add(clist.get(0).getCost11()
                                                .add(clist.get(0).getCost12().add(clist.get(0).getCost1().add(clist.get(0).getCost2().add(clist.get(0).getCost3()))))))))))));

                                clist.get(0).setTotalmanhourf(clist.get(0).getManhour4f().add(clist.get(0).getManhour5f().add(clist.get(0).getManhour6f().add(clist.get(0).getManhour7f()
                                        .add(clist.get(0).getManhour8f().add(clist.get(0).getManhour9f().add(clist.get(0).getManhour10f().add(clist.get(0).getManhour11f()
                                                .add(clist.get(0).getManhour12f().add(clist.get(0).getManhour1f().add(clist.get(0).getManhour2f().add(clist.get(0).getManhour3f()))))))))))));

                                clist.get(0).setTotalcostf(clist.get(0).getCost4f().add(clist.get(0).getCost5f().add(clist.get(0).getCost6f().add(clist.get(0).getCost7f()
                                        .add(clist.get(0).getCost8f().add(clist.get(0).getCost9f().add(clist.get(0).getCost10f().add(clist.get(0).getCost11f()
                                                .add(clist.get(0).getCost12f().add(clist.get(0).getCost1f().add(clist.get(0).getCost2f().add(clist.get(0).getCost3f()))))))))))));
                                clist.get(0).preUpdate(tokenModel);
                                cmyListupdateall.add(clist.get(0));
                            }
                            else
                            {
                                cpany.setCompanystatistics_id(UUID.randomUUID().toString());
                                cpany.preInsert();
                                cmyListinsertall.add(cpany);
                            }
                            break;
                        case 8:
                            clist = companyStatisticsMapper.select(company);
                            if(clist.size()>0)
                            {
                                clist.get(0).setManhour8(cpany.getManhour8());
                                clist.get(0).setCost8(cpany.getCost8());
                                clist.get(0).setManhour8f(cpany.getManhour8f());
                                clist.get(0).setCost8f(cpany.getCost8f());
                                clist.get(0).setTotalmanhour(clist.get(0).getManhour4().add(clist.get(0).getManhour5().add(clist.get(0).getManhour6().add(clist.get(0).getManhour7()
                                        .add(clist.get(0).getManhour8().add(clist.get(0).getManhour9().add(clist.get(0).getManhour10().add(clist.get(0).getManhour11()
                                                .add(clist.get(0).getManhour12().add(clist.get(0).getManhour1().add(clist.get(0).getManhour2().add(clist.get(0).getManhour3()))))))))))));

                                clist.get(0).setTotalcost(clist.get(0).getCost4().add(clist.get(0).getCost5().add(clist.get(0).getCost6().add(clist.get(0).getCost7()
                                        .add(clist.get(0).getCost8().add(clist.get(0).getCost9().add(clist.get(0).getCost10().add(clist.get(0).getCost11()
                                                .add(clist.get(0).getCost12().add(clist.get(0).getCost1().add(clist.get(0).getCost2().add(clist.get(0).getCost3()))))))))))));

                                clist.get(0).setTotalmanhourf(clist.get(0).getManhour4f().add(clist.get(0).getManhour5f().add(clist.get(0).getManhour6f().add(clist.get(0).getManhour7f()
                                        .add(clist.get(0).getManhour8f().add(clist.get(0).getManhour9f().add(clist.get(0).getManhour10f().add(clist.get(0).getManhour11f()
                                                .add(clist.get(0).getManhour12f().add(clist.get(0).getManhour1f().add(clist.get(0).getManhour2f().add(clist.get(0).getManhour3f()))))))))))));

                                clist.get(0).setTotalcostf(clist.get(0).getCost4f().add(clist.get(0).getCost5f().add(clist.get(0).getCost6f().add(clist.get(0).getCost7f()
                                        .add(clist.get(0).getCost8f().add(clist.get(0).getCost9f().add(clist.get(0).getCost10f().add(clist.get(0).getCost11f()
                                                .add(clist.get(0).getCost12f().add(clist.get(0).getCost1f().add(clist.get(0).getCost2f().add(clist.get(0).getCost3f()))))))))))));
                                clist.get(0).preUpdate(tokenModel);
                                cmyListupdateall.add(clist.get(0));
                            }
                            else
                            {
                                cpany.setCompanystatistics_id(UUID.randomUUID().toString());
                                cpany.preInsert();
                                cmyListinsertall.add(cpany);
                            }
                            break;
                        case 9:
                            clist = companyStatisticsMapper.select(company);
                            if(clist.size()>0)
                            {
                                clist.get(0).setManhour9(cpany.getManhour9());
                                clist.get(0).setCost9(cpany.getCost9());
                                clist.get(0).setManhour9f(cpany.getManhour9f());
                                clist.get(0).setCost9f(cpany.getCost9f());
                                clist.get(0).setTotalmanhour(clist.get(0).getManhour4().add(clist.get(0).getManhour5().add(clist.get(0).getManhour6().add(clist.get(0).getManhour7()
                                        .add(clist.get(0).getManhour8().add(clist.get(0).getManhour9().add(clist.get(0).getManhour10().add(clist.get(0).getManhour11()
                                                .add(clist.get(0).getManhour12().add(clist.get(0).getManhour1().add(clist.get(0).getManhour2().add(clist.get(0).getManhour3()))))))))))));

                                clist.get(0).setTotalcost(clist.get(0).getCost4().add(clist.get(0).getCost5().add(clist.get(0).getCost6().add(clist.get(0).getCost7()
                                        .add(clist.get(0).getCost8().add(clist.get(0).getCost9().add(clist.get(0).getCost10().add(clist.get(0).getCost11()
                                                .add(clist.get(0).getCost12().add(clist.get(0).getCost1().add(clist.get(0).getCost2().add(clist.get(0).getCost3()))))))))))));

                                clist.get(0).setTotalmanhourf(clist.get(0).getManhour4f().add(clist.get(0).getManhour5f().add(clist.get(0).getManhour6f().add(clist.get(0).getManhour7f()
                                        .add(clist.get(0).getManhour8f().add(clist.get(0).getManhour9f().add(clist.get(0).getManhour10f().add(clist.get(0).getManhour11f()
                                                .add(clist.get(0).getManhour12f().add(clist.get(0).getManhour1f().add(clist.get(0).getManhour2f().add(clist.get(0).getManhour3f()))))))))))));

                                clist.get(0).setTotalcostf(clist.get(0).getCost4f().add(clist.get(0).getCost5f().add(clist.get(0).getCost6f().add(clist.get(0).getCost7f()
                                        .add(clist.get(0).getCost8f().add(clist.get(0).getCost9f().add(clist.get(0).getCost10f().add(clist.get(0).getCost11f()
                                                .add(clist.get(0).getCost12f().add(clist.get(0).getCost1f().add(clist.get(0).getCost2f().add(clist.get(0).getCost3f()))))))))))));
                                clist.get(0).preUpdate(tokenModel);
                                cmyListupdateall.add(clist.get(0));
                            }
                            else
                            {
                                cpany.setCompanystatistics_id(UUID.randomUUID().toString());
                                cpany.preInsert();
                                cmyListinsertall.add(cpany);
                            }
                            break;
                        case 10:
                            clist = companyStatisticsMapper.select(company);
                            if(clist.size()>0)
                            {
                                clist.get(0).setManhour10(cpany.getManhour10());
                                clist.get(0).setCost10(cpany.getCost10());
                                clist.get(0).setManhour10f(cpany.getManhour10f());
                                clist.get(0).setCost10f(cpany.getCost10f());
                                clist.get(0).setTotalmanhour(clist.get(0).getManhour4().add(clist.get(0).getManhour5().add(clist.get(0).getManhour6().add(clist.get(0).getManhour7()
                                        .add(clist.get(0).getManhour8().add(clist.get(0).getManhour9().add(clist.get(0).getManhour10().add(clist.get(0).getManhour11()
                                                .add(clist.get(0).getManhour12().add(clist.get(0).getManhour1().add(clist.get(0).getManhour2().add(clist.get(0).getManhour3()))))))))))));

                                clist.get(0).setTotalcost(clist.get(0).getCost4().add(clist.get(0).getCost5().add(clist.get(0).getCost6().add(clist.get(0).getCost7()
                                        .add(clist.get(0).getCost8().add(clist.get(0).getCost9().add(clist.get(0).getCost10().add(clist.get(0).getCost11()
                                                .add(clist.get(0).getCost12().add(clist.get(0).getCost1().add(clist.get(0).getCost2().add(clist.get(0).getCost3()))))))))))));

                                clist.get(0).setTotalmanhourf(clist.get(0).getManhour4f().add(clist.get(0).getManhour5f().add(clist.get(0).getManhour6f().add(clist.get(0).getManhour7f()
                                        .add(clist.get(0).getManhour8f().add(clist.get(0).getManhour9f().add(clist.get(0).getManhour10f().add(clist.get(0).getManhour11f()
                                                .add(clist.get(0).getManhour12f().add(clist.get(0).getManhour1f().add(clist.get(0).getManhour2f().add(clist.get(0).getManhour3f()))))))))))));

                                clist.get(0).setTotalcostf(clist.get(0).getCost4f().add(clist.get(0).getCost5f().add(clist.get(0).getCost6f().add(clist.get(0).getCost7f()
                                        .add(clist.get(0).getCost8f().add(clist.get(0).getCost9f().add(clist.get(0).getCost10f().add(clist.get(0).getCost11f()
                                                .add(clist.get(0).getCost12f().add(clist.get(0).getCost1f().add(clist.get(0).getCost2f().add(clist.get(0).getCost3f()))))))))))));
                                clist.get(0).preUpdate(tokenModel);
                                cmyListupdateall.add(clist.get(0));
                            }
                            else
                            {
                                cpany.setCompanystatistics_id(UUID.randomUUID().toString());
                                cpany.preInsert();
                                cmyListinsertall.add(cpany);
                            }
                            break;
                        case 11:
                            clist = companyStatisticsMapper.select(company);
                            if(clist.size()>0)
                            {
                                clist.get(0).setManhour11(cpany.getManhour11());
                                clist.get(0).setCost11(cpany.getCost11());
                                clist.get(0).setManhour11f(cpany.getManhour11f());
                                clist.get(0).setCost11f(cpany.getCost11f());
                                clist.get(0).setTotalmanhour(clist.get(0).getManhour4().add(clist.get(0).getManhour5().add(clist.get(0).getManhour6().add(clist.get(0).getManhour7()
                                        .add(clist.get(0).getManhour8().add(clist.get(0).getManhour9().add(clist.get(0).getManhour10().add(clist.get(0).getManhour11()
                                                .add(clist.get(0).getManhour12().add(clist.get(0).getManhour1().add(clist.get(0).getManhour2().add(clist.get(0).getManhour3()))))))))))));

                                clist.get(0).setTotalcost(clist.get(0).getCost4().add(clist.get(0).getCost5().add(clist.get(0).getCost6().add(clist.get(0).getCost7()
                                        .add(clist.get(0).getCost8().add(clist.get(0).getCost9().add(clist.get(0).getCost10().add(clist.get(0).getCost11()
                                                .add(clist.get(0).getCost12().add(clist.get(0).getCost1().add(clist.get(0).getCost2().add(clist.get(0).getCost3()))))))))))));

                                clist.get(0).setTotalmanhourf(clist.get(0).getManhour4f().add(clist.get(0).getManhour5f().add(clist.get(0).getManhour6f().add(clist.get(0).getManhour7f()
                                        .add(clist.get(0).getManhour8f().add(clist.get(0).getManhour9f().add(clist.get(0).getManhour10f().add(clist.get(0).getManhour11f()
                                                .add(clist.get(0).getManhour12f().add(clist.get(0).getManhour1f().add(clist.get(0).getManhour2f().add(clist.get(0).getManhour3f()))))))))))));

                                clist.get(0).setTotalcostf(clist.get(0).getCost4f().add(clist.get(0).getCost5f().add(clist.get(0).getCost6f().add(clist.get(0).getCost7f()
                                        .add(clist.get(0).getCost8f().add(clist.get(0).getCost9f().add(clist.get(0).getCost10f().add(clist.get(0).getCost11f()
                                                .add(clist.get(0).getCost12f().add(clist.get(0).getCost1f().add(clist.get(0).getCost2f().add(clist.get(0).getCost3f()))))))))))));
                                clist.get(0).preUpdate(tokenModel);
                                cmyListupdateall.add(clist.get(0));
                            }
                            else
                            {
                                cpany.setCompanystatistics_id(UUID.randomUUID().toString());
                                cpany.preInsert();
                                cmyListinsertall.add(cpany);
                            }
                            break;
                        case 12:
                            clist = companyStatisticsMapper.select(company);
                            if(clist.size()>0)
                            {
                                clist.get(0).setManhour12(cpany.getManhour12());
                                clist.get(0).setCost12(cpany.getCost12());
                                clist.get(0).setManhour12f(cpany.getManhour12f());
                                clist.get(0).setCost12f(cpany.getCost12f());
                                clist.get(0).setTotalmanhour(clist.get(0).getManhour4().add(clist.get(0).getManhour5().add(clist.get(0).getManhour6().add(clist.get(0).getManhour7()
                                        .add(clist.get(0).getManhour8().add(clist.get(0).getManhour9().add(clist.get(0).getManhour10().add(clist.get(0).getManhour11()
                                                .add(clist.get(0).getManhour12().add(clist.get(0).getManhour1().add(clist.get(0).getManhour2().add(clist.get(0).getManhour3()))))))))))));

                                clist.get(0).setTotalcost(clist.get(0).getCost4().add(clist.get(0).getCost5().add(clist.get(0).getCost6().add(clist.get(0).getCost7()
                                        .add(clist.get(0).getCost8().add(clist.get(0).getCost9().add(clist.get(0).getCost10().add(clist.get(0).getCost11()
                                                .add(clist.get(0).getCost12().add(clist.get(0).getCost1().add(clist.get(0).getCost2().add(clist.get(0).getCost3()))))))))))));

                                clist.get(0).setTotalmanhourf(clist.get(0).getManhour4f().add(clist.get(0).getManhour5f().add(clist.get(0).getManhour6f().add(clist.get(0).getManhour7f()
                                        .add(clist.get(0).getManhour8f().add(clist.get(0).getManhour9f().add(clist.get(0).getManhour10f().add(clist.get(0).getManhour11f()
                                                .add(clist.get(0).getManhour12f().add(clist.get(0).getManhour1f().add(clist.get(0).getManhour2f().add(clist.get(0).getManhour3f()))))))))))));

                                clist.get(0).setTotalcostf(clist.get(0).getCost4f().add(clist.get(0).getCost5f().add(clist.get(0).getCost6f().add(clist.get(0).getCost7f()
                                        .add(clist.get(0).getCost8f().add(clist.get(0).getCost9f().add(clist.get(0).getCost10f().add(clist.get(0).getCost11f()
                                                .add(clist.get(0).getCost12f().add(clist.get(0).getCost1f().add(clist.get(0).getCost2f().add(clist.get(0).getCost3f()))))))))))));
                                clist.get(0).preUpdate(tokenModel);
                                cmyListupdateall.add(clist.get(0));
                            }
                            else
                            {
                                cpany.setCompanystatistics_id(UUID.randomUUID().toString());
                                cpany.preInsert();
                                cmyListinsertall.add(cpany);
                            }
                            break;
                        case 1:
                            clist = companyStatisticsMapper.select(company);
                            if(clist.size()>0)
                            {
                                clist.get(0).setManhour1(cpany.getManhour1());
                                clist.get(0).setCost1(cpany.getCost1());
                                clist.get(0).setManhour1f(cpany.getManhour1f());
                                clist.get(0).setCost1f(cpany.getCost1f());
                                clist.get(0).setTotalmanhour(clist.get(0).getManhour4().add(clist.get(0).getManhour5().add(clist.get(0).getManhour6().add(clist.get(0).getManhour7()
                                        .add(clist.get(0).getManhour8().add(clist.get(0).getManhour9().add(clist.get(0).getManhour10().add(clist.get(0).getManhour11()
                                                .add(clist.get(0).getManhour12().add(clist.get(0).getManhour1().add(clist.get(0).getManhour2().add(clist.get(0).getManhour3()))))))))))));

                                clist.get(0).setTotalcost(clist.get(0).getCost4().add(clist.get(0).getCost5().add(clist.get(0).getCost6().add(clist.get(0).getCost7()
                                        .add(clist.get(0).getCost8().add(clist.get(0).getCost9().add(clist.get(0).getCost10().add(clist.get(0).getCost11()
                                                .add(clist.get(0).getCost12().add(clist.get(0).getCost1().add(clist.get(0).getCost2().add(clist.get(0).getCost3()))))))))))));

                                clist.get(0).setTotalmanhourf(clist.get(0).getManhour4f().add(clist.get(0).getManhour5f().add(clist.get(0).getManhour6f().add(clist.get(0).getManhour7f()
                                        .add(clist.get(0).getManhour8f().add(clist.get(0).getManhour9f().add(clist.get(0).getManhour10f().add(clist.get(0).getManhour11f()
                                                .add(clist.get(0).getManhour12f().add(clist.get(0).getManhour1f().add(clist.get(0).getManhour2f().add(clist.get(0).getManhour3f()))))))))))));

                                clist.get(0).setTotalcostf(clist.get(0).getCost4f().add(clist.get(0).getCost5f().add(clist.get(0).getCost6f().add(clist.get(0).getCost7f()
                                        .add(clist.get(0).getCost8f().add(clist.get(0).getCost9f().add(clist.get(0).getCost10f().add(clist.get(0).getCost11f()
                                                .add(clist.get(0).getCost12f().add(clist.get(0).getCost1f().add(clist.get(0).getCost2f().add(clist.get(0).getCost3f()))))))))))));
                                clist.get(0).preUpdate(tokenModel);
                                cmyListupdateall.add(clist.get(0));
                            }
                            else
                            {
                                cpany.setCompanystatistics_id(UUID.randomUUID().toString());
                                cpany.preInsert();
                                cmyListinsertall.add(cpany);
                            }
                            break;
                        case 2:
                            clist = companyStatisticsMapper.select(company);
                            if(clist.size()>0)
                            {
                                clist.get(0).setManhour2(cpany.getManhour2());
                                clist.get(0).setCost2(cpany.getCost2());
                                clist.get(0).setManhour2f(cpany.getManhour2f());
                                clist.get(0).setCost2f(cpany.getCost2f());
                                clist.get(0).setTotalmanhour(clist.get(0).getManhour4().add(clist.get(0).getManhour5().add(clist.get(0).getManhour6().add(clist.get(0).getManhour7()
                                        .add(clist.get(0).getManhour8().add(clist.get(0).getManhour9().add(clist.get(0).getManhour10().add(clist.get(0).getManhour11()
                                                .add(clist.get(0).getManhour12().add(clist.get(0).getManhour1().add(clist.get(0).getManhour2().add(clist.get(0).getManhour3()))))))))))));

                                clist.get(0).setTotalcost(clist.get(0).getCost4().add(clist.get(0).getCost5().add(clist.get(0).getCost6().add(clist.get(0).getCost7()
                                        .add(clist.get(0).getCost8().add(clist.get(0).getCost9().add(clist.get(0).getCost10().add(clist.get(0).getCost11()
                                                .add(clist.get(0).getCost12().add(clist.get(0).getCost1().add(clist.get(0).getCost2().add(clist.get(0).getCost3()))))))))))));

                                clist.get(0).setTotalmanhourf(clist.get(0).getManhour4f().add(clist.get(0).getManhour5f().add(clist.get(0).getManhour6f().add(clist.get(0).getManhour7f()
                                        .add(clist.get(0).getManhour8f().add(clist.get(0).getManhour9f().add(clist.get(0).getManhour10f().add(clist.get(0).getManhour11f()
                                                .add(clist.get(0).getManhour12f().add(clist.get(0).getManhour1f().add(clist.get(0).getManhour2f().add(clist.get(0).getManhour3f()))))))))))));

                                clist.get(0).setTotalcostf(clist.get(0).getCost4f().add(clist.get(0).getCost5f().add(clist.get(0).getCost6f().add(clist.get(0).getCost7f()
                                        .add(clist.get(0).getCost8f().add(clist.get(0).getCost9f().add(clist.get(0).getCost10f().add(clist.get(0).getCost11f()
                                                .add(clist.get(0).getCost12f().add(clist.get(0).getCost1f().add(clist.get(0).getCost2f().add(clist.get(0).getCost3f()))))))))))));
                                clist.get(0).preUpdate(tokenModel);
                                cmyListupdateall.add(clist.get(0));
                            }
                            else
                            {
                                cpany.setCompanystatistics_id(UUID.randomUUID().toString());
                                cpany.preInsert();
                                cmyListinsertall.add(cpany);
                            }
                            break;
                        case 3:
                            clist = companyStatisticsMapper.select(company);
                            if(clist.size()>0)
                            {
                                clist.get(0).setManhour3(cpany.getManhour3());
                                clist.get(0).setCost3(cpany.getCost3());
                                clist.get(0).setManhour3f(cpany.getManhour3f());
                                clist.get(0).setCost3f(cpany.getCost3f());
                                clist.get(0).setTotalmanhour(clist.get(0).getManhour4().add(clist.get(0).getManhour5().add(clist.get(0).getManhour6().add(clist.get(0).getManhour7()
                                        .add(clist.get(0).getManhour8().add(clist.get(0).getManhour9().add(clist.get(0).getManhour10().add(clist.get(0).getManhour11()
                                                .add(clist.get(0).getManhour12().add(clist.get(0).getManhour1().add(clist.get(0).getManhour2().add(clist.get(0).getManhour3()))))))))))));

                                clist.get(0).setTotalcost(clist.get(0).getCost4().add(clist.get(0).getCost5().add(clist.get(0).getCost6().add(clist.get(0).getCost7()
                                        .add(clist.get(0).getCost8().add(clist.get(0).getCost9().add(clist.get(0).getCost10().add(clist.get(0).getCost11()
                                                .add(clist.get(0).getCost12().add(clist.get(0).getCost1().add(clist.get(0).getCost2().add(clist.get(0).getCost3()))))))))))));

                                clist.get(0).setTotalmanhourf(clist.get(0).getManhour4f().add(clist.get(0).getManhour5f().add(clist.get(0).getManhour6f().add(clist.get(0).getManhour7f()
                                        .add(clist.get(0).getManhour8f().add(clist.get(0).getManhour9f().add(clist.get(0).getManhour10f().add(clist.get(0).getManhour11f()
                                                .add(clist.get(0).getManhour12f().add(clist.get(0).getManhour1f().add(clist.get(0).getManhour2f().add(clist.get(0).getManhour3f()))))))))))));

                                clist.get(0).setTotalcostf(clist.get(0).getCost4f().add(clist.get(0).getCost5f().add(clist.get(0).getCost6f().add(clist.get(0).getCost7f()
                                        .add(clist.get(0).getCost8f().add(clist.get(0).getCost9f().add(clist.get(0).getCost10f().add(clist.get(0).getCost11f()
                                                .add(clist.get(0).getCost12f().add(clist.get(0).getCost1f().add(clist.get(0).getCost2f().add(clist.get(0).getCost3f()))))))))))));
                                clist.get(0).preUpdate(tokenModel);
                                cmyListupdateall.add(clist.get(0));
                            }
                            else
                            {
                                cpany.setCompanystatistics_id(UUID.randomUUID().toString());
                                cpany.preInsert();
                                cmyListinsertall.add(cpany);
                            }
                            break;
                    }
                }
            }

        }
        Integer insertCount = 0;
        if(cmyListinsertall.size()>0)
        {
            insertCount = companyStatisticsMapper.insertAll(cmyListinsertall);
        }
        if(cmyListupdateall.size()>0)
        {
            insertCount = insertCount + companyStatisticsMapper.updateAll(cmyListupdateall);
        }

        return insertCount;
    }

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
            CompanyStatistics companyStatistics = new CompanyStatistics();
            companyStatistics.setYear(years);
            companyStatistics.setGroup_id(groupid);
            List<CompanyStatistics> companyStatisticsList = new ArrayList<>();
            if(groupid.equals(""))
            {
                companyStatisticsList = companyStatisticsMapper.selectAllcompany(years);
            }
            else
            {
                companyStatisticsList = companyStatisticsMapper.select(companyStatistics);
            }
            Calendar now = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("MM月");
            now.setTime(sdf.parse("04月"));
            //日期赋值
            for (int j = 1; j <= 12; j++) {
                sheet1.getRow(1).getCell(3 +  4 * (j - 1)).setCellValue(sdf.format(now.getTime()));
                now.set(Calendar.MONTH, now.get(Calendar.MONTH) + 1);
            }
            //
            Map<String, Double> totalCostMap = new HashMap<>();
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
                        property = "totalmanhour";
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
                row.createCell(2).setCellValue(c.getBpcompany());
                i++;
            }
            int rowIndex = companyStatisticsList.size() + 3;
            //合计行
            XSSFRow rowT = sheet1.createRow(1 + rowIndex);
            rowT.createCell(1).setCellValue("合计");

            CellRangeAddress region = new CellRangeAddress(1 + rowIndex, 1 + rowIndex, 1, 2);
            sheet1.addMergedRegion(region);
            // 设置值
            for (int k = 1; k <= 13; k++) {
                String property = "manhour" + k;
                String propertyC = "cost" + k;
                String propertyf = "manhour" + k + "f";
                String propertyCf = "cost" + k + "f";
                if (k > 12) {
                    property = "totalmanhour";
                    propertyC = "totalcost";
                    propertyf = "totalmanhourf";
                    propertyCf = "totalcostf";
                }
                int colIndex = getColIndex41Month(k);
                if (totalCostMap.size() > 0) {
                    //合计行
                    rowT.createCell(colIndex).setCellValue(totalCostMap.get(property));
                    rowT.createCell(colIndex + 1).setCellValue(totalCostMap.get(propertyC));
                    rowT.createCell(colIndex + 2).setCellValue(totalCostMap.get(propertyf));
                    rowT.createCell(colIndex + 3).setCellValue(totalCostMap.get(propertyCf));

                } else {
                    rowT.createCell(colIndex).setCellValue(0.0);
                    rowT.createCell(colIndex + 1).setCellValue(0.0);
                }
                //外注総合費用合計(元)
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
    public List downloadPdf(String dates) throws Exception {
        List listAll = new ArrayList<>();

        String strYears = dates.substring(0,4);
        int intMonths = Integer.valueOf(dates.substring(5,7));
        if(intMonths == 1 || intMonths == 2 || intMonths == 3){
            strYears = String.valueOf(Integer.valueOf(strYears) - 1);
        }
        List<DepartmentVo> allDepartment = orgTreeService.getAllDepartment();
        if(allDepartment.size() > 0){
            //需要查詢的字段
            String[] strColumn = new String[]{"MANHOUR" + intMonths, "COST" + intMonths,"MANHOUR" + intMonths + "F","COST" + intMonths + "F"};

            Map<String, Object> companyMap = new HashMap<>();
            List<CompanyStatistics> companyList = companyStatisticsMapper.getCompanyList(strYears);
            for(CompanyStatistics company : companyList){
                companyMap = new HashMap<>();
                companyMap.put("company", company.getBpcompany());
                listAll.add(companyMap);
            }
            companyMap = new HashMap<>();
            companyMap.put("company", "合计");
            listAll.add(companyMap);

            // 循环部门
            for(DepartmentVo depVo : allDepartment){
                List<CompanyStatistics> companyStatisticsList = companyStatisticsMapper.getcompanyStatisticsList(depVo.getDepartmentId(),strYears,strColumn);
                for(CompanyStatistics bp : companyStatisticsList){
                    for(int i = 0; i < listAll.size(); i++) {
                        HashMap totalCostMap = (HashMap) listAll.get(i);
                        if(bp.getBpcompany().equals(totalCostMap.get("company"))){
                            totalCostMap.put("manhour" + depVo.getDepartmentEn(), bp.getManhour4());
                            totalCostMap.put("cost" + depVo.getDepartmentEn(), bp.getCost4());
                            totalCostMap.put("manhourf" + depVo.getDepartmentEn(), bp.getManhour4f());
                            totalCostMap.put("costf" + depVo.getDepartmentEn(), bp.getCost4f());
                        }
                    }
                }
            }
        }
        return listAll;
    }
}
