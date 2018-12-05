package com.nt.service_Org.Impl;


import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.UserAccount;
import com.nt.dao_Org.Vo.UserAccountVo;
import com.nt.service_Org.UserAccountVoService;
import com.nt.utils.LogicalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.nt.utils.MongoObject.CustmizeQuery;


@Service
public class UserAccountVoServiceImpl implements UserAccountVoService {
    @Autowired
    private MongoTemplate mongoTemplate;


    /**
     * @方法名：changePassword
     * @描述：获取密码加以验证
     * @创建日期：2018/11/05
     * @作者：WANGSHUAI
     * @参数：[userAccount]
     * @返回值：userAccountVo
     */
    @Override
    public void confirmPassword(UserAccountVo userAccountVo) throws Exception {
        //根据条件检索数据
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(userAccountVo.getUserid()));
        query.addCriteria(Criteria.where("password").is(userAccountVo.getPassword()));
        List<UserAccount> userAccountlist = mongoTemplate.find(query, UserAccount.class);
         //如果获取到的密码和用户名相同则保存，不然提交异常
        if (userAccountlist != null && userAccountlist.size() > 0) {
            UserAccount account = new UserAccount();
            account.set_id(userAccountVo.getUserid());
            account.setPassword(userAccountVo.getNewPsw());
            mongoTemplate.save(account);
        } else {
            throw new LogicalException("验证未通过！");
        }
    }

}

