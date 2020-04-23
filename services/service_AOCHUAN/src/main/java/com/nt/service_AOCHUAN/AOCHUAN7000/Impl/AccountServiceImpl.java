package com.nt.service_AOCHUAN.AOCHUAN7000.Impl;

import com.nt.dao_AOCHUAN.AOCHUAN7000.Account;
import com.nt.service_AOCHUAN.AOCHUAN7000.AccountService;
import com.nt.service_AOCHUAN.AOCHUAN7000.mapper.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountMapper accountMapper;

    @Override
    public List<Account> get(Account account) throws Exception {
        return accountMapper.selectAll();
    }
}
