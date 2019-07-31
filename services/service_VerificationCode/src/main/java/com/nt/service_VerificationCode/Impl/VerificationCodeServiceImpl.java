package com.nt.service_VerificationCode.Impl;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.result.DeleteResult;
import com.nt.dao_VerificationCode.VerificationCode;
import com.nt.service_VerificationCode.VerificationCodeService;
import com.nt.utils.ApiResult;
import com.nt.utils.MsgConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class VerificationCodeServiceImpl implements VerificationCodeService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public JSONObject insert(String phone, String code) {
        JSONObject jsonObject = new JSONObject();
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setPhone(phone);
        verificationCode.setCode(code);
        verificationCode.setCreateon(new Date());
        mongoTemplate.insert(verificationCode);
        if (verificationCode.get_id() == null || "".equals(verificationCode.get_id())) {
            jsonObject.put("message", MsgConstants.FAIL);
        } else {
            jsonObject.put("message", MsgConstants.SUCCESS);
        }
        return jsonObject;
    }

    @Override
    public ApiResult ck(String phone, String code) {
        JSONObject jsonObject = new JSONObject();
        Query query = new Query();
        query.addCriteria(Criteria.where("phone").is(phone));
        query.addCriteria(Criteria.where("code").is(code));
        VerificationCode verificationCode = mongoTemplate.findOne(query, VerificationCode.class);
        if (verificationCode != null) {
            query = new Query();
            query.addCriteria(Criteria.where("_id").is(verificationCode.get_id()));
            DeleteResult deleteResult = mongoTemplate.remove(query, VerificationCode.class);
            if (deleteResult.getDeletedCount() > 0) {
                // 验证码验证成功
                return ApiResult.success();
            } else {
                // 验证码验证成功，删除失败
                return ApiResult.success(MsgConstants.CODE_CK_ERR_02);
            }
        } else {
            // 验证码验证失败
            return ApiResult.success(MsgConstants.CODE_CK_ERR_01);
        }
    }
}
