package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS5000.CompanyProjects;
import com.nt.dao_Pfans.PFANS5000.Vo.CompanyProjectsVo;
import com.nt.service_pfans.PFANS5000.CompanyProjectsService;
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
}