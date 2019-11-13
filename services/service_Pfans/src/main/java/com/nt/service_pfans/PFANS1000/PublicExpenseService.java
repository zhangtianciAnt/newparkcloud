package com.nt.service_pfans.PFANS1000;

import java.util.List;

import com.nt.dao_Pfans.PFANS1000.PublicExpense;
import com.nt.dao_Pfans.PFANS1000.Vo.PublicExpenseVo;
import com.nt.utils.dao.TokenModel;



public interface PublicExpenseService {
    //列表查询
    List<PublicExpense> get(PublicExpense publicExpense) throws  Exception;

    //新建
    void insert(PublicExpenseVo publicExpenseVo, TokenModel tokenModel) throws Exception;

    //编辑
    void update(PublicExpenseVo publicExpenseVo, TokenModel tokenModel) throws Exception;
    //按id查询
    PublicExpenseVo selectById(String publicexpenseid) throws Exception;

}
