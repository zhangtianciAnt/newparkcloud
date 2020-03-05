package com.nt.controller.Controller.PFANS;

import com.nt.dao_Org.Dictionary;
import com.nt.dao_Pfans.PFANS1000.Award;
import com.nt.dao_Pfans.PFANS1000.AwardDetail;
import com.nt.dao_Pfans.PFANS1000.Vo.AwardVo;
import com.nt.service_Org.DictionaryService;
import com.nt.service_pfans.PFANS1000.AwardService;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/award")
public class Pfans1025Controller {
    @Autowired
    private AwardService awardService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private DictionaryService dictionaryService;

    @RequestMapping(value = "/generateJxls", method = {RequestMethod.POST})
    public void generateJxls(@RequestBody AwardVo awardVo, HttpServletRequest request,HttpServletResponse response) throws Exception {
        TokenModel tokenModel=tokenService.getToken(request);
        Award aw = awardVo.getAward();
        List<AwardDetail> awalist= awardVo.getAwardDetail();
        List<Dictionary> dictionaryList = dictionaryService.getForSelect("HT006");
        for(Dictionary item:dictionaryList){
            if(item.getCode().equals(aw.getCurrencyposition())) {

                aw.setCurrencyposition(item.getValue1());
            }
        }
        Map<String, Object> data = new HashMap<>();
        data.put("aw",aw);
        data.put("awalist",awalist);
        ExcelOutPutUtil.OutPut(awardVo.getAward().getContractnumber().toUpperCase()+"_決裁書(委託)","juecaishu_weituo.xlsx",data,response);
    }

    @RequestMapping(value = "/get",method = {RequestMethod.GET})
    public ApiResult get(Award award,HttpServletRequest request) throws Exception{
        TokenModel tokenModel=tokenService.getToken(request);
        award.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(awardService.get(award));
    }

    @RequestMapping(value = "/selectById",method = {RequestMethod.GET})
    public ApiResult selectById(String award_id,HttpServletRequest request) throws Exception{
        if(award_id==null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(awardService.selectById(award_id));
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody AwardVo awardVo,HttpServletRequest request)throws Exception{
        if(awardVo==null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel=tokenService.getToken(request);
        awardService.updateAwardVo(awardVo,tokenModel);
        return ApiResult.success();
    }


}
