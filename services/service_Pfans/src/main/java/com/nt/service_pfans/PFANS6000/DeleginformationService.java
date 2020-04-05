package com.nt.service_pfans.PFANS6000;

import com.nt.dao_Pfans.PFANS6000.Delegainformation;
import com.nt.dao_Pfans.PFANS6000.Vo.DelegainformationVo;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface DeleginformationService {

    //取出项目体制数据与项目数据，再对数据进行计算存入到月份表中
    public void updateDeleginformation(List<Delegainformation> delegainformationList, TokenModel tokenModel) throws Exception;

//    List<DelegainformationVo> getDelegainformation() throws Exception;

    List<DelegainformationVo> getYears(String year,List<String> owners) throws Exception;
}
