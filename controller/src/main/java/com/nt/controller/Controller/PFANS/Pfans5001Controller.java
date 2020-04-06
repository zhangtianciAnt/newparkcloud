package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS5000.CompanyProjects;
import com.nt.dao_Pfans.PFANS5000.StageInformation;
import com.nt.dao_Pfans.PFANS5000.Vo.CompanyProjectsVo;
import com.nt.dao_Pfans.PFANS5000.Vo.LogmanagementStatusVo;
import com.nt.dao_Pfans.PFANS6000.Customerinfor;
import com.nt.dao_Pfans.PFANS6000.Expatriatesinfor;
import com.nt.dao_Pfans.PFANS6000.Supplierinfor;
import com.nt.service_pfans.PFANS5000.CompanyProjectsService;
import com.nt.service_pfans.PFANS5000.LogManagementService;
import com.nt.service_pfans.PFANS6000.CustomerinforService;
import com.nt.service_pfans.PFANS6000.ExpatriatesinforService;
import com.nt.service_pfans.PFANS6000.SupplierinforService;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.RequestUtils;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

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
        return ApiResult.success(companyProjectsService.getList2(flag));
    }

}
