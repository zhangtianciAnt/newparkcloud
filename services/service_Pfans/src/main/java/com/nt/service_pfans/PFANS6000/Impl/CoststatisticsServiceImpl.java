package com.nt.service_pfans.PFANS6000.Impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nt.dao_Auth.Vo.MembersVo;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_Org.OrgTree;
import com.nt.dao_Org.ToDoNotice;
import com.nt.dao_Pfans.PFANS1000.Businessplan;
import com.nt.dao_Pfans.PFANS1000.Contractapplication;
import com.nt.dao_Pfans.PFANS6000.*;
import com.nt.dao_Workflow.Workflowinstance;
import com.nt.service_Auth.RoleService;
import com.nt.service_Org.DictionaryService;
import com.nt.service_Org.ToDoNoticeService;
import com.nt.service_Org.mapper.DictionaryMapper;
import com.nt.service_pfans.PFANS1000.mapper.BusinessplanMapper;
import com.nt.service_pfans.PFANS1000.mapper.ContractapplicationMapper;
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
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.MongoTemplate;

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
import java.util.stream.Collectors;

import static com.nt.utils.MongoObject.CustmizeQuery;

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

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private CoststatisticsdetailMapper coststatisticsdetailMapper;
    @Autowired
    private ToDoNoticeService toDoNoticeService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private BusinessplanMapper businessplanMapper;
    @Autowired
    private ContractapplicationMapper contractapplicationMapper;


    @Override
    public List<Coststatistics> getCostList(Coststatistics coststatistics) throws Exception {
        return coststatisticsMapper.select(coststatistics);
    }

    @Override
    public List<Coststatistics> getCostListBygroupid(String groupid,String year) throws Exception {
        Calendar calendar = Calendar.getInstance();
//        int year = 0;
//        int month = calendar.get(Calendar.MONTH)+1;
//        if(month >= 1 && month <= 3) {
//            year = calendar.get(Calendar.YEAR) - 1;
//        }else {
//            year = calendar.get(Calendar.YEAR);
//        }
        return coststatisticsMapper.selectBygroupid(groupid,String.valueOf(year));
    }

    @Override
    public Integer insertCoststatistics(String groupid,String year ,Coststatistics coststatistics, TokenModel tokenModel) throws Exception {
        Calendar calendar = Calendar.getInstance();
//        int year = 0;
//        int month = calendar.get(Calendar.MONTH)+1;
//        if(month >= 1 && month <= 3) {
//            year = calendar.get(Calendar.YEAR) - 1;
//        }else {
//            year = calendar.get(Calendar.YEAR);
//        }


        List<Coststatistics> allCostList = getCostList(groupid,year,coststatistics, tokenModel);

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

    private List<Coststatistics> getCostList(String groupid,String year ,Coststatistics coststatistics, TokenModel tokenModel) throws Exception {
        //获取经费
        Variousfunds variousfunds = new Variousfunds();
//        variousfunds.setOwner(tokenModel.getUserId());
        Calendar calendar = Calendar.getInstance();
//        int year = 0;
//        int month = calendar.get(Calendar.MONTH)+1;
//        if(month >= 1 && month <= 3) {
//            year = calendar.get(Calendar.YEAR) - 1;
//        }else {
//            year = calendar.get(Calendar.YEAR);
//        }
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
        List<Coststatistics> allCostList = coststatisticsMapper.getCoststatisticsBygroupid(Integer.valueOf(year), groupid);
        for ( Coststatistics c : allCostList ) {
            // 合计费用
            double totalmanhours = 0;
            // 合计工数
            double totalcost = 0;

            for ( int i = 1 ; i <= 12; i++ ) {
                // 单价
                double price = 0;
                String priceKey = c.getBpname() + c.getGroupid() +  "price" + i;
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
        int month = now.get(Calendar.MONTH)+1;
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
            String key = priceset.getUser_id() + priceset.getGroup_id() +"price" + i;
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
            String key = priceset.getUser_id() + priceset.getGroup_id() +  "price" + i;
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

    //gbb add 0804 月度赏与列表
    @Override
    public List<Coststatistics>  getcostMonthList(String dates,String role,String groupid,TokenModel tokenModel) throws Exception {
        List<String> groupIdList = new ArrayList<String>();
        String[] groupArray = null;
        if(StringUtils.isNotBlank(groupid))
        {
            groupArray = groupid.split(",");
            for(String group :groupArray)
            {
                groupIdList.add(group);
            }
        }
//        Query query = CustmizeQuery(new OrgTree());
//        // update gbb 20210325 查询组织架构添加【有效】条件 start
//        query.addCriteria(Criteria.where("status").is("0"));
//        // update gbb 20210325 查询组织架构添加【有效】条件 end
//        OrgTree orgTree = mongoTemplate.findOne(query, OrgTree.class);
//        List<OrgTree>  orgTrees =  orgTree.getOrgs();

//        if(role.equals("2")){
//            for (OrgTree org: orgTrees ) {
//                for(String group :groupArray)
//                {
//                    if(org.get_id().equals(group)){
//                        for (OrgTree org1: org.getOrgs() ) {
//                            groupIdList.add(org1.get_id());
//                        }
//                    }
//                }
//            }
//        }
//        else if(role.equals("4")){
//            for (OrgTree org: orgTrees ) {
//                for (OrgTree org1: org.getOrgs() ) {
//                    groupIdList.add(org1.get_id());
//                }
//            }
//        }
//        else{
//            for(String group :groupArray)
//            {
//                groupIdList.add(group);
//            }
//        }
        List<Coststatistics> costMonthList = coststatisticsMapper.getcostMonthList(dates,groupIdList);
        return costMonthList;
    }
    //gbb add 0804 月度赏与详情
    @Override
    public List<Map<String, String>>  getcostMonth(String dates,String role,String groupid,TokenModel tokenModel) throws Exception {
        List<String> groupIdList = new ArrayList<String>();
        Query query = CustmizeQuery(new OrgTree());
        // update gbb 20210325 查询组织架构添加【有效】条件 start
        query.addCriteria(Criteria.where("status").is("0"));
        // update gbb 20210325 查询组织架构添加【有效】条件 end
        OrgTree orgTree = mongoTemplate.findOne(query, OrgTree.class);
        List<OrgTree>  orgTrees =  orgTree.getOrgs();
        //外驻担当看全部
        if(role.equals("4")){
            for (OrgTree org: orgTrees ) {
                for (OrgTree org1: org.getOrgs() ) {
                    groupIdList.add(org1.get_id());
                }
            }
        }
        else{
            groupIdList.add(groupid);
        }
        //月份
        String months = String.valueOf(Integer.valueOf(dates.substring(dates.length() - 2,7)));
        //工数
        String manhour = "manhour" + months;
        //费用
        String cost = "cost" + months;
        //经费（3,6,9,12月有数据）
        String expense = "expense" + months;
        Integer groupIdListcount = groupIdList.size();
        List<Map<String, String>> dataList = new ArrayList<Map<String,String>>();
        String strGroupid = "";
        if(groupIdList.size() > 0){
            for(int i=0;i<groupIdList.size();i++ ){
                strGroupid = strGroupid + groupIdList.get(i) + ",";

                //region 事业计划费用
                List<Map<String, String>> databuList = new ArrayList<Map<String,String>>();
                Businessplan plan = new Businessplan();
                plan.setYear(dates.substring(0,4));
                plan.setGroup_id(groupIdList.get(i));
                List<Businessplan> planList = businessplanMapper.select(plan);
                if(planList.size() > 0){
                    JSONArray array1 = new JSONArray();
                    JSONArray array2 = new JSONArray();

                    if(!planList.get(0).getGroupB1().equals("")){
                        array1 = JSONArray.parseArray(planList.get(0).getGroupB1());
                    }
                    if(!planList.get(0).getGroupB2().equals("")){
                        array2 = JSONArray.parseArray(planList.get(0).getGroupB2());
                    }
                    if(array1.size() > 0){
                        for( int x = 0 ; x< array1.size() ; x++){
                            JSONObject objarray1 = array1.getJSONObject(x);
                            JSONObject objarray2 = array2.getJSONObject(x);
                            Map<String,String> map =new HashMap<String,String>();
                            //供应商ID
                            map.put("bpcompany",objarray1.getString("supplierinfor_id"));
                            BigDecimal objarray1number  = new BigDecimal(0);
                            BigDecimal objarray2number  = new BigDecimal(0);
                            BigDecimal objarray1money  = new BigDecimal(0);
                            BigDecimal objarray2money  = new BigDecimal(0);
                            if(objarray1.getString("number" + months) != null){
                                objarray1number = BigDecimal.valueOf(Double.valueOf(objarray1.getString("number" + months)));
                            }
                            if(objarray2.getString("number" + months) != null){
                                objarray2number = BigDecimal.valueOf(Double.valueOf(objarray2.getString("number" + months)));
                            }
                            if(objarray1.getString("money" + months) != null){
                                objarray1money = BigDecimal.valueOf(Double.valueOf(objarray1.getString("money" + months)));
                            }
                            if(objarray2.getString("money" + months) != null){
                                objarray2money = BigDecimal.valueOf(Double.valueOf(objarray2.getString("money" + months)));
                            }
                            String ex3manhour = String.valueOf(objarray1number.add(objarray2number));
                            String ex3cost = String.valueOf(objarray1money.add(objarray2money));
                            if(ex3manhour.equals("0") && (ex3cost.equals("0") || ex3cost.equals("0.0"))){
                                continue;
                            }
                            map.put("ex3manhour",ex3manhour);
                            map.put("ex3cost",ex3cost);
                            map.put("group_id",String.valueOf(planList.get(0).getGroup_id()));
                            databuList.add(map);
                        }
                    }
                }
                //endregion 事业计划费用

                List<Map<String, String>> data = coststatisticsMapper.getcostMonth(dates.substring(0,7),manhour,cost,expense,months,groupIdList.get(i));
                if(data.size() > 0){
                    if(i == 0){
                        dataList = data;
                        dataList.get(0).put("bpmanhourcount", String.valueOf(data.get(0).get("bpmanhourcount")));

                        //region 事业计划费用
                        if(databuList.size() > 0){
                            for(int j=0;j<dataList.size();j++ ){
                                for (Map<String, String> m : databuList){
                                    if(m.get("bpcompany").equals(dataList.get(j).get("bpcompany"))){
                                        //预计工数
                                        dataList.get(j).put("ex3manhour" + String.valueOf(i), m.get("ex3manhour"));
                                        //预计费用
                                        if(m.get("ex3cost").equals("0") ||m.get("ex3cost").equals("0.0")){
                                            dataList.get(j).put("ex3cost" + String.valueOf(i), "");
                                        }
                                        else{
                                            dataList.get(j).put("ex3cost" + String.valueOf(i), m.get("ex3cost"));
                                        }
                                        //预计费用合计
                                        BigDecimal ex3costcountList = BigDecimal.valueOf(Double.valueOf(String.valueOf(dataList.get(j).get("ex3costcount"))));
                                        BigDecimal ex3costm = BigDecimal.valueOf(Double.valueOf(m.get("ex3cost")));
                                        //预计总费用
                                        dataList.get(j).put("ex3costcount", String.valueOf(ex3costcountList.add(ex3costm)));
                                    }
                                }
                            }
                        }
                        //endregion 事业计划费用
                    }
                    else{
                        for(int j=0;j<dataList.size();j++ ){
                            if(dataList.get(j).get("bpcompany").equals(data.get(j).get("bpcompany"))){//会社名
                                //委任工数
                                dataList.get(j).put("ex1manhour" + String.valueOf(i), String.valueOf(data.get(j).get("ex1manhour0")));
                                //委任费用
                                dataList.get(j).put("ex1cost" + String.valueOf(i), String.valueOf(data.get(j).get("ex1cost0")));
                                //委任人数
                                dataList.get(j).put("ex1usercount" + String.valueOf(i), String.valueOf(data.get(j).get("ex1usercount0")));
                                //請負工数
                                dataList.get(j).put("ex2manhour" + String.valueOf(i), String.valueOf(data.get(j).get("ex2manhour0")));
                                //請負费用
                                dataList.get(j).put("ex2cost" + String.valueOf(i), String.valueOf(data.get(j).get("ex2cost0")));
                                //請負人数
                                dataList.get(j).put("ex2usercount" + String.valueOf(i), String.valueOf(data.get(j).get("ex2usercount0")));
                                //請負人数
                                dataList.get(j).put("costcount" + String.valueOf(i), String.valueOf(data.get(j).get("costcount0")));

                                //会社总工数
                                BigDecimal dataListmanhourcount = BigDecimal.valueOf(Double.valueOf(String.valueOf(dataList.get(j).get("bpmanhourcount"))));
                                BigDecimal bddatamanhourcount = BigDecimal.valueOf(Double.valueOf(String.valueOf(data.get(j).get("manhourcount0"))));
                                //会社总工数
                                dataList.get(j).put("bpmanhourcount", String.valueOf(dataListmanhourcount.add(bddatamanhourcount)));

                                //会社总费用
                                BigDecimal dataListcostcount = BigDecimal.valueOf(Double.valueOf(String.valueOf(dataList.get(j).get("bpcostcount"))));
                                BigDecimal bddatacostcount = BigDecimal.valueOf(Double.valueOf(String.valueOf(data.get(j).get("costcount0"))));
                                //会社总费用
                                dataList.get(j).put("bpcostcount", String.valueOf(dataListcostcount.add(bddatacostcount)));
                            }

                            //region 事业计划费用
                            if(databuList.size() > 0){
                                for (Map<String, String> m : databuList){
                                    if(m.get("bpcompany").equals(dataList.get(j).get("bpcompany"))){
                                        //预计工数
                                        dataList.get(j).put("ex3manhour" + String.valueOf(i), m.get("ex3manhour"));
                                        //预计费用
                                        if(m.get("ex3cost").equals("0") ||m.get("ex3cost").equals("0.0")){
                                            dataList.get(j).put("ex3cost" + String.valueOf(i), "");
                                        }
                                        else{
                                            dataList.get(j).put("ex3cost" + String.valueOf(i), m.get("ex3cost"));
                                        }
                                        //预计费用合计
                                        BigDecimal ex3costcountList = BigDecimal.valueOf(Double.valueOf(String.valueOf(dataList.get(j).get("ex3costcount"))));
                                        BigDecimal ex3costm = BigDecimal.valueOf(Double.valueOf(m.get("ex3cost")));
                                        //预计总费用
                                        String strex3costcount = String.valueOf(ex3costcountList.add(ex3costm));
                                        if(strex3costcount.equals("0.0")){
                                            strex3costcount = "0";
                                        }
                                        dataList.get(j).put("ex3costcount", strex3costcount);
                                    }
                                }
                            }
                            //endregion 事业计划费用
                        }
                    }
                }
            }
        }
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for(int x=0;x<dataList.size();x++ ){
            if(!String.valueOf(dataList.get(x).get("bpcostcount")).equals("0.0")){
                if(!String.valueOf(dataList.get(x).get("bpcostcount")).equals("0")){
                    Map<String,String> map = dataList.get(x);
                    list.add(map);
                }
            }
            if(groupIdListcount==1)
            {
                if(!String.valueOf(dataList.get(x).get("ex1usercount0")).equals("")
                        || !String.valueOf(dataList.get(x).get("ex2usercount0")).equals("")){
                    Map<String,String> map = dataList.get(x);
                    if(!list.contains(map))
                    {
                        list.add(map);
                    }
                }
            }
        }
        if(list.size() >0){
            list.get(0).put("groupnumber", String.valueOf(groupIdListcount));//group个数
            list.get(0).put("strgroupid", strGroupid);//groupid
        }
        return list;
    }

    //gbb add 0805 添加費用統計
    @Override
    public void insertcoststatisticsdetail(List<ArrayList> strData, TokenModel tokenModel) throws Exception {
        //外协岳父费用年月
        String strDates = "";
        //审批部门
        String strGroupid = "";
        //审批部门名称
        String strGroupname = "";
        List<Map<String, String>> strDatanew = strData.get(0);
        if(strDatanew.size() > 0){
            for(int i=0;i<strDatanew.size();i++ ){
                Coststatisticsdetail detail = new Coststatisticsdetail();

                //部门
                strGroupid = strDatanew.get(i).get("groupid");
                detail.setGroupid(String.valueOf(strGroupid));
                //部门名称
                strGroupname = strDatanew.get(i).get("groupname");
                detail.setGroupname(String.valueOf(strGroupname));
                //供应商
                detail.setSupplierinforid(strDatanew.get(i).get("bpcompany"));
                //年月
                strDates = strDatanew.get(i).get("dates");
                detail.setDates(strDates);
                //总费用
                detail.setCost(String.valueOf(strDatanew.get(i).get("bpcostcount")));
                //主键
                detail.setCoststatisticsdetail_id(UUID.randomUUID().toString());

                detail.preInsert(tokenModel);

                coststatisticsdetailMapper.insert(detail);
            }
        }
        String pp[] = strDates.split("-");

        List<String> groupIdList = new ArrayList<String>();
//        Query query = CustmizeQuery(new OrgTree());
//        OrgTree orgTree = mongoTemplate.findOne(query, OrgTree.class);
//        List<OrgTree>  orgTrees =  orgTree.getOrgs();
//        for (OrgTree org: orgTrees ) {
//            for (OrgTree org1: org.getOrgs() ) {
//                groupIdList.add(org1.get_id() + "," + pp[0] +  "," + pp[1]);
//            }
//        }
        Expatriatesinfor infor = new Expatriatesinfor();
        List<Expatriatesinfor> inforlist = expatriatesinforMapper.select(infor);
        Map<String,List<Expatriatesinfor>> userGroupMap = inforlist.stream().collect(Collectors.groupingBy(Expatriatesinfor::getGroup_id));
        for (String key : userGroupMap.keySet()) {
            groupIdList.add(key + "," + pp[0] +  "," + pp[1]);
        }
        //查询所有审批通过的部门
        List<Workflowinstance> workflow = coststatisticsMapper.getworkflowinstance(groupIdList);
        if(workflow.size() == groupIdList.size()){
            // 创建代办
            ToDoNotice toDoNotice = new ToDoNotice();
            List<String> params = new ArrayList<String>();
            toDoNotice.setTitle("本月费用总览已全部审批完毕，可生成合同！");
            toDoNotice.setInitiator(tokenModel.getUserId());
            toDoNotice.setContent("本月费用总览已全部审批完毕，可生成合同！");
            toDoNotice.setDataid(strGroupid);
            toDoNotice.setUrl("/PFANS6010View");
            toDoNotice.setWorkflowurl("/PFANS6010View");
            toDoNotice.preInsert(tokenModel);
            //给合同担当发待办
            List<MembersVo> rolelist = roleService.getMembers("5e7862618f43163084351135");
            if(rolelist.size() > 0)
            {
                toDoNotice.setOwner(rolelist.get(0).getUserid());
            }
            toDoNoticeService.save(toDoNotice);
        }
    }

    //0807 check是否已经生成个别合同
    @Override
    public int checkcontract(Contractapplication contract) throws Exception {
        int count = 0;
        List<Contractapplication> contractList = contractapplicationMapper.select(contract);
        if(contractList.size() > 0){
            return 1;
        }
        return 0;
    }
}
