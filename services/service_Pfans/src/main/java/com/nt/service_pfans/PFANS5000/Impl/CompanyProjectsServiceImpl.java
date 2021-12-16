package com.nt.service_pfans.PFANS5000.Impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.nt.dao_Auth.Role;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_Org.OrgTree;
import com.nt.dao_Org.ToDoNotice;
import com.nt.dao_Org.UserAccount;
import com.nt.dao_Org.Vo.UserVo;
import com.nt.dao_Pfans.PFANS1000.Award;
import com.nt.dao_Pfans.PFANS1000.Contractapplication;
import com.nt.dao_Pfans.PFANS1000.Contractnumbercount;
import com.nt.dao_Pfans.PFANS1000.Vo.ProjectIncomeVo4;
import com.nt.dao_Pfans.PFANS5000.*;
import com.nt.dao_Pfans.PFANS5000.Vo.*;
import com.nt.dao_Pfans.PFANS6000.Delegainformation;
import com.nt.dao_Pfans.PFANS6000.Expatriatesinfor;
import com.nt.dao_Pfans.PFANS8000.WorkingDay;
import com.nt.service_Auth.RoleService;
import com.nt.service_Org.DictionaryService;
import com.nt.service_Org.OrgTreeService;
import com.nt.service_Org.ToDoNoticeService;
import com.nt.service_Org.UserService;
import com.nt.service_pfans.PFANS1000.mapper.AwardMapper;
import com.nt.service_pfans.PFANS1000.mapper.ContractapplicationMapper;
import com.nt.service_pfans.PFANS1000.mapper.ContractnumbercountMapper;
import com.nt.service_pfans.PFANS2000.AnnualLeaveService;
import com.nt.service_pfans.PFANS5000.CompanyProjectsService;
import com.nt.service_pfans.PFANS5000.mapper.*;
import com.nt.service_pfans.PFANS6000.mapper.DelegainformationMapper;
import com.nt.service_pfans.PFANS6000.mapper.ExpatriatesinforMapper;
import com.nt.service_pfans.PFANS8000.mapper.WorkingDayMapper;
import com.nt.utils.LogicalException;
import com.nt.utils.StringUtils;
import com.nt.utils.dao.TimePair;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.impl.IsOverLapImpl;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


@Service
@Transactional(rollbackFor = Exception.class)
public class CompanyProjectsServiceImpl implements CompanyProjectsService {
    @Override
    public List<ProjectContract> selectAll() throws Exception {
        return projectcontractMapper.selectAll();
    }

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private CompanyProjectsMapper companyprojectsMapper;

    @Autowired
    private ToDoNoticeService toDoNoticeService;

    @Autowired
    private AnnualLeaveService annualLeaveService;

    @Autowired
    private OrgTreeService orgTreeService;

    @Autowired
    private UserService userService;

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
    @Autowired
    private ContractnumbercountMapper contractnumbercountMapper;
    @Autowired
    private RoleService roleService;
    //zy start 报表追加 2021/06/13
    @Autowired
    private WorkingDayMapper workingDayMapper;
    //zy end 报表追加 2021/06/13

    @Autowired
    private AwardMapper awardMapper;

    @Autowired
    private ContractapplicationMapper contractapplicationMapper;

    @Autowired
    private DictionaryService dictionaryService;

    @Override
    public List<CompanyProjects> getCompanyProjectList(CompanyProjects companyprojects, HttpServletRequest request) throws Exception {
        return companyprojectsMapper.select(companyprojects);
    }

    //pj起案数据结转
    @Override
    public void update1(CompanyProjects companyprojects, TokenModel tokenModel) throws Exception {
        CompanyProjects comp = companyprojectsMapper.selectByPrimaryKey(companyprojects);
        comp.setCenter_id(companyprojects.getCenter_id());
        comp.setGroup_id(companyprojects.getGroup_id());
        comp.setTeam_id(companyprojects.getTeam_id());
        comp.preUpdate(tokenModel);
        companyprojectsMapper.updateByPrimaryKey(comp);
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
    public List<CompanyProjects> getPjnameList6007(String account) throws Exception {
        List<CompanyProjects> companyProjectList = new ArrayList<CompanyProjects>();
        if(account!= null && !account.isEmpty())
        {
            List<Projectsystem> projectsystemList = new ArrayList<Projectsystem>();
            Projectsystem projectsystem = new Projectsystem();
            projectsystem.setName(account);
            projectsystemList = projectsystemMapper.select(projectsystem);
            for (int i = 0; i < projectsystemList.size(); i++)
            {
                CompanyProjects companyProjects = new CompanyProjects();
                companyProjects.setCompanyprojects_id(projectsystemList.get(i).getCompanyprojects_id());
                companyProjectList.add(companyprojectsMapper.selectByPrimaryKey(companyProjects));
            }
        }
        List<Comproject> comprojectList = new ArrayList<Comproject>();
        comprojectList = getPjnameList6007_1(account);
        if(comprojectList.size()>0)
        {
            for(Comproject comproject :comprojectList)
            {
                CompanyProjects companyP = new CompanyProjects();
                companyP.setNumbers(comproject.getNumbers());
                companyP.setProject_name(comproject.getProject_name());
                companyProjectList.add(companyP);
            }
        }

        return companyProjectList;
    }

    @Override
    public List<Comproject> getPjnameList6007_1(String account) throws Exception {
        List<Comproject> comprojectList = new ArrayList<Comproject>();
        if(account!= null && !account.isEmpty())
        {
            List<Prosystem> prosystemList = new ArrayList<Prosystem>();
            Prosystem prosystem = new Prosystem();
            prosystem.setName(account);
            prosystemList = prosystemMapper.select(prosystem);
            for (int i = 0; i < prosystemList.size(); i++)
            {
                Comproject comproject = new Comproject();
                comproject.setComproject_id(prosystemList.get(i).getComproject_id());
                comprojectList.add(comProjectMapper.selectByPrimaryKey(comproject));
            }
        }
        return comprojectList;
    }

    @Override
    public List<CompanyProjects> listPsdcd(String numbers) throws Exception {
        List<CompanyProjects> companyProjectList = new ArrayList<CompanyProjects>();
        CompanyProjects companyProjects = new CompanyProjects();
        companyProjects.setNumbers(numbers);
        companyProjectList = companyprojectsMapper.select(companyProjects);
        if(companyProjectList.size()==0)
        {
            List<Comproject> compjectList = new ArrayList<Comproject>();
            Comproject comproject = new Comproject();
            comproject.setNumbers(numbers);
            compjectList = comProjectMapper.select(comproject);
            if(compjectList.size()>0)
            {
                CompanyProjects com = new CompanyProjects();
                com.setLeaderid(compjectList.get(0).getLeaderid());
                companyProjectList.add(com);
            }
        }
        return companyProjectList;
    }

    @Override
    public Contractnumbercount selectConnumList(String contractnumbercount_id) throws Exception {
        Contractnumbercount contractnumbercount = new Contractnumbercount();
        contractnumbercount = contractnumbercountMapper.selectByPrimaryKey(contractnumbercount_id);
        return contractnumbercount;
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
        //可以进行重复选择，只需要做进组退组时间不重复的check ztc
        //region scc add 同委托元多条构外人月数之和与总人月数check from
        List<Projectsystem> projectsystemallList = companyProjectsVo.getProjectsystem();//获取项目体制
        Map<String, List<Projectsystem>> check = projectsystemallList.stream().filter(item -> "2".equals(item.getType())).collect(Collectors.groupingBy(Projectsystem::getContractno));//构外
        Iterator<Map.Entry<String, List<Projectsystem>>> it = check.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, List<Projectsystem>> entry = it.next();
            BigDecimal all = new BigDecimal(BigInteger.ZERO);
            if(entry.getValue().get(0).getTotalnumber() == null || entry.getValue().get(0).getTotalnumber() == ""){
                continue;
            }else{
                all = new BigDecimal(entry.getValue().get(0).getTotalnumber());//一个委托元对应总人月数
            }
            BigDecimal temp = new BigDecimal(BigInteger.ZERO);
            for (Projectsystem item : entry.getValue()) {
                temp = temp.add(new BigDecimal(item.getNumberofmonths())).setScale(2, BigDecimal.ROUND_HALF_UP);//同委托元多条构外人月数之和
            }
            if(temp.compareTo(all) == 1){//如果每条之和大于总人月数check
                throw new LogicalException("人月数总和不能超过合同人月数，请重新输入");
            }else{
                continue;
            }
        }
        //endregion scc add 同委托元多条构外人月数之和与总人月数check to
        projectsystemallList = projectsystemallList.stream().filter(item -> !"2".equals(item.getType())).collect(Collectors.toList());//构外不做进出场时间check
//        this.checkDupSystem(companyProjectsVo.getProjectsystem());
        this.checkDupSystem(projectsystemallList);
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
//            List<Projectsystem> pps = projectsystemMapper.select(projectsystem);
//            if(pps.size() > 0){
//                pps = pps.stream().filter(item -> item.getType().equals("0")).collect(Collectors.toList());
//            }
            projectsystemMapper.delete(projectsystem);
//            List<Projectsystem> ps = projectsystemMapper.select(projectsystem);
//            for(Projectsystem item:ps){
//                item.setStatus(AuthConstants.DEL_FLAG_DELETE);
//                projectsystemMapper.updateByPrimaryKey(item);
//            }
            Delegainformation delegainformation1 = new Delegainformation();
            delegainformation1.setCompanyprojects_id(companyprojectsid);
            delegainformationMapper.delete(delegainformation1);
            int rowundex = 0;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            //2019
            //String thisYear_s = String.valueOf(Integer.parseInt(DateUtil.format(new Date(), "YYYY")) - 1);
            String thisYear_s = DateUtil.format(new Date(), "yyyy");
            //2020
//            int thatYear_i = Integer.parseInt(thisYear_s) + 1;
//            String thatYear_s = String.valueOf(thatYear_i);
//            int tThatYear_i = thatYear_i + 1;
//            String tThatYear_s = String.valueOf(tThatYear_i);
//            int tThatYearThat_i = tThatYear_i + 1;
//            String tThatYeatThat_s = String.valueOf(tThatYearThat_i);
//            String initial_s_01 = "0401";
//            String initial_s_02 = "0331";
//            //今年四月一号
//            String aprilFirst_s = thisYear_s + initial_s_01;
//            Date aprilFirst_d = sdf.parse(aprilFirst_s);
//            //明年3月31日
//            String marchLast_s = thatYear_s + initial_s_02;
//            Date marchLast_d = sdf.parse(marchLast_s);
            //后年4月1日
//            String marchThatLast_s = tThatYear_s + initial_s_01;
//            Date marchThatLast_d = sdf.parse(marchThatLast_s);
            //大后年3月31日
//            String marchThatFirst_s = tThatYeatThat_s + initial_s_02;
//            Date marchThatFirst_d = sdf.parse(marchThatFirst_s);
            for (Projectsystem pro : projectsystemList) {
                //add-ws-4/23-体制表社内根据name_id有无进行判断，社外根据name判断
                if(pro.getType().equals("1")){
                    if(pro.getName_id()!=""&&pro.getName_id()!=null){
                        rowundex = rowundex + 1;
                        pro.preInsert(tokenModel);
                        pro.setProjectsystem_id(UUID.randomUUID().toString());
                        pro.setCompanyprojects_id(companyprojectsid);
                        pro.setRowindex(rowundex);
                        projectsystemMapper.insertSelective(pro);
                    }
                }else if(pro.getType().equals("0")){
                    if(pro.getName()!=""&&pro.getName()!=null){
                        rowundex = rowundex + 1;
                        pro.preInsert(tokenModel);
                        pro.setProjectsystem_id(UUID.randomUUID().toString());
                        pro.setCompanyprojects_id(companyprojectsid);
                        pro.setRowindex(rowundex);
                        projectsystemMapper.insertSelective(pro);
                        //add_fjl_07/03 start PL权限相关

                        if (companyProjects.getStatus().equals("4")) {
                            //先收回PL权限
//                            for(Projectsystem p:pps){
                            List<Projectsystem> lsp = getNamePl(pro.getName());
                            //只在一个项目中担任PL，离场时间小于当前日期，收回PL权限
                            if (lsp.size() == 1 && lsp.get(0).getPosition().toUpperCase().equals("PL") && Integer.parseInt(sdf.format(lsp.get(0).getExittime())) < Integer.parseInt(sdf.format(new Date()))) {
                                Query query = new Query();
                                String pname = pro.getName();
                                query.addCriteria(Criteria.where("_id").is(pname));
                                UserAccount accountn = mongoTemplate.findOne(query, UserAccount.class);
                                if (accountn != null) {
                                    List<Role> stringLis = new ArrayList<>();
                                    stringLis = accountn.getRoles();
                                    //5efe9877d526bf1c94d2b7ce
                                    stringLis = stringLis.stream().filter(item -> !item.get_id().equals("5f101f889729aa2a0c824c97")).collect(Collectors.toList());
                                    accountn.setRoles(stringLis);
                                    mongoTemplate.save(accountn);
                                }
                            }
//                            }
                            if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(pro.getPosition()) && pro.getPosition().toUpperCase().equals("PL") && Integer.parseInt(sdf.format(lsp.get(0).getExittime())) >= Integer.parseInt(sdf.format(new Date()))) {
                                Query query = new Query();
                                List<Role> rlAll = new ArrayList<>();
                                List<Role> rl = new ArrayList<>();
                                Role role = new Role();
                                query.addCriteria(Criteria.where("_id").is(pro.getName()));
                                UserAccount account = mongoTemplate.findOne(query, UserAccount.class);
                                //添加PL权限
                                if (account != null) {
                                    role.set_id("5f101f889729aa2a0c824c97");//5efe9877d526bf1c94d2b7ce
                                    role.setRolename("PL权限");
                                    role.setDescription("PL权限");
                                    role.setDefaultrole("true");
                                    rl.add(role);
                                    rlAll = account.getRoles();
                                    String rlid = "";
                                    if (rlAll.size() > 0) {
                                        for (int i = 0; i < rlAll.size(); i++) {
                                            rlid += rlAll.get(i).get_id() + ",";
                                        }
                                        //判断是否已经有PL角色了
                                        if (rlid.indexOf(role.get_id()) == -1) {
                                            rlAll.addAll(rl);
                                        }
                                    }
                                    account.setRoles(rlAll);
                                    account.setStatus("0");
                                    mongoTemplate.save(account);
                                }
                            }
                        }
                        //status = 9;结项了收回PL权限
                        if (companyProjects.getStatus().equals("9")) {
                            if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(pro.getPosition()) && pro.getPosition().toUpperCase().equals("PL")) {
                                Query query = new Query();
                                query.addCriteria(Criteria.where("_id").is(pro.getName()));
                                UserAccount account = mongoTemplate.findOne(query, UserAccount.class);
                                List<Projectsystem> ls = getNamePl(pro.getName());
                                if (ls.size() == 1) {
                                    //判断是否在其他项目中还有PL权限
//                                    if(sdf.format(ls.get(0).getExittime()).equals(sdf.format(pro.getExittime()))) {
                                    if (account != null) {
                                        List<Role> stringList = new ArrayList<>();
                                        stringList = account.getRoles();
                                        //5efe9877d526bf1c94d2b7ce
                                        stringList = stringList.stream().filter(item -> !item.get_id().equals("5f101f889729aa2a0c824c97")).collect(Collectors.toList());
                                        account.setRoles(stringList);
                                        mongoTemplate.save(account);
                                    }
                                }
                            }
                        }
                        //add_fjl_07/03 end  PL权限相关
                    }
                }
                //region scc add 插入构外 from
                else if ("2".equals(pro.getType())) {
                    rowundex = rowundex + 1;
                    pro.preInsert(tokenModel);
                    pro.setProjectsystem_id(UUID.randomUUID().toString());
                    pro.setCompanyprojects_id(companyprojectsid);
                    pro.setRowindex(rowundex);
                    projectsystemMapper.insertSelective(pro);
                }
                //endregion scc add 插入构外 to
                //add-ws-4/23-体制表社内根据name_id有无进行判断，社外根据name判断

                //活用情报
                if (pro.getAdmissiontime() != null && pro.getType().equals("1")) {

                    String supplierinfor_id = "";
                    if(pro.getName()  != null){
                        Expatriatesinfor expatriatesinfor = new Expatriatesinfor();
                        expatriatesinfor.setAccount(pro.getName());
                        List<Expatriatesinfor> expatriatesinforList = expatriatesinforMapper.select(expatriatesinfor);
                        if (expatriatesinforList != null) {
                            if (expatriatesinforList.size() > 0) {
                                supplierinfor_id = expatriatesinforList.get(0).getSupplierinfor_id();
                            }
                        }
                    }
                    Delegainformation delegainformation = new Delegainformation();
                    delegainformation.preInsert(tokenModel);
                    delegainformation.setSupplierinfor_id(supplierinfor_id);
                    delegainformation.setGroup_id(companyProjects.getGroup_id());
                    delegainformation.setDelegainformation_id(UUID.randomUUID().toString());
                    delegainformation.setCompanyprojects_id(companyprojectsid);
                    delegainformation.setProjectsystem_id(pro.getProjectsystem_id());
                    delegainformation.setAdmissiontime(pro.getAdmissiontime());
                    delegainformation.setExittime(pro.getExittime());
                    delegainformation.setProjectsystem_id(pro.getProjectsystem_id());
                    delegainformation.setAccount(pro.getName());
                    //String admissiontimeMonth_s = DateUtil.format(delegainformation.getAdmissiontime(), "MM");
                    //String exitimeMonth_s = DateUtil.format(delegainformation.getExittime(), "MM");
                    //入退场都不为空
//                    if (pro.getAdmissiontime() != null && pro.getExittime() != null) {
//
//                    }
                    //region 无用代码
                    //                        if (aprilFirst_d.after(delegainformation.getAdmissiontime()) && delegainformation.getExittime().after(marchLast_d)) {
//                            //本事业年度都在此工作
//                            delegainformation.setApril("1");
//                            delegainformation.setMay("1");
//                            delegainformation.setJune("1");
//                            delegainformation.setJuly("1");
//                            delegainformation.setAugust("1");
//                            delegainformation.setSeptember("1");
//                            delegainformation.setOctober("1");
//                            delegainformation.setNovember("1");
//                            delegainformation.setDecember("1");
//                            delegainformation.setJanuary("1");
//                            delegainformation.setFebruary("1");
//                            delegainformation.setMarch("1");
//                            delegainformation.setYear(thisYear_s);
//                        } else if (delegainformation.getAdmissiontime().after(aprilFirst_d)
//                                && delegainformation.getExittime().after(marchLast_d)
//                                && (DateUtil.format(delegainformation.getAdmissiontime(), "YYYY").equals(thisYear_s))) {
//                            //今年4月1号之后入场，明年3月末之后退场
//                            delegainformation.setYear(thisYear_s);
//                            if (admissiontimeMonth_s.equals("04")) {
//                                delegainformation.setApril("1");
//                                delegainformation.setMay("1");
//                                delegainformation.setJune("1");
//                                delegainformation.setJuly("1");
//                                delegainformation.setAugust("1");
//                                delegainformation.setSeptember("1");
//                                delegainformation.setOctober("1");
//                                delegainformation.setNovember("1");
//                                delegainformation.setDecember("1");
//                                delegainformation.setJanuary("1");
//                                delegainformation.setFebruary("1");
//                                delegainformation.setMarch("1");
//                            } else if (admissiontimeMonth_s.equals("05")) {
//                                delegainformation.setMay("1");
//                                delegainformation.setJune("1");
//                                delegainformation.setJuly("1");
//                                delegainformation.setAugust("1");
//                                delegainformation.setSeptember("1");
//                                delegainformation.setOctober("1");
//                                delegainformation.setNovember("1");
//                                delegainformation.setDecember("1");
//                                delegainformation.setJanuary("1");
//                                delegainformation.setFebruary("1");
//                                delegainformation.setMarch("1");
//                            } else if (admissiontimeMonth_s.equals("06")) {
//                                delegainformation.setJune("1");
//                                delegainformation.setJuly("1");
//                                delegainformation.setAugust("1");
//                                delegainformation.setSeptember("1");
//                                delegainformation.setOctober("1");
//                                delegainformation.setNovember("1");
//                                delegainformation.setDecember("1");
//                                delegainformation.setJanuary("1");
//                                delegainformation.setFebruary("1");
//                                delegainformation.setMarch("1");
//                            } else if (admissiontimeMonth_s.equals("07")) {
//                                delegainformation.setJuly("1");
//                                delegainformation.setAugust("1");
//                                delegainformation.setSeptember("1");
//                                delegainformation.setOctober("1");
//                                delegainformation.setNovember("1");
//                                delegainformation.setDecember("1");
//                                delegainformation.setJanuary("1");
//                                delegainformation.setFebruary("1");
//                                delegainformation.setMarch("1");
//                            } else if (admissiontimeMonth_s.equals("08")) {
//                                delegainformation.setAugust("1");
//                                delegainformation.setSeptember("1");
//                                delegainformation.setOctober("1");
//                                delegainformation.setNovember("1");
//                                delegainformation.setDecember("1");
//                                delegainformation.setJanuary("1");
//                                delegainformation.setFebruary("1");
//                                delegainformation.setMarch("1");
//                            } else if (admissiontimeMonth_s.equals("09")) {
//                                delegainformation.setSeptember("1");
//                                delegainformation.setOctober("1");
//                                delegainformation.setNovember("1");
//                                delegainformation.setDecember("1");
//                                delegainformation.setJanuary("1");
//                                delegainformation.setFebruary("1");
//                                delegainformation.setMarch("1");
//                            } else if (admissiontimeMonth_s.equals("10")) {
//                                delegainformation.setOctober("1");
//                                delegainformation.setNovember("1");
//                                delegainformation.setDecember("1");
//                                delegainformation.setJanuary("1");
//                                delegainformation.setFebruary("1");
//                                delegainformation.setMarch("1");
//                            } else if (admissiontimeMonth_s.equals("11")) {
//                                delegainformation.setNovember("1");
//                                delegainformation.setDecember("1");
//                                delegainformation.setJanuary("1");
//                                delegainformation.setFebruary("1");
//                                delegainformation.setMarch("1");
//                            } else if (admissiontimeMonth_s.equals("12")) {
//                                delegainformation.setDecember("1");
//                                delegainformation.setJanuary("1");
//                                delegainformation.setFebruary("1");
//                                delegainformation.setMarch("1");
//                            } else if (admissiontimeMonth_s.equals("01")) {
//                                delegainformation.setJanuary("1");
//                                delegainformation.setFebruary("1");
//                                delegainformation.setMarch("1");
//                            } else if (admissiontimeMonth_s.equals("02")) {
//                                delegainformation.setFebruary("1");
//                                delegainformation.setMarch("1");
//                            } else if (admissiontimeMonth_s.equals("03")) {
//                                delegainformation.setMarch("1");
//                            }
//                            //4月一号之后入场，3月末之前退场（本事业年度）(aaaa)
//                        } else if (delegainformation.getAdmissiontime().after(aprilFirst_d) && delegainformation.getExittime().before(marchLast_d)) {
//                            delegainformation.setYear(thisYear_s);
//                            for (int m = 1; m < 13; m++) {
//                                if (m >= Integer.parseInt(admissiontimeMonth_s) && m <= Integer.parseInt(exitimeMonth_s)) {
//                                    if (m == 4) {
//                                        //入退场月份都为4月份
//                                        if (admissiontimeMonth_s.equals("04") && admissiontimeMonth_s.equals(exitimeMonth_s)) {
//                                            delegainformation.setApril("1");
//                                            //仅入场时间为4月份
//                                        } else if (admissiontimeMonth_s.equals("04")) {
//                                            delegainformation.setApril("1");
//                                            //仅退场时间为4月份
//                                        } else if (exitimeMonth_s.equals("04")) {
//                                            delegainformation.setApril("1");
//                                            //入退场都不为4月份
//                                        } else {
//                                            delegainformation.setApril("1");
//                                        }
//                                    } else if (m == 5) {
//                                        //入退场月份都为5月份
//                                        if (admissiontimeMonth_s.equals("05") && admissiontimeMonth_s.equals(exitimeMonth_s)) {
//                                            delegainformation.setMay("1");
//                                            //仅入场时间为5月份
//                                        } else if (admissiontimeMonth_s.equals("05")) {
//                                            delegainformation.setMay("1");
//                                            //仅退场时间为5月份
//                                        } else if (exitimeMonth_s.equals("05")) {
//                                            delegainformation.setMay("1");
//                                            //入退场都不为5月份
//                                        } else {
//                                            delegainformation.setMay("1");
//                                        }
//                                    } else if (m == 6) {
//                                        //入退场月份都为6月份
//                                        if (admissiontimeMonth_s.equals("06") && admissiontimeMonth_s.equals(exitimeMonth_s)) {
//                                            delegainformation.setJune("1");
//                                            //仅入场时间为6月份
//                                        } else if (admissiontimeMonth_s.equals("06")) {
//                                            delegainformation.setJune("1");
//                                            //仅退场时间为6月份
//                                        } else if (exitimeMonth_s.equals("06")) {
//                                            delegainformation.setJune("1");
//                                            //入退场都不为6月份
//                                        } else {
//                                            delegainformation.setJune("1");
//                                        }
//                                    } else if (m == 7) {
//                                        //入退场月份都为7月份
//                                        if (admissiontimeMonth_s.equals("07") && admissiontimeMonth_s.equals(exitimeMonth_s)) {
//                                            delegainformation.setJuly("1");
//                                            //仅入场时间为7月份
//                                        } else if (admissiontimeMonth_s.equals("07")) {
//                                            delegainformation.setJuly("1");
//                                            //仅退场时间为7月份
//                                        } else if (exitimeMonth_s.equals("07")) {
//                                            delegainformation.setJuly("1");
//                                            //入退场都不为7月份
//                                        } else {
//                                            delegainformation.setJuly("1");
//                                        }
//                                    } else if (m == 8) {
//                                        //入退场月份都为8月份
//                                        if (admissiontimeMonth_s.equals("08") && admissiontimeMonth_s.equals(exitimeMonth_s)) {
//                                            delegainformation.setAugust("1");
//                                            //仅入场时间为8月份
//                                        } else if (admissiontimeMonth_s.equals("08")) {
//                                            delegainformation.setAugust("1");
//                                            //仅退场时间为8月份
//                                        } else if (exitimeMonth_s.equals("08")) {
//                                            delegainformation.setAugust("1");
//                                            //入退场都不为8月份
//                                        } else {
//                                            delegainformation.setAugust("1");
//                                        }
//                                    } else if (m == 9) {
//                                        //入退场月份都为9月份
//                                        if (admissiontimeMonth_s.equals("09") && admissiontimeMonth_s.equals(exitimeMonth_s)) {
//                                            delegainformation.setSeptember("1");
//                                            //仅入场时间为9月份
//                                        } else if (admissiontimeMonth_s.equals("09")) {
//                                            delegainformation.setSeptember("1");
//                                            //仅退场时间为9月份
//                                        } else if (exitimeMonth_s.equals("09")) {
//                                            delegainformation.setSeptember("1");
//                                            //入退场都不为9月份
//                                        } else {
//                                            delegainformation.setSeptember("1");
//                                        }
//                                    } else if (m == 10) {
//                                        //入退场月份都为10月份
//                                        if (admissiontimeMonth_s.equals("10") && admissiontimeMonth_s.equals(exitimeMonth_s)) {
//                                            delegainformation.setOctober("1");
//                                            //仅入场时间为10月份
//                                        } else if (admissiontimeMonth_s.equals("10")) {
//                                            delegainformation.setOctober("1");
//                                            //仅退场时间为10月份
//                                        } else if (exitimeMonth_s.equals("10")) {
//                                            delegainformation.setOctober("1");
//                                            //入退场都不为10月份
//                                        } else {
//                                            delegainformation.setOctober("1");
//                                        }
//                                    } else if (m == 11) {
//                                        //入退场月份都为11月份
//                                        if (admissiontimeMonth_s.equals("11") && admissiontimeMonth_s.equals(exitimeMonth_s)) {
//                                            delegainformation.setNovember("1");
//                                            //仅入场时间为11月份
//                                        } else if (admissiontimeMonth_s.equals("11")) {
//                                            delegainformation.setNovember("1");
//                                            //仅退场时间为11月份
//                                        } else if (exitimeMonth_s.equals("11")) {
//                                            delegainformation.setNovember("1");
//                                            //入退场都不为11月份
//                                        } else {
//                                            delegainformation.setNovember("1");
//                                        }
//                                    } else if (m == 12) {
//                                        //入退场月份都为12月份
//                                        if (admissiontimeMonth_s.equals("12") && admissiontimeMonth_s.equals(exitimeMonth_s)) {
//                                            delegainformation.setDecember("1");
//                                            //仅入场时间为12月份
//                                        } else if (admissiontimeMonth_s.equals("12")) {
//                                            delegainformation.setDecember("1");
//                                            //仅退场时间为12月份
//                                        } else if (exitimeMonth_s.equals("12")) {
//                                            delegainformation.setDecember("1");
//                                            //入退场都不为12月份
//                                        } else {
//                                            delegainformation.setDecember("1");
//                                        }
//                                    } else if (m == 1) {
//                                        //入退场月份都为1月份
//                                        if (admissiontimeMonth_s.equals("01") && admissiontimeMonth_s.equals(exitimeMonth_s)) {
//                                            delegainformation.setJanuary("1");
//                                            //仅入场时间为1月份
//                                        } else if (admissiontimeMonth_s.equals("01")) {
//                                            delegainformation.setJanuary("1");
//                                            //仅退场时间为1月份
//                                        } else if (exitimeMonth_s.equals("01")) {
//                                            delegainformation.setJanuary("1");
//                                            //入退场都不为1月份
//                                        } else {
//                                            delegainformation.setJanuary("1");
//                                        }
//                                    } else if (m == 2) {
//                                        //入退场月份都为2月份
//                                        if (admissiontimeMonth_s.equals("02") && admissiontimeMonth_s.equals(exitimeMonth_s)) {
//                                            delegainformation.setFebruary("1");
//                                            //仅入场时间为2月份
//                                        } else if (admissiontimeMonth_s.equals("02")) {
//                                            delegainformation.setFebruary("1");
//                                            //仅退场时间为2月份
//                                        } else if (exitimeMonth_s.equals("02")) {
//                                            delegainformation.setFebruary("1");
//                                            //入退场都不为2月份
//                                        } else {
//                                            delegainformation.setFebruary("1");
//                                        }
//                                    } else if (m == 3) {
//                                        //入退场月份都为3月份
//                                        if (admissiontimeMonth_s.equals("03") && admissiontimeMonth_s.equals(exitimeMonth_s)) {
//                                            delegainformation.setMarch("1");
//                                            //仅入场时间为3月份
//                                        } else if (admissiontimeMonth_s.equals("03")) {
//                                            delegainformation.setMarch("1");
//                                            //仅退场时间为3月份
//                                        } else if (exitimeMonth_s.equals("03")) {
//                                            delegainformation.setMarch("1");
//                                            //入退场都不为3月份
//                                        } else {
//                                            delegainformation.setFebruary("1");
//                                        }
//                                    }
//                                }
//                            }
//                            //4月1号之前入场，明年三月末之前退场
//                        } else if (delegainformation.getAdmissiontime().before(aprilFirst_d) && delegainformation.getExittime().before(marchLast_d)) {
//                            delegainformation.setYear(thisYear_s);
//                            if (exitimeMonth_s.equals("04")) {
//                                delegainformation.setApril("1");
//                                delegainformation.setMay("1");
//                                delegainformation.setJune("1");
//                                delegainformation.setJuly("1");
//                                delegainformation.setAugust("1");
//                                delegainformation.setSeptember("1");
//                                delegainformation.setOctober("1");
//                                delegainformation.setNovember("1");
//                                delegainformation.setDecember("1");
//                                delegainformation.setJanuary("1");
//                                delegainformation.setFebruary("1");
//                                delegainformation.setMarch("1");
//                            } else if (exitimeMonth_s.equals("05")) {
//                                delegainformation.setMay("1");
//                                delegainformation.setJune("1");
//                                delegainformation.setJuly("1");
//                                delegainformation.setAugust("1");
//                                delegainformation.setSeptember("1");
//                                delegainformation.setOctober("1");
//                                delegainformation.setNovember("1");
//                                delegainformation.setDecember("1");
//                                delegainformation.setJanuary("1");
//                                delegainformation.setFebruary("1");
//                                delegainformation.setMarch("1");
//                            } else if (exitimeMonth_s.equals("06")) {
//                                delegainformation.setJune("1");
//                                delegainformation.setJuly("1");
//                                delegainformation.setAugust("1");
//                                delegainformation.setSeptember("1");
//                                delegainformation.setOctober("1");
//                                delegainformation.setNovember("1");
//                                delegainformation.setDecember("1");
//                                delegainformation.setJanuary("1");
//                                delegainformation.setFebruary("1");
//                                delegainformation.setMarch("1");
//                            } else if (exitimeMonth_s.equals("07")) {
//                                delegainformation.setJuly("1");
//                                delegainformation.setAugust("1");
//                                delegainformation.setSeptember("1");
//                                delegainformation.setOctober("1");
//                                delegainformation.setNovember("1");
//                                delegainformation.setDecember("1");
//                                delegainformation.setJanuary("1");
//                                delegainformation.setFebruary("1");
//                                delegainformation.setMarch("1");
//                            } else if (exitimeMonth_s.equals("08")) {
//                                delegainformation.setAugust("1");
//                                delegainformation.setSeptember("1");
//                                delegainformation.setOctober("1");
//                                delegainformation.setNovember("1");
//                                delegainformation.setDecember("1");
//                                delegainformation.setJanuary("1");
//                                delegainformation.setFebruary("1");
//                                delegainformation.setMarch("1");
//                            } else if (exitimeMonth_s.equals("09")) {
//                                delegainformation.setSeptember("1");
//                                delegainformation.setOctober("1");
//                                delegainformation.setNovember("1");
//                                delegainformation.setDecember("1");
//                                delegainformation.setJanuary("1");
//                                delegainformation.setFebruary("1");
//                                delegainformation.setMarch("1");
//                            } else if (exitimeMonth_s.equals("10")) {
//                                delegainformation.setOctober("1");
//                                delegainformation.setNovember("1");
//                                delegainformation.setDecember("1");
//                                delegainformation.setJanuary("1");
//                                delegainformation.setFebruary("1");
//                                delegainformation.setMarch("1");
//                            } else if (exitimeMonth_s.equals("11")) {
//                                delegainformation.setNovember("1");
//                                delegainformation.setDecember("1");
//                                delegainformation.setJanuary("1");
//                                delegainformation.setFebruary("1");
//                                delegainformation.setMarch("1");
//                            } else if (exitimeMonth_s.equals("12")) {
//                                delegainformation.setDecember("1");
//                                delegainformation.setJanuary("1");
//                                delegainformation.setFebruary("1");
//                                delegainformation.setMarch("1");
//                            } else if (exitimeMonth_s.equals("01")) {
//                                delegainformation.setJanuary("1");
//                                delegainformation.setFebruary("1");
//                                delegainformation.setMarch("1");
//                            } else if (exitimeMonth_s.equals("02")) {
//                                delegainformation.setFebruary("1");
//                                delegainformation.setMarch("1");
//                            } else if (exitimeMonth_s.equals("03")) {
//                                delegainformation.setMarch("1");
//                            }
//                        } else if (delegainformation.getAdmissiontime().after(aprilFirst_d)
//                                && delegainformation.getExittime().before(marchThatFirst_d)
//                                && DateUtil.format(delegainformation.getExittime(), "YYYY").equals(thatYear_s)) {
//                            //111       本事业年度之后入场，跨事业年度退场
//                            for (int m = 1; m < 13; m++) {
//                                if (Integer.parseInt(admissiontimeMonth_s) <= m && m <= 3) {
//                                    delegainformation.setYear(thisYear_s);
//                                    if (m == 1) {
//                                        delegainformation.setJanuary("1");
//                                        delegainformation.setFebruary("1");
//                                        delegainformation.setMarch("1");
//                                    } else if (m == 2) {
//                                        delegainformation.setFebruary("1");
//                                        delegainformation.setMarch("1");
//                                    } else if (m == 3) {
//                                        delegainformation.setMarch("1");
//                                    }
//                                } else if (4 <= Integer.parseInt(admissiontimeMonth_s) && Integer.parseInt(admissiontimeMonth_s) <= m) {
//                                    delegainformation.setYear(thisYear_s);
//                                    if (m == 4) {
//                                        delegainformation.setApril("1");
//                                        delegainformation.setMay("1");
//                                        delegainformation.setJune("1");
//                                        delegainformation.setJuly("1");
//                                        delegainformation.setAugust("1");
//                                        delegainformation.setSeptember("1");
//                                        delegainformation.setOctober("1");
//                                        delegainformation.setNovember("1");
//                                        delegainformation.setDecember("1");
//                                        delegainformation.setJanuary("1");
//                                        delegainformation.setFebruary("1");
//                                        delegainformation.setMarch("1");
//                                    } else if (m == 5) {
//                                        delegainformation.setMay("1");
//                                        delegainformation.setJune("1");
//                                        delegainformation.setJuly("1");
//                                        delegainformation.setAugust("1");
//                                        delegainformation.setSeptember("1");
//                                        delegainformation.setOctober("1");
//                                        delegainformation.setNovember("1");
//                                        delegainformation.setDecember("1");
//                                        delegainformation.setJanuary("1");
//                                        delegainformation.setFebruary("1");
//                                        delegainformation.setMarch("1");
//                                    } else if (m == 6) {
//                                        delegainformation.setJune("1");
//                                        delegainformation.setJuly("1");
//                                        delegainformation.setAugust("1");
//                                        delegainformation.setSeptember("1");
//                                        delegainformation.setOctober("1");
//                                        delegainformation.setNovember("1");
//                                        delegainformation.setDecember("1");
//                                        delegainformation.setJanuary("1");
//                                        delegainformation.setFebruary("1");
//                                        delegainformation.setMarch("1");
//                                    } else if (m == 7) {
//                                        delegainformation.setJuly("1");
//                                        delegainformation.setAugust("1");
//                                        delegainformation.setSeptember("1");
//                                        delegainformation.setOctober("1");
//                                        delegainformation.setNovember("1");
//                                        delegainformation.setDecember("1");
//                                        delegainformation.setJanuary("1");
//                                        delegainformation.setFebruary("1");
//                                        delegainformation.setMarch("1");
//                                    } else if (m == 8) {
//                                        delegainformation.setAugust("1");
//                                        delegainformation.setSeptember("1");
//                                        delegainformation.setOctober("1");
//                                        delegainformation.setNovember("1");
//                                        delegainformation.setDecember("1");
//                                        delegainformation.setJanuary("1");
//                                        delegainformation.setFebruary("1");
//                                        delegainformation.setMarch("1");
//                                    } else if (m == 9) {
//                                        delegainformation.setSeptember("1");
//                                        delegainformation.setOctober("1");
//                                        delegainformation.setNovember("1");
//                                        delegainformation.setDecember("1");
//                                        delegainformation.setJanuary("1");
//                                        delegainformation.setFebruary("1");
//                                        delegainformation.setMarch("1");
//                                    } else if (m == 10) {
//                                        delegainformation.setOctober("1");
//                                        delegainformation.setNovember("1");
//                                        delegainformation.setDecember("1");
//                                        delegainformation.setJanuary("1");
//                                        delegainformation.setFebruary("1");
//                                        delegainformation.setMarch("1");
//                                    } else if (m == 11) {
//                                        delegainformation.setNovember("1");
//                                        delegainformation.setDecember("1");
//                                        delegainformation.setJanuary("1");
//                                        delegainformation.setFebruary("1");
//                                        delegainformation.setMarch("1");
//                                    } else if (m == 12) {
//                                        delegainformation.setDecember("1");
//                                        delegainformation.setJanuary("1");
//                                        delegainformation.setFebruary("1");
//                                        delegainformation.setMarch("1");
//                                    }
//                                }
//                            }
//                        }
//                    } else if (pro.getAdmissiontime() != null && pro.getType().equals("1")) {
//                        if (aprilFirst_d.before(delegainformation.getAdmissiontime()) && marchLast_d.after(delegainformation.getAdmissiontime())) {
//                            //入场时间为本事业年度4月1号之后，无退场时间
//                            delegainformation.setYear(thisYear_s);
//                            if (admissiontimeMonth_s.equals("04")) {
//                                delegainformation.setApril("1");
//                                delegainformation.setMay("1");
//                                delegainformation.setJune("1");
//                                delegainformation.setJuly("1");
//                                delegainformation.setAugust("1");
//                                delegainformation.setSeptember("1");
//                                delegainformation.setOctober("1");
//                                delegainformation.setNovember("1");
//                                delegainformation.setDecember("1");
//                                delegainformation.setJanuary("1");
//                                delegainformation.setFebruary("1");
//                                delegainformation.setMarch("1");
//                            } else if (admissiontimeMonth_s.equals("05")) {
//                                delegainformation.setMay("1");
//                                delegainformation.setJune("1");
//                                delegainformation.setJuly("1");
//                                delegainformation.setAugust("1");
//                                delegainformation.setSeptember("1");
//                                delegainformation.setOctober("1");
//                                delegainformation.setNovember("1");
//                                delegainformation.setDecember("1");
//                                delegainformation.setJanuary("1");
//                                delegainformation.setFebruary("1");
//                                delegainformation.setMarch("1");
//                            } else if (admissiontimeMonth_s.equals("06")) {
//                                delegainformation.setJune("1");
//                                delegainformation.setJuly("1");
//                                delegainformation.setAugust("1");
//                                delegainformation.setSeptember("1");
//                                delegainformation.setOctober("1");
//                                delegainformation.setNovember("1");
//                                delegainformation.setDecember("1");
//                                delegainformation.setJanuary("1");
//                                delegainformation.setFebruary("1");
//                                delegainformation.setMarch("1");
//                            } else if (admissiontimeMonth_s.equals("07")) {
//                                delegainformation.setJuly("1");
//                                delegainformation.setAugust("1");
//                                delegainformation.setSeptember("1");
//                                delegainformation.setOctober("1");
//                                delegainformation.setNovember("1");
//                                delegainformation.setDecember("1");
//                                delegainformation.setJanuary("1");
//                                delegainformation.setFebruary("1");
//                                delegainformation.setMarch("1");
//                            } else if (admissiontimeMonth_s.equals("08")) {
//                                delegainformation.setAugust("1");
//                                delegainformation.setSeptember("1");
//                                delegainformation.setOctober("1");
//                                delegainformation.setNovember("1");
//                                delegainformation.setDecember("1");
//                                delegainformation.setJanuary("1");
//                                delegainformation.setFebruary("1");
//                                delegainformation.setMarch("1");
//                            } else if (admissiontimeMonth_s.equals("09")) {
//                                delegainformation.setSeptember("1");
//                                delegainformation.setOctober("1");
//                                delegainformation.setNovember("1");
//                                delegainformation.setDecember("1");
//                                delegainformation.setJanuary("1");
//                                delegainformation.setFebruary("1");
//                                delegainformation.setMarch("1");
//                            } else if (admissiontimeMonth_s.equals("10")) {
//                                delegainformation.setOctober("1");
//                                delegainformation.setNovember("1");
//                                delegainformation.setDecember("1");
//                                delegainformation.setJanuary("1");
//                                delegainformation.setFebruary("1");
//                                delegainformation.setMarch("1");
//                            } else if (admissiontimeMonth_s.equals("11")) {
//                                delegainformation.setNovember("1");
//                                delegainformation.setDecember("1");
//                                delegainformation.setJanuary("1");
//                                delegainformation.setFebruary("1");
//                                delegainformation.setMarch("1");
//                            } else if (admissiontimeMonth_s.equals("12")) {
//                                delegainformation.setDecember("1");
//                                delegainformation.setJanuary("1");
//                                delegainformation.setFebruary("1");
//                                delegainformation.setMarch("1");
//                            } else if (admissiontimeMonth_s.equals("01")) {
//                                delegainformation.setJanuary("1");
//                                delegainformation.setFebruary("1");
//                                delegainformation.setMarch("1");
//                            } else if (admissiontimeMonth_s.equals("02")) {
//                                delegainformation.setFebruary("1");
//                                delegainformation.setMarch("1");
//                            } else if (admissiontimeMonth_s.equals("03")) {
//                                delegainformation.setMarch("1");
//                            }
//                        }
//                        //今年三月份之后入场并且是明年3月三十一号之前（退场时间为空）(下一个事业年度)
//                        if (delegainformation.getAdmissiontime().after(marchLast_d) && delegainformation.getAdmissiontime().before(marchThatFirst_d)) {
//                            delegainformation.setYear(thatYear_s);
//                            if (admissiontimeMonth_s.equals("04")) {
//                                delegainformation.setApril("1");
//                                delegainformation.setMay("1");
//                                delegainformation.setJune("1");
//                                delegainformation.setJuly("1");
//                                delegainformation.setAugust("1");
//                                delegainformation.setSeptember("1");
//                                delegainformation.setOctober("1");
//                                delegainformation.setNovember("1");
//                                delegainformation.setDecember("1");
//                                delegainformation.setJanuary("1");
//                                delegainformation.setFebruary("1");
//                                delegainformation.setMarch("1");
//                            } else if (admissiontimeMonth_s.equals("05")) {
//                                delegainformation.setMay("1");
//                                delegainformation.setJune("1");
//                                delegainformation.setJuly("1");
//                                delegainformation.setAugust("1");
//                                delegainformation.setSeptember("1");
//                                delegainformation.setOctober("1");
//                                delegainformation.setNovember("1");
//                                delegainformation.setDecember("1");
//                                delegainformation.setJanuary("1");
//                                delegainformation.setFebruary("1");
//                                delegainformation.setMarch("1");
//                            } else if (admissiontimeMonth_s.equals("06")) {
//                                delegainformation.setJune("1");
//                                delegainformation.setJuly("1");
//                                delegainformation.setAugust("1");
//                                delegainformation.setSeptember("1");
//                                delegainformation.setOctober("1");
//                                delegainformation.setNovember("1");
//                                delegainformation.setDecember("1");
//                                delegainformation.setJanuary("1");
//                                delegainformation.setFebruary("1");
//                                delegainformation.setMarch("1");
//                            } else if (admissiontimeMonth_s.equals("07")) {
//                                delegainformation.setJuly("1");
//                                delegainformation.setAugust("1");
//                                delegainformation.setSeptember("1");
//                                delegainformation.setOctober("1");
//                                delegainformation.setNovember("1");
//                                delegainformation.setDecember("1");
//                                delegainformation.setJanuary("1");
//                                delegainformation.setFebruary("1");
//                                delegainformation.setMarch("1");
//                            } else if (admissiontimeMonth_s.equals("08")) {
//                                delegainformation.setAugust("1");
//                                delegainformation.setSeptember("1");
//                                delegainformation.setOctober("1");
//                                delegainformation.setNovember("1");
//                                delegainformation.setDecember("1");
//                                delegainformation.setJanuary("1");
//                                delegainformation.setFebruary("1");
//                                delegainformation.setMarch("1");
//                            } else if (admissiontimeMonth_s.equals("09")) {
//                                delegainformation.setSeptember("1");
//                                delegainformation.setOctober("1");
//                                delegainformation.setNovember("1");
//                                delegainformation.setDecember("1");
//                                delegainformation.setJanuary("1");
//                                delegainformation.setFebruary("1");
//                                delegainformation.setMarch("1");
//                            } else if (admissiontimeMonth_s.equals("10")) {
//                                delegainformation.setOctober("1");
//                                delegainformation.setNovember("1");
//                                delegainformation.setDecember("1");
//                                delegainformation.setJanuary("1");
//                                delegainformation.setFebruary("1");
//                                delegainformation.setMarch("1");
//                            } else if (admissiontimeMonth_s.equals("11")) {
//                                delegainformation.setNovember("1");
//                                delegainformation.setDecember("1");
//                                delegainformation.setJanuary("1");
//                                delegainformation.setFebruary("1");
//                                delegainformation.setMarch("1");
//                            } else if (admissiontimeMonth_s.equals("12")) {
//                                delegainformation.setDecember("1");
//                                delegainformation.setJanuary("1");
//                                delegainformation.setFebruary("1");
//                                delegainformation.setMarch("1");
//                            } else if (admissiontimeMonth_s.equals("01")) {
//                                delegainformation.setJanuary("1");
//                                delegainformation.setFebruary("1");
//                                delegainformation.setMarch("1");
//                            } else if (admissiontimeMonth_s.equals("02")) {
//                                delegainformation.setFebruary("1");
//                                delegainformation.setMarch("1");
//                            } else if (admissiontimeMonth_s.equals("03")) {
//                                delegainformation.setMarch("1");
//                            }
//                        }
//                    }
                    //endregion
                    delegainformation.setYear(thisYear_s);
                    //delegainformationMapper.insertSelective(delegainformation);
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

    //add_fjl_07/07 start  PL权限相关
//    public List<Projectsystem> getPLinfo() throws Exception {
//        List<Projectsystem> projectsystemList = projectsystemMapper.getPLinfo();
//        return projectsystemList;
//    }
    //根据name获取职务和退场日期  asc降序
    public List<Projectsystem> getNamePl(String name) throws Exception {
        List<Projectsystem> list = projectsystemMapper.getNamePl(name);
        return list;
    }

    //到退场日，收回PL角色权限
    //【每天凌晨0点5分】
    @Scheduled(cron = "0 05 0 * * ?")
    public void getPLExittime() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        List<Projectsystem> projeclist = projectsystemMapper.getProsysList();
        if (projeclist != null && projeclist.size() > 0) {
            for (Projectsystem ps : projeclist) {
                if (StringUtils.isNotEmpty(ps.getPosition()) && ps.getPosition().toUpperCase().equals("PL")) {
                    Query query = new Query();
                    query.addCriteria(Criteria.where("userid").is(ps.getName()));
                    CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                    if (customerInfo != null) {
                        //如果已经离职，收回PL权限
                        if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(customerInfo.getUserinfo().getResignation_date())) {
                            Date temp = sdf.parse(customerInfo.getUserinfo().getResignation_date());
                            Calendar cld = Calendar.getInstance();
                            cld.setTime(temp);
                            cld.add(Calendar.DATE, 1);
                            temp = cld.getTime();
                            //获得下一天日期字符串
                            String regndate = sdf.format(temp);
                            if (Integer.parseInt(regndate) < Integer.parseInt(sdf.format(new Date()))) {
                                query.addCriteria(Criteria.where("_id").is(ps.getName()));
                                UserAccount account = mongoTemplate.findOne(query, UserAccount.class);
                                if (account != null) {
                                    List<Role> stringList = new ArrayList<>();
                                    stringList = account.getRoles();
                                    //5efe9877d526bf1c94d2b7ce
                                    stringList = stringList.stream().filter(item -> !item.get_id().equals("5f101f889729aa2a0c824c97")).collect(Collectors.toList());
                                    account.setRoles(stringList);
                                    mongoTemplate.save(account);
                                }
                            }
                        }
                    }
                    if (ps.getExittime() != null) {
                        List<Projectsystem> ls = getNamePl(ps.getName());
                        if (ls.size() > 0) {
                            if (Integer.parseInt(sdf.format(ls.get(0).getExittime())) > Integer.parseInt(sdf.format(new Date()))) {
                                query = new Query();
                                query.addCriteria(Criteria.where("_id").is(ps.getName()));
                                UserAccount account = mongoTemplate.findOne(query, UserAccount.class);
                                if (account != null) {
                                    List<Role> stringList = new ArrayList<>();
                                    stringList = account.getRoles();
                                    //5efe9877d526bf1c94d2b7ce
                                    stringList = stringList.stream().filter(item -> !item.get_id().equals("5f101f889729aa2a0c824c97")).collect(Collectors.toList());
                                    account.setRoles(stringList);
                                    mongoTemplate.save(account);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private OrgTree getCurrentOrg(OrgTree org, String orgId) throws Exception {
        if (org.get_id().equals(orgId)) {
            return org;
        } else {
            if (org.getOrgs() != null) {
                for (OrgTree item : org.getOrgs()) {
                    OrgTree or = getCurrentOrg(item, orgId);
                    if (or.get_id().equals(orgId)) {
                        return or;
                    }
                }
            }

        }
        return org;
    }
    //获取center上级
    private OrgTree getOrgCenterLeader(OrgTree org, String centerId) throws Exception {
        if (org.getOrgs() != null) {
            for (OrgTree item : org.getOrgs()) {
                if (item.get_id().equals(centerId)) {
                    return org;
                }
                if (item.getOrgs() != null) {
                    for (OrgTree center : item.getOrgs()) {
                        if (center.get_id().equals(centerId)) {
                            return item;
                        }
                    }
                }
            }
        }
        return org;
    }

    //给即将到退场日的PL的leader发代办
    //【每天凌晨0点10分】
    @Scheduled(cron = "0 10 0 * * ?")
    public void getPLLeader() throws Exception {
        TokenModel tokenModel = new TokenModel();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<Projectsystem> projeclist = projectsystemMapper.getProsysList();
        if (projeclist != null && projeclist.size() > 0) {
            projeclist = projeclist.stream().filter(item -> StringUtils.isNotEmpty(item.getPosition()) && item.getPosition().toUpperCase().equals("PL")).collect(Collectors.toList());
            if (projeclist != null && projeclist.size() > 0) {
                for (Projectsystem ps : projeclist) {
                    if (ps.getExittime() != null) {
//                    if(ps.getPosition().toUpperCase().equals("PL")){
                        //退场前7天发代办,之前的代码时间弄反了
                        //if (daysBetween(sdf.format(ps.getExittime()), sdf.format(new Date())) == 7) {
                        if (daysBetween(sdf.format(new Date()),sdf.format(ps.getExittime())) == 7) {
                            String comis = ps.getCompanyprojects_id();
                            CompanyProjects companyProjects = companyprojectsMapper.selectByPrimaryKey(comis);
                            //设计权限交接所改成owner
                            //UserVo userInfo = userService.getAccountCustomerById(companyProjects.getCreateby());
                            UserVo userInfo = userService.getAccountCustomerById(companyProjects.getOwner());
                            OrgTree orgs = orgTreeService.get(new OrgTree());
                            String flgid = "";
                            String curuser = "";
                            int flgroles = 0;//区分flg = 0 ？正式社员 ：（领导）
                            UserAccount us = userInfo.getUserAccount();
                            if (us != null) {
                                String ro = "";
                                for (int i = 0; i < us.getRoles().size(); i++) {
                                    ro += us.getRoles().get(i).getRolename() + ",";
                                }
                                if (ro.indexOf("总经理") != -1) {
                                    flgroles++;
                                } else if (ro.toUpperCase().indexOf("CENTER") != -1) {
                                    flgroles++;
                                } else if (ro.toUpperCase().indexOf("GM") != -1) {
                                    flgroles++;
                                } else if (ro.toUpperCase().indexOf("TL") != -1) {
                                    flgroles++;
                                }
                            }
                            CustomerInfo.UserInfo cus = userInfo.getCustomerInfo().getUserinfo();
                            if (cus != null) {
                                if (flgroles == 0) {
                                    if (cus.getTeamid() != null && StrUtil.isNotEmpty(cus.getTeamid())) {
                                        flgid = cus.getTeamid();
                                    } else if (cus.getGroupid() != null && StrUtil.isNotEmpty(cus.getGroupid())) {
                                        flgid = cus.getGroupid();
                                    } else if (cus.getCenterid() != null && StrUtil.isNotEmpty(cus.getCenterid())) {
                                        flgid = cus.getCenterid();
                                    }
                                } else {
                                    if (cus.getTeamid() != null && StrUtil.isNotEmpty(cus.getTeamid())) {
                                        flgid = cus.getGroupid();
                                    } else if (cus.getGroupid() != null && StrUtil.isNotEmpty(cus.getGroupid())) {
                                        flgid = cus.getCenterid();
                                    } else if (cus.getCenterid() != null && StrUtil.isNotEmpty(cus.getCenterid())) {
                                        //获取center的上级
                                        OrgTree OrgCenterLeader = getOrgCenterLeader(orgs, cus.getCenterid());
                                        if(OrgCenterLeader != null){
                                            flgid = OrgCenterLeader.get_id();
                                        }
                                    }
                                }
                            }
                            OrgTree currentOrg = getCurrentOrg(orgs, flgid);
                            if (currentOrg.getUser() != null && StrUtil.isNotEmpty(currentOrg.getUser())) {
                                curuser = currentOrg.getUser();
                            }
                            //给当前pl的leader发代办
                            ToDoNotice toDoNotice = new ToDoNotice();
                            toDoNotice.setTitle("项目编号[" + companyProjects.getNumbers() + "]的PL即将到期离场，请确认。");
                            toDoNotice.setInitiator(ps.getName());//当前PL
                            toDoNotice.setContent("项目编号[" + companyProjects.getNumbers() + "]的PL即将到期离场，请确认。");
                            toDoNotice.setDataid(companyProjects.getCompanyprojects_id());
                            toDoNotice.setUrl("/PFANS5009FormView");
                            toDoNotice.setWorkflowurl("/PFANS5009View");
                            toDoNotice.preInsert(tokenModel);
                            toDoNotice.setOwner(curuser);//起案人的直属上级
                            toDoNoticeService.save(toDoNotice);
                        }
                    }
                }
            }
        }
    }

    //zy start 报表追加 2021/06/13
    private List<ProjectIncomeVo4> getOrgInfo(String start) throws Exception {
        List<ProjectIncomeVo4> projectincomevo4list = new ArrayList<>();
        OrgTree org20s = new OrgTree();
        OrgTree org21s = new OrgTree();
        if(start.substring(0,4).equals("2020"))
        {
            org20s = orgTreeService.getTreeYears(start.substring(0,4),"0");
            org21s = orgTreeService.getTreeYears(String.valueOf(Integer.valueOf(start.substring(0,4))+1),"0");
        }
        else
        {
            org21s = orgTreeService.getTreeYears(start.substring(0,4),"0");
            org20s = orgTreeService.getTreeYears(String.valueOf(Integer.valueOf(start.substring(0,4))-1),"0");
        }

//        OrgTree orgs = orgTreeService.get(new OrgTree());
        for (OrgTree org : org21s.getOrgs()) {
                for (OrgTree orgC : org.getOrgs()) {
                    if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(orgC.getEncoding()))
                    {
                        ProjectIncomeVo4 projectincomevo4 = new ProjectIncomeVo4();
                        projectincomevo4.setGroupid(orgC.get_id());
                        projectincomevo4.setGroupname(orgC.getCompanyname());
                        projectincomevo4.setEncoding(orgC.getEncoding().substring(0, 2));
                        projectincomevo4.setFlag("0");
                        projectincomevo4list.add(projectincomevo4);
                    }
                    else
                    {
                        for (OrgTree orgG : orgC.getOrgs())
                        {
                            if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(orgG.getEncoding()))
                            {
                                ProjectIncomeVo4 projectincomevo4 = new ProjectIncomeVo4();
                                projectincomevo4.setGroupid(orgG.get_id());
                                projectincomevo4.setGroupname(orgG.getCompanyname());
                                projectincomevo4.setEncoding(orgG.getEncoding().substring(0, 2));
                                projectincomevo4.setFlag("1");
                                projectincomevo4list.add(projectincomevo4);
                            }
                        }
                    }

                }
            }
        for (OrgTree orgC : org20s.getOrgs()){
                    for (OrgTree orgG : orgC.getOrgs())
                    {
                        if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(orgG.getEncoding()))
                        {
                            ProjectIncomeVo4 projectincomevo4 = new ProjectIncomeVo4();
                            projectincomevo4.setGroupid(orgG.get_id());
                            projectincomevo4.setGroupname(orgG.getCompanyname());
                            projectincomevo4.setEncoding(orgG.getEncoding().substring(0, 2));
                            projectincomevo4.setFlag("1");
                            projectincomevo4list.add(projectincomevo4);
                        }
                    }
                }

        return projectincomevo4list;
    }

    @Override
    public List<Object> report(String start, String end, List<String> ownerList, String userId) throws Exception{
        List<ProjectIncomeVo4> projectincomevo4list = this.getOrgInfo(start);
        Map<String, String> params = new HashMap<>();
        params.put("start", start);
        params.put("end", end);
        List<CompanyProjectsReport> baseList = companyprojectsMapper.getRepotBaseData(params);

        List<Object> result = new ArrayList<>();

        for (CompanyProjectsReport baseData : baseList ) {
            result.addAll(extraBaseData(baseData, projectincomevo4list));
        }

        return result;
    }

    private List<Object> extraBaseData(CompanyProjectsReport baseData, List<ProjectIncomeVo4> projectincomevo4list) throws Exception {
        Calendar pos = Calendar.getInstance();
        pos.setTime(baseData.getStartdate());
        pos.set(pos.get(Calendar.YEAR), pos.get(Calendar.MONTH), 1);
        Date endDate = baseData.getEnddate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, String> logMonthTimeMap = baseData.toLogMonthTimeMap();
        List<Object> list = new ArrayList<>();
//        NumberFormat percent = NumberFormat.getPercentInstance();
//        percent.setMaximumFractionDigits(2);

        // 经费初始化
        List<Monthly> moneyList = companyprojectsMapper.getMoneysByProject(baseData.getCompanyprojects_id());
        Map<String, String> moneyMap = new HashMap<>();
        moneyMap = moneyList.stream().collect(Collectors.toMap(Monthly::getMonth, Monthly::getData));

        // 部门名
        String orgname = "";
        for(ProjectIncomeVo4 pr4 : projectincomevo4list)
        {
            if(pr4.getGroupid().equals(baseData.getCenter_id()) && pr4.getEncoding() != null)
            {
                orgname = pr4.getGroupname();
                break;
            }else if(pr4.getGroupid().equals(baseData.getGroup_id())) {
                orgname = pr4.getGroupname();
                break;
            }
        }

        Map<String, Double> shouldWorkTimeMap = getShouldWorkTime(baseData);

        while (!pos.getTime().after(endDate) ) {
            Map<String, Object> line = new HashMap<>();
            // 部门名
            line.put("orgname", orgname);
            line.put("groupId", baseData.getGroup_id());
            line.put("centerId", baseData.getCenter_id());
            // 项目名
            String proname = "";
            if(com.nt.utils.StringUtils.isBase64Encode(baseData.getProject_name())){
                proname = Base64.decodeStr(baseData.getProject_name());
            }else{
                proname = baseData.getProject_name();
            }
            line.put("name", proname);
            // 合同号
            if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(baseData.getContract()))
            {
                String []arr = baseData.getContract().split(",");
                Set set = new HashSet();
                for(int i=0;i < arr.length;i++ )
                {
                    set.add(arr[i]);
                }
                baseData.setContract("");
                for(int i=0;i < set.toArray().length;i++ )
                {
                    baseData.setContract(baseData.getContract() + set.toArray()[i]+",");
                }
                if(!baseData.getContract().equals(""))
                {
                    baseData.setContract(baseData.getContract().substring(0,baseData.getContract().length()-1));
                }
            }
            line.put("contract", baseData.getContract());
            // 分配金额
            line.put("contractamount", baseData.getContractamount());
            // 处理月数据
            String month = sdf.format(pos.getTime());
            // 月份
            line.put("month", month);
            // 出勤开始时间
            Date workStart = pos.after(baseData.getStartdate()) ? baseData.getStartdate() : pos.getTime();
            // 出勤结束时间(月末时间或者是结束时间)
            Date workEnd = getLastDateOfMonth(pos).before(baseData.getEnddate()) ? getLastDateOfMonth(pos) : baseData.getEnddate();

            //add ccm fr
            String projectId = baseData.getCompanyprojects_id();
            Projectsystem systemParam = new Projectsystem();
            systemParam.setCompanyprojects_id(projectId);

            List<Projectsystem> projectsystemList = projectsystemMapper.select(systemParam);

            for (Projectsystem projectsystem : projectsystemList)
            {
                if(projectsystem.getType().equals("0"))
                {
                    // 应出勤时间（员工）
                    String worktime = String.valueOf(shouldWorkTimeMap.getOrDefault(month + "_0" + "_"+ projectsystem.getName(), 0d) * 8);
                    BigDecimal workTimesOfMonth = new BigDecimal(worktime);

                    // 实际出勤时间（员工）
                    BigDecimal realWorkTime = new BigDecimal(logMonthTimeMap.getOrDefault(month + "_0" + "_"+ projectsystem.getName(), "0"));

                    // 工数（员工）
                    if(workTimesOfMonth.compareTo(BigDecimal.ZERO)==0){
                        line.put("personType0", 0);
                    }else {

                        BigDecimal personType0 = realWorkTime.divide(workTimesOfMonth,2,4);

                        if(line.containsKey("personType0"))
                        {
                            personType0 = personType0.add(new BigDecimal(String.valueOf(line.get("personType0"))));
                            line.put("personType0", personType0);
                        }
                        else
                        {
                            line.put("personType0", personType0);
                        }
                    }
                }
                else
                {
                    // 实际出勤时间（外驻）
                    BigDecimal realWorkTime2 = new BigDecimal(logMonthTimeMap.getOrDefault(month + "_1" + "_"+ projectsystem.getName(), "0"));
                    // 应出勤时间（外驻）
                    String worktime2 = String.valueOf(shouldWorkTimeMap.getOrDefault(month + "_1" + "_"+ projectsystem.getName(), 0d) * 8);
                    BigDecimal workTimesOfMonth2 = new BigDecimal(worktime2);
                    if(StringUtils.isNotEmpty(worktime2)) {
                        workTimesOfMonth2 = new BigDecimal(worktime2);
                    }
                    // 工数（外驻）
                    if(workTimesOfMonth2.compareTo(BigDecimal.ZERO)==0){
                        line.put("personType1", 0);
                    }else {
                        BigDecimal personType1 = realWorkTime2.divide(workTimesOfMonth2, 2,4);
                        if(line.containsKey("personType1"))
                        {
                            personType1 = personType1.add(new BigDecimal(String.valueOf(line.get("personType1"))));
                            line.put("personType1", personType1);
                        }
                        else
                        {
                            line.put("personType1", personType1);
                        }
                    }
                }
            }
            //add ccm to



            // 经费
            String moneys = moneyMap.getOrDefault(month, "0");
            BigDecimal moneysOfMonth = new BigDecimal(0);
            if(StringUtils.isNotEmpty(moneys)){
                moneysOfMonth = new BigDecimal(moneys);
            }

            line.put("moneys", moneysOfMonth);
            list.add(line);
            pos.add(Calendar.MONTH, 1);
        }
        return list;
    }

    private Set<Long> getSumDaySet(CompanyProjectsReport baseData) {
        Date startDate = baseData.getStartdate();
        Date endDate = baseData.getEnddate();
        SimpleDateFormat _sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<WorkingDay> workingDays = workingDayMapper.getWorkingday(_sdf.format(startDate), _sdf.format(endDate));
        Map<Date, String> workingDayMap = workingDays.stream().collect(Collectors.toMap(WorkingDay::getWorkingdate, WorkingDay::getType));
        Set<Long> sumDaySet = new HashSet<>();
        Calendar startPos = Calendar.getInstance();
        startPos.setTime(startDate);
        while ( startPos.getTimeInMillis() <= endDate.getTime() ) {
            int week = startPos.get(Calendar.DAY_OF_WEEK);
            String type = workingDayMap.get(startPos.getTime());
            if ( week == 0 || week == 6 ) {
                if ( StringUtils.isNotEmpty(type) && !"4".equals(type) ) {
                    sumDaySet.add(startPos.getTimeInMillis());
                }
            } else {
                if ( "4".equals(type) ) {
                    sumDaySet.add(startPos.getTimeInMillis());
                }
            }

            startPos.add(Calendar.DAY_OF_MONTH, 1);
        }
        return sumDaySet;
    }

    private Map<String, Double> getShouldWorkTime(CompanyProjectsReport baseData) throws Exception {
        Map<String, Double> result = new HashMap<>();

        Set<Long> sumDaySet = getSumDaySet(baseData);

        String projectId = baseData.getCompanyprojects_id();
        Projectsystem systemParam = new Projectsystem();
        systemParam.setCompanyprojects_id(projectId);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        List<Projectsystem> projectsystemList = projectsystemMapper.select(systemParam);
        for (Projectsystem projectsystem : projectsystemList) {
            Date sDate = projectsystem.getAdmissiontime();
            Date eDate = projectsystem.getExittime();
            String type = projectsystem.getType();
            Calendar pos = Calendar.getInstance();
            pos.setTime(sDate);
            while (!pos.getTime().after(eDate)) {
                String key = sdf.format(pos.getTime()) + "_" + type + "_"+ projectsystem.getName();
                Double oldDays = result.getOrDefault(key, 0d);
                Date lastMonthDate = getLastDateOfMonth(pos).after(eDate) ? eDate : getLastDateOfMonth(pos);
//
//                int workDays = 0;
                Calendar posMonth = Calendar.getInstance();
                posMonth.setTime(pos.getTime());
//                while (!posMonth.getTime().after(lastMonthDate)) {
//                    if ( !sumDaySet.contains(posMonth.getTimeInMillis()) ) {
//                        workDays++;
//                    }
                    SimpleDateFormat fy = new SimpleDateFormat("yyyy");
                    SimpleDateFormat fym = new SimpleDateFormat("yyyy-MM");
                    SimpleDateFormat fm = new SimpleDateFormat("MM");
                    SimpleDateFormat fymd = new SimpleDateFormat("yyyy-MM-dd");
                    String year = fy.format(posMonth.getTime());
                    if(Integer.valueOf(fm.format(posMonth.getTime())) < 4)
                    {
                        year = String.valueOf(Integer.valueOf(fy.format(posMonth.getTime())) - 1);
                    }
                    String days = "0";
                    days = annualLeaveService.workDayBymonth(fymd.format(posMonth.getTime()),fymd.format(lastMonthDate),year);
//                    posMonth.add(Calendar.DAY_OF_MONTH, 1);
//                }

                result.put(key, oldDays + Double.valueOf(days));
                pos.add(Calendar.MONTH, 1);
                pos.set(Calendar.DAY_OF_MONTH, 1);
            }
        }

        return result;
    }

    private Date getLastDateOfMonth(Calendar pos) {
        Calendar now = Calendar.getInstance();
        now.setTime(pos.getTime());
        now.set(Calendar.MONTH, now.get(Calendar.MONTH) + 1);
        now.set(Calendar.DAY_OF_MONTH, 1);
        now.add(Calendar.DAY_OF_MONTH, -1);
        return now.getTime();
    }
//zy end 报表追加 2021/06/13

    //计算日期相差天数
    public static int daysBetween(String smdate, String bdate) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(smdate));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(bdate));
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }
    //add_fjl_07/07 end  PL权限相关

    /**
     * 判断同一个人多个入退场是否有重叠（交集）
     * @return 返回是否重叠 true重叠 false不重叠
     */
    public void checkDupSystem(List<Projectsystem> checkDepSystem) throws Exception {
        //region scc add 构外开始事件和结束事件，存为表字段开始时间和结束时间，不参与进出场时间check from
        checkDepSystem = checkDepSystem.stream().filter(item -> !"2".equals(item.getType())).collect(Collectors.toList());
        //endregion scc add 构外开始事件和结束事件，存为表字段开始时间和结束时间，不参与进出场时间check to
        Map<String,List<Projectsystem>> groupSyeMap = checkDepSystem.stream()
                .collect(Collectors.groupingBy(Projectsystem::getName));
        for(Map.Entry<String,List<Projectsystem>> entryName : groupSyeMap.entrySet()){
            List<TimePair> timePairList = new ArrayList<>();
            String nameId = entryName.getKey();
            List<Projectsystem> proList = entryName.getValue();
            if(proList.size() > 1){
                proList.forEach(item ->{
                    TimePair timePair = new TimePair();
                    timePair.setStart(item.getAdmissiontime().getTime());
                    timePair.setEnd(item.getExittime().getTime());
                    timePairList.add(timePair);
                });
                Boolean dupResult = IsOverLapImpl.isOverlap(timePairList,true);
                if(dupResult){
                    String staffName = "";
                    if(proList.get(0).getType().equals("1")){
                        staffName = proList.get(0).getName_id();
                    }else{
                        Query cusquery = new Query();
                        cusquery.addCriteria(Criteria.where("userid").is(nameId));
                        CustomerInfo cus = mongoTemplate.findOne(cusquery, CustomerInfo.class);
                        if(cus != null){
                            staffName = cus.getUserinfo().getCustomername();
                        }
                    }
                    throw new LogicalException("体制中" + staffName + "进退场时间重复");
                }
            }
        }
    }

    //新建
    @Override
    public void insert(CompanyProjectsVo companyProjectsVo, TokenModel tokenModel) throws Exception {
        //可以进行重复选择，只需要做进组退组时间不重复的check ztc
        //region scc add 同委托元多条构外人月数之和与总人月数check from
        List<Projectsystem> projectsystemallList = companyProjectsVo.getProjectsystem();//获取项目体制
        Map<String, List<Projectsystem>> check = projectsystemallList.stream().filter(item -> "2".equals(item.getType())).collect(Collectors.groupingBy(Projectsystem::getContractno));//构外
        Iterator<Map.Entry<String, List<Projectsystem>>> it = check.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, List<Projectsystem>> entry = it.next();
            BigDecimal all = new BigDecimal(BigInteger.ZERO);
            if(entry.getValue().get(0).getTotalnumber() == null || entry.getValue().get(0).getTotalnumber() == ""){
                continue;
            }else{
                all = new BigDecimal(entry.getValue().get(0).getTotalnumber());//一个委托元对应总人月数
            }
            BigDecimal temp = new BigDecimal(BigInteger.ZERO);
            for (Projectsystem item : entry.getValue()) {
                temp = temp.add(new BigDecimal(item.getNumberofmonths())).setScale(2, BigDecimal.ROUND_HALF_UP);//同委托元多条构外人月数之和
            }
            if(temp.compareTo(all) == 1){//如果每条之和大于总人月数check
                throw new LogicalException("人月数总和不能超过合同人月数，请重新输入");
            }else{
                continue;
            }
        }
        //endregion scc add 同委托元多条构外人月数之和与总人月数check to
        projectsystemallList = projectsystemallList.stream().filter(item -> !"2".equals(item.getType())).collect(Collectors.toList());//构外不做进出场时间check
//        this.checkDupSystem(companyProjectsVo.getProjectsystem());
        this.checkDupSystem(projectsystemallList);
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
                    String checknumber = StringUtils.uncapitalize(StringUtils.substring(comprotect.getNumbers(), 2,10));
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
        Numbers = "PJ"+year+ no;
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
            //String thisYear_s = String.valueOf(Integer.parseInt(DateUtil.format(new Date(), "YYYY")) - 1);
            String thisYear_s = DateUtil.format(new Date(), "yyyy");
            //2020
//            int thatYear_i = Integer.parseInt(thisYear_s) + 1;
//            String thatYear_s = String.valueOf(thatYear_i);
//            int tThatYear_i = thatYear_i + 1;
//            String tThatYear_s = String.valueOf(tThatYear_i);
//            int tThatYearThat_i = tThatYear_i + 1;
//            String tThatYeatThat_s = String.valueOf(tThatYearThat_i);
//            String initial_s_01 = "0401";
//            String initial_s_02 = "0331";
            //今年四月一号
//            String aprilFirst_s = thisYear_s + initial_s_01;
//            Date aprilFirst_d = sdf.parse(aprilFirst_s);
            //明年3月31日
//            String marchLast_s = thatYear_s + initial_s_02;
//            Date marchLast_d = sdf.parse(marchLast_s);
            //后年4月1日
//            String marchThatLast_s = tThatYear_s + initial_s_01;
//            Date marchThatLast_d = sdf.parse(marchThatLast_s);
            //大后年3月31日
            //String marchThatFirst_s = tThatYeatThat_s + initial_s_02;
//            Date marchThatFirst_d = sdf.parse(marchThatFirst_s);
            int rowundex = 0;
            for (Projectsystem projectsystem : projectsystemList) {
                //add-ws-4/23-体制表社内根据name_id有无进行判断，社外根据name判断
                if(projectsystem.getType().equals("1")){
                    if(projectsystem.getName_id()!=""&&projectsystem.getName_id()!=null) {
                        rowundex = rowundex + 1;
                        projectsystem.preInsert(tokenModel);
                        projectsystem.setProjectsystem_id(UUID.randomUUID().toString());
                        projectsystem.setCompanyprojects_id(companyprojectsid);
                        projectsystem.setRowindex(rowundex);
                        projectsystemMapper.insertSelective(projectsystem);
                    }
                }else  if(projectsystem.getType().equals("0")){
                    if(projectsystem.getName()!=""&&projectsystem.getName()!=null) {
                        rowundex = rowundex + 1;
                        projectsystem.preInsert(tokenModel);
                        projectsystem.setProjectsystem_id(UUID.randomUUID().toString());
                        projectsystem.setCompanyprojects_id(companyprojectsid);
                        projectsystem.setRowindex(rowundex);
                        projectsystemMapper.insertSelective(projectsystem);
                    }
                }
                //region scc add 插入构外 from
                else if ("2".equals(projectsystem.getType())) {
                    rowundex = rowundex + 1;
                    projectsystem.preInsert(tokenModel);
                    projectsystem.setProjectsystem_id(UUID.randomUUID().toString());
                    projectsystem.setCompanyprojects_id(companyprojectsid);
                    projectsystem.setRowindex(rowundex);
                    projectsystemMapper.insertSelective(projectsystem);
                }
                //endregion scc add 插入构外 to
                //add-ws-4/23-体制表社内根据name_id有无进行判断，社外根据name判断
                //活用情报
                if (projectsystem.getAdmissiontime() != null && projectsystem.getType().equals("1")) {
                    //外驻
                    String supplierinfor_id = "";
                    String suppliernameid = projectsystem.getName();
                    Expatriatesinfor expatriatesinfor = new Expatriatesinfor();
                    expatriatesinfor.setAccount(suppliernameid);
                    List<Expatriatesinfor> expatriatesinforList = expatriatesinforMapper.select(expatriatesinfor);
                    if (expatriatesinforList != null) {
                        if(expatriatesinforList.size() >  0){
                            supplierinfor_id = expatriatesinforList.get(0).getSupplierinfor_id();
                            expatriatesinfor.setExpatriatesinfor_id(expatriatesinforList.get(0).getExpatriatesinfor_id());
                        }
                        expatriatesinfor.setProject_name(companyProjects.getProject_name());
                        expatriatesinfor.setManagerid(companyProjects.getManagerid());
                        expatriatesinfor.preUpdate(tokenModel);
                        expatriatesinforMapper.updateByPrimaryKeySelective(expatriatesinfor);
                    }
                    Delegainformation delegainformation = new Delegainformation();
                    delegainformation.preInsert(tokenModel);
                    delegainformation.setGroup_id(companyProjects.getGroup_id());
                    delegainformation.setDelegainformation_id(UUID.randomUUID().toString());
                    delegainformation.setCompanyprojects_id(companyprojectsid);
                    delegainformation.setProjectsystem_id(projectsystem.getProjectsystem_id());
                    delegainformation.setAdmissiontime(projectsystem.getAdmissiontime());
                    delegainformation.setExittime(projectsystem.getExittime());
                    delegainformation.setSupplierinfor_id(supplierinfor_id);
                    delegainformation.setAccount(supplierinfor_id);
//                    String admissiontimeMonth_s = DateUtil.format(delegainformation.getAdmissiontime(), "MM");
//                    String exitimeMonth_s = DateUtil.format(delegainformation.getExittime(), "MM");
                    //入退场都不为空
//                    if (projectsystem.getAdmissiontime() != null && projectsystem.getExittime() != null && projectsystem.getType().equals("1")) {
//
//                    }
                    delegainformation.setYear(thisYear_s);
                    //delegainformationMapper.insert(delegainformation);
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
        List<CompanyProjectsVo2> rst2 = companyprojectsMapper.getListPL4(companyProjects.getOwner());
        for(CompanyProjectsVo2 item:rst2){
            if(rst.stream().filter(item2 -> item2.getCompanyprojects_id().equals(item.getCompanyprojects_id())).count() == 0){
                rst.add(item);
            }
        }

        //ADD
        for(CompanyProjectsVo2 it:rst)
        {
            if(com.mysql.jdbc.StringUtils.isNullOrEmpty(it.getGroup_id()))
            {
                CompanyProjects cp = new CompanyProjects();
                cp = companyprojectsMapper.selectByPrimaryKey(it.getCompanyprojects_id());
                if(cp!=null)
                {
                    it.setGroup_id(cp.getCenter_id());
                }
            }
            it.setGroup_id(selectEcodeById(it.getGroup_id()));
        }
        //ADD
        return rst;
    }

    public String selectEcodeById(String id) throws Exception
    {
        String ecode = id;
        OrgTree orgs = orgTreeService.get(new OrgTree());
        //副总
        for (OrgTree orgfu : orgs.getOrgs()) {
            //Center
            for (OrgTree orgCenter : orgfu.getOrgs()) {
                for(OrgTree orgGroup : orgCenter.getOrgs())
                {
                    if(orgGroup.get_id().equals(id))
                    {
                        if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(orgCenter.getEncoding()))
                        {
                            ecode = orgCenter.get_id();
                        }
                        else
                        {
                            ecode = id;
                        }
                        break;
                    }
                }
            }
        }
        return ecode;
    }

    @Override
    public List<CompanyProjectsVo2> getSiteList4(CompanyProjects companyProjects) throws Exception {
        //add_fjl_去重
        List<CompanyProjectsVo2> rst = companyprojectsMapper.getList4(companyProjects.getOwner());
        rst = rst.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(t -> t.getCompanyprojects_id()))), ArrayList::new));
        return rst;
    }

    @Override
    public List<CompanyProjectsVo2> getList2(String flag, List<String> ownerList, String owner) throws Exception {
        if ("0".equals(flag)) {
            List<CompanyProjectsVo2> list2 = companyprojectsMapper.getList2(ownerList);
            List<CompanyProjectsVo2> list3 = companyprojectsMapper.getListPL2(owner);
            for (CompanyProjectsVo2 item : list3) {
                if (list2.stream().filter(item2 -> item2.getCompanyprojects_id().equals(item.getCompanyprojects_id())).count() == 0) {
                    list2.add(item);
                }
            }
            return list2;
        } else {
            List<CompanyProjectsVo2> list3 = companyprojectsMapper.getList3(ownerList);
            List<CompanyProjectsVo2> list4 = companyprojectsMapper.getListPL3(owner);
            for (CompanyProjectsVo2 item : list4) {
                if (list3.stream().filter(item2 -> item2.getCompanyprojects_id().equals(item.getCompanyprojects_id())).count() == 0) {
                    list3.add(item);
                }
            }
            return list3;
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
    //region scc add 根据合同号获取相应决裁信息，返回到项目构外tab页使用 from
    @Override
    public List<Map<String,String>> forDetail(String contractNo, String centerId, String groupId) throws Exception {
        List<Map<String,String>> resultList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<Dictionary> typeOfList = dictionaryService.getForSelect("HT014");
//        Contractapplication contra = new Contractapplication();
//        contra.setContractnumber(contractNo);
//        Contractapplication contractapplication = contractapplicationMapper.selectOne(contra);
        //项目选择受托合同，找出关联的委托合同
        List<Contractapplication> entrList = companyprojectsMapper.selectCont(contractNo,centerId,groupId);
        if(entrList.size() > 0){
            entrList.forEach(ent -> {
                AtomicReference<Boolean> flag = new AtomicReference<>(false);
                typeOfList.forEach(item -> {
                    if (item.getCode().equals(ent.getContracttype())) {
                        flag.set(true);
                    }
                });
                if (flag.get()) {
                    Map<String, String> maptt = new HashMap<>();
                    String time = "";
                    if(ent.getExtensiondate() == null){
                        time = ent.getContractdate();
                    }else {
                        String enddate = sdf.format(ent.getExtensiondate());
                        time = ent.getContractdate().split("~")[0].trim() + "~" + enddate.trim();
                    }
                    Award award = new Award();
                    //委托合同号
                    award.setContractnumber(ent.getContractnumber());
                    Award find = awardMapper.selectOne(award);
                    if (find != null && "4".equals(find.getStatus())) {
                        maptt.put("ContractNo", contractNo);
                        maptt.put("Custojapanese", find.getCustojapanese());
                        maptt.put("Madoguchi", find.getMadoguchi());
                        maptt.put("Numberofworkers", find.getNumberofworkers());
                        maptt.put("Interval", time);
                        maptt.put("Amountof", find.getAmountof());
                        resultList.add(maptt);
                    }
                }
            });
        }
        return resultList;
    }
    //endregion scc add 根据合同号获取相应决裁信息，返回到项目构外tab页使用 to
    @Override
    public Boolean getReport(String user_id,String reporter) throws Exception {
        //取user_id的所有上级
        List<String> toGetRList = new ArrayList<>();
        toGetRList.add(user_id);
        //List<String>

        return true;
    }
    public List<String> getAllHiger(List<String> userList) throws Exception {
        List<String> resList = new ArrayList<>();

        return resList;
    }

}
