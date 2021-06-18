package com.nt.controller.Controller.PFANS;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.nt.dao_Pfans.PFANS1000.Contractnumbercount;
import com.nt.dao_Pfans.PFANS2000.Vo.PersonalCostExpVo;
import com.nt.dao_Pfans.PFANS5000.CompanyProjects;
import com.nt.dao_Pfans.PFANS5000.ProjectContract;
import com.nt.dao_Pfans.PFANS5000.StageInformation;
import com.nt.dao_Pfans.PFANS5000.Vo.CompanyProjectsReportCheckVo;
import com.nt.dao_Pfans.PFANS5000.Vo.CompanyProjectsVo;
import com.nt.dao_Pfans.PFANS5000.Vo.LogmanagementStatusVo;
import com.nt.dao_Pfans.PFANS6000.Customerinfor;
import com.nt.dao_Pfans.PFANS6000.Expatriatesinfor;
import com.nt.dao_Pfans.PFANS6000.Supplierinfor;
import com.nt.service_pfans.PFANS1000.mapper.ContractnumbercountMapper;
import com.nt.service_pfans.PFANS5000.CompanyProjectsService;
import com.nt.service_pfans.PFANS5000.LogManagementService;
import com.nt.service_pfans.PFANS5000.mapper.ProjectContractMapper;
import com.nt.service_pfans.PFANS6000.CustomerinforService;
import com.nt.service_pfans.PFANS6000.ExpatriatesinforService;
import com.nt.service_pfans.PFANS6000.SupplierinforService;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.RequestUtils;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.sql.Array;
import java.util.*;

@RestController
@RequestMapping("/companyprojects")
public class Pfans5001Controller {

    @Autowired
    private CompanyProjectsService companyProjectsService;

    @Autowired
    private CustomerinforService customerinforService;

    @Autowired
    private ExpatriatesinforService expatriatesinforService;

    @Autowired
    private LogManagementService logmanagementService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ContractnumbercountMapper contractnumbercountMapper;

    @Autowired
    private ProjectContractMapper projectcontractMapper;


    /**
     * 查看
     */
    @RequestMapping(value = "/selectById", method = {RequestMethod.GET})
    public ApiResult selectById(String companyprojectsid, HttpServletRequest request) throws Exception {
        if(companyprojectsid==null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03,RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(companyProjectsService.selectById(companyprojectsid));
    }
    /**
     * 查看
     */
    @RequestMapping(value = "/selectConnumList", method = {RequestMethod.GET})
    public ApiResult selectConnumList(String contractnumbercount_id, HttpServletRequest request) throws Exception {
        if(contractnumbercount_id==null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03,RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(companyProjectsService.selectConnumList(contractnumbercount_id));
    }

    @RequestMapping(value = "/selectAll", method = {RequestMethod.GET})
    public ApiResult selectAll(HttpServletRequest request) throws Exception {
        return ApiResult.success(companyProjectsService.selectAll());
    }

    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public ApiResult List(@RequestBody CompanyProjects companyProjects, HttpServletRequest request) throws Exception {
        if (companyProjects == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        companyProjects.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(companyProjectsService.list(companyProjects));
    }

    @RequestMapping(value = "/list2", method = {RequestMethod.POST})
    public ApiResult List2(@RequestBody CompanyProjects companyProjects, HttpServletRequest request) throws Exception {
        if (companyProjects == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(companyProjectsService.list(companyProjects));
    }

    @RequestMapping(value = "/getPjnameList", method = {RequestMethod.POST})
    public ApiResult getPjnameList(CompanyProjects companyProjects, HttpServletRequest request) throws Exception {
        if (companyProjects == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(companyProjectsService.getPjnameList(companyProjects));
    }

    //获取外住人员所在的项目
    @RequestMapping(value = "/getCompanyProject", method = {RequestMethod.GET})
    public ApiResult getCompanyProject(String SyspName, HttpServletRequest request) throws Exception {
        if (SyspName == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(companyProjectsService.getCompanyProject(SyspName));
    }

    /**
     *
     * 获取工时确认列表
     */
    @RequestMapping(value = "/getProjectList", method = {RequestMethod.GET})
    public ApiResult getProjectList(String StrFlg, String StrDate,HttpServletRequest request) throws Exception {
        if (StrFlg == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(logmanagementService.getProjectList(StrFlg,StrDate,tokenModel));
    }

    /**
     *
     * 查询工时确认
     */
    @RequestMapping(value = "/getTimestart", method = {RequestMethod.GET})
    public ApiResult getTimestart(String project_id, String starttime,String endtime,HttpServletRequest request) throws Exception {
        if (project_id == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(logmanagementService.getTimestart(project_id,starttime,endtime));
    }

    /**
     *
     * 查询共通工时确认
     */
    @RequestMapping(value = "/getGroupTimestart", method = {RequestMethod.POST})
    public ApiResult getGroupTimestart(@RequestBody List<ArrayList> strData,HttpServletRequest request) throws Exception {
        if (strData == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        List<String> createby = strData.get(0);
        String starttime = strData.get(1).get(0).toString();
        String endtime = strData.get(1).get(1).toString();
        return ApiResult.success(logmanagementService.getGroupTimestart(createby,starttime,endtime));
    }

    /**
     *
     * 修改工时确认
     */
    @RequestMapping(value = "/updateTimestart", method = {RequestMethod.POST})
    public ApiResult updateTimestart(@RequestBody LogmanagementStatusVo LogmanagementStatusVo, HttpServletRequest request) throws Exception {
        if (LogmanagementStatusVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        logmanagementService.updateTimestart(LogmanagementStatusVo,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value="/list1", method={RequestMethod.POST})
    public ApiResult List1(HttpServletRequest request) throws Exception {
        CompanyProjects companyProjects = new CompanyProjects();
        TokenModel tokenModel = tokenService.getToken(request);
        companyProjects.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(companyProjectsService.logmanageMentVo(companyProjects));
    }

    /**
     *
     * 修改
     */
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody CompanyProjectsVo companyProjectsVo, HttpServletRequest request) throws Exception {
        if(companyProjectsVo==null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03,RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel=tokenService.getToken(request);
        companyProjectsService.update(companyProjectsVo,tokenModel);
        return ApiResult.success();
    }

    /**
     *
     * 修改
     */
//    合同号+请求回数
    @RequestMapping(value = "/upcnpro01", method = {RequestMethod.GET})
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public ApiResult upcnpro(TokenModel tokenModel) throws Exception {
        List<Contractnumbercount> contractnumbercountList = contractnumbercountMapper.selectAll();
        List<ProjectContract> projectContractsList = projectcontractMapper.selectAll();
        for (int i = 0; i < contractnumbercountList.size(); i++) {
            if (contractnumbercountList.get(i).getClaimtype() != null) {
                for (int h = 0; h < projectContractsList.size(); h++) {
                    if (projectContractsList.get(h).getClaimtype() != null ) {
                        ProjectContract projectContract = new ProjectContract();
                        BeanUtils.copyProperties(projectContractsList.get(h), projectContract);
                        if (projectContractsList.get(h).getContract().equals(contractnumbercountList.get(i).getContractnumber())
                                && projectContractsList.get(h).getClaimtype().equals(contractnumbercountList.get(i).getClaimtype())) {
                            projectContract.setProjectcontract_id(projectContractsList.get(h).getProjectcontract_id());
                            projectContract.setContractnumbercount_id(contractnumbercountList.get(i).getContractnumbercount_id());
                            projectcontractMapper.updateByPrimaryKey(projectContract);
                            break;
                        }
                    }
                }
            }
        }
        return ApiResult.success();
    }



//    合同号+金额
    @RequestMapping(value = "/upcnpro02", method = {RequestMethod.GET})
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public ApiResult upcnpro02(TokenModel tokenModel) throws Exception{
        List<Contractnumbercount> contractnumbercountList = contractnumbercountMapper.selectAll();
        List<ProjectContract> projectContractsList = projectcontractMapper.selectAll();
        for(int i = 0;i < contractnumbercountList.size(); i ++){
            if (contractnumbercountList.get(i).getClaimamount() != null) {
                for (int h = 0; h < projectContractsList.size(); h++) {
                    if (projectContractsList.get(h).getContractrequestamount() != null) {
                        ProjectContract projectContract = new ProjectContract();
                        BeanUtils.copyProperties(projectContractsList.get(h), projectContract);
                        if (projectContractsList.get(h).getContract().equals(contractnumbercountList.get(i).getContractnumber())
                                && projectContractsList.get(h).getContractrequestamount().equals(contractnumbercountList.get(i).getClaimamount())) {
                            projectContract.setProjectcontract_id(projectContractsList.get(h).getProjectcontract_id());
                            projectContract.setContractnumbercount_id(contractnumbercountList.get(i).getContractnumbercount_id());
                            projectcontractMapper.updateByPrimaryKey(projectContract);
                            break;
                        }
                    }
                }
            }
        }
        return ApiResult.success();
    }

//    合同号+devilfinshdate
    @RequestMapping(value = "/upcnpro03", method = {RequestMethod.GET})
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public ApiResult upcnpro03(TokenModel tokenModel) throws Exception{
        List<Contractnumbercount> contractnumbercountList = contractnumbercountMapper.selectAll();
        List<ProjectContract> projectContractsList = projectcontractMapper.selectAll();
        for(int i = 0;i < contractnumbercountList.size(); i ++){
            if (contractnumbercountList.get(i).getDeliveryfinshdate() != null) {
                for (int h = 0; h < projectContractsList.size(); h++) {
                    if (projectContractsList.get(h).getDeliveryfinshdate() != null && (!projectContractsList.get(h).getContract().equals("NS1911310010"))) {
                        ProjectContract projectContract = new ProjectContract();
                        BeanUtils.copyProperties(projectContractsList.get(h), projectContract);
                        if (projectContractsList.get(h).getContract().equals(contractnumbercountList.get(i).getContractnumber())
                                && projectContractsList.get(h).getDeliveryfinshdate().equals(contractnumbercountList.get(i).getDeliveryfinshdate())) {
                            projectContract.setProjectcontract_id(projectContractsList.get(h).getProjectcontract_id());
                            projectContract.setContractnumbercount_id(contractnumbercountList.get(i).getContractnumbercount_id());
                            projectcontractMapper.updateByPrimaryKey(projectContract);
                            break;
                        }
                    }
                }
            }
        }
        return ApiResult.success();
    }


    /**
     * 新建
     */
    @RequestMapping(value = "/insert", method = { RequestMethod.POST })
    public ApiResult insert(@RequestBody CompanyProjectsVo companyProjectsVo, HttpServletRequest request) throws Exception{
        if (companyProjectsVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03,RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        companyProjectsService.insert(companyProjectsVo,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/getcustomer", method = { RequestMethod.POST })
    public ApiResult getcustomer(@RequestBody Customerinfor customerinfor, HttpServletRequest request) throws Exception{
        if (customerinfor == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(customerinforService.getcustomerinfor(customerinfor, tokenModel));
    }

    @RequestMapping(value = "/getexpat", method = { RequestMethod.POST })
    public ApiResult getexpat(@RequestBody Expatriatesinfor expatriatesinfor, HttpServletRequest request) throws Exception{
        if (expatriatesinfor == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel=tokenService.getToken(request);
        return ApiResult.success(expatriatesinforService.getexpatriatesinfor(expatriatesinfor));
    }

    @RequestMapping(value = "/getstageInformation", method = { RequestMethod.POST })
    public ApiResult getstageInformation(@RequestBody StageInformation stageInformation, HttpServletRequest request) throws Exception{
        if (stageInformation == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel=tokenService.getToken(request);
        return ApiResult.success(companyProjectsService.getstageInformation(stageInformation));
    }

    /**
     * @方法名：getSiteList
     * @描述：获取现场管理列表
     * @创建日期：2020/02/25
     * @作者：zy
     * @参数：[]
     * @返回值：List<CompanyProjectsVo2>
     */
    @RequestMapping(value="/getSiteList", method={RequestMethod.GET})
    public ApiResult getSiteList(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        CompanyProjects companyProjects = new CompanyProjects();
        companyProjects.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(companyProjectsService.getSiteList(companyProjects));
    }

    @RequestMapping(value="/getSiteList3", method={RequestMethod.GET})
    public ApiResult getSiteList3(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        CompanyProjects companyProjects = new CompanyProjects();
        companyProjects.setOwners(tokenModel.getOwnerList());
        companyProjects.setOwner(tokenModel.getUserId());
        return ApiResult.success(companyProjectsService.getSiteList2(companyProjects));
    }

    @RequestMapping(value="/getSiteList4", method={RequestMethod.GET})
    public ApiResult getSiteList4(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        CompanyProjects companyProjects = new CompanyProjects();
        companyProjects.setOwners(tokenModel.getOwnerList());
        companyProjects.setOwner(tokenModel.getUserId());
        return ApiResult.success(companyProjectsService.getSiteList3(companyProjects));
    }

    @RequestMapping(value="/getSiteList5", method={RequestMethod.GET})
    public ApiResult getSiteList5(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        CompanyProjects companyProjects = new CompanyProjects();
        companyProjects.setOwners(tokenModel.getOwnerList());
        companyProjects.setOwner(tokenModel.getUserId());
        return ApiResult.success(companyProjectsService.getSiteList4(companyProjects));
    }

    @RequestMapping(value="/getSiteList2", method={RequestMethod.GET})
    public ApiResult getSiteList2(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        CompanyProjects companyProjects = new CompanyProjects();
        return ApiResult.success(companyProjectsService.getSiteList(companyProjects));
    }
    /**
     * @方法名：getPjList
     * @描述：获取PJ完了审批列表
     * @创建日期：2020/02/26
     * @作者：zy
     * @参数：[]
     * @返回值：List<CompanyProjectsVo2>
     */
    @RequestMapping(value="/getPjList", method={RequestMethod.GET})
    public ApiResult getPjList(HttpServletRequest request,String flag) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(companyProjectsService.getPjList(flag));
    }
    @RequestMapping(value="/getList2", method={RequestMethod.GET})
    public ApiResult getList2(HttpServletRequest request,String flag) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(companyProjectsService.getList2(flag, tokenModel.getOwnerList(), tokenModel.getUserId()));
    }
//zy start 报表追加 2021/06/13
    /**
     * @方法名：report
     * @描述：导出每个月项目报表
     * @创建日期：2021/06/11
     * @作者：zy
     * @参数：[]
     * @返回值：List<CompanyProjectsReport>
     */
    @RequestMapping(value = "/report", method={RequestMethod.GET})
    public ApiResult report(HttpServletRequest request, String start,String end) throws Exception {
        if (StringUtils.isEmpty(start) || StringUtils.isEmpty(end)) {
            return ApiResult.fail("开始时间，结束时间不能为空");
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(companyProjectsService.report(start, end, tokenModel.getOwnerList(), tokenModel.getUserId()));
    }
//zy end 报表追加 2021/06/13

    /**
     * @方法名：reportCheck
     * @描述：导出项目相关信息
     * @创建日期：2021/06/18
     * @作者：ztc
     * @参数：[
     * flag：0 项目开始时间小于2021-04-01，
     *       1 项目结束时间小于2021-04-01，且体制的结束时间大于2020-04-01；
     * time：时间格式[YYYY-MM-DD];
     * ]
     * @返回值：List<CompanyProjectsReport>
     */
    @RequestMapping(value = "/reportCheck", method={RequestMethod.GET})
    public ApiResult report() throws Exception {
        List<CompanyProjectsReportCheckVo> cprcList = companyProjectsService.reportCheck();
        ArrayList<Map<String, Object>> rowLists = CollUtil.newArrayList();

        for (CompanyProjectsReportCheckVo item : cprcList
        ) {
            int compNum = 0;
            int sysmNum = 0;
            int contNum = 0;
            int stagNum = 0;
            if(item.getProjectsystemList() != null){
                sysmNum = item.getProjectsystemList().size();
            }
            if(item.getStageInformationList() != null){
                stagNum = item.getStageInformationList().size();
            }
            if(item.getProjectContractList() != null){
                contNum = item.getProjectContractList().size();
            }

            int maxNum = ((maxNum=(sysmNum > stagNum) ? sysmNum : stagNum) > contNum ? maxNum : contNum);

            for(int i = 0; i < maxNum; i++){
                Map<String, Object> row = new LinkedHashMap<>();
                if(compNum == 0){
                    row.put("项目编号", item.getCompanyProjects().getNumbers());
                    row.put("项目名称", item.getCompanyProjects().getProject_name());
                    row.put("开始时间", item.getCompanyProjects().getStartdate());
                    row.put("预计完成时间", item.getCompanyProjects().getEnddate());
                    row.put("受託工数（人月）", item.getCompanyProjects().getManmonth());
                    compNum ++;
                }else{
                    row.put("项目编号", "");
                    row.put("项目名称", "");
                    row.put("开始时间", "");
                    row.put("预计完成时间", "");
                    row.put("受託工数（人月）", "");
                }
                if(i < stagNum){
                    row.put("工作阶段", item.getStageInformationList().get(i).getPhase());
                    row.put("阶段成果物", item.getStageInformationList().get(i).getStageproduct());
                    row.put("预计工数（人月）", item.getStageInformationList().get(i).getEstimatedwork());
                    row.put("实际工数（人月）", item.getStageInformationList().get(i).getActualwork());
                    row.put("预计开始时间", item.getStageInformationList().get(i).getEstimatedstarttime());
                    row.put("预计结束时间", item.getStageInformationList().get(i).getEstimatedendtime());
                    row.put("实际开始时间", item.getStageInformationList().get(i).getActualstarttime());
                    row.put("实际结束时间", item.getStageInformationList().get(i).getActualendtime());
                }else{
                    row.put("工作阶段", "");
                    row.put("阶段成果物", "");
                    row.put("预计工数（人月）", "");
                    row.put("实际工数（）", "");
                    row.put("预计开始时间", "");
                    row.put("预计结束时间", "");
                    row.put("实际开始时间", "");
                    row.put("实际结束时间", "");
                }
                if(i < sysmNum){
                    row.put("社内/社外", (item.getProjectsystemList().get(i).getType().equals("0") ? "社内" : "社外"));
                    row.put("姓名", item.getProjectsystemList().get(i).getName());
                    row.put("入场时间", item.getProjectsystemList().get(i).getAdmissiontime());
                    row.put("退出时间", item.getProjectsystemList().get(i).getExittime());
                }else{
                    row.put("社内/社外", "");
                    row.put("姓名", "");
                    row.put("入场时间", "");
                    row.put("退出时间", "");
                }
                if(i < contNum){
                    row.put("合同", item.getProjectContractList().get(i).getContract());
                    row.put("请求方式", item.getProjectContractList().get(i).getClaimtype());
                    row.put("请求期间", item.getProjectContractList().get(i).getWorkinghours());
                    row.put("请求金额", item.getProjectContractList().get(i).getContractrequestamount());
                    row.put("分配金额", item.getProjectContractList().get(i).getContractamount());
                }else{
                    row.put("合同", "");
                    row.put("请求方式", "");
                    row.put("请求期间", "");
                    row.put("请求金额", "");
                    row.put("分配金额", "");
                }
                rowLists.add(row);
            }
        }
        String destFilePath = "D:/" + "项目相关信息.xlsx";
        ExcelWriter writer = ExcelUtil.getWriter(destFilePath, "项目相关信息");
        writer.write(rowLists);
        writer.close();
        return ApiResult.success();
    }
}
