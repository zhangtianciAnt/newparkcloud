package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.Award;
import com.nt.dao_Pfans.PFANS1000.AwardDetail;
import com.nt.dao_Pfans.PFANS1000.Vo.AwardVo;
import com.nt.dao_Pfans.PFANS6000.Coststatisticsdetail;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface AwardService {

      List<Award> get(Award award) throws Exception;
      AwardVo selectById(String award_id) throws Exception;
      // 禅道任务152
      List<Award> One(Award award) throws Exception;
      // 禅道任务152
      //修改返回结果集类型 1103 fr
      boolean updateAwardVo(AwardVo awardVo,TokenModel tokenModel) throws Exception;
      //修改返回结果集类型 1103 to

      //      void generateJxls(String awarded, HttpServletResponse response) throws Exception ;

      //决裁书数据结转
      void dataCarryover(Award award, TokenModel tokenModel) throws Exception;

      //region scc add 21/8/20 受托合同，详情，部门下拉框数据源 from
      List<String> getCompanyen() throws Exception;
      //region scc add 21/8/20 受托合同，详情，部门下拉框数据源 to

      //region scc add 21/8/20 受托合同，详情，部门下拉框数据源 from
      List<AwardDetail> getAwardEntr(List<String> awardIdList) throws Exception;
      //region scc add 21/8/20 受托合同，详情，部门下拉框数据源 to

      List<Award> getEntSearch(Award award) throws Exception;

}
