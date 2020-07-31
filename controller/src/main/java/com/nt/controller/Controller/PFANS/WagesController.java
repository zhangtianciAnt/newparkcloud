package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS2000.Wages;
import com.nt.service_pfans.PFANS2000.WagesService;
import com.nt.utils.ApiResult;
import com.nt.utils.ExcelOutPutUtil;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/wages")
public class WagesController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private WagesService wagesService;

    @RequestMapping(value = "/getWagesByGivingId", method = {RequestMethod.GET})
    public ApiResult getWagesByGivingId(HttpServletRequest request, @RequestParam String givingId) throws Exception {
        return new ApiResult(wagesService.getWagesByGivingId(givingId));
    }

    @RequestMapping(value = "/insertWages", method = {RequestMethod.POST})
    public ApiResult insertWages(HttpServletRequest request, @RequestBody List<Wages> wagesList) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        wagesService.insertWages(wagesList, tokenModel);
        return ApiResult.success();
    }
    //获取工资部门集计
    @RequestMapping(value = "/getWagesdepartment", method = {RequestMethod.GET})
    public ApiResult getWagesdepartment(HttpServletRequest request, String dates) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        return new ApiResult(wagesService.getWagesdepartment(dates));
    }
    //获取工资公司集计
    @RequestMapping(value = "/getWagecompany", method = {RequestMethod.GET})
    public ApiResult getWagecompany(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        return new ApiResult(wagesService.getWagecompany());
    }

    //获取离职人员工资
    @RequestMapping(value = "/getWagesByResign", method = {RequestMethod.GET})
    public ApiResult getWagesByResign(HttpServletRequest request, @RequestParam String user_id) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        return new ApiResult(wagesService.getWagesByResign(user_id,tokenModel));
    }

    //gbb 0724 模板下载 start
    @RequestMapping(value = "/download", method = {RequestMethod.GET})
    public void download(String type, HttpServletResponse response) throws Exception {
        Map<String, Object> data = new HashMap<>();
        String templateName = null;
        String fileName = null;
        if ( "0".equals(type) ) {
            templateName = "othertwo.xlsx";
            fileName = "其他2导入模板";
        }
        if ( "1".equals(type) ) {
            templateName = "otherfour.xlsx";
            fileName = "其他4导入模板";
        }
        if ( "2".equals(type) ) {
            templateName = "otherfive.xlsx";
            fileName = "其他5导入模板";
        }
        if ( "3".equals(type) ) {
            templateName = "lunarbasic.xlsx";
            fileName = "月度赏与导入模板";
        }
        if ( "4".equals(type) ) {
            templateName = "additional.xlsx";
            fileName = "附加控除导入模板";
        }
        if (templateName != null ) {
            ExcelOutPutUtil.OutPut(fileName,templateName,data,response);
        }
    }
}
