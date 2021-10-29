package com.nt.service_pfans.PFANS1000.mapper;

import com.nt.dao_Pfans.PFANS1000.Ruling;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RulingMapper extends MyMapper<Ruling> {

    void insetList(@Param("list") List<Ruling> rulings);

    Integer updateRulingInfo(@Param("ruling_id") String rulingid, @Param("useMoney") String useMoney, @Param("oldVersion") Long oldVersion);

    Integer cgTpReRulingInfo(@Param("ruling_id") String rulingid, @Param("renMoney") String renMoney, @Param("oldVersion") Long oldVersion);

    Integer woffRulingInfo(@Param("ruling_id") String rulingid, @Param("offMoney") String offMoney, @Param("oldVersion") Long oldVersion);

    Integer updateRulingInfoAnt(@Param("useMoney") String useMoney, @Param("code") String code, @Param("years") String years,
                                @Param("depart") String depart, @Param("oldVersion") Long oldVersion);

    Integer cgTpReRulingInfoAnt(@Param("renMoney") String renMoney, @Param("code") String code, @Param("years") String years,
                                @Param("depart") String depart, @Param("oldVersion") Long oldVersion);

    Integer woffRulingInfoAnt(@Param("offMoney") String offMoney, @Param("code") String code, @Param("years") String years,
                              @Param("depart") String depart, @Param("oldVersion") Long oldVersion);


}
