package com.nt.service_BASF;

import com.nt.dao_BASF.Webchatuserinfo;
import com.nt.utils.ApiResult;


public interface WebchatuserinfoServices {

    //获取报警单信息
    ApiResult checkPwd(Webchatuserinfo webchatuserinfo) throws Exception;
}
