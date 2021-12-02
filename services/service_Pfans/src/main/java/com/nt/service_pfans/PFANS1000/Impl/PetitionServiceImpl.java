package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Pfans.PFANS1000.Contractapplication;
import com.nt.dao_Pfans.PFANS1000.Napalm;
import com.nt.dao_Pfans.PFANS1000.Petition;
import com.nt.dao_Pfans.PFANS5000.CompanyProjects;
import com.nt.service_pfans.PFANS1000.PetitionService;
import com.nt.service_pfans.PFANS1000.mapper.ContractapplicationMapper;
import com.nt.service_pfans.PFANS1000.mapper.PetitionMapper;
import com.nt.service_pfans.PFANS5000.mapper.CompanyProjectsMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.text.SimpleDateFormat;
@Service
@Transactional(rollbackFor = Exception.class)
public class PetitionServiceImpl implements PetitionService {

    @Autowired
    private PetitionMapper petitionMapper;

    @Autowired
    CompanyProjectsMapper  companyProjectsMapper;

    @Autowired
    private ContractapplicationMapper contractapplicationMapper;

    @Override
    public List<Petition> get(Petition petition) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Petition> petitionlist = petitionMapper.select(petition);
        if (petitionlist.size() > 0) {
            petitionlist = petitionlist.stream().sorted(Comparator.comparing(Petition::getCreateon).reversed()).collect(Collectors.toList());
        }
        return petitionlist;
    }

    //  add  ml  211201  分页  from
    @Override
    public List<Petition> getPage(Petition petition) throws Exception {
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

        List<Petition> petitionlist = petitionMapper.select(petition);
        if (petitionlist.size() > 0) {
            petitionlist = petitionlist.stream().sorted(Comparator.comparing(Petition::getCreateon).reversed()).collect(Collectors.toList());
        }

        List<Petition> petitionList = new ArrayList<>();
        for (Map<String, String> data : checkdata) {
            for (Petition pet : petitionlist) {
                if (data.get("contractnumber").equals(pet.getContractnumber())) {
                    petitionList.add(pet);
                }
            }
        }

        return petitionList;
    }
    //  add  ml  211201  分页  to

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
