package com.nt.controller.Controller.BASF.BASFLANController;

import cn.hutool.core.util.StrUtil;
import com.nt.dao_BASF.*;
import com.nt.dao_Org.Vo.UserVo;
import com.nt.service_BASF.EmailConfigServices;
import com.nt.service_BASF.SendEmailServices;
import com.nt.service_BASF.StartprogramServices;
import com.nt.service_BASF.TrainjoinlistServices;
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
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

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
    private TrainjoinlistServices trainjoinlistServices;

    @Autowired
    private EmailConfigServices emailConfigServices;

    @Autowired
    private SendEmailServices sendEmailServices;

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
        if (!("BC039004").equals(startprogram.getProgramtype()) && !("BC039003").equals(startprogram.getProgramtype())) {
            Startprogram tmp = startprogramServices.one(startprogram.getStartprogramid());
            startprogram.setProgramtype(tmp.getProgramtype());
            startprogram.setIsonline(tmp.getIsonline());
            // 线下可开班状态
            if (startprogram.getIsonline().equals("BC032002")) {
                switch (startprogram.getProgramtype()) {
                    case "BC039001":
                        startprogram.setProgramtype("BC039005");
                        break;
                    case "BC039005":
                        Startprogram tmp02 = startprogramServices.one(startprogram.getStartprogramid());
                        tmp02.setProgramname(startprogram.getProgramname() + "02");
                        tmp02.setStartprogramid(UUID.randomUUID().toString());
                        tmp02.setProgramtype("BC039002");
                        tmp02.setInformpeople(startprogram.getInformpeople());
                        tmp02.setStarttheorydate(startprogram.getStarttheorydate());
                        tmp02.setTheorysite(startprogram.getTheorysite());
                        tmp02.setStartoperationdate(startprogram.getStartoperationdate());
                        tmp02.setOperationsite(startprogram.getOperationsite());
                        tmp02.setExpirationdate(startprogram.getExpirationdate());
                        tmp02.setMaterialsexpirationdate(startprogram.getMaterialsexpirationdate());
                        tmp02.setInformperson(startprogram.getInformperson());
                        tmp02.setIsfirst(startprogram.getIsfirst());
                        startprogramServices.insert(tmp02, tokenModel);
                        if ("BC039005".equals(tmp.getProgramtype())) {
                            tmp.setProgramtype("BC039002");
                        }
                        tmp.setProgramname(tmp.getProgramname() + "01");
                        startprogramServices.update(tmp, tokenModel);
                        return ApiResult.success();
                }
            }
        }
        startprogramServices.update(startprogram, tokenModel);
        return ApiResult.success();
    }

    //更新培训项目补考状态
    @RequestMapping(value = "/updateMakeup", method = {RequestMethod.GET})
    public ApiResult updateMakeup(String startprogramid, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        Startprogram startprogram = new Startprogram();
        startprogram.setStartprogramid(startprogramid);
        startprogram.setMakeup("1");    // 已补考
        startprogramServices.update(startprogram, tokenModel);
        return ApiResult.success();
    }

    //发送邮件通知
    @RequestMapping(value = "/resultsEmail", method = {RequestMethod.POST})
    public ApiResult updateResultsEmail(@RequestBody Startprogram startprogram, HttpServletRequest request) throws Exception {
        if (startprogram == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        List<Startprogram> startprogramlist = startprogramServices.select(startprogram);
        if (startprogramlist.size() > 0) {
            //通知人员list
            String ePersonlist = startprogramlist.get(0).getInformperson();
            String[] notifyPerson = ePersonlist.split(",");
            //培训名称
            String programname = startprogramlist.get(0).getProgramname();
            //线上/线下
            String isonline = startprogramlist.get(0).getIsonline().equals("BC032001") ? "线上" : "线下";
            String startprogramid = startprogramlist.get(0).getStartprogramid();
            //发信人
            List<EmailConfig> emailconfig = emailConfigServices.get();

            for (String person : notifyPerson) {
                String useremail = "";
                UserVo uservo = userService.getAccountCustomerById(person);
                String depid = uservo.getCustomerInfo().getUserinfo().getDepartmentid().get(0);
                if (StringUtils.isNotEmpty(uservo.getCustomerInfo().getUserinfo().getEmail())) {
                    useremail = uservo.getCustomerInfo().getUserinfo().getEmail();
                } else {
                    continue;
                }
                Trainjoinlist trainjoilist = new Trainjoinlist();
//                trainjoilist.setDepartmentid(depid);
                trainjoilist.setStartprogramid(startprogramid);
                List<Trainjoinlist> Departmentidlists = trainjoinlistServices.addUserName(trainjoinlistMapper.select(trainjoilist));

                String EMAILCONTENT =
                        "您好：<br>【" + programname + "/" + isonline + "】考核结果已发布，您装置/部门的培训人员的考核结果如下：<br>" +
                                "<table width=\"100%\" border=\"1\" cellspacing=\"0\" cellpadding=\"2\">"
                                + "<tr>" +
                                "<td>姓名</td>" +
                                "<td>员工号</td>" +
                                "<td>通过状态</td>" +
                                "<td>理论成绩</td>" +
                                "<td>实操成绩</td>" +
                                "<td>证书编号</td>" +
                                "<td>发证日期</td>" +
                                "</tr>";
                StringBuilder neirong = new StringBuilder();
                for (Trainjoinlist departmentidlist : Departmentidlists) {
                    String customername = departmentidlist.getCustomername();
                    String documentnumber = departmentidlist.getJobnumber();
                    String performance = departmentidlist.getPerformance(); // 理论成绩
                    String actualperformance = departmentidlist.getActualperformance(); // 实操成绩
                    String certificateno = departmentidlist.getCertificateno(); // 证书编号
                    String issuedate = departmentidlist.getIssuedate(); // 发证日期
                    String throughtype = departmentidlist.getThroughtype();

                    if (customername == null) {
                        customername = " ";
                    }
                    if (documentnumber == null) {
                        documentnumber = " ";
                    }
                    if (performance == null) {
                        performance = " ";
                    }
                    if (throughtype == null) {
                        throughtype = " ";
                    }
                    if (actualperformance == null) {
                        actualperformance = " ";
                    }
                    if (certificateno == null) {
                        certificateno = " ";
                    }
                    if (issuedate == null) {
                        issuedate = " ";
                    }

                    neirong.append("<tr>")
                            .append("<td>").append(customername).append("</td>")
                            .append("<td>").append(documentnumber).append("</td>")
                            .append("<td>").append(throughtype).append("</td>")
                            .append("<td>").append(performance).append("</td>")
                            .append("<td>").append(actualperformance).append("</td>")
                            .append("<td>").append(certificateno).append("</td>")
                            .append("<td>").append(issuedate).append("</td>")
                            .append("</tr>");
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
                sendEmailServices.sendmail(tokenModel, sendemail);
            }
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
    public ApiResult selectbyuserid(String userid, String selecttype, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(startprogramServices.selectbyuserid(userid, selecttype));
    }

    //未来三个月培训信息
    @RequestMapping(value = "/getFutureProgram", method = {RequestMethod.POST})
    public ApiResult getFutureProgram(HttpServletRequest request) throws Exception {
        return ApiResult.success(startprogramServices.getFutureProgram());
    }

    @RequestMapping(value = "/exportSignin", method = {RequestMethod.GET})
    public void excelout(String noStartRowid, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //判断传来的参数是否存在
        if (StrUtil.isEmpty(noStartRowid)) {
            //不存在，返回错误提示
            return;
        }
        startprogramServices.exportSignin(noStartRowid,response);
    }

    //即将到期人员的一览
    @RequestMapping(value = "/getUsersEnd", method = {RequestMethod.POST})
    public ApiResult getUsersEnd(HttpServletRequest request) throws Exception {
        return ApiResult.success(startprogramServices.getUsersEnd());
    }
}
