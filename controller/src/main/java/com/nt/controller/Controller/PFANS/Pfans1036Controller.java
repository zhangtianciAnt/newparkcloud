package com.nt.controller.Controller.PFANS;

import com.nt.dao_Org.Dictionary;
import com.nt.dao_Pfans.PFANS1000.Businessplan;
import com.nt.dao_Pfans.PFANS1000.Vo.BusinessplanVo;
import com.nt.dao_Pfans.PFANS1000.Vo.ReportBusinessVo;
import com.nt.service_Org.DictionaryService;
import com.nt.service_pfans.PFANS1000.BusinessplanService;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/businessplan")
public class Pfans1036Controller {
    @Autowired
    private BusinessplanService businessplanService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private DictionaryService dictionaryService;

    @PostMapping("/importUser")
    public ApiResult importUser(HttpServletRequest request, String radio) {
        try {
            TokenModel tokenModel = tokenService.getToken(request);
            return ApiResult.success(businessplanService.importUser(request, tokenModel,radio));
        } catch (LogicalException e) {
            return ApiResult.fail(e.getMessage());
        } catch (Exception e) {
            return ApiResult.fail("操作失败！");
        }
    }

    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    public ApiResult get(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        Businessplan businessplan = new Businessplan();
        businessplan.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(businessplanService.get(businessplan));
    }

    @RequestMapping(value = "/getgroupA1", method = {RequestMethod.GET})
    public ApiResult getgroupA1(String year,String groupid,HttpServletRequest request) throws Exception {
        return ApiResult.success(businessplanService.getgroupA1(year,groupid));
    }

    @RequestMapping(value = "/getgroupcompanyen", method = {RequestMethod.GET})
    public ApiResult getgroupcompanyen(String year,String groupid,HttpServletRequest request) throws Exception {
        return ApiResult.success(businessplanService.getgroupcompanyen(year,groupid));
    }

    @RequestMapping(value = "/getgroup", method = {RequestMethod.GET})
    public ApiResult getgroup(String year,String groupid,String type,HttpServletRequest request) throws Exception {
        return ApiResult.success(businessplanService.getgroup(year,groupid,type));
    }

    @RequestMapping(value = "/selectById", method = {RequestMethod.GET})
    public ApiResult selectById(String businessplanid, HttpServletRequest request) throws Exception {
        if (businessplanid == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(businessplanService.selectById(businessplanid));
    }

    @RequestMapping(value = "/create", method = {RequestMethod.POST})
    public ApiResult insertBusinessplanVo(@RequestBody Businessplan businessplan, HttpServletRequest request) throws Exception {
        if (businessplan == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        businessplanService.insertBusinessplan(businessplan, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult updateBusinessplanVo(@RequestBody Businessplan businessplan, HttpServletRequest request) throws Exception {
        if (businessplan == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        businessplanService.updateBusinessplanVo(businessplan, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/getpersonplan", method = {RequestMethod.GET})
    public ApiResult getPersonPlan(@RequestParam String year,@RequestParam String groupid, HttpServletRequest request) throws Exception {
        if (groupid == "") {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(businessplanService.getPersonPlan(year, groupid));
    }

    @RequestMapping(value = "/whetherEditor", method = {RequestMethod.GET})
    public ApiResult whetherEditor(@RequestParam String years,@RequestParam String centerid, HttpServletRequest request) throws Exception {
        if (centerid == "") {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(businessplanService.whetherEditor(years, centerid));
    }

    /**
     * scc 事业计划，模板下载
     * */
    @RequestMapping(value = "/download", method = {RequestMethod.GET})
    public void download(String type, HttpServletResponse response) throws Exception {
        Map<String, Object> data = new HashMap<>();
        String templateName = null;
        String fileName = null;
        if ("0".equals(type) ) {
            templateName = "shiyejihuayiqiannianduzichan.xlsx";
            fileName = "事业计划以前年度资产导入模板";
        }
        if (templateName != null ) {
            ExcelOutPutUtil.OutPut(fileName,templateName,data,response);
        }
    }

    /**
     * scc 事业计划PL导出
     * */
    @RequestMapping(value = "/export", method = {RequestMethod.POST})
    public void export(@RequestBody List<ReportBusinessVo> businessVos, HttpServletRequest request,HttpServletResponse response) throws Exception {
        if(businessVos != null && businessVos.size() != 0){
            for (ReportBusinessVo item : businessVos) {
                if (item.getName1() != null) {
                    Dictionary dic = new Dictionary();
                    dic.setCode(item.getName1());
                    List<Dictionary> forSelect = dictionaryService.getDictionaryList(dic);
                    if(forSelect != null && forSelect.size() > 0){
                        item.setName1(forSelect.get(0).getValue1());
                    }
                }
            }
            businessplanService.export(businessVos,request,response);
        }
    }

    /**
     * scc Pl相关保存
     * */
    @RequestMapping(value = "/Pl", method = {RequestMethod.POST})
    public ApiResult Pl(@RequestBody List<ReportBusinessVo> businessVos, HttpServletRequest request) throws Exception {
        if(businessVos == null && businessVos.size() == 0){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03,RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        businessplanService.PlRelated(businessVos,tokenModel);
        return ApiResult.success();
    }
}
