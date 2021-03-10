package com.nt.service_pfans.PFANS1000.Impl;

import cn.hutool.json.JSON;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.druid.support.json.JSONParser;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mysql.jdbc.StringUtils;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_Org.OrgTree;
import com.nt.dao_Pfans.PFANS1000.Contractapplication;
import com.nt.dao_Pfans.PFANS1000.ProjectIncome;
import com.nt.dao_Pfans.PFANS1000.Vo.*;
import com.nt.dao_Pfans.PFANS2000.PersonalCost;
import com.nt.dao_Pfans.PFANS2000.PersonalCostYears;
import com.nt.dao_Pfans.PFANS5000.CompanyProjects;
import com.nt.dao_Pfans.PFANS5000.LogManagement;
import com.nt.dao_Pfans.PFANS5000.PersonalProjects;
import com.nt.dao_Pfans.PFANS5000.ProjectContract;
import com.nt.dao_Pfans.PFANS6000.Coststatistics;
import com.nt.dao_Pfans.PFANS6000.CoststatisticsVo;
import com.nt.dao_Pfans.PFANS6000.Priceset;
import com.nt.dao_Pfans.PFANS6000.PricesetGroup;
import com.nt.service_Org.DictionaryService;
import com.nt.service_Org.OrgTreeService;
import com.nt.service_pfans.PFANS1000.ProjectIncomeService;
import com.nt.service_pfans.PFANS1000.mapper.ContractapplicationMapper;
import com.nt.service_pfans.PFANS1000.mapper.EvectionMapper;
import com.nt.service_pfans.PFANS1000.mapper.ProjectIncomeMapper;
import com.nt.service_pfans.PFANS1000.mapper.PublicExpenseMapper;
import com.nt.service_pfans.PFANS2000.mapper.PersonalCostMapper;
import com.nt.service_pfans.PFANS2000.mapper.PersonalCostYearsMapper;
import com.nt.service_pfans.PFANS5000.mapper.ComProjectMapper;
import com.nt.service_pfans.PFANS5000.mapper.CompanyProjectsMapper;
import com.nt.service_pfans.PFANS5000.mapper.LogManagementMapper;
import com.nt.service_pfans.PFANS5000.mapper.ProjectContractMapper;
import com.nt.service_pfans.PFANS6000.mapper.PricesetGroupMapper;
import com.nt.service_pfans.PFANS6000.mapper.PricesetMapper;
import com.nt.utils.LogicalException;
import com.nt.utils.dao.TokenModel;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class ProjectIncomeServiceImpl implements ProjectIncomeService {
    @Autowired
    private ContractapplicationMapper contractapplicationMapper;
    @Autowired
    private LogManagementMapper logmanagementmapper;
    @Autowired
    private EvectionMapper evectionMapper;
    @Autowired
    private PublicExpenseMapper publicExpenseMapper;
    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private OrgTreeService orgTreeService;
    @Autowired
    private PersonalCostMapper personalcostmapper;
    @Autowired
    private PersonalCostYearsMapper personalcostyearsmapper;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private PricesetMapper pricesetMapper;
    @Autowired
    private ProjectIncomeMapper projectincomemapper;
    @Autowired
    private CompanyProjectsMapper companyprojectsMapper;
    @Autowired
    private ProjectContractMapper projectcontractmapper;
    @Autowired
    private ComProjectMapper comProjectMapper;
    @Autowired
    private PricesetGroupMapper pricesetGroupMapper;


    @Override
    public XSSFWorkbook downloadExcel(String projectincomeid, HttpServletRequest request, HttpServletResponse resp) throws LogicalException {
        try {
            // 导出文件
            ProjectIncome projectincome = new ProjectIncome();
            projectincome.setProjectincomeid(projectincomeid);
            ProjectIncome project = projectincomemapper.select(projectincome).get(0);
            InputStream in = null;
            FileOutputStream f = null;
            XSSFWorkbook work = null;
            //表格操作
            in = getClass().getClassLoader().getResourceAsStream("jxls_templates/xiangmujiezhuanbiao.xlsx");
            work = new XSSFWorkbook(in);
            XSSFSheet sheet1 = work.getSheetAt(0);
            //将数据放入Excel
            JSONArray jsonArray = JSONArray.parseArray(project.getProjectincomevo1());
            JSONArray jsonArray1 = JSONArray.parseArray(project.getProjectincomevo5());
            int i = -1;
            int k = 0;
            int q = -1;
            int g = -1;

            String[] s = new String[100];
            int[] shu = new int[5];
            XSSFRow row = sheet1.createRow(0);
            for (Object ob : jsonArray) {
                i += 3;
                q++;
                CellRangeAddress callRangeAddress = new CellRangeAddress(0, 0, i, i + 2);
                sheet1.addMergedRegion(callRangeAddress);
                String companyproject = getProperty(ob, "companyproject");
                s[q] = companyproject;
                row.createCell(0).setCellValue("姓名");
                row.createCell(1).setCellValue("人件费");
            }
            for (int h = 0; h < s.length; h++) {
                g += 3;
                row.createCell(g).setCellValue(s[h]);
            }
            for (Object ob1 : jsonArray1) {
                int m = -2;
                int n = -1;
                k++;
                XSSFRow row1 = sheet1.createRow(k);
                for (Object ob : jsonArray) {
                    n++;
                    m += 3;
                    String type = getProperty(ob1, "type");
                    String works = getProperty(ob1, "works" + n);
                    String cost = getProperty(ob1, "cost" + n);
                    String radio = getProperty(ob1, "radio" + n);
                    if (!type.equals("3")) {
                        row1.createCell(m + 1).setCellValue(works);
                        row1.createCell(m + 2).setCellValue(radio);
                        row1.createCell(m + 3).setCellValue(cost);
                    }
                }
                String name = getProperty(ob1, "name");
                String money = getProperty(ob1, "money");
                row1.createCell(0).setCellValue(name);
                row1.createCell(1).setCellValue(money);
            }

            CellRangeAddress callRangeAddress0 = new CellRangeAddress(k - 4, k - 4, 0, 1);
            XSSFRow row0 = sheet1.createRow(k - 4);
            sheet1.addMergedRegion(callRangeAddress0);
            row0.createCell(0).setCellValue("人件费合计");
            CellRangeAddress callRangeAddress1 = new CellRangeAddress(k - 3, k - 3, 0, 1);
            XSSFRow row11 = sheet1.createRow(k - 3);
            sheet1.addMergedRegion(callRangeAddress1);
            row11.createCell(0).setCellValue("外注费合计");
            CellRangeAddress callRangeAddress2 = new CellRangeAddress(k - 2, k - 2, 0, 1);
            XSSFRow row2 = sheet1.createRow(k - 2);
            sheet1.addMergedRegion(callRangeAddress2);
            row2.createCell(0).setCellValue("经费合计");
            CellRangeAddress callRangeAddress3 = new CellRangeAddress(k - 1, k - 1, 0, 1);
            XSSFRow row3 = sheet1.createRow(k - 1);
            sheet1.addMergedRegion(callRangeAddress3);
            row3.createCell(0).setCellValue("合计支出");
            CellRangeAddress callRangeAddress4 = new CellRangeAddress(k, k, 0, 1);
            XSSFRow row4 = sheet1.createRow(k);
            sheet1.addMergedRegion(callRangeAddress4);
            row4.createCell(0).setCellValue("合计收入");
            int l = k - 5;
            for (Object ob1 : jsonArray1) {
                int m = -2;
                int n = -1;
                int p = 0;
                for (Object ob : jsonArray) {
                    p++;
                    n++;
                    m += 3;
                    String type = getProperty(ob1, "type");
                    String works = getProperty(ob1, "works" + n);
                    String cost = getProperty(ob1, "cost" + n);
                    String radio = getProperty(ob1, "radio" + n);
                    if (type.equals("3")) {
                        if (p == 1) {
                            l++;
                        }
                        if (l == k - 4) {
                            row0.createCell(m + 1).setCellValue(works);
                            row0.createCell(m + 2).setCellValue(radio);
                            row0.createCell(m + 3).setCellValue(cost);
                        } else if (l == k - 3) {
                            row11.createCell(m + 1).setCellValue(works);
                            row11.createCell(m + 2).setCellValue(radio);
                            row11.createCell(m + 3).setCellValue(cost);
                        } else if (l == k - 2) {
                            row2.createCell(m + 1).setCellValue(works);
                            row2.createCell(m + 2).setCellValue(radio);
                            row2.createCell(m + 3).setCellValue(cost);
                        } else if (l == k - 1) {
                            row3.createCell(m + 1).setCellValue(works);
                            row3.createCell(m + 2).setCellValue(radio);
                            row3.createCell(m + 3).setCellValue(cost);
                        } else if (l == k) {
                            row4.createCell(m + 1).setCellValue(works);
                            row4.createCell(m + 2).setCellValue(radio);
                            row4.createCell(m + 3).setCellValue(cost);
                        }
                    }
                }
            }
            return work;
        } catch (Exception e) {
            throw new LogicalException(e.getMessage());
        }
    }

    @Override
    public ProjectIncome selectById(String projectincomeid) throws Exception {
        ProjectIncome projectincome = new ProjectIncome();
        projectincome.setProjectincomeid(projectincomeid);
        return projectincomemapper.select(projectincome).get(0);
    }

    private String getProperty(Object o, String key) throws Exception {
        try {
            return org.apache.commons.beanutils.BeanUtils.getProperty(o, key);
        } catch (Exception e) {
            throw new LogicalException(e.getMessage());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public List<String> importUser(HttpServletRequest request, TokenModel tokenModel) throws Exception {
        try {
            List<String> Result = new ArrayList<String>();
            MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
            File f = null;
            f = File.createTempFile("tmp", null);
            file.transferTo(f);
            ExcelReader reader = ExcelUtil.getReader(f);
            List<List<Object>> list = reader.read();
            List<Object> model = new ArrayList<Object>();
            model.add("厚生費");
            model.add("オフィス家賃");
            model.add("リース費");
            model.add("出向者賃借料");
            model.add("その他(固定費)");
            model.add("研究材料費");
            model.add("原動費");
            model.add("旅費交通費");
            model.add("通信費");
            model.add("消耗品費");
            model.add("会議費/交際費/研修費");
            model.add("共同事務費");
            model.add("ブランド使用料");
            model.add("その他経費");
            model.add("外注費");
            model.add("減価償却費（設備）");
            model.add("減価償却費（ソフト）");
            List<Object> key = list.get(0);
            for (int i = 1; i < key.size(); i++) {
                if (!key.get(i).toString().trim().equals(model.get(i - 1))) {
                    throw new LogicalException("第" + (i) + "列标题错误，应为" + model.get(i - 1).toString());
                }
            }
            int n = 1;
            int accesscount = 0;
            int error = 0;
            List<Map<String, String>> listmap = new ArrayList<>();
            for (int i = 1; i < list.size(); i++) {
                Map<String, String> map = new HashMap<>();
                List<Object> value = list.get(n);
                n++;
                if (value != null && !value.isEmpty()) {
                    if (value.get(0).toString().equals("")) {
                        continue;
                    }
                }
                if (value.size() > 1) {
                    ProjectIncome projectincome = new ProjectIncome();
                    projectincome.setEncoding(value.get(0).toString().substring(0, 2));
                    List<ProjectIncome> projectincomelist = projectincomemapper.select(projectincome);
                    if (projectincomelist.size() == 0) {
                        error = error + 1;
                        Result.add("模板第" + (n - 1) + "行的部门未创建项目结转表，请创建数据，导入失败");
                        map.put("delete", value.get(0).toString());
                        listmap.add(map);
                        continue;
                    }
                }
            }
            if (listmap.size() > 0) {
                for (int k = 0; k < listmap.size(); k++) {
                    for (int m = 1; m < list.size(); m++) {
                        List<Object> value = list.get(m);
                        for (Map<String, String> map : listmap) {
                            for (String s : map.values()) {
                                if (value.get(0).toString().equals(s)) {
                                    list.remove(m);
                                }
                            }
                        }
                    }
                }
            }
            int m = 1;
            for (int i = 1; i < list.size(); i++) {
                List<Map<String, String>> lists = new ArrayList<>();
                List<Object> value = list.get(m);
                m++;
                if (value.size() > 1) {
                    ProjectIncome projectincome = new ProjectIncome();
                    projectincome.setEncoding(value.get(0).toString().substring(0, 2));
                    List<ProjectIncome> projectincomelist = projectincomemapper.select(projectincome);
                    if (projectincomelist.size() > 0) {
                        String name = "";
                        String nameid = "";
                        String money = "";
                        String type = "";
                        int scale = 2;//设置位数
                        int roundingMode = 4;//表示四舍五入，可以选择其他舍值方式，例如去尾，等等.
                        JSONArray jsonArray = JSONArray.parseArray(projectincomelist.get(0).getProjectincomevo2());
                        for (Object ob : jsonArray) {
                            Map<String, String> map = new HashMap<>();
                            name = getProperty(ob, "name");
                            nameid = getProperty(ob, "nameid");
                            money = getProperty(ob, "money");
                            type = getProperty(ob, "type");
                            BigDecimal bd2 = new BigDecimal("0");
                            if (money == null) {
                                money = "0";
                            }
                            if (name != null) {
                                if (name.equals("厚生費")) {
                                    BigDecimal bd = new BigDecimal(money);
                                    BigDecimal bd1 = new BigDecimal(value.get(1).toString());
                                    bd2 = bd.add(bd1).setScale(scale, roundingMode);
                                } else if (name.equals("オフィス家賃")) {
                                    BigDecimal bd = new BigDecimal(money);
                                    BigDecimal bd1 = new BigDecimal(value.get(2).toString());
                                    bd2 = bd.add(bd1).setScale(scale, roundingMode);
                                } else if (name.equals("リース費")) {
                                    BigDecimal bd = new BigDecimal(money);
                                    BigDecimal bd1 = new BigDecimal(value.get(3).toString());
                                    bd2 = bd.add(bd1).setScale(scale, roundingMode);
                                } else if (name.equals("出向者賃借料")) {
                                    BigDecimal bd = new BigDecimal(money);
                                    BigDecimal bd1 = new BigDecimal(value.get(4).toString());
                                    bd2 = bd.add(bd1).setScale(scale, roundingMode);
                                } else if (name.equals("その他(固定費)")) {
                                    BigDecimal bd = new BigDecimal(money);
                                    BigDecimal bd1 = new BigDecimal(value.get(5).toString());
                                    bd2 = bd.add(bd1).setScale(scale, roundingMode);
                                } else if (name.equals("研究材料費")) {
                                    BigDecimal bd = new BigDecimal(money);
                                    BigDecimal bd1 = new BigDecimal(value.get(6).toString());
                                    bd2 = bd.add(bd1).setScale(scale, roundingMode);
                                } else if (name.equals("原動費")) {
                                    BigDecimal bd = new BigDecimal(money);
                                    BigDecimal bd1 = new BigDecimal(value.get(7).toString());
                                    bd2 = bd.add(bd1).setScale(scale, roundingMode);
                                } else if (name.equals("旅費交通費")) {
                                    BigDecimal bd = new BigDecimal(money);
                                    BigDecimal bd1 = new BigDecimal(value.get(8).toString());
                                    bd2 = bd.add(bd1).setScale(scale, roundingMode);
                                } else if (name.equals("通信費")) {
                                    BigDecimal bd = new BigDecimal(money);
                                    BigDecimal bd1 = new BigDecimal(value.get(9).toString());
                                    bd2 = bd.add(bd1).setScale(scale, roundingMode);
                                } else if (name.equals("消耗品費")) {
                                    BigDecimal bd = new BigDecimal(money);
                                    BigDecimal bd1 = new BigDecimal(value.get(10).toString());
                                    bd2 = bd.add(bd1).setScale(scale, roundingMode);
                                } else if (name.equals("会議費/交際費/研修費")) {
                                    BigDecimal bd = new BigDecimal(money);
                                    BigDecimal bd1 = new BigDecimal(value.get(11).toString());
                                    bd2 = bd.add(bd1).setScale(scale, roundingMode);
                                } else if (name.equals("共同事務費")) {
                                    BigDecimal bd = new BigDecimal(money);
                                    BigDecimal bd1 = new BigDecimal(value.get(12).toString());
                                    bd2 = bd.add(bd1).setScale(scale, roundingMode);
                                } else if (name.equals("ブランド使用料")) {
                                    BigDecimal bd = new BigDecimal(money);
                                    BigDecimal bd1 = new BigDecimal(value.get(13).toString());
                                    bd2 = bd.add(bd1).setScale(scale, roundingMode);
                                } else if (name.equals("その他経費")) {
                                    BigDecimal bd = new BigDecimal(money);
                                    BigDecimal bd1 = new BigDecimal(value.get(14).toString());
                                    bd2 = bd.add(bd1).setScale(scale, roundingMode);
                                } else if (name.equals("外注費")) {
                                    BigDecimal bd = new BigDecimal(money);
                                    BigDecimal bd1 = new BigDecimal(value.get(15).toString());
                                    bd2 = bd.add(bd1).setScale(scale, roundingMode);
                                } else if (name.equals("減価償却費（設備）")) {
                                    BigDecimal bd = new BigDecimal(money);
                                    BigDecimal bd1 = new BigDecimal(value.get(16).toString());
                                    bd2 = bd.add(bd1).setScale(scale, roundingMode);
                                } else if (name.equals("減価償却費（ソフト）")) {
                                    BigDecimal bd = new BigDecimal(money);
                                    BigDecimal bd1 = new BigDecimal(value.get(17).toString());
                                    bd2 = bd.add(bd1).setScale(scale, roundingMode);
                                }
                            }
                            map.put("name", name);
                            map.put("nameid", nameid);
                            map.put("money", bd2.toString());
                            map.put("type", type);
                            lists.add(map);
                        }
                        ProjectIncome pro = new ProjectIncome();
                        BeanUtils.copyProperties(projectincomelist.get(0), pro);
                        projectincomemapper.delete(pro);
                        ProjectIncome project = new ProjectIncome();
                        BeanUtils.copyProperties(projectincomelist.get(0), project);
                        project.preInsert(tokenModel);
                        project.setProjectincomevo2(JSONArray.toJSONString(lists));
                        projectincomemapper.insert(project);
                        accesscount = accesscount + 1;
                    }
                }
            }
            Result.add("失败数：" + error);
            Result.add("成功数：" + accesscount);
            return Result;
        } catch (Exception e) {
            throw new LogicalException("导入失败");
        }
    }


    @Override
    public List<ProjectIncome> get(ProjectIncome projectincome) throws Exception {
        List<ProjectIncome> projectincomelist = projectincomemapper.select(projectincome);
        List<ProjectIncome> projectlist = new ArrayList<>();
        List<ProjectIncomeVo4> projectincomevo4list = new ArrayList<>();
        List<ProjectIncomeVo4> projectincomevo5list = new ArrayList<>();
        OrgTree orgs = orgTreeService.get(new OrgTree());
        for (OrgTree org : orgs.getOrgs()) {
            for (OrgTree org1 : org.getOrgs()) {
                ProjectIncomeVo4 projectincomevo4 = new ProjectIncomeVo4();
                projectincomevo4.setGroupid(org1.get_id());
                projectincomevo4.setGroupname(org1.getCompanyname());
                projectincomevo4list.add(projectincomevo4);
                projectincomevo5list.add(projectincomevo4);
            }
        }
        for (ProjectIncome Project : projectincomelist) {
            projectincomevo4list = projectincomevo5list.stream().filter(item -> (item.getGroupid().equals(Project.getGroup_id()))).collect(Collectors.toList());
            Project.setGroup_id(projectincomevo4list.get(0).getGroupname());
            projectlist.add(Project);
        }
        return projectlist;
    }

    @Override
    public void insert(ProjectIncome projectincome, TokenModel tokenModel) throws Exception {
        String projectincomeid = projectincome.getProjectincomeid();
        if (StringUtils.isNullOrEmpty(projectincomeid)) {
            ProjectIncome project = new ProjectIncome();
            project.setYear(projectincome.getYear());
            project.setMonth(projectincome.getMonth());
            project.setGroup_id(projectincome.getGroup_id());
            List<ProjectIncome> projectincomelist = projectincomemapper.select(project);
            if (projectincomelist.size() > 0) {
                throw new LogicalException("本部门该年度项目结转表已经创建，请到列表页中查找编辑。");
            }
            projectincome.setProjectincomeid(UUID.randomUUID().toString());
            projectincome.preInsert(tokenModel);
            projectincomemapper.insert(projectincome);
        } else {
            ProjectIncome project = new ProjectIncome();
            project.setYear(projectincome.getYear());
            project.setMonth(projectincome.getMonth());
            project.setGroup_id(projectincome.getGroup_id());
            List<ProjectIncome> projectincomelist = projectincomemapper.select(project);
            projectincomelist = projectincomelist.stream().filter(item -> (!item.getProjectincomeid().equals(projectincomeid))).collect(Collectors.toList());
            if (projectincomelist.size() > 0) {
                throw new LogicalException("本部门该年度事业计划已经创建，请到列表页中查找编辑。");
            }
//            ProjectIncome project1 = new ProjectIncome();
//            project1.setProjectincomeid(projectincomeid);
//            projectincomemapper.delete(project1);
//            ProjectIncome pro = new ProjectIncome();
//            BeanUtils.copyProperties(projectincome, pro);
//            pro.setProjectincomeid(UUID.randomUUID().toString());
//            pro.preInsert(tokenModel);
//            projectincomemapper.insert(pro);
            projectincome.preUpdate(tokenModel);
            projectincomemapper.updateByPrimaryKeySelective(projectincome);
        }
    }

    @Override
    public ProjectIncomeVo3 getprojects(String groupid, String userid, String year, String month) throws Exception {
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMM");
        List<ProjectIncomeVo1> projectincomevo1list = new ArrayList<>();
        List<ProjectIncomeVo2> projectincomevo2list = new ArrayList<>();
        List<ProjectIncomeVo4> projectincomevo4list = new ArrayList<>();
        List<Map<String, String>> lists = new ArrayList<>();
        OrgTree orgs = orgTreeService.get(new OrgTree());
        for (OrgTree org : orgs.getOrgs()) {
            for (OrgTree org1 : org.getOrgs()) {
                ProjectIncomeVo4 projectincomevo4 = new ProjectIncomeVo4();
                projectincomevo4.setGroupid(org1.get_id());
                projectincomevo4.setGroupname(org1.getCompanyname());
                projectincomevo4.setEncoding(org1.getEncoding().substring(0, 2));
                projectincomevo4list.add(projectincomevo4);
            }
        }
        if (groupid.equals("")) {
            groupid = projectincomevo4list.get(0).getGroupid();
        }
        //PJ起案相应项目获取
        String check = year + month;
        CompanyProjects companyprojects = new CompanyProjects();
        companyprojects.setGroup_id(groupid);
        companyprojects.setStatus("4");
        List<CompanyProjects> companyProjectsList = companyprojectsMapper.select(companyprojects);
        if (companyProjectsList.size() > 0) {
            for (CompanyProjects companyprojectslist : companyProjectsList) {
                String id = companyprojectslist.getCompanyprojects_id();
                List<ProjectContract> projectcontractlist = projectincomemapper.getprojectcontract(id, year, month);
                if (projectcontractlist.size() > 0) {
                    BigDecimal contractamount = new BigDecimal("0");
                    BigDecimal contractrequestamount = new BigDecimal("0");
                    int i = 0;
                    for (ProjectContract projectcontract : projectcontractlist) {
                        Contractapplication contractapplication = new Contractapplication();
                        contractapplication.setContractnumber(projectcontract.getContract());
                        List<Contractapplication> coList = contractapplicationMapper.select(contractapplication);
                        if (coList.size() > 0) {
                            if (coList.get(0).getExtensiondate() == null) {
                                String[] claimdatetime = coList.get(0).getClaimdatetime().split("~");
                                String claimdate = claimdatetime[1].replace("-", "");
                                if (claimdate.contains(check)) {
                                    i++;
                                }
                            } else {
                                String extensiondate = coList.get(0).getExtensiondate().toString().replace("-", "");
                                if (extensiondate.contains(check)) {
                                    i++;
                                }
                            }
                        }
                        BigDecimal contractamounts = new BigDecimal(projectcontract.getContractamount());
                        contractamount = contractamount.add(contractamounts);
                        BigDecimal contractrequestamounts = new BigDecimal(projectcontract.getContractrequestamount());
                        contractrequestamount = contractrequestamounts.add(contractrequestamounts);
                    }
                    ProjectIncomeVo1 projectincomevo1 = new ProjectIncomeVo1();

                    StringBuilder ab = new StringBuilder();
                    ab.append(companyprojectslist.getProject_name()).append("_自主投资");
                    if (companyprojectslist.getToolstype().equals("0")) {
                        projectincomevo1.setCompanyproject(String.valueOf(companyprojectslist.getProject_name()));
                        projectincomevo1.setContractamount(String.valueOf(contractamount));
                    } else {
                        projectincomevo1.setCompanyproject(String.valueOf(ab));
                        projectincomevo1.setContractamount(String.valueOf(contractrequestamount));
                    }
                    if (projectcontractlist.size() == i) {
                        projectincomevo1.setType("0");
                    } else {
                        projectincomevo1.setType("1");
                    }
                    projectincomevo1.setCompanyprojectid(companyprojectslist.getCompanyprojects_id());
                    projectincomevo1.setTheme(projectcontractlist.get(0).getTheme());
                    projectincomevo1list.add(projectincomevo1);
                }

            }
        }
        //共同部署相应项目获取
//        Comproject comproject = new Comproject();
//        comproject.setGroup_id(groupid);
//        comproject.setStatus("4");
//        List<Comproject> comprojectlist = comProjectMapper.select(comproject);
//        if (comprojectlist.size() > 0) {
//            for (Comproject comlist : comprojectlist) {
//                SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
//                String starrdata = sf1.format(comlist.getStartdate());
//                String enddate = sf1.format(comlist.getEnddate());
//                String yearmonth = year + month + "01";
//                if (Integer.valueOf(starrdata) <= Integer.valueOf(yearmonth) && Integer.valueOf(yearmonth) <= Integer.valueOf(enddate)) {
//                    ProjectIncomeVo1 projectincomevo1 = new ProjectIncomeVo1();
//                    projectincomevo1.setCompanyproject(comlist.getProject_name());
//                    projectincomevo1.setCompanyprojectid(comlist.getComproject_id());
//                    projectincomevo1.setContractamount("0");
//                    projectincomevo1list.add(projectincomevo1);
//                }
//            }
//        }
        ProjectIncomeVo1 projectincomevo1 = new ProjectIncomeVo1();
        projectincomevo1.setCompanyproject("共通项目（会议研修等）");
        projectincomevo1.setCompanyprojectid("PP024001");
        projectincomevo1.setContractamount("0.00");
        projectincomevo1list.add(projectincomevo1);
        //经费
        String name1 = "PJ111";
        List<com.nt.dao_Org.Dictionary> curListA = dictionaryService.getForSelect(name1);
        for (Dictionary iteA : curListA) {
            ProjectIncomeVo2 projectincomevo2 = new ProjectIncomeVo2();
            projectincomevo2.setNameid(iteA.getCode());
            projectincomevo2.setName(iteA.getValue1());
            projectincomevo2.setType("2");
            projectincomevo2list.add(projectincomevo2);
        }
        //人件费
        PersonalCostYears personalcostyears = new PersonalCostYears();
        personalcostyears.setYears(year);
        List<PersonalCostYears> personalcostyearslist = personalcostyearsmapper.select(personalcostyears);
        if (personalcostyearslist.size() > 0) {
            PersonalCost personalcost = new PersonalCost();
            personalcost.setYearsantid(personalcostyearslist.get(0).getYearsantid());
            personalcost.setGroupid(groupid);
            List<PersonalCost> personalcostlist = personalcostmapper.select(personalcost);
            for (PersonalCost personallist : personalcostlist) {
                ProjectIncomeVo2 projectincomevo2 = new ProjectIncomeVo2();
                projectincomevo2.setNameid(personallist.getUserid());
                Query query = new Query();
                query.addCriteria(Criteria.where("userid").is(personallist.getUserid()));
                CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                if (customerInfo != null) {
                    projectincomevo2.setName(customerInfo.getUserinfo().getCustomername());
                } else {
                    projectincomevo2.setName("");
                }
                projectincomevo2.setType("1");
                if (month.equals("01")) {
                    projectincomevo2.setMoney(personallist.getJanTrue());
                } else if (month.equals("02")) {
                    projectincomevo2.setMoney(personallist.getFebTrue());
                } else if (month.equals("03")) {
                    projectincomevo2.setMoney(personallist.getMarTrue());
                } else if (month.equals("04")) {
                    projectincomevo2.setMoney(personallist.getAprilTrue());
                } else if (month.equals("05")) {
                    projectincomevo2.setMoney(personallist.getMayTrue());
                } else if (month.equals("06")) {
                    projectincomevo2.setMoney(personallist.getJuneTrue());
                } else if (month.equals("07")) {
                    projectincomevo2.setMoney(personallist.getJulyTrue());
                } else if (month.equals("08")) {
                    projectincomevo2.setMoney(personallist.getAugTrue());
                } else if (month.equals("09")) {
                    projectincomevo2.setMoney(personallist.getSepTrue());
                } else if (month.equals("10")) {
                    projectincomevo2.setMoney(personallist.getOctTrue());
                } else if (month.equals("11")) {
                    projectincomevo2.setMoney(personallist.getNoveTrue());
                } else if (month.equals("12")) {
                    projectincomevo2.setMoney(personallist.getDeceTrue());
                }
                projectincomevo2list.add(projectincomevo2);
            }
            ProjectIncomeVo2 projectincomevo2 = new ProjectIncomeVo2();
            projectincomevo2.setNameid("111111");
            projectincomevo2.setName("调整项目");
            projectincomevo2.setMoney("0");
            projectincomevo2.setType("1");
            projectincomevo2list.add(projectincomevo2);
        }
        //外注费
        String yearmonth = year + "-" + month;
        PricesetGroup pricesetGroup = new PricesetGroup();
        pricesetGroup.setPd_date(yearmonth);
        List<PricesetGroup> pricesetgrouplist = pricesetGroupMapper.select(pricesetGroup);
        if (pricesetgrouplist.size() > 0) {
            String pricesetgroupid = pricesetgrouplist.get(0).getPricesetgroup_id();
            Priceset priceset = new Priceset();
            priceset.setGroup_id(groupid);
            priceset.setPricesetgroup_id(pricesetgroupid);
            List<Priceset> pricesetlist = pricesetMapper.select(priceset);
            for (Priceset prilist : pricesetlist) {
                ProjectIncomeVo2 projectincomevo2 = new ProjectIncomeVo2();
                projectincomevo2.setName(prilist.getUsername());
                projectincomevo2.setNameid(prilist.getUser_id());
                projectincomevo2.setMoney(prilist.getTotalunit());
                projectincomevo2.setType("0");
                projectincomevo2list.add(projectincomevo2);
            }
        }
        for (int i = 0; i < 5; i++) {
            ProjectIncomeVo2 projectincomevo2 = new ProjectIncomeVo2();
            projectincomevo2.setType("3");
            projectincomevo2list.add(projectincomevo2);
        }


        int scale = 2;//设置位数
        int roundingMode = 4;//表示四舍五入，可以选择其他舍值方式，例如去尾，等等.
        List<ProjectIncomeVo2> type0 = projectincomevo2list.stream().filter(item -> (item.getType().equals("2"))).collect(Collectors.toList());
        List<ProjectIncomeVo2> type1 = projectincomevo2list.stream().filter(item -> (item.getType().equals("1"))).collect(Collectors.toList());
        List<ProjectIncomeVo2> type2 = projectincomevo2list.stream().filter(item -> (item.getType().equals("0"))).collect(Collectors.toList());
        for (ProjectIncomeVo2 list0 : type0) {
            Map<String, String> map = new HashMap<>();
            map.put("name", list0.getName());
            map.put("nameid", list0.getNameid());
            map.put("money", list0.getMoney());
            map.put("type", list0.getType());
            int i = -1;
            for (ProjectIncomeVo1 projectlist : projectincomevo1list) {
                i++;
                String key1 = "works" + i;
                String key2 = "cost" + i;
                String key3 = "radio" + i;
                map.put(key1, "----------");
                map.put(key2, "0.00");
                map.put(key3, "0.00%");
            }
            lists.add(map);
        }
//        BigDecimal ratioall = new BigDecimal("0");
//        BigDecimal ratioalls = new BigDecimal("0");
//        for (ProjectIncomeVo2 list1 : type1) {
//            for (ProjectIncomeVo1 projectlist : projectincomevo1list) {
//                String projectid = projectlist.getCompanyprojectid();
//                String createby = list1.getNameid();
//                List<LogManagement> logmanagementlist = projectincomemapper.getlogmanagement(projectid, createby, year, month);
//                if (logmanagementlist.size() > 0) {
//                    ratioall = new BigDecimal(logmanagementlist.get(0).getTime_start());
//                    ratioalls = ratioall.add(ratioall);
//                }
//            }
//        }
//        BigDecimal ratio2 = new BigDecimal("0");
//        BigDecimal ratio2s = new BigDecimal("0");
//        for (ProjectIncomeVo2 list2 : type2) {
//            for (ProjectIncomeVo1 projectlist : projectincomevo1list) {
//                String projectid = projectlist.getCompanyprojectid();
//                String createby = list2.getNameid();
//                List<LogManagement> logmanagementlist = projectincomemapper.getlogmanagement(projectid, createby, year, month);
//                if (logmanagementlist.size() > 0) {
//                    ratio2 = new BigDecimal(logmanagementlist.get(0).getTime_start());
//                    ratio2s = ratio2.add(ratio2);
//                }
//            }
//        }

        for (ProjectIncomeVo2 list1 : type1) {
            BigDecimal ratio = new BigDecimal("0");
            BigDecimal ratio1s = new BigDecimal("0");
            Map<String, String> map = new HashMap<>();
            map.put("name", list1.getName());
            map.put("nameid", list1.getNameid());
            map.put("money", list1.getMoney());
            map.put("type", list1.getType());
            for (ProjectIncomeVo1 projectlist : projectincomevo1list) {
                String projectid = projectlist.getCompanyprojectid();
                String createby = list1.getNameid();
                List<LogManagement> logmanagementlist = projectincomemapper.getlogmanagement(projectid, createby, year, month);
                if (logmanagementlist.size() > 0) {
                    ratio = new BigDecimal(logmanagementlist.get(0).getTime_start());
                    ratio1s = ratio1s.add(ratio);
                }
            }
            int i = -1;
            for (ProjectIncomeVo1 projectlist : projectincomevo1list) {
                i++;
                String key1 = "works" + i;
                String key2 = "cost" + i;
                String key3 = "radio" + i;
                String projectid = projectlist.getCompanyprojectid();
                String createby = list1.getNameid();
                List<LogManagement> logmanagementlist = projectincomemapper.getlogmanagement(projectid, createby, year, month);
                if (logmanagementlist.size() > 0) {
                    BigDecimal bai = new BigDecimal("100");
                    BigDecimal work = new BigDecimal(logmanagementlist.get(0).getTime_start());
                    BigDecimal ratios = work.divide(ratio1s, scale, roundingMode).multiply(bai);
                    ratios = ratios.setScale(scale, roundingMode);
                    String ratiosend = String.valueOf(ratios) + '%';
                    if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(list1.getMoney())) {
                        BigDecimal money = new BigDecimal(list1.getMoney());
                        BigDecimal costs = money.divide(bai, scale, roundingMode).multiply(ratios);
                        costs = costs.setScale(scale, roundingMode);
                        map.put(key2, String.valueOf(costs));
                    } else {
                        map.put(key2, "0.00");
                    }
                    map.put(key1, String.valueOf(work));
                    map.put(key3, ratiosend);
                } else {
                    map.put(key1, "0.00");
                    map.put(key2, "0.00");
                    map.put(key3, "0.00%");
                }
            }
            lists.add(map);
        }
        for (ProjectIncomeVo2 list2 : type2) {
            BigDecimal ratio3 = new BigDecimal("0");
            BigDecimal ratio3s = new BigDecimal("0");
            Map<String, String> map = new HashMap<>();
            map.put("name", list2.getName());
            map.put("nameid", list2.getNameid());
            map.put("money", list2.getMoney());
            map.put("type", list2.getType());
            int i = -1;
            for (ProjectIncomeVo1 projectlist : projectincomevo1list) {
                String projectid = projectlist.getCompanyprojectid();
                String createby = list2.getNameid();
                List<LogManagement> logmanagementlist = projectincomemapper.getlogmanagement(projectid, createby, year, month);
                if (logmanagementlist.size() > 0) {
                    ratio3 = new BigDecimal(logmanagementlist.get(0).getTime_start());
                    ratio3s = ratio3s.add(ratio3);
                }
            }
            for (ProjectIncomeVo1 projectlist : projectincomevo1list) {
                i++;
                String key1 = "works" + i;
                String key2 = "cost" + i;
                String key3 = "radio" + i;
                String projectid = projectlist.getCompanyprojectid();
                String createby = list2.getNameid();
                List<LogManagement> logmanagementlist = projectincomemapper.getlogmanagement(projectid, createby, year, month);
                if (logmanagementlist.size() > 0) {
                    BigDecimal bai = new BigDecimal("100");
                    BigDecimal work = new BigDecimal(logmanagementlist.get(0).getTime_start());
                    BigDecimal ratios = work.divide(ratio3s, scale, roundingMode).multiply(bai);
                    ratios = ratios.setScale(scale, roundingMode);
                    String ratiosend = String.valueOf(ratios) + '%';
                    if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(list2.getMoney())) {
                        BigDecimal money = new BigDecimal(list2.getMoney());
                        BigDecimal costs = money.divide(bai, scale, roundingMode).multiply(ratios);
                        costs = costs.setScale(scale, roundingMode);
                        map.put(key2, String.valueOf(costs));
                    } else {
                        map.put(key2, "0.00");
                    }
                    map.put(key1, String.valueOf(work));
                    map.put(key3, ratiosend);
                } else {
                    map.put(key1, "0.00");
                    map.put(key2, "0.00");
                    map.put(key3, "0.00%");
                }
            }
            lists.add(map);
        }

        for (int i = 0; i < 5; i++) {
            Map<String, String> map = new HashMap<>();
            map.put("type", "3");
            map.put("name", "");
            map.put("nameid", "");
            map.put("money", "");
            for (ProjectIncomeVo1 projectlist : projectincomevo1list) {
                String key1 = "works" + i;
                String key2 = "cost" + i;
                String key3 = "radio" + i;
                map.put(key1, "");
                map.put(key2, "");
                map.put(key3, "");
            }
            lists.add(map);
        }
        ProjectIncomeVo3 projectincomevo3 = new ProjectIncomeVo3();
        projectincomevo3.setProjectincomevo1(projectincomevo1list);
        projectincomevo3.setProjectincomevo2(projectincomevo2list);
        projectincomevo3.setProjectincomevo4(projectincomevo4list);
        projectincomevo3.setProjectincomevo5(lists);
        return projectincomevo3;
    }
}
