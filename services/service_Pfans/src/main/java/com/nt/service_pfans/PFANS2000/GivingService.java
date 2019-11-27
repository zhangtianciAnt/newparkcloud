package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.Vo.AccumulatedTaxVo;
import com.nt.dao_Pfans.PFANS2000.Vo.GivingVo;
import com.nt.dao_Pfans.PFANS2000.Vo.DisciplinaryVo;
import com.nt.utils.dao.TokenModel;
import com.nt.dao_Pfans.PFANS2000.Giving;
import java.util.List;

public interface GivingService {

    GivingVo List(String giving_id) throws Exception;

    void insert(String generation, TokenModel tokenModel)throws Exception;

    void insertBase(String  givingid,TokenModel tokenModel)throws Exception;

    void insertOtherTwo(String  givingid,TokenModel tokenModel)throws Exception;

    void insertContrast(String  givingid,TokenModel tokenModel)throws Exception;

    List<Giving> getDataList(Giving giving) throws Exception;

}


