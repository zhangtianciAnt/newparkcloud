package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.Award;
import com.nt.dao_Pfans.PFANS1000.Vo.AwardVo;
import com.nt.dao_Pfans.PFANS6000.Coststatisticsdetail;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface AwardService {

      List<Award> get(Award award) throws Exception;
      AwardVo selectById(String award_id) throws Exception;
      //region  add_qhr_20210616 委托决裁书-情报2表格带入信息
      List<Coststatisticsdetail> selectsupplier(String supplierinfor_id, String dates) throws Exception;
      //endregion  add_qhr_20210616 委托决裁书-情报2表格带入信息  上面引用
      // 禅道任务152
      List<Award> One(Award award) throws Exception;
      // 禅道任务152
      void updateAwardVo(AwardVo awardVo,TokenModel tokenModel) throws Exception;
//      void generateJxls(String awarded, HttpServletResponse response) throws Exception ;

      //决裁书数据结转
      void dataCarryover(Award award, TokenModel tokenModel) throws Exception;
}
