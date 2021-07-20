package com.nt.controller.Controller.PFANS;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.nt.dao_Pfans.PFANS1000.Contractapplication;
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
import com.nt.service_pfans.PFANS1000.ContractapplicationService;
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
import jxl.Sheet;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import java.io.*;
import java.sql.Array;
import java.text.SimpleDateFormat;
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

    @Autowired
    private ContractapplicationService contractapplicationService;

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
     * PJ起案数据结转
     */
    @RequestMapping(value = "/update1", method = {RequestMethod.POST})
    public ApiResult update1(@RequestBody CompanyProjects companyprojects, HttpServletRequest request) throws Exception {
        if(companyprojects==null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03,RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel=tokenService.getToken(request);
        companyProjectsService.update1(companyprojects,tokenModel);
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

    //根据合同号查合同区间 scc
    @RequestMapping(value = "/getcontra", method={RequestMethod.GET})
    public ApiResult getContranumber(HttpServletRequest request, String contra) throws Exception {
        if (StringUtils.isEmpty(contra)) {
            return ApiResult.fail("当前未找到合同");
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(contractapplicationService.getContranumber(contra,tokenModel));
    }
    //根据合同号查合同区间 scc
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
//    @RequestMapping(value = "/reportCheck", method={RequestMethod.GET})
//    public ApiResult report() throws Exception {
//        List<CompanyProjectsReportCheckVo> cprcList = companyProjectsService.reportCheck();
//        ArrayList<Map<String, Object>> rowLists = CollUtil.newArrayList();
//        int temMax = 1;
//        int cow = 0;
//        int temp = 0;
//        SimpleDateFormat format0 = new SimpleDateFormat("yyyy/MM/dd");
//        SXSSFWorkbook sxssfWorkbook = new SXSSFWorkbook();
//        SXSSFSheet sheet = sxssfWorkbook.createSheet("项目相关信息");
//        SXSSFRow headTitle = sheet.createRow(0);
//        headTitle.createCell(0).setCellValue("重点工程项目计划表");
//        SXSSFRow headRow = (SXSSFRow) sheet.createRow(0);
//        headRow.createCell(0).setCellValue("项目编号");
//        headRow.createCell(1).setCellValue("项目名称");
//        headRow.createCell(2).setCellValue("开始时间");
//        headRow.createCell(3).setCellValue("预计完成时间");
//        headRow.createCell(4).setCellValue("受託工数（人月）");
//        headRow.createCell(5).setCellValue("工作阶段");
//        headRow.createCell(6).setCellValue("阶段成果物");
//        headRow.createCell(7).setCellValue("预计工数（人月）");
//        headRow.createCell(8).setCellValue("实际工数（人月）");
//        headRow.createCell(9).setCellValue("预计开始时间");
//        headRow.createCell(10).setCellValue("预计结束时间");
//        headRow.createCell(11).setCellValue("实际开始时间");
//        headRow.createCell(12).setCellValue("实际结束时间");
//        headRow.createCell(13).setCellValue("社内/社外");
//        headRow.createCell(14).setCellValue("姓名");
//        headRow.createCell(15).setCellValue("入场时间");
//        headRow.createCell(16).setCellValue("退出时间");
//        headRow.createCell(17).setCellValue("合同");
//        headRow.createCell(18).setCellValue("请求方式");
//        headRow.createCell(19).setCellValue("请求期间");
//        headRow.createCell(20).setCellValue("请求金额");
//        headRow.createCell(21).setCellValue("分配金额");
//        FileOutputStream stream = new FileOutputStream("C:/aa/student.xlsx",true);
//
//        for (CompanyProjectsReportCheckVo item : cprcList
//        ) {
//            int compNum = 0;
//            int sysmNum = 0;
//            int contNum = 0;
//            int stagNum = 0;
//            if(item.getProjectsystemList() != null){
//                sysmNum = item.getProjectsystemList().size();
//            }
//            if(item.getStageInformationList() != null){
//                stagNum = item.getStageInformationList().size();
//            }
//            if(item.getProjectContractList() != null){
//                contNum = item.getProjectContractList().size();
//            }
//
//            int maxNum = ((maxNum=(sysmNum > stagNum) ? sysmNum : stagNum) > contNum ? maxNum : contNum);
//
//            temp = maxNum;
//
//            maxNum =cow + maxNum;
//            for(int i = 0; i < 5; i++){
//                CellRangeAddress firstRegion = null;
//                SXSSFRow dataRow = (SXSSFRow) sheet.createRow(sheet.getLastRowNum() + 1);
//
//                for(int j = 0; j < temp; j++) {
//
//                    if(compNum == 0){
//                        dataRow.createCell(0).setCellValue(item.getCompanyProjects().getNumbers());
//                        dataRow.createCell(1).setCellValue(item.getCompanyProjects().getProject_name() );
////                    dataRow.createCell(2).setCellValue(item.getCompanyProjects().getStartdate() != null ? format0.format(item.getCompanyProjects().getStartdate()) : "");
//                        dataRow.createCell(2).setCellValue(item.getCompanyProjects().getStartdate());
////                    dataRow.createCell(3).setCellValue(item.getCompanyProjects().getEnddate() != null ? format0.format(item.getCompanyProjects().getEnddate()) : "");
//                        dataRow.createCell(3).setCellValue(item.getCompanyProjects().getEnddate());
//                        dataRow.createCell(4).setCellValue( item.getCompanyProjects().getManmonth());
//                    }
//                    if (j < stagNum) {
//                        dataRow.createCell(5).setCellValue(item.getStageInformationList().get(j).getPhase());
//                        dataRow.createCell(6).setCellValue(item.getStageInformationList().get(j).getStageproduct());
//                        dataRow.createCell(7).setCellValue(item.getStageInformationList().get(j).getEstimatedwork());
//                        dataRow.createCell(8).setCellValue(item.getStageInformationList().get(j).getActualwork());
////                    dataRow.createCell(9).setCellValue(item.getStageInformationList().get(i).getEstimatedstarttime() != null ? format0.format(item.getStageInformationList().get(i).getEstimatedstarttime()) : "");
//                        dataRow.createCell(9).setCellValue(item.getStageInformationList().get(j).getEstimatedstarttime());
////                    dataRow.createCell(10).setCellValue(item.getStageInformationList().get(i).getEstimatedendtime() != null ? format0.format(item.getStageInformationList().get(i).getEstimatedendtime()) : "");
//                        dataRow.createCell(10).setCellValue(item.getStageInformationList().get(j).getEstimatedendtime());
//                        dataRow.createCell(11).setCellValue(item.getStageInformationList().get(j).getActualstarttime());
//                        dataRow.createCell(12).setCellValue(item.getStageInformationList().get(j).getActualendtime());
//                    }
//                    if (j < sysmNum) {
//                        dataRow.createCell(13).setCellValue(item.getProjectsystemList().get(j).getType().equals("0") ? "社内" : "社外");
//                        dataRow.createCell(14).setCellValue(item.getProjectsystemList().get(j).getName());
////                    dataRow.createCell(15).setCellValue(item.getProjectsystemList().get(i).getAdmissiontime() != null ? format0.format(item.getProjectsystemList().get(i).getAdmissiontime()) : "");
//                        dataRow.createCell(15).setCellValue(item.getProjectsystemList().get(j).getAdmissiontime());
////                    dataRow.createCell(16).setCellValue(item.getProjectsystemList().get(i).getExittime() != null ? format0.format(item.getProjectsystemList().get(i).getExittime()) : "");
//                        dataRow.createCell(16).setCellValue(item.getProjectsystemList().get(j).getExittime());
//                    }
////                if(i < contNum){
//                    if (j < contNum) {
//                        dataRow.createCell(17).setCellValue(item.getProjectContractList().get(j).getContract());
//                        dataRow.createCell(18).setCellValue(item.getProjectContractList().get(j).getClaimtype());
//                        dataRow.createCell(19).setCellValue(item.getProjectContractList().get(j).getWorkinghours());
//                        dataRow.createCell(20).setCellValue(item.getProjectContractList().get(j).getContractrequestamount());
//                        dataRow.createCell(21).setCellValue(item.getProjectContractList().get(j).getContractamount());
//                    }
//
//
//
//                    if(i >= 0) {
//                        firstRegion = new CellRangeAddress(temMax, maxNum, i, i);
//                        sheet.addMergedRegion(firstRegion);
//                    }
//            }}
//            temMax = maxNum + 1;
//            cow = maxNum;
//        }
//
//
//        sxssfWorkbook.write(stream);
////        InputStream is = new FileInputStream("C:/aa/student.xls");
////        //文件系统
////        POIFSFileSystem fs = new POIFSFileSystem(is);
////        //定义工作簿
////        XSSFWorkbook wb = new XSSFWorkbook(is);
////        HSSFWorkbook wb = new HSSFWorkbook(fs);
//
//        return ApiResult.success();
//    }
}
