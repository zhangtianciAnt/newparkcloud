package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.Examinationobject;
import com.nt.dao_Pfans.PFANS2000.Vo.LunardetailVo;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface ExaminationobjectService {

    List<Examinationobject> getList() throws Exception;

}
