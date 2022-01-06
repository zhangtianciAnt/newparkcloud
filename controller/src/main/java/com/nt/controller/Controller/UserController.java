package com.nt.controller.Controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.Log;
import com.nt.dao_Org.ToDoNotice;
import com.nt.dao_Org.UserAccount;
import com.nt.dao_Org.Vo.UserAccountVo;
import com.nt.dao_Org.Vo.UserVo;
import com.nt.dao_Pfans.PFANS6000.Expatriatesinfor;
import com.nt.service_Org.LogService;
import com.nt.service_Org.ToDoNoticeService;
import com.nt.service_Org.UserService;
import com.nt.service_pfans.PFANS2000.AnnualLeaveService;
import com.nt.service_pfans.PFANS6000.ExpatriatesinforService;
import com.nt.utils.*;
import com.nt.utils.dao.JsTokenModel;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.mongodb.core.query.Query;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.controller.Controller
 * @ClassName: UserController
 * @Description: 用户相关操作Controller
 * @Author: SKAIXX
 * @CreateDate: 2018/10/25
 * @UpdateUser: SKAIXX
 * @UpdateDate: 2018/10/25
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AnnualLeaveService annualLeaveService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private LogService logService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ExpatriatesinforService expatriatesinforService;

    @Autowired
    private ToDoNoticeService toDoNoticeService;

//    出向者 身份证 出生年月 不是必填项 ztc from
    private static final SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyy/MM/dd");
//    出向者 身份证 出生年月 不是必填项 ztc to
    @RequestMapping(value = "/download", method = {RequestMethod.GET})
    public void download(String type, HttpServletResponse response) throws Exception {
        Map<String, Object> data = new HashMap<>();
        String templateName = null;
        String fileName = null;
        if ( "0".equals(type) ) {
            templateName = "renyuanxinxi.xlsx";
            fileName = "用户导入模板";
        }
        if (templateName != null ) {
            ExcelOutPutUtil.OutPut(fileName,templateName,data,response);
        }
    }

    //注册
    @RequestMapping(value = "/register", method = {RequestMethod.POST})
    public ApiResult addUser(@RequestBody UserAccount userAccount,HttpServletRequest request) throws Exception {
        if (userAccount == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03,RequestUtils.CurrentLocale(request)));
        }
        userAccount.preInsert();
        userService.inUserAccount(userAccount);
        return ApiResult.success();
    }

    //获取用户账户
    @RequestMapping(value = "/getCurrentUserAccount", method = {RequestMethod.POST})
    public ApiResult getCurrentUserAccount(@RequestBody UserAccount userAccount,HttpServletRequest request) throws Exception {
        if (userAccount == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03,RequestUtils.CurrentLocale(request)));
        }

        var list = userService.getUserAccount(userAccount);
        return ApiResult.success(list.size());
    }

    //登陆
    @RequestMapping(value = "/login", method = {RequestMethod.POST})
    public ApiResult login(@RequestBody UserAccount userAccount, HttpServletRequest request) throws Exception {
        try {
            if (userAccount == null) {
                return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03,RequestUtils.CurrentLocale(request)));
            }

            //域登录
            //userService.activeDirectory(userAccount,RequestUtils.CurrentLocale(request),"0");

            JsTokenModel tokenModel = userService.login(userAccount,RequestUtils.CurrentLocale(request));

            var log = new Log();
            log.setType(AuthConstants.LOG_TYPE_LOGIN);
            var logs = new Log.Logs();
            logs.setIp(Ip.getIPAddress(request));
            logs.setEquipment(AuthConstants.LOG_EQUIPMENT_PC);
            logs.setCreateby(tokenModel.getUserId());
            logs.preInsert();
            log.setLogs(new ArrayList<Log.Logs>());
            log.getLogs().add(logs);
            log.preInsert();
            logService.save(log);
            messagingTemplate.convertAndSend("/topicLogin/subscribe", tokenModel.getToken());
            return ApiResult.success(tokenModel);
        } catch (LogicalException ex) {
            return ApiResult.fail(ex.getMessage());
        }
    }

    //获取当前用户信息
    @RequestMapping(value = "/getCurrentUserInfo", method = {RequestMethod.POST})
    public ApiResult getCurrentUserInfo(HttpServletRequest request) throws Exception {
        String userId = RequestUtils.CurrentUserId(request);
        var customerInfo = new CustomerInfo();
        customerInfo.setUserid(userId);
        return ApiResult.success(userService.getCustomerInfo(customerInfo));
    }

    //创建当前用户用户信息
    @RequestMapping(value = "/inCurrentUserInfo", method = {RequestMethod.POST})
    public ApiResult inCurrentUserInfo(@RequestBody CustomerInfo customerInfo, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        customerInfo.setUserid(tokenModel.getUserId());
        customerInfo.preInsert(tokenModel);
        userService.inCustomerInfo(customerInfo);
        return ApiResult.success();
    }

    //更新当前用户用户信息
    @RequestMapping(value = "/upCurrentUserInfo", method = {RequestMethod.POST})
    public ApiResult upCurrentUserInfo(@RequestBody CustomerInfo customerInfo, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        customerInfo.setUserid(tokenModel.getUserId());
        customerInfo.preUpdate(tokenModel);
        userService.upCustomerInfo(customerInfo);
        return ApiResult.success();
    }

    //当前用户申请成为正式租户
    @RequestMapping(value = "/applyCurrentUserTenantId", method = {RequestMethod.POST})
    public ApiResult applyTenantId(HttpServletRequest request) throws Exception {
        var userAccount = new UserAccount();
        userAccount.setUserid(RequestUtils.CurrentUserId(request));
        val userAccountlist = userService.getUserAccount(userAccount);
        if (userAccountlist.size() <= 0) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_04,RequestUtils.CurrentLocale(request)));
        } else if (userAccountlist.get(0).getStatus().equals(AuthConstants.APPLYTENANTID)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_05,RequestUtils.CurrentLocale(request)));
        } else {
            TokenModel tokenModel = tokenService.getToken(request);
            userAccountlist.get(0).setStatus(AuthConstants.APPLYTENANTID);
            userAccountlist.get(0).preUpdate(tokenModel);
            userService.upUserAccount(userAccountlist.get(0));
        }
        return ApiResult.success();
    }

    /**
     * @方法名：addAccountCustomer
     * @描述：添加用户账号及用户信息
     * @创建日期：2018/12/06
     * @作者：ZHANGYING
     * @参数：[userVo, request]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/addAccountCustomer", method = {RequestMethod.POST})
    public ApiResult addAccountCustomer(@RequestBody UserVo userVo, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
//        出向者 身份证 出生年月 不是必填项 ztc from
        if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(userVo.getCustomerInfo().getUserinfo().getBirthday())){
            if(userVo.getCustomerInfo().getUserinfo().getBirthday().indexOf("00:00.000Z",10) > 0){
                String birthday = userVo.getCustomerInfo().getUserinfo().getBirthday().substring(0, 10).replace("-", "/");
                Calendar cal1 = Calendar.getInstance();
                try {
                    cal1.setTime(sdfYMD.parse(birthday));//设置起时间
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                cal1.add(Calendar.DATE, +1);
                userVo.getCustomerInfo().getUserinfo().setBirthday(sdfYMD.format(cal1.getTime()));
            }
        }
//        出向者 身份证 出生年月 不是必填项 ztc to
        String id = "";
        CustomerInfo info = new CustomerInfo();
        if (StrUtil.isNotBlank(userVo.getUserAccount().get_id())) {
            userVo.getUserAccount().preUpdate(tokenModel);
            userVo.getCustomerInfo().preUpdate(tokenModel);
            info = userService.addAccountCustomer(userVo);
            id = info.getUserid();
            annualLeaveService.insertannualLeave(info);
        } else {
            userVo.getUserAccount().preInsert(tokenModel);
            userVo.getUserAccount().setPassword(userVo.getCustomerInfo().getUserinfo().getAdfield());
            userVo.getCustomerInfo().preInsert(tokenModel);
            info = userService.addAccountCustomer(userVo);
            annualLeaveService.insertannualLeave(info);
            id = info.getUserid();
        }
        return ApiResult.success(id);
    }

    @RequestMapping(value = "/importUser", method = {RequestMethod.POST})
    public ApiResult importUser( HttpServletRequest request) throws Exception {
        try {
            TokenModel tokenModel = tokenService.getToken(request);
            List<String> stringList =  userService.importUser(request, tokenModel);
            String[]  groupUserid = stringList.get(2).replace("[","").replace("]","").replace("成功数人数id","").split(",");
            //add 导入人员计算年休 20201030
            annualLeaveService.insertAnnualImport(groupUserid);
            //add 导入人员计算年休 20201030
            List<String> returnList = new ArrayList<String>();
            returnList.add(stringList.get(0));
            returnList.add(stringList.get(1));
            return ApiResult.success(returnList);
        }catch(LogicalException e){
            return ApiResult.fail(e.getMessage());
        }catch (Exception e) {
            return ApiResult.fail("操作失败！");
        }
    }

    /**
     * @方法名：getAccountCustomer
     * @描述：根据orgid获取用户账号及用户信息
     * @创建日期：2018/12/06
     * @作者：ZHANGYING
     * @参数：[orgid, orgtype, request]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/getAccountCustomer", method = {RequestMethod.GET})
    public ApiResult getAccountCustomer(String orgid, String orgtype, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(userService.getAccountCustomer(orgid, orgtype,tokenModel));
    }

    /**
     * @方法名：getAccountCustomer
     * @描述：根据orgid获取用户账号及用户信息
     * @创建日期：2018/12/06
     * @作者：ZHANGYING
     * @参数：[orgid, orgtype,virtual, request]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/getAccountCustomer2", method = {RequestMethod.GET})
    public ApiResult getAccountCustomer2(String orgid, String orgtype, String virtual,HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(userService.getAccountCustomer2(orgid, orgtype,virtual,tokenModel));
    }

    //add-ws-9/12-财务人员编码处理
    @RequestMapping(value = "/getAccountCustomer3", method = {RequestMethod.GET})
    public ApiResult getAccountCustomer3(String orgid, String orgtype, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(userService.getAccountCustomer3(orgid, orgtype,tokenModel));
    }
    //add-ws-9/12-财务人员编码处理
    /**
     * @方法名：getAccountCustomerById
     * @描述：根据用户id获取用户账号及用户信息
     * @创建日期：2018/12/06
     * @作者：ZHANGYING
     * @参数：[userid, request]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/getAccountCustomerById", method = {RequestMethod.GET})
    public ApiResult getAccountCustomerById(String userid, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(userService.getAccountCustomerById(userid));
    }

    @RequestMapping(value = "/getme", method = {RequestMethod.GET})
    public ApiResult getme(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(userService.getAccountCustomerById(tokenModel.getUserId()));
    }

    /**
     * @方法名：mobileCheck
     * @描述：验证手机号是否重复
     * @创建日期：2018/12/06
     * @作者：ZHANGYING
     * @参数：[id, mobileCheck]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/mobileCheck", method = {RequestMethod.GET})
    public ApiResult mobileCheck(String id, String mobilenumber, HttpServletRequest request) throws Exception {
        try {
            TokenModel tokenModel = tokenService.getToken(request);
            userService.mobileCheck(id, mobilenumber);
            return ApiResult.success();
        } catch (LogicalException e) {
            return ApiResult.fail(e.getMessage());
        }
    }

    /**
     * @方法名：updUserStatus
     * @描述：更新用户状态
     * @创建日期：2018/12/06
     * @作者：ZHANGYING
     * @参数：[userid, status, request]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/updUserStatus", method = {RequestMethod.GET})
    public ApiResult updUserStatus(String userid, String status, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        userService.updUserStatus(userid, status);
        return ApiResult.success();
    }

    /**
     * @方法名：setRoleToUser
     * @描述：给用户赋角色
     * @创建日期：2018/12/07
     * @作者：ZHANGYING
     * @参数：[userAccount, request]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/setRoleToUser", method = {RequestMethod.POST})
    public ApiResult setRoleToUser(@RequestBody UserAccount userAccount, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        userService.setRoleToUser(userAccount);
        return ApiResult.success();
    }

    /**
     * @方法名：updUserInfo
     * @描述：更新微信端用户信息
     * @创建日期：2018/12/14
     * @作者：ZHANGYING
     * @参数：[customerInfo, request]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/updUserInfo", method = {RequestMethod.POST})
    public ApiResult updUserInfo(@RequestBody CustomerInfo customerInfo, HttpServletRequest request) throws Exception {
        if (customerInfo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03,RequestUtils.CurrentLocale(request)));
        }

        return ApiResult.success(userService.updUserInfo(customerInfo));
    }

    /**
     * @方法名：getWxById
     * @描述：微信端根据用户id获取用户账号及用户信息
     * @创建日期：2018/12/06
     * @作者：ZHANGYING
     * @参数：[userid, request]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/getWxById", method = {RequestMethod.GET})
    public ApiResult getWxById(String userid, HttpServletRequest request) throws Exception {
        return ApiResult.success(userService.getWxById(userid));
    }

    /**
     * 获取customerinfo表数据
     */
    @RequestMapping(value = "/getAllCustomer", method = {RequestMethod.POST})
    public ApiResult getAllCustomer() throws Exception {
        return ApiResult.success(userService.getAllCustomerInfo());
    }

    //获取当前用户登陸信息（IP）
    @RequestMapping(value = "/getSigninlog", method = {RequestMethod.GET})
    public ApiResult getSigninlog(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        String userId = tokenModel.getUserId();//RequestUtils.CurrentUserId(request);
        return ApiResult.success(userService.getSigninlog(userId));
    }
    //add-lyt-21/2/3-PSDCD_PFANS_20201124_XQ_033
    @RequestMapping(value = "/checkpassword", method = {RequestMethod.GET})
    public ApiResult checkpassword(UserAccountVo userAccountVo, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(userAccountVo.getUserid()));
        query.addCriteria(Criteria.where("password").is(userAccountVo.getPassword()));
        List<UserAccount> userAccountlist = mongoTemplate.find(query, UserAccount.class);
        if (userAccountlist.size() > 0) {
            return ApiResult.success("1");
        } else {
            return ApiResult.success("0");
        }
    }

    //  region  add  ml  211224  密码重置  from
    @RequestMapping(value = "/resetPassword", method = {RequestMethod.POST})
    public ApiResult resetPassword(@RequestBody UserAccount userAccount, HttpServletRequest request) throws Exception {
        try {
            TokenModel tokenModel = tokenService.getToken(request);
            userService.resetPassword(userAccount, tokenModel);
            if (!tokenModel.getUserId().equals("5e78b2034e3b194874180e37")) {
                Expatriatesinfor exinfo = new Expatriatesinfor();
                exinfo.setAccount(userAccount.get_id());
                List<Expatriatesinfor> expatriatesinforList = expatriatesinforService.getexpatriatesinfor(exinfo);
                if(expatriatesinforList.size() > 0){
                    ToDoNotice toDoNotice = new ToDoNotice();
                    if(com.nt.utils.StringUtils.isBase64Encode(expatriatesinforList.get(0).getExpname())){
                        toDoNotice.setTitle(cn.hutool.core.codec.Base64.decodeStr(expatriatesinforList.get(0).getExpname()) + "的密码已经重置成功，请及时通知！");
                    }else{
                        toDoNotice.setTitle(expatriatesinforList.get(0).getExpname() + "的密码已经重置成功，请及时通知！");
                    }
                    toDoNotice.setType("2");//消息
                    toDoNotice.setInitiator(tokenModel.getUserId());
                    toDoNotice.setUrl("/PFANS6004View");
                    toDoNotice.preInsert(tokenModel);
                    toDoNotice.setOwner(tokenModel.getUserId());
                    toDoNoticeService.save(toDoNotice);
                }
            }
            return ApiResult.success();
        } catch (LogicalException e) {
            return ApiResult.fail(e.getMessage());
        }
    }
    //  endregion  add  ml  211224  密码重置  to

    //定时任务清除log
    @RequestMapping(value = "/remMgoLog", method = {RequestMethod.POST})
    public ApiResult remMgoLog() throws Exception {
        logService.remMgoLog();
        return ApiResult.success();
    }
}


