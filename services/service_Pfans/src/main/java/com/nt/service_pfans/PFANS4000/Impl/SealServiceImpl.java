package com.nt.service_pfans.PFANS4000.Impl;

import com.mysql.jdbc.StringUtils;
import com.nt.dao_Auth.Vo.MembersVo;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.ToDoNotice;
import com.nt.dao_Pfans.PFANS1000.Contractapplication;
import com.nt.dao_Pfans.PFANS1000.Contractnumbercount;
import com.nt.dao_Pfans.PFANS1000.*;
import com.nt.dao_Pfans.PFANS4000.Seal;
import com.nt.dao_Pfans.PFANS4000.SealDetail;
import com.nt.service_Auth.RoleService;
import com.nt.service_Org.ToDoNoticeService;
import com.nt.service_Org.mapper.TodoNoticeMapper;
import com.nt.service_pfans.PFANS1000.mapper.*;
import com.nt.service_pfans.PFANS4000.SealService;
import com.nt.service_pfans.PFANS4000.mapper.SealDetailMapper;
import com.nt.service_pfans.PFANS4000.mapper.SealMapper;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
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
        if (seal.getStatus().equals("4")) {
            seal.setAcceptor(tokenModel.getUserId());
//            总经理代办
            ToDoNotice toDoNotice = new ToDoNotice();
            toDoNotice.setUrl("/PFANS4001View");
            toDoNotice.setTitle("【印章申请】有需要您盖印承认得数据");
            toDoNotice.setStatus("0");
            List<ToDoNotice> todonoticelist = todoNoticeMapper.select(toDoNotice);
            if (todonoticelist.size() == 0) {
                List<MembersVo> rolelist = roleService.getMembers("5e785fd38f4316308435112d");
                if (rolelist.size() > 0) {
                    ToDoNotice toDoNotice3 = new ToDoNotice();
                    toDoNotice3.setTitle("【印章申请】有需要您盖印承认得数据");
                    toDoNotice3.setInitiator(seal.getUserid());
                    toDoNotice3.setContent("【印章申请】有需要您盖印承认得数据");
                    toDoNotice3.setDataid(seal.getSealid());
                    toDoNotice3.setUrl("/PFANS4001View");
                    toDoNotice3.setWorkflowurl("/PFANS4001View");
                    toDoNotice3.preInsert(tokenModel);
                    toDoNotice3.setOwner(rolelist.get(0).getUserid());
                    toDoNoticeService.save(toDoNotice3);
                }
            } else {
                ToDoNotice todonotice = new ToDoNotice();
                BeanUtils.copyProperties(todonoticelist.get(0), todonotice);
                todonotice.setCreateon(new Date());
                todonotice.setCreateby(seal.getUserid());
                todoNoticeMapper.updateByPrimaryKey(todonotice);
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
        List<SealDetail> sealdetaillist = sealdetailmapper.selectAll();
        if (sealdetaillist.size() > 0) {
            SealDetail seal = new SealDetail();
            seal.setSealdetailid(sealdetaillist.get(0).getSealdetailid());
            sealdetailmapper.delete(seal);
        }
        SealDetail sealdetail = new SealDetail();
        sealdetail.preInsert(tokenModel);
        sealdetail.setSealdetaildate(sealdetaildate);
        sealdetail.setSealdetailname(sealdetailname);
        sealdetail.setSealdetailid(UUID.randomUUID().toString());
        sealdetailmapper.insert(sealdetail);
    }

    @Override
    public List<SealDetail> selectcognition() throws Exception {
        return sealdetailmapper.selectAll();
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
                        seal.setAcceptstate("true");
                        seal.preUpdate(tokenModel);
                        sealMapper.updateByPrimaryKey(seal);
                    }
                } else {
                    if (seal != null) {
                        seal.setAcceptstate("true");
                        seal.preUpdate(tokenModel);
                        sealMapper.updateByPrimaryKey(seal);
                    }
                    ToDoNotice toDoNotice = new ToDoNotice();
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
                }
            }
        }
    }
    //add-ws-12/21-印章盖印
}
