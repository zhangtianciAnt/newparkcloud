package com.nt.service_Org.Impl;

import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.UserAccount;
import com.nt.service_Org.UserService;
import com.nt.utils.LogicalException;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.service_Org.Impl
 * @ClassName: UserServiceImpl
 * @Description: java类作用描述
 * @Author: SKAIXX
 * @CreateDate: 2018/10/25
 * @UpdateUser: SKAIXX
 * @UpdateDate: 2018/10/25
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private TokenService tokenService;
    /**
     * @方法名：register
     * @描述：用户注册
     * @创建日期：2018/10/25
     * @作者：SKAIXX
     * @参数：[userAccount, customerInfo]
     * @返回值：void
     */
    @Override
    public void register(UserAccount userAccount) throws Exception {
        userAccount.preInsert();
        mongoTemplate.insert(userAccount);
    }

    @Override
    public TokenModel login(UserAccount userAccount) throws Exception {
        TokenModel tokenModel = new TokenModel();
        //根据条件检索数据
        Query query = new Query();
        query.addCriteria(Criteria.where("userid").is(userAccount.getUserid()));
        query.addCriteria(Criteria.where("password").is(userAccount.getPassword()));
        List<UserAccount> userAccountlist = mongoTemplate.find(query,UserAccount.class);

        //数据不存在时
        if(userAccountlist.size() <= 0){
            throw new LogicalException(MessageUtil.getMessage(MsgConstants.LOGIN_ERR_01));
        }else{
            tokenModel.setUserId(userAccountlist.get(0).getUserid());
            tokenModel.setUserType(userAccountlist.get(0).getUsertype());
            tokenModel.setTenantId(userAccountlist.get(0).getTenantid());
            tokenService.setToken(tokenModel);
        }
        return tokenModel;
    }

    /**
     * @方法名：saveCustomerInfo
     * @描述：添加客户信息（公司/个人（个人用户包括注册用户及个人客户））
     * @创建日期：2018/10/25
     * @作者：SKAIXX
     * @参数：[customerInfo]
     * @返回值：void
     */
    @Override
    public void saveCustomerInfo(CustomerInfo customerInfo) throws Exception {

    }
}
