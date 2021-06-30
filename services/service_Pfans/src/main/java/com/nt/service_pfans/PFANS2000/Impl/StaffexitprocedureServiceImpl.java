package com.nt.service_pfans.PFANS2000.Impl;

import com.mongodb.client.result.UpdateResult;
import com.nt.dao_Auth.Role;
import com.nt.dao_Auth.Vo.MembersVo;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.ToDoNotice;
import com.nt.dao_Workflow.*;
import com.nt.dao_Pfans.PFANS1000.*;
import com.nt.dao_Pfans.PFANS3000.*;
import com.nt.dao_Pfans.PFANS4000.*;
import com.nt.dao_Pfans.PFANS6000.*;
import com.nt.dao_Pfans.PFANS2000.*;
import com.nt.dao_Pfans.PFANS2000.Vo.StaffexitprocedureVo;
import com.nt.dao_Pfans.PFANS5000.*;
import com.nt.dao_Pfans.PFANS5000.Prosystem;
import com.nt.dao_Pfans.PFANS8000.InformationDelivery;
import com.nt.dao_Pfans.PFANS8000.WorkingDay;
import com.nt.dao_Workflow.Vo.StartWorkflowVo;
import com.nt.dao_Workflow.Vo.WorkflowLogDetailVo;
import com.nt.dao_Workflow.Workflowinstance;
import com.nt.dao_Workflow.Workflownodeinstance;
import com.nt.service_Org.mapper.TodoNoticeMapper;
import com.nt.service_WorkFlow.WorkflowServices;
import com.nt.service_WorkFlow.mapper.WorkflownodeMapper;
import com.nt.service_WorkFlow.mapper.WorkflownodeinstanceMapper;
import com.nt.service_pfans.PFANS1000.mapper.*;
import com.nt.service_pfans.PFANS2000.AnnualLeaveService;
import com.nt.service_pfans.PFANS2000.mapper.*;
import com.nt.service_pfans.PFANS3000.mapper.*;
import com.nt.service_pfans.PFANS4000.mapper.*;
import com.nt.service_pfans.PFANS6000.mapper.*;
import com.nt.service_pfans.PFANS2000.StaffexitprocedureService;
import com.nt.service_pfans.PFANS5000.mapper.*;
import com.nt.service_pfans.PFANS8000.mapper.InformationDeliveryMapper;
import com.nt.service_pfans.PFANS8000.mapper.WorkingDayMapper;
import com.nt.utils.AuthConstants;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.dao.TokenModel;
import com.nt.service_Auth.RoleService;
import com.nt.service_Org.ToDoNoticeService;
import com.nt.utils.services.TokenService;
import com.nt.utils.sign;
import lombok.Builder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class StaffexitprocedureServiceImpl implements StaffexitprocedureService {
    @Autowired
    private WorkflownodeMapper workflownodeMapper;
    @Autowired
    private ContractapplicationMapper contractapplicationMapper;
    @Autowired
    private ContractcompoundMapper contractcompoundMapper;
    @Autowired
    private QuotationMapper quotationMapper;
    @Autowired
    private ContractMapper contractMapper;
    @Autowired
    private NonJudgmentMapper nonJudgmentMapper;
    @Autowired
    private AwardMapper AwardMapper;
    @Autowired
    private NapalmMapper napalmMapper;
    @Autowired
    private PetitionMapper PetitionMapper;
    @Autowired
    private BonussendMapper bonussendMapper;
    @Autowired
    private GoalManagementMapper goalmanagementMapper;
    @Autowired
    private JapanCondominiumMapper japancondominiumMapper;
    @Autowired
    private UseCouponMapper usecouponMapper;
    @Autowired
    private AppointmentCarMapper appointmentcarMapper;
    @Autowired
    private PurchaseMapper purchaseMapper;
    @Autowired
    private StationeryMapper stationeryMapper;

    @Autowired
    private BusinessCardMapper businesscardMapper;
    @Autowired
    private HotelReservationMapper hotelreservationMapper;
    @Autowired
    private TicketsMapper ticketsMapper;
    @Autowired
    private TicketsdetailsMapper ticketsdetailsMapper;

    @Autowired
    private TalentPlanMapper talentPlanMapper;
    @Autowired
    private DelegainformationMapper delegainformationMapper;
    @Autowired
    private VariousfundsMapper variousfundsMapper;

    @Autowired
    private CooperinterviewMapper cooperinterviewMapper;

    @Autowired
    private ExpatriatesinforMapper expatriatesinforMapper;

    @Autowired
    private ExpatriatesinforDetailMapper expatriatesinforDetailMapper;

    @Autowired
    private PricesetMapper pricesetMapper;
    @Autowired
    private SupplierinforMapper supplierinforMapper;

    @Autowired
    private CustomerinforMapper customerinforMapper;

    @Autowired
    private WorkingDayMapper workingdayMapper;
    @Autowired
    private InformationDeliveryMapper informationDeliveryMapper;
    @Autowired
    private ConfidentialMapper confidentialMapper;
    @Autowired
    private SecurityMapper securityMapper;

    @Autowired
    private SecuritydetailMapper securitydetailMapper;
    @Autowired
    private OutsideMapper outsideMapper;

    @Autowired
    private OutsidedetailMapper outsidedetailMapper;

    @Autowired
    private TrialsoftMapper trialsoftMapper;

    @Autowired
    private TrialsoftdetailMapper trialsoftdetailMapper;

    @Autowired
    private PsdcdMapper psdcdMapper;

    @Autowired
    private GlobalMapper globalMapper;

    @Autowired
    private PsdcddetailMapper psdcddetailMapper;

    @Autowired
    private RoutingMapper routingMapper;

    @Autowired
    private RoutingdetailMapper routingdetailMapper;
    @Autowired
    private FixedassetsMapper fixedassetsMapper;
    @Autowired
    private SoftwaretransferMapper softwaretransferMapper;
    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private AssetinformationMapper assetinformationMapper;

    @Autowired
    private ScrapdetailsMapper scrapdetailsMapper;

    @Autowired
    private SalesdetailsMapper salesdetailsMapper;
    @Autowired
    private EvectionMapper evectionMapper;
    @Autowired
    private AccommodationDetailsMapper accommodationdetailsMapper;
    @Autowired
    private CurrencyexchangeMapper currencyexchangeMapper;
    @Autowired
    private TravelCostMapper travelcostmapper;
    @Autowired
    private TrafficDetailsMapper trafficDetailsMapper;

    @Autowired
    private PurchaseDetailsMapper purchaseDetailsMapper;

    @Autowired
    private OtherDetailsMapper otherDetailsMapper;

    @Autowired
    private TotalCostMapper totalCostMapper;

    @Autowired
    private PublicExpenseMapper publicExpenseMapper;

    @Autowired
    private InvoiceMapper invoicemapper;

    @Autowired
    private CommunicationMapper communicationMapper;
    @Autowired
    private PurchaseApplyMapper purchaseApplyMapper;
    @Autowired
    private ShoppingDetailedMapper shoppingDetailedMapper;
    @Autowired
    private LoanApplicationMapper loanapplicationMapper;
    @Autowired
    private JudgementMapper judgementMapper;

    @Autowired
    private UnusedeviceMapper unusedeviceMapper;
    @Autowired
    private TravelContentMapper travelcontentMapper;
    @Autowired
    private BusinessMapper businessMapper;
    @Autowired
    private OffshoreMapper offshoreMapper;
    @Autowired
    private PltabMapper pltabMapper;
    @Autowired
    private CostCarryForwardMapper costcarryforwardmapper;
    @Autowired
    private ThemePlanMapper themePlanMapper;
    @Autowired
    private ThemeInforMapper themeinformapper;
    @Autowired
    private ThemePlanDetailMapper themePlanDetailMapper;

    @Autowired
    private WorkflownodeinstanceMapper workflownodeinstanceMapper;

    @Autowired
    private BusinessplanMapper businessplanMapper;

    @Autowired
    private PersonnelplanMapper personnelplanMapper;
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
    private ContractnumbercountMapper contractnumbercountMapper;

    @Autowired
    private SealMapper sealMapper;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private TodoNoticeMapper todoNoticeMapper;

    @Autowired
    private StaffexitprocedureMapper staffexitprocedureMapper;

    @Autowired
    private StaffexitproceMapper staffexitproceMapper;

    @Autowired
    private WorkflowServices workflowServices;

    @Autowired
    private CitationMapper citationMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private AnnualLeaveMapper annualLeaveMapper;

    @Autowired
    private ToDoNoticeService toDoNoticeService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AnnualLeaveService annualLeaveService;

    //列表查询
    @Override
    public List<Staffexitprocedure> get(Staffexitprocedure staffexitprocedure) throws Exception {
        return staffexitprocedureMapper.select(staffexitprocedure);
    }

    @Override
    public List<Staffexitproce> get2(Staffexitproce staffexit) throws Exception {
        return staffexitproceMapper.select(staffexit);
    }

    //add-ws-6/16-禅道106
    @Override
    public void delete(Staffexitprocedure staffexitprocedure) throws Exception {
        staffexitprocedure.setType("1");
        staffexitprocedureMapper.updateByPrimaryKeySelective(staffexitprocedure);
    }

    //add-ws-6/16-禅道106
    //按id查询
    @Override
    public StaffexitprocedureVo selectById(String staffexitprocedureid) throws Exception {
        StaffexitprocedureVo staffVo = new StaffexitprocedureVo();
        Citation citation = new Citation();
        citation.setStaffexitproce_id(staffexitprocedureid);
        List<Citation> citationlist = citationMapper.select(citation);
        citationlist = citationlist.stream().sorted(Comparator.comparing(Citation::getRowindex)).collect(Collectors.toList());
        Staffexitprocedure Staff = staffexitprocedureMapper.selectByPrimaryKey(staffexitprocedureid);
        staffVo.setStaffexitprocedure(Staff);
        staffVo.setCitation(citationlist);
        return staffVo;
    }


    @Override
    public StaffexitprocedureVo selectById2(String staffexitproceid) throws Exception {
        StaffexitprocedureVo staffVo = new StaffexitprocedureVo();
        Citation citation = new Citation();
        citation.setStaffexitproce_id(staffexitproceid);
        List<Citation> citationlist = citationMapper.select(citation);
        citationlist = citationlist.stream().sorted(Comparator.comparing(Citation::getRowindex)).collect(Collectors.toList());
        Staffexitproce Staff = staffexitproceMapper.selectByPrimaryKey(staffexitproceid);
        staffVo.setStaffexitproce(Staff);
        staffVo.setCitation(citationlist);
        return staffVo;
    }

    //更新
    @Override
    public void update(StaffexitprocedureVo staffexitprocedureVo, TokenModel tokenModel) throws Exception {
        Staffexitprocedure staffexitprocedure = new Staffexitprocedure();
        StartWorkflowVo startWorkflowVo = new StartWorkflowVo();
        startWorkflowVo.setDataId(staffexitprocedureVo.getStaffexitprocedure().getStaffexitprocedure_id());
        List<WorkflowLogDetailVo> wfList = workflowServices.ViewWorkflow2(startWorkflowVo, tokenModel.getLocale());
//        if (staffexitprocedureVo.getStaffexitprocedure().getStage().equals("1") && staffexitprocedureVo.getStaffexitprocedure().getStatus().equals("0")) {
//            if(staffexitprocedureVo.getStaffexitprocedure().getStage().equals("1")){
        //代办完成  Status:1
//                ToDoNotice toDoNotice1 = new ToDoNotice();
//                toDoNotice1.setDataid(staffexitprocedureVo.getStaffexitprocedure().getStaffexitprocedure_id());
//                toDoNotice1.setUrl("/PFANS2026FormView");
//                toDoNotice1.setOwner(staffexitprocedureVo.getStaffexitprocedure().getLoginper());
//                toDoNotice1.setStatus(AuthConstants.DEL_FLAG_NORMAL);
//                List<ToDoNotice> rst1 = toDoNoticeService.get(toDoNotice1);
//                if(rst1.size() > 0){
//                    for (ToDoNotice item : rst1) {
//                        item.setStatus(AuthConstants.TODO_STATUS_DONE);
//                        toDoNoticeService.updateNoticesStatus(item);
//                    }
//                } else {
//            //所属TL
//            Query query = new Query();
//            query.addCriteria(Criteria.where("userinfo.teamid").is(staffexitprocedureVo.getStaffexitprocedure().getTeam_id()).and("userinfo.post").is("PG021005"));
//            query.addCriteria(Criteria.where("status").is(AuthConstants.DEL_FLAG_NORMAL));
//            List<CustomerInfo> CustinfoList = mongoTemplate.find(query, CustomerInfo.class);
//            if (CustinfoList.size() > 0) {
//                for (CustomerInfo cs : CustinfoList) {
//                    cs.getUserid();
//                    cs.getUserinfo().getCustomername();
//                }
//            }
        //给离职审批人员发代办
        if (staffexitprocedureVo.getStaffexitprocedure().getStatus().equals("4")) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            updateRetireDate(staffexitprocedureVo);
            String Userid = staffexitprocedureVo.getStaffexitprocedure().getUser_id();
//            if (wfList.size() > 0) {
//                Userid = wfList.get(0).getUserId();
//            }
            String username = "";
            String center = "";
            String group = "";
            String team = "";
            String date = sdf.format(staffexitprocedureVo.getStaffexitprocedure().getHope_exit_date());
            Query query3 = new Query();
            query3.addCriteria(Criteria.where("userid").is(staffexitprocedureVo.getStaffexitprocedure().getUser_id()));
            CustomerInfo customerInfo2 = mongoTemplate.findOne(query3, CustomerInfo.class);
            if (customerInfo2 != null) {
                username = customerInfo2.getUserinfo().getCustomername();
            }
            //薪资担当代办
            List<MembersVo> rolelist = roleService.getMembers("5e7863668f43163084351139");
            if (rolelist.size() > 0) {
                ToDoNotice toDoNotice3 = new ToDoNotice();
                toDoNotice3.setTitle("【" + username + "将于" + date + "日离职，请悉知】");
                toDoNotice3.setInitiator(Userid);
                toDoNotice3.setContent("您的离职申请已审批通过！");
                toDoNotice3.setDataid(staffexitprocedureVo.getStaffexitprocedure().getStaffexitprocedure_id());
                toDoNotice3.setUrl("/PFANS2026FormView");
                toDoNotice3.setWorkflowurl("/PFANS2026View");
                toDoNotice3.preInsert(tokenModel);
                toDoNotice3.setOwner(rolelist.get(0).getUserid());
                toDoNoticeService.save(toDoNotice3);
            }

            //发起人创建代办
            ToDoNotice toDoNotice = new ToDoNotice();
            toDoNotice.setTitle("【您的离职申请已审批通过，系统中如有进行中的流程，请及时处理。您可以提前进行离职日前的考勤承认】");
            toDoNotice.setInitiator(Userid);
            toDoNotice.setContent("您的离职申请已审批通过！");
            toDoNotice.setDataid(staffexitprocedureVo.getStaffexitprocedure().getStaffexitprocedure_id());
            toDoNotice.setUrl("/PFANS2026FormView");
            toDoNotice.setWorkflowurl("/PFANS2026View");
            toDoNotice.preInsert(tokenModel);
            toDoNotice.setOwner(staffexitprocedureVo.getStaffexitprocedure().getUser_id());
            toDoNoticeService.save(toDoNotice);

//            String user = "";
//            if (wfList.get(4).getRemark() != null) {
//                if (wfList.get(4).getRemark().equals("系统自动跳过")) {
//                    Query query2 = new Query();
//                    query2.addCriteria(Criteria.where("userid").is(wfList.get(3).getUserId()));
//                    CustomerInfo customerInfo = mongoTemplate.findOne(query2, CustomerInfo.class);
//                    if (customerInfo != null) {
//                        user = wfList.get(3).getUserId();
//                    }
//                } else if (wfList.get(5).getRemark().equals("系统自动跳过")) {
//                    Query query2 = new Query();
//                    query2.addCriteria(Criteria.where("userid").is(wfList.get(4).getUserId()));
//                    CustomerInfo customerInfo = mongoTemplate.findOne(query2, CustomerInfo.class);
//                    if (customerInfo != null) {
//                        user = wfList.get(4).getUserId();
//                    }
//                } else {
//                    Query query2 = new Query();
//                    query2.addCriteria(Criteria.where("userid").is(wfList.get(5).getUserId()));
//                    CustomerInfo customerInfo = mongoTemplate.findOne(query2, CustomerInfo.class);
//                    if (customerInfo != null) {
//                        user = wfList.get(5).getUserId();
//                    }
//                }
//            }
            //发起人创建代办
            ToDoNotice toDoNotice2 = new ToDoNotice();
            toDoNotice2.setTitle("【" + username + "离职审批已通过，请填写退职者调书】");
            toDoNotice2.setInitiator(Userid);
            toDoNotice2.setContent("请填写退职者调书！");
            toDoNotice2.setDataid(staffexitprocedureVo.getStaffexitprocedure().getStaffexitprocedure_id());
            toDoNotice2.setUrl("/PFANS2026FormView");
            toDoNotice2.setWorkflowurl("/PFANS2026View");
            toDoNotice2.preInsert(tokenModel);
            toDoNotice2.setOwner(wfList.get(2).getUserId());
            toDoNoticeService.save(toDoNotice2);
        }

//        }
//        if(staffexitprocedureVo.getStaffexitprocedure().getStage().equals("1") && staffexitprocedureVo.getStaffexitprocedure().getStatus().equals("4")) {
        BeanUtils.copyProperties(staffexitprocedureVo.getStaffexitprocedure(), staffexitprocedure);
        if (staffexitprocedure.getStatus().equals("4")) {
            staffexitprocedure.setNewhope_exit_date(staffexitprocedure.getHope_exit_date());
        }
        staffexitprocedure.preUpdate(tokenModel);
        staffexitprocedureMapper.updateByPrimaryKey(staffexitprocedure);
        String staffexitprocedureid = staffexitprocedure.getStaffexitprocedure_id();
        Citation cita = new Citation();
        cita.setStaffexitproce_id(staffexitprocedureid);
        citationMapper.delete(cita);
        List<Citation> citationlist = staffexitprocedureVo.getCitation();
        if (citationlist != null) {
            int rowundex = 0;
            for (Citation citation : citationlist) {
                rowundex = rowundex + 1;
                citation.preInsert(tokenModel);
                citation.setCitation_id(UUID.randomUUID().toString());
                citation.setStaffexitproce_id(staffexitprocedureid);
                citation.setRowindex(rowundex);
                citationMapper.insertSelective(citation);
            }
        }
    }

    //【每天凌晨0点5分】
    //进行数据权限和审批权限交接
    @Scheduled(cron = "0 05 0 * * ?")
    public void getdataExittime() throws Exception {
        SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
        String month2 = sd.format(new Date());
        Date currdate = sd.parse(month2);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currdate);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
        String month1 = sd.format(calendar.getTime());
//        TokenModel tokenModel = tokenService.getToken(HttpServletRequest);
        Staffexitproce Staffexitproce = new Staffexitproce();
        List<Staffexitproce> Staffexitprocelist = staffexitproceMapper.select(Staffexitproce);
        Staffexitprocelist = Staffexitprocelist.stream().filter(item -> (item.getStatus().equals("4"))).collect(Collectors.toList());
        for (Staffexitproce list : Staffexitprocelist) {
            if (Integer.valueOf(month1) <= Integer.valueOf(sd.format(list.getResignation_date())) && Integer.valueOf(sd.format(list.getResignation_date())) < Integer.valueOf(month2)) {
                //审批权限交接 gbb 20210326 此处不需要dataid,应将离职人员的所有代办转给数据权限交接担当
                ToDoNotice condition = new ToDoNotice();
                condition.setOwner(list.getUser_id());
                condition.setStatus(AuthConstants.TODO_STATUS_TODO);
                List<ToDoNotice> ToDoNoticelist = todoNoticeMapper.select(condition);
                for (ToDoNotice todono : ToDoNoticelist) {
                    todono.setModifyon(new Date());
                    todono.setOwner(list.getUserdata());
                    todoNoticeMapper.updateByPrimaryKey(todono);
                }
                Workflownodeinstance workflownodeinstance = new Workflownodeinstance();
                workflownodeinstance.setItemid(list.getUser_id());
                List<Workflownodeinstance> workflownodeinstancelist = workflownodeinstanceMapper.select(workflownodeinstance);
                for (Workflownodeinstance worklist : workflownodeinstancelist) {
                    worklist.setItemid(list.getUserdata());
                    worklist.setModifyon(new Date());
                    workflownodeinstanceMapper.updateByPrimaryKey(worklist);
                }
                Workflownode workflownode = new Workflownode();
                workflownode.setItemid(list.getUser_id());
                List<Workflownode> workflownodelist = workflownodeMapper.select(workflownode);
                for (Workflownode workflowlist : workflownodelist) {
                    workflowlist.setItemid(list.getUserdata());
                    workflowlist.setModifyon(new Date());
                    workflownodeMapper.updateByPrimaryKey(workflowlist);
                }

                //数据权限交接
                //印章管理
                Seal seal = new Seal();
                seal.setOwner(list.getUser_id());
                List<Seal> seallist = sealMapper.select(seal);
                for (Seal list1 : seallist) {
                    list1.setModifyon(new Date());
                    list1.setOwner(list.getUserdata());
                    sealMapper.updateByPrimaryKey(list1);
                }
                //PJ起案，现场管理，PJ完了   项目表
                CompanyProjects companyProjects = new CompanyProjects();
                companyProjects.setOwner(list.getUser_id());
                List<CompanyProjects> companyprojectlist = companyprojectsMapper.select(companyProjects);
                for (CompanyProjects list2 : companyprojectlist) {
                    list2.setModifyon(new Date());
                    list2.setOwner(list.getUserdata());
                    companyprojectsMapper.updateByPrimaryKey(list2);
                }

                //PJ起案，现场管理，PJ完了   项目计划
                StageInformation stageinformation = new StageInformation();
                stageinformation.setOwner(list.getUser_id());
                List<StageInformation> stageinformationlist = stageinformationMapper.select(stageinformation);
                for (StageInformation list3 : stageinformationlist) {
                    list3.setModifyon(new Date());
                    list3.setOwner(list.getUserdata());
                    stageinformationMapper.updateByPrimaryKey(list3);
                }
                //PJ起案，现场管理，PJ完了   体制
                Projectsystem projectsystem3 = new Projectsystem();
                projectsystem3.setOwner(list.getUser_id());
                List<Projectsystem> projectsystemlist = projectsystemMapper.select(projectsystem3);
                for (Projectsystem list4 : projectsystemlist) {
                    list4.setModifyon(new Date());
                    list4.setOwner(list.getUserdata());
                    projectsystemMapper.updateByPrimaryKey(list4);
                }
                //PJ起案，现场管理，PJ完了   项目合同
                ProjectContract projectcontract = new ProjectContract();
                projectcontract.setOwner(list.getUser_id());
                List<ProjectContract> projectcontractlist = projectcontractMapper.select(projectcontract);
                for (ProjectContract list5 : projectcontractlist) {
                    list5.setModifyon(new Date());
                    list5.setOwner(list.getUserdata());
                    projectcontractMapper.updateByPrimaryKey(list5);
                }
                //PJ起案，现场管理，PJ完了   委托元为内采时，合同
                Contractnumbercount contractnumbercount = new Contractnumbercount();
                contractnumbercount.setOwner(list.getUser_id());
                List<Contractnumbercount> contractnumbercountlist = contractnumbercountMapper.select(contractnumbercount);
                for (Contractnumbercount list6 : contractnumbercountlist) {
                    list6.setModifyon(new Date());
                    list6.setOwner(list.getUserdata());
                    contractnumbercountMapper.updateByPrimaryKey(list6);
                }
                //共同部署起案，共同部署完了   项目表
                Comproject comproject = new Comproject();
                comproject.setOwner(list.getUser_id());
                List<Comproject> comprojectlist = comProjectMapper.select(comproject);
                for (Comproject list7 : comprojectlist) {
                    list7.setModifyon(new Date());
                    list7.setOwner(list.getUserdata());
                    comProjectMapper.updateByPrimaryKey(list7);
                }
                //共同部署起案，共同部署完了   体制
                Prosystem prosystem2 = new Prosystem();
                prosystem2.setOwner(list.getUser_id());
                List<Prosystem> prosystemlist = prosystemMapper.select(prosystem2);
                for (Prosystem list8 : prosystemlist) {
                    list8.setModifyon(new Date());
                    list8.setOwner(list.getUserdata());
                    prosystemMapper.updateByPrimaryKey(list8);
                }
                //人员计划
                PersonnelPlan personnelPlan = new PersonnelPlan();
                personnelPlan.setOwner(list.getUser_id());
                List<PersonnelPlan> personnelPlanlist = personnelplanMapper.select(personnelPlan);
                for (PersonnelPlan list9 : personnelPlanlist) {
                    list9.setModifyon(new Date());
                    list9.setOwner(list.getUserdata());
                    personnelplanMapper.updateByPrimaryKey(list9);
                }
                //事业计划
                Businessplan businessplan = new Businessplan();
                businessplan.setOwner(list.getUser_id());
                List<Businessplan> businessplanlist = businessplanMapper.select(businessplan);
                for (Businessplan list10 : businessplanlist) {
                    list10.setModifyon(new Date());
                    list10.setOwner(list.getUserdata());
                    businessplanMapper.updateByPrimaryKey(list10);
                }
                //受託theme，委托theme  主表
                ThemePlan ct = new ThemePlan();
                ct.setOwner(list.getUser_id());
                List<ThemePlan> themeplanlist = themePlanMapper.select(ct);
                for (ThemePlan list11 : themeplanlist) {
                    list11.setModifyon(new Date());
                    list11.setOwner(list.getUserdata());
                    themePlanMapper.updateByPrimaryKey(list11);
                }
                //受託theme，委托theme  详细表
                ThemePlanDetail ctDetail = new ThemePlanDetail();
                ctDetail.setOwner(list.getUser_id());
                List<ThemePlanDetail> themeplandetaillist = themePlanDetailMapper.select(ctDetail);
                for (ThemePlanDetail list12 : themeplandetaillist) {
                    list12.setModifyon(new Date());
                    list12.setOwner(list.getUserdata());
                    themePlanDetailMapper.updateByPrimaryKey(list12);
                }
                //theme管理
                ThemeInfor themeinfor = new ThemeInfor();
                themeinfor.setOwner(list.getUser_id());
                List<ThemeInfor> themeinforlist = themeinformapper.select(themeinfor);
                for (ThemeInfor list13 : themeinforlist) {
                    list13.setModifyon(new Date());
                    list13.setOwner(list.getUserdata());
                    themeinformapper.updateByPrimaryKey(list13);
                }
                //PL成本结转表
//                Pltab pltab = new Pltab();
//                pltab.setOwner(list.getUser_id());
//                List<Pltab> pltablist = pltabMapper.select(pltab);
//                for (Pltab list14 : pltablist) {
//                    list14.setModifyon(new Date());
//                    list14.setOwner(list.getUserdata());
//                    pltabMapper.updateByPrimaryKey(list14);
//                }
                //PL成本结转表
                CostCarryForward costcarry = new CostCarryForward();
                costcarry.setOwner(list.getUser_id());
                List<CostCarryForward> costcarrylist = costcarryforwardmapper.select(costcarry);
                for (CostCarryForward list15 : costcarrylist) {
                    list15.setModifyon(new Date());
                    list15.setOwner(list.getUserdata());
                    costcarryforwardmapper.updateByPrimaryKey(list15);
                }
                //决裁 事情面谈票
                Offshore offshore = new Offshore();
                offshore.setOwner(list.getUser_id());
                List<Offshore> offshorelist = offshoreMapper.select(offshore);
                for (Offshore list16 : offshorelist) {
                    list16.setModifyon(new Date());
                    list16.setOwner(list.getUserdata());
                    offshoreMapper.updateByPrimaryKey(list16);
                }
                //决裁 境内/外出差申请
                Business business = new Business();
                business.setOwner(list.getUser_id());
                List<Business> businesslist = businessMapper.select(business);
                for (Business list17 : businesslist) {
                    list17.setModifyon(new Date());
                    list17.setOwner(list.getUserdata());
                    businessMapper.updateByPrimaryKey(list17);
                }
                //决裁 境内/外出差申请
                TravelContent travel = new TravelContent();
                travel.setOwner(list.getUser_id());
                List<TravelContent> travelcontentlist = travelcontentMapper.select(travel);
                for (TravelContent list18 : travelcontentlist) {
                    list18.setModifyon(new Date());
                    list18.setOwner(list.getUserdata());
                    travelcontentMapper.updateByPrimaryKey(list18);
                }
                //决裁 其他申请
                Judgement judgement = new Judgement();
                judgement.setOwner(list.getUser_id());
                List<Judgement> judgementlist = judgementMapper.select(judgement);
                for (Judgement list19 : judgementlist) {
                    list19.setModifyon(new Date());
                    list19.setOwner(list.getUserdata());
                    judgementMapper.updateByPrimaryKey(list19);
                }
                //决裁 其他申请,无偿设备
                Unusedevice unusedevice = new Unusedevice();
                unusedevice.setOwner(list.getUser_id());
                List<Unusedevice> unusedevicelist = unusedeviceMapper.select(unusedevice);
                for (Unusedevice list20 : unusedevicelist) {
                    list20.setModifyon(new Date());
                    list20.setOwner(list.getUserdata());
                    unusedeviceMapper.updateByPrimaryKey(list20);
                }
                //决裁 暂借款申请单
                LoanApplication loanapplication = new LoanApplication();
                loanapplication.setOwner(list.getUser_id());
                List<LoanApplication> loanapplicationlist = loanapplicationMapper.select(loanapplication);
                for (LoanApplication list21 : loanapplicationlist) {
                    list21.setModifyon(new Date());
                    list21.setOwner(list.getUserdata());
                    loanapplicationMapper.updateByPrimaryKey(list21);
                }
                //决裁 千元以下费用
                PurchaseApply purchaseApply = new PurchaseApply();
                purchaseApply.setOwner(list.getUser_id());
                List<PurchaseApply> purchaseApplylist = purchaseApplyMapper.select(purchaseApply);
                for (PurchaseApply list22 : purchaseApplylist) {
                    list22.setModifyon(new Date());
                    list22.setOwner(list.getUserdata());
                    purchaseApplyMapper.updateByPrimaryKey(list22);
                }
                //决裁 千元以下费用
                ShoppingDetailed spd = new ShoppingDetailed();
                spd.setOwner(list.getUser_id());
                List<ShoppingDetailed> shoppingdetailedlist = shoppingDetailedMapper.select(spd);
                for (ShoppingDetailed list23 : shoppingdetailedlist) {
                    list23.setModifyon(new Date());
                    list23.setOwner(list.getUserdata());
                    shoppingDetailedMapper.updateByPrimaryKey(list23);
                }
                //决裁 交际费事前决裁
                Communication communication = new Communication();
                communication.setOwner(list.getUser_id());
                List<Communication> communicationlist = communicationMapper.select(communication);
                for (Communication list24 : communicationlist) {
                    list24.setModifyon(new Date());
                    list24.setOwner(list.getUserdata());
                    communicationMapper.updateByPrimaryKey(list24);
                }
                //决裁 方针合同
//            CostCarryForward costcarry = new CostCarryForward();
//            costcarry.preUpdate(tokenModel);
//            costcarry.setOwner(wfList.get(0).getUserId());
//            costcarryforwardmapper.updateByPrimaryKey(costcarry);
                //公共费用精算 主表
                PublicExpense publicExpense = new PublicExpense();
                publicExpense.setOwner(list.getUser_id());
                List<PublicExpense> publicExpenselist = publicExpenseMapper.select(publicExpense);
                for (PublicExpense list25 : publicExpenselist) {
                    list25.setModifyon(new Date());
                    list25.setOwner(list.getUserdata());
                    publicExpenseMapper.updateByPrimaryKey(list25);
                }

                //公共费用精算，出差精算书 增值税表
                Invoice invoice = new Invoice();
                invoice.setOwner(list.getUser_id());
                List<Invoice> invoicelist = invoicemapper.select(invoice);
                for (Invoice list26 : invoicelist) {
                    list26.setModifyon(new Date());
                    list26.setOwner(list.getUserdata());
                    invoicemapper.updateByPrimaryKey(list26);
                }
                //公共费用精算，出差精算书 其他费用表
                OtherDetails other = new OtherDetails();
                other.setOwner(list.getUser_id());
                List<OtherDetails> otherlist = otherDetailsMapper.select(other);
                for (OtherDetails list27 : otherlist) {
                    list27.setModifyon(new Date());
                    list27.setOwner(list.getUserdata());
                    otherDetailsMapper.updateByPrimaryKey(list27);
                }
                //公共费用精算 采购费用表
                PurchaseDetails purchase = new PurchaseDetails();
                purchase.setOwner(list.getUser_id());
                List<PurchaseDetails> purchasedetailslist = purchaseDetailsMapper.select(purchase);
                for (PurchaseDetails list28 : purchasedetailslist) {
                    list28.setModifyon(new Date());
                    list28.setOwner(list.getUserdata());
                    purchaseDetailsMapper.updateByPrimaryKey(list28);
                }
                //公共费用精算 ，出差精算书 交通费用表
                TrafficDetails traffic = new TrafficDetails();
                traffic.setOwner(list.getUser_id());
                List<TrafficDetails> trafficdetailslist = trafficDetailsMapper.select(traffic);
                for (TrafficDetails list29 : trafficdetailslist) {
                    list29.setModifyon(new Date());
                    list29.setOwner(list.getUserdata());
                    trafficDetailsMapper.updateByPrimaryKey(list29);
                }
                //公共费用精算 打印csv表
                TotalCost totalcost = new TotalCost();
                totalcost.setOwner(list.getUser_id());
                List<TotalCost> totalcostlist = totalCostMapper.select(totalcost);
                for (TotalCost list30 : totalcostlist) {
                    list30.setModifyon(new Date());
                    list30.setOwner(list.getUserdata());
                    totalCostMapper.updateByPrimaryKey(list30);
                }
                //出差精算书 主表
                Evection evection = new Evection();
                evection.setOwner(list.getUser_id());
                List<Evection> evectionlist = evectionMapper.select(evection);
                for (Evection list31 : evectionlist) {
                    list31.setModifyon(new Date());
                    list31.setOwner(list.getUserdata());
                    evectionMapper.updateByPrimaryKey(list31);
                }
                //出差精算书 住宿费明细
                AccommodationDetails accommodation = new AccommodationDetails();
                accommodation.setOwner(list.getUser_id());
                List<AccommodationDetails> accommodationlist = accommodationdetailsMapper.select(accommodation);
                for (AccommodationDetails list32 : accommodationlist) {
                    list32.setModifyon(new Date());
                    list32.setOwner(list.getUserdata());
                    accommodationdetailsMapper.updateByPrimaryKey(list32);
                }
                //出差精算书 外币兑换
                Currencyexchange currencyexchange = new Currencyexchange();
                currencyexchange.setOwner(list.getUser_id());
                List<Currencyexchange> currencyexchangelist = currencyexchangeMapper.select(currencyexchange);
                for (Currencyexchange list33 : currencyexchangelist) {
                    list33.setModifyon(new Date());
                    list33.setOwner(list.getUserdata());
                    currencyexchangeMapper.updateByPrimaryKey(list33);
                }
                //出差精算书 打印csv表
                TravelCost travelCost = new TravelCost();
                travelCost.setOwner(list.getUser_id());
                List<TravelCost> travelCostlist = travelcostmapper.select(travelCost);
                for (TravelCost list34 : travelCostlist) {
                    list34.setModifyon(new Date());
                    list34.setOwner(list.getUserdata());
                    travelcostmapper.updateByPrimaryKey(list34);
                }
                //资产报废处理决裁
                Assetinformation assetinformation = new Assetinformation();
                assetinformation.setOwner(list.getUser_id());
                List<Assetinformation> assetinformationlist = assetinformationMapper.select(assetinformation);
                for (Assetinformation list35 : assetinformationlist) {
                    list35.setModifyon(new Date());
                    list35.setOwner(list.getUserdata());
                    assetinformationMapper.updateByPrimaryKey(list35);
                }
                //资产报废处理决裁
                Scrapdetails sce = new Scrapdetails();
                sce.setOwner(list.getUser_id());
                List<Scrapdetails> scrapdetailslist = scrapdetailsMapper.select(sce);
                for (Scrapdetails list36 : scrapdetailslist) {
                    list36.setModifyon(new Date());
                    list36.setOwner(list.getUserdata());
                    scrapdetailsMapper.updateByPrimaryKey(list36);
                }
                //资产报废处理决裁
                Salesdetails sal = new Salesdetails();
                sal.setOwner(list.getUser_id());
                List<Salesdetails> salesdetailslist = salesdetailsMapper.select(sal);
                for (Salesdetails list37 : salesdetailslist) {
                    list37.setModifyon(new Date());
                    list37.setOwner(list.getUserdata());
                    salesdetailsMapper.updateByPrimaryKey(list37);
                }
                //资产部门间转移及管理者变更决裁
                Softwaretransfer softwaretransfer = new Softwaretransfer();
                softwaretransfer.setOwner(list.getUser_id());
                List<Softwaretransfer> softwaretransferlist = softwaretransferMapper.select(softwaretransfer);
                for (Softwaretransfer list38 : softwaretransferlist) {
                    list38.setModifyon(new Date());
                    list38.setOwner(list.getUserdata());
                    softwaretransferMapper.updateByPrimaryKey(list38);
                }
                //资产部门间转移及管理者变更决裁
                Notification not = new Notification();
                not.setOwner(list.getUser_id());
                List<Notification> notificationlist = notificationMapper.select(not);
                for (Notification list39 : notificationlist) {
                    list39.setModifyon(new Date());
                    list39.setOwner(list.getUserdata());
                    notificationMapper.updateByPrimaryKey(list39);
                }
                //固定资产借出修理持出决裁
                Fixedassets fixedassets = new Fixedassets();
                fixedassets.setOwner(list.getUser_id());
                List<Fixedassets> fixedassetslist = fixedassetsMapper.select(fixedassets);
                for (Fixedassets list40 : fixedassetslist) {
                    list40.setModifyon(new Date());
                    list40.setOwner(list.getUserdata());
                    fixedassetsMapper.updateByPrimaryKey(list40);
                }
                //路由申请
                Routing routing = new Routing();
                routing.setOwner(list.getUser_id());
                List<Routing> routinglist = routingMapper.select(routing);
                for (Routing list41 : routinglist) {
                    list41.setModifyon(new Date());
                    list41.setOwner(list.getUserdata());
                    routingMapper.updateByPrimaryKey(list41);
                }
                //路由申请
                Routingdetail tail = new Routingdetail();
                tail.setOwner(list.getUser_id());
                List<Routingdetail> routingdetaillist = routingdetailMapper.select(tail);
                for (Routingdetail list42 : routingdetaillist) {
                    list42.setModifyon(new Date());
                    list42.setOwner(list.getUserdata());
                    routingdetailMapper.updateByPrimaryKey(list42);
                }
                //PSDCD ID申請一览
                Psdcd psdcd = new Psdcd();
                psdcd.setOwner(list.getUser_id());
                List<Psdcd> psdcdlist = psdcdMapper.select(psdcd);
                for (Psdcd list43 : psdcdlist) {
                    list43.setModifyon(new Date());
                    list43.setOwner(list.getUserdata());
                    psdcdMapper.updateByPrimaryKey(list43);
                }
                //PSDCD ID申請一览
                Psdcddetail psdcddetail = new Psdcddetail();
                psdcddetail.setOwner(list.getUser_id());
                List<Psdcddetail> psdcddetaillist = psdcddetailMapper.select(psdcddetail);
                for (Psdcddetail list44 : psdcddetaillist) {
                    list44.setModifyon(new Date());
                    list44.setOwner(list.getUserdata());
                    psdcddetailMapper.updateByPrimaryKey(list44);
                }
                //Global ID(EPOCH)申請一览
                Global global = new Global();
                global.setOwner(list.getUser_id());
                List<Global> globallist = globalMapper.select(global);
                for (Global list45 : globallist) {
                    list45.setModifyon(new Date());
                    list45.setOwner(list.getUserdata());
                    globalMapper.updateByPrimaryKey(list45);
                }
                //免费・试用软件使用申請一览
                Trialsoft trialsoft = new Trialsoft();
                trialsoft.setOwner(list.getUser_id());
                List<Trialsoft> trialsoftlist = trialsoftMapper.select(trialsoft);
                for (Trialsoft list46 : trialsoftlist) {
                    list46.setModifyon(new Date());
                    list46.setOwner(list.getUserdata());
                    trialsoftMapper.updateByPrimaryKey(list46);
                }
                //免费・试用软件使用申請一览
                Trialsoftdetail trialsoftdetail = new Trialsoftdetail();
                trialsoftdetail.setOwner(list.getUser_id());
                List<Trialsoftdetail> trialsoftdetaillist = trialsoftdetailMapper.select(trialsoftdetail);
                for (Trialsoftdetail list47 : trialsoftdetaillist) {
                    list47.setModifyon(new Date());
                    list47.setOwner(list.getUserdata());
                    trialsoftdetailMapper.updateByPrimaryKey(list47);
                }
                //社外邮件送信申請一览
                Outside outside = new Outside();
                outside.setOwner(list.getUser_id());
                List<Outside> outsidelist = outsideMapper.select(outside);
                for (Outside list48 : outsidelist) {
                    list48.setModifyon(new Date());
                    list48.setOwner(list.getUserdata());
                    outsideMapper.updateByPrimaryKey(list48);
                }
                //社外邮件送信申請一览
                Outsidedetail outsidedetail = new Outsidedetail();
                outsidedetail.setOwner(list.getUser_id());
                List<Outsidedetail> outsidedetaillist = outsidedetailMapper.select(outsidedetail);
                for (Outsidedetail list49 : outsidedetaillist) {
                    list49.setModifyon(new Date());
                    list49.setOwner(list.getUserdata());
                    outsidedetailMapper.updateByPrimaryKey(list49);
                }
                //门禁卡申請一览
                Security security = new Security();
                security.setOwner(list.getUser_id());
                List<Security> securitylist = securityMapper.select(security);
                for (Security list50 : securitylist) {
                    list50.setModifyon(new Date());
                    list50.setOwner(list.getUserdata());
                    securityMapper.updateByPrimaryKey(list50);
                }
                //门禁卡申請一览
                Securitydetail securitydetail = new Securitydetail();
                securitydetail.setOwner(list.getUser_id());
                List<Securitydetail> securitydetaillist = securitydetailMapper.select(securitydetail);
                for (Securitydetail list51 : securitydetaillist) {
                    list51.setModifyon(new Date());
                    list51.setOwner(list.getUserdata());
                    securitydetailMapper.updateByPrimaryKey(list51);
                }
                //机密信息・记忆媒体持出申请一览
                Confidential confidential = new Confidential();
                confidential.setOwner(list.getUser_id());
                List<Confidential> confidentiallist = confidentialMapper.select(confidential);
                for (Confidential list52 : confidentiallist) {
                    list52.setModifyon(new Date());
                    list52.setOwner(list.getUserdata());
                    confidentialMapper.updateByPrimaryKey(list52);
                }
                //信息一览
                InformationDelivery informationDelivery = new InformationDelivery();
                informationDelivery.setOwner(list.getUser_id());
                List<InformationDelivery> informationdeliverylist = informationDeliveryMapper.select(informationDelivery);
                for (InformationDelivery list53 : informationdeliverylist) {
                    list53.setModifyon(new Date());
                    list53.setOwner(list.getUserdata());
                    informationDeliveryMapper.updateByPrimaryKey(list53);
                }
                //工作日设置
                WorkingDay workingday = new WorkingDay();
                workingday.setOwner(list.getUser_id());
                List<WorkingDay> workingdaylist = workingdayMapper.select(workingday);
                for (WorkingDay list54 : workingdaylist) {
                    list54.setModifyon(new Date());
                    list54.setOwner(list.getUserdata());
                    workingdayMapper.updateByPrimaryKey(list54);
                }
                //客户信息
                Customerinfor customerinfor = new Customerinfor();
                customerinfor.setOwner(list.getUser_id());
                List<Customerinfor> customerinforlist = customerinforMapper.select(customerinfor);
                for (Customerinfor list55 : customerinforlist) {
                    list55.setModifyon(new Date());
                    list55.setOwner(list.getUserdata());
                    customerinforMapper.updateByPrimaryKey(list55);
                }
                //供应商信息
                Supplierinfor supplierinfor = new Supplierinfor();
                supplierinfor.setOwner(list.getUser_id());
                List<Supplierinfor> supplierinforlist = supplierinforMapper.select(supplierinfor);
                for (Supplierinfor list56 : supplierinforlist) {
                    list56.setModifyon(new Date());
                    list56.setOwner(list.getUserdata());
                    supplierinforMapper.updateByPrimaryKey(list56);
                }
                //外协人员登记表
                Expatriatesinfor expatriatesinfor = new Expatriatesinfor();
                expatriatesinfor.setOwner(list.getUser_id());
                List<Expatriatesinfor> expatriatesinforlist = expatriatesinforMapper.select(expatriatesinfor);
                for (Expatriatesinfor list57 : expatriatesinforlist) {
                    list57.setModifyon(new Date());
                    list57.setOwner(list.getUserdata());
                    expatriatesinforMapper.updateByPrimaryKey(list57);
                }
                //外协人员登记表
                ExpatriatesinforDetail expatriatesinforDetail = new ExpatriatesinforDetail();
                expatriatesinforDetail.setOwner(list.getUser_id());
                List<ExpatriatesinforDetail> expatriatesinfordetaillist = expatriatesinforDetailMapper.select(expatriatesinforDetail);
                for (ExpatriatesinforDetail list58 : expatriatesinfordetaillist) {
                    list58.setModifyon(new Date());
                    list58.setOwner(list.getUserdata());
                    expatriatesinforDetailMapper.updateByPrimaryKey(list58);
                }
                //外协人员登记表
                Priceset priceset = new Priceset();
                priceset.setOwner(list.getUser_id());
                List<Priceset> pricesetlist = pricesetMapper.select(priceset);
                for (Priceset list59 : pricesetlist) {
                    list59.setModifyon(new Date());
                    list59.setOwner(list.getUserdata());
                    pricesetMapper.updateByPrimaryKey(list59);
                }
                //外协员工面试记录
                Cooperinterview cooperinterview = new Cooperinterview();
                cooperinterview.setOwner(list.getUser_id());
                List<Cooperinterview> cooperinterviewlist = cooperinterviewMapper.select(cooperinterview);
                for (Cooperinterview list60 : cooperinterviewlist) {
                    list60.setModifyon(new Date());
                    list60.setOwner(list.getUserdata());
                    cooperinterviewMapper.updateByPrimaryKey(list60);
                }
                //外协公司各种经费
                Variousfunds variousfunds = new Variousfunds();
                variousfunds.setOwner(list.getUser_id());
                List<Variousfunds> variousfundslist = variousfundsMapper.select(variousfunds);
                for (Variousfunds list61 : variousfundslist) {
                    list61.setModifyon(new Date());
                    list61.setOwner(list.getUserdata());
                    variousfundsMapper.updateByPrimaryKey(list61);
                }
                //外协员工委托信息
                Delegainformation delegainformation = new Delegainformation();
                delegainformation.setOwner(list.getUser_id());
                List<Delegainformation> delegainformationlist = delegainformationMapper.select(delegainformation);
                for (Delegainformation list62 : delegainformationlist) {
                    list62.setModifyon(new Date());
                    list62.setOwner(list.getUserdata());
                    delegainformationMapper.updateByPrimaryKey(list62);
                }
                //退职者调书&离职手续
                Citation citation = new Citation();
                citation.setOwner(list.getUser_id());
                List<Citation> citationlist = citationMapper.select(citation);
                for (Citation list63 : citationlist) {
                    list63.setModifyon(new Date());
                    list63.setOwner(list.getUserdata());
                    citationMapper.updateByPrimaryKey(list63);
                }
                //退职者调书&离职手续
                Staffexitproce Staff = new Staffexitproce();
                Staff.setOwner(list.getUser_id());
                List<Staffexitproce> staffexitprocelist = staffexitproceMapper.select(Staff);
                for (Staffexitproce list64 : staffexitprocelist) {
                    list64.setModifyon(new Date());
                    list64.setOwner(list.getUserdata());
                    staffexitproceMapper.updateByPrimaryKey(list64);
                }
                //目标管理
                GoalManagement goalManagement = new GoalManagement();
                goalManagement.setOwner(list.getUser_id());
                List<GoalManagement> goalmanagementlist = goalmanagementMapper.select(goalManagement);
                for (GoalManagement list65 : goalmanagementlist) {
                    list65.setModifyon(new Date());
                    list65.setOwner(list.getUserdata());
                    goalmanagementMapper.updateByPrimaryKey(list65);
                }
                //人才育成计划
                TalentPlan talentPlan = new TalentPlan();
                talentPlan.setOwner(list.getUser_id());
                List<TalentPlan> talentplanlist = talentPlanMapper.select(talentPlan);
                for (TalentPlan list66 : talentplanlist) {
                    list66.setModifyon(new Date());
                    list66.setOwner(list.getUserdata());
                    talentPlanMapper.updateByPrimaryKey(list66);
                }
                //国内/国际机票预约表
                Tickets tickets = new Tickets();
                tickets.setOwner(list.getUser_id());
                List<Tickets> ticketslist = ticketsMapper.select(tickets);
                for (Tickets list67 : ticketslist) {
                    list67.setModifyon(new Date());
                    list67.setOwner(list.getUserdata());
                    ticketsMapper.updateByPrimaryKey(list67);
                }
                //国内/国际机票预约表
                Ticketsdetails ticketsdetails = new Ticketsdetails();
                ticketsdetails.setOwner(list.getUser_id());
                List<Ticketsdetails> ticketsdetailslist = ticketsdetailsMapper.select(ticketsdetails);
                for (Ticketsdetails list68 : ticketsdetailslist) {
                    list68.setModifyon(new Date());
                    list68.setOwner(list.getUserdata());
                    ticketsdetailsMapper.updateByPrimaryKey(list68);
                }
                //来客酒店预订
                HotelReservation hotelreservation = new HotelReservation();
                hotelreservation.setOwner(list.getUser_id());
                List<HotelReservation> hotelreservationlist = hotelreservationMapper.select(hotelreservation);
                for (HotelReservation list69 : hotelreservationlist) {
                    list69.setModifyon(new Date());
                    list69.setOwner(list.getUserdata());
                    hotelreservationMapper.updateByPrimaryKey(list69);
                }
                //名片申请
                BusinessCard businesscard = new BusinessCard();
                businesscard.setOwner(list.getUser_id());
                List<BusinessCard> businesscardlist = businesscardMapper.select(businesscard);
                for (BusinessCard list70 : businesscardlist) {
                    list70.setModifyon(new Date());
                    list70.setOwner(list.getUserdata());
                    businesscardMapper.updateByPrimaryKey(list70);
                }
                //文具领取申请
                Stationery stationery = new Stationery();
                stationery.setOwner(list.getUser_id());
                List<Stationery> stationerylist = stationeryMapper.select(stationery);
                for (Stationery list71 : stationerylist) {
                    list71.setModifyon(new Date());
                    list71.setOwner(list.getUserdata());
                    stationeryMapper.updateByPrimaryKey(list71);
                }
                //购买申请表
                Purchase purchase1 = new Purchase();
                purchase1.setOwner(list.getUser_id());
                List<Purchase> purchaselist = purchaseMapper.select(purchase1);
                for (Purchase list72 : purchaselist) {
                    list72.setModifyon(new Date());
                    list72.setOwner(list.getUserdata());
                    purchaseMapper.updateByPrimaryKey(list72);
                }
                //社用车预约表
                AppointmentCar appointmentcar = new AppointmentCar();
                appointmentcar.setOwner(list.getUser_id());
                List<AppointmentCar> appointmentcarlist = appointmentcarMapper.select(appointmentcar);
                for (AppointmentCar list73 : appointmentcarlist) {
                    list73.setModifyon(new Date());
                    list73.setOwner(list.getUserdata());
                    appointmentcarMapper.updateByPrimaryKey(list73);
                }
                //日本出差公寓预约
                JapanCondominium japancondominium = new JapanCondominium();
                japancondominium.setOwner(list.getUser_id());
                List<JapanCondominium> japancondominiumlist = japancondominiumMapper.select(japancondominium);
                for (JapanCondominium list74 : japancondominiumlist) {
                    list74.setModifyon(new Date());
                    list74.setOwner(list.getUserdata());
                    japancondominiumMapper.updateByPrimaryKey(list74);
                }
                //日本出差公寓预约
                UseCoupon usecoupon = new UseCoupon();
                usecoupon.setOwner(list.getUser_id());
                List<UseCoupon> usecouponlist = usecouponMapper.select(usecoupon);
                for (UseCoupon list75 : usecouponlist) {
                    list75.setModifyon(new Date());
                    list75.setOwner(list.getUserdata());
                    usecouponMapper.updateByPrimaryKey(list75);
                }
                //奖金发送
                Bonussend bonussend = new Bonussend();
                bonussend.setOwner(list.getUser_id());
                List<Bonussend> bonussendlist = bonussendMapper.select(bonussend);
                for (Bonussend list76 : bonussendlist) {
                    list76.setModifyon(new Date());
                    list76.setOwner(list.getUserdata());
                    bonussendMapper.updateByPrimaryKey(list76);
                }
                //委托契约，受託契约，其他契约  主表
                Contractapplication contractapplication = new Contractapplication();
                contractapplication.setOwner(list.getUser_id());
                List<Contractapplication> contractapplicationlist = contractapplicationMapper.select(contractapplication);
                for (Contractapplication list77 : contractapplicationlist) {
                    list77.setModifyon(new Date());
                    list77.setOwner(list.getUserdata());
                    contractapplicationMapper.updateByPrimaryKey(list77);
                }
                //委托契约，受託契约，其他契约  复合合同
                Contractcompound compound = new Contractcompound();
                compound.setOwner(list.getUser_id());
                List<Contractcompound> contractcompoundlist = contractcompoundMapper.select(compound);
                for (Contractcompound list78 : contractcompoundlist) {
                    list78.setModifyon(new Date());
                    list78.setOwner(list.getUserdata());
                    contractcompoundMapper.updateByPrimaryKey(list78);
                }
                //报价单
                Quotation quotation2 = new Quotation();
                quotation2.setOwner(list.getUser_id());
                List<Quotation> quotationlist = quotationMapper.select(quotation2);
                for (Quotation list79 : quotationlist) {
                    list79.setModifyon(new Date());
                    list79.setOwner(list.getUserdata());
                    quotationMapper.updateByPrimaryKey(list79);
                }
                //该非判定
                NonJudgment nonJudgment2 = new NonJudgment();
                nonJudgment2.setOwner(list.getUser_id());
                List<NonJudgment> nonjudgmentlist = nonJudgmentMapper.select(nonJudgment2);
                for (NonJudgment list80 : nonjudgmentlist) {
                    list80.setModifyon(new Date());
                    list80.setOwner(list.getUserdata());
                    nonJudgmentMapper.updateByPrimaryKey(list80);
                }
                //契约书
                Contract contract2 = new Contract();
                contract2.setOwner(list.getUser_id());
                List<Contract> contractlist = contractMapper.select(contract2);
                for (Contract list81 : contractlist) {
                    list81.setModifyon(new Date());
                    list81.setOwner(list.getUserdata());
                    contractMapper.updateByPrimaryKey(list81);
                }
                //决裁书
                Award award2 = new Award();
                award2.setOwner(list.getUser_id());
                List<Award> awardlist = AwardMapper.select(award2);
                for (Award list82 : awardlist) {
                    list82.setModifyon(new Date());
                    list82.setOwner(list.getUserdata());
                    AwardMapper.updateByPrimaryKey(list82);
                }
                //纳品书
                Napalm napalm2 = new Napalm();
                napalm2.setOwner(list.getUser_id());
                List<Napalm> napalmlist = napalmMapper.select(napalm2);
                for (Napalm list83 : napalmlist) {
                    list83.setModifyon(new Date());
                    list83.setOwner(list.getUserdata());
                    napalmMapper.updateByPrimaryKey(list83);
                }
                //请求书
                Petition petition2 = new Petition();
                petition2.setOwner(list.getUser_id());
                List<Petition> petitionlist = PetitionMapper.select(petition2);
                for (Petition list84 : petitionlist) {
                    list84.setModifyon(new Date());
                    list84.setOwner(list.getUserdata());
                    PetitionMapper.updateByPrimaryKey(list84);
                }

            }
        }


    }


    //更新
    @Override
    public void update2(StaffexitprocedureVo staffexitprocedureVo, TokenModel tokenModel) throws Exception {
        Staffexitproce staffexitproce = new Staffexitproce();
        StartWorkflowVo startWorkflowVo = new StartWorkflowVo();
        startWorkflowVo.setDataId(staffexitprocedureVo.getStaffexitproce().getStaffexitproce_id());
        List<WorkflowLogDetailVo> wfList = workflowServices.ViewWorkflow2(startWorkflowVo, tokenModel.getLocale());
        if (staffexitprocedureVo.getStaffexitproce().getStatus().equals("4")) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String Userid = staffexitprocedureVo.getStaffexitproce().getUser_id();
//            if (wfList.size() > 0) {
//                Userid = wfList.get(0).getUserId();
//            }
            String username = "";
            String center = "";
            String group = "";
            String team = "";
            Query query3 = new Query();
            query3.addCriteria(Criteria.where("userid").is(staffexitprocedureVo.getStaffexitproce().getUser_id()));
            CustomerInfo customerInfo2 = mongoTemplate.findOne(query3, CustomerInfo.class);
            if (customerInfo2 != null) {
                username = customerInfo2.getUserinfo().getCustomername();
            }
            ToDoNotice toDoNotice3 = new ToDoNotice();
            toDoNotice3.setTitle("【" + username + "离职确认流程结束】");
            toDoNotice3.setInitiator(Userid);
            toDoNotice3.setContent("您的离职申请已审批通过！");
            toDoNotice3.setDataid(staffexitprocedureVo.getStaffexitproce().getStaffexitproce_id());
            toDoNotice3.setUrl("/PFANS2032FormView");
            toDoNotice3.setWorkflowurl("/PFANS2032View");
            toDoNotice3.preInsert(tokenModel);
            toDoNotice3.setOwner(staffexitprocedureVo.getStaffexitproce().getReporter());
            toDoNoticeService.save(toDoNotice3);

            Projectsystem projectsystem = new Projectsystem();
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
            projectsystem.setName(staffexitprocedureVo.getStaffexitproce().getUser_id());
            List<Projectsystem> projectsystemList = projectsystemMapper.select(projectsystem);
            for (Projectsystem pro : projectsystemList) {
                String Date = sdf1.format(pro.getExittime());
                String Date1 = sdf1.format(new Date());
                if (Integer.valueOf(Date) >= Integer.valueOf(Date1)) {
                    //20210202 GBB 离职者调书审批通过之后更新项目退场日 start
                    //pro.setExittime(new Date());
                    pro.setExittime(staffexitprocedureVo.getStaffexitproce().getResignation_date());                    ;
                    //20210202 GBB 离职者调书审批通过之后更新项目退场日 end
                    projectsystemMapper.updateByPrimaryKeySelective(pro);
                }
            }

            Prosystem prosystem = new Prosystem();
            prosystem.setName(staffexitprocedureVo.getStaffexitproce().getUser_id());
            List<Prosystem> prosystemList = prosystemMapper.select(prosystem);
            for (Prosystem prolist : prosystemList) {
                String Date = sdf1.format(prolist.getExittime());
                String Date1 = sdf1.format(new Date());
                if (Integer.valueOf(Date) >= Integer.valueOf(Date1)) {
                    //20210202 GBB 离职者调书审批通过之后更新项目退场日 start
                    //prolist.setExittime(new Date());
                    prolist.setExittime(staffexitprocedureVo.getStaffexitproce().getResignation_date());                    ;
                    //20210202 GBB 离职者调书审批通过之后更新项目退场日 end
                    prosystemMapper.updateByPrimaryKeySelective(prolist);
                }
            }
            //离职卡号更新只有系统服务可操作   20210201  gbb start
//            Query query = new Query();
//            query.addCriteria(Criteria.where("userid").is(staffexitprocedureVo.getStaffexitproce().getUser_id()));
//            CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
//            if (customerInfo != null) {
//                customerInfo.getUserinfo().setJobnumber("00000");
//            }
//            mongoTemplate.save(customerInfo);
            //离职卡号更新只有系统服务可操作   20210201  gbb end


            ToDoNotice condition = new ToDoNotice();
            //update gbb 20210326 退职者调书审批通过之后将该数据涉及到的代办更新办结状态 start
            condition.setDataid(staffexitprocedureVo.getStaffexitproce().getStaffexitproce_id());
            //update gbb 20210326 退职者调书审批通过之后将该数据涉及到的代办更新办结状态 end
            condition.setOwner(staffexitprocedureVo.getStaffexitproce().getUser_id());
            condition.setStatus(AuthConstants.TODO_STATUS_TODO);
            List<ToDoNotice> list = todoNoticeMapper.select(condition);
            for (ToDoNotice todono : list) {
                todono.preUpdate(tokenModel);
                todono.setStatus(AuthConstants.TODO_STATUS_DONE);
                todono.setOwner(wfList.get(0).getUserId());
                todoNoticeMapper.updateByPrimaryKeySelective(todono);
            }
        }
//        }
//        if(staffexitprocedureVo.getStaffexitprocedure().getStage().equals("1") && staffexitprocedureVo.getStaffexitprocedure().getStatus().equals("4")) {
        BeanUtils.copyProperties(staffexitprocedureVo.getStaffexitproce(), staffexitproce);
        staffexitproce.preUpdate(tokenModel);

        //add ccm 20210513
        Staffexitproce sta = new Staffexitproce();
        sta = staffexitproceMapper.selectByPrimaryKey(staffexitprocedureVo.getStaffexitproce().getStaffexitproce_id());
        if(sta.getStatus().equals("0") && staffexitprocedureVo.getStaffexitproce().getStatus().equals("2"))
        {
            //发提醒给离职者
            ToDoNotice toDoNotice = new ToDoNotice();
            toDoNotice.setTitle("【您的调书已经创建完成，请选中待办跳转到调书页面处理离职手续！】");
            toDoNotice.setInitiator(staffexitprocedureVo.getStaffexitproce().getReporter());
            toDoNotice.setContent("您的调书已经创建完成，请处理离职手续！");
            toDoNotice.setDataid(staffexitprocedureVo.getStaffexitproce().getStaffexitproce_id());
            toDoNotice.setUrl("/PFANS2032FormView");
            toDoNotice.setWorkflowurl("/PFANS2032View");
            toDoNotice.preInsert(tokenModel);
            toDoNotice.setOwner(staffexitprocedureVo.getStaffexitproce().getUser_id());
            toDoNoticeService.save(toDoNotice);
        }
        //add ccm 20210513

        staffexitproceMapper.updateByPrimaryKey(staffexitproce);
        String staffexitproceid = staffexitproce.getStaffexitproce_id();
        Citation cita = new Citation();
        cita.setStaffexitproce_id(staffexitproceid);
        citationMapper.delete(cita);
        List<Citation> citationlist = staffexitprocedureVo.getCitation();
        if (citationlist != null) {
            int rowundex = 0;
            for (Citation citation : citationlist) {
                rowundex = rowundex + 1;
                citation.preInsert(tokenModel);
                citation.setCitation_id(UUID.randomUUID().toString());
                citation.setStaffexitproce_id(staffexitproceid);
                citation.setRowindex(rowundex);
                citationMapper.insertSelective(citation);
            }
        }
    }

    @Override
    public List<Staffexitproce> getList(Staffexitproce staffexitproce, TokenModel tokenModel) throws Exception {
        return staffexitproceMapper.select(staffexitproce);
    }

    @Override
    public List<Staffexitprocedure> getList2(Staffexitprocedure staffexitprocedure, TokenModel tokenModel) throws Exception {
        return staffexitprocedureMapper.select(staffexitprocedure);
    }

    //新建
    @Override
    public void insert(StaffexitprocedureVo staffexitprocedureVo, TokenModel tokenModel) throws Exception {
        String staffexitprocedureid = UUID.randomUUID().toString();
        Staffexitprocedure staffexitprocedure = new Staffexitprocedure();
        BeanUtils.copyProperties(staffexitprocedureVo.getStaffexitprocedure(), staffexitprocedure);
        staffexitprocedure.preInsert(tokenModel);
        staffexitprocedure.setStaffexitprocedure_id(staffexitprocedureid);
        staffexitprocedureMapper.insertSelective(staffexitprocedure);
        List<Citation> citationlist = staffexitprocedureVo.getCitation();
        if (citationlist != null) {
            int rowundex = 0;
            for (Citation citation : citationlist) {
                rowundex = rowundex + 1;
                citation.preInsert(tokenModel);
                citation.setCitation_id(UUID.randomUUID().toString());
                citation.setStaffexitproce_id(staffexitprocedureid);
                citation.setRowindex(rowundex);
                citationMapper.insertSelective(citation);
            }
        }
    }

    //新建
    @Override
    public void insert2(StaffexitprocedureVo staffexitprocedureVo, TokenModel tokenModel) throws Exception {
        String staffexitproceid = UUID.randomUUID().toString();
        Staffexitproce staffexitproce = new Staffexitproce();
        BeanUtils.copyProperties(staffexitprocedureVo.getStaffexitproce(), staffexitproce);
        staffexitproce.preInsert(tokenModel);
        staffexitproce.setStaffexitproce_id(staffexitproceid);
        staffexitproce.setOwner(staffexitprocedureVo.getStaffexitproce().getReporter());
        staffexitproce.setVersion(0);
        staffexitproceMapper.insertSelective(staffexitproce);
        List<Citation> citationlist = staffexitprocedureVo.getCitation();
        if (citationlist != null) {
            int rowundex = 0;
            for (Citation citation : citationlist) {
                rowundex = rowundex + 1;
                citation.preInsert(tokenModel);
                citation.setCitation_id(UUID.randomUUID().toString());
                citation.setStaffexitproce_id(staffexitproceid);
                citation.setRowindex(rowundex);
                citationMapper.insertSelective(citation);
            }
        }
    }

    public void updateRetireDate(StaffexitprocedureVo staffexitprocedureVo) throws Exception {
        Date date = staffexitprocedureVo.getStaffexitprocedure().getHope_exit_date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);//设置起时间
        cal.add(Calendar.DATE, -1);//减1天
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String aaa = "T16:00:00.000Z";
        String resignation_date = sdf.format(cal.getTime()) + aaa;
        Query query3 = new Query();
        query3.addCriteria(Criteria.where("userid").is(staffexitprocedureVo.getStaffexitprocedure().getUser_id()));
        CustomerInfo customerInfo2 = mongoTemplate.findOne(query3, CustomerInfo.class);
        if (customerInfo2 != null) {
            customerInfo2.getUserinfo().setResignation_date(resignation_date);
        }

        mongoTemplate.save(customerInfo2);
        //add ccm 0721 更新离职日时，更新考勤数据
        annualLeaveService.getattendanceByuser(staffexitprocedureVo.getStaffexitprocedure().getUser_id());
        //add ccm 0721 更新离职日时，更新考勤数据
    }

    private void updateAnnualDays(StaffexitprocedureVo staffexitprocedureVo) throws Exception {
        int result = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        AnnualLeave annualLeave = new AnnualLeave();
        Calendar now = Calendar.getInstance();
        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();
        Date _now = sdf.parse(sdf.format(now.getTime()));
        now.setTime(_now);
        int year = now.get(Calendar.MONTH) < 3 ? now.get(Calendar.YEAR) - 1 : now.get(Calendar.YEAR);
        annualLeave.setYears(year + "");
        annualLeave.setUser_id(staffexitprocedureVo.getStaffexitprocedure().getUser_id());
        AnnualLeave _annualLeave = annualLeaveMapper.selectOne(annualLeave);
        if (_annualLeave != null) {
            min.setTime(staffexitprocedureVo.getStaffexitprocedure().getHope_exit_date());
            Date date = sdf.parse(year + "-" + "04" + "-" + "01");
            max.setTime(date);
            while (min.before(max)) {
                result++;
                min.add(Calendar.MONTH, 1);
            }
            Double annual_leave_thisyear = _annualLeave.getAnnual_leave_thisyear().doubleValue();
            BigDecimal annualRest = new BigDecimal((float) (12 - result) / 12 * annual_leave_thisyear);
            Double _annual_leave_thisyear = Math.floor(annualRest.doubleValue());
            _annualLeave.setAnnual_leave_thisyear(BigDecimal.valueOf(_annual_leave_thisyear));
            Double deduct_annual_leave_thisyear = _annualLeave.getDeduct_annual_leave_thisyear().doubleValue();
            _annualLeave.setRemaining_annual_leave_thisyear(BigDecimal.valueOf(_annual_leave_thisyear - deduct_annual_leave_thisyear));

//            Double paid_leave_thisyear = _annualLeave.getPaid_leave_thisyear().doubleValue();
//            BigDecimal _annualRest = new BigDecimal((float) (12 - result) / 12 * paid_leave_thisyear);
//            Double _paid_leave_thisyear = Math.floor(_annualRest.doubleValue());
//            _annualLeave.setPaid_leave_thisyear(BigDecimal.valueOf(_paid_leave_thisyear));
//            Double deduct_paid_leave_thisyear = _annualLeave.getDeduct_paid_leave_thisyear().doubleValue();
//            _annualLeave.setRemaining_annual_leave_thisyear(BigDecimal.valueOf(_paid_leave_thisyear - deduct_paid_leave_thisyear));

            annualLeaveMapper.updateByPrimaryKeySelective(_annualLeave);


        }
    }
}
