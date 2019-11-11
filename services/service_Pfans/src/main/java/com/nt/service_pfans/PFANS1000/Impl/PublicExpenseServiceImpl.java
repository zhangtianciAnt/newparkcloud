package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Pfans.PFANS1000.PublicExpense;
import com.nt.service_pfans.PFANS1000.PublicExpenseService;
import com.nt.service_pfans.PFANS1000.mapper.PublicExpenseMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PublicExpenseServiceImpl implements PublicExpenseService {


    @Autowired
    private PublicExpenseMapper publicExpenseMapper;

    //列表查询
    @Override
    public List<PublicExpense> get(PublicExpense publicExpense) throws Exception {
        return publicExpenseMapper.select(publicExpense);
    }

    //新建
    @Override
    public void insert(PublicExpense publicExpense, TokenModel tokenModel) throws Exception {
        if (!(publicExpense.equals(null) || publicExpense.equals(""))) {
            publicExpense.preInsert(tokenModel);
            publicExpense.setPublicexpenseid(UUID.randomUUID().toString());
            publicExpenseMapper.insertSelective(publicExpense);
        }

    }

    //编辑
    @Override
    public void update(PublicExpense publicExpense, TokenModel tokenModel) throws Exception {
        publicExpenseMapper.updateByPrimaryKey(publicExpense);
    }

    //按id查询
    @Override
    public PublicExpense selectById(String publicexpenseid) throws Exception {
        if (publicexpenseid.equals("")) {
            return null;
        }
        return publicExpenseMapper.selectByPrimaryKey(publicexpenseid);
    }
}
