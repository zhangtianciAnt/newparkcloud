package com.nt.service_pfans.PFANS1000.mapper;

import com.nt.dao_Assets.Assets;
import com.nt.dao_Pfans.PFANS1000.Judgement;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface JudgementMapper extends MyMapper<Judgement>{
    List<Judgement> selectJudgement();

    @Select("select judgnumbers,money,status,judgement_id AS judgementid from judgement where JUDGNUMBERS like  CONCAT('%',#{judgnumbers},'%')")
    List<Judgement> judgementAnt(@Param("judgnumbers") String judgnumbers);

    //region   add  ml  220112  检索  from
    List<Judgement> getJudgementSearch(Judgement judgement);
    //endregion   add  ml  220112  检索  to
}
