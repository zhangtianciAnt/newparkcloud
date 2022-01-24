package com.nt.service_pfans.PFANS1000;


import com.nt.dao_Pfans.PFANS1000.Business;
import com.nt.dao_Pfans.PFANS1000.Vo.BusinessVo;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface BusinessService {

    List<Business> get(Business business) throws Exception;

    List<Business> getBuse() throws Exception;
    //add-ws-7/10-禅道247
    List<Business> list(BusinessVo businessVo) throws Exception;
    //add-ws-7/10-禅道247
    //add-ws-7/7-禅道153
    List<Business> selectById3(String offshore_id) throws Exception;
    //add-ws-7/7-禅道153

    BusinessVo selectById(String businessid) throws Exception;

    void insertBusinessVo(BusinessVo businessvo, TokenModel tokenModel)throws Exception;

    void updateBusinessVo(BusinessVo businessvo, TokenModel tokenModel)throws Exception;

    void saveDaka() throws Exception;

    void change(String center_id,String group_id,String team_id,String budgetunit,String change_id,String flag, TokenModel tokenModel)throws Exception;

    //region scc add 10/28 境内外决裁逻辑删除 from
    void busdelete(Business business, TokenModel tokenModel) throws Exception;
    //endregion scc add 10/28 境内外决裁逻辑删除 to

    //region   add  ml  220112  检索  from
    List<Business> getBusinessSearch(Business business) throws Exception;
    //endregion   add  ml  220112  检索  to

}
