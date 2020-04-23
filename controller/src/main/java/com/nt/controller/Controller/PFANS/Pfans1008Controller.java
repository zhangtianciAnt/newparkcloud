package com.nt.controller.Controller.PFANS;

import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_Pfans.PFANS1000.Softwaretransfer;
import com.nt.dao_Pfans.PFANS1000.Vo.SoftwaretransferVo;
import com.nt.service_Org.DictionaryService;
import com.nt.service_pfans.PFANS1000.SoftwaretransferService;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/softwaretransfer")
public class Pfans1008Controller {

    @Autowired
    private SoftwaretransferService softwaretransferService;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    public ApiResult get(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        Softwaretransfer softwaretransfer = new Softwaretransfer();
        softwaretransfer.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(softwaretransferService.getSoftwaretransfer(softwaretransfer));
    }

    @RequestMapping(value = "insert", method = {RequestMethod.POST})
    public ApiResult insert(@RequestBody SoftwaretransferVo softwaretransferVo, HttpServletRequest request) throws Exception {
        if (softwaretransferVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        softwaretransferService.insert(softwaretransferVo, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/selectById", method = {RequestMethod.GET})
    public ApiResult selectById(String softwaretransferid, HttpServletRequest request) throws Exception {
        if (softwaretransferid == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(softwaretransferService.selectById(softwaretransferid));
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody SoftwaretransferVo softwaretransferVo, HttpServletRequest request) throws Exception {
        if (softwaretransferVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        softwaretransferService.updateSoftwaretransfer(softwaretransferVo, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/downLoad1", method = {RequestMethod.GET})
    public void downLoad1(String softwaretransferId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
//        for (int i = 0; i < softwaretransferList.size(); i++) {
        SoftwaretransferVo soft = softwaretransferService.selectById(softwaretransferId);

        Query query = new Query();
        query.addCriteria(Criteria.where("userid").is(soft.getSoftwaretransfer().getUser_id()));
        CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
        if (customerInfo != null) {
            soft.getSoftwaretransfer().setUser_id(customerInfo.getUserinfo().getCustomername());
        }
        List<Dictionary> curList = dictionaryService.getForSelect("JY002");
        for (Dictionary item : curList) {
            if (item.getCode().equals(soft.getSoftwaretransfer().getFerrybudgetunit())) {
                soft.getSoftwaretransfer().setFerrybudgetunit(item.getValue1());
            }
        }
        List<Dictionary> curList1 = dictionaryService.getForSelect("JY002");
        for (Dictionary item : curList1) {
            if (item.getCode().equals(soft.getSoftwaretransfer().getTubebudgetunit())) {
                soft.getSoftwaretransfer().setTubebudgetunit(item.getValue1());
            }
        }


        //region 查询TL
        String StrTl = "";
        query = new Query();
        String strFerryteam_id = soft.getSoftwaretransfer().getFerryteam_id();
        query.addCriteria(Criteria.where("userinfo.teamid").is(strFerryteam_id).and("userinfo.post").is("PG021005"));
        customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
        if (customerInfo != null) {
            StrTl = customerInfo.getUserinfo().getCustomername();
        }
        //endregion

        //region 查询GM
        String StrGM = "";
        query = new Query();
        String strFerrygroup_id = soft.getSoftwaretransfer().getFerrygroup_id();
        query.addCriteria(Criteria.where("userinfo.groupid").is(strFerrygroup_id).and("userinfo.post").is("PG021002"));
        customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
        if (customerInfo != null) {
            StrGM = customerInfo.getUserinfo().getCustomername();
        }
        //endregion


        //region 查询TL
        String CtrTL = "";
        query = new Query();
        String strTubeteam_id = soft.getSoftwaretransfer().getTubeteam_id();
        query.addCriteria(Criteria.where("userinfo.teamid").is(strTubeteam_id).and("userinfo.post").is("PG021005"));
        customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
        if (customerInfo != null) {
            CtrTL = customerInfo.getUserinfo().getCustomername();
        }
        //endregion

        //region 查询GM
        String CtrGM = "";
        query = new Query();
        String strTubegroup_id = soft.getSoftwaretransfer().getTubegroup_id();
        query.addCriteria(Criteria.where("userinfo.groupid").is(strTubegroup_id).and("userinfo.post").is("PG021002"));
        customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
        if (customerInfo != null) {
            CtrGM = customerInfo.getUserinfo().getCustomername();
        }
        //endregion
            Map<String, Object> data = new HashMap<>();
            data.put("soft", soft);
        data.put("StrTl", StrTl);
        data.put("StrGM", StrGM);
        data.put("CtrTL", CtrTL);
        data.put("CtrGM", CtrGM);
            ExcelOutPutUtil.OutPutPdf("固定资产·软件移转申请", "gudingzichanfq.xls", data, response);
        }
//    }
}
