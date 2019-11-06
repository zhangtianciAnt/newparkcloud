package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS2000.Staffexitprocedure;
import com.nt.dao_Pfans.PFANS2000.Vo.StaffexitprocedureVo;
import com.nt.service_pfans.PFANS2000.StaffexitprocedureService;
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
@RequestMapping("/staffexitprocedure")
public class Pfans2026Controller {

    @Autowired
    private StaffexitprocedureService staffexitprocedureService;
    @Autowired
    private TokenService tokenService;
    /*
     * 列表查看
     * */
    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    public ApiResult get(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        Staffexitprocedure staffexitprocedure=new Staffexitprocedure();
        staffexitprocedure.setOwners(tokenModel.getOwnerList());
       return ApiResult.success(staffexitprocedureService.get(staffexitprocedure));
    }
    /**
     * 查看
     */
    @RequestMapping(value = "/selectById", method = {RequestMethod.GET})
    public ApiResult one(String staffexitprocedureid, HttpServletRequest request) throws Exception {
        if(staffexitprocedureid==null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03,RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(staffexitprocedureService.selectById(staffexitprocedureid));
    }
    /**
     *
     * 修改
     */
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody StaffexitprocedureVo staffexitprocedureVo, HttpServletRequest request) throws Exception {
        if(staffexitprocedureVo==null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03,RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel=tokenService.getToken(request);
        staffexitprocedureService.update(staffexitprocedureVo,tokenModel);
        return ApiResult.success();

    }
    /**
     * 新建
     */
    @RequestMapping(value = "insert", method = { RequestMethod.POST })
    public ApiResult insert(@RequestBody StaffexitprocedureVo staffexitprocedureVo, HttpServletRequest request) throws Exception{
        if (staffexitprocedureVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03,RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        staffexitprocedureService.insert(staffexitprocedureVo,tokenModel);
        return ApiResult.success();
    }
}
