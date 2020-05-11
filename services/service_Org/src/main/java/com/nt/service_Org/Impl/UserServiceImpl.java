package com.nt.service_Org.Impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.mysql.jdbc.StringUtils;
import com.nt.dao_Auth.Role;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.UserAccount;
import com.nt.dao_Org.Vo.UserVo;
import com.nt.dao_Org.Dictionary;
import com.nt.service_Org.DictionaryService;
import com.nt.service_Org.UserService;
import com.nt.utils.*;
import com.nt.utils.dao.JsTokenModel;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.JsTokenService;
import com.nt.utils.services.TokenService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.nt.utils.MongoObject.CustmizeQuery;

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
    private JsTokenService jsTokenService;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private TokenService tokenService;

    /**
     * @方法名：getUserAccount
     * @描述：获取用户
     * @创建日期：2018/10/25
     * @作者：DONG
     * @参数：[userAccount]
     * @返回值：List<UserAccount>
     */
    @Override
    public List<UserAccount> getUserAccount(UserAccount userAccount) throws Exception {
        Query query = CustmizeQuery(userAccount);
        return mongoTemplate.find(query, UserAccount.class);
    }

    /**
     * @方法名：inUserAccount
     * @描述：创建用户
     * @创建日期：2018/10/25
     * @作者：SKAIXX
     * @参数：[userAccount]
     * @返回值：void
     */
    @Override
    public void inUserAccount(UserAccount userAccount) throws Exception {
        mongoTemplate.insert(userAccount);
    }

    /**
     * @方法名：upUserAccount
     * @描述：更新用户
     * @创建日期：2018/10/25
     * @作者：DONG
     * @参数：[userAccount]
     * @返回值：void
     */
    @Override
    public void upUserAccount(UserAccount userAccount) throws Exception {
        mongoTemplate.save(userAccount);
    }

    /**
     * @方法名：login
     * @描述：用户登陆
     * @创建日期：2018/10/25
     * @作者：DONG
     * @参数：[userAccount]
     * @返回值：TokenModel
     */
    @Override
    public JsTokenModel login(UserAccount userAccount, String locale) throws Exception {

        final Base64.Decoder decoder = Base64.getDecoder();

        //根据条件检索数据
        Query query = new Query();
        query.addCriteria(Criteria.where("account").is(userAccount.getAccount()));
        query.addCriteria(Criteria.where("password").is(new String(decoder.decode(userAccount.getPassword()), "UTF-8")));
        query.addCriteria(Criteria.where("status").is(AuthConstants.DEL_FLAG_NORMAL));
        List<UserAccount> userAccountlist = mongoTemplate.find(query, UserAccount.class);

        //数据不存在时
        if (userAccountlist.size() <= 0) {
            throw new LogicalException(MessageUtil.getMessage(MsgConstants.ERROR_04, locale));
        } else {
            List<String> roleIds = new ArrayList<String>();
            query = new Query();
            query.addCriteria(Criteria.where("_id").is(userAccountlist.get(0).get_id()));
            UserAccount account = mongoTemplate.findOne(query, UserAccount.class);
            List<Role> roles = account.getRoles();
            if (roles != null) {
                for (int i = 0; i < roles.size(); i++) {
                    roleIds.add(roles.get(i).get_id());
                }
            }

            return jsTokenService.createToken(userAccountlist.get(0).get_id(), userAccountlist.get(0).getTenantid(), userAccountlist.get(0).getUsertype(), new ArrayList<String>(), locale, "",roleIds);
        }

    }

    /**
     * @方法名：getCustomerInfo
     * @描述：查询用户信息
     * @创建日期：2018/10/25
     * @作者：DONG
     * @参数：[customerInfo]
     * @返回值：List<CustomerInfo>
     */
    @Override
    public List<CustomerInfo> getCustomerInfo(CustomerInfo customerInfo) throws Exception {
        Query query = CustmizeQuery(customerInfo);
        return mongoTemplate.find(query, CustomerInfo.class);
    }

    /**
     * @方法名：inCustomerInfo
     * @描述：添加客户信息（公司/个人（个人用户包括注册用户及个人客户））
     * @创建日期：2018/10/25
     * @作者：SKAIXX
     * @参数：[customerInfo,tokenModel]
     * @返回值：void
     */
    @Override
    public void inCustomerInfo(CustomerInfo customerInfo) throws Exception {
        mongoTemplate.insert(customerInfo);
    }

    /**
     * @方法名：upCustomerInfo
     * @描述：更新客户信息（公司/个人（个人用户包括注册用户及个人客户））
     * @创建日期：2018/10/25
     * @作者：DONG
     * @参数：[customerInfo,tokenModel]
     * @返回值：void
     */
    @Override
    public void upCustomerInfo(CustomerInfo customerInfo) throws Exception {
        mongoTemplate.save(customerInfo);
    }

    /**
     * @方法名：addAccountCustomer
     * @描述：添加用户及用户信息
     * @创建日期：2018/12/06
     * @作者：ZHANGYING
     * @参数：[userVo]
     * @返回值：_id
     */
    @Override
    public CustomerInfo addAccountCustomer(UserVo userVo) throws Exception {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        UserAccount userAccount = new UserAccount();
        BeanUtils.copyProperties(userVo.getUserAccount(), userAccount);
        mongoTemplate.save(userAccount);
        Query query = new Query();
        query.addCriteria(Criteria.where("account").is(userAccount.getAccount()));
        query.addCriteria(Criteria.where("password").is(userAccount.getPassword()));
        List<UserAccount> userAccountlist = mongoTemplate.find(query, UserAccount.class);
        if (userAccountlist.size() > 0) {
            String _id = userAccountlist.get(0).get_id();
            CustomerInfo customerInfo = new CustomerInfo();
            BeanUtils.copyProperties(userVo.getCustomerInfo(), customerInfo);
            CustomerInfo.UserInfo userInfo = new CustomerInfo.UserInfo();
            BeanUtils.copyProperties(userVo.getCustomerInfo().getUserinfo(), userInfo);
            int flg1 = 0;
            int flg2 = 0;
            int flg3 = 0;
            int flg4 = 0;
//邮箱重复check
            Query queryEmail = new Query();
            if(!StringUtils.isNullOrEmpty(userInfo.getEmail())){
                queryEmail.addCriteria(Criteria.where("userinfo.email").is(userInfo.getEmail()));
                List<CustomerInfo> qcEmail = mongoTemplate.find(queryEmail, CustomerInfo.class);
                if(qcEmail.size() == 0 || qcEmail.get(0).getUserid().equals(customerInfo.getUserid())){
                    flg1 = 1;
                }
                else {
                    throw new LogicalException("邮箱已存在！");
                }
            }
//add-ws-4/28-人员重复check
            Query queryname = new Query();
            if(!StringUtils.isNullOrEmpty(userInfo.getCustomername())){
                queryname.addCriteria(Criteria.where("userinfo.customername").is(userInfo.getCustomername()));
                List<CustomerInfo> qcname = mongoTemplate.find(queryname, CustomerInfo.class);
                if(qcname.size() == 0 || qcname.get(0).getUserid().equals(customerInfo.getUserid())){
                    flg4 = 1;
                }
                else {
                    throw new LogicalException("人名已存在！");
                }
            }

//add-ws-4/28-人员重复check
//个人编码重复check
            Query queryCode = new Query();
            if(!StringUtils.isNullOrEmpty(userInfo.getAdfield())){
                queryCode.addCriteria(Criteria.where("userinfo.adfield").is(userInfo.getAdfield()));
                List<CustomerInfo> qcAD = mongoTemplate.find(queryCode, CustomerInfo.class);
                if(qcAD.size() == 0 || qcAD.get(0).getUserid().equals(customerInfo.getUserid())){
                    flg2 = 1;
                }
                else {
                    throw new LogicalException("AD域账号重复");
                }
            }

//AD域重复check
            Query queryAD = new Query();
            if(!StringUtils.isNullOrEmpty(userInfo.getPersonalcode())){
                queryAD.addCriteria(Criteria.where("userinfo.personalcode").is(userInfo.getPersonalcode()));
                List<CustomerInfo> qcCode = mongoTemplate.find(queryAD, CustomerInfo.class);
                if(qcCode.size() == 0 || qcCode.get(0).getUserid().equals(customerInfo.getUserid())){
                    flg3 = 1;
                }
                else {
                    throw new LogicalException("个人编码重复");
                }
            }
            if(flg1 == 1 && flg2 == 1 && flg3 == 1&& flg4 == 1){
                customerInfo.setUserid(_id);
                customerInfo.setUserinfo(userInfo);
                mongoTemplate.save(customerInfo);
            }
            return customerInfo;
        } else {
            return new CustomerInfo();
        }
    }

    /**
     * @方法名：getAccountCustomer
     * @描述：根据orgid获取用户及用户信息列表
     * @创建日期：2018/12/06
     * @作者：ZHANGYING
     * @参数：[orgid, orgtype]
     * @返回值：List<CustomerInfo>
     */
    @Override
    public List<CustomerInfo> getAccountCustomer(String orgid, String orgtype,TokenModel tokenModel) throws Exception {
        Query query = new Query();
//        if (StrUtil.isNotBlank(orgid)) {
//            query.addCriteria(new Criteria().orOperator(Criteria.where("userinfo.centerid").is(orgid),
//                    Criteria.where("userinfo.groupid").is(orgid), Criteria.where("userinfo.teamid").is(orgid)));
//        }
        //根据登陆用户id查看人员信息
        List<CustomerInfo> customerInfos = new ArrayList<CustomerInfo>();
        if (!"5e78fefff1560b363cdd6db7".equals(tokenModel.getUserId()) && !"5e78b22c4e3b194874180f5f".equals(tokenModel.getUserId()) && !"5e78b2284e3b194874180f47".equals(tokenModel.getUserId())
                && !"5e78b2034e3b194874180e37".equals(tokenModel.getUserId()) && !"5e78b17ef3c8d71e98a2aa30".equals(tokenModel.getUserId())){
            query.addCriteria(Criteria.where("userid").is(tokenModel.getUserId()));
            List<CustomerInfo> CustomerInfolist = mongoTemplate.find(query, CustomerInfo.class);
            query = new Query();
            if(CustomerInfolist.size() > 0){
                if(StrUtil.isNotBlank(CustomerInfolist.get(0).getUserinfo().getTeamid())){
                    query.addCriteria(Criteria.where("userinfo.teamid").is(CustomerInfolist.get(0).getUserinfo().getTeamid()));
                }else  if(StrUtil.isNotBlank(CustomerInfolist.get(0).getUserinfo().getGroupid())){
                    query.addCriteria(Criteria.where("userinfo.groupid").is(CustomerInfolist.get(0).getUserinfo().getGroupid()));
                }else  if(StrUtil.isNotBlank(CustomerInfolist.get(0).getUserinfo().getCenterid())){
                    query.addCriteria(Criteria.where("userinfo.centerid").is(CustomerInfolist.get(0).getUserinfo().getCenterid()));
                }

                customerInfos.addAll(mongoTemplate.find(query, CustomerInfo.class));

                if(CustomerInfolist.get(0).getUserinfo().getOtherorgs() != null && CustomerInfolist.get(0).getUserinfo().getOtherorgs().size() > 0){
                    for(CustomerInfo.OtherOrgs itemO:CustomerInfolist.get(0).getUserinfo().getOtherorgs()){
                        query = new Query();

                        if(StrUtil.isNotBlank(itemO.getTeamid())){
                            query.addCriteria(Criteria.where("userinfo.teamid").is(itemO.getTeamid()));
                        }else  if(StrUtil.isNotBlank(itemO.getGroupid())){
                            query.addCriteria(Criteria.where("userinfo.groupid").is(itemO.getGroupid()));
                        }else  if(StrUtil.isNotBlank(itemO.getCenterid())){
                            query.addCriteria(Criteria.where("userinfo.centerid").is(itemO.getCenterid()));
                        }

                        customerInfos.addAll(mongoTemplate.find(query, CustomerInfo.class));
                    }
                }
            }
        }else{
            query = new Query();
            customerInfos.addAll(mongoTemplate.find(query, CustomerInfo.class));
        }


        return customerInfos;
    }

    @Override
    public List<CustomerInfo> getAccountCustomer2(String orgid, String orgtype,TokenModel tokenModel) throws Exception {
        Query query = new Query();
        if (StrUtil.isNotBlank(orgid)) {
            query.addCriteria(new Criteria().orOperator(Criteria.where("userinfo.centerid").is(orgid),
                    Criteria.where("userinfo.groupid").is(orgid), Criteria.where("userinfo.teamid").is(orgid)));
        }
        List<CustomerInfo> customerInfos = mongoTemplate.find(query, CustomerInfo.class);
        for(CustomerInfo item:customerInfos){
            item.getUserinfo().setIdnumber("");
            item.getUserinfo().setPassport("");
            item.getUserinfo().setSecurity("");
            item.getUserinfo().setHousefund("");
            item.getUserinfo().setAddress("");
            item.getUserinfo().setMobilenumber("");
            item.getUserinfo().setPhone("");
            item.getUserinfo().setExtension("");
            item.getUserinfo().setWorkday("");
            item.getUserinfo().setBeforeWorkTable(new ArrayList<CustomerInfo.TableInfo>());
            item.getUserinfo().setJobnumber("");
            item.getUserinfo().setBudgetunit("");
            item.getUserinfo().setPersonalcode("");
            item.getUserinfo().setType("");
            item.getUserinfo().setOccupationtype("");
            item.getUserinfo().setDifference("");
            item.getUserinfo().setLaborcontracttype("");
            item.getUserinfo().setFixedate("");
            item.getUserinfo().setEnterday("");
            item.getUserinfo().setUpgraded("");
            item.getUserinfo().setEnddate("");
            item.getUserinfo().setAnnualyear("");
            item.getUserinfo().setAnnuallastyear("");
            item.getUserinfo().setWelfareyear("");
            item.getUserinfo().setWelfarelastyear("");
            item.getUserinfo().setRestyear("");
            item.getUserinfo().setRestlastyear("");
            item.getUserinfo().setSeatnumber("");
            item.getUserinfo().setBasic("");
            item.getUserinfo().setDuty("");
            item.getUserinfo().setOldageinsurance("");
            item.getUserinfo().setHouseinsurance("");
            item.getUserinfo().setMedicalinsurance("");
            item.getUserinfo().setSalary("");
            item.getUserinfo().setCaution("");
            item.getUserinfo().setWorkAfterTable(new ArrayList<CustomerInfo.TableInfo>());
            item.getUserinfo().setTrainTable(new ArrayList<CustomerInfo.TableInfo>());
            item.getUserinfo().setRewardTable(new ArrayList<CustomerInfo.TableInfo>());
            item.getUserinfo().setResignation_date("");
            item.getUserinfo().setReason2("");
        }
        return customerInfos;
    }

    /**
     * @方法名：getAccountCustomerById
     * @描述：根据用户id获取用户信息
     * @创建日期：2018/12/06
     * @作者：ZHANGYING
     * @参数：[userid]
     * @返回值：CustomerInfo
     */
    @Override
    public UserVo getAccountCustomerById(String userid) throws Exception {
        Query query = new Query();
        query.addCriteria(Criteria.where("userid").is(userid));
        CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
        Query queryAccount = new Query();
        queryAccount.addCriteria(Criteria.where("_id").is(userid));
        UserAccount userAccount = mongoTemplate.findOne(queryAccount, UserAccount.class);
        UserVo userVo = new UserVo();
        userVo.setCustomerInfo(customerInfo);
        userVo.setUserAccount(userAccount);
        return userVo;
    }

    /**
     * @方法名：mobileCheck
     * @描述：验证手机号是否重复
     * @创建日期：2018/12/06
     * @作者：ZHANGYING
     * @参数：[mobilenumber]
     * @返回值：void
     */
    @Override
    public void mobileCheck(String id, String mobilenumber) throws Exception {
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("account").is(mobilenumber));
            query.addCriteria(Criteria.where("_id").ne(id));
            List<UserAccount> userAccounts = mongoTemplate.find(query, UserAccount.class);
            if (userAccounts.size() > 0) {
                throw new LogicalException("手机号已经被注册");
            }
        } catch (Exception e) {
            throw new LogicalException(e.getMessage());
        }
    }

    /**
     * @方法名：updUserStatus
     * @描述：更新用户状态
     * @创建日期：2018/12/06
     * @作者：ZHANGYING
     * @参数：[userid, status]
     * @返回值：void
     */
    @Override
    public void updUserStatus(String userid, String status) throws Exception {
        Query query = new Query();
        query.addCriteria(Criteria.where("userid").is(userid));
        CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
        customerInfo.setStatus(status);
        mongoTemplate.save(customerInfo);
        Query queryAccount = new Query();
        queryAccount.addCriteria(Criteria.where("_id").is(userid));
        UserAccount userAccount = mongoTemplate.findOne(queryAccount, UserAccount.class);
        userAccount.setStatus(status);
        mongoTemplate.save(userAccount);
    }

    /**
     * @方法名：setRoleToUser
     * @描述：给用户赋角色
     * @创建日期：2018/12/07
     * @作者：ZHANGYING
     * @参数：[userAccount]
     * @返回值：void
     */
    @Override
    public void setRoleToUser(UserAccount userAccount) throws Exception {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(userAccount.get_id()));
        UserAccount userAccountInfo = mongoTemplate.findOne(query, UserAccount.class);
        if (userAccountInfo != null) {
            userAccountInfo.setRoles(userAccount.getRoles());
            mongoTemplate.save(userAccountInfo);
        }
    }

    /**
     * @方法名：getUserInfo
     * @描述：微信端用获取用户信息
     * @创建日期：2018/12/14
     * @作者：ZHANGYING
     * @参数：[customerInfo]
     * @返回值：void
     */
    @Override
    public UserVo updUserInfo(CustomerInfo customerInfo) throws Exception {
        UserVo userVo = new UserVo();
        Query query = new Query();
        query.addCriteria(Criteria.where("userid").is(customerInfo.getUserid()));
        UserAccount userAccountInfo = mongoTemplate.findOne(query, UserAccount.class);
        if (userAccountInfo != null) {
            //更新账号信息
            userAccountInfo.setAccount(customerInfo.getUserinfo().getMobilenumber());
            userAccountInfo.setPassword(customerInfo.getUserinfo().getMobilenumber());
            userAccountInfo.setIsPassing("1");//资质通过 0：没通过；1：通过
            mongoTemplate.save(userAccountInfo);
            //更新用户信息
            Query queryCusomer = new Query();
            queryCusomer.addCriteria(Criteria.where("userid").is(userAccountInfo.get_id()));
            CustomerInfo upCustomer = mongoTemplate.findOne(queryCusomer, CustomerInfo.class);
            if (upCustomer != null) {
                upCustomer.getUserinfo().setCompanyname(customerInfo.getUserinfo().getCompanyname());//公司名称
                upCustomer.getUserinfo().setCustomername(customerInfo.getUserinfo().getCustomername());//真实姓名
                upCustomer.getUserinfo().setMobilenumber(customerInfo.getUserinfo().getMobilenumber());//联系方式
                mongoTemplate.save(upCustomer);
                userVo.setCustomerInfo(upCustomer);
            } else {
                CustomerInfo cusInfo = new CustomerInfo();
                cusInfo.setType(customerInfo.getType());
                cusInfo.setUserid(userAccountInfo.get_id());
                CustomerInfo.UserInfo userInfo = new CustomerInfo.UserInfo();
                userInfo.setMobilenumber(customerInfo.getUserinfo().getMobilenumber());
                userInfo.setCustomername(customerInfo.getUserinfo().getCustomername());
                userInfo.setCompanyname(customerInfo.getUserinfo().getCompanyname());
                cusInfo.setUserinfo(userInfo);
                mongoTemplate.save(cusInfo);
                userVo.setCustomerInfo(cusInfo);
            }
        }

        userVo.setUserAccount(userAccountInfo);
        return userVo;
//        else {
//            //插入账号信息
//            UserAccount userAccount = new UserAccount();
//            userAccount.setUserid(customerInfo.getUserid());
//            userAccount.setOpenid(customerInfo.getUserid());
//            userAccount.setAccount(customerInfo.getUserinfo().getMobilenumber());
//            userAccount.setPassword(customerInfo.getUserinfo().getMobilenumber());
//            userAccount.setUsertype("1");//账户类型 0:内部;1:外部
//            userAccount.setIsPassing("1");//是否资质通过 0:没通过;1:通过
//            mongoTemplate.save(userAccount);
//            Query queryAcc = new Query();
//            queryAcc.addCriteria(Criteria.where("account").is(customerInfo.getUserinfo().getMobilenumber()));
//            queryAcc.addCriteria(Criteria.where("password").is(customerInfo.getUserinfo().getMobilenumber()));
//            List<UserAccount> userAcclist = mongoTemplate.find(queryAcc,UserAccount.class);
//            if(userAcclist.size() > 0) {
//                CustomerInfo cusInfo = new CustomerInfo();
//                cusInfo.setType(customerInfo.getType());
//                cusInfo.setUserid(userAcclist.get(0).get_id());
//                CustomerInfo.UserInfo userInfo = new CustomerInfo.UserInfo();
//                userInfo.setMobilenumber(customerInfo.getUserinfo().getMobilenumber());
//                userInfo.setCustomername(customerInfo.getUserinfo().getCustomername());
//                userInfo.setCompanyname(customerInfo.getUserinfo().getCompanyname());
//                cusInfo.setUserinfo(userInfo);
//                mongoTemplate.save(cusInfo);
//            }
//        }
    }

    /**
     * @方法名：wxLogin
     * @描述：微信端登录
     * @创建日期：2018/12/14
     * @作者：ZHANGYING
     * @参数：[weChatUserId]
     * @返回值：TokenModel
     */
    @Override
    public TokenModel wxLogin(String weChatUserId) throws Exception {
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("openid").is(weChatUserId));
//            query.addCriteria(Criteria.where("status").is(AuthConstants.DEL_FLAG_NORMAL));
            UserAccount useraccount = mongoTemplate.findOne(query, UserAccount.class);

            if (useraccount != null) {
                TokenModel tokenModel = new TokenModel();
                //存在用户时，获取用户信息，生成token
                tokenModel.setUserId(useraccount.get_id());
                tokenModel.setUserType(useraccount.getUsertype());
                tokenModel.setTenantId(useraccount.getTenantid());
//                tokenService.setToken(tokenModel);
                return tokenModel;
            } else {
                throw new LogicalException("微信用户不在系统中，请先前往个人中心完善个人信息");
            }
        } catch (LogicalException e) {
            throw e;
        }
    }

    /**
     * @方法名：getWxById
     * @描述：微信端根据用户id获取用户信息
     * @创建日期：2018/12/06
     * @作者：ZHANGYING
     * @参数：[userid]
     * @返回值：userVo
     */
    @Override
    public Map<String, Object> getWxById(String userid) throws Exception {
        UserVo userVo = new UserVo();
        Query queryAccount = new Query();
        queryAccount.addCriteria(Criteria.where("openid").is(userid));
        UserAccount userAccount = mongoTemplate.findOne(queryAccount, UserAccount.class);
        CustomerInfo customerInfo = new CustomerInfo();
        if (userAccount != null) {
            Query query = new Query();
            query.addCriteria(Criteria.where("userid").is(userAccount.get_id()));
            customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
            userVo.setCustomerInfo(customerInfo);
            userVo.setUserAccount(userAccount);
        } else {
            userAccount = new UserAccount();
            // 插入账号
            userAccount.setUserid(userid);
            userAccount.setUsertype("1");
            userAccount.setOpenid(userid);
            userAccount.setIsPassing("0");
            mongoTemplate.save(userAccount);
            userVo.setCustomerInfo(customerInfo);
            userVo.setUserAccount(userAccount);
        }
        //生成token
//        TokenModel tokenModel = new TokenModel();
//        tokenModel.setUserId(userAccount.get_id());
//        tokenModel.setUserType(userAccount.getUsertype());
//        tokenModel.setTenantId(userAccount.getTenantid());
//        tokenService.setToken(tokenModel);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("userVo", userVo);
//        result.put("tokenModel", tokenModel);
        return result;
    }

    @Override
    public List<CustomerInfo> getAllCustomerInfo() {
        return mongoTemplate.findAll(CustomerInfo.class);
    }

//    @Override
//    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
//    public List<String> importUser(HttpServletRequest request) throws Exception {
//        try {
//            List<String> Result = new ArrayList<String>();
//            MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
//            File f = null;
//            f = File.createTempFile("tmp", null);
//            file.transferTo(f);
//            ExcelReader reader = ExcelUtil.getReader(f);
//            List<Map<String, Object>> readAll = reader.readAll();
//            int accesscount = 0;
//            int error = 0;
//            for (Map<String, Object> item : readAll) {
//                Query query = new Query();
//                query.addCriteria(Criteria.where("userid").is(item.get("社員ID")));
//                List<CustomerInfo> customerInfos = mongoTemplate.find(query, CustomerInfo.class);
//
//                if (customerInfos.size() > 0) {
//                    customerInfos.get(0).getUserinfo().setCenterid(item.get("centerid").toString());
//                    customerInfos.get(0).getUserinfo().setGroupid(item.get("groupid").toString());
//                    customerInfos.get(0).getUserinfo().setTeamid(item.get("teamid").toString());
//                    customerInfos.get(0).getUserinfo().setCentername(item.get("所属センター").toString());
//                    customerInfos.get(0).getUserinfo().setGroupname(item.get("所属グループ").toString());
//                    customerInfos.get(0).getUserinfo().setTeamname(item.get("所属チーム").toString());
//                    customerInfos.get(0).getUserinfo().setBudgetunit(item.get("予算単位").toString());
//                    customerInfos.get(0).getUserinfo().setPost(item.get("職務").toString());
//                    customerInfos.get(0).getUserinfo().setRank(item.get("ランク").toString());
//                    customerInfos.get(0).getUserinfo().setLaborcontractday(item.get("労働契約締切日").toString());
//                    customerInfos.get(0).getUserinfo().setAnnuallastyear(item.get("去年年休数(残)").toString());
//                    customerInfos.get(0).getUserinfo().setAnnualyear(item.get("今年年休数").toString());
//                    customerInfos.get(0).getUserinfo().setUpgraded(item.get("昇格昇号年月日").toString());
//                    customerInfos.get(0).getUserinfo().setSeatnumber(item.get("口座番号").toString());
//                    List<CustomerInfo.Personal> cupList = new ArrayList<CustomerInfo.Personal>();
//                    CustomerInfo.Personal personal = new CustomerInfo.Personal();
//                    personal.setAfter(item.get("変更前基本工资").toString());
//                    personal.setBefore(item.get("変更前职责工资").toString());
//                    personal.setBasic(item.get("変更后基本工资").toString());
//                    personal.setDuty(item.get("変更后职责工资").toString());
//                    personal.setDate(item.get("給料変更日").toString());
//                    cupList.add(personal);
//                    customerInfos.get(0).getUserinfo().setGridData(cupList);
//                    customerInfos.get(0).getUserinfo().setOldageinsurance(item.get("養老保険基数").toString());
//                    customerInfos.get(0).getUserinfo().setMedicalinsurance(item.get("医療保険基数").toString());
//                    customerInfos.get(0).getUserinfo().setHouseinsurance(item.get("住宅積立金納付基数").toString());
//                    mongoTemplate.save(customerInfos.get(0));
//                }
//
//                accesscount = accesscount + 1;
//            }
//            Result.add("失败数：" + error);
//            Result.add("成功数：" + accesscount);
//            return Result;
//        } catch (Exception e) {
//            throw new LogicalException(e.getMessage());
//        }
//    }
@Override
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
public List<String> importUser(HttpServletRequest request, TokenModel tokenModel) throws Exception {
    try {
        List<CustomerInfo> listVo = new ArrayList<CustomerInfo>();
        List<String> Result = new ArrayList<String>();
        MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
        File f = null;
        f = File.createTempFile("tmp", null);
        file.transferTo(f);
        ExcelReader reader = ExcelUtil.getReader(f);
        List<List<Object>> list = reader.read();
        List<Object> model = new ArrayList<Object>();
        model.add("卡号");
        model.add("姓名");
        model.add("center");
        model.add("group");
        model.add("team");
        model.add("入社时间");
        model.add("职务");
        model.add("Rank");
        model.add("性别");
        model.add("预算编码");
        model.add("生年月日");
        model.add("AD域账号");
        model.add("国籍");
        model.add("民族");
        model.add("户籍");
        model.add("住所");
        model.add("最终毕业学校");
        model.add("专业");
        model.add("是否有工作经验");
        model.add("身份证号码");
        model.add("毕业年月日");
        model.add("最终学位");
        model.add("仕事开始年月日");
        model.add("员工ID");
        model.add("劳动合同类型");
        model.add("年龄");
        model.add("是否独生子女");
//            model.add("今年年休数(残)");
        model.add("今年年休数");
        model.add("升格升号年月日");
        model.add("银行账号");
        model.add("固定期限締切日");
        model.add("変更前基本工资");
        model.add("変更前职责工资");
        model.add("现基本工资");
        model.add("现职责工资");
        model.add("給料変更日");
        model.add("养老保险基数");
        model.add("医疗保险基数");
        model.add("失业保险基数");
        model.add("工伤保险基数");
        model.add("生育保险基数");
        model.add("住房公积金缴纳基数");
        List<Object> key = list.get(0);
//           上传模板与标准模板 校验
        for (int i = 0; i < key.size(); i++) {
            if (!key.get(i).toString().replace("●","").trim().equals(model.get(i))) {
                throw new LogicalException("第" + (i + 1) + "列标题错误，应为" + model.get(i).toString());
            }
        }

        List<Integer> updint = new ArrayList<Integer>();
        //resultInsUpd true:插入 false:更新
        boolean resultInsUpd = true;
        for (int j = 0; j < key.size(); j++) {
            if (key.get(j).toString().trim().contains("●")) {
                resultInsUpd = false;
                updint.add(j);
            }
        }
        int k = 1;
        int accesscount = 0;
        int error = 0;
        if (resultInsUpd) {
            for (int i = 1; i < list.size(); i++) {
                List<CustomerInfo.Personal> cupList = new ArrayList<CustomerInfo.Personal>();
                CustomerInfo.Personal personal = new CustomerInfo.Personal();
                CustomerInfo customerInfo = new CustomerInfo();
                UserAccount ust = new UserAccount();
                CustomerInfo.UserInfo userinfo = new CustomerInfo.UserInfo();
                List<Object> value = list.get(k);
                k++;
                if (value != null && !value.isEmpty()) {
                    //工号
                    if (value.get(0) != null) {
                        userinfo.setJobnumber(Convert.toStr(value.get(0)));
                        Query query = new Query();
                        query.addCriteria(Criteria.where("userinfo.jobnumber").is(userinfo.getJobnumber()));
                        List<CustomerInfo> customerInfoList = new ArrayList<CustomerInfo>();
                        customerInfoList = mongoTemplate.find(query, CustomerInfo.class);
                        if (customerInfoList.size() > 0) {
                            throw new LogicalException("卡号（" + Convert.toStr(value.get(0)) + "）" + "在人员表中已存在，请勿重复填写。");
                        }
                    } else {
                        throw new LogicalException("第" + i + "行 卡号 不能为空，请确认。");
                    }
                    //姓名
                    if (value.get(1) != null) {
                        userinfo.setCustomername(Convert.toStr(value.get(1)));
                        Query query = new Query();
                        query.addCriteria(Criteria.where("userinfo.customername").is(userinfo.getCustomername()));
                        List<CustomerInfo> customerInfoList = new ArrayList<CustomerInfo>();
                        customerInfoList = mongoTemplate.find(query, CustomerInfo.class);
                        if (customerInfoList.size() > 0) {
                            throw new LogicalException("卡号（" + Convert.toStr(value.get(0)) + "）" + "对应的 姓名 在人员表中已存在，请勿重复填写。");
                        }
                    }
//                        //center
//                        if (value.get(2) != null) {
//                            userinfo.setCentername(value.get(2).toString());
//                        }
//                        //group
//                        if (value.get(3) != null) {
//                            userinfo.setGroupname(value.get(3).toString());
//                        }
//                        //team
//                        if (value.get(4) != null) {
////                            userinfo.setTeamname(value.get(4).toString());
//                        }
                    //入社时间
                    if (value.get(5) != null) {
                        userinfo.setEnterday(value.get(5).toString());
                    }
                    //职务
                    if (value.get(6) != null) {
                        String post = value.get(6).toString();
                        if (post != null) {
                            Dictionary dictionary = new Dictionary();
                            dictionary.setValue1(post.trim());
                            dictionary.setType("CW");
                            List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                            if (dictionaryList.size() > 0) {
                                userinfo.setPost(dictionaryList.get(0).getCode());
                            }
                        }
                    }
                    //RANK
                    if (value.get(7) != null) {
                        String rank = value.get(7).toString();
                        if (rank != null) {
                            Dictionary dictionary = new Dictionary();
                            dictionary.setValue1(rank.trim());
                            dictionary.setType("RS");
                            List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                            if (dictionaryList.size() > 0) {
                                userinfo.setRank(dictionaryList.get(0).getCode());
                            }
                        }
                    }
                    //性别
                    if (value.get(8) != null) {
                        String sex = value.get(8).toString();
                        if (sex != null) {
                            Dictionary dictionary = new Dictionary();
                            dictionary.setValue1(sex.trim());
                            dictionary.setType("GT");
                            List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                            if (dictionaryList.size() > 0) {
                                userinfo.setSex(dictionaryList.get(0).getCode());
                            }
                        }
                    }
                    //预算编码
                    if (value.get(9) != null) {
                        //upd_fjl  --修改预算编码值
                        userinfo.setBudgetunit(value.get(9).toString());
//                        String budgetunit = value.get(9).toString();
//                        if (budgetunit != null) {
//                            Dictionary dictionary = new Dictionary();
//                            dictionary.setValue1(budgetunit.trim());
//                            dictionary.setType("JY");
//                            List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
//                            if (dictionaryList.size() > 0) {
//                                userinfo.setBudgetunit(dictionaryList.get(0).getCode());
//                            }
//                        }
                        //upd_fjl  --修改预算编码值
                    }
                    //生年月日
                    if (value.get(10) != null) {
                        userinfo.setBirthday(value.get(10).toString());
                    }
                    //AD域账号
                    if (value.get(11) != null) {
                        userinfo.setAdfield(value.get(11).toString());
                        Query query = new Query();
                        query.addCriteria(Criteria.where("userinfo.adfield").is(userinfo.getAdfield()));
                        List<CustomerInfo> customerInfoList = new ArrayList<CustomerInfo>();
                        customerInfoList = mongoTemplate.find(query, CustomerInfo.class);
                        if (customerInfoList.size() > 0) {
                            throw new LogicalException("AD域账号（" + value.get(11).toString() + "）" + "在人员表中已存在，请勿重复填写。");
                        }else {
//                            UserAccount userAccount = new UserAccount();
                            ust.setAccount(userinfo.getAdfield());
                            ust.setPassword(userinfo.getAdfield());
                            ust.setUsertype("0");
                            query = new Query();
                            query.addCriteria(Criteria.where("account").is(ust.getAccount()));
                            query.addCriteria(Criteria.where("password").is(ust.getPassword()));
                            query.addCriteria(Criteria.where("usertype").is(ust.getUsertype()));
                            List<UserAccount> userAccountlist = mongoTemplate.find(query, UserAccount.class);
                            if (userAccountlist.size() > 0) {
                                throw new LogicalException("卡号（" + Convert.toStr(value.get(0)) + "）" + "对应的 姓名 在人员表中已存在同音的员工，生成登陆账号时会重复，请确认。");
                            }
                        }
                    }
                    //国籍
                    if (value.get(12) != null) {
                        userinfo.setNationality(value.get(12).toString());
                    }
                    //民族
                    if (value.get(13) != null) {
                        userinfo.setNation(value.get(13).toString());
                    }
                    //户籍
                    if (value.get(14) != null) {
                        userinfo.setRegister(value.get(14).toString());
                    }
                    //住所
                    if (value.get(15) != null) {
                        userinfo.setAddress(value.get(15).toString());
                    }
                    //最终毕业学校
                    if (value.get(16) != null) {
                        userinfo.setGraduation(value.get(16).toString());
                    }
                    //专业
                    if (value.get(17) != null) {
                        userinfo.setSpecialty(value.get(17).toString());
                    }
                    //是否有工作经验
                    if (value.get(18) != null) {
                        String experience = value.get(18).toString();
                        if (experience != null) {
                            if (experience.equals("是")) {
                                userinfo.setExperience("0");
                            } else if (experience.equals("否")) {
                                userinfo.setExperience("1");
                            }
                        }
                    }
                    //身份证号码
                    if (value.get(19) != null) {
                        userinfo.setIdnumber(value.get(19).toString());
                    }
                    //毕业年月日
                    if (value.get(20) != null) {
                        userinfo.setGraduationday(value.get(20).toString());
                    }
                    //最终学位
                    if (value.get(21) != null) {
                        String degree = value.get(21).toString();
                        if (degree != null) {
                            Dictionary dictionary = new Dictionary();
                            dictionary.setValue1(degree.trim());
                            dictionary.setType("GT");
                            List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                            if (dictionaryList.size() > 0) {
                                userinfo.setDegree(dictionaryList.get(0).getCode());
                            }
                        }
                    }
                    //仕事开始年月日
                    if (value.get(22) != null) {
                        userinfo.setWorkday(value.get(22).toString());
                    }
                    //员工ID
                    if (value.get(23) != null) {
                        userinfo.setPersonalcode(value.get(23).toString());
                    }
                    //劳动合同类型
                    if (value.get(24) != null) {
                        String laborcontracttype = value.get(24).toString();
                        if (laborcontracttype != null) {
                            if (laborcontracttype.equals("固定时限")) {
                                userinfo.setLaborcontracttype("0");
                            } else if (laborcontracttype.equals("非固定时限")) {
                                userinfo.setLaborcontracttype("1");
                            }
                        }
                    }
                    //年龄
                    if (value.get(25) != null) {
                        userinfo.setAge(value.get(25).toString());
                    }
                    //是否独生子女
                    if (value.get(26) != null) {
                        String children = value.get(26).toString();
                        if (children != null) {
                            if (children.equals("否")) {
                                userinfo.setChildren("0");
                            } else if (children.equals("是")) {
                                userinfo.setChildren("1");
                            }
                        }
                    }
                    //今年年休数(残)
//                        if (value.get(27) != null) {
//                            userinfo.annualyearto(value.get(27).toString());
//                        }
                    //今年年休数
                    if (value.get(27) != null) {
                        userinfo.setAnnualyear(value.get(27).toString());
                    }
                    //升格升号年月日
                    if (value.get(28) != null) {
                        userinfo.setUpgraded(value.get(28).toString());
                    }
                    //银行账号
                    if (value.get(29) != null) {
                        userinfo.setSeatnumber(value.get(29).toString());
                    }
                    //固定期限締切日
                    if (value.get(30) != null) {
                        userinfo.setFixedate(value.get(30).toString());
                    }
                    //変更前基本工资
                    if (value.get(31) != null) {
                        personal.setAfter(value.get(31).toString());
                    }
                    //変更前职责工资
                    if (value.get(32) != null) {
                        personal.setBefore(value.get(32).toString());
                    }
                    //现基本工资
                    if (value.get(33) != null) {
                        personal.setBasic(value.get(33).toString());
                    }
                    //现职责工资
                    if (value.get(34) != null) {
                        personal.setDuty(value.get(34).toString());
                    }
                    //給料変更日
                    if (value.get(35) != null) {
                        personal.setDate(value.get(35).toString());
                    }
                    //养老保险基数
                    if (value.get(36) != null) {
                        userinfo.setYanglaoinsurance(value.get(36).toString());
                    }
                    //医疗保险基数
                    if (value.get(37) != null) {
                        userinfo.setYiliaoinsurance(value.get(37).toString());
                    }
                    //失业保险基数
                    if (value.get(38) != null) {
                        userinfo.setShiyeinsurance(value.get(38).toString());
                    }
                    //工伤保险基数
                    if (value.get(39) != null) {
                        userinfo.setGongshanginsurance(value.get(39).toString());
                    }
                    //生育保险基数
                    if (value.get(40) != null) {
                        userinfo.setShengyuinsurance(value.get(40).toString());
                    }
                    //住房公积金缴纳基数
                    if (value.get(41) != null) {
                        userinfo.setHouseinsurance(value.get(41).toString());
                    }
                }
                cupList.add(personal);
                //如果有工资履历变更，給料変更日不能为空
                if ((!StringUtils.isNullOrEmpty(personal.getAfter()) || !StringUtils.isNullOrEmpty(personal.getBasic()) ||
                        !StringUtils.isNullOrEmpty(personal.getDuty()) || !StringUtils.isNullOrEmpty(personal.getBefore())) &&
                        StringUtils.isNullOrEmpty(personal.getDate())) {
                    throw new LogicalException("卡号（" + Convert.toStr(value.get(0)) + "）" + "的 給料変更日 未填写");
                }
                userinfo.setGridData(cupList);
                customerInfo.setUserinfo(userinfo);
                customerInfo.setType("1");
                customerInfo.setStatus("0");
                customerInfo.getUserinfo().setType("0");
                mongoTemplate.save(ust);
                Query query = new Query();
                query.addCriteria(Criteria.where("account").is(ust.getAccount()));
                query.addCriteria(Criteria.where("password").is(ust.getPassword()));
                List<UserAccount> userAccountlist = mongoTemplate.find(query, UserAccount.class);
                if (userAccountlist.size() > 0) {
                    String _id = userAccountlist.get(0).get_id();
                    customerInfo.setUserid(_id);
                    mongoTemplate.save(customerInfo);
                }
                accesscount = accesscount + 1;
            }
        }
        else {
            for (int i = 1; i < list.size(); i++)
            {
                UserAccount userAccount = new UserAccount();
                List<CustomerInfo.Personal> cupList = new ArrayList<CustomerInfo.Personal>();
                CustomerInfo.Personal personal = new CustomerInfo.Personal();
                CustomerInfo.UserInfo userinfo = new CustomerInfo.UserInfo();
                List<Object> value = list.get(i);
                if (value != null && !value.isEmpty() && value.size()>0)
                {
                    userinfo.setJobnumber(Convert.toStr(value.get(0)));
                    Query query = new Query();
                    query.addCriteria(Criteria.where("userinfo.jobnumber").is(userinfo.getJobnumber()));
                    List<CustomerInfo> customerInfoList = new ArrayList<CustomerInfo>();
                    customerInfoList = mongoTemplate.find(query, CustomerInfo.class);
                    if(customerInfoList.size()>0)
                    {
                        for(int j = 0;j < updint.size();j++)
                        {
                            if(value.size() > updint.get(j))
                            {
                                switch (updint.get(j))
                                {
                                    case 0:
                                        break;
                                    case 1:
                                        if (value.get(1) != null) {
                                            String customername = Convert.toStr(value.get(1));
                                            if (!(customername.equals(customerInfoList.get(0).getUserinfo().getCustomername()))) {
                                                customerInfoList.get(0).getUserinfo().setCustomername(Convert.toStr(value.get(1)));
                                                query = new Query();
                                                query.addCriteria(Criteria.where("userinfo.customername").is(customerInfoList.get(0).getUserinfo().getCustomername()));
                                                List<CustomerInfo> customerInfoLists = new ArrayList<CustomerInfo>();
                                                customerInfoLists = mongoTemplate.find(query, CustomerInfo.class);
                                                if (customerInfoLists.size() > 0) {
                                                    throw new LogicalException("卡号（" + Convert.toStr(value.get(0)) + "）" + "对应的 姓名 在人员表中已存在，请确认。");
                                                }
                                            }
                                        }
                                        break;
                                    case 2:
//                                            customerInfoList.get(0).getUserinfo().setCentername(Convert.toStr(value.get(3)));
                                        break;
                                    case 3:
//                                            customerInfoList.get(0).getUserinfo().setGroupname(Convert.toStr(value.get(4)));
                                        break;
                                    case 4:
//                                            customerInfoList.get(0).getUserinfo().setTeamname(Convert.toStr(value.get(5)));
                                        break;
                                    case 5:
                                        if (value.get(5) != null) {
                                            customerInfoList.get(0).getUserinfo().setEnterday(Convert.toStr(value.get(5)));
                                        }
                                        break;
                                    case 6:
                                        if (value.get(6) != null) {
                                            String post = value.get(6).toString();
                                            if (post != null) {
                                                Dictionary dictionary = new Dictionary();
                                                dictionary.setValue1(post.trim());
                                                dictionary.setType("CW");
                                                List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                                                if (dictionaryList.size() > 0) {
                                                    customerInfoList.get(0).getUserinfo().setPost(dictionaryList.get(0).getCode());
                                                }
                                            }
                                        }
                                        break;
                                    case 7:
                                        if (value.get(7) != null) {
                                            String rank = value.get(7).toString();
                                            if (rank != null) {
                                                Dictionary dictionary = new Dictionary();
                                                dictionary.setValue1(rank.trim());
                                                dictionary.setType("RS");
                                                List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                                                if (dictionaryList.size() > 0) {
                                                    customerInfoList.get(0).getUserinfo().setRank(dictionaryList.get(0).getCode());
                                                }
                                            }
                                        }
                                        break;
                                    case 8:
                                        if (value.get(8) != null) {
                                            String sex = value.get(8).toString();
                                            if (sex != null) {
                                                Dictionary dictionary = new Dictionary();
                                                dictionary.setValue1(sex.trim());
                                                dictionary.setType("GT");
                                                List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                                                if (dictionaryList.size() > 0) {
                                                    customerInfoList.get(0).getUserinfo().setSex(dictionaryList.get(0).getCode());
                                                }
                                            }
                                        }
                                        break;
                                    case 9:
                                        if (value.get(9) != null) {
                                            //upd_fjl  --修改预算编码值
                                            customerInfoList.get(0).getUserinfo().setBudgetunit(value.get(9).toString());
//                                            String budgetunit = value.get(9).toString();
//                                            if (budgetunit != null) {
//                                                Dictionary dictionary = new Dictionary();
//                                                dictionary.setValue1(budgetunit.trim());
//                                                dictionary.setType("JY");
//                                                List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
//                                                if (dictionaryList.size() > 0) {
//                                                    customerInfoList.get(0).getUserinfo().setBudgetunit(dictionaryList.get(0).getCode());
//                                                }
//                                            }
                                            //upd_fjl  --修改预算编码值
                                        }
                                        break;
                                    case 10:
                                        if (value.get(10) != null) {
                                            customerInfoList.get(0).getUserinfo().setBirthday(value.get(10).toString());
                                        }
                                        break;
                                    case 11:
                                        if (value.get(11) != null) {
                                            customerInfoList.get(0).getUserinfo().setAdfield(value.get(11).toString());
                                            query = new Query();
                                            query.addCriteria(Criteria.where("userinfo.adfield").is(customerInfoList.get(0).getUserinfo().getAdfield()));
                                            List<CustomerInfo> customerInfoLists = new ArrayList<CustomerInfo>();
                                            customerInfoLists = mongoTemplate.find(query, CustomerInfo.class);
                                            if (customerInfoLists.size() > 0) {
                                                throw new LogicalException("AD域账号（" + value.get(11).toString() + "）" + "在人员表中已存在，请勿重复填写。");
                                            } else {
                                                userAccount.setAccount(customerInfoList.get(0).getUserinfo().getAdfield());
                                                userAccount.setPassword(customerInfoList.get(0).getUserinfo().getAdfield());
                                                userAccount.setUsertype("0");
                                                query = new Query();
                                                query.addCriteria(Criteria.where("account").is(userAccount.getAccount()));
                                                query.addCriteria(Criteria.where("password").is(userAccount.getPassword()));
                                                query.addCriteria(Criteria.where("usertype").is(userAccount.getUsertype()));
                                                List<UserAccount> userAccountlist = mongoTemplate.find(query, UserAccount.class);
                                                if (userAccountlist.size() > 0) {
                                                    throw new LogicalException("卡号（" + Convert.toStr(value.get(0)) + "）" + "对应的 姓名 在人员表中已存在同音的员工，生成登陆账号时会重复，请确认。");
                                                }
                                            }
                                        }
                                        break;
                                    case 12:
                                        if (value.get(12) != null) {
                                            customerInfoList.get(0).getUserinfo().setNationality(value.get(12).toString());
                                        }
                                        break;
                                    case 13:
                                        if (value.get(13) != null) {
                                            customerInfoList.get(0).getUserinfo().setNation(value.get(13).toString());
                                        }
                                        break;
                                    case 14:
                                        if (value.get(14) != null) {
                                            customerInfoList.get(0).getUserinfo().setRegister(value.get(14).toString());
                                        }
                                        break;
                                    case 15:
                                        if (value.get(15) != null) {
                                            customerInfoList.get(0).getUserinfo().setAddress(value.get(15).toString());
                                        }
                                        break;
                                    case 16:
                                        if (value.get(16) != null) {
                                            customerInfoList.get(0).getUserinfo().setGraduation(value.get(16).toString());
                                        }
                                        break;
                                    case 17:
                                        if (value.get(17) != null) {
                                            customerInfoList.get(0).getUserinfo().setSpecialty(value.get(17).toString());
                                        }
                                        break;
                                    case 18:
                                        if (value.get(18) != null) {
                                            String experience = value.get(18).toString();
                                            if (experience != null) {
                                                if (experience.equals("是")) {
                                                    customerInfoList.get(0).getUserinfo().setExperience("0");
                                                } else if (experience.equals("否")) {
                                                    customerInfoList.get(0).getUserinfo().setExperience("1");
                                                }
                                            }
                                        }
                                        break;
                                    case 19:
                                        if (value.get(19) != null) {
                                            customerInfoList.get(0).getUserinfo().setIdnumber(value.get(19).toString());
                                        }
                                        break;
                                    case 20:
                                        if (value.get(20) != null) {
                                            customerInfoList.get(0).getUserinfo().setGraduationday(value.get(20).toString());
                                        }
                                        break;
                                    case 21:
                                        if (value.get(21) != null) {
                                            String degree = value.get(21).toString();
                                            if (degree != null) {
                                                Dictionary dictionary = new Dictionary();
                                                dictionary.setValue1(degree.trim());
                                                dictionary.setType("GT");
                                                List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                                                if (dictionaryList.size() > 0) {
                                                    customerInfoList.get(0).getUserinfo().setDegree(dictionaryList.get(0).getCode());
                                                }
                                            }
                                        }
                                        break;
                                    case 22:
                                        if (value.get(22) != null) {
                                            customerInfoList.get(0).getUserinfo().setWorkday(value.get(22).toString());
                                        }
                                        break;
                                    case 23:
                                        if (value.get(23) != null) {
                                            customerInfoList.get(0).getUserinfo().setPersonalcode(value.get(23).toString());
                                        }
                                        break;
                                    case 24:
                                        if (value.get(24) != null) {
                                            String laborcontracttype = value.get(24).toString();
                                            if (laborcontracttype != null) {
                                                if (laborcontracttype.equals("固定时限")) {
                                                    customerInfoList.get(0).getUserinfo().setLaborcontracttype("0");
                                                } else if (laborcontracttype.equals("非固定时限")) {
                                                    customerInfoList.get(0).getUserinfo().setLaborcontracttype("1");
                                                }
                                            }
                                        }
                                        break;
                                    case 25:
                                        if (value.get(25) != null) {
                                            customerInfoList.get(0).getUserinfo().setAge(value.get(25).toString());
                                        }
                                        break;
                                    case 26:
                                        if (value.get(26) != null) {
                                            String children = value.get(26).toString();
                                            if (children != null) {
                                                if (children.equals("否")) {
                                                    customerInfoList.get(0).getUserinfo().setChildren("0");
                                                } else if (children.equals("是")) {
                                                    customerInfoList.get(0).getUserinfo().setChildren("1");
                                                }
                                            }
                                        }
                                        break;
                                    case 27:
                                        if (value.get(27) != null) {
                                            customerInfoList.get(0).getUserinfo().setAnnualyear(value.get(27).toString());
                                        }
                                        break;
                                    case 28:
                                        if (value.get(28) != null) {
                                            customerInfoList.get(0).getUserinfo().setUpgraded(value.get(28).toString());
                                        }
                                        break;
                                    case 29:
                                        if (value.get(29) != null) {
                                            customerInfoList.get(0).getUserinfo().setSeatnumber(value.get(29).toString());
                                        }
                                        break;
                                    case 30:
                                        if (value.get(30) != null) {
                                            customerInfoList.get(0).getUserinfo().setFixedate(value.get(30).toString());
                                        }
                                        break;
                                    case 31:
                                        if (value.get(31) != null) {
                                            personal.setAfter(value.get(31).toString());
                                        }
                                        break;
                                    case 32:
                                        if (value.get(32) != null) {
                                            personal.setBefore(value.get(32).toString());
                                        }
                                        break;
                                    case 33:
                                        if (value.get(33) != null) {
                                            personal.setBasic(value.get(33).toString());
                                        }
                                        break;
                                    case 34:
                                        if (value.get(34) != null) {
                                            personal.setDuty(value.get(34).toString());
                                        }
                                        break;
                                    case 35:
                                        if (value.get(35) != null) {
                                            personal.setDate(value.get(35).toString());
                                        }
                                        break;
                                    case 36:
                                        if (value.get(36) != null) {
                                            customerInfoList.get(0).getUserinfo().setYanglaoinsurance(value.get(36).toString());
                                        }
                                        break;
                                    case 37:
                                        if (value.get(37) != null) {
                                            customerInfoList.get(0).getUserinfo().setYiliaoinsurance(value.get(37).toString());
                                        }
                                        break;
                                    case 38:
                                        if (value.get(38) != null) {
                                            customerInfoList.get(0).getUserinfo().setShiyeinsurance(value.get(38).toString());
                                        }
                                        break;
                                    case 39:
                                        if (value.get(39) != null) {
                                            customerInfoList.get(0).getUserinfo().setGongshanginsurance(value.get(39).toString());
                                        }
                                        break;
                                    case 40:
                                        if (value.get(40) != null) {
                                            customerInfoList.get(0).getUserinfo().setShengyuinsurance(value.get(40).toString());
                                        }
                                        break;
                                    case 41:
                                        if (value.get(41) != null) {
                                            customerInfoList.get(0).getUserinfo().setHouseinsurance(value.get(41).toString());
                                        }
                                        break;
                                }
                            }
                        }
                        //判断工资是否有变更履历，如果有添加进来
                        if (customerInfoList.get(0).getUserinfo().getGridData().size() > 0) {
                            cupList.addAll(customerInfoList.get(0).getUserinfo().getGridData());
                        }
                        cupList.add(personal);

                        //如果有工资履历变更，給料変更日不能为空
                        if ((!StringUtils.isNullOrEmpty(personal.getAfter()) || !StringUtils.isNullOrEmpty(personal.getBasic()) ||
                                !StringUtils.isNullOrEmpty(personal.getDuty()) || !StringUtils.isNullOrEmpty(personal.getBefore())) &&
                                StringUtils.isNullOrEmpty(personal.getDate())) {
                            throw new LogicalException("卡号（" + Convert.toStr(value.get(0)) + "）" + "的 給料変更日 未填写");
                        }
                        customerInfoList.get(0).getUserinfo().setGridData(cupList);
                        mongoTemplate.save(customerInfoList.get(0));
                        mongoTemplate.save(userAccount);
                        accesscount = accesscount + 1;
                    }
                    else
                    {
                        throw new LogicalException("卡号（"+ Convert.toStr(value.get(0)) +"）"  + "在人员表中不存在，无法更新，请确认。");
                    }
                }
            }
        }

        Result.add("失败数：" + error);
        Result.add("成功数：" + accesscount);
        return Result;
    } catch (Exception e) {
        throw new LogicalException(e.getMessage());
    }
}
}
