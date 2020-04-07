package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Pfans.PFANS1000.Award;
import com.nt.dao_Pfans.PFANS1000.AwardDetail;
import com.nt.dao_Pfans.PFANS1000.Contractnumbercount;
import com.nt.dao_Pfans.PFANS1000.StaffDetail;
import com.nt.dao_Pfans.PFANS1000.Vo.AwardVo;
import com.nt.dao_Pfans.PFANS5000.CompanyProjects;
import com.nt.service_pfans.PFANS1000.AwardService;
import com.nt.service_pfans.PFANS1000.mapper.AwardDetailMapper;
import com.nt.service_pfans.PFANS1000.mapper.AwardMapper;
import com.nt.service_pfans.PFANS1000.mapper.ContractnumbercountMapper;
import com.nt.service_pfans.PFANS1000.mapper.StaffDetailMapper;
import com.nt.service_pfans.PFANS5000.mapper.CompanyProjectsMapper;
import com.nt.utils.ExcelOutPutUtil;
import com.nt.utils.dao.TokenModel;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor=Exception.class)
public class AwardServiceImpl implements AwardService {

    @Autowired
    private AwardMapper awardMapper;

    @Autowired
    private AwardDetailMapper awardDetailMapper;

    @Autowired
    private StaffDetailMapper staffDetailMapper;

    @Autowired
    private ContractnumbercountMapper contractnumbercountMapper;

    @Autowired
    CompanyProjectsMapper companyProjectsMapper;

    @Override
    public List<Award> get(Award award) throws Exception {
        return awardMapper.select(award);
    }

    @Override
    public AwardVo selectById(String award_id) throws Exception {
        AwardVo awavo=new AwardVo();
        AwardDetail awadetail=new AwardDetail();
        awadetail.setAward_id(award_id);
        StaffDetail staffdetail=new StaffDetail();
        staffdetail.setAward_id(award_id);
        List<AwardDetail> awalist=awardDetailMapper.select(awadetail);
        List<StaffDetail> stafflist=staffDetailMapper.select(staffdetail);
        awalist=awalist.stream().sorted(Comparator.comparing(AwardDetail::getRowindex)).collect(Collectors.toList());
        stafflist=stafflist.stream().sorted(Comparator.comparing(StaffDetail::getRowindex)).collect(Collectors.toList());
        Award awa = awardMapper.selectByPrimaryKey(award_id);
        Award award = awardMapper.selectByPrimaryKey(award_id);
//        String name = "";
//        String [] companyProjectsid = award.getPjnamechinese().split(",");
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
//        award.setPjnamechinese(name);
        awavo.setAward(awa);
        awavo.setAwardDetail(awalist);
        awavo.setStaffDetail(stafflist);

        if ( awa != null ) {
            Contractnumbercount contractnumbercount = new Contractnumbercount();
            contractnumbercount.setContractnumber(awa.getContractnumber());
            List<Contractnumbercount> contractList = contractnumbercountMapper.select(contractnumbercount);
            awavo.setNumbercounts(contractList);
        }

        return awavo;
    }
    @Override
    public void updateAwardVo(AwardVo awardVo, TokenModel tokenModel) throws Exception {
        Award award=new Award();
        BeanUtils.copyProperties(awardVo.getAward(),award);
        award.preUpdate(tokenModel);
        awardMapper.updateByPrimaryKey(award);
        String awardid=award.getAward_id();

        AwardDetail award2=new AwardDetail();
        award2.setAward_id(awardid);
        awardDetailMapper.delete(award2);
        List<AwardDetail> awardDetails=awardVo.getAwardDetail();


        StaffDetail sta=new StaffDetail();
        sta.setAward_id(awardid);
        staffDetailMapper.delete(sta);
        List<StaffDetail> stalist=awardVo.getStaffDetail();


        if(awardDetails!=null){
            int rowindex=0;
            for(AwardDetail awarddetail: awardDetails){
                rowindex =rowindex +1;
                awarddetail.preInsert(tokenModel);
                awarddetail.setAwarddetail_id(UUID.randomUUID().toString());
                awarddetail.setAward_id(awardid);
                awarddetail.setRowindex(rowindex);
                awardDetailMapper.insertSelective(awarddetail);
            }
        }

        if(stalist!=null){
            int rowindex=0;
            for(StaffDetail staffDetail: stalist){
                rowindex =rowindex +1;
                staffDetail.preInsert(tokenModel);
                staffDetail.setStaffdetail_id(UUID.randomUUID().toString());
                staffDetail.setAward_id(awardid);
                staffDetail.setRowindex(rowindex);
                staffDetailMapper.insertSelective(staffDetail);
            }
        }

    }

}
