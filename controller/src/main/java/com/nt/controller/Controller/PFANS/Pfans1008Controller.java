package com.nt.controller.Controller.PFANS;

import cn.hutool.core.io.FileUtil;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_Pfans.PFANS1000.Notification;
import com.nt.dao_Pfans.PFANS1000.Softwaretransfer;
import com.nt.dao_Pfans.PFANS1000.Vo.SoftwaretransferVo;
import com.nt.dao_Workflow.Vo.StartWorkflowVo;
import com.nt.dao_Workflow.Vo.WorkflowLogDetailVo;
import com.nt.service_Org.DictionaryService;
import com.nt.service_WorkFlow.WorkflowServices;
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
    private WorkflowServices workflowServices;
    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private TokenService tokenService;

//    @RequestMapping(value = "/get", method = {RequestMethod.GET})
//    public ApiResult get(HttpServletRequest request) throws Exception {
//        TokenModel tokenModel = tokenService.getToken(request);
//        Softwaretransfer softwaretransfer = new Softwaretransfer();
//        softwaretransfer.setOwners(tokenModel.getOwnerList());
//        return ApiResult.success(softwaretransferService.getSoftwaretransfer(softwaretransfer));
//    }

    @RequestMapping(value = "/getSoftwaretransfer", method = {RequestMethod.GET})
    public ApiResult getSoftwaretransfer(HttpServletRequest request) throws Exception {
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
        String wfList1 = "";
        SoftwaretransferVo soft = softwaretransferService.selectById(softwaretransferId);
        StartWorkflowVo startWorkflowVo = new StartWorkflowVo();
        startWorkflowVo.setDataId(soft.getSoftwaretransfer().getSoftwaretransferid());
        List<WorkflowLogDetailVo> wfList = workflowServices.ViewWorkflow2(startWorkflowVo, tokenModel.getLocale());
        List<Notification> noto = soft.getNotification();
        Query query = new Query();
        query.addCriteria(Criteria.where("userid").is(soft.getSoftwaretransfer().getUser_id()));
        CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
        if (customerInfo != null) {
            soft.getSoftwaretransfer().setUser_id(customerInfo.getUserinfo().getCustomername());
        }
        for (int i = 0; i < noto.size(); i++) {
            query = new Query();
            query.addCriteria(Criteria.where("userid").is(noto.get(i).getPerson()));
            customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
            if (customerInfo != null) {
                noto.get(i).setPerson(customerInfo.getUserinfo().getCustomername());
            }
            query = new Query();
            query.addCriteria(Criteria.where("userid").is(noto.get(i).getEafter()));
            customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
            if (customerInfo != null) {
                noto.get(i).setEafter(customerInfo.getUserinfo().getCustomername());
            }
        }

        if (wfList.size() > 0) {
            query = new Query();
            query.addCriteria(Criteria.where("userid").is(wfList.get(0).getUserId()));
            customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
            if (customerInfo != null) {
                wfList1 = customerInfo.getUserinfo().getCustomername();
                wfList1 = sign.startGraphics2D(wfList1);
            }
        }
        //字典
        List<Dictionary> curList = dictionaryService.getForSelect("JY002");
        for (Dictionary item : curList) {
            if (item.getCode().equals(soft.getSoftwaretransfer().getFerrybudgetunit())) {
                soft.getSoftwaretransfer().setFerrybudgetunit(item.getValue3());
            }
        }
        List<Dictionary> curList1 = dictionaryService.getForSelect("JY002");
        for (Dictionary item : curList1) {
            if (item.getCode().equals(soft.getSoftwaretransfer().getTubebudgetunit())) {
                soft.getSoftwaretransfer().setTubebudgetunit(item.getValue3());
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
        query = new Query();
        query.addCriteria(Criteria.where("userinfo.groupid").is(soft.getSoftwaretransfer().getFerrygroup_id()));
        customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
        if (customerInfo != null) {
            soft.getSoftwaretransfer().setFerrygroup_id(customerInfo.getUserinfo().getGroupname());
        }
        query = new Query();
        query.addCriteria(Criteria.where("userinfo.groupid").is(soft.getSoftwaretransfer().getTubegroup_id()));
        customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
        if (customerInfo != null) {
            soft.getSoftwaretransfer().setTubegroup_id(customerInfo.getUserinfo().getGroupname());
        }
        Map<String, Object> data = new HashMap<>();
        data.put("wfList1", wfList1);
        data.put("wfList", wfList);
        data.put("soft", soft.getSoftwaretransfer());
        data.put("notn", soft.getNotification());
        data.put("StrTl", StrTl);
        data.put("StrGM", StrGM);
        data.put("CtrTL", CtrTL);
        data.put("CtrGM", CtrGM);
        ExcelOutPutUtil.OutPutPdf("固定资产·软件移转申请", "gudingzichanzy.xls", data, response);
//        FileUtil.del("E:\\PFANS\\image" + "/" + wfList1);
        ExcelOutPutUtil.deleteDir("E:\\PFANS\\image");
        }
//    }
}
