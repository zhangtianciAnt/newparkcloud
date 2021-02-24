package com.nt.service_pfans.PFANS6000;

import com.nt.dao_Pfans.PFANS6000.Delegainformation;
import com.nt.dao_Pfans.PFANS6000.Vo.DelegainformationVo;
import com.nt.dao_Pfans.PFANS6000.Vo.DelegainformationtaxVo;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface DeleginformationService {

    //取出项目体制数据与项目数据，再对数据进行计算存入到月份表中
    //insert gbb 20210223 PSDCD_PFANS_20201117_XQ_011 外协委托信息添加【总额税金】和【税率】 start
    //public void updateDeleginformation(List<Delegainformation> delegainformationList, TokenModel tokenModel) throws Exception;
    public void updateDeleginformation(DelegainformationtaxVo taxVo, TokenModel tokenModel) throws Exception;
    //insert gbb 20210223 PSDCD_PFANS_20201117_XQ_011 外协委托信息添加【总额税金】和【税率】 end

//    List<DelegainformationVo> getDelegainformation() throws Exception;

    //insert gbb 20210223 PSDCD_PFANS_20201117_XQ_011 外协委托信息添加【总额税金】和【税率】 start
    //List<DelegainformationVo> getYears(String year,String group_id,List<String> owners) throws Exception;
    DelegainformationtaxVo getYears(String year, String group_id, List<String> owners) throws Exception;
    //insert gbb 20210223 PSDCD_PFANS_20201117_XQ_011 外协委托信息添加【总额税金】和【税率】 start
    void saveDelegaTask()throws Exception ;
}
