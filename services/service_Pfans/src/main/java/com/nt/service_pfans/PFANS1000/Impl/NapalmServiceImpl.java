package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Pfans.PFANS1000.Napalm;
import com.nt.dao_Pfans.PFANS5000.CompanyProjects;
import com.nt.service_pfans.PFANS1000.NapalmService;
import com.nt.service_pfans.PFANS1000.mapper.NapalmMapper;
import com.nt.service_pfans.PFANS2000.Impl.WagesServiceImpl;
import com.nt.service_pfans.PFANS5000.mapper.CompanyProjectsMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class NapalmServiceImpl implements NapalmService {

    @Autowired
    private NapalmMapper napalmMapper;

    @Autowired
    CompanyProjectsMapper  companyProjectsMapper;

    @Override
    public List<Napalm> get(Napalm napalm) throws Exception{
        return napalmMapper.select(napalm);
    }

    @Override
    public Napalm One(String napalm_id) throws Exception {
        Napalm napalm = napalmMapper.selectByPrimaryKey(napalm_id);
        CompanyProjects companyProjects = new CompanyProjects();
        companyProjects.setCompanyprojects_id(napalm.getPjnamechinese());
        List<CompanyProjects> comList = companyProjectsMapper.select(companyProjects);
        String name = "";
        for(CompanyProjects cs:comList){
            if(cs.getProject_name().length()>1){
                name += cs.getProject_name() + ",";
            } else {
                name += cs.getProject_name();
            }
        }
        napalm.setPjnamechinese(name);
        return napalm;

    }

    @Override
    public void update(Napalm napalm, TokenModel tokenModel) throws Exception {
        napalm.preUpdate(tokenModel);
        napalmMapper.updateByPrimaryKey(napalm);
    }


}
