package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Pfans.PFANS1000.Award;
import com.nt.dao_Pfans.PFANS1000.Contractapplication;
import com.nt.dao_Pfans.PFANS1000.Individual;
import com.nt.dao_Pfans.PFANS1000.Napalm;
import com.nt.dao_Pfans.PFANS5000.CompanyProjects;
import com.nt.service_pfans.PFANS1000.NapalmService;
import com.nt.service_pfans.PFANS1000.mapper.ContractapplicationMapper;
import com.nt.service_pfans.PFANS1000.mapper.NapalmMapper;
import com.nt.service_pfans.PFANS2000.Impl.WagesServiceImpl;
import com.nt.service_pfans.PFANS5000.mapper.CompanyProjectsMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class NapalmServiceImpl implements NapalmService {

    @Autowired
    private NapalmMapper napalmMapper;

    @Autowired
    CompanyProjectsMapper companyProjectsMapper;

    @Autowired
    private ContractapplicationMapper contractapplicationMapper;

    @Override
    public List<Napalm> get(Napalm napalm) throws Exception {
        List<Napalm> napalmlist = napalmMapper.select(napalm);
        if (napalmlist.size() > 0) {
            napalmlist = napalmlist.stream().sorted(Comparator.comparing(Napalm::getCreateon).reversed()).collect(Collectors.toList());
        }
        return napalmlist;
    }

    //  add  ml  211130  分页  from
    @Override
    public List<Napalm> getPage(Napalm napalm) throws Exception {
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

        List<Napalm> napalmlist = napalmMapper.select(napalm);
        if (napalmlist.size() > 0) {
            napalmlist = napalmlist.stream().sorted(Comparator.comparing(Napalm::getCreateon).reversed()).collect(Collectors.toList());
        }

        List<Napalm> napalmList = new ArrayList<>();
        for (Map<String, String> data : checkdata) {
            for (Napalm nap : napalmlist) {
                if (data.get("contractnumber").equals(nap.getContractnumber())) {
                    napalmList.add(nap);
                }
            }
        }

//        List<Napalm> napalmLists = new ArrayList<>();
//        for (Napalm napa : napalmlist) {
//            for (Napalm napas : napalmList) {
//                if (napas.getContractnumber().equals(napa.getContractnumber())) {
//                    napalmLists.add(napa);
//                }
//            }
//        }

        return napalmList;
    }
    //  add  ml  211130  分页  to

    @Override
    public Napalm One(String napalm_id) throws Exception {
        Napalm napalm = napalmMapper.selectByPrimaryKey(napalm_id);
//        String name = "";
//        String [] companyProjectsid = napalm.getPjnamechinese().split(",");
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
//        napalm.setPjnamechinese(name);
        return napalm;
    }

    @Override
    public void update(Napalm napalm, TokenModel tokenModel) throws Exception {
        napalm.preUpdate(tokenModel);
        napalmMapper.updateByPrimaryKey(napalm);
    }


}
