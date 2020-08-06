package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Pfans.PFANS1000.LoanApplication;
import com.nt.dao_Pfans.PFANS1000.PolicyContract;
import com.nt.dao_Pfans.PFANS1000.Vo.PolicyContractVo;
import com.nt.dao_Pfans.PFANS1000.PolicyContractDetails;
import com.nt.service_pfans.PFANS1000.PolicyContractService;
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

    @Override
    public PolicyContractVo One(String policycontract_id) throws Exception {
        PolicyContractVo policycontractvo = new PolicyContractVo();
        PolicyContract policycontrac = new PolicyContract();
        policycontrac.setPolicycontract_id(policycontract_id);
        List<PolicyContract> PolicyContractlist = policycontractmapper.select(policycontrac);
        PolicyContractDetails policycontractdetails = new PolicyContractDetails();
        policycontractdetails.setPolicycontract_id(policycontract_id);
        List<PolicyContractDetails> policycontractdetailslist = policycontractdetailsmapper.select(policycontractdetails);
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
        if (status.equals("4")) {
            policy.preInsert(tokenModel);
            policy.setType("1");
            policy.setStatus(status);
            policycontractmapper.insertSelective(policy);
        } else {
            policy.preInsert(tokenModel);
            policy.setStatus(status);
            policycontractmapper.insertSelective(policy);
        }

        String policycontract_id = policy.getPolicycontract_id();
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
        if (sf2.format(policycontract.getApplicationdate()).equals("08") || sf2.format(policycontract.getApplicationdate()).equals("02") || sf2.format(policycontract.getApplicationdate()).equals("03")) {
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
