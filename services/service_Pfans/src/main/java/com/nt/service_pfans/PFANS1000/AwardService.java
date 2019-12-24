package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.Award;
import com.nt.dao_Pfans.PFANS1000.Vo.AwardVo;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface AwardService {

      List<Award> get(Award award) throws Exception;
      AwardVo selectById(String award_id) throws Exception;
      void insertAwardVo(AwardVo awardVo, TokenModel tokenModel) throws Exception;
      void updateAwardVo(AwardVo awardVo,TokenModel tokenModel) throws Exception;

}
