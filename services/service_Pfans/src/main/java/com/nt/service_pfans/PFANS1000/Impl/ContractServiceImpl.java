package com.nt.service_pfans.PFANS1000.Impl;
import com.nt.dao_Pfans.PFANS1000.Contract;
import com.nt.dao_Pfans.PFANS1000.Contractnumbercount;
import com.nt.dao_Pfans.PFANS1000.Vo.ContractVo;
import com.nt.dao_Pfans.PFANS5000.CompanyProjects;
import com.nt.service_pfans.PFANS1000.ContractService;
import com.nt.service_pfans.PFANS1000.mapper.ContractMapper;
import com.nt.service_pfans.PFANS1000.mapper.ContractnumbercountMapper;
import com.nt.service_pfans.PFANS5000.mapper.CompanyProjectsMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional(rollbackFor = Exception.class)
public class ContractServiceImpl implements ContractService {

    @Autowired
    private ContractMapper contractMapper;

    @Autowired
    private ContractnumbercountMapper contractnumbercountMapper;

    @Autowired
    CompanyProjectsMapper companyProjectsMapper;

    @Override
    public List<Contract> get(Contract contract) throws Exception {
        return contractMapper.select(contract);
    }

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
