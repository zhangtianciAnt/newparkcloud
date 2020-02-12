package com.nt.service_pfans.PFANS5000;

import com.nt.dao_Pfans.PFANS5000.CloseApplicat;
import com.nt.dao_Pfans.PFANS5000.Vo.CloseApplicatVo;
import com.nt.utils.dao.TokenModel;


import java.util.List;

public interface CloseApplicatService {
    void insert(CloseApplicatVo closeApplicatVo,TokenModel tokenModel) throws Exception;

    void update(CloseApplicatVo closeApplicatVo,TokenModel tokenModel) throws Exception;

    List<CloseApplicat> get(CloseApplicat closeApplicat) throws Exception;

    CloseApplicatVo selectById(String closeApplicatid) throws Exception;

}
