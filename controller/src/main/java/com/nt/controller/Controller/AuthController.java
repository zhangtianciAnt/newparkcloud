package com.nt.controller.Controller;

import cn.hutool.core.util.StrUtil;
import com.nt.dao_Auth.AppPermission;
import com.nt.dao_Auth.Role;
import com.nt.dao_Pfans.PFANS1000.DepartmentAccount;
import com.nt.dao_Pfans.PFANS2000.Punchcard;
import com.nt.service_Auth.AuthService;
import com.nt.service_Auth.RoleService;
import com.nt.service_pfans.PFANS1000.BusinessService;
import com.nt.service_pfans.PFANS1000.DepartmentAccountService;
import com.nt.service_pfans.PFANS2000.AnnualLeaveService;
import com.nt.service_pfans.PFANS5000.CompanyProjectsService;
import com.nt.service_pfans.PFANS6000.DeleginformationService;
import com.nt.service_pfans.PFANS6000.PjExternalInjectionService;
import com.nt.service_pfans.PFANS6000.PricesetService;
import com.nt.service_pfans.PFANS8000.MonthlyRateService;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.controller.Controller
 * @ClassName: AuthController
 * @Description: 权限相关Controller
 * @Author: WenChao
 * @CreateDate: 2018/12/14
 * @UpdateUser: WenChao
 * @UpdateDate: 2018/12/14
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AnnualLeaveService annualLeaveService;

    @Autowired
    private DeleginformationService deleginformationService;

    @Autowired
    private PricesetService pricesetService;

    @Autowired
    private BusinessService businessService;

    @Autowired
    private MonthlyRateService monthlyRateService;

    @Autowired
    private CompanyProjectsService companyProjectsService;

    @Autowired
    private PjExternalInjectionService pjExternalInjectionService;

    @Autowired
    private DepartmentAccountService departmentAccountService;

    /**
     * @方法名：getActionsAuth
     * @描述：获取按钮权限（新建，编辑，删除）
     * @创建日期：2018/12/14
     * @作者：WENCHAO
     * @参数：[role, request]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/getActionsAuth",method={RequestMethod.GET})
    public ApiResult getActionsAuth(String ownerid, HttpServletRequest request) throws Exception {
        if (StrUtil.isEmpty(ownerid)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        String url = request.getHeader(AuthConstants.CURRENTURL);
        TokenModel tokenModel = tokenService.getToken(request);
        String userid = tokenModel.getUserId();
        return ApiResult.success(authService.getActionsAuth(url,userid,ownerid));
    }

    /**
     * @方法名：getActionsAuth
     * @描述：获取按钮权限（新建）
     * @创建日期：2018/12/14
     * @作者：WENCHAO
     * @参数：[role, request]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/getNewActionAuth",method={RequestMethod.GET})
    public ApiResult getNewActionAuth(HttpServletRequest request) throws Exception {
        String url = request.getHeader(AuthConstants.CURRENTURL);
        TokenModel tokenModel = tokenService.getToken(request);
        String userid = tokenModel.getUserId();
        return ApiResult.success(authService.getNewActionAuth(url,userid));
    }

    @RequestMapping(value = "/getAttendance",method={RequestMethod.GET})
    public ApiResult getAttendance(Integer diffday,String staffId,String staffNo,HttpServletRequest request) throws Exception {
        annualLeaveService.insertattendance(diffday,staffId,staffNo);
        return ApiResult.success();
    }

    @RequestMapping(value = "/getAttendancebp",method={RequestMethod.GET})
    public ApiResult getAttendancebp(Integer diffday,String staffId,String staffNo,HttpServletRequest request) throws Exception {
        annualLeaveService.insertattendancebp(diffday,staffId,staffNo);
        return ApiResult.success();
    }

    @RequestMapping(value = "/insertpunchcard",method={RequestMethod.GET})
    public ApiResult insertpunchcard(Integer diffday,HttpServletRequest request) throws Exception {
        annualLeaveService.insertpunchcard(diffday);
        return ApiResult.success();
    }

    @RequestMapping(value = "/insertHistoricalCard",method={RequestMethod.GET})
    public ApiResult insertHistoricalCard(String strStartDate,String strendDate,String strFlg,String staffNo,HttpServletRequest request) throws Exception {
        annualLeaveService.insertHistoricalCard(strStartDate,strendDate,strFlg,staffNo);
        return ApiResult.success();
    }

    @RequestMapping(value = "/getattendanceByuser",method={RequestMethod.GET})
    public ApiResult getattendanceByuser(String userid,HttpServletRequest request) throws Exception {
        annualLeaveService.getattendanceByuser(userid);
        return ApiResult.success();
    }

    @RequestMapping(value = "/selectattendance",method={RequestMethod.GET})
    public ApiResult selectattendance(HttpServletRequest request) throws Exception {
        annualLeaveService.selectattendance();
        return ApiResult.success();
    }

    @RequestMapping(value = "/selectattendancebp",method={RequestMethod.GET})
    public ApiResult selectattendancebp(HttpServletRequest request) throws Exception {
        annualLeaveService.selectattendancebp();
        return ApiResult.success();
    }

    @RequestMapping(value = "/insertAnnualImport",method={RequestMethod.GET})
    public ApiResult insertAnnualImport(HttpServletRequest request) throws Exception {
        annualLeaveService.insertAnnualImport(null);
        return ApiResult.success();
    }

    //获取打卡记录（参数）
    @RequestMapping(value = "/getPunchcard",method={RequestMethod.POST})
    public ApiResult getPunchcard(@RequestBody List<Punchcard> Punchcard,HttpServletRequest request) throws Exception {
        annualLeaveService.getPunchcard(Punchcard);
        return ApiResult.success();
    }

    //获取打卡记录bp（参数）
    @RequestMapping(value = "/getPunchcardbp",method={RequestMethod.POST})
    public ApiResult getPunchcardbp(@RequestBody List<Punchcard> Punchcard,HttpServletRequest request) throws Exception {
        annualLeaveService.getPunchcardbp(Punchcard);
        return ApiResult.success();
    }

    //每月最后一天计算实际工资 add gbb 0728
    @RequestMapping(value = "/getrealwages",method={RequestMethod.GET})
    public ApiResult getrealwages(HttpServletRequest request) throws Exception {
        annualLeaveService.getrealwages();
        return ApiResult.success();
    }

    //每天的定时任务，工数截止日+1 > 当前日期,执行
    @RequestMapping(value = "/saveDelegaTask",method={RequestMethod.GET})
    public ApiResult saveDelegaTask(HttpServletRequest request) throws Exception {
        deleginformationService.saveDelegaTask();
        return ApiResult.success();
    }

    //add ccm 20201212  外驻单价自动保存
    @RequestMapping(value = "/savePriceset",method={RequestMethod.GET})
    public ApiResult savePriceset(HttpServletRequest request) throws Exception {
        pricesetService.savePriceset();
        return ApiResult.success();
    }
    //add ccm 20201212

    //境外出差根据时长创建打卡记录 定时任务
    @RequestMapping(value = "/saveDaka",method={RequestMethod.GET})
    public ApiResult saveDaka(HttpServletRequest request) throws Exception {
        businessService.saveDaka();
        return ApiResult.success();
    }

    //每月汇率 定时任务
    @RequestMapping(value = "/getExchangeRateY",method={RequestMethod.GET})
    public ApiResult getExchangeRateY(HttpServletRequest request) throws Exception {
        monthlyRateService.getExchangeRateY();
        return ApiResult.success();
    }

    //事业年度开始之前3月31日晚11点半更新年度年休表
    @RequestMapping(value = "/updateAnBefore",method={RequestMethod.GET})
    public ApiResult updateAnBefore(HttpServletRequest request) throws Exception {
        annualLeaveService.updateAnBefore();
        return ApiResult.success();
    }

    //正式时间每年4月1日零时执行--事业年度开始获取年休
    @RequestMapping(value = "/insert",method={RequestMethod.GET})
    public ApiResult insert(HttpServletRequest request) throws Exception {
        annualLeaveService.insert();
        return ApiResult.success();
    }

    //pl退场前7天给上级发送待办
    @RequestMapping(value = "/getPLLeader",method={RequestMethod.GET})
    public ApiResult getPLLeader(HttpServletRequest request) throws Exception {
        companyProjectsService.getPLLeader();
        return ApiResult.success();
    }

    //每月统计pj别外注费用
    @RequestMapping(value = "/saveTableinfo",method={RequestMethod.GET})
    public ApiResult saveTableinfo(HttpServletRequest request) throws Exception {
        pjExternalInjectionService.saveTableinfo();
        return ApiResult.success();
    }

    @RequestMapping(value = "/insertwww",method={RequestMethod.GET})
    public ApiResult insertwww(HttpServletRequest request) throws Exception {
        departmentAccountService.insert();
        return ApiResult.success();
    }
}
