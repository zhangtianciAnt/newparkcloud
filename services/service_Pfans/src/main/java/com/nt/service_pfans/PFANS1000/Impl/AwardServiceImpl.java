package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Pfans.PFANS1000.Award;
import com.nt.dao_Pfans.PFANS1000.AwardDetail;
import com.nt.dao_Pfans.PFANS1000.Vo.AwardVo;
import com.nt.service_pfans.PFANS1000.AwardService;
import com.nt.service_pfans.PFANS1000.mapper.AwardDetailMapper;
import com.nt.service_pfans.PFANS1000.mapper.AwardMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor=Exception.class)
public class AwardServiceImpl implements AwardService {

    @Autowired
    private AwardMapper awardMapper;

    @Autowired
    private AwardDetailMapper awardDetailMapper;

    @Override
    public List<Award> get(Award award) throws Exception {
        return awardMapper.select(award);
    }

    @Override
    public AwardVo selectById(String award_id) throws Exception {
        return null;
    }

    @Override
    public void insertAwardVo(AwardVo awardVo, TokenModel tokenModel) throws Exception {

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
    }

}
