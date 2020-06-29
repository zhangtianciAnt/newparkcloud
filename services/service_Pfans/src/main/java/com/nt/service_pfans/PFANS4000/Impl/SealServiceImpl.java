package com.nt.service_pfans.PFANS4000.Impl;

import com.mysql.jdbc.StringUtils;
import com.nt.dao_Auth.Vo.MembersVo;
import com.nt.dao_Org.ToDoNotice;
import com.nt.dao_Pfans.PFANS1000.Contractapplication;
import com.nt.dao_Pfans.PFANS1000.Contractnumbercount;
import com.nt.dao_Pfans.PFANS1000.Napalm;
import com.nt.dao_Pfans.PFANS1000.Petition;
import com.nt.dao_Pfans.PFANS4000.Seal;
import com.nt.service_Auth.RoleService;
import com.nt.service_Org.ToDoNoticeService;
import com.nt.service_pfans.PFANS1000.mapper.ContractapplicationMapper;
import com.nt.service_pfans.PFANS1000.mapper.ContractnumbercountMapper;
import com.nt.service_pfans.PFANS1000.mapper.NapalmMapper;
import com.nt.service_pfans.PFANS1000.mapper.PetitionMapper;
import com.nt.service_pfans.PFANS4000.SealService;
import com.nt.service_pfans.PFANS4000.mapper.SealMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class SealServiceImpl implements SealService {
    @Autowired
    private SealMapper sealMapper;
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
    @Override
    public List<Seal> list(Seal seal) throws Exception {
        return sealMapper.select(seal);
    }
    @Override
    public void insert(Seal seal, TokenModel tokenModel) throws Exception {
        seal.preInsert(tokenModel);
        seal.setSealid(UUID.randomUUID().toString());
        sealMapper.insert(seal);
    }

    //add_fjl_添加合同回款相关  start
    @Override
    public Seal createbook(Seal seal, TokenModel tokenModel) throws Exception {
        String seid = "";
        seal.preInsert(tokenModel);
        seal.setSealid(UUID.randomUUID().toString());
        sealMapper.insert(seal);
        Seal se = sealMapper.selectByPrimaryKey(seal);
        if (se != null) {
            seid = se.getSealid();
        }
        Petition petition = new Petition();
        if (!StringUtils.isNullOrEmpty(seal.getBookid())) {
            String[] boksplit = seal.getBookid().split(",");
            if (boksplit.length > 1) {
                if (boksplit[0].equals("6")) {//请求书
                    for (int a = 1; a < boksplit.length; a++) {
                        Petition pt = ptitionMapper.selectByPrimaryKey(boksplit[a]);
                        if (pt != null) {
                            pt.setSealstatus("1");
                            pt.preUpdate(tokenModel);
                            ptitionMapper.updateByPrimaryKey(pt);
                        }
                    }
                } else if (boksplit[0].equals("5")) {//纳品书
                    for (int a = 1; a < boksplit.length; a++) {
                        Napalm np = napalmMapper.selectByPrimaryKey(boksplit[a]);
                        if (np != null) {
                            np.setSealstatus("1");
                            np.preUpdate(tokenModel);
                            napalmMapper.updateByPrimaryKey(np);
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
                } else if (boksplit[0].equals("5")) {//纳品书
                    for (int a = 1; a < boksplit.length; a++) {
                        Napalm np = napalmMapper.selectByPrimaryKey(boksplit[a]);
                        if (np != null) {
                            np.setSealstatus("2");
                            np.preUpdate(tokenModel);
                            napalmMapper.updateByPrimaryKey(np);
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
                    }
                    if (countLi.get(0).getClaimconditionqh().equals("HT011003") && countLi.get(0).getDeliveryconditionqh().equals("HT009003")) {
                        //向财务部长（角色）发送待办
                        List<MembersVo> rolelist = roleService.getMembers("5e7861948f43163084351132");
                        if (rolelist.size() > 0) {
                            for (MembersVo rt : rolelist) {
                                //发起人创建代办
                                ToDoNotice toDoNotice = new ToDoNotice();
                                List<String> params = new ArrayList<String>();
                                toDoNotice.setTitle(countLi.get(0).getContractnumber() + countLi.get(0).getClaimtype() + "作成完毕，可进行资金回收确认");
                                toDoNotice.setInitiator(seal.getUserid());
                                toDoNotice.setContent(countLi.get(0).getContractnumber() + countLi.get(0).getClaimtype() + "作成完毕，可进行资金回收确认");
                                toDoNotice.setDataid(countLi.get(0).getContractnumber()); //合同是按照合同编号查询
                                toDoNotice.setUrl("/PFANS1026FormView");
                                toDoNotice.setWorkflowurl("/PFANS1026View");
                                toDoNotice.preInsert(tokenModel);
                                toDoNotice.setOwner(rt.getUserid());
                                toDoNoticeService.save(toDoNotice);
                            }
                        }
                    }
                }
            }
        }
        //add_fjl_添加合同回款相关  end
    }
    @Override
    public Seal One(String sealid) throws Exception {
        return sealMapper.selectByPrimaryKey(sealid);
    }
}
