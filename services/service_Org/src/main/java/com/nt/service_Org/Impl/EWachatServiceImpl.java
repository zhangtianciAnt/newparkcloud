package com.nt.service_Org.Impl;

import com.nt.dao_Org.CustomerInfo;
import com.nt.service_Org.EWechatService;
import com.nt.service_Org.mapper.EarlyvacationMapper;
import com.nt.utils.AuthConstants;
import com.nt.utils.services.JsTokenService;
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
 * @ClassName: EWachatServiceImpl
 * @Description: 企业微信
 * @Author: YANGSHUBO
 * @CreateDate: 2020/06/08
 * @UpdateUser: YANGSHUBO
 * @UpdateDate: 2020/06/08
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */

@Service
public class EWachatServiceImpl implements EWechatService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private JsTokenService jsTokenService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private EarlyvacationMapper earlyvacationMapper;

    /**
     * @方法名：getEWxById
     * @描述：企业微信获取考勤上班下班打卡信息
     * @创建日期：2020/06/08
     * @作者：YANGSHUBO
     * @参数：[starttime][endtime][useridlist]
     * @返回值：userVo
     */
//    @Override
//    public Map<String, Object> getWxById(String userid) throws Exception {
//        UserVo userVo = new UserVo();
//        Query queryAccount = new Query();
//        queryAccount.addCriteria(Criteria.where("openid").is(userid));
//        UserAccount userAccount = mongoTemplate.findOne(queryAccount, UserAccount.class);
//        CustomerInfo customerInfo = new CustomerInfo();
//        if (userAccount != null) {
//            Query query = new Query();
//            query.addCriteria(Criteria.where("userid").is(userAccount.get_id()));
//            customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
//            userVo.setCustomerInfo(customerInfo);
//            userVo.setUserAccount(userAccount);
//        } else {
//            userAccount = new UserAccount();
//            // 插入账号
//            userAccount.setUserid(userid);
//            userAccount.setUsertype("1");
//            userAccount.setOpenid(userid);
//            userAccount.setIsPassing("0");
//            mongoTemplate.save(userAccount);
//            userVo.setCustomerInfo(customerInfo);
//            userVo.setUserAccount(userAccount);
//        }
    //生成token
//        TokenModel tokenModel = new TokenModel();
//        tokenModel.setUserId(userAccount.get_id());
//        tokenModel.setUserType(userAccount.getUsertype());
//        tokenModel.setTenantId(userAccount.getTenantid());
//        tokenService.setToken(tokenModel);
//        Map<String, Object> result = new HashMap<String, Object>();
//        result.put("userVo", userVo);
////        result.put("tokenModel", tokenModel);
//        return result;
//    }

    /**
     * @方法名：getEWxById
     * @描述：企业微信获取考勤上班下班打卡信息
     * @创建日期：2020/06/08
     * @作者：YANGSHUBO
     * @参数：[starttime][endtime][useridlist]
     * @返回值：userVo
     */

    @Override
    public String ewxLogin(String access_token) throws Exception {
        return access_token;
    }

    @Override
    public List<CustomerInfo> useridList() throws Exception {
        //mongodb
        Query query = new Query();
        query.addCriteria(Criteria.where("status").is(AuthConstants.DEL_FLAG_NORMAL).andOperator(Criteria.where("userinfo.mobilenumber").ne("15900000000")));
        query.fields().include("userinfo.ewechatid");
        List<CustomerInfo> customerInfolist = mongoTemplate.find(query, CustomerInfo.class);
        return customerInfolist;
    }

    /**
     * @方法名：getEWxById
     * @描述：企业微信获取考勤上班下班打卡信息
     * @创建日期：2020/06/08
     * @作者：YANGSHUBO
     * @参数：[starttime][endtime][useridlist]
     * @返回值：userVo
     */


}



