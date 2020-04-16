package com.nt.service_pfans.PFANS5000.Impl;

import cn.hutool.core.date.DateUtil;
import com.nt.dao_Pfans.PFANS1000.Contractnumbercount;
import com.nt.dao_Pfans.PFANS5000.*;
import com.nt.dao_Pfans.PFANS5000.Vo.CompanyProjectsVo;
import com.nt.dao_Pfans.PFANS5000.Vo.CompanyProjectsVo2;
import com.nt.dao_Pfans.PFANS5000.Vo.CompanyProjectsVo3;
import com.nt.dao_Pfans.PFANS5000.Vo.LogmanageMentVo;
import com.nt.dao_Pfans.PFANS6000.Delegainformation;
import com.nt.dao_Pfans.PFANS6000.Expatriatesinfor;
import com.nt.dao_Pfans.PFANS6000.Priceset;
import com.nt.service_pfans.PFANS1000.mapper.ContractnumbercountMapper;
import com.nt.service_pfans.PFANS5000.CompanyProjectsService;
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
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional(rollbackFor = Exception.class)
public class CompanyProjectsServiceImpl implements CompanyProjectsService {

    @Autowired
    private CompanyProjectsMapper companyprojectsMapper;
    @Autowired
    private StageInformationMapper stageinformationMapper;
    @Autowired
    private ProjectsystemMapper projectsystemMapper;
    @Autowired
    private ProjectContractMapper projectcontractMapper;
    @Autowired
    private LogManagementMapper logManagementMapper;
    @Autowired
    private ExpatriatesinforMapper expatriatesinforMapper;
    @Autowired
    private DelegainformationMapper delegainformationMapper;
    @Autowired
    private ContractnumbercountMapper contractnumbercountMapper;


    @Override
    public List<CompanyProjects> getCompanyProjectList(CompanyProjects companyprojects, HttpServletRequest request) throws Exception {
        return companyprojectsMapper.select(companyprojects);
    }

    @Override
    public List<CompanyProjects> list(CompanyProjects companyProjects) throws Exception {
        return companyprojectsMapper.select(companyProjects);
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
    public CompanyProjectsVo selectById(String companyprojectsid) throws Exception {
        CompanyProjectsVo staffVo = new CompanyProjectsVo();
        //项目计划
        StageInformation stageInformation = new StageInformation();
        //项目体制
        Projectsystem projectsystem = new Projectsystem();
        //项目合同
        ProjectContract projectcontract = new ProjectContract();
        //日志管理
        LogManagement logManagement = new LogManagement();
        //ADD 03-18 ,委托元为内采时，合同可自行添加请求金额
        Contractnumbercount contractnumbercount = new Contractnumbercount();
        contractnumbercount.setCompanyprojectsid(companyprojectsid);
        List<Contractnumbercount> contractnumbercountList = contractnumbercountMapper.select(contractnumbercount);
        contractnumbercountList = contractnumbercountList.stream().sorted(Comparator.comparing(Contractnumbercount::getRowindex)).collect(Collectors.toList());
        staffVo.setContractnumbercount(contractnumbercountList);
        //ADD 03-18 ,委托元为内采时，合同可自行添加请求金额 END

        projectsystem.setCompanyprojects_id(companyprojectsid);
        stageInformation.setCompanyprojects_id(companyprojectsid);
        projectcontract.setCompanyprojects_id(companyprojectsid);
        logManagement.setProject_id(companyprojectsid);

        CompanyProjects companyprojects = companyprojectsMapper.selectByPrimaryKey(companyprojectsid);
        List<Projectsystem> projectsystemList = projectsystemMapper.select(projectsystem);
        List<StageInformation> stageinformationList = stageinformationMapper.select(stageInformation);
        List<ProjectContract> projectcontractList = projectcontractMapper.select(projectcontract);
        List<LogManagement> logManagementList = logManagementMapper.select(logManagement);

        projectsystemList = projectsystemList.stream().sorted(Comparator.comparing(Projectsystem::getRowindex)).collect(Collectors.toList());
        stageinformationList = stageinformationList.stream().sorted(Comparator.comparing(StageInformation::getRowindex)).collect(Collectors.toList());
        projectcontractList = projectcontractList.stream().sorted(Comparator.comparing(ProjectContract::getRowindex)).collect(Collectors.toList());

        staffVo.setCompanyprojects(companyprojects);
        staffVo.setProjectsystem(projectsystemList);
        staffVo.setStageinformation(stageinformationList);
        staffVo.setProjectcontract(projectcontractList);
        staffVo.setLogmanagement(logManagementList);
        return staffVo;
    }


    //更新
    @Override
    public void update(CompanyProjectsVo companyProjectsVo, TokenModel tokenModel) throws Exception {
        CompanyProjects companyProjects = new CompanyProjects();
        BeanUtils.copyProperties(companyProjectsVo.getCompanyprojects(), companyProjects);
        companyProjects.preUpdate(tokenModel);
        companyprojectsMapper.updateByPrimaryKey(companyProjects);
        String companyprojectsid = companyProjects.getCompanyprojects_id();
        //项目计划
        List<StageInformation> stageinformationList = companyProjectsVo.getStageinformation();
        if (stageinformationList != null) {
            StageInformation stageinformation = new StageInformation();
            stageinformation.setCompanyprojects_id(companyprojectsid);
            stageinformationMapper.delete(stageinformation);
            int rowindex = 0;
            for (StageInformation sta : stageinformationList) {
                rowindex = rowindex + 1;
                sta.preInsert(tokenModel);
                sta.setStageinformation_id(UUID.randomUUID().toString());
                sta.setCompanyprojects_id(companyprojectsid);
                sta.setRowindex(rowindex);
                stageinformationMapper.insertSelective(sta);
            }
        }
        //项目体制
        List<Projectsystem> projectsystemList = companyProjectsVo.getProjectsystem();
        if (projectsystemList != null && projectsystemList.size() > 0) {
            Projectsystem projectsystem = new Projectsystem();
            projectsystem.setCompanyprojects_id(companyprojectsid);
            projectsystemMapper.delete(projectsystem);
            Delegainformation delegainformation1 = new Delegainformation();
            delegainformation1.setCompanyprojects_id(companyprojectsid);
            delegainformationMapper.delete(delegainformation1);
            int rowundex = 0;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            //2019
            String thisYear_s = String.valueOf(Integer.parseInt(DateUtil.format(new Date(), "YYYY")) - 1);
            //2020
            int thatYear_i = Integer.parseInt(thisYear_s) + 1;
            String thatYear_s = String.valueOf(thatYear_i);
            int tThatYear_i = thatYear_i + 1;
            String tThatYear_s = String.valueOf(tThatYear_i);
            int tThatYearThat_i = tThatYear_i + 1;
            String tThatYeatThat_s = String.valueOf(tThatYearThat_i);
            String initial_s_01 = "0401";
            String initial_s_02 = "0331";
            //今年四月一号
            String aprilFirst_s = thisYear_s + initial_s_01;
            Date aprilFirst_d = sdf.parse(aprilFirst_s);
            //明年3月31日
            String marchLast_s = thatYear_s + initial_s_02;
            Date marchLast_d = sdf.parse(marchLast_s);
            //后年4月1日
            String marchThatLast_s = tThatYear_s + initial_s_01;
            Date marchThatLast_d = sdf.parse(marchThatLast_s);
            //大后年3月31日
            String marchThatFirst_s = tThatYeatThat_s + initial_s_02;
            Date marchThatFirst_d = sdf.parse(marchThatFirst_s);
            for (Projectsystem pro : projectsystemList) {
                rowundex = rowundex + 1;
                pro.preInsert(tokenModel);
                pro.setProjectsystem_id(UUID.randomUUID().toString());
                pro.setCompanyprojects_id(companyprojectsid);
                pro.setRowindex(rowundex);
                projectsystemMapper.insertSelective(pro);
                //活用情报
                if (pro.getAdmissiontime() != null && pro.getType().equals("1")) {
                    Delegainformation delegainformation = new Delegainformation();
                    delegainformation.preInsert(tokenModel);
                    delegainformation.setDelegainformation_id(UUID.randomUUID().toString());
                    delegainformation.setCompanyprojects_id(companyprojectsid);
                    delegainformation.setProjectsystem_id(pro.getProjectsystem_id());
                    delegainformation.setAdmissiontime(pro.getAdmissiontime());
                    delegainformation.setExittime(pro.getExittime());
                    delegainformation.setSupplierinfor_id(pro.getSuppliernameid());
                    delegainformation.setProjectsystem_id(pro.getProjectsystem_id());
                    String admissiontimeMonth_s = DateUtil.format(delegainformation.getAdmissiontime(), "MM");
                    String exitimeMonth_s = DateUtil.format(delegainformation.getExittime(), "MM");
                    //入退场都不为空
                    if (pro.getAdmissiontime() != null && pro.getExittime() != null && pro.getType().equals("1")) {
                        if (aprilFirst_d.after(delegainformation.getAdmissiontime()) && delegainformation.getExittime().after(marchLast_d)) {
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
                        } else if (delegainformation.getAdmissiontime().after(aprilFirst_d)
                                && delegainformation.getExittime().after(marchLast_d)
                                && (DateUtil.format(delegainformation.getAdmissiontime(), "YYYY").equals(thisYear_s))) {
                            //今年4月1号之后入场，明年3月末之后退场
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
                            //4月一号之后入场，3月末之前退场（本事业年度）(aaaa)
                        } else if (delegainformation.getAdmissiontime().after(aprilFirst_d) && delegainformation.getExittime().before(marchLast_d)) {
                            delegainformation.setYear(thisYear_s);
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
                            //4月1号之前入场，明年三月末之前退场
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
                        } else if (delegainformation.getAdmissiontime().after(aprilFirst_d)
                                && delegainformation.getExittime().before(marchThatFirst_d)
                                && DateUtil.format(delegainformation.getExittime(), "YYYY").equals(thatYear_s)) {
                            //111       本事业年度之后入场，跨事业年度退场
                            for (int m = 1; m < 13; m++) {
                                if (Integer.parseInt(admissiontimeMonth_s) <= m && m <= 3) {
                                    delegainformation.setYear(thisYear_s);
                                    if (m == 1) {
                                        delegainformation.setJanuary("1");
                                        delegainformation.setFebruary("1");
                                        delegainformation.setMarch("1");
                                    } else if (m == 2) {
                                        delegainformation.setFebruary("1");
                                        delegainformation.setMarch("1");
                                    } else if (m == 3) {
                                        delegainformation.setMarch("1");
                                    }
                                } else if (4 <= Integer.parseInt(admissiontimeMonth_s) && Integer.parseInt(admissiontimeMonth_s) <= m) {
                                    delegainformation.setYear(thisYear_s);
                                    if (m == 4) {
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
                                    } else if (m == 5) {
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
                                    } else if (m == 6) {
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
                                    } else if (m == 7) {
                                        delegainformation.setJuly("1");
                                        delegainformation.setAugust("1");
                                        delegainformation.setSeptember("1");
                                        delegainformation.setOctober("1");
                                        delegainformation.setNovember("1");
                                        delegainformation.setDecember("1");
                                        delegainformation.setJanuary("1");
                                        delegainformation.setFebruary("1");
                                        delegainformation.setMarch("1");
                                    } else if (m == 8) {
                                        delegainformation.setAugust("1");
                                        delegainformation.setSeptember("1");
                                        delegainformation.setOctober("1");
                                        delegainformation.setNovember("1");
                                        delegainformation.setDecember("1");
                                        delegainformation.setJanuary("1");
                                        delegainformation.setFebruary("1");
                                        delegainformation.setMarch("1");
                                    } else if (m == 9) {
                                        delegainformation.setSeptember("1");
                                        delegainformation.setOctober("1");
                                        delegainformation.setNovember("1");
                                        delegainformation.setDecember("1");
                                        delegainformation.setJanuary("1");
                                        delegainformation.setFebruary("1");
                                        delegainformation.setMarch("1");
                                    } else if (m == 10) {
                                        delegainformation.setOctober("1");
                                        delegainformation.setNovember("1");
                                        delegainformation.setDecember("1");
                                        delegainformation.setJanuary("1");
                                        delegainformation.setFebruary("1");
                                        delegainformation.setMarch("1");
                                    } else if (m == 11) {
                                        delegainformation.setNovember("1");
                                        delegainformation.setDecember("1");
                                        delegainformation.setJanuary("1");
                                        delegainformation.setFebruary("1");
                                        delegainformation.setMarch("1");
                                    } else if (m == 12) {
                                        delegainformation.setDecember("1");
                                        delegainformation.setJanuary("1");
                                        delegainformation.setFebruary("1");
                                        delegainformation.setMarch("1");
                                    }
                                }
                            }
                        }
                    } else if (pro.getAdmissiontime() != null && pro.getType().equals("1")) {
                        if (aprilFirst_d.before(delegainformation.getAdmissiontime()) && marchLast_d.after(delegainformation.getAdmissiontime())) {
                            //入场时间为本事业年度4月1号之后，无退场时间
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
                        //今年三月份之后入场并且是明年3月三十一号之前（退场时间为空）(下一个事业年度)
                        if (delegainformation.getAdmissiontime().after(marchLast_d) && delegainformation.getAdmissiontime().before(marchThatFirst_d)) {
                            delegainformation.setYear(thatYear_s);
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
                    delegainformationMapper.insertSelective(delegainformation);
                }
            }
        }
        //项目合同
        List<ProjectContract> projectcontractList = companyProjectsVo.getProjectcontract();
        if (projectcontractList != null && projectcontractList.size() >= 0) {
            ProjectContract projectcontract = new ProjectContract();
            projectcontract.setCompanyprojects_id(companyprojectsid);
            projectcontractMapper.delete(projectcontract);
            int rowundex = 0;
            for (ProjectContract pro : projectcontractList) {
                rowundex = rowundex + 1;
                pro.preInsert(tokenModel);
                pro.setProjectcontract_id(UUID.randomUUID().toString());
                pro.setCompanyprojects_id(companyprojectsid);
                pro.setRowindex(rowundex);
                projectcontractMapper.insertSelective(pro);
            }
        }

        //ADD 03-18 ,委托元为内采时，合同可自行添加请求金额
        List<Contractnumbercount> contractnumbercountList = companyProjectsVo.getContractnumbercount();
        if (contractnumbercountList!= null && contractnumbercountList.size() > 0) {
            Contractnumbercount contractnumbercount = new Contractnumbercount();
            contractnumbercount.setCompanyprojectsid(companyprojectsid);
            contractnumbercountMapper.delete(contractnumbercount);
            int rowindex = 0;
            for (Contractnumbercount info : contractnumbercountList) {
                rowindex = rowindex + 1;
                info.preInsert(tokenModel);
                info.setContractnumbercount_id(UUID.randomUUID().toString());
                info.setCompanyprojectsid(companyprojectsid);
                info.setRowindex(rowindex);
                contractnumbercountMapper.insertSelective(info);
            }
        }
        //ADD 03-18 ,委托元为内采时，合同可自行添加请求金额 END
    }

    //新建
    @Override
    public void insert(CompanyProjectsVo companyProjectsVo, TokenModel tokenModel) throws Exception {


        String companyprojectsid = UUID.randomUUID().toString();
        CompanyProjects companyProjects = new CompanyProjects();
        BeanUtils.copyProperties(companyProjectsVo.getCompanyprojects(), companyProjects);
        List<CompanyProjects> companyProjectslist = companyprojectsMapper.selectAll();
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();

        String year = sf1.format(date);
//        String year = new SimpleDateFormat("yyyymmdd",Locale.CHINESE).format(Calendar.getInstance().getTime());
        int number = 0;
        String Numbers = "";
        String no = "";
        if(companyProjectslist.size()>0){
            for(CompanyProjects comprotect :companyProjectslist){
                //add-ws-根据当前年月日从001开始增加项目编号
                if(comprotect.getNumbers()!="" && comprotect.getNumbers()!=null){
                    String checknumber = StringUtils.uncapitalize(StringUtils.substring(comprotect.getNumbers(), 0,8));
                    if(Integer.valueOf(year).equals(Integer.valueOf(checknumber))){
                        number = number+1;
                    }
                }
                //add-ws-根据当前年月日从001开始增加项目编号
            }
//            String.format("%2d", number + 1).replace(" ", "00");
            if(number<=8){
                no="00"+(number + 1);
            }else{
                no="0"+(number + 1);
            }
        }else{
            no = "001";
        }
        Numbers = year+ no;
        companyProjects.setNumbers(Numbers);
        companyProjects.preInsert(tokenModel);
        companyProjects.setCompanyprojects_id(companyprojectsid);
        companyprojectsMapper.insertSelective(companyProjects);
        //项目计划
        List<StageInformation> stageInformationList = companyProjectsVo.getStageinformation();
        //项目体制
        List<Projectsystem> projectsystemList = companyProjectsVo.getProjectsystem();
        //项目合同
        List<ProjectContract> projectcontractList = companyProjectsVo.getProjectcontract();
        //ADD 03-18 ,委托元为内采时，合同可自行添加请求金额
        List<Contractnumbercount> contractnumbercountList = companyProjectsVo.getContractnumbercount();
        //ADD 03-18 ,委托元为内采时，合同可自行添加请求金额 END
        if (projectsystemList != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            //2019
            String thisYear_s = String.valueOf(Integer.parseInt(DateUtil.format(new Date(), "YYYY")) - 1);
            //2020
            int thatYear_i = Integer.parseInt(thisYear_s) + 1;
            String thatYear_s = String.valueOf(thatYear_i);
            int tThatYear_i = thatYear_i + 1;
            String tThatYear_s = String.valueOf(tThatYear_i);
            int tThatYearThat_i = tThatYear_i + 1;
            String tThatYeatThat_s = String.valueOf(tThatYearThat_i);
            String initial_s_01 = "0401";
            String initial_s_02 = "0331";
            //今年四月一号
            String aprilFirst_s = thisYear_s + initial_s_01;
            Date aprilFirst_d = sdf.parse(aprilFirst_s);
            //明年3月31日
            String marchLast_s = thatYear_s + initial_s_02;
            Date marchLast_d = sdf.parse(marchLast_s);
            //后年4月1日
            String marchThatLast_s = tThatYear_s + initial_s_01;
            Date marchThatLast_d = sdf.parse(marchThatLast_s);
            //大后年3月31日
            String marchThatFirst_s = tThatYeatThat_s + initial_s_02;
            Date marchThatFirst_d = sdf.parse(marchThatFirst_s);
            int rowundex = 0;
            for (Projectsystem projectsystem : projectsystemList) {
                rowundex = rowundex + 1;
                projectsystem.preInsert(tokenModel);
                projectsystem.setProjectsystem_id(UUID.randomUUID().toString());
                projectsystem.setCompanyprojects_id(companyprojectsid);
                projectsystem.setRowindex(rowundex);
                projectsystemMapper.insertSelective(projectsystem);

                //外驻
                String suppliernameid = projectsystem.getSuppliernameid();
                Expatriatesinfor expatriatesinfor = new Expatriatesinfor();
                expatriatesinfor.setExpatriatesinfor_id(suppliernameid);
                List<Expatriatesinfor> expatriatesinforList = expatriatesinforMapper.select(expatriatesinfor);
                if (expatriatesinforList != null) {
                    expatriatesinfor.setProject_name(companyProjects.getProject_name());
                    expatriatesinfor.setManagerid(companyProjects.getManagerid());
                    expatriatesinforMapper.updateByPrimaryKeySelective(expatriatesinfor);
                }
                //活用情报
                if (projectsystem.getAdmissiontime() != null && projectsystem.getType().equals("1")) {
                    Delegainformation delegainformation = new Delegainformation();
                    delegainformation.preInsert(tokenModel);
                    delegainformation.setDelegainformation_id(UUID.randomUUID().toString());
                    delegainformation.setCompanyprojects_id(companyprojectsid);
                    delegainformation.setProjectsystem_id(projectsystem.getProjectsystem_id());
                    delegainformation.setAdmissiontime(projectsystem.getAdmissiontime());
                    delegainformation.setExittime(projectsystem.getExittime());
                    delegainformation.setSupplierinfor_id(projectsystem.getSuppliernameid());
                    String admissiontimeMonth_s = DateUtil.format(delegainformation.getAdmissiontime(), "MM");
                    String exitimeMonth_s = DateUtil.format(delegainformation.getExittime(), "MM");
                    //入退场都不为空
                    if (projectsystem.getAdmissiontime() != null && projectsystem.getExittime() != null && projectsystem.getType().equals("1")) {
                        if (aprilFirst_d.after(delegainformation.getAdmissiontime()) && delegainformation.getExittime().after(marchLast_d)) {
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
                        } else if (delegainformation.getAdmissiontime().after(aprilFirst_d)
                                && delegainformation.getExittime().after(marchLast_d)
                                && (DateUtil.format(delegainformation.getAdmissiontime(), "YYYY").equals(thisYear_s))) {
                            //今年4月1号之后入场，明年3月末之后退场
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
                            //4月一号之后入场，3月末之前退场（本事业年度）(aaaa)
                        } else if (delegainformation.getAdmissiontime().after(aprilFirst_d) && delegainformation.getExittime().before(marchLast_d)) {
                            delegainformation.setYear(thisYear_s);
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
                            //4月1号之前入场，明年三月末之前退场
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
                        } else if (delegainformation.getAdmissiontime().after(aprilFirst_d)
                                && delegainformation.getExittime().before(marchThatFirst_d)
                                && DateUtil.format(delegainformation.getExittime(), "YYYY").equals(thatYear_s)) {
                            //111       本事业年度之后入场，跨事业年度退场
                            for (int m = 1; m < 13; m++) {
                                if (Integer.parseInt(admissiontimeMonth_s) <= m && m <= 3) {
                                    delegainformation.setYear(thisYear_s);
                                    if (m == 1) {
                                        delegainformation.setJanuary("1");
                                        delegainformation.setFebruary("1");
                                        delegainformation.setMarch("1");
                                    } else if (m == 2) {
                                        delegainformation.setFebruary("1");
                                        delegainformation.setMarch("1");
                                    } else if (m == 3) {
                                        delegainformation.setMarch("1");
                                    }
                                } else if (4 <= Integer.parseInt(admissiontimeMonth_s) && Integer.parseInt(admissiontimeMonth_s) <= m) {
                                    delegainformation.setYear(thisYear_s);
                                    if (m == 4) {
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
                                    } else if (m == 5) {
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
                                    } else if (m == 6) {
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
                                    } else if (m == 7) {
                                        delegainformation.setJuly("1");
                                        delegainformation.setAugust("1");
                                        delegainformation.setSeptember("1");
                                        delegainformation.setOctober("1");
                                        delegainformation.setNovember("1");
                                        delegainformation.setDecember("1");
                                        delegainformation.setJanuary("1");
                                        delegainformation.setFebruary("1");
                                        delegainformation.setMarch("1");
                                    } else if (m == 8) {
                                        delegainformation.setAugust("1");
                                        delegainformation.setSeptember("1");
                                        delegainformation.setOctober("1");
                                        delegainformation.setNovember("1");
                                        delegainformation.setDecember("1");
                                        delegainformation.setJanuary("1");
                                        delegainformation.setFebruary("1");
                                        delegainformation.setMarch("1");
                                    } else if (m == 9) {
                                        delegainformation.setSeptember("1");
                                        delegainformation.setOctober("1");
                                        delegainformation.setNovember("1");
                                        delegainformation.setDecember("1");
                                        delegainformation.setJanuary("1");
                                        delegainformation.setFebruary("1");
                                        delegainformation.setMarch("1");
                                    } else if (m == 10) {
                                        delegainformation.setOctober("1");
                                        delegainformation.setNovember("1");
                                        delegainformation.setDecember("1");
                                        delegainformation.setJanuary("1");
                                        delegainformation.setFebruary("1");
                                        delegainformation.setMarch("1");
                                    } else if (m == 11) {
                                        delegainformation.setNovember("1");
                                        delegainformation.setDecember("1");
                                        delegainformation.setJanuary("1");
                                        delegainformation.setFebruary("1");
                                        delegainformation.setMarch("1");
                                    } else if (m == 12) {
                                        delegainformation.setDecember("1");
                                        delegainformation.setJanuary("1");
                                        delegainformation.setFebruary("1");
                                        delegainformation.setMarch("1");
                                    }
                                }
                            }
                        }
                    } else if (projectsystem.getAdmissiontime() != null && projectsystem.getType().equals("1")) {
                        if (aprilFirst_d.before(delegainformation.getAdmissiontime()) && marchLast_d.after(delegainformation.getAdmissiontime())) {
                            //入场时间为本事业年度4月1号之后，无退场时间
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
                        //今年三月份之后入场并且是明年3月三十一号之前（退场时间为空）(下一个事业年度)
                        if (delegainformation.getAdmissiontime().after(marchLast_d) && delegainformation.getAdmissiontime().before(marchThatFirst_d)) {
                            delegainformation.setYear(thatYear_s);
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
        }
        if (stageInformationList != null) {
            int rowundex = 0;
            for (StageInformation stageInformation : stageInformationList) {
                rowundex = rowundex + 1;
                stageInformation.preInsert(tokenModel);
                stageInformation.setStageinformation_id(UUID.randomUUID().toString());
                stageInformation.setCompanyprojects_id(companyprojectsid);
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
                projectcontract.setCompanyprojects_id(companyprojectsid);
                projectcontract.setRowindex(rowundex);
                projectcontractMapper.insertSelective(projectcontract);
            }
        }
        //ADD 03-18 ,委托元为内采时，合同可自行添加请求金额
        if (contractnumbercountList.size() > 0) {
            int rowindex = 0;
            for (Contractnumbercount contractnumbercount : contractnumbercountList) {
                rowindex = rowindex + 1;
                contractnumbercount.preInsert(tokenModel);
                contractnumbercount.setContractnumbercount_id(UUID.randomUUID().toString());
                contractnumbercount.setCompanyprojectsid(companyprojectsid);
                contractnumbercount.setRowindex(rowindex);
                contractnumbercountMapper.insertSelective(contractnumbercount);
            }
        }
        //ADD 03-18 ,委托元为内采时，合同可自行添加请求金额 END
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
        List<CompanyProjectsVo2> rst = companyprojectsMapper.getList(companyProjects.getOwners());
        return rst;
    }

    @Override
    public List<CompanyProjectsVo2> getSiteList2(CompanyProjects companyProjects) throws Exception {
        List<CompanyProjectsVo2> rst = companyprojectsMapper.getList(companyProjects.getOwners());
        List<CompanyProjectsVo2> rst2   =  companyprojectsMapper.getList4(companyProjects.getOwner());
        for(CompanyProjectsVo2 item:rst2){
            if(rst.stream().filter(item2 -> item2.getCompanyprojects_id().equals(item.getCompanyprojects_id())).count() == 0){
                rst.add(item);
            }
        }
        return rst;
    }

    @Override
    public List<CompanyProjectsVo2> getSiteList3(CompanyProjects companyProjects) throws Exception {
        List<CompanyProjectsVo2> rst = companyprojectsMapper.getList5(companyProjects.getOwners());
        List<CompanyProjectsVo2> rst2   = companyprojectsMapper.getList4(companyProjects.getOwner());
        for(CompanyProjectsVo2 item:rst2){
            if(rst.stream().filter(item2 -> item2.getCompanyprojects_id().equals(item.getCompanyprojects_id())).count() == 0){
                rst.add(item);
            }
        }
        return rst;
    }

    @Override
    public List<CompanyProjectsVo2> getList2(String flag,List<String> ownerList) throws Exception {
        if ("0".equals(flag)) {
            return companyprojectsMapper.getList2(ownerList);
        } else {
            return companyprojectsMapper.getList3(ownerList);
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
}
