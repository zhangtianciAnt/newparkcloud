package com.nt.controller.Controller.PFANS;

import com.nt.dao_Assets.Inventoryplan;
import com.nt.dao_Auth.Vo.MembersVo;
import com.nt.dao_Pfans.PFANS4000.Seal;
import com.nt.dao_Pfans.PFANS4000.SealDetail;
import com.nt.dao_Workflow.Workflowinstance;
import com.nt.dao_Workflow.Workflownodeinstance;
import com.nt.dao_Workflow.Workflowstep;
import com.nt.service_Auth.RoleService;
import com.nt.service_WorkFlow.mapper.WorkflowinstanceMapper;
import com.nt.service_WorkFlow.mapper.WorkflownodeinstanceMapper;
import com.nt.service_WorkFlow.mapper.WorkflowstepMapper;
import com.nt.service_pfans.PFANS4000.SealService;
import com.nt.service_pfans.PFANS4000.mapper.SealDetailMapper;
import com.nt.service_pfans.PFANS4000.mapper.SealMapper;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.RequestUtils;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.bytedeco.javacpp.presets.opencv_core;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/seal")
public class Pfans4001Controller {

    @Autowired
    private SealService sealService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SealDetailMapper sealdetailmapper;

    @Autowired
    private SealMapper sealMapper;

    @Autowired
    private WorkflowinstanceMapper workflowinstanceMapper;

    @Autowired
    private WorkflownodeinstanceMapper workflownodeinstanceMapper;

    @Autowired
    private WorkflowstepMapper workflowstepMapper;

    @Autowired
    private RoleService roleService;

    @RequestMapping(value = "changeSealWorkFlowInfo", method = {RequestMethod.GET})
    public ApiResult SealWorkFlowInfo(HttpServletRequest request) throws Exception {
        Seal seal = new Seal();
        seal.setStatus("4");
        List<Seal> sealList = sealMapper.select(seal);
        List<MembersVo> rolelist = roleService.getMembers("5e785fd38f4316308435112d");
        String user_id = "";
        if (rolelist.size() > 0) {
            user_id = rolelist.get(0).getUserid();
        }
        int count = 0;
        if(sealList.size() > 0){
            for(Seal se : sealList){
                Workflowinstance workflowinstance = new Workflowinstance();
                workflowinstance.setDataid(se.getSealid());
                workflowinstance.setStatus("4");
                String agreeGm = "";
                List<Workflowinstance> wfiList = workflowinstanceMapper.select(workflowinstance);
                if(wfiList.size() > 0){
                    Workflownodeinstance workflownodeinstance = new Workflownodeinstance();
                    workflownodeinstance.setWorkflowinstanceid(wfiList.get(0).getWorkflowinstanceid());
                    List<Workflownodeinstance> wfnodeList = workflownodeinstanceMapper.select(workflownodeinstance);
                    Date modate = null;
                    if(wfnodeList.size() == 1){
                        Workflownodeinstance nodeInsert = new Workflownodeinstance();
                        nodeInsert.setWorkflownodeinstanceid(UUID.randomUUID().toString());
                        nodeInsert.setWorkflowinstanceid(wfnodeList.get(0).getWorkflowinstanceid());
                        nodeInsert.setNodename("总经理");
                        nodeInsert.setNodeord(2);
                        nodeInsert.setNodetype("2");
                        nodeInsert.setNodeusertype("1");
//                        nodeInsert.setItemid("5e78fefff1560b363cdd6db7");
                        nodeInsert.setItemid(user_id);
                        nodeInsert.setCreateby(wfnodeList.get(0).getCreateby());
                        nodeInsert.setCreateon(wfnodeList.get(0).getCreateon());
                        nodeInsert.setOwner(wfnodeList.get(0).getOwner());
                        nodeInsert.setStatus(wfnodeList.get(0).getStatus());
                        workflownodeinstanceMapper.insert(nodeInsert);

                        Workflowstep getStep = new Workflowstep();
                        getStep.setWorkflownodeinstanceid(wfnodeList.get(0).getWorkflownodeinstanceid());
                        List<Workflowstep> getStepList = workflowstepMapper.select(getStep);
                        if(getStepList.size() > 0){
                            Workflowstep workflowstep = new Workflowstep();
                            workflowstep.setWorkflowstepid(UUID.randomUUID().toString());
                            workflowstep.setWorkflownodeinstanceid(nodeInsert.getWorkflownodeinstanceid());
                            workflowstep.setResult("0");
                            workflowstep.setName("总经理");
//                            workflowstep.setItemid("5e78fefff1560b363cdd6db7");
                            workflowstep.setItemid(user_id);
                            agreeGm = getStepList.get(0).getItemid();
                            workflowstep.setCreateby(getStepList.get(0).getItemid());
                            workflowstep.setCreateon(getStepList.get(0).getModifyon());
//                            workflowstep.setModifyby("5e78fefff1560b363cdd6db7");
                            workflowstep.setModifyby(user_id);
                            int randsec;
                            randsec = (int)(Math.random() * 7200);
                            Date newDateStr = addDateMinut(String.valueOf(getStepList.get(0).getModifyon()),randsec);
                            modate = newDateStr;
                            workflowstep.setModifyon(newDateStr);
                            workflowstep.setOwner(getStepList.get(0).getModifyby());
                            workflowstep.setStatus("4");
                            workflowstepMapper.insert(workflowstep);
                        }else{
                            System.out.println("有错误");
                        }
                        Workflowinstance workflowTime = wfiList.get(0);
                        workflowTime.setModifyon(modate);
                        workflowinstanceMapper.updateByPrimaryKeySelective(workflowTime);
                    }
                }
                int flag = 0;
                if(com.mysql.jdbc.StringUtils.isNullOrEmpty(se.getAcceptor())){
                    flag ++;
                    se.setAcceptor(agreeGm);
                }
                if(com.mysql.jdbc.StringUtils.isNullOrEmpty(se.getAcceptstate())){
                    flag ++;
                    se.setAcceptstate("true");
                }
                if(com.mysql.jdbc.StringUtils.isNullOrEmpty(se.getRegulator())){
                    flag ++;
//                    se.setRegulator("5e78fefff1560b363cdd6db7");
                    se.setRegulator(user_id);
                }
                if(com.mysql.jdbc.StringUtils.isNullOrEmpty(se.getRegulatorstate())){
                    flag ++;
                    se.setRegulatorstate("true");
                }
                if(flag > 0){
                    sealMapper.updateByPrimaryKeySelective(se);
                }
            }
        }
        Seal sealEmpty = new Seal();
        sealEmpty.setStatus("4");
        sealEmpty.setAcceptor("");
        List<Seal> sealEmptyList = sealMapper.select(sealEmpty);
        String itemid = "";
        if(sealEmptyList.size() > 0){
            for(Seal setl :sealEmptyList){
                Workflowinstance workflowinstanceEmpty = new Workflowinstance();
                workflowinstanceEmpty.setDataid(setl.getSealid());
                workflowinstanceEmpty.setStatus("4");
                List<Workflowinstance> wemptList = workflowinstanceMapper.select(workflowinstanceEmpty);
                if(wemptList.size() > 0){
                    itemid = workflowinstanceMapper.getItemid(wemptList.get(0).getWorkflowinstanceid());
                    setl.setAcceptor(itemid);
                    sealMapper.updateByPrimaryKey(setl);
                }
            }
        }

        return ApiResult.success();
    }


    public static Date addDateMinut(String day, int second){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK).parse(day);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (date == null){
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND, second);// 24小时制
        date = cal.getTime();
        cal = null;
        return date;
    }

    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public ApiResult list(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);

        Seal seal = new Seal();
        List<SealDetail> sealdetaillist = sealdetailmapper.selectAll();
        if (sealdetaillist.size() > 0) {
            if (!tokenModel.getUserId().equals(sealdetaillist.get(0).getSealdetailname())) {
                seal.setOwners(tokenModel.getOwnerList());
            }
        }else{
            seal.setOwners(tokenModel.getOwnerList());
        }
        return ApiResult.success(sealService.list(seal));
    }

    @RequestMapping(value = "/insertInfo", method = {RequestMethod.POST})
    public ApiResult create(@RequestBody Seal seal, HttpServletRequest request) throws Exception {
        if (seal == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        sealService.insert(seal, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/createbook", method = {RequestMethod.POST})
    public ApiResult createbook(@RequestBody Seal seal, HttpServletRequest request) throws Exception {
        if (seal == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(sealService.createbook(seal, tokenModel));
    }

    @RequestMapping(value = "/updateInfo", method = {RequestMethod.POST})
    public ApiResult updateInformation(@RequestBody Seal seal, HttpServletRequest request) throws Exception {
        if (seal == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        sealService.upd(seal, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/oneInfo", method = {RequestMethod.POST})
    public ApiResult one(@RequestBody Seal seal, HttpServletRequest request) throws Exception {
        if (seal == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(sealService.One(seal.getSealid()));
    }
    //add-ws-12/21-印章盖印
    @RequestMapping(value = "/insertrecognition", method = {RequestMethod.POST})
    public ApiResult insertrecognition(@RequestBody Seal seal, HttpServletRequest request) throws Exception {
        if (seal == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        sealService.insertrecognition(seal.getSealid(),tokenModel);
        return ApiResult.success();
    }
    @GetMapping("/selectcognition")
    public ApiResult selectcognition(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success( sealService.selectcognition());
    }
    @GetMapping("/insertnamedialog")
    public ApiResult insertnamedialog(String sealdetailname, String sealdetaildate, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        sealService.insertnamedialog(sealdetailname,sealdetaildate,tokenModel);
        return ApiResult.success();
    }
    //add-ws-12/21-印章盖印

    @RequestMapping(value = "/selectEffective", method = {RequestMethod.POST})
    public ApiResult check(@RequestBody SealDetail sealDetail, HttpServletRequest request) throws Exception {
        if (sealDetail == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(sealService.selectEffective(sealDetail));
    }
}
