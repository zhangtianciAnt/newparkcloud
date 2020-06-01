package com.nt.service_Org.Impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
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
import java.util.stream.Collectors;

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

            return jsTokenService.createToken(userAccountlist.get(0).get_id(), userAccountlist.get(0).getTenantid(), userAccountlist.get(0).getUsertype(), new ArrayList<String>(), locale, "", roleIds);
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
            if (!StringUtils.isNullOrEmpty(userInfo.getEmail())) {
                queryEmail.addCriteria(Criteria.where("userinfo.email").is(userInfo.getEmail()));
                List<CustomerInfo> qcEmail = mongoTemplate.find(queryEmail, CustomerInfo.class);
                if (qcEmail.size() == 0 || qcEmail.get(0).getUserid().equals(customerInfo.getUserid())) {
                    flg1 = 1;
                } else {
                    throw new LogicalException("邮箱已存在！");
                }
            }
//add-ws-4/28-人员重复check
            Query queryname = new Query();
            if (!StringUtils.isNullOrEmpty(userInfo.getCustomername())) {
                queryname.addCriteria(Criteria.where("userinfo.customername").is(userInfo.getCustomername()));
                List<CustomerInfo> qcname = mongoTemplate.find(queryname, CustomerInfo.class);
                if (qcname.size() == 0 || qcname.get(0).getUserid().equals(customerInfo.getUserid())) {
                    flg4 = 1;
                } else {
                    throw new LogicalException("人名已存在！");
                }
            }

//add-ws-4/28-人员重复check
//个人编码重复check
            Query queryCode = new Query();
            if (!StringUtils.isNullOrEmpty(userInfo.getAdfield())) {
                queryCode.addCriteria(Criteria.where("userinfo.adfield").is(userInfo.getAdfield()));
                List<CustomerInfo> qcAD = mongoTemplate.find(queryCode, CustomerInfo.class);
                if (qcAD.size() == 0 || qcAD.get(0).getUserid().equals(customerInfo.getUserid())) {
                    flg2 = 1;
                } else {
                    throw new LogicalException("AD域账号重复");
                }
            }

//AD域重复check
            Query queryAD = new Query();
            if (!StringUtils.isNullOrEmpty(userInfo.getPersonalcode())) {
                queryAD.addCriteria(Criteria.where("userinfo.personalcode").is(userInfo.getPersonalcode()));
                List<CustomerInfo> qcCode = mongoTemplate.find(queryAD, CustomerInfo.class);
                if (qcCode.size() == 0 || qcCode.get(0).getUserid().equals(customerInfo.getUserid())) {
                    flg3 = 1;
                } else {
                    throw new LogicalException("个人编码重复");
                }
            }
            if (flg1 == 1 && flg2 == 1 && flg3 == 1 && flg4 == 1) {
                customerInfo.setUserid(_id);
//                ADD_FJL_05/21   --添加降序
                List<CustomerInfo.Personal> cupList = customerInfo.getUserinfo().getGridData();
                //去除Invalid date的数据
                cupList = cupList.stream().filter(item1 -> (!item1.getDate().equals("Invalid date") || item1.getDate() != null)).collect(Collectors.toList());
                cupList = cupList.stream().sorted(Comparator.comparing(CustomerInfo.Personal::getDate).reversed()).collect(Collectors.toList());
                userInfo.setGridData(cupList);
//                if(userInfo.getGridData().size() > 0){
                userInfo.setBasic(userInfo.getGridData().get(0).getBasic());
                userInfo.setDuty(userInfo.getGridData().get(0).getDuty());
//                }
//                ADD_FJL_05/21   --添加降序
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
    public List<CustomerInfo> getAccountCustomer(String orgid, String orgtype, TokenModel tokenModel) throws Exception {
        Query query = new Query();
//        if (StrUtil.isNotBlank(orgid)) {
//            query.addCriteria(new Criteria().orOperator(Criteria.where("userinfo.centerid").is(orgid),
//                    Criteria.where("userinfo.groupid").is(orgid), Criteria.where("userinfo.teamid").is(orgid)));
//        }
        //根据登陆用户id查看人员信息
        List<CustomerInfo> customerInfos = new ArrayList<CustomerInfo>();
        if (!"5e78fefff1560b363cdd6db7".equals(tokenModel.getUserId()) && !"5e78b22c4e3b194874180f5f".equals(tokenModel.getUserId()) && !"5e78b2284e3b194874180f47".equals(tokenModel.getUserId())
                && !"5e78b2034e3b194874180e37".equals(tokenModel.getUserId()) && !"5e78b17ef3c8d71e98a2aa30".equals(tokenModel.getUserId())) {
            query.addCriteria(Criteria.where("userid").is(tokenModel.getUserId()));
            List<CustomerInfo> CustomerInfolist = mongoTemplate.find(query, CustomerInfo.class);
            query = new Query();
            if (CustomerInfolist.size() > 0) {
                if (StrUtil.isNotBlank(CustomerInfolist.get(0).getUserinfo().getTeamid())) {
                    query.addCriteria(Criteria.where("userinfo.teamid").is(CustomerInfolist.get(0).getUserinfo().getTeamid()));
                } else if (StrUtil.isNotBlank(CustomerInfolist.get(0).getUserinfo().getGroupid())) {
                    query.addCriteria(Criteria.where("userinfo.groupid").is(CustomerInfolist.get(0).getUserinfo().getGroupid()));
                } else if (StrUtil.isNotBlank(CustomerInfolist.get(0).getUserinfo().getCenterid())) {
                    query.addCriteria(Criteria.where("userinfo.centerid").is(CustomerInfolist.get(0).getUserinfo().getCenterid()));
                }

                customerInfos.addAll(mongoTemplate.find(query, CustomerInfo.class));

                if (CustomerInfolist.get(0).getUserinfo().getOtherorgs() != null && CustomerInfolist.get(0).getUserinfo().getOtherorgs().size() > 0) {
                    for (CustomerInfo.OtherOrgs itemO : CustomerInfolist.get(0).getUserinfo().getOtherorgs()) {
                        query = new Query();

                        if (StrUtil.isNotBlank(itemO.getTeamid())) {
                            query.addCriteria(Criteria.where("userinfo.teamid").is(itemO.getTeamid()));
                        } else if (StrUtil.isNotBlank(itemO.getGroupid())) {
                            query.addCriteria(Criteria.where("userinfo.groupid").is(itemO.getGroupid()));
                        } else if (StrUtil.isNotBlank(itemO.getCenterid())) {
                            query.addCriteria(Criteria.where("userinfo.centerid").is(itemO.getCenterid()));
                        }

                        customerInfos.addAll(mongoTemplate.find(query, CustomerInfo.class));
                    }
                }
            }
        } else {
            query = new Query();
            customerInfos.addAll(mongoTemplate.find(query, CustomerInfo.class));
        }


        return customerInfos;
    }

    @Override
    public List<CustomerInfo> getAccountCustomer2(String orgid, String orgtype, TokenModel tokenModel) throws Exception {
        Query query = new Query();
        if (StrUtil.isNotBlank(orgid)) {
            query.addCriteria(new Criteria().orOperator(Criteria.where("userinfo.centerid").is(orgid),
                    Criteria.where("userinfo.groupid").is(orgid), Criteria.where("userinfo.teamid").is(orgid)));
        }
        List<CustomerInfo> customerInfos = mongoTemplate.find(query, CustomerInfo.class);
        for (CustomerInfo item : customerInfos) {
            item.getUserinfo().setIdnumber("");
            item.getUserinfo().setPassport("");
            item.getUserinfo().setSecurity("");
            item.getUserinfo().setHousefund("");
            item.getUserinfo().setAddress("");
            item.getUserinfo().setMobilenumber("");
            item.getUserinfo().setPhone("");
//            item.getUserinfo().setExtension("");
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
//            item.getUserinfo().setResignation_date("");
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
//        UPD_FJL_2020/05/12 --修改人员导入
//        try {
//            List<CustomerInfo> listVo = new ArrayList<CustomerInfo>();
            List<String> Result = new ArrayList<String>();
            MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
            File f = null;
            f = File.createTempFile("tmp", null);
            file.transferTo(f);
            ExcelReader reader = ExcelUtil.getReader(f);
//        List<List<Object>> list = reader.read();
            List<Map<String, Object>> readAll = reader.readAll();
//            List<Object> model = new ArrayList<Object>();
//        model.add("卡号");
//        model.add("姓名");
//        model.add("center");
//        model.add("group");
//        model.add("team");
//        model.add("入社时间");
//        model.add("职务");
//        model.add("Rank");
//        model.add("性别");
//        model.add("预算编码");
//        model.add("生年月日");
//        model.add("AD域账号");
//        model.add("国籍");
//        model.add("民族");
//        model.add("户籍");
//        model.add("住所");
//        model.add("最终毕业学校");
//        model.add("专业");
//        model.add("是否有工作经验");
//        model.add("身份证号码");
//        model.add("毕业年月日");
//        model.add("最终学位");
//        model.add("仕事开始年月日");
//        model.add("员工ID");
//        model.add("劳动合同类型");
//        model.add("年龄");
//        model.add("是否独生子女");
////            model.add("今年年休数(残)");
//        model.add("今年年休数");
//        model.add("升格升号年月日");
//        model.add("银行账号");
//        model.add("固定期限締切日");
//        model.add("変更前基本工资");
//        model.add("変更前职责工资");
//        model.add("现基本工资");
//        model.add("现职责工资");
//        model.add("給料変更日");
//        model.add("养老保险基数");
//        model.add("医疗保险基数");
//        model.add("失业保险基数");
//        model.add("工伤保险基数");
//        model.add("生育保险基数");
//        model.add("住房公积金缴纳基数");
//        List<Object> key = list.get(0);
//           上传模板与标准模板 校验
//        for (int i = 0; i < key.size(); i++) {
//            if (!key.get(i).toString().replace("●","").trim().equals(model.get(i))) {
//                throw new LogicalException("第" + (i + 1) + "列标题错误，应为" + model.get(i).toString());
//            }
//        }

//            List<Integer> updint = new ArrayList<Integer>();
            //resultInsUpd true:插入 false:更新
            boolean resultInsUpd = true;
            Map<String, Object> key = readAll.get(0);

            if (key.keySet().toString().trim().contains("●")) {
                //只要有一个●就走更新
                resultInsUpd = false;
            }
            int k = 1;
            int accesscount = 0;
            int error = 0;
            if (resultInsUpd) {
                for (Map<String, Object> item : readAll) {
                    List<CustomerInfo.Personal> cupList = new ArrayList<CustomerInfo.Personal>();
                    List<CustomerInfo.Personal> cupList1 = new ArrayList<CustomerInfo.Personal>();
                    List<CustomerInfo.Personal> cupList2 = new ArrayList<CustomerInfo.Personal>();
                    List<CustomerInfo.Personal> cupList3 = new ArrayList<CustomerInfo.Personal>();
                    List<CustomerInfo.Personal> cupList4 = new ArrayList<CustomerInfo.Personal>();
                    List<CustomerInfo.Personal> cupList5 = new ArrayList<CustomerInfo.Personal>();
                    List<CustomerInfo.Personal> cupList6 = new ArrayList<CustomerInfo.Personal>();
                    List<CustomerInfo.Personal> cupList7 = new ArrayList<CustomerInfo.Personal>();
                    List<CustomerInfo.Personal> cupList8 = new ArrayList<CustomerInfo.Personal>();
                    CustomerInfo.Personal personal = new CustomerInfo.Personal();
                    CustomerInfo.Personal personal1 = new CustomerInfo.Personal();
                    CustomerInfo.Personal personal2 = new CustomerInfo.Personal();
                    CustomerInfo.Personal personal3 = new CustomerInfo.Personal();
                    CustomerInfo.Personal personal4 = new CustomerInfo.Personal();
                    CustomerInfo.Personal personal5 = new CustomerInfo.Personal();
                    CustomerInfo.Personal personal6 = new CustomerInfo.Personal();
                    CustomerInfo.Personal personal7 = new CustomerInfo.Personal();
                    CustomerInfo.Personal personal8 = new CustomerInfo.Personal();
                    CustomerInfo customerInfo = new CustomerInfo();
                    UserAccount ust = new UserAccount();
                    CustomerInfo.UserInfo userinfo = new CustomerInfo.UserInfo();
                    k++;
                    //卡号
                    if (item.get("卡号") != null) {
                        userinfo.setJobnumber(Convert.toStr(item.get("卡号")));
                        Query query = new Query();
                        query.addCriteria(Criteria.where("userinfo.jobnumber").is(userinfo.getJobnumber()));
                        List<CustomerInfo> customerInfoList = new ArrayList<CustomerInfo>();
                        customerInfoList = mongoTemplate.find(query, CustomerInfo.class);
                        if (customerInfoList.size() > 0) {
                            throw new LogicalException("卡号（" + Convert.toStr(item.get("卡号")) + "）" + "在人员表中已存在，请勿重复填写。");
                        }
                    } else {
                        throw new LogicalException("第" + k + "行 卡号 不能为空，请确认。");
                    }
                    //姓名
                    if (item.get("姓名") != null) {
                        userinfo.setCustomername(Convert.toStr(item.get("姓名")));
                        Query query = new Query();
                        query.addCriteria(Criteria.where("userinfo.customername").is(userinfo.getCustomername()));
                        List<CustomerInfo> customerInfoList = new ArrayList<CustomerInfo>();
                        customerInfoList = mongoTemplate.find(query, CustomerInfo.class);
                        if (customerInfoList.size() > 0) {
                            throw new LogicalException("卡号（" + Convert.toStr(item.get("卡号")) + "）" + "对应的 姓名 在人员表中已存在，请勿重复填写。");
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
                    if (item.get("入社时间") != null) {
                        userinfo.setEnterday(item.get("入社时间").toString());
                    }
                    //职务
                    if (item.get("职务") != null) {
                        String post = item.get("职务").toString();
                        if (post != null) {
                            Dictionary dictionary = new Dictionary();
                            dictionary.setValue1(post.trim());
                            dictionary.setType("CW");
                            List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                            if (dictionaryList.size() > 0) {
                                userinfo.setPost(dictionaryList.get(0).getCode());
                                personal1.setDate(DateUtil.format(new Date(), "YYYY-MM-dd"));
                                personal1.setBasic(dictionaryList.get(0).getCode());
                            }
                        }
                    }
                    //RANK
                    if (item.get("Rank") != null) {
                        String rank = item.get("Rank").toString();
                        if (rank != null) {
                            Dictionary dictionary = new Dictionary();
                            dictionary.setValue1(rank.trim());
                            dictionary.setType("RS");
                            List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                            if (dictionaryList.size() > 0) {
                                userinfo.setRank(dictionaryList.get(0).getCode());
                                personal2.setDate(DateUtil.format(new Date(), "YYYY-MM-dd"));
                                personal2.setBasic(dictionaryList.get(0).getCode());
                            }
                        }
                    }
                    //性别
                    if (item.get("性别") != null) {
                        String sex = item.get("性别").toString();
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
                    if (item.get("预算编码") != null) {
                        //upd_fjl  --修改预算编码值
                        userinfo.setBudgetunit(item.get("预算编码").toString());
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
                    if (item.get("生年月日") != null) {
                        userinfo.setBirthday(item.get("生年月日").toString());
                    }
                    //AD域账号
                    if (item.get("AD域账号") != null) {
                        userinfo.setAdfield(item.get("AD域账号").toString());
                        Query query = new Query();
                        query.addCriteria(Criteria.where("userinfo.adfield").is(userinfo.getAdfield()));
                        List<CustomerInfo> customerInfoList = new ArrayList<CustomerInfo>();
                        customerInfoList = mongoTemplate.find(query, CustomerInfo.class);
                        if (customerInfoList.size() > 0) {
                            throw new LogicalException("卡号（" + item.get("卡号").toString() + "）" + "对应的 AD域账号 在人员表中已存在，请勿重复填写。");
                        } else {
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
                                throw new LogicalException("卡号（" + Convert.toStr(item.get("卡号")) + "）" + "对应的 姓名 在人员表中已存在同音的员工，生成登陆账号时会重复，请确认。");
                            }
                        }
                    }
                    //国籍
                    if (item.get("国籍") != null) {
                        userinfo.setNationality(item.get("国籍").toString());
                    }
                    //民族
                    if (item.get("民族") != null) {
                        userinfo.setNation(item.get("民族").toString());
                    }
                    //户籍
                    if (item.get("户籍") != null) {
                        userinfo.setRegister(item.get("户籍").toString());
                    }
                    //住所
                    if (item.get("住所") != null) {
                        userinfo.setAddress(item.get("住所").toString());
                    }
                    //最终毕业学校
                    if (item.get("最终毕业学校") != null) {
                        userinfo.setGraduation(item.get("最终毕业学校").toString());
                    }
                    //专业
                    if (item.get("专业") != null) {
                        userinfo.setSpecialty(item.get("专业").toString());
                    }
                    //是否有工作经验
                    if (item.get("是否有工作经验") != null) {
                        String experience = item.get("是否有工作经验").toString();
                        if (experience != null) {
                            if (experience.equals("是")) {
                                userinfo.setExperience("0");
                            } else if (experience.equals("否")) {
                                userinfo.setExperience("1");
                            }
                        }
                    }
                    //身份证号码
                    if (item.get("身份证号码") != null) {
                        userinfo.setIdnumber(item.get("身份证号码").toString());
                    }
                    //毕业年月日
                    if (item.get("毕业年月日") != null) {
                        userinfo.setGraduationday(item.get("毕业年月日").toString());
                    }
                    //最终学位
                    if (item.get("最终学位") != null) {
                        String degree = item.get("最终学位").toString();
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
                    if (item.get("仕事开始年月日") != null) {
                        userinfo.setWorkday(item.get("仕事开始年月日").toString());
                    }
                    //员工ID
                    if (item.get("员工ID") != null) {
                        userinfo.setPersonalcode(item.get("员工ID").toString());
                    }
                    //劳动合同类型
                    if (item.get("劳动合同类型") != null) {
                        String laborcontracttype = item.get("劳动合同类型").toString();
                        if (laborcontracttype != null) {
                            if (laborcontracttype.equals("固定时限")) {
                                userinfo.setLaborcontracttype("0");
                            } else if (laborcontracttype.equals("非固定时限")) {
                                userinfo.setLaborcontracttype("1");
                            }
                        }
                    }
                    //年龄
                    if (item.get("年龄") != null) {
                        userinfo.setAge(item.get("年龄").toString());
                    }
                    //是否独生子女
                    if (item.get("是否独生子女") != null) {
                        String children = item.get("是否独生子女").toString();
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
                    if (item.get("今年年休数") != null) {
                        userinfo.setAnnualyear(item.get("今年年休数").toString());
                    }
                    //升格升号年月日
                    if (item.get("升格升号年月日") != null) {
                        userinfo.setUpgraded(item.get("升格升号年月日").toString());
                    }
                    //银行账号
                    if (item.get("银行账号") != null) {
                        userinfo.setSeatnumber(item.get("银行账号").toString());
                    }
                    //固定期限締切日
                    if (item.get("固定期限締切日") != null) {
                        userinfo.setFixedate(item.get("固定期限締切日").toString());
                    }
                    //変更前基本工资
//                    if (item.get("変更前基本工资") != null) {
//                        personal.setAfter(item.get("変更前基本工资").toString());
//                    }
//                    //変更前职责工资
//                    if (item.get("変更前职责工资") != null) {
//                        personal.setBefore(item.get("変更前职责工资").toString());
//                    }
                    //现基本工资
                    if (item.get("现基本工资") != null) {
                        personal.setBasic(item.get("现基本工资").toString());
                        userinfo.setBasic(item.get("现基本工资").toString());
                        personal.setDuty("0");
                    }
                    //现职责工资
                    if (item.get("现职责工资") != null) {
                        if (StringUtils.isNullOrEmpty(personal.getBasic())) {
                            personal.setBasic("0");
                        }
                        personal.setDuty(item.get("现职责工资").toString());
                        userinfo.setDuty(item.get("现职责工资").toString());
                    }
                    //給料変更日
                    if (item.get("給料変更日") != null && item.get("給料変更日").toString().length() >= 10) {
//                        personal.setDate(item.get("給料変更日").toString());
                        String dateSubs = item.get("給料変更日").toString().substring(0, 10);
                        personal.setDate(dateSubs);
                    }
                    //养老保险基数
                    if (item.get("养老保险基数") != null) {
                        userinfo.setYanglaoinsurance(item.get("养老保险基数").toString());
                        personal3.setDate(DateUtil.format(new Date(), "YYYY-MM-dd"));
                        personal3.setBasic(item.get("养老保险基数").toString());
                    }
                    //医疗保险基数
                    if (item.get("医疗保险基数") != null) {
                        userinfo.setYiliaoinsurance(item.get("医疗保险基数").toString());
                        personal4.setDate(DateUtil.format(new Date(), "YYYY-MM-dd"));
                        personal4.setBasic(item.get("医疗保险基数").toString());
                    }
                    //失业保险基数
                    if (item.get("失业保险基数") != null) {
                        userinfo.setShiyeinsurance(item.get("失业保险基数").toString());
                        personal5.setDate(DateUtil.format(new Date(), "YYYY-MM-dd"));
                        personal5.setBasic(item.get("失业保险基数").toString());
                    }
                    //工伤保险基数
                    if (item.get("工伤保险基数") != null) {
                        userinfo.setGongshanginsurance(item.get("工伤保险基数").toString());
                        personal6.setDate(DateUtil.format(new Date(), "YYYY-MM-dd"));
                        personal6.setBasic(item.get("工伤保险基数").toString());
                    }
                    //生育保险基数
                    if (item.get("生育保险基数") != null) {
                        userinfo.setShengyuinsurance(item.get("生育保险基数").toString());
                        personal7.setDate(DateUtil.format(new Date(), "YYYY-MM-dd"));
                        personal7.setBasic(item.get("生育保险基数").toString());
                    }
                    //住房公积金缴纳基数
                    if (item.get("住房公积金缴纳基数") != null) {
                        userinfo.setHouseinsurance(item.get("住房公积金缴纳基数").toString());
                        personal8.setDate(DateUtil.format(new Date(), "YYYY-MM-dd"));
                        personal8.setBasic(item.get("住房公积金缴纳基数").toString());
                    }
//                }
                    cupList.add(personal);
                    cupList1.add(personal1);
                    cupList2.add(personal2);
                    cupList3.add(personal3);
                    cupList4.add(personal4);
                    cupList5.add(personal5);
                    cupList6.add(personal6);
                    cupList7.add(personal7);
                    cupList8.add(personal8);
                    //如果有工资履历变更，給料変更日不能为空
                    if ((!StringUtils.isNullOrEmpty(personal.getBasic()) || !StringUtils.isNullOrEmpty(personal.getDuty())) &&
                            StringUtils.isNullOrEmpty(personal.getDate())) {
                        throw new LogicalException("卡号（" + Convert.toStr(item.get(0)) + "）" + "的 給料変更日 未填写");
                    }
                    //如果有給料変更日，工资履历不能为空
                    if (!StringUtils.isNullOrEmpty(personal.getDate())) {
                        if (StringUtils.isNullOrEmpty(personal.getBasic()) && StringUtils.isNullOrEmpty(personal.getDuty())) {
                            throw new LogicalException("卡号（" + Convert.toStr(item.get("卡号")) + "）" + "的 工资履历 未填写");
                        }
                    }
                    userinfo.setGridData(cupList);
                    userinfo.setPostData(cupList1);
                    userinfo.setRankData(cupList2);
                    userinfo.setOldageData(cupList3);
                    userinfo.setMedicalData(cupList4);
                    userinfo.setSyeData(cupList5);
                    userinfo.setGsData(cupList6);
                    userinfo.setSyuData(cupList7);
                    userinfo.setHouseData(cupList8);
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
            } else {
                for (Map<String, Object> item : readAll) {
                    UserAccount userAccount = new UserAccount();
                    List<CustomerInfo.Personal> cupList = new ArrayList<CustomerInfo.Personal>();
                    List<CustomerInfo.Personal> cupList1 = new ArrayList<CustomerInfo.Personal>();
                    List<CustomerInfo.Personal> cupList2 = new ArrayList<CustomerInfo.Personal>();
                    List<CustomerInfo.Personal> cupList3 = new ArrayList<CustomerInfo.Personal>();
                    List<CustomerInfo.Personal> cupList4 = new ArrayList<CustomerInfo.Personal>();
                    List<CustomerInfo.Personal> cupList5 = new ArrayList<CustomerInfo.Personal>();
                    List<CustomerInfo.Personal> cupList6 = new ArrayList<CustomerInfo.Personal>();
                    List<CustomerInfo.Personal> cupList7 = new ArrayList<CustomerInfo.Personal>();
                    List<CustomerInfo.Personal> cupList8 = new ArrayList<CustomerInfo.Personal>();
                    CustomerInfo.Personal personal = new CustomerInfo.Personal();
                    CustomerInfo.Personal personal1 = new CustomerInfo.Personal();
                    CustomerInfo.Personal personal2 = new CustomerInfo.Personal();
                    CustomerInfo.Personal personal3 = new CustomerInfo.Personal();
                    CustomerInfo.Personal personal4 = new CustomerInfo.Personal();
                    CustomerInfo.Personal personal5 = new CustomerInfo.Personal();
                    CustomerInfo.Personal personal6 = new CustomerInfo.Personal();
                    CustomerInfo.Personal personal7 = new CustomerInfo.Personal();
                    CustomerInfo.Personal personal8 = new CustomerInfo.Personal();
                    Query query = new Query();
                    query.addCriteria(Criteria.where("userinfo.jobnumber").is(item.get("卡号")));
                    List<CustomerInfo> customerInfoList = mongoTemplate.find(query, CustomerInfo.class);
                    k++;
                    if (customerInfoList.size() > 0) {
                        if (item.get("姓名●") != null) {
                            String customername = Convert.toStr(item.get("姓名●"));
                            if (!(customername.equals(customerInfoList.get(0).getUserinfo().getCustomername()))) {
                                customerInfoList.get(0).getUserinfo().setCustomername(Convert.toStr(item.get("姓名●")));
                                query = new Query();
                                query.addCriteria(Criteria.where("userinfo.customername").is(customerInfoList.get(0).getUserinfo().getCustomername()));
                                List<CustomerInfo> customerInfoLists = new ArrayList<CustomerInfo>();
                                customerInfoLists = mongoTemplate.find(query, CustomerInfo.class);
                                if (customerInfoLists.size() > 0) {
                                    throw new LogicalException("卡号（" + Convert.toStr(item.get("卡号")) + "）" + "对应的 姓名 在人员表中已存在，请确认。");
                                }
                            }
                        }
                        //center
//                                  customerInfoList.get(0).getUserinfo().setCentername(Convert.toStr(value.get(3)));
                        //group
//                                  customerInfoList.get(0).getUserinfo().setGroupname(Convert.toStr(value.get(4)));
                        //team
//                                  customerInfoList.get(0).getUserinfo().setTeamname(Convert.toStr(value.get(5)));

                        if (item.get("入社时间●") != null) {
                            customerInfoList.get(0).getUserinfo().setEnterday(Convert.toStr(item.get("入社时间●")));
                        }
                        if (item.get("职务●") != null) {
                            String post = item.get("职务●").toString();
                            if (post != null) {
                                Dictionary dictionary = new Dictionary();
                                dictionary.setValue1(post.trim());
                                dictionary.setType("CW");
                                List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                                if (dictionaryList.size() > 0) {
                                    customerInfoList.get(0).getUserinfo().setPost(dictionaryList.get(0).getCode());
                                    personal1.setDate(DateUtil.format(new Date(), "YYYY-MM-dd"));
                                    personal1.setBasic(dictionaryList.get(0).getCode());
                                }
                            }
                        }
                        if (item.get("Rank●") != null) {
                            String rank = item.get("Rank●").toString();
                            if (rank != null) {
                                Dictionary dictionary = new Dictionary();
                                dictionary.setValue1(rank.trim());
                                dictionary.setType("RS");
                                List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                                if (dictionaryList.size() > 0) {
                                    customerInfoList.get(0).getUserinfo().setRank(dictionaryList.get(0).getCode());
                                    personal2.setDate(DateUtil.format(new Date(), "YYYY-MM-dd"));
                                    personal2.setBasic(dictionaryList.get(0).getCode());
                                }
                            }
                        }
                        if (item.get("性别●") != null) {
                            String sex = item.get("性别●").toString();
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
                        if (item.get("预算编码●") != null) {
                            customerInfoList.get(0).getUserinfo().setBudgetunit(item.get("预算编码●").toString());
                        }
                        if (item.get("生年月日●") != null) {
                            customerInfoList.get(0).getUserinfo().setBirthday(item.get("生年月日●").toString());
                        }
                        if (item.get("AD域账号●") != null) {
                            customerInfoList.get(0).getUserinfo().setAdfield(item.get("AD域账号●").toString());
                            query = new Query();
                            query.addCriteria(Criteria.where("userinfo.adfield").is(customerInfoList.get(0).getUserinfo().getAdfield()));
                            List<CustomerInfo> customerInfoLists = new ArrayList<CustomerInfo>();
                            customerInfoLists = mongoTemplate.find(query, CustomerInfo.class);
                            if (customerInfoLists.size() > 0) {
                                throw new LogicalException("AD域账号（" + item.get("AD域账号●").toString() + "）" + "在人员表中已存在，请勿重复填写。");
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
                                    throw new LogicalException("卡号（" + Convert.toStr(item.get("卡号")) + "）" + "对应的 姓名 在人员表中已存在同音的员工，生成登陆账号时会重复，请确认。");
                                }
                            }
                        }
                        if (item.get("国籍●") != null) {
                            customerInfoList.get(0).getUserinfo().setNationality(item.get("国籍●").toString());
                        }
                        if (item.get("民族●") != null) {
                            customerInfoList.get(0).getUserinfo().setNation(item.get("民族●").toString());
                        }
                        if (item.get("户籍●") != null) {
                            customerInfoList.get(0).getUserinfo().setRegister(item.get("户籍●").toString());
                        }
                        if (item.get("住所●") != null) {
                            customerInfoList.get(0).getUserinfo().setAddress(item.get("住所●").toString());
                        }
                        if (item.get("最终毕业学校●") != null) {
                            customerInfoList.get(0).getUserinfo().setGraduation(item.get("最终毕业学校●").toString());
                        }
                        if (item.get("专业●") != null) {
                            customerInfoList.get(0).getUserinfo().setSpecialty(item.get("专业●").toString());
                        }
                        if (item.get("是否有工作经验●") != null) {
                            String experience = item.get("是否有工作经验●").toString();
                            if (experience != null) {
                                if (experience.equals("是")) {
                                    customerInfoList.get(0).getUserinfo().setExperience("0");
                                } else if (experience.equals("否")) {
                                    customerInfoList.get(0).getUserinfo().setExperience("1");
                                }
                            }
                        }
                        if (item.get("身份证号码●") != null) {
                            customerInfoList.get(0).getUserinfo().setIdnumber(item.get("身份证号码●").toString());
                        }
                        if (item.get("毕业年月日●") != null) {
                            customerInfoList.get(0).getUserinfo().setGraduationday(item.get("毕业年月日●").toString());
                        }
                        if (item.get("最终学位●") != null) {
                            String degree = item.get("最终学位●").toString();
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
                        if (item.get("仕事开始年月日●") != null) {
                            customerInfoList.get(0).getUserinfo().setWorkday(item.get("仕事开始年月日●").toString());
                        }
                        if (item.get("员工ID●") != null) {
                            customerInfoList.get(0).getUserinfo().setPersonalcode(item.get("员工ID●").toString());
                        }
                        if (item.get("劳动合同类型●") != null) {
                            String laborcontracttype = item.get("劳动合同类型●").toString();
                            if (laborcontracttype != null) {
                                if (laborcontracttype.equals("固定时限●")) {
                                    customerInfoList.get(0).getUserinfo().setLaborcontracttype("0");
                                } else if (laborcontracttype.equals("非固定时限●")) {
                                    customerInfoList.get(0).getUserinfo().setLaborcontracttype("1");
                                }
                            }
                        }
                        if (item.get("年龄●") != null) {
                            customerInfoList.get(0).getUserinfo().setAge(item.get("年龄●").toString());
                        }
                        if (item.get("是否独生子女●") != null) {
                            String children = item.get("是否独生子女●").toString();
                            if (children != null) {
                                if (children.equals("否")) {
                                    customerInfoList.get(0).getUserinfo().setChildren("0");
                                } else if (children.equals("是")) {
                                    customerInfoList.get(0).getUserinfo().setChildren("1");
                                }
                            }
                        }
                        if (item.get("今年年休数●") != null) {
                            customerInfoList.get(0).getUserinfo().setAnnualyear(item.get("今年年休数●").toString());
                        }
                        if (item.get("升格升号年月日●") != null) {
                            customerInfoList.get(0).getUserinfo().setUpgraded(item.get("升格升号年月日●").toString());
                        }
                        if (item.get("银行账号●") != null) {
                            customerInfoList.get(0).getUserinfo().setSeatnumber(item.get("银行账号●").toString());
                        }
                        if (item.get("固定期限締切日●") != null) {
                            customerInfoList.get(0).getUserinfo().setFixedate(item.get("固定期限締切日●").toString());
                        }
//                        if (item.get("変更前基本工资●") != null) {
//                            personal.setAfter(item.get("変更前基本工资●").toString());
//                        }
//                        if (item.get("変更前职责工资●") != null) {
//                            personal.setBefore(item.get("変更前职责工资●").toString());
//                        }
                        if (item.get("现基本工资●") != null) {
                            personal.setBasic(item.get("现基本工资●").toString());
//                            customerInfoList.get(0).getUserinfo().setBasic(item.get("现基本工资●").toString());
                        }
                        if (item.get("现职责工资●") != null) {
                            personal.setDuty(item.get("现职责工资●").toString());
//                            customerInfoList.get(0).getUserinfo().setDuty(item.get("现职责工资●").toString());
                        }
                        if (item.get("給料変更日●") != null && item.get("給料変更日●").toString().length() >= 10) {
//                            personal.setDate(item.get("給料変更日●").toString());
                            String dateSubs = item.get("給料変更日●").toString().substring(0, 10);
                            personal.setDate(dateSubs);
                        }
                        if (item.get("养老保险基数●") != null) {
                            customerInfoList.get(0).getUserinfo().setYanglaoinsurance(item.get("养老保险基数●").toString());
                            personal3.setDate(DateUtil.format(new Date(), "YYYY-MM-dd"));
                            personal3.setBasic(item.get("养老保险基数●").toString());
                        }
                        if (item.get("医疗保险基数●") != null) {
                            customerInfoList.get(0).getUserinfo().setYiliaoinsurance(item.get("医疗保险基数●").toString());
                            personal4.setDate(DateUtil.format(new Date(), "YYYY-MM-dd"));
                            personal4.setBasic(item.get("医疗保险基数●").toString());
                        }
                        if (item.get("失业保险基数●") != null) {
                            customerInfoList.get(0).getUserinfo().setShiyeinsurance(item.get("失业保险基数●").toString());
                            personal5.setDate(DateUtil.format(new Date(), "YYYY-MM-dd"));
                            personal5.setBasic(item.get("失业保险基数●").toString());
                        }
                        if (item.get("工伤保险基数●") != null) {
                            customerInfoList.get(0).getUserinfo().setGongshanginsurance(item.get("工伤保险基数●").toString());
                            personal6.setDate(DateUtil.format(new Date(), "YYYY-MM-dd"));
                            personal6.setBasic(item.get("工伤保险基数●").toString());
                        }
                        if (item.get("生育保险基数●") != null) {
                            customerInfoList.get(0).getUserinfo().setShengyuinsurance(item.get("生育保险基数●").toString());
                            personal7.setDate(DateUtil.format(new Date(), "YYYY-MM-dd"));
                            personal7.setBasic(item.get("生育保险基数●").toString());
                        }
                        if (item.get("住房公积金缴纳基数●") != null) {
                            customerInfoList.get(0).getUserinfo().setHouseinsurance(item.get("住房公积金缴纳基数●").toString());
                            personal8.setDate(DateUtil.format(new Date(), "YYYY-MM-dd"));
                            personal8.setBasic(item.get("住房公积金缴纳基数●").toString());
                        }
                    } else {
                        throw new LogicalException("第" + k + "行卡号（" + Convert.toStr(item.get("卡号")) + "）" + "不存在,或输入格式不正确！");
                    }
                    //判断 工资 是否有履历，如果有变更，没有添加履历
                    float addflg = 0;
                    if (customerInfoList.get(0).getUserinfo().getGridData() != null) {
                        if (personal.getBasic() != null || personal.getDuty() != null) {
                            List<CustomerInfo.Personal> perList = customerInfoList.get(0).getUserinfo().getGridData();
                            if (perList != null) {
                                //去除  null 的数据
                                perList = perList.stream().filter(item1 -> (!item1.getDate().equals("Invalid date") || item1.getDate() != null)).collect(Collectors.toList());
                            }
                            perList = perList.stream().sorted(Comparator.comparing(CustomerInfo.Personal::getDate)).collect(Collectors.toList());
                            int i = 0;
                            for (CustomerInfo.Personal pp : perList) {
                                i++;
                                if (i == perList.size()) {
                                    if (pp.getDate().length() >= 10) {
                                        pp.setDate(pp.getDate().substring(0, 10));
                                    }
                                    int aa = Integer.valueOf(personal.getDate().replace("-", ""));
                                    int bb = Integer.valueOf(pp.getDate().replace("-", ""));
                                    if (aa >= bb) {
                                        customerInfoList.get(0).getUserinfo().setDuty(item.get("现职责工资●").toString());
                                        customerInfoList.get(0).getUserinfo().setBasic(item.get("现基本工资●").toString());
                                    }
                                }
                                if (pp.getDate() != null && pp.getDate().equals(personal.getDate())) {
                                    addflg = 1;
                                    pp.setBasic(personal.getBasic());
                                    pp.setDuty(personal.getDuty());
                                }
                            }
                            if (addflg == 0) {
                                cupList.addAll(customerInfoList.get(0).getUserinfo().getGridData());
                                cupList.add(personal);
                            } else {
                                cupList.addAll(customerInfoList.get(0).getUserinfo().getGridData());
                            }
                        }
                    } else {
                        cupList.add(personal);
                    }
                    //判断 职务 是否有履历，如果有变更，没有添加履历
                    float addflg1 = 0;
                    if (customerInfoList.get(0).getUserinfo().getPostData() != null) {
                        if (personal1.getBasic() != null) {
                            for (CustomerInfo.Personal pp : customerInfoList.get(0).getUserinfo().getPostData()) {
                                if (pp.getDate() != null && pp.getDate().equals(personal1.getDate())) {
                                    addflg1 = 1;
                                    pp.setBasic(personal1.getBasic());
                                }
                            }
                            if (addflg1 == 0) {
                                cupList1.add(personal1);
                                cupList1.addAll(customerInfoList.get(0).getUserinfo().getPostData());
                            } else {
                                cupList1.addAll(customerInfoList.get(0).getUserinfo().getPostData());
                            }
                        }
                    } else {
                        cupList1.add(personal1);
                    }
                    //判断 RANK 是否有履历，如果有变更，没有添加履历
                    float addflg2 = 0;
                    if (customerInfoList.get(0).getUserinfo().getRankData() != null) {
                        if (personal2.getBasic() != null) {
                            for (CustomerInfo.Personal pp : customerInfoList.get(0).getUserinfo().getRankData()) {
                                if (pp.getDate() != null && pp.getDate().equals(personal2.getDate())) {
                                    addflg2 = 1;
                                    pp.setBasic(personal2.getBasic());
                                }
                            }
                            if (addflg2 == 0) {
                                cupList2.add(personal2);
                                cupList2.addAll(customerInfoList.get(0).getUserinfo().getRankData());
                            } else {
                                cupList2.addAll(customerInfoList.get(0).getUserinfo().getRankData());
                            }
                        }
                    } else {
                        cupList2.add(personal2);
                    }
                    //判断 养老 是否有履历，如果有变更，没有添加履历
                    float addflg3 = 0;
                    if (customerInfoList.get(0).getUserinfo().getOldageData() != null) {
                        if (personal3.getBasic() != null) {
                            for (CustomerInfo.Personal pp : customerInfoList.get(0).getUserinfo().getOldageData()) {
                                if (pp.getDate() != null && pp.getDate().equals(personal3.getDate())) {
                                    addflg3 = 1;
                                    pp.setBasic(personal3.getBasic());
                                }
                            }
                            if (addflg3 == 0) {
                                cupList3.add(personal3);
                                cupList3.addAll(customerInfoList.get(0).getUserinfo().getOldageData());
                            } else {
                                cupList3.addAll(customerInfoList.get(0).getUserinfo().getOldageData());
                            }
                        }
                    } else {
                        cupList3.add(personal3);
                    }
                    //判断 医疗 是否有履历，如果有变更，没有添加履历
                    float addflg4 = 0;
                    if (customerInfoList.get(0).getUserinfo().getMedicalData() != null) {
                        if (personal4.getBasic() != null) {
                            for (CustomerInfo.Personal pp : customerInfoList.get(0).getUserinfo().getMedicalData()) {
                                if (pp.getDate() != null && pp.getDate().equals(personal4.getDate())) {
                                    addflg4 = 1;
                                    pp.setBasic(personal4.getBasic());
                                }
                            }
                            if (addflg4 == 0) {
                                cupList4.add(personal4);
                                cupList4.addAll(customerInfoList.get(0).getUserinfo().getMedicalData());
                            } else {
                                cupList4.addAll(customerInfoList.get(0).getUserinfo().getMedicalData());
                            }
                        }
                    } else {
                        cupList4.add(personal4);
                    }
                    //判断 失业 是否有履历，如果有变更，没有添加履历
                    float addflg5 = 0;
                    if (customerInfoList.get(0).getUserinfo().getSyeData() != null) {
                        if (personal5.getBasic() != null) {
                            for (CustomerInfo.Personal pp : customerInfoList.get(0).getUserinfo().getSyeData()) {
                                if (pp.getDate() != null && pp.getDate().equals(personal5.getDate())) {
                                    addflg5 = 1;
                                    pp.setBasic(personal5.getBasic());
                                }
                            }
                            if (addflg5 == 0) {
                                cupList5.add(personal5);
                                cupList5.addAll(customerInfoList.get(0).getUserinfo().getSyeData());
                            } else {
                                cupList5.addAll(customerInfoList.get(0).getUserinfo().getSyeData());
                            }
                        }
                    } else {
                        cupList5.add(personal5);
                    }
                    //判断 工伤 是否有履历，如果有变更，没有添加履历
                    float addflg6 = 0;
                    if (customerInfoList.get(0).getUserinfo().getGsData() != null) {
                        if (personal6.getBasic() != null) {
                            for (CustomerInfo.Personal pp : customerInfoList.get(0).getUserinfo().getGsData()) {
                                if (pp.getDate() != null && pp.getDate().equals(personal6.getDate())) {
                                    addflg6 = 1;
                                    pp.setBasic(personal6.getBasic());
                                }
                            }
                            if (addflg6 == 0) {
                                cupList6.add(personal6);
                                cupList6.addAll(customerInfoList.get(0).getUserinfo().getGsData());
                            } else {
                                cupList6.addAll(customerInfoList.get(0).getUserinfo().getGsData());
                            }
                        }
                    } else {
                        cupList6.add(personal6);
                    }

                    //判断 生育 是否有履历，如果有变更，没有添加履历
                    float addflg7 = 0;
                    if (customerInfoList.get(0).getUserinfo().getSyuData() != null) {
                        if (personal7.getBasic() != null) {
                            for (CustomerInfo.Personal pp : customerInfoList.get(0).getUserinfo().getSyuData()) {
                                if (pp.getDate() != null && pp.getDate().equals(personal7.getDate())) {
                                    addflg7 = 1;
                                    pp.setBasic(personal7.getBasic());
                                }
                            }
                            if (addflg7 == 0) {
                                cupList7.add(personal7);
                                cupList7.addAll(customerInfoList.get(0).getUserinfo().getSyuData());
                            } else {
                                cupList7.addAll(customerInfoList.get(0).getUserinfo().getSyuData());
                            }
                        }
                    } else {
                        cupList7.add(personal7);
                    }

                    //判断 住房 是否有履历，如果有变更，没有添加履历
                    float addflg8 = 0;
                    if (customerInfoList.get(0).getUserinfo().getHouseData() != null) {
                        if (personal8.getBasic() != null) {
                            for (CustomerInfo.Personal pp : customerInfoList.get(0).getUserinfo().getHouseData()) {
                                if (pp.getDate() != null && pp.getDate().equals(personal8.getDate())) {
                                    addflg8 = 1;
                                    pp.setBasic(personal8.getBasic());
                                }
                            }
                            if (addflg8 == 0) {
                                cupList8.add(personal8);
                                cupList8.addAll(customerInfoList.get(0).getUserinfo().getHouseData());
                            } else {
                                cupList8.addAll(customerInfoList.get(0).getUserinfo().getHouseData());
                            }
                        }
                    } else {
                        cupList8.add(personal8);
                    }


                    //如果有工资履历变更，給料変更日不能为空
                    if ((!StringUtils.isNullOrEmpty(personal.getBasic()) || !StringUtils.isNullOrEmpty(personal.getDuty())) &&
                            StringUtils.isNullOrEmpty(personal.getDate())) {
                        throw new LogicalException("卡号（" + Convert.toStr(item.get("卡号")) + "）" + "的 給料変更日 未填写");
                    }
                    //如果有給料変更日，工资履历不能为空
                    if (!StringUtils.isNullOrEmpty(personal.getDate())) {
                        if (StringUtils.isNullOrEmpty(personal.getBasic()) && StringUtils.isNullOrEmpty(personal.getDuty())) {
                            throw new LogicalException("卡号（" + Convert.toStr(item.get("卡号")) + "）" + "的 工资履历 未填写");
                        }
                    }
                    //降序
                    if (cupList.size() > 0) {
                        cupList = cupList.stream().filter(item1 -> (item1.getDate() != null)).collect(Collectors.toList());
                        cupList = cupList.stream().sorted(Comparator.comparing(CustomerInfo.Personal::getDate).reversed()).collect(Collectors.toList());
                        customerInfoList.get(0).getUserinfo().setGridData(cupList);
                    }
                    if (cupList1.size() > 0) {
                        cupList1 = cupList1.stream().filter(item1 -> (item1.getDate() != null)).collect(Collectors.toList());
                        cupList1 = cupList1.stream().sorted(Comparator.comparing(CustomerInfo.Personal::getDate).reversed()).collect(Collectors.toList());
                        customerInfoList.get(0).getUserinfo().setPostData(cupList1);
                    }
                    if (cupList2.size() > 0) {
                        cupList2 = cupList2.stream().filter(item1 -> (item1.getDate() != null)).collect(Collectors.toList());
                        cupList2 = cupList2.stream().sorted(Comparator.comparing(CustomerInfo.Personal::getDate).reversed()).collect(Collectors.toList());
                        customerInfoList.get(0).getUserinfo().setRankData(cupList2);
                    }
                    if (cupList3.size() > 0) {
                        cupList3 = cupList3.stream().filter(item1 -> (item1.getDate() != null)).collect(Collectors.toList());
                        cupList3 = cupList3.stream().sorted(Comparator.comparing(CustomerInfo.Personal::getDate).reversed()).collect(Collectors.toList());
                        customerInfoList.get(0).getUserinfo().setOldageData(cupList3);
                    }
                    if (cupList4.size() > 0) {
                        cupList4 = cupList4.stream().filter(item1 -> (item1.getDate() != null)).collect(Collectors.toList());
                        cupList4 = cupList4.stream().sorted(Comparator.comparing(CustomerInfo.Personal::getDate).reversed()).collect(Collectors.toList());
                        customerInfoList.get(0).getUserinfo().setMedicalData(cupList4);
                    }
                    if (cupList5.size() > 0) {
                        cupList5 = cupList5.stream().filter(item1 -> (item1.getDate() != null)).collect(Collectors.toList());
                        cupList5 = cupList5.stream().sorted(Comparator.comparing(CustomerInfo.Personal::getDate).reversed()).collect(Collectors.toList());
                        customerInfoList.get(0).getUserinfo().setSyeData(cupList5);
                    }
                    if (cupList6.size() > 0) {
                        cupList6 = cupList6.stream().filter(item1 -> (item1.getDate() != null)).collect(Collectors.toList());
                        cupList6 = cupList6.stream().sorted(Comparator.comparing(CustomerInfo.Personal::getDate).reversed()).collect(Collectors.toList());
                        customerInfoList.get(0).getUserinfo().setGsData(cupList6);
                    }
                    if (cupList7.size() > 0) {
                        cupList7 = cupList7.stream().filter(item1 -> (item1.getDate() != null)).collect(Collectors.toList());
                        cupList7 = cupList7.stream().sorted(Comparator.comparing(CustomerInfo.Personal::getDate).reversed()).collect(Collectors.toList());
                        customerInfoList.get(0).getUserinfo().setSyuData(cupList7);
                    }
                    if (cupList8.size() > 0) {
                        cupList8 = cupList8.stream().filter(item1 -> (item1.getDate() != null)).collect(Collectors.toList());
                        cupList8 = cupList8.stream().sorted(Comparator.comparing(CustomerInfo.Personal::getDate).reversed()).collect(Collectors.toList());
                        customerInfoList.get(0).getUserinfo().setHouseData(cupList8);
                    }
                    mongoTemplate.save(customerInfoList.get(0));
                    //如果更新AD域账号,登录名和密码默认设置成AD域账号
                    if (userAccount.getPassword() != null && userAccount.getAccount() != null) {
                        Query qq = new Query();
                        qq.addCriteria(Criteria.where("_id").is(customerInfoList.get(0).getUserid()));
                        UserAccount ul = mongoTemplate.findOne(qq, UserAccount.class);
                        if (ul != null) {
                            ul.setPassword(userAccount.getPassword());
                            ul.setAccount(userAccount.getAccount());
                            mongoTemplate.save(ul);
                        }
                    }
                    accesscount = accesscount + 1;
                }

            }
//        UPD_FJL_2020/05/12 --修改人员导入
            Result.add("失败数：" + error);
            Result.add("成功数：" + accesscount);
            return Result;
//        } catch (Exception e) {
//            throw new LogicalException(e.getMessage());
//        }
    }
}
