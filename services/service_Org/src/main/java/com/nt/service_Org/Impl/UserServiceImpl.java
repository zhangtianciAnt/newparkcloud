package com.nt.service_Org.Impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.nt.dao_Auth.Role;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.Earlyvacation;
import com.nt.dao_Org.UserAccount;
import com.nt.dao_Org.Vo.UserVo;
import com.nt.service_Org.UserService;
import com.nt.service_Org.mapper.EarlyvacationMapper;
import com.nt.utils.*;
import com.nt.utils.dao.JsTokenModel;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.JsTokenService;
import com.nt.utils.services.TokenService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
    private TokenService tokenService;

    @Autowired
    private EarlyvacationMapper earlyvacationMapper;

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

        //根据条件检索数据
        Query query = new Query();
        query.addCriteria(Criteria.where("account").is(userAccount.getAccount()));
        query.addCriteria(Criteria.where("password").is(userAccount.getPassword()));
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
//邮箱重复check
            Query queryEmail = new Query();
            queryEmail.addCriteria(Criteria.where("userinfo.email").is(userInfo.getEmail()));
            List<CustomerInfo> qcEmail = mongoTemplate.find(queryEmail, CustomerInfo.class);
            if(qcEmail.size() == 0 || qcEmail.get(0).getUserid().equals(customerInfo.getUserid())){
                flg1 = 1;
            }
            else {
                throw new LogicalException("邮箱已重复");
            }

//个人编码重复check
//            Query queryCode = new Query();
//            queryCode.addCriteria(Criteria.where("userinfo.adfield").is(userInfo.getAdfield()));
//            List<CustomerInfo> qcAD = mongoTemplate.find(queryCode, CustomerInfo.class);
//            if(qcAD.size() == 0 || qcAD.get(0).getUserid().equals(customerInfo.getUserid())){
//                flg2 = 1;
//            }
//            else {
//                throw new LogicalException("AD域账号重复");
//            }

//AD域重复check
//            Query queryAD = new Query();
//            queryAD.addCriteria(Criteria.where("userinfo.personalcode").is(userInfo.getPersonalcode()));
//            List<CustomerInfo> qcCode = mongoTemplate.find(queryAD, CustomerInfo.class);
//            if(qcCode.size() == 0 || qcCode.get(0).getUserid().equals(customerInfo.getUserid())){
//                flg3 = 1;
//            }
//            else {
//                throw new LogicalException("个人编码重复");
//            }
            //if(flg1 == 1 && flg2 == 1 && flg3 == 1){
            if(flg1 == 1){
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
        if (StrUtil.isNotBlank(orgid)) {
            query.addCriteria(new Criteria().orOperator(Criteria.where("userinfo.centerid").is(orgid),
                    Criteria.where("userinfo.groupid").is(orgid), Criteria.where("userinfo.teamid").is(orgid)));
        }
//        List<CustomerInfo> customerInfos = new ArrayList<CustomerInfo>();
//        if(!"5e78fefff1560b363cdd6db7".equals(tokenModel.getUserId()) && !"5e78b22c4e3b194874180f5f".equals(tokenModel.getUserId())
//                && !"5e78b2034e3b194874180e37".equals(tokenModel.getUserId()) && !"5e78b17ef3c8d71e98a2aa30".equals(tokenModel.getUserId())){
//            query.addCriteria(Criteria.where("userid").is(tokenModel.getUserId()));
//            List<CustomerInfo> CustomerInfolist = mongoTemplate.find(query, CustomerInfo.class);
//            query = new Query();
//            if(CustomerInfolist.size() > 0){
//                if(StrUtil.isNotBlank(CustomerInfolist.get(0).getUserinfo().getTeamid())){
//                    query.addCriteria(Criteria.where("userinfo.teamid").is(CustomerInfolist.get(0).getUserinfo().getTeamid()));
//                }else  if(StrUtil.isNotBlank(CustomerInfolist.get(0).getUserinfo().getGroupid())){
//                    query.addCriteria(Criteria.where("userinfo.groupid").is(CustomerInfolist.get(0).getUserinfo().getGroupid()));
//                }else  if(StrUtil.isNotBlank(CustomerInfolist.get(0).getUserinfo().getCenterid())){
//                    query.addCriteria(Criteria.where("userinfo.centerid").is(CustomerInfolist.get(0).getUserinfo().getCenterid()));
//                }
//
//                customerInfos.addAll(mongoTemplate.find(query, CustomerInfo.class));
//
//                if(CustomerInfolist.get(0).getUserinfo().getOtherorgs() != null && CustomerInfolist.get(0).getUserinfo().getOtherorgs().size() > 0){
//                    for(CustomerInfo.OtherOrgs itemO:CustomerInfolist.get(0).getUserinfo().getOtherorgs()){
//                        query = new Query();
//
//                        if(StrUtil.isNotBlank(itemO.getTeamid())){
//                            query.addCriteria(Criteria.where("userinfo.teamid").is(itemO.getTeamid()));
//                        }else  if(StrUtil.isNotBlank(itemO.getGroupid())){
//                            query.addCriteria(Criteria.where("userinfo.groupid").is(itemO.getGroupid()));
//                        }else  if(StrUtil.isNotBlank(itemO.getCenterid())){
//                            query.addCriteria(Criteria.where("userinfo.centerid").is(itemO.getCenterid()));
//                        }
//
//                        customerInfos.addAll(mongoTemplate.find(query, CustomerInfo.class));
//                    }
//                }
//            }
//        }else{
//            query = new Query();
//            customerInfos.addAll(mongoTemplate.find(query, CustomerInfo.class));
//        }

        List<CustomerInfo> customerInfos = mongoTemplate.find(query, CustomerInfo.class);
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
//wxl 查询设置休假天数初期值 start
        Earlyvacation earlyvacation = new Earlyvacation();
        earlyvacation.setUsernames(userid);
        Earlyvacation earlyresult = earlyvacationMapper.selectOne(earlyvacation);
//wxl 查询设置休假天数初期值 end
        UserVo userVo = new UserVo();
        userVo.setCustomerInfo(customerInfo);
        userVo.setUserAccount(userAccount);
        userVo.setEarlyvacation(earlyresult);
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

    @Override
    public List<CustomerInfo> getOKRS(String orgid) throws Exception{
        Query query = new Query();
        if (StrUtil.isNotBlank(orgid)) {
            query.addCriteria(Criteria.where("userinfo.groupid").is(orgid));
        }
        List<CustomerInfo> customerInfos = mongoTemplate.find(query, CustomerInfo.class);
        return customerInfos;
    }

    /**
     * 按月获取Customer
     * （ADD） 2020-04-29 -NC
     * @param orgid
     * @return
     * @throws Exception
     */
    @Override
    public List<CustomerInfo> getOKRSByMonth(String orgid) throws Exception{

        System.out.println("1111111111111111111111111111111111111111111111111111111111111111");
        Date tempDate = new Date();
        SimpleDateFormat  sdf = new SimpleDateFormat("yyyy-MM");

        String month =sdf.format(tempDate);

        List<CustomerInfo> customerInfos = new ArrayList<>();
        if (StrUtil.isNotBlank(month)) {
            Criteria cri = Criteria.where("_time").regex(month);
            Query query = new Query();
            query.addCriteria(Criteria.where("status").is("0"));
            query.addCriteria(Criteria.where("userinfo.groupid").is(orgid));
            query.addCriteria(Criteria.where("userinfo.OkrsTable").elemMatch(cri));
            query.with(
                    Sort.by(
                            Sort.Order.desc("userinfo.OkrsTable.completed")
                    )
            );
            query.limit(3);
            customerInfos = mongoTemplate.find(query, CustomerInfo.class);
        }
        return customerInfos;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public List<String> importUser(HttpServletRequest request) throws Exception {
        try {
            List<String> Result = new ArrayList<String>();
            MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
            File f = null;
            f = File.createTempFile("tmp", null);
            file.transferTo(f);
            ExcelReader reader = ExcelUtil.getReader(f);
            List<Map<String, Object>> readAll = reader.readAll();
            int accesscount = 0;
            int error = 0;
            for (Map<String, Object> item : readAll) {
                Query query = new Query();
                query.addCriteria(Criteria.where("userid").is(item.get("社員ID")));
                List<CustomerInfo> customerInfos = mongoTemplate.find(query, CustomerInfo.class);

                if (customerInfos.size() > 0) {
                    customerInfos.get(0).getUserinfo().setCenterid(item.get("centerid").toString());
                    customerInfos.get(0).getUserinfo().setGroupid(item.get("groupid").toString());
                    customerInfos.get(0).getUserinfo().setTeamid(item.get("teamid").toString());
                    customerInfos.get(0).getUserinfo().setCentername(item.get("所属センター").toString());
                    customerInfos.get(0).getUserinfo().setGroupname(item.get("所属グループ").toString());
                    customerInfos.get(0).getUserinfo().setTeamname(item.get("所属チーム").toString());
                    customerInfos.get(0).getUserinfo().setBudgetunit(item.get("予算単位").toString());
                    customerInfos.get(0).getUserinfo().setPost(item.get("職務").toString());
                    customerInfos.get(0).getUserinfo().setRank(item.get("ランク").toString());
                    customerInfos.get(0).getUserinfo().setLaborcontractday(item.get("労働契約締切日").toString());
                    customerInfos.get(0).getUserinfo().setAnnuallastyear(item.get("去年年休数(残)").toString());
                    customerInfos.get(0).getUserinfo().setAnnualyear(item.get("今年年休数").toString());
                    customerInfos.get(0).getUserinfo().setUpgraded(item.get("昇格昇号年月日").toString());
                    customerInfos.get(0).getUserinfo().setSeatnumber(item.get("口座番号").toString());
                    List<CustomerInfo.Personal> cupList = new ArrayList<CustomerInfo.Personal>();
                    CustomerInfo.Personal personal = new CustomerInfo.Personal();
                    personal.setAfter(item.get("変更前基本工资").toString());
                    personal.setBefore(item.get("変更前职责工资").toString());
                    personal.setBasic(item.get("変更后基本工资").toString());
                    personal.setDuty(item.get("変更后职责工资").toString());
                    personal.setDate(item.get("給料変更日").toString());
                    cupList.add(personal);
                    customerInfos.get(0).getUserinfo().setGridData(cupList);
                    customerInfos.get(0).getUserinfo().setOldageinsurance(item.get("養老保険基数").toString());
                    customerInfos.get(0).getUserinfo().setMedicalinsurance(item.get("医療保険基数").toString());
                    customerInfos.get(0).getUserinfo().setHouseinsurance(item.get("住宅積立金納付基数").toString());
                    mongoTemplate.save(customerInfos.get(0));
                }

                accesscount = accesscount + 1;
            }
            Result.add("失败数：" + error);
            Result.add("成功数：" + accesscount);
            return Result;
        } catch (Exception e) {
            throw new LogicalException(e.getMessage());
        }
    }

//    @Override
//    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
//    public List<String> importUser(HttpServletRequest request) throws Exception {
//        try {
//            List<CustomerInfo> listVo = new ArrayList<CustomerInfo>();
//            List<String> Result = new ArrayList<String>();
//            MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
//            File f = null;
//            f = File.createTempFile("tmp", null);
//            file.transferTo(f);
//            ExcelReader reader = ExcelUtil.getReader(f);
//            List<List<Object>> list = reader.read();
//            List<Object> model = new ArrayList<Object>();
//            model.add("姓名");
//            model.add("性别");
//            model.add("AD域账号");
//            model.add("生年月日");
//            model.add("国籍");
//            model.add("民族");
//            model.add("户籍");
//            model.add("住所");
//            model.add("最终毕业学校");
//            model.add("专业");
//            model.add("最终学位");
//            model.add("最终学历");
//            model.add("毕业年月日");
//            model.add("身份证号码");
//            model.add("婚姻状况");
//            model.add("仕事开始年月日");
//            model.add("center");
//            model.add("group");
//            model.add("team");
//            model.add("center");
//            model.add("group");
//            model.add("team");
//            model.add("社員ID");
//            model.add("预算单位");
//            model.add("奖金记上区分");
//            model.add("职务");
//            model.add("类别");
//            model.add("Rank");
//            model.add("入社时间");
//            model.add("银行账号");
//            model.add("邮箱");
//            List<Object> key = list.get(0);
//            for (int i = 0; i < key.size(); i++) {
//                if (!key.get(i).toString().trim().equals(model.get(i))) {
//                    throw new LogicalException("第" + (i + 1) + "列标题错误，应为" + model.get(i).toString());
//                }
//            }
//            int accesscount = 0;
//            int error = 0;
//            for (int i = 2; i < list.size(); i++) {
//                CustomerInfo customerInfo = new CustomerInfo();
//                UserAccount useraccount = new UserAccount();
////            CustomerInfo.UserInfo getUserinfo = new CustomerInfo.UserInfo();
//                CustomerInfo.UserInfo userinfo = new CustomerInfo.UserInfo();
//                List<Object> value = list.get(i);
//                if (value != null && !value.isEmpty()) {
//                    if(value.get(0) != null){
//                        userinfo.setCustomername(value.get(0).toString());
//                    }
//                    if(value.get(1) != null) {
//                        userinfo.setSex(value.get(1).toString());
//                    }
//                    if(value.get(2) != null) {
//                        userinfo.setAdfield(value.get(2).toString());
//                    }
//                    if(value.get(2) != null) {
//                        useraccount.setAccount(value.get(2).toString());
//                    }
//                    if(value.get(2) != null) {
//                        useraccount.setPassword(value.get(2).toString());
//                    }
//                    if(value.get(3) != null) {
//                        userinfo.setBirthday(value.get(3).toString());
//                    }
//                    if(value.get(4) != null) {
//                        userinfo.setNationality(value.get(4).toString());
//                    }
//                    if(value.get(5) != null) {
//                        userinfo.setNation(value.get(5).toString());
//                    }
//                    if(value.get(6) != null) {
//                        userinfo.setRegister(value.get(6).toString());
//                    }
//                    if(value.get(7) != null) {
//                        userinfo.setAddress(value.get(7).toString());
//                    }
//                    if(value.get(8) != null) {
//                        userinfo.setGraduation(value.get(8).toString());
//                    }
//                    if(value.get(9) != null) {
//                        userinfo.setSpecialty(value.get(9).toString());
//                    }
//                    if(value.get(10) != null) {
//                        userinfo.setDegree(value.get(10).toString());
//                    }
//                    if(value.get(11) != null) {
//                        userinfo.setEducational(value.get(11).toString());
//                    }
//                    if(value.get(12) != null) {
//                        userinfo.setGraduationday(value.get(12).toString());
//                    }
//                    if(value.get(13) != null) {
//                        userinfo.setIdnumber(value.get(13).toString());
//                    }
//                    if(value.get(14) != null) {
//                        userinfo.setMarital(value.get(14).toString());
//                    }
//                    if(value.get(15) != null) {
//                        userinfo.setWorkday(value.get(15).toString());
//                    }
//                    if(value.get(16) != null){
//                        userinfo.setCenterid(value.get(16).toString());
//                    }
//                    if(value.get(17) != null) {
//                        userinfo.setGroupid(value.get(17).toString());
//                    }
//                    if(value.get(18) != null) {
//                        userinfo.setTeamid(value.get(18).toString());
//                    }
//                    if(value.get(19) != null) {
//                        userinfo.setCentername(value.get(19).toString());
//                    }
//                    if(value.get(20) != null) {
//                        userinfo.setGroupname(value.get(20).toString());
//                    }
//                    if(value.get(21) != null) {
//                        userinfo.setTeamname(value.get(21).toString());
//                    }
//                    if(value.get(22) != null) {
//                        userinfo.setJobnumber(value.get(22).toString());
//                    }
//                    if(value.get(23) != null) {
//                        userinfo.setBudgetunit(value.get(23).toString());
//                    }
//                    if(value.get(24) != null) {
//                        userinfo.setDifference(value.get(24).toString());
//                    }
//                    if(value.get(25) != null) {
//                        userinfo.setPost(value.get(25).toString());
//                    }
//                    if(value.get(26) != null) {
//                        userinfo.setType(value.get(26).toString());
//                    }
//                    if(value.get(27) != null) {
//                        userinfo.setRank(value.get(27).toString());
//                    }
//                    if(value.get(28) != null) {
//                        userinfo.setEnterday(value.get(28).toString());
//                    }
//                    if(value.get(29) != null) {
//                        userinfo.setSeatnumber(value.get(29).toString());
//                    }
//                    if(value.get(30) != null) {
//                        userinfo.setEmail(value.get(30).toString());
//                    }
//                }
//
//                UserVo uservo = new UserVo();
//                customerInfo.setUserinfo(userinfo);
//                customerInfo.setType("1");
//                customerInfo.setStatus("0");
//                customerInfo.getUserinfo().setType("0");
//                uservo.setCustomerInfo(customerInfo);
//                listVo.add(customerInfo);
//                uservo.setUserAccount(useraccount);
//                TokenModel tokenModel = tokenService.getToken(request);
//                useraccount.preInsert(tokenModel);
//                addAccountCustomer(uservo);
//                accesscount = accesscount + 1;
//            }
//            Result.add("失败数：" + error);
//            Result.add("成功数：" + accesscount);
//            return Result;
//        } catch (Exception e) {
//            throw new LogicalException(e.getMessage());
//        }
//    }
}
