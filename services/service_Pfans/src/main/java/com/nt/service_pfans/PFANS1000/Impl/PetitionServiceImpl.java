package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Pfans.PFANS1000.Petition;
import com.nt.dao_Pfans.PFANS5000.CompanyProjects;
import com.nt.service_pfans.PFANS1000.PetitionService;
import com.nt.service_pfans.PFANS1000.mapper.PetitionMapper;
import com.nt.service_pfans.PFANS5000.mapper.CompanyProjectsMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class PetitionServiceImpl implements PetitionService {

    @Autowired
    private PetitionMapper petitionMapper;

    @Autowired
    CompanyProjectsMapper  companyProjectsMapper;

    @Override
    public List<Petition> get(Petition petition) throws Exception {

        return petitionMapper.select(petition);
    }

    @Override
    public Petition one(String petition_id) throws Exception {
        Petition petition = petitionMapper.selectByPrimaryKey(petition_id);
        CompanyProjects companyProjects = new CompanyProjects();
        companyProjects.setCompanyprojects_id(petition.getPjnamechinese());
        List<CompanyProjects> comList = companyProjectsMapper.select(companyProjects);
        String name = "";
        for(CompanyProjects cs:comList){
            if(cs.getProject_name().length() > 1){
                name += cs.getProject_name() + ",";
            } else {
                name += cs.getProject_name();
            }
        }
        petition.setPjnamechinese(name);
        return petition;

    }

    @Override
    public void update(Petition petition, TokenModel tokenModel) throws Exception {
        petition.preUpdate(tokenModel);
        petitionMapper.updateByPrimaryKeySelective(petition);

    }

}
