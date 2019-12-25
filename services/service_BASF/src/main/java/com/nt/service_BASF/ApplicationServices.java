package com.nt.service_BASF;
/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF
 * @ClassName: ApplicationServices
 * @Author: LXY
 * @Description: 消防设备申请模块接口
 * @Date: 2019/11/13
 * @Version: 1.0
 */
import com.nt.dao_BASF.Application;
import com.nt.utils.dao.TokenModel;
import com.nt.dao_BASF.VO.ApplicationVo;

import java.util.List;


public interface ApplicationServices {

    List<Application> get(Application application) throws Exception;

    List<ApplicationVo> getList() throws Exception;

    void insert(TokenModel tokenModel, Application application) throws Exception;

    void update(TokenModel tokenModel, Application application) throws Exception;

    void del(TokenModel tokenModel, Application application) throws Exception;
    //获取详情
    Application one(String applicationid) throws Exception;

}
