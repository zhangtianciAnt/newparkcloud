package com.nt.service_BASF.Impl;

import com.nt.dao_BASF.SendEmail;
import com.nt.service_BASF.SendEmailServices;
import com.nt.utils.dao.TokenModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

@Service
@Transactional(rollbackFor = Exception.class)
public class SendEmailServicesImpl implements SendEmailServices {
    @Override
    public Boolean sendmail(TokenModel tokenModel, SendEmail sendemail) throws Exception {

        SendEmail email = new SendEmail();
        email.setUserName(sendemail.getUserName());
        email.setPassword(sendemail.getPassword());
        email.setHost(sendemail.getHost());
        email.setPort(sendemail.getPort());
        email.setFromAddress(sendemail.getFromAddress());
        email.setToAddress(sendemail.getToAddress());
        email.setSubject(sendemail.getSubject());
        email.setContext(sendemail.getContext());
        email.setContextType(sendemail.getContextType());
        boolean flag = EmailSendTest(email);
        return flag;
    }

    public static boolean EmailSendTest(SendEmail emailEntity){
        try {
            //配置文件
            Properties properties = new Properties();
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.host", emailEntity.getHost());
            properties.put("mail.smtp.port", 25);
            properties.put("mail.smtp.starrttls.enable", "true");
            //创建会话
//            VerifyEmail verifyEmail = new VerifyEmail(emailEntity.getUserName(), emailEntity.getPassword());
            Session mailSession = Session.getInstance(properties, null);
            mailSession.setDebug(true);
            //创建信息对象
            Message message = new MimeMessage(mailSession);
            InternetAddress from = new InternetAddress(emailEntity.getFromAddress());
            InternetAddress[] to = new InternetAddress().parse(emailEntity.getToAddress());
            //设置邮件信息的来源
            message.setFrom(from);
            //设置邮件的接收者
            message.setRecipients(MimeMessage.RecipientType.TO, to);
            message.setSubject(emailEntity.getSubject());
            //设置邮件发送日期
            message.setSentDate(new Date());
            //设置邮件内容
            message.setContent(emailEntity.getContext() , emailEntity.getContextType());
            message.saveChanges();
            //发送邮件
            Transport transport = mailSession.getTransport("smtp");
            transport.connect(emailEntity.getHost(), emailEntity.getUserName(), emailEntity.getPassword());
            transport.sendMessage(message, message.getAllRecipients());
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}
