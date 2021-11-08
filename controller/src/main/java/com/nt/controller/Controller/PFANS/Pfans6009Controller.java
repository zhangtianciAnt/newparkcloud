package com.nt.controller.Controller.PFANS;

import com.mysql.jdbc.StringUtils;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.Vo.DepartmentVo;
import com.nt.dao_Pfans.PFANS6000.Coststatistics;
import com.nt.dao_Pfans.PFANS6000.CoststatisticsVo;
import com.nt.dao_Workflow.Vo.StartWorkflowVo;
import com.nt.dao_Workflow.Vo.WorkflowLogDetailVo;
import com.nt.service_Org.OrgTreeService;
import com.nt.service_WorkFlow.WorkflowServices;
import com.nt.service_pfans.PFANS6000.CompanyStatisticsService;
import com.nt.service_pfans.PFANS6000.CoststatisticsService;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/companystatistics")
public class Pfans6009Controller {

    @Autowired
    private CompanyStatisticsService companyStatisticsService;


    @Autowired
    private TokenService tokenService;
    @Autowired
    private OrgTreeService orgTreeService;
    @Autowired
    private WorkflowServices workflowServices;

    @Autowired
    private MongoTemplate mongoTemplate;

    @RequestMapping(value = "/getCompanyReport1", method = {RequestMethod.GET})
    public ApiResult getCompanyReport1(String groupid,String years, HttpServletRequest request) throws Exception {
        if(groupid.equals("all"))
        {
            groupid = "";
        }
        else if(StringUtils.isNullOrEmpty(groupid))
        {
            groupid = "1";
        }
        Coststatistics coststatistics = new Coststatistics();
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(companyStatisticsService.getCostsByGrpAndY(groupid,years));
    }


    @RequestMapping(value = "/getCompanyReport2", method = {RequestMethod.GET})
    public ApiResult getCompanyReport2(String groupid,String years,HttpServletRequest request) throws Exception {
        if(groupid.equals("all"))
        {
            groupid = "";
        }
        else if(StringUtils.isNullOrEmpty(groupid))
        {
            groupid = "1";
        }
        Coststatistics coststatistics = new Coststatistics();
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(companyStatisticsService.getWorkTimes(groupid,years));
    }

    @RequestMapping(value = "/getCompanyReport3", method = {RequestMethod.GET})
    public ApiResult getCompanyReport3(String groupid,String years,HttpServletRequest request) throws Exception {
        if(groupid.equals("all"))
        {
            groupid = "";
        }
        else if(StringUtils.isNullOrEmpty(groupid))
        {
            groupid = "1";
        }
        Coststatistics coststatistics = new Coststatistics();
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(companyStatisticsService.getWorkerCounts(groupid,years));
    }

    /**
     * 导出Excel
     *
     */
    @RequestMapping(value = "/downloadExcel", method = { RequestMethod.GET })
    public ApiResult downloadExcel(String groupid,String years,HttpServletRequest request, HttpServletResponse resp) {
        try {
            XSSFWorkbook work =  companyStatisticsService.downloadExcel(groupid,years,request,resp);
            OutputStream os = resp.getOutputStream();// 取得输出流
            String fileName = "任务清单";
            resp.setContentType("application/vnd.ms-excel;charset=utf-8");
            resp.setHeader("Content-Disposition", "attachment;filename="
                    + new String((fileName + ".xls").getBytes(), "iso-8859-1"));
            work.write(os);
            os.close();
            return ApiResult.success();
        }catch (LogicalException e) {
            return ApiResult.fail(e.getMessage());
        } catch (Exception e) {
            return ApiResult.fail("操作失败！");
        }
    }

    /**
     * 月度费用总览导出pdf
     *
     */
    @RequestMapping(value = "/downloadPdf", method = { RequestMethod.GET })
    public void downloadPdf(String dates,HttpServletRequest request, HttpServletResponse resp)  throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);

        List<DepartmentVo> allDepartment = orgTreeService.getAllDepartment();
        String strYears = dates.substring(0,4);
        String strMonths = dates.substring(5,7);
        if(Integer.valueOf(dates.substring(5,7)) == 1 || Integer.valueOf(dates.substring(5,7)) == 2 || Integer.valueOf(dates.substring(5,7)) == 3){
            strYears = String.valueOf(Integer.valueOf(strYears) - 1);
        }
        List pdfMap = companyStatisticsService.downloadPdf(dates);
        Map<String, Object> data = new HashMap<>();
        dates = dates.replace("-"," 年 ");
        data.put("dates", dates.replace("-"," 年 "));
        HashMap totalCostMap = (HashMap) pdfMap.get(pdfMap.size() - 1);
        pdfMap.remove(pdfMap.size() - 1);
        data.put("dataList", pdfMap);
        //承认人
        if(allDepartment.size() > 0){
            Query query = new Query();
            for(DepartmentVo depVo : allDepartment){
                // 构内工数
                data.put("manhourCount" + depVo.getDepartmentEn(), totalCostMap.get("manhourCount" + depVo.getDepartmentEn()));
                // 构外工数
                data.put("manhourfCount" + depVo.getDepartmentEn(), totalCostMap.get("manhourfCount" + depVo.getDepartmentEn()));
                // 部门组件 + 年 + 月
                String strDataid = depVo.getDepartmentId() + "," + strYears + "," + strMonths;
                StartWorkflowVo startWorkflowVo = new StartWorkflowVo();
                startWorkflowVo.setDataId(strDataid);
                // 审批人
                List<WorkflowLogDetailVo> wfList = workflowServices.ViewWorkflow2(startWorkflowVo, tokenModel.getLocale());
                if (wfList.size() > 0) {
                    //月度费用总览pdf增加发起者 1104 ztc fr
                    query = new Query();
                    query.addCriteria(Criteria.where("userid").is(wfList.get(1).getUserId()));
                    CustomerInfo customerInfoOne = mongoTemplate.findOne(query, CustomerInfo.class);
                    if (customerInfoOne != null) {
                        // 发起人图片
                        String strUser = sign.startGraphics2D(customerInfoOne.getUserinfo().getCustomername());
                        data.put(depVo.getDepartmentEn() + "UserOne", strUser);
                    }
                    //月度费用总览pdf增加发起者 1104 ztc to
                    query = new Query();
                    query.addCriteria(Criteria.where("userid").is(wfList.get(0).getUserId()));
                    CustomerInfo customerInfoTwo = mongoTemplate.findOne(query, CustomerInfo.class);
                    if (customerInfoTwo != null) {
                        // 审批人图片
                        String strUser = sign.startGraphics2D(customerInfoTwo.getUserinfo().getCustomername());
                        data.put(depVo.getDepartmentEn() + "User", strUser);
                    }
                }
            }
        }
        ExcelOutPutUtil.OutPutPdf("月度费用总览", "yuedufeiyongzonglan.xls", data, resp);

        ExcelOutPutUtil.deleteDir(AuthConstants.FILE_DIRECTORY + "image");
    }
}
