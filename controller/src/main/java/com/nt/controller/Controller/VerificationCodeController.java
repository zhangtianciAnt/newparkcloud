/**
 * 短信controller
 */
package com.nt.controller.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.nt.service_VerificationCode.VerificationCodeService;
import com.nt.utils.ApiResult;
import com.nt.utils.MsgConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.aliyuncs.profile.DefaultProfile;

import java.security.SecureRandom;
import java.util.Random;

@RestController
@RequestMapping("/verificationCode")
public class VerificationCodeController {

    @Autowired
    private VerificationCodeService verificationCodeService;

    /**
     * 对指定手机号发送验证码
     *
     * @param phone 手机号
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/sendVerificationCode", method = {RequestMethod.POST})
    public ApiResult sendVerificationCode(String phone) throws Exception {
        JSONObject obj = new JSONObject();
        try {
            if (phone == null || "".equals(phone)) {
                obj.put("message", MsgConstants.Phone_ERR_01);
                return ApiResult.fail(obj);
            }
            String code = getNonce_str();
            if (SendSms(phone, code)) {
                // 向数据库中插入数据
                obj = verificationCodeService.insert(phone, code);
            } else {
                // 验证码发送失败
                obj.put("message", MsgConstants.CODE_ERR);
                return ApiResult.fail(obj);
            }
            return ApiResult.success(obj);
        } catch (Exception ex) {
            obj.put("message", ex);
            return ApiResult.fail(obj);
        }
    }

    /**
     * 校验验证码
     *
     * @param phone 手机号
     * @param code  验证码
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/ckVerificationCode", method = {RequestMethod.POST})
    public ApiResult ckVerificationCode(String phone, String code) throws Exception {
        JSONObject obj = new JSONObject();
        try {
            return verificationCodeService.ck(phone, code);
        } catch (Exception ex) {
            obj.put("message", ex);
            return ApiResult.fail(obj);
        }
    }

    private static final String SYMBOLS = "0123456789"; // 数字
    private static final Random RANDOM = new SecureRandom();

    /**
     * 获取长度为 4 的随机数字
     *
     * @return 随机数字
     */
    private static String getNonce_str() {

        char[] nonceChars = new char[4];

        for (int index = 0; index < nonceChars.length; ++index) {
            nonceChars[index] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
        }

        return new String(nonceChars);
    }

    /**
     * 对指定手机号发送短信
     *
     * @param phone  手机号
     * @param random 验证码
     */
    private Boolean SendSms(String phone, String random) {
        DefaultProfile profile = DefaultProfile.getProfile("default", "LTAIXdnmwr6T9CUb", "GxRfXHjqfuk589K8bIiW45ApphpynG");
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "default");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", "新园素");
        request.putQueryParameter("TemplateCode", "SMS_105955142");
        request.putQueryParameter("TemplateParam", "{\"code\":\"" + random + "\"}");

        try {
            CommonResponse response = client.getCommonResponse(request);
            JSONObject jsonObject = JSON.parseObject(response.getData());
            if ("OK".equals(jsonObject.get("Message")) && "OK".equals(jsonObject.get("Code"))) {
                return true;
            }
            return false;

        } catch (ServerException e) {
            e.printStackTrace();
            return false;
        } catch (ClientException e) {
            e.printStackTrace();
            return false;
        }
    }
}
