package com.nt.controller.Controller.BASF.BASFLANController;

import com.nt.dao_BASF.EmailConfig;
import com.nt.dao_BASF.SendEmail;
import com.nt.dao_BASF.Startprogram;
import com.nt.dao_BASF.Trainjoinlist;
import com.nt.dao_Org.Vo.UserVo;
import com.nt.service_BASF.EmailConfigServices;
import com.nt.service_BASF.SendEmailServices;
import com.nt.service_BASF.StartprogramServices;
import com.nt.service_BASF.mapper.TrainjoinlistMapper;
import com.nt.service_Org.UserService;
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
 * @ProjectName: BASF应急平台
 * @Package: com.nt.controller.Controller.BASF.BASFLANController
 * @ClassName: BASF21209Controller
 * @Author: 王哲
 * @Description: 申请考核
 * @Date: 2020/1/7 14:04
 * @Version: 1.0
 */
@RestController
@RequestMapping("/BASF21209")
public class BASF21209Controller {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private StartprogramServices startprogramServices;

    @Autowired
    private UserService userService;

    @Autowired
    private TrainjoinlistMapper trainjoinlistMapper;

    @Autowired
    private EmailConfigServices emailConfigServices;

    @Autowired
    private SendEmailServices sendEmailServices;

    private String as;

    //创建培训列表
    @RequestMapping(value = "/create", method = {RequestMethod.POST})
    public ApiResult create(@RequestBody Startprogram startprogram, HttpServletRequest request) throws Exception {
        if (startprogram == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        startprogramServices.insert(startprogram, tokenModel);
        return ApiResult.success();
    }

    //更新培训项目
        @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody Startprogram startprogram, HttpServletRequest request) throws Exception {
        if (startprogram == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        startprogramServices.update(startprogram, tokenModel);
        return ApiResult.success();
    }

    //更新培训列表
    @RequestMapping(value = "/resultsEmail", method = {RequestMethod.POST})
    public ApiResult updateResultsEmail(@RequestBody Startprogram startprogram, HttpServletRequest request) throws Exception {
        if (startprogram == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        List<Startprogram> startprogramlist  = startprogramServices.select(startprogram);
        startprogramServices.update(startprogram, tokenModel);

        //负责人list
        String programhardlist = startprogramlist.get(0).getProgramhard();
        String[] programhard = programhardlist.split(",");
        //通知人员list
        String ePersonlist = startprogramlist.get(0).getInformperson();
        String[] notifyPerson = ePersonlist.split(",");
        //培训名称
        String programname = startprogramlist.get(0).getProgramname();
        //线上/线下
        String isonline = startprogramlist.get(0).getIsonline()=="BC032001"?"线上":"线下";
        String startprogramid = startprogramlist.get(0).getStartprogramid();

        Trainjoinlist trainjoinlist = new Trainjoinlist();
        trainjoinlist.setStartprogramid(startprogram.getStartprogramid());
        List<Trainjoinlist> trainjoinlists = trainjoinlistMapper.select(trainjoinlist);
       //发信人
        List<EmailConfig> emailconfig =  emailConfigServices.get();

        for (int l = 0 ; l <notifyPerson.length ; l++){
            UserVo uservo = userService.getAccountCustomerById(notifyPerson[l]);
            String depid =  uservo.getCustomerInfo().getUserinfo().getDepartmentid().get(0);
            String useremail = uservo.getCustomerInfo().getUserinfo().getEmail();
            Trainjoinlist trainjoinlist1 = new Trainjoinlist();
            trainjoinlist1.setDepartmentid(depid);
            trainjoinlist1.setStartprogramid(startprogramid);
            List<Trainjoinlist> Departmentidlists = trainjoinlistMapper.select(trainjoinlist1);

            String EMAILCONTENT =
                    "您好：<br>【"+programname+"/"+isonline+"】考核结果已发布，您装置/部门的培训人员的考核结果如下：<br>" +
                            "<table width=\"100%\" border=\"1\" cellspacing=\"0\" cellpadding=\"2\">"
                            + "<tr>" +
                            //"<td width=\"10%\">ID</td>" +
                            "<td>姓名</td>" +
                            "<td>员工号</td>" +
                            "<td>成绩</td>" +
                            "<td>通过状态</td>" +
                            "</tr>";
            String neirong = "";
            for(int i = 0;i<Departmentidlists.size();i++)
            {
                String customername = Departmentidlists.get(i).getCustomername();
                String documentnumber = Departmentidlists.get(i).getJobnumber();
                String performance = Departmentidlists.get(i).getPerformance();
                String throughtype = Departmentidlists.get(i).getThroughtype();

                if (customername == null){
                    customername = " ";
                }
                if (documentnumber == null){
                    documentnumber = " ";
                }
                if (performance == null){
                    performance = " ";
                }
                if (throughtype == null){
                    throughtype = " ";
                }

             neirong =neirong +
                    "<tr>" +
                            "<td>"+customername+"</td>" +
                            "<td>"+documentnumber+"</td>" +
                            "<td>"+performance+"</td>" +
                            "<td>"+throughtype+"</td>" +
                            "</tr>";

//            EMAILCONTENT = EMAILCONTENT + neirong;
//            EMAILCONTENT = EMAILCONTENT + "</table>";
//            as = EMAILCONTENT;
            }
            EMAILCONTENT = EMAILCONTENT + neirong + "</table>";

            SendEmail sendemail = new SendEmail();
            sendemail.setUserName(emailconfig.get(0).getUsername());
            sendemail.setPassword(emailconfig.get(0).getPassword());
            sendemail.setHost(emailconfig.get(0).getHost());
            sendemail.setPort(emailconfig.get(0).getPort());
            sendemail.setFromAddress(emailconfig.get(0).getFromaddress());
            sendemail.setContextType(emailconfig.get(0).getContexttype());
            sendemail.setToAddress(useremail);
            sendemail.setSubject("【培训结果发布】");
            sendemail.setContext(EMAILCONTENT);
            sendEmailServices.sendmail(tokenModel,sendemail);
        }
        return ApiResult.success();
    }


    //更新培训清单
    @RequestMapping(value = "/updateprogramlist", method = {RequestMethod.GET})
    public ApiResult updateprogramlist(String startprogramid, HttpServletRequest request) throws Exception {
        if (!StringUtils.isNotBlank(startprogramid)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        startprogramServices.updateprogramlist(startprogramid, tokenModel);
        return ApiResult.success();
    }

    //查询培训列表
    @RequestMapping(value = "/select", method = {RequestMethod.POST})
    public ApiResult select(@RequestBody Startprogram startprogram, HttpServletRequest request) throws Exception {
        if (startprogram == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(startprogramServices.select(startprogram));
    }

    //查询培训列表增强
    @RequestMapping(value = "/selectEnhance", method = {RequestMethod.POST})
    public ApiResult selectEnhance(@RequestBody Startprogram startprogram, HttpServletRequest request) throws Exception {
        if (startprogram == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(startprogramServices.selectEnhance(startprogram));
    }

    //查询培训one
    @RequestMapping(value = "/selectOne", method = {RequestMethod.GET})
    public ApiResult selectOne(String startprogramid, HttpServletRequest request) throws Exception {
        if (!StringUtils.isNotBlank(startprogramid)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(startprogramServices.one(startprogramid));
    }

    //删除培训
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public ApiResult delete(@RequestBody Startprogram startprogram, HttpServletRequest request) throws Exception {
        if (startprogram == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        startprogram.setStatus(AuthConstants.DEL_FLAG_DELETE);
        startprogramServices.delete(startprogram, tokenModel);
        return ApiResult.success();
    }

    //by人员id查询培训项目
    @RequestMapping(value = "/selectbyuserid", method = {RequestMethod.GET})
    public ApiResult selectbyuserid(String userid,String selecttype, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(startprogramServices.selectbyuserid(userid,selecttype));
    }

    //未来三个月培训信息
    @RequestMapping(value = "/getFutureProgram", method = {RequestMethod.POST})
    public ApiResult getFutureProgram(HttpServletRequest request) throws Exception {
        return ApiResult.success(startprogramServices.getFutureProgram());
    }
}
