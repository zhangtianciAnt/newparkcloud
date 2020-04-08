package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Pfans.PFANS1000.NonJudgment;
import com.nt.dao_Pfans.PFANS5000.CompanyProjects;
import com.nt.service_pfans.PFANS1000.NonJudgmentService;
import com.nt.service_pfans.PFANS1000.mapper.NonJudgmentMapper;
import com.nt.service_pfans.PFANS5000.mapper.CompanyProjectsMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class NonJudgmentServiceImpl implements NonJudgmentService {


    @Autowired
    private NonJudgmentMapper nonJudgmentMapper;

    @Autowired
    CompanyProjectsMapper companyProjectsMapper;

    @Override
    public List<NonJudgment> get(NonJudgment nonJudgment) throws Exception {
        return nonJudgmentMapper.select(nonJudgment);
    }

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
