package com.nt.controller.Controller.AOCHUAN;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.policy.HackLoopTableRenderPolicy;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Quotations;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Enquiry;
import com.nt.dao_AOCHUAN.AOCHUAN3000.TransportGood;
import com.nt.service_AOCHUAN.AOCHUAN3000.QuotationsService;
import com.nt.service_AOCHUAN.AOCHUAN3000.mapper.QuotationsMapper;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/quotations")
public class AOCHUAN3001Controller {

    @Autowired
    private QuotationsMapper quotationsMapper;
    @Autowired
    private QuotationsService quotationsService;
    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/get",method={RequestMethod.GET})
    public ApiResult get(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        Quotations quotations = new Quotations();
        quotations.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(quotationsService.get(quotations));
    }

    @RequestMapping(value = "/getone",method={RequestMethod.GET})
    public ApiResult getOne(@RequestParam String id, HttpServletRequest request) throws Exception {
        if(!StringUtils.isNotBlank(id)){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(quotationsService.getOne(id));
    }

    @RequestMapping(value = "/getForSupplier",method={RequestMethod.GET})
    public ApiResult getForSupplier(@RequestParam String id, HttpServletRequest request) throws Exception {
        if(!StringUtils.isNotBlank(id)){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(quotationsService.getForSupplier(id));
    }

    @RequestMapping(value = "/getForCustomer",method={RequestMethod.GET})
    public ApiResult getForCustomer(@RequestParam String id, HttpServletRequest request) throws Exception {
        if(!StringUtils.isNotBlank(id)){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(quotationsService.getForCustomer(id));
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
    public void pdf(@RequestBody Quotations quotations, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SimpleDateFormat sf =new SimpleDateFormat("yyyy/MM/dd");
        List<Enquiry> enquiry = quotations.getEnquiry();
        String inquirydate = sf.format(quotations.getInquirydate());
        String quotationsno = quotations.getQuotationsno();
        String others =quotations.getOthers();
        String account = quotations.getAccount();
        BigDecimal amounts = new BigDecimal(0);
        int i = 0;
        for (Enquiry en: enquiry) {
            i += 1;
            en.setIndex(i);
            en.setProducten(quotations.getProducten());
            // amounts = new BigDecimal(en.getCounts() == null ? "0" : getNumber(en.getCounts())).multiply(new BigDecimal(en.getQuotedprice() == null ? "0" : en.getQuotedprice())).add(amounts);
        }
       // String amount = amounts.toString();
        HackLoopTableRenderPolicy policy = new HackLoopTableRenderPolicy();
        Configure config = Configure.newBuilder()
                .bind("enquiry", policy).build();
       //File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "Quotation.docx");
        ClassPathResource resource = new ClassPathResource("Quotation.docx");
       // InputStream inputStream = this.getClass().getResourceAsStream("Quotation.docx");
        XWPFTemplate template = XWPFTemplate.compile(resource.getInputStream(), config).render(
                new HashMap<String, Object>() {{
                    put("enquiry", enquiry);
                    put("inquirydate",inquirydate);
                    put("quotationsno",quotationsno);
                    put("account",account);
                   // put("amount",amount);
                    put("others",others);
                }}
        );
        ServletOutputStream out = response.getOutputStream();
      template.writeToFile("/home/Quotation1.docx");
       // template.writeToFile("E:\\Quotation1.docx");
        Document document = new Document();
     document.loadFromFile("/home/Quotation1.docx");
      // document.loadFromFile("E:\\Quotation1.docx");
        document.saveToStream(out, FileFormat.PDF);
        template.close();

        if(quotations.isNotice() == true){
            Quotations quotation = new Quotations();
            quotation.setQuotations_id(quotations.getQuotations_id());
            quotation.setQuotationdate1(quotations.getQuotationdate1());
            quotationsMapper.updateByPrimaryKeySelective(quotation);
        }
    }

//    导出
    @RequestMapping(value = "/getexport", method = {RequestMethod.POST})
    public void getexport(@RequestBody List<Quotations> quotationsList, HttpServletResponse response) throws Exception {
        quotationsService.setExport(response , quotationsList);
//        return ApiResult.success();
    }

    private String getNumber(String num){
        Pattern pattern = Pattern.compile("[^0-9]");
        Matcher matcher = pattern.matcher(num);
        String all = matcher.replaceAll("");
        return  all;
    }
}