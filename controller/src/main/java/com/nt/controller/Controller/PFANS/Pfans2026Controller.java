package com.nt.controller.Controller.PFANS;

import cn.hutool.core.io.FileUtil;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Pfans.PFANS2000.Staffexitprocedure;
import com.nt.dao_Pfans.PFANS2000.Vo.StaffexitprocedureVo;
import com.nt.dao_Workflow.Vo.StartWorkflowVo;
import com.nt.dao_Workflow.Vo.WorkflowLogDetailVo;
import com.nt.service_WorkFlow.WorkflowServices;
import com.nt.service_pfans.PFANS2000.StaffexitprocedureService;
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
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/staffexitprocedure")
public class Pfans2026Controller {

    @Autowired
    private StaffexitprocedureService staffexitprocedureService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private WorkflowServices workflowServices;
    @Autowired
    private MongoTemplate mongoTemplate;
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
    public ApiResult selectById(String staffexitprocedureid, HttpServletRequest request) throws Exception {
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

    /**
     * 打印
     */
    @RequestMapping(value = "/downLoad", method = {RequestMethod.GET})
    public void downLoad1(String staffexitprocedureid, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        String wfList1 = "";
        String wfList2 = "";
        String wfList3 = "";
        String wfList4 = "";
        String wfList5 = "";
        String wfList6 = "";
        StaffexitprocedureVo staVo = staffexitprocedureService.selectById(staffexitprocedureid);
        StartWorkflowVo startWorkflowVo = new StartWorkflowVo();
        startWorkflowVo.setDataId(staVo.getStaffexitprocedure().getStaffexitprocedure_id());
        List<WorkflowLogDetailVo> wfList = workflowServices.ViewWorkflow2(startWorkflowVo, tokenModel.getLocale());
        SimpleDateFormat str = new SimpleDateFormat("yyyy年MM月dd日");
        Query query = new Query();
        CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
        if (wfList.size() > 0) {
            //总经理
            query = new Query();
            query.addCriteria(Criteria.where("userid").is(wfList.get(0).getUserId()));
            customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
            if (customerInfo != null) {
                wfList1 = customerInfo.getUserinfo().getCustomername();
                wfList1 = sign.startGraphics2D(wfList1);
            }
            //冷美琴
            query = new Query();
            query.addCriteria(Criteria.where("userid").is(wfList.get(1).getUserId()));
            customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
            if (customerInfo != null) {
                wfList2 = customerInfo.getUserinfo().getCustomername();
                wfList2 = sign.startGraphics2D(wfList2);
            }
            //曹金钰
            query = new Query();
            query.addCriteria(Criteria.where("userid").is(wfList.get(2).getUserId()));
            customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
            if (customerInfo != null) {
                wfList3 = customerInfo.getUserinfo().getCustomername();
                wfList3 = sign.startGraphics2D(wfList3);
            }
            //center长
            query = new Query();
            query.addCriteria(Criteria.where("userid").is(wfList.get(3).getUserId()));
            customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
            if (customerInfo != null) {
                wfList4 = customerInfo.getUserinfo().getCustomername();
                wfList4 = sign.startGraphics2D(wfList4);
            }
            //GM
            query = new Query();
            query.addCriteria(Criteria.where("userid").is(wfList.get(4).getUserId()));
            customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
            if (customerInfo != null) {
                wfList5 = customerInfo.getUserinfo().getCustomername();
                wfList5 = sign.startGraphics2D(wfList5);
            }
            //TL
            query = new Query();
            query.addCriteria(Criteria.where("userid").is(wfList.get(5).getUserId()));
            customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
            if (customerInfo != null) {
                wfList6 = customerInfo.getUserinfo().getCustomername();
            }
        }
        query = new Query();
        query.addCriteria(Criteria.where("userinfo.centerid").is(staVo.getStaffexitprocedure().getCenter_id()));
        customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
        if (customerInfo != null) {
            staVo.getStaffexitprocedure().setCenter_id(customerInfo.getUserinfo().getCentername());
        }
        query = new Query();
        query.addCriteria(Criteria.where("userinfo.groupid").is(staVo.getStaffexitprocedure().getGroup_id()));
        customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
        if (customerInfo != null) {
            staVo.getStaffexitprocedure().setCenter_id(customerInfo.getUserinfo().getGroupname());
        }
        query = new Query();
        query.addCriteria(Criteria.where("userinfo.teamid").is(staVo.getStaffexitprocedure().getTeam_id()));
        customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
        if (customerInfo != null) {
            staVo.getStaffexitprocedure().setCenter_id(customerInfo.getUserinfo().getTeamname());
        }
        //endregion
        query = new Query();
        query.addCriteria(Criteria.where("userinfo.centerid").is(staVo.getStaffexitprocedure().getCenter_id()));
        customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
        if (customerInfo != null) {
            staVo.getStaffexitprocedure().setCenter_id(customerInfo.getUserinfo().getCentername());
        }
        query = new Query();
        query.addCriteria(Criteria.where("userinfo.groupid").is(staVo.getStaffexitprocedure().getGroup_id()));
        customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
        if (customerInfo != null) {
            staVo.getStaffexitprocedure().setGroup_id(customerInfo.getUserinfo().getGroupname());
        }
        query = new Query();
        query.addCriteria(Criteria.where("userinfo.teamid").is(staVo.getStaffexitprocedure().getTeam_id()));
        customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
        if (customerInfo != null) {
            staVo.getStaffexitprocedure().setTeam_id(customerInfo.getUserinfo().getTeamname());
        }
        query = new Query();
        query.addCriteria(Criteria.where("userid").is(staVo.getStaffexitprocedure().getUser_id()));
        customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
        if (customerInfo != null) {
            staVo.getStaffexitprocedure().setUser_id(customerInfo.getUserinfo().getCustomername());
        }
        Map<String, Object> data = new HashMap<>();
        data.put("wfList1", wfList1);
        data.put("wfList2", wfList2);
        data.put("wfList3", wfList3);
        data.put("wfList4", wfList4);
        data.put("wfList5", wfList5);
        data.put("wfList6", wfList6);
        data.put("wfList7", str.format(wfList.get(6).getEdata()));
        data.put("leavedate", str.format(staVo.getStaffexitprocedure().getHope_exit_date()));
        data.put("staff", staVo.getStaffexitprocedure());
        ExcelOutPutUtil.OutPutPdf("劳动者离职报告", "laodongzhelizhibaogao.xls", data, response);
        ExcelOutPutUtil.deleteDir("E:\\PFANS\\image");
//        FileUtil.del("E:\\PFANS\\image" + "/" + wfList1);
//        FileUtil.del("E:\\PFANS\\image" + "/" + wfList2);
//        FileUtil.del("E:\\PFANS\\image" + "/" + wfList3);
//        FileUtil.del("E:\\PFANS\\image" + "/" + wfList4);
//        FileUtil.del("E:\\PFANS\\image" + "/" + wfList5);
//        FileUtil.del("E:\\PFANS\\image" + "/" + wfList6);
    }
}
