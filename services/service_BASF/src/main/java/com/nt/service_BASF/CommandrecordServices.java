package com.nt.service_BASF;
import com.nt.dao_BASF.Commandrecord;
import com.nt.utils.dao.TokenModel;

import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF
 * @ClassName: BASF20201Services
 * @Author: SUN
 * @Description: BASF大屏接警指挥
 * @Date: 2019/11/14 16:09
 * @Version: 1.0
 */
public interface CommandrecordServices {

    //保存
    void save(Commandrecord commandrecord, TokenModel tokenModel) throws Exception;

    //获取
    Commandrecord get(String cid) throws Exception;
}
