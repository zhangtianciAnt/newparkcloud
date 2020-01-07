package com.nt.service_BASF;

import com.nt.dao_BASF.Startprogram;
import com.nt.utils.dao.TokenModel;

import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF
 * @ClassName: StartprogramServices
 * @Author: 王哲
 * @Description: 申请考核接口类
 * @Date: 2020/1/7 11:06
 * @Version: 1.0
 */
public interface StartprogramServices {

    //获取未开班培训列表
    List<Startprogram> nostart() throws Exception;

    //添加培训列表
    void insert(Startprogram startprogram, TokenModel tokenModel) throws Exception;

}
