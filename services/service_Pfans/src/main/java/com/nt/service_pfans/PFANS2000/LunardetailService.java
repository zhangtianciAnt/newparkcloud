package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.Lunarbonus;
import com.nt.dao_Pfans.PFANS2000.Lunardetail;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface LunardetailService {

    List<Lunardetail> getLunardetail(Lunardetail lunardetail) throws  Exception;

    void update(List<Lunardetail> lunardetailList, TokenModel tokenModel) throws Exception;

}
