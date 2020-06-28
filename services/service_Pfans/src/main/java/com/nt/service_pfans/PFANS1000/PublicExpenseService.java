package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.PublicExpense;
import com.nt.dao_Pfans.PFANS1000.TotalCost;
import com.nt.dao_Pfans.PFANS1000.Vo.PublicExpenseVo;
import com.nt.dao_Pfans.PFANS1000.Vo.TotalCostVo;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


public interface PublicExpenseService {
    List<PublicExpense> get(PublicExpense publicExpense) throws Exception;

//    Map<String, Object> exportjs(String publicexpenseid, HttpServletRequest request) throws Exception;

    List<PublicExpense> getpublicelist(String publicexpenseid) throws Exception;

    void insert(PublicExpenseVo publicExpenseVo, TokenModel tokenModel) throws Exception;

    List<TotalCost> gettotalcost(TotalCostVo totalcostvo) throws Exception;

    void update(PublicExpenseVo publicExpenseVo, TokenModel tokenModel) throws Exception;

    PublicExpenseVo selectById(String publicexpenseid) throws Exception;


}
