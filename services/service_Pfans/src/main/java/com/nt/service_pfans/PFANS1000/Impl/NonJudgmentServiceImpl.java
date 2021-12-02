package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Pfans.PFANS1000.Contractapplication;
import com.nt.dao_Pfans.PFANS1000.NonJudgment;
import com.nt.dao_Pfans.PFANS1000.Quotation;
import com.nt.dao_Pfans.PFANS5000.CompanyProjects;
import com.nt.service_pfans.PFANS1000.NonJudgmentService;
import com.nt.service_pfans.PFANS1000.mapper.ContractapplicationMapper;
import com.nt.service_pfans.PFANS1000.mapper.NonJudgmentMapper;
import com.nt.service_pfans.PFANS5000.mapper.CompanyProjectsMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.util.StringUtil;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class NonJudgmentServiceImpl implements NonJudgmentService {


    @Autowired
    private NonJudgmentMapper nonJudgmentMapper;

    @Autowired
    CompanyProjectsMapper companyProjectsMapper;

    @Autowired
    private ContractapplicationMapper contractapplicationMapper;

    @Autowired
    private CompanyProjectsMapper companyprojectsMapper;

    @Override
    public List<NonJudgment> get(NonJudgment nonJudgment) throws Exception {
        List<NonJudgment> nonjudgmentlist = nonJudgmentMapper.select(nonJudgment);
        if (nonjudgmentlist.size() > 0) {
            nonjudgmentlist = nonjudgmentlist.stream().sorted(Comparator.comparing(NonJudgment::getCreateon).reversed()).collect(Collectors.toList());
        }
        return nonjudgmentlist;
    }

    //   add  ml  211130  分页  from
    @Override
    public List<NonJudgment> getPage(NonJudgment nonJudgment) throws Exception {
        Contractapplication contract = new Contractapplication();
        contract.setType("1");
        List<Contractapplication> coList = contractapplicationMapper.select(contract);
        List<Map<String, String>> checkdata = new ArrayList<>();
        List<Contractapplication> pjCodeList = contractapplicationMapper.getPjCode();
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

        CompanyProjects pjnameSearch = new CompanyProjects();
        List<Map<String, String>> pjnameflg = new ArrayList<>();
        List<CompanyProjects> pjList = companyprojectsMapper.select(pjnameSearch);
        for (CompanyProjects pj : pjList) {
            Map<String, String> map = new HashMap<>();
            map.put("pjcode", pj.getCompanyprojects_id());
            map.put("pjname", pj.getProject_name());
            pjnameflg.add(map);
        }

        List<NonJudgment> nonjudgmentlist = nonJudgmentMapper.select(nonJudgment);
        if (nonjudgmentlist.size() > 0) {
            nonjudgmentlist = nonjudgmentlist.stream().sorted(Comparator.comparing(NonJudgment::getCreateon).reversed()).collect(Collectors.toList());
        }

        List<NonJudgment> nonList = new ArrayList<>();
        for (Map<String, String> check : checkdata) {
            for (NonJudgment non : nonjudgmentlist) {
                if (check.get("contractnumber").equals(non.getContractnumber())) {
                    if (!StringUtil.isEmpty(non.getJaname())) {
                        String numcount[] = non.getJaname().split(",");
                        if (numcount.length > 1) {
                            String bb = "";
                            for (int i = 1; i < numcount.length; i++) {
                                for (int j = 1; j < pjnameflg.size(); j++) {
                                    if (numcount[i].equals(pjnameflg.get(j).get("pjcode"))) {
                                        bb = bb + pjnameflg.get(j).get("pjname") + ',';
                                    }
                                }
                            }
                            if (!StringUtil.isEmpty(bb)) {
                                non.setJaname(bb.substring(0, bb.length() - 1));
                            }
                        } else {
                            for (int i = 1; i < pjnameflg.size(); i++) {
                                if (pjnameflg.get(i).get("pjcode").equals(non.getJaname())) {
                                    non.setJaname(pjnameflg.get(i).get("pjname"));
                                }
                            }
                        }
                    }
                    nonList.add(non);
                }
            }
        }

        List<NonJudgment> nonLists = new ArrayList<>();
        for (NonJudgment nonjudgment : nonjudgmentlist) {
            for (NonJudgment nonjudgments : nonList) {
                if (nonjudgment.getContractnumber().equals(nonjudgments.getContractnumber())) {
                    nonLists.add(nonjudgment);
                }
            }
        }
        return nonLists;
    }
    //   add  ml  211130  分页  to

    @Override
    public NonJudgment one(String nonjudgment_id) throws Exception {
        NonJudgment nonjudgment = nonJudgmentMapper.selectByPrimaryKey(nonjudgment_id);
//        String name = "";
//        String [] companyProjectsid = nonjudgment.getJaname().split(",");
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
//        nonjudgment.setJaname(name);
        return nonjudgment;
    }

    @Override
    public void update(NonJudgment nonJudgment, TokenModel tokenModel) throws Exception {
        nonJudgment.preUpdate(tokenModel);
        nonJudgmentMapper.updateByPrimaryKeySelective(nonJudgment);
    }
}
