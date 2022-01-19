package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.PublicExpense;
import com.nt.dao_Pfans.PFANS1000.TotalCost;
import com.nt.dao_Pfans.PFANS1000.Vo.PublicExpenseVo;
import com.nt.dao_Pfans.PFANS1000.Vo.TotalCostVo;
import com.nt.dao_Pfans.PFANS3000.Purchase;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


public interface PublicExpenseService {
    List<PublicExpense> get(PublicExpense publicExpense) throws Exception;
//add-ws-7/9-禅道任务248
    Map<String, Object> exportjs(String publicexpenseid, HttpServletRequest request) throws Exception;
    //add-ws-7/9-禅道任务248
    List<PublicExpense> getpublicelist(String publicexpenseid) throws Exception;

    void insert(PublicExpenseVo publicExpenseVo, TokenModel tokenModel) throws Exception;

    List<TotalCost> gettotalcost(TotalCostVo totalcostvo) throws Exception;

    void update(PublicExpenseVo publicExpenseVo, TokenModel tokenModel) throws Exception;

    PublicExpenseVo selectById(String publicexpenseid) throws Exception;

    Map<String, String> getworkfolwPurchaseData(PublicExpense publicExpense) throws Exception;

    //region   add  ml  220112  检索  from
    List<PublicExpense> getSearch(PublicExpense publicExpense) throws Exception;
    //endregion   add  ml  220112  检索  to

}
