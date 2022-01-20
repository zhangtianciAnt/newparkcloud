package com.nt.service_pfans.PFANS1000.Impl;
import com.nt.dao_Pfans.PFANS1000.Contract;
import com.nt.dao_Pfans.PFANS1000.Contractapplication;
import com.nt.dao_Pfans.PFANS1000.Contractnumbercount;
import com.nt.dao_Pfans.PFANS1000.NonJudgment;
import com.nt.dao_Pfans.PFANS1000.Vo.ContractVo;
import com.nt.dao_Pfans.PFANS5000.CompanyProjects;
import com.nt.service_pfans.PFANS1000.ContractService;
import com.nt.service_pfans.PFANS1000.mapper.ContractMapper;
import com.nt.service_pfans.PFANS1000.mapper.ContractapplicationMapper;
import com.nt.service_pfans.PFANS1000.mapper.ContractnumbercountMapper;
import com.nt.service_pfans.PFANS5000.mapper.*;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class ContractServiceImpl implements ContractService {

    @Autowired
    private ContractMapper contractMapper;

    @Autowired
    private ContractnumbercountMapper contractnumbercountMapper;

    @Autowired
    CompanyProjectsMapper companyProjectsMapper;

    @Autowired
    private ContractapplicationMapper contractapplicationMapper;

    @Override
    public List<Contract> get(Contract contract) throws Exception {
        List<Contract> contractlist = contractMapper.select(contract);
        if (contractlist.size()  > 0) {
            contractlist = contractlist.stream().sorted(Comparator.comparing(Contract::getCreateon).reversed()).collect(Collectors.toList());
        }
        return contractlist;
    }

    //  add  ml  211130  分页  from
    @Override
    public List<Contract> getPage(Contract contract) throws Exception {
        Contractapplication contractapplication = new Contractapplication();
        contractapplication.setType("1");
        List<Contractapplication> coList = contractapplicationMapper.select(contractapplication);
        List<Contractapplication> pjCodeList = contractapplicationMapper.getPjCode();
        List<Map<String, String>> checkdata = new ArrayList<>();
        for (Contractapplication con : coList) {
            List<Contractapplication> newpjCodeList = pjCodeList.stream().filter(str -> (str.getContractnumber().equals(con.getContractnumber()))).collect(Collectors.toList());
            if (newpjCodeList.size() > 0) {
                String strProjectnumber = newpjCodeList.get(0).getProjectnumber();
                con.setProjectnumber(strProjectnumber.substring(0, strProjectnumber.length() - 1));
            }
            if ("1".equals(con.getState()) || "有效".equals(con.getState())) {
                Map<String, String> map = new HashMap<>();
                map.put("contractnumber", con.getContractnumber());
                checkdata.add(map);
            }
        }

        List<Contract> contractlist = contractMapper.select(contract);
        if (contractlist.size()  > 0) {
            contractlist = contractlist.stream().sorted(Comparator.comparing(Contract::getCreateon).reversed()).collect(Collectors.toList());
        }

        List<Contract> conList = new ArrayList<>();
        for (Map<String, String> data : checkdata) {
            for (Contract con : contractlist) {
                if (data.get("contractnumber").equals(con.getContractnumber())) {
                    conList.add(con);
                }
            }
        }

        List<Contract> conLists = new ArrayList<>();
        for (Contract conTract : contractlist) {
            for (Contract conTracts : conList) {
                if (conTracts.getContractnumber().equals(conTract.getContractnumber())) {
                    conLists.add(conTract);
                }
            }
        }
        return conLists;
    }
    //  add  ml  211130  分页  to

    @Override
    public ContractVo One(String contract_id) throws Exception {
        Contract contract = contractMapper.selectByPrimaryKey(contract_id);
//        String name = "";
//        String [] companyProjectsid = contract.getPjnamechinese().split(",");
//        if(companyProjectsid.length > 0){
//            for (int i = 0;i < companyProjectsid.length;i++){
//                CompanyProjects companyProjects = new CompanyProjects();
//                companyProjects.setCompanyprojects_id(companyProjectsid[i]);
//                List<CompanyProjects> comList = companyProjectsMapper.select(companyProjects);
//                if(comList.size() > 0){
//                    name = name + comList.get(0).getProject_name() + ",";
//                }
//            }
//            if(!name.equals("")){
//                name = name.substring(0,name.length()-1);
//            }
//        }
//        contract.setPjnamechinese(name);
        // add by zy
        ContractVo result = new ContractVo();
        if ( contract!= null ) {
            BeanUtils.copyProperties(contract, result);

            Contractnumbercount count = new Contractnumbercount();
            count.setContractnumber(contract.getContractnumber());
            List<Contractnumbercount> contractnumbercountList = contractnumbercountMapper.select(count);
            contractnumbercountList = contractnumbercountList.stream().sorted(Comparator.comparing(Contractnumbercount::getRowindex)).collect(Collectors.toList());
            result.setNumberCount(contractnumbercountList);
        }
        // add end
        result.setContract(contract);
        return result;
    }

    @Override
    public void update(Contract contract, TokenModel tokenModel) throws Exception {
        contract.preUpdate(tokenModel);
        contractMapper.updateByPrimaryKey(contract);
    }


}
