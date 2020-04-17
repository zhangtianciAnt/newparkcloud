package com.nt.controller.Controller.BASF.BASFLANController;

import com.nt.dao_BASF.EmailConfig;
import com.nt.dao_BASF.SendEmail;
import com.nt.service_BASF.TrainjoinlistServices;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.RequestUtils;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import com.nt.service_BASF.EmailConfigServices;
import com.nt.service_BASF.SendEmailServices;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.controller.Controller.BASF.BASFLANController
 * @ClassName: BASF21210Controller
 * @Author: 王哲
 * @Description: 培训档案Controller
 * @Date: 2020/3/18 17:41
 * @Version: 1.0
 */
@RestController
@RequestMapping("/BASF21210")
public class BASF21210Controller {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private TrainjoinlistServices trainjoinlistServices;

    @Autowired
    private EmailConfigServices emailConfigServices;
    @Autowired
    private SendEmailServices sendEmailServices;


    //获取参加过培训的人员信息
    @PostMapping("/list")
    public ApiResult upTrain(HttpServletRequest request) throws Exception {
        return ApiResult.success(trainjoinlistServices.joinPersonnel(trainjoinlistServices.joinPersonnelid()));
    }

    //发送邮件
    @PostMapping("/sendemail21210")
    public ApiResult sendemail21210(@RequestBody SendEmail sendemail,HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        List<EmailConfig> emailconfig =  emailConfigServices.get();

        sendemail.setUserName(emailconfig.get(0).getUsername());
        sendemail.setPassword(emailconfig.get(0).getPassword());
        sendemail.setHost(emailconfig.get(0).getHost());
        sendemail.setPort(emailconfig.get(0).getPort());
        sendemail.setFromAddress(emailconfig.get(0).getFromaddress());
        sendemail.setContextType(emailconfig.get(0).getContexttype());

        String EMAILCONTENT = "XXXX您好：<br>" +
                "<table width=\"100%\" border=\"1\" cellspacing=\"0\" cellpadding=\"2\">"
                + "<tr>" +
                //"<td width=\"10%\">ID</td>" +
                "<td>ID</td>" +
                "<td>负责人</td>" +
                "<td>数据量</td>" +
                "<td>说明</td>" +
                "<td>文件</td>" +
                "</tr>" +
                "</table>";

        sendemail.setContext(EMAILCONTENT);

        return ApiResult.success(sendEmailServices.sendmail(tokenModel,sendemail));
    }

    //培训档案导出
    @PostMapping("/exportRecordData")
    public ApiResult exportData(HttpServletRequest request) throws Exception{
        return ApiResult.success(trainjoinlistServices.exportRecordData());
    }
}
