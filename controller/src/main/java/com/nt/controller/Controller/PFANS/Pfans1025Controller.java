package com.nt.controller.Controller.PFANS;

import com.nt.dao_Org.Dictionary;
import com.nt.dao_Pfans.PFANS1000.Award;
import com.nt.dao_Pfans.PFANS1000.AwardDetail;
import com.nt.dao_Pfans.PFANS1000.Contractnumbercount;
import com.nt.dao_Pfans.PFANS1000.Vo.AwardVo;
import com.nt.service_Org.DictionaryService;
import com.nt.service_pfans.PFANS1000.AwardService;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
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
    public void generateJxls(@RequestBody AwardVo av, HttpServletRequest request,HttpServletResponse response) throws Exception {
        TokenModel tokenModel=tokenService.getToken(request);
        AwardVo  nu = awardService.selectById(av.getAward().getAward_id());
        String aa[] = av.getAward().getClaimdatetime().split(" ~ ");
       List<Map<String,String>> grouplist = (List<Map<String,String>>)av.getGroupN();
       List<AwardDetail> adlist = av.getAwardDetail();
        for (Map<String,String> user : grouplist) {
            String groupid = user.get("groupid");
            String groupname = user.get("groupname");
            for(AwardDetail grn :adlist){
                if(groupid.equals(grn.getDepart())){
                    grn.setDepart(groupname);
                }
            }
        }
        List<Dictionary> dictionaryList = dictionaryService.getForSelect("HT006");
        for(Dictionary item:dictionaryList){
            if(item.getCode().equals(av.getAward().getCurrencyposition())) {

                av.getAward().setCurrencyposition(item.getValue1());
            }
        }
        List<Dictionary> planList = dictionaryService.getForSelect("HT018");
        for(Dictionary item:planList){
            if(item.getCode().equals(av.getAward().getPlan())) {

                av.getAward().setPlan(item.getValue1());
            }
        }
        List<Dictionary> valuationList = dictionaryService.getForSelect("HT005");
        for(Dictionary item:valuationList){
            if(item.getCode().equals(av.getAward().getValuation()) || item.getCode().equals(av.getAward().getIndividual())) {

                av.getAward().setValuation(item.getValue1());
                av.getAward().setIndividual(item.getValue1());
            }
        }
        Map<String, Object> data = new HashMap<>();
        data.put("aw",av.getAward());
        data.put("alist",av.getAwardDetail());
        data.put("num",nu.getNumbercounts());
        data.put("sta",av.getStaffDetail());
        if(aa.length > 0){
            data.put("statime",aa);
        } else {
            data.put("statime","");
        }
        if(av.getAward().getMaketype().equals("4")){
            ExcelOutPutUtil.OutPut(av.getAward().getContractnumber().toUpperCase()+"_"+av.getAward().getConjapanese()+"_決裁書(受託)","juecaishu_shoutuo.xlsx",data,response);
        } else {
            ExcelOutPutUtil.OutPut(av.getAward().getContractnumber().toUpperCase()+"_"+av.getAward().getConjapanese()+"_決裁書(委託)","juecaishu_weituo.xlsx",data,response);
        }
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
