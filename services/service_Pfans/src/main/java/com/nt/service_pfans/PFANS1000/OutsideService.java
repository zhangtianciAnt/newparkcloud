package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.Outside;
import com.nt.dao_Pfans.PFANS1000.Vo.OutsideVo;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface OutsideService {

    List<Outside> get(Outside outside) throws Exception;

    public OutsideVo selectById(String outsideid) throws Exception;

    void update(OutsideVo outsideVo, TokenModel tokenModel) throws Exception;

    public void insert(OutsideVo outsideVo, TokenModel tokenModel)throws Exception;

}
