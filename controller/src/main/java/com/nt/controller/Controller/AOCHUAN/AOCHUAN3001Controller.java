package com.nt.controller.Controller.AOCHUAN;

import cn.hutool.core.util.StrUtil;
import com.aspose.cells.SaveFormat;
import com.aspose.cells.Workbook;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Quotations;
import com.nt.service_AOCHUAN.AOCHUAN3000.QuotationsService;
import com.nt.utils.*;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/quotations")
public class AOCHUAN3001Controller {

    @Autowired
    private QuotationsService quotationsService;
    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/get",method={RequestMethod.GET})
    public ApiResult get(HttpServletRequest request) throws Exception {
        return ApiResult.success(quotationsService.get());
    }

    @RequestMapping(value = "/getone",method={RequestMethod.GET})
    public ApiResult getOne(@RequestParam String id, HttpServletRequest request) throws Exception {
        if(!StringUtils.isNotBlank(id)){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(quotationsService.getOne(id));
    }

    @RequestMapping(value = "/update",method={RequestMethod.POST})
    public ApiResult update(@RequestBody Quotations quotations, HttpServletRequest request) throws Exception {
        if(quotations == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        quotationsService.update(quotations,tokenService.getToken(request));
        return ApiResult.success();
    }

    @RequestMapping(value = "/insert",method={RequestMethod.POST})
    public ApiResult insert(@RequestBody Quotations quotations, HttpServletRequest request) throws Exception {
        if(quotations == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        quotationsService.insert(quotations,tokenService.getToken(request));
        return ApiResult.success();
    }

    @RequestMapping(value = "/delete",method={RequestMethod.GET})
    public ApiResult delete(@RequestParam String id, HttpServletRequest request) throws Exception {
        if(!StringUtils.isNotBlank(id)){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        quotationsService.delete(id);
        return ApiResult.success();
    }

    @RequestMapping(value = "/pdf",method={RequestMethod.POST})
    public void pdf(HttpServletRequest request, HttpServletResponse response) throws Exception {
            //Map<String, Object> data = new HashMap<>();
//         Quotations quotation = new Quotations();
//          quotation.setAccount("哈哈哈");
//        Quotations quotation1 = new Quotations();
//        quotation1.setAccount("哈哈哈2");
//         List<Quotations> quotations = new ArrayList<>();
//         quotations.add(quotation);
//        quotations.add(quotation1);
//         data.put("quotations",quotations);
 //        ExcelOutPutUtil.OutPut1("aochuan","aochuan.xlsx",data,response);
       // Excel2pdf.excel2pdf("C:\\Users\\Administrator\\Desktop\\jiaban.xlsx","C:\\Users\\Administrator\\Desktop\\jiaban.pdf");
        //File pdfFile = new File("C:\\Users\\Administrator\\Desktop\\jiaban.pdf");// 输出路径
        Workbook wb = new Workbook("C:\\Users\\Administrator\\Desktop\\jiaban.xlsx");// 原始excel路径
        //FileOutputStream fileOS = new FileOutputStream(pdfFile);
        wb.save(response.getOutputStream(), SaveFormat.PDF);
        //fileOS.close();
    }
}
