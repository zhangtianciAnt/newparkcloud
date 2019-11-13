package com.nt.service_BASF;
import com.nt.dao_BASF.Commandrecord;
import com.nt.utils.dao.TokenModel;

import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF
 * @ClassName: BASF10103Services
 * @Author: SUN
 * @Description: BASF用户组模块接口
 * @Date: 2019/11/4 16:09
 * @Version: 1.0
 */
public interface CommandrecordServices {

    //保存
    void save(Commandrecord commandrecord, TokenModel tokenModel) throws Exception;

    //获取
    List<Commandrecord> get(Commandrecord commandrecord) throws Exception;
}
