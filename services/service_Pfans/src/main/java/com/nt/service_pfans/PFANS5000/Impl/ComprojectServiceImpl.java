package com.nt.service_pfans.PFANS5000.Impl;

import cn.hutool.core.date.DateUtil;
import com.nt.dao_Pfans.PFANS5000.*;
import com.nt.dao_Pfans.PFANS5000.Vo.CompanyProjectsVo;
import com.nt.dao_Pfans.PFANS5000.Vo.CompanyProjectsVo2;
import com.nt.dao_Pfans.PFANS5000.Vo.CompanyProjectsVo3;
import com.nt.dao_Pfans.PFANS5000.Vo.LogmanageMentVo;
import com.nt.dao_Pfans.PFANS6000.Delegainformation;
import com.nt.dao_Pfans.PFANS6000.Expatriatesinfor;
import com.nt.service_pfans.PFANS5000.CompanyProjectsService;
import com.nt.service_pfans.PFANS5000.ComprojectService;
import com.nt.service_pfans.PFANS5000.mapper.*;
import com.nt.service_pfans.PFANS6000.mapper.DelegainformationMapper;
import com.nt.service_pfans.PFANS6000.mapper.ExpatriatesinforMapper;
import com.nt.utils.StringUtils;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional(rollbackFor = Exception.class)
public class ComprojectServiceImpl implements ComprojectService {

    @Autowired
    private CompanyProjectsMapper companyprojectsMapper;
    @Autowired
    private ComProjectMapper comProjectMapper;

    @Autowired
    private StageInformationMapper stageinformationMapper;
    @Autowired
    private ProjectsystemMapper projectsystemMapper;

    @Autowired
    private ProsystemMapper prosystemMapper;
    @Autowired
    private ProjectContractMapper projectcontractMapper;
    @Autowired
    private LogManagementMapper logManagementMapper;
    @Autowired
    private ExpatriatesinforMapper expatriatesinforMapper;
    @Autowired
    private DelegainformationMapper delegainformationMapper;


    @Override
    public List<CompanyProjects> getCompanyProjectList(CompanyProjects companyprojects, HttpServletRequest request) throws Exception {
        return companyprojectsMapper.select(companyprojects);
    }

    @Override
    public List<Comproject> list(Comproject comproject) throws Exception {
        return comProjectMapper.select(comproject).stream().filter(item -> (!"9".equals(item.getStatus()))).collect(Collectors.toList());
    }

    @Override
    public List<CompanyProjects> getPjnameList(CompanyProjects companyProjects) throws Exception {
        List<CompanyProjects> companyProjectList2 = new ArrayList<>();
        List<CompanyProjects> companyProjectsList = companyprojectsMapper.select(companyProjects);
        for (int i = 0; i < companyProjectsList.size(); i++) {
            if (companyProjectsList.get(i).getEntrust() != null && companyProjectsList.get(i).getDeployment() != null) {
                companyProjects.setProject_name(companyProjectsList.get(i).getProject_name());
                companyProjects.setManagerid(companyProjectsList.get(i).getManagerid());
                companyProjects.setEntrust(companyProjectsList.get(i).getEntrust());
                companyProjects.setDeployment(companyProjectsList.get(i).getDeployment());
                companyProjectList2.add(companyProjects);
            }
        }
        return companyProjectList2;
    }


    @Override
    public LogmanageMentVo logmanageMentVo(CompanyProjects companyProjects) throws Exception {
        double a = 0;
        LogmanageMentVo logmanageMentVo = new LogmanageMentVo();
        logmanageMentVo.setCompanyProjects(companyprojectsMapper.select(companyProjects));
        for (CompanyProjects companyProjects1 : logmanageMentVo.getCompanyProjects()) {
            LogManagement logManagement = new LogManagement();
            logManagement.setProject_id(companyProjects1.getCompanyprojects_id());
            logmanageMentVo.setLogManagements(logManagementMapper.select(logManagement));
            for (LogManagement logManagement1 : logmanageMentVo.getLogManagements()) {
                a = a + Double.parseDouble(logManagement1.getTime_start());
            }
        }
        return logmanageMentVo;
    }

    //按id查询
    @Override
    public CompanyProjectsVo selectById(String comproject_id) throws Exception {
        CompanyProjectsVo staffVo = new CompanyProjectsVo();
        //项目计划
        StageInformation stageInformation = new StageInformation();
        //项目体制
//        Projectsystem projectsystem = new Projectsystem();
        Prosystem prosystem = new Prosystem();
        //项目合同
        ProjectContract projectcontract = new ProjectContract();
        //日志管理
        LogManagement logManagement = new LogManagement();

        prosystem.setComproject_id(comproject_id);
        stageInformation.setCompanyprojects_id(comproject_id);
        projectcontract.setCompanyprojects_id(comproject_id);
        logManagement.setProject_id(comproject_id);

//        CompanyProjects companyprojects = companyprojectsMapper.selectByPrimaryKey(companyprojectsid);
        Comproject comproject = comProjectMapper.selectByPrimaryKey(comproject_id);

        List<Prosystem> prosystemList = prosystemMapper.select(prosystem);
        List<StageInformation> stageinformationList = stageinformationMapper.select(stageInformation);
        List<ProjectContract> projectcontractList = projectcontractMapper.select(projectcontract);
        List<LogManagement> logManagementList = logManagementMapper.select(logManagement);

        prosystemList = prosystemList.stream().sorted(Comparator.comparing(Prosystem::getRowindex)).collect(Collectors.toList());
        stageinformationList = stageinformationList.stream().sorted(Comparator.comparing(StageInformation::getRowindex)).collect(Collectors.toList());
        projectcontractList = projectcontractList.stream().sorted(Comparator.comparing(ProjectContract::getRowindex)).collect(Collectors.toList());

//        staffVo.setCompanyprojects(companyprojects);
        staffVo.setComproject(comproject);

        staffVo.setProsystem(prosystemList);
        staffVo.setStageinformation(stageinformationList);
        staffVo.setProjectcontract(projectcontractList);
        staffVo.setLogmanagement(logManagementList);
        return staffVo;
    }


    //更新
    @Override
    public void update(CompanyProjectsVo companyProjectsVo, TokenModel tokenModel) throws Exception {
//        CompanyProjects companyProjects = new CompanyProjects();
//        BeanUtils.copyProperties(companyProjectsVo.getCompanyprojects(), companyProjects);
        Comproject comproject = new Comproject();
        BeanUtils.copyProperties(companyProjectsVo.getComproject(), comproject);
        comproject.preUpdate(tokenModel);
        comProjectMapper.updateByPrimaryKey(comproject);
//        companyprojectsMapper.updateByPrimaryKey(companyProjects);
//        String companyprojectsid = companyProjects.getCompanyprojects_id();
        String comprojectid = comproject.getComproject_id();
        //项目计划
//        List<StageInformation> stageinformationList = companyProjectsVo.getStageinformation();
//        if (stageinformationList != null) {
//            StageInformation stageinformation = new StageInformation();
//            stageinformation.setCompanyprojects_id(companyprojectsid);
//            stageinformationMapper.delete(stageinformation);
//            int rowindex = 0;
//            for (StageInformation sta : stageinformationList) {
//                rowindex = rowindex + 1;
//                sta.preInsert(tokenModel);
//                sta.setStageinformation_id(UUID.randomUUID().toString());
//                sta.setCompanyprojects_id(companyprojectsid);
//                sta.setRowindex(rowindex);
//                stageinformationMapper.insertSelective(sta);
//            }
//        }
        //项目体制
        List<Prosystem> prosystemList = companyProjectsVo.getProsystem();
//        List<Projectsystem> projectsystemList = companyProjectsVo.getProjectsystem();

        if (prosystemList != null) {
            Prosystem prosystem = new Prosystem();
            prosystem.setComproject_id(comprojectid);
            prosystemMapper.delete(prosystem);
            int rowundex = 0;
            for (Prosystem pro : prosystemList) {
                //add-ws-4/23-体制表社内根据name_id有无进行判断，社外根据name判断
                if (pro.getType().equals("1")) {
                    if (pro.getName_id() != "" && pro.getName_id() != null) {
                        rowundex = rowundex + 1;
                        pro.preInsert(tokenModel);
                        pro.setProsystem_id(UUID.randomUUID().toString());
                        pro.setComproject_id(comprojectid);
                        pro.setRowindex(rowundex);
                        prosystemMapper.insertSelective(pro);
                    }
                } else if (pro.getType().equals("0")) {
                    if (pro.getName() != "" && pro.getName() != null) {
                        rowundex = rowundex + 1;
                        pro.preInsert(tokenModel);
                        pro.setProsystem_id(UUID.randomUUID().toString());
                        pro.setComproject_id(comprojectid);
                        pro.setRowindex(rowundex);
                        prosystemMapper.insertSelective(pro);
                    }
                }
//add-ws-4/23-体制表社内根据name_id有无进行判断，社外根据name判断
            }
        }
        //项目合同
//        List<ProjectContract> projectcontractList = companyProjectsVo.getProjectcontract();
//        if (projectcontractList != null) {
//            ProjectContract projectcontract = new ProjectContract();
//            projectcontract.setCompanyprojects_id(companyprojectsid);
//            projectcontractMapper.delete(projectcontract);
//            int rowundex = 0;
//            for (ProjectContract pro : projectcontractList) {
//                rowundex = rowundex + 1;
//                pro.preInsert(tokenModel);
//                pro.setProjectcontract_id(UUID.randomUUID().toString());
//                pro.setCompanyprojects_id(companyprojectsid);
//                pro.setRowindex(rowundex);
//                projectcontractMapper.insertSelective(pro);
//            }
//        }
    }

    //新建
    @Override
    public void insert(CompanyProjectsVo companyProjectsVo, TokenModel tokenModel) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String thisYear_s = String.valueOf(Integer.parseInt(DateUtil.format(new Date(), "YYYY")) - 1);
        String initial_s_01 = "0401";
        int thatYear_i = Integer.parseInt(thisYear_s) + 1;
        String thatYear_s = String.valueOf(thatYear_i);
        String initial_s_02 = "0331";
        //今年四月一号
        String aprilFirst_s = thisYear_s + initial_s_01;
        Date aprilFirst_d = sdf.parse(aprilFirst_s);
        //明年3月31日
        String marchLast_s = thatYear_s + initial_s_02;
        Date marchLast_d = sdf.parse(marchLast_s);

        String comprojectid = UUID.randomUUID().toString();
//        CompanyProjects companyProjects = new CompanyProjects();
//        BeanUtils.copyProperties(companyProjectsVo.getCompanyprojects(), companyProjects);
        Comproject comproject = new Comproject();
        BeanUtils.copyProperties(companyProjectsVo.getComproject(), comproject);
        //add-ws-项目编码添加
        List<Comproject> comprojectlist = comProjectMapper.selectAll();
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        String year = sf1.format(date);
        int number = 0;
        String Numbers = "";
        String no = "";
        if (comprojectlist.size() > 0) {
            for (Comproject comprotect : comprojectlist) {
                //add-ws-根据当前年月日从001开始增加项目编号
                if (comprotect.getNumbers() != "" && comprotect.getNumbers() != null) {
                    String checknumber = StringUtils.uncapitalize(StringUtils.substring(comprotect.getNumbers(), 0, 8));
                    if (Integer.valueOf(year).equals(Integer.valueOf(checknumber))) {
                        number = number + 1;
                    }
                }
                //add-ws-根据当前年月日从001开始增加项目编号
            }
//            String.format("%2d", number + 1).replace(" ", "00");
            if (number <= 8) {
                no = "00" + (number + 1);
            } else {
                no = "0" + (number + 1);
            }
        } else {
            no = "001";
        }
        Numbers = year + no;
        comproject.setNumbers(Numbers);
        //add-ws-项目编码添加
        comproject.preInsert(tokenModel);
        comproject.setComproject_id(comprojectid);
        comProjectMapper.insertSelective(comproject);
        //项目计划
        List<StageInformation> stageInformationList = companyProjectsVo.getStageinformation();
        //项目体制
        List<Prosystem> projectsystemList = companyProjectsVo.getProsystem();
//        List<Projectsystem> projectsystemList = companyProjectsVo.getProjectsystem();
        //项目合同
        List<ProjectContract> projectcontractList = companyProjectsVo.getProjectcontract();
        if (projectsystemList != null) {
            int rowundex = 0;
            for (Prosystem prosystem : projectsystemList) {
                //add-ws-4/23-体制表社内根据name_id有无进行判断，社外根据name判断
                if(prosystem.getType().equals("0")){
                    if (prosystem.getName() != "" && prosystem.getName() != null) {
                        rowundex = rowundex + 1;
                        prosystem.preInsert(tokenModel);
                        prosystem.setProsystem_id(UUID.randomUUID().toString());
                        prosystem.setComproject_id(comprojectid);
                        prosystem.setRowindex(rowundex);
                        prosystemMapper.insertSelective(prosystem);
                    }
                }else  if(prosystem.getType().equals("1")){
                    if (prosystem.getName_id() != "" && prosystem.getName_id() != null) {
                        rowundex = rowundex + 1;
                        prosystem.preInsert(tokenModel);
                        prosystem.setProsystem_id(UUID.randomUUID().toString());
                        prosystem.setComproject_id(comprojectid);
                        prosystem.setRowindex(rowundex);
                        prosystemMapper.insertSelective(prosystem);
                    }
                }
                //add-ws-4/23-体制表社内根据name_id有无进行判断，社外根据name判断
                //外驻
                String suppliernameid = prosystem.getSuppliernameid();
                Expatriatesinfor expatriatesinfor = new Expatriatesinfor();
                expatriatesinfor.setExpatriatesinfor_id(suppliernameid);
                List<Expatriatesinfor> expatriatesinforList = expatriatesinforMapper.select(expatriatesinfor);
                if (expatriatesinforList != null) {
                    expatriatesinfor.setProject_name(comproject.getProject_name());
//                    expatriatesinfor.setManagerid(comproject.getManagerid());
                    expatriatesinforMapper.updateByPrimaryKeySelective(expatriatesinfor);
                }

                //入退场都不为空
                Delegainformation delegainformation = new Delegainformation();
                delegainformation.preInsert(tokenModel);
                delegainformation.setDelegainformation_id(UUID.randomUUID().toString());
                delegainformation.setCompanyprojects_id(comprojectid);
                delegainformation.setProjectsystem_id(prosystem.getProsystem_id());
                delegainformation.setAdmissiontime(prosystem.getAdmissiontime());
                delegainformation.setExittime(prosystem.getExittime());
                delegainformation.setSupplierinfor_id(prosystem.getSuppliernameid());
                String admissiontimeMonth_s = DateUtil.format(delegainformation.getAdmissiontime(), "MM");
                String exitimeMonth_s = DateUtil.format(delegainformation.getExittime(), "MM");
                if (prosystem.getAdmissiontime() != null && prosystem.getExittime() != null && prosystem.getType().equals("1")) {
                    if (aprilFirst_d.before(delegainformation.getAdmissiontime()) && delegainformation.getExittime().before(marchLast_d)) {
                        //本事业年度都在此工作
                        delegainformation.setApril("1");
                        delegainformation.setMay("1");
                        delegainformation.setJune("1");
                        delegainformation.setJuly("1");
                        delegainformation.setAugust("1");
                        delegainformation.setSeptember("1");
                        delegainformation.setOctober("1");
                        delegainformation.setNovember("1");
                        delegainformation.setDecember("1");
                        delegainformation.setJanuary("1");
                        delegainformation.setFebruary("1");
                        delegainformation.setMarch("1");
                        delegainformation.setYear(thisYear_s);
                    } else if (delegainformation.getAdmissiontime().after(aprilFirst_d) && delegainformation.getExittime().after(marchLast_d)) {
                        //4月1号之后入场，明年3月末之后退场
                        delegainformation.setYear(thisYear_s);
                        if (admissiontimeMonth_s.equals("04")) {
                            delegainformation.setApril("1");
                            delegainformation.setMay("1");
                            delegainformation.setJune("1");
                            delegainformation.setJuly("1");
                            delegainformation.setAugust("1");
                            delegainformation.setSeptember("1");
                            delegainformation.setOctober("1");
                            delegainformation.setNovember("1");
                            delegainformation.setDecember("1");
                            delegainformation.setJanuary("1");
                            delegainformation.setFebruary("1");
                            delegainformation.setMarch("1");
                        } else if (admissiontimeMonth_s.equals("05")) {
                            delegainformation.setMay("1");
                            delegainformation.setJune("1");
                            delegainformation.setJuly("1");
                            delegainformation.setAugust("1");
                            delegainformation.setSeptember("1");
                            delegainformation.setOctober("1");
                            delegainformation.setNovember("1");
                            delegainformation.setDecember("1");
                            delegainformation.setJanuary("1");
                            delegainformation.setFebruary("1");
                            delegainformation.setMarch("1");
                        } else if (admissiontimeMonth_s.equals("06")) {
                            delegainformation.setJune("1");
                            delegainformation.setJuly("1");
                            delegainformation.setAugust("1");
                            delegainformation.setSeptember("1");
                            delegainformation.setOctober("1");
                            delegainformation.setNovember("1");
                            delegainformation.setDecember("1");
                            delegainformation.setJanuary("1");
                            delegainformation.setFebruary("1");
                            delegainformation.setMarch("1");
                        } else if (admissiontimeMonth_s.equals("07")) {
                            delegainformation.setJuly("1");
                            delegainformation.setAugust("1");
                            delegainformation.setSeptember("1");
                            delegainformation.setOctober("1");
                            delegainformation.setNovember("1");
                            delegainformation.setDecember("1");
                            delegainformation.setJanuary("1");
                            delegainformation.setFebruary("1");
                            delegainformation.setMarch("1");
                        } else if (admissiontimeMonth_s.equals("08")) {
                            delegainformation.setAugust("1");
                            delegainformation.setSeptember("1");
                            delegainformation.setOctober("1");
                            delegainformation.setNovember("1");
                            delegainformation.setDecember("1");
                            delegainformation.setJanuary("1");
                            delegainformation.setFebruary("1");
                            delegainformation.setMarch("1");
                        } else if (admissiontimeMonth_s.equals("09")) {
                            delegainformation.setSeptember("1");
                            delegainformation.setOctober("1");
                            delegainformation.setNovember("1");
                            delegainformation.setDecember("1");
                            delegainformation.setJanuary("1");
                            delegainformation.setFebruary("1");
                            delegainformation.setMarch("1");
                        } else if (admissiontimeMonth_s.equals("10")) {
                            delegainformation.setOctober("1");
                            delegainformation.setNovember("1");
                            delegainformation.setDecember("1");
                            delegainformation.setJanuary("1");
                            delegainformation.setFebruary("1");
                            delegainformation.setMarch("1");
                        } else if (admissiontimeMonth_s.equals("11")) {
                            delegainformation.setNovember("1");
                            delegainformation.setDecember("1");
                            delegainformation.setJanuary("1");
                            delegainformation.setFebruary("1");
                            delegainformation.setMarch("1");
                        } else if (admissiontimeMonth_s.equals("12")) {
                            delegainformation.setDecember("1");
                            delegainformation.setJanuary("1");
                            delegainformation.setFebruary("1");
                            delegainformation.setMarch("1");
                        } else if (admissiontimeMonth_s.equals("01")) {
                            delegainformation.setJanuary("1");
                            delegainformation.setFebruary("1");
                            delegainformation.setMarch("1");
                        } else if (admissiontimeMonth_s.equals("02")) {
                            delegainformation.setFebruary("1");
                            delegainformation.setMarch("1");
                        } else if (admissiontimeMonth_s.equals("03")) {
                            delegainformation.setMarch("1");
                        }
                        //4月一号之后入场，3月末之前退场
                    } else if (delegainformation.getAdmissiontime().after(aprilFirst_d) && delegainformation.getExittime().before(marchLast_d)) {
                        for (int m = 1; m < 13; m++) {
                            if (m >= Integer.parseInt(admissiontimeMonth_s) && m <= Integer.parseInt(exitimeMonth_s)) {
                                if (m == 4) {
                                    //入退场月份都为4月份
                                    if (admissiontimeMonth_s.equals("04") && admissiontimeMonth_s.equals(exitimeMonth_s)) {
                                        delegainformation.setApril("1");
                                        //仅入场时间为4月份
                                    } else if (admissiontimeMonth_s.equals("04")) {
                                        delegainformation.setApril("1");
                                        //仅退场时间为4月份
                                    } else if (exitimeMonth_s.equals("04")) {
                                        delegainformation.setApril("1");
                                        //入退场都不为4月份
                                    } else {
                                        delegainformation.setApril("1");
                                    }
                                } else if (m == 5) {
                                    //入退场月份都为5月份
                                    if (admissiontimeMonth_s.equals("05") && admissiontimeMonth_s.equals(exitimeMonth_s)) {
                                        delegainformation.setMay("1");
                                        //仅入场时间为5月份
                                    } else if (admissiontimeMonth_s.equals("05")) {
                                        delegainformation.setMay("1");
                                        //仅退场时间为5月份
                                    } else if (exitimeMonth_s.equals("05")) {
                                        delegainformation.setMay("1");
                                        //入退场都不为5月份
                                    } else {
                                        delegainformation.setMay("1");
                                    }
                                } else if (m == 6) {
                                    //入退场月份都为6月份
                                    if (admissiontimeMonth_s.equals("06") && admissiontimeMonth_s.equals(exitimeMonth_s)) {
                                        delegainformation.setJune("1");
                                        //仅入场时间为6月份
                                    } else if (admissiontimeMonth_s.equals("06")) {
                                        delegainformation.setJune("1");
                                        //仅退场时间为6月份
                                    } else if (exitimeMonth_s.equals("06")) {
                                        delegainformation.setJune("1");
                                        //入退场都不为6月份
                                    } else {
                                        delegainformation.setJune("1");
                                    }
                                } else if (m == 7) {
                                    //入退场月份都为7月份
                                    if (admissiontimeMonth_s.equals("07") && admissiontimeMonth_s.equals(exitimeMonth_s)) {
                                        delegainformation.setJuly("1");
                                        //仅入场时间为7月份
                                    } else if (admissiontimeMonth_s.equals("07")) {
                                        delegainformation.setJuly("1");
                                        //仅退场时间为7月份
                                    } else if (exitimeMonth_s.equals("07")) {
                                        delegainformation.setJuly("1");
                                        //入退场都不为7月份
                                    } else {
                                        delegainformation.setJuly("1");
                                    }
                                } else if (m == 8) {
                                    //入退场月份都为8月份
                                    if (admissiontimeMonth_s.equals("08") && admissiontimeMonth_s.equals(exitimeMonth_s)) {
                                        delegainformation.setAugust("1");
                                        //仅入场时间为8月份
                                    } else if (admissiontimeMonth_s.equals("08")) {
                                        delegainformation.setAugust("1");
                                        //仅退场时间为8月份
                                    } else if (exitimeMonth_s.equals("08")) {
                                        delegainformation.setAugust("1");
                                        //入退场都不为8月份
                                    } else {
                                        delegainformation.setAugust("1");
                                    }
                                } else if (m == 9) {
                                    //入退场月份都为9月份
                                    if (admissiontimeMonth_s.equals("09") && admissiontimeMonth_s.equals(exitimeMonth_s)) {
                                        delegainformation.setSeptember("1");
                                        //仅入场时间为9月份
                                    } else if (admissiontimeMonth_s.equals("09")) {
                                        delegainformation.setSeptember("1");
                                        //仅退场时间为9月份
                                    } else if (exitimeMonth_s.equals("09")) {
                                        delegainformation.setSeptember("1");
                                        //入退场都不为9月份
                                    } else {
                                        delegainformation.setSeptember("1");
                                    }
                                } else if (m == 10) {
                                    //入退场月份都为10月份
                                    if (admissiontimeMonth_s.equals("10") && admissiontimeMonth_s.equals(exitimeMonth_s)) {
                                        delegainformation.setOctober("1");
                                        //仅入场时间为10月份
                                    } else if (admissiontimeMonth_s.equals("10")) {
                                        delegainformation.setOctober("1");
                                        //仅退场时间为10月份
                                    } else if (exitimeMonth_s.equals("10")) {
                                        delegainformation.setOctober("1");
                                        //入退场都不为10月份
                                    } else {
                                        delegainformation.setOctober("1");
                                    }
                                } else if (m == 11) {
                                    //入退场月份都为11月份
                                    if (admissiontimeMonth_s.equals("11") && admissiontimeMonth_s.equals(exitimeMonth_s)) {
                                        delegainformation.setNovember("1");
                                        //仅入场时间为11月份
                                    } else if (admissiontimeMonth_s.equals("11")) {
                                        delegainformation.setNovember("1");
                                        //仅退场时间为11月份
                                    } else if (exitimeMonth_s.equals("11")) {
                                        delegainformation.setNovember("1");
                                        //入退场都不为11月份
                                    } else {
                                        delegainformation.setNovember("1");
                                    }
                                } else if (m == 12) {
                                    //入退场月份都为12月份
                                    if (admissiontimeMonth_s.equals("12") && admissiontimeMonth_s.equals(exitimeMonth_s)) {
                                        delegainformation.setDecember("1");
                                        //仅入场时间为12月份
                                    } else if (admissiontimeMonth_s.equals("12")) {
                                        delegainformation.setDecember("1");
                                        //仅退场时间为12月份
                                    } else if (exitimeMonth_s.equals("12")) {
                                        delegainformation.setDecember("1");
                                        //入退场都不为12月份
                                    } else {
                                        delegainformation.setDecember("1");
                                    }
                                } else if (m == 1) {
                                    //入退场月份都为1月份
                                    if (admissiontimeMonth_s.equals("01") && admissiontimeMonth_s.equals(exitimeMonth_s)) {
                                        delegainformation.setJanuary("1");
                                        //仅入场时间为1月份
                                    } else if (admissiontimeMonth_s.equals("01")) {
                                        delegainformation.setJanuary("1");
                                        //仅退场时间为1月份
                                    } else if (exitimeMonth_s.equals("01")) {
                                        delegainformation.setJanuary("1");
                                        //入退场都不为1月份
                                    } else {
                                        delegainformation.setJanuary("1");
                                    }
                                } else if (m == 2) {
                                    //入退场月份都为2月份
                                    if (admissiontimeMonth_s.equals("02") && admissiontimeMonth_s.equals(exitimeMonth_s)) {
                                        delegainformation.setFebruary("1");
                                        //仅入场时间为2月份
                                    } else if (admissiontimeMonth_s.equals("02")) {
                                        delegainformation.setFebruary("1");
                                        //仅退场时间为2月份
                                    } else if (exitimeMonth_s.equals("02")) {
                                        delegainformation.setFebruary("1");
                                        //入退场都不为2月份
                                    } else {
                                        delegainformation.setFebruary("1");
                                    }
                                } else if (m == 3) {
                                    //入退场月份都为3月份
                                    if (admissiontimeMonth_s.equals("03") && admissiontimeMonth_s.equals(exitimeMonth_s)) {
                                        delegainformation.setMarch("1");
                                        //仅入场时间为3月份
                                    } else if (admissiontimeMonth_s.equals("03")) {
                                        delegainformation.setMarch("1");
                                        //仅退场时间为3月份
                                    } else if (exitimeMonth_s.equals("03")) {
                                        delegainformation.setMarch("1");
                                        //入退场都不为3月份
                                    } else {
                                        delegainformation.setFebruary("1");
                                    }
                                }
                            }
                        }
                    } else if (delegainformation.getAdmissiontime().before(aprilFirst_d) && delegainformation.getExittime().before(marchLast_d)) {
                        delegainformation.setYear(thisYear_s);
                        if (exitimeMonth_s.equals("04")) {
                            delegainformation.setApril("1");
                            delegainformation.setMay("1");
                            delegainformation.setJune("1");
                            delegainformation.setJuly("1");
                            delegainformation.setAugust("1");
                            delegainformation.setSeptember("1");
                            delegainformation.setOctober("1");
                            delegainformation.setNovember("1");
                            delegainformation.setDecember("1");
                            delegainformation.setJanuary("1");
                            delegainformation.setFebruary("1");
                            delegainformation.setMarch("1");
                        } else if (exitimeMonth_s.equals("05")) {
                            delegainformation.setMay("1");
                            delegainformation.setJune("1");
                            delegainformation.setJuly("1");
                            delegainformation.setAugust("1");
                            delegainformation.setSeptember("1");
                            delegainformation.setOctober("1");
                            delegainformation.setNovember("1");
                            delegainformation.setDecember("1");
                            delegainformation.setJanuary("1");
                            delegainformation.setFebruary("1");
                            delegainformation.setMarch("1");
                        } else if (exitimeMonth_s.equals("06")) {
                            delegainformation.setJune("1");
                            delegainformation.setJuly("1");
                            delegainformation.setAugust("1");
                            delegainformation.setSeptember("1");
                            delegainformation.setOctober("1");
                            delegainformation.setNovember("1");
                            delegainformation.setDecember("1");
                            delegainformation.setJanuary("1");
                            delegainformation.setFebruary("1");
                            delegainformation.setMarch("1");
                        } else if (exitimeMonth_s.equals("07")) {
                            delegainformation.setJuly("1");
                            delegainformation.setAugust("1");
                            delegainformation.setSeptember("1");
                            delegainformation.setOctober("1");
                            delegainformation.setNovember("1");
                            delegainformation.setDecember("1");
                            delegainformation.setJanuary("1");
                            delegainformation.setFebruary("1");
                            delegainformation.setMarch("1");
                        } else if (exitimeMonth_s.equals("08")) {
                            delegainformation.setAugust("1");
                            delegainformation.setSeptember("1");
                            delegainformation.setOctober("1");
                            delegainformation.setNovember("1");
                            delegainformation.setDecember("1");
                            delegainformation.setJanuary("1");
                            delegainformation.setFebruary("1");
                            delegainformation.setMarch("1");
                        } else if (exitimeMonth_s.equals("09")) {
                            delegainformation.setSeptember("1");
                            delegainformation.setOctober("1");
                            delegainformation.setNovember("1");
                            delegainformation.setDecember("1");
                            delegainformation.setJanuary("1");
                            delegainformation.setFebruary("1");
                            delegainformation.setMarch("1");
                        } else if (exitimeMonth_s.equals("10")) {
                            delegainformation.setOctober("1");
                            delegainformation.setNovember("1");
                            delegainformation.setDecember("1");
                            delegainformation.setJanuary("1");
                            delegainformation.setFebruary("1");
                            delegainformation.setMarch("1");
                        } else if (exitimeMonth_s.equals("11")) {
                            delegainformation.setNovember("1");
                            delegainformation.setDecember("1");
                            delegainformation.setJanuary("1");
                            delegainformation.setFebruary("1");
                            delegainformation.setMarch("1");
                        } else if (exitimeMonth_s.equals("12")) {
                            delegainformation.setDecember("1");
                            delegainformation.setJanuary("1");
                            delegainformation.setFebruary("1");
                            delegainformation.setMarch("1");
                        } else if (exitimeMonth_s.equals("01")) {
                            delegainformation.setJanuary("1");
                            delegainformation.setFebruary("1");
                            delegainformation.setMarch("1");
                        } else if (exitimeMonth_s.equals("02")) {
                            delegainformation.setFebruary("1");
                            delegainformation.setMarch("1");
                        } else if (exitimeMonth_s.equals("03")) {
                            delegainformation.setMarch("1");
                        }
                    }
                } else if (prosystem.getAdmissiontime() != null && prosystem.getType().equals("1")) {
                    if (aprilFirst_d.after(delegainformation.getAdmissiontime())) {
                        delegainformation.setApril("1");
                        delegainformation.setMay("1");
                        delegainformation.setJune("1");
                        delegainformation.setJuly("1");
                        delegainformation.setAugust("1");
                        delegainformation.setSeptember("1");
                        delegainformation.setOctober("1");
                        delegainformation.setNovember("1");
                        delegainformation.setDecember("1");
                        delegainformation.setJanuary("1");
                        delegainformation.setFebruary("1");
                        delegainformation.setMarch("1");
                        delegainformation.setYear(thisYear_s);
                    } else if (aprilFirst_d.before(delegainformation.getAdmissiontime()) && marchLast_d.after(delegainformation.getAdmissiontime())) {
                        delegainformation.setYear(thisYear_s);
                        if (admissiontimeMonth_s.equals("04")) {
                            delegainformation.setApril("1");
                            delegainformation.setMay("1");
                            delegainformation.setJune("1");
                            delegainformation.setJuly("1");
                            delegainformation.setAugust("1");
                            delegainformation.setSeptember("1");
                            delegainformation.setOctober("1");
                            delegainformation.setNovember("1");
                            delegainformation.setDecember("1");
                            delegainformation.setJanuary("1");
                            delegainformation.setFebruary("1");
                            delegainformation.setMarch("1");
                        } else if (admissiontimeMonth_s.equals("05")) {
                            delegainformation.setMay("1");
                            delegainformation.setJune("1");
                            delegainformation.setJuly("1");
                            delegainformation.setAugust("1");
                            delegainformation.setSeptember("1");
                            delegainformation.setOctober("1");
                            delegainformation.setNovember("1");
                            delegainformation.setDecember("1");
                            delegainformation.setJanuary("1");
                            delegainformation.setFebruary("1");
                            delegainformation.setMarch("1");
                        } else if (admissiontimeMonth_s.equals("06")) {
                            delegainformation.setJune("1");
                            delegainformation.setJuly("1");
                            delegainformation.setAugust("1");
                            delegainformation.setSeptember("1");
                            delegainformation.setOctober("1");
                            delegainformation.setNovember("1");
                            delegainformation.setDecember("1");
                            delegainformation.setJanuary("1");
                            delegainformation.setFebruary("1");
                            delegainformation.setMarch("1");
                        } else if (admissiontimeMonth_s.equals("07")) {
                            delegainformation.setJuly("1");
                            delegainformation.setAugust("1");
                            delegainformation.setSeptember("1");
                            delegainformation.setOctober("1");
                            delegainformation.setNovember("1");
                            delegainformation.setDecember("1");
                            delegainformation.setJanuary("1");
                            delegainformation.setFebruary("1");
                            delegainformation.setMarch("1");
                        } else if (admissiontimeMonth_s.equals("08")) {
                            delegainformation.setAugust("1");
                            delegainformation.setSeptember("1");
                            delegainformation.setOctober("1");
                            delegainformation.setNovember("1");
                            delegainformation.setDecember("1");
                            delegainformation.setJanuary("1");
                            delegainformation.setFebruary("1");
                            delegainformation.setMarch("1");
                        } else if (admissiontimeMonth_s.equals("09")) {
                            delegainformation.setSeptember("1");
                            delegainformation.setOctober("1");
                            delegainformation.setNovember("1");
                            delegainformation.setDecember("1");
                            delegainformation.setJanuary("1");
                            delegainformation.setFebruary("1");
                            delegainformation.setMarch("1");
                        } else if (admissiontimeMonth_s.equals("10")) {
                            delegainformation.setOctober("1");
                            delegainformation.setNovember("1");
                            delegainformation.setDecember("1");
                            delegainformation.setJanuary("1");
                            delegainformation.setFebruary("1");
                            delegainformation.setMarch("1");
                        } else if (admissiontimeMonth_s.equals("11")) {
                            delegainformation.setNovember("1");
                            delegainformation.setDecember("1");
                            delegainformation.setJanuary("1");
                            delegainformation.setFebruary("1");
                            delegainformation.setMarch("1");
                        } else if (admissiontimeMonth_s.equals("12")) {
                            delegainformation.setDecember("1");
                            delegainformation.setJanuary("1");
                            delegainformation.setFebruary("1");
                            delegainformation.setMarch("1");
                        } else if (admissiontimeMonth_s.equals("01")) {
                            delegainformation.setJanuary("1");
                            delegainformation.setFebruary("1");
                            delegainformation.setMarch("1");
                        } else if (admissiontimeMonth_s.equals("02")) {
                            delegainformation.setFebruary("1");
                            delegainformation.setMarch("1");
                        } else if (admissiontimeMonth_s.equals("03")) {
                            delegainformation.setMarch("1");
                        }
                    }
                }
                delegainformationMapper.insert(delegainformation);
            }
        }
        if (stageInformationList != null) {
            int rowundex = 0;
            for (StageInformation stageInformation : stageInformationList) {
                rowundex = rowundex + 1;
                stageInformation.preInsert(tokenModel);
                stageInformation.setStageinformation_id(UUID.randomUUID().toString());
                stageInformation.setCompanyprojects_id(comprojectid);
                stageInformation.setRowindex(rowundex);
                stageinformationMapper.insertSelective(stageInformation);
            }
        }
        if (projectcontractList != null) {
            int rowundex = 0;
            for (ProjectContract projectcontract : projectcontractList) {
                rowundex = rowundex + 1;
                projectcontract.preInsert(tokenModel);
                projectcontract.setProjectcontract_id(UUID.randomUUID().toString());
                projectcontract.setCompanyprojects_id(comprojectid);
                projectcontract.setRowindex(rowundex);
                projectcontractMapper.insertSelective(projectcontract);
            }
        }

    }

    /**
     * 开发计划查询列表
     *
     * @param stageInformation
     * @return
     * @throws Exception
     */
    @Override
    public List<StageInformation> getstageInformation(StageInformation stageInformation) throws Exception {
        return stageinformationMapper.select(stageInformation);
    }

    /**
     * 现场管理列表
     *
     * @return List<CompanyProjectsVo2>
     * @作者：zy
     */
    @Override
    public List<CompanyProjectsVo2> getSiteList(CompanyProjects companyProjects) throws Exception {
        return companyprojectsMapper.getList(companyProjects.getOwners());
    }

    @Override
    public List<Comproject> getList2(String flag) throws Exception {
        if ("0".equals(flag)) {
            return comProjectMapper.getList2();
        } else {
            return comProjectMapper.getList3();
        }
    }


    /**
     * PJ完了审批列表
     *
     * @return List<CompanyProjectsVo2>
     * @作者：zy
     */
    @Override
    public List<CompanyProjectsVo2> getPjList(String flag) throws Exception {
        //项目表
        CompanyProjects companyProjects = new CompanyProjects();
        List<CompanyProjects> companyProjectsList = companyprojectsMapper.select(companyProjects);
        if ("0".equals(flag)) {
            companyProjectsList = companyProjectsList.stream().filter(item -> (
                    "4".equals(item.getStatus()) ||
                            "7".equals(item.getStatus()) ||
                            "8".equals(item.getStatus()))).collect(Collectors.toList());
        } else {
            companyProjectsList = companyProjectsList.stream().filter(item -> "9".equals(item.getStatus())).collect(Collectors.toList());
        }


        //阶段信息表
        StageInformation stageInformation = new StageInformation();
        List<StageInformation> stageInformationList = stageinformationMapper.select(stageInformation);
        //合同表，获取该项目下最新的合同的契约番号
        List<CompanyProjectsVo2> listVo2 = companyprojectsMapper.getListVo2();
        Map<String, String> mapVo2 = new HashMap<>();
        for (CompanyProjectsVo2 vo2 : listVo2) {
            String key = vo2.getCompanyprojects_id();
            String value = vo2.getContractnumber();
            if (!mapVo2.containsKey(key)) {
                mapVo2.put(key, value);
            }
        }
        //获取该项目下阶段信息最小的实际开始时间，最大的结束时间
        Map<String, StageInformation> map = new HashMap<>();
        for (StageInformation info : stageInformationList) {
            String key = info.getCompanyprojects_id();
            if (!map.containsKey(key)) {
                map.put(key, info);
            } else {
                StageInformation oldVo = map.get(key);
                Date oStart = oldVo.getActualstarttime();
                Date oEnd = oldVo.getActualendtime();

                if (info.getActualstarttime() != null) {
                    if (info.getActualstarttime().before(oStart)) {
                        oldVo.setActualstarttime(info.getActualstarttime());
                    }
                }
                if (info.getActualendtime() != null) {
                    if (info.getActualendtime().after(oEnd)) {
                        oldVo.setActualendtime(info.getActualendtime());
                    }
                }
            }
        }
        List<CompanyProjectsVo2> result = new ArrayList<>();
        for (CompanyProjects projects : companyProjectsList) {
            CompanyProjectsVo2 vo = new CompanyProjectsVo2();
            vo.setCompanyprojects_id(projects.getCompanyprojects_id());
            //契约番号
            if (mapVo2.containsKey(projects.getCompanyprojects_id())) {
                vo.setContractnumber(mapVo2.get(projects.getCompanyprojects_id()));
            }
            vo.setEntrust(projects.getEntrust());//委托元
            vo.setField(projects.getField());//项目分野
            vo.setLeaderid(projects.getLeaderid());//PL
            vo.setNumbers(projects.getNumbers());//项目编号
            vo.setProject_name(projects.getProject_name());//项目名称
            vo.setStatus(projects.getStatus());//审批状态
            vo.setWork(projects.getWork());//受托工数
            StageInformation info = map.get(projects.getCompanyprojects_id());
            if (info != null) {
                vo.setActualstarttime(info.getActualstarttime());
                vo.setActualendtime(info.getActualendtime());
            }
            result.add(vo);
        }
        return result;
    }

    @Override
    public List<CompanyProjectsVo3> getCompanyProject(String SyspName) throws Exception {
        return companyprojectsMapper.getCompanyProject(SyspName);
    }
    //add-ws-6/5-禅道075任务，项目名称问题修正
    @Override
    public List<Comproject> Listproject(Comproject comproject) throws Exception {
        return comProjectMapper.select(comproject);
    }
    @Override
    public List<CompanyProjects> Listproject2(CompanyProjects companyprojects) throws Exception {
        return companyprojectsMapper.select(companyprojects);
    }
    //add-ws-6/5-禅道075任务，项目名称问题修正
    @Override
    public List<Comproject> getComproject(Comproject comproject) throws Exception {
        List<Comproject> rst = new ArrayList<Comproject>();
        Comproject con = new Comproject();
        con.setOwner(comproject.getOwner());
        con.setStatus("4");
        rst = comProjectMapper.select(con);

        Prosystem prosystem = new Prosystem();
        prosystem.setName(comproject.getOwner());
        List<Prosystem> prosystemlist = prosystemMapper.select(prosystem);
        for (Prosystem item : prosystemlist) {
            Comproject rs = comProjectMapper.selectByPrimaryKey(item.getComproject_id());
            //add-ws-4/24-因数据问题没匹配上会报错，因此进行非空判断
            if(rs!=null){
                if ("4".equals(rs.getStatus())) {
                    rst.add(rs);
                }
            }
            //add-ws-4/24-因数据问题没匹配上会报错，因此进行非空判断
        }
        return rst;
    }


    //add-ws-阚总日志问题修正
    @Override
    public List<Comproject> getComproject2(Comproject comproject) throws Exception {
        List<Comproject> rst = new ArrayList<Comproject>();
        Prosystem prosystem = new Prosystem();
        prosystem.setName(comproject.getOwner());
        List<Prosystem> prosystemlist = prosystemMapper.select(prosystem);
        for (Prosystem item : prosystemlist) {
            Comproject rs = comProjectMapper.selectByPrimaryKey(item.getComproject_id());
            if(rs!=null){
                if ("4".equals(rs.getStatus())) {
                    rst.add(rs);
                }
            }
        }
        return rst;
    }
//add-ws-阚总日志问题修正
}
