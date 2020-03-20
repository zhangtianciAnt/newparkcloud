package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.Bonussend;
import com.nt.dao_Pfans.PFANS2000.Wages;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface WagesService {

    List<Wages> select(TokenModel tokenModel);

    List<Wages> wagesList(Wages wages) throws Exception;

    List<Bonussend> bonusList(Bonussend bonussend) throws Exception;

    /**
     * 根据givingid获取工资
     *
     * @param givingId
     * @return
     * @throws Exception
     */
    List<Wages> getWagesByGivingId(String givingId) throws Exception;

}


