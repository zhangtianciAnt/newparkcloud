package com.nt.service_VerificationCode;

import com.alibaba.fastjson.JSONObject;
import com.nt.utils.ApiResult;

public interface VerificationCodeService {
    JSONObject insert(String phone, String code);

    ApiResult ck(String phone, String code);
}
