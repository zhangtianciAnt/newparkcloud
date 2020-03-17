package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.Examinationproject;
import com.nt.dao_Pfans.PFANS2000.Lunardetail;
import com.nt.dao_Pfans.PFANS2000.Vo.LunardetailVo;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface LunardetailService {

    List<Lunardetail> getLunardetail(LunardetailVo lunardetailVo) throws  Exception;

    List<Examinationproject> getExam(String id) throws  Exception;

    void update(List<Lunardetail> lunardetailList, TokenModel tokenModel) throws Exception;

}
