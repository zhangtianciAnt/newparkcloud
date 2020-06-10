package com.nt.controller.Controller;

import com.nt.dao_AOCHUAN.AOCHUAN6000.ReimbursementDetail;
import com.nt.dao_Org.CustomerInfo;
import com.nt.service_Org.EWechatService;
import com.nt.utils.ApiResult;
import com.nt.utils.EWxUserApi;
import com.nt.utils.LogicalException;
import com.nt.utils.dao.EWeixinOauth2Token;
import com.nt.utils.dao.EWxBaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.controller.Controller
 * @ClassName: EnterpriseWeChatController
 * @Description: 企业微信Controller
 * @Author: YANGSHUBO
 * @CreateDate: 2020/06/08
 * @UpdateUser: YANGSHUBO
 * @UpdateDate: 2020/06/08
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@RestController
@RequestMapping("/eweChat")
public class EnterpriseWeChatController {

    @Autowired
    private EWechatService ewechatService;

    //自动获取企业微信打卡记录
    @RequestMapping(value = "/getautocheckindata", method = {RequestMethod.GET})
    public ApiResult getAutoCheckInData(String code) throws Exception {

        try {
            String corpid = "ww5c49fd2e4bbc6981";
            String corpSecret = "kQQjJMqhxYk9OmzPRXtwl0JsGRDS2pNx9erhv2Vw8";
            EWeixinOauth2Token eweixinOauth = EWxUserApi.getWeChatOauth2Token(corpid, corpSecret);
            if (eweixinOauth != null && eweixinOauth.getErrcode() == 0) {
                String token = eweixinOauth.getAccess_token();
                String access_token = ewechatService.ewxLogin(token);
                List<CustomerInfo> userIdList = ewechatService.useridList();
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String strDateFormat = dateFormat.format(date);
                String startStrDateFormat = strDateFormat + " " + "08:01:01";
                String endStrDateFormat = strDateFormat + " " + "23:59:59";
                Date startDatDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startStrDateFormat);
                Date endDatDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endStrDateFormat);
                long startTime = startDatDateFormat.getTime() / 1000;
                long endTime = endDatDateFormat.getTime() / 1000;
                String[] strList = new String[userIdList.size()];
                for (int i = 0; i < userIdList.size(); i++) {
                    strList[i] = userIdList.get(i).getUserinfo().getJobnumber();
                }
                return ApiResult.success(EWxUserApi.inData(access_token, 1, startTime, endTime, strList));
//                return ApiResult.success(ewechatService.ewxLogin(access_token));
            } else {
                return ApiResult.fail("获取企业微信access_token失败");
            }
        } catch (LogicalException e) {
            return ApiResult.fail(e.getMessage());
        }
    }

//    //获取企业微信打卡记录
//    @RequestMapping(value = "/getcheckindata", method = {RequestMethod.POST})
//    public ApiResult getcheckindata(@RequestBody EWxBaseResponse ewxBaseResponse, HttpServletRequest request) throws Exception {
//        String[] a = new String[]{"ZhangYing", "YangShuBo"};
//        Date date = new Date();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");
//        System.out.println(dateFormat.format(date));
//        return ApiResult.success(EWxUserApi.inData("s", ewxBaseResponse.getOpencheckindatatype(), ewxBaseResponse.getStarttime(), ewxBaseResponse.getEndtime(), a));
//    }


//    //获取企业微信access_token
//    @RequestMapping(value = "/gettoken", method = {RequestMethod.GET})
//    public ApiResult getWeChatUser(String code) throws Exception {
//
//        try {
//            String corpid = "ww5c49fd2e4bbc6981";
//            String corpSecret = "kQQjJMqhxYk9OmzPRXtwl0JsGRDS2pNx9erhv2Vw8";
//            EWeixinOauth2Token eweixinOauth = EWxUserApi.getWeChatOauth2Token(corpid, corpSecret);
//            if (eweixinOauth != null && eweixinOauth.getErrcode() == 0) {
//                String token = eweixinOauth.getAccess_token();
//                String access_token = ewechatService.ewxLogin(token);
//                Date date = new Date();
//                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                String strDateFormat = dateFormat.format(date);
//                String startStrDateFormat = strDateFormat + " " + "08:01:01";
//                String endStrDateFormat = strDateFormat + " " + "18:01:01";
//                Date startDatDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startStrDateFormat);
//                Date endDatDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endStrDateFormat);
//                long startTime = startDatDateFormat.getTime() / 1000;
//                long endTime = endDatDateFormat.getTime() / 1000;
//                System.out.println(startTime);
//                System.out.println(endTime);
//                String[] userIdList;
//                return ApiResult.success(EWxUserApi.inData(access_token, "1", startTime, endTime, userIdList));
////                return ApiResult.success(ewechatService.ewxLogin(access_token));
//            } else {
//                return ApiResult.fail("获取企业微信access_token失败");
//            }
//        } catch (LogicalException e) {
//            return ApiResult.fail(e.getMessage());
//        }
//    }
}
