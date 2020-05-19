package com.nt.controller.Controller.PFANS;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_Pfans.PFANS2000.GoalManagement;
import com.nt.dao_Workflow.Vo.StartWorkflowVo;
import com.nt.dao_Workflow.Vo.WorkflowLogDetailVo;
import com.nt.service_Org.DictionaryService;
import com.nt.service_WorkFlow.WorkflowServices;
import com.nt.service_pfans.PFANS2000.GoalManagementService;
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
import java.util.*;

@RestController
@RequestMapping("/goalmanagement")
public class Pfans2023Controller {

    @Autowired
    private GoalManagementService goalmanagementService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private WorkflowServices workflowServices;

    @RequestMapping(value = "/yearsCheck", method = {RequestMethod.POST})
    public ApiResult yearsCheck(@RequestBody GoalManagement goalmanagement, HttpServletRequest request) throws Exception {
        if (goalmanagement == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(goalmanagementService.yearsCheck(goalmanagement));
    }

    @RequestMapping(value = "/one", method = {RequestMethod.POST})
    public ApiResult one(@RequestBody GoalManagement goalmanagement, HttpServletRequest request) throws Exception {
        if (goalmanagement == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        GoalManagement log = goalmanagementService.One(goalmanagement.getGoalmanagement_id());
        return ApiResult.success(log);
    }

    @RequestMapping(value="/list", method={RequestMethod.POST})
    public ApiResult List(@RequestBody GoalManagement goalManagement, HttpServletRequest request) throws Exception {
        if (goalManagement == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        goalManagement.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(goalmanagementService.list(goalManagement));
    }


    @RequestMapping(value="/updateInfo",method = {RequestMethod.POST})
    public ApiResult updateInformation(@RequestBody GoalManagement goalManagement, HttpServletRequest request) throws Exception{
        if (goalManagement == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        goalmanagementService.upd(goalManagement,tokenModel);
        return ApiResult.success();
    }


    @RequestMapping(value = "/createNewUser",method={RequestMethod.POST})
    public ApiResult create(@RequestBody GoalManagement goalmanagement, HttpServletRequest request) throws Exception {
        if (goalmanagement == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        goalmanagementService.insert(goalmanagement,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/downLoad1", method = {RequestMethod.POST})
    public void downLoad1(@RequestBody GoalManagement goalmanagement, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        GoalManagement gmt = goalmanagementService.One(goalmanagement.getGoalmanagement_id());
        Map<String, Object> data = new HashMap<>();
        data.put("gmt", gmt);
        Query query = new Query();
        query.addCriteria(Criteria.where("userid").is(gmt.getUser_id()));
        List<CustomerInfo> CustomerInfolist = mongoTemplate.find(query, CustomerInfo.class);
        if(CustomerInfolist.size() >0){
            data.put("user", CustomerInfolist.get(0).getUserinfo());
        }
        List<Dictionary> dictionaryList = dictionaryService.getForSelect("PR021");
        for (Dictionary item : dictionaryList) {
            if (item.getCode().equals(gmt.getSkill_rank())) {

                gmt.setSkill_rank(item.getValue1());
            }
        }

        StartWorkflowVo startWorkflowVo = new StartWorkflowVo();
        startWorkflowVo.setDataId(goalmanagement.getGoalmanagement_id());
        List<WorkflowLogDetailVo> wfList = workflowServices.ViewWorkflow2(startWorkflowVo, tokenModel.getLocale());

        String me ="";
        String wfList2="";

        if("3".equals(gmt.getStage()) && "4".equals(gmt.getStatus())){
            if (wfList.size() > 0) {
                query = new Query();
                query.addCriteria(Criteria.where("userid").is(wfList.get(0).getUserId()));
                CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                if (customerInfo != null) {
                    wfList2 = customerInfo.getUserinfo().getCustomername();
                    wfList2 = sign.startGraphics2D(wfList2);
                }
            }
            me = sign.startGraphics2D(CustomerInfolist.get(0).getUserinfo().getCustomername());
            data.put("me", me);
            data.put("up", wfList2);
        }

        data.put("now", DateUtil.year(new Date()));
        ExcelOutPutUtil.OutPutPdf("目标管理", "mubiaoguanli.xlsx", data, response);

        if("3".equals(gmt.getStage()) && "4".equals(gmt.getStatus())){
            FileUtil.del(AuthConstants.FILE_DIRECTORY + me);
            FileUtil.del(AuthConstants.FILE_DIRECTORY + wfList2);
        }

    }
}
