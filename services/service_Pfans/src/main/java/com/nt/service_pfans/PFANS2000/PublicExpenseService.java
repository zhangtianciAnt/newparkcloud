package com.nt.service_pfans.PFANS2000;
import java.util.List;

import com.nt.dao_Pfans.PFANS1000.PublicExpense;
import com.nt.utils.dao.TokenModel;

public interface PublicExpenseService {

    //列表查询
    List<PublicExpense> get(PublicExpense publicExpense) throws  Exception;

    //新建
    void create(PublicExpense publicExpense, TokenModel tokenModel) throws Exception;

    //编辑
    void update(PublicExpense publicExpense,TokenModel tokenModel) throws Exception;
    //按id查询
    PublicExpense one(String publicexpenseid) throws Exception;


}
