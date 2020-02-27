package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS5000.CompanyProjects;
import com.nt.dao_Pfans.PFANS5000.StageInformation;
import com.nt.dao_Pfans.PFANS5000.Vo.CompanyProjectsVo;
import com.nt.dao_Pfans.PFANS6000.Customerinfor;
import com.nt.dao_Pfans.PFANS6000.Expatriatesinfor;
import com.nt.dao_Pfans.PFANS6000.Supplierinfor;
import com.nt.service_pfans.PFANS5000.CompanyProjectsService;
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

    @RequestMapping(value="/list", method={RequestMethod.POST})
    public ApiResult List(@RequestBody CompanyProjects companyProjects, HttpServletRequest request) throws Exception {
        if (companyProjects == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        companyProjects.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(companyProjectsService.list(companyProjects));
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
        return ApiResult.success(companyProjectsService.getSiteList());
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
    public ApiResult getPjList(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(companyProjectsService.getPjList());
    }
}
