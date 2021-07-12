package com.nt.service_Org.Impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.mysql.jdbc.StringUtils;
import com.nt.dao_Auth.Role;
import com.nt.dao_Auth.Vo.MembersVo;
import com.nt.dao_Org.*;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_Org.Vo.UserAccountVo;
import com.nt.dao_Org.Vo.UserVo;
import com.nt.service_Auth.RoleService;
import com.nt.service_Org.DictionaryService;
import com.nt.service_Org.ToDoNoticeService;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import javax.naming.directory.BasicAttributes;
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

    @Autowired
    private ToDoNoticeService toDoNoticeService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

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
     * @描述：用户登录
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
        //域登录的时候把下面password这句话注释掉
        query.addCriteria(Criteria.where("password").is(userAccount.getPassword()));
        query.addCriteria(Criteria.where("status").is(AuthConstants.DEL_FLAG_NORMAL));
        List<UserAccount> userAccountlist = mongoTemplate.find(query, UserAccount.class);

        //数据不存在时创建新用户
        if (userAccountlist.size() <= 0) {
//            Object name = activeDirectory(userAccount, locale, "1");
//            UserAccount ust = new UserAccount();
//            ust.setAccount(userAccount.getAccount());
//            ust.setPassword(userAccount.getPassword());
//            ust.setUsertype("0");
//            List<Role> rl = new ArrayList<>();
//            Role role = new Role();
//            role.set_id("5e7860c68f43163084351131");
//            role.setRolename("正式社员");
//            role.setDescription("正式社员");
//            role.setDefaultrole("true");
//            rl.add(role);
//            ust.setRoles(rl);
//            ust.setStatus("0");
//            mongoTemplate.save(ust);
//            Query queryer = new Query();
//            queryer.addCriteria(Criteria.where("account").is(ust.getAccount()));
//            queryer.addCriteria(Criteria.where("password").is(ust.getPassword()));
//            List<UserAccount> userAccountlistNew = mongoTemplate.find(queryer, UserAccount.class);
//            CustomerInfo customerInfo = new CustomerInfo();
//            CustomerInfo.UserInfo userinfo = new CustomerInfo.UserInfo();
//            userinfo.setCustomername(name.toString());
//            userinfo.setAdfield(userAccount.getAccount());
//            customerInfo.setUserinfo(userinfo);
//            customerInfo.setType("1");
//            customerInfo.setStatus("0");
//            if (userAccountlistNew.size() > 0) {
//                String _id = userAccountlistNew.get(0).get_id();
//                customerInfo.setUserid(_id);
//                mongoTemplate.save(customerInfo);
//            }
//            Query queryUser = new Query();
//            queryUser.addCriteria(Criteria.where("account").is(userAccount.getAccount()));
//            queryUser.addCriteria(Criteria.where("status").is(AuthConstants.DEL_FLAG_NORMAL));
//            List<UserAccount> userAccountUserlist = mongoTemplate.find(query, UserAccount.class);
//            List<String> roleIds = new ArrayList<String>();
//            query = new Query();
//            query.addCriteria(Criteria.where("_id").is(userAccountUserlist.get(0).get_id()));
//            UserAccount account = mongoTemplate.findOne(query, UserAccount.class);
//            List<Role> roles = account.getRoles();
//            if (roles != null) {
//                for (int i = 0; i < roles.size(); i++) {
//                    roleIds.add(roles.get(i).get_id());
//                }
//            }
//            return jsTokenService.createToken(userAccountUserlist.get(0).get_id(), userAccountUserlist.get(0).getTenantid(), userAccountUserlist.get(0).getUsertype(), new ArrayList<String>(), locale, "", roleIds);
            //发布域登录把这句话注释了把上面的全解了
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
     * @return
     * @方法名：activeDirectory
     * @描述：AD单点登录
     * @创建日期：2020/12/02
     * @作者：SHUBO
     * @参数：[userAccount]
     */
    @Override
    public Object activeDirectory(UserAccount userAccount, String locale, String firstTime) throws Exception {

        String sn = " ";
        String givenName = " ";
        String name = " ";
        String username = userAccount.getAccount();
        String password = userAccount.getPassword();
        DirContext ctx;
        List<String> nameList = new ArrayList();
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        //域LDAPURL/dc=域名,dc=域名.后面的。例子:newtouch.com
        env.put(Context.PROVIDER_URL, "ldap://192.168.1.102:389/dc=yiduanhen,dc=com");
        env.put(Context.SECURITY_PRINCIPAL, username);
        env.put(Context.SECURITY_CREDENTIALS, password);

        try {
            ctx = new InitialDirContext(env);
            //测试用firstTime
//            firstTime = "1";
            if (firstTime.equals("1")) {
                int totalResults = 0;
                String searchFilter = "objectClass=User";
                SearchControls searchCtls = new SearchControls();
                searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
                //返回属性sn=姓，givenName=名
                String returnedAtts[] = {"sn", "givenName"};
                searchCtls.setReturningAttributes(returnedAtts);
                //组织OU="组织名称"
                String searchBase = "OU=p";
                NamingEnumeration answer = ctx.search(searchBase, searchFilter,
                        searchCtls);
                while (answer.hasMoreElements()) {
                    SearchResult sr = (SearchResult) answer.next();
                    String dn = sr.getName();
                    String match = dn.split("CN=")[1].split(",")[0];
                    if (username.equals(match)) {
                        BasicAttributes Attrs = (BasicAttributes) sr.getAttributes();
                        if (Attrs != null) {
                            try {
                                Iterator ite = Attrs.getIDs().asIterator();
                                //迭代器取得key,在下面用for去取
//                                while (ite.hasNext()){
//                                  String keyId =  (String) ite.next();
//                                    System.out.println(keyId);
//                                }
                                for (NamingEnumeration ne = Attrs.getAll(); ne.hasMore(); ) {
                                    Attribute Attr = (Attribute) ne.next();
                                    for (NamingEnumeration e = Attr.getAll(); e.hasMore(); totalResults++) {
                                        String keyId = (String) ite.next();
                                        switch (keyId) {
                                            case "sn":
                                                sn = e.next().toString();
                                                break;
                                            case "givenName":
                                                givenName = e.next().toString();
                                                break;
                                        }
                                    }
                                }
                                name = sn + givenName;
                                ctx.close();
                                return name;
                            } catch (NamingException e) {
                                throw new LogicalException(MessageUtil.getMessage(MsgConstants.ERROR_04, locale));
                            }
                        }
                    }
                }
            }
            ctx.close();
        } catch (AuthenticationException e) {
            throw new LogicalException(MessageUtil.getMessage(MsgConstants.ERROR_04, locale));
        }
        return null;
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
        mongoTemplate.save(customerInfo);//没有用到
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
        SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd");
        UserAccount userAccount = new UserAccount();
        BeanUtils.copyProperties(userVo.getUserAccount(), userAccount);
//        add_fjl_06/12 start -- 新员工添加默认正式社员的角色
        Query query1 = new Query();
        query1.addCriteria(Criteria.where("account").is(userAccount.getAccount()));
        List<UserAccount> userAcco = mongoTemplate.find(query1, UserAccount.class);
        if (userAcco.size() == 0) {
            List<Role> rl = new ArrayList<>();
            Role role = new Role();
            role.set_id("5e7860c68f43163084351131");
            role.setRolename("正式社员");
            role.setDescription("正式社员");
            role.setDefaultrole("true");
            rl.add(role);
            userAccount.setRoles(rl);
        }
//        add_fjl_06/12 end -- 新员工添加默认正式社员的角色
        CustomerInfo customerInfo = new CustomerInfo();
        BeanUtils.copyProperties(userVo.getCustomerInfo(), customerInfo);
        CustomerInfo.UserInfo userInfo = new CustomerInfo.UserInfo();
        BeanUtils.copyProperties(userVo.getCustomerInfo().getUserinfo(), userInfo);
        //add-ws-9/14-禅道任务518
        if (userInfo.getEnterday().indexOf("Z") != -1) {
            String enterday = userInfo.getEnterday().substring(0, 10).replace("-", "/");
            Calendar cal = Calendar.getInstance();
            cal.setTime(sf.parse(enterday));//设置起时间
            cal.add(Calendar.DATE, +1);
            userInfo.setEnterday(sf.format(cal.getTime()));
        }
        //add-ws-9/14-禅道任务518
        //add-ws-9/14-禅道任务525
        if (userInfo.getEnddate() != null) {
            if (userInfo.getEnddate().indexOf("Z") != -1) {
                String enddate = userInfo.getEnddate().substring(0, 10).replace("-", "/");
                Calendar cal1 = Calendar.getInstance();
                cal1.setTime(sf.parse(enddate));//设置起时间
                cal1.add(Calendar.DATE, +1);
                userInfo.setEnddate(sf.format(cal1.getTime()));
            }
        }
        //add-ws-9/14-禅道任务525
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
        } else {
            flg1 = 1;
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
                throw new LogicalException("登录账户重复");
            }
        }

//登录重复check
        Query queryAD = new Query();
        if (!userInfo.getJobnumber().equals("00000")) {
            if (!StringUtils.isNullOrEmpty(userInfo.getJobnumber())) {
                queryAD.addCriteria(Criteria.where("userinfo.jobnumber").is(userInfo.getJobnumber()));
                List<CustomerInfo> qcCode = mongoTemplate.find(queryAD, CustomerInfo.class);
                if (qcCode.size() == 0 || qcCode.get(0).getUserid().equals(customerInfo.getUserid())) {
                    flg3 = 1;
                } else {
                    throw new LogicalException("卡号重复");
                }
            }
        } else {
            flg3 = 1;
        }

        if (flg1 == 1 && flg2 == 1 && flg3 == 1 && flg4 == 1) {
            mongoTemplate.save(userAccount);
            Query query = new Query();
            query.addCriteria(Criteria.where("account").is(userAccount.getAccount()));
            query.addCriteria(Criteria.where("password").is(userAccount.getPassword()));
            List<UserAccount> userAccountlist = mongoTemplate.find(query, UserAccount.class);
            if (userAccountlist.size() > 0) {
                String _id = userAccountlist.get(0).get_id();
                customerInfo.setUserid(_id);
//                }
//                ADD_FJL_05/21   --添加降序
                List<CustomerInfo.Personal> cupList = customerInfo.getUserinfo().getGridData();
                //去除Invalid date的数据
                if (cupList != null && cupList.size() > 0) {
                    cupList = cupList.stream().filter(item1 -> (!item1.getDate().equals("Invalid date") && item1.getDate() != null)).collect(Collectors.toList());
                    if (cupList != null && cupList.size() > 0) {
                        cupList = cupList.stream().sorted(Comparator.comparing(CustomerInfo.Personal::getDate).reversed()).collect(Collectors.toList());
                    }
                    userInfo.setGridData(cupList);
//del ccm  工资数据不能反向覆盖 不可以用履历盖原值 fr
//                    if (userInfo.getGridData().size() > 0) {
//                        userInfo.setBasic(userInfo.getGridData().get(0).getBasic());
//                        userInfo.setDuty(userInfo.getGridData().get(0).getDuty());
//                    }
//del ccm  工资数据不能反向覆盖 不可以用履历盖原值 to
                }
//                ADD_FJL_05/21   --添加降序
                customerInfo.setUserinfo(userInfo);
                mongoTemplate.save(customerInfo);
                //add_fjl_0602  --删除代办
                ToDoNotice toDoNotice1 = new ToDoNotice();
                toDoNotice1.setDataid(customerInfo.getUserid());
                toDoNotice1.setUrl("/usersFormView");
                toDoNotice1.setStatus(AuthConstants.DEL_FLAG_NORMAL);
                List<ToDoNotice> rst1 = toDoNoticeService.get(toDoNotice1);
                if (rst1.size() > 0) {
                    for (ToDoNotice item : rst1) {
                        item.setStatus(AuthConstants.TODO_STATUS_DONE);
                        toDoNoticeService.updateNoticesStatus(item);
                    }
                }
                //add_fjl_0602  --删除代办
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
        //根据登录用户id查看人员信息
        List<CustomerInfo> customerInfos = new ArrayList<CustomerInfo>();
        //5e785fd38f4316308435112d
        List<MembersVo> rolelist = roleService.getMembers("5e785fd38f4316308435112d");
        String user_id = "";
        if (rolelist.size() > 0) {
            user_id = rolelist.get(0).getUserid();
        }
        if (!user_id.equals(tokenModel.getUserId()) && !"5e78b22c4e3b194874180f5f".equals(tokenModel.getUserId()) && !"5e78b2284e3b194874180f47".equals(tokenModel.getUserId())
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
                int check = 0;
                if (CustomerInfolist.get(0).getUserinfo().getOtherorgs() != null && CustomerInfolist.get(0).getUserinfo().getOtherorgs().size() > 0) {
                    for (CustomerInfo.OtherOrgs itemO : CustomerInfolist.get(0).getUserinfo().getOtherorgs()) {
                        query = new Query();
                        if (StrUtil.isNotBlank(itemO.getTeamid())) {
                            check = check + 1;
                            query.addCriteria(Criteria.where("userinfo.teamid").is(itemO.getTeamid()));
                        } else if (StrUtil.isNotBlank(itemO.getGroupid())) {
                            check = check + 1;
                            query.addCriteria(Criteria.where("userinfo.groupid").is(itemO.getGroupid()));
                        } else if (StrUtil.isNotBlank(itemO.getCenterid())) {
                            check = check + 1;
                            query.addCriteria(Criteria.where("userinfo.centerid").is(itemO.getCenterid()));
                        }
                        if (check != 0) {
                            customerInfos.addAll(mongoTemplate.find(query, CustomerInfo.class));
                        }
                    }
                }
            }
        } else {
            query = new Query();
            customerInfos.addAll(mongoTemplate.find(query, CustomerInfo.class));
        }
        customerInfos = customerInfos.stream().distinct().collect(Collectors.toList());
        return customerInfos;
    }

    @Override
    public List<CustomerInfo> getAccountCustomer2(String orgid, String orgtype, String virtual,TokenModel tokenModel) throws Exception {
        Query query = new Query();
        if (StrUtil.isNotBlank(orgid)) {
            //update gbb 20210330 选择树根节点时显示总经理和副总经理 start
            if(orgtype.equals("1") && !virtual.equals("")){
                //职务为总经理或副总经理+虚拟组织
                query.addCriteria(Criteria.where("userinfo.post").in("PG021013","PG021017"));
            }
            else{
                //职务不为总经理或副总经理的数据
                query.addCriteria(new Criteria().orOperator(Criteria.where("userinfo.centerid").is(orgid),
                        Criteria.where("userinfo.groupid").is(orgid), Criteria.where("userinfo.teamid").is(orgid))
                        .andOperator(Criteria.where("userinfo.post").nin("PG021013","PG021017")));
            }
            //update gbb 20210330 选择树根节点时显示总经理和副总经理 end
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
//          item.getUserinfo().setBudgetunit("");
//            item.getUserinfo().setPersonalcode("");
            item.getUserinfo().setType("");
            item.getUserinfo().setOccupationtype("");
            item.getUserinfo().setDifference("");
            item.getUserinfo().setLaborcontracttype("");
            item.getUserinfo().setFixedate("");
//            item.getUserinfo().setEnterday("");
            item.getUserinfo().setUpgraded("");
//            item.getUserinfo().setEnddate("");
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

    //add-ws-9/12-财务人员编码处理
    @Override
    public List<CustomerInfo> getAccountCustomer3(String orgid, String orgtype, TokenModel tokenModel) throws Exception {
        Query query = new Query();
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
//          item.getUserinfo().setBudgetunit("");
//            item.getUserinfo().setPersonalcode("");
            item.getUserinfo().setType("");
            item.getUserinfo().setOccupationtype("");
            item.getUserinfo().setDifference("");
            item.getUserinfo().setLaborcontracttype("");
            item.getUserinfo().setFixedate("");
//            item.getUserinfo().setEnterday("");
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
    //add-ws-9/12-财务人员编码处理

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
//        Query query = new Query();
//        query.addCriteria(Criteria.where("userid").is(userid));
//        CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
//        customerInfo.setStatus(status);
//        mongoTemplate.save(customerInfo);
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
    //【每天凌晨0点5分】
    //系统服务--每天00:05 更新离职人员的信息，已经离职人员不能登录系统  fjl add start
    @Scheduled(cron = "0 05 0 * * ?")
    public void updUseraccountStatus() throws Exception {
        SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd");
        String redate = st.format(new Date());
        int re = Integer.parseInt(redate.replace("-", ""));
        List<CustomerInfo> customerInfos = new ArrayList<CustomerInfo>();
        Query query = new Query();
        customerInfos.addAll(mongoTemplate.find(query, CustomerInfo.class));
        if (customerInfos.size() > 0) {
//            customerInfos = customerInfos.stream().filter(item -> item.getUserinfo().getResignation_date() != null).collect(Collectors.toList());
            for (CustomerInfo c : customerInfos) {
                if (!StringUtils.isNullOrEmpty(c.getUserinfo().getResignation_date())) {
                    //upd ccm 20210712 定时任务更新时间为离职日后俩一天，离职日格式转换修改 fr
//                    Date temp = st.parse(c.getUserinfo().getResignation_date());
//                    Calendar cld = Calendar.getInstance();
//                    cld.setTime(temp);
//                    cld.add(Calendar.DATE, 1);
//                    temp = cld.getTime();
//                    //获得下一天日期字符串
//                    String regndate = st.format(temp);

                    String resignationdate = c.getUserinfo().getResignation_date().substring(0, 10);
                    Calendar rightNow = Calendar.getInstance();
                    rightNow.setTime(Convert.toDate(resignationdate));
                    rightNow.add(Calendar.DAY_OF_YEAR, 1);

                    if (c.getUserinfo().getResignation_date().length() >= 24) {
                        rightNow.setTime(Convert.toDate(resignationdate));
                        rightNow.add(Calendar.DAY_OF_YEAR, 2);
                    }
                    String regndate = st.format(rightNow.getTime());
                    //upd ccm 20210712 定时任务更新时间为离职日后俩一天，离职日格式转换修改 to

//                    String regndate = st.format(st.parse(c.getUserinfo().getResignation_date()));
                    int ret = Integer.parseInt(regndate.replace("-", ""));
                    if (re > ret) {
                        query = new Query();
                        query.addCriteria(Criteria.where("_id").is(c.getUserid()));
                        query.addCriteria(Criteria.where("status").is("0"));
                        UserAccount usount = mongoTemplate.findOne(query, UserAccount.class);
                        if (usount != null) {
                            usount.setStatus("1");
                            mongoTemplate.save(usount);
                            c.getUserinfo().setJobnumber("00000");
                            mongoTemplate.save(c);
                        }
                    }
                }
            }
        }
    }
    //系统服务--每天00:05 更新离职人员的信息，已经离职人员不能登录系统  fjl add  end

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
//        model.add("登录账户");
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
        // update gbb 20210325 查询组织架构添加【有效】条件 start
        Query queryorg = CustmizeQuery(new OrgTree());
        queryorg.addCriteria(Criteria.where("status").is("0"));
        OrgTree orgTree = mongoTemplate.findOne(queryorg, OrgTree.class);
        List<OrgTree> orgTreeList = new ArrayList<>();
        orgTreeList.add(orgTree);
        //region update gbb 20210330 2021组织架构变更-人员导入组织架构变更 start
        //center
        List<OrgTree> orgTreeCenterList = new ArrayList<>();
        //group
        List<OrgTree> orgTreeGroupList = new ArrayList<>();
        if (orgTreeList.size() > 0) {
            if (orgTreeList.get(0).getOrgs().size() > 0) {
                //副总经理
                for (int z = 0; z < orgTreeList.get(0).getOrgs().size(); z++) {
                    if(orgTreeList.get(0).getOrgs().get(z).getOrgs().size() > 0){
                        //center
                        for (int c = 0; c < orgTreeList.get(0).getOrgs().get(z).getOrgs().size(); c++) {
                            orgTreeCenterList.add(orgTreeList.get(0).getOrgs().get(z).getOrgs().get(c));
                            if(orgTreeList.get(0).getOrgs().get(z).getOrgs().get(c).getOrgs().size() > 0){
                                //group
                                for (int g = 0; g < orgTreeList.get(0).getOrgs().get(z).getOrgs().get(c).getOrgs().size(); g++) {
                                    orgTreeGroupList.add(orgTreeList.get(0).getOrgs().get(z).getOrgs().get(c).getOrgs().get(g));
                                }
                            }
                        }
                    }
                }
            }
        }
        //endregion update gbb 20210330 2021组织架构变更-人员导入组织架构变更 end
        // update gbb 20210325 查询组织架构添加【有效】条件 end
        List<String> useradd = new ArrayList<String>();
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
                //登录账户
                if (item.get("登录账户") != null) {
                    userinfo.setAdfield(item.get("登录账户").toString());
                    Query query = new Query();
                    query.addCriteria(Criteria.where("userinfo.adfield").is(userinfo.getAdfield()));
                    List<CustomerInfo> customerInfoList = new ArrayList<CustomerInfo>();
                    customerInfoList = mongoTemplate.find(query, CustomerInfo.class);
                    if (customerInfoList.size() > 0) {
                        throw new LogicalException("卡号（" + item.get("卡号").toString() + "）" + "对应的 登录账户 在人员表中已存在，请勿重复填写。");
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
                            throw new LogicalException("卡号（" + Convert.toStr(item.get("卡号")) + "）" + "对应的 姓名 在人员表中已存在同音的员工，生成登录账户时会重复，请确认。");
                        }
                    }
                }
                //center
                if (item.get("center") != null) {
                    String cen = item.get("center").toString();
                    if(cen.equals("废弃")){
                        userinfo.setCentername("废弃");
                        userinfo.setCenterid("废弃");
                        userinfo.setGroupname("废弃");
                        userinfo.setGroupid("废弃");
                        userinfo.setTeamname("废弃");
                        userinfo.setTeamid("废弃");
                    }
                    else{
                        // update gbb 20210325 用户导入时获取组织架构改为查询一次 start
                        //List<OrgTree> orgTreeList = mongoTemplate.findAll(OrgTree.class);
                        // update gbb 20210325 用户导入时获取组织架构改为查询一次 end
                        // update gbb 20210330 2021组织架构变更-人员导入组织架构变更 start
                        //region 2021人员信息变更-组织架构导入
//                        int cf = 0;
//                        if (orgTreeList.size() > 0 && orgTreeList.get(0).getOrgs().size() > 0) {
//                            for (int c = 0; c < orgTreeList.get(0).getOrgs().size(); c++) {
//                                if (orgTreeList.get(0).getOrgs().get(c).getCompanyname().equals(cen.trim()) || orgTreeList.get(0).getOrgs().get(c).getTitle().equals(cen.trim()) || orgTreeList.get(0).getOrgs().get(c).getCompanyshortname().equals(cen.trim())) {
//                                    cf++;
//                                    userinfo.setCentername(orgTreeList.get(0).getOrgs().get(c).getCompanyname());
//                                    userinfo.setCenterid(orgTreeList.get(0).getOrgs().get(c).get_id());
//                                    userinfo.setGroupname(null);
//                                    userinfo.setGroupid(null);
//                                    userinfo.setTeamname(null);
//                                    userinfo.setTeamid(null);
//                                    break;
//                                }
//                            }
//                            if (cf == 0) {
//                                throw new LogicalException("卡号（" + Convert.toStr(item.get("卡号")) + "）" + "对应的 center(" + item.get("center").toString() + ")不存在！");
//                            }
//                        }
                        //endregion

                        //region 2021人员信息变更-组织架构导入
                        List<OrgTree> orgTreeCenter = orgTreeCenterList.stream().filter(center -> (center.getTitle().equals(cen.trim()))
                                || (center.getCompanyname().equals(cen.trim())) || (center.getCompanyshortname().equals(cen.trim()))).collect(Collectors.toList());
                        if(orgTreeCenter.size() > 0){
                            userinfo.setCentername(orgTreeCenter.get(0).getCompanyname());
                            userinfo.setCenterid(orgTreeCenter.get(0).get_id());
                            userinfo.setGroupname(null);
                            userinfo.setGroupid(null);
                            userinfo.setTeamname(null);
                            userinfo.setTeamid(null);
                        }
                        else{
                            throw new LogicalException("卡号（" + Convert.toStr(item.get("卡号")) + "）" + "对应的 center(" + item.get("center").toString() + ")不存在！");
                        }
                        //endregion
                        // update gbb 20210330 2021组织架构变更-人员导入组织架构变更 end

//                    Query query = new Query();
//                    query.addCriteria(Criteria.where("userinfo.centername").is(cen.trim()));
//                    CustomerInfo cuinfo = mongoTemplate.findOne(query, CustomerInfo.class);
//                    if (cuinfo != null) {
//                        userinfo.setCentername(cuinfo.getUserinfo().getCentername());
//                        userinfo.setCenterid(cuinfo.getUserinfo().getCenterid());
//                    } else {
//                        throw new LogicalException("卡号（" + Convert.toStr(item.get("卡号")) + "）" + "对应的 center(" + item.get("center").toString() + ")不存在，或者有空格！");
//                    }
                    }
                }
                //group
                if (item.get("group") != null) {
                    if (item.get("center") == null) {
                        throw new LogicalException("请输入与" + item.get("group").toString() + "同一组织的 center");
                    }
                    String cen = item.get("center").toString();
                    String grp = item.get("group").toString();
                    int cf = 0;
                    int gf = 0;
                    // update gbb 20210330 2021组织架构变更-人员导入组织架构变更 start
                    //region 2020人员信息变更-组织架构导入
//                    // update gbb 20210325 用户导入时获取组织架构改为查询一次 start
//                    //List<OrgTree> orgTreeList = mongoTemplate.findAll(OrgTree.class);
//                    // update gbb 20210325 用户导入时获取组织架构改为查询一次 ene
//                    if (orgTreeList.size() > 0 && orgTreeList.get(0).getOrgs().size() > 0) {
//                        for (int c = 0; c < orgTreeList.get(0).getOrgs().size(); c++) {
//                            if (gf == 0) {
//
////                                }
//                                if (orgTreeList.get(0).getOrgs().get(c).getCompanyname().equals(cen.trim()) || orgTreeList.get(0).getOrgs().get(c).getTitle().equals(cen.trim()) || orgTreeList.get(0).getOrgs().get(c).getCompanyshortname().equals(cen.trim())) {
//                                    cf++;
//                                    userinfo.setCentername(orgTreeList.get(0).getOrgs().get(c).getCompanyname());
//                                    userinfo.setCenterid(orgTreeList.get(0).getOrgs().get(c).get_id());
//                                }
//                                if (cf > 0 && item.get("group") != null && orgTreeList.get(0).getOrgs().get(c).getOrgs() != null && orgTreeList.get(0).getOrgs().get(c).getOrgs().size() > 0) {
//                                    for (int g = 0; g < orgTreeList.get(0).getOrgs().get(c).getOrgs().size(); g++) {
//                                        if (orgTreeList.get(0).getOrgs().get(c).getOrgs().get(g).getCompanyname().equals(grp.trim()) || orgTreeList.get(0).getOrgs().get(c).getOrgs().get(g).getTitle().equals(grp.trim())) {
//                                            gf++;
//                                            userinfo.setGroupname(orgTreeList.get(0).getOrgs().get(c).getOrgs().get(g).getCompanyname());
//                                            userinfo.setGroupid(orgTreeList.get(0).getOrgs().get(c).getOrgs().get(g).get_id());
//                                            userinfo.setTeamname(null);
//                                            userinfo.setTeamid(null);
//                                            break;
//                                        }
//                                    }
//                                    if (gf == 0) {
//                                        throw new LogicalException("卡号（" + Convert.toStr(item.get("卡号")) + "）" + "对应的 group(" + item.get("group").toString() + ")不存在，或不属于本组织！");
//                                    }
//                                }
//                            } else {
//                                break;
//                            }
//                        }
//                        if (cf == 0) {
//                            throw new LogicalException("卡号（" + Convert.toStr(item.get("卡号")) + "）" + "对应的 center(" + item.get("center").toString() + ")不存在！");
//                        }
//                    }
                    //endregion

                    //region 2021人员信息变更-组织架构导入
                    List<OrgTree> orgTreeCenter = orgTreeCenterList.stream().filter(center -> (center.getTitle().equals(cen.trim()))
                            || (center.getCompanyname().equals(cen.trim())) || (center.getCompanyshortname().equals(cen.trim()))).collect(Collectors.toList());
                    if(orgTreeCenter.size() > 0){
                        userinfo.setCentername(orgTreeCenter.get(0).getCompanyname());
                        userinfo.setCenterid(orgTreeCenter.get(0).get_id());
                    }
                    else{
                        throw new LogicalException("卡号（" + Convert.toStr(item.get("卡号")) + "）" + "对应的 center(" + item.get("center").toString() + ")不存在！");
                    }
                    List<OrgTree> orgTreeGroup = orgTreeGroupList.stream().filter(center -> (center.getTitle().equals(grp.trim()))
                            || (center.getDepartmentname().equals(grp.trim()))).collect(Collectors.toList());
                    if(orgTreeGroup.size() > 0){
                        userinfo.setGroupname(orgTreeGroup.get(0).getDepartmentname());
                        userinfo.setGroupid(orgTreeGroup.get(0).get_id());
                        userinfo.setTeamname(null);
                        userinfo.setTeamid(null);
                    }
                    else{
                        throw new LogicalException("卡号（" + Convert.toStr(item.get("卡号")) + "）" + "对应的 group(" + item.get("group").toString() + ")不存在，或不属于本组织！");
                    }
                    //endregion
                    // update gbb 20210330 2021组织架构变更-人员导入组织架构变更 end
//                    String grp = item.get("group").toString();
//                    Query query = new Query();
//                    query.addCriteria(Criteria.where("userinfo.groupname").is(grp.trim()));
//                    CustomerInfo cuinfo = mongoTemplate.findOne(query, CustomerInfo.class);
//                    if (cuinfo != null) {
//                        userinfo.setGroupname(cuinfo.getUserinfo().getGroupname());
//                        userinfo.setGroupid(cuinfo.getUserinfo().getGroupid());
//                    } else {
//                        throw new LogicalException("卡号（" + Convert.toStr(item.get("卡号")) + "）" + "对应的 group(" + item.get("group").toString() + ")不存在，或者有空格！");
//                    }
                }
                //team
                //region update gbb 20210330 2021组织架构变更-取消team start
//                if (item.get("team") != null) {
//                    if (item.get("group") == null) {
//                        throw new LogicalException("请输入与" + item.get("team").toString() + "同一组织的 group");
//                    }
//                    if (item.get("center") == null) {
//                        throw new LogicalException("请输入与" + item.get("group").toString() + "同一组织的 center");
//                    }
//                    String cen = item.get("center").toString();
//                    String grp = item.get("group").toString();
//                    String tem = item.get("team").toString();
//                    int cf = 0;
//                    int gf = 0;
//                    int tf = 0;
//                    // update gbb 20210325 用户导入时获取组织架构改为查询一次 start
//                    //List<OrgTree> orgTreeList = mongoTemplate.findAll(OrgTree.class);
//                    // update gbb 20210325 用户导入时获取组织架构改为查询一次 end
//                    if (orgTreeList.size() > 0 && orgTreeList.get(0).getOrgs().size() > 0) {
//                        for (int c = 0; c < orgTreeList.get(0).getOrgs().size(); c++) {
//                            if (tf == 0 && gf == 0) {
//
////                                }
//                                if (orgTreeList.get(0).getOrgs().get(c).getCompanyname().equals(cen.trim()) || orgTreeList.get(0).getOrgs().get(c).getTitle().equals(cen.trim()) || orgTreeList.get(0).getOrgs().get(c).getCompanyshortname().equals(cen.trim())) {
//                                    cf++;
//                                    userinfo.setCentername(orgTreeList.get(0).getOrgs().get(c).getCompanyname());
//                                    userinfo.setCenterid(orgTreeList.get(0).getOrgs().get(c).get_id());
//                                }
//                                if (cf > 0 && item.get("group") != null && orgTreeList.get(0).getOrgs().get(c).getOrgs() != null && orgTreeList.get(0).getOrgs().get(c).getOrgs().size() > 0) {
//                                    for (int g = 0; g < orgTreeList.get(0).getOrgs().get(c).getOrgs().size(); g++) {
//                                        if (tf == 0) {
//
////                                        }
//                                            if (orgTreeList.get(0).getOrgs().get(c).getOrgs().get(g).getCompanyname().equals(grp.trim()) || orgTreeList.get(0).getOrgs().get(c).getOrgs().get(g).getTitle().equals(grp.trim())) {
//                                                gf++;
//                                                userinfo.setGroupname(orgTreeList.get(0).getOrgs().get(c).getOrgs().get(g).getCompanyname());
//                                                userinfo.setGroupid(orgTreeList.get(0).getOrgs().get(c).getOrgs().get(g).get_id());
//                                            }
//                                            if (gf > 0 && item.get("team") != null && orgTreeList.get(0).getOrgs().get(c).getOrgs().get(g).getOrgs() != null && orgTreeList.get(0).getOrgs().get(c).getOrgs().get(g).getOrgs().size() > 0) {
//                                                for (int t = 0; t < orgTreeList.get(0).getOrgs().get(c).getOrgs().get(g).getOrgs().size(); t++) {
//                                                    if (orgTreeList.get(0).getOrgs().get(c).getOrgs().get(g).getOrgs().get(t).getCompanyname().equals(tem.trim()) || orgTreeList.get(0).getOrgs().get(c).getOrgs().get(g).getOrgs().get(t).getTitle().equals(tem.trim())) {
//                                                        tf++;
//                                                        userinfo.setTeamname(orgTreeList.get(0).getOrgs().get(c).getOrgs().get(g).getOrgs().get(t).getCompanyname());
//                                                        userinfo.setTeamid(orgTreeList.get(0).getOrgs().get(c).getOrgs().get(g).getOrgs().get(t).get_id());
//                                                        break;
//                                                    }
//                                                }
//                                                if (tf == 0) {
//                                                    throw new LogicalException("卡号（" + Convert.toStr(item.get("卡号")) + "）" + "对应的 team(" + item.get("team").toString() + ")不存在，或不属于本组织！");
//                                                }
//                                            }
//                                        } else {
//                                            break;
//                                        }
//                                    }
//                                    if (gf == 0) {
//                                        throw new LogicalException("卡号（" + Convert.toStr(item.get("卡号")) + "）" + "对应的 group(" + item.get("group").toString() + ")不存在，或不属于本组织！");
//                                    }
//                                }
//                            } else {
//                                break;
//                            }
//                        }
//                        if (cf == 0) {
//                            throw new LogicalException("卡号（" + Convert.toStr(item.get("卡号")) + "）" + "对应的 center(" + item.get("center").toString() + ")不存在！");
//                        }
//                    }
////                    String tem = item.get("team").toString();
////                    Query query = new Query();
////                    query.addCriteria(Criteria.where("userinfo.teamname").is(tem.trim()));
////                    CustomerInfo cuinfo = mongoTemplate.findOne(query, CustomerInfo.class);
////                    if (cuinfo != null) {
////                        userinfo.setTeamname(cuinfo.getUserinfo().getTeamname());
////                        userinfo.setTeamid(cuinfo.getUserinfo().getTeamid());
////                    } else {
////                        throw new LogicalException("卡号（" + Convert.toStr(item.get("卡号")) + "）" + "对应的 team(" + item.get("team").toString() + ")不存在，或者有空格！");
////                    }
//                }
                //endregion update gbb 20210330 2021组织架构变更-取消team end

                //入社时间
                if (item.get("入社时间") != null && item.get("入社时间").toString().length() >= 10) {
                    String enterday = item.get("入社时间").toString().substring(0, 10).replace("-", "/");
                    userinfo.setEnterday(enterday);
                }
                //职务
                if (item.get("职务") != null) {
                    String post = item.get("职务").toString();
                    if (post != null) {
                        Dictionary dictionary = new Dictionary();
                        dictionary.setValue1(post.trim());
                        dictionary.setPcode("PG021");
                        List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                        if (dictionaryList.size() > 0) {
                            userinfo.setPost(dictionaryList.get(0).getCode());
                            personal1.setDate(DateUtil.format(new Date(), "yyyy-MM-dd"));
                            personal1.setBasic(dictionaryList.get(0).getCode());
                        } else {
                            throw new LogicalException("卡号（" + Convert.toStr(item.get("卡号")) + "）" + "对应的职务（" + item.get("职务").toString() + "）在字典中不存在！");
                        }
                    }
                }
                //RANK
                if (item.get("Rank") != null) {
                    //1:出向者;2：正式社员
                    if (item.get("Rank").toString().trim().equals("その他")) {
                        userinfo.setType("1");
                        userinfo.setRank("その他");
                    } else {
                        userinfo.setType("0");
                        String rank = item.get("Rank").toString();
                        if (rank != null) {
                            Dictionary dictionary = new Dictionary();
                            dictionary.setValue1(rank.trim());
                            dictionary.setPcode("PR021");
                            List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                            if (dictionaryList.size() > 0) {
                                userinfo.setRank(dictionaryList.get(0).getCode());
                                personal2.setDate(DateUtil.format(new Date(), "yyyy-MM-dd"));
                                personal2.setBasic(dictionaryList.get(0).getCode());
                            } else {
                                throw new LogicalException("卡号（" + Convert.toStr(item.get("卡号")) + "）" + "对应的Rank（" + item.get("Rank").toString() + "）在字典中不存在！");
                            }
                        }
                    }
                }
                //职级类型
                if (item.get("职级类型") != null) {
                    String occupationtype = item.get("职级类型").toString();
                    if (occupationtype != null) {
                        Dictionary dictionary = new Dictionary();
                        dictionary.setValue1(occupationtype.trim());
                        dictionary.setPcode("PR055");
                        List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                        if (dictionaryList.size() > 0) {
                            userinfo.setOccupationtype(dictionaryList.get(0).getCode());
                        } else {
                            throw new LogicalException("卡号（" + Convert.toStr(item.get("卡号")) + "）" + "对应的职级类型（" + item.get("职级类型").toString() + "）在字典中不存在！");
                        }
                    }
                }
                //性别
                if (item.get("性别") != null) {
                    String sex = item.get("性别").toString();
                    if (sex != null) {
                        Dictionary dictionary = new Dictionary();
                        dictionary.setValue1(sex.trim());
                        dictionary.setPcode("PR019");
                        List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                        if (dictionaryList.size() > 0) {
                            userinfo.setSex(dictionaryList.get(0).getCode());
                        } else {
                            throw new LogicalException("卡号（" + Convert.toStr(item.get("卡号")) + "）" + "对应的性别（" + item.get("性别").toString() + "）在字典中不存在！");
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
                if (item.get("婚姻状况") != null) {
                    String children = item.get("婚姻状况").toString();
                    if (children != null) {
                        userinfo.setMarital(children);
                    }
                }
                if (item.get("最终学历") != null) {
                    String degree = item.get("最终学历").toString();
                    if (degree != null) {
                        Dictionary dictionary = new Dictionary();
                        dictionary.setValue1(degree.trim());
                        dictionary.setPcode("PR016");
                        List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                        if (dictionaryList.size() > 0) {
                            userinfo.setDegree(dictionaryList.get(0).getCode());
                        } else {
                            throw new LogicalException("卡号（" + Convert.toStr(item.get("卡号")) + "）" + "对应的最终学历（" + item.get("最终学历").toString() + "）在字典中不存在！");
                        }
                    }
                }
                //最终学位
                if (item.get("最终学位") != null) {
                    String degree = item.get("最终学位").toString();
                    if (degree != null) {
                        Dictionary dictionary = new Dictionary();
                        dictionary.setValue1(degree.trim());
                        dictionary.setPcode("PG018");
                        List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                        if (dictionaryList.size() > 0) {
                            userinfo.setDegree(dictionaryList.get(0).getCode());
                        } else {
                            throw new LogicalException("卡号（" + Convert.toStr(item.get("卡号")) + "）" + "对应的最终学位（" + item.get("最终学位").toString() + "）在字典中不存在！");
                        }
                    }
                }
                //仕事开始年月日
                if (item.get("仕事开始年月日") != null) {
                    userinfo.setWorkday(item.get("仕事开始年月日").toString());
                }
                //转正日
                if (item.get("转正日") != null) {
                    userinfo.setEnddate(item.get("转正日").toString());
                }

                //        ws-6/28-禅道141任务
                //离职理由分类
                if (item.get("离职理由分类") != null) {
                    String classification = item.get("离职理由分类").toString();
                    if (classification != null) {
                        Dictionary dictionary = new Dictionary();
                        dictionary.setValue1(classification.trim());
                        dictionary.setPcode("RS002");
                        List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                        if (dictionaryList.size() > 0) {
                            userinfo.setClassification(dictionaryList.get(0).getCode());
                        } else {
                            throw new LogicalException("卡号（" + Convert.toStr(item.get("卡号")) + "）" + "对应的离职理由分类（" + item.get("离职理由分类").toString() + "）在字典中不存在！");
                        }
                    }
                }
                //离职去向
                if (item.get("离职去向") != null) {
                    String wheretoleave = item.get("离职去向").toString();
                    if (wheretoleave != null) {
                        Dictionary dictionary = new Dictionary();
                        dictionary.setValue1(wheretoleave.trim());
                        dictionary.setPcode("RS003");
                        List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                        if (dictionaryList.size() > 0) {
                            userinfo.setWheretoleave(dictionaryList.get(0).getCode());
                        } else {
                            throw new LogicalException("卡号（" + Convert.toStr(item.get("卡号")) + "）" + "对应的离职去向（" + item.get("离职去向").toString() + "）在字典中不存在！");
                        }
                    }
                }
                //离职去向（手动）
                if (item.get("离职去向（手动）") != null) {
                    userinfo.setWheretoleave2(item.get("离职去向（手动）").toString());
                }
                //转职公司
                if (item.get("转职公司") != null) {
                    userinfo.setTransfercompany(item.get("转职公司").toString());
                }
                //        zy-7/6-禅道207/231任务 start
                //退职日
                if (item.get("退职日") != null) {
                    //upd_fjl_0916 修改退职日的保存格式 start
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String regindate = item.get("退职日").toString();
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(sdf.parse(regindate));//设置起时间
                    cal.add(Calendar.DATE, -1);//减1天
                    String aaa = "T16:00:00.000Z";
                    regindate = sdf.format(cal.getTime()) + aaa;
                    userinfo.setResignation_date(regindate);
//                    userinfo.setResignation_date(item.get("退职日").toString());
                    //upd_fjl_0916 修改退职日的保存格式 end
                }
                //        zy-7/6-禅道207/231任务 end
                //退职理由
                if (item.get("退职理由") != null) {
                    userinfo.setReason2(item.get("退职理由").toString());
                }
                //其他
                if (item.get("其他") != null) {
                    userinfo.setOther(item.get("其他").toString());
                }


                //        ws-6/28-禅道141任务
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
                //是否大连户籍
                if (item.get("是否大连户籍") != null) {
                    String dlnation = item.get("是否大连户籍").toString();
                    if (dlnation != null) {
                        if (dlnation.equals("否")) {
                            userinfo.setDlnation("0");
                        } else if (dlnation.equals("是")) {
                            userinfo.setDlnation("1");
                        }
                    }
                }
                //奖金记上区分
                if (item.get("奖金记上区分") != null) {
                    String dlnation = item.get("奖金记上区分").toString();
                    if (dlnation != null) {
                        if (dlnation.equals("老员工")) {
                            userinfo.setDifference("2");
                        } else if (dlnation.equals("新员工")) {
                            userinfo.setDifference("1");
                        }
                    }
                }
                //今年年休数(残)
//                        if (value.get(27) != null) {
//                            userinfo.annualyearto(value.get(27).toString());
//                        }
                //社会保险号码
                if (item.get("社会保险号码") != null) {
                    userinfo.setSecurity(item.get("社会保险号码").toString());
                }
                //住房公积金号码
                if (item.get("住房公积金号码") != null) {
                    userinfo.setHousefund(item.get("住房公积金号码").toString());
                }
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
                } else {
                    //add gbb 0724 等级联动职责工资 start
                    if (item.get("Rank") != null) {
                        String rank = item.get("Rank").toString();
                        if (rank != null) {
                            Dictionary dictionary = new Dictionary();
                            dictionary.setValue1(rank.trim());
                            dictionary.setPcode("PR021");
                            List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                            if (dictionaryList.size() > 0) {
                                dictionary = new Dictionary();
                                dictionary.setCode(dictionaryList.get(0).getCode());
                                List<Dictionary> diclist = dictionaryService.getDictionary(dictionary);
                                if (diclist.size() > 0) {
                                    //现职责工资
                                    userinfo.setDuty(diclist.get(0).getValue3());
                                }
                            }
                        }
                    }
                    //add gbb 0724 等级联动职责工资 end
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
                    personal3.setDate(DateUtil.format(new Date(), "yyyy-MM-dd"));
                    personal3.setBasic(item.get("养老保险基数").toString());
                }
                //医疗保险基数
                if (item.get("医疗保险基数") != null) {
                    userinfo.setYiliaoinsurance(item.get("医疗保险基数").toString());
                    personal4.setDate(DateUtil.format(new Date(), "yyyy-MM-dd"));
                    personal4.setBasic(item.get("医疗保险基数").toString());
                }
                //失业保险基数
                if (item.get("失业保险基数") != null) {
                    userinfo.setShiyeinsurance(item.get("失业保险基数").toString());
                    personal5.setDate(DateUtil.format(new Date(), "yyyy-MM-dd"));
                    personal5.setBasic(item.get("失业保险基数").toString());
                }
                //工伤保险基数
                if (item.get("工伤保险基数") != null) {
                    userinfo.setGongshanginsurance(item.get("工伤保险基数").toString());
                    personal6.setDate(DateUtil.format(new Date(), "yyyy-MM-dd"));
                    personal6.setBasic(item.get("工伤保险基数").toString());
                }
                //生育保险基数
                if (item.get("生育保险基数") != null) {
                    userinfo.setShengyuinsurance(item.get("生育保险基数").toString());
                    personal7.setDate(DateUtil.format(new Date(), "yyyy-MM-dd"));
                    personal7.setBasic(item.get("生育保险基数").toString());
                }
                //住房公积金缴纳基数
                if (item.get("住房公积金缴纳基数") != null) {
                    userinfo.setHouseinsurance(item.get("住房公积金缴纳基数").toString());
                    personal8.setDate(DateUtil.format(new Date(), "yyyy-MM-dd"));
                    personal8.setBasic(item.get("住房公积金缴纳基数").toString());
                }
//                }
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

                cupList.add(personal);
                cupList1.add(personal1);
                cupList2.add(personal2);
                cupList3.add(personal3);
                cupList4.add(personal4);
                cupList5.add(personal5);
                cupList6.add(personal6);
                cupList7.add(personal7);
                cupList8.add(personal8);

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
                if (item.get("Rank") != null) {
                    if (item.get("Rank").toString().trim().equals("その他")) {
                        customerInfo.getUserinfo().setType("1");
                    } else {
                        customerInfo.getUserinfo().setType("0");
                    }
                }
//        add_fjl_06/12 start -- 新员工添加默认正式社员的角色
                List<Role> rl = new ArrayList<>();
                Role role = new Role();
                role.set_id("5e7860c68f43163084351131");
                role.setRolename("正式社员");
                role.setDescription("正式社员");
                role.setDefaultrole("true");
                rl.add(role);
                ust.setRoles(rl);
                ust.setStatus("0");
//        add_fjl_06/12 end -- 新员工添加默认正式社员的角色
                mongoTemplate.save(ust);
                Query query = new Query();
                query.addCriteria(Criteria.where("account").is(ust.getAccount()));
                query.addCriteria(Criteria.where("password").is(ust.getPassword()));
                List<UserAccount> userAccountlist = mongoTemplate.find(query, UserAccount.class);
                if(ust.getAccount() != null && ust.getAccount() != null && userAccountlist.size() > 0){
                    customerInfo.setUserid(userAccountlist.get(0).get_id());
                }
                else{
                    customerInfo.setUserid(UUID.randomUUID().toString());//111
                }
                customerInfo.preInsert(tokenModel);
                mongoTemplate.save(customerInfo);
                accesscount = accesscount + 1;
                //成功人员
                useradd.add(customerInfo.getUserid());
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
                    if (item.get("登录账户●") != null) {
                        customerInfoList.get(0).getUserinfo().setAdfield(item.get("登录账户●").toString());
                        query = new Query();
                        query.addCriteria(Criteria.where("userinfo.adfield").is(customerInfoList.get(0).getUserinfo().getAdfield()));
                        List<CustomerInfo> customerInfoLists = new ArrayList<CustomerInfo>();
                        customerInfoLists = mongoTemplate.find(query, CustomerInfo.class);
                        if (customerInfoLists.size() > 0) {
                            throw new LogicalException("登录账户（" + item.get("登录账户●").toString() + "）" + "在人员表中已存在，请勿重复填写。");
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
                                throw new LogicalException("卡号（" + Convert.toStr(item.get("卡号")) + "）" + "对应的 姓名 在人员表中已存在同音的员工，生成登录账户时会重复，请确认。");
                            }
                        }
                    }
                    //center
                    if (item.get("center●") != null) {
                        String cen = item.get("center●").toString();
                        // update gbb 20210325 用户导入时获取组织架构改为查询一次 start
                        //List<OrgTree> orgTreeList = mongoTemplate.findAll(OrgTree.class);
                        // update gbb 20210325 用户导入时获取组织架构改为查询一次 end

                        // update gbb 20210330 2021组织架构变更-人员导入组织架构变更 start
                        //region 2020人员信息变更-组织架构导入
                        //                        int cf = 0;
//                        if (orgTreeList.size() > 0 && orgTreeList.get(0).getOrgs().size() > 0) {
//                            for (int c = 0; c < orgTreeList.get(0).getOrgs().size(); c++) {
//                                if (orgTreeList.get(0).getOrgs().get(c).getCompanyname().equals(cen.trim()) || orgTreeList.get(0).getOrgs().get(c).getTitle().equals(cen.trim()) || orgTreeList.get(0).getOrgs().get(c).getCompanyshortname().equals(cen.trim())) {
//                                    cf++;
//                                    customerInfoList.get(0).getUserinfo().setCentername(orgTreeList.get(0).getOrgs().get(c).getCompanyname());
//                                    customerInfoList.get(0).getUserinfo().setCenterid(orgTreeList.get(0).getOrgs().get(c).get_id());
//                                    customerInfoList.get(0).getUserinfo().setGroupname(null);
//                                    customerInfoList.get(0).getUserinfo().setGroupid(null);
//                                    customerInfoList.get(0).getUserinfo().setTeamname(null);
//                                    customerInfoList.get(0).getUserinfo().setTeamid(null);
//                                    break;
//                                }
//                            }
//                            if (cf == 0) {
//                                throw new LogicalException("卡号（" + Convert.toStr(item.get("卡号")) + "）" + "对应的 center(" + item.get("center●").toString() + ")不存在！");
//                            }
//                        }
                        //endregion

                        //region 2021人员信息变更-组织架构导入
                        List<OrgTree> orgTreeCenter = orgTreeCenterList.stream().filter(center -> (center.getTitle().equals(cen.trim()))
                                || (center.getCompanyname().equals(cen.trim())) || (center.getCompanyshortname().equals(cen.trim()))).collect(Collectors.toList());
                        if(orgTreeCenter.size() > 0){
                            customerInfoList.get(0).getUserinfo().setCentername(orgTreeCenter.get(0).getCompanyname());
                            customerInfoList.get(0).getUserinfo().setCenterid(orgTreeCenter.get(0).get_id());
                            customerInfoList.get(0).getUserinfo().setGroupname(null);
                            customerInfoList.get(0).getUserinfo().setGroupid(null);
                            customerInfoList.get(0).getUserinfo().setTeamname(null);
                            customerInfoList.get(0).getUserinfo().setTeamid(null);
                        }
                        else{
                            throw new LogicalException("卡号（" + Convert.toStr(item.get("卡号")) + "）" + "对应的 center(" + item.get("center●").toString() + ")不存在！");
                        }
                        //endregion
                        // update gbb 20210330 2021组织架构变更-人员导入组织架构变更 end

//                    Query query = new Query();
//                    query.addCriteria(Criteria.where("userinfo.centername").is(cen.trim()));
//                    CustomerInfo cuinfo = mongoTemplate.findOne(query, CustomerInfo.class);
//                    if (cuinfo != null) {
//                        customerInfoList.get(0).getUserinfo().setCentername(cuinfo.getUserinfo().getCentername());
//                        customerInfoList.get(0).getUserinfo().setCenterid(cuinfo.getUserinfo().getCenterid());
//                    } else {
//                        throw new LogicalException("卡号（" + Convert.toStr(item.get("卡号")) + "）" + "对应的 center(" + item.get("center●").toString() + ")不存在，或者有空格！");
//                    }
                    }
                    //group
                    if (item.get("group●") != null) {
                        if (item.get("center●") == null) {
                            throw new LogicalException("请输入与" + item.get("group●").toString() + "同一组织的 center");
                        }
                        String cen = item.get("center●").toString();
                        String grp = item.get("group●").toString();
                        int cf = 0;
                        int gf = 0;
                        // update gbb 20210325 用户导入时获取组织架构改为查询一次 start
                        //List<OrgTree> orgTreeList = mongoTemplate.findAll(OrgTree.class);
                        // update gbb 20210325 用户导入时获取组织架构改为查询一次 end
                        // update gbb 20210330 2021组织架构变更-人员导入组织架构变更 start
                        //region 2020人员信息变更-组织架构导入
//                        if (orgTreeList.size() > 0 && orgTreeList.get(0).getOrgs().size() > 0) {
//                            for (int c = 0; c < orgTreeList.get(0).getOrgs().size(); c++) {
//                                if (gf == 0) {
//
////                                }
//                                    if (orgTreeList.get(0).getOrgs().get(c).getCompanyname().equals(cen.trim()) || orgTreeList.get(0).getOrgs().get(c).getTitle().equals(cen.trim()) || orgTreeList.get(0).getOrgs().get(c).getCompanyshortname().equals(cen.trim()) || orgTreeList.get(0).getOrgs().get(c).getCompanyshortname().equals(cen.trim())) {
//                                        cf++;
//                                        customerInfoList.get(0).getUserinfo().setCentername(orgTreeList.get(0).getOrgs().get(c).getCompanyname());
//                                        customerInfoList.get(0).getUserinfo().setCenterid(orgTreeList.get(0).getOrgs().get(c).get_id());
//                                    }
//                                    if (cf > 0 && item.get("group●") != null && orgTreeList.get(0).getOrgs().get(c).getOrgs() != null && orgTreeList.get(0).getOrgs().get(c).getOrgs().size() > 0) {
//                                        for (int g = 0; g < orgTreeList.get(0).getOrgs().get(c).getOrgs().size(); g++) {
//                                            if (orgTreeList.get(0).getOrgs().get(c).getOrgs().get(g).getCompanyname().equals(grp.trim()) || orgTreeList.get(0).getOrgs().get(c).getOrgs().get(g).getTitle().equals(grp.trim())) {
//                                                gf++;
//                                                customerInfoList.get(0).getUserinfo().setGroupname(orgTreeList.get(0).getOrgs().get(c).getOrgs().get(g).getCompanyname());
//                                                customerInfoList.get(0).getUserinfo().setGroupid(orgTreeList.get(0).getOrgs().get(c).getOrgs().get(g).get_id());
//                                                customerInfoList.get(0).getUserinfo().setTeamname(null);
//                                                customerInfoList.get(0).getUserinfo().setTeamid(null);
//                                                break;
//                                            }
//                                        }
//                                        if (gf == 0) {
//                                            throw new LogicalException("卡号（" + Convert.toStr(item.get("卡号")) + "）" + "对应的 group(" + item.get("group●").toString() + ")不存在，或不属于本组织！");
//                                        }
//                                    }
//                                } else {
//                                    break;
//                                }
//                            }
//                            if (cf == 0) {
//                                throw new LogicalException("卡号（" + Convert.toStr(item.get("卡号")) + "）" + "对应的 center(" + item.get("center●").toString() + ")不存在！");
//                            }
//                        }
                        //endregion

                        //region 2021人员信息变更-组织架构导入
                        List<OrgTree> orgTreeCenter = orgTreeCenterList.stream().filter(center -> (center.getTitle().equals(cen.trim()))
                                || (center.getCompanyname().equals(cen.trim())) || (center.getCompanyshortname().equals(cen.trim()))).collect(Collectors.toList());
                        if(orgTreeCenter.size() > 0){
                            customerInfoList.get(0).getUserinfo().setCentername(orgTreeCenter.get(0).getCompanyname());
                            customerInfoList.get(0).getUserinfo().setCenterid(orgTreeCenter.get(0).get_id());
                        }
                        else{
                            throw new LogicalException("卡号（" + Convert.toStr(item.get("卡号")) + "）" + "对应的 center(" + item.get("center●").toString() + ")不存在！");
                        }

                        List<OrgTree> orgTreeGroup = orgTreeGroupList.stream().filter(center -> (center.getTitle().equals(grp.trim()))
                                || (center.getDepartmentname().equals(grp.trim()))).collect(Collectors.toList());
                        if(orgTreeGroup.size() > 0){
                            customerInfoList.get(0).getUserinfo().setGroupname(orgTreeGroup.get(0).getDepartmentname());
                            customerInfoList.get(0).getUserinfo().setGroupid(orgTreeGroup.get(0).get_id());
                            customerInfoList.get(0).getUserinfo().setTeamname(null);
                            customerInfoList.get(0).getUserinfo().setTeamid(null);
                        }
                        else{
                            throw new LogicalException("卡号（" + Convert.toStr(item.get("卡号")) + "）" + "对应的 group(" + item.get("group●").toString() + ")不存在，或不属于本组织！");
                        }
                        //endregion
                        // update gbb 20210330 2021组织架构变更-人员导入组织架构变更 end
//                    String grp = item.get("group").toString();
//                    Query query = new Query();
//                    query.addCriteria(Criteria.where("userinfo.groupname").is(grp.trim()));
//                    CustomerInfo cuinfo = mongoTemplate.findOne(query, CustomerInfo.class);
//                    if (cuinfo != null) {
//                        customerInfoList.get(0).getUserinfo().setGroupname(cuinfo.getUserinfo().getGroupname());
//                        customerInfoList.get(0).getUserinfo().setGroupid(cuinfo.getUserinfo().getGroupid());
//                    } else {
//                        throw new LogicalException("卡号（" + Convert.toStr(item.get("卡号")) + "）" + "对应的 group(" + item.get("group●").toString() + ")不存在，或者有空格！");
//                    }
                    }
                    //team
                    //region update gbb 20210330 2021组织架构变更-取消team start
//                    if (item.get("team●") != null) {
//                        if (item.get("group●") == null) {
//                            throw new LogicalException("请输入与" + item.get("team●").toString() + "同一组织的 group");
//                        }
//                        if (item.get("center●") == null) {
//                            throw new LogicalException("请输入与" + item.get("group●").toString() + "同一组织的 center");
//                        }
//                        String cen = item.get("center●").toString();
//                        String grp = item.get("group●").toString();
//                        String tem = item.get("team●").toString();
//                        int cf = 0;
//                        int gf = 0;
//                        int tf = 0;
//                        // update gbb 20210325 用户导入时获取组织架构改为查询一次 start
//                        //List<OrgTree> orgTreeList = mongoTemplate.findAll(OrgTree.class);
//                        // update gbb 20210325 用户导入时获取组织架构改为查询一次 end
//                        if (orgTreeList.size() > 0 && orgTreeList.get(0).getOrgs().size() > 0) {
//                            for (int c = 0; c < orgTreeList.get(0).getOrgs().size(); c++) {
//                                if (tf == 0 && gf == 0) {
//
////                                }
//                                    if (orgTreeList.get(0).getOrgs().get(c).getCompanyname().equals(cen.trim()) || orgTreeList.get(0).getOrgs().get(c).getTitle().equals(cen.trim()) || orgTreeList.get(0).getOrgs().get(c).getCompanyshortname().equals(cen.trim())) {
//                                        cf++;
//                                        customerInfoList.get(0).getUserinfo().setCentername(orgTreeList.get(0).getOrgs().get(c).getCompanyname());
//                                        customerInfoList.get(0).getUserinfo().setCenterid(orgTreeList.get(0).getOrgs().get(c).get_id());
//                                    }
//                                    if (cf > 0 && item.get("group●") != null && orgTreeList.get(0).getOrgs().get(c).getOrgs() != null && orgTreeList.get(0).getOrgs().get(c).getOrgs().size() > 0) {
//                                        for (int g = 0; g < orgTreeList.get(0).getOrgs().get(c).getOrgs().size(); g++) {
//                                            if (tf == 0) {
//
////                                        }
//                                                if (orgTreeList.get(0).getOrgs().get(c).getOrgs().get(g).getCompanyname().equals(grp.trim()) || orgTreeList.get(0).getOrgs().get(c).getOrgs().get(g).getTitle().equals(grp.trim())) {
//                                                    gf++;
//                                                    customerInfoList.get(0).getUserinfo().setGroupname(orgTreeList.get(0).getOrgs().get(c).getOrgs().get(g).getCompanyname());
//                                                    customerInfoList.get(0).getUserinfo().setGroupid(orgTreeList.get(0).getOrgs().get(c).getOrgs().get(g).get_id());
//                                                }
//                                                if (gf > 0 && item.get("team●") != null && orgTreeList.get(0).getOrgs().get(c).getOrgs().get(g).getOrgs() != null && orgTreeList.get(0).getOrgs().get(c).getOrgs().get(g).getOrgs().size() > 0) {
//                                                    for (int t = 0; t < orgTreeList.get(0).getOrgs().get(c).getOrgs().get(g).getOrgs().size(); t++) {
//                                                        if (orgTreeList.get(0).getOrgs().get(c).getOrgs().get(g).getOrgs().get(t).getCompanyname().equals(tem.trim()) || orgTreeList.get(0).getOrgs().get(c).getOrgs().get(g).getOrgs().get(t).getTitle().equals(tem.trim())) {
//                                                            tf++;
//                                                            customerInfoList.get(0).getUserinfo().setTeamname(orgTreeList.get(0).getOrgs().get(c).getOrgs().get(g).getOrgs().get(t).getCompanyname());
//                                                            customerInfoList.get(0).getUserinfo().setTeamid(orgTreeList.get(0).getOrgs().get(c).getOrgs().get(g).getOrgs().get(t).get_id());
//                                                            break;
//                                                        }
//                                                    }
//                                                    if (tf == 0) {
//                                                        throw new LogicalException("卡号（" + Convert.toStr(item.get("卡号")) + "）" + "对应的 team(" + item.get("team●").toString() + ")不存在，或不属于本组织！");
//                                                    }
//                                                }
//                                            } else {
//                                                break;
//                                            }
//                                        }
//                                        if (gf == 0) {
//                                            throw new LogicalException("卡号（" + Convert.toStr(item.get("卡号")) + "）" + "对应的 group(" + item.get("group●").toString() + ")不存在，或不属于本组织！");
//                                        }
//                                    }
//                                } else {
//                                    break;
//                                }
//                            }
//                            if (cf == 0) {
//                                throw new LogicalException("卡号（" + Convert.toStr(item.get("卡号")) + "）" + "对应的 center(" + item.get("center●").toString() + ")不存在！");
//                            }
//                        }
////                    String tem = item.get("team●").toString();
////                    Query query = new Query();
////                    query.addCriteria(Criteria.where("userinfo.teamname").is(tem.trim()));
////                    CustomerInfo cuinfo = mongoTemplate.findOne(query, CustomerInfo.class);
////                    if (cuinfo != null) {
////                        customerInfoList.get(0).getUserinfo().setTeamname(cuinfo.getUserinfo().getTeamname());
////                        customerInfoList.get(0).getUserinfo().setTeamid(cuinfo.getUserinfo().getTeamid());
////                    } else {
////                        throw new LogicalException("卡号（" + Convert.toStr(item.get("卡号")) + "）" + "对应的 team(" + item.get("team●").toString() + ")不存在，或者有空格！");
////                    }
//                    }
                    //endregion update gbb 20210330 2021组织架构变更-取消team end
                    if (item.get("入社时间●") != null && item.get("入社时间●").toString().length() >= 10) {
                        String enterday = item.get("入社时间●").toString().substring(0, 10).replace("-", "/");
                        customerInfoList.get(0).getUserinfo().setEnterday(enterday);
                    }
                    if (item.get("职务●") != null) {
                        String post = item.get("职务●").toString();
                        if (post != null) {
                            Dictionary dictionary = new Dictionary();
                            dictionary.setValue1(post.trim());
                            dictionary.setPcode("PG021");
                            List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                            if (dictionaryList.size() > 0) {
                                customerInfoList.get(0).getUserinfo().setPost(dictionaryList.get(0).getCode());
                                personal1.setDate(DateUtil.format(new Date(), "yyyy-MM-dd"));
                                personal1.setBasic(dictionaryList.get(0).getCode());
                            } else {
                                throw new LogicalException("卡号（" + Convert.toStr(item.get("卡号")) + "）" + "对应的职务（" + item.get("职务●").toString() + "）在字典中不存在！");
                            }
                        }
                    }
                    //        ws-6/28-禅道141任务
                    //离职理由分类
                    if (item.get("离职理由分类●") != null) {
                        String classification = item.get("离职理由分类●").toString();
                        if (classification != null) {
                            Dictionary dictionary = new Dictionary();
                            dictionary.setValue1(classification.trim());
                            dictionary.setPcode("RS002");
                            List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                            if (dictionaryList.size() > 0) {
                                customerInfoList.get(0).getUserinfo().setClassification(dictionaryList.get(0).getCode());
                            } else {
                                throw new LogicalException("卡号（" + Convert.toStr(item.get("卡号")) + "）" + "对应的离职理由分类（" + item.get("离职理由分类●").toString() + "）在字典中不存在！");
                            }
                        }
                    }
                    //离职去向
                    if (item.get("离职去向●") != null) {
                        String wheretoleave = item.get("离职去向●").toString();
                        if (wheretoleave != null) {
                            Dictionary dictionary = new Dictionary();
                            dictionary.setValue1(wheretoleave.trim());
                            dictionary.setPcode("RS003");
                            List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                            if (dictionaryList.size() > 0) {
                                customerInfoList.get(0).getUserinfo().setWheretoleave(dictionaryList.get(0).getCode());
                            } else {
                                throw new LogicalException("卡号（" + Convert.toStr(item.get("卡号")) + "）" + "对应的离职去向（" + item.get("离职去向●").toString() + "）在字典中不存在！");
                            }
                        }
                    }
                    //离职去向（手动）
                    if (item.get("离职去向（手动）●") != null) {
                        customerInfoList.get(0).getUserinfo().setWheretoleave2(item.get("离职去向（手动）●").toString());
                    }
                    //转职公司
                    if (item.get("转职公司●") != null) {
                        customerInfoList.get(0).getUserinfo().setTransfercompany(item.get("转职公司●").toString());
                    }
                    //        zy-7/6-禅道207/231任务 start
                    //退职日
                    if (item.get("退职日●") != null) {
                        //upd_fjl_0916 修改退职日的保存格式 start
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String regindate = item.get("退职日●").toString();
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(sdf.parse(regindate));//设置起时间
                        cal.add(Calendar.DATE, -1);//减1天
                        String aaa = "T16:00:00.000Z";
                        regindate = sdf.format(cal.getTime()) + aaa;
                        customerInfoList.get(0).getUserinfo().setResignation_date(regindate);
//                        customerInfoList.get(0).getUserinfo().setResignation_date(item.get("退职日●").toString());
                        //upd_fjl_0916 修改退职日的保存格式 end
                    }
                    //        zy-7/6-禅道207/231任务 end
                    //退职理由
                    if (item.get("退职理由●") != null) {
                        customerInfoList.get(0).getUserinfo().setReason2(item.get("退职理由●").toString());
                    }
                    //其他
                    if (item.get("其他●") != null) {
                        customerInfoList.get(0).getUserinfo().setOther(item.get("其他●").toString());
                    }
                    //        ws-6/28-禅道141任务
                    if (item.get("Rank●") != null) {
                        //1:出向者;2：正式社员
                        if (item.get("Rank●").toString().trim().equals("その他")) {
                            customerInfoList.get(0).getUserinfo().setType("1");
                            customerInfoList.get(0).getUserinfo().setRank("その他");
                        } else {
                            customerInfoList.get(0).getUserinfo().setType("0");
                            String rank = item.get("Rank●").toString();
                            if (rank != null) {
                                Dictionary dictionary = new Dictionary();
                                dictionary.setValue1(rank.trim());
                                dictionary.setPcode("PR021");
                                List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                                if (dictionaryList.size() > 0) {
                                    customerInfoList.get(0).getUserinfo().setRank(dictionaryList.get(0).getCode());
                                    personal2.setDate(DateUtil.format(new Date(), "yyyy-MM-dd"));
                                    personal2.setBasic(dictionaryList.get(0).getCode());
                                } else {
                                    throw new LogicalException("卡号（" + Convert.toStr(item.get("卡号")) + "）" + "对应的Rank（" + item.get("Rank●").toString() + "）在字典中不存在！");
                                }
                            }
                        }
                    }
                    if (item.get("性别●") != null) {
                        String sex = item.get("性别●").toString();
                        if (sex != null) {
                            Dictionary dictionary = new Dictionary();
                            dictionary.setValue1(sex.trim());
                            dictionary.setPcode("PR019");
                            List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                            if (dictionaryList.size() > 0) {
                                customerInfoList.get(0).getUserinfo().setSex(dictionaryList.get(0).getCode());
                            } else {
                                throw new LogicalException("卡号（" + Convert.toStr(item.get("卡号")) + "）" + "对应的性别（" + item.get("性别●").toString() + "）在字典中不存在！");
                            }
                        }
                    }
                    if (item.get("职级类型●") != null) {
                        String occupationtype = item.get("职级类型●").toString();
                        if (occupationtype != null) {
                            Dictionary dictionary = new Dictionary();
                            dictionary.setValue1(occupationtype.trim());
                            dictionary.setPcode("PR055");
                            List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                            if (dictionaryList.size() > 0) {
                                customerInfoList.get(0).getUserinfo().setOccupationtype(dictionaryList.get(0).getCode());
                            } else {
                                throw new LogicalException("卡号（" + Convert.toStr(item.get("卡号")) + "）" + "对应的职级类型（" + item.get("职级类型●").toString() + "）在字典中不存在！");
                            }
                        }
                    }
                    if (item.get("预算编码●") != null) {
                        customerInfoList.get(0).getUserinfo().setBudgetunit(item.get("预算编码●").toString());
                    }
                    if (item.get("生年月日●") != null) {
                        customerInfoList.get(0).getUserinfo().setBirthday(item.get("生年月日●").toString());
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
                            if (experience.equals("有")) {
                                customerInfoList.get(0).getUserinfo().setExperience("0");
                            } else if (experience.equals("无")) {
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
                            dictionary.setPcode("PG018");
                            List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                            if (dictionaryList.size() > 0) {
                                customerInfoList.get(0).getUserinfo().setDegree(dictionaryList.get(0).getCode());
                            } else {
                                throw new LogicalException("卡号（" + Convert.toStr(item.get("卡号")) + "）" + "对应的最终学位（" + item.get("最终学位●").toString() + "）在字典中不存在！");
                            }
                        }
                    }
                    if (item.get("仕事开始年月日●") != null) {
                        customerInfoList.get(0).getUserinfo().setWorkday(item.get("仕事开始年月日●").toString());
                    }
                    if (item.get("转正日●") != null) {
                        customerInfoList.get(0).getUserinfo().setEnddate(formatStringDateadd(item.get("转正日●").toString()));
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
                    if (item.get("是否大连户籍●") != null) {
                        String dlnation = item.get("是否大连户籍●").toString();
                        if (dlnation != null) {
                            if (dlnation.equals("否")) {
                                customerInfoList.get(0).getUserinfo().setDlnation("0");
                            } else if (dlnation.equals("是")) {
                                customerInfoList.get(0).getUserinfo().setDlnation("1");
                            }
                        }
                    }
                    if (item.get("奖金记上区分●") != null) {
                        String dlnation = item.get("奖金记上区分●").toString();
                        if (dlnation != null) {
                            if (dlnation.equals("老员工")) {
                                customerInfoList.get(0).getUserinfo().setDifference("2");
                            } else if (dlnation.equals("新员工")) {
                                customerInfoList.get(0).getUserinfo().setDifference("1");
                            }
                        }
                    }
                    //社会保险号码
                    if (item.get("社会保险号码●") != null) {
                        customerInfoList.get(0).getUserinfo().setSecurity(item.get("社会保险号码●").toString());
                    }
                    //住房公积金号码
                    if (item.get("住房公积金号码●") != null) {
                        customerInfoList.get(0).getUserinfo().setHousefund(item.get("住房公积金号码●").toString());
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
                    } else {
                        //add gbb 0724 等级联动职责工资 start
                        if (item.get("Rank●") != null) {
                            Dictionary dictionary = new Dictionary();
                            dictionary.setValue1(item.get("Rank●").toString());
                            dictionary.setPcode("PR021");
                            List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                            if (dictionaryList.size() > 0) {
                                dictionary = new Dictionary();
                                dictionary.setCode(dictionaryList.get(0).getCode());
                                List<Dictionary> diclist = dictionaryService.getDictionaryList(dictionary);
                                if (diclist.size() > 0) {
                                    customerInfoList.get(0).getUserinfo().setDuty(diclist.get(0).getValue3());
                                }
                            }
                        }
                        //add gbb 0724 等级联动职责工资 end
                    }
                    if (item.get("給料変更日●") != null) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        personal.setDate(DateUtil.format(sdf.parse(item.get("給料変更日●").toString()), "yyyy-MM-dd"));
                    }
                    if (item.get("养老保险基数●") != null) {
                        customerInfoList.get(0).getUserinfo().setYanglaoinsurance(item.get("养老保险基数●").toString());
                        personal3.setDate(DateUtil.format(new Date(), "yyyy-MM-dd"));
                        personal3.setBasic(item.get("养老保险基数●").toString());
                    }
                    if (item.get("医疗保险基数●") != null) {
                        customerInfoList.get(0).getUserinfo().setYiliaoinsurance(item.get("医疗保险基数●").toString());
                        personal4.setDate(DateUtil.format(new Date(), "yyyy-MM-dd"));
                        personal4.setBasic(item.get("医疗保险基数●").toString());
                    }
                    if (item.get("失业保险基数●") != null) {
                        customerInfoList.get(0).getUserinfo().setShiyeinsurance(item.get("失业保险基数●").toString());
                        personal5.setDate(DateUtil.format(new Date(), "yyyy-MM-dd"));
                        personal5.setBasic(item.get("失业保险基数●").toString());
                    }
                    if (item.get("工伤保险基数●") != null) {
                        customerInfoList.get(0).getUserinfo().setGongshanginsurance(item.get("工伤保险基数●").toString());
                        personal6.setDate(DateUtil.format(new Date(), "yyyy-MM-dd"));
                        personal6.setBasic(item.get("工伤保险基数●").toString());
                    }
                    if (item.get("生育保险基数●") != null) {
                        customerInfoList.get(0).getUserinfo().setShengyuinsurance(item.get("生育保险基数●").toString());
                        personal7.setDate(DateUtil.format(new Date(), "yyyy-MM-dd"));
                        personal7.setBasic(item.get("生育保险基数●").toString());
                    }
                    if (item.get("住房公积金缴纳基数●") != null) {
                        customerInfoList.get(0).getUserinfo().setHouseinsurance(item.get("住房公积金缴纳基数●").toString());
                        personal8.setDate(DateUtil.format(new Date(), "yyyy-MM-dd"));
                        personal8.setBasic(item.get("住房公积金缴纳基数●").toString());
                    }
                } else {
                    throw new LogicalException("第" + k + "行卡号（" + Convert.toStr(item.get("卡号")) + "）" + "不存在,或输入格式不正确！");
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

                //判断 工资 是否有履历，如果有变更，没有添加履历
                float addflg = 0;
                if (customerInfoList.get(0).getUserinfo().getGridData() != null) {
                    if (personal.getBasic() != null || personal.getDuty() != null) {
                        List<CustomerInfo.Personal> perList = customerInfoList.get(0).getUserinfo().getGridData();
                        if (perList != null) {
                            //去除  null 的数据
                            perList = perList.stream().filter(item1 -> (!item1.getDate().equals("Invalid date") && item1.getDate() != null)).collect(Collectors.toList());
                        }
                        perList = perList.stream().sorted(Comparator.comparing(CustomerInfo.Personal::getDate)).collect(Collectors.toList());
                        int i = 0;
                        for (CustomerInfo.Personal pp : perList) {
                            i++;
                            if (i == perList.size()) {
                                if (pp.getDate().length() >= 10) {
                                    pp.setDate(pp.getDate().substring(0, 10));
                                }
                                int aa = Integer.valueOf(personal.getDate().replace("-", "").replace("/", ""));
                                int bb = Integer.valueOf(pp.getDate().replace("-", "").replace("/", ""));
                                if (aa >= bb) {
                                    if (item.get("现职责工资●") != null) {
                                        customerInfoList.get(0).getUserinfo().setDuty(item.get("现职责工资●").toString());
                                    }
                                    if (item.get("现基本工资●") != null) {
                                        customerInfoList.get(0).getUserinfo().setBasic(item.get("现基本工资●").toString());
                                    }
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
                            if (item.get("现职责工资●") != null) {
                                customerInfoList.get(0).getUserinfo().setDuty(item.get("现职责工资●").toString());
                            }
                            if (item.get("现基本工资●") != null) {
                                customerInfoList.get(0).getUserinfo().setBasic(item.get("现基本工资●").toString());
                            }
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
                                Dictionary dictionary = new Dictionary();
                                dictionary.setValue1(personal1.getBasic().trim());
                                dictionary.setPcode("PG021");
                                List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                                if (dictionaryList.size() > 0) {
                                    pp.setBasic(dictionaryList.get(0).getCode());
                                }
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
                                Dictionary dictionary = new Dictionary();
                                dictionary.setValue1(personal2.getBasic().trim());
                                dictionary.setPcode("PR021");
                                List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                                if (dictionaryList.size() > 0) {
                                    pp.setBasic(dictionaryList.get(0).getCode());
                                }
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
                customerInfoList.get(0).preUpdate(tokenModel);
                mongoTemplate.save(customerInfoList.get(0));
                //如果更新登录账户,登录名和密码默认设置成登录账户
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
                //成功人员
                useradd.add(customerInfoList.get(0).getUserid());
            }

        }
//        UPD_FJL_2020/05/12 --修改人员导入
        Result.add("失败数：" + error);
        Result.add("成功数：" + accesscount);
        Result.add("成功数人数id" + useradd);
        return Result;
//        } catch (Exception e) {
//            throw new LogicalException(e.getMessage());
//        }
    }

    //获取当前用户登陸信息（IP）
    @Override
    public List<Log.Logs> getSigninlog(String userId) throws Exception {
        List<Log.Logs> logslist = new ArrayList<>();
        Query query = new Query();
        List<Log> loglist = mongoTemplate.find(query, Log.class);
        //UPD CCM 20210219 PSDCD_PFANS_20210209_BUG_014 FROM
        //        if (loglist.size() > 0) {
        //            logslist = loglist.get(0).getLogs();
        //            if (logslist.size() > 0) {
        //                logslist = logslist.stream().filter(coi -> (coi.getCreateby() != null)).collect(Collectors.toList());
        //                logslist = logslist.stream().filter(coi -> (coi.getCreateby().contains(userId))).collect(Collectors.toList());
        //                logslist = logslist.stream().sorted(Comparator.comparing(Log.Logs::getCreateon).reversed()).collect(Collectors.toList());
        //            }
        //        }
        //        return logslist;
        List<Log.Logs> logslists = new ArrayList<>();
        if (loglist.size() > 0) {
            for(Log log : loglist)
            {
                logslist = log.getLogs();
                if (logslist.size() > 0) {
                    for(Log.Logs logs:logslist)
                    {
                        if(logs.getCreateby()!=null && logs.getCreateby().contains(userId))
                        {
                            logslists.add(logs);
                        }
                    }
                }
            }
        }
        logslists = logslists.stream().sorted(Comparator.comparing(Log.Logs::getCreateon).reversed()).collect(Collectors.toList());
        return logslists;
        //UPD CCM 20210219 PSDCD_PFANS_20210209_BUG_014 TO
    }

    /**
     * 时间格式化
     *
     * @param date
     * @return
     */
    private String formatStringDateadd(String date) {
        date = date.replace("-", "/").substring(0, 10);
        Calendar rightNow = Calendar.getInstance();
        SimpleDateFormat ymd = new SimpleDateFormat("yyyy/MM/dd");
        rightNow.setTime(Convert.toDate(date));
        rightNow.add(Calendar.DAY_OF_YEAR, -1);
        date = ymd.format(rightNow.getTime());
        return date;
    }

    //本年度离职，账号不能登录的人员
    //参数：years，年度
    @Override
    public List<CustomerInfo> getCustomerInfoResign(String years) throws Exception {
        List<UserAccount> userAccountList = new ArrayList<UserAccount>();
        List<CustomerInfo> customerInfoList = new ArrayList<CustomerInfo>();
        SimpleDateFormat sf1ymd = new SimpleDateFormat("yyyy-MM-dd");
        Query query = new Query();
        query.addCriteria(Criteria.where("usertype").ne("1"));
        query.addCriteria(Criteria.where("status").is("1"));
        List<UserAccount> userAccountInfo = mongoTemplate.find(query, UserAccount.class);
        for (UserAccount uAccount : userAccountInfo) {
            Query query1 = new Query();
            query1.addCriteria(Criteria.where("userid").is(uAccount.get_id()));
            CustomerInfo customerInfo = mongoTemplate.findOne(query1, CustomerInfo.class);
            if (customerInfo != null) {
                if (customerInfo.getUserinfo().getResignation_date() != null && !customerInfo.getUserinfo().getResignation_date().isEmpty()) {
                    Calendar rightNow = Calendar.getInstance();
                    //离职日
                    String resignationdate = customerInfo.getUserinfo().getResignation_date().substring(0, 10);
                    if (customerInfo.getUserinfo().getResignation_date().length() >= 24) {
                        rightNow.setTime(Convert.toDate(resignationdate));
                        rightNow.add(Calendar.DAY_OF_YEAR, 1);
                        resignationdate = sf1ymd.format(rightNow.getTime());
                    }
                    String resignYear = resignationdate.substring(0, 4);
                    Integer resignmonth = Integer.valueOf(resignationdate.substring(5, 7));
                    if (resignmonth < 4) {
                        resignYear = String.valueOf(Integer.valueOf(resignYear) - 1);
                    }
                    if (years.equals(resignYear)) {
                        customerInfoList.add(customerInfo);
                    }
                }
            }
        }
        return customerInfoList;
    }
    //add-21/2/3-PSDCD_PFANS_20201124_XQ_033
    @Override
    public void checkpassword(UserAccountVo userAccountVo) throws Exception {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(userAccountVo.getUserid()));
        query.addCriteria(Criteria.where("password").is(userAccountVo.getPassword()));
        List<UserAccount> userAccountlist = mongoTemplate.find(query, UserAccount.class);
        if (userAccountlist.size() > 0) {
            throw new LogicalException("1");
        } else {
            throw new LogicalException("0");
        }
    }
}
