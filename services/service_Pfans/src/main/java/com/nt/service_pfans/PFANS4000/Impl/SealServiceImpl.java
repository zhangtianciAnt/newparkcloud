package com.nt.service_pfans.PFANS4000.Impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.mysql.jdbc.StringUtils;
import com.nt.dao_Assets.Inventoryplan;
import com.nt.dao_Auth.Vo.MembersVo;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.ToDoNotice;
import com.nt.dao_Pfans.PFANS1000.Contractapplication;
import com.nt.dao_Pfans.PFANS1000.Contractnumbercount;
import com.nt.dao_Pfans.PFANS1000.*;
import com.nt.dao_Pfans.PFANS4000.Seal;
import com.nt.dao_Pfans.PFANS4000.SealDetail;
import com.nt.dao_Workflow.Workflowinstance;
import com.nt.dao_Workflow.Workflownodeinstance;
import com.nt.dao_Workflow.Workflowstep;
import com.nt.service_Auth.RoleService;
import com.nt.service_Org.ToDoNoticeService;
import com.nt.service_Org.mapper.TodoNoticeMapper;
import com.nt.service_WorkFlow.mapper.WorkflowinstanceMapper;
import com.nt.service_WorkFlow.mapper.WorkflownodeinstanceMapper;
import com.nt.service_WorkFlow.mapper.WorkflowstepMapper;
import com.nt.service_pfans.PFANS1000.mapper.*;
import com.nt.service_pfans.PFANS4000.SealService;
import com.nt.service_pfans.PFANS4000.mapper.SealDetailMapper;
import com.nt.service_pfans.PFANS4000.mapper.SealMapper;
import com.nt.utils.AuthConstants;
import com.nt.utils.LogicalException;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.nt.dao_Pfans.PFANS4000.Vo.SealVo;

@Service
@Transactional(rollbackFor = Exception.class)
public class SealServiceImpl implements SealService {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private SealMapper sealMapper;

    @Autowired
    private TodoNoticeMapper todoNoticeMapper;

    @Autowired
    private AwardMapper awardMapper;
    @Autowired
    private PetitionMapper ptitionMapper;
    @Autowired
    private NapalmMapper napalmMapper;
    @Autowired
    private ContractnumbercountMapper contractnumbercountMapper;
    @Autowired
    private RoleService roleService;
    @Autowired
    private ToDoNoticeService toDoNoticeService;
    @Autowired
    private ContractapplicationMapper contractapplicationMapper;
    @Autowired
    private SealDetailMapper sealdetailmapper;
    @Autowired
    private WorkflowinstanceMapper workflowinstanceMapper;
    @Autowired
    private WorkflownodeinstanceMapper workflownodeinstanceMapper;
    @Autowired
    private WorkflowstepMapper workflowstepMapper;


    @Override
    public SealVo list(Seal seal) throws Exception {
        SealVo sealvo = new SealVo();
        List<Seal> seallist = sealMapper.select(seal);
        List<SealDetail> sealdetaillist = sealdetailmapper.selectAll();
        sealvo.setSeal(seallist);
        sealvo.setSealdetail(sealdetaillist);
        return sealvo;
    }

    @Override
    public void insert(Seal seal, TokenModel tokenModel) throws Exception {
        seal.preInsert(tokenModel);
        seal.setSealid(UUID.randomUUID().toString());
        sealMapper.insert(seal);
        //add-ws-9/3-禅道任务493
        Seal se = sealMapper.selectByPrimaryKey(seal.getSealid());
        if (!StringUtils.isNullOrEmpty(seal.getBookid())) {
            String[] boksplit = seal.getBookid().split(",");
            if (boksplit.length > 1) {
                if (boksplit[0].equals("6")) {//请求书
                    for (int a = 1; a < boksplit.length; a++) {
                        Petition pt = ptitionMapper.selectByPrimaryKey(boksplit[a]);
                        if (pt != null) {
                            pt.setSealstatus("1");
                            pt.setSealid(se.getSealid());
                            pt.preUpdate(tokenModel);
                            ptitionMapper.updateByPrimaryKey(pt);
                        }
                    }
                } else if (boksplit[0].equals("5")) {//纳品书
                    for (int a = 1; a < boksplit.length; a++) {
                        Napalm np = napalmMapper.selectByPrimaryKey(boksplit[a]);
                        if (np != null) {
                            np.setSealstatus("1");
                            np.setSealid(se.getSealid());
                            np.preUpdate(tokenModel);
                            napalmMapper.updateByPrimaryKey(np);
                        }
                    }
                } else if (boksplit[0].equals("7")) {//委托决裁
                    for (int a = 1; a < boksplit.length; a++) {
                        Award aw = awardMapper.selectByPrimaryKey(boksplit[a]);
                        if (aw != null) {
                            aw.setSealstatus("1");
                            aw.setSealid(se.getSealid());
                            aw.preUpdate(tokenModel);
                            awardMapper.updateByPrimaryKey(aw);
                        }
                    }
                } else if (boksplit[0].equals("9")) {//其他契约决裁
                    for (int a = 1; a < boksplit.length; a++) {
                        Award aw = awardMapper.selectByPrimaryKey(boksplit[a]);
                        if (aw != null) {
                            aw.setSealstatus("1");
                            aw.setSealid(se.getSealid());
                            aw.preUpdate(tokenModel);
                            awardMapper.updateByPrimaryKey(aw);
                        }
                    }
                }
            }
            //add-ws-9/3-禅道任务493
        }
    }

    //add_fjl_添加合同回款相关  start
    @Override
    public Seal createbook(Seal seal, TokenModel tokenModel) throws Exception {
        seal.preInsert(tokenModel);
        seal.setSealid(UUID.randomUUID().toString());
        sealMapper.insert(seal);
        Seal se = sealMapper.selectByPrimaryKey(seal);
        if (!StringUtils.isNullOrEmpty(seal.getBookid())) {
            String[] boksplit = seal.getBookid().split(",");
            if (boksplit.length > 1) {
                if (boksplit[0].equals("6")) {//请求书
                    for (int a = 1; a < boksplit.length; a++) {
                        Petition pt = ptitionMapper.selectByPrimaryKey(boksplit[a]);
                        if (pt != null) {
                            pt.setSealstatus("1");
                            pt.setSealid(se.getSealid());
                            pt.preUpdate(tokenModel);
                            ptitionMapper.updateByPrimaryKey(pt);
                        }
                    }
                } else if (boksplit[0].equals("5")) {//纳品书
                    for (int a = 1; a < boksplit.length; a++) {
                        Napalm np = napalmMapper.selectByPrimaryKey(boksplit[a]);
                        if (np != null) {
                            np.setSealstatus("1");
                            np.setSealid(se.getSealid());
                            np.preUpdate(tokenModel);
                            napalmMapper.updateByPrimaryKey(np);
                        }
                    }
                } else if (boksplit[0].equals("7")) {//委托决裁
                    for (int a = 1; a < boksplit.length; a++) {
                        Award aw = awardMapper.selectByPrimaryKey(boksplit[a]);
                        if (aw != null) {
                            aw.setSealstatus("1");
                            aw.setSealid(se.getSealid());
                            aw.preUpdate(tokenModel);
                            awardMapper.updateByPrimaryKey(aw);
                        }
                    }
                } else if (boksplit[0].equals("9")) {//其他契约决裁
                    for (int a = 1; a < boksplit.length; a++) {
                        Award aw = awardMapper.selectByPrimaryKey(boksplit[a]);
                        if (aw != null) {
                            aw.setSealstatus("1");
                            aw.setSealid(se.getSealid());
                            aw.preUpdate(tokenModel);
                            awardMapper.updateByPrimaryKey(aw);
                        }
                    }
                }
            }
        }
        return se;
    }

    //add_fjl_添加合同回款相关  end
    @Override
    public void upd(Seal seal, TokenModel tokenModel) throws Exception {
        seal.preUpdate(tokenModel);
//        if (seal.getStatus().equals("4")) {
//            seal.setAcceptor(tokenModel.getUserId());
////            总经理代办
//            ToDoNotice toDoNotice = new ToDoNotice();
//            //!!DATAID不能加，特殊需求（张总【印章申请】待办不存在时再发待办，存在【印章申请】待办不需要再发）  ztc start
//            //update gbb 20210326 代办查询添加dataid条件 start
//            //toDoNotice.setDataid(seal.getSealid());
//            //update gbb 20210326 代办查询添加dataid条件 end
//            toDoNotice.setUrl("/PFANS4001View");
//            toDoNotice.setTitle("【印章申请】有需要您盖印承认的数据");
//            toDoNotice.setStatus("0");
//            List<ToDoNotice> todonoticelist = todoNoticeMapper.select(toDoNotice);
//            if (todonoticelist.size() == 0) {
//                List<MembersVo> rolelist = roleService.getMembers("5e785fd38f4316308435112d");
//                if (rolelist.size() > 0) {
//                    ToDoNotice toDoNotice3 = new ToDoNotice();
//                    toDoNotice3.setTitle("【印章申请】有需要您盖印承认的数据");
//                    toDoNotice3.setInitiator(seal.getUserid());
//                    toDoNotice3.setContent("【印章申请】有需要您盖印承认的数据");
//                    toDoNotice3.setDataid(seal.getSealid());
//                    toDoNotice3.setUrl("/PFANS4001View");
//                    toDoNotice3.setWorkflowurl("/PFANS4001View");
//                    toDoNotice3.preInsert(tokenModel);
//                    toDoNotice3.setOwner(rolelist.get(0).getUserid());
//                    toDoNoticeService.save(toDoNotice3);
//                }//!!DATAID不能加，特殊需求（张总待办总不存在时再发待办，存在待办不需要再发）  ztc edn
//            } else {
//                ToDoNotice todonotice = new ToDoNotice();
//                BeanUtils.copyProperties(todonoticelist.get(0), todonotice);
//                todonotice.setCreateon(new Date());
//                todonotice.setCreateby(seal.getUserid());
//                todoNoticeMapper.updateByPrimaryKey(todonotice);
//            }
//        }
        if (seal.getStatus().equals("2")) {
            Workflowinstance wfince = new Workflowinstance();
            wfince.setDataid(seal.getSealid());
            wfince.setStatus("0");
            List<Workflowinstance> wfinceList = workflowinstanceMapper.select(wfince);
            if(wfinceList.size() > 0){
                Workflownodeinstance workflownodeinstance = new Workflownodeinstance();
                workflownodeinstance.setWorkflowinstanceid(wfinceList.get(0).getWorkflowinstanceid());
                workflownodeinstance.setNodeord(1);
                List<Workflownodeinstance> wfNodeinceList = workflownodeinstanceMapper.select(workflownodeinstance);
                if(wfNodeinceList.size() > 0){
                    Workflowstep workflowstep = new Workflowstep();
                    workflowstep.setWorkflownodeinstanceid(wfNodeinceList.get(0).getWorkflownodeinstanceid());
                    workflowstep.setResult("0");
                    List<Workflowstep> wfspList = workflowstepMapper.select(workflowstep);
                    if(wfspList.size() > 0){
                        seal.setAcceptor(wfspList.get(0).getItemid());
                    }
                }
            }
        }
        sealMapper.updateByPrimaryKey(seal);
        //add_fjl_添加合同回款相关  start
        Petition petition = new Petition();
        if (seal.getStatus().equals("2") && seal.getBookid().length() > 0) {
            String[] boksplit = seal.getBookid().split(",");
            if (boksplit.length > 1) {
                if (boksplit[0].equals("6")) {//请求书
                    for (int a = 1; a < boksplit.length; a++) {
                        Petition pt = ptitionMapper.selectByPrimaryKey(boksplit[a]);
                        if (pt != null) {
                            pt.setSealstatus("2");
                            pt.preUpdate(tokenModel);
                            ptitionMapper.updateByPrimaryKey(pt);
                        }
                    }
                } else if (boksplit[0].equals("7")) {//委托决裁
                    for (int a = 1; a < boksplit.length; a++) {
                        Award aw = awardMapper.selectByPrimaryKey(boksplit[a]);
                        if (aw != null) {
                            aw.setSealstatus("2");
                            aw.preUpdate(tokenModel);
                            awardMapper.updateByPrimaryKey(aw);
                        }
                    }
                } else if (boksplit[0].equals("5")) {//纳品书
                    for (int a = 1; a < boksplit.length; a++) {
                        Napalm np = napalmMapper.selectByPrimaryKey(boksplit[a]);
                        if (np != null) {
                            np.setSealstatus("2");
                            np.preUpdate(tokenModel);
                            napalmMapper.updateByPrimaryKey(np);
                        }
                    }
                } else if (boksplit[0].equals("9")) {//其他决裁
                    for (int a = 1; a < boksplit.length; a++) {
                        Award aw = awardMapper.selectByPrimaryKey(boksplit[a]);
                        if (aw != null) {
                            aw.setSealstatus("2");
                            aw.preUpdate(tokenModel);
                            awardMapper.updateByPrimaryKey(aw);
                        }
                    }
                }
            }
        } else if (seal.getStatus().equals("4") && seal.getBookid().length() > 0) {
            String[] ls = seal.getBookid().split(",");
            String bktype = ls[0].substring(0, 1);
            String book = "";
            List<Contractnumbercount> countLi = new ArrayList<>();
            if (ls.length > 1) {
                for (int i = 1; i < ls.length; i++) {
                    if (bktype.equals("6")) {
//                        book = "请求书";
                        Petition ption = ptitionMapper.selectByPrimaryKey(ls[i]);
                        if (ption != null) {
                            ption.setSealstatus("3");
                            ption.preUpdate(tokenModel);
                            ptitionMapper.updateByPrimaryKey(ption);
                            //纳品回数
                            Contractnumbercount pp = new Contractnumbercount();
                            pp.setClaimnumber(ption.getClaimnumber());
                            countLi = contractnumbercountMapper.select(pp);
                            //更新状况为完了
                            countLi.get(0).setClaimconditionqh("HT011003");
                            contractnumbercountMapper.updateByPrimaryKeySelective(countLi.get(0));
                        }
                    } else if (bktype.equals("5")) {
//                        book = "纳品书";
                        Napalm npalm = napalmMapper.selectByPrimaryKey(ls[i]);
                        if (npalm != null) {
                            npalm.setSealstatus("3");
                            npalm.preUpdate(tokenModel);
                            napalmMapper.updateByPrimaryKey(npalm);

                            //纳品回数
                            Contractnumbercount pp = new Contractnumbercount();
                            pp.setClaimnumber(npalm.getClaimnumber());
                            countLi = contractnumbercountMapper.select(pp);
                            //更新状况为完了
                            countLi.get(0).setDeliveryconditionqh("HT009003");
                            contractnumbercountMapper.updateByPrimaryKeySelective(countLi.get(0));
                        }
                    } else if (bktype.equals("7")) {
//                        book = "委托决裁";
                        Award award = awardMapper.selectByPrimaryKey(ls[i]);
                        if (award != null) {
                            award.setSealstatus("3");
                            award.preUpdate(tokenModel);
                            awardMapper.updateByPrimaryKey(award);
                            //更新状况为完了
                            Contractnumbercount pp = new Contractnumbercount();
                            pp.setContractnumber(award.getContractnumber());
                            countLi = contractnumbercountMapper.select(pp);
                            countLi.get(0).setDeliveryconditionqh("HT009003");
                            contractnumbercountMapper.updateByPrimaryKeySelective(countLi.get(0));
                        }
                        //外注代办
//                        List<MembersVo> rolelist = roleService.getMembers("5e78633d8f43163084351138");
//                        if (rolelist.size() > 0) {
//                            ToDoNotice toDoNotice3 = new ToDoNotice();
//                            toDoNotice3.setTitle("【" + countLi.get(0).getContractnumber() + "】发起得印章申请已成功");
//                            toDoNotice3.setInitiator(seal.getUserid());
//                            toDoNotice3.setContent("委托决裁发起得印章申请已成功！");
//                            toDoNotice3.setDataid(award.getAward_id());
//                            toDoNotice3.setUrl("/PFANS1025FormView");
//                            toDoNotice3.setWorkflowurl("/PFANS1025FormView");
//                            toDoNotice3.preInsert(tokenModel);
//                            toDoNotice3.setOwner(rolelist.get(0).getUserid());
//                            toDoNoticeService.save(toDoNotice3);
//                        }
                    } else if (bktype.equals("9")) {
//                        book = "其他决裁";
                        Award award = awardMapper.selectByPrimaryKey(ls[i]);
                        if (award != null) {
                            award.setSealstatus("3");
                            award.preUpdate(tokenModel);
                            awardMapper.updateByPrimaryKey(award);
                            //更新状况为完了
                            Contractnumbercount pp = new Contractnumbercount();
                            pp.setContractnumber(award.getContractnumber());
                            countLi = contractnumbercountMapper.select(pp);
                            countLi.get(0).setDeliveryconditionqh("HT009003");
                            contractnumbercountMapper.updateByPrimaryKeySelective(countLi.get(0));
                        }
                        //采购担当待办
                        List<MembersVo> rolelist = roleService.getMembers("5e7863d88f4316308435113b");
                        if (rolelist.size() > 0) {
                            //采购担当待办
                            ToDoNotice toDoNotice = new ToDoNotice();
                            toDoNotice.setTitle("【您有一个采购需处理】");
                            toDoNotice.setInitiator(seal.getUserid());
                            toDoNotice.setContent("有一个采购合同已经通过审批，可进行请款或支付！");
                            toDoNotice.setDataid(seal.getUserid());
                            toDoNotice.setUrl("/PFANS3005FormView");
                            toDoNotice.setWorkflowurl("/PFANS3005View");
                            toDoNotice.preInsert(tokenModel);
                            toDoNotice.setOwner(rolelist.get(0).getUserid());
                            toDoNoticeService.save(toDoNotice);
                        }
                    }
                }
            }
        }
        //add_fjl_添加合同回款相关  end
    }

    //add-ws-7/20-禅道任务342
    @Override
    public Seal One(String sealid) throws Exception {
        return sealMapper.selectByPrimaryKey(sealid);
    }

    //add-ws-12/21-印章盖印
    @Override
    public void insertnamedialog(String sealdetailname, String sealdetaildate, TokenModel tokenModel) throws Exception {
        SealDetail sealdetail = new SealDetail();
        sealdetail.preInsert(tokenModel);
        sealdetail.setSealdetaildate(sealdetaildate);
        sealdetail.setSealdetailname(sealdetailname);
        sealdetail.setSealdetailid(UUID.randomUUID().toString());
        sealdetailmapper.insert(sealdetail);
    }

    @Override
    public List<SealDetail> selectcognition() throws Exception {
        // 盖印监管者增加履历 ztc 0723 fr
        return sealdetailmapper.selectAll().stream().sorted(Comparator.comparing(SealDetail::getSealdetaildate).reversed()).collect(Collectors.toList());
        // 盖印监管者增加履历 ztc 0723 to
    }
    @Override
    public void insertrecognition(String sealid, TokenModel tokenModel) throws Exception {
        String[] sealidlist = sealid.split(",");
        List<SealDetail> sealdetaillist = sealdetailmapper.selectAll();
        if (sealdetaillist.size() > 0) {
            for (var i = 0; i < sealidlist.length; i++) {
                Seal seal = sealMapper.selectByPrimaryKey(sealidlist[i]);
                if (tokenModel.getUserId().equals(sealdetaillist.get(0).getSealdetailname())) {
                    if (seal != null) {
                        seal.setRegulator(sealdetaillist.get(0).getSealdetailname());
                        seal.setRegulatorstate("true");
                        //seal.setAcceptstate("true");
                        seal.preUpdate(tokenModel);
                        sealMapper.updateByPrimaryKey(seal);
                    }
                } else {
                    if (seal != null) {
                        seal.setAcceptstate("true");
                        seal.preUpdate(tokenModel);
                        seal.setStatus("4");
                        sealMapper.updateByPrimaryKey(seal);
                    }
                    String wfnodeid = "";
                    Workflowinstance workflowinstance = new Workflowinstance();
                    workflowinstance.setDataid(seal.getSealid());
                    List<Workflowinstance> wfceList = workflowinstanceMapper.select(workflowinstance);
                    if(wfceList.size() > 0){
                        BeanUtils.copyProperties(wfceList.get(0), workflowinstance);
                        workflowinstance.setModifyby(tokenModel.getUserId());
                        workflowinstance.setModifyon(new Date());
                        workflowinstance.setStatus(AuthConstants.APPROVED_FLAG_YES);
                        workflowinstanceMapper.updateByPrimaryKey(workflowinstance);

                        Workflownodeinstance workflownodeinstance = new Workflownodeinstance();
                        workflownodeinstance.setWorkflowinstanceid(workflowinstance.getWorkflowinstanceid());
                        workflownodeinstance.setItemid(tokenModel.getUserId());
                        List<Workflownodeinstance> wfnodeList = workflownodeinstanceMapper.select(workflownodeinstance);
                        if(wfnodeList.size() > 0){
                            wfnodeid = wfnodeList.get(0).getWorkflownodeinstanceid();
                            Workflowstep workflowstep = new Workflowstep();
                            workflowstep.setWorkflownodeinstanceid(wfnodeid);
                            List<Workflowstep> wfspList = workflowstepMapper.select(workflowstep);
                            if(wfspList.size() > 0){
                                BeanUtils.copyProperties(wfspList.get(0), workflowstep);
                                workflowstep.setResult("0");
                                workflowstep.setModifyby(tokenModel.getUserId());
                                workflowstep.setModifyon(new Date());
                                workflowstep.setStatus(AuthConstants.APPROVED_FLAG_YES);
                                workflowstepMapper.updateByPrimaryKey(workflowstep);
                            }
                        }
                        ToDoNotice toDoApply = new ToDoNotice();
                        toDoApply.setTitle("您发起的【印章申请】审批已结束！注意查看！");
                        toDoApply.setInitiator(seal.getUserid());
                        toDoApply.setUrl("/PFANS4001FormView");
                        toDoApply.setWorkflowurl("/PFANS4001FormView");
                        toDoApply.setDataid(seal.getSealid());
                        toDoApply.setStatus("0");
                        toDoApply.setOwner(seal.getUserid());
                        toDoApply.preInsert(tokenModel);
                        toDoNoticeService.save(toDoApply);

                        ToDoNotice toDoNotice = new ToDoNotice();
                        //update gbb 20210326 代办查询添加dataid条件 start
                        //toDoNotice.setDataid(seal.getSealid());
                        //update gbb 20210326 代办查询添加dataid条件 end
                        toDoNotice.setUrl("/PFANS4001View");
                        toDoNotice.setTitle("总经理已承认【印章申请】，需要您监管盖印");
                        toDoNotice.setOwner(sealdetaillist.get(0).getSealdetailname());
                        toDoNotice.setStatus("0");
                        List<ToDoNotice> todonoticelist = todoNoticeMapper.select(toDoNotice);
                        if (todonoticelist.size() == 0) {
                            ToDoNotice toDoNotice3 = new ToDoNotice();
                            toDoNotice3.setTitle("总经理已承认【印章申请】，需要您监管盖印");
                            toDoNotice3.setInitiator(tokenModel.getUserId());
                            toDoNotice3.setContent("已被赋予盖印监管者权限！");
                            toDoNotice3.setDataid(tokenModel.getUserId());
                            toDoNotice3.setUrl("/PFANS4001View");
                            toDoNotice3.setWorkflowurl("/PFANS4001View");
                            toDoNotice3.preInsert(tokenModel);
                            toDoNotice3.setOwner(sealdetaillist.get(0).getSealdetailname());
                            toDoNoticeService.save(toDoNotice3);
                        } else {
                            ToDoNotice todonotice = new ToDoNotice();
                            BeanUtils.copyProperties(todonoticelist.get(0), todonotice);
                            todonotice.setCreateon(new Date());
                            todonotice.setCreateby(seal.getUserid());
                            todoNoticeMapper.updateByPrimaryKey(todonotice);
                        }
                    }else{
                        throw new LogicalException("流程不匹配，请通知管理员");
                    }

                }
            }
        }
    }
    //add-ws-12/21-印章盖印

    @Override
    public int selectEffective(SealDetail sealDetail) throws Exception {
        List<SealDetail> selectAll = sealdetailmapper.selectAll();
        Date st = DateUtil.parse(sealDetail.getSealdetaildate().split("~")[0]);
        Date ed = DateUtil.parse(sealDetail.getSealdetaildate().split("~")[1]);
        for(SealDetail item : selectAll){
            // 盖印监管者增加履历 ztc 0723 fr
            String[] sealts = item.getSealdetaildate().split("~");
            if(sealts.length == 2){
                Date st1 = DateUtil.parse(sealts[0]);
                Date ed1 = DateUtil.parse(sealts[1]);
                // 盖印监管者增加履历 ztc 0723 to
                if(DateUtil.between(st, ed1, DateUnit.DAY,false) >= 0 && DateUtil.between(ed, st1, DateUnit.DAY,false) <= 0){
                    return 1;
                }
            }
        }
        return 0;
    }
    // 盖印监管者增加履历 ztc 0723 fr
    @Override
    public SealDetail getEffSeal(String newDateStr) throws Exception{
        SimpleDateFormat formatter  = new SimpleDateFormat("yyyy-MM-dd");
        List<SealDetail> sealDetailList = sealdetailmapper.selectAll();
        for(SealDetail sealDetail : sealDetailList){
            if (newDateStr.trim().equals(sealDetail.getSealdetaildate().split("~")[0].trim())
                    || newDateStr.trim().equals(sealDetail.getSealdetaildate().split("~")[1].trim())) {
                return sealDetail;
            }
            Calendar date = Calendar.getInstance();
            date.setTime(formatter.parse(newDateStr));

            Calendar begin = Calendar.getInstance();
            begin.setTime(formatter.parse(sealDetail.getSealdetaildate().split("~")[0]));

            Calendar end = Calendar.getInstance();
            end.setTime(formatter.parse(sealDetail.getSealdetaildate().split("~")[1]));

            if (date.after(begin) && date.before(end)) {
                return sealDetail;
            }
        }
        return null;
    }
    // 盖印监管者增加履历 ztc 0723 to
}
