package com.nt.service_pfans.PFANS1000.Impl;


import com.alibaba.fastjson.JSONArray;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_Org.OrgTree;
import com.nt.dao_Pfans.PFANS1000.*;
import com.nt.dao_Pfans.PFANS1000.Vo.IncomeExpenditureVo;
import com.nt.dao_Pfans.PFANS1000.Vo.ProjectIncomeVo4;
import com.nt.dao_Pfans.PFANS5000.ProjectContract;
import com.nt.service_Org.DictionaryService;
import com.nt.service_Org.OrgTreeService;
import com.nt.service_pfans.PFANS1000.IncomeExpenditureService;

import com.nt.service_pfans.PFANS1000.mapper.*;
import com.nt.utils.LogicalException;
import com.nt.utils.dao.TokenModel;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class IncomeExpenditureServiceImpl implements IncomeExpenditureService {
    @Autowired
    private ThemePlanDetailMapper themePlanDetailMapper;

    @Autowired
    private CostCarryForwardMapper costcarryforwardmapper;

    @Autowired
    private BusinessplanMapper businessplanMapper;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private ThemePlanMapper themePlanMapper;

    @Autowired
    private IncomeExpenditureMapper incomeexpendituremapper;

    @Autowired
    private OrgTreeService orgTreeService;

    @Autowired
    private PersonnelplanMapper personnelplanmapper;

    @Autowired
    private ProjectIncomeMapper projectincomemapper;

    @Override
    public List<IncomeExpenditure> getdatalist() throws Exception {
        List<IncomeExpenditure> themeplanlist = incomeexpendituremapper.selectAll();
        List<IncomeExpenditure> list = themeplanlist.stream().collect(
                Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getYear() + ";" + o.getGroup_id() + ";" + o.getCenter_id()))), ArrayList::new)
        );
        return list;
    }

    private String getProperty(Object o, String key) throws Exception {
        try {
            return org.apache.commons.beanutils.BeanUtils.getProperty(o, key);
        } catch (Exception e) {
            throw new LogicalException(e.getMessage());
        }
    }

    @Override
    public XSSFWorkbook getradio(String radiox, String radioy, HttpServletRequest request, HttpServletResponse resp) throws Exception {
        InputStream in = null;
        FileOutputStream f = null;
        XSSFWorkbook work = null;
        //表格操作
        if (radiox.equals("1") && radioy.equals("4")) {
            in = getClass().getClassLoader().getResourceAsStream("jxls_templates/shouzhithemeone.xlsx");
        } else if (radiox.equals("2") && radioy.equals("4")) {
            in = getClass().getClassLoader().getResourceAsStream("jxls_templates/shouzhithemetwo.xlsx");
        } else if (radiox.equals("3") && radioy.equals("4")) {
            in = getClass().getClassLoader().getResourceAsStream("jxls_templates/shouzhithemethree.xlsx");
        } else if (radiox.equals("1") && radioy.equals("5")) {
            in = getClass().getClassLoader().getResourceAsStream("jxls_templates/shouzhiprojectone.xlsx");
        } else if (radiox.equals("2") && radioy.equals("5")) {
            in = getClass().getClassLoader().getResourceAsStream("jxls_templates/shouzhiprojecttwo.xlsx");
        } else if (radiox.equals("3") && radioy.equals("5")) {
            in = getClass().getClassLoader().getResourceAsStream("jxls_templates/shouzhiprojectthree.xlsx");
        }

        work = new XSSFWorkbook(in);
        XSSFSheet sheet1 = work.getSheetAt(0);
        List<IncomeExpenditureVo> incomeexpenditurevolist = new ArrayList<>();
        SimpleDateFormat s = new SimpleDateFormat("MM");
        SimpleDateFormat s1 = new SimpleDateFormat("YYYY");
        int year = Integer.valueOf(s.format(new Date())) >= 4 ? Integer.valueOf(s1.format(new Date())) + 1 : Integer.valueOf(s1.format(new Date()));
        if (radioy.equals("4")) {
            OrgTree orgs = orgTreeService.get(new OrgTree());
            for (OrgTree org : orgs.getOrgs()) {
                for (OrgTree org1 : org.getOrgs()) {
                    IncomeExpenditure incomeexpenditure = new IncomeExpenditure();
                    incomeexpenditure.setYear(String.valueOf(year));
                    incomeexpenditure.setGroup_id(org1.get_id());
                    List<IncomeExpenditure> incomeexpenditurelist = incomeexpendituremapper.select(incomeexpenditure);
                    if (radiox.equals("1")) {
                        if (incomeexpenditurelist.size() > 0) {
                            for (IncomeExpenditure list : incomeexpenditurelist) {
                                IncomeExpenditureVo incomeexpenditurevo = new IncomeExpenditureVo();
                                List<com.nt.dao_Org.Dictionary> curListA = dictionaryService.getForSelect("PJ063");
                                for (Dictionary iteA : curListA) {
                                    if (iteA.getCode().equals(list.getBranch())) {
                                        list.setBranch(iteA.getValue1());
                                    }
                                }
                                List<com.nt.dao_Org.Dictionary> curListD = dictionaryService.getForSelect("PJ143");
                                for (Dictionary iteD : curListD) {
                                    if (iteD.getCode().equals(list.getOtherone())) {
                                        list.setOtherone(iteD.getValue1());
                                    }
                                }
                                List<com.nt.dao_Org.Dictionary> curListC = dictionaryService.getForSelect("PJ144");
                                for (Dictionary iteC : curListC) {
                                    if (iteC.getCode().equals(list.getOthertwo())) {
                                        list.setOthertwo(iteC.getValue1());
                                    }
                                }
                                List<com.nt.dao_Org.Dictionary> curListB = dictionaryService.getForSelect("PJ145");
                                for (Dictionary iteB : curListB) {
                                    if (iteB.getCode().equals(list.getOtherthree())) {
                                        list.setOtherthree(iteB.getValue1());
                                    }
                                }
                                //预计十二个月
                                incomeexpenditurevo.setAmount1(list.getAmount1());
                                incomeexpenditurevo.setAmount2(list.getAmount2());
                                incomeexpenditurevo.setAmount3(list.getAmount3());
                                incomeexpenditurevo.setAmount4(list.getAmount4());
                                incomeexpenditurevo.setAmount5(list.getAmount5());
                                incomeexpenditurevo.setAmount6(list.getAmount6());
                                incomeexpenditurevo.setAmount7(list.getAmount7());
                                incomeexpenditurevo.setAmount8(list.getAmount8());
                                incomeexpenditurevo.setAmount9(list.getAmount9());
                                incomeexpenditurevo.setAmount10(list.getAmount10());
                                incomeexpenditurevo.setAmount11(list.getAmount11());
                                incomeexpenditurevo.setAmount12(list.getAmount12());
                                //实际十二个月
                                incomeexpenditurevo.setPlanamount1(list.getPlanamount1());
                                incomeexpenditurevo.setPlanamount2(list.getPlanamount2());
                                incomeexpenditurevo.setPlanamount3(list.getPlanamount3());
                                incomeexpenditurevo.setPlanamount4(list.getPlanamount4());
                                incomeexpenditurevo.setPlanamount5(list.getPlanamount5());
                                incomeexpenditurevo.setPlanamount6(list.getPlanamount6());
                                incomeexpenditurevo.setPlanamount7(list.getPlanamount7());
                                incomeexpenditurevo.setPlanamount8(list.getPlanamount8());
                                incomeexpenditurevo.setPlanamount9(list.getPlanamount9());
                                incomeexpenditurevo.setPlanamount10(list.getPlanamount10());
                                incomeexpenditurevo.setPlanamount11(list.getPlanamount11());
                                incomeexpenditurevo.setPlanamount12(list.getPlanamount12());
                                incomeexpenditurevo.setGroup_id(org1.getCompanyen());
                                incomeexpenditurevo.setBranch(list.getBranch());
                                incomeexpenditurevo.setThemename(list.getThemename());
                                incomeexpenditurevo.setOtherone(list.getOtherone());
                                incomeexpenditurevo.setOthertwo(list.getOthertwo());
                                incomeexpenditurevo.setOtherthree(list.getOtherthree());
                                incomeexpenditurevo.setCenter_id("大连松下");
                                incomeexpenditurevolist.add(incomeexpenditurevo);
                            }
                        }
                    } else if (radiox.equals("2")) {
                        if (incomeexpenditurelist.size() > 0) {
                            for (IncomeExpenditure list : incomeexpenditurelist) {
                                IncomeExpenditureVo incomeexpenditurevo = new IncomeExpenditureVo();
                                List<com.nt.dao_Org.Dictionary> curListA = dictionaryService.getForSelect("PJ063");
                                for (Dictionary iteA : curListA) {
                                    if (iteA.getCode().equals(list.getBranch())) {
                                        list.setBranch(iteA.getValue1());
                                    }
                                }
                                List<com.nt.dao_Org.Dictionary> curListD = dictionaryService.getForSelect("PJ143");
                                for (Dictionary iteD : curListD) {
                                    if (iteD.getCode().equals(list.getOtherone())) {
                                        list.setOtherone(iteD.getValue1());
                                    }
                                }
                                List<com.nt.dao_Org.Dictionary> curListC = dictionaryService.getForSelect("PJ144");
                                for (Dictionary iteC : curListC) {
                                    if (iteC.getCode().equals(list.getOthertwo())) {
                                        list.setOthertwo(iteC.getValue1());
                                    }
                                }
                                List<com.nt.dao_Org.Dictionary> curListB = dictionaryService.getForSelect("PJ145");
                                for (Dictionary iteB : curListB) {
                                    if (iteB.getCode().equals(list.getOtherthree())) {
                                        list.setOtherthree(iteB.getValue1());
                                    }
                                }
                                BigDecimal mount1 = new BigDecimal(list.getAmount9()).add(new BigDecimal(list.getAmount8())).add(new BigDecimal(list.getAmount7())).add(new BigDecimal(list.getAmount4())).add(new BigDecimal(list.getAmount5())).add(new BigDecimal(list.getAmount6()));
                                BigDecimal mount2 = new BigDecimal(list.getAmount10()).add(new BigDecimal(list.getAmount11())).add(new BigDecimal(list.getAmount12())).add(new BigDecimal(list.getAmount1())).add(new BigDecimal(list.getAmount2())).add(new BigDecimal(list.getAmount3()));
                                BigDecimal mount3 = mount1.add(mount2);
                                BigDecimal planamount1 = new BigDecimal(list.getPlanamount9()).add(new BigDecimal(list.getPlanamount8())).add(new BigDecimal(list.getPlanamount7())).add(new BigDecimal(list.getPlanamount4())).add(new BigDecimal(list.getPlanamount5())).add(new BigDecimal(list.getPlanamount6()));
                                BigDecimal planamount2 = new BigDecimal(list.getPlanamount10()).add(new BigDecimal(list.getPlanamount11())).add(new BigDecimal(list.getPlanamount12())).add(new BigDecimal(list.getPlanamount1())).add(new BigDecimal(list.getPlanamount2())).add(new BigDecimal(list.getPlanamount3()));
                                BigDecimal planamount3 = planamount1.add(planamount2);

                                //预计上半年，下半年，年度

                                incomeexpenditurevo.setAmount1(String.valueOf(mount1));
                                incomeexpenditurevo.setAmount2(String.valueOf(mount2));
                                incomeexpenditurevo.setAmount3(String.valueOf(mount3));
                                //实际上半年，下半年，年度
                                incomeexpenditurevo.setPlanamount1(String.valueOf(planamount1));
                                incomeexpenditurevo.setPlanamount2(String.valueOf(planamount2));
                                incomeexpenditurevo.setPlanamount3(String.valueOf(planamount3));
                                incomeexpenditurevo.setGroup_id(org1.getCompanyen());
                                incomeexpenditurevo.setBranch(list.getBranch());
                                incomeexpenditurevo.setThemename(list.getThemename());
                                incomeexpenditurevo.setOtherone(list.getOtherone());
                                incomeexpenditurevo.setOthertwo(list.getOthertwo());
                                incomeexpenditurevo.setOtherthree(list.getOtherthree());
                                incomeexpenditurevo.setCenter_id("大连松下");
                                incomeexpenditurevolist.add(incomeexpenditurevo);
                            }
                        }
                    } else if (radiox.equals("3")) {
                        if (incomeexpenditurelist.size() > 0) {
                            for (IncomeExpenditure list : incomeexpenditurelist) {
                                IncomeExpenditureVo incomeexpenditurevo = new IncomeExpenditureVo();
                                List<com.nt.dao_Org.Dictionary> curListA = dictionaryService.getForSelect("PJ063");
                                for (Dictionary iteA : curListA) {
                                    if (iteA.getCode().equals(list.getBranch())) {
                                        list.setBranch(iteA.getValue1());
                                    }
                                }
                                List<com.nt.dao_Org.Dictionary> curListD = dictionaryService.getForSelect("PJ143");
                                for (Dictionary iteD : curListD) {
                                    if (iteD.getCode().equals(list.getOtherone())) {
                                        list.setOtherone(iteD.getValue1());
                                    }
                                }
                                List<com.nt.dao_Org.Dictionary> curListC = dictionaryService.getForSelect("PJ144");
                                for (Dictionary iteC : curListC) {
                                    if (iteC.getCode().equals(list.getOthertwo())) {
                                        list.setOthertwo(iteC.getValue1());
                                    }
                                }
                                List<com.nt.dao_Org.Dictionary> curListB = dictionaryService.getForSelect("PJ145");
                                for (Dictionary iteB : curListB) {
                                    if (iteB.getCode().equals(list.getOtherthree())) {
                                        list.setOtherthree(iteB.getValue1());
                                    }
                                }
                                BigDecimal mount1 = new BigDecimal(list.getAmount4()).add(new BigDecimal(list.getAmount5())).add(new BigDecimal(list.getAmount6()));
                                BigDecimal mount2 = new BigDecimal(list.getAmount9()).add(new BigDecimal(list.getAmount8())).add(new BigDecimal(list.getAmount7()));
                                BigDecimal mount3 = new BigDecimal(list.getAmount10()).add(new BigDecimal(list.getAmount11())).add(new BigDecimal(list.getAmount12()));
                                BigDecimal mount4 = new BigDecimal(list.getAmount1()).add(new BigDecimal(list.getAmount2())).add(new BigDecimal(list.getAmount3()));
                                BigDecimal planamount1 = new BigDecimal(list.getPlanamount4()).add(new BigDecimal(list.getPlanamount5())).add(new BigDecimal(list.getPlanamount6()));
                                BigDecimal planamount2 = new BigDecimal(list.getPlanamount9()).add(new BigDecimal(list.getPlanamount8())).add(new BigDecimal(list.getPlanamount7()));
                                BigDecimal planamount3 = new BigDecimal(list.getPlanamount10()).add(new BigDecimal(list.getPlanamount11())).add(new BigDecimal(list.getPlanamount12()));
                                BigDecimal planamount4 = new BigDecimal(list.getPlanamount1()).add(new BigDecimal(list.getPlanamount2())).add(new BigDecimal(list.getPlanamount3()));
                                //预计季度
                                incomeexpenditurevo.setAmount1(String.valueOf(mount1));
                                incomeexpenditurevo.setAmount2(String.valueOf(mount2));
                                incomeexpenditurevo.setAmount3(String.valueOf(mount3));
                                incomeexpenditurevo.setAmount4(String.valueOf(mount4));
                                //实际季度
                                incomeexpenditurevo.setPlanamount1(String.valueOf(planamount1));
                                incomeexpenditurevo.setPlanamount2(String.valueOf(planamount2));
                                incomeexpenditurevo.setPlanamount3(String.valueOf(planamount3));
                                incomeexpenditurevo.setPlanamount4(String.valueOf(planamount4));
                                incomeexpenditurevo.setGroup_id(org1.getCompanyen());
                                incomeexpenditurevo.setBranch(list.getBranch());
                                incomeexpenditurevo.setThemename(list.getThemename());
                                incomeexpenditurevo.setOtherone(list.getOtherone());
                                incomeexpenditurevo.setOthertwo(list.getOthertwo());
                                incomeexpenditurevo.setOtherthree(list.getOtherthree());
                                incomeexpenditurevo.setCenter_id("大连松下");
                                incomeexpenditurevolist.add(incomeexpenditurevo);
                            }
                        }
                    }

                }
            }
        }
        if (radioy.equals("5") && radiox.equals("1")) {
            incomeexpenditurevolist = getrodiodetailone("5", "1");
        } else if (radioy.equals("5") && radiox.equals("2")) {
            incomeexpenditurevolist = getrodiodetailtwo("5", "2");
        } else if (radioy.equals("5") && radiox.equals("3")) {
            incomeexpenditurevolist = getrodiodetailthree("5", "3");
        }
        int p = 1;
        if (radioy.equals("4") && radiox.equals("1")) {
            for (IncomeExpenditureVo list : incomeexpenditurevolist) {
                p++;
                XSSFRow row1 = sheet1.createRow(p);
                row1.createCell(0).setCellValue(list.getCenter_id());
                row1.createCell(1).setCellValue(list.getGroup_id());
                row1.createCell(2).setCellValue(list.getThemename());
                row1.createCell(3).setCellValue(list.getBranch());
                row1.createCell(4).setCellValue(list.getOtherone());
                row1.createCell(5).setCellValue(list.getOthertwo());
                row1.createCell(6).setCellValue(list.getOtherthree());
                row1.createCell(7).setCellValue(list.getPlanamount4());
                row1.createCell(8).setCellValue(list.getAmount4());
                row1.createCell(9).setCellValue(list.getPlanamount5());
                row1.createCell(10).setCellValue(list.getAmount5());
                row1.createCell(11).setCellValue(list.getPlanamount6());
                row1.createCell(12).setCellValue(list.getAmount6());
                row1.createCell(13).setCellValue(list.getPlanamount7());
                row1.createCell(14).setCellValue(list.getAmount7());
                row1.createCell(15).setCellValue(list.getPlanamount8());
                row1.createCell(16).setCellValue(list.getAmount8());
                row1.createCell(17).setCellValue(list.getPlanamount9());
                row1.createCell(18).setCellValue(list.getAmount9());
                row1.createCell(19).setCellValue(list.getPlanamount10());
                row1.createCell(20).setCellValue(list.getAmount10());
                row1.createCell(21).setCellValue(list.getPlanamount11());
                row1.createCell(22).setCellValue(list.getAmount11());
                row1.createCell(23).setCellValue(list.getPlanamount12());
                row1.createCell(24).setCellValue(list.getAmount12());
                row1.createCell(25).setCellValue(list.getPlanamount1());
                row1.createCell(26).setCellValue(list.getAmount1());
                row1.createCell(27).setCellValue(list.getPlanamount2());
                row1.createCell(28).setCellValue(list.getAmount2());
                row1.createCell(29).setCellValue(list.getPlanamount3());
                row1.createCell(30).setCellValue(list.getAmount3());
            }
        } else if (radioy.equals("4") && radiox.equals("2")) {
            for (IncomeExpenditureVo list : incomeexpenditurevolist) {
                p++;
                XSSFRow row1 = sheet1.createRow(p);
                row1.createCell(0).setCellValue(list.getCenter_id());
                row1.createCell(1).setCellValue(list.getGroup_id());
                row1.createCell(2).setCellValue(list.getThemename());
                row1.createCell(3).setCellValue(list.getBranch());
                row1.createCell(4).setCellValue(list.getOtherone());
                row1.createCell(5).setCellValue(list.getOthertwo());
                row1.createCell(6).setCellValue(list.getOtherthree());
                row1.createCell(7).setCellValue(list.getPlanamount1());
                row1.createCell(8).setCellValue(list.getAmount1());
                row1.createCell(9).setCellValue(list.getPlanamount2());
                row1.createCell(10).setCellValue(list.getAmount2());
                row1.createCell(11).setCellValue(list.getPlanamount3());
                row1.createCell(12).setCellValue(list.getAmount3());
            }
        } else if (radioy.equals("4") && radiox.equals("3")) {
            for (IncomeExpenditureVo list : incomeexpenditurevolist) {
                p++;
                XSSFRow row1 = sheet1.createRow(p);
                row1.createCell(0).setCellValue(list.getCenter_id());
                row1.createCell(1).setCellValue(list.getGroup_id());
                row1.createCell(2).setCellValue(list.getThemename());
                row1.createCell(3).setCellValue(list.getBranch());
                row1.createCell(4).setCellValue(list.getOtherone());
                row1.createCell(5).setCellValue(list.getOthertwo());
                row1.createCell(6).setCellValue(list.getOtherthree());
                row1.createCell(7).setCellValue(list.getPlanamount1());
                row1.createCell(8).setCellValue(list.getAmount1());
                row1.createCell(9).setCellValue(list.getPlanamount2());
                row1.createCell(10).setCellValue(list.getAmount2());
                row1.createCell(11).setCellValue(list.getPlanamount3());
                row1.createCell(12).setCellValue(list.getAmount3());
                row1.createCell(13).setCellValue(list.getPlanamount4());
                row1.createCell(14).setCellValue(list.getAmount4());
            }
        } else if (radioy.equals("5") && radiox.equals("1")) {
            for (IncomeExpenditureVo list : incomeexpenditurevolist) {
                p++;
                XSSFRow row1 = sheet1.createRow(p);
                row1.createCell(0).setCellValue(list.getCenter_id());
                row1.createCell(1).setCellValue(list.getGroup_id());
                row1.createCell(2).setCellValue(list.getThemename());
                row1.createCell(3).setCellValue(list.getPlanamount4());
                row1.createCell(4).setCellValue(list.getAmount4());
                row1.createCell(5).setCellValue(list.getPlanamount5());
                row1.createCell(6).setCellValue(list.getAmount5());
                row1.createCell(7).setCellValue(list.getPlanamount6());
                row1.createCell(8).setCellValue(list.getAmount6());
                row1.createCell(9).setCellValue(list.getPlanamount7());
                row1.createCell(10).setCellValue(list.getAmount7());
                row1.createCell(11).setCellValue(list.getPlanamount8());
                row1.createCell(12).setCellValue(list.getAmount8());
                row1.createCell(13).setCellValue(list.getPlanamount9());
                row1.createCell(14).setCellValue(list.getAmount9());
                row1.createCell(15).setCellValue(list.getPlanamount10());
                row1.createCell(16).setCellValue(list.getAmount10());
                row1.createCell(17).setCellValue(list.getPlanamount11());
                row1.createCell(18).setCellValue(list.getAmount11());
                row1.createCell(19).setCellValue(list.getPlanamount12());
                row1.createCell(20).setCellValue(list.getAmount12());
                row1.createCell(21).setCellValue(list.getPlanamount1());
                row1.createCell(22).setCellValue(list.getAmount1());
                row1.createCell(23).setCellValue(list.getPlanamount2());
                row1.createCell(24).setCellValue(list.getAmount2());
                row1.createCell(25).setCellValue(list.getPlanamount3());
                row1.createCell(26).setCellValue(list.getAmount3());
            }
        } else if (radioy.equals("5") && radiox.equals("2")) {
            for (IncomeExpenditureVo list : incomeexpenditurevolist) {
                p++;
                XSSFRow row1 = sheet1.createRow(p);
                row1.createCell(0).setCellValue(list.getCenter_id());
                row1.createCell(1).setCellValue(list.getGroup_id());
                row1.createCell(2).setCellValue(list.getThemename());
                row1.createCell(3).setCellValue(list.getPlanamount4());
                row1.createCell(4).setCellValue(list.getAmount1());
                row1.createCell(5).setCellValue(list.getPlanamount1());
                row1.createCell(6).setCellValue(list.getAmount2());
                row1.createCell(7).setCellValue(list.getPlanamount2());
                row1.createCell(8).setCellValue(list.getAmount3());
                row1.createCell(9).setCellValue(list.getPlanamount3());
            }
        } else if (radioy.equals("5") && radiox.equals("3")) {
            for (IncomeExpenditureVo list : incomeexpenditurevolist) {
                p++;
                XSSFRow row1 = sheet1.createRow(p);
                row1.createCell(0).setCellValue(list.getCenter_id());
                row1.createCell(1).setCellValue(list.getGroup_id());
                row1.createCell(2).setCellValue(list.getThemename());
                row1.createCell(3).setCellValue(list.getPlanamount1());
                row1.createCell(4).setCellValue(list.getAmount1());
                row1.createCell(5).setCellValue(list.getPlanamount2());
                row1.createCell(6).setCellValue(list.getAmount2());
                row1.createCell(7).setCellValue(list.getPlanamount3());
                row1.createCell(8).setCellValue(list.getAmount3());
                row1.createCell(9).setCellValue(list.getPlanamount4());
                row1.createCell(10).setCellValue(list.getAmount4());

            }
        }

        return work;
    }

    public List<IncomeExpenditureVo> getrodiodetailone(String radioy, String radiox) throws Exception {
        List<IncomeExpenditureVo> incomeexpenditurevolist = new ArrayList<>();
        SimpleDateFormat s = new SimpleDateFormat("MM");
        SimpleDateFormat s1 = new SimpleDateFormat("YYYY");
        int scale = 2;//设置位数
        int roundingMode = 4;//表示四舍五入，可以选择其他舍值方式，例如去尾，等等.
        int year = Integer.valueOf(s.format(new Date())) >= 4 ? Integer.valueOf(s1.format(new Date())) + 1 : Integer.valueOf(s1.format(new Date()));
        if (radioy.equals("5")) {
            OrgTree orgs = orgTreeService.get(new OrgTree());
            for (OrgTree org : orgs.getOrgs()) {
                for (OrgTree org1 : org.getOrgs()) {
                    if (radiox.equals("1")) {
                        Businessplan businessplan = new Businessplan();
                        businessplan.setYear(String.valueOf(year));
                        businessplan.setGroup_id(org1.get_id());
                        List<Businessplan> businessplanlist = businessplanMapper.select(businessplan);
                        CostCarryForward costcarryforward = new CostCarryForward();
                        costcarryforward.setYear(String.valueOf(year));
                        costcarryforward.setGroup_id(org1.get_id());
                        List<CostCarryForward> costcarryforwardlist = costcarryforwardmapper.select(costcarryforward);

                        ProjectIncome projectincome = new ProjectIncome();
                        projectincome.setYear(String.valueOf(year));
                        projectincome.setGroup_id(org1.get_id());
                        List<ProjectIncome> projectincomelist = projectincomemapper.select(projectincome);

                        PersonnelPlan personnelplan = new PersonnelPlan();
                        personnelplan.setYears(String.valueOf(year));
                        personnelplan.setCenterid(org1.get_id());
                        List<PersonnelPlan> personnelplanlist = personnelplanmapper.select(personnelplan);
                        BigDecimal personnel1 = new BigDecimal("0");
                        BigDecimal personnel2 = new BigDecimal("0");
                        BigDecimal personnel3 = new BigDecimal("0");
                        BigDecimal personnel4 = new BigDecimal("0");
                        BigDecimal personnel5 = new BigDecimal("0");
                        BigDecimal personnel6 = new BigDecimal("0");
                        BigDecimal personnel7 = new BigDecimal("0");
                        BigDecimal personnel8 = new BigDecimal("0");
                        BigDecimal personnel9 = new BigDecimal("0");
                        BigDecimal personnel10 = new BigDecimal("0");
                        BigDecimal personnel11 = new BigDecimal("0");
                        BigDecimal personnel12 = new BigDecimal("0");
                        BigDecimal wpersonnel1 = new BigDecimal("0");
                        BigDecimal wpersonnel2 = new BigDecimal("0");
                        BigDecimal wpersonnel3 = new BigDecimal("0");
                        BigDecimal wpersonnel4 = new BigDecimal("0");
                        BigDecimal wpersonnel5 = new BigDecimal("0");
                        BigDecimal wpersonnel6 = new BigDecimal("0");
                        BigDecimal wpersonnel7 = new BigDecimal("0");
                        BigDecimal wpersonnel8 = new BigDecimal("0");
                        BigDecimal wpersonnel9 = new BigDecimal("0");
                        BigDecimal wpersonnel10 = new BigDecimal("0");
                        BigDecimal wpersonnel11 = new BigDecimal("0");
                        BigDecimal wpersonnel12 = new BigDecimal("0");
                        for (PersonnelPlan list : personnelplanlist) {
                            if (list.getType().equals("0")) {
                                JSONArray jsonemployed = JSONArray.parseArray(list.getEmployed());
                                JSONArray jsonnewentry = JSONArray.parseArray(list.getNewentry());
                                personnel1 = new BigDecimal(jsonemployed.size());
                                personnel2 = new BigDecimal(jsonemployed.size());
                                personnel3 = new BigDecimal(jsonemployed.size());
                                personnel4 = new BigDecimal(jsonemployed.size());
                                personnel5 = new BigDecimal(jsonemployed.size());
                                personnel6 = new BigDecimal(jsonemployed.size());
                                personnel7 = new BigDecimal(jsonemployed.size());
                                personnel8 = new BigDecimal(jsonemployed.size());
                                personnel9 = new BigDecimal(jsonemployed.size());
                                personnel10 = new BigDecimal(jsonemployed.size());
                                personnel11 = new BigDecimal(jsonemployed.size());
                                personnel12 = new BigDecimal(jsonemployed.size());
                                for (Object ob : jsonnewentry) {
                                    String month = getProperty(ob, "entermouth");
                                    if (s.format(month).equals("01")) {
                                        personnel1.add(new BigDecimal("1"));
                                        personnel2.add(new BigDecimal("1"));
                                        personnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("02")) {
                                        personnel2.add(new BigDecimal("1"));
                                        personnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("03")) {
                                        personnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("04")) {
                                        personnel4.add(new BigDecimal("1"));
                                        personnel5.add(new BigDecimal("1"));
                                        personnel6.add(new BigDecimal("1"));
                                        personnel7.add(new BigDecimal("1"));
                                        personnel8.add(new BigDecimal("1"));
                                        personnel9.add(new BigDecimal("1"));
                                        personnel10.add(new BigDecimal("1"));
                                        personnel11.add(new BigDecimal("1"));
                                        personnel12.add(new BigDecimal("1"));
                                        personnel1.add(new BigDecimal("1"));
                                        personnel2.add(new BigDecimal("1"));
                                        personnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("05")) {
                                        personnel5.add(new BigDecimal("1"));
                                        personnel6.add(new BigDecimal("1"));
                                        personnel7.add(new BigDecimal("1"));
                                        personnel8.add(new BigDecimal("1"));
                                        personnel9.add(new BigDecimal("1"));
                                        personnel10.add(new BigDecimal("1"));
                                        personnel11.add(new BigDecimal("1"));
                                        personnel12.add(new BigDecimal("1"));
                                        personnel1.add(new BigDecimal("1"));
                                        personnel2.add(new BigDecimal("1"));
                                        personnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("06")) {
                                        personnel6.add(new BigDecimal("1"));
                                        personnel7.add(new BigDecimal("1"));
                                        personnel8.add(new BigDecimal("1"));
                                        personnel9.add(new BigDecimal("1"));
                                        personnel10.add(new BigDecimal("1"));
                                        personnel11.add(new BigDecimal("1"));
                                        personnel12.add(new BigDecimal("1"));
                                        personnel1.add(new BigDecimal("1"));
                                        personnel2.add(new BigDecimal("1"));
                                        personnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("07")) {
                                        personnel7.add(new BigDecimal("1"));
                                        personnel8.add(new BigDecimal("1"));
                                        personnel9.add(new BigDecimal("1"));
                                        personnel10.add(new BigDecimal("1"));
                                        personnel11.add(new BigDecimal("1"));
                                        personnel12.add(new BigDecimal("1"));
                                        personnel1.add(new BigDecimal("1"));
                                        personnel2.add(new BigDecimal("1"));
                                        personnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("08")) {
                                        personnel8.add(new BigDecimal("1"));
                                        personnel9.add(new BigDecimal("1"));
                                        personnel10.add(new BigDecimal("1"));
                                        personnel11.add(new BigDecimal("1"));
                                        personnel12.add(new BigDecimal("1"));
                                        personnel1.add(new BigDecimal("1"));
                                        personnel2.add(new BigDecimal("1"));
                                        personnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("09")) {
                                        personnel9.add(new BigDecimal("1"));
                                        personnel10.add(new BigDecimal("1"));
                                        personnel11.add(new BigDecimal("1"));
                                        personnel12.add(new BigDecimal("1"));
                                        personnel1.add(new BigDecimal("1"));
                                        personnel2.add(new BigDecimal("1"));
                                        personnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("10")) {
                                        personnel10.add(new BigDecimal("1"));
                                        personnel11.add(new BigDecimal("1"));
                                        personnel12.add(new BigDecimal("1"));
                                        personnel1.add(new BigDecimal("1"));
                                        personnel2.add(new BigDecimal("1"));
                                        personnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("11")) {
                                        personnel11.add(new BigDecimal("1"));
                                        personnel12.add(new BigDecimal("1"));
                                        personnel1.add(new BigDecimal("1"));
                                        personnel2.add(new BigDecimal("1"));
                                        personnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("12")) {
                                        personnel12.add(new BigDecimal("1"));
                                        personnel1.add(new BigDecimal("1"));
                                        personnel2.add(new BigDecimal("1"));
                                        personnel3.add(new BigDecimal("1"));
                                    }
                                }
                            } else if (list.getType().equals("1")) {
                                JSONArray jsonemployed = JSONArray.parseArray(list.getEmployed());
                                JSONArray jsonnewentry = JSONArray.parseArray(list.getNewentry());
                                wpersonnel1 = new BigDecimal(jsonemployed.size());
                                wpersonnel2 = new BigDecimal(jsonemployed.size());
                                wpersonnel3 = new BigDecimal(jsonemployed.size());
                                wpersonnel4 = new BigDecimal(jsonemployed.size());
                                wpersonnel5 = new BigDecimal(jsonemployed.size());
                                wpersonnel6 = new BigDecimal(jsonemployed.size());
                                wpersonnel7 = new BigDecimal(jsonemployed.size());
                                wpersonnel8 = new BigDecimal(jsonemployed.size());
                                wpersonnel9 = new BigDecimal(jsonemployed.size());
                                wpersonnel10 = new BigDecimal(jsonemployed.size());
                                wpersonnel11 = new BigDecimal(jsonemployed.size());
                                wpersonnel12 = new BigDecimal(jsonemployed.size());
                                for (Object ob : jsonnewentry) {
                                    String month = getProperty(ob, "entermouth");
                                    if (s.format(month).equals("01")) {
                                        wpersonnel1.add(new BigDecimal("1"));
                                        wpersonnel2.add(new BigDecimal("1"));
                                        wpersonnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("02")) {
                                        wpersonnel2.add(new BigDecimal("1"));
                                        wpersonnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("03")) {
                                        wpersonnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("04")) {
                                        wpersonnel4.add(new BigDecimal("1"));
                                        wpersonnel5.add(new BigDecimal("1"));
                                        wpersonnel6.add(new BigDecimal("1"));
                                        wpersonnel7.add(new BigDecimal("1"));
                                        wpersonnel8.add(new BigDecimal("1"));
                                        wpersonnel9.add(new BigDecimal("1"));
                                        wpersonnel10.add(new BigDecimal("1"));
                                        wpersonnel11.add(new BigDecimal("1"));
                                        wpersonnel12.add(new BigDecimal("1"));
                                        wpersonnel1.add(new BigDecimal("1"));
                                        wpersonnel2.add(new BigDecimal("1"));
                                        wpersonnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("05")) {
                                        wpersonnel5.add(new BigDecimal("1"));
                                        wpersonnel6.add(new BigDecimal("1"));
                                        wpersonnel7.add(new BigDecimal("1"));
                                        wpersonnel8.add(new BigDecimal("1"));
                                        wpersonnel9.add(new BigDecimal("1"));
                                        wpersonnel10.add(new BigDecimal("1"));
                                        wpersonnel11.add(new BigDecimal("1"));
                                        wpersonnel12.add(new BigDecimal("1"));
                                        wpersonnel1.add(new BigDecimal("1"));
                                        wpersonnel2.add(new BigDecimal("1"));
                                        wpersonnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("06")) {
                                        wpersonnel6.add(new BigDecimal("1"));
                                        wpersonnel7.add(new BigDecimal("1"));
                                        wpersonnel8.add(new BigDecimal("1"));
                                        wpersonnel9.add(new BigDecimal("1"));
                                        wpersonnel10.add(new BigDecimal("1"));
                                        wpersonnel11.add(new BigDecimal("1"));
                                        wpersonnel12.add(new BigDecimal("1"));
                                        wpersonnel1.add(new BigDecimal("1"));
                                        wpersonnel2.add(new BigDecimal("1"));
                                        wpersonnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("07")) {
                                        wpersonnel7.add(new BigDecimal("1"));
                                        wpersonnel8.add(new BigDecimal("1"));
                                        wpersonnel9.add(new BigDecimal("1"));
                                        wpersonnel10.add(new BigDecimal("1"));
                                        wpersonnel11.add(new BigDecimal("1"));
                                        wpersonnel12.add(new BigDecimal("1"));
                                        wpersonnel1.add(new BigDecimal("1"));
                                        wpersonnel2.add(new BigDecimal("1"));
                                        wpersonnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("08")) {
                                        wpersonnel8.add(new BigDecimal("1"));
                                        wpersonnel9.add(new BigDecimal("1"));
                                        wpersonnel10.add(new BigDecimal("1"));
                                        wpersonnel11.add(new BigDecimal("1"));
                                        wpersonnel12.add(new BigDecimal("1"));
                                        wpersonnel1.add(new BigDecimal("1"));
                                        wpersonnel2.add(new BigDecimal("1"));
                                        wpersonnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("09")) {
                                        wpersonnel9.add(new BigDecimal("1"));
                                        wpersonnel10.add(new BigDecimal("1"));
                                        wpersonnel11.add(new BigDecimal("1"));
                                        wpersonnel12.add(new BigDecimal("1"));
                                        wpersonnel1.add(new BigDecimal("1"));
                                        wpersonnel2.add(new BigDecimal("1"));
                                        wpersonnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("10")) {
                                        wpersonnel10.add(new BigDecimal("1"));
                                        wpersonnel11.add(new BigDecimal("1"));
                                        wpersonnel12.add(new BigDecimal("1"));
                                        wpersonnel1.add(new BigDecimal("1"));
                                        wpersonnel2.add(new BigDecimal("1"));
                                        wpersonnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("11")) {
                                        wpersonnel11.add(new BigDecimal("1"));
                                        wpersonnel12.add(new BigDecimal("1"));
                                        wpersonnel1.add(new BigDecimal("1"));
                                        wpersonnel2.add(new BigDecimal("1"));
                                        wpersonnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("12")) {
                                        wpersonnel12.add(new BigDecimal("1"));
                                        wpersonnel1.add(new BigDecimal("1"));
                                        wpersonnel2.add(new BigDecimal("1"));
                                        wpersonnel3.add(new BigDecimal("1"));
                                    }
                                }
                            }
                        }
                        List<com.nt.dao_Org.Dictionary> curListA = dictionaryService.getForSelect("PJ147");
                        for (Dictionary iteA : curListA) {
                            IncomeExpenditureVo incomeexpenditurevo = new IncomeExpenditureVo();
                            incomeexpenditurevo.setCenter_id("大连松下");
                            incomeexpenditurevo.setGroup_id(org1.getCompanyen());
                            BigDecimal planmonth1 = new BigDecimal("0");
                            BigDecimal planmonth2 = new BigDecimal("0");
                            BigDecimal planmonth3 = new BigDecimal("0");
                            BigDecimal planmonth4 = new BigDecimal("0");
                            BigDecimal planmonth5 = new BigDecimal("0");
                            BigDecimal planmonth6 = new BigDecimal("0");
                            BigDecimal planmonth7 = new BigDecimal("0");
                            BigDecimal planmonth8 = new BigDecimal("0");
                            BigDecimal planmonth9 = new BigDecimal("0");
                            BigDecimal planmonth10 = new BigDecimal("0");
                            BigDecimal planmonth11 = new BigDecimal("0");
                            BigDecimal planmonth12 = new BigDecimal("0");
                            if (iteA.getCode().equals("PJ147001")) {
                                for (CostCarryForward costlist : costcarryforwardlist) {
                                    if (costlist.getRegion().substring(6, 7).equals("01")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal());
                                        planmonth1 = planmonth1.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("02")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal());
                                        planmonth2 = planmonth2.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("03")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal());
                                        planmonth3 = planmonth3.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("04")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal());
                                        planmonth4 = planmonth4.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("05")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal());
                                        planmonth5 = planmonth5.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("06")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal());
                                        planmonth6 = planmonth6.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("07")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal());
                                        planmonth7 = planmonth7.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("08")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal());
                                        planmonth8 = planmonth8.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("09")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal());
                                        planmonth9 = planmonth9.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("10")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal());
                                        planmonth10 = planmonth10.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("11")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal());
                                        planmonth11 = planmonth11.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("12")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal());
                                        planmonth12 = planmonth12.add(intotal);
                                    }
                                }
                                incomeexpenditurevo.setPlanamount1(String.valueOf(planmonth1));
                                incomeexpenditurevo.setPlanamount2(String.valueOf(planmonth2));
                                incomeexpenditurevo.setPlanamount3(String.valueOf(planmonth3));
                                incomeexpenditurevo.setPlanamount4(String.valueOf(planmonth4));
                                incomeexpenditurevo.setPlanamount5(String.valueOf(planmonth5));
                                incomeexpenditurevo.setPlanamount6(String.valueOf(planmonth6));
                                incomeexpenditurevo.setPlanamount7(String.valueOf(planmonth7));
                                incomeexpenditurevo.setPlanamount8(String.valueOf(planmonth8));
                                incomeexpenditurevo.setPlanamount9(String.valueOf(planmonth9));
                                incomeexpenditurevo.setPlanamount10(String.valueOf(planmonth10));
                                incomeexpenditurevo.setPlanamount11(String.valueOf(planmonth11));
                                incomeexpenditurevo.setPlanamount12(String.valueOf(planmonth12));
                                BigDecimal month1 = new BigDecimal("0");
                                BigDecimal month2 = new BigDecimal("0");
                                BigDecimal month3 = new BigDecimal("0");
                                BigDecimal month4 = new BigDecimal("0");
                                BigDecimal month5 = new BigDecimal("0");
                                BigDecimal month6 = new BigDecimal("0");
                                BigDecimal month7 = new BigDecimal("0");
                                BigDecimal month8 = new BigDecimal("0");
                                BigDecimal month9 = new BigDecimal("0");
                                BigDecimal month10 = new BigDecimal("0");
                                BigDecimal month11 = new BigDecimal("0");
                                BigDecimal month12 = new BigDecimal("0");
                                incomeexpenditurevo.setThemename(iteA.getValue1());
                                JSONArray jsongroupa1 = JSONArray.parseArray(businessplanlist.get(0).getGroupA1());
                                JSONArray jsongroupa2 = JSONArray.parseArray(businessplanlist.get(0).getGroupA2());
                                for (Object ob : jsongroupa1) {
                                    BigDecimal months1 = new BigDecimal(getProperty(ob, "money1"));
                                    BigDecimal months2 = new BigDecimal(getProperty(ob, "money2"));
                                    BigDecimal months3 = new BigDecimal(getProperty(ob, "money3"));
                                    BigDecimal months4 = new BigDecimal(getProperty(ob, "money4"));
                                    BigDecimal months5 = new BigDecimal(getProperty(ob, "money5"));
                                    BigDecimal months6 = new BigDecimal(getProperty(ob, "money6"));
                                    BigDecimal months7 = new BigDecimal(getProperty(ob, "money7"));
                                    BigDecimal months8 = new BigDecimal(getProperty(ob, "money8"));
                                    BigDecimal months9 = new BigDecimal(getProperty(ob, "money9"));
                                    BigDecimal months10 = new BigDecimal(getProperty(ob, "money10"));
                                    BigDecimal months11 = new BigDecimal(getProperty(ob, "money11"));
                                    BigDecimal months12 = new BigDecimal(getProperty(ob, "money12"));
                                    month1 = months1.add(month1);
                                    month2 = months2.add(month2);
                                    month3 = months3.add(month3);
                                    month4 = months4.add(month4);
                                    month5 = months5.add(month5);
                                    month6 = months6.add(month6);
                                    month7 = months7.add(month7);
                                    month8 = months8.add(month8);
                                    month9 = months9.add(month9);
                                    month10 = months10.add(month10);
                                    month11 = months11.add(month11);
                                    month12 = months12.add(month12);
                                }
                                for (Object ob : jsongroupa2) {
                                    BigDecimal months1 = new BigDecimal(getProperty(ob, "money1"));
                                    BigDecimal months2 = new BigDecimal(getProperty(ob, "money2"));
                                    BigDecimal months3 = new BigDecimal(getProperty(ob, "money3"));
                                    BigDecimal months4 = new BigDecimal(getProperty(ob, "money4"));
                                    BigDecimal months5 = new BigDecimal(getProperty(ob, "money5"));
                                    BigDecimal months6 = new BigDecimal(getProperty(ob, "money6"));
                                    BigDecimal months7 = new BigDecimal(getProperty(ob, "money7"));
                                    BigDecimal months8 = new BigDecimal(getProperty(ob, "money8"));
                                    BigDecimal months9 = new BigDecimal(getProperty(ob, "money9"));
                                    BigDecimal months10 = new BigDecimal(getProperty(ob, "money10"));
                                    BigDecimal months11 = new BigDecimal(getProperty(ob, "money11"));
                                    BigDecimal months12 = new BigDecimal(getProperty(ob, "money12"));
                                    month1 = months1.add(month1);
                                    month2 = months2.add(month2);
                                    month3 = months3.add(month3);
                                    month4 = months4.add(month4);
                                    month5 = months5.add(month5);
                                    month6 = months6.add(month6);
                                    month7 = months7.add(month7);
                                    month8 = months8.add(month8);
                                    month9 = months9.add(month9);
                                    month10 = months10.add(month10);
                                    month11 = months11.add(month11);
                                    month12 = months12.add(month12);
                                }
                                incomeexpenditurevo.setAmount1(String.valueOf(month1));
                                incomeexpenditurevo.setAmount2(String.valueOf(month2));
                                incomeexpenditurevo.setAmount3(String.valueOf(month3));
                                incomeexpenditurevo.setAmount4(String.valueOf(month4));
                                incomeexpenditurevo.setAmount5(String.valueOf(month5));
                                incomeexpenditurevo.setAmount6(String.valueOf(month6));
                                incomeexpenditurevo.setAmount7(String.valueOf(month7));
                                incomeexpenditurevo.setAmount8(String.valueOf(month8));
                                incomeexpenditurevo.setAmount9(String.valueOf(month9));
                                incomeexpenditurevo.setAmount10(String.valueOf(month10));
                                incomeexpenditurevo.setAmount11(String.valueOf(month11));
                                incomeexpenditurevo.setAmount12(String.valueOf(month12));
                            } else if (iteA.getCode().equals("PJ147002")) {
                                for (CostCarryForward costlist : costcarryforwardlist) {
                                    if (costlist.getRegion().substring(6, 7).equals("01")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getCosttotal());
                                        planmonth1 = planmonth1.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("02")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getCosttotal());
                                        planmonth2 = planmonth2.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("03")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getCosttotal());
                                        planmonth3 = planmonth3.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("04")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getCosttotal());
                                        planmonth4 = planmonth4.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("05")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getCosttotal());
                                        planmonth5 = planmonth5.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("06")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getCosttotal());
                                        planmonth6 = planmonth6.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("07")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getCosttotal());
                                        planmonth7 = planmonth7.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("08")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getCosttotal());
                                        planmonth8 = planmonth8.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("09")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getCosttotal());
                                        planmonth9 = planmonth9.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("10")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getCosttotal());
                                        planmonth10 = planmonth10.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("11")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getCosttotal());
                                        planmonth11 = planmonth11.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("12")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getCosttotal());
                                        planmonth12 = planmonth12.add(intotal);
                                    }
                                }
                                incomeexpenditurevo.setPlanamount1(String.valueOf(planmonth1));
                                incomeexpenditurevo.setPlanamount2(String.valueOf(planmonth2));
                                incomeexpenditurevo.setPlanamount3(String.valueOf(planmonth3));
                                incomeexpenditurevo.setPlanamount4(String.valueOf(planmonth4));
                                incomeexpenditurevo.setPlanamount5(String.valueOf(planmonth5));
                                incomeexpenditurevo.setPlanamount6(String.valueOf(planmonth6));
                                incomeexpenditurevo.setPlanamount7(String.valueOf(planmonth7));
                                incomeexpenditurevo.setPlanamount8(String.valueOf(planmonth8));
                                incomeexpenditurevo.setPlanamount9(String.valueOf(planmonth9));
                                incomeexpenditurevo.setPlanamount10(String.valueOf(planmonth10));
                                incomeexpenditurevo.setPlanamount11(String.valueOf(planmonth11));
                                incomeexpenditurevo.setPlanamount12(String.valueOf(planmonth12));
                                BigDecimal month1 = new BigDecimal("0");
                                BigDecimal month2 = new BigDecimal("0");
                                BigDecimal month3 = new BigDecimal("0");
                                BigDecimal month4 = new BigDecimal("0");
                                BigDecimal month5 = new BigDecimal("0");
                                BigDecimal month6 = new BigDecimal("0");
                                BigDecimal month7 = new BigDecimal("0");
                                BigDecimal month8 = new BigDecimal("0");
                                BigDecimal month9 = new BigDecimal("0");
                                BigDecimal month10 = new BigDecimal("0");
                                BigDecimal month11 = new BigDecimal("0");
                                BigDecimal month12 = new BigDecimal("0");
                                incomeexpenditurevo.setThemename(iteA.getValue1());
                                JSONArray jsongroupb1 = JSONArray.parseArray(businessplanlist.get(0).getGroupB1());
                                JSONArray jsongroupb2 = JSONArray.parseArray(businessplanlist.get(0).getGroupB2());
                                JSONArray jsongroupb3 = JSONArray.parseArray(businessplanlist.get(0).getGroupB3());
                                JSONArray jsongroupc = JSONArray.parseArray(businessplanlist.get(0).getGroupC());
                                for (Object ob : jsongroupb1) {
                                    BigDecimal months1 = new BigDecimal(getProperty(ob, "money1"));
                                    BigDecimal months2 = new BigDecimal(getProperty(ob, "money2"));
                                    BigDecimal months3 = new BigDecimal(getProperty(ob, "money3"));
                                    BigDecimal months4 = new BigDecimal(getProperty(ob, "money4"));
                                    BigDecimal months5 = new BigDecimal(getProperty(ob, "money5"));
                                    BigDecimal months6 = new BigDecimal(getProperty(ob, "money6"));
                                    BigDecimal months7 = new BigDecimal(getProperty(ob, "money7"));
                                    BigDecimal months8 = new BigDecimal(getProperty(ob, "money8"));
                                    BigDecimal months9 = new BigDecimal(getProperty(ob, "money9"));
                                    BigDecimal months10 = new BigDecimal(getProperty(ob, "money10"));
                                    BigDecimal months11 = new BigDecimal(getProperty(ob, "money11"));
                                    BigDecimal months12 = new BigDecimal(getProperty(ob, "money12"));
                                    month1 = months1.add(month1);
                                    month2 = months2.add(month2);
                                    month3 = months3.add(month3);
                                    month4 = months4.add(month4);
                                    month5 = months5.add(month5);
                                    month6 = months6.add(month6);
                                    month7 = months7.add(month7);
                                    month8 = months8.add(month8);
                                    month9 = months9.add(month9);
                                    month10 = months10.add(month10);
                                    month11 = months11.add(month11);
                                    month12 = months12.add(month12);
                                }
                                for (Object ob : jsongroupb2) {
                                    BigDecimal months1 = new BigDecimal(getProperty(ob, "money1"));
                                    BigDecimal months2 = new BigDecimal(getProperty(ob, "money2"));
                                    BigDecimal months3 = new BigDecimal(getProperty(ob, "money3"));
                                    BigDecimal months4 = new BigDecimal(getProperty(ob, "money4"));
                                    BigDecimal months5 = new BigDecimal(getProperty(ob, "money5"));
                                    BigDecimal months6 = new BigDecimal(getProperty(ob, "money6"));
                                    BigDecimal months7 = new BigDecimal(getProperty(ob, "money7"));
                                    BigDecimal months8 = new BigDecimal(getProperty(ob, "money8"));
                                    BigDecimal months9 = new BigDecimal(getProperty(ob, "money9"));
                                    BigDecimal months10 = new BigDecimal(getProperty(ob, "money10"));
                                    BigDecimal months11 = new BigDecimal(getProperty(ob, "money11"));
                                    BigDecimal months12 = new BigDecimal(getProperty(ob, "money12"));
                                    month1 = months1.add(month1);
                                    month2 = months2.add(month2);
                                    month3 = months3.add(month3);
                                    month4 = months4.add(month4);
                                    month5 = months5.add(month5);
                                    month6 = months6.add(month6);
                                    month7 = months7.add(month7);
                                    month8 = months8.add(month8);
                                    month9 = months9.add(month9);
                                    month10 = months10.add(month10);
                                    month11 = months11.add(month11);
                                    month12 = months12.add(month12);
                                }
                                for (Object ob : jsongroupb3) {
                                    BigDecimal months1 = new BigDecimal(getProperty(ob, "money1"));
                                    BigDecimal months2 = new BigDecimal(getProperty(ob, "money2"));
                                    BigDecimal months3 = new BigDecimal(getProperty(ob, "money3"));
                                    BigDecimal months4 = new BigDecimal(getProperty(ob, "money4"));
                                    BigDecimal months5 = new BigDecimal(getProperty(ob, "money5"));
                                    BigDecimal months6 = new BigDecimal(getProperty(ob, "money6"));
                                    BigDecimal months7 = new BigDecimal(getProperty(ob, "money7"));
                                    BigDecimal months8 = new BigDecimal(getProperty(ob, "money8"));
                                    BigDecimal months9 = new BigDecimal(getProperty(ob, "money9"));
                                    BigDecimal months10 = new BigDecimal(getProperty(ob, "money10"));
                                    BigDecimal months11 = new BigDecimal(getProperty(ob, "money11"));
                                    BigDecimal months12 = new BigDecimal(getProperty(ob, "money12"));
                                    month1 = months1.add(month1);
                                    month2 = months2.add(month2);
                                    month3 = months3.add(month3);
                                    month4 = months4.add(month4);
                                    month5 = months5.add(month5);
                                    month6 = months6.add(month6);
                                    month7 = months7.add(month7);
                                    month8 = months8.add(month8);
                                    month9 = months9.add(month9);
                                    month10 = months10.add(month10);
                                    month11 = months11.add(month11);
                                    month12 = months12.add(month12);
                                }
                                for (Object ob : jsongroupc) {
                                    BigDecimal months1 = new BigDecimal(getProperty(ob, "money1"));
                                    BigDecimal months2 = new BigDecimal(getProperty(ob, "money2"));
                                    BigDecimal months3 = new BigDecimal(getProperty(ob, "money3"));
                                    BigDecimal months4 = new BigDecimal(getProperty(ob, "money4"));
                                    BigDecimal months5 = new BigDecimal(getProperty(ob, "money5"));
                                    BigDecimal months6 = new BigDecimal(getProperty(ob, "money6"));
                                    BigDecimal months7 = new BigDecimal(getProperty(ob, "money7"));
                                    BigDecimal months8 = new BigDecimal(getProperty(ob, "money8"));
                                    BigDecimal months9 = new BigDecimal(getProperty(ob, "money9"));
                                    BigDecimal months10 = new BigDecimal(getProperty(ob, "money10"));
                                    BigDecimal months11 = new BigDecimal(getProperty(ob, "money11"));
                                    BigDecimal months12 = new BigDecimal(getProperty(ob, "money12"));
                                    month1 = months1.add(month1);
                                    month2 = months2.add(month2);
                                    month3 = months3.add(month3);
                                    month4 = months4.add(month4);
                                    month5 = months5.add(month5);
                                    month6 = months6.add(month6);
                                    month7 = months7.add(month7);
                                    month8 = months8.add(month8);
                                    month9 = months9.add(month9);
                                    month10 = months10.add(month10);
                                    month11 = months11.add(month11);
                                    month12 = months12.add(month12);
                                }
                                incomeexpenditurevo.setAmount1(String.valueOf(month1));
                                incomeexpenditurevo.setAmount2(String.valueOf(month2));
                                incomeexpenditurevo.setAmount3(String.valueOf(month3));
                                incomeexpenditurevo.setAmount4(String.valueOf(month4));
                                incomeexpenditurevo.setAmount5(String.valueOf(month5));
                                incomeexpenditurevo.setAmount6(String.valueOf(month6));
                                incomeexpenditurevo.setAmount7(String.valueOf(month7));
                                incomeexpenditurevo.setAmount8(String.valueOf(month8));
                                incomeexpenditurevo.setAmount9(String.valueOf(month9));
                                incomeexpenditurevo.setAmount10(String.valueOf(month10));
                                incomeexpenditurevo.setAmount11(String.valueOf(month11));
                                incomeexpenditurevo.setAmount12(String.valueOf(month12));
                            } else if (iteA.getCode().equals("PJ147003")) {
                                for (CostCarryForward costlist : costcarryforwardlist) {
                                    if (costlist.getRegion().substring(6, 7).equals("01")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getPeocost());
                                        planmonth1 = planmonth1.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("02")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getPeocost());
                                        planmonth2 = planmonth2.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("03")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getPeocost());
                                        planmonth3 = planmonth3.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("04")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getPeocost());
                                        planmonth4 = planmonth4.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("05")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getPeocost());
                                        planmonth5 = planmonth5.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("06")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getPeocost());
                                        planmonth6 = planmonth6.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("07")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getPeocost());
                                        planmonth7 = planmonth7.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("08")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getPeocost());
                                        planmonth8 = planmonth8.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("09")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getPeocost());
                                        planmonth9 = planmonth9.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("10")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getPeocost());
                                        planmonth10 = planmonth10.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("11")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getPeocost());
                                        planmonth11 = planmonth11.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("12")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getPeocost());
                                        planmonth12 = planmonth12.add(intotal);
                                    }
                                }
                                incomeexpenditurevo.setPlanamount1(String.valueOf(planmonth1));
                                incomeexpenditurevo.setPlanamount2(String.valueOf(planmonth2));
                                incomeexpenditurevo.setPlanamount3(String.valueOf(planmonth3));
                                incomeexpenditurevo.setPlanamount4(String.valueOf(planmonth4));
                                incomeexpenditurevo.setPlanamount5(String.valueOf(planmonth5));
                                incomeexpenditurevo.setPlanamount6(String.valueOf(planmonth6));
                                incomeexpenditurevo.setPlanamount7(String.valueOf(planmonth7));
                                incomeexpenditurevo.setPlanamount8(String.valueOf(planmonth8));
                                incomeexpenditurevo.setPlanamount9(String.valueOf(planmonth9));
                                incomeexpenditurevo.setPlanamount10(String.valueOf(planmonth10));
                                incomeexpenditurevo.setPlanamount11(String.valueOf(planmonth11));
                                incomeexpenditurevo.setPlanamount12(String.valueOf(planmonth12));
                                incomeexpenditurevo.setThemename(iteA.getValue1());
                                for (PersonnelPlan list : personnelplanlist) {
                                    if (list.getType().equals("0")) {
                                        BigDecimal moneyavg = new BigDecimal(list.getMoneyavg());
                                        incomeexpenditurevo.setAmount1(String.valueOf(moneyavg.multiply(personnel1)));
                                        incomeexpenditurevo.setAmount2(String.valueOf(moneyavg.multiply(personnel2)));
                                        incomeexpenditurevo.setAmount3(String.valueOf(moneyavg.multiply(personnel3)));
                                        incomeexpenditurevo.setAmount4(String.valueOf(moneyavg.multiply(personnel4)));
                                        incomeexpenditurevo.setAmount5(String.valueOf(moneyavg.multiply(personnel5)));
                                        incomeexpenditurevo.setAmount6(String.valueOf(moneyavg.multiply(personnel6)));
                                        incomeexpenditurevo.setAmount7(String.valueOf(moneyavg.multiply(personnel7)));
                                        incomeexpenditurevo.setAmount8(String.valueOf(moneyavg.multiply(personnel8)));
                                        incomeexpenditurevo.setAmount9(String.valueOf(moneyavg.multiply(personnel9)));
                                        incomeexpenditurevo.setAmount10(String.valueOf(moneyavg.multiply(personnel10)));
                                        incomeexpenditurevo.setAmount11(String.valueOf(moneyavg.multiply(personnel11)));
                                        incomeexpenditurevo.setAmount12(String.valueOf(moneyavg.multiply(personnel12)));
                                    }
                                }
                            } else if (iteA.getCode().equals("PJ147004")) {
                                for (CostCarryForward costlist : costcarryforwardlist) {
                                    if (costlist.getRegion().substring(6, 7).equals("01")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOutcost());
                                        planmonth1 = planmonth1.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("02")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOutcost());
                                        planmonth2 = planmonth2.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("03")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOutcost());
                                        planmonth3 = planmonth3.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("04")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOutcost());
                                        planmonth4 = planmonth4.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("05")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOutcost());
                                        planmonth5 = planmonth5.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("06")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOutcost());
                                        planmonth6 = planmonth6.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("07")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOutcost());
                                        planmonth7 = planmonth7.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("08")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOutcost());
                                        planmonth8 = planmonth8.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("09")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOutcost());
                                        planmonth9 = planmonth9.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("10")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOutcost());
                                        planmonth10 = planmonth10.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("11")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOutcost());
                                        planmonth11 = planmonth11.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("12")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOutcost());
                                        planmonth12 = planmonth12.add(intotal);
                                    }
                                }
                                incomeexpenditurevo.setPlanamount1(String.valueOf(planmonth1));
                                incomeexpenditurevo.setPlanamount2(String.valueOf(planmonth2));
                                incomeexpenditurevo.setPlanamount3(String.valueOf(planmonth3));
                                incomeexpenditurevo.setPlanamount4(String.valueOf(planmonth4));
                                incomeexpenditurevo.setPlanamount5(String.valueOf(planmonth5));
                                incomeexpenditurevo.setPlanamount6(String.valueOf(planmonth6));
                                incomeexpenditurevo.setPlanamount7(String.valueOf(planmonth7));
                                incomeexpenditurevo.setPlanamount8(String.valueOf(planmonth8));
                                incomeexpenditurevo.setPlanamount9(String.valueOf(planmonth9));
                                incomeexpenditurevo.setPlanamount10(String.valueOf(planmonth10));
                                incomeexpenditurevo.setPlanamount11(String.valueOf(planmonth11));
                                incomeexpenditurevo.setPlanamount12(String.valueOf(planmonth12));
                                incomeexpenditurevo.setThemename(iteA.getValue1());
                                for (PersonnelPlan list : personnelplanlist) {
                                    if (list.getType().equals("1")) {
                                        BigDecimal moneyavg = new BigDecimal(list.getMoneyavg());
                                        incomeexpenditurevo.setAmount1(String.valueOf(moneyavg.multiply(wpersonnel1)));
                                        incomeexpenditurevo.setAmount2(String.valueOf(moneyavg.multiply(wpersonnel2)));
                                        incomeexpenditurevo.setAmount3(String.valueOf(moneyavg.multiply(wpersonnel3)));
                                        incomeexpenditurevo.setAmount4(String.valueOf(moneyavg.multiply(wpersonnel4)));
                                        incomeexpenditurevo.setAmount5(String.valueOf(moneyavg.multiply(wpersonnel5)));
                                        incomeexpenditurevo.setAmount6(String.valueOf(moneyavg.multiply(wpersonnel6)));
                                        incomeexpenditurevo.setAmount7(String.valueOf(moneyavg.multiply(wpersonnel7)));
                                        incomeexpenditurevo.setAmount8(String.valueOf(moneyavg.multiply(wpersonnel8)));
                                        incomeexpenditurevo.setAmount9(String.valueOf(moneyavg.multiply(wpersonnel9)));
                                        incomeexpenditurevo.setAmount10(String.valueOf(moneyavg.multiply(wpersonnel10)));
                                        incomeexpenditurevo.setAmount11(String.valueOf(moneyavg.multiply(wpersonnel11)));
                                        incomeexpenditurevo.setAmount12(String.valueOf(moneyavg.multiply(wpersonnel12)));
                                    }
                                }
                            } else if (iteA.getCode().equals("PJ147005")) {
                                for (CostCarryForward costlist : costcarryforwardlist) {
                                    if (costlist.getRegion().substring(6, 7).equals("01")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationsoft());
                                        planmonth1 = planmonth1.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("02")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationsoft());
                                        planmonth2 = planmonth2.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("03")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationsoft());
                                        planmonth3 = planmonth3.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("04")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationsoft());
                                        planmonth4 = planmonth4.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("05")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationsoft());
                                        planmonth5 = planmonth5.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("06")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationsoft());
                                        planmonth6 = planmonth6.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("07")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationsoft());
                                        planmonth7 = planmonth7.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("08")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationsoft());
                                        planmonth8 = planmonth8.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("09")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationsoft());
                                        planmonth9 = planmonth9.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("10")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationsoft());
                                        planmonth10 = planmonth10.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("11")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationsoft());
                                        planmonth11 = planmonth11.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("12")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationsoft());
                                        planmonth12 = planmonth12.add(intotal);
                                    }
                                }
                                incomeexpenditurevo.setPlanamount1(String.valueOf(planmonth1));
                                incomeexpenditurevo.setPlanamount2(String.valueOf(planmonth2));
                                incomeexpenditurevo.setPlanamount3(String.valueOf(planmonth3));
                                incomeexpenditurevo.setPlanamount4(String.valueOf(planmonth4));
                                incomeexpenditurevo.setPlanamount5(String.valueOf(planmonth5));
                                incomeexpenditurevo.setPlanamount6(String.valueOf(planmonth6));
                                incomeexpenditurevo.setPlanamount7(String.valueOf(planmonth7));
                                incomeexpenditurevo.setPlanamount8(String.valueOf(planmonth8));
                                incomeexpenditurevo.setPlanamount9(String.valueOf(planmonth9));
                                incomeexpenditurevo.setPlanamount10(String.valueOf(planmonth10));
                                incomeexpenditurevo.setPlanamount11(String.valueOf(planmonth11));
                                incomeexpenditurevo.setPlanamount12(String.valueOf(planmonth12));
                                BigDecimal month1 = new BigDecimal("0");
                                BigDecimal month2 = new BigDecimal("0");
                                BigDecimal month3 = new BigDecimal("0");
                                BigDecimal month4 = new BigDecimal("0");
                                BigDecimal month5 = new BigDecimal("0");
                                BigDecimal month6 = new BigDecimal("0");
                                BigDecimal month7 = new BigDecimal("0");
                                BigDecimal month8 = new BigDecimal("0");
                                BigDecimal month9 = new BigDecimal("0");
                                BigDecimal month10 = new BigDecimal("0");
                                BigDecimal month11 = new BigDecimal("0");
                                BigDecimal month12 = new BigDecimal("0");
                                incomeexpenditurevo.setThemename(iteA.getValue1());
                                JSONArray jsonassetsnewyear = JSONArray.parseArray(businessplanlist.get(0).getAssets_newyear());
                                JSONArray jsonassetslastyear = JSONArray.parseArray(businessplanlist.get(0).getAssets_lastyear());
                                JSONArray jsonassetslodyear = JSONArray.parseArray(businessplanlist.get(0).getAssets_lodyear());
                                for (Object ob : jsonassetsnewyear) {
                                    BigDecimal months1 = new BigDecimal(getProperty(ob, "money1"));
                                    BigDecimal months2 = new BigDecimal(getProperty(ob, "money2"));
                                    BigDecimal months3 = new BigDecimal(getProperty(ob, "money3"));
                                    BigDecimal months4 = new BigDecimal(getProperty(ob, "money4"));
                                    BigDecimal months5 = new BigDecimal(getProperty(ob, "money5"));
                                    BigDecimal months6 = new BigDecimal(getProperty(ob, "money6"));
                                    BigDecimal months7 = new BigDecimal(getProperty(ob, "money7"));
                                    BigDecimal months8 = new BigDecimal(getProperty(ob, "money8"));
                                    BigDecimal months9 = new BigDecimal(getProperty(ob, "money9"));
                                    BigDecimal months10 = new BigDecimal(getProperty(ob, "money10"));
                                    BigDecimal months11 = new BigDecimal(getProperty(ob, "money11"));
                                    BigDecimal months12 = new BigDecimal(getProperty(ob, "money12"));
                                    month1 = months1.add(month1);
                                    month2 = months2.add(month2);
                                    month3 = months3.add(month3);
                                    month4 = months4.add(month4);
                                    month5 = months5.add(month5);
                                    month6 = months6.add(month6);
                                    month7 = months7.add(month7);
                                    month8 = months8.add(month8);
                                    month9 = months9.add(month9);
                                    month10 = months10.add(month10);
                                    month11 = months11.add(month11);
                                    month12 = months12.add(month12);
                                }
                                for (Object ob : jsonassetslastyear) {
                                    BigDecimal months1 = new BigDecimal(getProperty(ob, "money1"));
                                    BigDecimal months2 = new BigDecimal(getProperty(ob, "money2"));
                                    BigDecimal months3 = new BigDecimal(getProperty(ob, "money3"));
                                    BigDecimal months4 = new BigDecimal(getProperty(ob, "money4"));
                                    BigDecimal months5 = new BigDecimal(getProperty(ob, "money5"));
                                    BigDecimal months6 = new BigDecimal(getProperty(ob, "money6"));
                                    BigDecimal months7 = new BigDecimal(getProperty(ob, "money7"));
                                    BigDecimal months8 = new BigDecimal(getProperty(ob, "money8"));
                                    BigDecimal months9 = new BigDecimal(getProperty(ob, "money9"));
                                    BigDecimal months10 = new BigDecimal(getProperty(ob, "money10"));
                                    BigDecimal months11 = new BigDecimal(getProperty(ob, "money11"));
                                    BigDecimal months12 = new BigDecimal(getProperty(ob, "money12"));
                                    month1 = months1.add(month1);
                                    month2 = months2.add(month2);
                                    month3 = months3.add(month3);
                                    month4 = months4.add(month4);
                                    month5 = months5.add(month5);
                                    month6 = months6.add(month6);
                                    month7 = months7.add(month7);
                                    month8 = months8.add(month8);
                                    month9 = months9.add(month9);
                                    month10 = months10.add(month10);
                                    month11 = months11.add(month11);
                                    month12 = months12.add(month12);
                                }
                                for (Object ob : jsonassetslodyear) {
                                    BigDecimal months1 = new BigDecimal(getProperty(ob, "money1"));
                                    BigDecimal months2 = new BigDecimal(getProperty(ob, "money2"));
                                    BigDecimal months3 = new BigDecimal(getProperty(ob, "money3"));
                                    BigDecimal months4 = new BigDecimal(getProperty(ob, "money4"));
                                    BigDecimal months5 = new BigDecimal(getProperty(ob, "money5"));
                                    BigDecimal months6 = new BigDecimal(getProperty(ob, "money6"));
                                    BigDecimal months7 = new BigDecimal(getProperty(ob, "money7"));
                                    BigDecimal months8 = new BigDecimal(getProperty(ob, "money8"));
                                    BigDecimal months9 = new BigDecimal(getProperty(ob, "money9"));
                                    BigDecimal months10 = new BigDecimal(getProperty(ob, "money10"));
                                    BigDecimal months11 = new BigDecimal(getProperty(ob, "money11"));
                                    BigDecimal months12 = new BigDecimal(getProperty(ob, "money12"));
                                    month1 = months1.add(month1);
                                    month2 = months2.add(month2);
                                    month3 = months3.add(month3);
                                    month4 = months4.add(month4);
                                    month5 = months5.add(month5);
                                    month6 = months6.add(month6);
                                    month7 = months7.add(month7);
                                    month8 = months8.add(month8);
                                    month9 = months9.add(month9);
                                    month10 = months10.add(month10);
                                    month11 = months11.add(month11);
                                    month12 = months12.add(month12);
                                }
                                incomeexpenditurevo.setAmount1(String.valueOf(month1));
                                incomeexpenditurevo.setAmount2(String.valueOf(month2));
                                incomeexpenditurevo.setAmount3(String.valueOf(month3));
                                incomeexpenditurevo.setAmount4(String.valueOf(month4));
                                incomeexpenditurevo.setAmount5(String.valueOf(month5));
                                incomeexpenditurevo.setAmount6(String.valueOf(month6));
                                incomeexpenditurevo.setAmount7(String.valueOf(month7));
                                incomeexpenditurevo.setAmount8(String.valueOf(month8));
                                incomeexpenditurevo.setAmount9(String.valueOf(month9));
                                incomeexpenditurevo.setAmount10(String.valueOf(month10));
                                incomeexpenditurevo.setAmount11(String.valueOf(month11));
                                incomeexpenditurevo.setAmount12(String.valueOf(month12));
                            } else if (iteA.getCode().equals("PJ147006")) {
                                for (CostCarryForward costlist : costcarryforwardlist) {
                                    if (costlist.getRegion().substring(6, 7).equals("01")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationequipment());
                                        planmonth1 = planmonth1.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("02")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationequipment());
                                        planmonth2 = planmonth2.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("03")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationequipment());
                                        planmonth3 = planmonth3.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("04")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationequipment());
                                        planmonth4 = planmonth4.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("05")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationequipment());
                                        planmonth5 = planmonth5.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("06")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationequipment());
                                        planmonth6 = planmonth6.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("07")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationequipment());
                                        planmonth7 = planmonth7.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("08")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationequipment());
                                        planmonth8 = planmonth8.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("09")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationequipment());
                                        planmonth9 = planmonth9.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("10")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationequipment());
                                        planmonth10 = planmonth10.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("11")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationequipment());
                                        planmonth11 = planmonth11.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("12")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationequipment());
                                        planmonth12 = planmonth12.add(intotal);
                                    }
                                }
                                incomeexpenditurevo.setPlanamount1(String.valueOf(planmonth1));
                                incomeexpenditurevo.setPlanamount2(String.valueOf(planmonth2));
                                incomeexpenditurevo.setPlanamount3(String.valueOf(planmonth3));
                                incomeexpenditurevo.setPlanamount4(String.valueOf(planmonth4));
                                incomeexpenditurevo.setPlanamount5(String.valueOf(planmonth5));
                                incomeexpenditurevo.setPlanamount6(String.valueOf(planmonth6));
                                incomeexpenditurevo.setPlanamount7(String.valueOf(planmonth7));
                                incomeexpenditurevo.setPlanamount8(String.valueOf(planmonth8));
                                incomeexpenditurevo.setPlanamount9(String.valueOf(planmonth9));
                                incomeexpenditurevo.setPlanamount10(String.valueOf(planmonth10));
                                incomeexpenditurevo.setPlanamount11(String.valueOf(planmonth11));
                                incomeexpenditurevo.setPlanamount12(String.valueOf(planmonth12));
                                BigDecimal month1 = new BigDecimal("0");
                                BigDecimal month2 = new BigDecimal("0");
                                BigDecimal month3 = new BigDecimal("0");
                                BigDecimal month4 = new BigDecimal("0");
                                BigDecimal month5 = new BigDecimal("0");
                                BigDecimal month6 = new BigDecimal("0");
                                BigDecimal month7 = new BigDecimal("0");
                                BigDecimal month8 = new BigDecimal("0");
                                BigDecimal month9 = new BigDecimal("0");
                                BigDecimal month10 = new BigDecimal("0");
                                BigDecimal month11 = new BigDecimal("0");
                                BigDecimal month12 = new BigDecimal("0");
                                incomeexpenditurevo.setThemename(iteA.getValue1());
                                JSONArray jsonequipmentnewyear = JSONArray.parseArray(businessplanlist.get(0).getEquipment_newyear());
                                JSONArray jsonequipmentlastyear = JSONArray.parseArray(businessplanlist.get(0).getEquipment_lastyear());
                                JSONArray jsonequipmentlodyear = JSONArray.parseArray(businessplanlist.get(0).getEquipment_lodyear());
                                for (Object ob : jsonequipmentnewyear) {
                                    BigDecimal months1 = new BigDecimal(getProperty(ob, "money1"));
                                    BigDecimal months2 = new BigDecimal(getProperty(ob, "money2"));
                                    BigDecimal months3 = new BigDecimal(getProperty(ob, "money3"));
                                    BigDecimal months4 = new BigDecimal(getProperty(ob, "money4"));
                                    BigDecimal months5 = new BigDecimal(getProperty(ob, "money5"));
                                    BigDecimal months6 = new BigDecimal(getProperty(ob, "money6"));
                                    BigDecimal months7 = new BigDecimal(getProperty(ob, "money7"));
                                    BigDecimal months8 = new BigDecimal(getProperty(ob, "money8"));
                                    BigDecimal months9 = new BigDecimal(getProperty(ob, "money9"));
                                    BigDecimal months10 = new BigDecimal(getProperty(ob, "money10"));
                                    BigDecimal months11 = new BigDecimal(getProperty(ob, "money11"));
                                    BigDecimal months12 = new BigDecimal(getProperty(ob, "money12"));
                                    month1 = months1.add(month1);
                                    month2 = months2.add(month2);
                                    month3 = months3.add(month3);
                                    month4 = months4.add(month4);
                                    month5 = months5.add(month5);
                                    month6 = months6.add(month6);
                                    month7 = months7.add(month7);
                                    month8 = months8.add(month8);
                                    month9 = months9.add(month9);
                                    month10 = months10.add(month10);
                                    month11 = months11.add(month11);
                                    month12 = months12.add(month12);
                                }
                                for (Object ob : jsonequipmentlastyear) {
                                    BigDecimal months1 = new BigDecimal(getProperty(ob, "money1"));
                                    BigDecimal months2 = new BigDecimal(getProperty(ob, "money2"));
                                    BigDecimal months3 = new BigDecimal(getProperty(ob, "money3"));
                                    BigDecimal months4 = new BigDecimal(getProperty(ob, "money4"));
                                    BigDecimal months5 = new BigDecimal(getProperty(ob, "money5"));
                                    BigDecimal months6 = new BigDecimal(getProperty(ob, "money6"));
                                    BigDecimal months7 = new BigDecimal(getProperty(ob, "money7"));
                                    BigDecimal months8 = new BigDecimal(getProperty(ob, "money8"));
                                    BigDecimal months9 = new BigDecimal(getProperty(ob, "money9"));
                                    BigDecimal months10 = new BigDecimal(getProperty(ob, "money10"));
                                    BigDecimal months11 = new BigDecimal(getProperty(ob, "money11"));
                                    BigDecimal months12 = new BigDecimal(getProperty(ob, "money12"));
                                    month1 = months1.add(month1);
                                    month2 = months2.add(month2);
                                    month3 = months3.add(month3);
                                    month4 = months4.add(month4);
                                    month5 = months5.add(month5);
                                    month6 = months6.add(month6);
                                    month7 = months7.add(month7);
                                    month8 = months8.add(month8);
                                    month9 = months9.add(month9);
                                    month10 = months10.add(month10);
                                    month11 = months11.add(month11);
                                    month12 = months12.add(month12);
                                }
                                for (Object ob : jsonequipmentlodyear) {
                                    BigDecimal months1 = new BigDecimal(getProperty(ob, "money1"));
                                    BigDecimal months2 = new BigDecimal(getProperty(ob, "money2"));
                                    BigDecimal months3 = new BigDecimal(getProperty(ob, "money3"));
                                    BigDecimal months4 = new BigDecimal(getProperty(ob, "money4"));
                                    BigDecimal months5 = new BigDecimal(getProperty(ob, "money5"));
                                    BigDecimal months6 = new BigDecimal(getProperty(ob, "money6"));
                                    BigDecimal months7 = new BigDecimal(getProperty(ob, "money7"));
                                    BigDecimal months8 = new BigDecimal(getProperty(ob, "money8"));
                                    BigDecimal months9 = new BigDecimal(getProperty(ob, "money9"));
                                    BigDecimal months10 = new BigDecimal(getProperty(ob, "money10"));
                                    BigDecimal months11 = new BigDecimal(getProperty(ob, "money11"));
                                    BigDecimal months12 = new BigDecimal(getProperty(ob, "money12"));
                                    month1 = months1.add(month1);
                                    month2 = months2.add(month2);
                                    month3 = months3.add(month3);
                                    month4 = months4.add(month4);
                                    month5 = months5.add(month5);
                                    month6 = months6.add(month6);
                                    month7 = months7.add(month7);
                                    month8 = months8.add(month8);
                                    month9 = months9.add(month9);
                                    month10 = months10.add(month10);
                                    month11 = months11.add(month11);
                                    month12 = months12.add(month12);
                                }
                                incomeexpenditurevo.setAmount1(String.valueOf(month1));
                                incomeexpenditurevo.setAmount2(String.valueOf(month2));
                                incomeexpenditurevo.setAmount3(String.valueOf(month3));
                                incomeexpenditurevo.setAmount4(String.valueOf(month4));
                                incomeexpenditurevo.setAmount5(String.valueOf(month5));
                                incomeexpenditurevo.setAmount6(String.valueOf(month6));
                                incomeexpenditurevo.setAmount7(String.valueOf(month7));
                                incomeexpenditurevo.setAmount8(String.valueOf(month8));
                                incomeexpenditurevo.setAmount9(String.valueOf(month9));
                                incomeexpenditurevo.setAmount10(String.valueOf(month10));
                                incomeexpenditurevo.setAmount11(String.valueOf(month11));
                                incomeexpenditurevo.setAmount12(String.valueOf(month12));
                            } else if (iteA.getCode().equals("PJ147007")) {
                                for (CostCarryForward costlist : costcarryforwardlist) {
                                    if (costlist.getRegion().substring(6, 7).equals("01")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getRent());
                                        planmonth1 = planmonth1.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("02")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getRent());
                                        planmonth2 = planmonth2.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("03")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getRent());
                                        planmonth3 = planmonth3.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("04")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getRent());
                                        planmonth4 = planmonth4.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("05")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getRent());
                                        planmonth5 = planmonth5.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("06")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getRent());
                                        planmonth6 = planmonth6.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("07")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getRent());
                                        planmonth7 = planmonth7.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("08")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getRent());
                                        planmonth8 = planmonth8.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("09")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getRent());
                                        planmonth9 = planmonth9.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("10")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getRent());
                                        planmonth10 = planmonth10.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("11")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getRent());
                                        planmonth11 = planmonth11.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("12")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getRent());
                                        planmonth12 = planmonth12.add(intotal);
                                    }
                                }
                                incomeexpenditurevo.setPlanamount1(String.valueOf(planmonth1));
                                incomeexpenditurevo.setPlanamount2(String.valueOf(planmonth2));
                                incomeexpenditurevo.setPlanamount3(String.valueOf(planmonth3));
                                incomeexpenditurevo.setPlanamount4(String.valueOf(planmonth4));
                                incomeexpenditurevo.setPlanamount5(String.valueOf(planmonth5));
                                incomeexpenditurevo.setPlanamount6(String.valueOf(planmonth6));
                                incomeexpenditurevo.setPlanamount7(String.valueOf(planmonth7));
                                incomeexpenditurevo.setPlanamount8(String.valueOf(planmonth8));
                                incomeexpenditurevo.setPlanamount9(String.valueOf(planmonth9));
                                incomeexpenditurevo.setPlanamount10(String.valueOf(planmonth10));
                                incomeexpenditurevo.setPlanamount11(String.valueOf(planmonth11));
                                incomeexpenditurevo.setPlanamount12(String.valueOf(planmonth12));
                                BigDecimal month1 = new BigDecimal("0");
                                BigDecimal month2 = new BigDecimal("0");
                                BigDecimal month3 = new BigDecimal("0");
                                BigDecimal month4 = new BigDecimal("0");
                                BigDecimal month5 = new BigDecimal("0");
                                BigDecimal month6 = new BigDecimal("0");
                                BigDecimal month7 = new BigDecimal("0");
                                BigDecimal month8 = new BigDecimal("0");
                                BigDecimal month9 = new BigDecimal("0");
                                BigDecimal month10 = new BigDecimal("0");
                                BigDecimal month11 = new BigDecimal("0");
                                BigDecimal month12 = new BigDecimal("0");
                                incomeexpenditurevo.setThemename(iteA.getValue1());
                                JSONArray jsontableo = JSONArray.parseArray(businessplanlist.get(0).getTableO());
                                for (Object ob : jsontableo) {
                                    String type = getProperty(ob, "type");
                                    if (type.equals("PJ111002")) {
                                        BigDecimal months1 = new BigDecimal(getProperty(ob, "money1"));
                                        BigDecimal months2 = new BigDecimal(getProperty(ob, "money2"));
                                        BigDecimal months3 = new BigDecimal(getProperty(ob, "money3"));
                                        BigDecimal months4 = new BigDecimal(getProperty(ob, "money4"));
                                        BigDecimal months5 = new BigDecimal(getProperty(ob, "money5"));
                                        BigDecimal months6 = new BigDecimal(getProperty(ob, "money6"));
                                        BigDecimal months7 = new BigDecimal(getProperty(ob, "money7"));
                                        BigDecimal months8 = new BigDecimal(getProperty(ob, "money8"));
                                        BigDecimal months9 = new BigDecimal(getProperty(ob, "money9"));
                                        BigDecimal months10 = new BigDecimal(getProperty(ob, "money10"));
                                        BigDecimal months11 = new BigDecimal(getProperty(ob, "money11"));
                                        BigDecimal months12 = new BigDecimal(getProperty(ob, "money12"));
                                        month1 = months1.add(month1);
                                        month2 = months2.add(month2);
                                        month3 = months3.add(month3);
                                        month4 = months4.add(month4);
                                        month5 = months5.add(month5);
                                        month6 = months6.add(month6);
                                        month7 = months7.add(month7);
                                        month8 = months8.add(month8);
                                        month9 = months9.add(month9);
                                        month10 = months10.add(month10);
                                        month11 = months11.add(month11);
                                        month12 = months12.add(month12);
                                    }
                                }
                                incomeexpenditurevo.setAmount1(String.valueOf(month1));
                                incomeexpenditurevo.setAmount2(String.valueOf(month2));
                                incomeexpenditurevo.setAmount3(String.valueOf(month3));
                                incomeexpenditurevo.setAmount4(String.valueOf(month4));
                                incomeexpenditurevo.setAmount5(String.valueOf(month5));
                                incomeexpenditurevo.setAmount6(String.valueOf(month6));
                                incomeexpenditurevo.setAmount7(String.valueOf(month7));
                                incomeexpenditurevo.setAmount8(String.valueOf(month8));
                                incomeexpenditurevo.setAmount9(String.valueOf(month9));
                                incomeexpenditurevo.setAmount10(String.valueOf(month10));
                                incomeexpenditurevo.setAmount11(String.valueOf(month11));
                                incomeexpenditurevo.setAmount12(String.valueOf(month12));
                            } else if (iteA.getCode().equals("PJ147008")) {
                                for (CostCarryForward costlist : costcarryforwardlist) {
                                    if (costlist.getRegion().substring(6, 7).equals("01")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getInwetuo());
                                        planmonth1 = planmonth1.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("02")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getInwetuo());
                                        planmonth2 = planmonth2.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("03")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getInwetuo());
                                        planmonth3 = planmonth3.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("04")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getInwetuo());
                                        planmonth4 = planmonth4.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("05")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getInwetuo());
                                        planmonth5 = planmonth5.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("06")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getInwetuo());
                                        planmonth6 = planmonth6.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("07")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getInwetuo());
                                        planmonth7 = planmonth7.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("08")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getInwetuo());
                                        planmonth8 = planmonth8.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("09")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getInwetuo());
                                        planmonth9 = planmonth9.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("10")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getInwetuo());
                                        planmonth10 = planmonth10.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("11")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getInwetuo());
                                        planmonth11 = planmonth11.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("12")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getInwetuo());
                                        planmonth12 = planmonth12.add(intotal);
                                    }
                                }
                                incomeexpenditurevo.setPlanamount1(String.valueOf(planmonth1));
                                incomeexpenditurevo.setPlanamount2(String.valueOf(planmonth2));
                                incomeexpenditurevo.setPlanamount3(String.valueOf(planmonth3));
                                incomeexpenditurevo.setPlanamount4(String.valueOf(planmonth4));
                                incomeexpenditurevo.setPlanamount5(String.valueOf(planmonth5));
                                incomeexpenditurevo.setPlanamount6(String.valueOf(planmonth6));
                                incomeexpenditurevo.setPlanamount7(String.valueOf(planmonth7));
                                incomeexpenditurevo.setPlanamount8(String.valueOf(planmonth8));
                                incomeexpenditurevo.setPlanamount9(String.valueOf(planmonth9));
                                incomeexpenditurevo.setPlanamount10(String.valueOf(planmonth10));
                                incomeexpenditurevo.setPlanamount11(String.valueOf(planmonth11));
                                incomeexpenditurevo.setPlanamount12(String.valueOf(planmonth12));
                                BigDecimal month1 = new BigDecimal("0");
                                BigDecimal month2 = new BigDecimal("0");
                                BigDecimal month3 = new BigDecimal("0");
                                BigDecimal month4 = new BigDecimal("0");
                                BigDecimal month5 = new BigDecimal("0");
                                BigDecimal month6 = new BigDecimal("0");
                                BigDecimal month7 = new BigDecimal("0");
                                BigDecimal month8 = new BigDecimal("0");
                                BigDecimal month9 = new BigDecimal("0");
                                BigDecimal month10 = new BigDecimal("0");
                                BigDecimal month11 = new BigDecimal("0");
                                BigDecimal month12 = new BigDecimal("0");
                                incomeexpenditurevo.setThemename(iteA.getValue1());
                                JSONArray jsongroupb3 = JSONArray.parseArray(businessplanlist.get(0).getGroupB3());
                                for (Object ob : jsongroupb3) {
                                    BigDecimal months1 = new BigDecimal(getProperty(ob, "money1"));
                                    BigDecimal months2 = new BigDecimal(getProperty(ob, "money2"));
                                    BigDecimal months3 = new BigDecimal(getProperty(ob, "money3"));
                                    BigDecimal months4 = new BigDecimal(getProperty(ob, "money4"));
                                    BigDecimal months5 = new BigDecimal(getProperty(ob, "money5"));
                                    BigDecimal months6 = new BigDecimal(getProperty(ob, "money6"));
                                    BigDecimal months7 = new BigDecimal(getProperty(ob, "money7"));
                                    BigDecimal months8 = new BigDecimal(getProperty(ob, "money8"));
                                    BigDecimal months9 = new BigDecimal(getProperty(ob, "money9"));
                                    BigDecimal months10 = new BigDecimal(getProperty(ob, "money10"));
                                    BigDecimal months11 = new BigDecimal(getProperty(ob, "money11"));
                                    BigDecimal months12 = new BigDecimal(getProperty(ob, "money12"));
                                    month1 = months1.add(month1);
                                    month2 = months2.add(month2);
                                    month3 = months3.add(month3);
                                    month4 = months4.add(month4);
                                    month5 = months5.add(month5);
                                    month6 = months6.add(month6);
                                    month7 = months7.add(month7);
                                    month8 = months8.add(month8);
                                    month9 = months9.add(month9);
                                    month10 = months10.add(month10);
                                    month11 = months11.add(month11);
                                    month12 = months12.add(month12);
                                }
                                incomeexpenditurevo.setAmount1(String.valueOf(month1));
                                incomeexpenditurevo.setAmount2(String.valueOf(month2));
                                incomeexpenditurevo.setAmount3(String.valueOf(month3));
                                incomeexpenditurevo.setAmount4(String.valueOf(month4));
                                incomeexpenditurevo.setAmount5(String.valueOf(month5));
                                incomeexpenditurevo.setAmount6(String.valueOf(month6));
                                incomeexpenditurevo.setAmount7(String.valueOf(month7));
                                incomeexpenditurevo.setAmount8(String.valueOf(month8));
                                incomeexpenditurevo.setAmount9(String.valueOf(month9));
                                incomeexpenditurevo.setAmount10(String.valueOf(month10));
                                incomeexpenditurevo.setAmount11(String.valueOf(month11));
                                incomeexpenditurevo.setAmount12(String.valueOf(month12));
                            } else if (iteA.getCode().equals("PJ147009")) {
                                for (CostCarryForward costlist : costcarryforwardlist) {
                                    if (costlist.getRegion().substring(6, 7).equals("01")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal()).subtract(new BigDecimal(costlist.getInwetuo())).subtract(new BigDecimal(costlist.getRent())).subtract(new BigDecimal(costlist.getCosttotal())).subtract(new BigDecimal(costlist.getPeocost())).subtract(new BigDecimal(costlist.getOutcost())).subtract(new BigDecimal(costlist.getDepreciationsoft())).subtract(new BigDecimal(costlist.getDepreciationequipment()));
                                        planmonth1 = planmonth1.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("02")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal()).subtract(new BigDecimal(costlist.getInwetuo())).subtract(new BigDecimal(costlist.getRent())).subtract(new BigDecimal(costlist.getCosttotal())).subtract(new BigDecimal(costlist.getPeocost())).subtract(new BigDecimal(costlist.getOutcost())).subtract(new BigDecimal(costlist.getDepreciationsoft())).subtract(new BigDecimal(costlist.getDepreciationequipment()));
                                        planmonth2 = planmonth2.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("03")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal()).subtract(new BigDecimal(costlist.getInwetuo())).subtract(new BigDecimal(costlist.getRent())).subtract(new BigDecimal(costlist.getCosttotal())).subtract(new BigDecimal(costlist.getPeocost())).subtract(new BigDecimal(costlist.getOutcost())).subtract(new BigDecimal(costlist.getDepreciationsoft())).subtract(new BigDecimal(costlist.getDepreciationequipment()));
                                        planmonth3 = planmonth3.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("04")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal()).subtract(new BigDecimal(costlist.getInwetuo())).subtract(new BigDecimal(costlist.getRent())).subtract(new BigDecimal(costlist.getCosttotal())).subtract(new BigDecimal(costlist.getPeocost())).subtract(new BigDecimal(costlist.getOutcost())).subtract(new BigDecimal(costlist.getDepreciationsoft())).subtract(new BigDecimal(costlist.getDepreciationequipment()));
                                        planmonth4 = planmonth4.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("05")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal()).subtract(new BigDecimal(costlist.getInwetuo())).subtract(new BigDecimal(costlist.getRent())).subtract(new BigDecimal(costlist.getCosttotal())).subtract(new BigDecimal(costlist.getPeocost())).subtract(new BigDecimal(costlist.getOutcost())).subtract(new BigDecimal(costlist.getDepreciationsoft())).subtract(new BigDecimal(costlist.getDepreciationequipment()));
                                        planmonth5 = planmonth5.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("06")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal()).subtract(new BigDecimal(costlist.getInwetuo())).subtract(new BigDecimal(costlist.getRent())).subtract(new BigDecimal(costlist.getCosttotal())).subtract(new BigDecimal(costlist.getPeocost())).subtract(new BigDecimal(costlist.getOutcost())).subtract(new BigDecimal(costlist.getDepreciationsoft())).subtract(new BigDecimal(costlist.getDepreciationequipment()));
                                        planmonth6 = planmonth6.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("07")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal()).subtract(new BigDecimal(costlist.getInwetuo())).subtract(new BigDecimal(costlist.getRent())).subtract(new BigDecimal(costlist.getCosttotal())).subtract(new BigDecimal(costlist.getPeocost())).subtract(new BigDecimal(costlist.getOutcost())).subtract(new BigDecimal(costlist.getDepreciationsoft())).subtract(new BigDecimal(costlist.getDepreciationequipment()));
                                        planmonth7 = planmonth7.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("08")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal()).subtract(new BigDecimal(costlist.getInwetuo())).subtract(new BigDecimal(costlist.getRent())).subtract(new BigDecimal(costlist.getCosttotal())).subtract(new BigDecimal(costlist.getPeocost())).subtract(new BigDecimal(costlist.getOutcost())).subtract(new BigDecimal(costlist.getDepreciationsoft())).subtract(new BigDecimal(costlist.getDepreciationequipment()));
                                        planmonth8 = planmonth8.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("09")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal()).subtract(new BigDecimal(costlist.getInwetuo())).subtract(new BigDecimal(costlist.getRent())).subtract(new BigDecimal(costlist.getCosttotal())).subtract(new BigDecimal(costlist.getPeocost())).subtract(new BigDecimal(costlist.getOutcost())).subtract(new BigDecimal(costlist.getDepreciationsoft())).subtract(new BigDecimal(costlist.getDepreciationequipment()));
                                        planmonth9 = planmonth9.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("10")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal()).subtract(new BigDecimal(costlist.getInwetuo())).subtract(new BigDecimal(costlist.getRent())).subtract(new BigDecimal(costlist.getCosttotal())).subtract(new BigDecimal(costlist.getPeocost())).subtract(new BigDecimal(costlist.getOutcost())).subtract(new BigDecimal(costlist.getDepreciationsoft())).subtract(new BigDecimal(costlist.getDepreciationequipment()));
                                        planmonth10 = planmonth10.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("11")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal()).subtract(new BigDecimal(costlist.getInwetuo())).subtract(new BigDecimal(costlist.getRent())).subtract(new BigDecimal(costlist.getCosttotal())).subtract(new BigDecimal(costlist.getPeocost())).subtract(new BigDecimal(costlist.getOutcost())).subtract(new BigDecimal(costlist.getDepreciationsoft())).subtract(new BigDecimal(costlist.getDepreciationequipment()));
                                        planmonth11 = planmonth11.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("12")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal()).subtract(new BigDecimal(costlist.getInwetuo())).subtract(new BigDecimal(costlist.getRent())).subtract(new BigDecimal(costlist.getCosttotal())).subtract(new BigDecimal(costlist.getPeocost())).subtract(new BigDecimal(costlist.getOutcost())).subtract(new BigDecimal(costlist.getDepreciationsoft())).subtract(new BigDecimal(costlist.getDepreciationequipment()));
                                        planmonth12 = planmonth12.add(intotal);
                                    }
                                }
                                incomeexpenditurevo.setPlanamount1(String.valueOf(planmonth1));
                                incomeexpenditurevo.setPlanamount2(String.valueOf(planmonth2));
                                incomeexpenditurevo.setPlanamount3(String.valueOf(planmonth3));
                                incomeexpenditurevo.setPlanamount4(String.valueOf(planmonth4));
                                incomeexpenditurevo.setPlanamount5(String.valueOf(planmonth5));
                                incomeexpenditurevo.setPlanamount6(String.valueOf(planmonth6));
                                incomeexpenditurevo.setPlanamount7(String.valueOf(planmonth7));
                                incomeexpenditurevo.setPlanamount8(String.valueOf(planmonth8));
                                incomeexpenditurevo.setPlanamount9(String.valueOf(planmonth9));
                                incomeexpenditurevo.setPlanamount10(String.valueOf(planmonth10));
                                incomeexpenditurevo.setPlanamount11(String.valueOf(planmonth11));
                                incomeexpenditurevo.setPlanamount12(String.valueOf(planmonth12));
                                BigDecimal month1 = new BigDecimal("0");
                                BigDecimal month2 = new BigDecimal("0");
                                BigDecimal month3 = new BigDecimal("0");
                                BigDecimal month4 = new BigDecimal("0");
                                BigDecimal month5 = new BigDecimal("0");
                                BigDecimal month6 = new BigDecimal("0");
                                BigDecimal month7 = new BigDecimal("0");
                                BigDecimal month8 = new BigDecimal("0");
                                BigDecimal month9 = new BigDecimal("0");
                                BigDecimal month10 = new BigDecimal("0");
                                BigDecimal month11 = new BigDecimal("0");
                                BigDecimal month12 = new BigDecimal("0");
                                incomeexpenditurevo.setThemename(iteA.getValue1());
                                JSONArray jsonbusiness = JSONArray.parseArray(businessplanlist.get(0).getBusiness());
                                JSONArray jsontableo = JSONArray.parseArray(businessplanlist.get(0).getTableO());
                                for (Object ob : jsontableo) {
                                    String type = getProperty(ob, "type");
                                    if (!type.equals("PJ111014") && !type.equals("PJ111002")) {
                                        BigDecimal months1 = new BigDecimal(getProperty(ob, "money1"));
                                        BigDecimal months2 = new BigDecimal(getProperty(ob, "money2"));
                                        BigDecimal months3 = new BigDecimal(getProperty(ob, "money3"));
                                        BigDecimal months4 = new BigDecimal(getProperty(ob, "money4"));
                                        BigDecimal months5 = new BigDecimal(getProperty(ob, "money5"));
                                        BigDecimal months6 = new BigDecimal(getProperty(ob, "money6"));
                                        BigDecimal months7 = new BigDecimal(getProperty(ob, "money7"));
                                        BigDecimal months8 = new BigDecimal(getProperty(ob, "money8"));
                                        BigDecimal months9 = new BigDecimal(getProperty(ob, "money9"));
                                        BigDecimal months10 = new BigDecimal(getProperty(ob, "money10"));
                                        BigDecimal months11 = new BigDecimal(getProperty(ob, "money11"));
                                        BigDecimal months12 = new BigDecimal(getProperty(ob, "money12"));
                                        month1 = months1.add(month1);
                                        month2 = months2.add(month2);
                                        month3 = months3.add(month3);
                                        month4 = months4.add(month4);
                                        month5 = months5.add(month5);
                                        month6 = months6.add(month6);
                                        month7 = months7.add(month7);
                                        month8 = months8.add(month8);
                                        month9 = months9.add(month9);
                                        month10 = months10.add(month10);
                                        month11 = months11.add(month11);
                                        month12 = months12.add(month12);
                                    }
                                }
                                for (Object ob : jsonbusiness) {
                                    BigDecimal months1 = new BigDecimal(getProperty(ob, "money1"));
                                    BigDecimal months2 = new BigDecimal(getProperty(ob, "money2"));
                                    BigDecimal months3 = new BigDecimal(getProperty(ob, "money3"));
                                    BigDecimal months4 = new BigDecimal(getProperty(ob, "money4"));
                                    BigDecimal months5 = new BigDecimal(getProperty(ob, "money5"));
                                    BigDecimal months6 = new BigDecimal(getProperty(ob, "money6"));
                                    BigDecimal months7 = new BigDecimal(getProperty(ob, "money7"));
                                    BigDecimal months8 = new BigDecimal(getProperty(ob, "money8"));
                                    BigDecimal months9 = new BigDecimal(getProperty(ob, "money9"));
                                    BigDecimal months10 = new BigDecimal(getProperty(ob, "money10"));
                                    BigDecimal months11 = new BigDecimal(getProperty(ob, "money11"));
                                    BigDecimal months12 = new BigDecimal(getProperty(ob, "money12"));
                                    month1 = months1.add(month1);
                                    month2 = months2.add(month2);
                                    month3 = months3.add(month3);
                                    month4 = months4.add(month4);
                                    month5 = months5.add(month5);
                                    month6 = months6.add(month6);
                                    month7 = months7.add(month7);
                                    month8 = months8.add(month8);
                                    month9 = months9.add(month9);
                                    month10 = months10.add(month10);
                                    month11 = months11.add(month11);
                                    month12 = months12.add(month12);
                                }
                                incomeexpenditurevo.setAmount1(String.valueOf(month1));
                                incomeexpenditurevo.setAmount2(String.valueOf(month2));
                                incomeexpenditurevo.setAmount3(String.valueOf(month3));
                                incomeexpenditurevo.setAmount4(String.valueOf(month4));
                                incomeexpenditurevo.setAmount5(String.valueOf(month5));
                                incomeexpenditurevo.setAmount6(String.valueOf(month6));
                                incomeexpenditurevo.setAmount7(String.valueOf(month7));
                                incomeexpenditurevo.setAmount8(String.valueOf(month8));
                                incomeexpenditurevo.setAmount9(String.valueOf(month9));
                                incomeexpenditurevo.setAmount10(String.valueOf(month10));
                                incomeexpenditurevo.setAmount11(String.valueOf(month11));
                                incomeexpenditurevo.setAmount12(String.valueOf(month12));
                            } else if (iteA.getCode().equals("PJ147010")) {
                                for (CostCarryForward costlist : costcarryforwardlist) {
                                    if (costlist.getRegion().substring(6, 7).equals("01")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOtherexpenses());
                                        planmonth1 = planmonth1.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("02")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOtherexpenses());
                                        planmonth2 = planmonth2.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("03")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOtherexpenses());
                                        planmonth3 = planmonth3.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("04")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOtherexpenses());
                                        planmonth4 = planmonth4.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("05")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOtherexpenses());
                                        planmonth5 = planmonth5.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("06")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOtherexpenses());
                                        planmonth6 = planmonth6.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("07")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOtherexpenses());
                                        planmonth7 = planmonth7.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("08")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOtherexpenses());
                                        planmonth8 = planmonth8.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("09")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOtherexpenses());
                                        planmonth9 = planmonth9.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("10")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOtherexpenses());
                                        planmonth10 = planmonth10.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("11")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOtherexpenses());
                                        planmonth11 = planmonth11.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("12")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOtherexpenses());
                                        planmonth12 = planmonth12.add(intotal);
                                    }
                                }
                                incomeexpenditurevo.setPlanamount1(String.valueOf(planmonth1));
                                incomeexpenditurevo.setPlanamount2(String.valueOf(planmonth2));
                                incomeexpenditurevo.setPlanamount3(String.valueOf(planmonth3));
                                incomeexpenditurevo.setPlanamount4(String.valueOf(planmonth4));
                                incomeexpenditurevo.setPlanamount5(String.valueOf(planmonth5));
                                incomeexpenditurevo.setPlanamount6(String.valueOf(planmonth6));
                                incomeexpenditurevo.setPlanamount7(String.valueOf(planmonth7));
                                incomeexpenditurevo.setPlanamount8(String.valueOf(planmonth8));
                                incomeexpenditurevo.setPlanamount9(String.valueOf(planmonth9));
                                incomeexpenditurevo.setPlanamount10(String.valueOf(planmonth10));
                                incomeexpenditurevo.setPlanamount11(String.valueOf(planmonth11));
                                incomeexpenditurevo.setPlanamount12(String.valueOf(planmonth12));
                                BigDecimal month1 = new BigDecimal("0");
                                BigDecimal month2 = new BigDecimal("0");
                                BigDecimal month3 = new BigDecimal("0");
                                BigDecimal month4 = new BigDecimal("0");
                                BigDecimal month5 = new BigDecimal("0");
                                BigDecimal month6 = new BigDecimal("0");
                                BigDecimal month7 = new BigDecimal("0");
                                BigDecimal month8 = new BigDecimal("0");
                                BigDecimal month9 = new BigDecimal("0");
                                BigDecimal month10 = new BigDecimal("0");
                                BigDecimal month11 = new BigDecimal("0");
                                BigDecimal month12 = new BigDecimal("0");
                                incomeexpenditurevo.setThemename(iteA.getValue1());
                                JSONArray jsontableo = JSONArray.parseArray(businessplanlist.get(0).getTableO());
                                for (Object ob : jsontableo) {
                                    String type = getProperty(ob, "type");
                                    if (type.equals("PJ111014")) {
                                        BigDecimal months1 = new BigDecimal(getProperty(ob, "money1"));
                                        BigDecimal months2 = new BigDecimal(getProperty(ob, "money2"));
                                        BigDecimal months3 = new BigDecimal(getProperty(ob, "money3"));
                                        BigDecimal months4 = new BigDecimal(getProperty(ob, "money4"));
                                        BigDecimal months5 = new BigDecimal(getProperty(ob, "money5"));
                                        BigDecimal months6 = new BigDecimal(getProperty(ob, "money6"));
                                        BigDecimal months7 = new BigDecimal(getProperty(ob, "money7"));
                                        BigDecimal months8 = new BigDecimal(getProperty(ob, "money8"));
                                        BigDecimal months9 = new BigDecimal(getProperty(ob, "money9"));
                                        BigDecimal months10 = new BigDecimal(getProperty(ob, "money10"));
                                        BigDecimal months11 = new BigDecimal(getProperty(ob, "money11"));
                                        BigDecimal months12 = new BigDecimal(getProperty(ob, "money12"));
                                        month1 = months1.add(month1);
                                        month2 = months2.add(month2);
                                        month3 = months3.add(month3);
                                        month4 = months4.add(month4);
                                        month5 = months5.add(month5);
                                        month6 = months6.add(month6);
                                        month7 = months7.add(month7);
                                        month8 = months8.add(month8);
                                        month9 = months9.add(month9);
                                        month10 = months10.add(month10);
                                        month11 = months11.add(month11);
                                        month12 = months12.add(month12);
                                    }
                                }
                                incomeexpenditurevo.setAmount1(String.valueOf(month1));
                                incomeexpenditurevo.setAmount2(String.valueOf(month2));
                                incomeexpenditurevo.setAmount3(String.valueOf(month3));
                                incomeexpenditurevo.setAmount4(String.valueOf(month4));
                                incomeexpenditurevo.setAmount5(String.valueOf(month5));
                                incomeexpenditurevo.setAmount6(String.valueOf(month6));
                                incomeexpenditurevo.setAmount7(String.valueOf(month7));
                                incomeexpenditurevo.setAmount8(String.valueOf(month8));
                                incomeexpenditurevo.setAmount9(String.valueOf(month9));
                                incomeexpenditurevo.setAmount10(String.valueOf(month10));
                                incomeexpenditurevo.setAmount11(String.valueOf(month11));
                                incomeexpenditurevo.setAmount12(String.valueOf(month12));
                            } else if (iteA.getCode().equals("PJ147011")) {
                                for (ProjectIncome listproject : projectincomelist) {
                                    if (listproject.getMonth().substring(6, 7).equals("01")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("1")) {
                                                planmonth1.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("02")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("1")) {
                                                planmonth2.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("03")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("1")) {
                                                planmonth3.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("04")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("1")) {
                                                planmonth4.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("05")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("1")) {
                                                planmonth5.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("06")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("1")) {
                                                planmonth6.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("07")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("1")) {
                                                planmonth7.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("08")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("1")) {
                                                planmonth8.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("09")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("1")) {
                                                planmonth9.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("10")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("1")) {
                                                planmonth10.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("11")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("1")) {
                                                planmonth11.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("12")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("1")) {
                                                planmonth12.add(new BigDecimal("1"));
                                            }
                                        }
                                    }
                                }
                                incomeexpenditurevo.setPlanamount1(String.valueOf(planmonth1));
                                incomeexpenditurevo.setPlanamount2(String.valueOf(planmonth2));
                                incomeexpenditurevo.setPlanamount3(String.valueOf(planmonth3));
                                incomeexpenditurevo.setPlanamount4(String.valueOf(planmonth4));
                                incomeexpenditurevo.setPlanamount5(String.valueOf(planmonth5));
                                incomeexpenditurevo.setPlanamount6(String.valueOf(planmonth6));
                                incomeexpenditurevo.setPlanamount7(String.valueOf(planmonth7));
                                incomeexpenditurevo.setPlanamount8(String.valueOf(planmonth8));
                                incomeexpenditurevo.setPlanamount9(String.valueOf(planmonth9));
                                incomeexpenditurevo.setPlanamount10(String.valueOf(planmonth10));
                                incomeexpenditurevo.setPlanamount11(String.valueOf(planmonth11));
                                incomeexpenditurevo.setPlanamount12(String.valueOf(planmonth12));
                                incomeexpenditurevo.setThemename(iteA.getValue1());
                                incomeexpenditurevo.setAmount1(String.valueOf(personnel1));
                                incomeexpenditurevo.setAmount2(String.valueOf(personnel2));
                                incomeexpenditurevo.setAmount3(String.valueOf(personnel3));
                                incomeexpenditurevo.setAmount4(String.valueOf(personnel4));
                                incomeexpenditurevo.setAmount5(String.valueOf(personnel5));
                                incomeexpenditurevo.setAmount6(String.valueOf(personnel6));
                                incomeexpenditurevo.setAmount7(String.valueOf(personnel7));
                                incomeexpenditurevo.setAmount8(String.valueOf(personnel8));
                                incomeexpenditurevo.setAmount9(String.valueOf(personnel9));
                                incomeexpenditurevo.setAmount10(String.valueOf(personnel10));
                                incomeexpenditurevo.setAmount11(String.valueOf(personnel11));
                                incomeexpenditurevo.setAmount12(String.valueOf(personnel12));
                            } else if (iteA.getCode().equals("PJ147012")) {
                                BigDecimal money1 = new BigDecimal("0");
                                BigDecimal money2 = new BigDecimal("0");
                                BigDecimal money3 = new BigDecimal("0");
                                BigDecimal money4 = new BigDecimal("0");
                                BigDecimal money5 = new BigDecimal("0");
                                BigDecimal money6 = new BigDecimal("0");
                                BigDecimal money7 = new BigDecimal("0");
                                BigDecimal money8 = new BigDecimal("0");
                                BigDecimal money9 = new BigDecimal("0");
                                BigDecimal money10 = new BigDecimal("0");
                                BigDecimal money11 = new BigDecimal("0");
                                BigDecimal money12 = new BigDecimal("0");
                                for (ProjectIncome listproject : projectincomelist) {
                                    if (listproject.getMonth().substring(6, 7).equals("01")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money1.add(new BigDecimal(money));
                                            if (type.equals("1")) {
                                                planmonth1.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("02")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money2.add(new BigDecimal(money));
                                            if (type.equals("1")) {
                                                planmonth2.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("03")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money3.add(new BigDecimal(money));
                                            if (type.equals("1")) {
                                                planmonth3.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("04")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money4.add(new BigDecimal(money));
                                            if (type.equals("1")) {
                                                planmonth4.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("05")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money5.add(new BigDecimal(money));
                                            if (type.equals("1")) {
                                                planmonth5.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("06")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money6.add(new BigDecimal(money));
                                            if (type.equals("1")) {
                                                planmonth6.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("07")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money7.add(new BigDecimal(money));
                                            if (type.equals("1")) {
                                                planmonth7.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("08")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money8.add(new BigDecimal(money));
                                            if (type.equals("1")) {
                                                planmonth8.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("09")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money9.add(new BigDecimal(money));
                                            if (type.equals("1")) {
                                                planmonth9.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("10")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money10.add(new BigDecimal(money));
                                            if (type.equals("1")) {
                                                planmonth10.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("11")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money11.add(new BigDecimal(money));
                                            if (type.equals("1")) {
                                                planmonth11.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("12")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money12.add(new BigDecimal(money));
                                            if (type.equals("1")) {
                                                planmonth12.add(new BigDecimal("1"));
                                            }
                                        }
                                    }
                                }
                                incomeexpenditurevo.setPlanamount1(String.valueOf(money1.divide(planmonth1, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount2(String.valueOf(money2.divide(planmonth2, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount3(String.valueOf(money3.divide(planmonth3, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount4(String.valueOf(money4.divide(planmonth4, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount5(String.valueOf(money5.divide(planmonth5, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount6(String.valueOf(money6.divide(planmonth6, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount7(String.valueOf(money7.divide(planmonth7, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount8(String.valueOf(money8.divide(planmonth8, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount9(String.valueOf(money9.divide(planmonth9, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount10(String.valueOf(money10.divide(planmonth10, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount11(String.valueOf(money11.divide(planmonth11, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount12(String.valueOf(money12.divide(planmonth12, scale, roundingMode)));
                                incomeexpenditurevo.setThemename(iteA.getValue1());
                                for (PersonnelPlan list : personnelplanlist) {
                                    if (list.getType().equals("0")) {
                                        BigDecimal moneyavg = new BigDecimal(list.getMoneyavg());
                                        incomeexpenditurevo.setAmount1(String.valueOf(moneyavg));
                                        incomeexpenditurevo.setAmount2(String.valueOf(moneyavg));
                                        incomeexpenditurevo.setAmount3(String.valueOf(moneyavg));
                                        incomeexpenditurevo.setAmount4(String.valueOf(moneyavg));
                                        incomeexpenditurevo.setAmount5(String.valueOf(moneyavg));
                                        incomeexpenditurevo.setAmount6(String.valueOf(moneyavg));
                                        incomeexpenditurevo.setAmount7(String.valueOf(moneyavg));
                                        incomeexpenditurevo.setAmount8(String.valueOf(moneyavg));
                                        incomeexpenditurevo.setAmount9(String.valueOf(moneyavg));
                                        incomeexpenditurevo.setAmount10(String.valueOf(moneyavg));
                                        incomeexpenditurevo.setAmount11(String.valueOf(moneyavg));
                                        incomeexpenditurevo.setAmount12(String.valueOf(moneyavg));
                                    }
                                }
                            } else if (iteA.getCode().equals("PJ147013")) {
                                for (ProjectIncome listproject : projectincomelist) {
                                    if (listproject.getMonth().substring(6, 7).equals("01")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("0")) {
                                                planmonth1.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("02")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("0")) {
                                                planmonth2.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("03")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("0")) {
                                                planmonth3.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("04")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("0")) {
                                                planmonth4.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("05")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("0")) {
                                                planmonth5.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("06")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("0")) {
                                                planmonth6.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("07")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("0")) {
                                                planmonth7.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("08")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("0")) {
                                                planmonth8.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("09")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("0")) {
                                                planmonth9.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("10")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("0")) {
                                                planmonth10.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("11")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("0")) {
                                                planmonth11.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("12")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("0")) {
                                                planmonth12.add(new BigDecimal("1"));
                                            }
                                        }
                                    }
                                }
                                incomeexpenditurevo.setPlanamount1(String.valueOf(planmonth1));
                                incomeexpenditurevo.setPlanamount2(String.valueOf(planmonth2));
                                incomeexpenditurevo.setPlanamount3(String.valueOf(planmonth3));
                                incomeexpenditurevo.setPlanamount4(String.valueOf(planmonth4));
                                incomeexpenditurevo.setPlanamount5(String.valueOf(planmonth5));
                                incomeexpenditurevo.setPlanamount6(String.valueOf(planmonth6));
                                incomeexpenditurevo.setPlanamount7(String.valueOf(planmonth7));
                                incomeexpenditurevo.setPlanamount8(String.valueOf(planmonth8));
                                incomeexpenditurevo.setPlanamount9(String.valueOf(planmonth9));
                                incomeexpenditurevo.setPlanamount10(String.valueOf(planmonth10));
                                incomeexpenditurevo.setPlanamount11(String.valueOf(planmonth11));
                                incomeexpenditurevo.setPlanamount12(String.valueOf(planmonth12));
                                incomeexpenditurevo.setThemename(iteA.getValue1());
                                incomeexpenditurevo.setAmount1(String.valueOf(wpersonnel1));
                                incomeexpenditurevo.setAmount2(String.valueOf(wpersonnel2));
                                incomeexpenditurevo.setAmount3(String.valueOf(wpersonnel3));
                                incomeexpenditurevo.setAmount4(String.valueOf(wpersonnel4));
                                incomeexpenditurevo.setAmount5(String.valueOf(wpersonnel5));
                                incomeexpenditurevo.setAmount6(String.valueOf(wpersonnel6));
                                incomeexpenditurevo.setAmount7(String.valueOf(wpersonnel7));
                                incomeexpenditurevo.setAmount8(String.valueOf(wpersonnel8));
                                incomeexpenditurevo.setAmount9(String.valueOf(wpersonnel9));
                                incomeexpenditurevo.setAmount10(String.valueOf(wpersonnel10));
                                incomeexpenditurevo.setAmount11(String.valueOf(wpersonnel11));
                                incomeexpenditurevo.setAmount12(String.valueOf(wpersonnel12));
                            } else if (iteA.getCode().equals("PJ147014")) {
                                BigDecimal money1 = new BigDecimal("0");
                                BigDecimal money2 = new BigDecimal("0");
                                BigDecimal money3 = new BigDecimal("0");
                                BigDecimal money4 = new BigDecimal("0");
                                BigDecimal money5 = new BigDecimal("0");
                                BigDecimal money6 = new BigDecimal("0");
                                BigDecimal money7 = new BigDecimal("0");
                                BigDecimal money8 = new BigDecimal("0");
                                BigDecimal money9 = new BigDecimal("0");
                                BigDecimal money10 = new BigDecimal("0");
                                BigDecimal money11 = new BigDecimal("0");
                                BigDecimal money12 = new BigDecimal("0");
                                for (ProjectIncome listproject : projectincomelist) {
                                    if (listproject.getMonth().substring(6, 7).equals("01")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money1.add(new BigDecimal(money));
                                            if (type.equals("0")) {
                                                planmonth1.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("02")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money2.add(new BigDecimal(money));
                                            if (type.equals("0")) {
                                                planmonth2.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("03")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money3.add(new BigDecimal(money));
                                            if (type.equals("0")) {
                                                planmonth3.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("04")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money4.add(new BigDecimal(money));
                                            if (type.equals("0")) {
                                                planmonth4.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("05")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money5.add(new BigDecimal(money));
                                            if (type.equals("0")) {
                                                planmonth5.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("06")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money6.add(new BigDecimal(money));
                                            if (type.equals("0")) {
                                                planmonth6.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("07")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money7.add(new BigDecimal(money));
                                            if (type.equals("0")) {
                                                planmonth7.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("08")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money8.add(new BigDecimal(money));
                                            if (type.equals("0")) {
                                                planmonth8.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("09")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money9.add(new BigDecimal(money));
                                            if (type.equals("0")) {
                                                planmonth9.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("10")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money10.add(new BigDecimal(money));
                                            if (type.equals("0")) {
                                                planmonth10.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("11")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money11.add(new BigDecimal(money));
                                            if (type.equals("0")) {
                                                planmonth11.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("12")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money12.add(new BigDecimal(money));
                                            if (type.equals("0")) {
                                                planmonth12.add(new BigDecimal("1"));
                                            }
                                        }
                                    }
                                }
                                incomeexpenditurevo.setPlanamount1(String.valueOf(money1.divide(planmonth1, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount2(String.valueOf(money2.divide(planmonth2, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount3(String.valueOf(money3.divide(planmonth3, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount4(String.valueOf(money4.divide(planmonth4, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount5(String.valueOf(money5.divide(planmonth5, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount6(String.valueOf(money6.divide(planmonth6, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount7(String.valueOf(money7.divide(planmonth7, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount8(String.valueOf(money8.divide(planmonth8, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount9(String.valueOf(money9.divide(planmonth9, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount10(String.valueOf(money10.divide(planmonth10, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount11(String.valueOf(money11.divide(planmonth11, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount12(String.valueOf(money12.divide(planmonth12, scale, roundingMode)));
                                incomeexpenditurevo.setThemename(iteA.getValue1());
                                for (PersonnelPlan list : personnelplanlist) {
                                    if (list.getType().equals("0")) {
                                        BigDecimal moneyavg = new BigDecimal(list.getMoneyavg());
                                        incomeexpenditurevo.setAmount1(String.valueOf(moneyavg));
                                        incomeexpenditurevo.setAmount2(String.valueOf(moneyavg));
                                        incomeexpenditurevo.setAmount3(String.valueOf(moneyavg));
                                        incomeexpenditurevo.setAmount4(String.valueOf(moneyavg));
                                        incomeexpenditurevo.setAmount5(String.valueOf(moneyavg));
                                        incomeexpenditurevo.setAmount6(String.valueOf(moneyavg));
                                        incomeexpenditurevo.setAmount7(String.valueOf(moneyavg));
                                        incomeexpenditurevo.setAmount8(String.valueOf(moneyavg));
                                        incomeexpenditurevo.setAmount9(String.valueOf(moneyavg));
                                        incomeexpenditurevo.setAmount10(String.valueOf(moneyavg));
                                        incomeexpenditurevo.setAmount11(String.valueOf(moneyavg));
                                        incomeexpenditurevo.setAmount12(String.valueOf(moneyavg));
                                    }
                                }
                            }
                            incomeexpenditurevolist.add(incomeexpenditurevo);
                        }
                        BigDecimal rmonth1 = new BigDecimal("0");
                        BigDecimal rmonth2 = new BigDecimal("0");
                        BigDecimal rmonth3 = new BigDecimal("0");
                        BigDecimal rmonth4 = new BigDecimal("0");
                        BigDecimal rmonth5 = new BigDecimal("0");
                        BigDecimal rmonth6 = new BigDecimal("0");
                        BigDecimal rmonth7 = new BigDecimal("0");
                        BigDecimal rmonth8 = new BigDecimal("0");
                        BigDecimal rmonth9 = new BigDecimal("0");
                        BigDecimal rmonth10 = new BigDecimal("0");
                        BigDecimal rmonth11 = new BigDecimal("0");
                        BigDecimal rmonth12 = new BigDecimal("0");
                        BigDecimal wmonth1 = new BigDecimal("0");
                        BigDecimal wmonth2 = new BigDecimal("0");
                        BigDecimal wmonth3 = new BigDecimal("0");
                        BigDecimal wmonth4 = new BigDecimal("0");
                        BigDecimal wmonth5 = new BigDecimal("0");
                        BigDecimal wmonth6 = new BigDecimal("0");
                        BigDecimal wmonth7 = new BigDecimal("0");
                        BigDecimal wmonth8 = new BigDecimal("0");
                        BigDecimal wmonth9 = new BigDecimal("0");
                        BigDecimal wmonth10 = new BigDecimal("0");
                        BigDecimal wmonth11 = new BigDecimal("0");
                        BigDecimal wmonth12 = new BigDecimal("0");
                        BigDecimal smonth1 = new BigDecimal("0");
                        BigDecimal smonth2 = new BigDecimal("0");
                        BigDecimal smonth3 = new BigDecimal("0");
                        BigDecimal smonth4 = new BigDecimal("0");
                        BigDecimal smonth5 = new BigDecimal("0");
                        BigDecimal smonth6 = new BigDecimal("0");
                        BigDecimal smonth7 = new BigDecimal("0");
                        BigDecimal smonth8 = new BigDecimal("0");
                        BigDecimal smonth9 = new BigDecimal("0");
                        BigDecimal smonth10 = new BigDecimal("0");
                        BigDecimal smonth11 = new BigDecimal("0");
                        BigDecimal smonth12 = new BigDecimal("0");
                        BigDecimal zmonth1 = new BigDecimal("0");
                        BigDecimal zmonth2 = new BigDecimal("0");
                        BigDecimal zmonth3 = new BigDecimal("0");
                        BigDecimal zmonth4 = new BigDecimal("0");
                        BigDecimal zmonth5 = new BigDecimal("0");
                        BigDecimal zmonth6 = new BigDecimal("0");
                        BigDecimal zmonth7 = new BigDecimal("0");
                        BigDecimal zmonth8 = new BigDecimal("0");
                        BigDecimal zmonth9 = new BigDecimal("0");
                        BigDecimal zmonth10 = new BigDecimal("0");
                        BigDecimal zmonth11 = new BigDecimal("0");
                        BigDecimal zmonth12 = new BigDecimal("0");

                        BigDecimal rmonth1s = new BigDecimal("0");
                        BigDecimal rmonth2s = new BigDecimal("0");
                        BigDecimal rmonth3s = new BigDecimal("0");
                        BigDecimal rmonth4s = new BigDecimal("0");
                        BigDecimal rmonth5s = new BigDecimal("0");
                        BigDecimal rmonth6s = new BigDecimal("0");
                        BigDecimal rmonth7s = new BigDecimal("0");
                        BigDecimal rmonth8s = new BigDecimal("0");
                        BigDecimal rmonth9s = new BigDecimal("0");
                        BigDecimal rmonth10s = new BigDecimal("0");
                        BigDecimal rmonth11s = new BigDecimal("0");
                        BigDecimal rmonth12s = new BigDecimal("0");
                        BigDecimal wmonth1s = new BigDecimal("0");
                        BigDecimal wmonth2s = new BigDecimal("0");
                        BigDecimal wmonth3s = new BigDecimal("0");
                        BigDecimal wmonth4s = new BigDecimal("0");
                        BigDecimal wmonth5s = new BigDecimal("0");
                        BigDecimal wmonth6s = new BigDecimal("0");
                        BigDecimal wmonth7s = new BigDecimal("0");
                        BigDecimal wmonth8s = new BigDecimal("0");
                        BigDecimal wmonth9s = new BigDecimal("0");
                        BigDecimal wmonth10s = new BigDecimal("0");
                        BigDecimal wmonth11s = new BigDecimal("0");
                        BigDecimal wmonth12s = new BigDecimal("0");
                        BigDecimal smonth1s = new BigDecimal("0");
                        BigDecimal smonth2s = new BigDecimal("0");
                        BigDecimal smonth3s = new BigDecimal("0");
                        BigDecimal smonth4s = new BigDecimal("0");
                        BigDecimal smonth5s = new BigDecimal("0");
                        BigDecimal smonth6s = new BigDecimal("0");
                        BigDecimal smonth7s = new BigDecimal("0");
                        BigDecimal smonth8s = new BigDecimal("0");
                        BigDecimal smonth9s = new BigDecimal("0");
                        BigDecimal smonth10s = new BigDecimal("0");
                        BigDecimal smonth11s = new BigDecimal("0");
                        BigDecimal smonth12s = new BigDecimal("0");
                        BigDecimal zmonth1s = new BigDecimal("0");
                        BigDecimal zmonth2s = new BigDecimal("0");
                        BigDecimal zmonth3s = new BigDecimal("0");
                        BigDecimal zmonth4s = new BigDecimal("0");
                        BigDecimal zmonth5s = new BigDecimal("0");
                        BigDecimal zmonth6s = new BigDecimal("0");
                        BigDecimal zmonth7s = new BigDecimal("0");
                        BigDecimal zmonth8s = new BigDecimal("0");
                        BigDecimal zmonth9s = new BigDecimal("0");
                        BigDecimal zmonth10s = new BigDecimal("0");
                        BigDecimal zmonth11s = new BigDecimal("0");
                        BigDecimal zmonth12s = new BigDecimal("0");
                        for (IncomeExpenditureVo list : incomeexpenditurevolist) {
                            if (list.getThemename().equals("收入")) {
                                smonth1 = new BigDecimal(list.getAmount1());
                                smonth2 = new BigDecimal(list.getAmount2());
                                smonth3 = new BigDecimal(list.getAmount3());
                                smonth4 = new BigDecimal(list.getAmount4());
                                smonth5 = new BigDecimal(list.getAmount5());
                                smonth6 = new BigDecimal(list.getAmount6());
                                smonth7 = new BigDecimal(list.getAmount7());
                                smonth8 = new BigDecimal(list.getAmount8());
                                smonth9 = new BigDecimal(list.getAmount9());
                                smonth10 = new BigDecimal(list.getAmount10());
                                smonth11 = new BigDecimal(list.getAmount11());
                                smonth12 = new BigDecimal(list.getAmount12());
                                smonth1s = new BigDecimal(list.getPlanamount1());
                                smonth2s = new BigDecimal(list.getPlanamount2());
                                smonth3s = new BigDecimal(list.getPlanamount3());
                                smonth4s = new BigDecimal(list.getPlanamount4());
                                smonth5s = new BigDecimal(list.getPlanamount5());
                                smonth6s = new BigDecimal(list.getPlanamount6());
                                smonth7s = new BigDecimal(list.getPlanamount7());
                                smonth8s = new BigDecimal(list.getPlanamount8());
                                smonth9s = new BigDecimal(list.getPlanamount9());
                                smonth10s = new BigDecimal(list.getPlanamount10());
                                smonth11s = new BigDecimal(list.getPlanamount11());
                                smonth12s = new BigDecimal(list.getPlanamount12());
                            } else if (list.getThemename().equals("人件费")) {
                                rmonth1 = new BigDecimal(list.getAmount1());
                                rmonth2 = new BigDecimal(list.getAmount2());
                                rmonth3 = new BigDecimal(list.getAmount3());
                                rmonth4 = new BigDecimal(list.getAmount4());
                                rmonth5 = new BigDecimal(list.getAmount5());
                                rmonth6 = new BigDecimal(list.getAmount6());
                                rmonth7 = new BigDecimal(list.getAmount7());
                                rmonth8 = new BigDecimal(list.getAmount8());
                                rmonth9 = new BigDecimal(list.getAmount9());
                                rmonth10 = new BigDecimal(list.getAmount10());
                                rmonth11 = new BigDecimal(list.getAmount11());
                                rmonth12 = new BigDecimal(list.getAmount12());
                                rmonth1s = new BigDecimal(list.getPlanamount1());
                                rmonth2s = new BigDecimal(list.getPlanamount2());
                                rmonth3s = new BigDecimal(list.getPlanamount3());
                                rmonth4s = new BigDecimal(list.getPlanamount4());
                                rmonth5s = new BigDecimal(list.getPlanamount5());
                                rmonth6s = new BigDecimal(list.getPlanamount6());
                                rmonth7s = new BigDecimal(list.getPlanamount7());
                                rmonth8s = new BigDecimal(list.getPlanamount8());
                                rmonth9s = new BigDecimal(list.getPlanamount9());
                                rmonth10s = new BigDecimal(list.getPlanamount10());
                                rmonth11s = new BigDecimal(list.getPlanamount11());
                                rmonth12s = new BigDecimal(list.getPlanamount12());
                            } else if (list.getThemename().equals("外注费")) {
                                wmonth1 = new BigDecimal(list.getAmount1());
                                wmonth2 = new BigDecimal(list.getAmount2());
                                wmonth3 = new BigDecimal(list.getAmount3());
                                wmonth4 = new BigDecimal(list.getAmount4());
                                wmonth5 = new BigDecimal(list.getAmount5());
                                wmonth6 = new BigDecimal(list.getAmount6());
                                wmonth7 = new BigDecimal(list.getAmount7());
                                wmonth8 = new BigDecimal(list.getAmount8());
                                wmonth9 = new BigDecimal(list.getAmount9());
                                wmonth10 = new BigDecimal(list.getAmount10());
                                wmonth11 = new BigDecimal(list.getAmount11());
                                wmonth12 = new BigDecimal(list.getAmount12());
                                wmonth1s = new BigDecimal(list.getPlanamount1());
                                wmonth2s = new BigDecimal(list.getPlanamount2());
                                wmonth3s = new BigDecimal(list.getPlanamount3());
                                wmonth4s = new BigDecimal(list.getPlanamount4());
                                wmonth5s = new BigDecimal(list.getPlanamount5());
                                wmonth6s = new BigDecimal(list.getPlanamount6());
                                wmonth7s = new BigDecimal(list.getPlanamount7());
                                wmonth8s = new BigDecimal(list.getPlanamount8());
                                wmonth9s = new BigDecimal(list.getPlanamount9());
                                wmonth10s = new BigDecimal(list.getPlanamount10());
                                wmonth11s = new BigDecimal(list.getPlanamount11());
                                wmonth12s = new BigDecimal(list.getPlanamount12());
                            } else if (list.getThemename().equals("支出")) {
                                zmonth1 = new BigDecimal(list.getAmount1());
                                zmonth2 = new BigDecimal(list.getAmount2());
                                zmonth3 = new BigDecimal(list.getAmount3());
                                zmonth4 = new BigDecimal(list.getAmount4());
                                zmonth5 = new BigDecimal(list.getAmount5());
                                zmonth6 = new BigDecimal(list.getAmount6());
                                zmonth7 = new BigDecimal(list.getAmount7());
                                zmonth8 = new BigDecimal(list.getAmount8());
                                zmonth9 = new BigDecimal(list.getAmount9());
                                zmonth10 = new BigDecimal(list.getAmount10());
                                zmonth11 = new BigDecimal(list.getAmount11());
                                zmonth12 = new BigDecimal(list.getAmount12());
                                zmonth1s = new BigDecimal(list.getPlanamount1());
                                zmonth2s = new BigDecimal(list.getPlanamount2());
                                zmonth3s = new BigDecimal(list.getPlanamount3());
                                zmonth4s = new BigDecimal(list.getPlanamount4());
                                zmonth5s = new BigDecimal(list.getPlanamount5());
                                zmonth6s = new BigDecimal(list.getPlanamount6());
                                zmonth7s = new BigDecimal(list.getPlanamount7());
                                zmonth8s = new BigDecimal(list.getPlanamount8());
                                zmonth9s = new BigDecimal(list.getPlanamount9());
                                zmonth10s = new BigDecimal(list.getPlanamount10());
                                zmonth11s = new BigDecimal(list.getPlanamount11());
                                zmonth12s = new BigDecimal(list.getPlanamount12());
                            }
                        }
                        for (Dictionary iteA : curListA) {
                            IncomeExpenditureVo incomeexpenditurevo = new IncomeExpenditureVo();
                            incomeexpenditurevo.setCenter_id("大连松下");
                            incomeexpenditurevo.setGroup_id(org1.getCompanyen());
                            if (iteA.getCode().equals("PJ1470015")) {
                                incomeexpenditurevo.setAmount1(String.valueOf(smonth1.subtract(rmonth1).subtract(wmonth1)));
                                incomeexpenditurevo.setAmount2(String.valueOf(smonth2.subtract(rmonth2).subtract(wmonth2)));
                                incomeexpenditurevo.setAmount3(String.valueOf(smonth3.subtract(rmonth3).subtract(wmonth3)));
                                incomeexpenditurevo.setAmount4(String.valueOf(smonth4.subtract(rmonth4).subtract(wmonth4)));
                                incomeexpenditurevo.setAmount5(String.valueOf(smonth5.subtract(rmonth5).subtract(wmonth5)));
                                incomeexpenditurevo.setAmount6(String.valueOf(smonth6.subtract(rmonth6).subtract(wmonth6)));
                                incomeexpenditurevo.setAmount7(String.valueOf(smonth7.subtract(rmonth7).subtract(wmonth7)));
                                incomeexpenditurevo.setAmount8(String.valueOf(smonth8.subtract(rmonth8).subtract(wmonth8)));
                                incomeexpenditurevo.setAmount9(String.valueOf(smonth9.subtract(rmonth9).subtract(wmonth9)));
                                incomeexpenditurevo.setAmount10(String.valueOf(smonth10.subtract(rmonth10).subtract(wmonth10)));
                                incomeexpenditurevo.setAmount11(String.valueOf(smonth11.subtract(rmonth11).subtract(wmonth11)));
                                incomeexpenditurevo.setAmount12(String.valueOf(smonth12.subtract(rmonth12).subtract(wmonth12)));

                                incomeexpenditurevo.setPlanamount1(String.valueOf(smonth1s.subtract(rmonth1s).subtract(wmonth1s)));
                                incomeexpenditurevo.setPlanamount2(String.valueOf(smonth2s.subtract(rmonth2s).subtract(wmonth2s)));
                                incomeexpenditurevo.setPlanamount3(String.valueOf(smonth3s.subtract(rmonth3s).subtract(wmonth3s)));
                                incomeexpenditurevo.setPlanamount4(String.valueOf(smonth4s.subtract(rmonth4s).subtract(wmonth4s)));
                                incomeexpenditurevo.setPlanamount5(String.valueOf(smonth5s.subtract(rmonth5s).subtract(wmonth5s)));
                                incomeexpenditurevo.setPlanamount6(String.valueOf(smonth6s.subtract(rmonth6s).subtract(wmonth6s)));
                                incomeexpenditurevo.setPlanamount7(String.valueOf(smonth7s.subtract(rmonth7s).subtract(wmonth7s)));
                                incomeexpenditurevo.setPlanamount8(String.valueOf(smonth8s.subtract(rmonth8s).subtract(wmonth8s)));
                                incomeexpenditurevo.setPlanamount9(String.valueOf(smonth9s.subtract(rmonth9s).subtract(wmonth9s)));
                                incomeexpenditurevo.setPlanamount10(String.valueOf(smonth10s.subtract(rmonth10s).subtract(wmonth10s)));
                                incomeexpenditurevo.setPlanamount11(String.valueOf(smonth11s.subtract(rmonth11s).subtract(wmonth11s)));
                                incomeexpenditurevo.setPlanamount12(String.valueOf(smonth12s.subtract(rmonth12s).subtract(wmonth12s)));
                                incomeexpenditurevo.setThemename(iteA.getValue1());
                            } else if (iteA.getCode().equals("PJ1470016")) {
                                incomeexpenditurevo.setAmount1(String.valueOf(smonth1.subtract(rmonth1).subtract(wmonth1).divide(smonth1, scale, roundingMode)));
                                incomeexpenditurevo.setAmount2(String.valueOf(smonth2.subtract(rmonth2).subtract(wmonth2).divide(smonth2, scale, roundingMode)));
                                incomeexpenditurevo.setAmount3(String.valueOf(smonth3.subtract(rmonth3).subtract(wmonth3).divide(smonth3, scale, roundingMode)));
                                incomeexpenditurevo.setAmount4(String.valueOf(smonth4.subtract(rmonth4).subtract(wmonth4).divide(smonth4, scale, roundingMode)));
                                incomeexpenditurevo.setAmount5(String.valueOf(smonth5.subtract(rmonth5).subtract(wmonth5).divide(smonth5, scale, roundingMode)));
                                incomeexpenditurevo.setAmount6(String.valueOf(smonth6.subtract(rmonth6).subtract(wmonth6).divide(smonth6, scale, roundingMode)));
                                incomeexpenditurevo.setAmount7(String.valueOf(smonth7.subtract(rmonth7).subtract(wmonth7).divide(smonth7, scale, roundingMode)));
                                incomeexpenditurevo.setAmount8(String.valueOf(smonth8.subtract(rmonth8).subtract(wmonth8).divide(smonth8, scale, roundingMode)));
                                incomeexpenditurevo.setAmount9(String.valueOf(smonth9.subtract(rmonth9).subtract(wmonth9).divide(smonth9, scale, roundingMode)));
                                incomeexpenditurevo.setAmount10(String.valueOf(smonth10.subtract(rmonth10).subtract(wmonth10).divide(smonth10, scale, roundingMode)));
                                incomeexpenditurevo.setAmount11(String.valueOf(smonth11.subtract(rmonth11).subtract(wmonth11).divide(smonth11, scale, roundingMode)));
                                incomeexpenditurevo.setAmount12(String.valueOf(smonth12.subtract(rmonth12).subtract(wmonth12).divide(smonth12, scale, roundingMode)));

                                incomeexpenditurevo.setPlanamount1(String.valueOf(smonth1s.subtract(rmonth1s).subtract(wmonth1s).divide(smonth1s, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount2(String.valueOf(smonth2s.subtract(rmonth2s).subtract(wmonth2s).divide(smonth2s, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount3(String.valueOf(smonth3s.subtract(rmonth3s).subtract(wmonth3s).divide(smonth3s, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount4(String.valueOf(smonth4s.subtract(rmonth4s).subtract(wmonth4s).divide(smonth4s, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount5(String.valueOf(smonth5s.subtract(rmonth5s).subtract(wmonth5s).divide(smonth5s, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount6(String.valueOf(smonth6s.subtract(rmonth6s).subtract(wmonth6s).divide(smonth6s, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount7(String.valueOf(smonth7s.subtract(rmonth7s).subtract(wmonth7s).divide(smonth7s, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount8(String.valueOf(smonth8s.subtract(rmonth8s).subtract(wmonth8s).divide(smonth8s, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount9(String.valueOf(smonth9s.subtract(rmonth9s).subtract(wmonth9s).divide(smonth9s, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount10(String.valueOf(smonth10s.subtract(rmonth10s).subtract(wmonth10s).divide(smonth10s, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount11(String.valueOf(smonth11s.subtract(rmonth11s).subtract(wmonth11s).divide(smonth11s, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount12(String.valueOf(smonth12s.subtract(rmonth12s).subtract(wmonth12s).divide(smonth12s, scale, roundingMode)));
                                incomeexpenditurevo.setThemename(iteA.getValue1());
                            } else if (iteA.getCode().equals("PJ1470017")) {
                                incomeexpenditurevo.setAmount1(String.valueOf(smonth1.subtract(zmonth1)));
                                incomeexpenditurevo.setAmount2(String.valueOf(smonth2.subtract(zmonth2)));
                                incomeexpenditurevo.setAmount3(String.valueOf(smonth3.subtract(zmonth3)));
                                incomeexpenditurevo.setAmount4(String.valueOf(smonth4.subtract(zmonth4)));
                                incomeexpenditurevo.setAmount5(String.valueOf(smonth5.subtract(zmonth5)));
                                incomeexpenditurevo.setAmount6(String.valueOf(smonth6.subtract(zmonth6)));
                                incomeexpenditurevo.setAmount7(String.valueOf(smonth7.subtract(zmonth7)));
                                incomeexpenditurevo.setAmount8(String.valueOf(smonth8.subtract(zmonth8)));
                                incomeexpenditurevo.setAmount9(String.valueOf(smonth9.subtract(zmonth9)));
                                incomeexpenditurevo.setAmount10(String.valueOf(smonth10.subtract(zmonth10)));
                                incomeexpenditurevo.setAmount11(String.valueOf(smonth11.subtract(zmonth11)));
                                incomeexpenditurevo.setAmount12(String.valueOf(smonth12.subtract(zmonth12)));
                                incomeexpenditurevo.setPlanamount1(String.valueOf(smonth1s.subtract(zmonth1s)));
                                incomeexpenditurevo.setPlanamount2(String.valueOf(smonth2s.subtract(zmonth2s)));
                                incomeexpenditurevo.setPlanamount3(String.valueOf(smonth3s.subtract(zmonth3s)));
                                incomeexpenditurevo.setPlanamount4(String.valueOf(smonth4s.subtract(zmonth4s)));
                                incomeexpenditurevo.setPlanamount5(String.valueOf(smonth5s.subtract(zmonth5s)));
                                incomeexpenditurevo.setPlanamount6(String.valueOf(smonth6s.subtract(zmonth6s)));
                                incomeexpenditurevo.setPlanamount7(String.valueOf(smonth7s.subtract(zmonth7s)));
                                incomeexpenditurevo.setPlanamount8(String.valueOf(smonth8s.subtract(zmonth8s)));
                                incomeexpenditurevo.setPlanamount9(String.valueOf(smonth9s.subtract(zmonth9s)));
                                incomeexpenditurevo.setPlanamount10(String.valueOf(smonth10s.subtract(zmonth10s)));
                                incomeexpenditurevo.setPlanamount11(String.valueOf(smonth11s.subtract(zmonth11s)));
                                incomeexpenditurevo.setPlanamount12(String.valueOf(smonth12s.subtract(zmonth12s)));
                                incomeexpenditurevo.setThemename(iteA.getValue1());
                            } else if (iteA.getCode().equals("PJ1470018")) {
                                incomeexpenditurevo.setAmount1(String.valueOf(smonth1.subtract(zmonth1).divide(smonth1, scale, roundingMode)));
                                incomeexpenditurevo.setAmount2(String.valueOf(smonth2.subtract(zmonth2).divide(smonth2, scale, roundingMode)));
                                incomeexpenditurevo.setAmount3(String.valueOf(smonth3.subtract(zmonth3).divide(smonth3, scale, roundingMode)));
                                incomeexpenditurevo.setAmount4(String.valueOf(smonth4.subtract(zmonth4).divide(smonth4, scale, roundingMode)));
                                incomeexpenditurevo.setAmount5(String.valueOf(smonth5.subtract(zmonth5).divide(smonth5, scale, roundingMode)));
                                incomeexpenditurevo.setAmount6(String.valueOf(smonth6.subtract(zmonth6).divide(smonth6, scale, roundingMode)));
                                incomeexpenditurevo.setAmount7(String.valueOf(smonth7.subtract(zmonth7).divide(smonth7, scale, roundingMode)));
                                incomeexpenditurevo.setAmount8(String.valueOf(smonth8.subtract(zmonth8).divide(smonth8, scale, roundingMode)));
                                incomeexpenditurevo.setAmount9(String.valueOf(smonth9.subtract(zmonth9).divide(smonth9, scale, roundingMode)));
                                incomeexpenditurevo.setAmount10(String.valueOf(smonth10.subtract(zmonth10).divide(smonth10, scale, roundingMode)));
                                incomeexpenditurevo.setAmount11(String.valueOf(smonth11.subtract(zmonth11).divide(smonth11, scale, roundingMode)));
                                incomeexpenditurevo.setAmount12(String.valueOf(smonth12.subtract(zmonth12).divide(smonth12, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount1(String.valueOf(smonth1s.subtract(zmonth1s).divide(smonth1s, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount2(String.valueOf(smonth2s.subtract(zmonth2s).divide(smonth2s, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount3(String.valueOf(smonth3s.subtract(zmonth3s).divide(smonth3s, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount4(String.valueOf(smonth4s.subtract(zmonth4s).divide(smonth4s, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount5(String.valueOf(smonth5s.subtract(zmonth5s).divide(smonth5s, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount6(String.valueOf(smonth6s.subtract(zmonth6s).divide(smonth6s, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount7(String.valueOf(smonth7s.subtract(zmonth7s).divide(smonth7s, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount8(String.valueOf(smonth8s.subtract(zmonth8s).divide(smonth8s, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount9(String.valueOf(smonth9s.subtract(zmonth9s).divide(smonth9s, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount10(String.valueOf(smonth10s.subtract(zmonth10s).divide(smonth10s, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount11(String.valueOf(smonth11s.subtract(zmonth11s).divide(smonth11s, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount12(String.valueOf(smonth12s.subtract(zmonth12s).divide(smonth12s, scale, roundingMode)));
                                incomeexpenditurevo.setThemename(iteA.getValue1());
                            }
                            incomeexpenditurevolist.add(incomeexpenditurevo);
                        }
                    }
                }
            }
        }
        return incomeexpenditurevolist;
    }

    public List<IncomeExpenditureVo> getrodiodetailtwo(String radiox, String radioy) throws Exception {
        List<IncomeExpenditureVo> incomeexpenditurevolist = new ArrayList<>();
        SimpleDateFormat s = new SimpleDateFormat("MM");
        SimpleDateFormat s1 = new SimpleDateFormat("YYYY");
        int scale = 2;//设置位数
        int roundingMode = 4;//表示四舍五入，可以选择其他舍值方式，例如去尾，等等.
        int year = Integer.valueOf(s.format(new Date())) >= 4 ? Integer.valueOf(s1.format(new Date())) + 1 : Integer.valueOf(s1.format(new Date()));
        if (radioy.equals("5")) {
            OrgTree orgs = orgTreeService.get(new OrgTree());
            for (OrgTree org : orgs.getOrgs()) {
                for (OrgTree org1 : org.getOrgs()) {
                    if (radiox.equals("2")) {
                        Businessplan businessplan = new Businessplan();
                        businessplan.setYear(String.valueOf(year));
                        businessplan.setGroup_id(org1.get_id());
                        List<Businessplan> businessplanlist = businessplanMapper.select(businessplan);
                        CostCarryForward costcarryforward = new CostCarryForward();
                        costcarryforward.setYear(String.valueOf(year));
                        costcarryforward.setGroup_id(org1.get_id());
                        List<CostCarryForward> costcarryforwardlist = costcarryforwardmapper.select(costcarryforward);

                        ProjectIncome projectincome = new ProjectIncome();
                        projectincome.setYear(String.valueOf(year));
                        projectincome.setGroup_id(org1.get_id());
                        List<ProjectIncome> projectincomelist = projectincomemapper.select(projectincome);

                        PersonnelPlan personnelplan = new PersonnelPlan();
                        personnelplan.setYears(String.valueOf(year));
                        personnelplan.setCenterid(org1.get_id());
                        List<PersonnelPlan> personnelplanlist = personnelplanmapper.select(personnelplan);
                        BigDecimal personnel1 = new BigDecimal("0");
                        BigDecimal personnel2 = new BigDecimal("0");
                        BigDecimal personnel3 = new BigDecimal("0");
                        BigDecimal personnel4 = new BigDecimal("0");
                        BigDecimal personnel5 = new BigDecimal("0");
                        BigDecimal personnel6 = new BigDecimal("0");
                        BigDecimal personnel7 = new BigDecimal("0");
                        BigDecimal personnel8 = new BigDecimal("0");
                        BigDecimal personnel9 = new BigDecimal("0");
                        BigDecimal personnel10 = new BigDecimal("0");
                        BigDecimal personnel11 = new BigDecimal("0");
                        BigDecimal personnel12 = new BigDecimal("0");
                        BigDecimal wpersonnel1 = new BigDecimal("0");
                        BigDecimal wpersonnel2 = new BigDecimal("0");
                        BigDecimal wpersonnel3 = new BigDecimal("0");
                        BigDecimal wpersonnel4 = new BigDecimal("0");
                        BigDecimal wpersonnel5 = new BigDecimal("0");
                        BigDecimal wpersonnel6 = new BigDecimal("0");
                        BigDecimal wpersonnel7 = new BigDecimal("0");
                        BigDecimal wpersonnel8 = new BigDecimal("0");
                        BigDecimal wpersonnel9 = new BigDecimal("0");
                        BigDecimal wpersonnel10 = new BigDecimal("0");
                        BigDecimal wpersonnel11 = new BigDecimal("0");
                        BigDecimal wpersonnel12 = new BigDecimal("0");
                        for (PersonnelPlan list : personnelplanlist) {
                            if (list.getType().equals("0")) {
                                JSONArray jsonemployed = JSONArray.parseArray(list.getEmployed());
                                JSONArray jsonnewentry = JSONArray.parseArray(list.getNewentry());
                                personnel1 = new BigDecimal(jsonemployed.size());
                                personnel2 = new BigDecimal(jsonemployed.size());
                                personnel3 = new BigDecimal(jsonemployed.size());
                                personnel4 = new BigDecimal(jsonemployed.size());
                                personnel5 = new BigDecimal(jsonemployed.size());
                                personnel6 = new BigDecimal(jsonemployed.size());
                                personnel7 = new BigDecimal(jsonemployed.size());
                                personnel8 = new BigDecimal(jsonemployed.size());
                                personnel9 = new BigDecimal(jsonemployed.size());
                                personnel10 = new BigDecimal(jsonemployed.size());
                                personnel11 = new BigDecimal(jsonemployed.size());
                                personnel12 = new BigDecimal(jsonemployed.size());
                                for (Object ob : jsonnewentry) {
                                    String month = getProperty(ob, "entermouth");
                                    if (s.format(month).equals("01")) {
                                        personnel1.add(new BigDecimal("1"));
                                        personnel2.add(new BigDecimal("1"));
                                        personnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("02")) {
                                        personnel2.add(new BigDecimal("1"));
                                        personnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("03")) {
                                        personnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("04")) {
                                        personnel4.add(new BigDecimal("1"));
                                        personnel5.add(new BigDecimal("1"));
                                        personnel6.add(new BigDecimal("1"));
                                        personnel7.add(new BigDecimal("1"));
                                        personnel8.add(new BigDecimal("1"));
                                        personnel9.add(new BigDecimal("1"));
                                        personnel10.add(new BigDecimal("1"));
                                        personnel11.add(new BigDecimal("1"));
                                        personnel12.add(new BigDecimal("1"));
                                        personnel1.add(new BigDecimal("1"));
                                        personnel2.add(new BigDecimal("1"));
                                        personnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("05")) {
                                        personnel5.add(new BigDecimal("1"));
                                        personnel6.add(new BigDecimal("1"));
                                        personnel7.add(new BigDecimal("1"));
                                        personnel8.add(new BigDecimal("1"));
                                        personnel9.add(new BigDecimal("1"));
                                        personnel10.add(new BigDecimal("1"));
                                        personnel11.add(new BigDecimal("1"));
                                        personnel12.add(new BigDecimal("1"));
                                        personnel1.add(new BigDecimal("1"));
                                        personnel2.add(new BigDecimal("1"));
                                        personnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("06")) {
                                        personnel6.add(new BigDecimal("1"));
                                        personnel7.add(new BigDecimal("1"));
                                        personnel8.add(new BigDecimal("1"));
                                        personnel9.add(new BigDecimal("1"));
                                        personnel10.add(new BigDecimal("1"));
                                        personnel11.add(new BigDecimal("1"));
                                        personnel12.add(new BigDecimal("1"));
                                        personnel1.add(new BigDecimal("1"));
                                        personnel2.add(new BigDecimal("1"));
                                        personnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("07")) {
                                        personnel7.add(new BigDecimal("1"));
                                        personnel8.add(new BigDecimal("1"));
                                        personnel9.add(new BigDecimal("1"));
                                        personnel10.add(new BigDecimal("1"));
                                        personnel11.add(new BigDecimal("1"));
                                        personnel12.add(new BigDecimal("1"));
                                        personnel1.add(new BigDecimal("1"));
                                        personnel2.add(new BigDecimal("1"));
                                        personnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("08")) {
                                        personnel8.add(new BigDecimal("1"));
                                        personnel9.add(new BigDecimal("1"));
                                        personnel10.add(new BigDecimal("1"));
                                        personnel11.add(new BigDecimal("1"));
                                        personnel12.add(new BigDecimal("1"));
                                        personnel1.add(new BigDecimal("1"));
                                        personnel2.add(new BigDecimal("1"));
                                        personnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("09")) {
                                        personnel9.add(new BigDecimal("1"));
                                        personnel10.add(new BigDecimal("1"));
                                        personnel11.add(new BigDecimal("1"));
                                        personnel12.add(new BigDecimal("1"));
                                        personnel1.add(new BigDecimal("1"));
                                        personnel2.add(new BigDecimal("1"));
                                        personnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("10")) {
                                        personnel10.add(new BigDecimal("1"));
                                        personnel11.add(new BigDecimal("1"));
                                        personnel12.add(new BigDecimal("1"));
                                        personnel1.add(new BigDecimal("1"));
                                        personnel2.add(new BigDecimal("1"));
                                        personnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("11")) {
                                        personnel11.add(new BigDecimal("1"));
                                        personnel12.add(new BigDecimal("1"));
                                        personnel1.add(new BigDecimal("1"));
                                        personnel2.add(new BigDecimal("1"));
                                        personnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("12")) {
                                        personnel12.add(new BigDecimal("1"));
                                        personnel1.add(new BigDecimal("1"));
                                        personnel2.add(new BigDecimal("1"));
                                        personnel3.add(new BigDecimal("1"));
                                    }
                                }
                            } else if (list.getType().equals("1")) {
                                JSONArray jsonemployed = JSONArray.parseArray(list.getEmployed());
                                JSONArray jsonnewentry = JSONArray.parseArray(list.getNewentry());
                                wpersonnel1 = new BigDecimal(jsonemployed.size());
                                wpersonnel2 = new BigDecimal(jsonemployed.size());
                                wpersonnel3 = new BigDecimal(jsonemployed.size());
                                wpersonnel4 = new BigDecimal(jsonemployed.size());
                                wpersonnel5 = new BigDecimal(jsonemployed.size());
                                wpersonnel6 = new BigDecimal(jsonemployed.size());
                                wpersonnel7 = new BigDecimal(jsonemployed.size());
                                wpersonnel8 = new BigDecimal(jsonemployed.size());
                                wpersonnel9 = new BigDecimal(jsonemployed.size());
                                wpersonnel10 = new BigDecimal(jsonemployed.size());
                                wpersonnel11 = new BigDecimal(jsonemployed.size());
                                wpersonnel12 = new BigDecimal(jsonemployed.size());
                                for (Object ob : jsonnewentry) {
                                    String month = getProperty(ob, "entermouth");
                                    if (s.format(month).equals("01")) {
                                        wpersonnel1.add(new BigDecimal("1"));
                                        wpersonnel2.add(new BigDecimal("1"));
                                        wpersonnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("02")) {
                                        wpersonnel2.add(new BigDecimal("1"));
                                        wpersonnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("03")) {
                                        wpersonnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("04")) {
                                        wpersonnel4.add(new BigDecimal("1"));
                                        wpersonnel5.add(new BigDecimal("1"));
                                        wpersonnel6.add(new BigDecimal("1"));
                                        wpersonnel7.add(new BigDecimal("1"));
                                        wpersonnel8.add(new BigDecimal("1"));
                                        wpersonnel9.add(new BigDecimal("1"));
                                        wpersonnel10.add(new BigDecimal("1"));
                                        wpersonnel11.add(new BigDecimal("1"));
                                        wpersonnel12.add(new BigDecimal("1"));
                                        wpersonnel1.add(new BigDecimal("1"));
                                        wpersonnel2.add(new BigDecimal("1"));
                                        wpersonnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("05")) {
                                        wpersonnel5.add(new BigDecimal("1"));
                                        wpersonnel6.add(new BigDecimal("1"));
                                        wpersonnel7.add(new BigDecimal("1"));
                                        wpersonnel8.add(new BigDecimal("1"));
                                        wpersonnel9.add(new BigDecimal("1"));
                                        wpersonnel10.add(new BigDecimal("1"));
                                        wpersonnel11.add(new BigDecimal("1"));
                                        wpersonnel12.add(new BigDecimal("1"));
                                        wpersonnel1.add(new BigDecimal("1"));
                                        wpersonnel2.add(new BigDecimal("1"));
                                        wpersonnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("06")) {
                                        wpersonnel6.add(new BigDecimal("1"));
                                        wpersonnel7.add(new BigDecimal("1"));
                                        wpersonnel8.add(new BigDecimal("1"));
                                        wpersonnel9.add(new BigDecimal("1"));
                                        wpersonnel10.add(new BigDecimal("1"));
                                        wpersonnel11.add(new BigDecimal("1"));
                                        wpersonnel12.add(new BigDecimal("1"));
                                        wpersonnel1.add(new BigDecimal("1"));
                                        wpersonnel2.add(new BigDecimal("1"));
                                        wpersonnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("07")) {
                                        wpersonnel7.add(new BigDecimal("1"));
                                        wpersonnel8.add(new BigDecimal("1"));
                                        wpersonnel9.add(new BigDecimal("1"));
                                        wpersonnel10.add(new BigDecimal("1"));
                                        wpersonnel11.add(new BigDecimal("1"));
                                        wpersonnel12.add(new BigDecimal("1"));
                                        wpersonnel1.add(new BigDecimal("1"));
                                        wpersonnel2.add(new BigDecimal("1"));
                                        wpersonnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("08")) {
                                        wpersonnel8.add(new BigDecimal("1"));
                                        wpersonnel9.add(new BigDecimal("1"));
                                        wpersonnel10.add(new BigDecimal("1"));
                                        wpersonnel11.add(new BigDecimal("1"));
                                        wpersonnel12.add(new BigDecimal("1"));
                                        wpersonnel1.add(new BigDecimal("1"));
                                        wpersonnel2.add(new BigDecimal("1"));
                                        wpersonnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("09")) {
                                        wpersonnel9.add(new BigDecimal("1"));
                                        wpersonnel10.add(new BigDecimal("1"));
                                        wpersonnel11.add(new BigDecimal("1"));
                                        wpersonnel12.add(new BigDecimal("1"));
                                        wpersonnel1.add(new BigDecimal("1"));
                                        wpersonnel2.add(new BigDecimal("1"));
                                        wpersonnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("10")) {
                                        wpersonnel10.add(new BigDecimal("1"));
                                        wpersonnel11.add(new BigDecimal("1"));
                                        wpersonnel12.add(new BigDecimal("1"));
                                        wpersonnel1.add(new BigDecimal("1"));
                                        wpersonnel2.add(new BigDecimal("1"));
                                        wpersonnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("11")) {
                                        wpersonnel11.add(new BigDecimal("1"));
                                        wpersonnel12.add(new BigDecimal("1"));
                                        wpersonnel1.add(new BigDecimal("1"));
                                        wpersonnel2.add(new BigDecimal("1"));
                                        wpersonnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("12")) {
                                        wpersonnel12.add(new BigDecimal("1"));
                                        wpersonnel1.add(new BigDecimal("1"));
                                        wpersonnel2.add(new BigDecimal("1"));
                                        wpersonnel3.add(new BigDecimal("1"));
                                    }
                                }
                            }
                        }
                        List<com.nt.dao_Org.Dictionary> curListA = dictionaryService.getForSelect("PJ147");
                        for (Dictionary iteA : curListA) {
                            IncomeExpenditureVo incomeexpenditurevo = new IncomeExpenditureVo();
                            incomeexpenditurevo.setCenter_id("大连松下");
                            incomeexpenditurevo.setGroup_id(org1.getCompanyen());
                            BigDecimal planmonth1 = new BigDecimal("0");
                            BigDecimal planmonth2 = new BigDecimal("0");
                            BigDecimal planmonth3 = new BigDecimal("0");
                            BigDecimal planmonth4 = new BigDecimal("0");
                            BigDecimal planmonth5 = new BigDecimal("0");
                            BigDecimal planmonth6 = new BigDecimal("0");
                            BigDecimal planmonth7 = new BigDecimal("0");
                            BigDecimal planmonth8 = new BigDecimal("0");
                            BigDecimal planmonth9 = new BigDecimal("0");
                            BigDecimal planmonth10 = new BigDecimal("0");
                            BigDecimal planmonth11 = new BigDecimal("0");
                            BigDecimal planmonth12 = new BigDecimal("0");
                            if (iteA.getCode().equals("PJ147001")) {
                                for (CostCarryForward costlist : costcarryforwardlist) {
                                    if (costlist.getRegion().substring(6, 7).equals("01")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal());
                                        planmonth1 = planmonth1.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("02")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal());
                                        planmonth2 = planmonth2.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("03")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal());
                                        planmonth3 = planmonth3.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("04")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal());
                                        planmonth4 = planmonth4.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("05")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal());
                                        planmonth5 = planmonth5.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("06")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal());
                                        planmonth6 = planmonth6.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("07")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal());
                                        planmonth7 = planmonth7.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("08")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal());
                                        planmonth8 = planmonth8.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("09")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal());
                                        planmonth9 = planmonth9.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("10")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal());
                                        planmonth10 = planmonth10.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("11")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal());
                                        planmonth11 = planmonth11.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("12")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal());
                                        planmonth12 = planmonth12.add(intotal);
                                    }
                                }
                                BigDecimal spanmount = planmonth4.add(planmonth5).add(planmonth6).add(planmonth7).add(planmonth8).add(planmonth9);
                                BigDecimal xpanmount = planmonth10.add(planmonth11).add(planmonth12).add(planmonth1).add(planmonth2).add(planmonth3);
                                BigDecimal qpanmount = spanmount.add(xpanmount);
                                incomeexpenditurevo.setPlanamount1(String.valueOf(spanmount));
                                incomeexpenditurevo.setPlanamount2(String.valueOf(xpanmount));
                                incomeexpenditurevo.setPlanamount3(String.valueOf(qpanmount));
                                BigDecimal month1 = new BigDecimal("0");
                                BigDecimal month2 = new BigDecimal("0");
                                BigDecimal month3 = new BigDecimal("0");
                                BigDecimal month4 = new BigDecimal("0");
                                BigDecimal month5 = new BigDecimal("0");
                                BigDecimal month6 = new BigDecimal("0");
                                BigDecimal month7 = new BigDecimal("0");
                                BigDecimal month8 = new BigDecimal("0");
                                BigDecimal month9 = new BigDecimal("0");
                                BigDecimal month10 = new BigDecimal("0");
                                BigDecimal month11 = new BigDecimal("0");
                                BigDecimal month12 = new BigDecimal("0");
                                incomeexpenditurevo.setThemename(iteA.getValue1());
                                JSONArray jsongroupa1 = JSONArray.parseArray(businessplanlist.get(0).getGroupA1());
                                JSONArray jsongroupa2 = JSONArray.parseArray(businessplanlist.get(0).getGroupA2());
                                for (Object ob : jsongroupa1) {
                                    BigDecimal months1 = new BigDecimal(getProperty(ob, "money1"));
                                    BigDecimal months2 = new BigDecimal(getProperty(ob, "money2"));
                                    BigDecimal months3 = new BigDecimal(getProperty(ob, "money3"));
                                    BigDecimal months4 = new BigDecimal(getProperty(ob, "money4"));
                                    BigDecimal months5 = new BigDecimal(getProperty(ob, "money5"));
                                    BigDecimal months6 = new BigDecimal(getProperty(ob, "money6"));
                                    BigDecimal months7 = new BigDecimal(getProperty(ob, "money7"));
                                    BigDecimal months8 = new BigDecimal(getProperty(ob, "money8"));
                                    BigDecimal months9 = new BigDecimal(getProperty(ob, "money9"));
                                    BigDecimal months10 = new BigDecimal(getProperty(ob, "money10"));
                                    BigDecimal months11 = new BigDecimal(getProperty(ob, "money11"));
                                    BigDecimal months12 = new BigDecimal(getProperty(ob, "money12"));
                                    month1 = months1.add(month1);
                                    month2 = months2.add(month2);
                                    month3 = months3.add(month3);
                                    month4 = months4.add(month4);
                                    month5 = months5.add(month5);
                                    month6 = months6.add(month6);
                                    month7 = months7.add(month7);
                                    month8 = months8.add(month8);
                                    month9 = months9.add(month9);
                                    month10 = months10.add(month10);
                                    month11 = months11.add(month11);
                                    month12 = months12.add(month12);
                                }
                                for (Object ob : jsongroupa2) {
                                    BigDecimal months1 = new BigDecimal(getProperty(ob, "money1"));
                                    BigDecimal months2 = new BigDecimal(getProperty(ob, "money2"));
                                    BigDecimal months3 = new BigDecimal(getProperty(ob, "money3"));
                                    BigDecimal months4 = new BigDecimal(getProperty(ob, "money4"));
                                    BigDecimal months5 = new BigDecimal(getProperty(ob, "money5"));
                                    BigDecimal months6 = new BigDecimal(getProperty(ob, "money6"));
                                    BigDecimal months7 = new BigDecimal(getProperty(ob, "money7"));
                                    BigDecimal months8 = new BigDecimal(getProperty(ob, "money8"));
                                    BigDecimal months9 = new BigDecimal(getProperty(ob, "money9"));
                                    BigDecimal months10 = new BigDecimal(getProperty(ob, "money10"));
                                    BigDecimal months11 = new BigDecimal(getProperty(ob, "money11"));
                                    BigDecimal months12 = new BigDecimal(getProperty(ob, "money12"));
                                    month1 = months1.add(month1);
                                    month2 = months2.add(month2);
                                    month3 = months3.add(month3);
                                    month4 = months4.add(month4);
                                    month5 = months5.add(month5);
                                    month6 = months6.add(month6);
                                    month7 = months7.add(month7);
                                    month8 = months8.add(month8);
                                    month9 = months9.add(month9);
                                    month10 = months10.add(month10);
                                    month11 = months11.add(month11);
                                    month12 = months12.add(month12);
                                }
                                BigDecimal smount = month4.add(month5).add(month6).add(month7).add(month8).add(month9);
                                BigDecimal xmount = month10.add(month11).add(month12).add(month1).add(month2).add(month3);
                                BigDecimal qmount = smount.add(xmount);
                                incomeexpenditurevo.setAmount1(String.valueOf(smount));
                                incomeexpenditurevo.setAmount2(String.valueOf(xmount));
                                incomeexpenditurevo.setAmount3(String.valueOf(qmount));
                            } else if (iteA.getCode().equals("PJ147002")) {
                                for (CostCarryForward costlist : costcarryforwardlist) {
                                    if (costlist.getRegion().substring(6, 7).equals("01")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getCosttotal());
                                        planmonth1 = planmonth1.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("02")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getCosttotal());
                                        planmonth2 = planmonth2.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("03")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getCosttotal());
                                        planmonth3 = planmonth3.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("04")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getCosttotal());
                                        planmonth4 = planmonth4.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("05")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getCosttotal());
                                        planmonth5 = planmonth5.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("06")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getCosttotal());
                                        planmonth6 = planmonth6.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("07")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getCosttotal());
                                        planmonth7 = planmonth7.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("08")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getCosttotal());
                                        planmonth8 = planmonth8.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("09")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getCosttotal());
                                        planmonth9 = planmonth9.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("10")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getCosttotal());
                                        planmonth10 = planmonth10.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("11")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getCosttotal());
                                        planmonth11 = planmonth11.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("12")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getCosttotal());
                                        planmonth12 = planmonth12.add(intotal);
                                    }
                                }
                                BigDecimal spanmount = planmonth4.add(planmonth5).add(planmonth6).add(planmonth7).add(planmonth8).add(planmonth9);
                                BigDecimal xpanmount = planmonth10.add(planmonth11).add(planmonth12).add(planmonth1).add(planmonth2).add(planmonth3);
                                BigDecimal qpanmount = spanmount.add(xpanmount);
                                incomeexpenditurevo.setPlanamount1(String.valueOf(spanmount));
                                incomeexpenditurevo.setPlanamount2(String.valueOf(xpanmount));
                                incomeexpenditurevo.setPlanamount3(String.valueOf(qpanmount));
                                BigDecimal month1 = new BigDecimal("0");
                                BigDecimal month2 = new BigDecimal("0");
                                BigDecimal month3 = new BigDecimal("0");
                                BigDecimal month4 = new BigDecimal("0");
                                BigDecimal month5 = new BigDecimal("0");
                                BigDecimal month6 = new BigDecimal("0");
                                BigDecimal month7 = new BigDecimal("0");
                                BigDecimal month8 = new BigDecimal("0");
                                BigDecimal month9 = new BigDecimal("0");
                                BigDecimal month10 = new BigDecimal("0");
                                BigDecimal month11 = new BigDecimal("0");
                                BigDecimal month12 = new BigDecimal("0");
                                incomeexpenditurevo.setThemename(iteA.getValue1());
                                JSONArray jsongroupb1 = JSONArray.parseArray(businessplanlist.get(0).getGroupB1());
                                JSONArray jsongroupb2 = JSONArray.parseArray(businessplanlist.get(0).getGroupB2());
                                JSONArray jsongroupb3 = JSONArray.parseArray(businessplanlist.get(0).getGroupB3());
                                JSONArray jsongroupc = JSONArray.parseArray(businessplanlist.get(0).getGroupC());
                                for (Object ob : jsongroupb1) {
                                    BigDecimal months1 = new BigDecimal(getProperty(ob, "money1"));
                                    BigDecimal months2 = new BigDecimal(getProperty(ob, "money2"));
                                    BigDecimal months3 = new BigDecimal(getProperty(ob, "money3"));
                                    BigDecimal months4 = new BigDecimal(getProperty(ob, "money4"));
                                    BigDecimal months5 = new BigDecimal(getProperty(ob, "money5"));
                                    BigDecimal months6 = new BigDecimal(getProperty(ob, "money6"));
                                    BigDecimal months7 = new BigDecimal(getProperty(ob, "money7"));
                                    BigDecimal months8 = new BigDecimal(getProperty(ob, "money8"));
                                    BigDecimal months9 = new BigDecimal(getProperty(ob, "money9"));
                                    BigDecimal months10 = new BigDecimal(getProperty(ob, "money10"));
                                    BigDecimal months11 = new BigDecimal(getProperty(ob, "money11"));
                                    BigDecimal months12 = new BigDecimal(getProperty(ob, "money12"));
                                    month1 = months1.add(month1);
                                    month2 = months2.add(month2);
                                    month3 = months3.add(month3);
                                    month4 = months4.add(month4);
                                    month5 = months5.add(month5);
                                    month6 = months6.add(month6);
                                    month7 = months7.add(month7);
                                    month8 = months8.add(month8);
                                    month9 = months9.add(month9);
                                    month10 = months10.add(month10);
                                    month11 = months11.add(month11);
                                    month12 = months12.add(month12);
                                }
                                for (Object ob : jsongroupb2) {
                                    BigDecimal months1 = new BigDecimal(getProperty(ob, "money1"));
                                    BigDecimal months2 = new BigDecimal(getProperty(ob, "money2"));
                                    BigDecimal months3 = new BigDecimal(getProperty(ob, "money3"));
                                    BigDecimal months4 = new BigDecimal(getProperty(ob, "money4"));
                                    BigDecimal months5 = new BigDecimal(getProperty(ob, "money5"));
                                    BigDecimal months6 = new BigDecimal(getProperty(ob, "money6"));
                                    BigDecimal months7 = new BigDecimal(getProperty(ob, "money7"));
                                    BigDecimal months8 = new BigDecimal(getProperty(ob, "money8"));
                                    BigDecimal months9 = new BigDecimal(getProperty(ob, "money9"));
                                    BigDecimal months10 = new BigDecimal(getProperty(ob, "money10"));
                                    BigDecimal months11 = new BigDecimal(getProperty(ob, "money11"));
                                    BigDecimal months12 = new BigDecimal(getProperty(ob, "money12"));
                                    month1 = months1.add(month1);
                                    month2 = months2.add(month2);
                                    month3 = months3.add(month3);
                                    month4 = months4.add(month4);
                                    month5 = months5.add(month5);
                                    month6 = months6.add(month6);
                                    month7 = months7.add(month7);
                                    month8 = months8.add(month8);
                                    month9 = months9.add(month9);
                                    month10 = months10.add(month10);
                                    month11 = months11.add(month11);
                                    month12 = months12.add(month12);
                                }
                                for (Object ob : jsongroupb3) {
                                    BigDecimal months1 = new BigDecimal(getProperty(ob, "money1"));
                                    BigDecimal months2 = new BigDecimal(getProperty(ob, "money2"));
                                    BigDecimal months3 = new BigDecimal(getProperty(ob, "money3"));
                                    BigDecimal months4 = new BigDecimal(getProperty(ob, "money4"));
                                    BigDecimal months5 = new BigDecimal(getProperty(ob, "money5"));
                                    BigDecimal months6 = new BigDecimal(getProperty(ob, "money6"));
                                    BigDecimal months7 = new BigDecimal(getProperty(ob, "money7"));
                                    BigDecimal months8 = new BigDecimal(getProperty(ob, "money8"));
                                    BigDecimal months9 = new BigDecimal(getProperty(ob, "money9"));
                                    BigDecimal months10 = new BigDecimal(getProperty(ob, "money10"));
                                    BigDecimal months11 = new BigDecimal(getProperty(ob, "money11"));
                                    BigDecimal months12 = new BigDecimal(getProperty(ob, "money12"));
                                    month1 = months1.add(month1);
                                    month2 = months2.add(month2);
                                    month3 = months3.add(month3);
                                    month4 = months4.add(month4);
                                    month5 = months5.add(month5);
                                    month6 = months6.add(month6);
                                    month7 = months7.add(month7);
                                    month8 = months8.add(month8);
                                    month9 = months9.add(month9);
                                    month10 = months10.add(month10);
                                    month11 = months11.add(month11);
                                    month12 = months12.add(month12);
                                }
                                for (Object ob : jsongroupc) {
                                    BigDecimal months1 = new BigDecimal(getProperty(ob, "money1"));
                                    BigDecimal months2 = new BigDecimal(getProperty(ob, "money2"));
                                    BigDecimal months3 = new BigDecimal(getProperty(ob, "money3"));
                                    BigDecimal months4 = new BigDecimal(getProperty(ob, "money4"));
                                    BigDecimal months5 = new BigDecimal(getProperty(ob, "money5"));
                                    BigDecimal months6 = new BigDecimal(getProperty(ob, "money6"));
                                    BigDecimal months7 = new BigDecimal(getProperty(ob, "money7"));
                                    BigDecimal months8 = new BigDecimal(getProperty(ob, "money8"));
                                    BigDecimal months9 = new BigDecimal(getProperty(ob, "money9"));
                                    BigDecimal months10 = new BigDecimal(getProperty(ob, "money10"));
                                    BigDecimal months11 = new BigDecimal(getProperty(ob, "money11"));
                                    BigDecimal months12 = new BigDecimal(getProperty(ob, "money12"));
                                    month1 = months1.add(month1);
                                    month2 = months2.add(month2);
                                    month3 = months3.add(month3);
                                    month4 = months4.add(month4);
                                    month5 = months5.add(month5);
                                    month6 = months6.add(month6);
                                    month7 = months7.add(month7);
                                    month8 = months8.add(month8);
                                    month9 = months9.add(month9);
                                    month10 = months10.add(month10);
                                    month11 = months11.add(month11);
                                    month12 = months12.add(month12);
                                }
                                BigDecimal smount = month4.add(month5).add(month6).add(month7).add(month8).add(month9);
                                BigDecimal xmount = month10.add(month11).add(month12).add(month1).add(month2).add(month3);
                                BigDecimal qmount = smount.add(xmount);
                                incomeexpenditurevo.setAmount1(String.valueOf(smount));
                                incomeexpenditurevo.setAmount2(String.valueOf(xmount));
                                incomeexpenditurevo.setAmount3(String.valueOf(qmount));
                            } else if (iteA.getCode().equals("PJ147003")) {
                                for (CostCarryForward costlist : costcarryforwardlist) {
                                    if (costlist.getRegion().substring(6, 7).equals("01")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getPeocost());
                                        planmonth1 = planmonth1.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("02")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getPeocost());
                                        planmonth2 = planmonth2.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("03")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getPeocost());
                                        planmonth3 = planmonth3.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("04")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getPeocost());
                                        planmonth4 = planmonth4.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("05")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getPeocost());
                                        planmonth5 = planmonth5.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("06")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getPeocost());
                                        planmonth6 = planmonth6.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("07")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getPeocost());
                                        planmonth7 = planmonth7.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("08")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getPeocost());
                                        planmonth8 = planmonth8.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("09")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getPeocost());
                                        planmonth9 = planmonth9.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("10")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getPeocost());
                                        planmonth10 = planmonth10.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("11")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getPeocost());
                                        planmonth11 = planmonth11.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("12")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getPeocost());
                                        planmonth12 = planmonth12.add(intotal);
                                    }
                                }
                                BigDecimal spanmount = planmonth4.add(planmonth5).add(planmonth6).add(planmonth7).add(planmonth8).add(planmonth9);
                                BigDecimal xpanmount = planmonth10.add(planmonth11).add(planmonth12).add(planmonth1).add(planmonth2).add(planmonth3);
                                BigDecimal qpanmount = spanmount.add(xpanmount);
                                incomeexpenditurevo.setPlanamount1(String.valueOf(spanmount));
                                incomeexpenditurevo.setPlanamount2(String.valueOf(xpanmount));
                                incomeexpenditurevo.setPlanamount3(String.valueOf(qpanmount));
                                incomeexpenditurevo.setThemename(iteA.getValue1());
                                for (PersonnelPlan list : personnelplanlist) {
                                    if (list.getType().equals("0")) {
                                        BigDecimal moneyavg = new BigDecimal(list.getMoneyavg());
                                        BigDecimal smount = moneyavg.multiply(personnel4).add(moneyavg.multiply(personnel5)).add(moneyavg.multiply(personnel6)).add(moneyavg.multiply(personnel7)).add(moneyavg.multiply(personnel8)).add(moneyavg.multiply(personnel9));
                                        BigDecimal xmount = moneyavg.multiply(personnel10).add(moneyavg.multiply(personnel11)).add(moneyavg.multiply(personnel12)).add(moneyavg.multiply(personnel1)).add(moneyavg.multiply(personnel2)).add(moneyavg.multiply(personnel3));
                                        BigDecimal qmount = smount.add(xmount);
                                        incomeexpenditurevo.setAmount1(String.valueOf(smount));
                                        incomeexpenditurevo.setAmount2(String.valueOf(xmount));
                                        incomeexpenditurevo.setAmount3(String.valueOf(qmount));
                                    }
                                }
                            } else if (iteA.getCode().equals("PJ147004")) {
                                for (CostCarryForward costlist : costcarryforwardlist) {
                                    if (costlist.getRegion().substring(6, 7).equals("01")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOutcost());
                                        planmonth1 = planmonth1.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("02")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOutcost());
                                        planmonth2 = planmonth2.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("03")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOutcost());
                                        planmonth3 = planmonth3.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("04")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOutcost());
                                        planmonth4 = planmonth4.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("05")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOutcost());
                                        planmonth5 = planmonth5.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("06")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOutcost());
                                        planmonth6 = planmonth6.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("07")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOutcost());
                                        planmonth7 = planmonth7.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("08")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOutcost());
                                        planmonth8 = planmonth8.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("09")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOutcost());
                                        planmonth9 = planmonth9.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("10")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOutcost());
                                        planmonth10 = planmonth10.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("11")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOutcost());
                                        planmonth11 = planmonth11.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("12")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOutcost());
                                        planmonth12 = planmonth12.add(intotal);
                                    }
                                }
                                BigDecimal spanmount = planmonth4.add(planmonth5).add(planmonth6).add(planmonth7).add(planmonth8).add(planmonth9);
                                BigDecimal xpanmount = planmonth10.add(planmonth11).add(planmonth12).add(planmonth1).add(planmonth2).add(planmonth3);
                                BigDecimal qpanmount = spanmount.add(xpanmount);
                                incomeexpenditurevo.setPlanamount1(String.valueOf(spanmount));
                                incomeexpenditurevo.setPlanamount2(String.valueOf(xpanmount));
                                incomeexpenditurevo.setPlanamount3(String.valueOf(qpanmount));
                                incomeexpenditurevo.setThemename(iteA.getValue1());
                                for (PersonnelPlan list : personnelplanlist) {
                                    if (list.getType().equals("1")) {
                                        BigDecimal moneyavg = new BigDecimal(list.getMoneyavg());
                                        BigDecimal smount = moneyavg.multiply(wpersonnel4).add(moneyavg.multiply(wpersonnel5)).add(moneyavg.multiply(wpersonnel6)).add(moneyavg.multiply(wpersonnel7)).add(moneyavg.multiply(wpersonnel8)).add(moneyavg.multiply(wpersonnel9));
                                        BigDecimal xmount = moneyavg.multiply(wpersonnel10).add(moneyavg.multiply(wpersonnel11)).add(moneyavg.multiply(wpersonnel12)).add(moneyavg.multiply(wpersonnel1)).add(moneyavg.multiply(wpersonnel2)).add(moneyavg.multiply(wpersonnel3));
                                        BigDecimal qmount = smount.add(xmount);
                                        incomeexpenditurevo.setAmount1(String.valueOf(smount));
                                        incomeexpenditurevo.setAmount2(String.valueOf(xmount));
                                        incomeexpenditurevo.setAmount3(String.valueOf(qmount));
                                    }
                                }
                            } else if (iteA.getCode().equals("PJ147005")) {
                                for (CostCarryForward costlist : costcarryforwardlist) {
                                    if (costlist.getRegion().substring(6, 7).equals("01")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationsoft());
                                        planmonth1 = planmonth1.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("02")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationsoft());
                                        planmonth2 = planmonth2.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("03")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationsoft());
                                        planmonth3 = planmonth3.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("04")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationsoft());
                                        planmonth4 = planmonth4.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("05")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationsoft());
                                        planmonth5 = planmonth5.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("06")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationsoft());
                                        planmonth6 = planmonth6.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("07")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationsoft());
                                        planmonth7 = planmonth7.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("08")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationsoft());
                                        planmonth8 = planmonth8.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("09")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationsoft());
                                        planmonth9 = planmonth9.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("10")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationsoft());
                                        planmonth10 = planmonth10.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("11")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationsoft());
                                        planmonth11 = planmonth11.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("12")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationsoft());
                                        planmonth12 = planmonth12.add(intotal);
                                    }
                                }
                                BigDecimal spanmount = planmonth4.add(planmonth5).add(planmonth6).add(planmonth7).add(planmonth8).add(planmonth9);
                                BigDecimal xpanmount = planmonth10.add(planmonth11).add(planmonth12).add(planmonth1).add(planmonth2).add(planmonth3);
                                BigDecimal qpanmount = spanmount.add(xpanmount);
                                incomeexpenditurevo.setPlanamount1(String.valueOf(spanmount));
                                incomeexpenditurevo.setPlanamount2(String.valueOf(xpanmount));
                                incomeexpenditurevo.setPlanamount3(String.valueOf(qpanmount));
                                BigDecimal month1 = new BigDecimal("0");
                                BigDecimal month2 = new BigDecimal("0");
                                BigDecimal month3 = new BigDecimal("0");
                                BigDecimal month4 = new BigDecimal("0");
                                BigDecimal month5 = new BigDecimal("0");
                                BigDecimal month6 = new BigDecimal("0");
                                BigDecimal month7 = new BigDecimal("0");
                                BigDecimal month8 = new BigDecimal("0");
                                BigDecimal month9 = new BigDecimal("0");
                                BigDecimal month10 = new BigDecimal("0");
                                BigDecimal month11 = new BigDecimal("0");
                                BigDecimal month12 = new BigDecimal("0");
                                incomeexpenditurevo.setThemename(iteA.getValue1());
                                JSONArray jsonassetsnewyear = JSONArray.parseArray(businessplanlist.get(0).getAssets_newyear());
                                JSONArray jsonassetslastyear = JSONArray.parseArray(businessplanlist.get(0).getAssets_lastyear());
                                JSONArray jsonassetslodyear = JSONArray.parseArray(businessplanlist.get(0).getAssets_lodyear());
                                for (Object ob : jsonassetsnewyear) {
                                    BigDecimal months1 = new BigDecimal(getProperty(ob, "money1"));
                                    BigDecimal months2 = new BigDecimal(getProperty(ob, "money2"));
                                    BigDecimal months3 = new BigDecimal(getProperty(ob, "money3"));
                                    BigDecimal months4 = new BigDecimal(getProperty(ob, "money4"));
                                    BigDecimal months5 = new BigDecimal(getProperty(ob, "money5"));
                                    BigDecimal months6 = new BigDecimal(getProperty(ob, "money6"));
                                    BigDecimal months7 = new BigDecimal(getProperty(ob, "money7"));
                                    BigDecimal months8 = new BigDecimal(getProperty(ob, "money8"));
                                    BigDecimal months9 = new BigDecimal(getProperty(ob, "money9"));
                                    BigDecimal months10 = new BigDecimal(getProperty(ob, "money10"));
                                    BigDecimal months11 = new BigDecimal(getProperty(ob, "money11"));
                                    BigDecimal months12 = new BigDecimal(getProperty(ob, "money12"));
                                    month1 = months1.add(month1);
                                    month2 = months2.add(month2);
                                    month3 = months3.add(month3);
                                    month4 = months4.add(month4);
                                    month5 = months5.add(month5);
                                    month6 = months6.add(month6);
                                    month7 = months7.add(month7);
                                    month8 = months8.add(month8);
                                    month9 = months9.add(month9);
                                    month10 = months10.add(month10);
                                    month11 = months11.add(month11);
                                    month12 = months12.add(month12);
                                }
                                for (Object ob : jsonassetslastyear) {
                                    BigDecimal months1 = new BigDecimal(getProperty(ob, "money1"));
                                    BigDecimal months2 = new BigDecimal(getProperty(ob, "money2"));
                                    BigDecimal months3 = new BigDecimal(getProperty(ob, "money3"));
                                    BigDecimal months4 = new BigDecimal(getProperty(ob, "money4"));
                                    BigDecimal months5 = new BigDecimal(getProperty(ob, "money5"));
                                    BigDecimal months6 = new BigDecimal(getProperty(ob, "money6"));
                                    BigDecimal months7 = new BigDecimal(getProperty(ob, "money7"));
                                    BigDecimal months8 = new BigDecimal(getProperty(ob, "money8"));
                                    BigDecimal months9 = new BigDecimal(getProperty(ob, "money9"));
                                    BigDecimal months10 = new BigDecimal(getProperty(ob, "money10"));
                                    BigDecimal months11 = new BigDecimal(getProperty(ob, "money11"));
                                    BigDecimal months12 = new BigDecimal(getProperty(ob, "money12"));
                                    month1 = months1.add(month1);
                                    month2 = months2.add(month2);
                                    month3 = months3.add(month3);
                                    month4 = months4.add(month4);
                                    month5 = months5.add(month5);
                                    month6 = months6.add(month6);
                                    month7 = months7.add(month7);
                                    month8 = months8.add(month8);
                                    month9 = months9.add(month9);
                                    month10 = months10.add(month10);
                                    month11 = months11.add(month11);
                                    month12 = months12.add(month12);
                                }
                                for (Object ob : jsonassetslodyear) {
                                    BigDecimal months1 = new BigDecimal(getProperty(ob, "money1"));
                                    BigDecimal months2 = new BigDecimal(getProperty(ob, "money2"));
                                    BigDecimal months3 = new BigDecimal(getProperty(ob, "money3"));
                                    BigDecimal months4 = new BigDecimal(getProperty(ob, "money4"));
                                    BigDecimal months5 = new BigDecimal(getProperty(ob, "money5"));
                                    BigDecimal months6 = new BigDecimal(getProperty(ob, "money6"));
                                    BigDecimal months7 = new BigDecimal(getProperty(ob, "money7"));
                                    BigDecimal months8 = new BigDecimal(getProperty(ob, "money8"));
                                    BigDecimal months9 = new BigDecimal(getProperty(ob, "money9"));
                                    BigDecimal months10 = new BigDecimal(getProperty(ob, "money10"));
                                    BigDecimal months11 = new BigDecimal(getProperty(ob, "money11"));
                                    BigDecimal months12 = new BigDecimal(getProperty(ob, "money12"));
                                    month1 = months1.add(month1);
                                    month2 = months2.add(month2);
                                    month3 = months3.add(month3);
                                    month4 = months4.add(month4);
                                    month5 = months5.add(month5);
                                    month6 = months6.add(month6);
                                    month7 = months7.add(month7);
                                    month8 = months8.add(month8);
                                    month9 = months9.add(month9);
                                    month10 = months10.add(month10);
                                    month11 = months11.add(month11);
                                    month12 = months12.add(month12);
                                }
                                BigDecimal smount = month4.add(month5).add(month6).add(month7).add(month8).add(month9);
                                BigDecimal xmount = month10.add(month11).add(month12).add(month1).add(month2).add(month3);
                                BigDecimal qmount = smount.add(xmount);
                                incomeexpenditurevo.setAmount1(String.valueOf(smount));
                                incomeexpenditurevo.setAmount2(String.valueOf(xmount));
                                incomeexpenditurevo.setAmount3(String.valueOf(qmount));
                            } else if (iteA.getCode().equals("PJ147006")) {
                                for (CostCarryForward costlist : costcarryforwardlist) {
                                    if (costlist.getRegion().substring(6, 7).equals("01")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationequipment());
                                        planmonth1 = planmonth1.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("02")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationequipment());
                                        planmonth2 = planmonth2.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("03")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationequipment());
                                        planmonth3 = planmonth3.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("04")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationequipment());
                                        planmonth4 = planmonth4.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("05")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationequipment());
                                        planmonth5 = planmonth5.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("06")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationequipment());
                                        planmonth6 = planmonth6.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("07")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationequipment());
                                        planmonth7 = planmonth7.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("08")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationequipment());
                                        planmonth8 = planmonth8.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("09")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationequipment());
                                        planmonth9 = planmonth9.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("10")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationequipment());
                                        planmonth10 = planmonth10.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("11")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationequipment());
                                        planmonth11 = planmonth11.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("12")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationequipment());
                                        planmonth12 = planmonth12.add(intotal);
                                    }
                                }
                                BigDecimal spanmount = planmonth4.add(planmonth5).add(planmonth6).add(planmonth7).add(planmonth8).add(planmonth9);
                                BigDecimal xpanmount = planmonth10.add(planmonth11).add(planmonth12).add(planmonth1).add(planmonth2).add(planmonth3);
                                BigDecimal qpanmount = spanmount.add(xpanmount);
                                incomeexpenditurevo.setPlanamount1(String.valueOf(spanmount));
                                incomeexpenditurevo.setPlanamount2(String.valueOf(xpanmount));
                                incomeexpenditurevo.setPlanamount3(String.valueOf(qpanmount));
                                BigDecimal month1 = new BigDecimal("0");
                                BigDecimal month2 = new BigDecimal("0");
                                BigDecimal month3 = new BigDecimal("0");
                                BigDecimal month4 = new BigDecimal("0");
                                BigDecimal month5 = new BigDecimal("0");
                                BigDecimal month6 = new BigDecimal("0");
                                BigDecimal month7 = new BigDecimal("0");
                                BigDecimal month8 = new BigDecimal("0");
                                BigDecimal month9 = new BigDecimal("0");
                                BigDecimal month10 = new BigDecimal("0");
                                BigDecimal month11 = new BigDecimal("0");
                                BigDecimal month12 = new BigDecimal("0");
                                incomeexpenditurevo.setThemename(iteA.getValue1());
                                JSONArray jsonequipmentnewyear = JSONArray.parseArray(businessplanlist.get(0).getEquipment_newyear());
                                JSONArray jsonequipmentlastyear = JSONArray.parseArray(businessplanlist.get(0).getEquipment_lastyear());
                                JSONArray jsonequipmentlodyear = JSONArray.parseArray(businessplanlist.get(0).getEquipment_lodyear());
                                for (Object ob : jsonequipmentnewyear) {
                                    BigDecimal months1 = new BigDecimal(getProperty(ob, "money1"));
                                    BigDecimal months2 = new BigDecimal(getProperty(ob, "money2"));
                                    BigDecimal months3 = new BigDecimal(getProperty(ob, "money3"));
                                    BigDecimal months4 = new BigDecimal(getProperty(ob, "money4"));
                                    BigDecimal months5 = new BigDecimal(getProperty(ob, "money5"));
                                    BigDecimal months6 = new BigDecimal(getProperty(ob, "money6"));
                                    BigDecimal months7 = new BigDecimal(getProperty(ob, "money7"));
                                    BigDecimal months8 = new BigDecimal(getProperty(ob, "money8"));
                                    BigDecimal months9 = new BigDecimal(getProperty(ob, "money9"));
                                    BigDecimal months10 = new BigDecimal(getProperty(ob, "money10"));
                                    BigDecimal months11 = new BigDecimal(getProperty(ob, "money11"));
                                    BigDecimal months12 = new BigDecimal(getProperty(ob, "money12"));
                                    month1 = months1.add(month1);
                                    month2 = months2.add(month2);
                                    month3 = months3.add(month3);
                                    month4 = months4.add(month4);
                                    month5 = months5.add(month5);
                                    month6 = months6.add(month6);
                                    month7 = months7.add(month7);
                                    month8 = months8.add(month8);
                                    month9 = months9.add(month9);
                                    month10 = months10.add(month10);
                                    month11 = months11.add(month11);
                                    month12 = months12.add(month12);
                                }
                                for (Object ob : jsonequipmentlastyear) {
                                    BigDecimal months1 = new BigDecimal(getProperty(ob, "money1"));
                                    BigDecimal months2 = new BigDecimal(getProperty(ob, "money2"));
                                    BigDecimal months3 = new BigDecimal(getProperty(ob, "money3"));
                                    BigDecimal months4 = new BigDecimal(getProperty(ob, "money4"));
                                    BigDecimal months5 = new BigDecimal(getProperty(ob, "money5"));
                                    BigDecimal months6 = new BigDecimal(getProperty(ob, "money6"));
                                    BigDecimal months7 = new BigDecimal(getProperty(ob, "money7"));
                                    BigDecimal months8 = new BigDecimal(getProperty(ob, "money8"));
                                    BigDecimal months9 = new BigDecimal(getProperty(ob, "money9"));
                                    BigDecimal months10 = new BigDecimal(getProperty(ob, "money10"));
                                    BigDecimal months11 = new BigDecimal(getProperty(ob, "money11"));
                                    BigDecimal months12 = new BigDecimal(getProperty(ob, "money12"));
                                    month1 = months1.add(month1);
                                    month2 = months2.add(month2);
                                    month3 = months3.add(month3);
                                    month4 = months4.add(month4);
                                    month5 = months5.add(month5);
                                    month6 = months6.add(month6);
                                    month7 = months7.add(month7);
                                    month8 = months8.add(month8);
                                    month9 = months9.add(month9);
                                    month10 = months10.add(month10);
                                    month11 = months11.add(month11);
                                    month12 = months12.add(month12);
                                }
                                for (Object ob : jsonequipmentlodyear) {
                                    BigDecimal months1 = new BigDecimal(getProperty(ob, "money1"));
                                    BigDecimal months2 = new BigDecimal(getProperty(ob, "money2"));
                                    BigDecimal months3 = new BigDecimal(getProperty(ob, "money3"));
                                    BigDecimal months4 = new BigDecimal(getProperty(ob, "money4"));
                                    BigDecimal months5 = new BigDecimal(getProperty(ob, "money5"));
                                    BigDecimal months6 = new BigDecimal(getProperty(ob, "money6"));
                                    BigDecimal months7 = new BigDecimal(getProperty(ob, "money7"));
                                    BigDecimal months8 = new BigDecimal(getProperty(ob, "money8"));
                                    BigDecimal months9 = new BigDecimal(getProperty(ob, "money9"));
                                    BigDecimal months10 = new BigDecimal(getProperty(ob, "money10"));
                                    BigDecimal months11 = new BigDecimal(getProperty(ob, "money11"));
                                    BigDecimal months12 = new BigDecimal(getProperty(ob, "money12"));
                                    month1 = months1.add(month1);
                                    month2 = months2.add(month2);
                                    month3 = months3.add(month3);
                                    month4 = months4.add(month4);
                                    month5 = months5.add(month5);
                                    month6 = months6.add(month6);
                                    month7 = months7.add(month7);
                                    month8 = months8.add(month8);
                                    month9 = months9.add(month9);
                                    month10 = months10.add(month10);
                                    month11 = months11.add(month11);
                                    month12 = months12.add(month12);
                                }
                                BigDecimal smount = month4.add(month5).add(month6).add(month7).add(month8).add(month9);
                                BigDecimal xmount = month10.add(month11).add(month12).add(month1).add(month2).add(month3);
                                BigDecimal qmount = smount.add(xmount);
                                incomeexpenditurevo.setAmount1(String.valueOf(smount));
                                incomeexpenditurevo.setAmount2(String.valueOf(xmount));
                                incomeexpenditurevo.setAmount3(String.valueOf(qmount));
                            } else if (iteA.getCode().equals("PJ147007")) {
                                for (CostCarryForward costlist : costcarryforwardlist) {
                                    if (costlist.getRegion().substring(6, 7).equals("01")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getRent());
                                        planmonth1 = planmonth1.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("02")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getRent());
                                        planmonth2 = planmonth2.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("03")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getRent());
                                        planmonth3 = planmonth3.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("04")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getRent());
                                        planmonth4 = planmonth4.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("05")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getRent());
                                        planmonth5 = planmonth5.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("06")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getRent());
                                        planmonth6 = planmonth6.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("07")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getRent());
                                        planmonth7 = planmonth7.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("08")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getRent());
                                        planmonth8 = planmonth8.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("09")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getRent());
                                        planmonth9 = planmonth9.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("10")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getRent());
                                        planmonth10 = planmonth10.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("11")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getRent());
                                        planmonth11 = planmonth11.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("12")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getRent());
                                        planmonth12 = planmonth12.add(intotal);
                                    }
                                }
                                BigDecimal spanmount = planmonth4.add(planmonth5).add(planmonth6).add(planmonth7).add(planmonth8).add(planmonth9);
                                BigDecimal xpanmount = planmonth10.add(planmonth11).add(planmonth12).add(planmonth1).add(planmonth2).add(planmonth3);
                                BigDecimal qpanmount = spanmount.add(xpanmount);
                                incomeexpenditurevo.setPlanamount1(String.valueOf(spanmount));
                                incomeexpenditurevo.setPlanamount2(String.valueOf(xpanmount));
                                incomeexpenditurevo.setPlanamount3(String.valueOf(qpanmount));
                                BigDecimal month1 = new BigDecimal("0");
                                BigDecimal month2 = new BigDecimal("0");
                                BigDecimal month3 = new BigDecimal("0");
                                BigDecimal month4 = new BigDecimal("0");
                                BigDecimal month5 = new BigDecimal("0");
                                BigDecimal month6 = new BigDecimal("0");
                                BigDecimal month7 = new BigDecimal("0");
                                BigDecimal month8 = new BigDecimal("0");
                                BigDecimal month9 = new BigDecimal("0");
                                BigDecimal month10 = new BigDecimal("0");
                                BigDecimal month11 = new BigDecimal("0");
                                BigDecimal month12 = new BigDecimal("0");
                                incomeexpenditurevo.setThemename(iteA.getValue1());
                                JSONArray jsontableo = JSONArray.parseArray(businessplanlist.get(0).getTableO());
                                for (Object ob : jsontableo) {
                                    String type = getProperty(ob, "type");
                                    if (type.equals("PJ111002")) {
                                        BigDecimal months1 = new BigDecimal(getProperty(ob, "money1"));
                                        BigDecimal months2 = new BigDecimal(getProperty(ob, "money2"));
                                        BigDecimal months3 = new BigDecimal(getProperty(ob, "money3"));
                                        BigDecimal months4 = new BigDecimal(getProperty(ob, "money4"));
                                        BigDecimal months5 = new BigDecimal(getProperty(ob, "money5"));
                                        BigDecimal months6 = new BigDecimal(getProperty(ob, "money6"));
                                        BigDecimal months7 = new BigDecimal(getProperty(ob, "money7"));
                                        BigDecimal months8 = new BigDecimal(getProperty(ob, "money8"));
                                        BigDecimal months9 = new BigDecimal(getProperty(ob, "money9"));
                                        BigDecimal months10 = new BigDecimal(getProperty(ob, "money10"));
                                        BigDecimal months11 = new BigDecimal(getProperty(ob, "money11"));
                                        BigDecimal months12 = new BigDecimal(getProperty(ob, "money12"));
                                        month1 = months1.add(month1);
                                        month2 = months2.add(month2);
                                        month3 = months3.add(month3);
                                        month4 = months4.add(month4);
                                        month5 = months5.add(month5);
                                        month6 = months6.add(month6);
                                        month7 = months7.add(month7);
                                        month8 = months8.add(month8);
                                        month9 = months9.add(month9);
                                        month10 = months10.add(month10);
                                        month11 = months11.add(month11);
                                        month12 = months12.add(month12);
                                    }
                                }
                                BigDecimal smount = month4.add(month5).add(month6).add(month7).add(month8).add(month9);
                                BigDecimal xmount = month10.add(month11).add(month12).add(month1).add(month2).add(month3);
                                BigDecimal qmount = smount.add(xmount);
                                incomeexpenditurevo.setAmount1(String.valueOf(smount));
                                incomeexpenditurevo.setAmount2(String.valueOf(xmount));
                                incomeexpenditurevo.setAmount3(String.valueOf(qmount));
                            } else if (iteA.getCode().equals("PJ147008")) {
                                for (CostCarryForward costlist : costcarryforwardlist) {
                                    if (costlist.getRegion().substring(6, 7).equals("01")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getInwetuo());
                                        planmonth1 = planmonth1.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("02")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getInwetuo());
                                        planmonth2 = planmonth2.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("03")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getInwetuo());
                                        planmonth3 = planmonth3.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("04")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getInwetuo());
                                        planmonth4 = planmonth4.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("05")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getInwetuo());
                                        planmonth5 = planmonth5.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("06")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getInwetuo());
                                        planmonth6 = planmonth6.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("07")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getInwetuo());
                                        planmonth7 = planmonth7.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("08")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getInwetuo());
                                        planmonth8 = planmonth8.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("09")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getInwetuo());
                                        planmonth9 = planmonth9.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("10")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getInwetuo());
                                        planmonth10 = planmonth10.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("11")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getInwetuo());
                                        planmonth11 = planmonth11.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("12")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getInwetuo());
                                        planmonth12 = planmonth12.add(intotal);
                                    }
                                }
                                BigDecimal spanmount = planmonth4.add(planmonth5).add(planmonth6).add(planmonth7).add(planmonth8).add(planmonth9);
                                BigDecimal xpanmount = planmonth10.add(planmonth11).add(planmonth12).add(planmonth1).add(planmonth2).add(planmonth3);
                                BigDecimal qpanmount = spanmount.add(xpanmount);
                                incomeexpenditurevo.setPlanamount1(String.valueOf(spanmount));
                                incomeexpenditurevo.setPlanamount2(String.valueOf(xpanmount));
                                incomeexpenditurevo.setPlanamount3(String.valueOf(qpanmount));
                                BigDecimal month1 = new BigDecimal("0");
                                BigDecimal month2 = new BigDecimal("0");
                                BigDecimal month3 = new BigDecimal("0");
                                BigDecimal month4 = new BigDecimal("0");
                                BigDecimal month5 = new BigDecimal("0");
                                BigDecimal month6 = new BigDecimal("0");
                                BigDecimal month7 = new BigDecimal("0");
                                BigDecimal month8 = new BigDecimal("0");
                                BigDecimal month9 = new BigDecimal("0");
                                BigDecimal month10 = new BigDecimal("0");
                                BigDecimal month11 = new BigDecimal("0");
                                BigDecimal month12 = new BigDecimal("0");
                                incomeexpenditurevo.setThemename(iteA.getValue1());
                                JSONArray jsongroupb3 = JSONArray.parseArray(businessplanlist.get(0).getGroupB3());
                                for (Object ob : jsongroupb3) {
                                    BigDecimal months1 = new BigDecimal(getProperty(ob, "money1"));
                                    BigDecimal months2 = new BigDecimal(getProperty(ob, "money2"));
                                    BigDecimal months3 = new BigDecimal(getProperty(ob, "money3"));
                                    BigDecimal months4 = new BigDecimal(getProperty(ob, "money4"));
                                    BigDecimal months5 = new BigDecimal(getProperty(ob, "money5"));
                                    BigDecimal months6 = new BigDecimal(getProperty(ob, "money6"));
                                    BigDecimal months7 = new BigDecimal(getProperty(ob, "money7"));
                                    BigDecimal months8 = new BigDecimal(getProperty(ob, "money8"));
                                    BigDecimal months9 = new BigDecimal(getProperty(ob, "money9"));
                                    BigDecimal months10 = new BigDecimal(getProperty(ob, "money10"));
                                    BigDecimal months11 = new BigDecimal(getProperty(ob, "money11"));
                                    BigDecimal months12 = new BigDecimal(getProperty(ob, "money12"));
                                    month1 = months1.add(month1);
                                    month2 = months2.add(month2);
                                    month3 = months3.add(month3);
                                    month4 = months4.add(month4);
                                    month5 = months5.add(month5);
                                    month6 = months6.add(month6);
                                    month7 = months7.add(month7);
                                    month8 = months8.add(month8);
                                    month9 = months9.add(month9);
                                    month10 = months10.add(month10);
                                    month11 = months11.add(month11);
                                    month12 = months12.add(month12);
                                }
                                BigDecimal smount = month4.add(month5).add(month6).add(month7).add(month8).add(month9);
                                BigDecimal xmount = month10.add(month11).add(month12).add(month1).add(month2).add(month3);
                                BigDecimal qmount = smount.add(xmount);
                                incomeexpenditurevo.setAmount1(String.valueOf(smount));
                                incomeexpenditurevo.setAmount2(String.valueOf(xmount));
                                incomeexpenditurevo.setAmount3(String.valueOf(qmount));
                            } else if (iteA.getCode().equals("PJ147009")) {
                                for (CostCarryForward costlist : costcarryforwardlist) {
                                    if (costlist.getRegion().substring(6, 7).equals("01")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal()).subtract(new BigDecimal(costlist.getInwetuo())).subtract(new BigDecimal(costlist.getRent())).subtract(new BigDecimal(costlist.getCosttotal())).subtract(new BigDecimal(costlist.getPeocost())).subtract(new BigDecimal(costlist.getOutcost())).subtract(new BigDecimal(costlist.getDepreciationsoft())).subtract(new BigDecimal(costlist.getDepreciationequipment()));
                                        planmonth1 = planmonth1.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("02")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal()).subtract(new BigDecimal(costlist.getInwetuo())).subtract(new BigDecimal(costlist.getRent())).subtract(new BigDecimal(costlist.getCosttotal())).subtract(new BigDecimal(costlist.getPeocost())).subtract(new BigDecimal(costlist.getOutcost())).subtract(new BigDecimal(costlist.getDepreciationsoft())).subtract(new BigDecimal(costlist.getDepreciationequipment()));
                                        planmonth2 = planmonth2.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("03")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal()).subtract(new BigDecimal(costlist.getInwetuo())).subtract(new BigDecimal(costlist.getRent())).subtract(new BigDecimal(costlist.getCosttotal())).subtract(new BigDecimal(costlist.getPeocost())).subtract(new BigDecimal(costlist.getOutcost())).subtract(new BigDecimal(costlist.getDepreciationsoft())).subtract(new BigDecimal(costlist.getDepreciationequipment()));
                                        planmonth3 = planmonth3.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("04")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal()).subtract(new BigDecimal(costlist.getInwetuo())).subtract(new BigDecimal(costlist.getRent())).subtract(new BigDecimal(costlist.getCosttotal())).subtract(new BigDecimal(costlist.getPeocost())).subtract(new BigDecimal(costlist.getOutcost())).subtract(new BigDecimal(costlist.getDepreciationsoft())).subtract(new BigDecimal(costlist.getDepreciationequipment()));
                                        planmonth4 = planmonth4.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("05")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal()).subtract(new BigDecimal(costlist.getInwetuo())).subtract(new BigDecimal(costlist.getRent())).subtract(new BigDecimal(costlist.getCosttotal())).subtract(new BigDecimal(costlist.getPeocost())).subtract(new BigDecimal(costlist.getOutcost())).subtract(new BigDecimal(costlist.getDepreciationsoft())).subtract(new BigDecimal(costlist.getDepreciationequipment()));
                                        planmonth5 = planmonth5.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("06")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal()).subtract(new BigDecimal(costlist.getInwetuo())).subtract(new BigDecimal(costlist.getRent())).subtract(new BigDecimal(costlist.getCosttotal())).subtract(new BigDecimal(costlist.getPeocost())).subtract(new BigDecimal(costlist.getOutcost())).subtract(new BigDecimal(costlist.getDepreciationsoft())).subtract(new BigDecimal(costlist.getDepreciationequipment()));
                                        planmonth6 = planmonth6.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("07")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal()).subtract(new BigDecimal(costlist.getInwetuo())).subtract(new BigDecimal(costlist.getRent())).subtract(new BigDecimal(costlist.getCosttotal())).subtract(new BigDecimal(costlist.getPeocost())).subtract(new BigDecimal(costlist.getOutcost())).subtract(new BigDecimal(costlist.getDepreciationsoft())).subtract(new BigDecimal(costlist.getDepreciationequipment()));
                                        planmonth7 = planmonth7.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("08")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal()).subtract(new BigDecimal(costlist.getInwetuo())).subtract(new BigDecimal(costlist.getRent())).subtract(new BigDecimal(costlist.getCosttotal())).subtract(new BigDecimal(costlist.getPeocost())).subtract(new BigDecimal(costlist.getOutcost())).subtract(new BigDecimal(costlist.getDepreciationsoft())).subtract(new BigDecimal(costlist.getDepreciationequipment()));
                                        planmonth8 = planmonth8.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("09")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal()).subtract(new BigDecimal(costlist.getInwetuo())).subtract(new BigDecimal(costlist.getRent())).subtract(new BigDecimal(costlist.getCosttotal())).subtract(new BigDecimal(costlist.getPeocost())).subtract(new BigDecimal(costlist.getOutcost())).subtract(new BigDecimal(costlist.getDepreciationsoft())).subtract(new BigDecimal(costlist.getDepreciationequipment()));
                                        planmonth9 = planmonth9.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("10")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal()).subtract(new BigDecimal(costlist.getInwetuo())).subtract(new BigDecimal(costlist.getRent())).subtract(new BigDecimal(costlist.getCosttotal())).subtract(new BigDecimal(costlist.getPeocost())).subtract(new BigDecimal(costlist.getOutcost())).subtract(new BigDecimal(costlist.getDepreciationsoft())).subtract(new BigDecimal(costlist.getDepreciationequipment()));
                                        planmonth10 = planmonth10.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("11")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal()).subtract(new BigDecimal(costlist.getInwetuo())).subtract(new BigDecimal(costlist.getRent())).subtract(new BigDecimal(costlist.getCosttotal())).subtract(new BigDecimal(costlist.getPeocost())).subtract(new BigDecimal(costlist.getOutcost())).subtract(new BigDecimal(costlist.getDepreciationsoft())).subtract(new BigDecimal(costlist.getDepreciationequipment()));
                                        planmonth11 = planmonth11.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("12")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal()).subtract(new BigDecimal(costlist.getInwetuo())).subtract(new BigDecimal(costlist.getRent())).subtract(new BigDecimal(costlist.getCosttotal())).subtract(new BigDecimal(costlist.getPeocost())).subtract(new BigDecimal(costlist.getOutcost())).subtract(new BigDecimal(costlist.getDepreciationsoft())).subtract(new BigDecimal(costlist.getDepreciationequipment()));
                                        planmonth12 = planmonth12.add(intotal);
                                    }
                                }
                                BigDecimal spanmount = planmonth4.add(planmonth5).add(planmonth6).add(planmonth7).add(planmonth8).add(planmonth9);
                                BigDecimal xpanmount = planmonth10.add(planmonth11).add(planmonth12).add(planmonth1).add(planmonth2).add(planmonth3);
                                BigDecimal qpanmount = spanmount.add(xpanmount);
                                incomeexpenditurevo.setPlanamount1(String.valueOf(spanmount));
                                incomeexpenditurevo.setPlanamount2(String.valueOf(xpanmount));
                                incomeexpenditurevo.setPlanamount3(String.valueOf(qpanmount));
                                BigDecimal month1 = new BigDecimal("0");
                                BigDecimal month2 = new BigDecimal("0");
                                BigDecimal month3 = new BigDecimal("0");
                                BigDecimal month4 = new BigDecimal("0");
                                BigDecimal month5 = new BigDecimal("0");
                                BigDecimal month6 = new BigDecimal("0");
                                BigDecimal month7 = new BigDecimal("0");
                                BigDecimal month8 = new BigDecimal("0");
                                BigDecimal month9 = new BigDecimal("0");
                                BigDecimal month10 = new BigDecimal("0");
                                BigDecimal month11 = new BigDecimal("0");
                                BigDecimal month12 = new BigDecimal("0");
                                incomeexpenditurevo.setThemename(iteA.getValue1());
                                JSONArray jsonbusiness = JSONArray.parseArray(businessplanlist.get(0).getBusiness());
                                JSONArray jsontableo = JSONArray.parseArray(businessplanlist.get(0).getTableO());
                                for (Object ob : jsontableo) {
                                    String type = getProperty(ob, "type");
                                    if (!type.equals("PJ111014") && !type.equals("PJ111002")) {
                                        BigDecimal months1 = new BigDecimal(getProperty(ob, "money1"));
                                        BigDecimal months2 = new BigDecimal(getProperty(ob, "money2"));
                                        BigDecimal months3 = new BigDecimal(getProperty(ob, "money3"));
                                        BigDecimal months4 = new BigDecimal(getProperty(ob, "money4"));
                                        BigDecimal months5 = new BigDecimal(getProperty(ob, "money5"));
                                        BigDecimal months6 = new BigDecimal(getProperty(ob, "money6"));
                                        BigDecimal months7 = new BigDecimal(getProperty(ob, "money7"));
                                        BigDecimal months8 = new BigDecimal(getProperty(ob, "money8"));
                                        BigDecimal months9 = new BigDecimal(getProperty(ob, "money9"));
                                        BigDecimal months10 = new BigDecimal(getProperty(ob, "money10"));
                                        BigDecimal months11 = new BigDecimal(getProperty(ob, "money11"));
                                        BigDecimal months12 = new BigDecimal(getProperty(ob, "money12"));
                                        month1 = months1.add(month1);
                                        month2 = months2.add(month2);
                                        month3 = months3.add(month3);
                                        month4 = months4.add(month4);
                                        month5 = months5.add(month5);
                                        month6 = months6.add(month6);
                                        month7 = months7.add(month7);
                                        month8 = months8.add(month8);
                                        month9 = months9.add(month9);
                                        month10 = months10.add(month10);
                                        month11 = months11.add(month11);
                                        month12 = months12.add(month12);
                                    }
                                }
                                for (Object ob : jsonbusiness) {
                                    BigDecimal months1 = new BigDecimal(getProperty(ob, "money1"));
                                    BigDecimal months2 = new BigDecimal(getProperty(ob, "money2"));
                                    BigDecimal months3 = new BigDecimal(getProperty(ob, "money3"));
                                    BigDecimal months4 = new BigDecimal(getProperty(ob, "money4"));
                                    BigDecimal months5 = new BigDecimal(getProperty(ob, "money5"));
                                    BigDecimal months6 = new BigDecimal(getProperty(ob, "money6"));
                                    BigDecimal months7 = new BigDecimal(getProperty(ob, "money7"));
                                    BigDecimal months8 = new BigDecimal(getProperty(ob, "money8"));
                                    BigDecimal months9 = new BigDecimal(getProperty(ob, "money9"));
                                    BigDecimal months10 = new BigDecimal(getProperty(ob, "money10"));
                                    BigDecimal months11 = new BigDecimal(getProperty(ob, "money11"));
                                    BigDecimal months12 = new BigDecimal(getProperty(ob, "money12"));
                                    month1 = months1.add(month1);
                                    month2 = months2.add(month2);
                                    month3 = months3.add(month3);
                                    month4 = months4.add(month4);
                                    month5 = months5.add(month5);
                                    month6 = months6.add(month6);
                                    month7 = months7.add(month7);
                                    month8 = months8.add(month8);
                                    month9 = months9.add(month9);
                                    month10 = months10.add(month10);
                                    month11 = months11.add(month11);
                                    month12 = months12.add(month12);
                                }
                                BigDecimal smount = month4.add(month5).add(month6).add(month7).add(month8).add(month9);
                                BigDecimal xmount = month10.add(month11).add(month12).add(month1).add(month2).add(month3);
                                BigDecimal qmount = smount.add(xmount);
                                incomeexpenditurevo.setAmount1(String.valueOf(smount));
                                incomeexpenditurevo.setAmount2(String.valueOf(xmount));
                                incomeexpenditurevo.setAmount3(String.valueOf(qmount));
                            } else if (iteA.getCode().equals("PJ147010")) {
                                for (CostCarryForward costlist : costcarryforwardlist) {
                                    if (costlist.getRegion().substring(6, 7).equals("01")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOtherexpenses());
                                        planmonth1 = planmonth1.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("02")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOtherexpenses());
                                        planmonth2 = planmonth2.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("03")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOtherexpenses());
                                        planmonth3 = planmonth3.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("04")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOtherexpenses());
                                        planmonth4 = planmonth4.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("05")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOtherexpenses());
                                        planmonth5 = planmonth5.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("06")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOtherexpenses());
                                        planmonth6 = planmonth6.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("07")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOtherexpenses());
                                        planmonth7 = planmonth7.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("08")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOtherexpenses());
                                        planmonth8 = planmonth8.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("09")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOtherexpenses());
                                        planmonth9 = planmonth9.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("10")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOtherexpenses());
                                        planmonth10 = planmonth10.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("11")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOtherexpenses());
                                        planmonth11 = planmonth11.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("12")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOtherexpenses());
                                        planmonth12 = planmonth12.add(intotal);
                                    }
                                }
                                BigDecimal spanmount = planmonth4.add(planmonth5).add(planmonth6).add(planmonth7).add(planmonth8).add(planmonth9);
                                BigDecimal xpanmount = planmonth10.add(planmonth11).add(planmonth12).add(planmonth1).add(planmonth2).add(planmonth3);
                                BigDecimal qpanmount = spanmount.add(xpanmount);
                                incomeexpenditurevo.setPlanamount1(String.valueOf(spanmount));
                                incomeexpenditurevo.setPlanamount2(String.valueOf(xpanmount));
                                incomeexpenditurevo.setPlanamount3(String.valueOf(qpanmount));
                                BigDecimal month1 = new BigDecimal("0");
                                BigDecimal month2 = new BigDecimal("0");
                                BigDecimal month3 = new BigDecimal("0");
                                BigDecimal month4 = new BigDecimal("0");
                                BigDecimal month5 = new BigDecimal("0");
                                BigDecimal month6 = new BigDecimal("0");
                                BigDecimal month7 = new BigDecimal("0");
                                BigDecimal month8 = new BigDecimal("0");
                                BigDecimal month9 = new BigDecimal("0");
                                BigDecimal month10 = new BigDecimal("0");
                                BigDecimal month11 = new BigDecimal("0");
                                BigDecimal month12 = new BigDecimal("0");
                                incomeexpenditurevo.setThemename(iteA.getValue1());
                                JSONArray jsontableo = JSONArray.parseArray(businessplanlist.get(0).getTableO());
                                for (Object ob : jsontableo) {
                                    String type = getProperty(ob, "type");
                                    if (type.equals("PJ111014")) {
                                        BigDecimal months1 = new BigDecimal(getProperty(ob, "money1"));
                                        BigDecimal months2 = new BigDecimal(getProperty(ob, "money2"));
                                        BigDecimal months3 = new BigDecimal(getProperty(ob, "money3"));
                                        BigDecimal months4 = new BigDecimal(getProperty(ob, "money4"));
                                        BigDecimal months5 = new BigDecimal(getProperty(ob, "money5"));
                                        BigDecimal months6 = new BigDecimal(getProperty(ob, "money6"));
                                        BigDecimal months7 = new BigDecimal(getProperty(ob, "money7"));
                                        BigDecimal months8 = new BigDecimal(getProperty(ob, "money8"));
                                        BigDecimal months9 = new BigDecimal(getProperty(ob, "money9"));
                                        BigDecimal months10 = new BigDecimal(getProperty(ob, "money10"));
                                        BigDecimal months11 = new BigDecimal(getProperty(ob, "money11"));
                                        BigDecimal months12 = new BigDecimal(getProperty(ob, "money12"));
                                        month1 = months1.add(month1);
                                        month2 = months2.add(month2);
                                        month3 = months3.add(month3);
                                        month4 = months4.add(month4);
                                        month5 = months5.add(month5);
                                        month6 = months6.add(month6);
                                        month7 = months7.add(month7);
                                        month8 = months8.add(month8);
                                        month9 = months9.add(month9);
                                        month10 = months10.add(month10);
                                        month11 = months11.add(month11);
                                        month12 = months12.add(month12);
                                    }
                                }
                                BigDecimal smount = month4.add(month5).add(month6).add(month7).add(month8).add(month9);
                                BigDecimal xmount = month10.add(month11).add(month12).add(month1).add(month2).add(month3);
                                BigDecimal qmount = smount.add(xmount);
                                incomeexpenditurevo.setAmount1(String.valueOf(smount));
                                incomeexpenditurevo.setAmount2(String.valueOf(xmount));
                                incomeexpenditurevo.setAmount3(String.valueOf(qmount));
                            } else if (iteA.getCode().equals("PJ147011")) {
                                for (ProjectIncome listproject : projectincomelist) {
                                    if (listproject.getMonth().substring(6, 7).equals("01")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("1")) {
                                                planmonth1.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("02")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("1")) {
                                                planmonth2.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("03")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("1")) {
                                                planmonth3.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("04")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("1")) {
                                                planmonth4.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("05")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("1")) {
                                                planmonth5.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("06")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("1")) {
                                                planmonth6.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("07")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("1")) {
                                                planmonth7.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("08")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("1")) {
                                                planmonth8.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("09")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("1")) {
                                                planmonth9.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("10")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("1")) {
                                                planmonth10.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("11")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("1")) {
                                                planmonth11.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("12")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("1")) {
                                                planmonth12.add(new BigDecimal("1"));
                                            }
                                        }
                                    }
                                }
                                BigDecimal spanmount = planmonth4.add(planmonth5).add(planmonth6).add(planmonth7).add(planmonth8).add(planmonth9);
                                BigDecimal xpanmount = planmonth10.add(planmonth11).add(planmonth12).add(planmonth1).add(planmonth2).add(planmonth3);
                                BigDecimal qpanmount = spanmount.add(xpanmount);
                                incomeexpenditurevo.setPlanamount1(String.valueOf(spanmount));
                                incomeexpenditurevo.setPlanamount2(String.valueOf(xpanmount));
                                incomeexpenditurevo.setPlanamount3(String.valueOf(qpanmount));
                                incomeexpenditurevo.setThemename(iteA.getValue1());
                                BigDecimal smount = personnel4.add(personnel5).add(personnel6).add(personnel7).add(personnel8).add(personnel9);
                                BigDecimal xmount = personnel10.add(personnel11).add(personnel12).add(personnel1).add(personnel2).add(personnel3);
                                BigDecimal qmount = smount.add(xmount);
                                incomeexpenditurevo.setAmount1(String.valueOf(smount));
                                incomeexpenditurevo.setAmount2(String.valueOf(xmount));
                                incomeexpenditurevo.setAmount3(String.valueOf(qmount));
                            } else if (iteA.getCode().equals("PJ147012")) {
                                BigDecimal money1 = new BigDecimal("0");
                                BigDecimal money2 = new BigDecimal("0");
                                BigDecimal money3 = new BigDecimal("0");
                                BigDecimal money4 = new BigDecimal("0");
                                BigDecimal money5 = new BigDecimal("0");
                                BigDecimal money6 = new BigDecimal("0");
                                BigDecimal money7 = new BigDecimal("0");
                                BigDecimal money8 = new BigDecimal("0");
                                BigDecimal money9 = new BigDecimal("0");
                                BigDecimal money10 = new BigDecimal("0");
                                BigDecimal money11 = new BigDecimal("0");
                                BigDecimal money12 = new BigDecimal("0");
                                for (ProjectIncome listproject : projectincomelist) {
                                    if (listproject.getMonth().substring(6, 7).equals("01")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money1.add(new BigDecimal(money));
                                            if (type.equals("1")) {
                                                planmonth1.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("02")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money2.add(new BigDecimal(money));
                                            if (type.equals("1")) {
                                                planmonth2.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("03")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money3.add(new BigDecimal(money));
                                            if (type.equals("1")) {
                                                planmonth3.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("04")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money4.add(new BigDecimal(money));
                                            if (type.equals("1")) {
                                                planmonth4.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("05")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money5.add(new BigDecimal(money));
                                            if (type.equals("1")) {
                                                planmonth5.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("06")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money6.add(new BigDecimal(money));
                                            if (type.equals("1")) {
                                                planmonth6.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("07")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money7.add(new BigDecimal(money));
                                            if (type.equals("1")) {
                                                planmonth7.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("08")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money8.add(new BigDecimal(money));
                                            if (type.equals("1")) {
                                                planmonth8.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("09")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money9.add(new BigDecimal(money));
                                            if (type.equals("1")) {
                                                planmonth9.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("10")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money10.add(new BigDecimal(money));
                                            if (type.equals("1")) {
                                                planmonth10.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("11")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money11.add(new BigDecimal(money));
                                            if (type.equals("1")) {
                                                planmonth11.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("12")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money12.add(new BigDecimal(money));
                                            if (type.equals("1")) {
                                                planmonth12.add(new BigDecimal("1"));
                                            }
                                        }
                                    }
                                }

                                BigDecimal spanmount = money4.divide(planmonth4, scale, roundingMode).add(money5.divide(planmonth5, scale, roundingMode)).add(money6.divide(planmonth6, scale, roundingMode)).add(money7.divide(planmonth7, scale, roundingMode)).add(money8.divide(planmonth8, scale, roundingMode)).add(money9.divide(planmonth9, scale, roundingMode));
                                BigDecimal xpanmount = money10.divide(planmonth10, scale, roundingMode).add(money11.divide(planmonth11, scale, roundingMode)).add(money12.divide(planmonth12, scale, roundingMode)).add(money1.divide(planmonth1, scale, roundingMode)).add(money2.divide(planmonth2, scale, roundingMode)).add(money3.divide(planmonth3, scale, roundingMode));
                                BigDecimal qpanmount = spanmount.add(xpanmount);
                                incomeexpenditurevo.setPlanamount1(String.valueOf(spanmount));
                                incomeexpenditurevo.setPlanamount2(String.valueOf(xpanmount));
                                incomeexpenditurevo.setPlanamount3(String.valueOf(qpanmount));
                                incomeexpenditurevo.setThemename(iteA.getValue1());
                                for (PersonnelPlan list : personnelplanlist) {
                                    if (list.getType().equals("0")) {
                                        BigDecimal moneyavg = new BigDecimal(list.getMoneyavg());
                                        BigDecimal smount = moneyavg.add(moneyavg).add(moneyavg).add(moneyavg).add(moneyavg).add(moneyavg);
                                        BigDecimal xmount = moneyavg.add(moneyavg).add(moneyavg).add(moneyavg).add(moneyavg).add(moneyavg);
                                        BigDecimal qmount = smount.add(xmount);
                                        incomeexpenditurevo.setAmount1(String.valueOf(smount));
                                        incomeexpenditurevo.setAmount2(String.valueOf(xmount));
                                        incomeexpenditurevo.setAmount3(String.valueOf(qmount));
                                    }
                                }
                            } else if (iteA.getCode().equals("PJ147013")) {
                                for (ProjectIncome listproject : projectincomelist) {
                                    if (listproject.getMonth().substring(6, 7).equals("01")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("0")) {
                                                planmonth1.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("02")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("0")) {
                                                planmonth2.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("03")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("0")) {
                                                planmonth3.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("04")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("0")) {
                                                planmonth4.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("05")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("0")) {
                                                planmonth5.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("06")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("0")) {
                                                planmonth6.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("07")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("0")) {
                                                planmonth7.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("08")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("0")) {
                                                planmonth8.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("09")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("0")) {
                                                planmonth9.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("10")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("0")) {
                                                planmonth10.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("11")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("0")) {
                                                planmonth11.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("12")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("0")) {
                                                planmonth12.add(new BigDecimal("1"));
                                            }
                                        }
                                    }
                                }
                                BigDecimal spanmount = planmonth4.add(planmonth5).add(planmonth6).add(planmonth7).add(planmonth8).add(planmonth9);
                                BigDecimal xpanmount = planmonth10.add(planmonth11).add(planmonth12).add(planmonth1).add(planmonth2).add(planmonth3);
                                BigDecimal qpanmount = spanmount.add(xpanmount);
                                incomeexpenditurevo.setPlanamount1(String.valueOf(spanmount));
                                incomeexpenditurevo.setPlanamount2(String.valueOf(xpanmount));
                                incomeexpenditurevo.setPlanamount3(String.valueOf(qpanmount));
                                incomeexpenditurevo.setThemename(iteA.getValue1());
                                BigDecimal smount = wpersonnel4.add(wpersonnel5).add(wpersonnel6).add(wpersonnel7).add(wpersonnel8).add(wpersonnel9);
                                BigDecimal xmount = wpersonnel10.add(wpersonnel11).add(wpersonnel12).add(wpersonnel1).add(wpersonnel2).add(wpersonnel3);
                                BigDecimal qmount = smount.add(xmount);
                                incomeexpenditurevo.setAmount1(String.valueOf(smount));
                                incomeexpenditurevo.setAmount2(String.valueOf(xmount));
                                incomeexpenditurevo.setAmount3(String.valueOf(qmount));
                            } else if (iteA.getCode().equals("PJ147014")) {
                                BigDecimal money1 = new BigDecimal("0");
                                BigDecimal money2 = new BigDecimal("0");
                                BigDecimal money3 = new BigDecimal("0");
                                BigDecimal money4 = new BigDecimal("0");
                                BigDecimal money5 = new BigDecimal("0");
                                BigDecimal money6 = new BigDecimal("0");
                                BigDecimal money7 = new BigDecimal("0");
                                BigDecimal money8 = new BigDecimal("0");
                                BigDecimal money9 = new BigDecimal("0");
                                BigDecimal money10 = new BigDecimal("0");
                                BigDecimal money11 = new BigDecimal("0");
                                BigDecimal money12 = new BigDecimal("0");
                                for (ProjectIncome listproject : projectincomelist) {
                                    if (listproject.getMonth().substring(6, 7).equals("01")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money1.add(new BigDecimal(money));
                                            if (type.equals("0")) {
                                                planmonth1.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("02")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money2.add(new BigDecimal(money));
                                            if (type.equals("0")) {
                                                planmonth2.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("03")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money3.add(new BigDecimal(money));
                                            if (type.equals("0")) {
                                                planmonth3.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("04")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money4.add(new BigDecimal(money));
                                            if (type.equals("0")) {
                                                planmonth4.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("05")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money5.add(new BigDecimal(money));
                                            if (type.equals("0")) {
                                                planmonth5.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("06")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money6.add(new BigDecimal(money));
                                            if (type.equals("0")) {
                                                planmonth6.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("07")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money7.add(new BigDecimal(money));
                                            if (type.equals("0")) {
                                                planmonth7.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("08")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money8.add(new BigDecimal(money));
                                            if (type.equals("0")) {
                                                planmonth8.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("09")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money9.add(new BigDecimal(money));
                                            if (type.equals("0")) {
                                                planmonth9.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("10")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money10.add(new BigDecimal(money));
                                            if (type.equals("0")) {
                                                planmonth10.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("11")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money11.add(new BigDecimal(money));
                                            if (type.equals("0")) {
                                                planmonth11.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("12")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money12.add(new BigDecimal(money));
                                            if (type.equals("0")) {
                                                planmonth12.add(new BigDecimal("1"));
                                            }
                                        }
                                    }
                                }
                                BigDecimal spanmount = money4.divide(planmonth4, scale, roundingMode).add(money5.divide(planmonth5, scale, roundingMode)).add(money6.divide(planmonth6, scale, roundingMode)).add(money7.divide(planmonth7, scale, roundingMode)).add(money8.divide(planmonth8, scale, roundingMode)).add(money9.divide(planmonth9, scale, roundingMode));
                                BigDecimal xpanmount = money10.divide(planmonth10, scale, roundingMode).add(money11.divide(planmonth11, scale, roundingMode)).add(money12.divide(planmonth12, scale, roundingMode)).add(money1.divide(planmonth1, scale, roundingMode)).add(money2.divide(planmonth2, scale, roundingMode)).add(money3.divide(planmonth3, scale, roundingMode));
                                BigDecimal qpanmount = spanmount.add(xpanmount);
                                incomeexpenditurevo.setPlanamount1(String.valueOf(spanmount));
                                incomeexpenditurevo.setPlanamount2(String.valueOf(xpanmount));
                                incomeexpenditurevo.setPlanamount3(String.valueOf(qpanmount));
                                incomeexpenditurevo.setThemename(iteA.getValue1());
                                for (PersonnelPlan list : personnelplanlist) {
                                    if (list.getType().equals("0")) {
                                        BigDecimal moneyavg = new BigDecimal(list.getMoneyavg());
                                        BigDecimal smount = moneyavg.add(moneyavg).add(moneyavg).add(moneyavg).add(moneyavg).add(moneyavg);
                                        BigDecimal xmount = moneyavg.add(moneyavg).add(moneyavg).add(moneyavg).add(moneyavg).add(moneyavg);
                                        BigDecimal qmount = smount.add(xmount);
                                        incomeexpenditurevo.setAmount1(String.valueOf(smount));
                                        incomeexpenditurevo.setAmount2(String.valueOf(xmount));
                                        incomeexpenditurevo.setAmount3(String.valueOf(qmount));
                                    }
                                }
                            }
                            incomeexpenditurevolist.add(incomeexpenditurevo);
                        }
                        BigDecimal rmonth1 = new BigDecimal("0");
                        BigDecimal rmonth2 = new BigDecimal("0");
                        BigDecimal rmonth3 = new BigDecimal("0");
                        BigDecimal wmonth1 = new BigDecimal("0");
                        BigDecimal wmonth2 = new BigDecimal("0");
                        BigDecimal wmonth3 = new BigDecimal("0");
                        BigDecimal smonth1 = new BigDecimal("0");
                        BigDecimal smonth2 = new BigDecimal("0");
                        BigDecimal smonth3 = new BigDecimal("0");
                        BigDecimal zmonth1 = new BigDecimal("0");
                        BigDecimal zmonth2 = new BigDecimal("0");
                        BigDecimal zmonth3 = new BigDecimal("0");
                        BigDecimal rmonth1s = new BigDecimal("0");
                        BigDecimal rmonth2s = new BigDecimal("0");
                        BigDecimal rmonth3s = new BigDecimal("0");
                        BigDecimal wmonth1s = new BigDecimal("0");
                        BigDecimal wmonth2s = new BigDecimal("0");
                        BigDecimal wmonth3s = new BigDecimal("0");
                        BigDecimal smonth1s = new BigDecimal("0");
                        BigDecimal smonth2s = new BigDecimal("0");
                        BigDecimal smonth3s = new BigDecimal("0");
                        BigDecimal zmonth1s = new BigDecimal("0");
                        BigDecimal zmonth2s = new BigDecimal("0");
                        BigDecimal zmonth3s = new BigDecimal("0");
                        for (IncomeExpenditureVo list : incomeexpenditurevolist) {
                            if (list.getThemename().equals("收入")) {
                                smonth1 = new BigDecimal(list.getAmount1());
                                smonth2 = new BigDecimal(list.getAmount2());
                                smonth3 = new BigDecimal(list.getAmount3());
                                smonth1s = new BigDecimal(list.getPlanamount1());
                                smonth2s = new BigDecimal(list.getPlanamount2());
                                smonth3s = new BigDecimal(list.getPlanamount3());
                            } else if (list.getThemename().equals("人件费")) {
                                rmonth1 = new BigDecimal(list.getAmount1());
                                rmonth2 = new BigDecimal(list.getAmount2());
                                rmonth3 = new BigDecimal(list.getAmount3());
                                rmonth1s = new BigDecimal(list.getPlanamount1());
                                rmonth2s = new BigDecimal(list.getPlanamount2());
                                rmonth3s = new BigDecimal(list.getPlanamount3());
                            } else if (list.getThemename().equals("外注费")) {
                                wmonth1 = new BigDecimal(list.getAmount1());
                                wmonth2 = new BigDecimal(list.getAmount2());
                                wmonth3 = new BigDecimal(list.getAmount3());
                                wmonth1s = new BigDecimal(list.getPlanamount1());
                                wmonth2s = new BigDecimal(list.getPlanamount2());
                                wmonth3s = new BigDecimal(list.getPlanamount3());
                            } else if (list.getThemename().equals("支出")) {
                                zmonth1 = new BigDecimal(list.getAmount1());
                                zmonth2 = new BigDecimal(list.getAmount2());
                                zmonth3 = new BigDecimal(list.getAmount3());
                                zmonth1s = new BigDecimal(list.getPlanamount1());
                                zmonth2s = new BigDecimal(list.getPlanamount2());
                                zmonth3s = new BigDecimal(list.getPlanamount3());
                            }
                        }
                        for (Dictionary iteA : curListA) {
                            IncomeExpenditureVo incomeexpenditurevo = new IncomeExpenditureVo();
                            incomeexpenditurevo.setCenter_id("大连松下");
                            incomeexpenditurevo.setGroup_id(org1.getCompanyen());
                            if (iteA.getCode().equals("PJ1470015")) {
                                incomeexpenditurevo.setAmount1(String.valueOf(smonth1.subtract(rmonth1).subtract(wmonth1)));
                                incomeexpenditurevo.setAmount2(String.valueOf(smonth2.subtract(rmonth2).subtract(wmonth2)));
                                incomeexpenditurevo.setAmount3(String.valueOf(smonth3.subtract(rmonth3).subtract(wmonth3)));
                                incomeexpenditurevo.setPlanamount1(String.valueOf(smonth1s.subtract(rmonth1s).subtract(wmonth1s)));
                                incomeexpenditurevo.setPlanamount2(String.valueOf(smonth2s.subtract(rmonth2s).subtract(wmonth2s)));
                                incomeexpenditurevo.setPlanamount3(String.valueOf(smonth3s.subtract(rmonth3s).subtract(wmonth3s)));
                                incomeexpenditurevo.setThemename(iteA.getValue1());
                            } else if (iteA.getCode().equals("PJ1470016")) {
                                incomeexpenditurevo.setAmount1(String.valueOf(smonth1.subtract(rmonth1).subtract(wmonth1).divide(smonth1, scale, roundingMode)));
                                incomeexpenditurevo.setAmount2(String.valueOf(smonth2.subtract(rmonth2).subtract(wmonth2).divide(smonth2, scale, roundingMode)));
                                incomeexpenditurevo.setAmount3(String.valueOf(smonth3.subtract(rmonth3).subtract(wmonth3).divide(smonth3, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount1(String.valueOf(smonth1s.subtract(rmonth1s).subtract(wmonth1s).divide(smonth1s, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount2(String.valueOf(smonth2s.subtract(rmonth2s).subtract(wmonth2s).divide(smonth2s, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount3(String.valueOf(smonth3s.subtract(rmonth3s).subtract(wmonth3s).divide(smonth3s, scale, roundingMode)));
                                incomeexpenditurevo.setThemename(iteA.getValue1());
                            } else if (iteA.getCode().equals("PJ1470017")) {
                                incomeexpenditurevo.setAmount1(String.valueOf(smonth1.subtract(zmonth1)));
                                incomeexpenditurevo.setAmount2(String.valueOf(smonth2.subtract(zmonth2)));
                                incomeexpenditurevo.setAmount3(String.valueOf(smonth3.subtract(zmonth3)));
                                incomeexpenditurevo.setPlanamount1(String.valueOf(smonth1s.subtract(zmonth1s)));
                                incomeexpenditurevo.setPlanamount2(String.valueOf(smonth2s.subtract(zmonth2s)));
                                incomeexpenditurevo.setPlanamount3(String.valueOf(smonth3s.subtract(zmonth3s)));
                                incomeexpenditurevo.setThemename(iteA.getValue1());
                            } else if (iteA.getCode().equals("PJ1470018")) {
                                incomeexpenditurevo.setAmount1(String.valueOf(smonth1.subtract(zmonth1).divide(smonth1, scale, roundingMode)));
                                incomeexpenditurevo.setAmount2(String.valueOf(smonth2.subtract(zmonth2).divide(smonth2, scale, roundingMode)));
                                incomeexpenditurevo.setAmount3(String.valueOf(smonth3.subtract(zmonth3).divide(smonth3, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount1(String.valueOf(smonth1s.subtract(zmonth1s).divide(smonth1s, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount2(String.valueOf(smonth2s.subtract(zmonth2s).divide(smonth2s, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount3(String.valueOf(smonth3s.subtract(zmonth3s).divide(smonth3s, scale, roundingMode)));
                                incomeexpenditurevo.setThemename(iteA.getValue1());
                            }
                            incomeexpenditurevolist.add(incomeexpenditurevo);
                        }
                    }
                }
            }
        }
        return incomeexpenditurevolist;
    }

    public List<IncomeExpenditureVo> getrodiodetailthree(String radiox, String radioy) throws Exception {
        List<IncomeExpenditureVo> incomeexpenditurevolist = new ArrayList<>();
        SimpleDateFormat s = new SimpleDateFormat("MM");
        SimpleDateFormat s1 = new SimpleDateFormat("YYYY");
        int scale = 2;//设置位数
        int roundingMode = 4;//表示四舍五入，可以选择其他舍值方式，例如去尾，等等.
        int year = Integer.valueOf(s.format(new Date())) >= 4 ? Integer.valueOf(s1.format(new Date())) + 1 : Integer.valueOf(s1.format(new Date()));
        if (radioy.equals("5")) {
            OrgTree orgs = orgTreeService.get(new OrgTree());
            for (OrgTree org : orgs.getOrgs()) {
                for (OrgTree org1 : org.getOrgs()) {
                    if (radiox.equals("3")) {
                        Businessplan businessplan = new Businessplan();
                        businessplan.setYear(String.valueOf(year));
                        businessplan.setGroup_id(org1.get_id());
                        List<Businessplan> businessplanlist = businessplanMapper.select(businessplan);
                        CostCarryForward costcarryforward = new CostCarryForward();
                        costcarryforward.setYear(String.valueOf(year));
                        costcarryforward.setGroup_id(org1.get_id());
                        List<CostCarryForward> costcarryforwardlist = costcarryforwardmapper.select(costcarryforward);

                        ProjectIncome projectincome = new ProjectIncome();
                        projectincome.setYear(String.valueOf(year));
                        projectincome.setGroup_id(org1.get_id());
                        List<ProjectIncome> projectincomelist = projectincomemapper.select(projectincome);

                        PersonnelPlan personnelplan = new PersonnelPlan();
                        personnelplan.setYears(String.valueOf(year));
                        personnelplan.setCenterid(org1.get_id());
                        List<PersonnelPlan> personnelplanlist = personnelplanmapper.select(personnelplan);
                        BigDecimal personnel1 = new BigDecimal("0");
                        BigDecimal personnel2 = new BigDecimal("0");
                        BigDecimal personnel3 = new BigDecimal("0");
                        BigDecimal personnel4 = new BigDecimal("0");
                        BigDecimal personnel5 = new BigDecimal("0");
                        BigDecimal personnel6 = new BigDecimal("0");
                        BigDecimal personnel7 = new BigDecimal("0");
                        BigDecimal personnel8 = new BigDecimal("0");
                        BigDecimal personnel9 = new BigDecimal("0");
                        BigDecimal personnel10 = new BigDecimal("0");
                        BigDecimal personnel11 = new BigDecimal("0");
                        BigDecimal personnel12 = new BigDecimal("0");
                        BigDecimal wpersonnel1 = new BigDecimal("0");
                        BigDecimal wpersonnel2 = new BigDecimal("0");
                        BigDecimal wpersonnel3 = new BigDecimal("0");
                        BigDecimal wpersonnel4 = new BigDecimal("0");
                        BigDecimal wpersonnel5 = new BigDecimal("0");
                        BigDecimal wpersonnel6 = new BigDecimal("0");
                        BigDecimal wpersonnel7 = new BigDecimal("0");
                        BigDecimal wpersonnel8 = new BigDecimal("0");
                        BigDecimal wpersonnel9 = new BigDecimal("0");
                        BigDecimal wpersonnel10 = new BigDecimal("0");
                        BigDecimal wpersonnel11 = new BigDecimal("0");
                        BigDecimal wpersonnel12 = new BigDecimal("0");
                        for (PersonnelPlan list : personnelplanlist) {
                            if (list.getType().equals("0")) {
                                JSONArray jsonemployed = JSONArray.parseArray(list.getEmployed());
                                JSONArray jsonnewentry = JSONArray.parseArray(list.getNewentry());
                                personnel1 = new BigDecimal(jsonemployed.size());
                                personnel2 = new BigDecimal(jsonemployed.size());
                                personnel3 = new BigDecimal(jsonemployed.size());
                                personnel4 = new BigDecimal(jsonemployed.size());
                                personnel5 = new BigDecimal(jsonemployed.size());
                                personnel6 = new BigDecimal(jsonemployed.size());
                                personnel7 = new BigDecimal(jsonemployed.size());
                                personnel8 = new BigDecimal(jsonemployed.size());
                                personnel9 = new BigDecimal(jsonemployed.size());
                                personnel10 = new BigDecimal(jsonemployed.size());
                                personnel11 = new BigDecimal(jsonemployed.size());
                                personnel12 = new BigDecimal(jsonemployed.size());
                                for (Object ob : jsonnewentry) {
                                    String month = getProperty(ob, "entermouth");
                                    if (s.format(month).equals("01")) {
                                        personnel1.add(new BigDecimal("1"));
                                        personnel2.add(new BigDecimal("1"));
                                        personnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("02")) {
                                        personnel2.add(new BigDecimal("1"));
                                        personnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("03")) {
                                        personnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("04")) {
                                        personnel4.add(new BigDecimal("1"));
                                        personnel5.add(new BigDecimal("1"));
                                        personnel6.add(new BigDecimal("1"));
                                        personnel7.add(new BigDecimal("1"));
                                        personnel8.add(new BigDecimal("1"));
                                        personnel9.add(new BigDecimal("1"));
                                        personnel10.add(new BigDecimal("1"));
                                        personnel11.add(new BigDecimal("1"));
                                        personnel12.add(new BigDecimal("1"));
                                        personnel1.add(new BigDecimal("1"));
                                        personnel2.add(new BigDecimal("1"));
                                        personnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("05")) {
                                        personnel5.add(new BigDecimal("1"));
                                        personnel6.add(new BigDecimal("1"));
                                        personnel7.add(new BigDecimal("1"));
                                        personnel8.add(new BigDecimal("1"));
                                        personnel9.add(new BigDecimal("1"));
                                        personnel10.add(new BigDecimal("1"));
                                        personnel11.add(new BigDecimal("1"));
                                        personnel12.add(new BigDecimal("1"));
                                        personnel1.add(new BigDecimal("1"));
                                        personnel2.add(new BigDecimal("1"));
                                        personnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("06")) {
                                        personnel6.add(new BigDecimal("1"));
                                        personnel7.add(new BigDecimal("1"));
                                        personnel8.add(new BigDecimal("1"));
                                        personnel9.add(new BigDecimal("1"));
                                        personnel10.add(new BigDecimal("1"));
                                        personnel11.add(new BigDecimal("1"));
                                        personnel12.add(new BigDecimal("1"));
                                        personnel1.add(new BigDecimal("1"));
                                        personnel2.add(new BigDecimal("1"));
                                        personnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("07")) {
                                        personnel7.add(new BigDecimal("1"));
                                        personnel8.add(new BigDecimal("1"));
                                        personnel9.add(new BigDecimal("1"));
                                        personnel10.add(new BigDecimal("1"));
                                        personnel11.add(new BigDecimal("1"));
                                        personnel12.add(new BigDecimal("1"));
                                        personnel1.add(new BigDecimal("1"));
                                        personnel2.add(new BigDecimal("1"));
                                        personnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("08")) {
                                        personnel8.add(new BigDecimal("1"));
                                        personnel9.add(new BigDecimal("1"));
                                        personnel10.add(new BigDecimal("1"));
                                        personnel11.add(new BigDecimal("1"));
                                        personnel12.add(new BigDecimal("1"));
                                        personnel1.add(new BigDecimal("1"));
                                        personnel2.add(new BigDecimal("1"));
                                        personnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("09")) {
                                        personnel9.add(new BigDecimal("1"));
                                        personnel10.add(new BigDecimal("1"));
                                        personnel11.add(new BigDecimal("1"));
                                        personnel12.add(new BigDecimal("1"));
                                        personnel1.add(new BigDecimal("1"));
                                        personnel2.add(new BigDecimal("1"));
                                        personnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("10")) {
                                        personnel10.add(new BigDecimal("1"));
                                        personnel11.add(new BigDecimal("1"));
                                        personnel12.add(new BigDecimal("1"));
                                        personnel1.add(new BigDecimal("1"));
                                        personnel2.add(new BigDecimal("1"));
                                        personnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("11")) {
                                        personnel11.add(new BigDecimal("1"));
                                        personnel12.add(new BigDecimal("1"));
                                        personnel1.add(new BigDecimal("1"));
                                        personnel2.add(new BigDecimal("1"));
                                        personnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("12")) {
                                        personnel12.add(new BigDecimal("1"));
                                        personnel1.add(new BigDecimal("1"));
                                        personnel2.add(new BigDecimal("1"));
                                        personnel3.add(new BigDecimal("1"));
                                    }
                                }
                            } else if (list.getType().equals("1")) {
                                JSONArray jsonemployed = JSONArray.parseArray(list.getEmployed());
                                JSONArray jsonnewentry = JSONArray.parseArray(list.getNewentry());
                                wpersonnel1 = new BigDecimal(jsonemployed.size());
                                wpersonnel2 = new BigDecimal(jsonemployed.size());
                                wpersonnel3 = new BigDecimal(jsonemployed.size());
                                wpersonnel4 = new BigDecimal(jsonemployed.size());
                                wpersonnel5 = new BigDecimal(jsonemployed.size());
                                wpersonnel6 = new BigDecimal(jsonemployed.size());
                                wpersonnel7 = new BigDecimal(jsonemployed.size());
                                wpersonnel8 = new BigDecimal(jsonemployed.size());
                                wpersonnel9 = new BigDecimal(jsonemployed.size());
                                wpersonnel10 = new BigDecimal(jsonemployed.size());
                                wpersonnel11 = new BigDecimal(jsonemployed.size());
                                wpersonnel12 = new BigDecimal(jsonemployed.size());
                                for (Object ob : jsonnewentry) {
                                    String month = getProperty(ob, "entermouth");
                                    if (s.format(month).equals("01")) {
                                        wpersonnel1.add(new BigDecimal("1"));
                                        wpersonnel2.add(new BigDecimal("1"));
                                        wpersonnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("02")) {
                                        wpersonnel2.add(new BigDecimal("1"));
                                        wpersonnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("03")) {
                                        wpersonnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("04")) {
                                        wpersonnel4.add(new BigDecimal("1"));
                                        wpersonnel5.add(new BigDecimal("1"));
                                        wpersonnel6.add(new BigDecimal("1"));
                                        wpersonnel7.add(new BigDecimal("1"));
                                        wpersonnel8.add(new BigDecimal("1"));
                                        wpersonnel9.add(new BigDecimal("1"));
                                        wpersonnel10.add(new BigDecimal("1"));
                                        wpersonnel11.add(new BigDecimal("1"));
                                        wpersonnel12.add(new BigDecimal("1"));
                                        wpersonnel1.add(new BigDecimal("1"));
                                        wpersonnel2.add(new BigDecimal("1"));
                                        wpersonnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("05")) {
                                        wpersonnel5.add(new BigDecimal("1"));
                                        wpersonnel6.add(new BigDecimal("1"));
                                        wpersonnel7.add(new BigDecimal("1"));
                                        wpersonnel8.add(new BigDecimal("1"));
                                        wpersonnel9.add(new BigDecimal("1"));
                                        wpersonnel10.add(new BigDecimal("1"));
                                        wpersonnel11.add(new BigDecimal("1"));
                                        wpersonnel12.add(new BigDecimal("1"));
                                        wpersonnel1.add(new BigDecimal("1"));
                                        wpersonnel2.add(new BigDecimal("1"));
                                        wpersonnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("06")) {
                                        wpersonnel6.add(new BigDecimal("1"));
                                        wpersonnel7.add(new BigDecimal("1"));
                                        wpersonnel8.add(new BigDecimal("1"));
                                        wpersonnel9.add(new BigDecimal("1"));
                                        wpersonnel10.add(new BigDecimal("1"));
                                        wpersonnel11.add(new BigDecimal("1"));
                                        wpersonnel12.add(new BigDecimal("1"));
                                        wpersonnel1.add(new BigDecimal("1"));
                                        wpersonnel2.add(new BigDecimal("1"));
                                        wpersonnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("07")) {
                                        wpersonnel7.add(new BigDecimal("1"));
                                        wpersonnel8.add(new BigDecimal("1"));
                                        wpersonnel9.add(new BigDecimal("1"));
                                        wpersonnel10.add(new BigDecimal("1"));
                                        wpersonnel11.add(new BigDecimal("1"));
                                        wpersonnel12.add(new BigDecimal("1"));
                                        wpersonnel1.add(new BigDecimal("1"));
                                        wpersonnel2.add(new BigDecimal("1"));
                                        wpersonnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("08")) {
                                        wpersonnel8.add(new BigDecimal("1"));
                                        wpersonnel9.add(new BigDecimal("1"));
                                        wpersonnel10.add(new BigDecimal("1"));
                                        wpersonnel11.add(new BigDecimal("1"));
                                        wpersonnel12.add(new BigDecimal("1"));
                                        wpersonnel1.add(new BigDecimal("1"));
                                        wpersonnel2.add(new BigDecimal("1"));
                                        wpersonnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("09")) {
                                        wpersonnel9.add(new BigDecimal("1"));
                                        wpersonnel10.add(new BigDecimal("1"));
                                        wpersonnel11.add(new BigDecimal("1"));
                                        wpersonnel12.add(new BigDecimal("1"));
                                        wpersonnel1.add(new BigDecimal("1"));
                                        wpersonnel2.add(new BigDecimal("1"));
                                        wpersonnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("10")) {
                                        wpersonnel10.add(new BigDecimal("1"));
                                        wpersonnel11.add(new BigDecimal("1"));
                                        wpersonnel12.add(new BigDecimal("1"));
                                        wpersonnel1.add(new BigDecimal("1"));
                                        wpersonnel2.add(new BigDecimal("1"));
                                        wpersonnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("11")) {
                                        wpersonnel11.add(new BigDecimal("1"));
                                        wpersonnel12.add(new BigDecimal("1"));
                                        wpersonnel1.add(new BigDecimal("1"));
                                        wpersonnel2.add(new BigDecimal("1"));
                                        wpersonnel3.add(new BigDecimal("1"));
                                    } else if (s.format(month).equals("12")) {
                                        wpersonnel12.add(new BigDecimal("1"));
                                        wpersonnel1.add(new BigDecimal("1"));
                                        wpersonnel2.add(new BigDecimal("1"));
                                        wpersonnel3.add(new BigDecimal("1"));
                                    }
                                }
                            }
                        }
                        List<com.nt.dao_Org.Dictionary> curListA = dictionaryService.getForSelect("PJ147");
                        for (Dictionary iteA : curListA) {
                            IncomeExpenditureVo incomeexpenditurevo = new IncomeExpenditureVo();
                            incomeexpenditurevo.setCenter_id("大连松下");
                            incomeexpenditurevo.setGroup_id(org1.getCompanyen());
                            BigDecimal planmonth1 = new BigDecimal("0");
                            BigDecimal planmonth2 = new BigDecimal("0");
                            BigDecimal planmonth3 = new BigDecimal("0");
                            BigDecimal planmonth4 = new BigDecimal("0");
                            BigDecimal planmonth5 = new BigDecimal("0");
                            BigDecimal planmonth6 = new BigDecimal("0");
                            BigDecimal planmonth7 = new BigDecimal("0");
                            BigDecimal planmonth8 = new BigDecimal("0");
                            BigDecimal planmonth9 = new BigDecimal("0");
                            BigDecimal planmonth10 = new BigDecimal("0");
                            BigDecimal planmonth11 = new BigDecimal("0");
                            BigDecimal planmonth12 = new BigDecimal("0");
                            if (iteA.getCode().equals("PJ147001")) {
                                for (CostCarryForward costlist : costcarryforwardlist) {
                                    if (costlist.getRegion().substring(6, 7).equals("01")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal());
                                        planmonth1 = planmonth1.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("02")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal());
                                        planmonth2 = planmonth2.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("03")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal());
                                        planmonth3 = planmonth3.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("04")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal());
                                        planmonth4 = planmonth4.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("05")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal());
                                        planmonth5 = planmonth5.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("06")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal());
                                        planmonth6 = planmonth6.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("07")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal());
                                        planmonth7 = planmonth7.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("08")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal());
                                        planmonth8 = planmonth8.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("09")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal());
                                        planmonth9 = planmonth9.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("10")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal());
                                        planmonth10 = planmonth10.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("11")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal());
                                        planmonth11 = planmonth11.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("12")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal());
                                        planmonth12 = planmonth12.add(intotal);
                                    }
                                }
                                BigDecimal jdplanmount1 = planmonth4.add(planmonth5).add(planmonth6);
                                BigDecimal jdplanmount2 = planmonth7.add(planmonth8).add(planmonth9);
                                BigDecimal jdplanmount3 = planmonth10.add(planmonth11).add(planmonth12);
                                BigDecimal jdplanmount4 = planmonth1.add(planmonth2).add(planmonth3);
                                incomeexpenditurevo.setPlanamount1(String.valueOf(jdplanmount1));
                                incomeexpenditurevo.setPlanamount2(String.valueOf(jdplanmount2));
                                incomeexpenditurevo.setPlanamount3(String.valueOf(jdplanmount3));
                                incomeexpenditurevo.setPlanamount4(String.valueOf(jdplanmount4));
                                BigDecimal month1 = new BigDecimal("0");
                                BigDecimal month2 = new BigDecimal("0");
                                BigDecimal month3 = new BigDecimal("0");
                                BigDecimal month4 = new BigDecimal("0");
                                BigDecimal month5 = new BigDecimal("0");
                                BigDecimal month6 = new BigDecimal("0");
                                BigDecimal month7 = new BigDecimal("0");
                                BigDecimal month8 = new BigDecimal("0");
                                BigDecimal month9 = new BigDecimal("0");
                                BigDecimal month10 = new BigDecimal("0");
                                BigDecimal month11 = new BigDecimal("0");
                                BigDecimal month12 = new BigDecimal("0");
                                incomeexpenditurevo.setThemename(iteA.getValue1());
                                JSONArray jsongroupa1 = JSONArray.parseArray(businessplanlist.get(0).getGroupA1());
                                JSONArray jsongroupa2 = JSONArray.parseArray(businessplanlist.get(0).getGroupA2());
                                for (Object ob : jsongroupa1) {
                                    BigDecimal months1 = new BigDecimal(getProperty(ob, "money1"));
                                    BigDecimal months2 = new BigDecimal(getProperty(ob, "money2"));
                                    BigDecimal months3 = new BigDecimal(getProperty(ob, "money3"));
                                    BigDecimal months4 = new BigDecimal(getProperty(ob, "money4"));
                                    BigDecimal months5 = new BigDecimal(getProperty(ob, "money5"));
                                    BigDecimal months6 = new BigDecimal(getProperty(ob, "money6"));
                                    BigDecimal months7 = new BigDecimal(getProperty(ob, "money7"));
                                    BigDecimal months8 = new BigDecimal(getProperty(ob, "money8"));
                                    BigDecimal months9 = new BigDecimal(getProperty(ob, "money9"));
                                    BigDecimal months10 = new BigDecimal(getProperty(ob, "money10"));
                                    BigDecimal months11 = new BigDecimal(getProperty(ob, "money11"));
                                    BigDecimal months12 = new BigDecimal(getProperty(ob, "money12"));
                                    month1 = months1.add(month1);
                                    month2 = months2.add(month2);
                                    month3 = months3.add(month3);
                                    month4 = months4.add(month4);
                                    month5 = months5.add(month5);
                                    month6 = months6.add(month6);
                                    month7 = months7.add(month7);
                                    month8 = months8.add(month8);
                                    month9 = months9.add(month9);
                                    month10 = months10.add(month10);
                                    month11 = months11.add(month11);
                                    month12 = months12.add(month12);
                                }
                                for (Object ob : jsongroupa2) {
                                    BigDecimal months1 = new BigDecimal(getProperty(ob, "money1"));
                                    BigDecimal months2 = new BigDecimal(getProperty(ob, "money2"));
                                    BigDecimal months3 = new BigDecimal(getProperty(ob, "money3"));
                                    BigDecimal months4 = new BigDecimal(getProperty(ob, "money4"));
                                    BigDecimal months5 = new BigDecimal(getProperty(ob, "money5"));
                                    BigDecimal months6 = new BigDecimal(getProperty(ob, "money6"));
                                    BigDecimal months7 = new BigDecimal(getProperty(ob, "money7"));
                                    BigDecimal months8 = new BigDecimal(getProperty(ob, "money8"));
                                    BigDecimal months9 = new BigDecimal(getProperty(ob, "money9"));
                                    BigDecimal months10 = new BigDecimal(getProperty(ob, "money10"));
                                    BigDecimal months11 = new BigDecimal(getProperty(ob, "money11"));
                                    BigDecimal months12 = new BigDecimal(getProperty(ob, "money12"));
                                    month1 = months1.add(month1);
                                    month2 = months2.add(month2);
                                    month3 = months3.add(month3);
                                    month4 = months4.add(month4);
                                    month5 = months5.add(month5);
                                    month6 = months6.add(month6);
                                    month7 = months7.add(month7);
                                    month8 = months8.add(month8);
                                    month9 = months9.add(month9);
                                    month10 = months10.add(month10);
                                    month11 = months11.add(month11);
                                    month12 = months12.add(month12);
                                }
                                BigDecimal jdmount1 = month4.add(month5).add(month6);
                                BigDecimal jdmount2 = month7.add(month8).add(month9);
                                BigDecimal jdmount3 = month10.add(month11).add(month12);
                                BigDecimal jdmount4 = month1.add(month2).add(month3);
                                incomeexpenditurevo.setAmount1(String.valueOf(jdmount1));
                                incomeexpenditurevo.setAmount2(String.valueOf(jdmount2));
                                incomeexpenditurevo.setAmount3(String.valueOf(jdmount3));
                                incomeexpenditurevo.setAmount4(String.valueOf(jdmount4));
                            } else if (iteA.getCode().equals("PJ147002")) {
                                for (CostCarryForward costlist : costcarryforwardlist) {
                                    if (costlist.getRegion().substring(6, 7).equals("01")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getCosttotal());
                                        planmonth1 = planmonth1.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("02")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getCosttotal());
                                        planmonth2 = planmonth2.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("03")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getCosttotal());
                                        planmonth3 = planmonth3.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("04")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getCosttotal());
                                        planmonth4 = planmonth4.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("05")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getCosttotal());
                                        planmonth5 = planmonth5.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("06")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getCosttotal());
                                        planmonth6 = planmonth6.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("07")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getCosttotal());
                                        planmonth7 = planmonth7.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("08")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getCosttotal());
                                        planmonth8 = planmonth8.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("09")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getCosttotal());
                                        planmonth9 = planmonth9.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("10")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getCosttotal());
                                        planmonth10 = planmonth10.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("11")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getCosttotal());
                                        planmonth11 = planmonth11.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("12")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getCosttotal());
                                        planmonth12 = planmonth12.add(intotal);
                                    }
                                }
                                BigDecimal jdplanmount1 = planmonth4.add(planmonth5).add(planmonth6);
                                BigDecimal jdplanmount2 = planmonth7.add(planmonth8).add(planmonth9);
                                BigDecimal jdplanmount3 = planmonth10.add(planmonth11).add(planmonth12);
                                BigDecimal jdplanmount4 = planmonth1.add(planmonth2).add(planmonth3);
                                incomeexpenditurevo.setPlanamount1(String.valueOf(jdplanmount1));
                                incomeexpenditurevo.setPlanamount2(String.valueOf(jdplanmount2));
                                incomeexpenditurevo.setPlanamount3(String.valueOf(jdplanmount3));
                                incomeexpenditurevo.setPlanamount4(String.valueOf(jdplanmount4));
                                BigDecimal month1 = new BigDecimal("0");
                                BigDecimal month2 = new BigDecimal("0");
                                BigDecimal month3 = new BigDecimal("0");
                                BigDecimal month4 = new BigDecimal("0");
                                BigDecimal month5 = new BigDecimal("0");
                                BigDecimal month6 = new BigDecimal("0");
                                BigDecimal month7 = new BigDecimal("0");
                                BigDecimal month8 = new BigDecimal("0");
                                BigDecimal month9 = new BigDecimal("0");
                                BigDecimal month10 = new BigDecimal("0");
                                BigDecimal month11 = new BigDecimal("0");
                                BigDecimal month12 = new BigDecimal("0");
                                incomeexpenditurevo.setThemename(iteA.getValue1());
                                JSONArray jsongroupb1 = JSONArray.parseArray(businessplanlist.get(0).getGroupB1());
                                JSONArray jsongroupb2 = JSONArray.parseArray(businessplanlist.get(0).getGroupB2());
                                JSONArray jsongroupb3 = JSONArray.parseArray(businessplanlist.get(0).getGroupB3());
                                JSONArray jsongroupc = JSONArray.parseArray(businessplanlist.get(0).getGroupC());
                                for (Object ob : jsongroupb1) {
                                    BigDecimal months1 = new BigDecimal(getProperty(ob, "money1"));
                                    BigDecimal months2 = new BigDecimal(getProperty(ob, "money2"));
                                    BigDecimal months3 = new BigDecimal(getProperty(ob, "money3"));
                                    BigDecimal months4 = new BigDecimal(getProperty(ob, "money4"));
                                    BigDecimal months5 = new BigDecimal(getProperty(ob, "money5"));
                                    BigDecimal months6 = new BigDecimal(getProperty(ob, "money6"));
                                    BigDecimal months7 = new BigDecimal(getProperty(ob, "money7"));
                                    BigDecimal months8 = new BigDecimal(getProperty(ob, "money8"));
                                    BigDecimal months9 = new BigDecimal(getProperty(ob, "money9"));
                                    BigDecimal months10 = new BigDecimal(getProperty(ob, "money10"));
                                    BigDecimal months11 = new BigDecimal(getProperty(ob, "money11"));
                                    BigDecimal months12 = new BigDecimal(getProperty(ob, "money12"));
                                    month1 = months1.add(month1);
                                    month2 = months2.add(month2);
                                    month3 = months3.add(month3);
                                    month4 = months4.add(month4);
                                    month5 = months5.add(month5);
                                    month6 = months6.add(month6);
                                    month7 = months7.add(month7);
                                    month8 = months8.add(month8);
                                    month9 = months9.add(month9);
                                    month10 = months10.add(month10);
                                    month11 = months11.add(month11);
                                    month12 = months12.add(month12);
                                }
                                for (Object ob : jsongroupb2) {
                                    BigDecimal months1 = new BigDecimal(getProperty(ob, "money1"));
                                    BigDecimal months2 = new BigDecimal(getProperty(ob, "money2"));
                                    BigDecimal months3 = new BigDecimal(getProperty(ob, "money3"));
                                    BigDecimal months4 = new BigDecimal(getProperty(ob, "money4"));
                                    BigDecimal months5 = new BigDecimal(getProperty(ob, "money5"));
                                    BigDecimal months6 = new BigDecimal(getProperty(ob, "money6"));
                                    BigDecimal months7 = new BigDecimal(getProperty(ob, "money7"));
                                    BigDecimal months8 = new BigDecimal(getProperty(ob, "money8"));
                                    BigDecimal months9 = new BigDecimal(getProperty(ob, "money9"));
                                    BigDecimal months10 = new BigDecimal(getProperty(ob, "money10"));
                                    BigDecimal months11 = new BigDecimal(getProperty(ob, "money11"));
                                    BigDecimal months12 = new BigDecimal(getProperty(ob, "money12"));
                                    month1 = months1.add(month1);
                                    month2 = months2.add(month2);
                                    month3 = months3.add(month3);
                                    month4 = months4.add(month4);
                                    month5 = months5.add(month5);
                                    month6 = months6.add(month6);
                                    month7 = months7.add(month7);
                                    month8 = months8.add(month8);
                                    month9 = months9.add(month9);
                                    month10 = months10.add(month10);
                                    month11 = months11.add(month11);
                                    month12 = months12.add(month12);
                                }
                                for (Object ob : jsongroupb3) {
                                    BigDecimal months1 = new BigDecimal(getProperty(ob, "money1"));
                                    BigDecimal months2 = new BigDecimal(getProperty(ob, "money2"));
                                    BigDecimal months3 = new BigDecimal(getProperty(ob, "money3"));
                                    BigDecimal months4 = new BigDecimal(getProperty(ob, "money4"));
                                    BigDecimal months5 = new BigDecimal(getProperty(ob, "money5"));
                                    BigDecimal months6 = new BigDecimal(getProperty(ob, "money6"));
                                    BigDecimal months7 = new BigDecimal(getProperty(ob, "money7"));
                                    BigDecimal months8 = new BigDecimal(getProperty(ob, "money8"));
                                    BigDecimal months9 = new BigDecimal(getProperty(ob, "money9"));
                                    BigDecimal months10 = new BigDecimal(getProperty(ob, "money10"));
                                    BigDecimal months11 = new BigDecimal(getProperty(ob, "money11"));
                                    BigDecimal months12 = new BigDecimal(getProperty(ob, "money12"));
                                    month1 = months1.add(month1);
                                    month2 = months2.add(month2);
                                    month3 = months3.add(month3);
                                    month4 = months4.add(month4);
                                    month5 = months5.add(month5);
                                    month6 = months6.add(month6);
                                    month7 = months7.add(month7);
                                    month8 = months8.add(month8);
                                    month9 = months9.add(month9);
                                    month10 = months10.add(month10);
                                    month11 = months11.add(month11);
                                    month12 = months12.add(month12);
                                }
                                for (Object ob : jsongroupc) {
                                    BigDecimal months1 = new BigDecimal(getProperty(ob, "money1"));
                                    BigDecimal months2 = new BigDecimal(getProperty(ob, "money2"));
                                    BigDecimal months3 = new BigDecimal(getProperty(ob, "money3"));
                                    BigDecimal months4 = new BigDecimal(getProperty(ob, "money4"));
                                    BigDecimal months5 = new BigDecimal(getProperty(ob, "money5"));
                                    BigDecimal months6 = new BigDecimal(getProperty(ob, "money6"));
                                    BigDecimal months7 = new BigDecimal(getProperty(ob, "money7"));
                                    BigDecimal months8 = new BigDecimal(getProperty(ob, "money8"));
                                    BigDecimal months9 = new BigDecimal(getProperty(ob, "money9"));
                                    BigDecimal months10 = new BigDecimal(getProperty(ob, "money10"));
                                    BigDecimal months11 = new BigDecimal(getProperty(ob, "money11"));
                                    BigDecimal months12 = new BigDecimal(getProperty(ob, "money12"));
                                    month1 = months1.add(month1);
                                    month2 = months2.add(month2);
                                    month3 = months3.add(month3);
                                    month4 = months4.add(month4);
                                    month5 = months5.add(month5);
                                    month6 = months6.add(month6);
                                    month7 = months7.add(month7);
                                    month8 = months8.add(month8);
                                    month9 = months9.add(month9);
                                    month10 = months10.add(month10);
                                    month11 = months11.add(month11);
                                    month12 = months12.add(month12);
                                }
                                BigDecimal jdmount1 = month4.add(month5).add(month6);
                                BigDecimal jdmount2 = month7.add(month8).add(month9);
                                BigDecimal jdmount3 = month10.add(month11).add(month12);
                                BigDecimal jdmount4 = month1.add(month2).add(month3);
                                incomeexpenditurevo.setAmount1(String.valueOf(jdmount1));
                                incomeexpenditurevo.setAmount2(String.valueOf(jdmount2));
                                incomeexpenditurevo.setAmount3(String.valueOf(jdmount3));
                                incomeexpenditurevo.setAmount4(String.valueOf(jdmount4));
                            } else if (iteA.getCode().equals("PJ147003")) {
                                for (CostCarryForward costlist : costcarryforwardlist) {
                                    if (costlist.getRegion().substring(6, 7).equals("01")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getPeocost());
                                        planmonth1 = planmonth1.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("02")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getPeocost());
                                        planmonth2 = planmonth2.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("03")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getPeocost());
                                        planmonth3 = planmonth3.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("04")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getPeocost());
                                        planmonth4 = planmonth4.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("05")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getPeocost());
                                        planmonth5 = planmonth5.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("06")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getPeocost());
                                        planmonth6 = planmonth6.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("07")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getPeocost());
                                        planmonth7 = planmonth7.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("08")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getPeocost());
                                        planmonth8 = planmonth8.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("09")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getPeocost());
                                        planmonth9 = planmonth9.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("10")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getPeocost());
                                        planmonth10 = planmonth10.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("11")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getPeocost());
                                        planmonth11 = planmonth11.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("12")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getPeocost());
                                        planmonth12 = planmonth12.add(intotal);
                                    }
                                }
                                BigDecimal jdplanmount1 = planmonth4.add(planmonth5).add(planmonth6);
                                BigDecimal jdplanmount2 = planmonth7.add(planmonth8).add(planmonth9);
                                BigDecimal jdplanmount3 = planmonth10.add(planmonth11).add(planmonth12);
                                BigDecimal jdplanmount4 = planmonth1.add(planmonth2).add(planmonth3);
                                incomeexpenditurevo.setPlanamount1(String.valueOf(jdplanmount1));
                                incomeexpenditurevo.setPlanamount2(String.valueOf(jdplanmount2));
                                incomeexpenditurevo.setPlanamount3(String.valueOf(jdplanmount3));
                                incomeexpenditurevo.setPlanamount4(String.valueOf(jdplanmount4));
                                incomeexpenditurevo.setThemename(iteA.getValue1());
                                for (PersonnelPlan list : personnelplanlist) {
                                    if (list.getType().equals("0")) {
                                        BigDecimal moneyavg = new BigDecimal(list.getMoneyavg());
                                        BigDecimal jdmount1 = moneyavg.multiply(personnel4).add(moneyavg.multiply(personnel5)).add(moneyavg.multiply(personnel6));
                                        BigDecimal jdmount2 = moneyavg.multiply(personnel7).add(moneyavg.multiply(personnel8)).add(moneyavg.multiply(personnel9));
                                        BigDecimal jdmount3 = moneyavg.multiply(personnel10).add(moneyavg.multiply(personnel11)).add(moneyavg.multiply(personnel12));
                                        BigDecimal jdmount4 = moneyavg.multiply(personnel1).add(moneyavg.multiply(personnel2)).add(moneyavg.multiply(personnel3));
                                        incomeexpenditurevo.setAmount1(String.valueOf(jdmount1));
                                        incomeexpenditurevo.setAmount2(String.valueOf(jdmount2));
                                        incomeexpenditurevo.setAmount3(String.valueOf(jdmount3));
                                        incomeexpenditurevo.setAmount4(String.valueOf(jdmount4));
                                    }
                                }
                            } else if (iteA.getCode().equals("PJ147004")) {
                                for (CostCarryForward costlist : costcarryforwardlist) {
                                    if (costlist.getRegion().substring(6, 7).equals("01")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOutcost());
                                        planmonth1 = planmonth1.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("02")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOutcost());
                                        planmonth2 = planmonth2.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("03")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOutcost());
                                        planmonth3 = planmonth3.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("04")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOutcost());
                                        planmonth4 = planmonth4.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("05")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOutcost());
                                        planmonth5 = planmonth5.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("06")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOutcost());
                                        planmonth6 = planmonth6.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("07")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOutcost());
                                        planmonth7 = planmonth7.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("08")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOutcost());
                                        planmonth8 = planmonth8.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("09")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOutcost());
                                        planmonth9 = planmonth9.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("10")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOutcost());
                                        planmonth10 = planmonth10.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("11")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOutcost());
                                        planmonth11 = planmonth11.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("12")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOutcost());
                                        planmonth12 = planmonth12.add(intotal);
                                    }
                                }
                                BigDecimal jdplanmount1 = planmonth4.add(planmonth5).add(planmonth6);
                                BigDecimal jdplanmount2 = planmonth7.add(planmonth8).add(planmonth9);
                                BigDecimal jdplanmount3 = planmonth10.add(planmonth11).add(planmonth12);
                                BigDecimal jdplanmount4 = planmonth1.add(planmonth2).add(planmonth3);
                                incomeexpenditurevo.setPlanamount1(String.valueOf(jdplanmount1));
                                incomeexpenditurevo.setPlanamount2(String.valueOf(jdplanmount2));
                                incomeexpenditurevo.setPlanamount3(String.valueOf(jdplanmount3));
                                incomeexpenditurevo.setPlanamount4(String.valueOf(jdplanmount4));
                                incomeexpenditurevo.setThemename(iteA.getValue1());
                                for (PersonnelPlan list : personnelplanlist) {
                                    if (list.getType().equals("1")) {
                                        BigDecimal moneyavg = new BigDecimal(list.getMoneyavg());
                                        BigDecimal jdmount1 = moneyavg.multiply(wpersonnel4).add(moneyavg.multiply(wpersonnel5)).add(moneyavg.multiply(wpersonnel6));
                                        BigDecimal jdmount2 = moneyavg.multiply(wpersonnel7).add(moneyavg.multiply(wpersonnel8)).add(moneyavg.multiply(wpersonnel9));
                                        BigDecimal jdmount3 = moneyavg.multiply(wpersonnel10).add(moneyavg.multiply(wpersonnel11)).add(moneyavg.multiply(wpersonnel12));
                                        BigDecimal jdmount4 = moneyavg.multiply(wpersonnel1).add(moneyavg.multiply(wpersonnel2)).add(moneyavg.multiply(wpersonnel3));
                                        incomeexpenditurevo.setAmount1(String.valueOf(jdmount1));
                                        incomeexpenditurevo.setAmount2(String.valueOf(jdmount2));
                                        incomeexpenditurevo.setAmount3(String.valueOf(jdmount3));
                                        incomeexpenditurevo.setAmount4(String.valueOf(jdmount4));
                                    }
                                }
                            } else if (iteA.getCode().equals("PJ147005")) {
                                for (CostCarryForward costlist : costcarryforwardlist) {
                                    if (costlist.getRegion().substring(6, 7).equals("01")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationsoft());
                                        planmonth1 = planmonth1.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("02")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationsoft());
                                        planmonth2 = planmonth2.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("03")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationsoft());
                                        planmonth3 = planmonth3.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("04")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationsoft());
                                        planmonth4 = planmonth4.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("05")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationsoft());
                                        planmonth5 = planmonth5.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("06")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationsoft());
                                        planmonth6 = planmonth6.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("07")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationsoft());
                                        planmonth7 = planmonth7.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("08")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationsoft());
                                        planmonth8 = planmonth8.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("09")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationsoft());
                                        planmonth9 = planmonth9.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("10")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationsoft());
                                        planmonth10 = planmonth10.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("11")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationsoft());
                                        planmonth11 = planmonth11.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("12")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationsoft());
                                        planmonth12 = planmonth12.add(intotal);
                                    }
                                }
                                BigDecimal jdplanmount1 = planmonth4.add(planmonth5).add(planmonth6);
                                BigDecimal jdplanmount2 = planmonth7.add(planmonth8).add(planmonth9);
                                BigDecimal jdplanmount3 = planmonth10.add(planmonth11).add(planmonth12);
                                BigDecimal jdplanmount4 = planmonth1.add(planmonth2).add(planmonth3);
                                incomeexpenditurevo.setPlanamount1(String.valueOf(jdplanmount1));
                                incomeexpenditurevo.setPlanamount2(String.valueOf(jdplanmount2));
                                incomeexpenditurevo.setPlanamount3(String.valueOf(jdplanmount3));
                                incomeexpenditurevo.setPlanamount4(String.valueOf(jdplanmount4));
                                BigDecimal month1 = new BigDecimal("0");
                                BigDecimal month2 = new BigDecimal("0");
                                BigDecimal month3 = new BigDecimal("0");
                                BigDecimal month4 = new BigDecimal("0");
                                BigDecimal month5 = new BigDecimal("0");
                                BigDecimal month6 = new BigDecimal("0");
                                BigDecimal month7 = new BigDecimal("0");
                                BigDecimal month8 = new BigDecimal("0");
                                BigDecimal month9 = new BigDecimal("0");
                                BigDecimal month10 = new BigDecimal("0");
                                BigDecimal month11 = new BigDecimal("0");
                                BigDecimal month12 = new BigDecimal("0");
                                incomeexpenditurevo.setThemename(iteA.getValue1());
                                JSONArray jsonassetsnewyear = JSONArray.parseArray(businessplanlist.get(0).getAssets_newyear());
                                JSONArray jsonassetslastyear = JSONArray.parseArray(businessplanlist.get(0).getAssets_lastyear());
                                JSONArray jsonassetslodyear = JSONArray.parseArray(businessplanlist.get(0).getAssets_lodyear());
                                for (Object ob : jsonassetsnewyear) {
                                    BigDecimal months1 = new BigDecimal(getProperty(ob, "money1"));
                                    BigDecimal months2 = new BigDecimal(getProperty(ob, "money2"));
                                    BigDecimal months3 = new BigDecimal(getProperty(ob, "money3"));
                                    BigDecimal months4 = new BigDecimal(getProperty(ob, "money4"));
                                    BigDecimal months5 = new BigDecimal(getProperty(ob, "money5"));
                                    BigDecimal months6 = new BigDecimal(getProperty(ob, "money6"));
                                    BigDecimal months7 = new BigDecimal(getProperty(ob, "money7"));
                                    BigDecimal months8 = new BigDecimal(getProperty(ob, "money8"));
                                    BigDecimal months9 = new BigDecimal(getProperty(ob, "money9"));
                                    BigDecimal months10 = new BigDecimal(getProperty(ob, "money10"));
                                    BigDecimal months11 = new BigDecimal(getProperty(ob, "money11"));
                                    BigDecimal months12 = new BigDecimal(getProperty(ob, "money12"));
                                    month1 = months1.add(month1);
                                    month2 = months2.add(month2);
                                    month3 = months3.add(month3);
                                    month4 = months4.add(month4);
                                    month5 = months5.add(month5);
                                    month6 = months6.add(month6);
                                    month7 = months7.add(month7);
                                    month8 = months8.add(month8);
                                    month9 = months9.add(month9);
                                    month10 = months10.add(month10);
                                    month11 = months11.add(month11);
                                    month12 = months12.add(month12);
                                }
                                for (Object ob : jsonassetslastyear) {
                                    BigDecimal months1 = new BigDecimal(getProperty(ob, "money1"));
                                    BigDecimal months2 = new BigDecimal(getProperty(ob, "money2"));
                                    BigDecimal months3 = new BigDecimal(getProperty(ob, "money3"));
                                    BigDecimal months4 = new BigDecimal(getProperty(ob, "money4"));
                                    BigDecimal months5 = new BigDecimal(getProperty(ob, "money5"));
                                    BigDecimal months6 = new BigDecimal(getProperty(ob, "money6"));
                                    BigDecimal months7 = new BigDecimal(getProperty(ob, "money7"));
                                    BigDecimal months8 = new BigDecimal(getProperty(ob, "money8"));
                                    BigDecimal months9 = new BigDecimal(getProperty(ob, "money9"));
                                    BigDecimal months10 = new BigDecimal(getProperty(ob, "money10"));
                                    BigDecimal months11 = new BigDecimal(getProperty(ob, "money11"));
                                    BigDecimal months12 = new BigDecimal(getProperty(ob, "money12"));
                                    month1 = months1.add(month1);
                                    month2 = months2.add(month2);
                                    month3 = months3.add(month3);
                                    month4 = months4.add(month4);
                                    month5 = months5.add(month5);
                                    month6 = months6.add(month6);
                                    month7 = months7.add(month7);
                                    month8 = months8.add(month8);
                                    month9 = months9.add(month9);
                                    month10 = months10.add(month10);
                                    month11 = months11.add(month11);
                                    month12 = months12.add(month12);
                                }
                                for (Object ob : jsonassetslodyear) {
                                    BigDecimal months1 = new BigDecimal(getProperty(ob, "money1"));
                                    BigDecimal months2 = new BigDecimal(getProperty(ob, "money2"));
                                    BigDecimal months3 = new BigDecimal(getProperty(ob, "money3"));
                                    BigDecimal months4 = new BigDecimal(getProperty(ob, "money4"));
                                    BigDecimal months5 = new BigDecimal(getProperty(ob, "money5"));
                                    BigDecimal months6 = new BigDecimal(getProperty(ob, "money6"));
                                    BigDecimal months7 = new BigDecimal(getProperty(ob, "money7"));
                                    BigDecimal months8 = new BigDecimal(getProperty(ob, "money8"));
                                    BigDecimal months9 = new BigDecimal(getProperty(ob, "money9"));
                                    BigDecimal months10 = new BigDecimal(getProperty(ob, "money10"));
                                    BigDecimal months11 = new BigDecimal(getProperty(ob, "money11"));
                                    BigDecimal months12 = new BigDecimal(getProperty(ob, "money12"));
                                    month1 = months1.add(month1);
                                    month2 = months2.add(month2);
                                    month3 = months3.add(month3);
                                    month4 = months4.add(month4);
                                    month5 = months5.add(month5);
                                    month6 = months6.add(month6);
                                    month7 = months7.add(month7);
                                    month8 = months8.add(month8);
                                    month9 = months9.add(month9);
                                    month10 = months10.add(month10);
                                    month11 = months11.add(month11);
                                    month12 = months12.add(month12);
                                }
                                BigDecimal jdmount1 = month4.add(month5).add(month6);
                                BigDecimal jdmount2 = month7.add(month8).add(month9);
                                BigDecimal jdmount3 = month10.add(month11).add(month12);
                                BigDecimal jdmount4 = month1.add(month2).add(month3);
                                incomeexpenditurevo.setAmount1(String.valueOf(jdmount1));
                                incomeexpenditurevo.setAmount2(String.valueOf(jdmount2));
                                incomeexpenditurevo.setAmount3(String.valueOf(jdmount3));
                                incomeexpenditurevo.setAmount4(String.valueOf(jdmount4));
                            } else if (iteA.getCode().equals("PJ147006")) {
                                for (CostCarryForward costlist : costcarryforwardlist) {
                                    if (costlist.getRegion().substring(6, 7).equals("01")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationequipment());
                                        planmonth1 = planmonth1.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("02")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationequipment());
                                        planmonth2 = planmonth2.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("03")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationequipment());
                                        planmonth3 = planmonth3.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("04")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationequipment());
                                        planmonth4 = planmonth4.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("05")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationequipment());
                                        planmonth5 = planmonth5.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("06")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationequipment());
                                        planmonth6 = planmonth6.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("07")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationequipment());
                                        planmonth7 = planmonth7.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("08")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationequipment());
                                        planmonth8 = planmonth8.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("09")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationequipment());
                                        planmonth9 = planmonth9.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("10")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationequipment());
                                        planmonth10 = planmonth10.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("11")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationequipment());
                                        planmonth11 = planmonth11.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("12")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getDepreciationequipment());
                                        planmonth12 = planmonth12.add(intotal);
                                    }
                                }
                                BigDecimal jdplanmount1 = planmonth4.add(planmonth5).add(planmonth6);
                                BigDecimal jdplanmount2 = planmonth7.add(planmonth8).add(planmonth9);
                                BigDecimal jdplanmount3 = planmonth10.add(planmonth11).add(planmonth12);
                                BigDecimal jdplanmount4 = planmonth1.add(planmonth2).add(planmonth3);
                                incomeexpenditurevo.setPlanamount1(String.valueOf(jdplanmount1));
                                incomeexpenditurevo.setPlanamount2(String.valueOf(jdplanmount2));
                                incomeexpenditurevo.setPlanamount3(String.valueOf(jdplanmount3));
                                incomeexpenditurevo.setPlanamount4(String.valueOf(jdplanmount4));
                                BigDecimal month1 = new BigDecimal("0");
                                BigDecimal month2 = new BigDecimal("0");
                                BigDecimal month3 = new BigDecimal("0");
                                BigDecimal month4 = new BigDecimal("0");
                                BigDecimal month5 = new BigDecimal("0");
                                BigDecimal month6 = new BigDecimal("0");
                                BigDecimal month7 = new BigDecimal("0");
                                BigDecimal month8 = new BigDecimal("0");
                                BigDecimal month9 = new BigDecimal("0");
                                BigDecimal month10 = new BigDecimal("0");
                                BigDecimal month11 = new BigDecimal("0");
                                BigDecimal month12 = new BigDecimal("0");
                                incomeexpenditurevo.setThemename(iteA.getValue1());
                                JSONArray jsonequipmentnewyear = JSONArray.parseArray(businessplanlist.get(0).getEquipment_newyear());
                                JSONArray jsonequipmentlastyear = JSONArray.parseArray(businessplanlist.get(0).getEquipment_lastyear());
                                JSONArray jsonequipmentlodyear = JSONArray.parseArray(businessplanlist.get(0).getEquipment_lodyear());
                                for (Object ob : jsonequipmentnewyear) {
                                    BigDecimal months1 = new BigDecimal(getProperty(ob, "money1"));
                                    BigDecimal months2 = new BigDecimal(getProperty(ob, "money2"));
                                    BigDecimal months3 = new BigDecimal(getProperty(ob, "money3"));
                                    BigDecimal months4 = new BigDecimal(getProperty(ob, "money4"));
                                    BigDecimal months5 = new BigDecimal(getProperty(ob, "money5"));
                                    BigDecimal months6 = new BigDecimal(getProperty(ob, "money6"));
                                    BigDecimal months7 = new BigDecimal(getProperty(ob, "money7"));
                                    BigDecimal months8 = new BigDecimal(getProperty(ob, "money8"));
                                    BigDecimal months9 = new BigDecimal(getProperty(ob, "money9"));
                                    BigDecimal months10 = new BigDecimal(getProperty(ob, "money10"));
                                    BigDecimal months11 = new BigDecimal(getProperty(ob, "money11"));
                                    BigDecimal months12 = new BigDecimal(getProperty(ob, "money12"));
                                    month1 = months1.add(month1);
                                    month2 = months2.add(month2);
                                    month3 = months3.add(month3);
                                    month4 = months4.add(month4);
                                    month5 = months5.add(month5);
                                    month6 = months6.add(month6);
                                    month7 = months7.add(month7);
                                    month8 = months8.add(month8);
                                    month9 = months9.add(month9);
                                    month10 = months10.add(month10);
                                    month11 = months11.add(month11);
                                    month12 = months12.add(month12);
                                }
                                for (Object ob : jsonequipmentlastyear) {
                                    BigDecimal months1 = new BigDecimal(getProperty(ob, "money1"));
                                    BigDecimal months2 = new BigDecimal(getProperty(ob, "money2"));
                                    BigDecimal months3 = new BigDecimal(getProperty(ob, "money3"));
                                    BigDecimal months4 = new BigDecimal(getProperty(ob, "money4"));
                                    BigDecimal months5 = new BigDecimal(getProperty(ob, "money5"));
                                    BigDecimal months6 = new BigDecimal(getProperty(ob, "money6"));
                                    BigDecimal months7 = new BigDecimal(getProperty(ob, "money7"));
                                    BigDecimal months8 = new BigDecimal(getProperty(ob, "money8"));
                                    BigDecimal months9 = new BigDecimal(getProperty(ob, "money9"));
                                    BigDecimal months10 = new BigDecimal(getProperty(ob, "money10"));
                                    BigDecimal months11 = new BigDecimal(getProperty(ob, "money11"));
                                    BigDecimal months12 = new BigDecimal(getProperty(ob, "money12"));
                                    month1 = months1.add(month1);
                                    month2 = months2.add(month2);
                                    month3 = months3.add(month3);
                                    month4 = months4.add(month4);
                                    month5 = months5.add(month5);
                                    month6 = months6.add(month6);
                                    month7 = months7.add(month7);
                                    month8 = months8.add(month8);
                                    month9 = months9.add(month9);
                                    month10 = months10.add(month10);
                                    month11 = months11.add(month11);
                                    month12 = months12.add(month12);
                                }
                                for (Object ob : jsonequipmentlodyear) {
                                    BigDecimal months1 = new BigDecimal(getProperty(ob, "money1"));
                                    BigDecimal months2 = new BigDecimal(getProperty(ob, "money2"));
                                    BigDecimal months3 = new BigDecimal(getProperty(ob, "money3"));
                                    BigDecimal months4 = new BigDecimal(getProperty(ob, "money4"));
                                    BigDecimal months5 = new BigDecimal(getProperty(ob, "money5"));
                                    BigDecimal months6 = new BigDecimal(getProperty(ob, "money6"));
                                    BigDecimal months7 = new BigDecimal(getProperty(ob, "money7"));
                                    BigDecimal months8 = new BigDecimal(getProperty(ob, "money8"));
                                    BigDecimal months9 = new BigDecimal(getProperty(ob, "money9"));
                                    BigDecimal months10 = new BigDecimal(getProperty(ob, "money10"));
                                    BigDecimal months11 = new BigDecimal(getProperty(ob, "money11"));
                                    BigDecimal months12 = new BigDecimal(getProperty(ob, "money12"));
                                    month1 = months1.add(month1);
                                    month2 = months2.add(month2);
                                    month3 = months3.add(month3);
                                    month4 = months4.add(month4);
                                    month5 = months5.add(month5);
                                    month6 = months6.add(month6);
                                    month7 = months7.add(month7);
                                    month8 = months8.add(month8);
                                    month9 = months9.add(month9);
                                    month10 = months10.add(month10);
                                    month11 = months11.add(month11);
                                    month12 = months12.add(month12);
                                }
                                BigDecimal jdmount1 = month4.add(month5).add(month6);
                                BigDecimal jdmount2 = month7.add(month8).add(month9);
                                BigDecimal jdmount3 = month10.add(month11).add(month12);
                                BigDecimal jdmount4 = month1.add(month2).add(month3);
                                incomeexpenditurevo.setAmount1(String.valueOf(jdmount1));
                                incomeexpenditurevo.setAmount2(String.valueOf(jdmount2));
                                incomeexpenditurevo.setAmount3(String.valueOf(jdmount3));
                                incomeexpenditurevo.setAmount4(String.valueOf(jdmount4));
                            } else if (iteA.getCode().equals("PJ147007")) {
                                for (CostCarryForward costlist : costcarryforwardlist) {
                                    if (costlist.getRegion().substring(6, 7).equals("01")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getRent());
                                        planmonth1 = planmonth1.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("02")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getRent());
                                        planmonth2 = planmonth2.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("03")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getRent());
                                        planmonth3 = planmonth3.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("04")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getRent());
                                        planmonth4 = planmonth4.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("05")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getRent());
                                        planmonth5 = planmonth5.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("06")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getRent());
                                        planmonth6 = planmonth6.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("07")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getRent());
                                        planmonth7 = planmonth7.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("08")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getRent());
                                        planmonth8 = planmonth8.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("09")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getRent());
                                        planmonth9 = planmonth9.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("10")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getRent());
                                        planmonth10 = planmonth10.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("11")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getRent());
                                        planmonth11 = planmonth11.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("12")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getRent());
                                        planmonth12 = planmonth12.add(intotal);
                                    }
                                }
                                BigDecimal jdplanmount1 = planmonth4.add(planmonth5).add(planmonth6);
                                BigDecimal jdplanmount2 = planmonth7.add(planmonth8).add(planmonth9);
                                BigDecimal jdplanmount3 = planmonth10.add(planmonth11).add(planmonth12);
                                BigDecimal jdplanmount4 = planmonth1.add(planmonth2).add(planmonth3);
                                incomeexpenditurevo.setPlanamount1(String.valueOf(jdplanmount1));
                                incomeexpenditurevo.setPlanamount2(String.valueOf(jdplanmount2));
                                incomeexpenditurevo.setPlanamount3(String.valueOf(jdplanmount3));
                                incomeexpenditurevo.setPlanamount4(String.valueOf(jdplanmount4));
                                BigDecimal month1 = new BigDecimal("0");
                                BigDecimal month2 = new BigDecimal("0");
                                BigDecimal month3 = new BigDecimal("0");
                                BigDecimal month4 = new BigDecimal("0");
                                BigDecimal month5 = new BigDecimal("0");
                                BigDecimal month6 = new BigDecimal("0");
                                BigDecimal month7 = new BigDecimal("0");
                                BigDecimal month8 = new BigDecimal("0");
                                BigDecimal month9 = new BigDecimal("0");
                                BigDecimal month10 = new BigDecimal("0");
                                BigDecimal month11 = new BigDecimal("0");
                                BigDecimal month12 = new BigDecimal("0");
                                incomeexpenditurevo.setThemename(iteA.getValue1());
                                JSONArray jsontableo = JSONArray.parseArray(businessplanlist.get(0).getTableO());
                                for (Object ob : jsontableo) {
                                    String type = getProperty(ob, "type");
                                    if (type.equals("PJ111002")) {
                                        BigDecimal months1 = new BigDecimal(getProperty(ob, "money1"));
                                        BigDecimal months2 = new BigDecimal(getProperty(ob, "money2"));
                                        BigDecimal months3 = new BigDecimal(getProperty(ob, "money3"));
                                        BigDecimal months4 = new BigDecimal(getProperty(ob, "money4"));
                                        BigDecimal months5 = new BigDecimal(getProperty(ob, "money5"));
                                        BigDecimal months6 = new BigDecimal(getProperty(ob, "money6"));
                                        BigDecimal months7 = new BigDecimal(getProperty(ob, "money7"));
                                        BigDecimal months8 = new BigDecimal(getProperty(ob, "money8"));
                                        BigDecimal months9 = new BigDecimal(getProperty(ob, "money9"));
                                        BigDecimal months10 = new BigDecimal(getProperty(ob, "money10"));
                                        BigDecimal months11 = new BigDecimal(getProperty(ob, "money11"));
                                        BigDecimal months12 = new BigDecimal(getProperty(ob, "money12"));
                                        month1 = months1.add(month1);
                                        month2 = months2.add(month2);
                                        month3 = months3.add(month3);
                                        month4 = months4.add(month4);
                                        month5 = months5.add(month5);
                                        month6 = months6.add(month6);
                                        month7 = months7.add(month7);
                                        month8 = months8.add(month8);
                                        month9 = months9.add(month9);
                                        month10 = months10.add(month10);
                                        month11 = months11.add(month11);
                                        month12 = months12.add(month12);
                                    }
                                }
                                BigDecimal jdmount1 = month4.add(month5).add(month6);
                                BigDecimal jdmount2 = month7.add(month8).add(month9);
                                BigDecimal jdmount3 = month10.add(month11).add(month12);
                                BigDecimal jdmount4 = month1.add(month2).add(month3);
                                incomeexpenditurevo.setAmount1(String.valueOf(jdmount1));
                                incomeexpenditurevo.setAmount2(String.valueOf(jdmount2));
                                incomeexpenditurevo.setAmount3(String.valueOf(jdmount3));
                                incomeexpenditurevo.setAmount4(String.valueOf(jdmount4));
                            } else if (iteA.getCode().equals("PJ147008")) {
                                for (CostCarryForward costlist : costcarryforwardlist) {
                                    if (costlist.getRegion().substring(6, 7).equals("01")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getInwetuo());
                                        planmonth1 = planmonth1.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("02")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getInwetuo());
                                        planmonth2 = planmonth2.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("03")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getInwetuo());
                                        planmonth3 = planmonth3.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("04")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getInwetuo());
                                        planmonth4 = planmonth4.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("05")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getInwetuo());
                                        planmonth5 = planmonth5.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("06")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getInwetuo());
                                        planmonth6 = planmonth6.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("07")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getInwetuo());
                                        planmonth7 = planmonth7.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("08")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getInwetuo());
                                        planmonth8 = planmonth8.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("09")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getInwetuo());
                                        planmonth9 = planmonth9.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("10")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getInwetuo());
                                        planmonth10 = planmonth10.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("11")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getInwetuo());
                                        planmonth11 = planmonth11.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("12")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getInwetuo());
                                        planmonth12 = planmonth12.add(intotal);
                                    }
                                }
                                BigDecimal jdplanmount1 = planmonth4.add(planmonth5).add(planmonth6);
                                BigDecimal jdplanmount2 = planmonth7.add(planmonth8).add(planmonth9);
                                BigDecimal jdplanmount3 = planmonth10.add(planmonth11).add(planmonth12);
                                BigDecimal jdplanmount4 = planmonth1.add(planmonth2).add(planmonth3);
                                incomeexpenditurevo.setPlanamount1(String.valueOf(jdplanmount1));
                                incomeexpenditurevo.setPlanamount2(String.valueOf(jdplanmount2));
                                incomeexpenditurevo.setPlanamount3(String.valueOf(jdplanmount3));
                                incomeexpenditurevo.setPlanamount4(String.valueOf(jdplanmount4));
                                BigDecimal month1 = new BigDecimal("0");
                                BigDecimal month2 = new BigDecimal("0");
                                BigDecimal month3 = new BigDecimal("0");
                                BigDecimal month4 = new BigDecimal("0");
                                BigDecimal month5 = new BigDecimal("0");
                                BigDecimal month6 = new BigDecimal("0");
                                BigDecimal month7 = new BigDecimal("0");
                                BigDecimal month8 = new BigDecimal("0");
                                BigDecimal month9 = new BigDecimal("0");
                                BigDecimal month10 = new BigDecimal("0");
                                BigDecimal month11 = new BigDecimal("0");
                                BigDecimal month12 = new BigDecimal("0");
                                incomeexpenditurevo.setThemename(iteA.getValue1());
                                JSONArray jsongroupb3 = JSONArray.parseArray(businessplanlist.get(0).getGroupB3());
                                for (Object ob : jsongroupb3) {
                                    BigDecimal months1 = new BigDecimal(getProperty(ob, "money1"));
                                    BigDecimal months2 = new BigDecimal(getProperty(ob, "money2"));
                                    BigDecimal months3 = new BigDecimal(getProperty(ob, "money3"));
                                    BigDecimal months4 = new BigDecimal(getProperty(ob, "money4"));
                                    BigDecimal months5 = new BigDecimal(getProperty(ob, "money5"));
                                    BigDecimal months6 = new BigDecimal(getProperty(ob, "money6"));
                                    BigDecimal months7 = new BigDecimal(getProperty(ob, "money7"));
                                    BigDecimal months8 = new BigDecimal(getProperty(ob, "money8"));
                                    BigDecimal months9 = new BigDecimal(getProperty(ob, "money9"));
                                    BigDecimal months10 = new BigDecimal(getProperty(ob, "money10"));
                                    BigDecimal months11 = new BigDecimal(getProperty(ob, "money11"));
                                    BigDecimal months12 = new BigDecimal(getProperty(ob, "money12"));
                                    month1 = months1.add(month1);
                                    month2 = months2.add(month2);
                                    month3 = months3.add(month3);
                                    month4 = months4.add(month4);
                                    month5 = months5.add(month5);
                                    month6 = months6.add(month6);
                                    month7 = months7.add(month7);
                                    month8 = months8.add(month8);
                                    month9 = months9.add(month9);
                                    month10 = months10.add(month10);
                                    month11 = months11.add(month11);
                                    month12 = months12.add(month12);
                                }
                                BigDecimal jdmount1 = month4.add(month5).add(month6);
                                BigDecimal jdmount2 = month7.add(month8).add(month9);
                                BigDecimal jdmount3 = month10.add(month11).add(month12);
                                BigDecimal jdmount4 = month1.add(month2).add(month3);
                                incomeexpenditurevo.setAmount1(String.valueOf(jdmount1));
                                incomeexpenditurevo.setAmount2(String.valueOf(jdmount2));
                                incomeexpenditurevo.setAmount3(String.valueOf(jdmount3));
                                incomeexpenditurevo.setAmount4(String.valueOf(jdmount4));
                            } else if (iteA.getCode().equals("PJ147009")) {
                                for (CostCarryForward costlist : costcarryforwardlist) {
                                    if (costlist.getRegion().substring(6, 7).equals("01")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal()).subtract(new BigDecimal(costlist.getInwetuo())).subtract(new BigDecimal(costlist.getRent())).subtract(new BigDecimal(costlist.getCosttotal())).subtract(new BigDecimal(costlist.getPeocost())).subtract(new BigDecimal(costlist.getOutcost())).subtract(new BigDecimal(costlist.getDepreciationsoft())).subtract(new BigDecimal(costlist.getDepreciationequipment()));
                                        planmonth1 = planmonth1.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("02")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal()).subtract(new BigDecimal(costlist.getInwetuo())).subtract(new BigDecimal(costlist.getRent())).subtract(new BigDecimal(costlist.getCosttotal())).subtract(new BigDecimal(costlist.getPeocost())).subtract(new BigDecimal(costlist.getOutcost())).subtract(new BigDecimal(costlist.getDepreciationsoft())).subtract(new BigDecimal(costlist.getDepreciationequipment()));
                                        planmonth2 = planmonth2.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("03")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal()).subtract(new BigDecimal(costlist.getInwetuo())).subtract(new BigDecimal(costlist.getRent())).subtract(new BigDecimal(costlist.getCosttotal())).subtract(new BigDecimal(costlist.getPeocost())).subtract(new BigDecimal(costlist.getOutcost())).subtract(new BigDecimal(costlist.getDepreciationsoft())).subtract(new BigDecimal(costlist.getDepreciationequipment()));
                                        planmonth3 = planmonth3.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("04")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal()).subtract(new BigDecimal(costlist.getInwetuo())).subtract(new BigDecimal(costlist.getRent())).subtract(new BigDecimal(costlist.getCosttotal())).subtract(new BigDecimal(costlist.getPeocost())).subtract(new BigDecimal(costlist.getOutcost())).subtract(new BigDecimal(costlist.getDepreciationsoft())).subtract(new BigDecimal(costlist.getDepreciationequipment()));
                                        planmonth4 = planmonth4.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("05")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal()).subtract(new BigDecimal(costlist.getInwetuo())).subtract(new BigDecimal(costlist.getRent())).subtract(new BigDecimal(costlist.getCosttotal())).subtract(new BigDecimal(costlist.getPeocost())).subtract(new BigDecimal(costlist.getOutcost())).subtract(new BigDecimal(costlist.getDepreciationsoft())).subtract(new BigDecimal(costlist.getDepreciationequipment()));
                                        planmonth5 = planmonth5.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("06")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal()).subtract(new BigDecimal(costlist.getInwetuo())).subtract(new BigDecimal(costlist.getRent())).subtract(new BigDecimal(costlist.getCosttotal())).subtract(new BigDecimal(costlist.getPeocost())).subtract(new BigDecimal(costlist.getOutcost())).subtract(new BigDecimal(costlist.getDepreciationsoft())).subtract(new BigDecimal(costlist.getDepreciationequipment()));
                                        planmonth6 = planmonth6.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("07")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal()).subtract(new BigDecimal(costlist.getInwetuo())).subtract(new BigDecimal(costlist.getRent())).subtract(new BigDecimal(costlist.getCosttotal())).subtract(new BigDecimal(costlist.getPeocost())).subtract(new BigDecimal(costlist.getOutcost())).subtract(new BigDecimal(costlist.getDepreciationsoft())).subtract(new BigDecimal(costlist.getDepreciationequipment()));
                                        planmonth7 = planmonth7.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("08")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal()).subtract(new BigDecimal(costlist.getInwetuo())).subtract(new BigDecimal(costlist.getRent())).subtract(new BigDecimal(costlist.getCosttotal())).subtract(new BigDecimal(costlist.getPeocost())).subtract(new BigDecimal(costlist.getOutcost())).subtract(new BigDecimal(costlist.getDepreciationsoft())).subtract(new BigDecimal(costlist.getDepreciationequipment()));
                                        planmonth8 = planmonth8.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("09")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal()).subtract(new BigDecimal(costlist.getInwetuo())).subtract(new BigDecimal(costlist.getRent())).subtract(new BigDecimal(costlist.getCosttotal())).subtract(new BigDecimal(costlist.getPeocost())).subtract(new BigDecimal(costlist.getOutcost())).subtract(new BigDecimal(costlist.getDepreciationsoft())).subtract(new BigDecimal(costlist.getDepreciationequipment()));
                                        planmonth9 = planmonth9.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("10")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal()).subtract(new BigDecimal(costlist.getInwetuo())).subtract(new BigDecimal(costlist.getRent())).subtract(new BigDecimal(costlist.getCosttotal())).subtract(new BigDecimal(costlist.getPeocost())).subtract(new BigDecimal(costlist.getOutcost())).subtract(new BigDecimal(costlist.getDepreciationsoft())).subtract(new BigDecimal(costlist.getDepreciationequipment()));
                                        planmonth10 = planmonth10.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("11")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal()).subtract(new BigDecimal(costlist.getInwetuo())).subtract(new BigDecimal(costlist.getRent())).subtract(new BigDecimal(costlist.getCosttotal())).subtract(new BigDecimal(costlist.getPeocost())).subtract(new BigDecimal(costlist.getOutcost())).subtract(new BigDecimal(costlist.getDepreciationsoft())).subtract(new BigDecimal(costlist.getDepreciationequipment()));
                                        planmonth11 = planmonth11.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("12")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getIntotal()).subtract(new BigDecimal(costlist.getInwetuo())).subtract(new BigDecimal(costlist.getRent())).subtract(new BigDecimal(costlist.getCosttotal())).subtract(new BigDecimal(costlist.getPeocost())).subtract(new BigDecimal(costlist.getOutcost())).subtract(new BigDecimal(costlist.getDepreciationsoft())).subtract(new BigDecimal(costlist.getDepreciationequipment()));
                                        planmonth12 = planmonth12.add(intotal);
                                    }
                                }
                                BigDecimal jdplanmount1 = planmonth4.add(planmonth5).add(planmonth6);
                                BigDecimal jdplanmount2 = planmonth7.add(planmonth8).add(planmonth9);
                                BigDecimal jdplanmount3 = planmonth10.add(planmonth11).add(planmonth12);
                                BigDecimal jdplanmount4 = planmonth1.add(planmonth2).add(planmonth3);
                                incomeexpenditurevo.setPlanamount1(String.valueOf(jdplanmount1));
                                incomeexpenditurevo.setPlanamount2(String.valueOf(jdplanmount2));
                                incomeexpenditurevo.setPlanamount3(String.valueOf(jdplanmount3));
                                incomeexpenditurevo.setPlanamount4(String.valueOf(jdplanmount4));
                                BigDecimal month1 = new BigDecimal("0");
                                BigDecimal month2 = new BigDecimal("0");
                                BigDecimal month3 = new BigDecimal("0");
                                BigDecimal month4 = new BigDecimal("0");
                                BigDecimal month5 = new BigDecimal("0");
                                BigDecimal month6 = new BigDecimal("0");
                                BigDecimal month7 = new BigDecimal("0");
                                BigDecimal month8 = new BigDecimal("0");
                                BigDecimal month9 = new BigDecimal("0");
                                BigDecimal month10 = new BigDecimal("0");
                                BigDecimal month11 = new BigDecimal("0");
                                BigDecimal month12 = new BigDecimal("0");
                                incomeexpenditurevo.setThemename(iteA.getValue1());
                                JSONArray jsonbusiness = JSONArray.parseArray(businessplanlist.get(0).getBusiness());
                                JSONArray jsontableo = JSONArray.parseArray(businessplanlist.get(0).getTableO());
                                for (Object ob : jsontableo) {
                                    String type = getProperty(ob, "type");
                                    if (!type.equals("PJ111014") && !type.equals("PJ111002")) {
                                        BigDecimal months1 = new BigDecimal(getProperty(ob, "money1"));
                                        BigDecimal months2 = new BigDecimal(getProperty(ob, "money2"));
                                        BigDecimal months3 = new BigDecimal(getProperty(ob, "money3"));
                                        BigDecimal months4 = new BigDecimal(getProperty(ob, "money4"));
                                        BigDecimal months5 = new BigDecimal(getProperty(ob, "money5"));
                                        BigDecimal months6 = new BigDecimal(getProperty(ob, "money6"));
                                        BigDecimal months7 = new BigDecimal(getProperty(ob, "money7"));
                                        BigDecimal months8 = new BigDecimal(getProperty(ob, "money8"));
                                        BigDecimal months9 = new BigDecimal(getProperty(ob, "money9"));
                                        BigDecimal months10 = new BigDecimal(getProperty(ob, "money10"));
                                        BigDecimal months11 = new BigDecimal(getProperty(ob, "money11"));
                                        BigDecimal months12 = new BigDecimal(getProperty(ob, "money12"));
                                        month1 = months1.add(month1);
                                        month2 = months2.add(month2);
                                        month3 = months3.add(month3);
                                        month4 = months4.add(month4);
                                        month5 = months5.add(month5);
                                        month6 = months6.add(month6);
                                        month7 = months7.add(month7);
                                        month8 = months8.add(month8);
                                        month9 = months9.add(month9);
                                        month10 = months10.add(month10);
                                        month11 = months11.add(month11);
                                        month12 = months12.add(month12);
                                    }
                                }
                                for (Object ob : jsonbusiness) {
                                    BigDecimal months1 = new BigDecimal(getProperty(ob, "money1"));
                                    BigDecimal months2 = new BigDecimal(getProperty(ob, "money2"));
                                    BigDecimal months3 = new BigDecimal(getProperty(ob, "money3"));
                                    BigDecimal months4 = new BigDecimal(getProperty(ob, "money4"));
                                    BigDecimal months5 = new BigDecimal(getProperty(ob, "money5"));
                                    BigDecimal months6 = new BigDecimal(getProperty(ob, "money6"));
                                    BigDecimal months7 = new BigDecimal(getProperty(ob, "money7"));
                                    BigDecimal months8 = new BigDecimal(getProperty(ob, "money8"));
                                    BigDecimal months9 = new BigDecimal(getProperty(ob, "money9"));
                                    BigDecimal months10 = new BigDecimal(getProperty(ob, "money10"));
                                    BigDecimal months11 = new BigDecimal(getProperty(ob, "money11"));
                                    BigDecimal months12 = new BigDecimal(getProperty(ob, "money12"));
                                    month1 = months1.add(month1);
                                    month2 = months2.add(month2);
                                    month3 = months3.add(month3);
                                    month4 = months4.add(month4);
                                    month5 = months5.add(month5);
                                    month6 = months6.add(month6);
                                    month7 = months7.add(month7);
                                    month8 = months8.add(month8);
                                    month9 = months9.add(month9);
                                    month10 = months10.add(month10);
                                    month11 = months11.add(month11);
                                    month12 = months12.add(month12);
                                }
                                BigDecimal jdmount1 = month4.add(month5).add(month6);
                                BigDecimal jdmount2 = month7.add(month8).add(month9);
                                BigDecimal jdmount3 = month10.add(month11).add(month12);
                                BigDecimal jdmount4 = month1.add(month2).add(month3);
                                incomeexpenditurevo.setAmount1(String.valueOf(jdmount1));
                                incomeexpenditurevo.setAmount2(String.valueOf(jdmount2));
                                incomeexpenditurevo.setAmount3(String.valueOf(jdmount3));
                                incomeexpenditurevo.setAmount4(String.valueOf(jdmount4));
                            } else if (iteA.getCode().equals("PJ147010")) {
                                for (CostCarryForward costlist : costcarryforwardlist) {
                                    if (costlist.getRegion().substring(6, 7).equals("01")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOtherexpenses());
                                        planmonth1 = planmonth1.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("02")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOtherexpenses());
                                        planmonth2 = planmonth2.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("03")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOtherexpenses());
                                        planmonth3 = planmonth3.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("04")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOtherexpenses());
                                        planmonth4 = planmonth4.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("05")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOtherexpenses());
                                        planmonth5 = planmonth5.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("06")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOtherexpenses());
                                        planmonth6 = planmonth6.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("07")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOtherexpenses());
                                        planmonth7 = planmonth7.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("08")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOtherexpenses());
                                        planmonth8 = planmonth8.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("09")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOtherexpenses());
                                        planmonth9 = planmonth9.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("10")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOtherexpenses());
                                        planmonth10 = planmonth10.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("11")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOtherexpenses());
                                        planmonth11 = planmonth11.add(intotal);
                                    } else if (costlist.getRegion().substring(6, 7).equals("12")) {
                                        BigDecimal intotal = new BigDecimal(costlist.getOtherexpenses());
                                        planmonth12 = planmonth12.add(intotal);
                                    }
                                }
                                BigDecimal jdplanmount1 = planmonth4.add(planmonth5).add(planmonth6);
                                BigDecimal jdplanmount2 = planmonth7.add(planmonth8).add(planmonth9);
                                BigDecimal jdplanmount3 = planmonth10.add(planmonth11).add(planmonth12);
                                BigDecimal jdplanmount4 = planmonth1.add(planmonth2).add(planmonth3);
                                incomeexpenditurevo.setPlanamount1(String.valueOf(jdplanmount1));
                                incomeexpenditurevo.setPlanamount2(String.valueOf(jdplanmount2));
                                incomeexpenditurevo.setPlanamount3(String.valueOf(jdplanmount3));
                                incomeexpenditurevo.setPlanamount4(String.valueOf(jdplanmount4));
                                BigDecimal month1 = new BigDecimal("0");
                                BigDecimal month2 = new BigDecimal("0");
                                BigDecimal month3 = new BigDecimal("0");
                                BigDecimal month4 = new BigDecimal("0");
                                BigDecimal month5 = new BigDecimal("0");
                                BigDecimal month6 = new BigDecimal("0");
                                BigDecimal month7 = new BigDecimal("0");
                                BigDecimal month8 = new BigDecimal("0");
                                BigDecimal month9 = new BigDecimal("0");
                                BigDecimal month10 = new BigDecimal("0");
                                BigDecimal month11 = new BigDecimal("0");
                                BigDecimal month12 = new BigDecimal("0");
                                incomeexpenditurevo.setThemename(iteA.getValue1());
                                JSONArray jsontableo = JSONArray.parseArray(businessplanlist.get(0).getTableO());
                                for (Object ob : jsontableo) {
                                    String type = getProperty(ob, "type");
                                    if (type.equals("PJ111014")) {
                                        BigDecimal months1 = new BigDecimal(getProperty(ob, "money1"));
                                        BigDecimal months2 = new BigDecimal(getProperty(ob, "money2"));
                                        BigDecimal months3 = new BigDecimal(getProperty(ob, "money3"));
                                        BigDecimal months4 = new BigDecimal(getProperty(ob, "money4"));
                                        BigDecimal months5 = new BigDecimal(getProperty(ob, "money5"));
                                        BigDecimal months6 = new BigDecimal(getProperty(ob, "money6"));
                                        BigDecimal months7 = new BigDecimal(getProperty(ob, "money7"));
                                        BigDecimal months8 = new BigDecimal(getProperty(ob, "money8"));
                                        BigDecimal months9 = new BigDecimal(getProperty(ob, "money9"));
                                        BigDecimal months10 = new BigDecimal(getProperty(ob, "money10"));
                                        BigDecimal months11 = new BigDecimal(getProperty(ob, "money11"));
                                        BigDecimal months12 = new BigDecimal(getProperty(ob, "money12"));
                                        month1 = months1.add(month1);
                                        month2 = months2.add(month2);
                                        month3 = months3.add(month3);
                                        month4 = months4.add(month4);
                                        month5 = months5.add(month5);
                                        month6 = months6.add(month6);
                                        month7 = months7.add(month7);
                                        month8 = months8.add(month8);
                                        month9 = months9.add(month9);
                                        month10 = months10.add(month10);
                                        month11 = months11.add(month11);
                                        month12 = months12.add(month12);
                                    }
                                }
                                BigDecimal jdmount1 = month4.add(month5).add(month6);
                                BigDecimal jdmount2 = month7.add(month8).add(month9);
                                BigDecimal jdmount3 = month10.add(month11).add(month12);
                                BigDecimal jdmount4 = month1.add(month2).add(month3);
                                incomeexpenditurevo.setAmount1(String.valueOf(jdmount1));
                                incomeexpenditurevo.setAmount2(String.valueOf(jdmount2));
                                incomeexpenditurevo.setAmount3(String.valueOf(jdmount3));
                                incomeexpenditurevo.setAmount4(String.valueOf(jdmount4));
                            } else if (iteA.getCode().equals("PJ147011")) {
                                for (ProjectIncome listproject : projectincomelist) {
                                    if (listproject.getMonth().substring(6, 7).equals("01")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("1")) {
                                                planmonth1.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("02")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("1")) {
                                                planmonth2.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("03")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("1")) {
                                                planmonth3.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("04")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("1")) {
                                                planmonth4.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("05")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("1")) {
                                                planmonth5.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("06")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("1")) {
                                                planmonth6.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("07")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("1")) {
                                                planmonth7.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("08")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("1")) {
                                                planmonth8.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("09")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("1")) {
                                                planmonth9.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("10")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("1")) {
                                                planmonth10.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("11")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("1")) {
                                                planmonth11.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("12")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("1")) {
                                                planmonth12.add(new BigDecimal("1"));
                                            }
                                        }
                                    }
                                }
                                BigDecimal jdplanmount1 = planmonth4.add(planmonth5).add(planmonth6);
                                BigDecimal jdplanmount2 = planmonth7.add(planmonth8).add(planmonth9);
                                BigDecimal jdplanmount3 = planmonth10.add(planmonth11).add(planmonth12);
                                BigDecimal jdplanmount4 = planmonth1.add(planmonth2).add(planmonth3);
                                incomeexpenditurevo.setPlanamount1(String.valueOf(jdplanmount1));
                                incomeexpenditurevo.setPlanamount2(String.valueOf(jdplanmount2));
                                incomeexpenditurevo.setPlanamount3(String.valueOf(jdplanmount3));
                                incomeexpenditurevo.setPlanamount4(String.valueOf(jdplanmount4));
                                incomeexpenditurevo.setThemename(iteA.getValue1());
                                BigDecimal jdmount1 = personnel4.add(personnel5).add(personnel6);
                                BigDecimal jdmount2 = personnel7.add(personnel8).add(personnel9);
                                BigDecimal jdmount3 = personnel10.add(personnel11).add(personnel12);
                                BigDecimal jdmount4 = personnel1.add(personnel2).add(personnel3);
                                incomeexpenditurevo.setAmount1(String.valueOf(jdmount1));
                                incomeexpenditurevo.setAmount2(String.valueOf(jdmount2));
                                incomeexpenditurevo.setAmount3(String.valueOf(jdmount3));
                                incomeexpenditurevo.setAmount4(String.valueOf(jdmount4));
                            } else if (iteA.getCode().equals("PJ147012")) {
                                BigDecimal money1 = new BigDecimal("0");
                                BigDecimal money2 = new BigDecimal("0");
                                BigDecimal money3 = new BigDecimal("0");
                                BigDecimal money4 = new BigDecimal("0");
                                BigDecimal money5 = new BigDecimal("0");
                                BigDecimal money6 = new BigDecimal("0");
                                BigDecimal money7 = new BigDecimal("0");
                                BigDecimal money8 = new BigDecimal("0");
                                BigDecimal money9 = new BigDecimal("0");
                                BigDecimal money10 = new BigDecimal("0");
                                BigDecimal money11 = new BigDecimal("0");
                                BigDecimal money12 = new BigDecimal("0");
                                for (ProjectIncome listproject : projectincomelist) {
                                    if (listproject.getMonth().substring(6, 7).equals("01")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money1.add(new BigDecimal(money));
                                            if (type.equals("1")) {
                                                planmonth1.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("02")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money2.add(new BigDecimal(money));
                                            if (type.equals("1")) {
                                                planmonth2.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("03")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money3.add(new BigDecimal(money));
                                            if (type.equals("1")) {
                                                planmonth3.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("04")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money4.add(new BigDecimal(money));
                                            if (type.equals("1")) {
                                                planmonth4.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("05")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money5.add(new BigDecimal(money));
                                            if (type.equals("1")) {
                                                planmonth5.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("06")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money6.add(new BigDecimal(money));
                                            if (type.equals("1")) {
                                                planmonth6.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("07")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money7.add(new BigDecimal(money));
                                            if (type.equals("1")) {
                                                planmonth7.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("08")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money8.add(new BigDecimal(money));
                                            if (type.equals("1")) {
                                                planmonth8.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("09")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money9.add(new BigDecimal(money));
                                            if (type.equals("1")) {
                                                planmonth9.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("10")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money10.add(new BigDecimal(money));
                                            if (type.equals("1")) {
                                                planmonth10.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("11")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money11.add(new BigDecimal(money));
                                            if (type.equals("1")) {
                                                planmonth11.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("12")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money12.add(new BigDecimal(money));
                                            if (type.equals("1")) {
                                                planmonth12.add(new BigDecimal("1"));
                                            }
                                        }
                                    }
                                }
                                BigDecimal jdplanmount1 = money4.divide(planmonth4, scale, roundingMode).add(money5.divide(planmonth5, scale, roundingMode)).add(money6.divide(planmonth6, scale, roundingMode));
                                BigDecimal jdplanmount2 = money7.divide(planmonth7, scale, roundingMode).add(money8.divide(planmonth8, scale, roundingMode)).add(money9.divide(planmonth9, scale, roundingMode));
                                BigDecimal jdplanmount3 = money10.divide(planmonth10, scale, roundingMode).add(money11.divide(planmonth11, scale, roundingMode)).add(money12.divide(planmonth12, scale, roundingMode));
                                BigDecimal jdplanmount4 = money1.divide(planmonth1, scale, roundingMode).add(money2.divide(planmonth2, scale, roundingMode)).add(money3.divide(planmonth3, scale, roundingMode));
                                incomeexpenditurevo.setPlanamount1(String.valueOf(jdplanmount1));
                                incomeexpenditurevo.setPlanamount2(String.valueOf(jdplanmount2));
                                incomeexpenditurevo.setPlanamount3(String.valueOf(jdplanmount3));
                                incomeexpenditurevo.setPlanamount4(String.valueOf(jdplanmount4));
                                incomeexpenditurevo.setThemename(iteA.getValue1());
                                for (PersonnelPlan list : personnelplanlist) {
                                    if (list.getType().equals("0")) {
                                        BigDecimal moneyavg = new BigDecimal(list.getMoneyavg());
                                        BigDecimal jdmount1 = moneyavg.add(moneyavg).add(moneyavg);
                                        BigDecimal jdmount2 = moneyavg.add(moneyavg).add(moneyavg);
                                        BigDecimal jdmount3 = moneyavg.add(moneyavg).add(moneyavg);
                                        BigDecimal jdmount4 = moneyavg.add(moneyavg).add(moneyavg);
                                        incomeexpenditurevo.setAmount1(String.valueOf(jdmount1));
                                        incomeexpenditurevo.setAmount2(String.valueOf(jdmount2));
                                        incomeexpenditurevo.setAmount3(String.valueOf(jdmount3));
                                        incomeexpenditurevo.setAmount4(String.valueOf(jdmount4));
                                    }
                                }
                            } else if (iteA.getCode().equals("PJ147013")) {
                                for (ProjectIncome listproject : projectincomelist) {
                                    if (listproject.getMonth().substring(6, 7).equals("01")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("0")) {
                                                planmonth1.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("02")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("0")) {
                                                planmonth2.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("03")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("0")) {
                                                planmonth3.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("04")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("0")) {
                                                planmonth4.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("05")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("0")) {
                                                planmonth5.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("06")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("0")) {
                                                planmonth6.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("07")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("0")) {
                                                planmonth7.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("08")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("0")) {
                                                planmonth8.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("09")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("0")) {
                                                planmonth9.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("10")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("0")) {
                                                planmonth10.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("11")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("0")) {
                                                planmonth11.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("12")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            if (type.equals("0")) {
                                                planmonth12.add(new BigDecimal("1"));
                                            }
                                        }
                                    }
                                }
                                BigDecimal jdplanmount1 = planmonth4.add(planmonth5).add(planmonth6);
                                BigDecimal jdplanmount2 = planmonth7.add(planmonth8).add(planmonth9);
                                BigDecimal jdplanmount3 = planmonth10.add(planmonth11).add(planmonth12);
                                BigDecimal jdplanmount4 = planmonth1.add(planmonth2).add(planmonth3);
                                incomeexpenditurevo.setPlanamount1(String.valueOf(jdplanmount1));
                                incomeexpenditurevo.setPlanamount2(String.valueOf(jdplanmount2));
                                incomeexpenditurevo.setPlanamount3(String.valueOf(jdplanmount3));
                                incomeexpenditurevo.setPlanamount4(String.valueOf(jdplanmount4));
                                incomeexpenditurevo.setThemename(iteA.getValue1());
                                BigDecimal jdmount1 = wpersonnel4.add(wpersonnel5).add(wpersonnel6);
                                BigDecimal jdmount2 = wpersonnel7.add(wpersonnel8).add(wpersonnel9);
                                BigDecimal jdmount3 = wpersonnel10.add(wpersonnel11).add(wpersonnel12);
                                BigDecimal jdmount4 = wpersonnel1.add(wpersonnel2).add(wpersonnel3);
                                incomeexpenditurevo.setAmount1(String.valueOf(jdmount1));
                                incomeexpenditurevo.setAmount2(String.valueOf(jdmount2));
                                incomeexpenditurevo.setAmount3(String.valueOf(jdmount3));
                                incomeexpenditurevo.setAmount4(String.valueOf(jdmount4));
                            } else if (iteA.getCode().equals("PJ147014")) {
                                BigDecimal money1 = new BigDecimal("0");
                                BigDecimal money2 = new BigDecimal("0");
                                BigDecimal money3 = new BigDecimal("0");
                                BigDecimal money4 = new BigDecimal("0");
                                BigDecimal money5 = new BigDecimal("0");
                                BigDecimal money6 = new BigDecimal("0");
                                BigDecimal money7 = new BigDecimal("0");
                                BigDecimal money8 = new BigDecimal("0");
                                BigDecimal money9 = new BigDecimal("0");
                                BigDecimal money10 = new BigDecimal("0");
                                BigDecimal money11 = new BigDecimal("0");
                                BigDecimal money12 = new BigDecimal("0");
                                for (ProjectIncome listproject : projectincomelist) {
                                    if (listproject.getMonth().substring(6, 7).equals("01")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money1.add(new BigDecimal(money));
                                            if (type.equals("0")) {
                                                planmonth1.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("02")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money2.add(new BigDecimal(money));
                                            if (type.equals("0")) {
                                                planmonth2.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("03")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money3.add(new BigDecimal(money));
                                            if (type.equals("0")) {
                                                planmonth3.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("04")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money4.add(new BigDecimal(money));
                                            if (type.equals("0")) {
                                                planmonth4.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("05")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money5.add(new BigDecimal(money));
                                            if (type.equals("0")) {
                                                planmonth5.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("06")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money6.add(new BigDecimal(money));
                                            if (type.equals("0")) {
                                                planmonth6.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("07")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money7.add(new BigDecimal(money));
                                            if (type.equals("0")) {
                                                planmonth7.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("08")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money8.add(new BigDecimal(money));
                                            if (type.equals("0")) {
                                                planmonth8.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("09")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money9.add(new BigDecimal(money));
                                            if (type.equals("0")) {
                                                planmonth9.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("10")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money10.add(new BigDecimal(money));
                                            if (type.equals("0")) {
                                                planmonth10.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("11")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money11.add(new BigDecimal(money));
                                            if (type.equals("0")) {
                                                planmonth11.add(new BigDecimal("1"));
                                            }
                                        }
                                    } else if (listproject.getMonth().substring(6, 7).equals("12")) {
                                        JSONArray jsonArray = JSONArray.parseArray(listproject.getProjectincomevo2());
                                        for (Object o : jsonArray) {
                                            String type = getProperty(o, "type");
                                            String money = getProperty(o, "money");
                                            if (money == null) {
                                                money = "0";
                                            }
                                            money12.add(new BigDecimal(money));
                                            if (type.equals("0")) {
                                                planmonth12.add(new BigDecimal("1"));
                                            }
                                        }
                                    }
                                }
                                BigDecimal jdplanmount1 = money4.divide(planmonth4, scale, roundingMode).add(money5.divide(planmonth5, scale, roundingMode)).add(money6.divide(planmonth6, scale, roundingMode));
                                BigDecimal jdplanmount2 = money7.divide(planmonth7, scale, roundingMode).add(money8.divide(planmonth8, scale, roundingMode)).add(money9.divide(planmonth9, scale, roundingMode));
                                BigDecimal jdplanmount3 = money10.divide(planmonth10, scale, roundingMode).add(money11.divide(planmonth11, scale, roundingMode)).add(money12.divide(planmonth12, scale, roundingMode));
                                BigDecimal jdplanmount4 = money1.divide(planmonth1, scale, roundingMode).add(money2.divide(planmonth2, scale, roundingMode)).add(money3.divide(planmonth3, scale, roundingMode));
                                incomeexpenditurevo.setPlanamount1(String.valueOf(jdplanmount1));
                                incomeexpenditurevo.setPlanamount2(String.valueOf(jdplanmount2));
                                incomeexpenditurevo.setPlanamount3(String.valueOf(jdplanmount3));
                                incomeexpenditurevo.setPlanamount4(String.valueOf(jdplanmount4));
                                incomeexpenditurevo.setThemename(iteA.getValue1());
                                for (PersonnelPlan list : personnelplanlist) {
                                    if (list.getType().equals("0")) {
                                        BigDecimal moneyavg = new BigDecimal(list.getMoneyavg());
                                        BigDecimal jdmount1 = moneyavg.add(moneyavg).add(moneyavg);
                                        BigDecimal jdmount2 = moneyavg.add(moneyavg).add(moneyavg);
                                        BigDecimal jdmount3 = moneyavg.add(moneyavg).add(moneyavg);
                                        BigDecimal jdmount4 = moneyavg.add(moneyavg).add(moneyavg);
                                        incomeexpenditurevo.setAmount1(String.valueOf(jdmount1));
                                        incomeexpenditurevo.setAmount2(String.valueOf(jdmount2));
                                        incomeexpenditurevo.setAmount3(String.valueOf(jdmount3));
                                        incomeexpenditurevo.setAmount4(String.valueOf(jdmount4));
                                    }
                                }
                            }
                            incomeexpenditurevolist.add(incomeexpenditurevo);
                        }
                        BigDecimal rmonth1 = new BigDecimal("0");
                        BigDecimal rmonth2 = new BigDecimal("0");
                        BigDecimal rmonth3 = new BigDecimal("0");
                        BigDecimal rmonth4 = new BigDecimal("0");
                        BigDecimal wmonth1 = new BigDecimal("0");
                        BigDecimal wmonth2 = new BigDecimal("0");
                        BigDecimal wmonth3 = new BigDecimal("0");
                        BigDecimal wmonth4 = new BigDecimal("0");
                        BigDecimal smonth1 = new BigDecimal("0");
                        BigDecimal smonth2 = new BigDecimal("0");
                        BigDecimal smonth3 = new BigDecimal("0");
                        BigDecimal smonth4 = new BigDecimal("0");
                        BigDecimal zmonth1 = new BigDecimal("0");
                        BigDecimal zmonth2 = new BigDecimal("0");
                        BigDecimal zmonth3 = new BigDecimal("0");
                        BigDecimal zmonth4 = new BigDecimal("0");
                        BigDecimal rmonth1s = new BigDecimal("0");
                        BigDecimal rmonth2s = new BigDecimal("0");
                        BigDecimal rmonth3s = new BigDecimal("0");
                        BigDecimal rmonth4s = new BigDecimal("0");
                        BigDecimal wmonth1s = new BigDecimal("0");
                        BigDecimal wmonth2s = new BigDecimal("0");
                        BigDecimal wmonth3s = new BigDecimal("0");
                        BigDecimal wmonth4s = new BigDecimal("0");
                        BigDecimal smonth1s = new BigDecimal("0");
                        BigDecimal smonth2s = new BigDecimal("0");
                        BigDecimal smonth3s = new BigDecimal("0");
                        BigDecimal smonth4s = new BigDecimal("0");
                        BigDecimal zmonth1s = new BigDecimal("0");
                        BigDecimal zmonth2s = new BigDecimal("0");
                        BigDecimal zmonth3s = new BigDecimal("0");
                        BigDecimal zmonth4s = new BigDecimal("0");
                        for (IncomeExpenditureVo list : incomeexpenditurevolist) {
                            if (list.getThemename().equals("收入")) {
                                smonth1 = new BigDecimal(list.getAmount1());
                                smonth2 = new BigDecimal(list.getAmount2());
                                smonth3 = new BigDecimal(list.getAmount3());
                                smonth4 = new BigDecimal(list.getAmount4());
                                smonth1s = new BigDecimal(list.getPlanamount1());
                                smonth2s = new BigDecimal(list.getPlanamount2());
                                smonth3s = new BigDecimal(list.getPlanamount3());
                                smonth4s = new BigDecimal(list.getPlanamount4());
                            } else if (list.getThemename().equals("人件费")) {
                                rmonth1 = new BigDecimal(list.getAmount1());
                                rmonth2 = new BigDecimal(list.getAmount2());
                                rmonth3 = new BigDecimal(list.getAmount3());
                                rmonth4 = new BigDecimal(list.getAmount4());
                                rmonth1s = new BigDecimal(list.getPlanamount1());
                                rmonth2s = new BigDecimal(list.getPlanamount2());
                                rmonth3s = new BigDecimal(list.getPlanamount3());
                                rmonth4s = new BigDecimal(list.getPlanamount4());
                            } else if (list.getThemename().equals("外注费")) {
                                wmonth1 = new BigDecimal(list.getAmount1());
                                wmonth2 = new BigDecimal(list.getAmount2());
                                wmonth3 = new BigDecimal(list.getAmount3());
                                wmonth4 = new BigDecimal(list.getAmount4());
                                wmonth1s = new BigDecimal(list.getPlanamount1());
                                wmonth2s = new BigDecimal(list.getPlanamount2());
                                wmonth3s = new BigDecimal(list.getPlanamount3());
                                wmonth4s = new BigDecimal(list.getPlanamount4());
                            } else if (list.getThemename().equals("支出")) {
                                zmonth1 = new BigDecimal(list.getAmount1());
                                zmonth2 = new BigDecimal(list.getAmount2());
                                zmonth3 = new BigDecimal(list.getAmount3());
                                zmonth4 = new BigDecimal(list.getAmount4());
                                zmonth1s = new BigDecimal(list.getPlanamount1());
                                zmonth2s = new BigDecimal(list.getPlanamount2());
                                zmonth3s = new BigDecimal(list.getPlanamount3());
                                zmonth4s = new BigDecimal(list.getPlanamount4());
                            }
                        }
                        for (Dictionary iteA : curListA) {
                            IncomeExpenditureVo incomeexpenditurevo = new IncomeExpenditureVo();
                            incomeexpenditurevo.setCenter_id("大连松下");
                            incomeexpenditurevo.setGroup_id(org1.getCompanyen());
                            if (iteA.getCode().equals("PJ1470015")) {
                                incomeexpenditurevo.setAmount1(String.valueOf(smonth1.subtract(rmonth1).subtract(wmonth1)));
                                incomeexpenditurevo.setAmount2(String.valueOf(smonth2.subtract(rmonth2).subtract(wmonth2)));
                                incomeexpenditurevo.setAmount3(String.valueOf(smonth3.subtract(rmonth3).subtract(wmonth3)));
                                incomeexpenditurevo.setAmount4(String.valueOf(smonth4.subtract(rmonth4).subtract(wmonth4)));
                                incomeexpenditurevo.setPlanamount1(String.valueOf(smonth1s.subtract(rmonth1s).subtract(wmonth1s)));
                                incomeexpenditurevo.setPlanamount2(String.valueOf(smonth2s.subtract(rmonth2s).subtract(wmonth2s)));
                                incomeexpenditurevo.setPlanamount3(String.valueOf(smonth3s.subtract(rmonth3s).subtract(wmonth3s)));
                                incomeexpenditurevo.setPlanamount4(String.valueOf(smonth4s.subtract(rmonth4s).subtract(wmonth4s)));
                                incomeexpenditurevo.setThemename(iteA.getValue1());
                            } else if (iteA.getCode().equals("PJ1470016")) {
                                incomeexpenditurevo.setAmount1(String.valueOf(smonth1.subtract(rmonth1).subtract(wmonth1).divide(smonth1, scale, roundingMode)));
                                incomeexpenditurevo.setAmount2(String.valueOf(smonth2.subtract(rmonth2).subtract(wmonth2).divide(smonth2, scale, roundingMode)));
                                incomeexpenditurevo.setAmount3(String.valueOf(smonth3.subtract(rmonth3).subtract(wmonth3).divide(smonth3, scale, roundingMode)));
                                incomeexpenditurevo.setAmount4(String.valueOf(smonth4.subtract(rmonth4).subtract(wmonth4).divide(smonth4, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount1(String.valueOf(smonth1s.subtract(rmonth1s).subtract(wmonth1s).divide(smonth1s, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount2(String.valueOf(smonth2s.subtract(rmonth2s).subtract(wmonth2s).divide(smonth2s, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount3(String.valueOf(smonth3s.subtract(rmonth3s).subtract(wmonth3s).divide(smonth3s, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount4(String.valueOf(smonth4s.subtract(rmonth4s).subtract(wmonth4s).divide(smonth4s, scale, roundingMode)));
                                incomeexpenditurevo.setThemename(iteA.getValue1());
                            } else if (iteA.getCode().equals("PJ1470017")) {
                                incomeexpenditurevo.setAmount1(String.valueOf(smonth1.subtract(zmonth1)));
                                incomeexpenditurevo.setAmount2(String.valueOf(smonth2.subtract(zmonth2)));
                                incomeexpenditurevo.setAmount3(String.valueOf(smonth3.subtract(zmonth3)));
                                incomeexpenditurevo.setAmount4(String.valueOf(smonth4.subtract(zmonth4)));
                                incomeexpenditurevo.setPlanamount1(String.valueOf(smonth1s.subtract(zmonth1s)));
                                incomeexpenditurevo.setPlanamount2(String.valueOf(smonth2s.subtract(zmonth2s)));
                                incomeexpenditurevo.setPlanamount3(String.valueOf(smonth3s.subtract(zmonth3s)));
                                incomeexpenditurevo.setPlanamount4(String.valueOf(smonth4s.subtract(zmonth4s)));
                                incomeexpenditurevo.setThemename(iteA.getValue1());
                            } else if (iteA.getCode().equals("PJ1470018")) {
                                incomeexpenditurevo.setAmount1(String.valueOf(smonth1.subtract(zmonth1).divide(smonth1, scale, roundingMode)));
                                incomeexpenditurevo.setAmount2(String.valueOf(smonth2.subtract(zmonth2).divide(smonth2, scale, roundingMode)));
                                incomeexpenditurevo.setAmount3(String.valueOf(smonth3.subtract(zmonth3).divide(smonth3, scale, roundingMode)));
                                incomeexpenditurevo.setAmount4(String.valueOf(smonth4.subtract(zmonth4).divide(smonth4, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount1(String.valueOf(smonth1s.subtract(zmonth1s).divide(smonth1s, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount2(String.valueOf(smonth2s.subtract(zmonth2s).divide(smonth2s, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount3(String.valueOf(smonth3s.subtract(zmonth3s).divide(smonth3s, scale, roundingMode)));
                                incomeexpenditurevo.setPlanamount4(String.valueOf(smonth4s.subtract(zmonth4s).divide(smonth4s, scale, roundingMode)));
                                incomeexpenditurevo.setThemename(iteA.getValue1());
                            }
                            incomeexpenditurevolist.add(incomeexpenditurevo);
                        }
                    }
                }
            }
        }
        return incomeexpenditurevolist;
    }

    //汇率定时任务
    @Scheduled(cron = "0 10 0 1 4 ?")
    public void getThemeDetatiList() throws Exception {
        SimpleDateFormat s = new SimpleDateFormat("MM");
        SimpleDateFormat s1 = new SimpleDateFormat("YYYY");
        OrgTree orgs = orgTreeService.get(new OrgTree());
        for (OrgTree org : orgs.getOrgs()) {
            for (OrgTree org1 : org.getOrgs()) {
                int year = Integer.valueOf(s.format(new Date())) >= 4 ? Integer.valueOf(s1.format(new Date())) + 1 : Integer.valueOf(s1.format(new Date()));
                ThemePlanDetail themePlanDetail = new ThemePlanDetail();
                themePlanDetail.setYear(String.valueOf(year));
                themePlanDetail.setGroup_id(org1.get_id());
                List<ThemePlanDetail> themeplandetail = themePlanDetailMapper.select(themePlanDetail);
                if (themeplandetail.size() > 0) {
                    for (ThemePlanDetail list : themeplandetail) {
                        IncomeExpenditure incomeexpenditures = new IncomeExpenditure();
                        String theme = list.getThemename();
                        for (int i = 1; i <= 12; i++) {
                            String month = i <= 9 ? "0" + i : String.valueOf(i);
                            StringBuilder sb = new StringBuilder();
                            sb.append(year).append("-").append(month);
                            ProjectIncome projectincome = new ProjectIncome();
                            projectincome.setYear(String.valueOf(year));
                            projectincome.setGroup_id(org1.get_id());
                            projectincome.setMonth(String.valueOf(sb));
                            List<ProjectIncome> projectincomelist = projectincomemapper.select(projectincome);
                            BigDecimal amount = new BigDecimal("0");
                            if (projectincomelist.size() > 0) {
                                JSONArray jsonArray = JSONArray.parseArray(projectincomelist.get(0).getProjectincomevo1());
                                for (Object ob : jsonArray) {
                                    String projectincomevo1theme = getProperty(ob, "theme");
                                    String projectincomevo1contractamount = getProperty(ob, "contractamount");
                                    if (projectincomevo1theme.equals(theme)) {
                                        BigDecimal contractamount = new BigDecimal(projectincomevo1contractamount);
                                        amount = amount.add(contractamount);
                                    }
                                }
                                List<ProjectContract> projectcontractlist = incomeexpendituremapper.getprojectcontractlist(theme, String.valueOf(year), month);
                                if (i == 1) {
                                    BigDecimal planamount = new BigDecimal(projectcontractlist.get(0).getContractamount()).add(amount);
                                    incomeexpenditures.setPlanamount1(String.valueOf(planamount));
                                } else if (i == 2) {
                                    BigDecimal planamount = new BigDecimal(projectcontractlist.get(0).getContractamount()).add(amount);
                                    incomeexpenditures.setPlanamount2(String.valueOf(planamount));
                                } else if (i == 3) {
                                    BigDecimal planamount = new BigDecimal(projectcontractlist.get(0).getContractamount()).add(amount);
                                    incomeexpenditures.setPlanamount3(String.valueOf(planamount));
                                } else if (i == 4) {
                                    BigDecimal planamount = new BigDecimal(projectcontractlist.get(0).getContractamount()).add(amount);
                                    incomeexpenditures.setPlanamount4(String.valueOf(planamount));
                                } else if (i == 5) {
                                    BigDecimal planamount = new BigDecimal(projectcontractlist.get(0).getContractamount()).add(amount);
                                    incomeexpenditures.setPlanamount5(String.valueOf(planamount));
                                } else if (i == 6) {
                                    BigDecimal planamount = new BigDecimal(projectcontractlist.get(0).getContractamount()).add(amount);
                                    incomeexpenditures.setPlanamount6(String.valueOf(planamount));
                                } else if (i == 7) {
                                    BigDecimal planamount = new BigDecimal(projectcontractlist.get(0).getContractamount()).add(amount);
                                    incomeexpenditures.setPlanamount7(String.valueOf(planamount));
                                } else if (i == 8) {
                                    BigDecimal planamount = new BigDecimal(projectcontractlist.get(0).getContractamount()).add(amount);
                                    incomeexpenditures.setPlanamount8(String.valueOf(planamount));
                                } else if (i == 9) {
                                    BigDecimal planamount = new BigDecimal(projectcontractlist.get(0).getContractamount()).add(amount);
                                    incomeexpenditures.setPlanamount9(String.valueOf(planamount));
                                } else if (i == 10) {
                                    BigDecimal planamount = new BigDecimal(projectcontractlist.get(0).getContractamount()).add(amount);
                                    incomeexpenditures.setPlanamount10(String.valueOf(planamount));
                                } else if (i == 11) {
                                    BigDecimal planamount = new BigDecimal(projectcontractlist.get(0).getContractamount()).add(amount);
                                    incomeexpenditures.setPlanamount11(String.valueOf(planamount));
                                } else if (i == 12) {
                                    BigDecimal planamount = new BigDecimal(projectcontractlist.get(0).getContractamount()).add(amount);
                                    incomeexpenditures.setPlanamount12(String.valueOf(planamount));
                                }
                            }
                        }
                        incomeexpenditures.setIncomeexpenditure_id(UUID.randomUUID().toString());
                        incomeexpenditures.setAmount1(list.getAmount1());
                        incomeexpenditures.setAmount2(list.getAmount2());
                        incomeexpenditures.setAmount3(list.getAmount3());
                        incomeexpenditures.setAmount4(list.getAmount4());
                        incomeexpenditures.setAmount5(list.getAmount5());
                        incomeexpenditures.setAmount6(list.getAmount6());
                        incomeexpenditures.setAmount7(list.getAmount7());
                        incomeexpenditures.setAmount8(list.getAmount8());
                        incomeexpenditures.setAmount9(list.getAmount9());
                        incomeexpenditures.setAmount10(list.getAmount10());
                        incomeexpenditures.setAmount11(list.getAmount11());
                        incomeexpenditures.setAmount12(list.getAmount12());
                        incomeexpenditures.setGroup_id(list.getGroup_id());
                        incomeexpenditures.setYear(list.getYear());
                        incomeexpenditures.setBranch(list.getBranch());
                        incomeexpenditures.setCenter_id(list.getCenter_id());
                        incomeexpenditures.setCurrencytype(list.getCurrencytype());
                        incomeexpenditures.setKind(list.getKind());
                        incomeexpenditures.setThemename(list.getThemename());
                        incomeexpendituremapper.insert(incomeexpenditures);
                    }
                }
            }
        }
    }

    @Override
    public List<IncomeExpenditure> selectlist(String year, String group_id) throws Exception {
        List<IncomeExpenditure> onlylist = new ArrayList<>();
        IncomeExpenditure incomeexpenditure = new IncomeExpenditure();
        incomeexpenditure.setYear(year);
        incomeexpenditure.setGroup_id(group_id);
        List<IncomeExpenditure> incomeexpenditurelist = incomeexpendituremapper.select(incomeexpenditure);
        if (incomeexpenditurelist.size() > 0) {
            onlylist.addAll(incomeexpenditurelist);
        } else {
            ThemePlanDetail themePlanDetail = new ThemePlanDetail();
            themePlanDetail.setYear(year);
            themePlanDetail.setGroup_id(group_id);
            List<ThemePlanDetail> themeplandetail = themePlanDetailMapper.select(themePlanDetail);
            for (ThemePlanDetail list : themeplandetail) {
                IncomeExpenditure incomeexpenditures = new IncomeExpenditure();
                String theme = list.getThemename();
                for (int i = 1; i <= 12; i++) {
                    String month = i <= 9 ? "0" + i : String.valueOf(i);
                    List<ProjectContract> projectcontractlist = incomeexpendituremapper.getprojectcontractlist(theme, year, month);
                    if (i == 1) {
                        incomeexpenditures.setPlanamount1(projectcontractlist.get(0).getContractamount());
                    } else if (i == 2) {
                        incomeexpenditures.setPlanamount2(projectcontractlist.get(0).getContractamount());
                    } else if (i == 3) {
                        incomeexpenditures.setPlanamount3(projectcontractlist.get(0).getContractamount());
                    } else if (i == 4) {
                        incomeexpenditures.setPlanamount4(projectcontractlist.get(0).getContractamount());
                    } else if (i == 5) {
                        incomeexpenditures.setPlanamount5(projectcontractlist.get(0).getContractamount());
                    } else if (i == 6) {
                        incomeexpenditures.setPlanamount6(projectcontractlist.get(0).getContractamount());
                    } else if (i == 7) {
                        incomeexpenditures.setPlanamount7(projectcontractlist.get(0).getContractamount());
                    } else if (i == 8) {
                        incomeexpenditures.setPlanamount8(projectcontractlist.get(0).getContractamount());
                    } else if (i == 9) {
                        incomeexpenditures.setPlanamount9(projectcontractlist.get(0).getContractamount());
                    } else if (i == 10) {
                        incomeexpenditures.setPlanamount10(projectcontractlist.get(0).getContractamount());
                    } else if (i == 11) {
                        incomeexpenditures.setPlanamount11(projectcontractlist.get(0).getContractamount());
                    } else if (i == 12) {
                        incomeexpenditures.setPlanamount12(projectcontractlist.get(0).getContractamount());
                    }
                }
                incomeexpenditures.setAmount1(list.getAmount1());
                incomeexpenditures.setAmount2(list.getAmount2());
                incomeexpenditures.setAmount3(list.getAmount3());
                incomeexpenditures.setAmount4(list.getAmount4());
                incomeexpenditures.setAmount5(list.getAmount5());
                incomeexpenditures.setAmount6(list.getAmount6());
                incomeexpenditures.setAmount7(list.getAmount7());
                incomeexpenditures.setAmount8(list.getAmount8());
                incomeexpenditures.setAmount9(list.getAmount9());
                incomeexpenditures.setAmount10(list.getAmount10());
                incomeexpenditures.setAmount11(list.getAmount11());
                incomeexpenditures.setAmount12(list.getAmount12());
                incomeexpenditures.setGroup_id(list.getGroup_id());
                incomeexpenditures.setYear(list.getYear());
                incomeexpenditures.setBranch(list.getBranch());
                incomeexpenditures.setCenter_id(list.getCenter_id());
                incomeexpenditures.setCurrencytype(list.getCurrencytype());
                incomeexpenditures.setKind(list.getKind());
                incomeexpenditures.setThemename(list.getThemename());
                onlylist.add(incomeexpenditures);
            }

        }
        return onlylist;
    }

    @Override
    public void insert(List<IncomeExpenditure> incomeexpenditure, TokenModel tokenModel) throws Exception {
        String groupid = incomeexpenditure.get(0).getGroup_id();
        String year = incomeexpenditure.get(0).getYear();
        IncomeExpenditure income = new IncomeExpenditure();
        income.setYear(year);
        income.setGroup_id(groupid);
        List<IncomeExpenditure> incomeexpenditurelist = incomeexpendituremapper.select(income);
        if (incomeexpenditurelist.size() > 0) {
            for (IncomeExpenditure inc : incomeexpenditure) {
                inc.preUpdate(tokenModel);
                incomeexpendituremapper.updateByPrimaryKeySelective(inc);
            }
        } else {
            for (IncomeExpenditure inc : incomeexpenditure) {
                inc.setIncomeexpenditure_id(UUID.randomUUID().toString());
                inc.preInsert(tokenModel);
                incomeexpendituremapper.insert(inc);
            }
        }
    }
}
