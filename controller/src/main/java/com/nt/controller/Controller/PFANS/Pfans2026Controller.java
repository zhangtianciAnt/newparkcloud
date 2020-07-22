package com.nt.controller.Controller.PFANS;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.OrgTree;
import com.nt.dao_Org.ToDoNotice;
import com.nt.dao_Org.UserAccount;
import com.nt.dao_Org.Vo.UserVo;
import com.nt.dao_Pfans.PFANS2000.Staffexitprocedure;
import com.nt.dao_Pfans.PFANS2000.Staffexitproce;
import com.nt.dao_Pfans.PFANS2000.Vo.StaffexitprocedureVo;
import com.nt.dao_Workflow.Vo.StartWorkflowVo;
import com.nt.dao_Workflow.Vo.WorkflowLogDetailVo;
import com.nt.dao_Workflow.Workflowinstance;
import com.nt.service_Org.OrgTreeService;
import com.nt.service_Org.UserService;
import com.nt.service_WorkFlow.WorkflowServices;
import com.nt.service_WorkFlow.mapper.WorkflowinstanceMapper;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/staffexitprocedure")
public class Pfans2026Controller {

    @Autowired
    private WorkflowinstanceMapper workflowinstanceMapper;

    @Autowired
    private OrgTreeService orgTreeService;

    @Autowired
    private UserService userService;

    @Autowired
    private StaffexitprocedureService staffexitprocedureService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private WorkflowServices workflowServices;
    @Autowired
    private MongoTemplate mongoTemplate;


    @RequestMapping(value = "/generatesta", method = {RequestMethod.POST})
    public void generateJxls(@RequestBody StaffexitprocedureVo staffexitprocedureVo, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        StaffexitprocedureVo StaList = staffexitprocedureService.selectById(staffexitprocedureVo.getStaffexitprocedure().getStaffexitprocedure_id());
        Map<String, Object> data = new HashMap<>();
        Query query = new Query();
        String userid = StaList.getStaffexitprocedure().getUser_id();
        query.addCriteria(Criteria.where("userid").is(StaList.getStaffexitprocedure().getUser_id()));
        CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
        if (customerInfo != null) {
            StaList.getStaffexitprocedure().setUser_id(customerInfo.getUserinfo().getCustomername());
            StaList.getStaffexitprocedure().setCenter_id(customerInfo.getUserinfo().getCentername());
            StaList.getStaffexitprocedure().setGroup_id(customerInfo.getUserinfo().getGroupname());
            StaList.getStaffexitprocedure().setTeam_id(customerInfo.getUserinfo().getTeamname());
        }

        String wfList1 = "";
        String wfList2 = "";
        String wfList3 = "";
        String wfList4 = "";
        String ro = "";
        int flgroles = 0;//区分flg = 0 ？正式社员 ：（领导）
        Query query8 = new Query();
        query8.addCriteria(Criteria.where("_id").is(userid));
        UserAccount userAccount = mongoTemplate.findOne(query8, UserAccount.class);
        if (userAccount != null) {
            if (userAccount.getRoles().size() > 0) {
                for (int i = 0; i < userAccount.getRoles().size(); i++) {
                    ro = ro + userAccount.getRoles().get(i).getDescription();
                }
                if (ro.indexOf("总经理") != -1) {
                    flgroles = 1;
                } else if (ro.toUpperCase().indexOf("CENTER") != -1) {
                    flgroles = 2;
                } else if (ro.toUpperCase().indexOf("GM") != -1) {
                    flgroles = 3;
                } else if (ro.toUpperCase().indexOf("TL") != -1) {
                    flgroles = 4;
                }
            }
        }
        StartWorkflowVo startWorkflowVo = new StartWorkflowVo();
        startWorkflowVo.setDataId(StaList.getStaffexitprocedure().getStaffexitprocedure_id());
        List<WorkflowLogDetailVo> wfList = workflowServices.ViewWorkflow2(startWorkflowVo, tokenModel.getLocale());
        if (wfList.size() > 0) {
            Query query2 = new Query();
            query2.addCriteria(Criteria.where("userid").is(wfList.get(0).getUserId()));
            CustomerInfo customerInfo2 = mongoTemplate.findOne(query2, CustomerInfo.class);
            if (customerInfo2 != null) {
                wfList1 = customerInfo2.getUserinfo().getCustomername();
                wfList1 = sign.startGraphics2D(wfList1);
            }
            if (flgroles == 0) {
                Query query3 = new Query();
                query3.addCriteria(Criteria.where("userid").is(wfList.get(2).getUserId()));
                CustomerInfo customerInfo3 = mongoTemplate.findOne(query3, CustomerInfo.class);
                if (customerInfo3 != null) {
                    wfList2 = customerInfo3.getUserinfo().getCustomername();
                    wfList2 = sign.startGraphics2D(wfList2);
                }
                Query query4 = new Query();
                query4.addCriteria(Criteria.where("userid").is(wfList.get(1).getUserId()));
                CustomerInfo customerInfo4 = mongoTemplate.findOne(query4, CustomerInfo.class);
                if (customerInfo4 != null) {
                    wfList3 = customerInfo4.getUserinfo().getCustomername();
                    wfList3 = sign.startGraphics2D(wfList3);
                }
            } else if (flgroles == 4) {
                Query query5 = new Query();
                query5.addCriteria(Criteria.where("userid").is(wfList.get(2).getUserId()));
                CustomerInfo customerInfo5 = mongoTemplate.findOne(query5, CustomerInfo.class);
                if (customerInfo5 != null) {
                    wfList3 = customerInfo5.getUserinfo().getCustomername();
                    wfList3 = sign.startGraphics2D(wfList3);
                }
                Query query6 = new Query();
                query6.addCriteria(Criteria.where("userid").is(wfList.get(1).getUserId()));
                CustomerInfo customerInfo6 = mongoTemplate.findOne(query6, CustomerInfo.class);
                if (customerInfo6 != null) {
                    wfList4 = customerInfo.getUserinfo().getCustomername();
                    wfList4 = sign.startGraphics2D(wfList4);
                }
            } else if (flgroles == 3) {
                Query query7 = new Query();
                query7.addCriteria(Criteria.where("userid").is(wfList.get(2).getUserId()));
                CustomerInfo customerInfo7 = mongoTemplate.findOne(query7, CustomerInfo.class);
                if (customerInfo7 != null) {
                    wfList4 = customerInfo7.getUserinfo().getCustomername();
                    wfList4 = sign.startGraphics2D(wfList4);
                }
            }
        }
        data.put("sta", StaList.getStaffexitprocedure());
        data.put("wfList4", wfList4);
        data.put("wfList3", wfList3);
        data.put("wfList2", wfList2);
        data.put("wfList1", wfList1);
        ExcelOutPutUtil.OutPutPdf("劳动者离职者报告", "lizhibaogao.xls", data, response);
        ExcelOutPutUtil.deleteDir("E:\\PFANS\\image");
    }

    /*
     * 列表查看
     * */
    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    public ApiResult get(String type, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        Staffexitprocedure staffexitprocedure = new Staffexitprocedure();
        staffexitprocedure.setOwners(tokenModel.getOwnerList());
        List<Staffexitprocedure> StaList = staffexitprocedureService.get(staffexitprocedure);
        StaList = StaList.stream().filter(item -> (item.getType().equals(type))).collect(Collectors.toList());
        return ApiResult.success(StaList);
    }

    @RequestMapping(value = "/getList", method = {RequestMethod.POST})
    public ApiResult getList(@RequestBody Staffexitproce staffexitproce, HttpServletRequest request) throws Exception {
        if (staffexitproce == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(staffexitprocedureService.getList(staffexitproce, tokenModel));
    }

    @RequestMapping(value = "/getList2", method = {RequestMethod.POST})
    public ApiResult getList2(@RequestBody Staffexitprocedure staffexitprocedure, HttpServletRequest request) throws Exception {
        if (staffexitprocedure == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        List<Staffexitprocedure> StaList = staffexitprocedureService.getList2(staffexitprocedure, tokenModel);
        StaList = StaList.stream().filter(item -> (item.getType().equals("0"))).collect(Collectors.toList());
        return ApiResult.success(StaList);
    }

    //add-ws-6/16-禅道106
    @RequestMapping(value = "/deletesta", method = {RequestMethod.POST})
    public ApiResult deleteLog(@RequestBody Staffexitprocedure staffexitprocedure, HttpServletRequest request) throws Exception {
        if (staffexitprocedure == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        staffexitprocedureService.delete(staffexitprocedure);
        return ApiResult.success();
    }
//add-ws-6/16-禅道106

    /**
     * 查看
     */
    @RequestMapping(value = "/selectById", method = {RequestMethod.GET})
    public ApiResult selectById(String staffexitprocedureid, HttpServletRequest request) throws Exception {
        if (staffexitprocedureid == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(staffexitprocedureService.selectById(staffexitprocedureid));
    }

    /**
     * 修改z
     */
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody StaffexitprocedureVo staffexitprocedureVo, HttpServletRequest request) throws Exception {
        if (staffexitprocedureVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        staffexitprocedureService.update(staffexitprocedureVo, tokenModel);
        return ApiResult.success();

    }

    /**
     * 新建
     */
    @RequestMapping(value = "insert", method = {RequestMethod.POST})
    public ApiResult insert(@RequestBody StaffexitprocedureVo staffexitprocedureVo, HttpServletRequest request) throws Exception {
        if (staffexitprocedureVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        staffexitprocedureService.insert(staffexitprocedureVo, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/get2", method = {RequestMethod.GET})
    public ApiResult get2(String type, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        Staffexitproce staffexitproce = new Staffexitproce();
        staffexitproce.setOwners(tokenModel.getOwnerList());
        List<Staffexitproce> StaList = staffexitprocedureService.get2(staffexitproce);
        return ApiResult.success(StaList);
    }

    @RequestMapping(value = "/selectById2", method = {RequestMethod.GET})
    public ApiResult selectById2(String staffexitproceid, HttpServletRequest request) throws Exception {
        if (staffexitproceid == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(staffexitprocedureService.selectById2(staffexitproceid));
    }


    @RequestMapping(value = "/get3", method = {RequestMethod.GET})
    public ApiResult list(String userid, HttpServletRequest request) throws Exception {
        if (userid == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        Workflowinstance workflowinstance = new Workflowinstance();
        workflowinstance.setOwner(userid);
        List<Workflowinstance> Workflowinstancelist = workflowinstanceMapper.select(workflowinstance);
        Workflowinstancelist = Workflowinstancelist.stream().filter(item -> (item.getStatus().equals("0") || item.getStatus().equals("3"))).collect(Collectors.toList());
        return ApiResult.success(Workflowinstancelist);
    }

    @RequestMapping(value = "/update2", method = {RequestMethod.POST})
    public ApiResult update2(@RequestBody StaffexitprocedureVo staffexitprocedureVo, HttpServletRequest request) throws Exception {
        if (staffexitprocedureVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        staffexitprocedureService.update2(staffexitprocedureVo, tokenModel);
        return ApiResult.success();

    }

    /**
     * 新建
     */
    @RequestMapping(value = "insert2", method = {RequestMethod.POST})
    public ApiResult insert2(@RequestBody StaffexitprocedureVo staffexitprocedureVo, HttpServletRequest request) throws Exception {
        if (staffexitprocedureVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        staffexitprocedureService.insert2(staffexitprocedureVo, tokenModel);
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
