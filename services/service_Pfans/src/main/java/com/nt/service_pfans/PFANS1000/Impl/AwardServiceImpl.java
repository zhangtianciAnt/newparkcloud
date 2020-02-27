package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Pfans.PFANS1000.Award;
import com.nt.dao_Pfans.PFANS1000.AwardDetail;
import com.nt.dao_Pfans.PFANS1000.StaffDetail;
import com.nt.dao_Pfans.PFANS1000.Vo.AwardVo;
import com.nt.service_pfans.PFANS1000.AwardService;
import com.nt.service_pfans.PFANS1000.mapper.AwardDetailMapper;
import com.nt.service_pfans.PFANS1000.mapper.AwardMapper;
import com.nt.service_pfans.PFANS1000.mapper.StaffDetailMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
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
       Award awa=awardMapper.selectByPrimaryKey(award_id);
       awavo.setAward(awa);
       awavo.setAwardDetail(awalist);
       awavo.setStaffDetails(stafflist);
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
        List<StaffDetail> stalist=awardVo.getStaffDetails();


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
