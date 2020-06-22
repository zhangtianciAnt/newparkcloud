package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.Award;
import com.nt.dao_Pfans.PFANS1000.Vo.AwardVo;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface AwardService {

      List<Award> get(Award award) throws Exception;
      AwardVo selectById(String award_id) throws Exception;
      // 禅道任务152
      List<Award> One(Award award) throws Exception;
      // 禅道任务152
      void updateAwardVo(AwardVo awardVo,TokenModel tokenModel) throws Exception;
//      void generateJxls(String awarded, HttpServletResponse response) throws Exception ;

}
