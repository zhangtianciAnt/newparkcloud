package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Auth.Vo.MembersVo;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.ToDoNotice;
import com.nt.dao_Pfans.PFANS1000.Award;
import com.nt.dao_Pfans.PFANS1000.LoanApplication;
import com.nt.dao_Pfans.PFANS1000.PolicyContract;
import com.nt.dao_Pfans.PFANS1000.Vo.PolicyContractVo;
import com.nt.dao_Pfans.PFANS1000.PolicyContractDetails;
import com.nt.service_Auth.RoleService;
import com.nt.service_Org.ToDoNoticeService;
import com.nt.service_pfans.PFANS1000.PolicyContractService;
import com.nt.service_pfans.PFANS1000.mapper.AwardMapper;
import com.nt.service_pfans.PFANS1000.mapper.PolicyContractMapper;
import com.nt.service_pfans.PFANS1000.mapper.PolicyContractDetailsMapper;
import com.nt.utils.StringUtils;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class PolicyContractServiceImpl implements PolicyContractService {

    @Autowired
    private PolicyContractMapper policycontractmapper;
    @Autowired
    private PolicyContractDetailsMapper policycontractdetailsmapper;

    @Autowired
    private AwardMapper awardMapper;
    @Autowired
    private RoleService roleService;
    @Autowired
    private ToDoNoticeService toDoNoticeService;
    @Override
    public PolicyContractVo One(String policycontract_id) throws Exception {
        PolicyContractVo policycontractvo = new PolicyContractVo();
        PolicyContract policycontrac = new PolicyContract();
        policycontrac.setPolicycontract_id(policycontract_id);
        List<PolicyContract> PolicyContractlist = policycontractmapper.select(policycontrac);
        PolicyContractDetails policycontractdetails = new PolicyContractDetails();
        policycontractdetails.setPolicycontract_id(policycontract_id);
        List<PolicyContractDetails> policycontractdetailslist = policycontractdetailsmapper.select(policycontractdetails);
        policycontractdetailslist = policycontractdetailslist.stream().sorted(Comparator.comparing(PolicyContractDetails::getInvoicenumber)).collect(Collectors.toList());
        policycontractvo.setPolicycontract(PolicyContractlist.get(0));
        policycontractvo.setPolicycontractdetails(policycontractdetailslist);
        return policycontractvo;
    }

    @Override
    public void updatePolicyContract(PolicyContractVo policycontractvo, TokenModel tokenModel) throws Exception {
        PolicyContract policy = new PolicyContract();
        PolicyContract policy2 = new PolicyContract();
        BeanUtils.copyProperties(policycontractvo.getPolicycontract(), policy);
        policy2.setPolicycontract_id(policy.getPolicycontract_id());
        policycontractmapper.delete(policy2);
        String status = policy.getStatus();
        String policycontract_id = policy.getPolicycontract_id();
        String user_id = policy.getUser_id();
        if (status.equals("4")) {
            policy.preInsert(tokenModel);
            policy.setType("1");
            policy.setStatus(status);
            policycontractmapper.insertSelective(policy);
            Award award = new Award();
            award.setPolicycontract_id(policycontract_id);
            List<Award> awardlist = awardMapper.select(award);
            for (Award awa : awardlist) {
                if(!awa.getStatus().equals("4")){
                    //军权——合同
                    List<MembersVo> rolelist = roleService.getMembers("5e9d62b91decd61bb0398686");
                    if (rolelist.size() > 0) {
                        ToDoNotice toDoNotice3 = new ToDoNotice();
                        toDoNotice3.setTitle("【方针合同觉书已创建完成！】");
                        toDoNotice3.setInitiator(user_id);
                        toDoNotice3.setContent("委托决裁发起得印章申请已成功！");
                        toDoNotice3.setDataid(awa.getAward_id());
                        toDoNotice3.setUrl("/PFANS1025FormView");
                        toDoNotice3.setWorkflowurl("/PFANS1025FormView");
                        toDoNotice3.preInsert(tokenModel);
                        toDoNotice3.setOwner(rolelist.get(0).getUserid());
                        toDoNoticeService.save(toDoNotice3);
                    }
                }
            }
        } else {
            policy.preInsert(tokenModel);
            policy.setStatus(status);
            policycontractmapper.insertSelective(policy);
        }
        PolicyContractDetails policycontractdetails = new PolicyContractDetails();
        policycontractdetails.setPolicycontract_id(policycontract_id);
        policycontractdetailsmapper.delete(policycontractdetails);
        List<PolicyContractDetails> policycontractdetailslist = policycontractvo.getPolicycontractdetails();
        if (policycontractdetailslist != null) {
            for (PolicyContractDetails policycontractdetail : policycontractdetailslist) {
                policycontractdetail.preInsert(tokenModel);
                policycontractdetail.setPolicycontractdetails_id(UUID.randomUUID().toString());
                policycontractdetail.setPolicycontract_id(policycontract_id);
                policycontractdetailsmapper.insertSelective(policycontractdetail);
            }
        }
    }

    @Override
    public List<PolicyContract> check(PolicyContract policycontract, TokenModel tokenModel) throws Exception {
        PolicyContract policy = new PolicyContract();
        policy.setOutsourcingcompany(policycontract.getOutsourcingcompany());
        return policycontractmapper.select(policy);
    }

    @Override
    public void insert(PolicyContractVo policycontractvo, TokenModel tokenModel) throws Exception {
        PolicyContract policycontract = new PolicyContract();
        BeanUtils.copyProperties(policycontractvo.getPolicycontract(), policycontract);
        List<PolicyContract> policycontractlist = policycontractmapper.selectAll();
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sf2 = new SimpleDateFormat("MM");
        SimpleDateFormat sf3 = new SimpleDateFormat("yyyy");
        Date date = new Date();
        String year = sf1.format(date);
        int number = 0;
        String Numbers = "";
        String no = "";
        if (policycontractlist.size() > 0) {
            for (PolicyContract policy : policycontractlist) {
                if (policy.getPolicynumbers() != "" && policy.getPolicynumbers() != null) {
                    String checknumber = StringUtils.uncapitalize(StringUtils.substring(policy.getPolicynumbers(), 2, 10));
                    if (Integer.valueOf(year).equals(Integer.valueOf(checknumber))) {
                        number = number + 1;
                    }
                }

            }
            if (number <= 8) {
                no = "00" + (number + 1);
            } else {
                no = "0" + (number + 1);
            }
        } else {
            no = "001";
        }
        Numbers = "FZ" + year + no;
        int aaa = Integer.valueOf(sf3.format(new Date())) - 1;
        if (sf2.format(policycontract.getApplicationdate()).equals("01") || sf2.format(policycontract.getApplicationdate()).equals("02") || sf2.format(policycontract.getApplicationdate()).equals("03")) {
            policycontract.setYearss(String.valueOf(aaa));
        } else {
            policycontract.setYearss(sf3.format(new Date()));
        }
        policycontract.setPolicynumbers(Numbers);
        policycontract.preInsert(tokenModel);
        policycontract.setPolicycontract_id(UUID.randomUUID().toString());
        policycontractmapper.insertSelective(policycontract);
        String policycontract_id = policycontract.getPolicycontract_id();
        List<PolicyContractDetails> policycontractdetailslist = policycontractvo.getPolicycontractdetails();
        if (policycontractdetailslist != null) {
            for (PolicyContractDetails policycontractdetail : policycontractdetailslist) {
                policycontractdetail.preInsert(tokenModel);
                policycontractdetail.setPolicycontractdetails_id(UUID.randomUUID().toString());
                policycontractdetail.setPolicycontract_id(policycontract_id);
                policycontractdetailsmapper.insertSelective(policycontractdetail);
            }
        }


    }
}
