package com.nt.service_Org.Impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.mongodb.client.result.DeleteResult;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.OrgTree;
import com.nt.dao_Org.UserAccount;
import com.nt.dao_Org.Vo.UserVo;
import com.nt.service_Org.UserService;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.NotWritablePropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
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
        userAccount.setLogintype("1");
        userAccount.setStatus("0");
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
    public TokenModel login(UserAccount userAccount,String locale) throws Exception {
        TokenModel tokenModel = new TokenModel();
        //根据条件检索数据
        Query query = new Query();
        query.addCriteria(Criteria.where("account").is(userAccount.getAccount()));
        query.addCriteria(Criteria.where("password").is(userAccount.getPassword()));
        query.addCriteria(Criteria.where("logintype").is("1"));
        query.addCriteria(Criteria.where("status").is("0"));
        List<UserAccount> userAccountlist = mongoTemplate.find(query, UserAccount.class);

        //数据不存在时
        if (userAccountlist.size() <= 0) {
            throw new LogicalException(MessageUtil.getMessage(MsgConstants.ERROR_04,locale));
        } else {
            tokenModel = tokenService.getToken(SecureUtil.md5(userAccountlist.get(0).get_id()));
            if (tokenModel != null) {
                tokenService.setToken(tokenModel);
            } else {
                tokenModel = new TokenModel();
                //存在用户时，获取用户信息，生成token
                tokenModel.setUserId(userAccountlist.get(0).get_id());
                tokenModel.setUserType(userAccountlist.get(0).getUsertype());
                tokenModel.setTenantId(userAccountlist.get(0).getTenantid());
                tokenService.setToken(tokenModel);
            }
        }
        return tokenModel;
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
    public String addAccountCustomer(UserVo userVo) throws Exception {
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
            customerInfo.setUserid(_id);
            customerInfo.setUserinfo(userInfo);
            mongoTemplate.save(customerInfo);
            return _id;
        } else {
            return "";
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
    public List<CustomerInfo> getAccountCustomer(String orgid, String orgtype, String logintype) throws Exception {
        Query query = new Query();
        if (StringUtils.isNotBlank(logintype)) {
            query.addCriteria(Criteria.where("logintype").is(logintype));
        }
        if ("1".equals(orgtype)) {
            if(StrUtil.isNotBlank(orgid)){
                query.addCriteria(Criteria.where("userinfo.companyid").is(orgid));
                query.addCriteria(Criteria.where("status").is("0"));
            }

            List<CustomerInfo> customerInfos = mongoTemplate.find(query, CustomerInfo.class);
            return customerInfos;
        } else {
            if(StrUtil.isNotBlank(orgid)){
                query.addCriteria(Criteria.where("userinfo.companyid").is(orgid));
                query.addCriteria(Criteria.where("status").is("0"));
            }
            List<CustomerInfo> customerInfos = mongoTemplate.find(query, CustomerInfo.class);
            return customerInfos;
        }
    }


    @Override
    public List<CustomerInfo> getAccountCustomeraccumulatedhour(String orgid, String orgtype, String logintype) throws Exception {
        Query query = new Query();
        if (StringUtils.isNotBlank(logintype)) {
            query.addCriteria(Criteria.where("logintype").is(logintype));
        }
        if ("1".equals(orgtype)) {
            if(StrUtil.isNotBlank(orgid)){
                query.addCriteria(Criteria.where("userinfo.companyid").is(orgid));
            }

            List<CustomerInfo> customerInfos = mongoTemplate.find(query, CustomerInfo.class);
            return customerInfos;
        } else {
            if(StrUtil.isNotBlank(orgid)){
                query.addCriteria(Criteria.where("userinfo.companyid").is(orgid));
            }
            List<CustomerInfo> customerInfos = mongoTemplate.find(query, CustomerInfo.class);
            return customerInfos;
        }
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


    @Override
    public void del(String userId) throws Exception {
        Query query = new Query();
        query.addCriteria(Criteria.where("userid").is(userId));
        CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
        customerInfo.setStatus(AuthConstants.DEL_FLAG_DELETE);
        mongoTemplate.save(customerInfo);
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
     * @方法名：delUser
     * @描述：删除用户（逻辑）
     * @创建日期：2020/01/14
     * @作者：王哲
     * @参数：[userid]
     * @返回值：void
     */
    @Override
    public void delUser(String id) throws Exception {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        Update update = new Update();
        update.set("status", "1");
        CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
        if (customerInfo != null && StringUtils.isNotBlank(customerInfo.getUserid())) {
            Query query1 = new Query();
            query1.addCriteria(Criteria.where("_id").is(customerInfo.getUserid()));
            UserAccount userAccount = mongoTemplate.findOne(query1, UserAccount.class);
            if (userAccount != null && StringUtils.isNotBlank(userAccount.get_id())) {
                mongoTemplate.upsert(query1, update, UserAccount.class);
                mongoTemplate.upsert(query, update, CustomerInfo.class);
            }
        }
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
                tokenService.setToken(tokenModel);
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
        TokenModel tokenModel = new TokenModel();
        tokenModel.setUserId(userAccount.get_id());
        tokenModel.setUserType(userAccount.getUsertype());
        tokenModel.setTenantId(userAccount.getTenantid());
        tokenService.setToken(tokenModel);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("userVo", userVo);
        result.put("tokenModel", tokenModel);
        return result;
    }

    @Override
    public List<CustomerInfo> getAllCustomerInfo() {
        return mongoTemplate.findAll(CustomerInfo.class);
    }

    /**
     * @param request
     * @param tokenModel
     * @Method excelCustomer
     * @Author
     * @Version 1.0
     * @Description BASF EXCEL批量导入用户
     * @Return void
     * @Date 2019/12/04
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public List<String> excelCustomer(HttpServletRequest request, TokenModel tokenModel) throws Exception {
        try {
            List<String> Result = new ArrayList<String>();
            MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
            File f = null;
            f = File.createTempFile("tmp", null);
            file.transferTo(f);
            ExcelReader reader = ExcelUtil.getReader(f);
            List<List<Object>> list = reader.read();
            List<Object> model = new ArrayList<Object>();
            model.add("部门");
            model.add("姓名");
            model.add("职位名称");
            model.add("性别");
            model.add("USE ID");
            model.add("员工号");
            model.add("部门联系邮箱");
            model.add("装置经理邮箱");
            model.add("身份证件号");
            model.add("文化程度");
            model.add("成本中心");
            model.add("本人邮箱");
            model.add("本人手机号");
            List<Object> key = list.get(0);
            for (int i = 0; i < key.size(); i++) {
                if (!key.get(i).toString().trim().equals(model.get(i))) {
                    throw new LogicalException("第" + (i + 1) + "列标题错误，应为" + model.get(i).toString());
                }
            }

            int accesscount = 0;
            int error = 0;

            Query queryorgTrees = new Query();
            List<OrgTree> orgTrees = mongoTemplate.find(queryorgTrees, OrgTree.class);
            List<OrgTree> orgs =  orgTrees.get(0).getOrgs();

            for (int i = 1; i < list.size(); i++) {

                List<Object> value = list.get(i);
                //部门
                String departmentname = value.get(0).toString();
                //姓名
                String name = value.get(1).toString();
                //职位名称
                String  positionname= value.get(2).toString();
                //性别
                String sex = value.get(3).toString().equals("男")?"0":"1";
                //USE ID
                String usercode = value.get(4).toString();
                //员工号
                String jobnumber = value.get(5).toString();
                //部门联系邮箱
                String departmentemail = value.get(6).toString();
                //装置经理邮箱
                String devicemanageremail = value.get(7).toString();
                //身份证件号
                String idnumber = value.get(8).toString();
                //文化程度
                String degreeeducation = value.get(9).toString();
                //成本中心
                String costcenter = value.get(10).toString();
                //本人邮箱
                String email = value.get(11).toString();
                //本人手机号
                String mobilenumber =  value.get(12).toString();


                UserAccount useraccount = new UserAccount();
                CustomerInfo customerinfo = new CustomerInfo();
                CustomerInfo.UserInfo userInfo = new CustomerInfo.UserInfo();
                useraccount.setAccount(name);
                useraccount.setPassword(name);
                useraccount.setLogintype("0");
                useraccount.setUsertype("0");
                useraccount.setStatus("0");
                useraccount.setJobnumber(jobnumber);

                customerinfo.setLogintype("0");
                customerinfo.setType("1");
                customerinfo.setStatus("0");
                userInfo.setEmail(email);
                List<String> dep = new ArrayList<>();
                List<String> com = new ArrayList<>();
                for(int j = 0;j<orgs.size();j++)
                {
                    if(orgs.get(j).getDepartmentname().equals(departmentname))
                    {
                        dep.add(orgs.get(j).get_id());
                        com.add(orgs.get(j).get_id());
                    }
                }

                userInfo.setDepartmentid(dep);
                userInfo.setCompanyid(com);
                userInfo.setCustomername(name);
                userInfo.setMobilenumber(mobilenumber);
                userInfo.setJobnumber(jobnumber);
                userInfo.setPositionname(positionname);
                userInfo.setIdnumber(idnumber);
                userInfo.setDocumentnumber(usercode);
                userInfo.setDepartmentemail(departmentemail);
                userInfo.setDevicemanageremail(devicemanageremail);
                userInfo.setDegreeeducation(degreeeducation);
                userInfo.setCostcenter(costcenter);
//                userInfo.setApplydataurl();
//                userInfo.setTraindataurl();
                userInfo.setSex(sex);
                customerinfo.setUserinfo(userInfo);

                Query query = new Query();
                query.addCriteria(Criteria.where("jobnumber").is(jobnumber));
                List<UserAccount> useraccountselect = mongoTemplate.find(query, UserAccount.class);

                if (useraccountselect.size() > 0) {
                    useraccount.preUpdate(tokenModel);
                    customerinfo.preUpdate(tokenModel);
                    useraccount.set_id(useraccountselect.get(0).get_id());
                    customerinfo.set_id(useraccountselect.get(0).get_id());

                    mongoTemplate.save(useraccount);
                    mongoTemplate.save(customerinfo);
                }
                else
                {
                    UserVo uservo = new UserVo();
                    uservo.setUserAccount(useraccount);
                    uservo.setCustomerInfo(customerinfo);
                    UserAccount userAccount1 = new UserAccount();
                    BeanUtils.copyProperties(uservo.getUserAccount(), userAccount1);
                    userAccount1.preInsert(tokenModel);
                    mongoTemplate.save(userAccount1);
                    Query query1 = new Query();
                    query1.addCriteria(Criteria.where("account").is(userAccount1.getAccount()));
                    query1.addCriteria(Criteria.where("password").is(userAccount1.getPassword()));
                    List<UserAccount> userAccountlist = mongoTemplate.find(query1, UserAccount.class);
                    if (userAccountlist.size() > 0) {
                        String _id = userAccountlist.get(0).get_id();
                        CustomerInfo customerInfo = new CustomerInfo();
                        BeanUtils.copyProperties(uservo.getCustomerInfo(), customerInfo);
                        CustomerInfo.UserInfo userInfo1 = new CustomerInfo.UserInfo();
                        BeanUtils.copyProperties(uservo.getCustomerInfo().getUserinfo(), userInfo1);
                        customerInfo.setUserid(_id);
                        customerInfo.setUserinfo(userInfo1);
                        customerInfo.preInsert(tokenModel);
                        mongoTemplate.save(customerInfo);
                    }
                    accesscount = accesscount + 1;
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
