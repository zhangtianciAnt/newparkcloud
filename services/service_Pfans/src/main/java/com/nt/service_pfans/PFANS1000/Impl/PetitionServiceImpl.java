package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Pfans.PFANS1000.Napalm;
import com.nt.dao_Pfans.PFANS1000.Petition;
import com.nt.dao_Pfans.PFANS5000.CompanyProjects;
import com.nt.service_pfans.PFANS1000.PetitionService;
import com.nt.service_pfans.PFANS1000.mapper.PetitionMapper;
import com.nt.service_pfans.PFANS5000.mapper.CompanyProjectsMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.text.SimpleDateFormat;
@Service
@Transactional(rollbackFor = Exception.class)
public class PetitionServiceImpl implements PetitionService {

    @Autowired
    private PetitionMapper petitionMapper;

    @Autowired
    CompanyProjectsMapper  companyProjectsMapper;

    @Override
    public List<Petition> get(Petition petition) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Petition> petitionlist = petitionMapper.select(petition);
        if (petitionlist.size() > 0) {
            petitionlist = petitionlist.stream().sorted(Comparator.comparing(Petition::getCreateon).reversed()).collect(Collectors.toList());
        }
        return petitionlist;
    }

    @Override
    public Petition one(String petition_id) throws Exception {
        Petition petition = petitionMapper.selectByPrimaryKey(petition_id);
//        String name = "";
//        String [] companyProjectsid = petition.getPjnamechinese().split(",");
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
//        petition.setPjnamechinese(name);
        return petition;
    }

    @Override
    public void update(Petition petition, TokenModel tokenModel) throws Exception {
        petition.preUpdate(tokenModel);
        petitionMapper.updateByPrimaryKeySelective(petition);

    }

}
