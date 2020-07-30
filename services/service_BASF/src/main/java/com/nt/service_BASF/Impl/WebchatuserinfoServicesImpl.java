package com.nt.service_BASF.Impl;


import com.nt.dao_BASF.Webchatuserinfo;
import com.nt.service_BASF.WebchatuserinfoServices;
import com.nt.service_BASF.mapper.WebchatuserinfoMapper;
import com.nt.utils.ApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class WebchatuserinfoServicesImpl implements WebchatuserinfoServices {

    private static Logger log = LoggerFactory.getLogger(WebchatuserinfoServicesImpl.class);

    @Autowired
    private WebchatuserinfoMapper webchatuserinfoMapper;

    @Override
    public ApiResult checkPwd(Webchatuserinfo webchatuserinfo) throws Exception {
        // 用户输入的密码
        String inputPwd = webchatuserinfo.getPassword();
        Webchatuserinfo userInfo = webchatuserinfoMapper.selectPwd(webchatuserinfo.getLoginid());
        if (inputPwd.equals(userInfo.getPassword())) {
            return ApiResult.success();
        } else {
            return ApiResult.fail();
        }
    }
}
